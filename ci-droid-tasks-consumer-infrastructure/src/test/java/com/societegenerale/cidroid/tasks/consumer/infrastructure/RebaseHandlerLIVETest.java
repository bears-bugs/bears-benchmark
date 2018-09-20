package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.config.InfraConfig;
import com.societegenerale.cidroid.tasks.consumer.services.Rebaser;
import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.actionHandlers.RebaseHandler;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PullRequest;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { InfraConfig.class, LiveTestConfig.class }, initializers = YamlFileApplicationContextInitializer.class)
@TestPropertySource("/application-test.yml")
@Ignore("to launch manually and test in local")
public class RebaseHandlerLIVETest {

    @Autowired
    Rebaser rebaser;

    @Autowired
    RemoteGitHub remoteGitHub;

    PullRequest singlePr;

    PushEvent pushEvent;

    RebaseHandler rebaseHandler;

    @Before
    public void setup() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        String prAsString = IOUtils
                .toString(RebaseHandlerLIVETest.class.getClassLoader().getResourceAsStream("myPullRequestToRebase.json"), "UTF-8");
        singlePr = objectMapper.readValue(prAsString, PullRequest.class);

        String pushEventPayload = IOUtils
                .toString(RebaseHandlerLIVETest.class.getClassLoader().getResourceAsStream("myPushEventToTest.json"), "UTF-8");

        pushEvent = objectMapper.readValue(pushEventPayload, PushEvent.class);

        rebaseHandler = new RebaseHandler(rebaser, remoteGitHub);
    }

    @Test
    public void manualTest() {

        rebaseHandler.handle(pushEvent, Arrays.asList(singlePr));
    }

}