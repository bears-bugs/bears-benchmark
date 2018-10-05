package org.imdea.vcd.queue.box;

import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.imdea.vcd.queue.clock.ExceptionSet;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.MaxInt;
import org.imdea.vcd.pb.Proto.Commit;
import org.imdea.vcd.pb.Proto.Message;
import org.imdea.vcd.queue.DepQueue;
import org.imdea.vcd.queue.QueueAddArgs;
import org.imdea.vcd.queue.clock.Dot;
import org.imdea.vcd.queue.clock.Dots;

/**
 *
 * @author Vitor Enes
 */
public class CommittedQueueBox implements QueueBox<CommittedQueueBox> {

    private final Dots dots;
    private final Clock<ExceptionSet> dep;
    private final MessageMap messageMap;

    public CommittedQueueBox(Dot dot, Clock<ExceptionSet> dep, Message message, Clock<MaxInt> conf) {
        this.dots = new Dots(dot);
        this.dep = dep;
        this.messageMap = new MessageMap(dot, message, conf);
    }

    public CommittedQueueBox(CommittedQueueBox commitDepBox) {
        this.dots = new Dots(commitDepBox.dots);
        this.dep = new Clock<>(commitDepBox.dep);
        this.messageMap = new MessageMap(commitDepBox.messageMap);
    }

    @Override
    public Dots getDots() {
        return this.dots;
    }

    @Override
    public boolean before(CommittedQueueBox o) {
        return o.dep.intersects(this.dots);
    }

    @Override
    public void merge(CommittedQueueBox o) {
        this.dots.merge(o.dots);
        this.dep.merge(o.dep);
        this.messageMap.merge(o.messageMap);
    }

    @Override
    public boolean canDeliver(Clock<ExceptionSet> delivered) {
        // delivered will be mutated, adding the dots from this box
        // - in case it can deliver, delivered will be the next queue clock
        delivered.addDots(this.dots);
        return delivered.equals(this.dep);
    }

    public List<Message> getMessages() {
        List<Message> result = new ArrayList<>();
        for (ArrayList<PerMessage> messages : this.messageMap.messages.values()) {
            for (PerMessage message : messages) {
                result.add(message.getMessage());
            }
        }
        return result;
    }

    @Override
    public List<Message> sortMessages() {
        List<Message> result = new ArrayList<>();
        for (ArrayList<PerMessage> messages : this.messageMap.messages.values()) {
            // sort for each color
            result.addAll(sortPerColor(messages));
        }
        return result;
    }

    @Override
    public int size() {
        return this.dots.size();
    }

    private List<Message> sortPerColor(List<PerMessage> messages) {
        // create queue to sort messages
        Integer nodeNumber = messages.get(0).getConf().size();
        DepQueue<DeliveredQueueBox> queue = new DepQueue<>(nodeNumber);

        // add all to the queue
        for (PerMessage message : messages) {
            DeliveredQueueBox box = new DeliveredQueueBox(message);
            QueueAddArgs args = new QueueAddArgs(null, null, box);
            queue.add(args);
        }

        // take all messages in the queue
        List<Message> result = new ArrayList<>();
        for (DeliveredQueueBox box : queue.toList()) {
            result.addAll(box.sortMessages());
        }
        return result;
    }

    @Override
    public String toString() {
        return dots + " " + dep;
    }

    @Override
    public Object clone() {
        CommittedQueueBox committedQueueBox = new CommittedQueueBox(this);
        return committedQueueBox;
    }

    private class MessageMap {

        private final HashMap<ByteString, ArrayList<PerMessage>> messages;

        public MessageMap(Dot dot, Commit commit) {
            this(dot, commit.getMessage(), Clock.vclock(commit.getConfMap()));
        }

        public MessageMap(Dot dot, Message message, Clock<MaxInt> conf) {
            this.messages = new HashMap<>();
            PerMessage p = new PerMessage(dot, message, conf);
            if (message.getHashesCount() > 1) {
                throw new RuntimeException("Number of hashes is bigger than 1");
            }
            this.messages.put(message.getHashes(0), new ArrayList<>(Arrays.asList(p)));
        }

        public MessageMap(MessageMap messageMap) {
            this.messages = new HashMap<>();
            for (Map.Entry<ByteString, ArrayList<PerMessage>> entry : messageMap.messages.entrySet()) {
                ArrayList<PerMessage> perMessageList = new ArrayList<>();
                for (PerMessage perMessage : entry.getValue()) {
                    perMessageList.add((PerMessage) perMessage.clone());
                }
                this.messages.put(entry.getKey(), perMessageList);
            }
        }

        public void merge(MessageMap o) {
            BiFunction<ArrayList<PerMessage>, ArrayList<PerMessage>, ArrayList<PerMessage>> f = (a, b) -> {
                a.addAll(b);
                return a;
            };
            for (Map.Entry<ByteString, ArrayList<PerMessage>> entry : o.messages.entrySet()) {
                this.messages.merge(entry.getKey(), entry.getValue(), f);
            }
        }

        @Override
        public Object clone() {
            MessageMap messageMap = new MessageMap(this);
            return messageMap;
        }
    }
}
