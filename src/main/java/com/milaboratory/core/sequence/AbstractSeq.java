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

import com.milaboratory.core.Range;

public abstract class AbstractSeq<S extends AbstractSeq<S>> implements Seq<S> {
    @Override
    public S getRange(Range range) {
        if (range.isReverse())
            throw new IllegalArgumentException("Reverse range not supported.");
        return getRange(range.getFrom(), range.getTo());
    }

    @Override
    public S concatenate(S other) {
        return getBuilder()
                .ensureCapacity(other.size() + size())
                .append((S) this).append(other).createAndDestroy();
    }
}
