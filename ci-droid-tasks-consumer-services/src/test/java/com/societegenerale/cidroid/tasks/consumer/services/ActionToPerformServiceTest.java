package com.societegenerale.cidroid.tasks.consumer.services;

import com.oneeyedmen.fakir.Faker;
import com.societegenerale.cidroid.api.ResourceToUpdate;
import com.societegenerale.cidroid.api.gitHubInteractions.DirectPushGitHubInteraction;
import com.societegenerale.cidroid.api.gitHubInteractions.PullRequestGitHubInteraction;
import com.societegenerale.cidroid.tasks.consumer.services.exceptions.BranchAlreadyExistsException;
import com.societegenerale.cidroid.tasks.consumer.services.exceptions.GitHubAuthorizationException;
import com.societegenerale.cidroid.tasks.consumer.services.model.BulkActionToPerform;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Base64;

import static com.societegenerale.cidroid.tasks.consumer.services.model.github.UpdatedResource.UpdateStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class ActionToPerformServiceTest {

    private static final String MODIFIED_CONTENT = "modifiedContent";

    private static final String REPO_FULL_NAME = "repoFullName";

    private final String SOME_USER_NAME = "someUserName";

    private final String SOME_OAUTH_TOKEN = "abcdefghij12345";

    private final String SOME_EMAIL = "someEmail@someDomain.com";

    private final String SOME_COMMIT_MESSAGE = "this is the original commit message by the user";

    private String sha1ForHeadOnMaster = "123456abcdef";

    private String branchNameToCreateForPR = "myPrBranch";

    private RemoteGitHub mockRemoteGitHub = mock(RemoteGitHub.class);

    private ActionNotificationService mockActionNotificationService = mock(ActionNotificationService.class);

    ResourceContent fakeResourceContentBeforeUpdate = new Faker<ResourceContent>() {
        String htmlLink = "http://github.com/someRepo/linkToTheResource";
    }.get();

    PullRequest fakePullRequest = new Faker<PullRequest>() {
        int number = 789;

        String htmlUrl = "http://linkToThePr";
    }.get();

    Repository fakeRepository = new Faker<Repository>() {
        String defaultBranch = "masterBranch";

        String fullName = REPO_FULL_NAME;
    }.get();

    Reference fakeHeadOnMasterReference = new Faker<Reference>() {
        Reference.ObjectReference object = new Reference.ObjectReference("commit", sha1ForHeadOnMaster);
    }.get();

    private Reference fakeBranchCreatedReference = Faker.fakeA(Reference.class);

    private ArgumentCaptor<UpdatedResource> updatedResourceCaptor = ArgumentCaptor.forClass(UpdatedResource.class);

    private ArgumentCaptor<DirectCommit> directCommitCaptor = ArgumentCaptor.forClass(DirectCommit.class);

    private ArgumentCaptor<PullRequestToCreate> newPrCaptor = ArgumentCaptor.forClass(PullRequestToCreate.class);

    private ActionToPerformService actionToPerformService = new ActionToPerformService(mockRemoteGitHub, mockActionNotificationService);

    private TestActionToPerform testActionToPerform = new TestActionToPerform();

    private ResourceToUpdate resourceToUpdate = new ResourceToUpdate(REPO_FULL_NAME, "someFile.txt", "master", StringUtils.EMPTY);

    private UpdatedResource updatedResource;

    private BulkActionToPerform.BulkActionToPerformBuilder bulkActionToPerformBuilder;

    @Before
    public void setup() throws GitHubAuthorizationException {

        testActionToPerform.setContentToProvide(MODIFIED_CONTENT);
        testActionToPerform.setContinueIfResourceDoesntExist(true);

        bulkActionToPerformBuilder = BulkActionToPerform.builder().gitLogin(SOME_USER_NAME)
                .gitHubOauthToken(SOME_OAUTH_TOKEN)
                .email(SOME_EMAIL)
                .commitMessage(SOME_COMMIT_MESSAGE)
                .resourcesToUpdate(Arrays.asList(resourceToUpdate))
                .actionToReplicate(testActionToPerform);

        UpdatedResource.Content content = new UpdatedResource.Content();
        content.setHtmlUrl("http://github.com/someRepo/linkToTheResource");

        updatedResource = UpdatedResource.builder().content(content)
                .commit(Faker.fakeA(Commit.class))
                .build();

        when(mockRemoteGitHub.updateContent(anyString(), anyString(), any(DirectCommit.class), anyString()))
                .thenReturn(updatedResource); // lenient mocking - we're asserting in verify.
    }

    @Test
    public void performActionOfType_DIRECT_PUSH() throws GitHubAuthorizationException {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", "master")).thenReturn(fakeResourceContentBeforeUpdate);

        actionToPerformService.perform(bulkActionToPerform);

        assertContentHasBeenUpdatedOnBranch("master");

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_OK);
    }

    @Test
    public void dontPushWhenContentIsNotModifiedByAction_andActionIs_DIRECT_PUSH() throws GitHubAuthorizationException {

        String contentThatWillNotBeModified = "some content that will not change";
        testActionToPerform.setContentToProvide(contentThatWillNotBeModified);

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();

        ResourceContent fakeResourceContentWithSpecificContentBeforeUpdate = new Faker<ResourceContent>() {
            String htmlLink = "http://github.com/someRepo/linkToTheResource";

            String base64EncodedContent = GitHubContentBase64codec.encode(contentThatWillNotBeModified);
        }.get();

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", "master")).thenReturn(fakeResourceContentWithSpecificContentBeforeUpdate);

        actionToPerformService.perform(bulkActionToPerform);

        verify(mockRemoteGitHub, never()).updateContent(anyString(), anyString(), any(DirectCommit.class), anyString());

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_KO_FILE_CONTENT_IS_SAME);

    }

    @Test
    public void dontPushWhenContentIsNotModifiedByAction_andActionIs_PULL_REQUEST()
            throws BranchAlreadyExistsException, GitHubAuthorizationException {

        String contentThatWillNotBeModified = "some content that will not change";

        testActionToPerform.setContentToProvide(contentThatWillNotBeModified);

        BulkActionToPerform bulkActionToPerform = doApullRequestAction();

        ResourceContent fakeResourceContentWithSpecificContentBeforeUpdate = new Faker<ResourceContent>() {
            String htmlLink = "http://github.com/someRepo/linkToTheResource";

            String base64EncodedContent = GitHubContentBase64codec.encode(contentThatWillNotBeModified);
        }.get();

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", branchNameToCreateForPR))
                .thenReturn(fakeResourceContentWithSpecificContentBeforeUpdate);

        actionToPerformService.perform(bulkActionToPerform);

        verify(mockRemoteGitHub, never()).updateContent(anyString(), anyString(), any(DirectCommit.class), anyString());

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_KO_FILE_CONTENT_IS_SAME);

    }

    @Test
    public void performActionOfType_PULL_REQUEST() throws BranchAlreadyExistsException, GitHubAuthorizationException {

        BulkActionToPerform bulkActionToPerform = doApullRequestAction();

        when(mockRemoteGitHub.createPullRequest(eq(REPO_FULL_NAME), any(PullRequestToCreate.class), anyString())).thenReturn(fakePullRequest);

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", branchNameToCreateForPR)).thenReturn(fakeResourceContentBeforeUpdate);

        actionToPerformService.perform(bulkActionToPerform);

        assertContentHasBeenUpdatedOnBranch(branchNameToCreateForPR);

        verify(mockRemoteGitHub, times(1)).createPullRequest(eq(REPO_FULL_NAME), newPrCaptor.capture(),anyString());

        assertPullRequestHasBeenCreated(bulkActionToPerform.getCommitMessage());

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_OK_WITH_PR_CREATED);

    }

    @Test
    public void continueIfResourceDoesntExist_whenActionPermitsIt() throws GitHubAuthorizationException {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();

        ResourceContent nonExistingResourceContent = new ResourceContent();

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", "master")).thenReturn(nonExistingResourceContent);

        actionToPerformService.perform(bulkActionToPerform);

        assertContentHasBeenUpdatedOnBranch("master");

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_OK);
    }

    @Test
    public void continueIfExistingResourceisNull_whenActionPermitsIt() throws GitHubAuthorizationException {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", "master")).thenReturn(null);

        actionToPerformService.perform(bulkActionToPerform);

        assertContentHasBeenUpdatedOnBranch("master");

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_OK);
    }

    @Test
    public void dontDoAnythingIfResourceDoesntExist_whenActionDoesntAllowIt_for_DIRECT_PUSH() throws GitHubAuthorizationException {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();

        testActionToPerform.setContinueIfResourceDoesntExist(false);

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", "master")).thenReturn(null);

        actionToPerformService.perform(bulkActionToPerform);

        verify(mockRemoteGitHub, never()).updateContent(anyString(), anyString(), any(DirectCommit.class), anyString());

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_KO_FILE_DOESNT_EXIST);
    }

    @Test
    public void dontDoAnythingIfResourceDoesntExist_whenActionDoesntAllowIt_for_PULL_REQUEST()
            throws BranchAlreadyExistsException, GitHubAuthorizationException {

        BulkActionToPerform bulkActionToPerform = doApullRequestAction();
        testActionToPerform.setContinueIfResourceDoesntExist(false);

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", "masterbranch")).thenReturn(null);

        actionToPerformService.perform(bulkActionToPerform);

        verify(mockRemoteGitHub, never()).updateContent(anyString(), anyString(), any(DirectCommit.class), anyString());

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_KO_FILE_DOESNT_EXIST);

    }

    @Test
    public void dontDoAnythingIfProblemWhileComputingContent() {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();

        testActionToPerform.setThrowIssueProvidingContentException(true);

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", "master")).thenReturn(fakeResourceContentBeforeUpdate);

        actionToPerformService.perform(bulkActionToPerform);

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_KO_CANT_PROVIDE_CONTENT_ISSUE);

    }

    @Test
    public void reuseBranchIfAlreadyExistWhenDoing_PULL_REQUEST() throws BranchAlreadyExistsException, GitHubAuthorizationException {

        BulkActionToPerform bulkActionToPerform = doApullRequestAction();

        when(mockRemoteGitHub.fetchHeadReferenceFrom(REPO_FULL_NAME, branchNameToCreateForPR)).thenReturn(fakeBranchCreatedReference);
        when(mockRemoteGitHub.createBranch(REPO_FULL_NAME, branchNameToCreateForPR, sha1ForHeadOnMaster, SOME_OAUTH_TOKEN))
                .thenThrow(new BranchAlreadyExistsException("branch already exists"));

        when(mockRemoteGitHub.createPullRequest(eq(REPO_FULL_NAME), any(PullRequestToCreate.class), anyString())).thenReturn(fakePullRequest);

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", branchNameToCreateForPR)).thenReturn(fakeResourceContentBeforeUpdate);

        actionToPerformService.perform(bulkActionToPerform);

        assertContentHasBeenUpdatedOnBranch(branchNameToCreateForPR);

        verify(mockRemoteGitHub, times(1)).createPullRequest(eq(REPO_FULL_NAME), newPrCaptor.capture(), anyString());

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_OK_WITH_PR_CREATED);

        assertPullRequestHasBeenCreated(bulkActionToPerform.getCommitMessage());

    }

    @Test
    public void notifyProperlyWhenIncorrectCredentials_whenCommitting() throws GitHubAuthorizationException {

        when(mockRemoteGitHub.updateContent(eq(REPO_FULL_NAME), anyString(), any(DirectCommit.class), anyString()))
                .thenThrow(new GitHubAuthorizationException("invalid credentials"));

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();

        when(mockRemoteGitHub.fetchContent(REPO_FULL_NAME, "someFile.txt", "master")).thenReturn(fakeResourceContentBeforeUpdate);

        actionToPerformService.perform(bulkActionToPerform);

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_KO_AUTHENTICATION_ISSUE);
    }


    @Test
    public void notifyProperlyWhenIncorrectCredentials_whenCreatingBranch() throws GitHubAuthorizationException, BranchAlreadyExistsException {

        BulkActionToPerform bulkActionToPerform = doApullRequestAction();

        when(mockRemoteGitHub.createBranch(eq(REPO_FULL_NAME), anyString(), anyString(), anyString()))
                .thenThrow(new GitHubAuthorizationException("invalid credentials"));

        actionToPerformService.perform(bulkActionToPerform);

        verify(mockActionNotificationService, times(1)).handleNotificationsFor(eq(bulkActionToPerform),
                eq(resourceToUpdate),
                updatedResourceCaptor.capture());

        assertThat(updatedResourceCaptor.getValue().getUpdateStatus()).isEqualTo(UPDATE_KO_AUTHENTICATION_ISSUE);
    }

    private BulkActionToPerform doApullRequestAction() throws BranchAlreadyExistsException, GitHubAuthorizationException {

        mockPullRequestSpecificBehavior();

        return bulkActionToPerformBuilder.gitHubInteraction(new PullRequestGitHubInteraction(branchNameToCreateForPR)).build();
    }

    private void assertPullRequestHasBeenCreated(String expectedCommitMessage) {
        PullRequestToCreate actualPrToCreate = newPrCaptor.getValue();
        assertThat(actualPrToCreate.getBase()).isEqualTo("masterBranch");
        assertThat(actualPrToCreate.getHead()).isEqualTo(branchNameToCreateForPR);
        assertThat(actualPrToCreate.getTitle()).isEqualTo(expectedCommitMessage);

        assertThat(actualPrToCreate.getBody()).startsWith("performed on behalf of someUserName by CI-droid");
        assertThat(actualPrToCreate.getBody()).endsWith(expectedCommitMessage);
    }

    private void assertContentHasBeenUpdatedOnBranch(String branchName) throws GitHubAuthorizationException {

        verify(mockRemoteGitHub, times(1)).updateContent(eq(REPO_FULL_NAME),
                eq("someFile.txt"),
                directCommitCaptor.capture(),
                eq(SOME_OAUTH_TOKEN));

        DirectCommit actualCommit = directCommitCaptor.getValue();

        assertThat(actualCommit.getBranch()).isEqualTo(branchName);
        assertThat(actualCommit.getCommitMessage()).isEqualTo(SOME_COMMIT_MESSAGE + " performed on behalf of someUserName by CI-droid");
        assertThat(actualCommit.getCommitter().getEmail()).isEqualTo(SOME_EMAIL);
        assertThat(actualCommit.getCommitter().getName()).isEqualTo(SOME_USER_NAME);

        String expectedEncodedContent = new String(Base64.getEncoder().encode(MODIFIED_CONTENT.getBytes()));
        assertThat(actualCommit.getBase64EncodedContent()).isEqualTo(expectedEncodedContent);

    }

    private void mockPullRequestSpecificBehavior() throws BranchAlreadyExistsException, GitHubAuthorizationException {
        when(mockRemoteGitHub.fetchRepository(REPO_FULL_NAME)).thenReturn(fakeRepository);
        when(mockRemoteGitHub.fetchHeadReferenceFrom(REPO_FULL_NAME, "masterBranch")).thenReturn(fakeHeadOnMasterReference);
        when(mockRemoteGitHub.createBranch(REPO_FULL_NAME, branchNameToCreateForPR, sha1ForHeadOnMaster, SOME_OAUTH_TOKEN))
                .thenReturn(fakeBranchCreatedReference);
    }

}