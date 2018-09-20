package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PullRequestTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void fieldsAreDeserialized() throws IOException {

        String prAsString = IOUtils
                .toString(PullRequestTest.class.getClassLoader().getResourceAsStream("singlePullRequest.json"), "UTF-8");

        PullRequest pr=objectMapper.readValue(prAsString,PullRequest.class);

        assertThat(pr).isNotNull();
        assertThat(pr.getNumber()).isGreaterThan(0);

    }


}