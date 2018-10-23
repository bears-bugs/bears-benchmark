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
package com.milaboratory.core.sequence;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.milaboratory.core.Range;
import com.milaboratory.core.io.sequence.fastq.QualityFormat;
import com.milaboratory.core.io.sequence.fastq.WrongQualityFormat;
import com.milaboratory.primitivio.annotations.Serializable;
import com.milaboratory.util.ArraysUtils;

import java.util.Arrays;

/**
 * Representation of nucleotide sequence quality based on phred quality scores.
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
@JsonSerialize(using = IO.SQSeqSerializer.class)
@JsonDeserialize(using = IO.SQSeqDeserializer.class)
@Serializable(by = IO.SequenceQualitySerializer.class)
public final class SequenceQuality extends AbstractSeq<SequenceQuality>
        implements java.io.Serializable {
    public static final SequenceQuality EMPTY = new SequenceQuality(new byte[0]);
    /**
     * Default value of good quality
     */
    public static final byte GOOD_QUALITY_VALUE = (byte) 34;
    /**
     * This value encoded with Phred+33 will giv 'z'.
     * 59 would produce '{' which is a special character, and may spoil formatting in some cases.
     */
    public static final byte MAX_QUALITY_VALUE = (byte) 58;
    /**
     * Default value of bad quality
     */
    public static final byte BAD_QUALITY_VALUE = (byte) 0;

    private static final long serialVersionUID = 1L;
    final byte[] data;

    /**
     * Creates a phred sequence quality from a Sanger formatted quality string (33 based).
     *
     * @param string
     */
    public SequenceQuality(String string) {
        this(string, 33);
    }

    /**
     * Creates a phred sequence quality from a string formatted with corresponding offset.
     *
     * @param string string
     */
    public SequenceQuality(String string, int offset) {
        this.data = string.getBytes();
        for (int i = this.data.length - 1; i >= 0; --i)
            this.data[i] -= offset;
    }

    /**
     * Creates a phred sequence quality from a string formatted with corresponding offset.
     *
     * @param string string
     */
    public SequenceQuality(String string, QualityFormat format) {
        this(string, format.getOffset());
    }


    /**
     * Creates quality object from raw quality score values.
     *
     * @param data raw quality score values
     */
    public SequenceQuality(byte[] data) {
        this.data = data.clone();
    }

    /**
     * Constructor for factory method.
     */
    SequenceQuality(byte[] data, boolean unsafe) {
        assert unsafe;
        this.data = data;
    }


    /**
     * Returns an underlying array of bytes.
     *
     * @return underlying array of bytes
     */
    public byte[] asArray() {
        return data.clone();
    }

    /**
     * Get the log10 of probability of error (e.g. nucleotide substitution) at given sequence point
     *
     * @param position positioninate in sequence
     * @return log10 of probability of error
     */
    public float log10ProbabilityOfErrorAt(int position) {
        return -((float) data[position]) / 10;
    }

    /**
     * Get probability of error (e.g. nucleotide substitution) at given sequence point
     *
     * @param position positioninate in sequence
     * @return probability of error
     */
    public float probabilityOfErrorAt(int position) {
        return (float) Math.pow(10.0, -(data[position]) / 10);
    }

    /**
     * Get the raw sequence quality value (in binary format) at given sequence point
     *
     * @param position positioninate in sequence
     * @return raw sequence quality value
     */
    public byte value(int position) {
        return data[position];
    }

    /**
     * Returns the worst sequence quality value
     *
     * @return worst sequence quality value
     */
    public byte minValue() {
        if (data.length == 0)
            return 0;

        byte min = Byte.MAX_VALUE;
        for (byte b : data)
            if (b < min)
                min = b;
        return min;
    }

    /**
     * Returns average sequence quality value
     *
     * @return average sequence quality value
     */
    public byte meanValue() {
        if (data.length == 0)
            return 0;

        int sum = 0;
        for (byte b : data)
            sum += b;
        return (byte) (sum / data.length);
    }

    /**
     * Gets quality values in reverse order
     *
     * @return quality values in reverse order
     */
    public SequenceQuality reverse() {
        return new SequenceQuality(reverseCopy(data), true);
    }

    /**
     * Returns substring of current quality scores line.
     *
     * @param from inclusive
     * @param to   exclusive
     * @return substring of current quality scores line
     */
    @Override
    public SequenceQuality getRange(int from, int to) {
        return getRange(new Range(from, to));
    }

    /**
     * Returns substring of current quality scores line.
     *
     * @param range range
     * @return substring of current quality scores line
     */
    @Override
    public SequenceQuality getRange(Range range) {
        byte[] rdata = Arrays.copyOfRange(data, range.getLower(), range.getUpper());
        if (range.isReverse())
            ArraysUtils.reverse(rdata);
        return new SequenceQuality(rdata, true);
    }

    /**
     * Returns size of quality array
     *
     * @return size of quality array
     */
    @Override
    public int size() {
        return data.length;
    }

    @Override
    public SequenceQualityBuilder getBuilder() {
        return new SequenceQualityBuilder();
    }

    public SequenceQuality concatenate(SequenceQuality... other) {
        if (other.length == 0)
            return this;
        int size = size();
        for (SequenceQuality sequenceQuality : other)
            size += sequenceQuality.size();
        byte[] r = Arrays.copyOf(data, size);
        size = size();
        for (SequenceQuality sq : other) {
            System.arraycopy(sq.data, 0, r, size, sq.size());
            size += sq.size();
        }
        return new SequenceQuality(r, true);
    }

    /**
     * Encodes current quality line with given offset. Common values for offset are 33 and 64.
     *
     * @param offset offset
     * @return bytes encoded quality values
     */
    public void encodeTo(QualityFormat format, byte[] buffer, int offset) {
        byte vo = format.getOffset();
        for (int i = 0; i < data.length; ++i)
            buffer[offset++] = (byte) (data[i] + vo);
    }

    /**
     * Encodes current quality line with given offset. Common values for offset are 33 and 64.
     *
     * @param offset offset
     * @return bytes encoded quality values
     */
    public byte[] encode(int offset) {
        if (offset < 0 || offset > 70)
            throw new IllegalArgumentException();

        byte[] copy = new byte[data.length];
        for (int i = copy.length - 1; i >= 0; --i)
            copy[i] += data[i] + offset;
        return copy;
    }

    /**
     * Encodes current quality line with given format. Common values for offset are 33 and 64.
     *
     * @param format quality format with offset
     * @return bytes encoded quality values
     */
    public byte[] encode(QualityFormat format) {
        return encode(format.getOffset());
    }

    /**
     * Encodes current quality line with given offset. Common values for offset are 33 and 64.
     *
     * @param offset offset
     * @return encoded quality values
     */
    public String encodeToString(int offset) {
        return new String(encode(offset));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data) * 31 + 17;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequenceQuality that = (SequenceQuality) o;
        return Arrays.equals(data, that.data);
    }

    @Override
    public String toString() {
        return encodeToString(33);
    }

    /**
     * Creates a phred sequence quality containing only given values of quality.
     *
     * @param qualityValue value to fill the quality values with
     * @param length       size of quality string
     */
    public static SequenceQuality getUniformQuality(byte qualityValue, int length) {
        byte[] data = new byte[length];
        Arrays.fill(data, qualityValue);
        return new SequenceQuality(data, true);
    }

    /******************
     * STATIC METHODS
     *****************/

    /**
     * Helper method.
     */
    private static byte[] reverseCopy(byte[] quality) {
        byte[] newData = new byte[quality.length];
        int reverseposition = quality.length - 1;
        for (int position = 0; position < quality.length; ++position, --reverseposition)
            //reverseposition = quality.length - 1 - position;
            newData[position] = quality[reverseposition];

        assert reverseposition == -1;

        return newData;
    }

    /**
     * Factory method for the SequenceQualityPhred object. It performs all necessary range checks if required.
     *
     * @param format format of encoded quality values
     * @param data   byte with encoded quality values
     * @param from   starting position in {@code data}
     * @param length number of bytes to parse
     * @param check  determines whether range check is required
     * @return quality line object
     * @throws WrongQualityFormat if encoded value are out of range and checking is enabled
     */
    public static SequenceQuality create(QualityFormat format, byte[] data, int from, int length, boolean check) {
        if (from + length >= data.length || from < 0 || length < 0)
            throw new IllegalArgumentException();
        //For performance
        final byte valueOffset = format.getOffset(),
                minValue = format.getMinValue(),
                maxValue = format.getMaxValue();
        byte[] res = new byte[length];
        int pointer = from;
        for (int i = 0; i < length; i++) {
            res[i] = (byte) (data[pointer++] - valueOffset);

            if (check &&
                    (res[i] < minValue || res[i] > maxValue))
                throw new WrongQualityFormat(((char) (data[i])) + " [" + res[i] + "]");
        }
        return new SequenceQuality(res, true);
    }

    /**
     * Factory method for the SequenceQualityPhred object. It performs all necessary range checks if required.
     *
     * @param format format of encoded quality values
     * @param data   byte with encoded quality values
     * @param check  determines whether range check is required
     * @return quality line object
     * @throws WrongQualityFormat if encoded value are out of range and checking is enabled
     */
    public static SequenceQuality create(QualityFormat format, byte[] data, boolean check) {
        return create(format, data, 0, data.length, check);
    }
}
