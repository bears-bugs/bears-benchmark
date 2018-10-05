package org.imdea.vcd;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricAttribute;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.imdea.vcd.pb.Proto.Commit;
import org.imdea.vcd.pb.Proto.Message;
import org.imdea.vcd.pb.Proto.MessageSet;
import org.imdea.vcd.pb.Proto.Reply;
import org.imdea.vcd.queue.ConfQueue;
import org.imdea.vcd.queue.DepQueue;
import org.imdea.vcd.queue.Queue;
import org.imdea.vcd.queue.QueueAddArgs;
import org.imdea.vcd.queue.QueueType;
import org.imdea.vcd.queue.RandomQueue;
import org.imdea.vcd.queue.box.CommittedQueueBox;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.Dot;
import org.imdea.vcd.queue.clock.ExceptionSet;
import org.imdea.vcd.queue.clock.MaxInt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vitor Enes
 */
public class DataRW {

    private static final MetricRegistry METRICS = new MetricRegistry();
    private static final int METRICS_REPORT_PERIOD = 10; // in seconds.

    static {
        Set<MetricAttribute> disabledMetricAttributes
                = new HashSet<>(Arrays.asList(new MetricAttribute[]{
            MetricAttribute.MAX,
            MetricAttribute.M1_RATE, MetricAttribute.M5_RATE,
            MetricAttribute.M15_RATE, MetricAttribute.MIN,
            MetricAttribute.P99, MetricAttribute.P50,
            MetricAttribute.P75, MetricAttribute.P95,
            MetricAttribute.P98, MetricAttribute.P999}));
        ConsoleReporter reporter = ConsoleReporter.forRegistry(METRICS)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MICROSECONDS)
                .disabledMetricAttributes(disabledMetricAttributes)
                .build();
        reporter.start(METRICS_REPORT_PERIOD, TimeUnit.SECONDS);
    }

    private final DataInputStream in;
    private final DataOutputStream out;

    private final LinkedBlockingQueue<MessageSet> toWriter;
    private final LinkedBlockingQueue<Optional<MessageSet>> toClient;
    private final WriteDelay writeDelay;

    private final Writer writer;
    private final SocketReader socketReader;

    public DataRW(DataInputStream in, DataOutputStream out, Config config) {
        this.in = in;
        this.out = out;
        this.toWriter = new LinkedBlockingQueue<>();
        this.toClient = new LinkedBlockingQueue<>();
        this.writeDelay = new WriteDelay();
        this.writer = new Writer(this.out, this.toWriter, this.writeDelay);
        this.socketReader = new SocketReader(this.in, this.toClient, this.writeDelay, config);
    }

    public void start() {
        this.writer.start();
        this.socketReader.start();
    }

    public void close() throws IOException {
        this.writer.interrupt();
        this.socketReader.close();
        this.socketReader.interrupt();
        this.in.close();
        this.out.close();
    }

    public MessageSet read() throws IOException, InterruptedException {
        Optional<MessageSet> result = this.toClient.take();
        if (!result.isPresent()) {
            throw new IOException();
        }
        return result.get();
    }

    public void write(MessageSet messageSet) throws IOException, InterruptedException {
        toWriter.put(messageSet);
    }

    private void doWrite(MessageSet messageSet, DataOutputStream o) throws IOException {
        byte[] data = messageSet.toByteArray();
        o.writeInt(data.length);
        o.write(data, 0, data.length);
        o.flush();
    }

    private void notifyFailureToClient(LinkedBlockingQueue<Optional<MessageSet>> toClient) throws InterruptedException {
        toClient.put(Optional.empty());
    }

    private class Writer extends Thread {

        private final Logger LOGGER = VCDLogger.init(Writer.class);

        private final LinkedBlockingQueue<MessageSet> toWriter;
        private final DataOutputStream out;
        private final Histogram batchSize;
        private final Timer delayWrite;
        private final WriteDelay writeDelay;

        public Writer(DataOutputStream out, LinkedBlockingQueue<MessageSet> toWriter, WriteDelay writeDelay) {
            this.out = out;
            this.toWriter = toWriter;
            this.writeDelay = writeDelay;
            this.batchSize = METRICS.histogram(MetricRegistry.name(DataRW.class, "batchSize"));
            this.delayWrite = METRICS.timer(MetricRegistry.name(DataRW.class, "delayWrite"));
        }

        @Override
        public void run() {
            try {
                try {
                    while (true) {
                        MessageSet first = toWriter.take();
                        List<MessageSet> set = new ArrayList<>();

                        // wait, and drain the queue
                        final Timer.Context delayWriteTimer = delayWrite.time();
                        writeDelay.waitDepsCommitted();
                        delayWriteTimer.stop();

                        toWriter.drainTo(set);

                        set.add(first);

                        // batch size metrics
                        batchSize.update(set.size());

                        for (MessageSet m : set) {
                            doWrite(m, this.out);
                        }
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    notifyFailureToClient(toClient);
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
    }

    // socket reader -> client | deliverer
    // deliverer -> sorter
    // sorter -> client
    private class SocketReader extends Thread {

        private final Logger LOGGER = VCDLogger.init(SocketReader.class);

        private final DataInputStream in;
        private final LinkedBlockingQueue<Optional<MessageSet>> toClient;
        private final LinkedBlockingQueue<Reply> toDeliverer;
        private final Deliverer deliverer;

        public SocketReader(DataInputStream in, LinkedBlockingQueue<Optional<MessageSet>> toClient, WriteDelay writeDelay, Config config) {
            this.in = in;
            this.toClient = toClient;
            this.toDeliverer = new LinkedBlockingQueue<>();
            this.deliverer = new Deliverer(this.toClient, this.toDeliverer, writeDelay, config);
        }

        public void close() {
            this.deliverer.close();
            this.deliverer.interrupt();
        }

        @Override
        public void run() {
            // start deliverer
            this.deliverer.start();

            try {
                try {
                    while (true) {
                        int length = in.readInt();
                        byte data[] = new byte[length];
                        in.readFully(data, 0, length);
                        Reply reply = Reply.parseFrom(data);

                        // if durable notification, send it to client
                        // otherwise to dep queue thread
                        switch (reply.getReplyCase()) {
                            case SET:
                                MessageSet durable = reply.getSet();
                                if (durable.getMessagesCount() != 1 || durable.getStatus() != MessageSet.Status.DURABLE) {
                                    LOGGER.log(Level.INFO, "Invalid durable notification");
                                }

                                toClient.put(Optional.of(durable));
                                break;
                            default:
                                if (reply.hasCommit()) {
                                    Metrics.startExecution(Dot.dot(reply.getCommit().getDot()));
                                }
                                toDeliverer.put(reply);
                                break;
                        }
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                    notifyFailureToClient(toClient);
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
    }

    private class Deliverer extends Thread {

        private final Logger LOGGER = VCDLogger.init(Deliverer.class);

        private final LinkedBlockingQueue<Reply> toDeliverer;
        private final LinkedBlockingQueue<List<CommittedQueueBox>> toSorter;
        private final WriteDelay writeDelay;

        private final Sorter sorter;
        private final QueueType queueType;
        private Queue<CommittedQueueBox> queue;

        // metrics
        private final Timer toAdd;
        private final Timer createBox;
        private final Timer tryDeliver;
        private final Histogram queueSize;
        private final Histogram queueElements;
        private final Histogram midExecution;
        private final Histogram execution;

        public Deliverer(LinkedBlockingQueue<Optional<MessageSet>> toClient, LinkedBlockingQueue<Reply> toDeliverer, WriteDelay writeDelay, Config config) {

            createBox = METRICS.timer(MetricRegistry.name(DataRW.class, "createBox"));
            toAdd = METRICS.timer(MetricRegistry.name(DataRW.class, "toAdd"));
            tryDeliver = METRICS.timer(MetricRegistry.name(DataRW.class, "tryDeliver"));

            queueSize = METRICS.histogram(MetricRegistry.name(DataRW.class, "queueSize"));
            queueElements = METRICS.histogram(MetricRegistry.name(DataRW.class, "queueElements"));
            midExecution = METRICS.histogram(MetricRegistry.name(DataRW.class, "midExecution"));
            execution = METRICS.histogram(MetricRegistry.name(DataRW.class, "execution"));

            this.toDeliverer = toDeliverer;
            this.toSorter = new LinkedBlockingQueue<>();
            this.writeDelay = writeDelay;
            this.sorter = new Sorter(toClient, this.toSorter);
            this.queueType = config.getQueueType();
        }

        public void close() {
            this.sorter.interrupt();
        }

        @Override
        public void run() {
            // start sorter
            this.sorter.start();

            try {
                while (true) {
                    Reply reply = toDeliverer.take();

                    switch (reply.getReplyCase()) {
                        case INIT:
                            // create committed clock
                            Clock<ExceptionSet> committed = Clock.eclock(reply.getInit().getCommittedMap());
                            this.writeDelay.init(reply.getInit());

                            // create delivery queue that delivers by dep, conf or randomly
                            switch (this.queueType) {
                                case DEP:
                                    this.queue = new DepQueue(committed);
                                    break;
                                case CONF:
                                    this.queue = new ConfQueue(committed);
                                    break;
                                case RANDOM:
                                    this.queue = new RandomQueue();
                                    break;
                            }
                            break;

                        case COMMIT:
                            final Timer.Context createBoxContext = createBox.time();
                            QueueAddArgs args = createArgs(reply);
                            // update write delay
                            this.writeDelay.commit(args.getDot(), args.getConf());
                            midExecution.update(Metrics.midExecution(args.getDot()));
                            createBoxContext.stop();

                            final Timer.Context toAddContext = toAdd.time();
                            // hack: pass all info, and each queue will use the information it needs
                            queue.add(args);
                            toAddContext.stop();

                            final Timer.Context tryDeliverContext = tryDeliver.time();
                            List<CommittedQueueBox> toDeliver = queue.tryDeliver();
                            tryDeliverContext.stop();

                            queueSize.update(queue.size());
                            queueElements.update(queue.elements());

                            if (!toDeliver.isEmpty()) {
                                // update write delay
                                for (CommittedQueueBox b : toDeliver) {
                                    for (Dot d : b.getDots()) {
                                        this.writeDelay.deliver(d);
                                        execution.update(Metrics.endExecution(d));
                                    }
                                }
                                toSorter.put(toDeliver);
                            }
                            break;
                        default:
                            throw new RuntimeException("Reply type not supported:" + reply.getReplyCase());
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        private QueueAddArgs createArgs(Reply reply) {
            Commit commit = reply.getCommit();

            // fetch dot, dep, message and conf
            Dot dot = Dot.dot(commit.getDot());
            Clock<ExceptionSet> dep = Clock.eclock(commit.getDepMap());
            Message message = commit.getMessage();
            Clock<MaxInt> conf = Clock.vclock(commit.getConfMap());

            // create box
            CommittedQueueBox box = new CommittedQueueBox(dot, dep, message, conf);

            // create args
            QueueAddArgs args = new QueueAddArgs(dot, conf, box);
            return args;
        }
    }

    private class Sorter extends Thread {

        private final Logger LOGGER = VCDLogger.init(Sorter.class);
        private final LinkedBlockingQueue<Optional<MessageSet>> toClient;
        private final LinkedBlockingQueue<List<CommittedQueueBox>> toSorter;

        private final Timer sorting;
        private final Histogram toSort;
        private final Histogram components;

        public Sorter(LinkedBlockingQueue<Optional<MessageSet>> toClient, LinkedBlockingQueue<List<CommittedQueueBox>> toSorter) {
            this.toClient = toClient;
            this.toSorter = toSorter;
            this.sorting = METRICS.timer(MetricRegistry.name(DataRW.class, "sorting"));
            this.toSort = METRICS.histogram(MetricRegistry.name(DataRW.class, "toSort"));
            this.components = METRICS.histogram(MetricRegistry.name(DataRW.class, "components"));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    List<CommittedQueueBox> toDeliver = toSorter.take();
                    components.update(toDeliver.size());

                    final Timer.Context sortingContext = sorting.time();
                    MessageSet.Builder builder = MessageSet.newBuilder();
                    for (CommittedQueueBox boxToDeliver : toDeliver) {
                        toSort.update(boxToDeliver.size());
                        for (Message message : boxToDeliver.sortMessages()) {
                            builder.addMessages(message);
                        }
                    }
                    builder.setStatus(MessageSet.Status.DELIVERED);
                    MessageSet messageSet = builder.build();
                    sortingContext.stop();

                    toClient.put(Optional.of(messageSet));
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
    }
}
