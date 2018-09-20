package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DirectCommit {

    private String branch;

    @JsonProperty("message")
    private String commitMessage;

    @JsonProperty("content")
    private String base64EncodedContent;

    @JsonProperty("sha")
    private String previousVersionSha1;

    private Committer committer;

    @Data
    @AllArgsConstructor
    public static class Committer {

        private String name;

        private String email;

    }
}
