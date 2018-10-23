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
package com.milaboratory.core.alignment;

import com.milaboratory.core.mutations.Mutation;
import com.milaboratory.core.sequence.Sequence;

/**
 * Iterates over all positions in alignment
 *
 * @param <S> sequence type
 */
public interface AlignmentIterator<S extends Sequence<S>> {
    /**
     * Points to current position in sequence1; if on insertion points to the next position after insertion point
     */
    int getSeq1Position();

    /**
     * Points to current position in sequence2; if deletion points to the next position after deletion
     */
    int getSeq2Position();

    /**
     * Points to current mutation or if currentMutation == {@link Mutation#NON_MUTATION} points to the next mutation.
     */
    int getMutationsPointer();

    /**
     * Current mutation or {@link Mutation#NON_MUTATION} if on match
     */
    int getCurrentMutation();

    /**
     * Advance iterator to the next position
     *
     * @return true if success, false if iteration is finished
     */
    boolean advance();
}
