package com.milaboratory.core.mutations;

import com.milaboratory.core.Range;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class CoverageCounter {
    final int refFrom, refTo;
    final long[] counters;

    public CoverageCounter(Range seqRange) {
        this(seqRange.getFrom(), seqRange.getTo());
    }

    public CoverageCounter(int refFrom, int refTo) {
        this.refFrom = refFrom;
        this.refTo = refTo;
        this.counters = new long[refTo - refFrom];
    }

    public void aggregate(final Range r, final Provider provider) {
        final int from = r.getFrom(), to = r.getTo();
        if (from < refFrom || to > refTo)
            throw new IndexOutOfBoundsException();
        for (int i = from; i < to; ++i)
            counters[i] += provider.delta(i);
    }

    public void aggregate(final Range r, final int delta) {
        aggregate(r, constantDelta(delta));
    }

    public long count(int position) {
        return counters[position];
    }

    public interface Provider {
        long delta(int position);
    }

    public static Provider constantDelta(final int delta) {
        return new Provider() {
            @Override
            public long delta(int position) {
                return delta;
            }
        };
    }
}
