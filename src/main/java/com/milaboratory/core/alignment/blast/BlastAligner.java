package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.Sequence;

public class BlastAligner<S extends Sequence<S>, P> extends BlastAlignerAbstract<S, P, BlastHit<S, P>> {
    public BlastAligner() {
    }

    public BlastAligner(BlastAlignerParameters parameters) {
        super(parameters);
    }

    @Override
    protected BlastHit<S, P> createHit(Alignment<S> alignment, P recordPayload, BlastHit<S, ?> hit) {
        return new BlastHit<>(alignment, recordPayload, hit);
    }
}
