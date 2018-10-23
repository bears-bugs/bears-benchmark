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
 * Created by poslavsky on 10/07/14.
 */
public class NSequenceWithQualityBuilder implements SeqBuilder<NSequenceWithQuality> {
    final SequenceBuilder<NucleotideSequence> sBuilder;
    final SequenceQualityBuilder qBuilder;

    public NSequenceWithQualityBuilder() {
        this(NucleotideSequence.ALPHABET.createBuilder(), new SequenceQualityBuilder());
    }

    NSequenceWithQualityBuilder(SequenceBuilder<NucleotideSequence> sBuilder, SequenceQualityBuilder qBuilder) {
        this.sBuilder = sBuilder;
        this.qBuilder = qBuilder;
    }

    @Override
    public int size() {
        return sBuilder.size();
    }

    @Override
    public NSequenceWithQualityBuilder ensureCapacity(int capacity) {
        sBuilder.ensureCapacity(capacity);
        qBuilder.ensureCapacity(capacity);
        return this;
    }

    @Override
    public NSequenceWithQuality createAndDestroy() {
        return new NSequenceWithQuality(sBuilder.createAndDestroy(), qBuilder.createAndDestroy());
    }

    @Override
    public NSequenceWithQualityBuilder append(NSequenceWithQuality seq) {
        sBuilder.append(seq.sequence);
        qBuilder.append(seq.quality);
        return this;
    }

    @Override
    public NSequenceWithQualityBuilder clone() {
        return new NSequenceWithQualityBuilder(sBuilder.clone(), qBuilder.clone());
    }
}
