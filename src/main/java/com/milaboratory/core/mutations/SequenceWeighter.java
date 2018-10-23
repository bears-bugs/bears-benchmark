package com.milaboratory.core.mutations;

import com.milaboratory.core.sequence.SequenceQuality;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public interface SequenceWeighter {
    int getMutationWeight(SequenceQuality quality, int position, int mutation, int[] mutations);

    int getCoverageWeight(SequenceQuality quality, int position);
}
