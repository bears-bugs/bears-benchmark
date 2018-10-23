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
package com.milaboratory.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.atomic.AtomicLongArray;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class AtomicEnumHistogram<E extends Enum<E>> {
    private final Class<E> enumClass;
    private final AtomicLongArray hist;

    public AtomicEnumHistogram(Class<E> enumClass) {
        this.enumClass = enumClass;
        this.hist = new AtomicLongArray(enumClass.getEnumConstants().length + 1);
    }

    public void add(E value) {
        if (value == null)
            hist.incrementAndGet(hist.length() - 1);
        else
            hist.incrementAndGet(value.ordinal());
    }

    public String[] getLabels() {
        String[] labels = new String[enumClass.getEnumConstants().length + 1];
        int i = 0;
        for (E e : enumClass.getEnumConstants())
            labels[i++] = e.name();
        labels[i] = "null";
        return labels;
    }

    public long[] getHist() {
        long[] result = new long[hist.length()];
        for (int i = 0; i < result.length; i++)
            result[i] = hist.get(i);
        return result;
    }

    @JsonUnwrapped
    @JsonValue
    public SerializableResult getSerializableResult() {
        return new SerializableResult(getLabels(), getHist());
    }

    @Override
    public String toString() {
        try {
            return GlobalObjectMappers.toOneLine(getSerializableResult());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static final class SerializableResult {
        public final String[] labels;
        public final long[] hist;

        public SerializableResult(String[] labels, long[] hist) {
            this.labels = labels;
            this.hist = hist;
        }
    }
}
