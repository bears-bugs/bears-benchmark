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
import com.milaboratory.primitivio.annotations.Serializable;
import com.milaboratory.util.HashFunctions;
import gnu.trove.impl.Constants;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TCharByteHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.io.ObjectStreamException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Interface for sequence letters alphabet (amino acid, nucleotide, etc.). {@code Alphabet} is responsible for
 * conversion between char representation of letters (e.g. 'A', 'T', 'G', 'C' in case of
 * {@link com.milaboratory.core.sequence.NucleotideAlphabet}) and their internal byte representation.
 *
 * <p>Alphabet also responsible for storing information about symbols wildcards.</p>
 *
 * <p>All alphabets letters grouped in two sets: <b>pure letters</b> and <b>wildcards</b>. Pure letters has codes less
 * than {@link #basicSize()}, wildcards has codes greater or equals to {@link #basicSize()}.</p>
 *
 * <p>Implementation note: all alphabets should be singletons.</p>
 *
 * @param <S> corresponding type of sequence
 * @author Dmitriy Bolotin (bolotin.dmitriy@gmail.com)
 * @author Stanislav Poslavsky (stvlpos@mail.ru)
 * @author Mikhail Shugay (mikhail.shugay@gmail.com)
 * @see com.milaboratory.core.sequence.Sequence
 * @see com.milaboratory.core.sequence.SequenceBuilder
 * @see com.milaboratory.core.sequence.NucleotideAlphabet
 * @see com.milaboratory.core.sequence.NucleotideSequence
 */
@JsonSerialize(using = Alphabets.Serializer.class)
@JsonDeserialize(using = Alphabets.Deserializer.class)
@Serializable(by = IO.AlphabetSerializer.class)
public abstract class Alphabet<S extends Sequence<S>> implements java.io.Serializable {
    /* ID */

    /**
     * Alphabet id
     */
    private final byte alphabetId;
    /**
     * Alphabet name
     */
    private final String alphabetName;

    private final int hashCode;

    /* Content */

    /**
     * Every code below this threshold represents definite letter, codes >= countOfPureLetters represents wildcards
     */
    private final int countOfBasicLetters;
    /**
     * Code to char upper case symbol mapping
     */
    private final char[] codeToSymbol;
    /**
     * Code to wildcard object mapping
     */
    private final Wildcard[] codeToWildcard;
    /**
     * Unmodifiable list of wildcards
     */
    private final List<Wildcard> wildcardsList;
    /**
     * Backward mapping for both cases
     */
    private final TCharByteHashMap symbolToCode;
    /**
     * Wildcard for any letter (e.g. N for nucleotides, X for amino acids)
     */
    private final Wildcard wildcardForAnyLetter;
    /**
     * Mapping between wildcard basicMask representation (bit representation) and wildcard object
     */
    private final TLongObjectMap<Wildcard> basicMaskToWildcard;
    /**
     * Singleton empty sequence
     */
    private volatile S empty;

    ///**
    // * 0b1111...11 = 2 ^ basicLettersCount - 1
    // */
    //final long basicLettersMask;

    Alphabet(String alphabetName, byte alphabetId, int countOfBasicLetters, Wildcard wildcardForAnyLetter,
             Wildcard... wildcards) {
        this.alphabetName = alphabetName;
        this.alphabetId = alphabetId;
        this.hashCode = HashFunctions.JenkinWang32shift(alphabetId);
        this.countOfBasicLetters = countOfBasicLetters;
        this.wildcardForAnyLetter = wildcardForAnyLetter;

        // Initialization of internal storage
        int size = wildcards.length;
        codeToSymbol = new char[size];
        // For error checking (see below)
        Arrays.fill(codeToSymbol, (char) 0xFFFF);
        codeToWildcard = new Wildcard[size];
        // -1 in constructor here is to simplify return of -1 for undefined symbols in symbolToCode
        symbolToCode = new TCharByteHashMap(Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR,
                (char) -1, (byte) -1);
        this.basicMaskToWildcard = new TLongObjectHashMap<>();

        // Filling internal maps/arrays
        for (Wildcard wildcard : wildcards) {
            if (wildcard.isBasic() && wildcard.getCode() >= countOfBasicLetters)
                throw new IllegalArgumentException("Definite letter outside countOfPureLetters range.");
            if (codeToSymbol[wildcard.getCode()] != 0xFFFF)
                throw new IllegalArgumentException("Duplicate code.");
            codeToSymbol[wildcard.getCode()] = wildcard.getSymbol();
            codeToWildcard[wildcard.getCode()] = wildcard;
            symbolToCode.put(wildcard.getSymbol(), wildcard.getCode());
            symbolToCode.put(Character.toLowerCase(wildcard.getSymbol()), wildcard.getCode());
            basicMaskToWildcard.put(wildcard.getBasicMask(), wildcard);
        }

        // Error checking
        for (int i = 0; i < codeToSymbol.length; i++)
            if (codeToSymbol[i] == 0xFFFF)
                throw new IllegalArgumentException("Symbol for code " + i + " is not set.");

        // To be returned by corresponding getter
        this.wildcardsList = Collections.unmodifiableList(Arrays.asList(codeToWildcard));
        //this.basicLettersMask = ~(0xFFFFFFFFFFFFFFFFL << countOfBasicLetters);
    }

    /**
     * Gets number of letters in this alphabet including wildcard letters
     *
     * @return number of letters in this alphabet including wildcard letters
     */
    public final int size() {
        return codeToSymbol.length;
    }

    /**
     * Gets number of letters in this alphabet without wildcard letters
     *
     * @return number of letters in this alphabet without wildcard letters
     */
    public final int basicSize() {
        return countOfBasicLetters;
    }

    /**
     * Returns {@literal true} if this code represents wildcard symbol
     *
     * @param code code of letter
     * @return {@literal true} if this code represents wildcard symbol
     */
    public final boolean isWildcard(byte code) {
        return code >= basicSize();
    }

    /* Wildcard methods */

    /**
     * Returns wildcard defined by specified code (letter).
     *
     * @param code code
     * @return wildcard defined by specified code (letter)
     */
    public final Wildcard codeToWildcard(byte code) {
        return codeToWildcard[code];
    }

    /**
     * Returns a wildcard object for specified letter.
     *
     * @param symbol symbol
     * @return wildcard object for specified letter
     */
    public final Wildcard symbolToWildcard(char symbol) {
        return codeToWildcard[symbolToCode.get(symbol)];
    }

    /**
     * Returns a collection of all wildcards defined for this.
     *
     * @return a collection of all wildcards defined for this.
     */
    public final List<Wildcard> getAllWildcards() {
        return wildcardsList;
    }

    /**
     * Returns wildcard for any letter (e.g. N for nucleotides, X for amino acids).
     *
     * @return wildcard for any letter (e.g. N for nucleotides, X for amino acids)
     */
    public final Wildcard getWildcardForAnyLetter() {
        return wildcardForAnyLetter;
    }

    /**
     * Converts wildcard basicMask to Wildcard object.
     *
     * @param basicMask bit represenatation of wildcard
     * @return wildcard object; {@literal null} if there is no such wildcard in the alphabet
     */
    public Wildcard maskToWildcard(long basicMask) {
        return basicMaskToWildcard.get(basicMask);
    }

    /* Conversion */

    /**
     * Gets a char symbol for an alphabet code of the letter
     *
     * @param code alphabet code of segment
     * @return char symbol for an alphabet code of the letter
     */
    public final char codeToSymbol(byte code) {
        return codeToSymbol[code];
    }

    /**
     * Gets the binary code representing given symbol (case insensitive) or -1 if there
     * is no such symbol in this alphabet
     *
     * @param symbol symbol to convert
     * @return binary code of the symbol (case insensitive) or -1 if there is no such symbol in the alphabet
     */
    public byte symbolToCode(char symbol) {
        return symbolToCode.get(symbol);
    }

    /**
     * Gets the binary code corresponding to given symbol (case insensitive) or throws {@link IllegalArgumentException}
     * if there is no such symbol in this alphabet
     *
     * @param symbol symbol to convert
     * @return binary code of the symbol (case insensitive)
     * @throws IllegalArgumentException if there is no such symbol in the alphabet
     */
    public final byte symbolToCodeWithException(char symbol) {
        byte b = symbolToCode(symbol);
        if (b == -1)
            throw new IllegalArgumentException("Unknown letter \'" + symbol + "\'");
        return b;
    }

    /**
     * Returns a sequence builder for corresponding sequence type.
     *
     * @return sequence builder for corresponding sequence type
     */
    public abstract SequenceBuilder<S> createBuilder();

    /**
     * Returns empty sequence singleton
     *
     * @return empty sequence singleton
     */
    public S getEmptySequence() {
        if (empty == null)
            synchronized (this) {
                if (empty == null)
                    empty = createBuilder().createAndDestroy();
            }
        return empty;
    }

    /**
     * Returns the human readable name of this alphabet.
     *
     * <p>This name can be then used to obtain the instance of this alphabet using {@link
     * com.milaboratory.core.sequence.Alphabets#getByName(String)} method if it is registered (see {@link
     * com.milaboratory.core.sequence.Alphabets#register(Alphabet)}).</p>
     */
    public final String getAlphabetName() {
        return alphabetName;
    }

    /**
     * Returns byte id of this alphabet
     *
     * <p>This name can be then used to obtain the instance of this alphabet using {@link
     * com.milaboratory.core.sequence.Alphabets#getById(byte)} method if it is registered (see {@link
     * com.milaboratory.core.sequence.Alphabets#register(Alphabet)}).</p>
     */
    public final byte getId() {
        return alphabetId;
    }

    /**
     * Parses string representation of sequence.
     *
     * @param string string representation of sequence
     * @return sequence
     */
    public final S parse(String string) {
        SequenceBuilder<S> builder = createBuilder().ensureCapacity(string.length());
        for (int i = 0; i < string.length(); ++i) {
            byte code = symbolToCode(string.charAt(i));
            if (code == -1)
                throw new IllegalArgumentException("Letter \'" + string.charAt(i) + "\' is not defined in \'" + toString() + "\'.");
            builder.append(code);
        }
        return builder.createAndDestroy();
    }

    /**
     * Convert alphabet to a readable string.
     *
     * @return alphabet as a readable string
     */
    @Override
    public final String toString() {
        return "Alphabet{" + alphabetName + '}';
    }

    /**
     * Returns "address in memory" (hash code as specified by {@link Object#hashCode()}. All Alphabet implementations
     * must be singletons.
     */
    @Override
    public final int hashCode() {
        return hashCode;
    }

    /**
     * Checks that in is the same object (this points to the same address as {@code obj})
     * All Alphabet implementations must be singletons.
     *
     * @param obj alphabet to check for equality with
     * @return {@literal true} if alphabets are the same
     */
    @Override
    public final boolean equals(Object obj) {
        return obj == this;
    }

    /* Internal methods for Java Serialization */

    protected Object writeReplace() throws ObjectStreamException {
        return new AlphabetSerialization(alphabetId);
    }

    protected static class AlphabetSerialization implements java.io.Serializable {
        final byte id;

        public AlphabetSerialization() {
            this.id = 0;
        }

        public AlphabetSerialization(byte id) {
            this.id = id;
        }

        private Object readResolve()
                throws ObjectStreamException {
            return Alphabets.getById(id);
        }
    }
}
