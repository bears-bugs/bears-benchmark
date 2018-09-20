package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import lombok.Data;

@Data
public class PullRequestToCreate {

    private String title;

    private String body;

    private String head;

    private String base;

}
