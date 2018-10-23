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

public class AverageQualityAggregator implements QualityAggregator {
    private static final long OVERFLOW = 0xFFFFFFFFFFFFFFFFL;
    final int size;
    /**
     * 0 = byte
     * 1 = short
     * 2 = int
     * 3 = long
     */
    byte state = 0;
    long[] data;

    public AverageQualityAggregator(int size) {
        this.size = size;
        this.data = new long[arraySize()];
    }

    private int arraySize() {
        return (size + 1 + (7 >> state)) >> (3 - state);
    }

    @Override
    public void aggregate(SequenceQuality quality) {
        if (quality.size() != size)
            throw new IllegalArgumentException();

        long tmp;
        int segments = arraySize();
        switch (state) {
            case 0:
                for (int i = 0; i < segments; i++) {
                    tmp = encode(quality, i);
                    tmp = add0(tmp, data[i]);
                    if (tmp == OVERFLOW) {
                        // System.out.println("0 -> 1");
                        // Revert array
                        --i;
                        for (; i >= 0; --i)
                            data[i] -= encode(quality, i);

                        // Grow
                        state = 1;
                        long[] newData = new long[arraySize()];
                        for (int j = 0; j < data.length; j++) {
                            newData[j * 2] = (0 |
                                    ((0xFF00_0000_0000_0000L & data[j]) >>> 8) |
                                    ((0x00FF_0000_0000_0000L & data[j]) >>> 16) |
                                    ((0x0000_FF00_0000_0000L & data[j]) >>> 24) |
                                    ((0x0000_00FF_0000_0000L & data[j]) >>> 32));
                            if (newData.length > j * 2 + 1)
                                newData[j * 2 + 1] = (0 |
                                        ((0x0000_0000_FF00_0000L & data[j]) << 24) |
                                        ((0x0000_0000_00FF_0000L & data[j]) << 16) |
                                        ((0x0000_0000_0000_FF00L & data[j]) << 8) |
                                        ((0x0000_0000_0000_00FFL & data[j])));
                        }
                        data = newData;
                        aggregate(quality);
                        return;
                    }
                    data[i] = tmp;
                }
                return;
            case 1:
                for (int i = 0; i < segments; i++) {
                    tmp = encode(quality, i);
                    tmp = add1(tmp, data[i]);
                    if (tmp == OVERFLOW) {
                        // System.out.println("1 -> 2");
                        // Revert array
                        --i;
                        for (; i >= 0; --i)
                            data[i] -= encode(quality, i);

                        // Grow
                        state = 2;
                        long[] newData = new long[arraySize()];
                        for (int j = 0; j < data.length; j++) {
                            newData[j * 2] = 0 |
                                    ((0xFFFF_0000_0000_0000L & data[j]) >>> 16) |
                                    ((0x0000_FFFF_0000_0000L & data[j]) >>> 32);
                            if (newData.length > j * 2 + 1)
                                newData[j * 2 + 1] = 0 |
                                        ((0x0000_0000_FFFF_0000L & data[j]) << 16) |
                                        ((0x0000_0000_0000_FFFFL & data[j]));
                        }
                        data = newData;
                        aggregate(quality);
                        return;
                    }
                    data[i] = tmp;
                }
                return;
            case 2:
                for (int i = 0; i < segments; i++) {
                    tmp = encode(quality, i);
                    tmp = add2(tmp, data[i]);
                    if (tmp == OVERFLOW) {
                        // System.out.println("2 -> 3");
                        // Revert array
                        --i;
                        for (; i >= 0; --i)
                            data[i] -= encode(quality, i);

                        // Grow
                        state = 3;
                        long[] newData = new long[arraySize()];
                        for (int j = 0; j < data.length; j++) {
                            newData[j * 2] = ((0xFFFF_FFFF_0000_0000L & data[j]) >>> 32);
                            if (newData.length > j * 2 + 1)
                                newData[j * 2 + 1] = ((0x0000_0000_FFFF_FFFFL & data[j]) << 32);
                        }
                        data = newData;
                        aggregate(quality);
                        return;
                    }
                    data[i] = tmp;
                }
                return;
            case 3:
                for (int i = 0; i < segments; i++)
                    data[i] = add3(encode(quality, i), data[i]);
                return;
        }
    }

    long encode(SequenceQuality quality, int segment) {
        long result = segment == 0 ? 1 : 0;
        int shift = 8 << state;
        int k = (segment << (3 - state)) - 1 + (int) result;
        int i = (8 >> state) - (int) result;
        for (; i > 0 && k < quality.size(); --i) {
            result <<= shift;
            result |= (0xFF & quality.value(k++));
        }
        result <<= shift * i;
        return result;
    }

    public byte getState() {
        return state;
    }

    long getCount() {
        long mask = 0xFFFFFFFFFFFFFFFFL >>> (64 - (8 << state));
        return mask & (data[0] >>> ((~0 & (7 >> state)) << (state + 3)));
    }

    long getTotal(int index) {
        ++index;
        long mask = 0xFFFFFFFFFFFFFFFFL >>> (64 - (8 << state));
        return mask & (data[index >>> (3 - state)] >>> ((~index & (7 >> state)) << (state + 3)));
    }

    public static long add0(long a, long b) {
        long result = a + b;
        if (((result ^ a ^ b) & 0x0101010101010100L) != 0 || ((a >>> 56) + (b >>> 56)) >= 0x100L)
            return OVERFLOW;
        return result;
    }

    public static long add1(long a, long b) {
        long result = a + b;
        if (((result ^ a ^ b) & 0x0001000100010000L) != 0 || ((a >>> 48) + (b >>> 48)) >= 0x10000L)
            return OVERFLOW;
        return result;
    }

    public static long add2(long a, long b) {
        long result = a + b;
        if (((result ^ a ^ b) & 0x0000000100000000L) != 0 || ((a >>> 32) + (b >>> 32)) >= 0x100000000L)
            return OVERFLOW;
        return result;
    }

    public static long add3(long a, long b) {
        return a + b;
    }

    @Override
    public SequenceQuality getQuality() {
        byte[] result = new byte[size];
        long count = getCount();
        for (int i = 0; i < size; i++)
            result[i] = (byte) ((getTotal(i) + count / 2) / count);
        return new SequenceQuality(result);
    }
}
