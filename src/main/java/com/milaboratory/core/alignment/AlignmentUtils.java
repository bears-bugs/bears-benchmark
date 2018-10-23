/*
 * Copyright 2015 MiLaboratory.com
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
import com.milaboratory.core.mutations.MutationsUtil;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;

import static com.milaboratory.core.mutations.Mutation.*;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class AlignmentUtils {
    private AlignmentUtils() {
    }

    /**
     * Calculates score of alignment
     *
     * @param seq1      target sequence
     * @param mutations mutations (alignment)
     * @param scoring   scoring
     * @param <S>       sequence type
     * @return score
     */
    public static <S extends Sequence<S>> int calculateScore(S seq1, Mutations<S> mutations,
                                                             AlignmentScoring<S> scoring) {
        return calculateScore(seq1, new Range(0, seq1.size()), mutations, scoring);
    }

    /**
     * Calculates score of alignment
     *
     * @param seq1      target sequence
     * @param seq1Range aligned range
     * @param mutations mutations (alignment)
     * @param scoring   scoring
     * @param <S>       sequence type
     * @return score
     */
    public static <S extends Sequence<S>> int calculateScore(S seq1, Range seq1Range, Mutations<S> mutations,
                                                             AlignmentScoring<S> scoring) {
        if (scoring instanceof LinearGapAlignmentScoring)
            return calculateScore(seq1, seq1Range, mutations, (LinearGapAlignmentScoring<S>) scoring);
        else if (scoring instanceof AffineGapAlignmentScoring)
            return calculateScore(seq1, seq1Range, mutations, (AffineGapAlignmentScoring<S>) scoring);
        else
            throw new IllegalArgumentException("Unknown scoring type");
    }

    /**
     * Calculates score of alignment
     *
     * @param seq1      target sequence
     * @param seq1Range aligned range
     * @param mutations mutations (alignment)
     * @param scoring   scoring
     * @param <S>       sequence type
     * @return score
     */
    public static <S extends Sequence<S>> int calculateScore(S seq1, Range seq1Range, Mutations<S> mutations,
                                                             LinearGapAlignmentScoring<S> scoring) {
        if (!mutations.isEmpty() && mutations.getPositionByIndex(0) < seq1Range.getFrom() - 1)
            throw new IllegalArgumentException();

        final AlignmentIteratorForward<S> iterator = new AlignmentIteratorForward<>(mutations, seq1Range);

        int score = 0;

        while (iterator.advance()) {
            final int mut = iterator.getCurrentMutation();
            switch (Mutation.getRawTypeCode(mut)) {
                case RAW_MUTATION_TYPE_SUBSTITUTION:
                    score += scoring.getScore(Mutation.getFrom(mut), Mutation.getTo(mut));
                    break;

                case RAW_MUTATION_TYPE_DELETION:
                case RAW_MUTATION_TYPE_INSERTION:
                    score += scoring.getGapPenalty();
                    break;

                default:
                    byte c = seq1.codeAt(iterator.getSeq1Position());
                    score += scoring.getScore(c, c);
                    break;
            }
        }

        return score;
    }

    /**
     * Calculates score of alignment
     *
     * @param seq1      target sequence
     * @param seq1Range aligned range
     * @param mutations mutations (alignment)
     * @param scoring   scoring
     * @param <S>       sequence type
     * @return score
     */
    public static <S extends Sequence<S>> int calculateScore(S seq1, Range seq1Range, Mutations<S> mutations,
                                                             AffineGapAlignmentScoring<S> scoring) {
        if (!mutations.isEmpty() && mutations.getPositionByIndex(0) < seq1Range.getFrom() - 1)
            throw new IllegalArgumentException();

        final AlignmentIteratorForward<S> iterator = new AlignmentIteratorForward<>(mutations, seq1Range);

        int score = 0;

        int prevMut = NON_MUTATION;

        while (iterator.advance()) {
            final int mut = iterator.getCurrentMutation();
            switch (Mutation.getRawTypeCode(mut)) {
                case RAW_MUTATION_TYPE_SUBSTITUTION:
                    score += scoring.getScore(Mutation.getFrom(mut), Mutation.getTo(mut));
                    break;

                case RAW_MUTATION_TYPE_DELETION:
                    if (Mutation.isDeletion(prevMut) && Mutation.getPosition(prevMut) == iterator.getSeq1Position() - 1)
                        score += scoring.getGapExtensionPenalty();
                    else
                        score += scoring.getGapOpenPenalty();
                    break;

                case RAW_MUTATION_TYPE_INSERTION:
                    if (Mutation.isInsertion(prevMut) && Mutation.getPosition(prevMut) == iterator.getSeq1Position())
                        score += scoring.getGapExtensionPenalty();
                    else
                        score += scoring.getGapOpenPenalty();
                    break;

                default:
                    byte c = seq1.codeAt(iterator.getSeq1Position());
                    score += scoring.getScore(c, c);
                    break;
            }
            prevMut = mut;
        }

        return score;
    }

    /** Shifts indels to the left at homopolymer regions */
    public static <S extends Sequence<S>> Alignment<S> shiftIndelsAtHomopolymers(Alignment<S> alignment) {
        return new Alignment<>(alignment.sequence1,
                MutationsUtil.shiftIndelsAtHomopolymers(alignment.sequence1, alignment.sequence1Range.getFrom(), alignment.mutations),
                alignment.sequence1Range, alignment.sequence2Range, alignment.score);
    }


    public static <S extends Sequence<S>> String toStringSimple(S initialSequence, Mutations<S> mutations) {
        int pointer = 0;
        int mutPointer = 0;
        int mut;
        final Alphabet<S> alphabet = initialSequence.getAlphabet();
        StringBuilder sb1 = new StringBuilder(),
                sb2 = new StringBuilder();
        while (pointer < initialSequence.size() || mutPointer < mutations.size()) {
            if (mutPointer < mutations.size() && ((mut = mutations.getMutation(mutPointer)) >>> POSITION_OFFSET) <= pointer)
                switch (mut & MUTATION_TYPE_MASK) {
                    case RAW_MUTATION_TYPE_SUBSTITUTION:
                        if (((mut >> FROM_OFFSET) & LETTER_MASK) != initialSequence.codeAt(pointer))
                            throw new IllegalArgumentException("Mutation = " + Mutation.toString(initialSequence.getAlphabet(), mut) +
                                    " but seq[" + pointer + "]=" + initialSequence.symbolAt(pointer));
                        sb1.append(Character.toLowerCase(initialSequence.symbolAt(pointer++)));
                        sb2.append(Character.toLowerCase(alphabet.codeToSymbol((byte) (mut & LETTER_MASK))));
                        ++mutPointer;
                        break;
                    case RAW_MUTATION_TYPE_DELETION:
                        if (((mut >> FROM_OFFSET) & LETTER_MASK) != initialSequence.codeAt(pointer))
                            throw new IllegalArgumentException("Mutation = " + Mutation.toString(initialSequence.getAlphabet(), mut) +
                                    " but seq[" + pointer + "]=" + initialSequence.symbolAt(pointer));
                        sb1.append(initialSequence.symbolAt(pointer++));
                        sb2.append("-");
                        ++mutPointer;
                        break;
                    case RAW_MUTATION_TYPE_INSERTION:
                        sb1.append("-");
                        sb2.append(alphabet.codeToSymbol((byte) (mut & LETTER_MASK)));
                        ++mutPointer;
                        break;
                }
            else {
                sb1.append(initialSequence.symbolAt(pointer));
                sb2.append(initialSequence.symbolAt(pointer++));
            }
        }

        return sb1.toString() + "\n" + sb2.toString() + '\n';
    }

    public static <S extends Sequence<S>> S getAlignedSequence2Part(Alignment<S> alignment) {
        return alignment.getRelativeMutations().mutate(alignment.getSequence1().getRange(alignment.getSequence1Range()));
    }
}
