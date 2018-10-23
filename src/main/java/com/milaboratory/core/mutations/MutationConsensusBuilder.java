package com.milaboratory.core.mutations;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class MutationConsensusBuilder {
    final CoverageCounter coverage;
    final MutationsCounter mutations;

    public MutationConsensusBuilder(Range seqRange) {
        this.coverage = new CoverageCounter(seqRange);
        this.mutations = new MutationsCounter();
    }

    public void aggregate(Alignment<?> alignment, CoverageCounter.Provider coverageFunction) {
        coverage.aggregate(alignment.getSequence1Range(), coverageFunction);
        MutationsEnumerator enumerator = new MutationsEnumerator(alignment.getAbsoluteMutations());
        while (enumerator.next()) {
            mutations.adjust(alignment.getAbsoluteMutations(),
                    enumerator,
                    (int) coverageFunction.delta(Mutation.getPosition(enumerator.getOffset())));
        }
    }
}
