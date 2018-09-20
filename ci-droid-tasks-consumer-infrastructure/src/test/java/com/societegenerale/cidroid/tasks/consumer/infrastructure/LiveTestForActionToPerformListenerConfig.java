package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.tasks.consumer.infrastructure.mocks.NotifierMock;
import com.societegenerale.cidroid.tasks.consumer.services.Rebaser;
import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.PullRequestEventHandler;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.PushEventOnDefaultBranchHandler;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.RebaseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
public class LiveTestForActionToPerformListenerConfig {

    @Bean
    public Rebaser mockRebaser() {
        return mock(Rebaser.class);
    }

    @Bean
    public NotifierMock mockNotifier() {
        return new NotifierMock();
    }


//    @Bean
//    @Primary
//    public ActionNotificationService actionNotificationService() {
//
//        return mock(ActionNotificationService.class);
//
//    }
//
    @Bean
    @Primary
    public PullRequestEventHandler pullRequestEventHandler() {

        return mock(PullRequestEventHandler.class);
    }

}