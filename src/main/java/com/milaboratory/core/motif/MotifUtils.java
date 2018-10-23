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

import com.milaboratory.core.sequence.Sequence;

public class MotifUtils {
    public static <S extends Sequence<S>> Motif<S> twoSequenceMotif(S seq1, int offset1,
                                                                    S seq2, int offset2,
                                                                    int length) {
        if (seq1 == null || seq2 == null)
            throw new NullPointerException();

        if (offset1 < 0 || offset2 < 0 ||
                seq1.size() < offset1 + length ||
                seq2.size() < offset2 + length)
            throw new IllegalArgumentException();

        MotifBuilder<S> builder = new MotifBuilder<>(seq1.getAlphabet(), length);

        for (int i = 0; i < length; ++i) {
            builder.setAllowedLetter(i, seq1.codeAt(offset1 + i));
            builder.setAllowedLetter(i, seq2.codeAt(offset2 + i));
        }

        return builder.createAndDestroy();
    }
}
