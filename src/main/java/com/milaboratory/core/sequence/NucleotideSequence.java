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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.milaboratory.core.Range;

/**
 * Representation of nucleotide sequence.
 *
 * @author Bolotin Dmitriy (bolotin.dmitriy@gmail.com)
 * @author Shugay Mikhail (mikhail.shugay@gmail.com)
 * @see com.milaboratory.core.sequence.Sequence
 * @see com.milaboratory.core.sequence.NucleotideAlphabet
 */
@JsonSerialize(using = IO.NSeqSerializer.class)
@JsonDeserialize(using = IO.NSeqDeserializer.class)
public final class NucleotideSequence extends AbstractArraySequence<NucleotideSequence>
        implements NSeq<NucleotideSequence>, java.io.Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * Nucleotide alphabet
     */
    public static final NucleotideAlphabet ALPHABET = NucleotideAlphabet.INSTANCE;

    /**
     * Empty instance
     */
    public static final NucleotideSequence EMPTY = new NucleotideSequence("");

    /**
     * Creates nucleotide sequence from its string representation (e.g. "ATCGG" or "atcgg").
     *
     * @param sequence string representation of sequence (case insensitive)
     * @throws java.lang.IllegalArgumentException if sequence contains unknown nucleotide symbol
     */
    public NucleotideSequence(String sequence) {
        super(sequence);
    }

    /**
     * Creates nucleotide sequence from char array of nucleotides (e.g. ['A','T','C','G','G']).
     *
     * @param sequence char array of nucleotides
     * @throws java.lang.IllegalArgumentException if sequence contains unknown nucleotide symbol
     */
    public NucleotideSequence(char[] sequence) {
        super(sequence);
    }

    /**
     * Creates nucleotide sequence from specified {@code Bit2Array} (will be copied in constructor).
     *
     * @param data Bit2Array
     */
    public NucleotideSequence(byte[] data) {
        super(data.clone());
    }

    NucleotideSequence(byte[] data, boolean unsafe) {
        super(data);
        assert unsafe;
    }

    @Override
    public NucleotideSequence getRange(Range range) {
        if (range.getLower() < 0 || range.getUpper() < 0
                || range.getLower() > size() || range.getUpper() > size())
            throw new IndexOutOfBoundsException();

        if (range.length() == 0)
            return EMPTY;

        if (range.isReverse())
            return new NucleotideSequence(
                    transformToRC(data, range.getLower(), range.getUpper()), true);
        else
            return super.getRange(range);
    }

    /**
     * Returns reverse complement of this sequence.
     *
     * @return reverse complement sequence
     */
    @Override
    public NucleotideSequence getReverseComplement() {
        return new NucleotideSequence(transformToRC(data, 0, data.length), true);
    }

    /**
     * Returns {@literal true} if sequence contains wildcards in specified region.
     *
     * @return {@literal true} if sequence contains wildcards in specified region
     */
    public boolean containsWildcards(int from, int to) {
        for (int i = from; i < to; i++)
            if (isWildcard(codeAt(i)))
                return true;
        return false;
    }

    /**
     * Returns {@literal true} if sequence contains wildcards.
     *
     * @return {@literal true} if sequence contains wildcards
     */
    public boolean containsWildcards() {
        return containsWildcards(0, size());
    }

    @Override
    public NucleotideAlphabet getAlphabet() {
        return ALPHABET;
    }

    /**
     * Creates nucleotide sequence from specified byte array.
     *
     * @param sequence byte array
     * @param offset   offset in {@code sequence}
     * @param length   length of resulting sequence
     * @return nucleotide sequence
     */
    public static NucleotideSequence fromSequence(byte[] sequence, int offset, int length) {
        byte[] storage = new byte[length];
        for (int i = 0; i < length; ++i)
            storage[i] = ALPHABET.symbolToCode((char) sequence[offset + i]);
        return new NucleotideSequence(storage, true);
    }

    private static byte[] transformToRC(byte[] data, int from, int to) {
        byte[] newData = new byte[to - from];
        int reverseCord;
        for (int coord = 0, s = to - from; coord < s; ++coord) {
            reverseCord = to - 1 - coord;
            newData[coord] = NucleotideAlphabet.complementCode(data[reverseCord]);
        }
        return newData;
    }

    private static boolean isWildcard(byte nucleotide) {
        return nucleotide >= 4;
    }
}
