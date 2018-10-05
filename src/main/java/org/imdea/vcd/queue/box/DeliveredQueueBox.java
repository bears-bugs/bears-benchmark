package org.imdea.vcd.queue.box;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.imdea.vcd.queue.clock.ExceptionSet;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.pb.Proto.Message;
import org.imdea.vcd.queue.clock.Dot;
import org.imdea.vcd.queue.clock.Dots;

/**
 *
 * @author Vitor Enes
 */
public class DeliveredQueueBox implements QueueBox<DeliveredQueueBox> {

    private final Dots dots;
    private final Clock<ExceptionSet> dep;
    private final TreeMap<Dot, Message> messageMap;

    public DeliveredQueueBox(DeliveredQueueBox deliveredQueueBox) {
        this.dots = new Dots(deliveredQueueBox.dots);
        this.dep = new Clock<>(deliveredQueueBox.dep);
        this.messageMap = new TreeMap<>(deliveredQueueBox.messageMap);
    }

    public DeliveredQueueBox(PerMessage perMessage) {
        this.dots = new Dots(perMessage.getDot());
        this.dep = Clock.eclock(perMessage.getConf());
        this.messageMap = new TreeMap<>();
        this.messageMap.put(perMessage.getDot(), perMessage.getMessage());
    }

    @Override
    public Dots getDots() {
        return this.dots;
    }

    @Override
    public boolean before(DeliveredQueueBox o) {
        return o.dep.intersects(this.dots);
    }

    @Override
    public void merge(DeliveredQueueBox o) {
        this.dots.merge(o.dots);
        this.dep.merge(o.dep);
        this.messageMap.putAll(o.messageMap);
    }

    @Override
    public boolean canDeliver(Clock<ExceptionSet> delivered) {
        // delivered will be mutated, adding the dots from this box
        // - in case it can deliver, delivered will be the next queue clock
        delivered.addDots(this.dots);
        return delivered.equals(this.dep);
    }

    @Override
    public List<Message> sortMessages() {
        List<Message> result = new ArrayList<>(messageMap.values());
        return result;
    }

    @Override
    public int size() {
        return this.dots.size();
    }

    @Override
    public String toString() {
        return dots + " " + dep;
    }

    @Override
    public Object clone() {
        DeliveredQueueBox deliveredQueueBox = new DeliveredQueueBox(this);
        return deliveredQueueBox;
    }
}
