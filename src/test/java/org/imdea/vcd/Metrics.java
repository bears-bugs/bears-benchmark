package org.imdea.vcd;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.imdea.vcd.pb.Proto.MessageSet;
import org.imdea.vcd.queue.clock.Dot;

/**
 *
 * @author Vitor Enes
 */
public class Metrics {

    private static final Averager DURABLE_AVG = new Averager();
    private static final Averager DELIVERED_AVG = new Averager();
    private static final Averager EXECUTION_AVG = new Averager();
    private static final Averager MID_EXECUTION_AVG = new Averager();
    private static final Averager CHAINS_AVG = new Averager();

    private static final StringBuilder DURABLE_TIMES = new StringBuilder();
    private static final StringBuilder DELIVERED_TIMES = new StringBuilder();
    private static final StringBuilder EXECUTION_TIMES = new StringBuilder();
    private static final StringBuilder MID_EXECUTION_TIMES = new StringBuilder();
    private static final StringBuilder CHAINS = new StringBuilder();

    private static final ConcurrentHashMap<Dot, Long> DOT_TO_START = new ConcurrentHashMap<>();

    public static Long start() {
        return time();
    }

    public static void end(MessageSet.Status status, Long start) {
        Long time = time() - start;

        switch (status) {
            case DURABLE:
                DURABLE_AVG.add(time);
                DURABLE_TIMES.append(time).append("\n");
                break;
            case DELIVERED:
                DELIVERED_AVG.add(time);
                DELIVERED_TIMES.append(time).append("\n");
                break;
        }
    }

    public static void startExecution(Dot dot) {
        DOT_TO_START.put(dot, start());
    }

    public static Long midExecution(Dot dot) {
        Long time = time() - DOT_TO_START.get(dot);
        MID_EXECUTION_AVG.add(time);
        MID_EXECUTION_TIMES.append(time).append("\n");
        return time;
    }

    public static Long endExecution(Dot dot) {
        Long time = time() - DOT_TO_START.remove(dot);
        EXECUTION_AVG.add(time);
        EXECUTION_TIMES.append(time).append("\n");
        return time;
    }

    public static void chain(Integer size) {
        CHAINS_AVG.add(size.longValue());
        CHAINS.append(time())
                .append("-")
                .append(size)
                .append("\n");
    }

    public static String show() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("CHAINS: ")
                .append(CHAINS_AVG.getAverage())
                .append("\n");
        sb.append("DURABLE: ")
                .append(DURABLE_AVG.getAverage())
                .append(" (ms)\n");
        sb.append("DELIVERED: ")
                .append(DELIVERED_AVG.getAverage())
                .append(" (ms)\n");
        sb.append("MID EXECUTION: ")
                .append(MID_EXECUTION_AVG.getAverage())
                .append(" (ms)\n");
        sb.append("EXECUTION: ")
                .append(EXECUTION_AVG.getAverage())
                .append(" (ms)\n");
        return sb.toString();
    }

    public static Map<String, String> serialize(Config config) {
        Map<String, String> m = new HashMap<>();
        m.put(
                key(config, "chains"),
                serialize(CHAINS)
        );
        m.put(
                key(config, "log", "Durable"),
                serialize(DURABLE_TIMES)
        );
        m.put(
                key(config, "log"),
                serialize(DELIVERED_TIMES)
        );
        m.put(
                key(config, "log", "MidExecution"),
                serialize(MID_EXECUTION_TIMES)
        );
        m.put(
                key(config, "log", "Execution"),
                serialize(EXECUTION_TIMES)
        );

        return m;
    }

    private static String key(Config config, String prefix) {
        return key(config, prefix, "");
    }

    private static String key(Config config, String prefix, String protocolSuffix) {
        return "" + config.getNodeNumber() + "/"
                + prefix + "-"
                + protocol(config.getMaxFaults(), protocolSuffix) + "-"
                + config.getCluster() + "-"
                + config.getClients() + "-"
                + config.getConflicts() + "-"
                + "100" + "-" // percentage of writes
                + config.getOp();
    }

    private static String protocol(Integer maxFaults, String protocolSuffix) {
        return "VCD" + "f" + maxFaults + protocolSuffix;
    }

    private static String serialize(StringBuilder sb) {
        int len = sb.length();
        if (len > 0 && sb.charAt(len - 1) == '\n') {
            sb.deleteCharAt(len - 1);
        }
        return sb.toString();
    }

    private static Long time() {
        return System.currentTimeMillis();
    }

    private static class Averager {

        private Long elements;
        private Long average;

        public Averager() {
            this.elements = 0L;
            this.average = 0L;
        }

        public void add(Long value) {
            this.average = (this.average * this.elements + value) / ++this.elements;
        }

        public Long getAverage() {
            return average;
        }
    }
}
