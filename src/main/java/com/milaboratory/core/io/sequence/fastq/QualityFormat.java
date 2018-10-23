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
package com.milaboratory.core.io.sequence.fastq;

/**
 * A base class to store various sequencing quality formats.
 *
 * See corresponding Wikipedia page for details: <a href="http://en.wikipedia.org/wiki/FASTQ_format">link</a>.
 */
public enum QualityFormat {
    /**
     * Phred quality values encoded with 33 value offset.
     *
     * <p>Allowed quality score values range is 0-50.</p>
     */
    Phred33((byte) 33, (byte) 0, (byte) 50, "phred33"),
    /**
     * Phred quality values encoded with 64 value offset.
     *
     * <p>Allowed quality score values range is 0-41.</p>
     */
    Phred64((byte) 64, (byte) 0, (byte) 41, "phred64");
    private byte offset, minValue, maxValue;
    private String name;

    QualityFormat(byte offset, byte minValue, byte maxVaule, String name) {
        this.offset = offset;
        this.minValue = minValue;
        this.maxValue = maxVaule;
        this.name = name;
    }

    public byte getMinValue() {
        return minValue;
    }

    public byte getMaxValue() {
        return maxValue;
    }

    public byte getOffset() {
        return offset;
    }

    public static QualityFormat fromName(String name) {
        switch (name.toLowerCase()) {
            case "phred33":
                return Phred33;
            case "phred64":
                return Phred64;
            default:
                throw new IllegalArgumentException("Unknown quality format: " + name + ".");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
