package org.imdea.vcd.queue.box;

import java.util.List;
import org.imdea.vcd.pb.Proto.Message;
import org.imdea.vcd.queue.clock.Clock;
import org.imdea.vcd.queue.clock.Dots;
import org.imdea.vcd.queue.clock.ExceptionSet;

/**
 *
 * @author Vitor Enes
 * @param <T>
 */
public interface QueueBox<T> {

    Dots getDots();

    boolean before(T box);

    void merge(T box);

    boolean canDeliver(Clock<ExceptionSet> delivered);

    List<Message> sortMessages();

    int size();

    Object clone();
}
