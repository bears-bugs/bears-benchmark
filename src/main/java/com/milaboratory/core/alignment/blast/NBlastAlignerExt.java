package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.NucleotideSequence;

public class NBlastAlignerExt extends BlastAlignerExtAbstract<NucleotideSequence, NBlastHitExt> {
    public NBlastAlignerExt(BlastDB database) {
        super(database);
    }

    public NBlastAlignerExt(BlastDB database, BlastAlignerParameters parameters) {
        super(database, parameters);
    }

    @Override
    protected NBlastHitExt createHit(Alignment<NucleotideSequence> alignment, double score, double bitScore,
                                     double eValue, Range subjectRange, String subjectId, String subjectTitle) {
        return new NBlastHitExt(alignment, score, bitScore, eValue, subjectRange, subjectId, subjectTitle);
    }
}
