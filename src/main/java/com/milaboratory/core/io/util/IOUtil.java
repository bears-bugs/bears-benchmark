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
package com.milaboratory.core.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils contains methods for compact encoding of 32 and 64 bit integers.
 *
 * <p>Code of this class is partially copied from com.google.protobuf.CodedOutputStream and
 * com.google.protobuf.CodedInputStream classes from Google's protobuf library.</p>
 */
public class IOUtil {
    /**
     * Read a raw Varint from the stream.
     *
     * <p>Based on com.google.protobuf.CodedInputStream class from Google's protobuf library.</p>
     */
    public static int readRawVarint32(final InputStream is, int eofVaule) throws IOException {
        int result = 0;
        int shift = 0;
        for (; shift < 32; shift += 7) {
            final int b = is.read();

            if (b == -1)
                if (shift == 0)
                    return eofVaule;
                else
                    throw new IOException("Malformed Varint");

            result |= (b & 0x7f) << shift;
            if ((b & 0x80) == 0)
                return result;
        }
        throw new IOException("Malformed Varint");
    }

    /**
     * Read a raw Varint from the stream.
     *
     * <p>Based on com.google.protobuf.CodedInputStream class from Google's protobuf library.</p>
     */
    public static long readRawVarint64(final InputStream is, long eofValue) throws IOException {
        int shift = 0;
        long result = 0;
        while (shift < 64) {
            final int b = is.read();

            if (b == -1)
                if (shift == 0)
                    return eofValue;
                else
                    throw new IOException("Malformed Varint");

            result |= (long) (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                return result;
            }
            shift += 7;
        }
        throw new IOException("Malformed Varint");
    }

    /**
     * Encode and write a varint.  {@code value} is treated as unsigned, so it won't be sign-extended if negative.
     *
     * <p>Copied from com.google.protobuf.CodedOutputStream from Google's protobuf library.</p>
     */
    public static void writeRawVarint32(OutputStream os, int value) throws IOException {
        while (true) {
            if ((value & ~0x7F) == 0) {
                os.write(value);
                return;
            } else {
                os.write((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    /**
     * Compute the number of bytes that would be needed to encode a varint. {@code value} is treated as unsigned, so it
     * won't be sign-extended if negative.
     *
     * <p>Copied from com.google.protobuf.CodedOutputStream from Google's protobuf library.</p>
     */
    public static int computeRawVarint32Size(final int value) {
        if ((value & (0xffffffff << 7)) == 0) return 1;
        if ((value & (0xffffffff << 14)) == 0) return 2;
        if ((value & (0xffffffff << 21)) == 0) return 3;
        if ((value & (0xffffffff << 28)) == 0) return 4;
        return 5;
    }

    /**
     * Encode and write a varint.
     *
     * <p>Copied from com.google.protobuf.CodedOutputStream from Google's protobuf library.</p>
     */
    public static void writeRawVarint64(OutputStream os, long value) throws IOException {
        while (true) {
            if ((value & ~0x7FL) == 0) {
                os.write((int) value);
                return;
            } else {
                os.write(((int) value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    /**
     * Compute the number of bytes that would be needed to encode a varint.
     *
     * <p>Copied from com.google.protobuf.CodedOutputStream from Google's protobuf library.</p>
     */
    public static int computeRawVarint64Size(final long value) {
        if ((value & (0xffffffffffffffffL << 7)) == 0) return 1;
        if ((value & (0xffffffffffffffffL << 14)) == 0) return 2;
        if ((value & (0xffffffffffffffffL << 21)) == 0) return 3;
        if ((value & (0xffffffffffffffffL << 28)) == 0) return 4;
        if ((value & (0xffffffffffffffffL << 35)) == 0) return 5;
        if ((value & (0xffffffffffffffffL << 42)) == 0) return 6;
        if ((value & (0xffffffffffffffffL << 49)) == 0) return 7;
        if ((value & (0xffffffffffffffffL << 56)) == 0) return 8;
        if ((value & (0xffffffffffffffffL << 63)) == 0) return 9;
        return 10;
    }

    /**
     * Encode a ZigZag-encoded 32-bit value.  ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint.  (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     *
     * <p>Copied from com.google.protobuf.CodedOutputStream from Google's protobuf library.</p>
     *
     * @param n A signed 32-bit integer.
     * @return An unsigned 32-bit integer, stored in a signed int because Java has no explicit unsigned support.
     */
    public static int encodeZigZag32(final int n) {
        // Note:  the right-shift must be arithmetic
        return (n << 1) ^ (n >> 31);
    }

    /**
     * Encode a ZigZag-encoded 64-bit value.  ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint.  (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     *
     * <p>Copied from com.google.protobuf.CodedOutputStream from Google's protobuf library.</p>
     *
     * @param n A signed 64-bit integer.
     * @return An unsigned 64-bit integer, stored in a signed int because Java has no explicit unsigned support.
     */
    public static long encodeZigZag64(final long n) {
        // Note:  the right-shift must be arithmetic
        return (n << 1) ^ (n >> 63);
    }

    /**
     * Decode a ZigZag-encoded 32-bit value.  ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint.  (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     *
     * <p>Copied from com.google.protobuf.CodedInputStream from Google's protobuf library.</p>
     *
     * @param n An unsigned 32-bit integer, stored in a signed int because Java has no explicit unsigned support.
     * @return A signed 32-bit integer.
     */
    public static int decodeZigZag32(final int n) {
        return (n >>> 1) ^ -(n & 1);
    }

    /**
     * Decode a ZigZag-encoded 64-bit value.  ZigZag encodes signed integers into values that can be efficiently encoded
     * with varint.  (Otherwise, negative values must be sign-extended to 64 bits to be varint encoded, thus always
     * taking 10 bytes on the wire.)
     *
     * <p>Copied from com.google.protobuf.CodedInputStream from Google's protobuf library.</p>
     *
     * @param n An unsigned 64-bit integer, stored in a signed int because Java has no explicit unsigned support.
     * @return A signed 64-bit integer.
     */
    public static long decodeZigZag64(final long n) {
        return (n >>> 1) ^ -(n & 1);
    }
}
