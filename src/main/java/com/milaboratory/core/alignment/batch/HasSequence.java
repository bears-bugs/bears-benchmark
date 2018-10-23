package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.sequence.Sequence;

/**
 * Interface for objects which natively have a sequence as their distinctive property.
 *
 * @param <S> type of sequence
 */
public interface HasSequence<S extends Sequence<S>> {
    S getSequence();
}
