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
package com.milaboratory.core.sequence;

import java.util.Arrays;

public abstract class ArraySeqBuilder<S extends AbstractSeq<S>, B extends ArraySeqBuilder<S, B>> implements SeqBuilder<S> {
    byte[] data;
    int size = 0;

    ArraySeqBuilder() {
    }

    ArraySeqBuilder(byte[] data, int size) {
        this.data = data;
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    protected void ensureInternalCapacity(int newSize) {
        if (size == -1)
            throw new IllegalStateException("Destroyed.");
        if (data == null)
            if (newSize != 0)
                data = new byte[Math.max(newSize, 10)];
            else
                return;
        if (data.length < newSize)
            data = Arrays.copyOf(data, Math.max(newSize, 3 * data.length / 2 + 1));
    }

    @Override
    public B ensureCapacity(int capacity) {
        if (size == -1)
            throw new IllegalStateException("Destroyed.");
        if (capacity > 0) {
            if (data == null)
                data = new byte[capacity];
            else if (capacity > data.length)
                data = Arrays.copyOf(data, capacity);
        }
        return (B) this;
    }

    abstract S createUnsafe(byte[] b);

    abstract byte[] getUnsafe(S s);

    @Override
    public S createAndDestroy() {
        S seq;

        if (data == null)
            return createUnsafe(new byte[0]);

        if (data.length == size)
            seq = createUnsafe(data);
        else
            seq = createUnsafe(Arrays.copyOf(data, size));
        data = null;
        size = -1;
        return seq;
    }

    public B append(byte value) {
        ensureCapacity(size + 1);
        data[size++] = value;
        return (B) this;
    }

    @Override
    public B append(S seq) {
        if (seq.size() == 0)
            return (B) this;

        ensureInternalCapacity(size + seq.size());
        System.arraycopy(getUnsafe(seq), 0, data, size, seq.size());
        size += seq.size();
        return (B) this;
    }

    @Override
    public abstract B clone();
}
