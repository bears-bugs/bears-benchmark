package org.imdea.vcd.queue;

import org.imdea.vcd.queue.box.QueueBox;
import java.util.List;

/**
 *
 * @author Vitor Enes
 * @param <E>
 */
public interface Queue<E extends QueueBox> {

    boolean isEmpty();

    void add(QueueAddArgs<E> args);

    List<E> tryDeliver();

    List<E> toList();

    int size();

    int elements();
}
