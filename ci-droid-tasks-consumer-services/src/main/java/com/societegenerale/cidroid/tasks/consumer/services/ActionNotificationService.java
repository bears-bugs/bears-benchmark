package com.societegenerale.cidroid.tasks.consumer.services;


import com.societegenerale.cidroid.api.ResourceToUpdate;
import com.societegenerale.cidroid.api.gitHubInteractions.DirectPushGitHubInteraction;
import com.societegenerale.cidroid.api.gitHubInteractions.PullRequestGitHubInteraction;
import com.societegenerale.cidroid.tasks.consumer.services.model.BulkActionToPerform;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.UpdatedResource;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.ActionNotifier;

import static com.societegenerale.cidroid.tasks.consumer.services.model.github.UpdatedResource.UpdateStatus.*;

public class ActionNotificationService {

    private final ActionNotifier notifier;

    public ActionNotificationService(ActionNotifier notifier) {
        this.notifier = notifier;
    }

    public void handleNotificationsFor(BulkActionToPerform action, ResourceToUpdate resourceToUpdate, UpdatedResource updatedResource) {

        String repoFullName = resourceToUpdate.getRepoFullName();

        User user = new User(action.getGitLogin(), action.getEmail());

        String notificationSubject = "Action '" + action.getActionType() + "' for " + resourceToUpdate.getFilePathOnRepo() + " on " +
                repoFullName + " on branch " + resourceToUpdate.getBranchName();

        if(updatedResource.getUpdateStatus()==UPDATE_KO_AUTHENTICATION_ISSUE){
            notifier.notify(user,
                    "[KO] " + notificationSubject,
                    "Content hasn't been modified on repository, as we haven't been able to commit content due to an authorization issue. please double check the credentials you provided");
        }

        //TODO refactor, as code in manageDirectPush and managePullRequest is very similar
        else if (action.getGitHubInteraction() instanceof DirectPushGitHubInteraction) {

            manageDirectPush(updatedResource, user, notificationSubject);

        } else if (action.getGitHubInteraction() instanceof PullRequestGitHubInteraction) {

            managePullRequest(updatedResource, user, notificationSubject);
        }

    }

    private void managePullRequest(UpdatedResource updatedResource, User user, String notificationSubject) {

        if (updatedResource.hasBeenUpdated()) {

            if (updatedResource.getUpdateStatus().equals(UPDATE_OK_WITH_PR_CREATED)) {

                notifier.notify(user,
                        "[OK] " + notificationSubject,
                        "CI-droid has created a PR on your behalf.\n you can double check its content here : " +
                                updatedResource.getContent().getHtmlUrl());
            }
            if (updatedResource.getUpdateStatus().equals(UPDATE_OK_BUT_PR_CREATION_KO)) {
                //TODO
            }

        } else if (updatedResource.getUpdateStatus().equals(UPDATE_KO_FILE_CONTENT_IS_SAME)) {

            StringBuilder notificationContent = new StringBuilder(
                    "Content hasn't been modified on repository, as the new file content is the same as the previous one\n");
            notificationContent.append("We haven't created a PR, but we may have created the branch though\n\n");
            notificationContent.append("you can double check its content here : " + updatedResource.getContent().getHtmlUrl());

            notifier.notify(user,
                    "[KO] " + notificationSubject,
                    notificationContent.toString());
        } else if (updatedResource.getUpdateStatus().equals(UPDATE_KO_FILE_DOESNT_EXIST)) {

            StringBuilder notificationContent = new StringBuilder(
                    "Content hasn't been modified on repository, as the file to update doesn't exist\n");
            notificationContent.append("We haven't created a PR, but we may have created the branch though\n\n");

            notifier.notify(user,
                    "[KO] " + notificationSubject,
                    notificationContent.toString());
        }
    }

    private void manageDirectPush(UpdatedResource updatedResource, User user, String notificationSubject) {
        if (updatedResource.hasBeenUpdated()) {

            notifier.notify(user,
                    "[OK] " + notificationSubject,
                    "CI-droid has updated the resource on your behalf.\n Link to the version we committed : " +
                            updatedResource.getContent().getHtmlUrl());
        } else if (updatedResource.getUpdateStatus().equals(UPDATE_KO_FILE_CONTENT_IS_SAME)) {

            StringBuilder notificationContent = new StringBuilder(
                    "Content hasn't been modified on repository, as the new file content is the same as the previous one\n");
            notificationContent.append("you can double check its content here : " + updatedResource.getContent().getHtmlUrl());

            notifier.notify(user,
                    "[KO] " + notificationSubject,
                    notificationContent.toString());
        } else if (updatedResource.getUpdateStatus().equals(UPDATE_KO_FILE_DOESNT_EXIST)) {

            StringBuilder notificationContent = new StringBuilder(
                    "Content hasn't been modified on repository, as the file to update doesn't exist\n");

            notifier.notify(user,
                    "[KO] " + notificationSubject,
                    notificationContent.toString());
        }
    }
}
