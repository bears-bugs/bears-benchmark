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

import gnu.trove.map.hash.TByteObjectHashMap;

import java.util.HashMap;

public final class CustomSerializerImpl implements Serializer {
    final HashMap<Class<?>, TypeInfo> infoByClass;
    final TByteObjectHashMap<TypeInfo> infoById;

    public CustomSerializerImpl(HashMap<Class<?>, TypeInfo> infoByClass) {
        this.infoByClass = infoByClass;
        this.infoById = new TByteObjectHashMap<>();
        for (TypeInfo info : infoByClass.values())
            infoById.put(info.id, info);
    }

    @Override
    public void write(PrimitivO output, Object object) {
        Class<?> type = object.getClass();

        // Trying to find appropriate serializer for
        TypeInfo info = infoByClass.get(type);
        if (info == null) {
            Class<?> parent = Util.findSerializableParent(type, true, false);
            info = infoByClass.get(parent);
            if (info == null)
                throw new RuntimeException("Can't find serializer for " + type);
            else
                //Caching this type->info mapping
                infoByClass.put(type, info);
        }

        // Writing type id
        output.writeByte(info.id);
        // Writing content using corresponding sub-serializer
        Serializer serializer = info.serializer;
        serializer.write(output, object);
        if (!serializer.handlesReference())
            output.writeReference(object);
    }

    @Override
    public Object read(PrimitivI input) {
        byte id = input.readByte();
        Serializer serializer = infoById.get(id).serializer;
        Object obj = serializer.read(input);
        if (!serializer.handlesReference())
            input.readReference(obj);
        return obj;
    }

    public static class TypeInfo {
        final byte id;
        final Serializer serializer;

        public TypeInfo(byte id, Serializer serializer) {
            this.id = id;
            this.serializer = serializer;
        }
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public boolean handlesReference() {
        return true;
    }
}
