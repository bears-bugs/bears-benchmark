package com.societegenerale.cidroid.tasks.consumer.services;

import com.societegenerale.cidroid.api.IssueProvidingContentException;
import com.societegenerale.cidroid.api.ResourceToUpdate;
import com.societegenerale.cidroid.api.actionToReplicate.ActionToReplicate;
import com.societegenerale.cidroid.api.gitHubInteractions.DirectPushGitHubInteraction;
import com.societegenerale.cidroid.api.gitHubInteractions.PullRequestGitHubInteraction;
import com.societegenerale.cidroid.tasks.consumer.services.exceptions.BranchAlreadyExistsException;
import com.societegenerale.cidroid.tasks.consumer.services.exceptions.GitHubAuthorizationException;
import com.societegenerale.cidroid.tasks.consumer.services.model.BulkActionToPerform;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ActionToPerformService {

    private RemoteGitHub remoteGitHub;

    private ActionNotificationService actionNotificationService;

    public ActionToPerformService(RemoteGitHub remoteGitHub, ActionNotificationService actionNotificationService) {
        this.remoteGitHub = remoteGitHub;
        this.actionNotificationService = actionNotificationService;
    }

    public void perform(BulkActionToPerform action) {

        //we're supposed to have only one element in list, but this may change in the future : we'll loop over them.
        ResourceToUpdate resourceToUpdate = action.getResourcesToUpdate().get(0);

        String repoFullName = resourceToUpdate.getRepoFullName();

        try {

            if (action.getGitHubInteraction() instanceof DirectPushGitHubInteraction) {

                UpdatedResource updatedResource = updateRemoteResource(repoFullName, resourceToUpdate, action, resourceToUpdate.getBranchName());

                actionNotificationService.handleNotificationsFor(action, resourceToUpdate, updatedResource);

            } else if (action.getGitHubInteraction() instanceof PullRequestGitHubInteraction) {

                PullRequestGitHubInteraction pullRequestAction = (PullRequestGitHubInteraction) action.getGitHubInteraction();

                Repository impactedRepo = remoteGitHub.fetchRepository(repoFullName);
                String branchNameForPR = pullRequestAction.getBranchNameToCreate();

                Reference masterCommitReference = remoteGitHub.fetchHeadReferenceFrom(repoFullName, impactedRepo.getDefaultBranch());

                Reference branchToUseForPr = null;

                try {
                    branchToUseForPr = remoteGitHub.createBranch(repoFullName, branchNameForPR, masterCommitReference.getObject().getSha(),
                            action.getGitHubOauthToken());
                } catch (BranchAlreadyExistsException e) {

                    log.warn("branch " + branchNameForPR + " already exists");

                    //TODO maybe we should add field in Reference to identify when it hasn't been created as expected
                    branchToUseForPr = remoteGitHub.fetchHeadReferenceFrom(repoFullName, branchNameForPR);
                }

                UpdatedResource updatedResource;

                if (branchToUseForPr == null) {
                    //TODO test this scenario
                    updatedResource = UpdatedResource.notUpdatedResource(UpdatedResource.UpdateStatus.UPDATE_KO_BRANCH_CREATION_ISSUE);

                } else {
                    updatedResource = updateRemoteResource(repoFullName, resourceToUpdate, action, branchNameForPR);

                    if (updatedResource.hasBeenUpdated()) {

                        createPullRequest(action, impactedRepo, branchNameForPR, updatedResource);
                    }
                }

                actionNotificationService.handleNotificationsFor(action, resourceToUpdate, updatedResource);
            } else {
                log.warn("unknown gitHub interaction type : {}", action.getGitHubInteraction());
            }

        } catch (GitHubAuthorizationException e) {
            actionNotificationService.handleNotificationsFor(action, resourceToUpdate, UpdatedResource.notUpdatedResource(UpdatedResource.UpdateStatus.UPDATE_KO_AUTHENTICATION_ISSUE));
        }

    }

    private void createPullRequest(BulkActionToPerform action, Repository impactedRepo, String branchNameForPR, UpdatedResource updatedResource) {
        Optional<PullRequest> createdPr = createPrOnBranch(impactedRepo, branchNameForPR, action);

        if (createdPr.isPresent()) {
            updatedResource.getContent().setHtmlUrl(createdPr.get().getHtmlUrl());
            updatedResource.setUpdateStatus(UpdatedResource.UpdateStatus.UPDATE_OK_WITH_PR_CREATED);
        } else {
            //TODO test this scenario
            updatedResource.setUpdateStatus(UpdatedResource.UpdateStatus.UPDATE_OK_BUT_PR_CREATION_KO);
        }
    }

    private UpdatedResource updateRemoteResource(String repoFullName, ResourceToUpdate resourceToUpdate, BulkActionToPerform action,
            String onBranch) throws GitHubAuthorizationException {

        ResourceContent existingResourceContent = remoteGitHub
                .fetchContent(repoFullName, resourceToUpdate.getFilePathOnRepo(), onBranch);

        String decodedOriginalContent = null;
        String newContent = null;

        ActionToReplicate actionToReplicate = action.getActionToReplicate();

        try {
            if (existingResourceExists(existingResourceContent)) {
                decodedOriginalContent = GitHubContentBase64codec.decode(existingResourceContent.getBase64EncodedContent());
                newContent = actionToReplicate.provideContent(decodedOriginalContent);
            }
            else if (actionToReplicate.canContinueIfResourceDoesntExist()) {
                newContent = actionToReplicate.provideContent(null);
            }
            else {
                //existing resource doesnt exist and we should not continue

                log.info("{} NOT updated on repo {}, on branch {}, as it doesnt exist", resourceToUpdate.getFilePathOnRepo(),
                        repoFullName, onBranch);

                return UpdatedResource.notUpdatedResource(UpdatedResource.UpdateStatus.UPDATE_KO_FILE_DOESNT_EXIST);

            }
        } catch (IssueProvidingContentException e) {
            log.warn("problem while computing the new content", e);
            return UpdatedResource
                    .notUpdatedResource(UpdatedResource.UpdateStatus.UPDATE_KO_CANT_PROVIDE_CONTENT_ISSUE, existingResourceContent.getHtmlLink());
        }

        if (contentIsSame(decodedOriginalContent, newContent)) {
            log.info("{} NOT updated on repo {}, on branch {}, as new content is same as existing content", resourceToUpdate.getFilePathOnRepo(),
                    repoFullName, onBranch);

            return UpdatedResource
                    .notUpdatedResource(UpdatedResource.UpdateStatus.UPDATE_KO_FILE_CONTENT_IS_SAME, existingResourceContent.getHtmlLink());
        }

        UpdatedResource updatedResource = commitResource(action, newContent, resourceToUpdate, existingResourceContent, onBranch);

        logWhatHasBeenDone(repoFullName, resourceToUpdate, onBranch, existingResourceContent, decodedOriginalContent, updatedResource);

        return updatedResource;

    }

    private boolean existingResourceExists(ResourceContent existingResourceContent) {
        return existingResourceContent != null && existingResourceContent.getSha() != null;
    }

    private void logWhatHasBeenDone(String repoFullName, ResourceToUpdate resourceToUpdate, String onBranch, ResourceContent existingResourceContent,
            String decodedOriginalContent, UpdatedResource updatedResource) {

        if (resourceWasNotExisting(decodedOriginalContent)) {

            log.info("{} created on repo {}, on branch {}. SHA1: {}", resourceToUpdate.getFilePathOnRepo(), repoFullName, onBranch,
                    updatedResource.getCommit().getSha());
        } else {
            log.info("{} updated on repo {}, on branch {}. old SHA1: {} . new SHA1: {}", resourceToUpdate.getFilePathOnRepo(), repoFullName, onBranch,
                    updatedResource.getCommit().getSha(), existingResourceContent.getSha());
        }
    }

    private UpdatedResource commitResource(BulkActionToPerform action, String newContent, ResourceToUpdate resourceToUpdate,
            ResourceContent existingResourceContent, String onBranch) throws GitHubAuthorizationException {

        DirectCommit directCommit = new DirectCommit();

        if (existingResourceExists(existingResourceContent)) {
            directCommit.setPreviousVersionSha1(existingResourceContent.getSha());
        }

        directCommit.setBranch(onBranch);
        directCommit.setCommitter(new DirectCommit.Committer(action.getGitLogin(), action.getEmail()));
        directCommit.setCommitMessage(action.getCommitMessage() + " performed on behalf of " + action.getGitLogin() + " by CI-droid");

        directCommit.setBase64EncodedContent(GitHubContentBase64codec.encode(newContent));

        UpdatedResource updatedResource = remoteGitHub
                .updateContent(resourceToUpdate.getRepoFullName(), resourceToUpdate.getFilePathOnRepo(), directCommit,
                        action.getGitHubOauthToken());

        updatedResource.setUpdateStatus(UpdatedResource.UpdateStatus.UPDATE_OK);

        return updatedResource;
    }

    private boolean resourceWasNotExisting(String decodedOriginalContent) {

        return decodedOriginalContent == null;
    }

    private boolean contentIsSame(String decodedOriginalContent, String newContent) {
        return newContent != null && newContent.equals(decodedOriginalContent);
    }

    private Optional<PullRequest> createPrOnBranch(Repository impactedRepo, String branchName, BulkActionToPerform action) {

        PullRequestToCreate newPr = new PullRequestToCreate();
        newPr.setHead(branchName);
        newPr.setBase(impactedRepo.getDefaultBranch());

        PullRequestGitHubInteraction pullRequestGitHubInteraction=(PullRequestGitHubInteraction)action.getGitHubInteraction();

        String providedPrTitle=pullRequestGitHubInteraction.getPullRequestTitle();

        newPr.setTitle(providedPrTitle!=null ? providedPrTitle : branchName);
        newPr.setBody("performed on behalf of " + action.getGitLogin() + " by CI-droid\n\n" + action.getCommitMessage());

        try{
            return Optional.of(remoteGitHub.createPullRequest(impactedRepo.getFullName(), newPr, action.getGitHubOauthToken()));
        }
        catch(GitHubAuthorizationException e){
            log.warn("issue while creating the PR",e);
            return Optional.empty();
        }
    }

}
