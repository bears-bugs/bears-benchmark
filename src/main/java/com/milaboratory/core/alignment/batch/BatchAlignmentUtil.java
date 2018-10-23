package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.sequence.Sequence;

import java.util.Comparator;

public class BatchAlignmentUtil {
    public static final Comparator<AlignmentHit<?, ?>> ALIGNMENT_SCORE_HIT_COMPARATOR = new Comparator<AlignmentHit<?, ?>>() {
        @Override
        public int compare(AlignmentHit<?, ?> o1, AlignmentHit<?, ?> o2) {
            return Float.compare(o2.getAlignment().getScore(), o1.getAlignment().getScore());
        }
    };

    public static final SequenceExtractor DUMMY_EXTRACTOR = new SequenceExtractor() {
        @Override
        public Sequence extract(Object object) {
            return ((HasSequence) object).getSequence();
        }
    };
}
