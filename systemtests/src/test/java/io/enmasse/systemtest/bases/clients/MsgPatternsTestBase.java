/*
 * Copyright 2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.systemtest.bases.clients;

import io.enmasse.systemtest.AddressType;
import io.enmasse.systemtest.Destination;
import io.enmasse.systemtest.clients.AbstractClient;
import io.enmasse.systemtest.clients.Argument;
import io.enmasse.systemtest.clients.ClientType;
import org.junit.Before;

import java.util.Arrays;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class MsgPatternsTestBase extends ClientTestBase {

    @Before
    public void setUpCommonArguments() {
        arguments.put(Argument.USERNAME, "test");
        arguments.put(Argument.PASSWORD, "test");
        arguments.put(Argument.LOG_MESSAGES, "json");
        arguments.put(Argument.CONN_SSL, "true");
    }

    protected void doBasicMessageTest(AbstractClient sender, AbstractClient receiver) throws Exception {
        clients.addAll(Arrays.asList(sender, receiver));
        int expectedMsgCount = 10;

        Destination dest = Destination.queue("message-basic" + ClientType.getAddressName(sender),
                getDefaultPlan(AddressType.QUEUE));
        setAddresses(sharedAddressSpace, dest);

        arguments.put(Argument.BROKER, getRouteEndpoint(sharedAddressSpace).toString());
        arguments.put(Argument.ADDRESS, dest.getAddress());
        arguments.put(Argument.COUNT, Integer.toString(expectedMsgCount));
        arguments.put(Argument.MSG_CONTENT, "msg no. %d");

        sender.setArguments(arguments);
        arguments.remove(Argument.MSG_CONTENT);
        receiver.setArguments(arguments);

        assertTrue("Sender failed, expected return code 0", sender.run());
        assertTrue("Receiver failed, expected return code 0", receiver.run());

        assertEquals(String.format("Expected %d sent messages", expectedMsgCount),
                expectedMsgCount, sender.getMessages().size());
        assertEquals(String.format("Expected %d received messages", expectedMsgCount),
                expectedMsgCount, receiver.getMessages().size());
    }

    protected void doRoundRobinReceiverTest(AbstractClient sender, AbstractClient receiver, AbstractClient receiver2)
            throws Exception {
        clients.addAll(Arrays.asList(sender, receiver, receiver2));
        int expectedMsgCount = 10;

        Destination dest = Destination.queue("receiver-round-robin" + ClientType.getAddressName(sender),
                getDefaultPlan(AddressType.QUEUE));
        setAddresses(sharedAddressSpace, dest);

        arguments.put(Argument.BROKER, getRouteEndpoint(sharedAddressSpace).toString());
        arguments.put(Argument.ADDRESS, dest.getAddress());
        arguments.put(Argument.COUNT, Integer.toString(expectedMsgCount / 2));
        arguments.put(Argument.TIMEOUT, "60");


        receiver.setArguments(arguments);
        receiver2.setArguments(arguments);

        Future<Boolean> recResult = receiver.runAsync();
        Future<Boolean> rec2Result = receiver2.runAsync();

        arguments.put(Argument.COUNT, Integer.toString(expectedMsgCount));
        arguments.put(Argument.MSG_CONTENT, "msg no. %d");

        sender.setArguments(arguments);

        assertTrue("Sender failed, expected return code 0", sender.run());
        assertTrue("Receiver failed, expected return code 0", recResult.get());
        assertTrue("Receiver failed, expected return code 0", rec2Result.get());

        assertEquals(String.format("Expected %d sent messages", expectedMsgCount),
                expectedMsgCount, sender.getMessages().size());
        assertEquals(String.format("Expected %d received messages", expectedMsgCount / 2),
                expectedMsgCount / 2, receiver.getMessages().size());
        assertEquals(String.format("Expected %d sent messages", expectedMsgCount / 2),
                expectedMsgCount / 2, receiver.getMessages().size());
    }

    protected void doTopicSubscribeTest(AbstractClient sender, AbstractClient subscriber, AbstractClient subscriber2,
                                        boolean hasTopicPrefix) throws Exception {
        clients.addAll(Arrays.asList(sender, subscriber, subscriber2));
        int expectedMsgCount = 10;

        Destination dest = Destination.topic("topic-subscribe" + ClientType.getAddressName(sender),
                getDefaultPlan(AddressType.TOPIC));
        setAddresses(sharedAddressSpace, dest);

        arguments.put(Argument.BROKER, getRouteEndpoint(sharedAddressSpace).toString());
        arguments.put(Argument.ADDRESS, getTopicPrefix(hasTopicPrefix) + dest.getAddress());
        arguments.put(Argument.COUNT, Integer.toString(expectedMsgCount));
        arguments.put(Argument.MSG_CONTENT, "msg no. %d");
        arguments.put(Argument.TIMEOUT, "100");

        sender.setArguments(arguments);
        arguments.remove(Argument.MSG_CONTENT);
        subscriber.setArguments(arguments);
        subscriber2.setArguments(arguments);

        Future<Boolean> recResult = subscriber.runAsync();
        Future<Boolean> recResult2 = subscriber2.runAsync();

        if (isBrokered(sharedAddressSpace)) {
            waitForSubscribers(sharedAddressSpace, dest.getAddress(), 2);
        } else {
            waitForSubscribersConsole(sharedAddressSpace, dest, 2);
        }

        assertTrue("Producer failed, expected return code 0", sender.run());
        assertTrue("Subscriber failed, expected return code 0", recResult.get());
        assertTrue("Subscriber failed, expected return code 0", recResult2.get());

        assertEquals(String.format("Expected %d sent messages", expectedMsgCount),
                expectedMsgCount, sender.getMessages().size());
        assertEquals(String.format("Expected %d received messages", expectedMsgCount),
                expectedMsgCount, subscriber.getMessages().size());
        assertEquals(String.format("Expected %d received messages", expectedMsgCount),
                expectedMsgCount, subscriber2.getMessages().size());
    }

    protected void doMessageBrowseTest(AbstractClient sender, AbstractClient receiver_browse, AbstractClient receiver_receive)
            throws Exception {
        clients.addAll(Arrays.asList(sender, receiver_browse, receiver_receive));
        int expectedMsgCount = 10;

        Destination dest = Destination.queue("message-browse" + ClientType.getAddressName(sender),
                getDefaultPlan(AddressType.QUEUE));
        setAddresses(sharedAddressSpace, dest);

        arguments.put(Argument.BROKER, getRouteEndpoint(sharedAddressSpace).toString());
        arguments.put(Argument.ADDRESS, dest.getAddress());
        arguments.put(Argument.COUNT, Integer.toString(expectedMsgCount));
        arguments.put(Argument.MSG_CONTENT, "msg no. %d");

        sender.setArguments(arguments);
        arguments.remove(Argument.MSG_CONTENT);

        arguments.put(Argument.RECV_BROWSE, "true");
        receiver_browse.setArguments(arguments);

        arguments.put(Argument.RECV_BROWSE, "false");
        receiver_receive.setArguments(arguments);

        assertTrue("Sender failed, expected return code 0", sender.run());
        assertTrue("Browse receiver failed, expected return code 0", receiver_browse.run());
        assertTrue("Receiver failed, expected return code 0", receiver_receive.run());

        assertEquals(String.format("Expected %d sent messages", expectedMsgCount),
                expectedMsgCount, sender.getMessages().size());
        assertEquals(String.format("Expected %d browsed messages", expectedMsgCount),
                expectedMsgCount, receiver_browse.getMessages().size());
        assertEquals(String.format("Expected %d received messages", expectedMsgCount),
                expectedMsgCount, receiver_receive.getMessages().size());
    }

    protected void doDrainQueueTest(AbstractClient sender, AbstractClient receiver) throws Exception {
        Destination dest = Destination.queue("drain-queue" + ClientType.getAddressName(sender),
                getDefaultPlan(AddressType.QUEUE));
        setAddresses(sharedAddressSpace, dest);

        clients.addAll(Arrays.asList(sender, receiver));
        int expectedMsgCount = 50;

        arguments.put(Argument.BROKER, getRouteEndpoint(sharedAddressSpace).toString());
        arguments.put(Argument.ADDRESS, dest.getAddress());
        arguments.put(Argument.COUNT, Integer.toString(expectedMsgCount));
        arguments.put(Argument.MSG_CONTENT, "msg no. %d");

        sender.setArguments(arguments);
        arguments.remove(Argument.MSG_CONTENT);

        arguments.put(Argument.COUNT, "0");
        receiver.setArguments(arguments);

        assertTrue("Sender failed, expected return code 0", sender.run());
        assertTrue("Drain receiver failed, expected return code 0", receiver.run());

        assertEquals(String.format("Expected %d sent messages", expectedMsgCount),
                expectedMsgCount, sender.getMessages().size());
        assertEquals(String.format("Expected %d received messages", expectedMsgCount),
                expectedMsgCount, receiver.getMessages().size());
    }

    protected void doMessageSelectorQueueTest(AbstractClient sender, AbstractClient receiver) throws Exception {
        int expectedMsgCount = 50;

        clients.addAll(Arrays.asList(sender, receiver));
        Destination queue = Destination.queue("selector-queue" + ClientType.getAddressName(sender),
                getDefaultPlan(AddressType.QUEUE));
        setAddresses(sharedAddressSpace, queue);

        arguments.put(Argument.BROKER, getRouteEndpoint(sharedAddressSpace).toString());
        arguments.put(Argument.COUNT, Integer.toString(expectedMsgCount));
        arguments.put(Argument.ADDRESS, queue.getAddress());
        arguments.put(Argument.MSG_PROPERTY, "colour~red");
        arguments.put(Argument.MSG_PROPERTY, "number~12.65");
        arguments.put(Argument.MSG_PROPERTY, "a~true");
        arguments.put(Argument.MSG_PROPERTY, "b~false");
        arguments.put(Argument.MSG_CONTENT, "msg no. %d");

        //send messages
        sender.setArguments(arguments);
        assertTrue("Sender failed, expected return code 0", sender.run());
        assertEquals(String.format("Expected %d sent messages", expectedMsgCount),
                expectedMsgCount, sender.getMessages().size());

        arguments.remove(Argument.MSG_PROPERTY);
        arguments.remove(Argument.MSG_CONTENT);
        arguments.put(Argument.RECV_BROWSE, "true");
        arguments.put(Argument.COUNT, "0");

        //receiver with selector colour = red
        arguments.put(Argument.SELECTOR, "colour = 'red'");
        receiver.setArguments(arguments);
        assertTrue("Receiver 'colour = red' failed, expected return code 0", receiver.run());
        assertEquals(String.format("Expected %d received messages 'colour = red'", expectedMsgCount),
                expectedMsgCount, receiver.getMessages().size());

        //receiver with selector number > 12.5
        arguments.put(Argument.SELECTOR, "number > 12.5");
        receiver.setArguments(arguments);
        assertTrue("Receiver 'number > 12.5' failed, expected return code 0", receiver.run());
        assertEquals(String.format("Expected %d received messages 'colour = red'", expectedMsgCount),
                expectedMsgCount, receiver.getMessages().size());


        //receiver with selector a AND b
        arguments.put(Argument.SELECTOR, "a AND b");
        receiver.setArguments(arguments);
        assertTrue("Receiver 'a AND b' failed, expected return code 0", receiver.run());
        assertEquals(String.format("Expected %d received messages 'a AND b'", 0),
                0, receiver.getMessages().size());

        //receiver with selector a OR b
        arguments.put(Argument.RECV_BROWSE, "false");
        arguments.put(Argument.SELECTOR, "a OR b");
        receiver.setArguments(arguments);
        assertTrue("Receiver 'a OR b' failed, expected return code 0", receiver.run());
        assertEquals(String.format("Expected %d received messages 'a OR b'", expectedMsgCount),
                expectedMsgCount, receiver.getMessages().size());
    }

    protected void doMessageSelectorTopicTest(AbstractClient sender, AbstractClient subscriber,
                                              AbstractClient subscriber2, AbstractClient subscriber3, boolean hasTopicPrefix) throws Exception {
        clients.addAll(Arrays.asList(sender, subscriber, subscriber2, subscriber3));
        int expectedMsgCount = 10;

        Destination topic = Destination.topic("selector-topic" + ClientType.getAddressName(sender),
                getDefaultPlan(AddressType.TOPIC));
        setAddresses(sharedAddressSpace, topic);

        arguments.put(Argument.BROKER, getRouteEndpoint(sharedAddressSpace).toString());
        arguments.put(Argument.COUNT, Integer.toString(expectedMsgCount));
        arguments.put(Argument.ADDRESS, getTopicPrefix(hasTopicPrefix) + topic.getAddress());
        arguments.put(Argument.MSG_PROPERTY, "colour~red");
        arguments.put(Argument.MSG_PROPERTY, "number~12.65");
        arguments.put(Argument.MSG_PROPERTY, "a~true");
        arguments.put(Argument.MSG_PROPERTY, "b~false");
        arguments.put(Argument.TIMEOUT, "100");
        arguments.put(Argument.MSG_CONTENT, "msg no. %d");

        //set up sender
        sender.setArguments(arguments);

        arguments.remove(Argument.MSG_PROPERTY);
        arguments.remove(Argument.MSG_CONTENT);

        //set up subscriber1
        arguments.put(Argument.SELECTOR, "colour = 'red'");
        subscriber.setArguments(arguments);

        //set up subscriber2
        arguments.put(Argument.SELECTOR, "number > 12.5");
        subscriber2.setArguments(arguments);

        //set up subscriber3
        arguments.put(Argument.SELECTOR, "a AND b");
        subscriber3.setArguments(arguments);

        Future<Boolean> result1 = subscriber.runAsync();
        Future<Boolean> result2 = subscriber2.runAsync();
        Future<Boolean> result3 = subscriber3.runAsync();

        if (isBrokered(sharedAddressSpace)) {
            waitForSubscribers(sharedAddressSpace, topic.getAddress(), 3);
        } else {
            waitForSubscribersConsole(sharedAddressSpace, topic, 3);
        }

        assertTrue("Sender failed, expected return code 0", sender.run());
        assertTrue("Receiver 'colour = red' failed, expected return code 0", result1.get());
        assertTrue("Receiver 'number > 12.5' failed, expected return code 0", result2.get());
        assertTrue("Receiver 'a AND b' failed, expected return code 0", result3.get());

        assertEquals(String.format("Expected %d sent messages", expectedMsgCount),
                expectedMsgCount, sender.getMessages().size());
        assertEquals(String.format("Expected %d received messages 'colour = red'", expectedMsgCount),
                expectedMsgCount, subscriber.getMessages().size());
        assertEquals(String.format("Expected %d received messages 'number > 12.5'", expectedMsgCount),
                expectedMsgCount, subscriber2.getMessages().size());
        assertEquals(String.format("Expected %d received messages 'a AND b'", 0),
                0, subscriber3.getMessages().size());
    }
}
