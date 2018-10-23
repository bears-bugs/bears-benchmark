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

import org.apache.commons.math3.random.RandomGenerator;

import static com.milaboratory.core.sequence.NucleotideAlphabet.*;

public final class SubstitutionModels {
    private SubstitutionModels() {
    }

    public static SubstitutionModel getEmpiricalNucleotideSubstitutionModel() {
        SubstitutionModelBuilder builder = new SubstitutionModelBuilder(4);
        builder.setProbability(A, G, 0.00267);
        builder.setProbability(A, C, 0.00009);
        builder.setProbability(A, T, 0.0019);

        builder.setProbability(G, A, 0.00079);
        builder.setProbability(G, C, 0.0001);
        builder.setProbability(G, T, 0.00234);

        builder.setProbability(C, A, 0);
        builder.setProbability(C, G, 0.00017);
        builder.setProbability(C, T, 0.00162);

        builder.setProbability(T, A, 0.00006);
        builder.setProbability(T, G, 0.00019);
        builder.setProbability(T, C, 0.00119);

        return builder.build();
    }

    public static SubstitutionModel getEmpiricalNucleotideSubstitutionModelWithNoise(RandomGenerator rd, double minFactor, double maxFactor) {
        SubstitutionModelBuilder builder = new SubstitutionModelBuilder(4);
        double l = maxFactor - minFactor;
        builder.setProbability(A, G, 0.00267 * (minFactor + l * rd.nextDouble()));
        builder.setProbability(A, C, 0.00009 * (minFactor + l * rd.nextDouble()));
        builder.setProbability(A, T, 0.0019 * (minFactor + l * rd.nextDouble()));

        builder.setProbability(G, A, 0.00079 * (minFactor + l * rd.nextDouble()));
        builder.setProbability(G, C, 0.0001 * (minFactor + l * rd.nextDouble()));
        builder.setProbability(G, T, 0.00234 * (minFactor + l * rd.nextDouble()));

        builder.setProbability(C, A, 0.00001 * (minFactor + l * rd.nextDouble()));
        builder.setProbability(C, G, 0.00017 * (minFactor + l * rd.nextDouble()));
        builder.setProbability(C, T, 0.00162 * (minFactor + l * rd.nextDouble()));

        builder.setProbability(T, A, 0.00006 * (minFactor + l * rd.nextDouble()));
        builder.setProbability(T, G, 0.00019 * (minFactor + l * rd.nextDouble()));
        builder.setProbability(T, C, 0.00119 * (minFactor + l * rd.nextDouble()));

        return builder.build();
    }


    public static SubstitutionModel getUniformNucleotideSubstitutionModel(double substitutionProbability) {
        SubstitutionModelBuilder builder = new SubstitutionModelBuilder(4);
        int i, j;
        for (i = 0; i < 4; ++i)
            for (j = i + 1; j < 4; ++j) {
                builder.setProbability(i, j, substitutionProbability / 3.0);
                builder.setProbability(j, i, substitutionProbability / 3.0);
            }

        return builder.build();
    }
}
