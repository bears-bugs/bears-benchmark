package org.imdea.vcd;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.imdea.vcd.pb.Proto;
import org.imdea.vcd.pb.Proto.MessageSet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Vitor Enes
 */
public class Socket {

    private static final Logger LOGGER = VCDLogger.init(Socket.class);

    private final DataRW rw;

    protected Socket(DataRW rw) {
        this.rw = rw;
        if (rw != null) {
            this.rw.start();
        }
    }

    public static Socket createStatic(Config config, int retries) throws IOException, InterruptedException {
        Pattern p = Pattern.compile("^\\s*(.*?):(\\d+)\\s*$");
        Matcher m = p.matcher(config.getZk());
        if (m.matches()) {
            LOGGER.log(Level.INFO, "Correct Address");
        }
        String host = m.group(1);
        Integer port = Integer.parseInt(m.group(2));

        LOGGER.log(Level.INFO, "Connecting to static node {0}:{1}",
                new String[]{host, String.valueOf(port)});

        for (int i = 0; i < retries; i++) {
            try {
                java.net.Socket socket = new java.net.Socket(host, port + 1000);
                socket.setTcpNoDelay(true);

                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataRW rw = new DataRW(in, out, config);

                return new Socket(rw);
            } catch (java.net.ConnectException e) {
                LOGGER.log(Level.INFO, "Failed to connect to static node. Trying again in 10ms.");
                // swallow exception and sleep 10ms before trying again
                Thread.sleep(10);
            }

        }
        throw new java.net.ConnectException();
    }

    public static Socket create(Config config) throws IOException, InterruptedException {
        Proto.NodeSpec closest = getClosestNode(config);
        LOGGER.log(Level.INFO, "Closest node is {0}:{1}",
                new String[]{closest.getIp(), String.valueOf(closest.getPort())});

        java.net.Socket socket = new java.net.Socket(closest.getIp(), closest.getPort() + 1000);
        socket.setTcpNoDelay(true);

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataRW rw = new DataRW(in, out, config);

        return new Socket(rw);
    }

    public static Socket create(Config config, int retries) throws IOException, InterruptedException {

        for (int i = 0; i < retries; i++) {
            try {
                return Socket.create(config);
            } catch (java.net.ConnectException e) {
                LOGGER.log(Level.INFO, "Failed to connect to closest node. Trying again in 10ms.");

                // swallow exception and sleep 10ms before trying again
                Thread.sleep(10);
            }
        }

        throw new java.net.ConnectException();
    }

    public void send(MessageSet messageSet) throws IOException, InterruptedException {
        this.rw.write(messageSet);
    }

    public MessageSet receive() throws IOException, InterruptedException {
        return this.rw.read();
    }

    public void close() throws IOException {
        this.rw.close();
    }

    public static Proto.NodeSpec getClosestNode(Config config) throws IOException, InterruptedException {
        List<Proto.NodeSpec> nodes = getAllNodes(config);

        Float min = Float.MAX_VALUE;
        Proto.NodeSpec closest = null;

        for (Proto.NodeSpec node : nodes) {
            Float delay = ping(node.getIp());
            if (delay != null && delay < min) {
                min = delay;
                closest = node;
            }
        }

        return closest;
    }

    private static List<Proto.NodeSpec> getAllNodes(Config config) throws IOException {
        List<Proto.NodeSpec> nodes = new ArrayList<>();
        String root = "/" + config.getTimestamp();

        try {
            ZooKeeper zk = zkConnection(config);

            for (String child : zk.getChildren(root, null)) {
                String path = root + "/" + child;
                byte[] data = zk.getData(path, null, null);
                nodes.add(Proto.NodeSpec.parseFrom(data));
            }
            zk.close();

        } catch (KeeperException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw new RuntimeException("Fatal error, cannot connect to Zk.");
        }

        return nodes;
    }

    /**
     * Since connecting to ZooKeeper is asynchronous, this method only returns
     * once the watcher is notified the connection is ready.
     */
    private static ZooKeeper zkConnection(Config config) throws IOException, InterruptedException {
        assert config.getZk().split(":").length == 2;

        Semaphore semaphore = new Semaphore(0);
        ZooKeeper zk = new ZooKeeper(
                config.getZk() + "/",
                5000, new ZkWatcher(semaphore));
        semaphore.acquire();

        return zk;
    }

    private static class ZkWatcher implements Watcher {

        private final Semaphore semaphore;

        public ZkWatcher(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void process(WatchedEvent watchedEvent) {
            switch (watchedEvent.getState()) {
                case SyncConnected:
                    semaphore.release();
                    break;
            }
        }
    }

    /**
     * ping -c 2 $IP | tail -n 1 | cut -d/ -f5
     */
    private static Float ping(String ip) throws InterruptedException, IOException {
        Float ping = null;

        List<String> output = executeCommand("ping -q -c 2 " + ip);
        if (output != null) {
            String stats = output.get(output.size() - 1);
            String average = stats.split("/")[4];
            ping = Float.parseFloat(average);
        }

        return ping;
    }

    private static List<String> executeCommand(String command) throws IOException, InterruptedException {
        List<String> output = new ArrayList<>();

        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.add(line);
        }

        if (p.exitValue() == 0) {
            return output;
        } else {
            LOGGER.log(Level.SEVERE, "Command {0} failed. Output:", command);
            LOGGER.log(Level.SEVERE, String.join("\n", output));
            return null;
        }
    }
}
