/*
 * Copyright 2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.systemtest.brokered;

import io.enmasse.systemtest.*;
import io.enmasse.systemtest.bases.BrokeredTestBase;
import io.enmasse.systemtest.amqp.AmqpClient;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.message.Message;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class QueueTest extends BrokeredTestBase {

    /**
     * related github issue: #387
     */
    @Test
    public void messageGroupTest() throws Exception {
        Destination dest = Destination.queue("messageGroupQueue", getDefaultPlan(AddressType.QUEUE));
        setAddresses(sharedAddressSpace, dest);

        AmqpClient client = amqpClientFactory.createQueueClient(sharedAddressSpace);

        int msgsCount = 20;
        int msgCountGroupA = 15;
        int msgCountGroupB = 5;
        List<Message> listOfMessages = new ArrayList<>();
        for (int i = 0; i < msgsCount; i++) {
            Message msg = Message.Factory.create();
            msg.setAddress(dest.getAddress());
            msg.setBody(new AmqpValue(dest.getAddress()));
            msg.setSubject("subject");
            msg.setGroupId(((i + 1) % 4 != 0) ? "group A" : "group B");
            listOfMessages.add(msg);
        }

        Future<List<Message>> receivedGroupA = client.recvMessages(dest.getAddress(), msgCountGroupA);
        Future<List<Message>> receivedGroupB = client.recvMessages(dest.getAddress(), msgCountGroupB);
        Thread.sleep(2000);

        Future<Integer> sent = client.sendMessages(dest.getAddress(),
                listOfMessages.toArray(new Message[listOfMessages.size()]));

        assertThat("Wrong count of messages sent", sent.get(1, TimeUnit.MINUTES), is(msgsCount));
        assertThat("Wrong count of messages received from group A",
                receivedGroupA.get(1, TimeUnit.MINUTES).size(), is(msgCountGroupA));
        assertThat("Wrong count of messages received from group A",
                receivedGroupB.get(1, TimeUnit.MINUTES).size(), is(msgCountGroupB));

        for (Message m : receivedGroupA.get()) {
            assertEquals("Group id is different", m.getGroupId(), "group A");
        }

        for (Message m : receivedGroupB.get()) {
            assertEquals("Group id is different", m.getGroupId(), "group B");
        }
    }
}
