/*
 * Copyright 2016 MiLaboratory.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.milaboratory.core.alignment;

import com.milaboratory.core.Range;
import com.milaboratory.core.mutations.Mutation;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.sequence.Sequence;

import static com.milaboratory.core.mutations.Mutation.*;

/**
 * Iterates over all positions in alignment
 *
 * @param <S> sequence type
 */
public final class AlignmentIteratorForward<S extends Sequence<S>> implements AlignmentIterator<S> {
    /**
     * Alignment mutations
     */
    private final Mutations<S> mutations;
    /**
     * Next after the rightmost aligned letter
     */
    private final int seq1To;
    /**
     * Points to current position in sequence1; if on insertion points to the next position after insertion point
     */
    private int seq1Position;
    /**
     * Points to current position in sequence2; if deletion points to the previous position before deletion
     */
    private int seq2Position;
    /**
     * Points to current mutation or if currentMutation == {@link Mutation#NON_MUTATION} points to the next mutation.
     */
    private int mutationsPointer = 0;
    /**
     * Current mutation or {@link Mutation#NON_MUTATION} if on match
     */
    private int currentMutation;

    /**
     * Create alignment iterator
     *
     * @param mutations mutations (alignment)
     * @param seq1Range aligned range
     */
    public AlignmentIteratorForward(final Mutations<S> mutations, final Range seq1Range) {
        this(mutations, seq1Range, 0);
    }

    /**
     * Create alignment iterator
     *
     * @param mutations    mutations (alignment)
     * @param seq1Range    aligned range
     * @param seq2Position seq2 start position
     */
    public AlignmentIteratorForward(final Mutations<S> mutations, final Range seq1Range, int seq2Position) {
        if (mutations.size() > 0 && (mutations.getPositionByIndex(0) < seq1Range.getFrom() ||
                mutations.getPositionByIndex(mutations.size() - 1) > seq1Range.getTo()))
            throw new IllegalArgumentException("Mutations outside target range.");

        this.mutations = mutations;
        this.seq1To = seq1Range.getTo();
        this.seq1Position = seq1Range.getFrom();

        // Signal that iterator is not initialized (will be initialized after first call to the advance() method)
        this.currentMutation = NON_MUTATION_1;

        this.seq2Position = seq2Position;
    }

    /**
     * Advance to the next alignment position
     *
     * @return {@literal true} if iteration successful; {@literal false} if iteration ended
     */
    @Override
    public boolean advance() {
        if (currentMutation != NON_MUTATION_1) { // Prevents invocation on the first execution of the method

            // This will be executed starting from the second call of the advance() method
            if (currentMutation != NON_MUTATION) {
                switch (Mutation.getRawTypeCode(currentMutation)) {
                    case RAW_MUTATION_TYPE_SUBSTITUTION:
                        ++seq1Position;
                        ++seq2Position;
                        break;

                    case RAW_MUTATION_TYPE_DELETION:
                        ++seq1Position;
                        break;

                    case RAW_MUTATION_TYPE_INSERTION:
                        ++seq2Position;
                        break;
                }
                ++mutationsPointer;
            } else {
                ++seq1Position;
                ++seq2Position;
            }

        }

        // Setting current state
        currentMutation = mutationsPointer < mutations.size() &&
                mutations.getPositionByIndex(mutationsPointer) == seq1Position ? mutations.getMutation(mutationsPointer) :
                NON_MUTATION;

        return seq1Position < seq1To || currentMutation != NON_MUTATION;
    }

    @Override
    public int getSeq1Position() {
        return seq1Position;
    }

    @Override
    public int getSeq2Position() {
        return seq2Position;
    }

    @Override
    public int getMutationsPointer() {
        return mutationsPointer;
    }

    @Override
    public int getCurrentMutation() {
        return currentMutation;
    }
}
