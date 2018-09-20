package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.tasks.consumer.services.GitCommit;
import com.societegenerale.cidroid.tasks.consumer.services.Rebaser;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.monitoring.Event;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.societegenerale.cidroid.tasks.consumer.services.MonitoringEvents.PR_NOT_REBASED;
import static com.societegenerale.cidroid.tasks.consumer.services.MonitoringEvents.PR_REBASED;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class GitRebaser implements Rebaser {

    public static final String REPOSITORY_KEY_FOR_MONITORING = "repository";

    public static final String PR_NUMBER_KEY_FOR_MONITORING = "prNumber";

    public static final String REBASE_STATUS_FOR_MONITORING = "rebaseStatus";

    private File workingDirectory;

    private String gitLogin;

    private String gitPassword;

    private GitWrapper gitWrapper;


    public GitRebaser(@Value("${gitHub.login}") String gitLogin, @Value("${gitHub.password}") String gitPassword, GitWrapper gitWrapper) {

        this.workingDirectory = createWorkingDirIfRequired();
        this.gitLogin = gitLogin;
        this.gitPassword = gitPassword;
        this.gitWrapper = gitWrapper;
    }

    @Override
    public Pair<PullRequest, List<GitCommit>> rebase(PullRequest pr) {

        MDC.put(REPOSITORY_KEY_FOR_MONITORING, pr.getRepo().getFullName());
        MDC.put(PR_NUMBER_KEY_FOR_MONITORING, String.valueOf(pr.getNumber()));

        log.info("rebasing PR #{}...", pr.getNumber());

        StopWatch stopWatchForMonitoring = StopWatch.createStarted();

        try (Git git = initRepoOnDefaultBranch(pr)) {

            logGitStatus(git);

            if (!pr.getBaseBranchName().equals(pr.getRepo().getDefaultBranch())) {
                switchToBranch(git, pr.getBaseBranchName());
            }

            switchToBranch(git, pr.getBranchName());

            logGitStatus(git);

            Status branchStatus = git.status().call();

            if (!branchStatus.getConflicting().isEmpty()) {

                markPrWithWarnMessageAndLog(pr,
                        "there are some conflicted files in branch for PR #" + pr.getNumber() + " - not even trying to rebase");

                monitorPRoutcome(PR_NOT_REBASED, pr, stopWatchForMonitoring);
                return new ImmutablePair(pr, emptyList());
            }

            List<RevCommit> commitsToRebase = getCommitsOnWhichBranchIsLateComparedToBaseBranch(git, pr);

            List<RevCommit> commitsInRemoteBranchOnlyBeforeWeDoAnything = getCommitsInBranchOnly(git, pr, false);

            List<RevCommit> commitsInLocalBranchOnlyBeforeWeDoAnything = getCommitsInBranchOnly(git, pr, true);

            log.info("nb of commits:\n\t- in master, to rebase : {}\n\t- in remote branch : {}\n\t- in local branch : {}", commitsToRebase.size(),
                    commitsInRemoteBranchOnlyBeforeWeDoAnything.size(), commitsInLocalBranchOnlyBeforeWeDoAnything.size());

            boolean isRebaseSuccessful = false;

            boolean isPushSuccessful = false;

            if (commitsToRebase.size() > 0) {

                log.info("rebasing branch {} on {} for PR #{}, as {} commit(s) are not in branch..", pr.getBranchName(), pr.getBaseBranchName(),
                        pr.getNumber(), commitsToRebase.size());

                RebaseResult rebaseResult = gitWrapper.rebaseFrom(git, pr.getBaseBranchName());

                MDC.put(REBASE_STATUS_FOR_MONITORING, rebaseResult.getStatus().name());

                if (rebaseResult.getStatus().isSuccessful()) {

                    log.info("Successful rebase - detailed status : " + rebaseResult.getStatus().name());

                    List<RevCommit> commitsInLocalBranchAfterRebase = getCommitsInBranchOnly(git, pr, true);

                    if (commitsInRemoteBranchOnlyBeforeWeDoAnything.size() != commitsInLocalBranchAfterRebase.size()) {
                        log.warn("we had {} commits in branch before rebase, but {} after - are we sure we want to force push ?",
                                commitsInRemoteBranchOnlyBeforeWeDoAnything.size(), commitsInLocalBranchAfterRebase.size());
                    }

                    isRebaseSuccessful = true;

                    log.info("now trying to force push on PR branch..", pr.getNumber());

                    val forcePushResult = gitWrapper.forcePush(git, gitLogin, gitPassword);

                    List<RevCommit> commitsInBranchOnlyAfterPush = getCommitsInBranchOnly(git, pr, false);

                    if (commitsInRemoteBranchOnlyBeforeWeDoAnything.size() != commitsInBranchOnlyAfterPush.size()) {

                        markPrWithWarnMessageAndLog(pr, "strange outcome of the push for PR " + pr.getNumber() +
                                " as we don't have the same number of commits in branch before and after push - please check the logs for more details, and your branch to make sure all your commits are still there");

                        GitWrapper.logDiffCommits(commitsInRemoteBranchOnlyBeforeWeDoAnything,
                                "Before rebase/push, there was " + commitsInRemoteBranchOnlyBeforeWeDoAnything.size() +
                                        " commit(s) in the remote branch");

                        GitWrapper.logDiffCommits(commitsInLocalBranchOnlyBeforeWeDoAnything,
                                "Before rebase/push, there was " + commitsInLocalBranchOnlyBeforeWeDoAnything.size() +
                                        " commit(s) in the local branch");

                        GitWrapper.logDiffCommits(commitsInBranchOnlyAfterPush,
                                "After rebase/push, we have " + commitsInBranchOnlyAfterPush.size() + " commit(s) in the branch");

                        logGitStatus(git);

                        gitWrapper.logLastCommitsInBranch(git, 30, "origin/" + pr.getBaseBranchName());
                        gitWrapper.logLastCommitsInBranch(git, 30, pr.getBaseBranchName());
                        gitWrapper.logLastCommitsInBranch(git, 30, "origin/" + pr.getBranchName());
                        gitWrapper.logLastCommitsInBranch(git, 30, pr.getBranchName());
                    }

                    if (allCommitsArePushedWithSuccess(forcePushResult)) {
                        isPushSuccessful = true;
                    } else {

                        markPrWithWarnMessageAndLog(pr, "couldn't push PR " + pr.getNumber() + " to remote - check the logs for more details");

                        //not sure below is required.. will need to check
                        monitorPRoutcome(PR_NOT_REBASED, pr, stopWatchForMonitoring);
                        logGitStatus(git);
                    }

                } else {

                    markPrWithWarnMessageAndLog(pr, "couldn't rebase PR " + pr.getNumber() + " - check the logs for more details");

                    git.rebase().setUpstream("origin/" + pr.getBaseBranchName()).setOperation(RebaseCommand.Operation.ABORT).call();
                    monitorPRoutcome(PR_NOT_REBASED, pr, stopWatchForMonitoring);
                    logIssueInRebaseResult(rebaseResult);
                    logGitStatus(git);
                }

            }

            if (isRebaseSuccessful && isPushSuccessful) {

                monitorPRoutcome(PR_REBASED, pr, stopWatchForMonitoring);

                return new ImmutablePair(pr,
                        commitsToRebase.stream()
                                .map(commit -> new GitCommit(commit.getName(), commit.getShortMessage()))
                                .collect(toList()));
            }

        } catch (GitAPIException | IOException e) {
            markPrWithWarnMessageAndLog(pr,
                    "problem while rebasing/pushing PR " + pr.getNumber() + " on repo " + pr.getRepo() + " - check the logs for more details");
            log.warn("exception and stacktrace :", e);
        } finally {

            MDC.remove(REPOSITORY_KEY_FOR_MONITORING);
            MDC.remove(PR_NUMBER_KEY_FOR_MONITORING);
            MDC.remove(REBASE_STATUS_FOR_MONITORING);

        }

        return new ImmutablePair(pr, emptyList());
    }



    private void markPrWithWarnMessageAndLog(PullRequest pr, String warnMessage) {
        pr.setWarningMessageDuringRebasing(warnMessage);
        log.warn(warnMessage);
    }

    private boolean allCommitsArePushedWithSuccess(Iterable<PushResult> forcePushResult) {

        long nbCommitsWithNotOkStatus = StreamSupport.stream(forcePushResult.spliterator(), false)
                .flatMap(pushResult -> pushResult.getRemoteUpdates().stream())
                .filter(result -> result.getStatus() != RemoteRefUpdate.Status.OK)
                .count();

        return (nbCommitsWithNotOkStatus == 0);
    }



    private void switchToBranch(Git git, String branchName) throws IOException, GitAPIException {

        log.info("switching to branch {}...", branchName);

        String remoteBranch = "refs/heads/" + branchName;

        if (gitWrapper.refDoesntExist(git, remoteBranch)) {
            // first we need to ensure that the remote branch is visible locally
            Ref ref = gitWrapper.createBranch(git, branchName);

            log.info("Created local with ref: " + ref);
        }

        logGitStatus(git);

        //checkout remote branch
        log.info("Checkout local branch : " + branchName);
        gitWrapper.checkoutBranch(git, branchName, true);

    }

    private Git initRepoOnDefaultBranch(PullRequest pr) throws IOException, GitAPIException {

        com.societegenerale.cidroid.tasks.consumer.services.model.github.Repository repo = pr.getRepo();

        Path localRepoDirectory = createRepoDirectoryIfRequired(repo.getName());

        String defaultBranch = repo.getDefaultBranch();

        Git git;

        //TODO check space on machine regularly

        boolean isLocalRepoEmpty = false;
        try (Stream<Path> files = Files.list(localRepoDirectory)) {
            if (files.count() == 0) {
                isLocalRepoEmpty = true;
            }
        }

        if (isLocalRepoEmpty) {

            log.info("cloning repo {} in {}...", repo.getName(), localRepoDirectory);

            git = gitWrapper.createRepository(repo.getCloneUrl(), localRepoDirectory.toFile());

        } else {

            log.info("pulling repo {} in existing location {}...", repo.getName(), workingDirectory);

            git = gitWrapper.openRepository(localRepoDirectory.toFile());

            Repository gitRepo = git.getRepository();

            log.info("opened existing repo...");
            logGitStatus(git);

            if (gitRepo.getRepositoryState() != RepositoryState.SAFE) {

                log.warn("Git repo state is {}", gitRepo.getRepositoryState().name());

                // asked question here https://stackoverflow.com/questions/49127289/cant-get-out-of-rebasing-merge-state-with-jgit
                if (gitRepo.getRepositoryState() == RepositoryState.REBASING) {

                    log.info("trying to abort the rebase that is apparently in progress..");
                    RebaseResult result = gitWrapper.abortRebase(git, defaultBranch);
                    log.info("aborting result : {}", result.getStatus().name());
                }
            }

            if (localRepoIsDirty(git)) {

                resetLocalRepoToProperState(git);

                log.info("Git repo status following file cleanup for PR #{}", pr.getNumber());
                logGitStatus(git);
            }

            try {
                log.info("switching to default branch and pulling latest changes...");
                Ref ref = gitWrapper.checkoutBranch(git, defaultBranch, false);
                log.info("local repo pointing to commit {}", ref.getObjectId().toString());

                PullResult pullResult = gitWrapper.pull(git);

                if (pullResult.isSuccessful()) {
                    log.info("pull from " + defaultBranch + " : OK");
                } else {
                    log.warn("issue while pulling from {}, but no exception", defaultBranch);
                }
            } catch (CheckoutConflictException | WrongRepositoryStateException e) {

                log.warn("problem while pulling default branch {}", defaultBranch, e);
            }
        }

        return git;
    }

    private void resetLocalRepoToProperState(Git git) throws GitAPIException {

        log.warn("conflicts in local repo - trying to fix them one by one...");

        Status status = git.status().call();

        log.info("cleaning up directory..");
        Set<String> removedFiles = gitWrapper.cleanDirectories(git);
        for (String removedFile : removedFiles) {
            log.info("\t removed {}", removedFile);
        }

        ResetCommand reset = gitWrapper.resetRepo(git);
        reset.setRef(Constants.HEAD);

        log.info("trying to remove conflicted files from workspace..");
        for (String conflictedFile : status.getConflicting()) {
            log.info("\t resetting {}", conflictedFile);
            reset.addPath(conflictedFile);
        }

        log.info("trying to remove uncommitted files from workspace..");
        for (String addedFile : status.getAdded()) {
            log.info("\t resetting {}", addedFile);
            reset.addPath(addedFile);
        }

        reset.call();

        log.info("reverting modified files to latest known state..");
        CheckoutCommand checkout = gitWrapper.checkOut(git);
        for (String modifiedFile : status.getModified()) {
            log.info("\t reverting {}", modifiedFile);
            checkout.addPath(modifiedFile);
        }
        checkout.call();

    }

    private boolean localRepoIsDirty(Git git) throws GitAPIException {

        Status status = git.status().call();

        List potentialIssues = new ArrayList();
        potentialIssues.addAll(status.getConflicting());
        potentialIssues.addAll(status.getIgnoredNotInIndex());
        potentialIssues.addAll(status.getAdded());
        potentialIssues.addAll(status.getChanged());
        potentialIssues.addAll(status.getMissing());
        potentialIssues.addAll(status.getModified());
        potentialIssues.addAll(status.getRemoved());
        potentialIssues.addAll(status.getUncommittedChanges());
        potentialIssues.addAll(status.getUntracked());
        potentialIssues.addAll(status.getUntrackedFolders());

        return !potentialIssues.isEmpty();

    }

    private void logIssueInRebaseResult(RebaseResult rebaseResult) {

        StringBuilder sb = new StringBuilder();

        sb.append("aborting the rebase...\n");

        sb.append("\t-status: ").append(rebaseResult.getStatus().name()).append("\n");
        sb.append("\t-current Commit: ").append(rebaseResult.getCurrentCommit().getShortMessage()).append("\n");

        logListContent(sb, rebaseResult.getConflicts(), "conflicts");
        logListContent(sb, rebaseResult.getUncommittedChanges(), "uncommitted changes");

        if (rebaseResult.getFailingPaths() != null && !rebaseResult.getFailingPaths().isEmpty()) {
            sb.append("\t- failing paths: ").append("\n");

            for (Map.Entry failingPath : rebaseResult.getFailingPaths().entrySet()) {
                sb.append("\t- ").append(failingPath.getKey()).append(" - ").append(failingPath.getValue()).append("\n");
            }
        }

        log.warn(sb.toString());
    }

    private void logListContent(StringBuilder sb, List<String> list, String listType) {

        if (list != null && !list.isEmpty()) {
            sb.append("\t- ").append(listType).append(": ").append("\n");

            for (String item : list) {
                sb.append("\t- ").append(item).append("\n");
            }
        }
    }

    private void monitorPRoutcome(String outcome, PullRequest pr, StopWatch stopWatchForMonitoring) {
        stopWatchForMonitoring.stop();

        Event techEvent = Event.technical(outcome);
        techEvent.addAttribute("pullRequestNumber", String.valueOf(pr.getNumber()));
        techEvent.addAttribute("pullRequestUrl", pr.getHtmlUrl());
        techEvent.addAttribute("repo", pr.getRepo().getFullName());
        techEvent.addAttribute("duration", String.valueOf(stopWatchForMonitoring.getTime()));
        techEvent.publish();
    }

    private List<RevCommit> getCommitsOnWhichBranchIsLateComparedToBaseBranch(Git git, PullRequest pr) throws GitAPIException, IOException {

        List<RevCommit> commitsToRebase = gitWrapper.getCommitsOnWhichBranchIsLateComparedToBaseBranch(git, pr);

        String branchName = pr.getBranchName();

        if (commitsToRebase.isEmpty()) {
            log.info("no commits are late in branch {}, compared to {}", branchName, pr.getBaseBranchName());
        } else {
            GitWrapper.logDiffCommits(commitsToRebase, "These commits are in base branch, but not in branch " + pr.getBranchName());
        }

        return commitsToRebase;
    }

    private List<RevCommit> getCommitsInBranchOnly(Git git, PullRequest pr, boolean localBranch) throws GitAPIException, IOException {

        String branchName = pr.getBranchName();

        String branchToCheck;

        if (!localBranch) {
            branchToCheck = "remotes/origin/" + branchName;
        } else {
            branchToCheck = branchName;
        }

        List<RevCommit> branchCommits = gitWrapper.getCommitsInBranchOnly(git, pr.getBaseBranchName(), branchToCheck);

        GitWrapper.logDiffCommits(branchCommits, "These commits are in branch " + branchToCheck + " but not in default branch");

        return branchCommits;
    }



    private void logGitStatus(Git git) throws GitAPIException {

        if (log.isInfoEnabled()) {

            StringBuilder sbInfo = new StringBuilder("GIT STATUS\n");

            try {
                sbInfo.append("currently on branch : ").append(git.getRepository().getBranch()).append("\n");
            } catch (IOException e) {
                log.warn("not able to get the current branch name..", e);
            }

            sbInfo.append("repo state : ").append(git.getRepository().getRepositoryState().name()).append("\n");

            Status status = git.status().call();
            sbInfo.append("Added: ").append(status.getAdded()).append("\n");
            sbInfo.append("Changed: ").append(status.getChanged()).append("\n");
            sbInfo.append("Conflicting: ").append(status.getConflicting()).append("\n");
            sbInfo.append("ConflictingStageState: ").append(status.getConflictingStageState()).append("\n");
            sbInfo.append("IgnoredNotInIndex: ").append(status.getIgnoredNotInIndex()).append("\n");
            sbInfo.append("Missing: ").append(status.getMissing()).append("\n");
            sbInfo.append("Modified: ").append(status.getModified()).append("\n");
            sbInfo.append("Removed: ").append(status.getRemoved()).append("\n");
            sbInfo.append("Untracked: ").append(status.getUntracked()).append("\n");
            sbInfo.append("UntrackedFolders: ").append(status.getUntrackedFolders()).append("\n");

            sbInfo.append("GIT BRANCHES\n");
            List<Ref> call = git.branchList().call();
            for (Ref ref : call) {
                sbInfo.append("\t - Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName()).append("\n");
            }

            sbInfo.append("\n\nNow including remote branches:\n");

            List<Ref> remoteRef = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : remoteRef) {
                sbInfo.append("\t - Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName()).append("\n");
            }

            log.info(sbInfo.toString());
        }
    }

    private Path createRepoDirectoryIfRequired(String name) throws IOException {

        Path localRepoPath = Paths.get(workingDirectory + File.separator + name);

        if (!Files.exists(localRepoPath)) {
            return Files.createDirectories(localRepoPath);
        }

        return localRepoPath;
    }

    @SuppressWarnings("squid:S00112") //if we can't create working dir, we should actually fail the app, hence throwing RuntimeException
    private File createWorkingDirIfRequired() {
        String tmpDirStr = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpDirStr);
        if (!tmpDir.exists()) {
            boolean created = tmpDir.mkdirs();
            if (!created) {
                throw new RuntimeException("Unable to create tmp directory in which we'll checkout repo " + tmpDir);
            }
        }

        return tmpDir;
    }
}
