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
package com.milaboratory.core.alignment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.milaboratory.core.sequence.Sequence;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class BandedAlignerParameters<S extends Sequence<S>> implements java.io.Serializable {
    AlignmentScoring<S> scoring;
    int width;
    int stopPenalty;

    @JsonCreator
    public BandedAlignerParameters(@JsonProperty("scoring") AlignmentScoring<S> scoring,
                                   @JsonProperty("width") int width,
                                   @JsonProperty("stopPenalty") int stopPenalty) {
        this.scoring = scoring;
        this.width = width;
        this.stopPenalty = stopPenalty;
    }

    /**
     * Returns scoring used for alignment.
     */
    public AlignmentScoring<S> getScoring() {
        return scoring;
    }

    /**
     * Sets scoring used for alignment.
     */
    public BandedAlignerParameters setScoring(AlignmentScoring<S> scoring) {
        this.scoring = scoring;
        return this;
    }

    /**
     * Width of banded alignment matrix. This value affects maximal possible number of indels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width of banded alignment matrix. This value is connected to max allowed number of indels.
     */
    public BandedAlignerParameters setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Alignment score value in banded alignment matrix at which alignment terminates.
     */
    public int getStopPenalty() {
        return stopPenalty;
    }

    /**
     * Sets alignment score value in banded alignment matrix at which alignment terminates.
     */
    public BandedAlignerParameters setStopPenalty(int stopPenalty) {
        this.stopPenalty = stopPenalty;
        return this;
    }

    public BandedAlignerParameters<S> clone() {
        return new BandedAlignerParameters<>(scoring, width, stopPenalty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BandedAlignerParameters that = (BandedAlignerParameters) o;

        if (stopPenalty != that.stopPenalty) return false;
        if (width != that.width) return false;
        if (!scoring.equals(that.scoring)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scoring.hashCode();
        result = 31 * result + width;
        result = 31 * result + stopPenalty;
        return result;
    }
}
