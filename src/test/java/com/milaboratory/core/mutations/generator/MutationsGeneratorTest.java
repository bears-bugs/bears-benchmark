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

import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.sequence.NucleotideSequence;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Test;

import static com.milaboratory.core.mutations.Mutation.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class MutationsGeneratorTest {
    @Test
    public void testGen() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("TTTTTTTTTTTTTTTTTTTT");
        GenericNucleotideMutationModel model = new GenericNucleotideMutationModel(SubstitutionModels.getUniformNucleotideSubstitutionModel(.05), .1, .1);
        model.reseed(1231432L);
        SummaryStatistics[] stats = new SummaryStatistics[3];
        for (int i = 0; i < 3; ++i)
            stats[i] = new SummaryStatistics();

        for (int k = 0; k < 100; ++k) {
            int ins = 0, del = 0, sub = 0;
            for (int i = 0; i < 10000; ++i) {
                Mutations<NucleotideSequence> muts = MutationsGenerator.generateMutations(seq1, model);
                //printAlignment(seq1, muts);
                //System.out.println();

                checkMutations(muts);

                for (int m : muts.getRAWMutations())
                    switch (getRawTypeCode(m)) {
                        case RAW_MUTATION_TYPE_SUBSTITUTION:
                            ++sub;
                            break;
                        case RAW_MUTATION_TYPE_DELETION:
                            ++del;
                            break;
                        case RAW_MUTATION_TYPE_INSERTION:
                            ++ins;
                            break;
                    }
            }

            stats[0].addValue(sub);
            stats[1].addValue(del);
            stats[2].addValue(ins);
        }

        assertEquals(11000.0, stats[0].getMean(), 50.0);
        assertEquals(20000.0, stats[1].getMean(), 50.0);
        assertEquals(21000.0, stats[2].getMean(), 50.0);
    }

    public static void checkMutations(Mutations mutations) {
        assertEquals("Encode/Decode", mutations, Mutations.decode(mutations.encode(), NucleotideSequence.ALPHABET));
    }
}