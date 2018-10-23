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

import com.milaboratory.util.HashFunctions;

import java.util.Arrays;

/**
 * Representation of a wildcard symbol.
 */
public final class Wildcard {
    /**
     * Symbol of wildcard
     */
    final char cSymbol;
    /**
     * Symbol of wildcard (byte)
     */
    final byte bSymbol;
    /**
     * Number of basic letters in matchingCodes array
     */
    final byte basicSize;
    /**
     * Set of codes in wildcard
     */
    final byte[] matchingCodes;
    /**
     * Code representing this wildcard (e.g. code == codes[0] for pure letters)
     */
    final byte code;
    /**
     * Wildcard bit basicMask is a long integer where:
     * ((basicMask >>> i) & 1) == 1, if wildcard matches i-th basic code
     */
    final long basicMask;
    /**
     * Wildcard bit mask is a long integer where:
     * ((mask >>> i) & 1) == 1, if wildcard matches i-th code
     */
    final long mask;

    /**
     * Pure letter constructor
     *
     * @param cSymbol uppercase symbol
     * @param code    code
     */
    Wildcard(char cSymbol, byte code) {
        this(cSymbol, code, 1, new byte[]{code});
    }

    /**
     * Wildcard constructor
     *
     * @param cSymbol            uppercase symbol of wildcard
     * @param code               code of wildcard
     * @param numberOfBasicCodes number of basic letters in matchingCodes array
     * @param matchingCodes      set of codes that this wildcards matches
     */
    Wildcard(char cSymbol, byte code, int numberOfBasicCodes, byte[] matchingCodes) {
        if (matchingCodes.length == 0 || Character.isLowerCase(cSymbol))
            throw new IllegalArgumentException();

        this.cSymbol = Character.toUpperCase(cSymbol);
        this.bSymbol = (byte) cSymbol;
        this.code = code;
        this.matchingCodes = matchingCodes.clone();
        this.basicSize = (byte) numberOfBasicCodes;

        // Sorting for binary search
        Arrays.sort(this.matchingCodes);

        // Assert for pure letters
        if (matchingCodes.length == 1 && code != matchingCodes[0])
            throw new IllegalArgumentException();

        // Creating basicMask representation
        long basicMask = 0, mask = 0;
        for (int i = 0; i < matchingCodes.length; i++) {
            byte c = matchingCodes[i];
            if (c >= 64)
                throw new IllegalArgumentException("Don't allow matching codes greater then 63.");
            if (i < numberOfBasicCodes)
                basicMask |= 1 << c;
            mask |= 1 << c;
        }
        this.basicMask = basicMask;
        this.mask = mask;
    }

    /**
     * Returns a wildcard letter.
     *
     * @return wildcard letter
     */
    public char getSymbol() {
        return cSymbol;
    }

    /**
     * Returns basicMask representation of the wildcard.
     *
     * Wildcard bit basicMask is a long integer where:
     * (basicMask >>> i) & 1 == 1, if wildcard includes i-th code
     *
     * @return basicMask representation of the wildcard
     */
    public long getBasicMask() {
        return basicMask;
    }

    /**
     * Returns a number of basic letters that matches this wildcard. For example, for nucleotide wildcard 'R' the
     * corresponding nucleotides are A and G, so the basicSize is 2.
     *
     * @return number of basic letters corresponding to this wildcard
     */
    public int basicSize() {
        return basicSize;
    }

    /**
     * Returns a number of letters (including wildcards) that matches this wildcard. For example, for nucleotide
     * wildcard 'R' the corresponding nucleotides and wildcards are A, G, N, R, S, W, K, M, B, D and V, so the size is
     * 11.
     *
     * @return number of basic letters corresponding to this wildcard
     */
    public int size() {
        return matchingCodes.length;
    }

    /**
     * Returns {@literal true} if and only if this wildcards has only one matching letter, so it represents definite
     * letter and formally it is not a wildcard.
     *
     * @return {@literal true} if and only if this wildcards has only one matching letter, so it represents definite
     * letter and formally it is not a wildcard
     */
    public boolean isBasic() {
        return basicSize == 1;
    }

    /**
     * Returns <i>i-th</i> code that this wildcard matches.
     *
     * @param i index of letter
     * @return <i>i-th</i> code that this wildcard matches
     */
    public byte getMatchingCode(int i) {
        return matchingCodes[i];
    }

    /**
     * Returns alphabet code.
     *
     * @return alphabet code
     */
    public byte getCode() {
        return code;
    }

    /**
     * Returns whether this wildcard contains specified element (nucleotide or amino acid etc.).
     *
     * @param code binary code of element
     * @return true if this wildcard contains specified element and false otherwise
     */
    public boolean matches(byte code) {
        return (mask & (1 << code)) != 0;
    }

    /**
     * Returns {@literal true} if set of symbols represented by this wildcard intersects with set of symbols
     * represented by {@code otherWildcard}.
     *
     * @param otherWildcard other wildcard
     * @return {@literal true} if set of symbols represented by this wildcard intersects with set of symbols represented
     * by {@code otherWildcard}
     */
    public boolean intersectsWith(Wildcard otherWildcard) {
        //byte[] a = this.matchingCodes, b = otherWildcard.matchingCodes;
        //int bPointer = 0, aPointer = 0;
        //while (aPointer < a.length && bPointer < b.length)
        //    if (a[aPointer] == b[bPointer]) {
        //        return true;
        //    } else if (a[aPointer] < b[bPointer])
        //        aPointer++;
        //    else if (a[aPointer] > b[bPointer]) {
        //        bPointer++;
        //    }
        //return false;
        return (this.basicMask & otherWildcard.basicMask) != 0;
    }

    /**
     * Returns uniformly distributed element (nucleotide or amino acid et cetera) corresponding to this wildcard.
     * Note: for same seeds the result will be the same.
     *
     * @param seed seed
     * @return uniformly distributed symbol corresponding to this wildcard
     */
    public byte getUniformlyDistributedBasicCode(long seed) {
        if (isBasic())
            return code;

        seed = HashFunctions.JenkinWang64shift(seed);
        if (seed < 0) seed = -seed;
        return matchingCodes[(int) (seed % basicSize)];
    }
}
