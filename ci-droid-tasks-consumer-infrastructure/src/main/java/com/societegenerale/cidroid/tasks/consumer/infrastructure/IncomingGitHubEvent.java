package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface IncomingGitHubEvent {

    @Input("push-on-default-branch")
    SubscribableChannel pushOnDefaultBranch();


    @Input("pull-request-event")
    SubscribableChannel pullRequestEvent();

    @Input("actions-to-perform")
    SubscribableChannel actionToPerform();

}
