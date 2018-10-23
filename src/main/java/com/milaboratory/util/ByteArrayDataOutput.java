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
package com.milaboratory.util;

import java.io.DataOutput;
import java.nio.ByteBuffer;

/**
 * Dynamically growing byte array backed data output.
 *
 * This class is not thread safe.
 *
 * writeUTF uses non-DataOutputStream-compatible results.
 */
public final class ByteArrayDataOutput implements DataOutput {
    public static final int DEFUALT_INITIAL_SIZE = 32;
    public static final int DEFAULT_GROW_MULTIPLIER = 3;
    public static final int DEFAULT_GROW_DIVISOR = 2;
    public static final int DEFAULT_GROW_SUMMAND = 32;
    public static final int DEFAULT_GROW_MAXIMUM_CHUNK = 1 << 23; // 8 Mb
    final int growMultiplier, growDivisor, growSummand, growMaximumChunk;
    /**
     * Backing array
     */
    private byte[] buffer;
    /**
     * Wraps buffer
     */
    private ByteBuffer byteBuffer;

    public ByteArrayDataOutput() {
        this(DEFUALT_INITIAL_SIZE, DEFAULT_GROW_MULTIPLIER, DEFAULT_GROW_DIVISOR, DEFAULT_GROW_SUMMAND, DEFAULT_GROW_MAXIMUM_CHUNK);
    }

    public ByteArrayDataOutput(int initialSize) {
        this(new byte[initialSize]);
    }

    public ByteArrayDataOutput(byte[] buffer) {
        this(buffer, DEFAULT_GROW_MULTIPLIER, DEFAULT_GROW_DIVISOR, DEFAULT_GROW_SUMMAND, DEFAULT_GROW_MAXIMUM_CHUNK);
    }

    public ByteArrayDataOutput(int initialSize, int growMultiplier, int growDivisor, int growSummand, int growMaximumChunk) {
        this(new byte[initialSize], growMultiplier, growDivisor, growSummand, growMaximumChunk);
    }

    public ByteArrayDataOutput(byte[] buffer, int growMultiplier, int growDivisor, int growSummand, int growMaximumChunk) {
        this.growMultiplier = growMultiplier;
        this.growDivisor = growDivisor;
        this.growSummand = growSummand;
        this.growMaximumChunk = growMaximumChunk;
        this.buffer = buffer;
        this.byteBuffer = ByteBuffer.wrap(buffer);
    }

    private void ensureCapacity(int size) {
        // Return if have enough space
        if (size <= byteBuffer.capacity() - byteBuffer.position())
            return;

        // Calculating new array size
        int newSize = buffer.length * growMultiplier / growDivisor + growSummand;
        if (newSize - buffer.length > growMaximumChunk)
            newSize = buffer.length + growMaximumChunk;
        if (newSize - buffer.length < size)
            newSize = buffer.length + size;

        // Allocating new array
        byte[] newBuffer = new byte[newSize];
        // And copy only part with actual data from the old buffer
        // Current absolute offset:
        int currentOffset = byteBuffer.position();
        System.arraycopy(buffer, 0, newBuffer, 0, currentOffset);

        this.buffer = newBuffer;
        // Wrapping new array
        this.byteBuffer = ByteBuffer.wrap(newBuffer, 0, newBuffer.length);
        // Setting correct position
        byteBuffer.position(currentOffset);
    }

    public int size() {
        return byteBuffer.position();
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void reset() {
        byteBuffer.rewind();
    }

    @Override
    public void write(int b) {
        ensureCapacity(1);
        byteBuffer.put((byte) b);
    }

    @Override
    public void write(byte[] b) {
        ensureCapacity(b.length);
        byteBuffer.put(b);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        ensureCapacity(len);
        byteBuffer.put(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) {
        ensureCapacity(1);
        byteBuffer.put((byte) (v ? 1 : 0));
    }

    @Override
    public void writeByte(int v) {
        ensureCapacity(1);
        byteBuffer.put((byte) v);
    }

    @Override
    public void writeShort(int v) {
        ensureCapacity(2);
        byteBuffer.putShort((short) v);
    }

    @Override
    public void writeChar(int v) {
        ensureCapacity(2);
        byteBuffer.putChar((char) v);
    }

    @Override
    public void writeInt(int v) {
        ensureCapacity(4);
        byteBuffer.putInt(v);
    }

    @Override
    public void writeLong(long v) {
        ensureCapacity(8);
        byteBuffer.putLong(v);
    }

    @Override
    public void writeFloat(float v) {
        ensureCapacity(4);
        byteBuffer.putFloat(v);
    }

    @Override
    public void writeDouble(double v) {
        ensureCapacity(8);
        byteBuffer.putDouble(v);
    }

    @Override
    public void writeBytes(String s) {
        // Too lazy to write this method
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeChars(String s) {
        // Too lazy to write this method
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeUTF(String str) {
        int len = str.length();
        int dataLen = 0;
        int c, count = 0;

        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F))
                dataLen++;
            else if (c > 0x07FF)
                dataLen += 3;
            else
                dataLen += 2;
        }

        if (dataLen > 65535)
            throw new RuntimeException();

        writeShort(dataLen);

        ensureCapacity(dataLen);

        int i = 0;

        // ASCII
        for (; i < len; i++) {
            c = str.charAt(i);
            if (!((c >= 0x0001) && (c <= 0x007F))) break;
            byteBuffer.put((byte) c);
        }

        // Full UTF
        for (; i < len; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                byteBuffer.put((byte) c);

            } else if (c > 0x07FF) {
                byteBuffer.put((byte) (0xE0 | ((c >> 12) & 0x0F)));
                byteBuffer.put((byte) (0x80 | ((c >> 6) & 0x3F)));
                byteBuffer.put((byte) (0x80 | ((c >> 0) & 0x3F)));
            } else {
                byteBuffer.put((byte) (0xC0 | ((c >> 6) & 0x1F)));
                byteBuffer.put((byte) (0x80 | ((c >> 0) & 0x3F)));
            }
        }
    }
}
