package com.societegenerale.cidroid.tasks.consumer.services;

import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import org.mockito.ArgumentMatcher;

public class PullRequestMatcher implements ArgumentMatcher<PullRequest> {

    private int pullRequestNumber;

    public PullRequestMatcher(int pullRequestNumber) {
        this.pullRequestNumber = pullRequestNumber;
    }

    @Override
    public boolean matches(PullRequest candidatePr) {
        return candidatePr.getNumber()==pullRequestNumber;
    }
}
