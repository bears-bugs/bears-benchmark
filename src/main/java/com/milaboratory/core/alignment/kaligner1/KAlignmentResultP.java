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
package com.milaboratory.core.alignment.kaligner1;

import com.milaboratory.core.alignment.batch.PipedAlignmentResult;
import com.milaboratory.core.sequence.NucleotideSequence;

/**
 * Piped version of {@link KAlignmentResult}.
 */
public final class KAlignmentResultP<P, Q> extends KAlignmentResult<P>
        implements PipedAlignmentResult<KAlignmentHit<P>, Q> {
    private final Q query;

    public KAlignmentResultP(Q query, KAligner<P> aligner, KMappingResult mappingResult,
                             NucleotideSequence target, int targetFrom, int targetTo) {
        super(aligner, mappingResult, target, targetFrom, targetTo);
        this.query = query;
        calculateAllAlignments();
    }

    @Override
    public Q getQuery() {
        return query;
    }
}
