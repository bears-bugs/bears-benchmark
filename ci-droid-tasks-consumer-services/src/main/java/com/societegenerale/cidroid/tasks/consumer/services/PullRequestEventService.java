package com.societegenerale.cidroid.tasks.consumer.services;

import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.PullRequestEventHandler;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequestEvent;
import com.societegenerale.cidroid.tasks.consumer.services.monitoring.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class PullRequestEventService {

    private List<PullRequestEventHandler> actionHandlers;

    public PullRequestEventService(List<PullRequestEventHandler> actionHandlers) {
        this.actionHandlers = actionHandlers;
    }

    private static List<String> acceptedPullrequestEventType = Arrays.asList("opened", "synchronize");

    public void onGitHubPullRequestEvent(PullRequestEvent pullRequestEvent) {

        if (!acceptedPullrequestEventType.contains(pullRequestEvent.getAction())) {
            log.debug("not processing pullRequest event of type {}", pullRequestEvent.getAction());
            return;
        }

        Event techEvent = Event.technical(MonitoringEvents.PULL_REQUEST_EVENT_TO_PROCESS);
        techEvent.addAttribute("repo", pullRequestEvent.getRepository().getFullName());
        techEvent.addAttribute("prNumber", String.valueOf(pullRequestEvent.getPrNumber()));

        StopWatch stopWatch = StopWatch.createStarted();

        for (PullRequestEventHandler pullRequestEventHandler : actionHandlers) {
            pullRequestEventHandler.handle(pullRequestEvent);
        }

        stopWatch.stop();
        techEvent.addAttribute("processTime", String.valueOf(stopWatch.getTime()));
        techEvent.publish();



    }

}
