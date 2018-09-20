package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.config.InfraConfig;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={ InfraConfig.class,LiveTestConfig.class}, initializers = YamlFileApplicationContextInitializer.class)
@TestPropertySource("/application-test.yml")
@Ignore("to launch manually and test in local on 'real' pushEvent documents")
public class GithubEventListenerLIVETest {

    @Autowired
    GithubEventListener githubEventListener;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void actualLiveTest() throws Exception {

        String pushEventPayload = IOUtils
                .toString(GithubEventListenerLIVETest.class.getClassLoader().getResourceAsStream("pushEventLive.json"), "UTF-8");

        PushEvent pushEvent = objectMapper.readValue(pushEventPayload, PushEvent.class);

        githubEventListener.onGitHubPushEventOnDefaultBranch(pushEvent);

    }

}