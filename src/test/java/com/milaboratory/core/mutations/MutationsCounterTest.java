/*
 * Copyright 2016 MiLaboratory.com
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
package com.milaboratory.core.mutations;

import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.test.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import static com.milaboratory.core.sequence.NucleotideSequence.ALPHABET;

/**
 * Created by dbolotin on 01/02/16.
 */
public class MutationsCounterTest {
    @Test
    public void test1() throws Exception {
        Mutations<NucleotideSequence> original1 = Mutations.decodeNuc("SA1T ST12C DG13 I16T I16G SA20G");
        Mutations<NucleotideSequence> original2 = Mutations.decodeNuc("SA1T       DG13 I16T I16G SA20G");
        Mutations<NucleotideSequence> original3 = Mutations.decodeNuc("SA1T ST12C DG13 I16T      SA20G");
        Mutations<NucleotideSequence> _expected = Mutations.decodeNuc("SA1T       DG13           SA20G");

        MutationsCounter counter = new MutationsCounter();
        counter.adjust(original1, 1);
        counter.adjust(original2, 1);
        counter.adjust(original3, 1);


        Mutations<NucleotideSequence> build = counter.build(ALPHABET, new MutationsCounter.Filter() {
            @Override
            public boolean accept(long count, int position, int mutation, int[] mutations) {
                return count == 3;
            }
        });

        Assert.assertEquals(_expected, build);
    }

    @Test
    public void test2() throws Exception {
        for (int c = 0; c < TestUtil.its(100, 1000); c++) {
            Mutations<NucleotideSequence> mutations = MutationsGenerator.generateMutations(TestUtil.randomSequence(ALPHABET, 10, 100), MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(500));
            MutationsCounter counter = new MutationsCounter();
            counter.adjust(mutations, 1);
            Mutations<NucleotideSequence> build = counter.build(ALPHABET, new MutationsCounter.Filter() {
                @Override
                public boolean accept(long count, int position, int mutation, int[] mutations) {
                    return true;
                }
            });
            Assert.assertEquals(mutations, build);
        }
    }
}