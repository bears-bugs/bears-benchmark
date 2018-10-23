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

import java.util.Arrays;

public final class MinimalQualityAggregator implements QualityAggregator {
    final byte[] data;

    public MinimalQualityAggregator(int size) {
        this.data = new byte[size];
        Arrays.fill(this.data, Byte.MAX_VALUE);
    }

    @Override
    public synchronized void aggregate(SequenceQuality quality) {
        if (quality.size() != this.data.length)
            throw new IllegalArgumentException();
        for (int i = 0; i < quality.size(); i++)
            data[i] = (byte) Math.min(data[i], quality.value(i));
    }

    @Override
    public SequenceQuality getQuality() {
        return new SequenceQuality(data);
    }
}
