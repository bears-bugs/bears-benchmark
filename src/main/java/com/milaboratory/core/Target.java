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
package com.milaboratory.core;

import com.milaboratory.core.sequence.NSequenceWithQuality;

public final class Target {
    private static final byte[] SINGLE_FORWARD = new byte[]{+1};
    private static final byte[] SINGLE_REVERSED = new byte[]{-1};
    public final NSequenceWithQuality[] targets;
    /**
     * -2 for RC R2
     * -1 for RC R1
     * +1 for Direct R1
     * +2 for Direct R2
     */
    private final byte[] readIds;

    Target(NSequenceWithQuality target, boolean reversed) {
        this.targets = new NSequenceWithQuality[]{target};
        this.readIds = reversed ? SINGLE_REVERSED : SINGLE_FORWARD;
    }

    Target(NSequenceWithQuality target1, NSequenceWithQuality target2, byte[] readIds) {
        this.targets = new NSequenceWithQuality[]{target1, target2};
        this.readIds = readIds;
    }

    /**
     * 1 for SingleRead input or 2 for PairedRead input
     */
    public int numberOfParts() {
        return readIds.length;
    }

    /**
     * @param targetId 0 or 1
     * @return 0 for R1, 1 for R2
     */
    public int getReadIdOfTarget(int targetId) {
        return Math.abs(readIds[targetId]) - 1;
    }

    /**
     * {@literal true} for RC, {@literal false} for Direct
     *
     * @param targetId 0 or 1
     * @return {@literal true} for RC, {@literal false} for Direct
     */
    public boolean getRCStateOfTarget(int targetId) {
        return readIds[targetId] < 0;
    }

    public byte getFullSourceId(int targetId) {
        return readIds[targetId];
    }
}
