package com.societegenerale.cidroid.tasks.consumer.services.model.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.services.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PullRequestCommentTest {

    @Test
    public void shouldDeserializeCorrectly() throws IOException {

        String pullRequestCommentsAsString = TestUtils.readFromInputStream(getClass().getResourceAsStream("/pullRequestComments.json"));
        List<PullRequestComment> prComments = new ObjectMapper()
                .readValue(pullRequestCommentsAsString, new TypeReference<List<PullRequestComment>>() {
                });

        Assertions.assertThat(prComments).hasSize(1);

        PullRequestComment firstPrComment = prComments.get(0);
        assertThat(firstPrComment.getComment()).isEqualTo("Me too");
        assertThat(firstPrComment.getAuthor().getLogin()).isEqualTo("octocat");
    }

}