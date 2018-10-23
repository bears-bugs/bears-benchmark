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
import com.milaboratory.core.sequence.NucleotideSequence;

public final class BandedLinearAligner {
    private BandedLinearAligner() {
    }

    /**
     * Classical Banded Alignment
     * <p/>
     * <p>Both sequences must be highly similar</p> <p>Align 2 sequence completely (i.e. while first sequence will be
     * aligned against whole second sequence)</p>
     *
     * @param scoring     scoring system
     * @param seq1        first sequence
     * @param seq2        second sequence
     * @param offset1     offset in first sequence
     * @param length1     length of first sequence's part to be aligned
     * @param offset2     offset in second sequence
     * @param length2     length of second sequence's part to be aligned
     * @param width       width of banded alignment matrix. In other terms max allowed number of indels
     * @param mutations   mutations array where all mutations will be kept
     * @param cachedArray cached (created once) array to be used in {@link BandedMatrix}, which is compact alignment
     *                    scoring matrix
     */
    public static float align0(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                               int offset1, int length1, int offset2, int length2,
                               int width, MutationsBuilder<NucleotideSequence> mutations, CachedIntArray cachedArray) {
        if(offset1 < 0 || length1 < 0 || offset2 < 0 || length2 < 0)
            throw new IllegalArgumentException();

        int size1 = length1 + 1,
                size2 = length2 + 1;

        BandedMatrix matrix = new BandedMatrix(cachedArray, size1, size2, width);

        int i, j;

        for (i = matrix.getRowFactor() - matrix.getColumnDelta(); i > 0; --i)
            matrix.set(0, i, scoring.getGapPenalty() * i);

        for (i = matrix.getColumnDelta(); i > 0; --i)
            matrix.set(i, 0, scoring.getGapPenalty() * i);

        matrix.set(0, 0, 0);

        int match, delete, insert, to;

        for (i = 0; i < length1; ++i) {
            to = Math.min(i + matrix.getRowFactor() - matrix.getColumnDelta() + 1, length2);
            for (j = Math.max(0, i - matrix.getColumnDelta()); j < to; ++j) {
                match = matrix.get(i, j) +
                        scoring.getScore(seq1.codeAt(offset1 + i), seq2.codeAt(offset2 + j));
                delete = matrix.get(i, j + 1) + scoring.getGapPenalty();
                insert = matrix.get(i + 1, j) + scoring.getGapPenalty();
                matrix.set(i + 1, j + 1, Math.max(match, Math.max(delete, insert)));
            }
        }

        to = mutations.size();
        i = length1 - 1;
        j = length2 - 1;
        byte c1, c2;
        while (i >= 0 || j >= 0) {
            if (i >= 0 && j >= 0 &&
                    matrix.get(i + 1, j + 1) == matrix.get(i, j) +
                            scoring.getScore(c1 = seq1.codeAt(offset1 + i),
                                    c2 = seq2.codeAt(offset2 + j))) {
                if (c1 != c2)
                    mutations.appendSubstitution(offset1 + i, c1, c2);
                --i;
                --j;
            } else if (i >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i, j + 1) + scoring.getGapPenalty()) {
                mutations.appendDeletion(offset1 + i, seq1.codeAt(offset1 + i));
                --i;
            } else if (j >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i + 1, j) + scoring.getGapPenalty()) {
                mutations.appendInsertion(offset1 + i + 1, seq2.codeAt(offset2 + j));
                --j;
            } else
                throw new RuntimeException();
        }

        mutations.reverseRange(to, mutations.size());
        return matrix.get(length1, length2);
    }

    /**
     * Semi-semi-global alignment with artificially added letters.
     * <p/>
     * <p>Alignment where second sequence is aligned to the right part of first sequence.</p> <p>Whole second sequence
     * must be highly similar to the first sequence</p>
     *
     * @param scoring           scoring system
     * @param seq1              first sequence
     * @param seq2              second sequence
     * @param offset1           offset in first sequence
     * @param length1           length of first sequence's part to be aligned including artificially added letters
     * @param addedNucleotides1 number of artificially added letters to the first sequence
     * @param offset2           offset in second sequence
     * @param length2           length of second sequence's part to be aligned including artificially added letters
     * @param addedNucleotides2 number of artificially added letters to the second sequence
     * @param width             width of banded alignment matrix. In other terms max allowed number of indels
     * @param mutations         mutations array where all mutations will be kept
     * @param cachedArray       cached (created once) array to be used in {@link BandedMatrix}, which is compact
     *                          alignment scoring matrix
     */
    public static BandedSemiLocalResult alignRightAdded0(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                         int offset1, int length1, int addedNucleotides1, int offset2, int length2, int addedNucleotides2,
                                                         int width, MutationsBuilder<NucleotideSequence> mutations, CachedIntArray cachedArray) {
        if(offset1 < 0 || length1 < 0 || offset2 < 0 || length2 < 0)
            throw new IllegalArgumentException();

        int size1 = length1 + 1,
                size2 = length2 + 1;

        BandedMatrix matrix = new BandedMatrix(cachedArray, size1, size2, width);

        int i, j;

        for (i = matrix.getRowFactor() - matrix.getColumnDelta(); i > 0; --i)
            matrix.set(0, i, scoring.getGapPenalty() * i);

        for (i = matrix.getColumnDelta(); i > 0; --i)
            matrix.set(i, 0, scoring.getGapPenalty() * i);

        matrix.set(0, 0, 0);

        int match, delete, insert, to;

        for (i = 0; i < length1; ++i) {
            to = Math.min(i + matrix.getRowFactor() - matrix.getColumnDelta() + 1, length2);
            for (j = Math.max(0, i - matrix.getColumnDelta()); j < to; ++j) {
                match = matrix.get(i, j) +
                        scoring.getScore(seq1.codeAt(offset1 + i), seq2.codeAt(offset2 + j));
                delete = matrix.get(i, j + 1) + scoring.getGapPenalty();
                insert = matrix.get(i + 1, j) + scoring.getGapPenalty();
                matrix.set(i + 1, j + 1, Math.max(match, Math.max(delete, insert)));
            }
        }

        //Searching for max.
        int maxI = -1, maxJ = -1;
        int maxScore = Integer.MIN_VALUE;

        j = length2;
        for (i = length1 - addedNucleotides1; i < size1; ++i)
            if (maxScore < matrix.get(i, j)) {
                maxScore = matrix.get(i, j);
                maxI = i;
                maxJ = j;
            }

        i = length1;
        for (j = length2 - addedNucleotides2; j < size2; ++j)
            if (maxScore < matrix.get(i, j)) {
                maxScore = matrix.get(i, j);
                maxI = i;
                maxJ = j;
            }

        to = mutations.size();
        i = maxI - 1;
        j = maxJ - 1;
        byte c1, c2;
        while (i >= 0 || j >= 0) {
            if (i >= 0 && j >= 0 &&
                    matrix.get(i + 1, j + 1) == matrix.get(i, j) +
                            scoring.getScore(c1 = seq1.codeAt(offset1 + i),
                                    c2 = seq2.codeAt(offset2 + j))) {
                if (c1 != c2)
                    mutations.appendSubstitution(offset1 + i, c1, c2);
                --i;
                --j;
            } else if (i >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i, j + 1) + scoring.getGapPenalty()) {
                mutations.appendDeletion(offset1 + i, seq1.codeAt(offset1 + i));
                --i;
            } else if (j >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i + 1, j) + scoring.getGapPenalty()) {
                mutations.appendInsertion(offset1 + i + 1, seq2.codeAt(offset2 + j));
                --j;
            } else
                throw new RuntimeException();
        }

        mutations.reverseRange(to, mutations.size());

        return new BandedSemiLocalResult(offset1 + maxI - 1, offset2 + maxJ - 1, maxScore);
    }


    /**
     * Semi-semi-global alignment with artificially added letters.
     *
     * <p>Alignment where second sequence is aligned to the left part of first sequence.</p>
     *
     * <p>Whole second sequence must be highly similar to the first sequence, except last {@code width} letters, which
     * are to be checked whether they can improve alignment or not.</p>
     *
     * @param scoring           scoring system
     * @param seq1              first sequence
     * @param seq2              second sequence
     * @param offset1           offset in first sequence
     * @param length1           length of first sequence's part to be aligned
     * @param addedNucleotides1 number of artificially added letters to the first sequence;
     *                          must be 0 if seq1 is a pattern to match and seq2 is target sequence
     *                          where we search the pattern
     * @param offset2           offset in second sequence
     * @param length2           length of second sequence's part to be aligned
     * @param addedNucleotides2 number of artificially added letters to the second sequence;
     *                          if seq2 is target sequence where we search the pattern, this parameter must be equal
     *                          to maximum allowed number of indels (same as width parameter)
     * @param width             width of banded alignment matrix. In other terms max allowed number of indels
     * @param mutations         mutations array where all mutations will be kept
     * @param cachedArray       cached (created once) array to be used in {@link BandedMatrix}, which is compact
     *                          alignment scoring matrix
     */
    public static BandedSemiLocalResult alignLeftAdded0(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                        int offset1, int length1, int addedNucleotides1, int offset2, int length2, int addedNucleotides2,
                                                        int width, MutationsBuilder<NucleotideSequence> mutations, CachedIntArray cachedArray) {
        if(offset1 < 0 || length1 < 0 || offset2 < 0 || length2 < 0)
            throw new IllegalArgumentException();

        int size1 = length1 + 1,
                size2 = length2 + 1;

        BandedMatrix matrix = new BandedMatrix(cachedArray, size1, size2, width);

        int i, j;

        for (i = matrix.getRowFactor() - matrix.getColumnDelta(); i > 0; --i)
            matrix.set(0, i, scoring.getGapPenalty() * i);

        for (i = matrix.getColumnDelta(); i > 0; --i)
            matrix.set(i, 0, scoring.getGapPenalty() * i);

        matrix.set(0, 0, 0);

        int match, delete, insert, to;

        for (i = 0; i < length1; ++i) {
            to = Math.min(i + matrix.getRowFactor() - matrix.getColumnDelta() + 1, length2);
            for (j = Math.max(0, i - matrix.getColumnDelta()); j < to; ++j) {
                match = matrix.get(i, j) +
                        scoring.getScore(seq1.codeAt(offset1 + length1 - 1 - i),
                                seq2.codeAt(offset2 + length2 - 1 - j));
                delete = matrix.get(i, j + 1) + scoring.getGapPenalty();
                insert = matrix.get(i + 1, j) + scoring.getGapPenalty();
                matrix.set(i + 1, j + 1, Math.max(match, Math.max(delete, insert)));
            }
        }

        //Searching for max.
        int maxI = -1, maxJ = -1;
        int maxScore = Integer.MIN_VALUE;

        j = length2;
        for (i = length1 - addedNucleotides1; i < size1; ++i)
            if (maxScore < matrix.get(i, j)) {
                maxScore = matrix.get(i, j);
                maxI = i;
                maxJ = j;
            }

        i = length1;
        for (j = length2 - addedNucleotides2; j < size2; ++j)
            if (maxScore < matrix.get(i, j)) {
                maxScore = matrix.get(i, j);
                maxI = i;
                maxJ = j;
            }

        i = maxI - 1;
        j = maxJ - 1;
        byte c1, c2;
        while (i >= 0 || j >= 0) {
            if (i >= 0 && j >= 0 &&
                    matrix.get(i + 1, j + 1) == matrix.get(i, j) +
                            scoring.getScore(c1 = seq1.codeAt(offset1 + length1 - 1 - i),
                                    c2 = seq2.codeAt(offset2 + length2 - 1 - j))) {
                if (c1 != c2)
                    mutations.appendSubstitution(offset1 + length1 - 1 - i, c1, c2);
                --i;
                --j;
            } else if (i >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i, j + 1) + scoring.getGapPenalty()) {
                mutations.appendDeletion(offset1 + length1 - 1 - i, seq1.codeAt(offset1 + length1 - 1 - i));
                --i;
            } else if (j >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i + 1, j) + scoring.getGapPenalty()) {
                mutations.appendInsertion(offset1 + length1 - 1 - i, seq2.codeAt(offset2 + length2 - 1 - j));
                --j;
            } else
                throw new RuntimeException();
        }

        return new BandedSemiLocalResult(offset1 + length1 - maxI, offset2 + length2 - maxJ, maxScore);
    }


    /**
     * Alignment which identifies what is the highly similar part of the both sequences.
     * <p/>
     * <p>Alignment is done in the way that beginning of second sequences is aligned to beginning of first
     * sequence.</p>
     * <p/>
     * <p>Alignment terminates when score in banded alignment matrix reaches {@code stopPenalty} value.</p>
     * <p/>
     * <p>In other words, only left part of second sequence is to be aligned</p>
     *
     * @param scoring     scoring system
     * @param seq1        first sequence
     * @param seq2        second sequence
     * @param offset1     offset in first sequence
     * @param length1     length of first sequence's part to be aligned
     * @param offset2     offset in second sequence
     * @param length2     length of second sequence's part to be aligned@param width
     * @param stopPenalty alignment score value in banded alignment matrix at which alignment terminates
     * @param mutations   array where all mutations will be kept
     * @param cachedArray cached (created once) array to be used in {@link BandedMatrix}, which is compact alignment
     *                    scoring matrix
     * @return object which contains positions at which alignment terminated and array of mutations
     */
    public static BandedSemiLocalResult alignSemiLocalLeft0(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                            int offset1, int length1, int offset2, int length2,
                                                            int width, int stopPenalty, MutationsBuilder<NucleotideSequence> mutations,
                                                            CachedIntArray cachedArray) {
        if(offset1 < 0 || length1 < 0 || offset2 < 0 || length2 < 0)
            throw new IllegalArgumentException();

        int size1 = length1 + 1,
                size2 = length2 + 1;

        int matchReward = scoring.getScore((byte) 0, (byte) 0);

        BandedMatrix matrix = new BandedMatrix(cachedArray, size1, size2, width);

        int i, j;

        for (i = matrix.getRowFactor() - matrix.getColumnDelta(); i > 0; --i)
            matrix.set(0, i, scoring.getGapPenalty() * i);

        for (i = matrix.getColumnDelta(); i > 0; --i)
            matrix.set(i, 0, scoring.getGapPenalty() * i);

        matrix.set(0, 0, 0);

        int match, delete, insert, to;
        int max = 0;
        int iStop = 0, jStop = 0;
        int rowMax;

        for (i = 0; i < length1; ++i) {
            to = Math.min(i + matrix.getRowFactor() - matrix.getColumnDelta() + 1, size2 - 1);
            rowMax = Integer.MIN_VALUE;
            for (j = Math.max(0, i - matrix.getColumnDelta()); j < to; ++j) {
                match = matrix.get(i, j) +
                        scoring.getScore(seq1.codeAt(offset1 + i), seq2.codeAt(offset2 + j));
                delete = matrix.get(i, j + 1) + scoring.getGapPenalty();
                insert = matrix.get(i + 1, j) + scoring.getGapPenalty();
                matrix.set(i + 1, j + 1, match = Math.max(match, Math.max(delete, insert)));
                if (max < match) {
                    iStop = i + 1;
                    jStop = j + 1;
                    max = match;
                }
                rowMax = Math.max(rowMax, match);
            }
            if (rowMax - i * matchReward < stopPenalty)
                break;
        }


        int fromL = mutations.size();

        i = iStop - 1;
        j = jStop - 1;
        byte c1, c2;
        while (i >= 0 || j >= 0) {
            if (i >= 0 && j >= 0 &&
                    matrix.get(i + 1, j + 1) == matrix.get(i, j) +
                            scoring.getScore(c1 = seq1.codeAt(offset1 + i),
                                    c2 = seq2.codeAt(offset2 + j))) {
                if (c1 != c2)
                    mutations.appendSubstitution(offset1 + i, c1, c2);
                --i;
                --j;
            } else if (i >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i, j + 1) + scoring.getGapPenalty()) {
                mutations.appendDeletion(offset1 + i, seq1.codeAt(offset1 + i));
                --i;
            } else if (j >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i + 1, j) + scoring.getGapPenalty()) {
                mutations.appendInsertion(offset1 + i + 1, seq2.codeAt(offset2 + j));
                --j;
            } else
                throw new RuntimeException();
        }

        mutations.reverseRange(fromL, mutations.size());

        return new BandedSemiLocalResult(offset1 + iStop - 1, offset2 + jStop - 1, max);
    }

    /**
     * Alignment which identifies what is the highly similar part of the both sequences.
     * <p/>
     * <p>Alignment is done in the way that end of second sequence is aligned to end of first sequence.</p> <p>Alignment
     * terminates when score in banded alignment matrix reaches {@code stopPenalty} value.</p> <p>In other words, only
     * right part of second sequence is to be aligned.</p>
     *
     * @param scoring     scoring system
     * @param seq1        first sequence
     * @param seq2        second sequence
     * @param offset1     offset in first sequence
     * @param length1     length of first sequence's part to be aligned
     * @param offset2     offset in second sequence
     * @param length2     length of second sequence's part to be aligned@param width
     * @param stopPenalty alignment score value in banded alignment matrix at which alignment terminates
     * @param mutations   array where all mutations will be kept
     * @param cachedArray cached (created once) array to be used in {@link BandedMatrix}, which is compact alignment
     *                    scoring matrix
     * @return object which contains positions at which alignment terminated and array of mutations
     */
    public static BandedSemiLocalResult alignSemiLocalRight0(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                             int offset1, int length1, int offset2, int length2,
                                                             int width, int stopPenalty, MutationsBuilder<NucleotideSequence> mutations,
                                                             CachedIntArray cachedArray) {
        if(offset1 < 0 || length1 < 0 || offset2 < 0 || length2 < 0)
            throw new IllegalArgumentException();

        int size1 = length1 + 1,
                size2 = length2 + 1;

        int matchReward = scoring.getScore((byte) 0, (byte) 0);

        BandedMatrix matrix = new BandedMatrix(cachedArray, size1, size2, width);

        int i, j;

        for (i = matrix.getRowFactor() - matrix.getColumnDelta(); i > 0; --i)
            matrix.set(0, i, scoring.getGapPenalty() * i);

        for (i = matrix.getColumnDelta(); i > 0; --i)
            matrix.set(i, 0, scoring.getGapPenalty() * i);

        matrix.set(0, 0, 0);

        int match, delete, insert, to;
        int max = 0;
        int iStop = 0, jStop = 0;
        int rowMax;

        for (i = 0; i < length1; ++i) {
            to = Math.min(i + matrix.getRowFactor() - matrix.getColumnDelta() + 1, length2);
            rowMax = Integer.MIN_VALUE;
            for (j = Math.max(0, i - matrix.getColumnDelta()); j < to; ++j) {
                match = matrix.get(i, j) +
                        scoring.getScore(seq1.codeAt(offset1 + length1 - 1 - i),
                                seq2.codeAt(offset2 + length2 - 1 - j));
                delete = matrix.get(i, j + 1) + scoring.getGapPenalty();
                insert = matrix.get(i + 1, j) + scoring.getGapPenalty();
                matrix.set(i + 1, j + 1, match = Math.max(match, Math.max(delete, insert)));
                if (max < match) {
                    iStop = i + 1;
                    jStop = j + 1;
                    max = match;
                }
                rowMax = Math.max(rowMax, match);
            }
            if (rowMax - i * matchReward < stopPenalty)
                break;
        }

        i = iStop - 1;
        j = jStop - 1;
        byte c1, c2;
        while (i >= 0 || j >= 0) {
            if (i >= 0 && j >= 0 &&
                    matrix.get(i + 1, j + 1) == matrix.get(i, j) +
                            scoring.getScore(c1 = seq1.codeAt(offset1 + length1 - 1 - i),
                                    c2 = seq2.codeAt(offset2 + length2 - 1 - j))) {
                if (c1 != c2)
                    mutations.appendSubstitution(offset1 + length1 - 1 - i, c1, c2);
                --i;
                --j;
            } else if (i >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i, j + 1) + scoring.getGapPenalty()) {
                mutations.appendDeletion(offset1 + length1 - 1 - i, seq1.codeAt(offset1 + length1 - 1 - i));
                --i;
            } else if (j >= 0 &&
                    matrix.get(i + 1, j + 1) ==
                            matrix.get(i + 1, j) + scoring.getGapPenalty()) {
                mutations.appendInsertion(offset1 + length1 - 1 - i, seq2.codeAt(offset2 + length2 - 1 - j));
                --j;
            } else
                throw new RuntimeException();
        }

        return new BandedSemiLocalResult(offset1 + length1 - iStop, offset2 + length2 - jStop, max);
    }


    /**
     * Classical Banded Alignment
     * <p/>
     * <p>Both sequences must be highly similar</p> <p>Align 2 sequence completely (i.e. while first sequence will be
     * aligned against whole second sequence)</p>
     *
     * @param scoring scoring system
     * @param seq1    first sequence
     * @param seq2    second sequence
     * @param width   width of banded alignment matrix. In other terms max allowed number of indels
     */
    public static Alignment<NucleotideSequence> align(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                      int width) {
        return align(scoring, seq1, seq2, 0, seq1.size(), 0, seq2.size(), width);
    }

    /**
     * Classical Banded Alignment
     * <p/>
     * <p>Both sequences must be highly similar</p> <p>Align 2 sequence completely (i.e. while first sequence will be
     * aligned against whole second sequence)</p>
     *
     * @param scoring scoring system
     * @param seq1    first sequence
     * @param seq2    second sequence
     * @param offset1 offset in first sequence
     * @param length1 length of first sequence's part to be aligned
     * @param offset2 offset in second sequence
     * @param length2 length of second sequence's part to be aligned
     * @param width   width of banded alignment matrix. In other terms max allowed number of indels
     */
    public static Alignment<NucleotideSequence> align(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                      int offset1, int length1, int offset2, int length2, int width) {
        try {
            MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
            float score = align0(scoring, seq1, seq2, offset1, length1, offset2, length2, width,
                    mutations, AlignmentCache.get());
            return new Alignment<>(seq1, mutations.createAndDestroy(),
                    new Range(offset1, offset1 + length1), new Range(offset2, offset2 + length2), score);
        } finally {
            AlignmentCache.release();
        }
    }

    /**
     * Semi-semi-global alignment with artificially added letters.
     *
     * <p>Alignment where second sequence is aligned to the left part of first sequence.</p>
     *
     * <p>Whole second sequence must be highly similar to the first sequence, except last {@code width} letters, which
     * are to be checked whether they can improve alignment or not.</p>
     *
     * @param scoring           scoring system
     * @param seq1              first sequence
     * @param seq2              second sequence
     * @param offset1           offset in first sequence
     * @param length1           length of first sequence's part to be aligned
     * @param addedNucleotides1 number of artificially added letters to the first sequence
     * @param offset2           offset in second sequence
     * @param length2           length of second sequence's part to be aligned
     * @param addedNucleotides2 number of artificially added letters to the second sequence
     * @param width             width of banded alignment matrix. In other terms max allowed number of indels
     */
    public static Alignment<NucleotideSequence> alignLeftAdded(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                               int offset1, int length1, int addedNucleotides1, int offset2, int length2, int addedNucleotides2,
                                                               int width) {
        try {
            MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
            BandedSemiLocalResult result = alignLeftAdded0(scoring, seq1, seq2,
                    offset1, length1, addedNucleotides1, offset2, length2, addedNucleotides2,
                    width, mutations, AlignmentCache.get());
            return new Alignment<>(seq1, mutations.createAndDestroy(),
                    new Range(result.sequence1Stop, offset1 + length1), new Range(result.sequence2Stop, offset2 + length2),
                    result.score);
        } finally {
            AlignmentCache.release();
        }
    }

    /**
     * Semi-semi-global alignment with artificially added letters.
     * <p/>
     * <p>Alignment where second sequence is aligned to the right part of first sequence.</p> <p>Whole second sequence
     * must be highly similar to the first sequence</p>
     *
     * @param scoring           scoring system
     * @param seq1              first sequence
     * @param seq2              second sequence
     * @param offset1           offset in first sequence
     * @param length1           length of first sequence's part to be aligned including artificially added letters
     * @param addedNucleotides1 number of artificially added letters to the first sequence
     * @param offset2           offset in second sequence
     * @param length2           length of second sequence's part to be aligned including artificially added letters
     * @param addedNucleotides2 number of artificially added letters to the second sequence
     * @param width             width of banded alignment matrix. In other terms max allowed number of indels
     */
    public static Alignment<NucleotideSequence> alignRightAdded(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                                int offset1, int length1, int addedNucleotides1, int offset2, int length2, int addedNucleotides2,
                                                                int width) {
        try {
            MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
            BandedSemiLocalResult result = alignRightAdded0(scoring, seq1, seq2,
                    offset1, length1, addedNucleotides1, offset2, length2, addedNucleotides2,
                    width, mutations, AlignmentCache.get());
            return new Alignment<>(seq1, mutations.createAndDestroy(),
                    new Range(offset1, result.sequence1Stop + 1), new Range(offset2, result.sequence2Stop + 1),
                    result.score);
        } finally {
            AlignmentCache.release();
        }
    }

    /**
     * Alignment which identifies what is the highly similar part of the both sequences.
     * <p/>
     * <p>Alignment is done in the way that beginning of second sequences is aligned to beginning of first
     * sequence.</p>
     * <p/>
     * <p>Alignment terminates when score in banded alignment matrix reaches {@code stopPenalty} value.</p>
     * <p/>
     * <p>In other words, only left part of second sequence is to be aligned</p>
     *
     * @param scoring     scoring system
     * @param seq1        first sequence
     * @param seq2        second sequence
     * @param offset1     offset in first sequence
     * @param length1     length of first sequence's part to be aligned
     * @param offset2     offset in second sequence
     * @param length2     length of second sequence's part to be aligned@param width
     * @param stopPenalty alignment score value in banded alignment matrix at which alignment terminates
     * @return object which contains positions at which alignment terminated and array of mutations
     */
    public static Alignment<NucleotideSequence> alignSemiLocalLeft(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                                   int offset1, int length1, int offset2, int length2,
                                                                   int width, int stopPenalty) {
        try {
            int minLength = Math.min(length1, length2) + width + 1;
            length1 = Math.min(length1, minLength);
            length2 = Math.min(length2, minLength);
            MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
            BandedSemiLocalResult result = alignSemiLocalLeft0(scoring, seq1, seq2,
                    offset1, length1, offset2, length2, width, stopPenalty, mutations, AlignmentCache.get());
            return new Alignment<>(seq1, mutations.createAndDestroy(),
                    new Range(offset1, result.sequence1Stop + 1), new Range(offset2, result.sequence2Stop + 1),
                    result.score);
        } finally {
            AlignmentCache.release();
        }
    }

    /**
     * Alignment which identifies what is the highly similar part of the both sequences.
     * <p/>
     * <p>Alignment is done in the way that beginning of second sequences is aligned to beginning of first
     * sequence.</p>
     * <p/>
     * <p>Alignment terminates when score in banded alignment matrix reaches {@code stopPenalty} value.</p>
     * <p/>
     * <p>In other words, only left part of second sequence is to be aligned</p>
     *
     * @param scoring     scoring system
     * @param seq1        first sequence
     * @param seq2        second sequence
     * @param stopPenalty alignment score value in banded alignment matrix at which alignment terminates
     * @return object which contains positions at which alignment terminated and array of mutations
     */
    public static Alignment<NucleotideSequence> alignSemiLocalLeft(LinearGapAlignmentScoring scoring,
                                                                   NucleotideSequence seq1, NucleotideSequence seq2,
                                                                   int width, int stopPenalty) {
        return alignSemiLocalLeft(scoring, seq1, seq2, 0, seq1.size(), 0, seq2.size(), width, stopPenalty);
    }

    /**
     * Alignment which identifies what is the highly similar part of the both sequences.
     * <p/>
     * <p>Alignment is done in the way that end of second sequence is aligned to end of first sequence.</p> <p>Alignment
     * terminates when score in banded alignment matrix reaches {@code stopPenalty} value.</p> <p>In other words, only
     * right part of second sequence is to be aligned.</p>
     *
     * @param scoring     scoring system
     * @param seq1        first sequence
     * @param seq2        second sequence
     * @param offset1     offset in first sequence
     * @param length1     length of first sequence's part to be aligned
     * @param offset2     offset in second sequence
     * @param length2     length of second sequence's part to be aligned@param width
     * @param stopPenalty alignment score value in banded alignment matrix at which alignment terminates
     * @return object which contains positions at which alignment terminated and array of mutations
     */
    public static Alignment<NucleotideSequence> alignSemiLocalRight(LinearGapAlignmentScoring scoring, NucleotideSequence seq1, NucleotideSequence seq2,
                                                                    int offset1, int length1, int offset2, int length2,
                                                                    int width, int stopPenalty) {
        try {
            int minLength = Math.min(length1, length2) + width + 1;
            int l1 = Math.min(length1, minLength);
            int l2 = Math.min(length2, minLength);
            offset1 = offset1 + length1 - l1;
            offset2 = offset2 + length2 - l2;
            length1 = l1;
            length2 = l2;
            MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
            BandedSemiLocalResult result = alignSemiLocalRight0(scoring, seq1, seq2,
                    offset1, length1, offset2, length2, width,
                    stopPenalty, mutations, AlignmentCache.get());
            return new Alignment<>(seq1, mutations.createAndDestroy(),
                    new Range(result.sequence1Stop, offset1 + length1), new Range(result.sequence2Stop, offset2 + length2),
                    result.score);
        } finally {
            AlignmentCache.release();
        }
    }

    /**
     * Alignment which identifies what is the highly similar part of the both sequences.
     * <p/>
     * <p>Alignment is done in the way that end of second sequence is aligned to end of first sequence.</p> <p>Alignment
     * terminates when score in banded alignment matrix reaches {@code stopPenalty} value.</p> <p>In other words, only
     * right part of second sequence is to be aligned.</p>
     *
     * @param scoring     scoring system
     * @param seq1        first sequence
     * @param seq2        second sequence
     * @param stopPenalty alignment score value in banded alignment matrix at which alignment terminates
     * @return object which contains positions at which alignment terminated and array of mutations
     */
    public static Alignment<NucleotideSequence> alignSemiLocalRight(LinearGapAlignmentScoring scoring,
                                                                    NucleotideSequence seq1, NucleotideSequence seq2,
                                                                    int width, int stopPenalty) {
        return alignSemiLocalRight(scoring, seq1, seq2, 0, seq1.size(), 0, seq2.size(), width, stopPenalty);
    }
}
