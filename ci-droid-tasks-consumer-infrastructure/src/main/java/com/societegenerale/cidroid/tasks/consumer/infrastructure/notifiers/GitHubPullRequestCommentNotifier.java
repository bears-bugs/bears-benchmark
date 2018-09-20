package com.societegenerale.cidroid.tasks.consumer.infrastructure.notifiers;

import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.Comment;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class GitHubPullRequestCommentNotifier implements Notifier {

    private RemoteGitHub remoteGitHub;

    public GitHubPullRequestCommentNotifier(RemoteGitHub remoteGitHub) {
        this.remoteGitHub = remoteGitHub;

    }

    @Override
    public void notify(User user, Message message,Map<String,Object> additionalInfos) {

        PullRequest pr=(PullRequest)additionalInfos.get(PULL_REQUEST);

        remoteGitHub.addCommentDescribingRebase(pr.getRepo().getFullName(),pr.getNumber(),new Comment(message.getContent()));

    }


    @Override
    public String getNotificationMode() {
        return "GithubPrComment";
    }

}
