package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.api.gitHubInteractions.PullRequestGitHubInteraction;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionToPerformCommandTest {

    @Test
    public void shouldMapPullRequestGitHubInteraction() throws IOException {

        String incomingCommandAsString = IOUtils
                .toString(ActionToPerformCommandTest.class.getClassLoader()
                                .getResourceAsStream("incomingOverWriteStaticContentAction_withPullRequestInteraction.json"),
                        "UTF-8");

        ActionToPerformCommand incomingCommand = new ObjectMapper().readValue(incomingCommandAsString, ActionToPerformCommand.class);

        assertThat(incomingCommand.getGitHubInteractionType()).isInstanceOf(PullRequestGitHubInteraction.class);

        PullRequestGitHubInteraction githubInteraction = (PullRequestGitHubInteraction) incomingCommand.getGitHubInteractionType();

        assertThat(githubInteraction.getBranchNameToCreate()).isEqualTo("someBranchName");

    }

}