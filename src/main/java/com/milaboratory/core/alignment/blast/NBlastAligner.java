package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.NucleotideSequence;

public class NBlastAligner<P> extends BlastAlignerAbstract<NucleotideSequence, P, NBlastHit<P>> {
    public NBlastAligner() {
    }

    public NBlastAligner(BlastAlignerParameters parameters) {
        super(parameters);
    }

    @Override
    protected NBlastHit<P> createHit(Alignment<NucleotideSequence> alignment, P recordPayload, BlastHit<NucleotideSequence, ?> hit) {
        return new NBlastHit<>(alignment, recordPayload, hit);
    }
}
