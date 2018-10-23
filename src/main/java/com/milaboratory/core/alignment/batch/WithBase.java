package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.sequence.Sequence;

/**
 * Interface for aligners with self-managed database (user can directly add subject sequences before running alignment)
 *
 * @param <S> type of sequence
 * @param <P> type of record payload, used to store additional information along with sequence to simplify it's
 *            subsequent identification in result (e.g. {@link Integer} to just index sequences.
 */
public interface WithBase<S extends Sequence<S>, P> {
    /**
     * Adds a record to the base of this aligner (a set of subject sequences that this instance aligns queries
     * with).
     *
     * @param sequence sequence
     * @param payload  payload to store additional information with this record (can be retrieved from resulting {@link
     *                 AlignmentHit})
     */
    void addReference(S sequence, P payload);
}
