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

import java.lang.reflect.Array;
import java.util.UUID;

public class DefaultSerializersProviderImpl implements DefaultSerializersProvider {
    @Override
    public Serializer createSerializer(Class<?> type, SerializersManager manager) {
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            if (componentType.isPrimitive()) {
                if (componentType == Integer.TYPE)
                    return new IntArraySerializer();
                else if (componentType == Byte.TYPE)
                    return new ByteArraySerializer();
                else if (componentType == Boolean.TYPE)
                    return new BooleanArraySerializer();
                else
                    return null;
            } else {
                return new ArraySerializer(manager.getSerializer(componentType) == null ? null : componentType);
            }
        }

        if (type.isEnum())
            return new EnumSerializer(type);

        if (type == Integer.class)
            return new IntegerSerializer();

        if (type == Long.class)
            return new LongSerializer();

        if (type == UUID.class)
            return new UUIDSerializer();

        if (type == String.class)
            return new StringSerializer();

        return null;
    }

    private static class IntegerSerializer implements Serializer<Integer> {
        @Override
        public void write(PrimitivO output, Integer object) {
            output.writeInt(object);
        }

        @Override
        public Integer read(PrimitivI input) {
            return input.readInt();
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    private static class LongSerializer implements Serializer<Long> {
        @Override
        public void write(PrimitivO output, Long object) {
            output.writeLong(object);
        }

        @Override
        public Long read(PrimitivI input) {
            return input.readLong();
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    private static class StringSerializer implements Serializer<String> {
        @Override
        public void write(PrimitivO output, String object) {
            output.writeUTF(object);
        }

        @Override
        public String read(PrimitivI input) {
            return input.readUTF();
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    private static class IntArraySerializer implements Serializer<int[]> {
        @Override
        public void write(PrimitivO output, int[] object) {
            output.writeVarInt(object.length);
            for (int i : object)
                output.writeInt(i);
        }

        @Override
        public int[] read(PrimitivI input) {
            int[] object = new int[input.readVarInt()];
            for (int i = 0; i < object.length; i++)
                object[i] = input.readInt();
            return object;
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    private static class ByteArraySerializer implements Serializer<byte[]> {
        @Override
        public void write(PrimitivO output, byte[] object) {
            output.writeVarInt(object.length);
            output.write(object);
        }

        @Override
        public byte[] read(PrimitivI input) {
            byte[] object = new byte[input.readVarInt()];
            input.readFully(object);
            return object;
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    private static class BooleanArraySerializer implements Serializer<boolean[]> {
        @Override
        public void write(PrimitivO output, boolean[] object) {
            output.writeVarInt(object.length);
            for (int i = 0; i < object.length; ++i)
                output.writeBoolean(object[i]);
        }

        @Override
        public boolean[] read(PrimitivI input) {
            boolean[] object = new boolean[input.readVarInt()];
            for (int i = 0; i < object.length; ++i)
                object[i] = input.readBoolean();
            return object;
        }

        @Override
        public boolean isReference() {
            return false;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    private static class EnumSerializer implements Serializer {
        final Class<?> enumType;
        final Object[] array;

        private EnumSerializer(Class<?> enumType) {
            this.enumType = enumType;
            this.array = new Object[enumType.getEnumConstants().length];
            System.arraycopy(enumType.getEnumConstants(), 0, array, 0, array.length);
        }

        @Override
        public void write(PrimitivO output, Object object) {
            Enum e = (Enum) object;
            output.writeVarInt(e.ordinal());
        }

        @Override
        public Object read(PrimitivI input) {
            return array[input.readVarInt()];
        }

        @Override
        public boolean isReference() {
            return false;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    /**
     * For Compatibility with previous format
     */
    public static final class CustomEnumSerializer<E extends Enum<E>> implements Serializer {
        final Class<E> enumType;
        final E[] array;

        public CustomEnumSerializer(Class<E> enumType, E... valuesByOrdinal) {
            this.enumType = enumType;
            this.array = valuesByOrdinal;
        }

        @Override
        public void write(PrimitivO output, Object object) {
            throw new IllegalStateException("Not implemented.");
        }

        @Override
        public Object read(PrimitivI input) {
            return array[input.readVarInt()];
        }

        @Override
        public boolean isReference() {
            return false;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    private static class UUIDSerializer implements Serializer<UUID> {
        @Override
        public void write(PrimitivO output, UUID object) {
            output.writeLong(object.getMostSignificantBits());
            output.writeLong(object.getLeastSignificantBits());
        }

        @Override
        public UUID read(PrimitivI input) {
            long msb = input.readLong();
            long lsb = input.readLong();
            return new UUID(msb, lsb);
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }

    /**
     * Default serializer for arrays of objects
     */
    private static class ArraySerializer implements Serializer {
        final Class<?> componentType;

        private ArraySerializer(Class<?> componentType) {
            this.componentType = componentType;
        }

        @Override
        public void write(PrimitivO output, Object object) {
            int length = Array.getLength(object);
            output.writeVarInt(length);
            for (int i = 0; i < length; i++) {
                if (componentType == null)
                    output.writeObject(Array.get(object, i));
                else
                    output.writeObject(Array.get(object, i), componentType);
            }
        }

        @Override
        public Object read(PrimitivI input) {
            int length = input.readVarInt();

            if (componentType == null)
                throw new RuntimeException("Unknown array type.");

            // Object array = Array.newInstance(componentType, length);

            // for (int i = 0; i < length; i++)
            //     Array.set(array, i, input.readObject(componentType));

            Object[] array = (Object[]) Array.newInstance(componentType, length);

            for (int i = 0; i < length; i++)
                array[i] = input.readObject(componentType);

            return array;
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public boolean handlesReference() {
            return false;
        }
    }
}
