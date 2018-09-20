package com.societegenerale.cidroid.tasks.consumer.services.actionHandlers;

import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.model.GitHubEvent;
import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import com.societegenerale.cidroid.tasks.consumer.services.monitoring.Event;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.societegenerale.cidroid.tasks.consumer.services.MonitoringEvents.NOTIFICATION_FOR_NON_MERGEABLE_PR;
import static com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier.PULL_REQUEST;

@Slf4j
public class NotificationsHandler implements PushEventOnDefaultBranchHandler {

    private RemoteGitHub gitHub;

    private List<Notifier> notifiers;

    public NotificationsHandler(RemoteGitHub gitHub, List<Notifier> notifiers) {

        this.gitHub = gitHub;
        this.notifiers = notifiers;

    }

    @Override
    public void handle(GitHubEvent event, List<PullRequest> pullRequests) {

        PushEvent pushEvent;

        if (event instanceof PushEvent){
            pushEvent = (PushEvent) event;
        }
        else{
            log.warn("can't process the event as we are expecting a {}, but we got a {}",PushEvent.class,event.getClass());
            return;
        }

        pullRequests.stream()
                .filter(pr -> pr.getMergeable().equals(Boolean.FALSE))
                .forEach(pr -> {

                            notifiers.forEach(n -> {
                                User user = User.buildFrom(pr, gitHub);

                                Event techEvent = Event.technical(NOTIFICATION_FOR_NON_MERGEABLE_PR);
                                techEvent.addAttribute("pullRequestNumber", String.valueOf(pr.getNumber()));
                                techEvent.addAttribute("pullRequestUrl", pr.getHtmlUrl());
                                techEvent.addAttribute("repo", pr.getRepo().getFullName());
                                techEvent.addAttribute("pullRequestOwner", user.getLogin());
                                techEvent.publish();


                                Map<String,Object> additionalInfos=new HashMap<>();
                                additionalInfos.put(PULL_REQUEST,pr);

                                log.info("notifying that PR #{} is not mergeable..", pr.getNumber());
                                n.notify(user, Message.buildFromNotMergeablePR(pr, pushEvent),additionalInfos);
                            });
                        }
                );

    }
}
