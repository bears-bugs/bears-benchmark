package com.societegenerale.cidroid.tasks.consumer.infrastructure.notifiers;

import com.societegenerale.cidroid.tasks.consumer.infrastructure.mocks.TargetHttpBackendForNotifier;
import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.Collections.emptyMap;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpNotifierTest {

    HttpNotifier httpNotifier=new HttpNotifier("http://localhost:9901/notify");

    static boolean hasHttpNotifierBackendStarted = false;

    TargetHttpBackendForNotifier httpBackendServer=new TargetHttpBackendForNotifier();

    @Before
    public void setup(){

        if (!hasHttpNotifierBackendStarted) {

            httpBackendServer.start();

            await().atMost(5, SECONDS)
                    .until(() -> assertThat(TargetHttpBackendForNotifier.hasStarted()));

            hasHttpNotifierBackendStarted = true;
        }

        httpBackendServer.reset();
    }

    @Test
    public void shouldSendData(){

        String emailUser="toto@socgen.com";

        User user=User.builder().email(emailUser).build();

        String messageContent = "message content";
        Message message=new Message(messageContent);

        httpNotifier.notify(user,message, emptyMap());

        assertThat(httpBackendServer.getNotificationsReceived()).hasSize(1);

        String notif=httpBackendServer.getNotificationsReceived().get(0);

        assertThat(notif).contains(emailUser);
        assertThat(notif).contains(messageContent);
    }



}