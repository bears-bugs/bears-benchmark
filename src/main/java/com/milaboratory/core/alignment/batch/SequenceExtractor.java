package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.sequence.Sequence;

/**
 * Interface for class to extract sequences from objects
 *
 * @param <O> object type
 * @param <S> sequence type
 */
public interface SequenceExtractor<O, S extends Sequence<S>> {
    S extract(O object);
}
