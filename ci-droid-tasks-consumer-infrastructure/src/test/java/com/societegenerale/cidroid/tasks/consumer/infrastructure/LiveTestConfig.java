package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.tasks.consumer.infrastructure.mocks.NotifierMock;
import com.societegenerale.cidroid.tasks.consumer.services.Rebaser;
import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.PushEventOnDefaultBranchHandler;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.RebaseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class LiveTestConfig {

    @Bean
    public Rebaser mockRebaser() {
        return mock(Rebaser.class);
    }

    @Bean
    public NotifierMock mockNotifier() {
        return new NotifierMock();
    }

    @Bean
    public Rebaser rebaser(@Value("${gitHub.login}") String gitLogin, @Value("${gitHub.password}") String gitPassword) {
        return new GitRebaser(gitLogin, gitPassword, new GitWrapper());
    }

    @Bean
    public PushEventOnDefaultBranchHandler rebaseHandler(Rebaser rebaser, RemoteGitHub remoteGitHub) {

        return new RebaseHandler(rebaser, remoteGitHub);
    }

//    @Bean
//    @Primary
//    public ActionNotificationService actionNotificationService() {
//
//        return mock(ActionNotificationService.class);
//
//    }
//
//    @Bean
//    @Primary
//    public PullRequestEventHandler pullRequestEventHandler() {
//
//        return mock(PullRequestEventHandler.class);
//    }

}