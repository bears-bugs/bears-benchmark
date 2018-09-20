package com.societegenerale.cidroid.tasks.consumer.services.actionHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.services.GitCommit;
import com.societegenerale.cidroid.tasks.consumer.services.PullRequestMatcher;
import com.societegenerale.cidroid.tasks.consumer.services.Rebaser;
import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.Comment;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.societegenerale.cidroid.tasks.consumer.services.TestUtils.readFromInputStream;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RebaseHandlerTest {

    private static final String SINGLE_PULL_REQUEST_JSON = "/singlePullRequest.json";

    RemoteGitHub mockRemoteGitHub = mock(RemoteGitHub.class);

    Rebaser mockRebaser = mock(Rebaser.class);

    RebaseHandler rebaseHandler;

    PullRequest singlePr;

    int singlePrNumber=1347;

    PushEvent pushEvent;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() throws IOException {

        String prAsString = readFromInputStream(getClass().getResourceAsStream(SINGLE_PULL_REQUEST_JSON));
        singlePr = objectMapper.readValue(prAsString, PullRequest.class);

        String pushEventPayload = readFromInputStream(getClass().getResourceAsStream("/pushEvent.json"));
        pushEvent = objectMapper.readValue(pushEventPayload, PushEvent.class);

        rebaseHandler=new RebaseHandler(mockRebaser,mockRemoteGitHub);
    }


    @Test
    public void shouldRebaseAndPostGitHubCommentForMergeablePr(){

        singlePr.setMergeable(true);

        String commitId="123456";

        List rebasedCommitsForOnePr = Arrays.asList(new GitCommit(commitId, "a tiny commit that was done on master"));
        Pair<PullRequest, List<GitCommit>> rebaseResult = new ImmutablePair(singlePr, rebasedCommitsForOnePr);
        when(mockRebaser.rebase(singlePr)).thenReturn(rebaseResult);

        rebaseHandler.handle(pushEvent,Arrays.asList(singlePr));

        verify(mockRebaser,times(1)).rebase(singlePr);

        ArgumentCaptor<Comment> commentCaptor=ArgumentCaptor.forClass(Comment.class);

        verify(mockRemoteGitHub,times(1)).addCommentDescribingRebase(eq(singlePr.getRepo().getFullName()),eq(singlePr.getNumber()),commentCaptor.capture());

        String comment=commentCaptor.getValue().getBody();

        assertThat(comment).startsWith("CI-droid has rebased below ");
        assertThat(comment).contains(commitId);

    }

    @Test
    public void shouldNotRebaseWhenPRisMadeFromFork(){

        singlePr.setMergeable(true);
        singlePr.setMadeFromForkedRepo(true);

        rebaseHandler.handle(pushEvent,Arrays.asList(singlePr));

        verify(mockRebaser,never()).rebase(singlePr);

    }

    @Test
    public void shouldPostGitHubWarningCommentWhenProblemWhileRebasing(){

        singlePr.setMergeable(true);

        singlePr.setWarningMessageDuringRebasing("one commit had conflicts");

        Pair<PullRequest, List<GitCommit>> rebaseResult = new ImmutablePair(singlePr, emptyList());

        PullRequestMatcher matchesSimplePr=new PullRequestMatcher(singlePrNumber);

        when(mockRebaser.rebase(argThat(matchesSimplePr))).thenReturn(rebaseResult);

        rebaseHandler.handle(pushEvent,Arrays.asList(singlePr));

        verify(mockRebaser,times(1)).rebase(argThat(matchesSimplePr));

        ArgumentCaptor<Comment> commentCaptor=ArgumentCaptor.forClass(Comment.class);

        verify(mockRemoteGitHub,times(1)).addCommentDescribingRebase(eq(singlePr.getRepo().getFullName()),eq(singlePr.getNumber()),commentCaptor.capture());

        String comment=commentCaptor.getValue().getBody();

        assertThat(comment).startsWith("There was a problem during the rebase/push process :");
        assertThat(comment).endsWith("one commit had conflicts");
    }


    @Test
    public void shouldNotRebase_whenPrIsNotMergeable(){

        rebaseHandler.handle(pushEvent,Arrays.asList(singlePr));

        assertThat(verify(mockRebaser, never()).rebase(any(PullRequest.class)));

    }
}