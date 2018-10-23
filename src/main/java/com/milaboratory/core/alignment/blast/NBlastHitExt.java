package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.NucleotideSequence;

public class NBlastHitExt extends BlastHitExt<NucleotideSequence> {
    public NBlastHitExt(Alignment<NucleotideSequence> alignment, double score, double bitScore, double eValue, Range subjectRange, String subjectId, String subjectTitle) {
        super(alignment, score, bitScore, eValue, subjectRange, subjectId, subjectTitle);
    }
}
