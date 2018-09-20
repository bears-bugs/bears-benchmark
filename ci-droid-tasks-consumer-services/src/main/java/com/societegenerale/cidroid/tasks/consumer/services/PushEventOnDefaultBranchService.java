package com.societegenerale.cidroid.tasks.consumer.services;

import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.PushEventOnDefaultBranchHandler;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PRmergeableStatus;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import com.societegenerale.cidroid.tasks.consumer.services.monitoring.Event;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;

import static com.societegenerale.cidroid.tasks.consumer.services.MonitoringEvents.PUSH_EVENT_TO_PROCESS;
import static java.util.stream.Collectors.toList;

@Slf4j
public class PushEventOnDefaultBranchService {

    private RemoteGitHub gitHub;

    private List<PushEventOnDefaultBranchHandler> actionHandlers;

    @Setter
    //@Value("${action.mergeable.retry.sleep:300}")
    private long sleepDurationBeforeTryingAgainToFetchMergeableStatus = 300;

    @Setter
    //@Value("${action.mergeable.retry.max:10}")
    private int maxRetriesForMergeableStatus = 10;

    public PushEventOnDefaultBranchService(RemoteGitHub gitHub, List<PushEventOnDefaultBranchHandler> pushEventOnDefaultBranchHandlers) {

        this.gitHub = gitHub;
        this.actionHandlers = pushEventOnDefaultBranchHandlers;
    }

    public void onGitHubPushEvent(PushEvent pushEvent) {

        if (shouldNotProcess(pushEvent)) {
            return;
        }

        Event techEvent = Event.technical(PUSH_EVENT_TO_PROCESS);
        techEvent.addAttribute("repo", pushEvent.getRepository().getFullName());

        StopWatch stopWatch = StopWatch.createStarted();

        List<PullRequest> openPRs = retrieveOpenPrs(pushEvent.getRepository().getFullName());

        List<PullRequest> openPRsWithDefinedMergeabilityStatus = figureOutMergeableStatusFor(openPRs, 0);

        logPrMergeabilityStatus(openPRsWithDefinedMergeabilityStatus);

        for (PushEventOnDefaultBranchHandler pushEventOnDefaultBranchHandler : actionHandlers) {
            pushEventOnDefaultBranchHandler.handle(pushEvent, openPRsWithDefinedMergeabilityStatus);
        }

        stopWatch.stop();
        techEvent.addAttribute("processTime", String.valueOf(stopWatch.getTime()));
        techEvent.publish();

    }

    private boolean shouldNotProcess(PushEvent pushEvent) {

        if (!pushEvent.happenedOnDefaultBranch()) {
            log.warn("received an event from branch that is not default, ie {} - how is it possible ? ", pushEvent.getRef());
            return true;
        }

        return false;
    }

    private void logPrMergeabilityStatus(List<PullRequest> openPRsWithDefinedMergeabilityStatus) {
        if (log.isInfoEnabled()) {

            StringBuilder sb = new StringBuilder("PR status :\n");

            for (PullRequest pr : openPRsWithDefinedMergeabilityStatus) {
                sb.append("\t- PR #").append(pr.getNumber()).append(" : ").append(pr.getMergeStatus()).append("\n");
            }

            log.info(sb.toString());
        }
    }

    private List<PullRequest> figureOutMergeableStatusFor(List<PullRequest> openPRs, int nbRetry) {

        List<PullRequest> pullRequestsWithDefinedMergeabilityStatus = openPRs.stream()
                .filter(pr -> PRmergeableStatus.UNKNOWN != pr.getMergeStatus())
                .collect(toList());

        List<PullRequest> pullRequestsWithUnknownMergeabilityStatus = openPRs.stream()
                .filter(pr -> PRmergeableStatus.UNKNOWN == pr.getMergeStatus())
                .collect(toList());

        if (pullRequestsWithUnknownMergeabilityStatus.size() > 0 && nbRetry < maxRetriesForMergeableStatus) {

            if (log.isDebugEnabled()) {

                StringBuilder sb = new StringBuilder("these PRs don't have a mergeable status yet :\n");

                pullRequestsWithUnknownMergeabilityStatus.stream().forEach(pr -> sb.append("\t - ").append(pr.getNumber()).append("\n"));

                sb.append("waiting for ")
                        .append(sleepDurationBeforeTryingAgainToFetchMergeableStatus)
                        .append("ms before trying again for the ")
                        .append(nbRetry + 1)
                        .append("th time...");

                log.debug(sb.toString());
            }

            try {
                Thread.sleep(sleepDurationBeforeTryingAgainToFetchMergeableStatus);
            } catch (InterruptedException e) {
                log.error("interrupted while sleeping to get PR status",e);
            }

            List<PullRequest> prsWithUpdatedStatus = pullRequestsWithUnknownMergeabilityStatus.stream()
                    .map(pr -> gitHub.fetchPullRequestDetails(pr.getRepo().getFullName(), pr.getNumber()))
                    .collect(toList());

            pullRequestsWithDefinedMergeabilityStatus.addAll(figureOutMergeableStatusFor(prsWithUpdatedStatus, ++nbRetry));
        } else if (nbRetry >= maxRetriesForMergeableStatus) {

            log.warn("not able to retrieve merge status for below PRs after several tries.. giving up");
            pullRequestsWithUnknownMergeabilityStatus.stream().forEach(pr -> log.info("\t - {}", pr.getNumber()));
        }

        return pullRequestsWithDefinedMergeabilityStatus;
    }

    private List<PullRequest> retrieveOpenPrs(String repoFullName) {

        List<PullRequest> openPrs = gitHub.fetchOpenPullRequests(repoFullName);

        log.info("{} open PRs found on repo {}", openPrs.size(), repoFullName);

        return openPrs.stream()
                .map(pr -> gitHub.fetchPullRequestDetails(repoFullName, pr.getNumber()))
                .collect(toList());
    }



}
