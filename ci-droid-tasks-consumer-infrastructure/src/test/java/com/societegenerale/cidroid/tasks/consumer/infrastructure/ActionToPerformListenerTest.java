package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.api.actionToReplicate.ActionToReplicate;
import com.societegenerale.cidroid.api.gitHubInteractions.DirectPushGitHubInteraction;
import com.societegenerale.cidroid.extensions.actionToReplicate.OverwriteStaticFileAction;
import com.societegenerale.cidroid.tasks.consumer.services.ActionToPerformService;
import com.societegenerale.cidroid.tasks.consumer.services.model.BulkActionToPerform;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ActionToPerformListenerTest {

    private ActionToReplicate overWriteStaticContentAction = new OverwriteStaticFileAction();

    private ActionToReplicate mockSomeOtherAction = mock(ActionToReplicate.class);

    private ActionToPerformService mockActionToPerformService = mock(ActionToPerformService.class);

    private ActionToPerformListener actionToPerformListener = new ActionToPerformListener(mockActionToPerformService, Arrays.asList(
            overWriteStaticContentAction, mockSomeOtherAction));

    private ArgumentCaptor<BulkActionToPerform> bulkActionToPerformCaptor = ArgumentCaptor.forClass(BulkActionToPerform.class);

    private ActionToPerformCommand incomingCommand;

    @Before
    public void setup() throws IOException {

        String incomingCommandAsString = IOUtils
                .toString(ActionToPerformListenerTest.class.getClassLoader().getResourceAsStream("incomingOverWriteStaticContentAction.json"),
                        "UTF-8");
        incomingCommand = new ObjectMapper().readValue(incomingCommandAsString, ActionToPerformCommand.class);

    }

    @Test
    public void shouldMapCorrectlyIncomingCommand() {
        //postConstruct
        actionToPerformListener.registerActionsToReplicate();

        actionToPerformListener.onActionToPerform(incomingCommand);

        verify(mockActionToPerformService, times(1)).perform(bulkActionToPerformCaptor.capture());

        BulkActionToPerform actualBulkActionToPerform = bulkActionToPerformCaptor.getValue();

        assertThat(actualBulkActionToPerform.getGitLogin()).isEqualTo("someUserName");
        assertThat(actualBulkActionToPerform.getGitHubOauthToken()).isEqualTo("somePassword");
        assertThat(actualBulkActionToPerform.getEmail()).isEqualTo("someEmail@someDomain.com");

        assertThat(actualBulkActionToPerform.getActionToReplicate()).isInstanceOf(OverwriteStaticFileAction.class);
        OverwriteStaticFileAction actualActionToReplicate = (OverwriteStaticFileAction) actualBulkActionToPerform.getActionToReplicate();
        assertThat(actualActionToReplicate.getStaticContent()).isEqualTo("some new content");

        assertThat(actualBulkActionToPerform.getGitHubInteraction()).isInstanceOf(DirectPushGitHubInteraction.class);
    }


    @Test
    public void shouldFailSilentlyIfNoMatchingTypeRegistered() {

        //only one action registered, not matching the one
        actionToPerformListener = new ActionToPerformListener(mockActionToPerformService, Arrays.asList(mockSomeOtherAction));

        actionToPerformListener.registerActionsToReplicate();
        actionToPerformListener.onActionToPerform(incomingCommand);

        verify(mockActionToPerformService, never()).perform(bulkActionToPerformCaptor.capture());
    }

}