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
package com.milaboratory.core.io.util;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937a;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class IOUtilTest {
    @Test
    public void test1() throws Exception {
        RandomGenerator rg = new Well19937a();

        int count = 1000;
        int[] values = new int[count];

        for (int n = 0; n < 10; ++n) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = 0; i < count; ++i) {
                final int d = rg.nextInt(31);
                values[i] = (rg.nextInt(Integer.MAX_VALUE) >>> d);
                IOUtil.writeRawVarint32(bos, values[i]);
            }

            byte[] data = bos.toByteArray();

            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            int g;
            for (int i = 0; i < count; ++i) {
                Assert.assertEquals(values[i], IOUtil.readRawVarint32(bis, -1));
            }
            Assert.assertEquals(-1, IOUtil.readRawVarint32(bis, -1));
        }
    }

    @Test
    public void test3() throws Exception {
        RandomGenerator rg = new Well19937a();

        int count = 1000;
        int[] values = new int[count];

        for (int n = 0; n < 10; ++n) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = 0; i < count; ++i) {
                final int d = rg.nextInt(31) + 1;
                values[i] = (rg.nextInt(Integer.MAX_VALUE) >>> d);
                if (rg.nextBoolean())
                    values[i] *= -1;
                final int encoded = IOUtil.encodeZigZag32(values[i]);
                Assert.assertTrue(-1 != encoded);
                IOUtil.writeRawVarint32(bos, encoded);
            }

            byte[] data = bos.toByteArray();

            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            int g;
            for (int i = 0; i < count; ++i) {
                Assert.assertEquals(values[i], IOUtil.decodeZigZag32(IOUtil.readRawVarint32(bis, -1)));
            }
            Assert.assertEquals(-1, IOUtil.readRawVarint32(bis, -1));
        }
    }

    @Test
    public void test2() throws Exception {
        RandomGenerator rg = new Well19937a();

        int count = 1000;
        long[] values = new long[count];

        for (int n = 0; n < 10; ++n) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = 0; i < count; ++i) {
                final int d = rg.nextInt(63) + 1;
                values[i] = (rg.nextLong() >>> d);
                IOUtil.writeRawVarint64(bos, values[i]);
            }

            byte[] data = bos.toByteArray();

            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            int g;
            for (int i = 0; i < count; ++i) {
                Assert.assertEquals(values[i], IOUtil.readRawVarint64(bis, -1));
            }
            Assert.assertEquals(-1L, IOUtil.readRawVarint64(bis, -1));
        }
    }

    @Test
    public void test4() throws Exception {
        RandomGenerator rg = new Well19937a();

        int count = 1000;
        long[] values = new long[count];

        for (int n = 0; n < 10; ++n) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = 0; i < count; ++i) {
                final int d = rg.nextInt(63) + 1;

                values[i] = (rg.nextLong() >>> d);

                if (rg.nextBoolean())
                    values[i] *= -1;

                final long encoded = IOUtil.encodeZigZag64(values[i]);
                Assert.assertTrue(-1L != encoded);
                IOUtil.writeRawVarint64(bos, encoded);
            }

            byte[] data = bos.toByteArray();

            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            int g;
            for (int i = 0; i < count; ++i) {
                Assert.assertEquals(values[i], IOUtil.decodeZigZag64(IOUtil.readRawVarint64(bis, -1)));
            }
            Assert.assertEquals(-1L, IOUtil.readRawVarint64(bis, -1));
        }
    }

    @Test
    public void test111() throws Exception {
        System.out.println(IOUtil.encodeZigZag32(-2));
        System.out.println(IOUtil.encodeZigZag64(-2));
        System.out.println(IOUtil.decodeZigZag32(-1));
        System.out.println(IOUtil.decodeZigZag64(-1));
    }
}
