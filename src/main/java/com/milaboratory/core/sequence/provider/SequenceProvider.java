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
package com.milaboratory.core.sequence.provider;

import com.milaboratory.core.Range;
import com.milaboratory.core.sequence.Sequence;

/**
 * Provides access to the sequence that may be too big to be loaded to the memory. Such sequence may be stored remotely
 * or in the file system.
 *
 * @param <S> type of the sequence
 */
public interface SequenceProvider<S extends Sequence<S>> {
    /**
     * Returns the length of the sequence this object represents.
     *
     * @return length of the sequence this object represents
     */
    int size();

    /**
     * Retrieves specified region of the sequence.
     *
     * @param range range of the sequence to be retrieved
     * @return sequence
     */
    S getRegion(Range range);
}
