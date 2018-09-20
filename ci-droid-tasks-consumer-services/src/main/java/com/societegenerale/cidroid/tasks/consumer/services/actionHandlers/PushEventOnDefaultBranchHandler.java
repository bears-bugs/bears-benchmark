package com.societegenerale.cidroid.tasks.consumer.services.actionHandlers;

import com.societegenerale.cidroid.tasks.consumer.services.model.GitHubEvent;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;

import java.util.List;

public interface PushEventOnDefaultBranchHandler {

    void handle(GitHubEvent event, List<PullRequest> pullRequests);

}
