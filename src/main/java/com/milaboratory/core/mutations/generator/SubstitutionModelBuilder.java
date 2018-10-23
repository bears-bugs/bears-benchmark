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
package com.milaboratory.core.mutations.generator;

import com.milaboratory.core.sequence.Alphabet;

public final class SubstitutionModelBuilder {
    final int size;
    final double[] probabilities;

    public SubstitutionModelBuilder(Alphabet alphabet) {
        this(alphabet.basicSize());
    }

    public SubstitutionModelBuilder(int letters) {
        this.size = letters;
        this.probabilities = new double[letters * letters];
    }

    public void setProbability(int from, int to, double value) {
        this.probabilities[from * size + to] = value;
    }

    public SubstitutionModel build() {
        return new SubstitutionModel(probabilities);
    }

    public double getProbability(int fromLetter, int toLetter) {
        return this.probabilities[fromLetter * size + toLetter];
    }
}
