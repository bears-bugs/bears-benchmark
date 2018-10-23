package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.AminoAcidSequence;

public class AABlastAlignerExt extends BlastAlignerExtAbstract<AminoAcidSequence, AABlastHitExt> {
    public AABlastAlignerExt(BlastDB database) {
        super(database);
    }

    public AABlastAlignerExt(BlastDB database, BlastAlignerParameters parameters) {
        super(database, parameters);
    }

    @Override
    protected AABlastHitExt createHit(Alignment<AminoAcidSequence> alignment, double score, double bitScore,
                                      double eValue, Range subjectRange, String subjectId, String subjectTitle) {
        return new AABlastHitExt(alignment, score, bitScore, eValue, subjectRange, subjectId, subjectTitle);
    }
}
