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
 * Interface for classes that build seq objects.
 *
 * @author Bolotin Dmitriy (bolotin.dmitriy@gmail.com)
 * @author Poslavsky Stanislav (stvlpos@mail.ru)
 * @author Shugay Mikhail (mikhail.shugay@gmail.com)
 * @see AbstractSeq
 */
public interface SeqBuilder<S extends Seq<S>> {
    /**
     * Size of sequence being created.
     *
     * @return size
     */
    int size();

    /**
     * Ensures capacity of this builder.
     *
     * @param capacity capacity
     * @return this
     */
    SeqBuilder<S> ensureCapacity(int capacity);

    /**
     * Creates the sequence and destroys this builder.
     *
     * @return created sequence
     */
    S createAndDestroy();

    /**
     * Appends seq.
     *
     * @param seq seq
     * @return this
     */
    SeqBuilder<S> append(S seq);

    /**
     * Returns a deep copy of this builder
     *
     * @return a deep copy of this builder
     */
    SeqBuilder<S> clone();
}
