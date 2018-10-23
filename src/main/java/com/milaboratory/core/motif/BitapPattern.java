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
package com.milaboratory.core.motif;

import com.milaboratory.core.sequence.Sequence;

/**
 * Use {@link Motif#getBitapPattern()} to create bitap pattern.
 */
public final class BitapPattern implements java.io.Serializable {
    final int size;
    final long[] patternMask;
    final long[] reversePatternMask;

    /**
     * Use {@link Motif#getBitapPattern()} to create bitap pattern.
     */
    BitapPattern(int size, long[] patternMask, long[] reversePatternMask) {
        this.size = size;
        this.patternMask = patternMask;
        this.reversePatternMask = reversePatternMask;
    }

    public int exactSearch(Sequence sequence) {
        return exactSearch(sequence, 0, sequence.size());
    }

    public int exactSearch(Sequence sequence, int from) {
        return exactSearch(sequence, from, sequence.size());
    }

    public int exactSearch(Sequence sequence, int from, int to) {
        if (sequence.getAlphabet().size() != patternMask.length)
            throw new IllegalArgumentException();

        long R = ~1L;
        long matchingMask = (1L << size);

        for (int i = from; i < to; ++i) {
            R |= patternMask[sequence.codeAt(i)];
            R <<= 1;
            if (0 == (R & matchingMask))
                return i - size + 1;
        }
        return -1;
    }

    public BitapMatcher exactMatcher(final Sequence sequence, final int from, final int to) {
        if (sequence.getAlphabet().size() != patternMask.length)
            throw new IllegalArgumentException();

        return new BitapMatcher() {
            long R = ~1L;
            int current = from;

            @Override
            public int findNext() {
                long matchingMask = (1L << size);
                for (int i = current; i < to; ++i) {
                    R |= patternMask[sequence.codeAt(i)];
                    R <<= 1;
                    if (0 == (R & matchingMask)) {
                        current = i + 1;
                        return i - size + 1;
                    }
                }
                current = to;
                return -1;
            }

            @Override
            public int getNumberOfErrors() {
                return 0;
            }
        };
    }

    /**
     * Returns a BitapMatcher preforming a fuzzy search in a whole {@code sequence}. Search allows no more than {@code
     * substitutions} number of substitutions. Matcher will return positions of first matched letter in the motif in
     * ascending order.
     *
     * @param substitutions maximal number of allowed substitutions
     * @param sequence      target sequence
     * @return matcher which will return positions of first matched letter in the motif in ascending order
     */
    public BitapMatcher substitutionOnlyMatcherFirst(int substitutions, final Sequence sequence) {
        return substitutionOnlyMatcherFirst(substitutions, sequence, 0, sequence.size());
    }

    /**
     * Returns a BitapMatcher preforming a fuzzy search in a subsequence of {@code sequence}. Search range starts from
     * {@code from} (inclusive) and ends at {@code to} (exclusive). Search allows no more than {@code substitutions}
     * number of substitutions. Matcher will return positions of first matched letter in the motif in ascending order.
     *
     * @param substitutions maximal number of allowed substitutions
     * @param sequence      target sequence
     * @param from          left boundary of search range (inclusive)
     * @param to            right boundary of search range (exclusive)
     * @return matcher which will return positions of first matched letter in the motif in ascending order
     */
    public BitapMatcher substitutionOnlyMatcherFirst(int substitutions, final Sequence sequence, int from, int to) {
        if (sequence.getAlphabet().size() != patternMask.length)
            throw new IllegalArgumentException();

        return new BitapMatcherImpl(substitutions + 1, from, to) {
            @Override
            public int findNext() {
                long matchingMask = (1L << (size - 1));

                int d;
                long preMismatchTmp, mismatchTmp;

                boolean match = false;

                for (int i = current; i < to; ++i) {
                    long currentPatternMask = patternMask[sequence.codeAt(i)];

                    // Exact match on the previous step == match with insertion on current step
                    R[0] <<= 1;
                    mismatchTmp = R[0];
                    R[0] |= currentPatternMask;

                    if (0 == (R[0] & matchingMask)) {
                        errors = 0;
                        match = true;
                    }

                    for (d = 1; d < R.length; ++d) {
                        R[d] <<= 1;
                        preMismatchTmp = R[d];
                        R[d] |= currentPatternMask;
                        R[d] &= mismatchTmp;
                        if (!match && 0 == (R[d] & matchingMask) && i >= size - 1) {
                            errors = d;
                            match = true;
                        }
                        mismatchTmp = preMismatchTmp;
                    }

                    if (match) {
                        current = i + 1;
                        return i - size + 1;
                    }
                }
                current = to;
                return -1;
            }
        };
    }

    /**
     * Returns a BitapMatcher preforming a fuzzy search in a whole {@code sequence}.  Search allows no more than {@code
     * maxNumberOfErrors} number of substitutions/insertions/deletions. Matcher will return positions of last matched
     * letter in the motif in ascending order.
     *
     * @param maxNumberOfErrors maximal number of allowed substitutions/insertions/deletions
     * @param sequence          target sequence
     * @return matcher which will return positions of last matched letter in the motif
     */
    public BitapMatcher substitutionAndIndelMatcherLast(int maxNumberOfErrors, final Sequence sequence) {
        return substitutionAndIndelMatcherLast(maxNumberOfErrors, sequence, 0, sequence.size());
    }

    /**
     * Returns a BitapMatcher preforming a fuzzy search in a subsequence of {@code sequence}. Search range starts from
     * {@code from} (inclusive) and ends at {@code to} (exclusive). Search allows no more than {@code
     * maxNumberOfErrors}
     * number of substitutions/insertions/deletions. Matcher will return positions of last matched letter in the motif
     * in ascending order.
     *
     * @param maxNumberOfErrors maximal number of allowed substitutions/insertions/deletions
     * @param sequence          target sequence
     * @param from              left boundary of search range (inclusive)
     * @param to                right boundary of search range (exclusive)
     * @return matcher which will return positions of last matched letter in the motif in ascending order
     */
    public BitapMatcher substitutionAndIndelMatcherLast(int maxNumberOfErrors, final Sequence sequence, int from, int to) {
        if (sequence.getAlphabet().size() != patternMask.length)
            throw new IllegalArgumentException();

        return new BitapMatcherImpl(maxNumberOfErrors + 1, from, to) {
            @Override
            public int findNext() {
                long matchingMask = (1L << (size - 1));

                int d;
                long preInsertionTmp, preMismatchTmp,
                        insertionTmp, deletionTmp, mismatchTmp;

                boolean match = false;

                for (int i = current; i < to; ++i) {
                    long currentPatternMask = patternMask[sequence.codeAt(i)];

                    // Exact match on the previous step == match with insertion on current step
                    insertionTmp = R[0];
                    R[0] <<= 1;
                    mismatchTmp = R[0];
                    R[0] |= currentPatternMask;
                    deletionTmp = R[0];

                    if (0 == (R[0] & matchingMask)) {
                        errors = 0;
                        match = true;
                    }

                    for (d = 1; d < R.length; ++d) {
                        preInsertionTmp = R[d];
                        R[d] <<= 1;
                        preMismatchTmp = R[d];
                        R[d] |= currentPatternMask;
                        R[d] &= insertionTmp & mismatchTmp & (deletionTmp << 1);
                        if (!match && 0 == (R[d] & matchingMask)) {
                            errors = d;
                            match = true;
                        }
                        deletionTmp = R[d];
                        insertionTmp = preInsertionTmp;
                        mismatchTmp = preMismatchTmp;
                    }

                    if (match) {
                        current = i + 1;
                        return i;
                    }
                }
                current = to;
                return -1;
            }
        };
    }

    /**
     * Returns a BitapMatcher preforming a fuzzy search in a whole {@code sequence}. Search allows no more than {@code
     * maxNumberOfErrors} number of substitutions/insertions/deletions. Matcher will return positions of first matched
     * letter in the motif in descending order.
     *
     * @param maxNumberOfErrors maximal number of allowed substitutions/insertions/deletions
     * @param sequence          target sequence
     * @return matcher which will return positions of first matched letter in the motif in descending order
     */
    public BitapMatcher substitutionAndIndelMatcherFirst(int maxNumberOfErrors, final Sequence sequence) {
        return substitutionAndIndelMatcherFirst(maxNumberOfErrors, sequence, 0, sequence.size());
    }

    /**
     * Returns a BitapMatcher preforming a fuzzy search in a subsequence of {@code sequence}. Search range starts from
     * {@code from} (inclusive) and ends at {@code to} (exclusive). Search allows no more than {@code
     * maxNumberOfErrors} number of substitutions/insertions/deletions. Matcher will return positions of first matched
     * letter in the motif in descending order.
     *
     * @param maxNumberOfErrors maximal number of allowed substitutions/insertions/deletions
     * @param sequence          target sequence
     * @param from              left boundary of search range (inclusive)
     * @param to                right boundary of search range (exclusive)
     * @return matcher which will return positions of first matched letter in the motif in descending order
     */
    public BitapMatcher substitutionAndIndelMatcherFirst(int maxNumberOfErrors, final Sequence sequence, int from, int to) {
        if (sequence.getAlphabet().size() != patternMask.length)
            throw new IllegalArgumentException();

        return new BitapMatcherImpl(maxNumberOfErrors + 1, to - 1, from) {
            @Override
            public int findNext() {
                long matchingMask = (1L << (size - 1));

                int d;
                long preInsertionTmp, preMismatchTmp,
                        insertionTmp, deletionTmp, mismatchTmp;

                boolean match = false;

                for (int i = current; i >= to; --i) {
                    long currentPatternMask = reversePatternMask[sequence.codeAt(i)];

                    // Exact match on the previous step == match with insertion on current step
                    insertionTmp = R[0];
                    R[0] <<= 1;
                    mismatchTmp = R[0];
                    R[0] |= currentPatternMask;
                    deletionTmp = R[0];

                    if (0 == (R[0] & matchingMask)) {
                        errors = 0;
                        match = true;
                    }

                    for (d = 1; d < R.length; ++d) {
                        preInsertionTmp = R[d];
                        R[d] <<= 1;
                        preMismatchTmp = R[d];
                        R[d] |= currentPatternMask;
                        R[d] &= insertionTmp & mismatchTmp & (deletionTmp << 1);
                        if (!match && 0 == (R[d] & matchingMask)) {
                            errors = d;
                            match = true;
                        }
                        deletionTmp = R[d];
                        insertionTmp = preInsertionTmp;
                        mismatchTmp = preMismatchTmp;
                    }

                    if (match) {
                        current = i - 1;
                        return i;
                    }
                }
                current = to - 1;
                return -1;
            }
        };
    }
}
