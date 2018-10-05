package org.imdea.vcd.queue.clock;

import java.util.List;

/**
 *
 * @author Vitor Enes
 * @param <T>
 */
public interface IntSet<T> {

    boolean isBottom();

    boolean contains(Long seq);

    boolean containsAll(Long seq);

    void add(Long seq);

    void merge(T o);

    List<Long> subtract(T o);

    boolean subtractIsBottom(T o);

    Long current();

    Long next();

    Object clone();
}
