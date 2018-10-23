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

/**
 * BandedSemiLocalResult - class which is result of BandedSemiLocal alignment.
 *
 * <p>BandedSemiLocal alignment - alignment where part of second sequence is aligned either to left part of to right
 * part of first sequence. </p>
 *
 * <p>"Banded alignment" means that sequences to be aligned are very similar and number of mutations is very low.</p>
 */
public final class BandedSemiLocalResult implements java.io.Serializable {
    /**
     * Positions at which alignment terminates.
     * <p/>
     * If BandedSemiLocalLeft alignment was performed, second sequence is aligned to the left part of first sequence. If
     * BandedSemiLocalRight alignment was performed, second sequence is aligned to the right part of first sequence.
     */
    public final int sequence1Stop, sequence2Stop;
    /**
     * Score
     */
    public final int score;

    /**
     * Creates new BandedSemiLocalResult
     *
     * @param sequence1Stop position at which alignment of first sequence terminates (inclusive)
     * @param sequence2Stop position at which alignment of second sequence terminates (inclusive)
     */
    public BandedSemiLocalResult(int sequence1Stop, int sequence2Stop, int score) {
        this.sequence1Stop = sequence1Stop;
        this.sequence2Stop = sequence2Stop;
        this.score = score;
    }
}
