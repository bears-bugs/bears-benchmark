package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.AminoAcidSequence;

public class AABlastAligner<P> extends BlastAlignerAbstract<AminoAcidSequence, P, AABlastHit<P>> {
    public AABlastAligner() {
    }

    public AABlastAligner(BlastAlignerParameters parameters) {
        super(parameters);
    }

    @Override
    protected AABlastHit<P> createHit(Alignment<AminoAcidSequence> alignment, P recordPayload, BlastHit<AminoAcidSequence, ?> hit) {
        return new AABlastHit<>(alignment, recordPayload, hit);
    }
}
