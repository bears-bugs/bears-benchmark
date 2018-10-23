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
package com.milaboratory.core.alignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.milaboratory.core.sequence.*;

import java.io.ObjectStreamException;

/**
 * LinearGapAlignmentScoring - scoring system which uses penalty for gap
 */
public final class LinearGapAlignmentScoring<S extends Sequence<S>> extends AbstractAlignmentScoring<S>
        implements java.io.Serializable {
    /**
     * Penalty for gap, must be < 0
     */
    private final int gapPenalty;

    /**
     * Creates new LinearGapAlignmentScoring. Required for deserialization defaults.
     */
    @SuppressWarnings("unchecked")
    private LinearGapAlignmentScoring() {
        super((Alphabet) NucleotideSequence.ALPHABET, new SubstitutionMatrix(Integer.MIN_VALUE, Integer.MIN_VALUE));
        gapPenalty = Integer.MIN_VALUE;
    }

    @JsonCreator
    public LinearGapAlignmentScoring(
            @JsonProperty("alphabet") Alphabet<S> alphabet,
            @JsonProperty("subsMatrix") SubstitutionMatrix subsMatrix,
            @JsonProperty("gapPenalty") int gapPenalty) {
        super(alphabet, subsMatrix);
        if (gapPenalty >= 0)
            throw new IllegalArgumentException();
        this.gapPenalty = gapPenalty;
    }

    /**
     * Creates new LinearGapAlignmentScoring
     *
     * @param alphabet   alphabet to be used
     * @param subsMatrix substitution matrix to be used
     * @param gapPenalty penalty for gap, must be < 0
     */
    public LinearGapAlignmentScoring(
            Alphabet<S> alphabet,
            int[] subsMatrix,
            int gapPenalty) {
        super(alphabet, new SubstitutionMatrix(subsMatrix));
        if (gapPenalty >= 0)
            throw new IllegalArgumentException();
        this.gapPenalty = gapPenalty;
    }

    /**
     * Returns scoring with uniform match and mismatch scores
     *
     * @param alphabet alphabet
     * @param match    match score > 0
     * @param mismatch mismatch score < 0
     * @param gap      gap penalty < 0
     * @return scoring with uniform match and mismatch scores
     */
    public LinearGapAlignmentScoring(Alphabet<S> alphabet,
                                     int match, int mismatch,
                                     int gap) {
        this(alphabet, new SubstitutionMatrix(match, mismatch), gap);
    }

    /**
     * Returns penalty score for gap
     *
     * @return penalty score for gap
     */
    public int getGapPenalty() {
        return gapPenalty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LinearGapAlignmentScoring that = (LinearGapAlignmentScoring) o;

        return gapPenalty == that.gapPenalty;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + gapPenalty;
        return result;
    }

    /* Nucleotide */

    /**
     * Returns standard Nucleotide BLAST scoring with {@code #gapPenalty=5}
     *
     * @return standard Nucleotide BLAST Scoring
     */
    public static LinearGapAlignmentScoring<NucleotideSequence> getNucleotideBLASTScoring() {
        return getNucleotideBLASTScoring(-5);
    }

    /**
     * Returns standard Nucleotide BLAST scoring
     *
     * @param gapPenalty penalty for gap value
     * @return standard Nucleotide BLAST scoring
     */
    public static LinearGapAlignmentScoring<NucleotideSequence> getNucleotideBLASTScoring(int gapPenalty) {
        return new LinearGapAlignmentScoring<>(NucleotideSequence.ALPHABET, 5, -4, gapPenalty);
    }

    /* Amino acid */

    /**
     * Returns standard amino acid BLAST scoring with {@code #gapPenalty=5}
     *
     * @param matrix BLAST substitution matrix
     * @return standard amino acid BLAST scoring
     */
    public static LinearGapAlignmentScoring<AminoAcidSequence> getAminoAcidBLASTScoring(BLASTMatrix matrix) {
        return getAminoAcidBLASTScoring(matrix, -5);
    }

    /**
     * Returns standard amino acid BLAST scoring
     *
     * @param matrix     BLAST substitution matrix
     * @param gapPenalty penalty for gap, must be < 0
     * @return standard amino acid BLAST scoring
     */
    public static LinearGapAlignmentScoring<AminoAcidSequence> getAminoAcidBLASTScoring(BLASTMatrix matrix, int gapPenalty) {
        return new LinearGapAlignmentScoring<>(AminoAcidSequence.ALPHABET,
                matrix.getMatrix(),
                gapPenalty);
    }

    /* Internal methods for Java Serialization */

    protected Object writeReplace() throws ObjectStreamException {
        return new SerializationObject(alphabet, subsMatrix, gapPenalty);
    }

    protected static class SerializationObject implements java.io.Serializable {
        final Alphabet alphabet;
        final SubstitutionMatrix matrix;
        final int gapPenalty;

        public SerializationObject() {
            this(null, null, 0);
        }

        public SerializationObject(Alphabet alphabet, SubstitutionMatrix matrix, int gapPenalty) {
            this.alphabet = alphabet;
            this.matrix = matrix;
            this.gapPenalty = gapPenalty;
        }

        @SuppressWarnings("unchecked")
        private Object readResolve()
                throws ObjectStreamException {
            return new LinearGapAlignmentScoring(alphabet, matrix, gapPenalty);
        }
    }
}