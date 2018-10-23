package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.sequence.Sequence;

/**
 * {@link PipedBatchAligner} with self-managed database (user can directly add subject sequences before running alignment).
 *
 * @param <S> type of sequence
 * @param <P> type of record payload, used to store additional information along with sequence to simplify it's
 *            subsequent identification in result (e.g. {@link Integer} to just index sequences.
 * @param <H> hit class
 */
public interface PipedBatchAlignerWithBase<S extends Sequence<S>, P, H extends AlignmentHit<? extends S, ? extends P>>
        extends PipedBatchAligner<S, H>, WithBase<S, P> {
}
