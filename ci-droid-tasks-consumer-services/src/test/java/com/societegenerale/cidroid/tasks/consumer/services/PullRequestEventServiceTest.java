package com.societegenerale.cidroid.tasks.consumer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.PullRequestEventHandler;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequestEvent;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class PullRequestEventServiceTest {

    PullRequestEventService pullRequestEventService;

    PullRequestEventHandler mockHandler = mock(PullRequestEventHandler.class);

    PullRequestEvent pullRequestEvent;

    @Before
    public void setup() throws IOException {

        pullRequestEventService = new PullRequestEventService(Arrays.asList(mockHandler));

        String pullRequestEventPayload = IOUtils
                .toString(PullRequestEventServiceTest.class.getClassLoader().getResourceAsStream("pullRequestEvent.json"), "UTF-8");

        pullRequestEvent = new ObjectMapper().readValue(pullRequestEventPayload, PullRequestEvent.class);

    }

    @Test
    public void shouldProcessPullRequestEventsThatAre_Opened() {

        pullRequestEventService.onGitHubPullRequestEvent(pullRequestEvent);

        verify(mockHandler, times(1)).handle(pullRequestEvent);

    }

    @Test
    public void shouldProcessPullRequestEventsThatAre_Synchronize() {

        pullRequestEvent.setAction("synchronize");

        pullRequestEventService.onGitHubPullRequestEvent(pullRequestEvent);

        verify(mockHandler, times(1)).handle(pullRequestEvent);

    }

    @Test
    public void should_NOT_ProcessPullRequestEventsThatAre_Assigned() {

        pullRequestEvent.setAction("assigned");

        pullRequestEventService.onGitHubPullRequestEvent(pullRequestEvent);

        verify(mockHandler, never()).handle(pullRequestEvent);

    }

}