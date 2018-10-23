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

import com.milaboratory.core.alignment.batch.AlignmentResult;
import com.milaboratory.core.alignment.kaligner1.KMappingResult;
import com.milaboratory.core.sequence.NucleotideSequence;

import java.util.List;

/**
 * Created by dbolotin on 26/10/15.
 */
public final class KAlignmentResult2<P> implements AlignmentResult<KAlignmentHit2<P>> {
    /**
     * Link to according {@link KMappingResult}
     */
    final KMappingResult2 mappingResult;
    /**
     * List of hits
     */
    final List<KAlignmentHit2<P>> hits;
    /**
     * Target sequence to be aligned
     */
    final NucleotideSequence target;
    /**
     * Range of target sequence to be aligned
     */
    final int targetFrom, targetTo;

    public KAlignmentResult2(KMappingResult2 mappingResult, List<KAlignmentHit2<P>> hits,
                             NucleotideSequence target, int targetFrom, int targetTo) {
        this.mappingResult = mappingResult;
        this.hits = hits;
        this.target = target;
        this.targetFrom = targetFrom;
        this.targetTo = targetTo;
    }

    @Override
    public List<KAlignmentHit2<P>> getHits() {
        return hits;
    }

    @Override
    public final KAlignmentHit2<P> getBestHit() {
        return hits.isEmpty() ? null : hits.get(0);
    }

    @Override
    public boolean hasHits() {
        return !hits.isEmpty();
    }
}
