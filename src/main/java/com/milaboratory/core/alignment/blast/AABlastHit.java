package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.AminoAcidSequence;

public class AABlastHit<P> extends BlastHit<AminoAcidSequence, P> {
    public AABlastHit(Alignment<AminoAcidSequence> alignment, P recordPayload, BlastHit<AminoAcidSequence, ?> hit) {
        super(alignment, recordPayload, hit);
    }

    public AABlastHit(Alignment<AminoAcidSequence> alignment, P recordPayload, double score, double bitScore, double eValue, Range subjectRange, String subjectId, String subjectTitle) {
        super(alignment, recordPayload, score, bitScore, eValue, subjectRange, subjectId, subjectTitle);
    }
}
