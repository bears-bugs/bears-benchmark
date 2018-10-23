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

import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.BitArray;
import com.milaboratory.util.IntArrayList;
import com.milaboratory.util.RandomUtil;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;
import static java.util.Arrays.copyOf;

/**
 * KMapper - class to perform fast alignment based only on matches between kMers of target and one of reference
 * sequences. Alignment performed using seed-and-vote procedure.
 *
 * <p>{@link #align(com.milaboratory.core.sequence.NucleotideSequence, int, int)} and {@link
 * #align(com.milaboratory.core.sequence.NucleotideSequence)} methods of this object are thread-safe and can
 * be concurrently used by several threads if no new sequences added after its first invocation.</p>
 *
 * <p><b>Algorithm inspired by:</b> <i>Liao Y et al.</i> The Subread aligner: fast, accurate and scalable read mapping
 * by seed-and-vote. <i>Nucleic Acids Res. 2013 May 1;41(10):e108. doi: 10.1093/nar/gkt214. Epub 2013 Apr 4.</i></p>
 */
public final class KMapper implements java.io.Serializable {
    public static final int SEED_NOT_FOUND_OFFSET = Integer.MIN_VALUE + 1;

    /*
                                   MSB                         LSB
                                   < --------- 32 bits --------- >
        Base record format:   int  |.... ID ....|.... OFFSET ....|
                                                 < bitsForOffset >
     */

    /**
     * Number of bits in base record for offset value
     */
    private final int bitsForOffset;
    /**
     * Mask to extract offset value (= 0xFFFFFFFF >>> (32 - bitsForOffset))
     */
    private final int offsetMask;

    /*           Parameters             */

    /**
     * Nucleotides in kMer (value of k)
     */
    private final int kValue;
    /**
     * Base of records for individual kMers
     */
    private final int[][] base;
    /**
     * Number of records for each individual kMer (used only for building of base)
     */
    private final int[] lengths;
    /**
     * Minimal absolute score value
     */
    private final float absoluteMinScore,
    /**
     * Minimal score in fractions of top score.
     */
    relativeMinScore,
    /**
     * Reward for match (must be > 0)
     */
    matchScore,
    /**
     * Penalty for kMer mismatch (not mapped kMer), must be < 0
     */
    mismatchPenalty,
    /**
     * Penalty for different offset between adjacent seeds
     */
    offsetShiftPenalty;
    /**
     * Minimal alignment length
     */
    private final int minAlignmentLength;
    /**
     * Maximal number of insertions and deletions
     */
    private final int maxIndels;
    /**
     * Determines boundaries type: floating(only part of sequence should be aligned) or fixed (whole sequence should be
     * aligned).
     */
    private final boolean floatingLeftBound, floatingRightBound;
    /**
     * Minimal and maximal distance between kMer seed positions in target sequence
     */
    private final int minDistance, maxDistance;

    /*                  Utility fields                   */
    private boolean built = false;
    private int[] refFrom = new int[10], refLength = new int[10];
    private int maxReferenceLength = 0, minReferenceLength = Integer.MAX_VALUE;
    private int sequencesInBase = 0;

    /**
     * Length = sequencesInBase, all bits set
     */
    private BitArray allFilter;

    /**
     * Creates new KMer mapper.
     *
     * @param kValue             nucleotides in kMer (value of k)
     * @param minDistance        minimal distance between kMer seed positions in target sequence
     * @param maxDistance        maximal distance between kMer seed positions in target sequence
     * @param absoluteMinScore   minimal score
     * @param relativeMinScore   maximal ratio between best hit score and other hits scores in returned result
     * @param minAlignmentLength minimal alignment length
     * @param matchScore         reward for match (must be > 0)
     * @param mismatchPenalty    penalty for mismatch (must be < 0)
     * @param maxIndels          maximal number of insertions and deletions
     * @param floatingLeftBound  true if left bound of alignment could be floating
     * @param floatingRightBound true if right bound of alignment could be floating
     */
    public KMapper(int kValue,
                   int minDistance, int maxDistance,
                   float absoluteMinScore, float relativeMinScore,
                   int minAlignmentLength,
                   float matchScore, float mismatchPenalty, float offsetShiftPenalty,
                   int maxIndels,
                   boolean floatingLeftBound, boolean floatingRightBound) {
        this.kValue = kValue;

        //Bits
        this.bitsForOffset = 18;
        this.offsetMask = 0xFFFFFFFF >>> (32 - bitsForOffset);

        //Initialize base
        int maxNumberOfKmers = 1 << (kValue * 2);
        base = new int[maxNumberOfKmers][10];
        lengths = new int[maxNumberOfKmers];

        //Parameters
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.absoluteMinScore = absoluteMinScore;
        this.relativeMinScore = relativeMinScore;
        this.minAlignmentLength = minAlignmentLength;
        this.matchScore = matchScore;
        this.mismatchPenalty = mismatchPenalty;
        this.offsetShiftPenalty = offsetShiftPenalty;
        this.maxIndels = maxIndels;
        this.floatingLeftBound = floatingLeftBound;
        this.floatingRightBound = floatingRightBound;

        //Initializing random with fixed seed for reproducible alignment results
        //this.random = new RandomDataImpl(new Well19937c(12364785L));
    }

    /**
     * Factory method to create KMapper using parametners specified in the {@link KAlignerParameters}
     * object.
     *
     * @param parameters parameters instance
     * @return new KMapper
     */
    public static KMapper createFromParameters(KAlignerParameters parameters) {
        return new KMapper(parameters.getMapperKValue(), parameters.getMapperMinSeedsDistance(),
                parameters.getMapperMaxSeedsDistance(), parameters.getMapperAbsoluteMinScore(), parameters.getMapperRelativeMinScore(),
                parameters.getMinAlignmentLength(), parameters.getMapperMatchScore(), parameters.getMapperMismatchPenalty(),
                parameters.getMapperOffsetShiftPenalty(),
                parameters.getMaxAdjacentIndels(),
                parameters.isFloatingLeftBound(), parameters.isFloatingRightBound());
    }

    /**
     * Searches for the best offset (with highest number of occurrences) in the sorted array of votes.
     */
    static int getBestOffset(IntArrayList offsets, int except, int shift, int maxOffsetDelta) {

        if (offsets.size() == 1)
            return offsets.get(0) >> shift;

        int current, old = Integer.MAX_VALUE >> 2,
                maxOffset = Integer.MIN_VALUE, maxCount = 0,
                lastMaxIndex = offsets.size() - 1;
        int[] counters = new int[maxOffsetDelta + 1];
        for (int i = offsets.size() - 1; i >= 0; --i) {
            current = offsets.get(i) >> shift;
            if (old - current > maxOffsetDelta) {
                if (maxCount < lastMaxIndex - i && old != except) {
                    maxOffset = old - maxIndex(counters);
                    maxCount = lastMaxIndex - i;
                }
                old = current;
                lastMaxIndex = i;
                Arrays.fill(counters, 0);
            }
            counters[old - current]++;
        }

        if (maxCount < lastMaxIndex + 1 && old != except)
            maxOffset = old - maxIndex(counters);

        assert maxOffset != Integer.MAX_VALUE >> 2 && maxOffset != Integer.MIN_VALUE;

        return maxOffset;
    }

    /**
     * Implements <i>argmax</i> function.
     *
     * @param array input array
     * @return index of the biggest element in array
     */
    private static int maxIndex(int[] array) {
        int value = array[array.length - 1];
        int maxI = array.length - 1;
        for (int i = maxI - 1; i >= 0; --i)
            if (array[i] > value) {
                value = array[i];
                maxI = i;
            }
        return maxI;
    }

    /**
     * Encodes and adds individual kMer to the base.
     */
    private void addKmer(int kmer, int id, int offset) {
        if (base[kmer].length == lengths[kmer])
            base[kmer] = copyOf(base[kmer], base[kmer].length * 3 / 2 + 1);

        if ((offset & offsetMask) != offset)
            throw new IllegalArgumentException("Record is too long.");

        base[kmer][lengths[kmer]++] = (id << bitsForOffset) | (offset);
    }

    /**
     * Adds new reference sequence to the base of this mapper and returns index assigned to it.
     *
     * @param sequence sequence
     * @return index assigned to the sequence
     */
    public int addReference(NucleotideSequence sequence) {
        return addReference(sequence, 0, sequence.size());
    }

    /**
     * Adds new reference sequence to the base of this mapper and returns index assigned to it.
     *
     * <p>User can specify a part of a sequence to be indexed (only this part will be identified during the alignment
     * procedure). The offset returned by alignment procedure will be in global sequence coordinates, relative to the
     * beginning of the sequence (not to the specified offset).</p>
     *
     * @param sequence sequence
     * @param offset   offset of subsequence to be indexed
     * @param length   length of subsequence to be indexed
     * @return index assigned to the sequence
     */
    public int addReference(NucleotideSequence sequence, int offset, int length) {
        //Resetting built flag
        built = false;

        //Next id.
        if (refLength.length == sequencesInBase) {
            refLength = copyOf(refLength, sequencesInBase * 3 / 2 + 1);
            refFrom = copyOf(refFrom, sequencesInBase * 3 / 2 + 1);
        }
        int id = sequencesInBase++;
        refFrom[id] = offset;
        refLength[id] = sequence.size();

        //Calculating min and max reference sequences lengths
        maxReferenceLength = max(maxReferenceLength, sequence.size());
        minReferenceLength = Math.min(minReferenceLength, sequence.size());

        int kmer = 0;
        int kmerMask = 0xFFFFFFFF >>> (32 - kValue * 2);
        int tMask = 0xFFFFFFFF >>> (34 - kValue * 2);

        int to = length - kValue;
        for (int j = 0; j < kValue; ++j)
            kmer = kmer << 2 | sequence.codeAt(j + offset);
        addKmer(kmer, id, offset);

        for (int i = 1; i <= to; ++i) {
            //Next kMer
            kmer = kmerMask & (kmer << 2 | sequence.codeAt(offset + i + kValue - 1));

            //Detecting homopolymeric kMers and dropping them
            if (((kmer ^ (kmer >>> 2)) & tMask) == 0 &&
                    ((kmer ^ (kmer << 2)) & (tMask << 2)) == 0)
                continue;

            addKmer(kmer, id, i + offset);
        }

        return id;
    }

    /**
     * Builds additional data fields used by this mapper. Invoked automatically if this mapper is not yet built by
     * {@link #align(com.milaboratory.core.sequence.NucleotideSequence, int, int)} method.
     */
    void ensureBuilt() {
        if (!built)
            synchronized (this) {
                if (!built) {
                    for (int i = 0; i < base.length; ++i)
                        base[i] = copyOf(base[i], lengths[i]);
                    refLength = copyOf(refLength, sequencesInBase);
                    refFrom = copyOf(refFrom, sequencesInBase);
                    allFilter = new BitArray(sequencesInBase);
                    allFilter.setAll();
                    built = true;
                }
            }
    }

    /**
     * Calculates maximal estimate of score for the hit.
     */
    private void getScoreFromOffsets(IntArrayList offsets, Info ret) {
        int score = 0;

        int shift = 32 - bitsForOffset;
        int bestOffset = getBestOffset(offsets, Integer.MIN_VALUE, shift, maxIndels),
                current;
        for (int i = offsets.size() - 1; i >= 0; --i) {
            current = (offsets.get(i) >> shift) - bestOffset;
            if ((current <= maxIndels && current >= 0)
                    || (current >= -maxIndels && current < 0))
                ++score;
        }

        ret.score = score;
        ret.offset = bestOffset;
    }

    /**
     * Performs an alignment.
     *
     * <p>This methods is thread-safe and can be concurrently used by several threads if no new sequences added after
     * its first invocation.</p>
     *
     * @param sequence target sequence
     * @return a list of hits found in the target sequence
     */
    public KMappingResult align(NucleotideSequence sequence) {
        return align(sequence, 0, sequence.size(), null);
    }

    /**
     * Performs an alignment for a part of the target sequence.
     *
     * <p>This methods is thread-safe and can be concurrently used by several threads if no new sequences added after
     * its first invocation.</p>
     *
     * @param sequence target sequence
     * @param from     first nucleotide to align (inclusive)
     * @param to       last nucleotide to align (exclusive)
     * @return a list of hits found in the target sequence
     */
    public KMappingResult align(NucleotideSequence sequence, int from, int to) {
        return align(sequence, from, to, null);
    }

    /**
     * Performs an alignment for a part of the target sequence.
     *
     * <p>This methods is thread-safe and can be concurrently used by several threads if no new sequences added after
     * its first invocation.</p>
     *
     * @param sequence target sequence
     * @param from     first nucleotide to align (inclusive)
     * @param to       last nucleotide to align (exclusive)
     * @param filter   record filter (align only records with filter[id] == true)
     * @return a list of hits found in the target sequence
     */
    public KMappingResult align(NucleotideSequence sequence, int from, int to, BitArray filter) {
        ensureBuilt();

        if (filter == null)
            filter = allFilter;

        ArrayList<KMappingHit> result = new ArrayList<>();

        if (to - from <= kValue)
            return new KMappingResult(null, result);

        IntArrayList seedPositions = new IntArrayList((to - from) / minDistance + 2);
        int seedPosition = from;
        seedPositions.add(seedPosition);

        Well19937c random = RandomUtil.getThreadLocalRandom();

        while ((seedPosition += random.nextInt(maxDistance + 1 - minDistance) + minDistance) < to - kValue)
            seedPositions.add(seedPosition);

        //if (seedPositions.get(seedPositions.size() - 1) != to - kValue)
        seedPositions.add(to - kValue);

        int[] seeds = new int[seedPositions.size()];

        int kmer;
        IntArrayList[] candidates = new IntArrayList[sequencesInBase];

        //Building candidates arrays (seed)
        int id, offset;
        for (int i = 0; i < seeds.length; ++i) {
            kmer = 0;
            for (int j = seedPositions.get(i); j < seedPositions.get(i) + kValue; ++j)
                kmer = kmer << 2 | sequence.codeAt(j);

            seeds[i] = kmer;
            if (base[kmer].length == 0)
                continue;

            for (int record : base[kmer]) {
                id = record >>> bitsForOffset;

                // Apply filter
                if (!filter.get(id))
                    continue;

                offset = record & offsetMask;

                if (candidates[id] == null)
                    candidates[id] = new IntArrayList();

                candidates[id].add((seedPositions.get(i) - offset) << (32 - bitsForOffset) | i);
            }
        }

        //Selecting best candidates (vote)
        //int resultId = 0;
        Info info = new Info();
        int cFrom, cTo, siFrom, siTo;
        int j, i;
        double preScore;
        for (i = candidates.length - 1; i >= 0; --i) {
            if (candidates[i] != null && candidates[i].size() >= ((minAlignmentLength - kValue + 1) / maxDistance)) {

                //Sorting (important)
                candidates[i].sort();
                //Calculating best score and offset values
                getScoreFromOffsets(candidates[i], info);

                //Theoretical range of target and reference sequence intersection
                cFrom = max(info.offset, from);
                cTo = min(info.offset + refLength[i], to) - kValue;

                //Calculating number of seeds in this range
                siTo = siFrom = -1;
                for (j = seedPositions.size() - 1; j >= 0; --j)
                    if ((seedPosition = seedPositions.get(j)) <= cTo) {
                        if (siTo == -1)
                            siTo = j + 1;
                        if (seedPosition < cFrom) {
                            siFrom = j + 1;
                            break;
                        }
                    }

                //If siFrom not set, first seed is inside the range of target and
                //reference sequence intersection
                if (siFrom == -1)
                    siFrom = 0;

                //Calculating score without penalty
                preScore = matchScore * info.score; //+ max(siTo - siFrom - info.score, 0) * mismatchScore;

                //Selecting candidates
                if (preScore >= absoluteMinScore)
                    result.add(new KMappingHit(info.offset, i, (float) preScore, siFrom, siTo));
            }
        }

        int c, seedIndex, seedIndexMask = (0xFFFFFFFF >>> (bitsForOffset)),
                offsetDelta, currentSeedPosition, prev;
        float score;

        KMappingHit hit;
        double maxScore = 0.0;
        for (j = result.size() - 1; j >= 0; --j) {
            hit = result.get(j);

            //Fulfilling the seed positions array
            //getting seed positions in intersection of target and reference sequences
            hit.seedOffsets = new int[hit.to - hit.from];
            Arrays.fill(hit.seedOffsets, SEED_NOT_FOUND_OFFSET);
            for (int k = candidates[hit.id].size() - 1; k >= 0; --k) {
                //  offset value | seed index
                c = candidates[hit.id].get(k);
                seedIndex = c & seedIndexMask;

                //filling seed position array with actual positions of seeds inside intersection
                if (seedIndex >= result.get(j).from && seedIndex < result.get(j).to) {
                    seedIndex -= hit.from;
                    offsetDelta = abs((c >> (32 - bitsForOffset)) - hit.offset);

                    //check if offset difference is less than max allowed and better seed position is found
                    if (offsetDelta <= maxIndels &&
                            (hit.seedOffsets[seedIndex] == SEED_NOT_FOUND_OFFSET ||
                                    abs(hit.seedOffsets[seedIndex] - hit.offset) > offsetDelta))
                        hit.seedOffsets[seedIndex] = (c >> (32 - bitsForOffset));
                }
            }

            //Previous seed position
            prev = -1;
            c = -1;
            for (i = hit.seedOffsets.length - 1; i >= 0; --i)
                if (hit.seedOffsets[i] != SEED_NOT_FOUND_OFFSET) {
                    if (c != -1)
                        //check for situation like: seedsOffset = [25,25,25,25,  28  ,25]
                        //we have to remove such offsets because it's most likely wrong mapping
                        //c - most left index, prev - middle index and i - right most index
                        //but we iterate in reverse direction
                        if (hit.seedOffsets[c] != hit.seedOffsets[prev] && hit.seedOffsets[prev] != hit.seedOffsets[i] &&
                                ((hit.seedOffsets[c] < hit.seedOffsets[prev])
                                        != (hit.seedOffsets[prev] < hit.seedOffsets[i]))) {
                            hit.seedOffsets[prev] = SEED_NOT_FOUND_OFFSET;
                            prev = -1;
                        }
                    c = prev;
                    prev = i;
                }


            //Calculating score
            score = 0.0f;
            for (int off : hit.seedOffsets)
                if (off != SEED_NOT_FOUND_OFFSET)
                    score += matchScore;
                else
                    score += mismatchPenalty;

            //Floating bounds reward
            if (floatingLeftBound) {
                prev = -1;
                for (i = 0; i < hit.seedOffsets.length; ++i)
                    if (hit.seedOffsets[i] != SEED_NOT_FOUND_OFFSET) {
                        if (prev == -1) {
                            prev = i;
                            continue;
                        }

                        //Calculating score gain for deleting first kMer
                        if (matchScore + abs(hit.seedOffsets[i] - hit.seedOffsets[prev]) * offsetShiftPenalty + (i - prev - 1) * mismatchPenalty <= 0.0f) {
                            //Bad kMer
                            hit.seedOffsets[prev] = SEED_NOT_FOUND_OFFSET;
                            prev = i;
                            continue;
                        }

                        score -= prev * mismatchPenalty;
                        break;
                    }
            }

            //Floating bounds reward
            if (floatingRightBound) {
                prev = -1;
                for (i = hit.seedOffsets.length - 1; i >= 0; --i)
                    if (hit.seedOffsets[i] != SEED_NOT_FOUND_OFFSET) {
                        if (prev == -1) {
                            prev = i;
                            continue;
                        }

                        //Calculating score gain for deleting first kMer
                        if (matchScore + abs(hit.seedOffsets[i] - hit.seedOffsets[prev]) * offsetShiftPenalty + (prev - 1 - i) * mismatchPenalty <= 0.0f) {
                            //Bad kMer
                            hit.seedOffsets[prev] = SEED_NOT_FOUND_OFFSET;
                            prev = i;
                            continue;
                        }

                        score -= (hit.seedOffsets.length - 1 - prev) * mismatchPenalty;
                    }
            }

            c = -1;
            prev = -1;
            //totalIndels = 0;
            for (i = hit.seedOffsets.length - 1; i >= 0; --i) {
                if (hit.seedOffsets[i] != SEED_NOT_FOUND_OFFSET) {
                    currentSeedPosition = seedPositions.get(i + hit.from) - hit.seedOffsets[i];
                    if (c == -1) {
                        c = currentSeedPosition;
                        prev = i;
                    } else if (c <= currentSeedPosition)
                        //Removing paradoxical kMer offsets
                        hit.seedOffsets[i] = SEED_NOT_FOUND_OFFSET;
                    else {
                        //totalIndels += abs(hit.seedOffsets[i] - hit.seedOffsets[prev]);
                        score += abs(hit.seedOffsets[i] - hit.seedOffsets[prev]) * offsetShiftPenalty;
                        c = currentSeedPosition;
                        prev = i;
                    }
                }
            }

            hit.score = score;

            if (score < absoluteMinScore)
                result.remove(j);

            if (maxScore < score)
                maxScore = score;

            //if (totalIndels > maxIndels * 2) {
            //    result.remove(j);
            //}
        }

        //Removing candidates with score < maxScore * hitsRange
        maxScore *= relativeMinScore;
        for (j = result.size() - 1; j >= 0; --j) {
            hit = result.get(j);
            if (hit.score < maxScore)
                result.remove(j);
        }

        //Removing seed conflicts

        return new KMappingResult(seedPositions, result);
    }

    /**
     * Returns number of nucleotides in kMer (value of k)
     *
     * @return number of nucleotides in kMer (value of k)
     */

    public int getKValue() {
        return kValue;
    }

    /**
     * Returns minimal score
     *
     * @return minimal score
     */
    public float getAbsoluteMinScore() {
        return absoluteMinScore;
    }

    /**
     * Returns maximal distance between kMer seed positions in target sequence
     *
     * @return maximal distance between kMer seed positions in target sequence
     */
    public int getMaxDistance() {
        return maxDistance;
    }

    /**
     * Returns minimal distance between kMer seed positions in target sequence
     *
     * @return minimal distance between kMer seed positions in target sequence
     */
    public int getMinDistance() {
        return minDistance;
    }

    /**
     * Returns maximal number of insertions and deletions
     *
     * @return maximal number of insertions and deletions
     */
    public int getMaxIndels() {
        return maxIndels;
    }

    /**
     * Returns maximal ratio between best hit score and other hits scores in returned result
     *
     * @return maximal ratio between best hit score and other hits scores in returned result
     */
    public float getRelativeMinScore() {
        return relativeMinScore;
    }

    /**
     * Method used internally.
     */
    public SummaryStatistics getRecordSizeSummaryStatistics() {
        SummaryStatistics ss = new SummaryStatistics();
        for (int len : lengths)
            ss.addValue(len);
        return ss;
    }

    @Override
    public String toString() {
        SummaryStatistics ss = getRecordSizeSummaryStatistics();
        return "K=" + kValue + "; Avr=" + ss.getMean() + "; SD=" + ss.getStandardDeviation();
    }

    /**
     * Used to store preliminary information about hit.
     */
    private static class Info {
        int offset, score;
    }
}
