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

public final class PrimitivIO {
    private PrimitivIO() {
    }

    /**
     * Serializer that throws exception for any serialization. Use for known objects.
     */
    public static <T> Serializer<T> dummySerializer() {
        return new Serializer<T>() {
            @Override
            public void write(PrimitivO output, T object) {
                throw new RuntimeException("Dummy serializer.");
            }

            @Override
            public T read(PrimitivI input) {
                throw new RuntimeException("Dummy serializer.");
            }

            @Override
            public boolean isReference() {
                return true;
            }

            @Override
            public boolean handlesReference() {
                return false;
            }
        };
    }
}
