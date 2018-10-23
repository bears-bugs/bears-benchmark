package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.Sequence;

public class AlignmentHitImpl<S extends Sequence<S>, P> implements AlignmentHit<S, P> {
    final Alignment<S> alignment;
    final P recordPayload;

    public AlignmentHitImpl(Alignment<S> alignment, P recordPayload) {
        this.alignment = alignment;
        this.recordPayload = recordPayload;
    }

    @Override
    public Alignment<S> getAlignment() {
        return alignment;
    }

    @Override
    public P getRecordPayload() {
        return recordPayload;
    }
}
