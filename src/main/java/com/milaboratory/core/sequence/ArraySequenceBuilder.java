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

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
final class ArraySequenceBuilder<S extends AbstractArraySequence<S>> extends ArraySeqBuilder<S, ArraySequenceBuilder<S>> implements SequenceBuilder<S> {
    private final AbstractArrayAlphabet<S> alphabet;

    ArraySequenceBuilder(AbstractArrayAlphabet<S> alphabet) {
        this.alphabet = alphabet;
    }

    ArraySequenceBuilder(byte[] data, int size, AbstractArrayAlphabet<S> alphabet) {
        super(data, size);
        this.alphabet = alphabet;
    }

    @Override
    S createUnsafe(byte[] b) {
        return alphabet.createUnsafe(b);
    }

    @Override
    byte[] getUnsafe(S s) {
        return s.data;
    }

    @Override
    public ArraySequenceBuilder<S> set(int position, byte letter) {
        if (position < 0 || position >= size)
            throw new IndexOutOfBoundsException();
        data[position] = letter;
        return this;
    }

    @Override
    public ArraySequenceBuilder<S> append(byte letter) {
        ensureInternalCapacity(size + 1);
        data[size++] = letter;
        return this;
    }

    @Override
    public ArraySequenceBuilder<S> clone() {
        return new ArraySequenceBuilder<>(data == null ? null : data.clone(), size, alphabet);
    }

    @Override
    public SequenceBuilder<S> append(byte[] letters) {
        if (letters.length == 0)
            return this;
        ensureInternalCapacity(size + letters.length);
        System.arraycopy(letters, 0, data, size, letters.length);
        size += letters.length;
        return this;
    }

    @Override
    public ArraySequenceBuilder<S> ensureCapacity(int capacity) {
        super.ensureCapacity(capacity);
        return this;
    }

    @Override
    public String toString() {
        return clone().createAndDestroy().toString();
    }
}
