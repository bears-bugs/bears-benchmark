package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.tasks.consumer.infrastructure.config.InfraConfig;
import com.societegenerale.cidroid.tasks.consumer.services.GitHubContentBase64codec;
import com.societegenerale.cidroid.tasks.consumer.services.exceptions.GitHubAuthorizationException;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.Comment;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.DirectCommit;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.ResourceContent;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.UpdatedResource;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={ InfraConfig.class,LiveTestConfig.class}, initializers = YamlFileApplicationContextInitializer.class)
@TestPropertySource("/application-test.yml")
@Ignore("to launch manually and test in local - you probably need to update config before running")
public class FeignRemoteGitHubLIVETest {

    @Autowired
    FeignRemoteGitHub feignRemoteGitHub;

    @Test
    public void manualTestForPublishingComment(){

        feignRemoteGitHub.addCommentDescribingRebase("myOrga/myProject", 2, new Comment("test Vincent"));

    }

    @Test
    public void manualTestForFetchingAResource() {

        ResourceContent resourceContent = feignRemoteGitHub.fetchContent("myOrga/myProject", "null", "master");

        assertThat(resourceContent).isNull();
    }

    @Test
    public void manualTestForUpdatingAResource() throws GitHubAuthorizationException {

        DirectCommit directCommit = new DirectCommit();
        directCommit.setBranch("master");
        directCommit.setCommitter(new DirectCommit.Committer("vincent-fuchs", "vincent.fuchs@gmail.com"));
        directCommit.setCommitMessage("test performed on behalf of vincent-fuchs by CI-droid");

        directCommit.setBase64EncodedContent(GitHubContentBase64codec.encode("hello world with OAuth token"));

        UpdatedResource updatedResource = feignRemoteGitHub.updateContent("myOrga/myProject",
                                                                          "testFile3.txt",
                                                                          directCommit, "someToken123456");

        assertThat(updatedResource).isNull();
    }

}