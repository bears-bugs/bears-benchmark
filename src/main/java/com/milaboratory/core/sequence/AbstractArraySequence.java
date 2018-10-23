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

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
abstract class AbstractArraySequence<S extends AbstractArraySequence<S>> extends Sequence<S>
        implements java.io.Serializable {
    protected final byte[] data;

    protected AbstractArraySequence(String sequence) {
        this.data = dataFromChars(getAlphabet(), sequence.toCharArray());
    }

    protected AbstractArraySequence(char[] sequence) {
        this.data = dataFromChars(getAlphabet(), sequence);
    }

    protected AbstractArraySequence(byte[] data) {
        this.data = data;
    }

    @Override
    public abstract AbstractArrayAlphabet<S> getAlphabet();

    @Override
    public byte codeAt(int position) {
        return data[position];
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public byte[] asArray() {
        return data.clone();
    }

    @Override
    public S getRange(int from, int to) {
        if (from == 0 && to == data.length)
            return (S) this;
        if (from > to || to > data.length)
            throw new IndexOutOfBoundsException();
        return getAlphabet().createUnsafe(Arrays.copyOfRange(data, from, to));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Arrays.equals(data, ((AbstractArraySequence) o).data);
    }

    @Override
    public final int hashCode() {
        int result = getAlphabet().hashCode();
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    protected static byte[] dataFromChars(Alphabet alphabet, char[] chars) {
        byte[] data = new byte[chars.length];
        for (int i = 0; i < chars.length; ++i)
            if ((data[i] = alphabet.symbolToCode(chars[i])) == -1)
                throw new IllegalArgumentException("Unknown symbol \"" + chars[i] + "\"");
        return data;
    }
}
