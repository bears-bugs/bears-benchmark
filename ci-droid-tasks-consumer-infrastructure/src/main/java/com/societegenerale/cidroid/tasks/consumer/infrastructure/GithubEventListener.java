package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.tasks.consumer.services.PullRequestEventService;
import com.societegenerale.cidroid.tasks.consumer.services.PushEventOnDefaultBranchService;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequestEvent;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;

@Slf4j
public class GithubEventListener {

    private PushEventOnDefaultBranchService pushOnDefaultBranchService;

    private PullRequestEventService pullRequestEventService;

    public GithubEventListener(PushEventOnDefaultBranchService pushOnDefaultBranchService, PullRequestEventService pullRequestEventService) {
        this.pushOnDefaultBranchService = pushOnDefaultBranchService;
        this.pullRequestEventService = pullRequestEventService;

    }

    @StreamListener("push-on-default-branch")
    public void onGitHubPushEventOnDefaultBranch(PushEvent pushEvent) {

        try {
            log.info("received event on branch {} for repo {}", pushEvent.getRef(), pushEvent.getRepository().getFullName());

            pushOnDefaultBranchService.onGitHubPushEvent(pushEvent);
        } catch (Exception e) {
            log.warn("problem while processing the event {}", pushEvent, e);
        }
    }

    @StreamListener("pull-request-event")
    public void onGitHubPullRequestEvent(PullRequestEvent pullRequestEvent) {

        log.info("received pullRequest event of type {} for repo {}",pullRequestEvent.getAction(),pullRequestEvent.getRepository().getFullName());

        pullRequestEventService.onGitHubPullRequestEvent(pullRequestEvent);

    }

}
