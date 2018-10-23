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
package com.milaboratory.core.alignment;

/**
 * CachedIntArray - class which is used for storing alignment matrix.
 */
public final class CachedIntArray implements java.io.Serializable {
    private int[] array = null;

    /**
     * Returns {@code int[]} array. If passed {@code #size} argument is more than actual size of CachedIntArray, then
     * CachedIntArray will increase its size to {@code size}.
     *
     * @param size needed sie
     * @return array
     */
    public int[] get(int size) {
        if (array == null || size > array.length)
            return array = new int[size];

        return array;
    }
}
