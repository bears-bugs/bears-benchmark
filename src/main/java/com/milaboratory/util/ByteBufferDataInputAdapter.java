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

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class ByteBufferDataInputAdapter implements DataInput {
    final ByteBuffer buffer;

    public ByteBufferDataInputAdapter(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void readFully(byte[] b) {
        buffer.get(b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) {
        buffer.get(b, off, len);
    }

    @Override
    public int skipBytes(int n) {
        buffer.position(buffer.position() + n);
        return n;
    }

    @Override
    public boolean readBoolean() {
        byte val = buffer.get();
        if (val != 0 && val != 1)
            throw new RuntimeException();
        return val == 1;
    }

    @Override
    public byte readByte() {
        return buffer.get();
    }

    @Override
    public int readUnsignedByte() {
        // too lazy
        throw new UnsupportedOperationException();
    }

    @Override
    public short readShort() {
        return buffer.getShort();
    }

    @Override
    public int readUnsignedShort() {
        return 0xFFFF & ((int) readShort());
    }

    @Override
    public char readChar() {
        return buffer.getChar();
    }

    @Override
    public int readInt() {
        return buffer.getInt();
    }

    @Override
    public long readLong() {
        return buffer.getLong();
    }

    @Override
    public float readFloat() {
        return buffer.getFloat();
    }

    @Override
    public double readDouble() {
        return buffer.getDouble();
    }

    @Override
    public String readLine() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }
}
