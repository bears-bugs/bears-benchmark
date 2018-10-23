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

import static com.milaboratory.core.mutations.generator.SubstitutionModels.getEmpiricalNucleotideSubstitutionModelWithNoise;

public class MutationModels {
    public static NucleotideMutationModel getEmpiricalNucleotideMutationModel() {
        return new GenericNucleotideMutationModel(
                SubstitutionModels.getEmpiricalNucleotideSubstitutionModel(), 0.00522, 0.00198);
    }

    public static NucleotideMutationModel getEmpiricalNucleotideMutationModelWithNoise(
            RandomGenerator rd, double minFactor, double maxFactor) {
        double l = maxFactor - minFactor;
        return new GenericNucleotideMutationModel(
                getEmpiricalNucleotideSubstitutionModelWithNoise(rd, minFactor, maxFactor),
                0.00522 * (minFactor + l * rd.nextDouble()),
                0.00198 * (minFactor + l * rd.nextDouble()),
                rd.nextLong());
    }
}
