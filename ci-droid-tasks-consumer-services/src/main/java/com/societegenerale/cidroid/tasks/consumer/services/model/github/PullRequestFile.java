package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestFile {

    private String sha;

    private String filename;

    private String status;

    private int additions;

    private int deletions;

    private int changes;

}
