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

import com.milaboratory.core.alignment.batch.AlignmentResult;
import com.milaboratory.core.sequence.NucleotideSequence;

import java.util.*;

/**
 * KAlignmentResult - class which is result of {@link KAligner#align(com.milaboratory.core.sequence.NucleotideSequence)},
 * {@link KAligner#align(com.milaboratory.core.sequence.NucleotideSequence,
 * int, int)}, {@link KAligner#align(com.milaboratory.core.sequence.NucleotideSequence,
 * int, int, boolean)} methods.
 *
 * <p> It contains link to according {@link KMappingResult}, list of hits found
 * in target sequence as well as range of target sequence to be aligned. </p>
 */
public class KAlignmentResult<P> implements AlignmentResult<KAlignmentHit<P>>,
        Iterable<KAlignmentHit<P>>, java.io.Serializable {
    /**
     * Custom comparator which sorts hits according to {@link KAlignmentHit}
     * alignment scores
     */
    private final static Comparator<KAlignmentHit> HIT_COMPARATOR = new Comparator<KAlignmentHit>() {
        @Override
        public int compare(KAlignmentHit o1, KAlignmentHit o2) {
            return Float.compare(o2.getAlignment().getScore(),
                    o1.getAlignment().getScore());
        }
    };

    /**
     * Custom comparator which sorts hits according to {@link KMappingHit}
     * alignment scores
     */
    private final static Comparator<KAlignmentHit> HIT_COMPARATOR_MAPPER = new Comparator<KAlignmentHit>() {
        @Override
        public int compare(KAlignmentHit o1, KAlignmentHit o2) {
            return Float.compare(o2.getKMersHit().score,
                    o1.getKMersHit().score);
        }
    };
    /**
     * Link to {@link KAligner}
     */
    final KAligner<P> aligner;
    /**
     * Link to according {@link KMappingResult}
     */
    final KMappingResult mappingResult;
    /**
     * List of hits
     */
    final List<KAlignmentHit<P>> hits;
    /**
     * Target sequence to be aligned
     */
    final NucleotideSequence target;
    /**
     * Range of target sequence to be aligned
     */
    final int targetFrom, targetTo;

    /**
     * Creates new KAlignmentResult
     *
     * @param aligner       link to aligner
     * @param mappingResult link to according mapping result (which is result of {@link com.milaboratory.core.sequence.alignment.KMapper#align(com.milaboratory.core.sequence.NucleotideSequence)},
     *                      {@link KMapper#align(com.milaboratory.core.sequence.NucleotideSequence,
     *                      int, int)} methods)
     * @param target        target sequence to be aligned
     * @param targetFrom    position of first nucleotide of target sequence to be aligned
     * @param targetTo      position of last nucleotide of target sequence to be aligned
     */
    public KAlignmentResult(KAligner<P> aligner, KMappingResult mappingResult, NucleotideSequence target,
                            int targetFrom, int targetTo) {
        this.aligner = aligner;
        this.mappingResult = mappingResult;
        this.target = target;
        this.targetFrom = targetFrom;
        this.targetTo = targetTo;
        this.hits = new ArrayList<>(mappingResult.hits.size());
        for (int i = 0; i < mappingResult.hits.size(); ++i)
            this.hits.add(new KAlignmentHit<>(this, i));
    }

    /**
     * Returns link to aligner
     *
     * @return aligner
     */
    public KAligner getAligner() {
        return aligner;
    }

    /**
     * Returns mapping result
     *
     * @return
     */
    public KMappingResult getMappingResult() {
        return mappingResult;
    }

    /**
     * Checks if there are hits
     *
     * @return {@code true} if {@link #hits} array is not empty
     */
    public boolean hasHits() {
        return !hits.isEmpty();
    }

    /**
     * Returns best hit (hit with highest alignment or mapper (if lazy alignment is used) score) or null if there is no
     * hits in this result
     *
     * @return best hit (hit with highest alignment or mapper (if lazy alignment is used) score) or null if there is no
     * hits in this result
     */
    @Override
    public KAlignmentHit getBestHit() {
        if (hits.isEmpty())
            return null;

        return hits.get(0);
    }

    /**
     * Returns list of hits found in target sequence
     *
     * @return list of hits found in target sequence
     */
    public List<KAlignmentHit<P>> getHits() {
        return hits;
    }

    /**
     * Returns target sequence
     *
     * @return target sequence
     */
    public NucleotideSequence getTarget() {
        return target;
    }

    /**
     * Returns position of first nucleotide of target sequence to be aligned
     *
     * @return position of first nucleotide of target sequence to be aligned
     */
    public int getTargetFrom() {
        return targetFrom;
    }

    /**
     * Returns position of last nucleotide of target sequence to be aligned
     *
     * @return position of last nucleotide of target sequence to be aligned
     */
    public int getTargetTo() {
        return targetTo;
    }

    /**
     * Calculates alignments for all hits
     */
    private void _calculateAllAlignments() {
        for (KAlignmentHit hit : hits)
            hit.calculateAlignment();
    }

    /**
     * Sorts hits according to {@link KMappingHit} alignment score
     */
    public void sortAccordingToMapperScores() {
        Collections.sort(hits, HIT_COMPARATOR_MAPPER);
    }

    /**
     * Calculates alignments for all hits
     */
    public void calculateAllAlignments() {
        _calculateAllAlignments();
        refresh();
    }

    /**
     * Refresh alignments of all hits
     */
    public void refresh() {
        KAlignerParameters params = aligner.parameters;
        sortHits(params.getAbsoluteMinScore(), params.getRelativeMinScore(), params.getMaxHits());
    }

    /**
     * Possible only after calculation of all individual alignments.
     *
     * @param minScore  hits having score less than this value will be removed
     * @param tolerance hits having score less than @code{tolerance*topScore} will be removed (typical value is 0.8)
     * @param maxHits   maximal number of hits
     */
    private void sortHits(double minScore, double tolerance, int maxHits) {
        Collections.sort(hits, HIT_COMPARATOR);
        if (hits.isEmpty())
            return;
        minScore = Math.max(minScore, hits.get(0).getAlignment().getScore() * tolerance);

        if (hits.size() > maxHits)
            minScore = Math.max(minScore, hits.get(maxHits - 1).getAlignment().getScore());

        for (int i = hits.size() - 1; i >= 0; --i)
            if (hits.get(i).getAlignment().getScore() < minScore)
                hits.remove(i);
            else
                break;
    }

    @Override
    public Iterator<KAlignmentHit<P>> iterator() {
        return hits.iterator();
    }
}
