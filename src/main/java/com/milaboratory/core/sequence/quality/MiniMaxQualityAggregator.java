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

public class MiniMaxQualityAggregator implements QualityAggregator {
    final int size;
    byte miniMax;

    public MiniMaxQualityAggregator(int size) {
        this.size = size;
    }

    @Override
    public synchronized void aggregate(SequenceQuality quality) {
        miniMax = (byte) Math.max(miniMax, quality.minValue());
    }

    @Override
    public SequenceQuality getQuality() {
        return SequenceQuality.getUniformQuality(miniMax, size);
    }
}
