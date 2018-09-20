package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.tasks.consumer.infrastructure.mocks.GitHubMock;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.mocks.NotifierMock;
import com.societegenerale.cidroid.tasks.consumer.services.Rebaser;
import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.*;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    @Bean
    public Rebaser mockRebaser() {
        return mock(Rebaser.class);
    }

    @Bean
    public NotifierMock mockNotifier() {
        return new NotifierMock();
    }

    @Bean
    public GitHubMock mockGitHub() {
        return new GitHubMock();
    }

    @Bean
    public PushEventOnDefaultBranchHandler rebaseHandler(Rebaser rebaser,RemoteGitHub remoteGitHub){

        return new RebaseHandler(rebaser, remoteGitHub);
    }

    @Bean
    public PushEventOnDefaultBranchHandler notificationsHandler(RemoteGitHub remoteGitHub, NotifierMock notifierMock) {

        return new NotificationsHandler(remoteGitHub, Arrays.asList(notifierMock));
    }

    @Bean
    public PullRequestEventHandler bestPracticeNotifierHandler(List<Notifier> notifiers, RemoteGitHub remoteGitHub) {

        return new BestPracticeNotifierHandler(Collections.emptyMap(), notifiers, remoteGitHub, new RestTemplateResourceFetcher());

    }

    @Bean
    public MailSender mockMailSender() {

        return mock(MailSender.class);

    }

}
