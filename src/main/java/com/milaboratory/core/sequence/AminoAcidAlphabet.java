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
 * Amino acid alphabet with additional symbols.
 *
 * <p>"_" represents incomplete codon (residual 1 or 2 nucleotides remaining after translation of all full codons)</p>
 *
 * @author Bolotin Dmitriy (bolotin.dmitriy@gmail.com)
 * @author Shugay Mikhail (mikhail.shugay@gmail.com)
 * @see com.milaboratory.core.sequence.Alphabet
 * @see com.milaboratory.core.sequence.AminoAcidSequence
 * @see com.milaboratory.core.sequence.Sequence
 */
public final class AminoAcidAlphabet extends AbstractArrayAlphabet<AminoAcidSequence> {
    /**
     * Stop codon byte representation
     */
    public static final byte STOP = 0;
    /**
     * Alanine byte representation
     */
    public static final byte A = 1;
    /**
     * Cysteine byte representation
     */
    public static final byte C = 2;
    /**
     * Aspartic Acid byte representation
     */
    public static final byte D = 3;
    /**
     * Glutamic Acid representation
     */
    public static final byte E = 4;
    /**
     * Phenylalanine byte representation
     */
    public static final byte F = 5;
    /**
     * Glycine byte representation
     */
    public static final byte G = 6;
    /**
     * Histidine byte representation
     */
    public static final byte H = 7;
    /**
     * Isoleucine byte representation
     */
    public static final byte I = 8;
    /**
     * Lysine byte representation
     */
    public static final byte K = 9;
    /**
     * Leucine byte representation
     */
    public static final byte L = 10;
    /**
     * Methionine byte representation
     */
    public static final byte M = 11;
    /**
     * Asparagine byte representation
     */
    public static final byte N = 12;
    /**
     * Proline byte representation
     */
    public static final byte P = 13;
    /**
     * Glutamine byte representation
     */
    public static final byte Q = 14;
    /**
     * Arginine byte representation
     */
    public static final byte R = 15;
    /**
     * Serine byte representation
     */
    public static final byte S = 16;
    /**
     * Threonine byte representation
     */
    public static final byte T = 17;
    /**
     * Valine byte representation
     */
    public static final byte V = 18;
    /**
     * Tryptophan byte representation
     */
    public static final byte W = 19;
    /**
     * Tyrosine byte representation
     */
    public static final byte Y = 20;
    /**
     * Incomplete codon byte representation
     */
    public static final byte INCOMPLETE_CODON = 21;

    /* Wildcard symbols */

    /**
     * Any amino acid byte representation
     */
    public static final byte X = 22;
    /**
     * Aspartic acid or Asparagine (N or D) byte representation
     */
    public static final byte B = 23;
    /**
     * Leucine or Isoleucine (I or L) byte representation
     */
    public static final byte J = 24;
    /**
     * Glutamine or Glutamic acid (E or Q) byte representation
     */
    public static final byte Z = 25;

    /* Wildcards */

    /**
     * Stop codon wildcard
     */
    public static final Wildcard STOP_WILDCARD = new Wildcard('*', STOP, 1, new byte[]{STOP});
    /**
     * Alanine wildcard
     */
    public static final Wildcard A_WILDCARD = new Wildcard('A', A, 1,new byte[]{A, X});
    /**
     * Cysteine wildcard
     */
    public static final Wildcard C_WILDCARD = new Wildcard('C', C, 1,new byte[]{C, X});
    /**
     * Aspartic Acid wildcard
     */
    public static final Wildcard D_WILDCARD = new Wildcard('D', D, 1,new byte[]{D, X, B});
    /**
     * Glutamic Acid wildcard
     */
    public static final Wildcard E_WILDCARD = new Wildcard('E', E, 1,new byte[]{E, X, Z});
    /**
     * Phenylalanine wildcard
     */
    public static final Wildcard F_WILDCARD = new Wildcard('F', F, 1,new byte[]{F, X});
    /**
     * Glycine wildcard
     */
    public static final Wildcard G_WILDCARD = new Wildcard('G', G, 1,new byte[]{G, X});
    /**
     * Histidine wildcard
     */
    public static final Wildcard H_WILDCARD = new Wildcard('H', H, 1,new byte[]{H, X});
    /**
     * Isoleucine wildcard
     */
    public static final Wildcard I_WILDCARD = new Wildcard('I', I, 1,new byte[]{I, X, J});
    /**
     * Lysine wildcard
     */
    public static final Wildcard K_WILDCARD = new Wildcard('K', K, 1,new byte[]{K, X});
    /**
     * Leucine wildcard
     */
    public static final Wildcard L_WILDCARD = new Wildcard('L', L, 1,new byte[]{L, X, J});
    /**
     * Methionine wildcard
     */
    public static final Wildcard M_WILDCARD = new Wildcard('M', M, 1,new byte[]{M, X});
    /**
     * Asparagine wildcard
     */
    public static final Wildcard N_WILDCARD = new Wildcard('N', N, 1,new byte[]{N, X, B});
    /**
     * Proline wildcard
     */
    public static final Wildcard P_WILDCARD = new Wildcard('P', P, 1,new byte[]{P, X});
    /**
     * Glutamine wildcard
     */
    public static final Wildcard Q_WILDCARD = new Wildcard('Q', Q, 1,new byte[]{Q, X, Z});
    /**
     * Arginine wildcard
     */
    public static final Wildcard R_WILDCARD = new Wildcard('R', R, 1,new byte[]{R, X});
    /**
     * Serine wildcard
     */
    public static final Wildcard S_WILDCARD = new Wildcard('S', S, 1,new byte[]{S, X});
    /**
     * Threonine wildcard
     */
    public static final Wildcard T_WILDCARD = new Wildcard('T', T, 1,new byte[]{T, X});
    /**
     * Valine wildcard
     */
    public static final Wildcard V_WILDCARD = new Wildcard('V', V, 1,new byte[]{V, X});
    /**
     * Tryptophan wildcard
     */
    public static final Wildcard W_WILDCARD = new Wildcard('W', W, 1,new byte[]{W, X});
    /**
     * Tyrosine wildcard
     */
    public static final Wildcard Y_WILDCARD = new Wildcard('Y', Y, 1,new byte[]{Y, X});
    /**
     * Incomplete codon wildcard
     */
    public static final Wildcard INCOMPLETE_CODON_WILDCARD = new Wildcard('_', INCOMPLETE_CODON, 1,new byte[]{INCOMPLETE_CODON});


    /**
     * Any amino acid wildcard
     */
    public static final Wildcard X_WILDCARD = new Wildcard('X', X, 20, new byte[]{A, C, D, E, F, G, H, I, K, L, M, N, P, Q, R, S, T, V, W, Y, X, B, J, Z});
    /**
     * Aspartic acid or Asparagine (N or D) wildcard
     */
    public static final Wildcard B_WILDCARD = new Wildcard('B', B, 2, new byte[]{D, N, X, B});
    /**
     * Leucine or Isoleucine (I or L) wildcard
     */
    public static final Wildcard J_WILDCARD = new Wildcard('J', J, 2, new byte[]{I, L, X, J});
    /**
     * Glutamine or Glutamic acid (E or Q) wildcard
     */
    public static final Wildcard Z_WILDCARD = new Wildcard('Z', Z, 2, new byte[]{E, Q, X, Z});

    /**
     * Instance of amino acid alphabet.
     */
    static final AminoAcidAlphabet INSTANCE = new AminoAcidAlphabet();

    private AminoAcidAlphabet() {
        super("aminoacid", (byte) 2, 22,
                // Any letter
                X_WILDCARD,
                // Content
                STOP_WILDCARD, A_WILDCARD, C_WILDCARD, D_WILDCARD, E_WILDCARD, F_WILDCARD, G_WILDCARD, H_WILDCARD,
                I_WILDCARD, K_WILDCARD, L_WILDCARD, M_WILDCARD, N_WILDCARD, P_WILDCARD, Q_WILDCARD, R_WILDCARD,
                S_WILDCARD, T_WILDCARD, V_WILDCARD, W_WILDCARD, Y_WILDCARD,
                INCOMPLETE_CODON_WILDCARD,
                // Wildcards
                X_WILDCARD,
                B_WILDCARD, J_WILDCARD, Z_WILDCARD);
    }

    @Override
    public byte symbolToCode(char symbol) {
        // Special case for backward compatibility
        if (symbol == '~')
            return INCOMPLETE_CODON;

        // Normal conversion (-1 will be returned for unknown symbols, see symbolToWildcard constructor parameters)
        return super.symbolToCode(symbol);
    }

    @Override
    AminoAcidSequence createUnsafe(byte[] array) {
        return new AminoAcidSequence(array, true);
    }
}
