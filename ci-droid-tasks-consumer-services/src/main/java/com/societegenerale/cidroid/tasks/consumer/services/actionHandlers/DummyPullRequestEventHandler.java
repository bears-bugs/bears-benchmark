package com.societegenerale.cidroid.tasks.consumer.services.actionHandlers;

import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequestEvent;

public class DummyPullRequestEventHandler implements PullRequestEventHandler {

    @Override
    public void handle(PullRequestEvent event) {
        //do nothing
    }
}
