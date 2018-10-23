/*
 * Copyright 2018 MiLaboratory.com
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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class QualityTrimmerParameters {
    private final float averageQualityThreshold;
    private final int windowSize;

    @JsonCreator
    public QualityTrimmerParameters(@JsonProperty("averageQualityThreshold") float averageQualityThreshold,
                                    @JsonProperty("windowSize") int windowSize) {
        this.averageQualityThreshold = averageQualityThreshold;
        this.windowSize = windowSize;
    }

    public float getAverageQualityThreshold() {
        return averageQualityThreshold;
    }

    public int getWindowSize() {
        return windowSize;
    }

    @Override
    public String toString() {
        return "QualityTrimmerParameters{" +
                "averageQualityThreshold=" + averageQualityThreshold +
                ", windowSize=" + windowSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualityTrimmerParameters)) return false;
        QualityTrimmerParameters that = (QualityTrimmerParameters) o;
        return Float.compare(that.averageQualityThreshold, averageQualityThreshold) == 0 &&
                windowSize == that.windowSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(averageQualityThreshold, windowSize);
    }
}
