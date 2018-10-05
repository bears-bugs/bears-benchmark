package org.imdea.vcd;

import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.imdea.vcd.pb.Proto.Message;
import org.imdea.vcd.pb.Proto.MessageSet;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Vitor Enes
 */
public class Client {

    private static final Logger LOGGER = VCDLogger.init(Client.class);

    private static final int CONNECT_RETRIES = 100;

    private static Config CONFIG;
    private static Socket SOCKET;
    private static Map<ByteString, PerData> MAP;
    private static int[] OPS_PER_CLIENT;
    private static ByteString[] CLIENT_KEY;
    private static int CLIENTS_DONE;

    public static void main(String[] args) {
        try {
            CONFIG = Config.parseArgs(args);
            SOCKET = Socket.create(CONFIG, CONNECT_RETRIES);
            MAP = new HashMap<>();
            OPS_PER_CLIENT = new int[CONFIG.getClients()];
            CLIENT_KEY = new ByteString[CONFIG.getClients()];
            CLIENTS_DONE = 0;

            // create a unique key for each client
            for (int i = 0; i < CONFIG.getClients(); i++) {
                CLIENT_KEY[i] = Generator.randomClientKey();
            }

            LOGGER.log(Level.INFO, "Connect OK!");

            start();

            while (CLIENTS_DONE != CONFIG.getClients()) {
                try {
                    MessageSet messageSet = SOCKET.receive();
                    List<Message> messages = messageSet.getMessagesList();
                    MessageSet.Status status = messageSet.getStatus();

                    ByteString data;
                    PerData perData;
                    switch (status) {
                        case DURABLE:
                            data = messages.get(0).getData();
                            perData = MAP.get(data);
                            // record commit time, if perData exists
                            // TODO check how to could have been delivered
                            // before being committed
                            // - maybe collision with another message,
                            //   in another node
                            // - or on recovery?
                            if (perData != null) {
                                Metrics.end(status, perData.getStartTime());
                            }
                            // keep waiting
                            break;
                        case DELIVERED:
                            // record chain size
                            Metrics.chain(messages.size());

                            Iterator<Message> it = messages.iterator();

                            // try to find operations from clients
                            while (it.hasNext()) {
                                data = it.next().getData();
                                perData = MAP.remove(data);

                                // if it belongs to a client
                                if (perData != null) {
                                    int client = perData.getClient();
                                    Long startTime = perData.getStartTime();

                                    // record delivery time
                                    Metrics.end(status, startTime);
                                    // increment number of ops of this client
                                    OPS_PER_CLIENT[client]++;

                                    // log every 100 ops
                                    if (OPS_PER_CLIENT[client] % 100 == 0) {
                                        LOGGER.log(Level.INFO, "{0} of {1}",
                                                new String[]{String.valueOf(OPS_PER_CLIENT[client]), String.valueOf(CONFIG.getOps())});
                                    }

                                    if (OPS_PER_CLIENT[client] == CONFIG.getOps()) {
                                        // if it performed all the operations
                                        // increment number of clients done
                                        CLIENTS_DONE++;
                                    } else {
                                        // otherwise send another operation
                                        sendOp(client);
                                    }
                                }
                            }
                            break;
                    }
                } catch (IOException e) {
                    // close current socket
                    SOCKET.close();
                    // if at any point the socket errors inside this loop,
                    // reconnect to the closest server
                    SOCKET = Socket.create(CONFIG, CONNECT_RETRIES);
                    // clear current map
                    MAP = new HashMap<>();
                    // and send a new op per client
                    start();
                }

            }

            // after all operations from all clients
            // show metrics
            LOGGER.log(Level.INFO, Metrics.show());

            // and push them to redis
            redisPush();

            SOCKET.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private static void start() throws IOException, InterruptedException {
        for (int i = 0; i < CONFIG.getClients(); i++) {
            sendOp(i);
        }
    }

    private static void sendOp(int client) throws IOException, InterruptedException {
        MessageSet messageSet = Generator.messageSet(CLIENT_KEY[client], CONFIG);
        ByteString data = messageSet.getMessagesList().get(0).getData();
        if (MAP.containsKey(data)) {
            // if this key already exists, try again
            sendOp(client);
        } else {
            // if it doesn't, send it and update map
            PerData perData = new PerData(client, Metrics.start());
            MAP.put(data, perData);
            SOCKET.send(messageSet);
        }
    }

    private static void redisPush() {
        String redis = CONFIG.getRedis();

        if (redis != null) {
            try (Jedis jedis = new Jedis(redis)) {
                Map<String, String> push = Metrics.serialize(CONFIG);
                for (String key : push.keySet()) {
                    jedis.sadd(key, push.get(key));
                }
            }
        }
    }

    private static class PerData {

        private final int client;
        private final Long startTime;

        public PerData(int client, Long startTime) {
            this.client = client;
            this.startTime = startTime;
        }

        public int getClient() {
            return client;
        }

        public long getStartTime() {
            return startTime;
        }
    }
}
