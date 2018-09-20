package com.societegenerale.cidroid.tasks.consumer.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.PushEventOnDefaultBranchHandler;
import com.societegenerale.cidroid.tasks.consumer.services.model.GitHubEvent;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PRmergeableStatus;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static com.societegenerale.cidroid.tasks.consumer.services.TestUtils.readFromInputStream;
import static com.societegenerale.cidroid.tasks.consumer.services.model.github.PRmergeableStatus.NOT_MERGEABLE;
import static com.societegenerale.cidroid.tasks.consumer.services.model.github.PRmergeableStatus.UNKNOWN;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PushOnDefaultBranchServiceTest {

    private static final String FULL_REPO_NAME = "baxterthehacker/public-repo";

    private static final int PULL_REQUEST_ID = 1347;

    private static final String SINGLE_PULL_REQUEST_JSON = "/singlePullRequest.json";

    RemoteGitHub mockRemoteGitHub = mock(RemoteGitHub.class);

    PushEventOnDefaultBranchHandler mockPushEventOnDefaultBranchHandler = mock(PushEventOnDefaultBranchHandler.class);

    PushEventOnDefaultBranchService pushOnDefaultBranchService;

    PushEvent pushEvent;

    PullRequest singlePr;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void setup() throws IOException {

        List<PushEventOnDefaultBranchHandler> pushEventOnDefaultBranchHandlers = new ArrayList<>();
        pushEventOnDefaultBranchHandlers.add(mockPushEventOnDefaultBranchHandler);

        pushOnDefaultBranchService = new PushEventOnDefaultBranchService(mockRemoteGitHub, pushEventOnDefaultBranchHandlers);

        String pushEventPayload = readFromInputStream(getClass().getResourceAsStream("/pushEvent.json"));
        pushEvent = objectMapper.readValue(pushEventPayload, PushEvent.class);


        String openPrsOnRepoAsString = readFromInputStream(getClass().getResourceAsStream("/pullRequests.json"));
        List openPrsOnRepo = objectMapper.readValue(openPrsOnRepoAsString, new TypeReference<List<PullRequest>>() {});
        when(mockRemoteGitHub.fetchOpenPullRequests(FULL_REPO_NAME)).thenReturn(openPrsOnRepo);

        String prAsString = readFromInputStream(getClass().getResourceAsStream(SINGLE_PULL_REQUEST_JSON));
        singlePr = objectMapper.readValue(prAsString, PullRequest.class);
        when(mockRemoteGitHub.fetchPullRequestDetails(FULL_REPO_NAME, PULL_REQUEST_ID)).thenReturn(singlePr);

        when(mockRemoteGitHub.fetchUser("octocat")).thenReturn(new User("octocat", "octocat@github.com"));

    }

    @Test
    public void shouldRequestAllOpenPRsWhenPushOnDefaultBranch() {

        pushOnDefaultBranchService.onGitHubPushEvent(pushEvent);

        verify(mockRemoteGitHub, times(1)).fetchOpenPullRequests(FULL_REPO_NAME);
    }

    @Test
    public void shouldNotDoAnythingIfPushEventNotOnDefaultBranch() {

        pushEvent.setRef("someOtherBranch");

        pushOnDefaultBranchService.onGitHubPushEvent(pushEvent);

        verify(mockRemoteGitHub, never()).fetchOpenPullRequests(any(String.class));
    }

    @Test
    public void shouldRequestOpenPRDetailsWhenPushOnDefaultBranch() {

        pushOnDefaultBranchService.onGitHubPushEvent(pushEvent);

        verify(mockRemoteGitHub, times(1)).fetchPullRequestDetails("baxterthehacker/public-repo", PULL_REQUEST_ID);
    }

    @Test
    public void shouldApplyActionHandlersForRepository(){

        pushOnDefaultBranchService.onGitHubPushEvent(pushEvent);

        verify(mockPushEventOnDefaultBranchHandler,times(1)).handle(eq(pushEvent),anyList());

    }


    @Test
    public void shouldTrySeveralTimesToGetMergeableStatusIfNotAvailableImmediately() throws Exception {

        updatePRmergeabilityStatus(UNKNOWN);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                System.out.println("about to sleep...");
                TimeUnit.MILLISECONDS.sleep(700);
                System.out.println("done sleeping !");
                updatePRmergeabilityStatus(NOT_MERGEABLE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("submitted task to update PR merge status shortly...");

        pushOnDefaultBranchService.onGitHubPushEvent(pushEvent);

        ArgumentCaptor<List> prListCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<GitHubEvent> gitHubEventCaptor = ArgumentCaptor.forClass(GitHubEvent.class);

        await().atMost(3, SECONDS)
                .until(() -> verify(mockPushEventOnDefaultBranchHandler, times(1)).handle(gitHubEventCaptor.capture(), prListCaptor.capture()));

        assertThat(prListCaptor.getValue()).containsExactly(singlePr);
        assertThat(gitHubEventCaptor.getValue()).isEqualTo(pushEvent);

        verify(mockRemoteGitHub, atLeast(2)).fetchPullRequestDetails("baxterthehacker/public-repo", PULL_REQUEST_ID);

        executor.shutdownNow();
    }

    private void updatePRmergeabilityStatus(PRmergeableStatus pRmergeableStatus) throws IOException {

        PullRequest pullRequest = objectMapper.readValue(readFromInputStream(getClass().getResourceAsStream(SINGLE_PULL_REQUEST_JSON)), PullRequest.class);
        pullRequest.setMergeable(pRmergeableStatus.getValue());

        when(mockRemoteGitHub.fetchPullRequestDetails(FULL_REPO_NAME, PULL_REQUEST_ID)).thenReturn(pullRequest);
    }

}