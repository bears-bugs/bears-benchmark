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

import java.lang.reflect.Array;
import java.util.Arrays;

public final class ArraysUtils {
    private ArraysUtils() {
    }

    public static void reverse(int[] array, int from, int to) {
        int i, v;
        int length = to - from;
        for (i = 0; i < (length + 1) / 2; ++i) {
            v = array[from + i];
            array[from + i] = array[from + length - i - 1];
            array[from + length - i - 1] = v;
        }
    }

    public static void reverse(int[] array) {
        int i, v;
        for (i = 0; i < (array.length + 1) / 2; ++i) {
            v = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = v;
        }
    }

    public static void reverse(byte[] array) {
        int i;
        byte v;
        for (i = 0; i < (array.length + 1) / 2; ++i) {
            v = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = v;
        }
    }

    /**
     * Sort array & return array with removed repetitive values.
     *
     * @param values input array (this method will quickSort this array)
     * @return sorted array of distinct values
     */
    public static long[] getSortedDistinct(long[] values) {
        if (values.length == 0)
            return values;
        Arrays.sort(values);
        int shift = 0;
        int i = 0;
        while (i + shift + 1 < values.length)
            if (values[i + shift] == values[i + shift + 1])
                ++shift;
            else {
                values[i] = values[i + shift];
                ++i;
            }
        values[i] = values[i + shift];
        return Arrays.copyOf(values, i + 1);
    }

    public static int[] concatenate(int[] array1, int... array2) {
        int[] r = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, r, 0, array1.length);
        System.arraycopy(array2, 0, r, array1.length, array2.length);
        return r;
    }

    public static long[] concatenate(long[] array1, long... array2) {
        long[] r = new long[array1.length + array2.length];
        System.arraycopy(array1, 0, r, 0, array1.length);
        System.arraycopy(array2, 0, r, array1.length, array2.length);
        return r;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] concatenate(T[] array1, T... array2) {
        Class<?> ct = array1.getClass().getComponentType();
        if (!ct.equals(array2.getClass().getComponentType()))
            throw new IllegalArgumentException("Different runtime types.");
        T[] r = (T[]) Array.newInstance(ct, array1.length + array2.length);
        System.arraycopy(array1, 0, r, 0, array1.length);
        System.arraycopy(array2, 0, r, array1.length, array2.length);
        return r;
    }
}
