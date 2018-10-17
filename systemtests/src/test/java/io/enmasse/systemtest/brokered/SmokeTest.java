/*
 * Copyright 2017-2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.systemtest.brokered;

import io.enmasse.systemtest.*;
import io.enmasse.systemtest.amqp.AmqpClient;
import io.enmasse.systemtest.bases.BrokeredTestBase;
import io.enmasse.systemtest.standard.QueueTest;
import org.apache.qpid.proton.message.Message;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SmokeTest extends BrokeredTestBase {

    /**
     * related github issue: #335
     */
    @Test
    public void testAddressTypes() throws Exception {
        Destination queueA = Destination.queue("brokeredQueueA", getDefaultPlan(AddressType.QUEUE));
        setAddresses(sharedAddressSpace, queueA);

        AmqpClient amqpQueueCli = amqpClientFactory.createQueueClient(sharedAddressSpace);
        amqpQueueCli.getConnectOptions().setUsername("test").setPassword("test");
        QueueTest.runQueueTest(amqpQueueCli, queueA);
        amqpQueueCli.close();

        Destination topicB = Destination.topic("brokeredTopicB", getDefaultPlan(AddressType.TOPIC));
        setAddresses(sharedAddressSpace, topicB);

        AmqpClient amqpTopicCli = amqpClientFactory.createTopicClient(sharedAddressSpace);
        amqpTopicCli.getConnectOptions().setUsername("test").setPassword("test");
        List<Future<List<Message>>> recvResults = Arrays.asList(
                amqpTopicCli.recvMessages(topicB.getAddress(), 1000),
                amqpTopicCli.recvMessages(topicB.getAddress(), 1000));

        List<String> msgsBatch = TestUtils.generateMessages(600);
        List<String> msgsBatch2 = TestUtils.generateMessages(400);

        assertThat("Wrong count of messages sent: batch1",
                amqpTopicCli.sendMessages(topicB.getAddress(), msgsBatch).get(1, TimeUnit.MINUTES), is(msgsBatch.size()));
        assertThat("Wrong count of messages sent: batch2",
                amqpTopicCli.sendMessages(topicB.getAddress(), msgsBatch2).get(1, TimeUnit.MINUTES), is(msgsBatch2.size()));

        assertThat("Wrong count of messages received",
                recvResults.get(0).get(1, TimeUnit.MINUTES).size(), is(msgsBatch.size() + msgsBatch2.size()));
        assertThat("Wrong count of messages received",
                recvResults.get(1).get(1, TimeUnit.MINUTES).size(), is(msgsBatch.size() + msgsBatch2.size()));
        amqpTopicCli.close();
    }

    /**
     * related github issue: #334
     */
    @Test
    public void testCreateDeleteAddressSpace() throws Exception {
        AddressSpace addressSpaceA = new AddressSpace("brokered-create-delete-a", AddressSpaceType.BROKERED);
        createAddressSpace(addressSpaceA, "standard");
        Destination queueB = Destination.queue("brokeredQueueB", getDefaultPlan(AddressType.QUEUE));
        setAddresses(addressSpaceA, queueB);
        getKeycloakClient().createUser(addressSpaceA.getName(), "test", "test");

        AmqpClient amqpQueueCliA = amqpClientFactory.createQueueClient(addressSpaceA);
        amqpQueueCliA.getConnectOptions().setUsername("test").setPassword("test");
        QueueTest.runQueueTest(amqpQueueCliA, queueB);
        amqpQueueCliA.close();

        AddressSpace addressSpaceC = new AddressSpace("brokered-create-delete-c", AddressSpaceType.BROKERED);
        createAddressSpace(addressSpaceC, "standard");
        setAddresses(addressSpaceC, queueB);
        getKeycloakClient().createUser(addressSpaceC.getName(), "test", "test");

        AmqpClient amqpQueueCliC = amqpClientFactory.createQueueClient(addressSpaceC);
        amqpQueueCliC.getConnectOptions().setUsername("test").setPassword("test");
        QueueTest.runQueueTest(amqpQueueCliC, queueB);
        amqpQueueCliC.close();

        deleteAddressSpace(addressSpaceA);

        QueueTest.runQueueTest(amqpQueueCliC, queueB);
    }

    //@Test(expected = AddressAlreadyExistsException.class) //!TODO disabled until #346 will be fixed
    public void testCreateAlreadyExistingAddress() throws Exception {
        AddressSpace addressSpaceA = new AddressSpace("brokered-a", AddressSpaceType.BROKERED);
        createAddressSpace(addressSpaceA, "standard");
        Destination queueA = Destination.queue("brokeredQueueA", getDefaultPlan(AddressType.QUEUE));
        setAddresses(addressSpaceA, queueA);

        Destination topicA = Destination.topic("brokeredTopicA", getDefaultPlan(AddressType.TOPIC));
        setAddresses(addressSpaceA, topicA); //address already exist exception
    }
}
