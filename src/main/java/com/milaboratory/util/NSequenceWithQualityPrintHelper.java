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

import com.milaboratory.core.sequence.NSequenceWithQuality;

import java.util.Arrays;

public class NSequenceWithQualityPrintHelper {
    final NSequenceWithQuality seq;
    final int offset, lineLength;
    final int linesCount;

    public NSequenceWithQualityPrintHelper(NSequenceWithQuality seq,
                                           int offset,
                                           int lineLength) {
        this.seq = seq;
        this.offset = offset;
        this.lineLength = lineLength;
        this.linesCount = (seq.size() + lineLength - 1) / lineLength;
    }

    public int getLinesCount() {
        return linesCount;
    }

    public String getSequenceLine(int lineIndex) {
        int initialPosition = (lineIndex * lineLength);
        String initialPositionStr = "" + initialPosition;
        int numberOfspaces = offset - 1 - initialPositionStr.length();
        int finalPosition = Math.min(initialPosition + lineLength, seq.size());
        StringBuilder sb = new StringBuilder();
        sb.append(spaces(numberOfspaces));
        sb.append(initialPositionStr);
        sb.append(" ");
        sb.append(seq.getSequence().getRange(initialPosition, finalPosition));
        sb.append(" ");
        sb.append(finalPosition - 1);
        return sb.toString();
    }

    public String getQualityLine(int lineIndex) {
        int initialPosition = lineIndex * lineLength;
        int finalPosition = Math.min(initialPosition + lineLength, seq.size());
        StringBuilder sb = new StringBuilder();
        sb.append(spaces(offset));
        sb.append(seq.getQuality().getRange(initialPosition, finalPosition));
        return sb.toString();
    }

    private static String spaces(int n) {
        char[] c = new char[n];
        Arrays.fill(c, ' ');
        return String.valueOf(c);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linesCount; ++i) {
            sb.append(getSequenceLine(i));
            sb.append("\n");
            sb.append(getQualityLine(i));
            sb.append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }
}
