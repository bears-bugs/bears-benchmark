package com.milaboratory.core.alignment.batch;

import cc.redberry.pipe.OutputPort;
import com.milaboratory.core.sequence.Sequence;

/**
 * Represents aligner that can align a sequence against a set of other sequences. This type of aligner works only as a
 * pipe processor.
 *
 * @param <S> sequence type
 * @param <H> hit class
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public interface PipedBatchAligner<S extends Sequence<S>, H extends AlignmentHit<? extends S, ?>> {
    /**
     * Starts processing of input sequences and returns pipe of results.
     *
     * @param input     pipe of queries
     * @param extractor extractor of sequences from query object
     * @param <Q>       type of query object
     * @return pipe of alignment results
     */
    <Q> OutputPort<? extends PipedAlignmentResult<H, Q>> align(OutputPort<Q> input, SequenceExtractor<Q, S> extractor);

    /**
     * Starts processing of input sequences and returns pipe of results.
     *
     * @param input pipe of queries
     * @param <Q>   type of query objects
     * @return pipe of alignment results
     */
    <Q extends HasSequence<S>> OutputPort<? extends PipedAlignmentResult<H, Q>> align(OutputPort<Q> input);
}
