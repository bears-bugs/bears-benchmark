package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.sequence.Sequence;

/**
 * Represents aligner that can align a sequence against a set of other sequences.
 *
 * @param <S> sequence type
 * @param <P> type of record payload, used to store additional information sequence in base to simplify it's subsequent
 *            identification in result (e.g. {@link Integer} to just index sequences, or
 *            {@link com.milaboratory.core.alignment.blast.BlastDBRecord} etc...)
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public interface BatchAligner<S extends Sequence<S>, H extends AlignmentHit<S, ?>> {
    AlignmentResult<H> align(S sequence);

    AlignmentResult<H> align(S sequence, int from, int to);
}
