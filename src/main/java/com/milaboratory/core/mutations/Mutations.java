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
package com.milaboratory.core.mutations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.milaboratory.core.Range;
import com.milaboratory.core.sequence.*;
import com.milaboratory.primitivio.annotations.Serializable;
import com.milaboratory.util.IntArrayList;

import java.util.Arrays;

import static com.milaboratory.core.mutations.Mutation.*;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
@JsonSerialize(using = IO.JsonMutationsSerializer.class)
@JsonDeserialize(using = IO.JsonMutationsDeserializer.class)
@Serializable(by = IO.MutationsSerializer.class)
public final class Mutations<S extends Sequence<S>>
        implements java.io.Serializable {
    final Alphabet<S> alphabet;
    final int[] mutations;

    public Mutations(Alphabet<S> alphabet, IntArrayList mutations) {
        this(alphabet, mutations.toArray(), true);
    }

    public Mutations(Alphabet<S> alphabet, String encodedMutations) {
        this(alphabet, MutationsUtil.decode(encodedMutations, alphabet), true);
    }

    public Mutations(Alphabet<S> alphabet, int... mutations) {
        if (!MutationsUtil.isSorted(mutations))
            throw new IllegalArgumentException("Not sorted according to positions.");
        this.mutations = mutations.clone();
        this.alphabet = alphabet;
    }

    Mutations(Alphabet<S> alphabet, int[] mutations, boolean unsafe) {
        assert unsafe;
        assert MutationsUtil.isSorted(mutations);
        this.mutations = mutations;
        this.alphabet = alphabet;
    }

    public int size() {
        return mutations.length;
    }

    public Alphabet<S> getAlphabet() {
        return alphabet;
    }

    public int getMutation(int index) {
        return mutations[index];
    }

    public int[] getRAWMutations() {
        return mutations.clone();
    }

    public boolean isEmpty() {
        return mutations.length == 0;
    }

    public int getPositionByIndex(int index) {
        return getPosition(mutations[index]);
    }

    public byte getFromAsCodeByIndex(int index) {
        return getFrom(mutations[index]);
    }

    public byte getToAsCodeByIndex(int index) {
        return getTo(mutations[index]);
    }

    public char getFromAsSymbolByIndex(int index) {
        return alphabet.codeToSymbol(getFromAsCodeByIndex(index));
    }

    public char getToAsSymbolByIndex(int index) {
        return alphabet.codeToSymbol(getToAsCodeByIndex(index));
    }

    public int getRawTypeByIndex(int index) {
        return getRawTypeCode(mutations[index]);
    }

    public MutationType getTypeByIndex(int index) {
        return getType(mutations[index]);
    }

    public boolean isCompatibleWith(S sequence) {
        return MutationsUtil.isCompatibleWithSequence(sequence, mutations);
    }

    public S mutate(S sequence) {
        int length = sequence.size();
        for (int i : mutations)
            switch (i & MUTATION_TYPE_MASK) {
                case RAW_MUTATION_TYPE_DELETION:
                    --length;
                    break;
                case RAW_MUTATION_TYPE_INSERTION:
                    ++length;
                    break;
            }
        SequenceBuilder<S> builder = alphabet.createBuilder().ensureCapacity(length);
        int pointer = 0;
        int mutPointer = 0;
        int mut;
        while (pointer < sequence.size() || mutPointer < mutations.length) {
            if (mutPointer < mutations.length && ((mut = mutations[mutPointer]) >>> POSITION_OFFSET) <= pointer)
                switch (mut & MUTATION_TYPE_MASK) {
                    case RAW_MUTATION_TYPE_SUBSTITUTION:
                        if (((mut >> FROM_OFFSET) & LETTER_MASK) != sequence.codeAt(pointer))
                            throw new IllegalArgumentException("Mutation = " + Mutation.toString(sequence.getAlphabet(), mut) +
                                    " but seq[" + pointer + "]=" + sequence.symbolAt(pointer));

                        ++pointer;
                        builder.append((byte) (mut & LETTER_MASK));
                        ++mutPointer;
                        break;
                    case RAW_MUTATION_TYPE_DELETION:
                        if (((mut >> FROM_OFFSET) & LETTER_MASK) != sequence.codeAt(pointer))
                            throw new IllegalArgumentException("Mutation = " + Mutation.toString(sequence.getAlphabet(), mut) +
                                    " but seq[" + pointer + "]=" + sequence.symbolAt(pointer));

                        ++pointer;
                        ++mutPointer;
                        break;
                    case RAW_MUTATION_TYPE_INSERTION:
                        builder.append((byte) (mut & LETTER_MASK));
                        ++mutPointer;
                        break;
                }
            else
                builder.append(sequence.codeAt(pointer++));
        }
        return builder.createAndDestroy();
    }

    /**
     * Converts position from coordinates in seq1 (before mutation) to coordinates in seq2 (after mutation) using this
     * alignment (mutations).
     *
     * If letter in provided position is marked as deleted (deletion) in this mutations, this method will return {@code
     * (- 1 - imagePosition)}, where {@code imagePosition} is a position of letter right after that place where target
     * nucleotide was removed according to this alignment.
     *
     * @param seq1Position position in seq1
     * @return position in seq2
     */
    public int convertToSeq2Position(int seq1Position) {
        int p, result = seq1Position;

        for (int mut : mutations) {
            p = getPosition(mut);

            if (p > seq1Position)
                return result;

            switch (mut & MUTATION_TYPE_MASK) {
                case RAW_MUTATION_TYPE_DELETION:
                    if (p == seq1Position)
                        return -result - 1;
                    --result;
                    break;
                case RAW_MUTATION_TYPE_INSERTION:
                    ++result;
                    break;
            }
        }

        return result;
    }

    /**
     * Converts position from coordinates in seq2 (after mutation) to coordinates in seq1 (before mutation) using this
     * alignment (mutations).
     *
     * If letter in provided position is marked as insertion in this mutations, this method will return {@code
     * (- 1 - imagePosition)}, where {@code imagePosition} is a position of letter right after that place where target
     * nucleotide was added according to this alignment.
     *
     * @param seq2Position position in seq2
     * @return position in seq1
     */
    public int convertToSeq1Position(int seq2Position) {
        int seq1p, seq2p = 0, prevSeq1p = 0, prevSeq2p = 0;
        boolean onInsertion;

        for (int mut : mutations) {
            seq1p = getPosition(mut);
            onInsertion = false;

            switch (mut & MUTATION_TYPE_MASK) {
                case RAW_MUTATION_TYPE_DELETION:
                    --seq2p;
                    break;
                case RAW_MUTATION_TYPE_INSERTION:
                    onInsertion = true;
                    --seq1p;
                    ++seq2p;
                    break;
            }

            seq2p += seq1p - prevSeq1p;

            if (seq2p == seq2Position && onInsertion)
                return -1 - (seq2Position - prevSeq2p + prevSeq1p);

            if (seq2p >= seq2Position) {
                return seq2Position - prevSeq2p + prevSeq1p;
            }

            prevSeq1p = seq1p;
            prevSeq2p = seq2p;
        }

        return seq2Position - prevSeq2p + prevSeq1p;
    }

    /**
     * Returns the difference between the length of initial sequence and length of mutated sequence. Negative values
     * denotes that mutated sequence is shorter.
     *
     * @return difference between the length of initial sequence and mutated sequence
     */
    public int getLengthDelta() {
        int delta = 0;

        for (int mut : mutations)
            switch (mut & MUTATION_TYPE_MASK) {
                case RAW_MUTATION_TYPE_DELETION:
                    --delta;
                    break;
                case RAW_MUTATION_TYPE_INSERTION:
                    ++delta;
                    break;
            }

        return delta;
    }

    /**
     * Concatenates this and other
     */
    public Mutations<S> concat(final Mutations<S> other) {
        return new MutationsBuilder<>(alphabet, false)
                .ensureCapacity(this.size() + other.size())
                .append(this)
                .append(other)
                .createAndDestroy();
    }

    /**
     * Returns combined mutations array ({@code this} applied before {@code other}).
     *
     * @param other second mutations object
     * @return combined mutations
     */
    public Mutations<S> combineWith(final Mutations<S> other) {
        IntArrayList result = new IntArrayList(mutations.length + other.mutations.length);

        //mut2 pointer
        int p2 = 0, position0 = 0, delta = 0;

        for (int p1 = 0; p1 < mutations.length; ++p1) {

            position0 = getPosition(mutations[p1]);

            while (p2 < other.mutations.length && // There are mutations in m2
                    (getPosition(other.mutations[p2]) < position0 + delta || // Before current point
                            (getPosition(other.mutations[p2]) == position0 + delta && // On the current point and it is insertion
                                    getRawTypeCode(other.mutations[p2]) == RAW_MUTATION_TYPE_INSERTION)))
                appendInCombine(result, Mutation.move(other.mutations[p2++], -delta));

            switch (getRawTypeCode(mutations[p1])) {
                case RAW_MUTATION_TYPE_INSERTION:
                    if (p2 < other.mutations.length && getPosition(other.mutations[p2]) == delta + position0) {
                        if (getTo(mutations[p1]) != getFrom(other.mutations[p2]))
                            throw new IllegalArgumentException();

                        if (isSubstitution(other.mutations[p2]))
                            appendInCombine(result, (mutations[p1] & (~LETTER_MASK)) | (other.mutations[p2] & LETTER_MASK));

                        ++p2;
                    } else
                        appendInCombine(result, mutations[p1]);
                    ++delta;
                    break;
                case RAW_MUTATION_TYPE_SUBSTITUTION:
                    if (p2 < other.mutations.length && getPosition(other.mutations[p2]) == delta + position0) {

                        if (getTo(mutations[p1]) != getFrom(other.mutations[p2]))
                            throw new IllegalArgumentException();

                        if (isSubstitution(other.mutations[p2])) {
                            if (getFrom(mutations[p1]) != getTo(other.mutations[p2]))
                                appendInCombine(result, (mutations[p1] & (~LETTER_MASK)) | (other.mutations[p2] & LETTER_MASK));
                        } else if (isDeletion(other.mutations[p2]))
                            appendInCombine(result, createDeletion(position0, getFrom(mutations[p1])));
                        else
                            throw new RuntimeException("Insertion after Del. or Subs.");

                        ++p2;

                    } else
                        appendInCombine(result, mutations[p1]);

                    break;
                case RAW_MUTATION_TYPE_DELETION:
                    --delta;
                    appendInCombine(result, mutations[p1]);
                    break;
            }

        }

        while (p2 < other.mutations.length)
            appendInCombine(result, Mutation.move(other.mutations[p2++], -delta));

        return new Mutations<S>(alphabet, result.toArray(), true);
    }

    /**
     * Moves positions of mutations by specified offset
     *
     * @param offset offset
     * @return relocated positions
     */
    public Mutations<S> move(int offset) {
        int[] newMutations = new int[mutations.length];
        for (int i = 0; i < mutations.length; ++i)
            newMutations[i] = Mutation.move(mutations[i], offset);
        return new Mutations<S>(alphabet, newMutations, true);
    }

    /**
     * Extracts mutations for a range of positions in the original sequence and performs shift of corresponding
     * positions (moves them to {@code -range.from}). <p/> <p>Insertions before {@code range.from} excluded. Insertions
     * after {@code (range.to - 1)} included.</p> <p/> <p><b>Important:</b> to extract leftmost insertions (trailing
     * insertions) use {@code range.from = -1}.</p>
     *
     * @param range range
     * @return mutations for a range of positions
     */
    public Mutations<S> extractRelativeMutationsForRange(Range range) {
        if (range.isReverse())
            throw new IllegalArgumentException("Reverse ranges are not supported by this method.");

        return extractRelativeMutationsForRange(range.getFrom(), range.getTo());
    }

    /**
     * Extracts mutations for a range of positions in the original sequence and performs shift of corresponding
     * positions (moves them to {@code -from}).
     *
     * <p>Insertions before {@code from} excluded. Insertions after {@code (to - 1)} included.</p>
     *
     * <p>
     * <b>Important:</b> to extract leftmost insertions (trailing insertions) use {@code from = -1}. E.g.
     * {@code extractRelativeMutationsForRange(mut, -1, seqLength) == mut}.
     * </p>
     *
     * @param from left bound of range, inclusive. Use -1 to extract leftmost insertions.
     * @param to   right bound of range, exclusive
     * @return mutations for a range of positions
     */
    public Mutations<S> extractRelativeMutationsForRange(int from, int to) {
        if (to < from)
            throw new IllegalArgumentException("Reversed ranges are not supported.");

        long indexRange = getIndexRange(from, to);

        // If range size is 0 return empty array
        if (indexRange == 0)
            return empty(alphabet);

        // Unpacking
        int fromIndex = (int) (indexRange >>> 32),
                toIndex = (int) (indexRange & 0xFFFFFFFF);

        // Don't create new object if result will be equal to this
        if (from == 0 && fromIndex == 0 && toIndex == mutations.length)
            return this;

        // Creating result
        int[] result = new int[toIndex - fromIndex];

        // Constant to move positions in the output array
        int offset;
        if (from == -1)
            offset = 0;
        else
            offset = ((-from) << POSITION_OFFSET);

        // Copy and move mutations
        for (int i = result.length - 1, j = toIndex - 1; i >= 0; --i, --j)
            result[i] = mutations[j] + offset;

        return new Mutations<>(alphabet, result, true);
    }

    /**
     * Extracts mutations for a range of positions in the original sequence.
     *
     * <p>Insertions before {@code from} excluded. Insertions after {@code (to - 1)} included.</p>
     *
     * <p><b>Important:</b> to extract leftmost insertions (trailing insertions) use range with {@code from = -1}.</p>
     *
     * @param range target range in original sequence
     * @return mutations for a range of positions in original sequence
     */
    public Mutations<S> extractAbsoluteMutationsForRange(Range range) {
        return extractAbsoluteMutationsForRange(range.getFrom(), range.getTo());
    }

    /**
     * Extracts mutations for a range of positions in the original sequence.
     *
     * <p>Insertions before {@code from} excluded. Insertions after {@code (to - 1)} included.</p>
     *
     * <p>
     * <b>Important:</b> to extract leftmost insertions (trailing insertions) use {@code from = -1}. E.g.
     * {@code extractAbsoluteMutationsForRange(mut, -1, seqLength) == mut}.
     * </p>
     *
     * @param from left bound of range, inclusive. Use -1 to extract leftmost insertions.
     * @param to   right bound of range, exclusive
     * @return mutations for a range of positions in original sequence
     */
    public Mutations<S> extractAbsoluteMutationsForRange(int from, int to) {
        if (to < from)
            throw new IllegalArgumentException("Reversed ranges are not supported.");

        long indexRange = getIndexRange(from, to);

        // If range size is 0 return empty array
        if (indexRange == 0)
            return empty(alphabet);

        // Unpacking
        int fromIndex = (int) (indexRange >>> 32),
                toIndex = (int) (indexRange & 0xFFFFFFFF);

        // Don't create new object if result will be equal to this
        if (from == 0 && fromIndex == 0 && toIndex == mutations.length)
            return this;

        return new Mutations<>(alphabet, Arrays.copyOfRange(mutations, fromIndex, toIndex), true);
    }

    /**
     * See {@link #removeMutationsInRange(int, int)}.
     *
     * <p>Ranges must be sorted.</p>
     *
     * @param ranges ranges to remove
     */
    public Mutations<S> removeMutationsInRanges(Range... ranges) {
        Mutations<S> result = this;
        int offset = 0;
        int lastTo = 0;
        for (Range range : ranges) {
            if (range.getFrom() < lastTo)
                throw new IllegalArgumentException("Ranges are not sorted.");
            result = result.removeMutationsInRange(range.move(offset));
            offset -= range.length();
            lastTo = range.getTo();
        }
        return result;
    }

    /**
     * See {@link #removeMutationsInRange(int, int)}.
     *
     * @param range range to removes
     */
    public Mutations<S> removeMutationsInRange(Range range) {
        return removeMutationsInRange(range.getFrom(), range.getTo());
    }

    /**
     * Removes mutations for a range of positions in the original sequence and performs shift of corresponding
     * positions of mutations.
     *
     * <p>Insertions before {@code from} will be left untouched. Insertions after {@code (to - 1)} will be removed.</p>
     *
     * <p>
     * <b>Important:</b> to remove leftmost insertions (left trailing insertions) use {@code from = -1}. E.g.
     * {@code extractRelativeMutationsForRange(mut, -1, seqLength) == mut}.
     * </p>
     *
     * @param from left bound of range, inclusive. Use -1 to extract leftmost insertions.
     * @param to   right bound of range, exclusive
     * @return mutations for a range of positions
     */
    public Mutations<S> removeMutationsInRange(int from, int to) {
        if (to < from)
            throw new IllegalArgumentException("Reversed ranges are not supported.");

        // If range is empty return untouched mutations object
        if (from == to)
            return this;

        // Determine range in mutations to remove
        long indexRange = getIndexRange(from, to);

        // Unpacking
        int fromIndex = (int) (indexRange >>> 32),
                toIndex = (int) (indexRange & 0xFFFFFFFF);

        if (fromIndex == 0 && toIndex == mutations.length)
            return empty(alphabet);

        // Creating result
        int[] result = new int[mutations.length - (toIndex - fromIndex)];

        // Constant to move positions in the output array
        int offset = (from - to) << POSITION_OFFSET; // Negative value

        // Copy and move mutations
        int i = 0;
        for (int j = 0; j < fromIndex; ++j)
            result[i++] = mutations[j];
        for (int j = toIndex; j < mutations.length; ++j)
            result[i++] = mutations[j] + offset;

        assert i == result.length;

        return new Mutations<>(alphabet, result, true);
    }

    private long getIndexRange(int from, int to) {
        // If range size is 0 return empty array
        if (from == to)
            return 0;

        // Find first mutation for the range
        int fromIndex = firstMutationWithPosition(from);
        if (fromIndex < 0)
            fromIndex = -fromIndex - 1;

        // If first mutations are insertions with position == from:
        // remove them from output
        while (fromIndex < mutations.length &&
                (mutations[fromIndex] >>> POSITION_OFFSET) == from &&
                (mutations[fromIndex] & MUTATION_TYPE_MASK) == RAW_MUTATION_TYPE_INSERTION)
            ++fromIndex;

        // Find last mutation
        int toIndex = firstMutationWithPosition(fromIndex, mutations.length, to);
        if (toIndex < 0)
            toIndex = -toIndex - 1;

        while (toIndex < mutations.length &&
                (mutations[toIndex] >>> POSITION_OFFSET) == to &&
                (mutations[toIndex] & MUTATION_TYPE_MASK) == RAW_MUTATION_TYPE_INSERTION)
            ++toIndex;

        // Return indices packed into single long value
        return (((long) fromIndex) << 32) | ((long) toIndex);
    }

    /**
     * Inverts mutations, so that they reflect difference from seq2 to seq1. <p/> E.g. for mutations generated with
     * <pre>
     * NucleotideSequence ref = randomSequence(300);
     * int[] mutations = Mutations.generateMutations(ref,
     *                             MutationModels.getEmpiricalNucleotideMutationModel()
     *                             .multiply(3.0));
     * </pre>
     * and the inverted mutations
     * <pre>
     * int[] invMutations = ConsensusAligner.invertMutations(mutations);
     * </pre>
     * The following two methods are equal
     * <pre>
     * Mutations.printAlignment(ref, mutations);
     * Mutations.printAlignment(Mutations.mutate(ref, mutations), invMutations);
     * </pre>
     * Same stands for
     * <pre>
     * Mutations.getPosition(mutations, posInSeq1)
     * </pre>
     *
     * @return mutations that will generate seq1 from seq2
     */
    public Mutations<S> invert() {
        if (mutations.length == 0)
            return this;

        int[] newMutations = new int[mutations.length];
        int delta = 0;
        for (int i = 0; i < mutations.length; i++) {
            int from = getFrom(mutations[i]);
            int to = getTo(mutations[i]);
            int pos = getPosition(mutations[i]);
            int type = getRawTypeCode(mutations[i]);
            switch (type) {
                case RAW_MUTATION_TYPE_DELETION:
                    delta--;
                    type = RAW_MUTATION_TYPE_INSERTION;
                    pos++;
                    break;
                case RAW_MUTATION_TYPE_INSERTION:
                    delta++;
                    type = RAW_MUTATION_TYPE_DELETION;
                    pos--;
                    break;
                default:
                    break;
            }
            newMutations[i] = createMutation(type, pos + delta, to, from);
        }
        return new Mutations<>(alphabet, newMutations, true);
    }

    public int countOfIndels() {
        int result = 0;
        for (int mutation : mutations)
            switch (mutation & MUTATION_TYPE_MASK) {
                case RAW_MUTATION_TYPE_DELETION:
                case RAW_MUTATION_TYPE_INSERTION:
                    result++;
            }

        return result;
    }

    public int countOf(final MutationType type) {
        int result = 0;
        for (int mutation : mutations)
            if ((mutation & MUTATION_TYPE_MASK) == type.rawType)
                result++;
        return result;
    }

    /**
     * Extracts sub mutations by {@code from}-{@code to} mutation indices.
     *
     * @param from index in current mutations object pointing to the first mutation to be extracted
     * @param to   index in current mutations object pointing to the next after last mutation to be extracted
     * @return sub mutations
     */
    public Mutations<S> getRange(int from, int to) {
        return new Mutations<>(alphabet, Arrays.copyOfRange(mutations, from, to));
    }

    public int firsMutationPosition() {
        if (isEmpty())
            return -1;

        return getPosition(mutations[0]);
    }

    public int lastMutationPosition() {
        if (isEmpty())
            return -1;

        return getPosition(mutations[mutations.length - 1]);
    }

    public Range getMutatedRange() {
        if (isEmpty())
            return null;
        return new Range(firsMutationPosition(), lastMutationPosition());
    }

    @Override
    public String toString() {
        if (mutations.length == 0)
            return "[]";

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int mut : mutations)
            builder.append(Mutation.toString(alphabet, mut) + ",");
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

    public String encode() {
        return MutationsUtil.encode(mutations, alphabet);
    }

    public String encode(String separator) {
        return MutationsUtil.encode(mutations, alphabet, separator);
    }

    public String encodeFixed() {
        return MutationsUtil.encodeFixed(mutations, alphabet);
    }

    public static Mutations<NucleotideSequence> decodeNuc(String string) {
        return decode(string, NucleotideSequence.ALPHABET);
    }

    public static Mutations<AminoAcidSequence> decodeAA(String string) {
        return decode(string, AminoAcidSequence.ALPHABET);
    }

    public static <S extends Sequence<S>> Mutations<S> decode(String string, Alphabet<S> alphabet) {
        return new Mutations<>(alphabet, MutationsUtil.decode(string, alphabet), true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mutations mutations1 = (Mutations) o;
        if (alphabet != mutations1.alphabet) return false;
        return Arrays.equals(mutations, mutations1.mutations);
    }

    @Override
    public int hashCode() {
        int result = alphabet.hashCode();
        result = 31 * result + Arrays.hashCode(mutations);
        return result;
    }

    /**
     * Converts positions returned by {@link #convertToSeq2Position(int)} and {@link #convertToSeq1Position(int)} to a
     * positive number by applying (-1 - x) transformation for negative input values. So, for non-existing positions
     * (if corresponding letter is absent in the target sequence, like in case of deletion and {@link
     * #convertToSeq1Position(int)} method) position of the first existing letter in target sequence will be returned.
     *
     * @param position position returned by {@link #convertToSeq2Position(int)} or {@link #convertToSeq1Position(int)}
     *                 methods
     * @return positive position
     */
    public static int pabs(int position) {
        return position >= 0 ? position : -1 - position;
    }

    public int firstMutationWithPosition(int position) {
        return firstMutationWithPosition(0, mutations.length, position);
    }

    public int firstMutationWithPosition(int fromIndex, int toIndex, int position) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = mutations[mid] >>> POSITION_OFFSET;

            if (midVal < position)
                low = mid + 1;
            else if (midVal > position)
                high = mid - 1;
            else {
                // key found
                // searching for first occurance
                while (mid > 0 && (mutations[mid - 1] >>> POSITION_OFFSET) == position)
                    --mid;

                return mid;
            }
        }
        return -(low + 1);  // key not found.
    }

    private static void appendInCombine(IntArrayList result, int mutation) {
        if (isSubstitution(mutation) || result.isEmpty())
            result.add(mutation);
        else {
            int last = result.peek();

            if (isSubstitution(last))
                result.add(mutation);
            else {

                int lPosition = getPosition(last);
                int mPosition = getPosition(mutation);

                if (lPosition == mPosition &&
                        isInsertion(last) && isDeletion(mutation))
                    cfs(result, lPosition, getFrom(mutation), getTo(last));
                else if (lPosition == mPosition - 1 &&
                        isDeletion(last) && isInsertion(mutation))
                    cfs(result, lPosition, getFrom(last), getTo(mutation));
                else
                    result.add(mutation);
            }
        }
    }

    private static void cfs(IntArrayList result, int position, int from, int to) {
        if (from == to)
            result.pop();
        else
            result.set(result.size() - 1, createSubstitution(position, from, to));
    }

    /**
     * Identity mutations object for nucleotide sequences.
     */
    public static final Mutations<NucleotideSequence> EMPTY_NUCLEOTIDE_MUTATIONS = new Mutations<>(NucleotideSequence.ALPHABET);

    /**
     * Identity mutations object for amino acid sequences.
     */
    public static final Mutations<AminoAcidSequence> EMPTY_AMINO_ACID_MUTATIONS = new Mutations<>(AminoAcidSequence.ALPHABET);

    @SuppressWarnings("unchecked")
    public static <S extends Sequence<S>> Mutations<S> empty(Alphabet<S> alphabet) {
        if ((Alphabet) alphabet == NucleotideSequence.ALPHABET)
            return (Mutations<S>) EMPTY_NUCLEOTIDE_MUTATIONS;
        else if ((Alphabet) alphabet == AminoAcidSequence.ALPHABET)
            return (Mutations<S>) EMPTY_AMINO_ACID_MUTATIONS;
        else
            return new Mutations<>(alphabet);
    }
}
