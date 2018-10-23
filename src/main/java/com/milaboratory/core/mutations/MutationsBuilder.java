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
package com.milaboratory.core.mutations;

import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.util.ArraysUtils;

import java.util.Arrays;

import static com.milaboratory.core.mutations.Mutation.*;

public final class MutationsBuilder<S extends Sequence<S>> {
    private final Alphabet<S> alphabet;
    private final boolean reversed;
    private int[] mutations = null;
    private int size = 0;

    public MutationsBuilder(Alphabet<S> alphabet, boolean reversed, int[] mutations, int size) {
        this.alphabet = alphabet;
        this.reversed = reversed;
        this.mutations = mutations;
        this.size = size;
    }

    public MutationsBuilder(Alphabet<S> alphabet) {
        this(alphabet, false);
    }

    public MutationsBuilder(Alphabet<S> alphabet, boolean reversed) {
        this.alphabet = alphabet;
        this.reversed = reversed;
    }

    public int get(int index) {
        return mutations[index];
    }

    public int size() {
        return size;
    }

    public void set(int index, int mutation) {
        mutations[index] = mutation;
    }

    public int getLast() {
        return mutations[size - 1];
    }

    public void removeLast() {
        --size;
    }

    private void ensureInternalCapacity(int newSize) {
        if (size == -1)
            throw new IllegalStateException("Destroyed.");
        if (mutations != null && mutations.length >= newSize)
            return;
        if (newSize == 0) {
            assert mutations == null;
            return;
        }
        if (mutations == null)
            mutations = new int[Math.max(newSize, 10)];
        if (mutations.length < newSize)
            mutations = Arrays.copyOf(mutations, Math.max(newSize, 3 * mutations.length / 2 + 1));
    }

    public MutationsBuilder<S> ensureCapacity(int capacity) {
        if (size == -1)
            throw new IllegalStateException("Destroyed.");
        if (capacity > 0) {
            if (mutations == null)
                mutations = new int[capacity];
            else if (capacity > mutations.length)
                mutations = Arrays.copyOf(mutations, capacity);
        }
        return this;
    }

    public Mutations<S> createAndDestroy() {
        final int[] m;

        if (mutations == null)
            m = new int[0];
        else if (mutations.length == size)
            m = mutations;
        else
            m = Arrays.copyOf(mutations, size);

        mutations = null;
        size = -1;

        if (reversed)
            ArraysUtils.reverse(m);

        if (m.length > 1)
            for (int i = 1; i < m.length; ++i)
                if (getPosition(m[i - 1]) > getPosition(m[i]))
                    throw new IllegalArgumentException("Mutations must be appended in descending/ascending order (position) " +
                            "depending on the value of reverse flag. Problem " + Mutation.encode(m[i - 1], alphabet) + ":"
                            + Mutation.encode(m[i], alphabet) + " in " + MutationsUtil.encode(m, alphabet) + ".");

        return new Mutations<>(alphabet, m, true);
    }

    public MutationsBuilder<S> append(Mutations<S> other) {
        append(other, 0, other.size());
        return this;
    }

    public MutationsBuilder<S> append(MutationsBuilder<S> other) {
        if (other.size == 0)
            return this;
        ensureInternalCapacity(size + other.size);
        System.arraycopy(other.mutations, 0, mutations, size, other.size);
        size += other.size;
        return this;
    }

    public MutationsBuilder<S> append(Mutations<S> other, int otherFrom, int length) {
        ensureInternalCapacity(size + length);
        if (length != 0)
            System.arraycopy(other.mutations, otherFrom, mutations, size, length);
        size += length;
        return this;
    }

    public MutationsBuilder<S> append(int[] other) {
        ensureInternalCapacity(size + other.length);
        for (int mutation : other)
            mutations[size++] = mutation;
        return this;
    }

    public MutationsBuilder<S> append(int mutation) {
        ensureInternalCapacity(size + 1);
        mutations[size++] = mutation;
        return this;
    }

    public MutationsBuilder<S> appendSubstitution(int position, int from, int to) {
        return append(createSubstitution(position, from, to));
    }

    public MutationsBuilder<S> appendDeletion(int position, int from) {
        return append(createDeletion(position, from));
    }

    public MutationsBuilder<S> appendInsertion(int position, int to) {
        return append(createInsertion(position, to));
    }

    public MutationsBuilder<S> appendInsertion(int position, S insert) {
        ensureCapacity(size + insert.size());
        for (int i = 0; i < insert.size(); i++)
            append(createInsertion(position, insert.codeAt(i)));
        return this;
    }

    public MutationsBuilder<S> appendDeletion(int position, int length, S originalSequence) {
        ensureCapacity(size + length);
        for (int i = 0; i < length; i++)
            append(createDeletion(position + i, originalSequence.codeAt(position + i)));
        return this;
    }

    public MutationsBuilder<S> reverseRange(int from, int to) {
        ArraysUtils.reverse(mutations, from, to);
        return this;
    }

    public MutationsBuilder<S> clone() {
        return new MutationsBuilder<>(alphabet,
                reversed,
                mutations == null ? null : mutations.clone(),
                size);
    }

    @Override
    public String toString() {
        return this.clone().createAndDestroy().toString();
    }
}
