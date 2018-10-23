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
package com.milaboratory.util;

import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by dbolotin on 01/02/16.
 */
public class IntArrayListTest {
    @Test
    public void testSort1() throws Exception {
        for (int j = 0; j < 100; j++) {
            Well19937c random = RandomUtil.getThreadLocalRandom();
            IntArrayList list = new IntArrayList();
            for (int i = 0; i < 1000; i++)
                list.add(random.nextInt());
            int[] arr = list.toArray();
            Arrays.sort(arr);
            list.stableSort(new IntArrayList.IntComparator() {
                @Override
                public int compare(int i1, int i2) {
                    return Integer.compare(i1, i2);
                }
            });
            Assert.assertArrayEquals(arr, list.toArray());
        }
    }
}