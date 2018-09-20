package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.beans.ConstructorProperties;
import java.util.Map;

@Data
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequest {

    private final int number;

    private Boolean mergeable;

    private User user;

    private String url;

    private Repository repo;

    private String branchName;

    private String baseBranchName;

    private String branchStartedFromCommit;

    @JsonIgnore
    private boolean isMadeFromForkedRepo;

    @JsonIgnore
    private String warningMessageDuringRebasing;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ObjectMapper objectMapper=new ObjectMapper();

    @JsonProperty("html_url")
    private String htmlUrl;

    @ConstructorProperties({ "number" })
    @JsonCreator
    public PullRequest(int number) {
        this.number = number;
    }

    public PRmergeableStatus getMergeStatus(){

       return PRmergeableStatus.mapping.get(mergeable);

    }

    @JsonProperty("base")
    private void unpackNestedBaseProperty(Map<String,Object> base) {
        this.branchStartedFromCommit=(String)base.get("sha");

        this.baseBranchName=(String)base.get("ref");

        this.repo=objectMapper.convertValue(base.get("repo"), Repository.class);
    }

    @JsonProperty("head")
    private void unpackNestedHeadProperty(Map<String,Object> base) {
        this.branchName=(String)base.get("ref");

       Repository repoFromWhichPrOriginates=objectMapper.convertValue(base.get("repo"), Repository.class);
       isMadeFromForkedRepo=repoFromWhichPrOriginates.isFork();
    }

}
