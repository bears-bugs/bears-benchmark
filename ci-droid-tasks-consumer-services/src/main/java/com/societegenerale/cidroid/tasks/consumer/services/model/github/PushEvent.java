package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.societegenerale.cidroid.tasks.consumer.services.model.GitHubEvent;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushEvent implements GitHubEvent {

    private String ref;

    private Repository repository;

    @JsonProperty("head_commit")
    private Commit headCommit;

    @Override
    public String getRepositoryUrl() {
        return getRepository().getUrl();
    }

    public boolean happenedOnDefaultBranch(){
        return ref.endsWith(getRepository().getDefaultBranch());
    }
}
