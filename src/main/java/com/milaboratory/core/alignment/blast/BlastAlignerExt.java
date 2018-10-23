package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.Sequence;

public class BlastAlignerExt<S extends Sequence<S>> extends BlastAlignerExtAbstract<S, BlastHitExt<S>> {
    public BlastAlignerExt(BlastDB database) {
        super(database);
    }

    public BlastAlignerExt(BlastDB database, BlastAlignerParameters parameters) {
        super(database, parameters);
    }

    @Override
    protected BlastHitExt<S> createHit(Alignment alignment, double score, double bitScore, double eValue, Range subjectRange, String subjectId, String subjectTitle) {
        return new BlastHitExt<>(alignment, score, bitScore, eValue, subjectRange, subjectId, subjectTitle);
    }
}
