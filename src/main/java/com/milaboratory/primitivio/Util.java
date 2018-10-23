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

import com.milaboratory.primitivio.annotations.Serializable;

import java.io.*;
import java.util.*;

public final class Util {
    private Util() {
    }

    static Class<?> findSerializableParent(Class<?> type, boolean direct, boolean withCustomSerializersOnly) {
        Class<?> root = null, tmp;

        Serializable a = type.getAnnotation(Serializable.class);
        if (a != null && (!withCustomSerializersOnly || a.custom().length > 0))
            if (direct)
                return type;
            else
                root = type;

        Class<?> superclass = type.getSuperclass();

        if (superclass != null) {
            tmp = findSerializableParent(superclass, direct, withCustomSerializersOnly);
            if (tmp != null) {
                if (root == null)
                    root = tmp;
                else
                    throw new RuntimeException("Custom serializers conflict: " + root + " and " + tmp + " through " + type);
            }
        }

        for (Class<?> cInterface : type.getInterfaces()) {
            tmp = findSerializableParent(cInterface, direct, withCustomSerializersOnly);
            if (tmp != null) {
                if (root == null || root == tmp)
                    root = tmp;
                else
                    throw new RuntimeException("Custom serializers conflict: " + root + " and " + tmp + " through " + type);
            }
        }

        return root;
    }

    public static void writeList(List<?> list, String fileName) throws IOException {
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileName))) {
            writeList(list, stream);
        }
    }

    public static void writeList(List<?> list, File file) throws IOException {
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
            writeList(list, stream);
        }
    }

    public static void writeList(List<?> list, OutputStream output) {
        writeList(list, new PrimitivO(output));
    }

    public static void writeList(List<?> list, PrimitivO output) {
        output.writeInt(list.size());
        for (Object o : list)
            output.writeObject(o);
    }

    public static <O> List<O> readList(Class<O> type, String fileName) throws IOException {
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(fileName))) {
            return readList(type, stream);
        }
    }

    public static <O> List<O> readList(Class<O> type, File file) throws IOException {
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            return readList(type, stream);
        }
    }

    public static <O> List<O> readList(Class<O> type, InputStream is) {
        return readList(type, new PrimitivI(is));
    }

    public static <O> List<O> readList(Class<O> type, PrimitivI input) {
        int size = input.readInt();
        List<O> list = new ArrayList<>(size);
        while ((--size) >= 0)
            list.add(input.readObject(type));
        return list;
    }

    public static <K, V> void writeMap(Map<K, V> map, PrimitivO output) {
        output.writeVarInt(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            output.writeObject(entry.getKey());
            output.writeObject(entry.getValue());
        }
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> readEnumMap(PrimitivI input, Class<K> keyClass, Class<V> valueClass) {
        int size = input.readVarInt();
        EnumMap<K, V> map = new EnumMap<>(keyClass);
        for (; size > 0; --size) {
            K key = input.readObject(keyClass);
            V value = input.readObject(valueClass);
            map.put(key, value);
        }
        return map;
    }

    public static <K, V> Map<K, V> readMap(PrimitivI input, Class<K> keyClass, Class<V> valueClass) {
        int size = input.readVarInt();
        Map<K, V> map = new HashMap<>(size);
        for (; size > 0; --size) {
            K key = input.readObject(keyClass);
            V value = input.readObject(valueClass);
            map.put(key, value);
        }
        return map;
    }

    public static long zigZagEncodeLong(long value) {
        return (value << 1) ^ (value >> 63);
    }

    public static long zigZagDecodeLong(long value) {
        return (value >>> 1) ^ ((value << 63) >> 63);
    }

    public static int zigZagEncodeInt(int value) {
        return (value << 1) ^ (value >> 31);
    }

    public static int zigZagDecodeInt(int value) {
        return (value >>> 1) ^ ((value << 31) >> 31);
    }
}
