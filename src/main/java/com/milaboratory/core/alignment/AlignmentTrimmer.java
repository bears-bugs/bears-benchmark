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

public class AlignmentTrimmer {
    /**
     * Try increase total alignment score by partially (or fully) trimming it from left side. If score can't be
     * increased the same alignment will be returned.
     *
     * @param alignment input alignment
     * @param scoring   scoring
     * @return resulting alignment
     */
    public static <S extends Sequence<S>> Alignment<S> leftTrimAlignment(Alignment<S> alignment,
                                                                         AlignmentScoring<S> scoring) {
        if (scoring instanceof LinearGapAlignmentScoring)
            return leftTrimAlignment(alignment, (LinearGapAlignmentScoring<S>) scoring);
        else if (scoring instanceof AffineGapAlignmentScoring)
            return leftTrimAlignment(alignment, (AffineGapAlignmentScoring<S>) scoring);
        else
            throw new IllegalArgumentException("Unknown scoring type");
    }

    /**
     * Try increase total alignment score by partially (or fully) trimming it from left side. If score can't be
     * increased the same alignment will be returned.
     *
     * LinearGapAlignmentScoring case.
     *
     * @param alignment input alignment
     * @param scoring   scoring
     * @return resulting alignment
     */
    public static <S extends Sequence<S>> Alignment<S> leftTrimAlignment(Alignment<S> alignment,
                                                                         LinearGapAlignmentScoring<S> scoring) {
        S seq1 = alignment.getSequence1();
        AlignmentIteratorForward<S> iterator = alignment.forwardIterator();

        int score = 0;
        int minScore = 1;
        int minSeq1Position = 0, minSeq2Position = 0, minMutPointer = 0;

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

            // score <= minScore to maximally trim alignment with several equal trimming options
            if (score <= 0 && score <= minScore) {
                minScore = score;
                minSeq1Position = Mutation.isInsertion(mut) ? iterator.getSeq1Position() : iterator.getSeq1Position() + 1;
                minSeq2Position = Mutation.isDeletion(mut) ? iterator.getSeq2Position() : iterator.getSeq2Position() + 1;
                minMutPointer = iterator.getMutationsPointer();
            }
        }

        if (minScore == 1)
            return alignment;

        Mutations<S> mutations = alignment.getAbsoluteMutations();

        return new Alignment<>(seq1, mutations.getRange(minMutPointer + 1, mutations.size()),
                new Range(minSeq1Position, alignment.getSequence1Range().getTo()),
                new Range(minSeq2Position, alignment.getSequence2Range().getTo()),
                score - minScore);
    }

    /**
     * Try increase total alignment score by partially (or fully) trimming it from left side. If score can't be
     * increased the same alignment will be returned.
     *
     * AffineGapAlignmentScoring case.
     *
     * @param alignment input alignment
     * @param scoring   scoring
     * @return resulting alignment
     */
    public static <S extends Sequence<S>> Alignment<S> leftTrimAlignment(Alignment<S> alignment,
                                                                         AffineGapAlignmentScoring<S> scoring) {
        S seq1 = alignment.getSequence1();
        AlignmentIteratorForward<S> iterator = alignment.forwardIterator();

        int score = 0;
        int minScore = 1;
        int minSeq1Position = 0, minSeq2Position = 0, minMutPointer = 0;

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

            // score <= minScore to maximally trim alignment with several equal trimming options
            if (score <= 0 && score <= minScore) {
                minScore = score;
                minSeq1Position = Mutation.isInsertion(mut) ? iterator.getSeq1Position() : iterator.getSeq1Position() + 1;
                minSeq2Position = Mutation.isDeletion(mut) ? iterator.getSeq2Position() : iterator.getSeq2Position() + 1;
                minMutPointer = iterator.getMutationsPointer();
            }
        }

        if (minScore == 1)
            return alignment;

        Mutations<S> mutations = alignment.getAbsoluteMutations();

        return new Alignment<>(seq1, mutations.getRange(minMutPointer + 1, mutations.size()),
                new Range(minSeq1Position, alignment.getSequence1Range().getTo()),
                new Range(minSeq2Position, alignment.getSequence2Range().getTo()),
                score - minScore);
    }

    /**
     * Try increase total alignment score by partially (or fully) trimming it from right side. If score can't be
     * increased the same alignment will be returned.
     *
     * @param alignment input alignment
     * @param scoring   scoring
     * @return resulting alignment
     */
    public static <S extends Sequence<S>> Alignment<S> rightTrimAlignment(Alignment<S> alignment,
                                                                         AlignmentScoring<S> scoring) {
        if (scoring instanceof LinearGapAlignmentScoring)
            return rightTrimAlignment(alignment, (LinearGapAlignmentScoring<S>) scoring);
        else if (scoring instanceof AffineGapAlignmentScoring)
            return rightTrimAlignment(alignment, (AffineGapAlignmentScoring<S>) scoring);
        else
            throw new IllegalArgumentException("Unknown scoring type");
    }

    /**
     * Try increase total alignment score by partially (or fully) trimming it from right side. If score can't be
     * increased the same alignment will be returned.
     *
     * LinearGapAlignmentScoring case.
     *
     * @param alignment input alignment
     * @param scoring   scoring
     * @return resulting alignment
     */
    public static <S extends Sequence<S>> Alignment<S> rightTrimAlignment(Alignment<S> alignment,
                                                                          LinearGapAlignmentScoring<S> scoring) {
        S seq1 = alignment.getSequence1();
        AlignmentIteratorReverse<S> iterator = alignment.reverseIterator();

        int score = 0;
        int minScore = 1;
        int minSeq1Position = 0, minSeq2Position = 0, minMutPointer = 0;

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

            // score <= minScore to maximally trim alignment with several equal trimming options
            if (score <= 0 && score <= minScore) {
                minScore = score;
                minSeq1Position = iterator.getSeq1Position();
                minSeq2Position = iterator.getSeq2Position();
                minMutPointer = iterator.getMutationsPointer();
            }
        }

        if (minScore == 1)
            return alignment;

        Mutations<S> mutations = alignment.getAbsoluteMutations();

        return new Alignment<>(seq1, mutations.getRange(0, minMutPointer),
                new Range(alignment.getSequence1Range().getFrom(), minSeq1Position),
                new Range(alignment.getSequence2Range().getFrom(), minSeq2Position),
                score - minScore);
    }

    /**
     * Try increase total alignment score by partially (or fully) trimming it from right side. If score can't be
     * increased the same alignment will be returned.
     *
     * LinearGapAlignmentScoring case.
     *
     * @param alignment input alignment
     * @param scoring   scoring
     * @return resulting alignment
     */
    public static <S extends Sequence<S>> Alignment<S> rightTrimAlignment(Alignment<S> alignment,
                                                                          AffineGapAlignmentScoring<S> scoring) {
        S seq1 = alignment.getSequence1();
        AlignmentIteratorReverse<S> iterator = alignment.reverseIterator();

        int score = 0;
        int minScore = 1;
        int minSeq1Position = 0, minSeq2Position = 0, minMutPointer = 0;

        int prevMut = NON_MUTATION;

        while (iterator.advance()) {
            final int mut = iterator.getCurrentMutation();
            switch (Mutation.getRawTypeCode(mut)) {
                case RAW_MUTATION_TYPE_SUBSTITUTION:
                    score += scoring.getScore(Mutation.getFrom(mut), Mutation.getTo(mut));
                    break;

                case RAW_MUTATION_TYPE_DELETION:
                    if (Mutation.isDeletion(prevMut) && Mutation.getPosition(prevMut) == iterator.getSeq1Position() + 1)
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

            // score <= minScore to maximally trim alignment with several equal trimming options
            if (score <= 0 && score <= minScore) {
                minScore = score;
                minSeq1Position = iterator.getSeq1Position();
                minSeq2Position = iterator.getSeq2Position();
                minMutPointer = iterator.getMutationsPointer();
            }
        }

        if (minScore == 1)
            return alignment;

        Mutations<S> mutations = alignment.getAbsoluteMutations();

        return new Alignment<>(seq1, mutations.getRange(0, minMutPointer),
                new Range(alignment.getSequence1Range().getFrom(), minSeq1Position),
                new Range(alignment.getSequence2Range().getFrom(), minSeq2Position),
                score - minScore);
    }
}
