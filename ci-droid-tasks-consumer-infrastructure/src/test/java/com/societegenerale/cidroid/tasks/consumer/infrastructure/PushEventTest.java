package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.PushEvent;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PushEventTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void fieldsAreDeserialized() throws IOException {

        String pushEventAsString = IOUtils
                .toString(PushEventTest.class.getClassLoader().getResourceAsStream("pushEvent.json"), "UTF-8");

        PushEvent pushEvent = objectMapper.readValue(pushEventAsString, PushEvent.class);

        assertThat(pushEvent).isNotNull();

    }

}