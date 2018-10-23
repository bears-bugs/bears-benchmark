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

import com.milaboratory.core.alignment.AlignmentScoring;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.util.IntArrayList;

public final class Mutation {
    public static final int RAW_MUTATION_TYPE_SUBSTITUTION = 0x20,
            RAW_MUTATION_TYPE_DELETION = 0x40,
            RAW_MUTATION_TYPE_INSERTION = 0x60,
            RAW_MUTATION_TYPE_RESERVED = 0x00,
            MUTATION_TYPE_MASK = 0x60,
            MUTATION_POSITION_MASK = 0xFFFFF000,
            LETTER_MASK = 0x1F,
            FROM_OFFSET = 7,
            POSITION_OFFSET = 12,
            MAX_POSITION_VALUE = 0xFFFFF,
            NON_MUTATION = 0,
            NON_MUTATION_1 = 1,
            MUTATION_TYPE_OFFSET = 5;

    private Mutation() {
    }

    public static int createInsertion(int position, int to) {
        return createMutation(RAW_MUTATION_TYPE_INSERTION, position, 0, to);
    }

    public static int createDeletion(int position, int from) {
        return createMutation(RAW_MUTATION_TYPE_DELETION, position, from, 0);
    }

    public static int createSubstitution(int position, int from, int to) {
        return createMutation(RAW_MUTATION_TYPE_SUBSTITUTION, position, from, to);
    }

    public static int createMutation(MutationType type, int from, int to) {
        return createMutation(type, 0, from, to);
    }

    public static int createMutation(int rawType, int from, int to) {
        return createMutation(rawType, 0, from, to);
    }

    public static int createMutation(MutationType type, int position, int from, int to) {
        if (type == null)
            throw new NullPointerException();

        return createMutation(type.rawType, position, from, to);
    }

    public static int createMutation(int rawType, int position, int from, int to) {
        if (position < 0 || position > MAX_POSITION_VALUE)
            throw new IllegalArgumentException();

        return (position << POSITION_OFFSET) | (from << FROM_OFFSET) | rawType | to;
    }

    public static int getPosition(int code) {
        return code >>> POSITION_OFFSET;
    }

    public static byte getFrom(int code) {
        return (byte) ((code >> FROM_OFFSET) & LETTER_MASK);
    }

    public static char getFromSymbol(int code, Alphabet alphabet) {
        return alphabet.codeToSymbol((byte) ((code >> FROM_OFFSET) & LETTER_MASK));
    }

    public static byte getTo(int code) {
        return (byte) (code & LETTER_MASK);
    }

    public static char getToSymbol(int code, Alphabet alphabet) {
        return alphabet.codeToSymbol((byte) (code & LETTER_MASK));
    }

    /**
     * Returns: 0x20 for substitution, 0x40 for Deletion, 0x60 for insertion.
     *
     * @param code mutation code form mutations array returned by {@link com.milaboratory.core.alignment.Aligner#alignGlobal(AlignmentScoring,
     *             com.milaboratory.core.sequence.Sequence, com.milaboratory.core.sequence.Sequence)} method.
     * @return 0x20 for substitution, 0x40 for Deletion, 0x60 for insertion
     */
    public static int getRawTypeCode(int code) {
        return code & MUTATION_TYPE_MASK;
    }

    public static MutationType getType(int code) {
        switch (code & MUTATION_TYPE_MASK) {
            case RAW_MUTATION_TYPE_SUBSTITUTION:
                return MutationType.Substitution;
            case RAW_MUTATION_TYPE_DELETION:
                return MutationType.Deletion;
            case RAW_MUTATION_TYPE_INSERTION:
                return MutationType.Insertion;
            default:
                return null;
        }
    }

    public static boolean isSubstitution(int code) {
        return (code & MUTATION_TYPE_MASK) == RAW_MUTATION_TYPE_SUBSTITUTION;
    }

    public static boolean isInsertion(int code) {
        return (code & MUTATION_TYPE_MASK) == RAW_MUTATION_TYPE_INSERTION;
    }

    public static boolean isDeletion(int code) {
        return (code & MUTATION_TYPE_MASK) == RAW_MUTATION_TYPE_DELETION;
    }

    public static boolean isInDel(int code) {
        final int m = (code & MUTATION_TYPE_MASK);
        return m == RAW_MUTATION_TYPE_DELETION || m == RAW_MUTATION_TYPE_INSERTION;
    }

    public static int move(int mutation, int offset) {
        return mutation + (offset << POSITION_OFFSET);
    }

    public static String toString(Alphabet alphabet, int mutation) {
        switch (mutation & MUTATION_TYPE_MASK) {
            case RAW_MUTATION_TYPE_SUBSTITUTION:
                return "S" + (mutation >>> POSITION_OFFSET) + ":" +
                        alphabet.codeToSymbol((byte) ((mutation >> FROM_OFFSET) & LETTER_MASK)) +
                        "->" + alphabet.codeToSymbol((byte) (mutation & LETTER_MASK));
            case RAW_MUTATION_TYPE_DELETION:
                return "D" + (mutation >>> POSITION_OFFSET) + ":" +
                        alphabet.codeToSymbol((byte) ((mutation >> FROM_OFFSET) & LETTER_MASK));
            case RAW_MUTATION_TYPE_INSERTION:
                return "I" + (mutation >>> POSITION_OFFSET) + ":" + alphabet.codeToSymbol((byte) (mutation & LETTER_MASK));
        }
        return null;
    }

    /**
     * Encodes single mutation in compact human-readable string, that can be decoded by method {@link
     * MutationsUtil#decode(String, com.milaboratory.core.sequence.Alphabet)}.
     *
     * <p>The format is following:
     *
     * <ul> <li><b>Substitution</b>: starts with {@code S} then nucleotide in initial sequence encoded in one letter
     * (<b>from</b>) then <b>position</b> then resulting nucleotide (<b>to</b>) encoded in one letter. (Example: {@code
     * SA12T} = substitution from A to T at position 12).</li>
     *
     * <li><b>Deletion</b>: starts with {@code D} then nucleotide that was deleted encoded in one letter (<b>from</b>)
     * then <b>position</b>. (Example: {@code DG43} = G deleted at position 43).</li>
     *
     * <li><b>Insertion</b>: starts with {@code I} then <b>position</b> then inserted letter <b>to</b>. (Example:
     * {@code
     * I54C} = C inserted before letter at position 54).</li>
     *
     * </ul>
     *
     * @param mutation mutation to encode
     * @return mutation in a human-readable format
     */
    public static String encode(int mutation, Alphabet alphabet) {
        switch (mutation & MUTATION_TYPE_MASK) {
            case RAW_MUTATION_TYPE_SUBSTITUTION:
                return "S" + alphabet.codeToSymbol((byte) getFrom(mutation)) + Integer.toString(getPosition(mutation)) + alphabet.codeToSymbol((byte) getTo(mutation));
            case RAW_MUTATION_TYPE_DELETION:
                return "D" + alphabet.codeToSymbol((byte) getFrom(mutation)) + Integer.toString(getPosition(mutation));
            case RAW_MUTATION_TYPE_INSERTION:
                return "I" + Integer.toString(getPosition(mutation)) + alphabet.codeToSymbol((byte) getTo(mutation));
        }
        throw new IllegalArgumentException("Illegal mutation code.");
    }

    public static String encodeFixed(int mutation, Alphabet alphabet) {
        switch (mutation & MUTATION_TYPE_MASK) {
            case RAW_MUTATION_TYPE_SUBSTITUTION:
                return "S" + alphabet.codeToSymbol((byte) getFrom(mutation)) + Integer.toString(getPosition(mutation)) + alphabet.codeToSymbol((byte) getTo(mutation));
            case RAW_MUTATION_TYPE_DELETION:
                return "D" + alphabet.codeToSymbol((byte) getFrom(mutation)) + Integer.toString(getPosition(mutation)) + ".";
            case RAW_MUTATION_TYPE_INSERTION:
                return "I" + "." + Integer.toString(getPosition(mutation)) + alphabet.codeToSymbol((byte) getTo(mutation));
        }
        throw new IllegalArgumentException("Illegal mutation code.");
    }

    /**
     * Compares int mutations by their positions
     */
    public static IntArrayList.IntComparator POSITION_COMPARATOR = new IntArrayList.IntComparator() {
        @Override
        public int compare(int a, int b) {
            return Integer.compare((MUTATION_TYPE_MASK ^ a) & (Mutation.MUTATION_TYPE_MASK | Mutation.MUTATION_POSITION_MASK),
                    (MUTATION_TYPE_MASK ^ b) & (Mutation.MUTATION_TYPE_MASK | Mutation.MUTATION_POSITION_MASK));
        }
    };
}
