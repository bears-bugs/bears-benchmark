package com.societegenerale.cidroid.tasks.consumer.services.model;

import com.societegenerale.cidroid.api.ResourceToUpdate;
import com.societegenerale.cidroid.api.actionToReplicate.ActionToReplicate;
import com.societegenerale.cidroid.api.gitHubInteractions.AbstractGitHubInteraction;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString(exclude = "gitHubOauthToken")
@Builder
public class BulkActionToPerform {

    @NotEmpty
    protected String gitLogin;

    @NotEmpty
    private String gitHubOauthToken;

    @Email
    private String email;

    @NotEmpty
    private String commitMessage;

    @NotNull
    private AbstractGitHubInteraction gitHubInteraction;

    @NotEmpty
    private List<ResourceToUpdate> resourcesToUpdate;

    @NotNull
    private ActionToReplicate actionToReplicate;

    public String getActionType() {
        return actionToReplicate.getClass().getName();
    }

}
