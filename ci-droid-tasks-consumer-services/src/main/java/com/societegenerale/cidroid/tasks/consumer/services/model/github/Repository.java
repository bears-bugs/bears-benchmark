package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {

    private String url;

    @JsonProperty("default_branch")
    private String defaultBranch;

    @JsonProperty("full_name")
    private String fullName;

    private String name;

    private boolean fork;

    @JsonProperty("clone_url")
    private String cloneUrl;
}
