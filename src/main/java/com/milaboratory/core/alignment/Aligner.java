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
import com.milaboratory.core.mutations.MutationsBuilder;
import com.milaboratory.core.sequence.Sequence;

public final class Aligner {
    private Aligner() {
    }

    public static <S extends Sequence<S>> int alignOnlySubstitutions0(S seq1, S seq2, int seq1From, int seq1Length,
                                                                      int seq2From, int seq2Length,
                                                                      AlignmentScoring<S> scoring,
                                                                      MutationsBuilder<S> builder) {
        if (seq1Length != seq2Length)
            throw new IllegalArgumentException("Size of 'seq1' and 'seq2' sequences must be the same.");
        int score = 0;
        byte c1, c2;
        for (int i = 0; i < seq1Length; ++i) {
            if ((c1 = seq1.codeAt(seq1From + i)) != (c2 = seq2.codeAt(seq2From + i)))
                builder.appendSubstitution(seq1From + i, c1, c2);
            score += scoring.getScore(c1, c2);
        }
        return score;
    }

    public static <S extends Sequence<S>> Alignment<S> alignOnlySubstitutions(S seq1, S seq2,
                                                                              int seq1From, int seq1Length,
                                                                              int seq2From, int seq2Length,
                                                                              AlignmentScoring<S> scoring) {
        MutationsBuilder<S> builder = new MutationsBuilder<>(seq1.getAlphabet());
        int score = alignOnlySubstitutions0(seq1, seq2, seq1From, seq1Length, seq2From, seq2Length, scoring, builder);
        return new Alignment<>(seq1, builder.createAndDestroy(), new Range(seq1From, seq1From + seq1Length),
                new Range(seq2From, seq2From + seq2Length), score);
    }

    public static <S extends Sequence<S>> Alignment<S> alignOnlySubstitutions(S from, S to) {
        if (from.getAlphabet() != to.getAlphabet())
            throw new IllegalArgumentException();
        if (from.size() != to.size())
            throw new IllegalArgumentException("Size of 'from' and 'to' sequences must be the same.");
        MutationsBuilder<S> builder = new MutationsBuilder<S>(from.getAlphabet());
        int score = 0;
        for (int i = 0; i < from.size(); ++i)
            if (from.codeAt(i) != to.codeAt(i)) {
                builder.appendSubstitution(i, from.codeAt(i), to.codeAt(i));
                --score;
            } else ++score;
        Range range = new Range(0, from.size());
        return new Alignment<>(from, builder.createAndDestroy(), range, range, score);
    }

    public static <S extends Sequence<S>> Alignment<S> alignGlobal(AlignmentScoring<S> alignmentScoring,
                                                                   S seq1, S seq2,
                                                                   int offset1, int length1,
                                                                   int offset2, int length2) {
        Range seq1Range = new Range(offset1, offset1 + length1),
                seq2Range = new Range(offset2, offset2 + length2);
        Alignment<S> al = Aligner.alignGlobal(
                alignmentScoring,
                seq1.getRange(seq1Range),
                seq2.getRange(seq2Range));
        return new Alignment<>(seq1, al.getAbsoluteMutations().move(offset1),
                seq1Range, seq2Range,
                al.getScore());
    }

    /**
     * Performs global alignment
     *
     * @param alignmentScoring scoring system
     * @param seq1             first sequence
     * @param seq2             second sequence
     * @return array of mutations
     */
    public static <S extends Sequence<S>> Alignment<S> alignGlobal(AlignmentScoring<S> alignmentScoring,
                                                                   S seq1, S seq2) {
        if (alignmentScoring instanceof AffineGapAlignmentScoring)
            return alignGlobalAffine((AffineGapAlignmentScoring<S>) alignmentScoring, seq1, seq2);
        if (alignmentScoring instanceof LinearGapAlignmentScoring)
            return alignGlobalLinear((LinearGapAlignmentScoring<S>) alignmentScoring, seq1, seq2);
        throw new RuntimeException("Unknown scoring type.");
    }

    /**
     * Performs global alignment using Linear scoring system (penalty exists only for gap)
     *
     * @param scoring linear scoring system
     * @param seq1    first sequence
     * @param seq2    second sequence
     * @return array of mutations
     */
    public static <S extends Sequence<S>> Alignment<S> alignGlobalLinear(LinearGapAlignmentScoring scoring,
                                                                         S seq1, S seq2) {
        if (seq1.getAlphabet() != seq2.getAlphabet() ||
                seq1.getAlphabet() != scoring.getAlphabet())
            throw new IllegalArgumentException("Different alphabets.");

        int size1 = seq1.size() + 1,
                size2 = seq2.size() + 1;
        int[] matrix = new int[size1 * (seq2.size() + 1)];

        for (int i = 0; i < size2; ++i)
            matrix[i] = scoring.getGapPenalty() * i;

        for (int j = 1; j < size1; ++j)
            matrix[size2 * j] = scoring.getGapPenalty() * j;

        int i1, i2,
                match, delete, insert;

        for (i1 = 0; i1 < seq1.size(); ++i1)
            for (i2 = 0; i2 < seq2.size(); ++i2) {
                match = matrix[i1 * size2 + i2] +
                        scoring.getScore(seq1.codeAt(i1), seq2.codeAt(i2));
                delete = matrix[i1 * size2 + i2 + 1] + scoring.getGapPenalty();
                insert = matrix[(i1 + 1) * size2 + i2] + scoring.getGapPenalty();
                matrix[(i1 + 1) * size2 + i2 + 1] = Math.max(match, Math.max(delete, insert));
            }

        MutationsBuilder<S> builder = new MutationsBuilder<>(seq1.getAlphabet(), true);

        i1 = seq1.size() - 1;
        i2 = seq2.size() - 1;
        int score = matrix[(i1 + 1) * size2 + i2 + 1];

        while (i1 >= 0 || i2 >= 0) {
            if (i1 >= 0 && i2 >= 0 &&
                    matrix[(i1 + 1) * size2 + i2 + 1] == matrix[i1 * size2 + i2] +
                            scoring.getScore(seq1.codeAt(i1), seq2.codeAt(i2))) {
                if (seq1.codeAt(i1) != seq2.codeAt(i2))
                    builder.appendSubstitution(i1, seq1.codeAt(i1), seq2.codeAt(i2));
                --i1;
                --i2;
            } else if (i1 >= 0 &&
                    matrix[(i1 + 1) * size2 + i2 + 1] ==
                            matrix[i1 * size2 + i2 + 1] + scoring.getGapPenalty()) {
                builder.appendDeletion(i1, seq1.codeAt(i1));
                i1--;
            } else if (i2 >= 0 &&
                    matrix[(i1 + 1) * size2 + i2 + 1] ==
                            matrix[(i1 + 1) * size2 + i2] + scoring.getGapPenalty()) {
                builder.appendInsertion(i1 + 1, seq2.codeAt(i2));
                i2--;
            } else
                throw new RuntimeException();
        }

        return new Alignment<>(seq1, builder.createAndDestroy(), new Range(0, seq1.size()), new Range(0, seq2.size()),
                score);
    }

    public static final int MIN_VALUE = Integer.MIN_VALUE / 2;

    static final class Matrix {
        final int nRows, nColumns;
        final int[] data;

        Matrix(int nRows, int nColumns) {
            this.nRows = nRows;
            this.nColumns = nColumns;
            this.data = new int[nRows * nColumns];
        }

        int get(int row, int col) {
            return data[nColumns * row + col];
        }

        void set(int row, int col, int value) {
            data[nColumns * row + col] = value;
        }
    }

    private static int max(int a, int b) {
        return Math.max(a, b);
    }

    private static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }

    private static int max(int a, int b, int c, int d) {
        return max(max(a, b, c), d);
    }

    /**
     * Performs global alignment using affine gap scoring system (different penalties exist for gap opening and gap
     * extension)
     *
     * @param scoring affine gap scoring system
     * @param seq1    first sequence
     * @param seq2    second sequence
     * @return array of mutations
     */
    public static <S extends Sequence<S>> Alignment<S> alignGlobalAffine(AffineGapAlignmentScoring<S> scoring,
                                                                         S seq1, S seq2) {
        if (seq1.getAlphabet() != seq2.getAlphabet() || seq1.getAlphabet() != scoring.getAlphabet())
            throw new IllegalArgumentException("Different alphabets.");

        int
                length1 = seq1.size(),
                length2 = seq2.size(),
                size1 = length1 + 1,
                size2 = length2 + 1;
        Matrix
                gapIn1 = new Matrix(size1, size2),
                gapIn2 = new Matrix(size1, size2),
                matrix = new Matrix(size1, size2);

        int
                gapExtensionPenalty = scoring.getGapExtensionPenalty(),
                gapOpenPenalty = scoring.getGapOpenPenalty() - gapExtensionPenalty;

        int i, j;
        for (i = 1; i < size1; ++i) {
            matrix.set(i, 0, gapOpenPenalty + i * gapExtensionPenalty);
            gapIn2.set(i, 0, gapOpenPenalty + i * gapExtensionPenalty);
            gapIn1.set(i, 0, MIN_VALUE);

        }
        for (j = 1; j < size2; ++j) {
            matrix.set(0, j, gapOpenPenalty + j * gapExtensionPenalty);
            gapIn1.set(0, j, gapOpenPenalty + j * gapExtensionPenalty);
            gapIn2.set(0, j, MIN_VALUE);
        }

        matrix.set(0, 0, 0);
        gapIn1.set(0, 0, MIN_VALUE);
        gapIn2.set(0, 0, MIN_VALUE);

        for (i = 1; i <= length1; ++i) {
            for (j = 1; j <= length2; ++j) {
                gapIn1.set(i, j, max(
                        matrix.get(i, j - 1) + gapOpenPenalty + gapExtensionPenalty,
                        gapIn1.get(i, j - 1) + gapExtensionPenalty));

                gapIn2.set(i, j, max(
                        matrix.get(i - 1, j) + gapOpenPenalty + gapExtensionPenalty,
                        gapIn2.get(i - 1, j) + gapExtensionPenalty));

                matrix.set(i, j, max(
                        matrix.get(i - 1, j - 1) +
                                scoring.getScore(seq1.codeAt(i - 1), seq2.codeAt(j - 1)),
                        gapIn2.get(i, j),
                        gapIn1.get(i, j)));
            }
        }

        i = length1;
        j = length2;

        MutationsBuilder<S> mutations = new MutationsBuilder<>(seq1.getAlphabet(), true);
        int score = matrix.get(length1, length2);
        boolean inGap1 = false, inGap2 = false;
        while (i > 0 || j > 0) {
            assert !inGap1 || !inGap2;
            if (!inGap2 && (inGap1 || (j > 0 && score == gapIn1.get(i, j)))) {
                inGap1 = false;
                if (score == gapIn1.get(i, j - 1) + gapExtensionPenalty) {
                    inGap1 = true;
                    score = gapIn1.get(i, j - 1);
                } else
                    score = matrix.get(i, j - 1);


                mutations.appendInsertion(i, seq2.codeAt(j - 1));
                j--;
            } else if (inGap2 || (i > 0 && score == gapIn2.get(i, j))) {
                inGap2 = false;
                if (score == gapIn2.get(i - 1, j) + gapExtensionPenalty) {
                    inGap2 = true;
                    score = gapIn2.get(i - 1, j);
                } else
                    score = matrix.get(i - 1, j);


                mutations.appendDeletion(i - 1, seq1.codeAt(i - 1));
                i--;
            } else if (i > 0 && j > 0
                    && score == matrix.get(i - 1, j - 1)
                    + scoring.getScore(seq1.codeAt(i - 1), seq2.codeAt(j - 1))) {
                score = matrix.get(i - 1, j - 1);
                if (seq1.codeAt(i - 1) != seq2.codeAt(j - 1))
                    mutations.appendSubstitution(i - 1, seq1.codeAt(i - 1), seq2.codeAt(j - 1));

                --i;
                --j;
            } else
                throw new RuntimeException();
        }

        return new Alignment<>(seq1, mutations.createAndDestroy(),
                new Range(0, seq1.size()), new Range(0, seq2.size()), matrix.get(length1, length2));
    }

    // TODO this is wrong, additional range calculation is required
    // public static <S extends Sequence<S>> Alignment<S> alignLocal(AlignmentScoring<S> alignmentScoring,
    //                                                               S seq1, S seq2,
    //                                                               int offset1, int length1,
    //                                                               int offset2, int length2) {
    //     Alignment<S> al = Aligner.alignLocal(
    //             alignmentScoring,
    //             seq1.getRange(offset1, offset1 + length1),
    //             seq2.getRange(offset2, offset2 + length2));
    //     return new Alignment<>(seq1, al.getAbsoluteMutations().move(offset1), al.getScore());
    // }

    /**
     * Performs local alignment
     *
     * @param alignmentScoring scoring system
     * @param seq1             first sequence
     * @param seq2             second sequence
     * @return result of alignment with information about alignment positions in both sequences and array of mutations
     */
    public static <S extends Sequence<S>> Alignment<S> alignLocal(AlignmentScoring<S> alignmentScoring,
                                                                  S seq1, S seq2) {
        if (alignmentScoring instanceof AffineGapAlignmentScoring)
            return alignLocalAffine((AffineGapAlignmentScoring<S>) alignmentScoring, seq1, seq2);
        if (alignmentScoring instanceof LinearGapAlignmentScoring)
            return alignLocalLinear((LinearGapAlignmentScoring<S>) alignmentScoring, seq1, seq2);
        throw new RuntimeException("Unknown scoring type.");
    }

    /**
     * Performs local alignment using Linear scoring system (penalty exists only for gap)
     *
     * @param seq1 first sequence
     * @param seq2 second sequence
     * @return result of alignment with information about alignment positions in both sequences and array of mutations
     */
    public static <S extends Sequence<S>> Alignment<S> alignLocalLinear(LinearGapAlignmentScoring<S> scoring,
                                                                        S seq1, S seq2) {
        if (seq1.getAlphabet() != seq2.getAlphabet() || seq1.getAlphabet() != scoring.getAlphabet())
            throw new IllegalArgumentException("Different alphabets.");

        int size1 = seq1.size() + 1,
                size2 = seq2.size() + 1;
        int[] matrix = new int[size1 * (seq2.size() + 1)];

        int i1, i2,
                match, delete, insert;

        int max = -1;
        int i1Start = 0;
        int i2Start = 0;

        for (i1 = 0; i1 < seq1.size(); ++i1)
            for (i2 = 0; i2 < seq2.size(); ++i2) {
                match = matrix[i1 * size2 + i2] +
                        scoring.getScore(seq1.codeAt(i1), seq2.codeAt(i2));
                delete = matrix[i1 * size2 + i2 + 1] + scoring.getGapPenalty();
                insert = matrix[(i1 + 1) * size2 + i2] + scoring.getGapPenalty();
                matrix[(i1 + 1) * size2 + i2 + 1] = Math.max(0, Math.max(match, Math.max(delete, insert)));

                if (matrix[(i1 + 1) * size2 + i2 + 1] > max && matrix[(i1 + 1) * size2 + i2 + 1] > 0) {
                    i1Start = i1 + 1;
                    i2Start = i2 + 1;
                    max = matrix[(i1 + 1) * size2 + i2 + 1];
                }
            }


        //it's not possible to find any local alignment
        if (max == -1)
            return null;

        MutationsBuilder<S> builder = new MutationsBuilder<S>(seq1.getAlphabet(), true);

        i1 = i1Start - 1;
        i2 = i2Start - 1;

        while (i1 >= 0 || i2 >= 0) {
            if (i1 >= 0 && i2 >= 0 &&
                    matrix[(i1 + 1) * size2 + i2 + 1] == matrix[i1 * size2 + i2] +
                            scoring.getScore(seq1.codeAt(i1), seq2.codeAt(i2))) {
                if (seq1.codeAt(i1) != seq2.codeAt(i2))
                    builder.appendSubstitution(i1, seq1.codeAt(i1), seq2.codeAt(i2));

                if (matrix[i1 * size2 + i2] == 0)
                    break;

                --i1;
                --i2;
            } else if (i1 >= 0 &&
                    matrix[(i1 + 1) * size2 + i2 + 1] ==
                            matrix[i1 * size2 + i2 + 1] + scoring.getGapPenalty()) {
                builder.appendDeletion(i1, seq1.codeAt(i1));

                if (matrix[i1 * size2 + i2 + 1] == 0)
                    break;

                i1--;
            } else if (i2 >= 0 &&
                    matrix[(i1 + 1) * size2 + i2 + 1] ==
                            matrix[(i1 + 1) * size2 + i2] + scoring.getGapPenalty()) {
                builder.appendInsertion(i1 + 1, seq2.codeAt(i2));

                if (matrix[(i1 + 1) * size2 + i2] == 0)
                    break;

                i2--;
            } else
                throw new RuntimeException();
        }

        int seq1Start = i1;
        int seq2Start = i2;

        return new Alignment<>(seq1, builder.createAndDestroy(),
                new Range(seq1Start, i1Start), new Range(seq2Start, i2Start), max);
    }

    /**
     * Performs local alignment using Affine gap scoring system (different penalties exist for gap opening and gap
     * extension)
     *
     * @param seq1 first sequence
     * @param seq2 second sequence
     * @return result of alignment with information about alignment positions in both sequences and array of mutations
     */
    public static <S extends Sequence<S>> Alignment<S> alignLocalAffine(AffineGapAlignmentScoring<S> scoring,
                                                                        S seq1, S seq2) {
        if (seq1.getAlphabet() != seq2.getAlphabet() || seq1.getAlphabet() != scoring.getAlphabet())
            throw new IllegalArgumentException("Different alphabets.");

        int
                length1 = seq1.size(),
                length2 = seq2.size(),
                size1 = length1 + 1,
                size2 = length2 + 1;
        Matrix
                gapIn1 = new Matrix(size1, size2),
                gapIn2 = new Matrix(size1, size2),
                matrix = new Matrix(size1, size2);

        int
                gapExtensionPenalty = scoring.getGapExtensionPenalty(),
                gapOpenPenalty = scoring.getGapOpenPenalty() - gapExtensionPenalty;

        int i, j;
        for (i = 1; i < size1; ++i)
            gapIn1.set(i, 0, MIN_VALUE);

        for (j = 1; j < size2; ++j)
            gapIn2.set(0, j, MIN_VALUE);

        int max = -1;
        int seq1end = 0;
        int seq2end = 0;

        for (i = 1; i <= length1; ++i) {
            for (j = 1; j <= length2; ++j) {
                gapIn1.set(i, j, max(
                        matrix.get(i, j - 1) + gapOpenPenalty + gapExtensionPenalty,
                        gapIn1.get(i, j - 1) + gapExtensionPenalty));

                gapIn2.set(i, j, max(
                        matrix.get(i - 1, j) + gapOpenPenalty + gapExtensionPenalty,
                        gapIn2.get(i - 1, j) + gapExtensionPenalty));

                matrix.set(i, j, max(0,
                        matrix.get(i - 1, j - 1) +
                                scoring.getScore(seq1.codeAt(i - 1), seq2.codeAt(j - 1)),
                        gapIn2.get(i, j),
                        gapIn1.get(i, j)));

                if (matrix.get(i, j) > max && matrix.get(i, j) != 0) {
                    assert matrix.get(i, j) >= 0;
                    seq1end = i;
                    seq2end = j;
                    max = matrix.get(i, j);
                }
            }
        }

        if (max == -1)
            return null;

        i = seq1end;
        j = seq2end;

        MutationsBuilder<S> mutations = new MutationsBuilder<>(seq1.getAlphabet(), true);
        int score = matrix.get(seq1end, seq2end);
        boolean inGap1 = false, inGap2 = false;
        while (i > 0 || j > 0) {
            assert !inGap1 || !inGap2;
            if (!inGap2 && (inGap1 || (j > 0 && score == gapIn1.get(i, j)))) {
                inGap1 = false;
                if (score == gapIn1.get(i, j - 1) + gapExtensionPenalty) {
                    inGap1 = true;
                    score = gapIn1.get(i, j - 1);
                } else
                    score = matrix.get(i, j - 1);

                if (score == 0)
                    break;

                mutations.appendInsertion(i, seq2.codeAt(j - 1));
                j--;
            } else if (inGap2 || (i > 0 && score == gapIn2.get(i, j))) {
                inGap2 = false;
                if (score == gapIn2.get(i - 1, j) + gapExtensionPenalty) {
                    inGap2 = true;
                    score = gapIn2.get(i - 1, j);
                } else
                    score = matrix.get(i - 1, j);

                if (score == 0)
                    break;

                mutations.appendDeletion(i - 1, seq1.codeAt(i - 1));
                i--;
            } else if (i > 0 && j > 0
                    && score == matrix.get(i - 1, j - 1)
                    + scoring.getScore(seq1.codeAt(i - 1), seq2.codeAt(j - 1))) {
                score = matrix.get(i - 1, j - 1);
                if (seq1.codeAt(i - 1) != seq2.codeAt(j - 1))
                    mutations.appendSubstitution(i - 1, seq1.codeAt(i - 1), seq2.codeAt(j - 1));

                if (score == 0)
                    break;

                --i;
                --j;
            } else
                throw new RuntimeException();
        }

        int
                seq1begin = i - 1,
                seq2begin = j - 1;

        return new Alignment<>(seq1, mutations.createAndDestroy(),
                new Range(seq1begin, seq1end), new Range(seq2begin, seq2end), max);
    }
}
