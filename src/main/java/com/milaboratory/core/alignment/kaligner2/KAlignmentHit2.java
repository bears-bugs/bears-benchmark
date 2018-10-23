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
package com.milaboratory.core.alignment.kaligner2;

import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.alignment.batch.AlignmentHit;
import com.milaboratory.core.sequence.NucleotideSequence;

public final class KAlignmentHit2<P> implements AlignmentHit<NucleotideSequence, P> {
    final KAlignmentResult2<P> result;
    final int indexOfMappingHit;
    final Alignment<NucleotideSequence> alignment;
    final P payload;

    public KAlignmentHit2(KAlignmentResult2<P> result, int indexOfMappingHit,
                          Alignment<NucleotideSequence> alignment, P payload) {
        this.result = result;
        this.indexOfMappingHit = indexOfMappingHit;
        this.alignment = alignment;
        this.payload = payload;
    }

    public int getTargetId() {
        return result.mappingResult.hits.get(indexOfMappingHit).id;
    }

    public KMappingHit2 getMappingHit() {
        return result.mappingResult.hits.get(indexOfMappingHit);
    }

    @Override
    public Alignment<NucleotideSequence> getAlignment() {
        return alignment;
    }

    @Override
    public P getRecordPayload() {
        return payload;
    }
}
