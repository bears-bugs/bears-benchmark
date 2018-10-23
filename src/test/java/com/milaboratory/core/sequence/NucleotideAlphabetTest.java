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
package com.milaboratory.core.sequence;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class NucleotideAlphabetTest {
    @Test
    public void test1() throws Exception {
        for (int i = 0; i < NucleotideSequencesTest.forRC.length(); i++) {
            char original = NucleotideSequencesTest.forRC.charAt(i);
            char complement = NucleotideSequencesTest.afterC.charAt(i);
            Assert.assertEquals(NucleotideAlphabet.INSTANCE.symbolToCode(complement),
                    NucleotideAlphabet.complementCode(NucleotideAlphabet.INSTANCE.symbolToCode(original)));
        }
    }

    @Test
    public void test2() throws Exception {
        Assert.assertTrue(NucleotideAlphabet.A_WILDCARD.isBasic());
        Assert.assertFalse(NucleotideAlphabet.R_WILDCARD.isBasic());
    }

    @Ignore
    @Test
    public void testCalculateIntersections() throws Exception {
        WildcardTest.calculateAllMatches(NucleotideAlphabet.INSTANCE);
    }

    @Test
    public void testMatches() throws Exception {
        WildcardTest.testMatches(NucleotideAlphabet.INSTANCE);
    }
}