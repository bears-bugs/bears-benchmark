package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.Sequence;

public class BlastHitExt<S extends Sequence<S>> extends BlastHit<S, String> {
    public BlastHitExt(Alignment<S> alignment, double score, double bitScore, double eValue, Range subjectRange, String subjectId, String subjectTitle) {
        super(alignment, subjectTitle, score, bitScore, eValue,
                subjectRange, subjectId, subjectTitle);
    }

    public String getTitle() {
        return getRecordPayload();
    }
}
