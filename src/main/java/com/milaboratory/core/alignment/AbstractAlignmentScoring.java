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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;

import java.util.Arrays;

/**
 * AbstractAlignmentScoring - abstract scoring system class used for alignment procedure.
 *
 * @param <S> type of sequences to be aligned using scoring system
 */
@JsonIgnoreProperties({"minimalMatchScore", "maximalMatchScore", "minimalMismatchScore", "maximalMismatchScore"})
public class AbstractAlignmentScoring<S extends Sequence<S>> implements AlignmentScoring<S> {
    /**
     * Link to alphabet
     */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected final Alphabet<S> alphabet;

    /**
     * Stores information about how the object was created. {@link #subsMatrixActual} used for actual calculations
     */
    protected final SubstitutionMatrix subsMatrix;

    /**
     * Actual substitution matrix
     */
    @JsonIgnore
    protected final int[] subsMatrixActual;

    /**
     * Flag indicating whether substitution matrix has the same value on main diagonal or not
     */
    @JsonIgnore
    final boolean uniformBasicMatch;

    private final transient int minimalMatchScore, maximalMatchScore, minimalMismatchScore, maximalMismatchScore;

    /**
     * Abstract class constructor. Used in deserialization.
     *
     * <p>Initializes substitution matrix to {@code null} and uniformBasicMatch to {@code true}</p>
     *
     * @param alphabet alphabet to be used by scoring system
     */
    protected AbstractAlignmentScoring(Alphabet<S> alphabet, SubstitutionMatrix subsMatrix) {
        this.alphabet = alphabet;
        this.subsMatrix = subsMatrix;
        this.subsMatrixActual = subsMatrix.createSubstitutionMatrix(alphabet);

        // Setting uniformity of match score flag
        int val = getScore((byte) 0, (byte) 0);
        boolean e = true;
        for (byte i = (byte) (alphabet.basicSize() - 1); i > 0; --i)
            if (getScore(i, i) != val) {
                e = false;
                break;
            }
        this.uniformBasicMatch = e;

        int minimalMatchScore = getScore((byte) 0, (byte) 0), maximalMatchScore = minimalMatchScore,
                minimalMismatchScore = getScore((byte) 0, (byte) 1), maximalMismatchScore = minimalMismatchScore;
        for (byte c0 = 0; c0 < alphabet.size(); c0++) {
            for (byte c1 = 0; c1 < alphabet.size(); c1++) {
                int score = getScore(c0, c1);
                if (c0 == c1) {
                    minimalMatchScore = Math.min(minimalMatchScore, score);
                    maximalMatchScore = Math.max(maximalMatchScore, score);
                } else {
                    minimalMismatchScore = Math.min(minimalMismatchScore, score);
                    maximalMismatchScore = Math.max(maximalMismatchScore, score);
                }
            }
        }

        this.minimalMatchScore = minimalMatchScore;
        this.maximalMatchScore = maximalMatchScore;
        this.minimalMismatchScore = minimalMismatchScore;
        this.maximalMismatchScore = maximalMismatchScore;

    }

    /**
     * Returns score value for specified alphabet letter codes
     *
     * @param from code of letter which is to be replaced
     * @param to   code of letter which is replacing
     * @return score value
     */
    public int getScore(byte from, byte to) {
        return subsMatrixActual[from * alphabet.size() + to];
    }

    /**
     * Returns alphabet
     *
     * @return alphabet
     */
    public Alphabet<S> getAlphabet() {
        return alphabet;
    }

    /**
     * Returns @code{true} if @code{getScore(i, i)} returns the same score for all basic letters values of @code{i}.
     *
     * @return @code{true} if @code{getScore(i, i)} returns the same score for all basic letters values of @code{i}
     */
    public boolean uniformBasicMatchScore() {
        return uniformBasicMatch;
    }

    @Override
    public int getMinimalMatchScore() {
        return minimalMatchScore;
    }

    @Override
    public int getMaximalMatchScore() {
        return maximalMatchScore;
    }

    @Override
    public int getMinimalMismatchScore() {
        return minimalMismatchScore;
    }

    @Override
    public int getMaximalMismatchScore() {
        return maximalMismatchScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractAlignmentScoring that = (AbstractAlignmentScoring) o;

        if (getAlphabet() != ((AbstractAlignmentScoring) o).getAlphabet())
            return false;

        return Arrays.equals(subsMatrixActual, that.subsMatrixActual);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(subsMatrixActual) + 31 * getAlphabet().hashCode();
    }
}
