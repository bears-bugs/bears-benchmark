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
package com.milaboratory.core.motif;

import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.core.sequence.Wildcard;
import com.milaboratory.util.BitArray;

public final class MotifBuilder<S extends Sequence<S>> {
    private final Alphabet<S> alphabet;
    private final int size;
    BitArray data;

    public MotifBuilder(Alphabet<S> alphabet, int size) {
        this.alphabet = alphabet;
        this.size = size;
        this.data = new BitArray(alphabet.size() * size);
    }

    public void setAllowedLetter(int position, byte letter) {
        if (letter > alphabet.basicSize())
            throw new IllegalArgumentException();
        Wildcard wc = alphabet.codeToWildcard(letter);
        for (int i = 0; i < wc.size(); i++)
            data.set(wc.getMatchingCode(i) * size + position);
    }

    public Motif<S> createAndDestroy() {
        BitArray d = data;
        data = null;
        return new Motif<>(alphabet, size, d);
    }
}
