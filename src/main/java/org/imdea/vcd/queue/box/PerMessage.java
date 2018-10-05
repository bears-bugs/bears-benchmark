package org.imdea.vcd.queue.box;

import org.imdea.vcd.pb.Proto.Message;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.Dot;
import org.imdea.vcd.queue.clock.MaxInt;

/**
 *
 * @author Vitor Enes
 */
public class PerMessage {

    private final Dot dot;
    private final Message message;
    private final Clock<MaxInt> conf;

    public PerMessage(Dot dot, Message message, Clock<MaxInt> conf) {
        this.dot = dot;
        this.message = message;
        this.conf = conf;
    }

    public PerMessage(PerMessage perMessage) {
        this.dot = new Dot(perMessage.dot);
        this.message = Message.newBuilder()
                .addAllHashes(perMessage.message.getHashesList())
                .setData(perMessage.message.getData())
                .build();
        this.conf = new Clock<>(perMessage.conf);
    }

    public Dot getDot() {
        return dot;
    }

    public Message getMessage() {
        return message;
    }

    public Clock<MaxInt> getConf() {
        return conf;
    }

    @Override
    public Object clone() {
        PerMessage perMessage = new PerMessage(this);
        return perMessage;
    }
}
