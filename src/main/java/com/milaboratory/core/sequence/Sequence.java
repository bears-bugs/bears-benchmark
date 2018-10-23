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

import com.milaboratory.core.alignment.batch.HasSequence;
import com.milaboratory.core.motif.Motif;
import com.milaboratory.primitivio.annotations.Serializable;

/**
 * Parent class for all types of sequences. Each element of sequence (e.g. nucleotide, or amino acid)
 * encoded in byte, so {@code Sequence} is a simple container of ordered bytes; the correspondence between byte codes
 * and particular elements is defined in {@link com.milaboratory.core.sequence.Alphabet} that corresponds to this
 * type of sequence (via {@link #getAlphabet()}).
 *
 * @param <S> type of sequence
 * @author Bolotin Dmitriy (bolotin.dmitriy@gmail.com)
 * @author Shugay Mikhail (mikhail.shugay@gmail.com)
 * @see com.milaboratory.core.sequence.Alphabet
 * @see com.milaboratory.core.sequence.SequenceBuilder
 * @see com.milaboratory.core.sequence.NucleotideSequence
 * @see com.milaboratory.core.sequence.AminoAcidSequence
 */
@Serializable(by = IO.SequenceSerializer.class)
public abstract class Sequence<S extends Sequence<S>> extends AbstractSeq<S> implements Comparable<S>, HasSequence<S> {
    /**
     * Returns letter code at specified position.
     *
     * @param position position in sequence
     * @return element at specified position
     */
    public abstract byte codeAt(int position);

    /**
     * Returns the alphabet corresponding to this type of sequence.
     *
     * @return alphabet corresponding to this type of sequence
     */
    public abstract Alphabet<S> getAlphabet();

    /**
     * Returns an array of bytes that encodes this sequence.
     *
     * @return array of bytes that encodes this sequence
     */
    public byte[] asArray() {
        byte[] bytes = new byte[size()];
        for (int i = size() - 1; i >= 0; --i)
            bytes[i] = codeAt(i);
        return bytes;
    }

    /**
     * Returns a character representation of element at specified position.
     *
     * @param position position in this sequence
     * @return character representation of element at specified position
     */
    public char symbolAt(int position) {
        return getAlphabet().codeToSymbol(codeAt(position));
    }

    /**
     * Converts sequnce to motif data structure efficient for exact and fuzzy wildcard-aware matching and searching of
     * sequences.
     *
     * @return motif
     */
    @SuppressWarnings("unchecked")
    public Motif<S> toMotif() {
        return new Motif(this);
    }

    public boolean containWildcards() {
        for (int i = 0; i < size(); i++)
            if (getAlphabet().isWildcard(codeAt(i)))
                return true;
        return false;
    }

    @Override
    public S getSequence() {
        return (S) this;
    }

    @Override
    public SequenceBuilder<S> getBuilder() {
        return getAlphabet().createBuilder();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Sequence))
            return false;
        final Sequence other = (Sequence) obj;
        if (other.getAlphabet() != getAlphabet())
            return false;
        if (other.size() != this.size())
            return false;
        for (int i = size() - 1; i >= 0; --i)
            if (other.codeAt(i) != codeAt(i))
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash += 31 * getAlphabet().hashCode();
        for (int i = size() - 1; i >= 0; --i)
            hash = hash * 7 + codeAt(i);
        return hash;
    }

    @Override
    public String toString() {
        char[] chars = new char[size()];
        for (int i = 0; i < size(); i++)
            chars[i] = getAlphabet().codeToSymbol(codeAt(i));
        return new String(chars);
    }

    @Override
    public int compareTo(S o) {
        if (this.getAlphabet() != o.getAlphabet())
            throw new RuntimeException();
        if (this.size() != o.size())
            if (this.size() < o.size())
                return -1;
            else
                return 1;
        byte b0, b1;
        for (int i = 0; i < size(); i++) {
            b0 = this.codeAt(i);
            b1 = o.codeAt(i);
            if (b0 != b1)
                if (b0 < b1)
                    return -1;
                else
                    return 1;
        }
        return 0;
    }

    /**
     * Tests whether this sequence contains {@code subSequence} and returns position of the first matched letter in
     * this sequence or -1 if it does not contain {@code subSequence}.
     *
     * @param subSequence subsequence
     * @return position of the first matched letter in this sequence or -1 if it does not contain {@code subSequence}
     */
    public int indexOf(S subSequence) {
        if (subSequence.size() == 0)
            return -1;
        int limit = size() - subSequence.size();
        next:
        for (int i = 0; i <= limit; i++) {
            for (int j = 0; j < subSequence.size(); j++)
                if (subSequence.codeAt(j) != codeAt(i + j))
                    continue next;
            return i;
        }
        return -1;
    }
}
