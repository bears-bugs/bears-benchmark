package com.societegenerale.cidroid.tasks.consumer.autoconfigure;

import com.societegenerale.cidroid.tasks.consumer.infrastructure.GitRebaser;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.GitWrapper;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.IncomingGitHubEvent;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.RestTemplateResourceFetcher;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.config.CiDroidBehavior;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.config.InfraConfig;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.notifiers.EMailNotifier;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.notifiers.GitHubPullRequestCommentNotifier;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.notifiers.HttpNotifier;
import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.*;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.MailSender;

import java.util.List;

@Configuration
@EnableBinding(IncomingGitHubEvent.class)
@Import({InfraConfig.class})
public class CiDroidTasksConsumerAutoConfiguration {

    @Bean
    public CiDroidBehavior ciDroidBehavior(){
        return new CiDroidBehavior();
    }


    @Bean
    @ConditionalOnProperty(value = "ciDroidBehavior.notifyOwnerForNonMergeablePr.enabled", havingValue = "true")
    public PushEventOnDefaultBranchHandler notificationHandler(RemoteGitHub gitHub, List<Notifier> notifiers) {
        return new NotificationsHandler(gitHub, notifiers);
    }

    @Bean
    @ConditionalOnProperty(value = "ciDroidBehavior.tryToRebaseOpenPrs.enabled", havingValue = "true")
    public PushEventOnDefaultBranchHandler rebaseHandler(RemoteGitHub gitHub, @Value("${gitHub.login}") String gitLogin,
            @Value("${gitHub.password}") String gitPassword) {

        return new RebaseHandler(new GitRebaser(gitLogin, gitPassword, new GitWrapper()), gitHub);
    }

    @Bean
    @ConditionalOnProperty(value = "ciDroidBehavior.bestPracticeNotifier.enabled", havingValue = "true")
    public PullRequestEventHandler bestPracticeNotifierHandler(CiDroidBehavior ciDroidBehavior, List<Notifier> notifiers,
            RemoteGitHub remoteGitHub) {

        return new BestPracticeNotifierHandler(ciDroidBehavior.getPatternToResourceMapping(), notifiers, remoteGitHub,
                new RestTemplateResourceFetcher());

    }

    @Bean
    @ConditionalOnProperty(prefix = "notifiers", value = "github.prComment.enable", havingValue = "true")
    public Notifier GitHubCommentOnPRnotifier(RemoteGitHub gitHub) {

        return new GitHubPullRequestCommentNotifier(gitHub);
    }

    @Bean
    @ConditionalOnProperty(prefix = "notifiers", value = "email.enable", havingValue = "true")
    public Notifier emailNotifier(MailSender javaMailSender, @Value("${spring.mail.sender}") String mailSender) {

        return new EMailNotifier(javaMailSender, mailSender);
    }

    @Bean
    @ConditionalOnProperty(prefix = "notifiers", value = "http.targetUrl")
    public Notifier httpNotifier(@Value("${notifiers.http.targetUrl}") String targetUrl) {

        return new HttpNotifier(targetUrl);
    }

}
