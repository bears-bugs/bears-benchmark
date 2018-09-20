package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.MDC;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * A wrapper over Git to enable dependency injection and therefore unit testing of components using Git
 */
@Slf4j
public class GitWrapper {

    public static final String COMMIT_SHA1_KEY_FOR_MONITORING = "SHA1";

    public static final String MORE_THAN_ONE_PARENT_FOR_MONITORING = "hasCommitWithMoreThan1parent";

    public Git createRepository(String cloneUrl, File localRepoDirectory) throws GitAPIException {

        return Git.cloneRepository()
                .setURI(cloneUrl)
                .setDirectory(localRepoDirectory)
                .call();
    }

    public Ref createBranch(Git git, String branchName) throws GitAPIException {

        return git.branchCreate()
                .setName(branchName)
                .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                .setStartPoint("origin/" + branchName)
                .setForce(true)
                .call();
    }

    public Ref checkoutBranch(Git git, String branchName, boolean shouldForce) throws GitAPIException {

        return git.checkout()
                .setForce(shouldForce)
                .setName(branchName)
                .call();
    }

    public List<RevCommit> getCommitsOnWhichBranchIsLateComparedToBaseBranch(Git git, PullRequest pr)
            throws IOException, GitAPIException {

        Repository repository = git.getRepository();

        Iterable<RevCommit> rawCommitsInDefaultBranchButMissingInPRbranch = git.log()
                .not(repository.resolve("remotes/origin/" + pr.getBranchName()))
                .add(repository.resolve(pr.getBaseBranchName()))
                .call();

        return StreamSupport.stream(rawCommitsInDefaultBranchButMissingInPRbranch.spliterator(), false).collect(toList());

    }

    public List<RevCommit> getCommitsInBranchOnly(Git git, String baseBranch, String otherBranch)
            throws IOException, GitAPIException {

        Repository repository = git.getRepository();

        Iterable<RevCommit> rawCommitsOnlyInPRbranch = git.log()
                .not(repository.resolve(baseBranch))
                .add(repository.resolve(otherBranch))
                .call();

        return StreamSupport.stream(rawCommitsOnlyInPRbranch.spliterator(), false).collect(toList());

    }

    public PullResult pull(Git git) throws GitAPIException {

        return git.pull().call();
    }

    public Git openRepository(File localRepoDirectory) throws IOException {
        return Git.open(localRepoDirectory);
    }

    public RebaseResult abortRebase(Git git, String defaultBranch) throws GitAPIException {

        return git.rebase().setUpstream("origin/" + defaultBranch).setOperation(RebaseCommand.Operation.ABORT).call();
    }

    public ResetCommand resetRepo(Git git) {

        return git.reset();
    }

    public Set<String> cleanDirectories(Git git) throws GitAPIException {

        return git.clean().setCleanDirectories(true).call();
    }

    public CheckoutCommand checkOut(Git git) {

        return git.checkout();
    }

    public boolean refDoesntExist(Git git, String branchName) throws IOException {

        return git.getRepository().exactRef(branchName) == null;
    }

    public static void logDiffCommits(List<RevCommit> commits, String message) {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        StringBuilder sb = new StringBuilder(message).append(" :\n");

        for (RevCommit commit : commits) {

            Date commitDate = commit.getAuthorIdent().getWhen();

            //logging commit SHA1 in MDC to ease monitoring
            MDC.put(COMMIT_SHA1_KEY_FOR_MONITORING, commit.getName());

            sb.append("commit time: ")
                    .append(sdf.format(commitDate))
                    .append(" - ")
                    .append(commit.getName())
                    .append(" - ")
                    .append(commit.getShortMessage())
                    .append("\n");

            MDC.remove(COMMIT_SHA1_KEY_FOR_MONITORING);
        }
        log.info(sb.toString());
    }

    public RebaseResult rebaseFrom(Git git, String branchName) throws GitAPIException {
        return git.rebase()
                .setUpstream("origin/" + branchName)
                // preserving merge commits, otherwise they are removed and it becomes confusing :
                // we seem to lose some commits during the rebase, even if we don't lose information (since the merge commit is empty)
                .setPreserveMerges(true)
                .call();
    }

    public Iterable<PushResult> forcePush(Git git, String login, String password) throws GitAPIException {

        val result = git.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(login, password))
                .setForce(true)
                .call();

        logPushResultDetails(result);

        return result;

    }

    public void logLastCommitsInBranch(Git git, int nbCommitsToLog, String branchName) throws IOException, GitAPIException {

        Iterable<RevCommit> lastCommitsOnBranch = git.log()
                .add(git.getRepository().resolve(branchName))
                .setMaxCount(nbCommitsToLog)
                .call();

        logDiffCommits(StreamSupport.stream(lastCommitsOnBranch.spliterator(), false).collect(toList()),
                "last " + nbCommitsToLog + "commits on branch " + branchName);

    }

    private void logPushResultDetails(Iterable<PushResult> forcePushResult) {
        if (log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("force push results : \n");

            for (PushResult pushResult : forcePushResult) {

                Collection<RemoteRefUpdate> pushResultDetails = pushResult.getRemoteUpdates();

                for (RemoteRefUpdate result : pushResultDetails) {
                    sb.append("- ").append(result.getRemoteName())
                            .append(" - ").append(result.getStatus().name())
                            .append(" - ").append(result.getNewObjectId());
                }

            }

            log.info(sb.toString());
        }
    }

}
