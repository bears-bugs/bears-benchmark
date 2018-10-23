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
package com.milaboratory.core.alignment.batch;

import com.milaboratory.core.alignment.Aligner;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.sequence.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simplest implementation of {@link BatchAligner}, which aligns target sequence with all subjects using classical (low
 * performance) alignment algorithms.
 *
 * @param <S> type of sequence
 * @param <P> type of record payload, used to store additional information along with sequence to simplify it's
 *            subsequent identification in result (e.g. {@link Integer} to just index sequences.
 */
public class SimpleBatchAligner<S extends Sequence<S>, P> extends AbstractBatchAligner<S, AlignmentHit<S, P>>
        implements BatchAlignerWithBase<S, P, AlignmentHit<S, P>> {
    final SimpleBatchAlignerParameters<S> parameters;
    final List<Record<S, P>> references = new ArrayList<>();

    public SimpleBatchAligner(SimpleBatchAlignerParameters<S> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void addReference(S sequence, P payload) {
        references.add(new Record<>(sequence, payload));
    }

    @Override
    public AlignmentResult<AlignmentHit<S, P>> align(S sequence, int from, int to) {
        throw new UnsupportedOperationException();
    }

    public AlignmentResult<AlignmentHit<S, P>> align(final S sequence) {
        // Special case
        if (references.isEmpty())
            return new AlignmentResultImpl<>();

        // Building all alignments
        ArrayList<AlignmentHit<S, P>> alignments = new ArrayList<>(references.size());
        for (Record<S, P> record : references)
            alignments.add(alignSingle(record, sequence));

        // Sorting alignments by score
        Collections.sort(alignments, BatchAlignmentUtil.ALIGNMENT_SCORE_HIT_COMPARATOR);

        // Calculating top score and score cutoff
        float topScore = alignments.get(0).getAlignment().getScore();
        float scoreThreshold = Math.max(topScore * parameters.getRelativeMinScore(), parameters.getAbsoluteMinScore());

        // Slicing results according to cutoff
        for (int i = 0; i < alignments.size(); i++)
            if (i == parameters.getMaxHits() || alignments.get(i).getAlignment().getScore() < scoreThreshold)
                return new AlignmentResultImpl<>(new ArrayList<>(alignments.subList(0, i)));

        return new AlignmentResultImpl<>(alignments);
    }

    AlignmentHit<S, P> alignSingle(Record<S, P> record, S query) {
        Alignment<S> alignment = parameters.isGlobal() ?
                Aligner.alignGlobal(parameters.getScoring(), record.sequence, query) :
                Aligner.alignLocal(parameters.getScoring(), record.sequence, query);
        return new AlignmentHitImpl<>(alignment, record.payload);
    }

    private static class Record<S extends Sequence<S>, P> {
        final S sequence;
        final P payload;

        public Record(S sequence, P payload) {
            this.sequence = sequence;
            this.payload = payload;
        }

        public S getSequence() {
            return sequence;
        }

        public P getPayload() {
            return payload;
        }
    }
}
