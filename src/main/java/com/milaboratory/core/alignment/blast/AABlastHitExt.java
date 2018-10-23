package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.AminoAcidSequence;

public class AABlastHitExt extends BlastHitExt<AminoAcidSequence> {
    public AABlastHitExt(Alignment<AminoAcidSequence> alignment, double score, double bitScore, double eValue, Range subjectRange, String subjectId, String subjectTitle) {
        super(alignment, score, bitScore, eValue, subjectRange, subjectId, subjectTitle);
    }
}
