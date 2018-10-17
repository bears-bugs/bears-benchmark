/*
 * Copyright 2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.systemtest.brokered.jms;

import io.enmasse.systemtest.*;
import io.enmasse.systemtest.Destination;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class QueueTest extends JMSTestBase {
    private static Logger log = CustomLogger.getLogger();

    private Hashtable<Object, Object> env;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Context context;
    private String queue = "jmsQueue";
    private Destination addressQueue;

    @Before
    public void setUp() throws Exception {
        addressQueue = Destination.queue(queue, getDefaultPlan(AddressType.QUEUE));
        setAddresses(sharedAddressSpace, addressQueue);

        env = setUpEnv("amqps://" + getRouteEndpoint(sharedAddressSpace).toString(), jmsUsername, jmsPassword, jmsClientID,
                new HashMap<String, String>() {{
                    put("queue." + queue, queue);
                }});
        context = new InitialContext(env);
        connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionFactory");
        connection = connectionFactory.createConnection();
        connection.start();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.stop();
        }
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }


    @Test
    public void transactionCommitRejectTest() throws Exception {
        log.info("testTransactionCommitReject");
        session = connection.createSession(true, Session.SESSION_TRANSACTED);
        Queue testQueue = (Queue) context.lookup(queue);

        MessageProducer sender = session.createProducer(testQueue);
        MessageConsumer receiver = session.createConsumer(testQueue);
        List<Message> recvd;

        int count = 50;

        List<Message> listMsgs = generateMessages(session, count);

        //send messages and commit
        sendMessages(sender, listMsgs);
        session.commit();
        log.info("messages sent commit");

        //receive commit messages
        recvd = receiveMessages(receiver, count, 1000);
        for (Message message : recvd) {
            assertNotNull("No message received", message);
        }
        session.commit();
        log.info("messages received commit");

        //send messages rollback
        sendMessages(sender, listMsgs);
        session.rollback();
        log.info("messages sent rollback");

        //check if queue is empty
        Message received = receiver.receive(1000);
        assertNull("Queue should be empty", received);
        log.info("queue is empty");

        //send messages
        sendMessages(sender, listMsgs);
        session.commit();
        log.info("messages sent commit");

        //receive messages rollback
        recvd.clear();
        recvd = receiveMessages(receiver, count, 1000);
        for (Message message : recvd) {
            assertNotNull("No message received", message);
        }
        session.rollback();
        log.info("messages received rollback");

        //receive messages
        recvd.clear();
        recvd = receiveMessages(receiver, count, 1000);
        for (Message message : recvd) {
            assertNotNull("No message received", message);
        }
        session.commit();
        log.info("messages received commit");

        sender.close();
        receiver.close();
    }
}
