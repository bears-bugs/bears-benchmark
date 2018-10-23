/*
 * Copyright 2016 MiLaboratory.com
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
import com.milaboratory.util.IntArrayList;
import gnu.trove.impl.Constants;
import gnu.trove.iterator.TIntLongIterator;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.custom_hash.TObjectIntCustomHashMap;
import gnu.trove.map.hash.TIntLongHashMap;
import gnu.trove.strategy.HashingStrategy;

import java.util.Arrays;

public final class MutationsCounter {
    /**
     * Main counter
     */
    final TIntLongHashMap counter = new TIntLongHashMap();
    /**
     * Mapping between long inserts and their ids in counter map.
     */
    TObjectIntCustomHashMap<int[]> insertMapping = null;

    public MutationsCounter() {
    }

    public void adjust(Mutations<?> mutations, int delta) {
        MutationsEnumerator enumerator = new MutationsEnumerator(mutations);
        while (enumerator.next())
            adjust(mutations, enumerator, delta);
    }

    public void adjust(Mutations<?> mutations, MutationsEnumerator enumerator, int delta) {
        adjust(mutations.mutations, enumerator.getOffset(), enumerator.getLength(), delta);
    }

    void adjust(int[] mutationsArray, int offset, int length, int delta) {
        assert length != 0;
        if (length == 1)
            adjustSingleMutation(mutationsArray[offset], delta);
        else
            adjustLongInsert(Arrays.copyOfRange(mutationsArray, offset, offset + length),
                    delta);
    }

    public <S extends Sequence<S>> Mutations<S> build(Alphabet<S> alphabet, Filter filter) {
        IntArrayList mutations = new IntArrayList();

        TIntLongIterator it = counter.iterator();
        while (it.hasNext()) {
            it.advance();
            int mutation = it.key();
            long count = it.value();
            if ((mutation & Mutation.MUTATION_TYPE_MASK) != 0
                    && filter.accept(count, Mutation.getPosition(mutation), mutation, null))
                mutations.add(mutation);
        }

        if (insertMapping != null) {
            TObjectIntIterator<int[]> itO = insertMapping.iterator();
            while (itO.hasNext()) {
                itO.advance();
                int[] muts = itO.key();
                long count = counter.get(itO.value());
                if (filter.accept(count, Mutation.getPosition(muts[0]), Mutation.NON_MUTATION, muts))
                    mutations.addAll(muts);
            }
        }
        mutations.stableSort(Mutation.POSITION_COMPARATOR);

        return new Mutations<>(alphabet, mutations);
    }

    private void adjustSingleMutation(int mutation, int delta) {
        counter.adjustOrPutValue(mutation, delta, delta);
    }

    private void adjustLongInsert(int[] insert, int delta) {
        if (insertMapping == null)
            insertMapping = new TObjectIntCustomHashMap<>(new IntArrayHashingStrategy(),
                    Constants.DEFAULT_CAPACITY,
                    Constants.DEFAULT_LOAD_FACTOR, Mutation.NON_MUTATION);
        int next = nextId();
        int mutCode = insertMapping.putIfAbsent(insert, next);
        if (mutCode == Mutation.NON_MUTATION)
            mutCode = next;
        counter.adjustOrPutValue(mutCode, delta, delta);
    }

    private int nextId() {
        // MUTATION_TYPE = 0
        // TO_LETTER = 0
        return (insertMapping.size() + 1) << Mutation.FROM_OFFSET;
    }

    private static final class IntArrayHashingStrategy implements HashingStrategy<int[]> {
        @Override
        public int computeHashCode(int[] object) {
            return Arrays.hashCode(object);
        }

        @Override
        public boolean equals(int[] o1, int[] o2) {
            return Arrays.equals(o1, o2);
        }
    }

    public interface Filter {
        boolean accept(long count, int position, int mutation, int[] mutations);
    }
}
