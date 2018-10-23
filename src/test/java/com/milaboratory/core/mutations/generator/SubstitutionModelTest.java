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

import com.milaboratory.test.TestUtil;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class SubstitutionModelTest {
    @Test
    public void test1() throws Exception {
        SubstitutionModel model = SubstitutionModels.getEmpiricalNucleotideSubstitutionModel();
        RandomGenerator rg = new Well19937c(12312);

        int trials = 100000;

        for (int lF = 0; lF < 4; ++lF) {

            int[] result = new int[4];
            for (int i = 0; i < trials; ++i)
                result[model.sample(rg, lF)]++;

            for (int lT = 0; lT < 4; ++lT)
                Assert.assertEquals(trials * model.getProbability(lF, lT), result[lT], Math.sqrt(trials * model.getProbability(lF, lT)) * 3);
        }
    }

    @Test
    public void test2() throws Exception {
        SubstitutionModel sm = SubstitutionModels.getUniformNucleotideSubstitutionModel(.1);
        RandomGenerator rg = new Well19937c(12313432L);
        int trials = 1000000;

        for (int k = 0; k < TestUtil.its(20, 200); ++k) {
            int mutations = 0;
            for (int i = 0; i < trials; ++i) {
                if (sm.sample(rg, 3) != 3)
                    ++mutations;
            }

            Assert.assertEquals(100000, mutations, 2000);
        }
    }
}
