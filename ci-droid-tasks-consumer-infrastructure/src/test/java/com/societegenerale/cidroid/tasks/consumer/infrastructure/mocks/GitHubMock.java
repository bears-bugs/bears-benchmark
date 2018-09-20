package com.societegenerale.cidroid.tasks.consumer.infrastructure.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PRmergeableStatus;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.codestory.http.Context;
import net.codestory.http.WebServer;
import net.codestory.http.payload.Payload;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class GitHubMock {

    public static final int GITHUB_MOCK_PORT=9900;

    private static boolean hasStarted = false;

    public static boolean hasStarted() {
        return hasStarted;
    }

    @Getter
    private int openPullRequestsFetchCount =0;

    @Getter
    Map<Integer,Integer> hitsPerPullRequestNumber = new HashMap<>();

    @Getter
    Map<String,String> commentsPerPr = new HashMap<>();

    ConcurrentMap<Integer,PRmergeableStatus> mergeablePRstatusPerPullRequestNumber = new ConcurrentHashMap<>();

    ObjectMapper objectMapper=new ObjectMapper();

    public boolean start() {

        WebServer gitHubWebServer = new WebServer();
        gitHubWebServer.configure(
                routes -> {
                    routes.get("/api/v3/repos/baxterthehacker/public-repo/pulls?state=open", context -> getOpenPullRequests());
                    routes.get("/api/v3/repos/baxterthehacker/public-repo/pulls/:pullRequestId", (context,pullRequestId) -> getPullRequest(pullRequestId));
                    routes.get("/api/v3/users/:login", (context,userLogin) -> getUser(userLogin));
                    routes.post("/api/v3/repos/baxterthehacker/public-repo/issues/:prNumber/comments", (context,prNumber) -> postCommentOnPullRequest(context,prNumber));

                }
        ).start(GITHUB_MOCK_PORT);

        hasStarted = true;

        return true;
    }

    private Payload postCommentOnPullRequest(Context context, String prNumber) throws IOException {

        log.info("receiving a comment for PR #{}",prNumber);

        commentsPerPr.put(prNumber,context.request().content());

        return Payload.created();
    }

    public void updatePRmergeabilityStatus(int prNumber, PRmergeableStatus status){

        log.info("updating merge status of PR {} to {}",prNumber,status);

        mergeablePRstatusPerPullRequestNumber.put(prNumber,status);
    }

    private Object getUser(String userLogin) throws IOException {

        log.info("received a 'details' request for user {}...",userLogin);

        return new Payload("application/json", IOUtils
                .toString(GitHubMock.class.getClassLoader().getResourceAsStream("user.json"), "UTF-8"));
    }

    private Object getPullRequest(String pullRequestNumberasString) throws IOException {

        log.info("received a 'details' request for PR {}...",pullRequestNumberasString);

        int pullRequestNumber=Integer.parseInt(pullRequestNumberasString);

        hitsPerPullRequestNumber.putIfAbsent(pullRequestNumber,0);
        hitsPerPullRequestNumber.compute(pullRequestNumber,(key,value) -> value=value+1);

        mergeablePRstatusPerPullRequestNumber.putIfAbsent(pullRequestNumber,PRmergeableStatus.NOT_MERGEABLE);

        String pullRequest = IOUtils
                .toString(GitHubMock.class.getClassLoader().getResourceAsStream("singlePullRequest.json"), "UTF-8");

        pullRequest=updateMergeabilityStatus(pullRequest,mergeablePRstatusPerPullRequestNumber.get(pullRequestNumber));

        return new Payload("application/json",pullRequest);
    }

    private String updateMergeabilityStatus(String pullRequestAsString, PRmergeableStatus pRmergeableStatus) throws IOException {

        PullRequest pullRequest= objectMapper.readValue(pullRequestAsString, PullRequest.class);
        pullRequest.setMergeable(pRmergeableStatus.getValue());

        return objectMapper.writeValueAsString(pullRequest);
    }

    private Object getOpenPullRequests() throws IOException {

        openPullRequestsFetchCount++;

        log.info("received an open PRs request for the {} times...", openPullRequestsFetchCount);

        return new Payload("application/json", IOUtils
                .toString(GitHubMock.class.getClassLoader().getResourceAsStream("pullRequests.json"), "UTF-8"));
    }

    public void reset() {
        openPullRequestsFetchCount =0;
        hitsPerPullRequestNumber.clear();
        commentsPerPr.clear();
    }

}
