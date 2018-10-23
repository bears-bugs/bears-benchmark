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
package com.milaboratory.core.sequence;

import com.milaboratory.core.Range;

/**
 * A container of sequence and its quality.
 *
 * @param <S> type of sequence
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 * @see com.milaboratory.core.sequence.Sequence
 * @see com.milaboratory.core.sequence.SequenceQuality
 * @see com.milaboratory.core.sequence.NSequenceWithQuality
 */
public class SequenceWithQuality<S extends Sequence<S>>
        implements java.io.Serializable {//implements Seq<? extends SequenceWithQuality<S>>
    final S sequence;
    final SequenceQuality quality;

    /**
     * Creates sequence with quality from specified values.
     *
     * @param sequence sequence
     * @param quality  sequence quality
     * @throws java.lang.IllegalArgumentException if {@code sequence.size() != quality.size()}
     */
    public SequenceWithQuality(S sequence, SequenceQuality quality) {
        if (sequence.size() != quality.size())
            throw new IllegalArgumentException("Different sizes.");
        this.sequence = sequence;
        this.quality = quality;
    }

    /**
     * Returns sequence.
     *
     * @return sequence
     */
    public S getSequence() {
        return sequence;
    }

    /**
     * Returns quality.
     *
     * @return quality
     */
    public SequenceQuality getQuality() {
        return quality;
    }

    /**
     * Returns subsequence with its quality starting at {@code from} (inclusive) and ending at {@code to}
     * (exclusive).
     *
     * @param from starting point of subsequence (inclusive)
     * @param to   ending point of subsequence (exclusive)
     * @return subsequence with its quality starting at {@code from} (inclusively) and ending at {@code to}
     * (exclusively
     */
//    @Override
    public SequenceWithQuality<S> getSubSequence(int from, int to) {
        return getSubSequence(new Range(from, to));
    }

    /**
     * Returns subsequence with its quality with starting (inclusive) and ending (exclusive) points defined by
     * {@code range}.
     *
     * @param range range that defines starting (inclusive) and ending (exclusive) points of subsequence
     * @return subsequence with its quality with starting (inclusive) and ending (exclusive) points defined by
     * {@code range}.
     */
//    @Override
    public SequenceWithQuality<S> getSubSequence(Range range) {
        return new SequenceWithQuality<>(sequence.getRange(range), quality.getRange(range));
    }

    /**
     * Returns the size of this sequence and quality (sequence and quality has same sizes).
     *
     * @return size of this sequence and quality
     */
//    @Override
    public int size() {
        return sequence.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SequenceWithQuality that = (SequenceWithQuality) o;

        return sequence.equals(that.sequence)
                && quality.equals(that.quality);
    }

    @Override
    public int hashCode() {
        int result = sequence.hashCode();
        result = 31 * result + quality.hashCode();
        return result;
    }

    /**
     * Returns a pretty string representation of this.
     *
     * @return pretty string representation of this
     */
    public String toPrettyString() {
        String seq = sequence.toString();
        String qual = quality.toString();
        return seq + '\n' + qual;
    }

    @Override
    public String toString() {
        return sequence.toString() + " " +
                quality.toString();
    }
}
