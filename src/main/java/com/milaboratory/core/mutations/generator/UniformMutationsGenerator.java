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
package com.milaboratory.core.mutations.generator;

import com.milaboratory.core.mutations.Mutation;
import com.milaboratory.core.mutations.MutationType;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.sequence.Sequence;
import org.apache.commons.math3.random.RandomGenerator;

public class UniformMutationsGenerator {
    private static final MutationType[] types = MutationType.values();

    public static <S extends Sequence<S>> Mutations<S> createUniformMutationAsObject(S sequence,
                                                                                     RandomGenerator generator) {
        return new Mutations<>(sequence.getAlphabet(), createUniformMutation(sequence, generator));
    }

    public static <S extends Sequence<S>> int createUniformMutation(S sequence, RandomGenerator generator) {
        return createUniformMutation(sequence, generator, types[generator.nextInt(3)]);
    }

    public static <S extends Sequence<S>> Mutations<S> createUniformMutationAsObject(S sequence,
                                                                                     RandomGenerator generator,
                                                                                     MutationType type) {
        return new Mutations<>(sequence.getAlphabet(), createUniformMutation(sequence, generator, type));
    }

    public static <S extends Sequence<S>> int createUniformMutation(S sequence, RandomGenerator generator,
                                                                    MutationType type) {
        if(sequence.containWildcards())
            throw new IllegalArgumentException("Sequences with wildcards are not supported.");

        int position;
        byte from, to;
        int alphabetSize = sequence.getAlphabet().basicSize();
        switch (type) {
            case Substitution:
                position = generator.nextInt(sequence.size());
                from = sequence.codeAt(position);
                to = (byte) ((from + 1 + generator.nextInt(alphabetSize - 1)) % alphabetSize);
                assert from != to;
                return Mutation.createSubstitution(position, from, to);
            case Deletion:
                position = generator.nextInt(sequence.size());
                from = sequence.codeAt(position);
                return Mutation.createDeletion(position, from);
            case Insertion:
                position = generator.nextInt(sequence.size() + 1);
                to = (byte) generator.nextInt(alphabetSize);
                return Mutation.createInsertion(position, to);
        }
        throw new NullPointerException();
    }
}
