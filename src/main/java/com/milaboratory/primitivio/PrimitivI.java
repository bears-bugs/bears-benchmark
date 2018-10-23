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
package com.milaboratory.primitivio;

import java.io.*;
import java.util.ArrayList;

public final class PrimitivI implements DataInput, AutoCloseable {
    final DataInput input;
    final SerializersManager manager;
    final ArrayList<Object> knownReferences;
    final ArrayList<Object> knownObjects;
    final ArrayList<Object> putKnownAfterReset = new ArrayList<>();
    int knownReferencesCount = 0;
    int depth = 0;

    public PrimitivI(InputStream input) {
        this(new DataInputStream(input),
                new SerializersManager());
    }

    public PrimitivI(DataInput input) {
        this(input, new SerializersManager());
    }

    public PrimitivI(DataInput input, SerializersManager manager) {
        this(input, manager, new ArrayList<>(), new ArrayList<>());
    }

    public PrimitivI(DataInput input, SerializersManager manager,
                     ArrayList<Object> knownReferences, ArrayList<Object> knownObjects) {
        this.input = input;
        this.manager = manager;
        this.knownReferences = knownReferences;
        this.knownObjects = knownObjects;
        this.knownReferencesCount = knownReferences.size();
    }

    public SerializersManager getSerializersManager() {
        return manager;
    }

    /**
     * Returns a copy of current PrimitivI state. The state can then be used to create PrimitivI with the same state of
     * known objects, known references and serialization manager.
     */
    public PrimitivIState getState() {
        return new PrimitivIState(manager, knownReferences, knownObjects);
    }

    public void putKnownObject(Object ref) {
        knownObjects.add(ref);
    }

    public void putKnownReference(Object ref) {
        if (depth > 0) {
            putKnownAfterReset.add(ref);
        } else {
            knownReferences.add(ref);
            ++knownReferencesCount;
        }
    }

    public void readReference(Object ref) {
        int id = readVarInt();
        if (id != knownReferences.size())
            throw new RuntimeException("wrong reference id.");
        knownReferences.add(ref);
    }

    private void reset() {
        for (int i = knownReferences.size() - 1; i >= knownReferencesCount; --i)
            knownReferences.remove(i);
        if (!putKnownAfterReset.isEmpty()) {
            for (Object ref : putKnownAfterReset)
                putKnownReference(ref);
            putKnownAfterReset.clear();
        }
    }

    public <T> T readObject(Class<T> type) {
        Serializer serializer = manager.getSerializer(type);
        if (serializer.isReference()) {
            int id = readVarInt();
            if (id == PrimitivO.NULL_ID) {
                return null;
            } else if (id == PrimitivO.NEW_OBJECT_ID) {
                boolean readReferenceAfter = !serializer.handlesReference();

                ++depth;
                try {
                    T obj = (T) serializer.read(this);

                    if (readReferenceAfter)
                        readReference(obj);

                    return obj;
                } finally {
                    --depth;
                    if (depth == 0)
                        reset();
                }
            } else if ((id & 1) == 0) {
                Object obj = knownReferences.get((id >>> 1) - 1);
                if (!type.isInstance(obj))
                    throw new RuntimeException("Wrong file format.");
                return (T) obj;
            } else {
                Object obj = knownObjects.get((id >>> 1) - 1);
                if (!type.isInstance(obj))
                    throw new RuntimeException("Wrong file format.");
                return (T) obj;
            }
        } else {
            ++depth;
            try {
                return (T) serializer.read(this);
            } finally {
                --depth;
                if (depth == 0)
                    reset();
            }
        }
    }

    public long readVarLongZigZag() {
        return Util.zigZagDecodeLong(readVarLong());
    }

    public long readVarLong() {
        long value = 0, tmp;
        int shift = 0;
        do {
            tmp = readByte();
            value |= (tmp & 0x7F) << (shift);
            shift += 7;
        } while ((tmp & 0x80) != 0);
        return value;
    }

    public int readVarIntZigZag() {
        return Util.zigZagDecodeInt(readVarInt());
    }

    public int readVarInt() {
        int value = 0, tmp;
        int shift = 0;
        do {
            tmp = readByte();
            value |= (tmp & 0x7F) << (shift);
            shift += 7;
        } while ((tmp & 0x80) != 0);
        return value;
    }

    @Override
    public void readFully(byte[] b) {
        try {
            input.readFully(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readFully(byte[] b, int off, int len) {
        try {
            input.readFully(b, off, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int skipBytes(int n) {
        try {
            return input.skipBytes(n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean readBoolean() {
        try {
            return input.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte readByte() {
        try {
            return input.readByte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int readUnsignedByte() {
        try {
            return input.readUnsignedByte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public short readShort() {
        try {
            return input.readShort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int readUnsignedShort() {
        try {
            return input.readUnsignedShort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public char readChar() {
        try {
            return input.readChar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int readInt() {
        try {
            return input.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long readLong() {
        try {
            return input.readLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public float readFloat() {
        try {
            return input.readFloat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double readDouble() {
        try {
            return input.readDouble();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readLine() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String readUTF() {
        try {
            return input.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (input instanceof Closeable)
                ((Closeable) input).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
