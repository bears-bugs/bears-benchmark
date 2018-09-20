package com.societegenerale.cidroid.tasks.consumer.services.model;

import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import lombok.Data;

@Data
public class Message {

    private final String content;

    public static Message buildFromNotMergeablePR(PullRequest pr,PushEvent pushEvent){

        return new Message(String.format("PR %s is not mergeable following commit %s - please rebase the PR and fix conflicts if you want it to be merged",
                                            pr.getNumber(),
                                            pushEvent.getHeadCommit().getUrl()));
    }
}
