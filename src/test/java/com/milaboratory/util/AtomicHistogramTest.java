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
package com.milaboratory.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dbolotin on 29/10/15.
 */
public class AtomicHistogramTest {
    @Test
    public void test1() throws Exception {
        Assert.assertArrayEquals(new double[]{0, 0.5, 1}, AtomicHistogram.bins(0, 1, 2), 0.0001);
    }

    @Test
    public void test2() throws Exception {
        AtomicHistogram hist = new AtomicHistogram(0, 1, 2);

        hist.add(-1);
        hist.add(0);
        hist.add(0.1);
        hist.add(0.5);
        hist.add(0.51);
        hist.add(1);
        hist.add(2);

        Assert.assertEquals(7, hist.getTotalProcessed());
        Assert.assertArrayEquals(new double[]{0, 0.5, 1}, hist.getBoundaries(), 0.0001);
        Assert.assertArrayEquals(new long[]{3, 2}, hist.getHist());
    }

    @Test
    public void test3() throws Exception {
        AtomicHistogram hist = new AtomicHistogram(0, 1);

        hist.add(0);
        hist.add(1);
        hist.add(1);
        hist.add(1);

        Assert.assertEquals(4, hist.getTotalProcessed());
        Assert.assertArrayEquals(new double[]{-0.5, 0.5, 1.5}, hist.getBoundaries(), 0.0001);
        Assert.assertArrayEquals(new long[]{1, 3}, hist.getHist());
    }
}