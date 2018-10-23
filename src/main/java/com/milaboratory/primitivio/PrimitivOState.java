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
package com.milaboratory.primitivio;

import gnu.trove.impl.Constants;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.custom_hash.TObjectIntCustomHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * Holds PrimitivO stream state (known objects, known references and serialization manager).
 *
 * PrimitivO always return new instance of the object, that has no references to the original stream.
 *
 * Objects of this class are immutable.
 */
public final class PrimitivOState {
    private final SerializersManager manager;

    private final TObjectIntCustomHashMap<Object> knownReferences;

    private final TObjectIntMap<Object> knownObjects;

    PrimitivOState(SerializersManager manager,
                   TObjectIntCustomHashMap<Object> knownReferences,
                   TObjectIntMap<Object> knownObjects) {
        this.manager = manager.clone();
        this.knownReferences = newKnownReferenceHashMap();
        this.knownReferences.putAll(knownReferences);
        this.knownObjects = newKnownObjectHashMap();
        this.knownObjects.putAll(knownObjects);
    }

    public PrimitivO createPrimitivO(DataOutput output) {
        return new PrimitivO(output, getManagerCopy(), getKnownReferencesCopy(), getKnownObjectsCopy());
    }

    public PrimitivO createPrimitivO(OutputStream output) {
        return createPrimitivO((DataOutput) new DataOutputStream(output));
    }

    private SerializersManager getManagerCopy() {
        return manager.clone();
    }

    private TObjectIntCustomHashMap<Object> getKnownReferencesCopy() {
        TObjectIntCustomHashMap<Object> ret = newKnownReferenceHashMap();
        ret.putAll(knownReferences);
        return ret;
    }

    private TObjectIntMap<Object> getKnownObjectsCopy() {
        TObjectIntMap<Object> ret = newKnownObjectHashMap();
        ret.putAll(knownObjects);
        return ret;
    }

    static TObjectIntCustomHashMap<Object> newKnownReferenceHashMap() {
        return new TObjectIntCustomHashMap<>(IdentityHashingStrategy.INSTANCE, Constants.DEFAULT_CAPACITY,
                Constants.DEFAULT_LOAD_FACTOR, Integer.MIN_VALUE);
    }

    static TObjectIntMap<Object> newKnownObjectHashMap() {
        return new TObjectIntHashMap<>(Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, Integer.MIN_VALUE);
    }
}
