package com.societegenerale.cidroid.tasks.consumer.services;

import com.societegenerale.cidroid.api.ResourceToUpdate;
import com.societegenerale.cidroid.api.gitHubInteractions.DirectPushGitHubInteraction;
import com.societegenerale.cidroid.api.gitHubInteractions.PullRequestGitHubInteraction;
import com.societegenerale.cidroid.tasks.consumer.services.model.BulkActionToPerform;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.UpdatedResource;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.ActionNotifier;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static com.societegenerale.cidroid.tasks.consumer.services.model.github.UpdatedResource.UpdateStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ActionNotificationServiceTest {

    private static final String MODIFIED_CONTENT = "modifiedContent";

    private static final String REPO_FULL_NAME = "repoFullName";

    private final String SOME_USER_NAME = "someUserName";

    private final String SOME_OAUTH_TOKEN = "123456789abcdef";

    private final String SOME_EMAIL = "someEmail@someDomain.com";

    private final String SOME_COMMIT_MESSAGE = "this is the original commit message by the user";

    private ArgumentCaptor<String> notificationContentCaptor = ArgumentCaptor.forClass(String.class);


    private TestActionToPerform testActionToPerform = new TestActionToPerform();

    private ResourceToUpdate resourceToUpdate = new ResourceToUpdate(REPO_FULL_NAME, "someFile.txt", "master", StringUtils.EMPTY);

    private UpdatedResource updatedResource;

    private ActionNotifier mockNotifier = mock(ActionNotifier.class);

    private ActionNotificationService actionNotificationService = new ActionNotificationService(mockNotifier);

    private User expectedUser = new User(SOME_USER_NAME, SOME_EMAIL);

    private BulkActionToPerform.BulkActionToPerformBuilder bulkActionToPerformBuilder;

    @Before
    public void setup() {
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

        updatedResource = UpdatedResource.builder().content(content).build();

    }

    @Test
    public void when_DIRECT_PUSH_and_update_OK() {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();
        updatedResource.setUpdateStatus(UPDATE_OK);

        actionNotificationService.handleNotificationsFor(bulkActionToPerform, resourceToUpdate, updatedResource);

        assertNotificationWhenDirectPush();
    }

    @Test
    public void when_PULL_REQUEST_and_update_OK() {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new PullRequestGitHubInteraction()).build();

        updatedResource.setUpdateStatus(UPDATE_OK_WITH_PR_CREATED);

        actionNotificationService.handleNotificationsFor(bulkActionToPerform, resourceToUpdate, updatedResource);

        assertNotificationWhenPR();
    }

    @Test
    public void when_DIRECT_PUSH_and_content_is_same() {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();
        updatedResource.setUpdateStatus(UPDATE_KO_FILE_CONTENT_IS_SAME);

        actionNotificationService.handleNotificationsFor(bulkActionToPerform, resourceToUpdate, updatedResource);

        String expectedSubject =
                "[KO] Action '" + testActionToPerform.getClass().getName() + "' for someFile.txt on repoFullName on branch master";

        verify(mockNotifier, times(1)).notify(eq(expectedUser), eq(expectedSubject), notificationContentCaptor.capture());

        assertThat(notificationContentCaptor.getValue())
                .startsWith("Content hasn't been modified on repository, as the new file content is the same as the previous one");
        assertThat(notificationContentCaptor.getValue()).contains("you can double check its content here : http:");

    }

    @Test
    public void when_PULL_REQUEST_and_content_is_same() {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new PullRequestGitHubInteraction()).build();

        updatedResource.setUpdateStatus(UPDATE_KO_FILE_CONTENT_IS_SAME);

        actionNotificationService.handleNotificationsFor(bulkActionToPerform, resourceToUpdate, updatedResource);

        String expectedSubject =
                "[KO] Action '" + testActionToPerform.getClass().getName() + "' for someFile.txt on repoFullName on branch master";

        verify(mockNotifier, times(1)).notify(eq(expectedUser), eq(expectedSubject), notificationContentCaptor.capture());

        assertThat(notificationContentCaptor.getValue())
                .startsWith("Content hasn't been modified on repository, as the new file content is the same as the previous one");

        assertThat(notificationContentCaptor.getValue()).contains("We haven't created a PR, but we may have created the branch though");

        assertThat(notificationContentCaptor.getValue()).contains("you can double check its content here : http:");

    }

    @Test
    public void dontDoAnythingIfResourceDoesntExist_whenActionDoesntAllowIt_for_DIRECT_PUSH() {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();
        updatedResource.setUpdateStatus(UPDATE_KO_FILE_DOESNT_EXIST);

        actionNotificationService.handleNotificationsFor(bulkActionToPerform, resourceToUpdate, updatedResource);

        String expectedSubject =
                "[KO] Action '" + testActionToPerform.getClass().getName() + "' for someFile.txt on repoFullName on branch master";

        verify(mockNotifier, times(1)).notify(eq(expectedUser), eq(expectedSubject), notificationContentCaptor.capture());

        assertThat(notificationContentCaptor.getValue())
                .startsWith("Content hasn't been modified on repository, as the file to update doesn't exist");

    }

    @Test
    public void dontDoAnythingIfResourceDoesntExist_whenActionDoesntAllowIt_for_PULL_REQUEST() {

        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new PullRequestGitHubInteraction()).build();

        updatedResource.setUpdateStatus(UPDATE_KO_FILE_DOESNT_EXIST);

        actionNotificationService.handleNotificationsFor(bulkActionToPerform, resourceToUpdate, updatedResource);

        String expectedSubject =
                "[KO] Action '" + testActionToPerform.getClass().getName() + "' for someFile.txt on repoFullName on branch master";

        verify(mockNotifier, times(1)).notify(eq(expectedUser), eq(expectedSubject), notificationContentCaptor.capture());

        assertThat(notificationContentCaptor.getValue())
                .startsWith("Content hasn't been modified on repository, as the file to update doesn't exist");


        assertThat(notificationContentCaptor.getValue()).contains("We haven't created a PR, but we may have created the branch though");

    }

    @Test
    public void sendKOnotification_whenAuthenticationIssue() {

        String expectedSubject = "[KO] Action '" + testActionToPerform.getClass().getName() + "' for someFile.txt on repoFullName on branch master";

        String expectedContent =
                "Content hasn't been modified on repository, as we haven't been able to commit content due to an authorization issue. please double check the credentials you provided";

        //for direct push
        BulkActionToPerform bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new DirectPushGitHubInteraction()).build();
        updatedResource.setUpdateStatus(UPDATE_KO_AUTHENTICATION_ISSUE);

        actionNotificationService.handleNotificationsFor(bulkActionToPerform, resourceToUpdate, updatedResource);

        //for pull request
        reset(mockNotifier);
        bulkActionToPerform = bulkActionToPerformBuilder.gitHubInteraction(new PullRequestGitHubInteraction()).build();
        updatedResource.setUpdateStatus(UPDATE_KO_AUTHENTICATION_ISSUE);

        actionNotificationService.handleNotificationsFor(bulkActionToPerform, resourceToUpdate, updatedResource);

        verify(mockNotifier, times(1)).notify(eq(expectedUser), eq(expectedSubject), notificationContentCaptor.capture());
        assertThat(notificationContentCaptor.getValue())
                .isEqualTo(expectedContent);

    }

    private void assertNotificationWhenDirectPush() {
        assertNotificationContent("CI-droid has updated the resource on your behalf", "Link to the version we committed : http://");
    }

    private void assertNotificationWhenPR() {
        assertNotificationContent("CI-droid has created a PR on your behalf", "you can double check its content here : http:");
    }

    private void assertNotificationContent(String startsWith, String content) {

        String expectedSubject =
                "[OK] Action '" + testActionToPerform.getClass().getName() + "' for someFile.txt on repoFullName on branch master";

        verify(mockNotifier, times(1)).notify(eq(expectedUser), eq(expectedSubject), notificationContentCaptor.capture());

        assertThat(notificationContentCaptor.getValue()).startsWith(startsWith);
        assertThat(notificationContentCaptor.getValue()).contains(content);

    }

}