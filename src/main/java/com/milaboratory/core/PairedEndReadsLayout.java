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

import com.milaboratory.core.io.sequence.PairedRead;
import com.milaboratory.core.io.sequence.SequenceRead;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.sequence.NSequenceWithQuality;

public enum PairedEndReadsLayout implements java.io.Serializable {
    /**
     * R1     R2
     * ---> <---
     */
    DirectOnly(new PairedTargetProvider(
            +1, -2),
            new SingleTargetProvider(false),
            true),
    /**
     * R2     R1
     * ---> <---
     */
    ReverseOnly(new PairedTargetProvider(
            +2, -1),
            new SingleTargetProvider(true),
            true),
    /**
     * R1     R2
     * ---> <---
     * +
     * R2     R1
     * ---> <---
     */
    Opposite(new PairedTargetProvider(
            +1, -2,
            +2, -1),
            new SingleTargetProvider(false, true),
            true),
    /**
     * R1     R2
     * ---> --->
     * +
     * R2     R1
     * <--- <---
     */
    Collinear(new PairedTargetProvider(
            +1, +2,
            -2, -1),
            new SingleTargetProvider(false, true),
            false),
    /**
     * R1     R2
     * ---> --->
     */
    CollinearDirect(new PairedTargetProvider(
            +1, +2),
            new SingleTargetProvider(false),
            false),
    /**
     * R1     R2
     * ---> <---
     * +
     * R2     R1
     * ---> <---
     * +
     * R1     R2
     * ---> --->
     * +
     * R2     R1
     * <--- <---
     */
    Unknown(new PairedTargetProvider(
            +1, -2,
            +2, -1,
            +1, +2,
            -2, -1),
            new SingleTargetProvider(false, true),
            false, true);
    private final PairedTargetProvider pairedProvider;
    private final SingleTargetProvider singleProvider;
    /**
     * Determines possible relative (R1 relative to R2) strands. (true for RC; false for same strand)
     *
     * Used only in paired-end reads merger.
     */
    private final boolean[] possibleRelativeStrands;

    PairedEndReadsLayout(PairedTargetProvider pairedProvider,
                         SingleTargetProvider singleProvider,
                         boolean... possibleRelativeStrands) {
        this.pairedProvider = pairedProvider;
        this.singleProvider = singleProvider;
        this.possibleRelativeStrands = possibleRelativeStrands;
    }

    public Target[] createTargets(PairedRead read) {
        return pairedProvider.createTargets(read);
    }

    public Target[] createTargets(SingleRead read) {
        return singleProvider.createTargets(read);
    }

    public Target[] createTargets(SequenceRead read) {
        if (read instanceof PairedRead)
            return pairedProvider.createTargets((PairedRead) read);
        if (read instanceof SingleRead)
            return singleProvider.createTargets((SingleRead) read);
        throw new IllegalArgumentException("Unknown read type.");
    }

    /**
     * Determines possible relative (R1 relative to R2) strands. (true for RC; false for same strand)
     *
     * Used only in paired-end reads merger.
     */
    public boolean[] getPossibleRelativeStrands() {
        return possibleRelativeStrands;
    }

    private static final class SingleTargetProvider {
        final boolean[] states;

        public SingleTargetProvider(boolean... states) {
            this.states = states;
        }

        Target[] createTargets(SingleRead read) {
            Target[] ts = new Target[states.length];
            int i = 0;
            for (boolean state : states)
                ts[i++] = new Target(state ?
                        read.getData().getReverseComplement() :
                        read.getData(), state);
            return ts;
        }
    }

    private static final class PairedTargetProvider {
        final byte[][] ids;

        PairedTargetProvider(int... ids) {
            assert ids.length % 2 == 0;
            this.ids = new byte[ids.length / 2][];
            for (int i = 0; i < ids.length / 2; i++) {
                this.ids[i] = new byte[]{(byte) ids[i * 2], (byte) ids[i * 2 + 1]};
            }
        }

        Target[] createTargets(PairedRead read) {
            final Target[] result = new Target[ids.length];
            for (int i = 0; i < ids.length; i++) {
                byte[] ii = ids[i];
                result[i] = new Target(dataFromId(read, ii[0]),
                        dataFromId(read, ii[1]), ii);
            }
            return result;
        }

        NSequenceWithQuality dataFromId(PairedRead read, byte id) {
            switch (id) {
                case +1:
                    return read.getR1().getData();
                case +2:
                    return read.getR2().getData();
                case -1:
                    return read.getR1().getData().getReverseComplement();
                case -2:
                    return read.getR2().getData().getReverseComplement();
            }
            throw new IllegalArgumentException();
        }
    }
}
