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
 * Interface for factory class for a certain type of sequence.
 *
 * @author Bolotin Dmitriy (bolotin.dmitriy@gmail.com)
 * @author Shugay Mikhail (mikhail.shugay@gmail.com)
 * @see com.milaboratory.core.sequence.Sequence
 */
public interface SequenceBuilder<S extends Sequence<S>> extends SeqBuilder<S> {
    /**
     * Sets letter at the specified position.
     *
     * @param position position {@code positin >=0 && positin < size()}
     * @param letter   letter
     * @return this
     */
    SequenceBuilder<S> set(int position, byte letter);

    /**
     * Appends letter.
     *
     * @param letter letter
     * @return this
     */
    SequenceBuilder<S> append(byte letter);

    /**
     * Appends letters array.
     *
     * @param letters array of letters
     * @return this
     */
    SequenceBuilder<S> append(byte[] letters);

    @Override
    SequenceBuilder<S> append(S seq);

    @Override
    SequenceBuilder<S> ensureCapacity(int capacity);

    @Override
    SequenceBuilder<S> clone();

    @Override
    S createAndDestroy();
}
