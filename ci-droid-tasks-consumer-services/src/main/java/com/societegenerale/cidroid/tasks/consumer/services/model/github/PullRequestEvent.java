package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.societegenerale.cidroid.tasks.consumer.services.model.GitHubEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestEvent implements GitHubEvent {

    private String action;

    @JsonProperty("number")
    private int prNumber;

    private Repository repository;

    @Override
    public String getRepositoryUrl() {
        return getRepository().getUrl();
    }
}
