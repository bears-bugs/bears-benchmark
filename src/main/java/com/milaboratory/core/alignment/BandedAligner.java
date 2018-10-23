package com.milaboratory.core.alignment;

import com.milaboratory.core.sequence.NucleotideSequence;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class BandedAligner {

    /**
     * Classical Banded Alignment.
     *
     * Both sequences must be highly similar.
     *
     * Align 2 sequence completely (i.e. while first sequence will be aligned against whole second sequence).
     *
     * @param scoring scoring system
     * @param seq1    first sequence
     * @param seq2    second sequence
     * @param offset1 offset in first sequence
     * @param length1 length of first sequence's part to be aligned
     * @param offset2 offset in second sequence
     * @param length2 length of second sequence's part to be aligned
     * @param width   width of banded alignment matrix. In other terms max allowed number of indels
     */
    public static Alignment<NucleotideSequence> alignGlobal(final AlignmentScoring<NucleotideSequence> scoring,
                                                            final NucleotideSequence seq1, final NucleotideSequence seq2,
                                                            final int offset1, final int length1,
                                                            final int offset2, final int length2,
                                                            final int width) {
        if (scoring instanceof AffineGapAlignmentScoring)
            return BandedAffineAligner.align((AffineGapAlignmentScoring<NucleotideSequence>) scoring, seq1, seq2, offset1, length1, offset2, length2, width);
        else
            return BandedLinearAligner.align((LinearGapAlignmentScoring<NucleotideSequence>) scoring, seq1, seq2, offset1, length1, offset2, length2, width);
    }
}
