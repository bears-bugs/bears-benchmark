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
package com.milaboratory.core.sequence.quality;

import com.milaboratory.core.sequence.SequenceQuality;
import com.milaboratory.util.RandomUtil;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dbolotin on 22/06/16.
 */
public class QualityAggregatorTest {
    @Test
    public void test1() throws Exception {
        Well19937c r = RandomUtil.getThreadLocalRandom();
        int falses = 0;
        for (int i = 0; i < 10000; i++) {
            int[] valuesA = {r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256),
                    r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256)};
            int[] valuesB = {r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256),
                    r.nextInt(256), r.nextInt(256), r.nextInt(256), r.nextInt(256)};
            long A = 0, B = 0;
            boolean overflow = false;
            for (int j = 0; j < 8; j++) {
                A <<= 8;
                A |= valuesA[j];
                B <<= 8;
                B |= valuesB[j];
                overflow |= (valuesA[j] + valuesB[j] >= 256);
            }
            long result = AverageQualityAggregator.add0(A, B);
            Assert.assertEquals(result == -1, overflow);
            if (!overflow)
                ++falses;
        }
        System.out.println(falses);
    }

    @Test
    public void test2() throws Exception {
        Well19937c r = RandomUtil.getThreadLocalRandom();
        int falses = 0;
        for (int i = 0; i < 10000; i++) {
            int[] valuesA = {r.nextInt(0x10000), r.nextInt(0x10000), r.nextInt(0x10000), r.nextInt(0x10000)};
            int[] valuesB = {r.nextInt(0x10000), r.nextInt(0x10000), r.nextInt(0x10000), r.nextInt(0x10000)};
            long A = 0, B = 0;
            boolean overflow = false;
            for (int j = 0; j < 4; j++) {
                A <<= 16;
                A |= valuesA[j];
                B <<= 16;
                B |= valuesB[j];
                overflow |= (valuesA[j] + valuesB[j] >= 0x10000);
            }
            long result = AverageQualityAggregator.add1(A, B);
            Assert.assertEquals(result == -1, overflow);
            if (!overflow)
                ++falses;
        }
        System.out.println(falses);
    }

    @Test
    public void test3() throws Exception {
        Well19937c r = RandomUtil.getThreadLocalRandom();
        int falses = 0;
        for (int i = 0; i < 10000; i++) {
            long[] valuesA = {r.nextLong(0x100000000L), r.nextLong(0x100000000L)};
            long[] valuesB = {r.nextLong(0x100000000L), r.nextLong(0x100000000L)};
            long A = 0, B = 0;
            boolean overflow = false;
            for (int j = 0; j < 2; j++) {
                A <<= 32;
                A |= valuesA[j];
                B <<= 32;
                B |= valuesB[j];
                overflow |= (valuesA[j] + valuesB[j] >= 0x100000000L);
            }
            long result = AverageQualityAggregator.add2(A, B);
            Assert.assertEquals(result == -1, overflow);
            if (!overflow)
                ++falses;
        }
        System.out.println(falses);
    }

    @Test
    public void test4() throws Exception {
        Well19937c r = RandomUtil.getThreadLocalRandom();
        for (int z = 0; z < 100; z++) {
            int length = 1 + r.nextInt(200);
            AverageQualityAggregator aga = new AverageQualityAggregator(length);
            byte[] q = new byte[length];
            long[] lAgg = new long[length];
            int iters = 10000;
            for (int k = 0; k < iters; k++) {
                int state = 0;
                for (int i = 0; i < length; i++)
                    state = Math.max(state,
                            ((64 + 7 - Long.numberOfLeadingZeros(lAgg[i] += q[i] = (byte) r.nextInt(128))) >>> 3) - 1);
                SequenceQuality sq = new SequenceQuality(q);
                Assert.assertEquals(k, aga.getCount());
                aga.aggregate(sq);
                Assert.assertEquals(state, aga.getState());
                for (int i = 0; i < length; i++)
                    Assert.assertEquals(lAgg[i], aga.getTotal(i));
            }
            SequenceQuality quality = aga.getQuality();
            for (int i = 0; i < length; i++) {
                Assert.assertEquals(Math.round(1.0 * lAgg[i] / iters), quality.value(i));
            }
        }
    }

    //@Test
    //public void test444() throws Exception {
    //    System.out.println((64 + 7 - Long.numberOfLeadingZeros(255)) >>> 3);
    //    //int state = 2;
    //    //int index = 1;
    //    //long l = 0xFFFFFFFFFFFFFFFFL >>> (64 - (8 << state));
    //    //System.out.println(((~index & (7 >> state)) << (state + 3)));
    //}
}