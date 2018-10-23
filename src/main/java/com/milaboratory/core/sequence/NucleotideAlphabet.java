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

/**
 * An alphabet for nucleotide sequences. This alphabet defines the following mapping:
 *
 * <p>0 - 'A', 1 - 'G', 2 - 'C', 3 - 'T'</p>
 *
 * <p> This class also defines wildcards as specified by IUPAC: 'R' for 'A' or 'G', 'Y' for 'C' or 'T' etc. </p>
 *
 * @author Bolotin Dmitriy (bolotin.dmitriy@gmail.com)
 * @author Shugay Mikhail (mikhail.shugay@gmail.com)
 * @see com.milaboratory.core.sequence.Alphabet
 * @see com.milaboratory.core.sequence.NucleotideSequence
 */
public final class NucleotideAlphabet extends AbstractArrayAlphabet<NucleotideSequence> {
    /**
     * Adenine byte representation
     */
    public static final byte A = 0;
    /**
     * Guanine byte representation
     */
    public static final byte G = 1;
    /**
     * Cytosine byte representation
     */
    public static final byte C = 2;
    /**
     * Thymine byte representation
     */
    public static final byte T = 3;

    /* Codes for wildcards */
    /**
     * any Nucleotide
     */
    public static final byte N = 4;

    /* Two-letter wildcard */
    /**
     * puRine
     */
    public static final byte R = 5;
    /**
     * pYrimidine
     */
    public static final byte Y = 6;
    /**
     * Strong
     */
    public static final byte S = 7;
    /**
     * Weak
     */
    public static final byte W = 8;
    /**
     * Keto
     */
    public static final byte K = 9;
    /**
     * aMino
     */
    public static final byte M = 10;

    /* Three-letter wildcard */
    /**
     * not A (B comes after A)
     */
    public static final byte B = 11;
    /**
     * not C (D comes after C)
     */
    public static final byte D = 12;
    /**
     * not G (H comes after G)
     */
    public static final byte H = 13;
    /**
     * not T (V comes after T and U)
     */
    public static final byte V = 14;

    /* Wildcards */

    /* Basic wildcards */
    /**
     * Adenine byte representation
     */
    public static final Wildcard A_WILDCARD = new Wildcard('A', A, 1, new byte[]{A, N, R, W, M, D, H, V});
    /**
     * Guanine byte representation
     */
    public static final Wildcard G_WILDCARD = new Wildcard('G', G, 1, new byte[]{G, N, R, S, K, B, D, V});
    /**
     * Cytosine byte representation
     */
    public static final Wildcard C_WILDCARD = new Wildcard('C', C, 1, new byte[]{C, N, Y, S, M, B, H, V});
    /**
     * Thymine byte representation
     */
    public static final Wildcard T_WILDCARD = new Wildcard('T', T, 1, new byte[]{T, N, Y, W, K, B, D, H});

    /* N wildcard */
    /**
     * any Nucleotide
     */
    public static final Wildcard N_WILDCARD = new Wildcard('N', N, 4, new byte[]{A, G, C, T, N, R, Y, S, W, K, M, B, D, H, V});

    /* Two-letter wildcards */
    /**
     * puRine
     */
    public static final Wildcard R_WILDCARD = new Wildcard('R', R, 2, new byte[]{A, G, N, R, S, W, K, M, B, D, H, V});
    /**
     * pYrimidine
     */
    public static final Wildcard Y_WILDCARD = new Wildcard('Y', Y, 2, new byte[]{C, T, N, Y, S, W, K, M, B, D, H, V});
    /**
     * Strong
     */
    public static final Wildcard S_WILDCARD = new Wildcard('S', S, 2, new byte[]{G, C, N, R, Y, S, K, M, B, D, H, V});
    /**
     * Weak
     */
    public static final Wildcard W_WILDCARD = new Wildcard('W', W, 2, new byte[]{A, T, N, R, Y, W, K, M, B, D, H, V});
    /**
     * Keto
     */
    public static final Wildcard K_WILDCARD = new Wildcard('K', K, 2, new byte[]{G, T, N, R, Y, S, W, K, B, D, H, V});
    /**
     * aMino
     */
    public static final Wildcard M_WILDCARD = new Wildcard('M', M, 2, new byte[]{A, C, N, R, Y, S, W, M, B, D, H, V});

    /* Three-letter wildcards */
    /**
     * not A (B comes after A)
     */
    public static final Wildcard B_WILDCARD = new Wildcard('B', B, 3, new byte[]{G, C, T, N, R, Y, S, W, K, M, B, D, H, V});
    /**
     * not C (D comes after C)
     */
    public static final Wildcard D_WILDCARD = new Wildcard('D', D, 3, new byte[]{A, G, T, N, R, Y, S, W, K, M, B, D, H, V});
    /**
     * not G (H comes after G)
     */
    public static final Wildcard H_WILDCARD = new Wildcard('H', H, 3, new byte[]{A, C, T, N, R, Y, S, W, K, M, B, D, H, V});
    /**
     * not T (V comes after T and U)
     */
    public static final Wildcard V_WILDCARD = new Wildcard('V', V, 3, new byte[]{A, G, C, N, R, Y, S, W, K, M, B, D, H, V});

    /**
     * All wildcards array. Each wildcard has index equals to its code.
     */
    private static final Wildcard[] WILDCARDS;
    /**
     * COMPLEMENT_CODE[c] = complement code of c
     */
    private static final byte[] COMPLEMENT_CODE;
    /**
     * COMPLEMENT_CODE[c] = complement wildcard for wildcard with code c
     */
    private static final Wildcard[] COMPLEMENT_WILDCARD;

    /**
     * Singleton instance.
     */
    final static NucleotideAlphabet INSTANCE = new NucleotideAlphabet();

    private NucleotideAlphabet() {
        super("nucleotide", (byte) 1, 4,
                // Any letter
                N_WILDCARD,
                // Content
                A_WILDCARD, T_WILDCARD, G_WILDCARD, C_WILDCARD,
                N_WILDCARD,
                R_WILDCARD, Y_WILDCARD, S_WILDCARD, W_WILDCARD, K_WILDCARD, M_WILDCARD,
                B_WILDCARD, D_WILDCARD, H_WILDCARD, V_WILDCARD);
    }

    /**
     * Returns a complementary nucleotide code.
     *
     * @param code byte code of nucleotide
     * @return complementary nucleotide code
     */
    public static byte complementCode(byte code) {
        return COMPLEMENT_CODE[code];
    }

    /**
     * Returns a complementary nucleotide code.
     *
     * @param wildcard wildcard to convert to complementary code
     * @return complementary nucleotide code
     */
    public static byte complementCode(Wildcard wildcard) {
        return COMPLEMENT_CODE[wildcard.code];
    }

    /**
     * Returns a complementary wildcard object
     *
     * @param code byte code of nucleotide
     * @return complementary wildcard object
     */
    public static Wildcard complementWildcard(byte code) {
        return COMPLEMENT_WILDCARD[code];
    }

    /**
     * Returns a complementary wildcard object
     *
     * @param wildcard wildcard to convert to complementary
     * @return complementary wildcard object
     */
    public static Wildcard complementWildcard(Wildcard wildcard) {
        return COMPLEMENT_WILDCARD[wildcard.code];
    }

    /**
     * Returns UTF-8 character corresponding to specified byte-code.
     *
     * @param code byte-code of nucleotide
     * @return UTF-8 character corresponding to specified byte-code
     */
    public static byte symbolByteFromCode(byte code) {
        //TODO optimize
        return (byte) INSTANCE.codeToSymbol(code);
    }

    public static byte byteSymbolToCode(byte symbol) {
        //TODO optimize
        return INSTANCE.symbolToCode((char) symbol);
    }

    @Override
    NucleotideSequence createUnsafe(byte[] array) {
        return new NucleotideSequence(array, true);
    }

    /**
     * Only for basic letters.
     */
    private static byte getComplement1(byte nucleotide) {
        return (byte) (nucleotide ^ 3);
    }

    private static long getComplementMask(Wildcard wildcard) {
        long basic = 0;
        for (int i = 0; i < wildcard.basicSize(); i++)
            basic |= 1 << getComplement1(wildcard.matchingCodes[i]);
        return basic;
    }

    static {
        WILDCARDS = INSTANCE.getAllWildcards().toArray(new Wildcard[INSTANCE.size()]);
        COMPLEMENT_CODE = new byte[WILDCARDS.length];
        COMPLEMENT_WILDCARD = new Wildcard[WILDCARDS.length];
        for (int i = 0; i < WILDCARDS.length; i++) {
            Wildcard complementWildcard = INSTANCE.maskToWildcard(getComplementMask(WILDCARDS[i]));
            COMPLEMENT_WILDCARD[i] = complementWildcard;
            COMPLEMENT_CODE[i] = complementWildcard.code;
        }
    }
}
