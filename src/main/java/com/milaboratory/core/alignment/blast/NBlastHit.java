package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.NucleotideSequence;

public class NBlastHit<P> extends BlastHit<NucleotideSequence, P> {
    public NBlastHit(Alignment<NucleotideSequence> alignment, P recordPayload, BlastHit<NucleotideSequence, ?> hit) {
        super(alignment, recordPayload, hit);
    }

    public NBlastHit(Alignment<NucleotideSequence> alignment, P recordPayload, double score, double bitScore, double eValue, Range subjectRange, String subjectId, String subjectTitle) {
        super(alignment, recordPayload, score, bitScore, eValue, subjectRange, subjectId, subjectTitle);
    }
}
