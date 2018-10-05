package org.imdea.vcd.queue.clock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Vitor Enes
 */
public class MaxInt implements IntSet<MaxInt> {

    private static final MaxInt BOTTOM = new MaxInt();

    private Long seq;

    public MaxInt() {
        this.seq = 0L;
    }

    public MaxInt(Long seq) {
        this.seq = seq;
    }

    public MaxInt(MaxInt maxInt) {
        this.seq = maxInt.seq;
    }

    public ExceptionSet toExceptionSet() {
        ExceptionSet exceptionSet = new ExceptionSet(this.seq);
        return exceptionSet;
    }

    @Override
    public boolean isBottom() {
        return this.equals(BOTTOM);
    }

    @Override
    public boolean contains(Long seq) {
        return seq <= this.seq;
    }

    @Override
    public boolean containsAll(Long seq) {
        return contains(seq);
    }

    @Override
    public void add(Long seq) {
        this.seq = Long.max(this.seq, seq);
    }

    @Override
    public void merge(MaxInt o) {
        this.seq = Long.max(this.seq, o.seq);
    }

    @Override
    public List<Long> subtract(MaxInt b) {
        List<Long> result = new ArrayList<>();

        // returns [b.seq + 1 .. this.seq]
        for (Long i = b.seq + 1; i <= this.seq; i++) {
            result.add(i);
        }

        return result;
    }

    @Override
    public boolean subtractIsBottom(MaxInt b) {
        return b.seq >= this.seq;
    }

    @Override
    public Long next() {
        return this.seq + 1;
    }

    @Override
    public Long current() {
        return this.seq;
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o) {
            return true;
        }
        // null check
        if (o == null) {
            return false;
        }
        // type check and cast
        if (getClass() != o.getClass()) {
            return false;
        }
        MaxInt t = (MaxInt) o;
        return Objects.equals(this.seq, t.seq);
    }

    @Override
    public String toString() {
        return this.seq.toString();
    }

    @Override
    public Object clone() {
        MaxInt maxInt = new MaxInt(this);
        return maxInt;
    }
}
