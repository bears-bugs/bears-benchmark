package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.api.ResourceToUpdate;
import com.societegenerale.cidroid.api.gitHubInteractions.AbstractGitHubInteraction;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString(exclude = "gitHubOauthToken")
@Slf4j
public class ActionToPerformCommand {

    @NotEmpty
    private String gitLogin;

    @NotEmpty
    private String gitHubOauthToken;

    @Email
    private String email;

    @NotEmpty
    private String commitMessage;

    @NotNull
    private Object updateAction;

    @NotNull
    private AbstractGitHubInteraction gitHubInteractionType;

    @NotEmpty
    private List<ResourceToUpdate> resourcesToUpdate;

}
