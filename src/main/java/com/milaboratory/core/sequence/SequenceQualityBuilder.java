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
 * Created by poslavsky on 09/07/14.
 */
public final class SequenceQualityBuilder extends ArraySeqBuilder<SequenceQuality, SequenceQualityBuilder> {
    public SequenceQualityBuilder() {
    }

    public SequenceQualityBuilder(byte[] data, int size) {
        super(data, size);
    }

    @Override
    SequenceQuality createUnsafe(byte[] b) {
        return new SequenceQuality(b, true);
    }

    @Override
    byte[] getUnsafe(SequenceQuality sequenceQuality) {
        return sequenceQuality.data;
    }

    @Override
    public SequenceQualityBuilder clone() {
        return new SequenceQualityBuilder(data.clone(), size);
    }
}
