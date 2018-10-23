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

import com.milaboratory.primitivio.annotations.CustomSerializer;
import com.milaboratory.primitivio.annotations.Serializable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.milaboratory.primitivio.Util.findSerializableParent;

public final class SerializersManager {
    final DefaultSerializersProvider defaultSerializersProvider;
    final HashMap<Class<?>, Serializer> registeredHelpers;

    public SerializersManager() {
        this(new DefaultSerializersProviderImpl(), new HashMap<>());
    }

    public SerializersManager(DefaultSerializersProvider defaultSerializersProvider,
                              HashMap<Class<?>, Serializer> registeredHelpers) {
        this.defaultSerializersProvider = defaultSerializersProvider;
        this.registeredHelpers = registeredHelpers;
    }

    public <T> Serializer<? super T> getSerializer(Class<T> type) {
        Serializer serializer = registeredHelpers.get(type);

        if (serializer == null) {
            Class<?> parent = findSerializableParent(type, true, false);
            serializer = registeredHelpers.get(parent);
            if (serializer != null)
                registeredHelpers.put(type, serializer);
        }

        if (serializer != null)
            return serializer;


        return createAndRegisterSerializer(type);
    }

    public void registerCustomSerializer(Class<?> type, Serializer<?> customSerializer) {
        registeredHelpers.put(type, customSerializer);
    }

    private Serializer createAndRegisterSerializer(Class<?> type) {
        Class<?> root = findRoot(type);

        Serializer serializer;
        if (root == null) {
            serializer = defaultSerializersProvider.createSerializer(type, this);
            if (serializer == null)
                throw new RuntimeException("" + type + " is not serializable.");
            else
                root = type;
        } else
            serializer = createSerializer0(root, false);

        registeredHelpers.put(root, serializer);

        if (type != root)
            registeredHelpers.put(type, serializer);

        if (serializer instanceof CustomSerializerImpl)
            for (Class<?> subType : ((CustomSerializerImpl) serializer).infoByClass.keySet())
                registeredHelpers.put(subType, serializer);

        return serializer;
    }

    private Serializer createSerializer0(Class<?> type, boolean nested) {
        Serializable annotation = type.getAnnotation(Serializable.class);

        if (annotation == null)
            throw new IllegalArgumentException("" + type + " is not serializable.");

        Serializer defaultSerializer =
                annotation.by() == Serializer.class ?
                        null :
                        instantiate(annotation.by());

        if (annotation.asJson()) {
            if (defaultSerializer != null)
                throw new RuntimeException("'asJson' and 'by' parameters are not compatible.");
            defaultSerializer = new JSONSerializer(type);
        }

        CustomSerializer[] css = annotation.custom();
        if (css.length > 0) {
            if (nested)
                throw new RuntimeException("Nested custom serializers in " + type + ".");

            HashMap<Class<?>, CustomSerializerImpl.TypeInfo> infoByClass = new HashMap<>();

            // Adding default serializer
            if (defaultSerializer != null)
                infoByClass.put(type, new CustomSerializerImpl.TypeInfo((byte) 0, defaultSerializer));

            // Adding custom serializers
            for (CustomSerializer cs : css)
                infoByClass.put(cs.type(), new CustomSerializerImpl.TypeInfo(cs.id(), createSerializer0(cs.type(), true)));

            return new CustomSerializerImpl(infoByClass);
        } else {
            if (defaultSerializer == null)
                throw new RuntimeException("No serializer for " + type);
            return defaultSerializer;
        }
    }

    @Override
    protected SerializersManager clone() {
        return new SerializersManager(
                defaultSerializersProvider,
                new HashMap<>(registeredHelpers)
        );
    }

    static Serializer instantiate(Class<? extends Serializer> cl) {
        boolean initialAccessibility = true;
        Constructor<? extends Serializer> constructor = null;
        try {
            constructor = cl.getConstructor();
            initialAccessibility = constructor.isAccessible();
            if (!initialAccessibility)
                constructor.setAccessible(true);
            return cl.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } finally {
            if (!initialAccessibility && constructor != null)
                constructor.setAccessible(false);
        }
    }

    static Class<?> findRoot(Class<?> type) {
        List<Class<?>> serializableClasses = getAllSerializableInTree(type);

        if (serializableClasses.isEmpty())
            return null;

        Class<?> realRoot = findRealRoot(serializableClasses.get(0)), tmp;

        for (int i = 1; i < serializableClasses.size(); i++) {
            tmp = findRealRoot(serializableClasses.get(i));
            if (!Objects.equals(tmp, realRoot))
                throw new IllegalArgumentException("Conflict between " + realRoot + " and " + tmp + " through " + serializableClasses.get(i));
        }

        return realRoot;
    }

    /* Utility methods for root calculation */

    private static List<Class<?>> getAllSerializableInTree(Class<?> type) {
        List<Class<?>> list = new ArrayList<>();
        addAllSerializableInTree(type, list);
        return list;
    }

    private static void addAllSerializableInTree(Class<?> type, List<Class<?>> list) {
        if (type.getAnnotation(Serializable.class) != null)
            list.add(type);

        Class<?> superclass = type.getSuperclass();
        if (superclass != null)
            addAllSerializableInTree(superclass, list);

        for (Class<?> cInterface : type.getInterfaces())
            addAllSerializableInTree(cInterface, list);
    }

    private static Class<?> findRealRoot(Class<?> type) {
        Class<?> tmp = findSerializableParent(type, false, true);
        if (tmp != null)
            return tmp;
        tmp = findSerializableParent(type, false, false);
        return tmp;
    }
}
