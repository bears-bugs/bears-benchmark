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
public class AlignmentIteratorReverse<S extends Sequence<S>> implements AlignmentIterator<S> {
    /**
     * Alignment mutations
     */
    private final Mutations<S> mutations;
    /**
     * Leftmost aligned letter
     */
    private final int seq1From;
    /**
     * Points to current position in sequence1; if on insertion points to the next position after insertion point
     */
    private int seq1Position;
    /**
     * Points to current position in sequence2; if deletion points to the next position after deletion
     */
    private int seq2Position;
    /**
     * Points to current mutation or if currentMutation == {@link Mutation#NON_MUTATION} points to the next mutation.
     */
    private int mutationsPointer;
    /**
     * Current mutation or {@link Mutation#NON_MUTATION} if on match
     */
    private int currentMutation;

    /**
     * Create alignment iterator
     *
     * @param mutations mutations (alignment)
     * @param seq1Range seq1 aligned range
     */
    public AlignmentIteratorReverse(final Mutations<S> mutations, final Range seq1Range) {
        this(mutations, seq1Range, 0);
    }

    /**
     * Create alignment iterator
     *
     * @param mutations    mutations (alignment)
     * @param seq1Range    seq1 aligned range
     * @param seq2Position seq2 position after last aligned letter
     */
    public AlignmentIteratorReverse(final Mutations<S> mutations, final Range seq1Range, int seq2Position) {
        this.mutations = mutations;
        this.seq1From = seq1Range.getFrom();
        this.mutationsPointer = mutations.size() - 1;
        this.seq1Position = seq1Range.getTo();
        this.seq2Position = seq2Position;
    }

    /**
     * Advance to the next alignment position
     *
     * @return {@literal true} if iteration successful; {@literal false} if iteration ended
     */
    public boolean advance() {
        // Stack / register variable for performance
        int mut = mutationsPointer >= 0 ? mutations.getMutation(mutationsPointer) : NON_MUTATION;

        // This if is required to process all non-insertions with the same
        // position, before other mutation types and match
        if (getRawTypeCode(mut) == RAW_MUTATION_TYPE_INSERTION)
            mut = getPosition(mut) != seq1Position ? NON_MUTATION : mut;
        else if (getPosition(mut) != seq1Position - 1)
            mut = NON_MUTATION;

        if (mut != NON_MUTATION) {
            switch (Mutation.getRawTypeCode(mut)) {
                case RAW_MUTATION_TYPE_SUBSTITUTION:
                    --seq1Position;
                    --seq2Position;
                    break;

                case RAW_MUTATION_TYPE_DELETION:
                    --seq1Position;
                    break;

                case RAW_MUTATION_TYPE_INSERTION:
                    --seq2Position;
                    break;
            }
            --mutationsPointer;
        } else {
            --seq1Position;
            --seq2Position;
        }

        currentMutation = mut;

        return seq1Position >= seq1From;
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
        return mutationsPointer + 1;
    }

    @Override
    public int getCurrentMutation() {
        return currentMutation;
    }
}
