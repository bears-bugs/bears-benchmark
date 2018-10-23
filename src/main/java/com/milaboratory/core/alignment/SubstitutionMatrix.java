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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.milaboratory.core.sequence.Alphabet;

import java.io.Serializable;
import java.util.Arrays;

import static com.milaboratory.core.alignment.ScoringUtils.getSymmetricMatrix;

@JsonSerialize(using = ScoringMatrixIO.Serializer.class)
@JsonDeserialize(using = ScoringMatrixIO.Deserializer.class)
final class SubstitutionMatrix implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * If this matrix have:
     *
     * - two elements => {matchScore, mismatchScore}
     *
     * - alphabet.size()^2 => all elements are set
     */
    final int[] data;

    public SubstitutionMatrix(int... data) {
        this.data = data;
        if (data.length != 2) {
            int size = (int) Math.sqrt(data.length);
            if (data.length != size * size)
                throw new IllegalArgumentException("Wrong matrix size.");
        }
    }

    public int[] createSubstitutionMatrix(Alphabet<?> alphabet) {
        if (data.length == 2)
            return getSymmetricMatrix(data[0], data[1], alphabet);
        else {
            if (data.length != alphabet.size() * alphabet.size())
                throw new IllegalArgumentException();
            return data;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubstitutionMatrix that = (SubstitutionMatrix) o;

        return Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
