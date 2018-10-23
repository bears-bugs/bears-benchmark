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

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.custom_hash.TObjectIntCustomHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;

import java.io.*;
import java.util.ArrayList;

import static com.milaboratory.primitivio.Util.zigZagEncodeInt;
import static com.milaboratory.primitivio.Util.zigZagEncodeLong;

public final class PrimitivO implements DataOutput, AutoCloseable {
    static final int NULL_ID = 0;
    static final int NEW_OBJECT_ID = 1;
    private static final float RELOAD_FACTOR = 0.5f;

    /**
     * Underlying output stream
     */
    final DataOutput output;

    /**
     * Holds serializers for this stream
     */
    final SerializersManager manager;

    /**
     * This array holds references that were explicitly added during this serialization round, will be flushed to
     * knownReferences after reset
     */
    final ArrayList<Object> putKnownAfterReset = new ArrayList<>();

    /**
     * List of references added during this serialization round
     */
    final ArrayList<Object> addedReferences = new ArrayList<>();

    /**
     * This REFERENCES will be replaced by "known reference token". This map holds references between serialization
     * rounds (more persistent then currentReferences).
     */
    final TObjectIntCustomHashMap<Object> knownReferences;

    /**
     * These OBJECTS will be replaced by "known object token"
     */
    final TObjectIntMap<Object> knownObjects;

    /**
     * Serialization depth
     */
    int depth = 0;

    /**
     * This map holds references during single serialization round, its state returns to knownReferences, after reset.
     */
    TObjectIntCustomHashMap<Object> currentReferences = null;

    PrimitivO(DataOutput output, SerializersManager manager,
              TObjectIntCustomHashMap<Object> knownReferences, TObjectIntMap<Object> knownObjects) {
        this.output = output;
        this.manager = manager;
        this.knownReferences = knownReferences;
        this.knownObjects = knownObjects;
    }

    public PrimitivO(DataOutput output, SerializersManager manager) {
        this(output, manager,
                PrimitivOState.newKnownReferenceHashMap(), PrimitivOState.newKnownObjectHashMap()
        );
    }

    public PrimitivO(DataOutput output) {
        this(output, new SerializersManager());
    }

    public PrimitivO(OutputStream output) {
        this(new DataOutputStream(output), new SerializersManager());
    }

    /**
     * Returns copy of current PrimitivO state. The state can then be used to create PrimitivO with the same state of
     * known objects, known references and serialization manager.
     */
    public PrimitivOState getState() {
        if (depth != 0)
            throw new IllegalStateException("Can't return state during serialization transaction.");
        return new PrimitivOState(manager, knownReferences, knownObjects);
    }

    public SerializersManager getSerializersManager() {
        return manager;
    }

    private void ensureCurrentReferencesInitialized() {
        if (currentReferences == null)
            currentReferences = new TObjectIntCustomHashMap<>(IdentityHashingStrategy.INSTANCE, knownReferences);
    }

    public int putKnownObject(Object object) {
        // Sequential id
        int id = knownObjects.size();
        knownObjects.put(object, id);
        return id;
    }

    public void putKnownReference(Object object) {
        if (depth > 0)
            putKnownAfterReset.add(object);
        else {
            // Assigning this reference next available id (0 assigned to null)
            knownReferences.put(object, knownReferences.size());
            currentReferences = null;
        }
    }

    private void reset() {
        if (currentReferences != null && currentReferences.size() != knownReferences.size()) {
            if ((currentReferences.size() - knownReferences.size()) * RELOAD_FACTOR > knownReferences.size())
                currentReferences = null;
            else
                for (Object ref : addedReferences)
                    currentReferences.remove(ref);

            // Resetting list of knownReferences added in this serialization round
            addedReferences.clear();
        }
        if (!putKnownAfterReset.isEmpty()) {
            for (Object ref : putKnownAfterReset)
                putKnownReference(ref);
            putKnownAfterReset.clear();
        }
    }

    private int addCurrentReference(Object ref) {
        int id = currentReferences.size();
        currentReferences.put(ref, id);
        addedReferences.add(ref);
        return id;
    }

    public void writeReference(Object ref) {
        int id = addCurrentReference(ref);
        writeVarInt(id);
    }

    public void writeObject(Object object, Class<?> type) {
        Serializer serializer = manager.getSerializer(type);

        if (object == null)
            if (serializer.isReference())
                writeNull();
            else
                throw new IllegalArgumentException("Non-reference type can't be null.");
        else {
            if (depth == 0)
                ensureCurrentReferencesInitialized();

            boolean writeIdAfter = false;
            if (serializer.isReference()) {
                // Checking if it is a known object
                int id = knownObjects.isEmpty() ? Integer.MIN_VALUE : knownObjects.get(object);
                if (id != Integer.MIN_VALUE) {
                    writeKnownObject(id);
                    return;
                }

                // Checking if it is a known reference
                id = currentReferences.get(object);
                if (id != Integer.MIN_VALUE) {
                    writeObjectReference(id);
                    return;
                }

                // Write just new object header to tell the reader that this object has no id yet
                writeNewObject();
                writeIdAfter = !serializer.handlesReference();
            }

            ++depth;
            try {
                serializer.write(this, object);
                if (writeIdAfter)
                    writeReference(object);
            } finally {
                --depth;
                if (depth == 0)
                    reset();
            }
        }
    }

    private void writeNull() {
        writeByte(NULL_ID);
    }

    private void writeNewObject() {
        writeByte(NEW_OBJECT_ID);
    }

    private void writeObjectReference(int value) {
        writeVarInt((value + 1) << 1);
    }

    private void writeKnownObject(int value) {
        writeVarInt(((value + 1) << 1) | 1);
    }

    public void writeVarIntZigZag(int value) {
        writeVarInt(zigZagEncodeInt(value));
    }

    public void writeVarInt(int value) {
        writeVarLong(0xFFFFFFFFL & value);
    }

    public void writeVarLongZigZag(long value) {
        writeVarLong(zigZagEncodeLong(value));
    }

    public void writeVarLong(long value) {
        do {
            int toWrite = (int) (value & 0x7F);
            value >>>= 7;
            if (value != 0)
                toWrite |= 0x80;
            writeByte(toWrite);
        } while (value != 0);
    }

    public void writeObject(Object object) {
        if (object == null)
            writeByte(0);
        else
            writeObject(object, object.getClass());
    }

    @Override
    public void write(int b) {
        try {
            output.write(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(byte[] b) {
        try {
            output.write(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) {
        try {
            output.write(b, off, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeBoolean(boolean v) {
        try {
            output.writeBoolean(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeByte(int v) {
        try {
            output.writeByte(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeShort(int v) {
        try {
            output.writeShort(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeChar(int v) {
        try {
            output.writeChar(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeInt(int v) {
        try {
            output.writeInt(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeLong(long v) {
        try {
            output.writeLong(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeFloat(float v) {
        try {
            output.writeFloat(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeDouble(double v) {
        try {
            output.writeDouble(v);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeBytes(String s) {
        try {
            output.writeBytes(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeChars(String s) {
        try {
            output.writeChars(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeUTF(String s) {
        try {
            output.writeUTF(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (output instanceof Closeable)
                ((Closeable) output).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
