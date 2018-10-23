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

import com.milaboratory.core.sequence.SequenceQuality;
import com.milaboratory.util.BitArray;

import java.util.Arrays;

import static com.milaboratory.util.StringUtil.spaces;

public final class AlignmentHelper implements java.io.Serializable {
    protected final String seq1String, seq2String;
    protected final String markup;
    protected final int[] seq1Position, seq2Position;
    protected final BitArray match;
    protected final int offset;

    public AlignmentHelper(String seq1String, String seq2String, int[] seq1Position, int[] seq2Position, BitArray match) {
        this(seq1String, seq2String, seq1Position, seq2Position, match, 0);
    }

    public AlignmentHelper(String seq1String, String seq2String, int[] seq1Position, int[] seq2Position, BitArray match, int offset) {
        if (seq1Position.length == 0)
            seq1Position = new int[1];
        if (seq2Position.length == 0)
            seq2Position = new int[1];

        this.seq1String = seq1String;
        this.seq2String = seq2String;
        this.seq1Position = seq1Position;
        this.seq2Position = seq2Position;
        this.match = match;
        this.offset = Math.max(Math.max(("" + aL(seq2Position[0])).length(), ("" + aL(seq1Position[0])).length()), offset);

        char[] chars = new char[match.size()];
        Arrays.fill(chars, ' ');
        for (int n : match.getBits())
            chars[n] = '|';

        this.markup = new String(chars);
    }

    public AlignmentHelper getRange(int from, int to) {
        return getRange(from, to, 0);
    }

    public AlignmentHelper getRange(int from, int to, int offset) {
        return new AlignmentHelper(seq1String.substring(from, to), seq2String.substring(from, to),
                Arrays.copyOfRange(seq1Position, from, to), Arrays.copyOfRange(seq2Position, from, to),
                match.getRange(from, to), offset);
    }

    public AlignmentHelper[] split(int length) {
        return split(length, 0);
    }

    public AlignmentHelper[] split(int length, int offset) {
        AlignmentHelper[] ret = new AlignmentHelper[(size() + length - 1) / length];
        for (int i = 0; i < ret.length; ++i) {
            int pointer = i * length;
            int l = Math.min(length, size() - pointer);
            ret[i] = getRange(pointer, pointer + l, offset);
        }
        return ret;
    }

    /**
     * Gets the identity of alignment, i.e. ratio of matching nucleotides in the aligned region
     */
    public double identity() {
        return match.bitCount() * 1.0 / match.size();
    }

    /**
     * Gets the size of aligned region
     */
    public int size() {
        return match.size();
    }

    public int getSequence1PositionAt(int i) {
        return seq1Position[i];
    }

    public int getSequence2PositionAt(int i) {
        return seq2Position[i];
    }

    /**
     * Get the aligned query sequence
     */
    public String getSeq1String() {
        return seq1String;
    }

    /**
     * Get the aligned subject sequence
     */
    public String getSeq2String() {
        return seq2String;
    }

    /**
     * Get the alignment markup line
     */
    public String getMarkup() {
        return markup;
    }

    /**
     * Gets the first line of formatted alignment (query sequence)
     */
    public String getLine1() {
        String startPosition = String.valueOf(aL(seq1Position[0]));
        int spaces = offset - startPosition.length();
        return spaces(spaces) + startPosition + " " + seq1String +
                " " + aR(seq1Position[seq1Position.length - 1]);
    }

    public String getLine1Compact() {
        String startPosition = String.valueOf(aL(seq1Position[0]));
        int spaces = offset - startPosition.length();
        return spaces(spaces) + startPosition + " " + toCompact(seq1String) +
                " " + aR(seq1Position[seq1Position.length - 1]);
    }

    /**
     * Gets the second line of formatted alignment (alignment markup)
     */
    public String getLine2() {
        return spaces(offset + 1) + markup;
    }

    /**
     * Gets the third line of formatted alignment (subject sequence)
     */
    public String getLine3() {
        String startPosition = String.valueOf(aL(seq2Position[0]));
        int spaces = offset - startPosition.length();
        return spaces(spaces) + startPosition + " " + seq2String +
                " " + aR(seq2Position[seq2Position.length - 1]);
    }

    public String toStringWithSeq2Quality(SequenceQuality quality) {
        char[] chars = new char[size()];
        for (int i = 0; i < size(); ++i)
            chars[i] = seq2Position[i] < 0 ? ' ' : (char) (33 + quality.value(seq2Position[i]));
        return getLine1() + "\n" + getLine2() + "\n" + getLine3() + "\n" + spaces(offset + 1) + new String(chars);
    }

    public String getLine3Compact() {
        String startPosition = String.valueOf(aL(seq2Position[0]));
        int spaces = offset - startPosition.length();
        return spaces(spaces) + startPosition + " " + toCompact(seq2String) +
                " " + aR(seq2Position[seq2Position.length - 1]);
    }

    private String toCompact(String seqString) {
        char[] chars = seqString.toCharArray();
        for (int i = 0; i < match.size(); ++i)
            if (match.get(i))
                chars[i] = Character.toLowerCase(chars[i]);
        return new String(chars);
    }

    @Override
    public String toString() {
        return getLine1() + "\n" + getLine2() + "\n" + getLine3();
    }

    /**
     * Gets the alignment string in contact format (no markup line, mismatches shown by lower-case characters)
     */
    public String toCompactString() {
        return getLine1Compact() + "\n" + getLine3Compact();
    }

    private static int aL(int f) {
        return f < 0 ? ~f : f;
    }

    private static int aR(int f) {
        return f < 0 ? ~f - 1 : f;
    }
}
