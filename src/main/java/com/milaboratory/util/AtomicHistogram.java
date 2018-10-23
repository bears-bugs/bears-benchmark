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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class AtomicHistogram {
    private final double[] boundaries;
    private final AtomicLong total = new AtomicLong();
    private final AtomicLongArray hist;

    public AtomicHistogram(double[] boundaries) {
        this.boundaries = boundaries;
        this.hist = new AtomicLongArray(boundaries.length - 1);
    }

    public AtomicHistogram(int lower, int upper) {
        this(lower - 0.5, upper + 0.5, upper - lower + 1);
    }

    public AtomicHistogram(double lower, double upper, int bins) {
        this(bins(lower, upper, bins));
    }

    public void add(double value) {
        total.incrementAndGet();

        if (value < boundaries[0] || boundaries[boundaries.length - 1] < value)
            return;

        int i = Arrays.binarySearch(boundaries, value);
        if (i < 0)
            i = -1 - i;
        if (i > 0)
            --i;

        hist.incrementAndGet(i);
    }

    public double[] getBoundaries() {
        return boundaries.clone();
    }

    public long[] getHist() {
        long[] result = new long[hist.length()];
        for (int i = 0; i < result.length; i++)
            result[i] = hist.get(i);
        return result;
    }

    public long getTotalCountInHist() {
        long result = 0;
        for (int i = 0; i < hist.length(); i++)
            result += hist.get(i);
        return result;
    }

    public double mean() {
        double sum = 0;
        long totalCount = 0;
        for (int i = 0; i < hist.length(); i++) {
            sum += hist.get(i) * (boundaries[i] + boundaries[i + 1]) / 2;
            totalCount += hist.get(i);
        }
        return sum / totalCount;
    }

    public double getCoveredFraction() {
        return 1.0 * getTotalCountInHist() / getTotalProcessed();
    }

    public long getTotalProcessed() {
        return total.get();
    }

    static double[] bins(double lower, double upper, int bins) {
        double[] result = new double[bins + 1];
        double step = (upper - lower) / bins;
        for (int i = 0; i < bins; i++)
            result[i] = lower + i * step;
        result[bins] = upper;
        return result;
    }

    @JsonUnwrapped
    @JsonValue
    public SerializableResult getSerializableResult() {
        return new SerializableResult(getBoundaries(), getTotalProcessed(), getHist(), getCoveredFraction());
    }

    @Override
    public String toString() {
        try {
            return GlobalObjectMappers.toOneLine(getSerializableResult());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static final class SerializableResult {
        public final double[] boundaries;
        public final long total;
        public final long[] hist;
        public final double coveredFraction;

        public SerializableResult(double[] boundaries, long total, long[] hist, double coveredFraction) {
            this.boundaries = boundaries;
            this.total = total;
            this.hist = hist;
            this.coveredFraction = coveredFraction;
        }
    }
}
