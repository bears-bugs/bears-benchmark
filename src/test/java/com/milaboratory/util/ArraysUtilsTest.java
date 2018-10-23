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

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ArraysUtilsTest {
    @Test
    public void test1() throws Exception {
        int[] a = {3, 6, 2, 1, 6, 7};
        int[] b = a.clone();
        int[] r = {7, 6, 1, 2, 6, 3};
        ArraysUtils.reverse(a);
        assertArrayEquals(r, a);
        ArraysUtils.reverse(a);
        assertArrayEquals(b, a);
    }

    @Test
    public void test2() throws Exception {
        int[] a = {3, 6, 2, 8, 1, 6, 7};
        int[] b = a.clone();
        int[] r = {7, 6, 1, 8, 2, 6, 3};
        ArraysUtils.reverse(a);
        assertArrayEquals(r, a);
        ArraysUtils.reverse(a);
        assertArrayEquals(b, a);
    }

    @Test
    public void test3() throws Exception {
        int[] a = {3, 6, 2, 1, 6, 7};
        int[] b = a.clone();
        int[] r = {3, 6, 6, 1, 2, 7};
        ArraysUtils.reverse(a, 2, 5);
        assertArrayEquals(r, a);
        ArraysUtils.reverse(a, 2, 5);
        assertArrayEquals(b, a);
    }
}