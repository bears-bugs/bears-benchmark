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

import com.milaboratory.util.HashFunctions;

/**
 * Don't use this class.
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class UnsafeFactory {
    private UnsafeFactory() {
    }

    public static NSequenceWithQuality fastqParse(
            byte[] buffer,
            int fromSequence,
            int fromQuality,
            int length,
            byte qualityValueOffset,
            long id,
            boolean replaceWildcards) {
        // Seed for random generator of letters substituting wildcards
        long seed = id;

        // Creating builders for sequence and quality
        SequenceBuilder<NucleotideSequence> sequence = NucleotideSequence.ALPHABET.createBuilder().ensureCapacity(length);
        SequenceQualityBuilder quality = new SequenceQualityBuilder().ensureCapacity(length);

        byte qual, code;
        int pointerSeq = fromSequence, pointerQua = fromQuality;

        // Parsing quality and sequence
        for (int i = 0; i < length; ++i) {
            qual = (byte) (buffer[pointerQua++] - qualityValueOffset);

            code = NucleotideAlphabet.byteSymbolToCode(buffer[pointerSeq++]);

            if (code == -1) {
                if (buffer[pointerSeq - 1] == '.')
                    code = NucleotideAlphabet.N;
                else
                    throw new IllegalArgumentException("Unknown letter \"" + buffer[pointerSeq - 1] + "\"" +
                            (buffer[pointerSeq - 1] == 13 ? ". FASTQ reader does not support Windows-style line breaks " +
                                    "(CR+LF), please convert file to standard FASTQ (with Unix-like LF line breaks)." : ""));
            }

            if (replaceWildcards && NucleotideSequence.ALPHABET.isWildcard(code)) {
                seed = HashFunctions.JenkinWang64shift(seed + i);
                code = NucleotideSequence.ALPHABET.codeToWildcard(code).getUniformlyDistributedBasicCode(seed);
                qual = 0;
            }

            sequence.append(code);
            quality.append(qual);
        }

        // Returning result
        return new NSequenceWithQuality(sequence.createAndDestroy(),
                quality.createAndDestroy());
    }
}
