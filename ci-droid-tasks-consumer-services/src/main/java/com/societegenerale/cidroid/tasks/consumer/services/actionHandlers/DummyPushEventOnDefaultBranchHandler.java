package com.societegenerale.cidroid.tasks.consumer.services.actionHandlers;

import com.societegenerale.cidroid.tasks.consumer.services.model.GitHubEvent;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;

import java.util.List;

public class DummyPushEventOnDefaultBranchHandler implements PushEventOnDefaultBranchHandler {
    @Override
    public void handle(GitHubEvent event, List<PullRequest> pullRequests) {
        //do nothing
    }
}
