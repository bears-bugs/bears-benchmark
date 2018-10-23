/*
 * Copyright 2017 MiLaboratory.com
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

import cc.redberry.pipe.OutputPort;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.primitivio.SerializersManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * Created by dbolotin on 04/04/2017.
 */
public interface ObjectSerializer<O> {
    /**
     * Implementation may close stream.
     *
     * @param data   objects
     * @param stream output stream
     */
    void write(Collection<O> data, OutputStream stream);

    OutputPort<O> read(InputStream stream);

    /**
     * Implementation of ObjectSerializer for objects supporting PrimitivIO based serialization.
     */
    final class PrimitivIOObjectSerializer<O> implements ObjectSerializer<O> {
        private Class<O> clazz;
        /**
         * Buffered serializers manager
         */
        private SerializersManager sm;

        public PrimitivIOObjectSerializer(Class<O> clazz) {
            this.clazz = clazz;
            this.sm = new SerializersManager();
        }

        @Override
        public void write(Collection<O> data, OutputStream stream) {
            PrimitivO o = new PrimitivO(new DataOutputStream(stream), sm);
            for (O datum : data)
                o.writeObject(datum);
        }

        @Override
        public OutputPort<O> read(InputStream stream) {
            final PrimitivI i = new PrimitivI(new DataInputStream(stream), sm);
            return new OutputPort<O>() {
                @Override
                public O take() {
                    return i.readObject(clazz);
                }
            };
        }
    }
}
