package com.milaboratory.core.alignment.batch;

import java.util.List;

public class PipedAlignmentResultImpl<H extends AlignmentHit<?, ?>, Q>
        extends AlignmentResultImpl<H>
        implements PipedAlignmentResult<H, Q> {
    final Q query;

    public PipedAlignmentResultImpl(List<H> alignmentHits, Q query) {
        super(alignmentHits);
        this.query = query;
    }

    @Override
    public Q getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return query + " -> " + super.toString();
    }
}
