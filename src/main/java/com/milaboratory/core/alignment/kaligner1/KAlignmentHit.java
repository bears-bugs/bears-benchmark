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

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.*;
import com.milaboratory.core.alignment.batch.AlignmentHit;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.MutationsBuilder;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.IntArrayList;

import java.util.Arrays;

/**
 * KAlignmentHit - class which represents single hit for {@link KAlignmentResult}
 */
public final class KAlignmentHit<P> implements java.io.Serializable, AlignmentHit<NucleotideSequence, P> {
    /*         Initially set      */
    /**
     * Link to result container
     */
    private final KAlignmentResult<P> result;
    /**
     * According hit index in {@link KMappingResult#hits} array
     */
    private final int index;

    /*         Set after alignment is build      */
    /**
     * Actual alignment
     */
    private Alignment<NucleotideSequence> alignment = null;

    /**
     * Creates new KAlignmentHit
     *
     * @param result
     * @param index
     */
    public KAlignmentHit(KAlignmentResult<P> result, int index) {
        this.result = result;
        this.index = index;
    }

    @Override
    public P getRecordPayload() {
        return result.aligner.payloads.get(getId());
    }

    public int getId() {
        return result.mappingResult.hits.get(index).id;
    }

    public void calculateAlignment() {
        final CachedIntArray array = AlignmentCache.get();

        try {
            KMappingHit hit = result.mappingResult.hits.get(index);
            IntArrayList seeds = result.mappingResult.seeds;
            KAligner aligner = result.aligner;
            KAlignerParameters parameters = aligner.parameters;

            NucleotideSequence reference = aligner.getReference(hit.id),
                    target = result.target;
            int targetFrom = result.targetFrom, targetTo = result.targetTo;

            MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);

            int maxIndels = parameters.getMaxAdjacentIndels();
            int kValue = parameters.getMapperKValue();
            int gRefFrom = -1, gRefTo = -1, gSeqFrom = -1, gSeqTo = -1;
            int previousOffset = Integer.MIN_VALUE, previousSeedPosition = Integer.MIN_VALUE, currentOffset, currentSeedPosition;
            int refFrom, refLength, seqFrom, seqLength, refAdded, seqAdded;
            int i = 0;
            BandedSemiLocalResult br;

            for (; i < hit.seedOffsets.length; ++i)
                if ((currentOffset = hit.seedOffsets[i]) != KMapper.SEED_NOT_FOUND_OFFSET)
                    if (previousOffset == Integer.MIN_VALUE) {
                        previousOffset = currentOffset;
                        previousSeedPosition = seeds.get(hit.from + i);
                        gSeqFrom = previousSeedPosition;
                        gSeqTo = gSeqFrom + kValue;
                        gRefFrom = gSeqFrom - previousOffset;
                        gRefTo = gSeqTo - previousOffset;
                        ++i;
                        break;
                    }

            //Left edge alignment

            seqFrom = previousOffset - maxIndels;
            seqLength = previousSeedPosition - seqFrom;
            seqAdded = maxIndels * 2 + 1;

            refFrom = targetFrom - previousOffset - maxIndels;
            refLength = previousSeedPosition - previousOffset - refFrom;
            refAdded = maxIndels * 2 + 1;

            if (seqFrom < targetFrom) {
                seqFrom = targetFrom - seqFrom;
                seqLength -= seqFrom;
                seqAdded -= seqFrom;
                seqFrom = targetFrom;
                if (seqAdded < 0)
                    seqAdded = 0;
                else if (seqAdded > seqLength)
                    seqAdded = seqLength;
            }

            if (refFrom < 0) {
                refLength += refFrom;
                refAdded += refFrom;
                refFrom = 0;
                if (refAdded < 0)
                    refAdded = 0;
                else if (refAdded > refLength)
                    refAdded = refLength;
            }

            LinearGapAlignmentScoring<NucleotideSequence> scoring = parameters.getScoring();
            if (parameters.isFloatingLeftBound()) {
                br = BandedLinearAligner.alignSemiLocalRight0(scoring, reference, target,
                        refFrom, refLength, seqFrom, seqLength,
                        maxIndels, parameters.getAlignmentStopPenalty(), mutations, array);

                gRefFrom = br.sequence1Stop;
                gSeqFrom = br.sequence2Stop;
            } else {
                br = BandedLinearAligner.alignLeftAdded0(scoring, reference, target,
                        refFrom, refLength, refAdded, seqFrom, seqLength, seqAdded,
                        maxIndels, mutations, array);

                assert br.sequence2Stop == seqFrom || br.sequence1Stop == refFrom;

                gSeqFrom = br.sequence2Stop;
                gRefFrom = br.sequence1Stop;
            }

            for (; i < hit.seedOffsets.length; ++i)
                if ((currentOffset = hit.seedOffsets[i]) != KMapper.SEED_NOT_FOUND_OFFSET)
                    if (previousOffset != Integer.MIN_VALUE) {
                        currentSeedPosition = seeds.get(hit.from + i);
                        seqFrom = previousSeedPosition + kValue;
                        seqLength = currentSeedPosition - seqFrom;
                        refFrom = seqFrom - previousOffset;
                        refLength = currentSeedPosition - currentOffset - refFrom;

                        assert target.getRange(seqFrom - kValue, seqFrom).equals(reference.getRange(refFrom - kValue, refFrom));

                        if (refLength < 0 || seqLength < 0) {
                            seqFrom -= kValue;
                            refFrom -= kValue;
                            seqLength += kValue;
                            refLength += kValue;
                        }

                        if (refLength > 0 || seqLength > 0)
                            BandedLinearAligner.align0(scoring, reference, target,
                                    refFrom, refLength, seqFrom, seqLength, parameters.getMaxAdjacentIndels(), mutations, array);

                        gSeqTo = currentSeedPosition + kValue;
                        gRefTo = gSeqTo - currentOffset;

                        previousOffset = currentOffset;
                        previousSeedPosition = currentSeedPosition;
                    }

            //Right edge alignment

            seqFrom = gSeqTo;
            seqLength = reference.size() - gRefTo + maxIndels;
            seqAdded = maxIndels * 2 + 1;

            refFrom = gRefTo;
            refLength = targetTo - gSeqTo + maxIndels;
            refAdded = maxIndels * 2 + 1;

            if (seqFrom + seqLength > targetTo) {
                seqAdded -= (seqFrom + seqLength) - targetTo;
                seqLength = targetTo - seqFrom;
                if (seqAdded > seqLength)
                    seqAdded = seqLength;
                else if (seqAdded < 0)
                    seqAdded = 0;
            }

            if (refFrom + refLength > reference.size()) {
                refAdded -= (refFrom + refLength) - reference.size();
                refLength = reference.size() - refFrom;
                if (refAdded > refLength)
                    refAdded = refLength;
                else if (refAdded < 0)
                    refAdded = 0;
            }

            if (parameters.isFloatingRightBound()) {
                br = BandedLinearAligner.alignSemiLocalLeft0(scoring, reference, target,
                        refFrom, refLength, seqFrom, seqLength,
                        maxIndels, parameters.getAlignmentStopPenalty(), mutations, array);
                gRefTo = br.sequence1Stop + 1;
                gSeqTo = br.sequence2Stop + 1;
            } else {
                br = BandedLinearAligner.alignRightAdded0(scoring, reference, target,
                        refFrom, refLength, refAdded, seqFrom, seqLength, seqAdded,
                        maxIndels, mutations, array);
                gRefTo = br.sequence1Stop + 1;
                gSeqTo = br.sequence2Stop + 1;
            }

            //refFrom = gRefTo;
            //seqFrom = gSeqTo;
            //
            //refLength = reference.size() - refFrom;
            //seqLength = target.size() - seqFrom;
            //int minLength = min(refLength, seqLength);
            //
            ////if (minLength > 0)
            //minLength += parameters.getMaxIndels();
            //
            //refLength = min(refLength, minLength);
            //seqLength = min(seqLength, minLength);
            ////TODO ?? (deletions / insertions at the first position)
            //if (refLength > 0 || seqLength > 0)
            //    if (parameters.isFloatingRightBound()) {
            //        BandedSemiLocalResult result = BandedAligner.alignSemiLocalLeft(parameters.getScoring(), reference, target, refFrom, refLength, seqFrom, seqLength,
            //                parameters.getMaxIndels(), parameters.getStopPenalty(), mutations, array);
            //        gRefTo = result.sequence1Stop + 1;
            //        gSeqTo = result.sequence2Stop + 1;
            //    } else {
            //        BandedAligner.align(parameters.getScoring(), reference, target, refFrom, refLength, seqFrom, seqLength,
            //                parameters.getMaxIndels(), mutations, array);
            //        gRefTo = refFrom + refLength;
            //        gSeqTo = seqFrom + seqLength;
            //    }

            Mutations<NucleotideSequence> muts = mutations.createAndDestroy();
            Alignment<NucleotideSequence> al = new Alignment<>(
                    reference, muts, new Range(gRefFrom, gRefTo), new Range(gSeqFrom, gSeqTo), scoring);

            if (parameters.isFloatingRightBound())
                al = AlignmentTrimmer.rightTrimAlignment(al, scoring);

            if (parameters.isFloatingLeftBound())
                al = AlignmentTrimmer.leftTrimAlignment(al, scoring);

            alignment = al;
        } finally {
            AlignmentCache.release();
        }
    }

    public KMappingHit getKMersHit() {
        return result.mappingResult.hits.get(index);
    }

    public Alignment<NucleotideSequence> getAlignment() {
        if (alignment == null)
            throw new IllegalStateException("Alignment has not yet been built.");
        return alignment;
    }

    public KAlignmentResult getResult() {
        return result;
    }

    public NucleotideSequence getHitSequence() {
        return result.aligner.getReference(getId());
    }

    public static void printHitAlignment(KAlignmentHit hit) {
        NucleotideSequence ref = hit.getResult().getAligner().getReference(hit.getId());
        Alignment la = hit.getAlignment();
        System.out.println(la.getAlignmentHelper());
        System.out.println(hit.getResult().getTarget());
        int prev = 0, curr;
        for (int i = hit.getKMersHit().from; i < hit.getKMersHit().to; ++i) {
            if (hit.getKMersHit().seedOffsets[i - hit.getKMersHit().from] != KMapper.SEED_NOT_FOUND_OFFSET) {
                curr = hit.getResult().getMappingResult().seeds.get(i);
                System.out.print(spaces(curr - prev) + "*");
                prev = curr + 1;
            }
        }
        System.out.println();
    }

    private static String spaces(int n) {
        char[] charArray = new char[n];
        Arrays.fill(charArray, ' ');
        return new String(charArray);
    }
}