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
package com.milaboratory.core.sequence;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class WildcardTest {
    @Test
    public void testRandomSymbol() throws Exception {
        byte[] codes;
        Wildcard symbol;
        int[] unif;
        int tries = 2000;
        double precision = 0.15, expected;
        int otherCodes = 10;
        for (int count = 2; count < 10; ++count) {
            codes = new byte[count + otherCodes];
            for (int i = 1; i < count + otherCodes; ++i)
                codes[i] = (byte) i;
            symbol = new Wildcard('A', (byte) 0, count, codes);
            unif = new int[count];
            for (int i = 0; i < tries; ++i)
                unif[symbol.getUniformlyDistributedBasicCode(i)]++;
            expected = (1. * tries) / count;
            for (int actual : unif)
                Assert.assertTrue(actual < (1. + precision) * expected && actual > (1. - precision) * expected);
        }
    }

    @Test
    public void testIntersection() throws Exception {
        Assert.assertTrue(NucleotideAlphabet.S_WILDCARD.intersectsWith(NucleotideAlphabet.Y_WILDCARD));
        Assert.assertFalse(NucleotideAlphabet.S_WILDCARD.intersectsWith(NucleotideAlphabet.W_WILDCARD));
        Assert.assertTrue(NucleotideAlphabet.S_WILDCARD.intersectsWith(NucleotideAlphabet.C_WILDCARD));
    }

    public static void calculateAllMatches(Alphabet<?> a) {
        for (Wildcard wc1 : a.getAllWildcards()) {
            System.out.print(wc1.cSymbol + " -> new byte[]{");
            boolean first = true;
            for (Wildcard wc2 : a.getAllWildcards())
                if (wc1.intersectsWith(wc2)) {
                    if (first)
                        first = false;
                    else
                        System.out.print(", ");
                    System.out.print(wc2.cSymbol);
                }
            System.out.println("}");
        }
    }

    public static void testMatches(Alphabet<?> a) {
        for (Wildcard wc1 : a.getAllWildcards()) {
            for (Wildcard wc2 : a.getAllWildcards())
                if (wc1.intersectsWith(wc2)) {
                    Assert.assertTrue(wc1.matches(wc2.code));
                    Assert.assertTrue(wc2.matches(wc1.code));
                }else {
                    Assert.assertFalse(wc1.matches(wc2.code));
                    Assert.assertFalse(wc2.matches(wc1.code));
                }
        }
    }
}