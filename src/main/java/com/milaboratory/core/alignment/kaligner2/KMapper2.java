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

import cc.redberry.pipe.CUtils;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.BitArray;
import com.milaboratory.util.IntArrayList;
import com.milaboratory.util.IntCombinations;
import com.milaboratory.util.RandomUtil;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.milaboratory.core.alignment.kaligner2.KAligner2Statistics.ClusterTrimmingType.*;
import static com.milaboratory.core.alignment.kaligner2.OffsetPacksAccumulator.*;
import static java.lang.Integer.bitCount;
import static java.lang.Math.*;
import static java.util.Arrays.copyOf;

/**
 * KMapper - class to perform fast alignment based only on matches between kMers of target and one of reference
 * sequences. Alignment performed using seed-and-vote procedure.
 *
 * <p>{@link #align(NucleotideSequence, int, int)} and {@link
 * #align(NucleotideSequence)} methods of this object are thread-safe and can
 * be concurrently used by several threads if no new sequences added after its first invocation.</p>
 *
 * <p><b>Algorithm inspired by:</b> <i>Liao Y et al.</i> The Subread aligner: fast, accurate and scalable read mapping
 * by seed-and-vote. <i>Nucleic Acids Res. 2013 May 1;41(10):e108. doi: 10.1093/nar/gkt214. Epub 2013 Apr 4.</i></p>
 */
public final class KMapper2 implements java.io.Serializable {
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
    private static final int bitsForIndex = 13;
    /**
     * Index mask (= 0xFFFFFFFF << (32 - bitsForIndex))
     */
    private static final int indexMask = 0xFFFFFFFF >>> (32 - bitsForIndex);
    /**
     * Mask to extract offset value (= 0xFFFFFFFF >>> bitsForIndex)
     */
    private static final int offsetMask = 0xFFFFFFFF >>> bitsForIndex;

    /*           Parameters             */

    /**
     * Nucleotides in kMer (value of k)
     */
    private final int nValue;
    /**
     * Allowed number of mutations in kMer
     */
    private final int kValue;
    /**
     * Iterations for each kMer
     */
    private final int kMersPerPosition;
    /**
     * Base of records for individual kMers
     */
    //base[combinationMask][kMer][seeds]
    private final int[][][] base;
    /**
     * Number of records for each individual kMer (used only for building of base)
     */
    //length[combinationMask][kMer]
    private final int[][] lengths;
    /**
     * Minimal absolute score value
     */
    private final int absoluteMinClusterScore,
    //TODO
    extraClusterScore,
    /**
     * Reward for match (must be > 0)
     */
    matchScore,
    /**
     * Penalty for kMer mismatch (not mapped kMer), must be < 0
     */
    mismatchScore,
    /**
     * Penalty for different offset between adjacent seeds
     */
    offsetShiftScore,

    slotCount,

    maxClusterIndels;

    private final int maxClusters;

    /**
     * Minimal absolute score.
     */
    private final int absoluteMinScore;
    /**
     * Minimal score in fractions of top score.
     */
    private final float relativeMinScore;

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
    private volatile boolean built = false;
    private int maxReferenceLength = 0, minReferenceLength = Integer.MAX_VALUE;
    private int sequencesInBase = 0;

    /**
     * Length = sequencesInBase, all bits set
     */
    private BitArray allFilter;

    /**
     * Cache to prevent excessive memory allocation
     */
    final ThreadLocal<ThreadLocalCache> memoryCache = new ThreadLocal<ThreadLocalCache>() {
        @Override
        protected ThreadLocalCache initialValue() {
            return new ThreadLocalCache(sequencesInBase, slotCount, maxClusterIndels, matchScore, mismatchScore, offsetShiftScore, absoluteMinClusterScore);
        }
    };
    /**
     * Statistics aggregator
     */
    private final KAligner2Statistics stat;
    //private final float terminationThreshold = 6.6e6f;

    /**
     * Creates new KMer mapper.
     *
     * @param nValue                  nucleotides in kMer (value of k)
     * @param minDistance             minimal distance between kMer seed positions in target sequence
     * @param maxDistance             maximal distance between kMer seed positions in target sequence
     * @param absoluteMinClusterScore minimal score
     * @param relativeMinScore        maximal ratio between best hit score and other hits scores in returned result
     * @param matchScore              reward for match (must be > 0)
     * @param mismatchScore           penalty for mismatch (must be < 0)
     * @param floatingLeftBound       true if left bound of alignment could be floating
     * @param floatingRightBound      true if right bound of alignment could be floating
     */
    public KMapper2(int nValue, int kValue,
                    int minDistance, int maxDistance,
                    int absoluteMinClusterScore, int extraClusterScore,
                    int absoluteMinScore, float relativeMinScore,
                    int matchScore, int mismatchScore, int offsetShiftScore,
                    int slotCount, int maxClusters, int maxClusterIndels, int kMersPerPosition,
                    boolean floatingLeftBound, boolean floatingRightBound) {
        this(nValue, kValue, minDistance, maxDistance, absoluteMinClusterScore, extraClusterScore, absoluteMinScore,
                relativeMinScore, matchScore, mismatchScore, offsetShiftScore, slotCount, maxClusters, maxClusterIndels,
                kMersPerPosition, floatingLeftBound, floatingRightBound, null);
    }

    /**
     * Creates new KMer mapper.
     *
     * @param nValue                  nucleotides in kMer (value of k)
     * @param minDistance             minimal distance between kMer seed positions in target sequence
     * @param maxDistance             maximal distance between kMer seed positions in target sequence
     * @param absoluteMinClusterScore minimal score
     * @param relativeMinScore        maximal ratio between best hit score and other hits scores in returned result
     * @param matchScore              reward for match (must be > 0)
     * @param mismatchScore           penalty for mismatch (must be < 0)
     * @param floatingLeftBound       true if left bound of alignment could be floating
     * @param floatingRightBound      true if right bound of alignment could be floating
     * @param stat                    stat
     */
    public KMapper2(int nValue, int kValue,
                    int minDistance, int maxDistance,
                    int absoluteMinClusterScore, int extraClusterScore,
                    int absoluteMinScore, float relativeMinScore,
                    int matchScore, int mismatchScore, int offsetShiftScore,
                    int slotCount, int maxClusters, int maxClusterIndels, int kMersPerPosition,
                    boolean floatingLeftBound, boolean floatingRightBound,
                    KAligner2Statistics stat) {
        if (nValue - kValue <= 2)
            throw new IllegalArgumentException("Wrong combination of K and N values. K = " + kValue + " N = " + nValue + ".");

        this.nValue = nValue;
        this.kValue = kValue;

        // TODO lazy
        int maxHolesMask = kValue == 0 ? 1 : (((0xFFFFFFFF >>> (32 - kValue)) << (nValue - kValue)) + 1);
        base = new int[maxHolesMask][][];
        lengths = new int[maxHolesMask][];

        if ((kValue == 0 && kMersPerPosition != 1)
                || (kValue != 0 && kMersPerPosition > nValue / kValue))
            throw new IllegalArgumentException("Wrong combination of nValue, kValue and kMersPerPosition.");

        this.kMersPerPosition = kMersPerPosition;

        IntCombinations combinations = new IntCombinations(nValue, kValue);
        for (int[] combination : CUtils.it(combinations)) {
            int holesMask = getCombinationMask(combination);
            base[holesMask] = new int[1 << ((nValue - kValue) * 2)][];
            lengths[holesMask] = new int[1 << ((nValue - kValue) * 2)];
        }

        //Parameters
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.absoluteMinClusterScore = absoluteMinClusterScore;
        this.extraClusterScore = extraClusterScore;
        this.absoluteMinScore = absoluteMinScore;
        this.relativeMinScore = relativeMinScore;
        this.matchScore = matchScore;
        this.mismatchScore = mismatchScore;
        this.offsetShiftScore = offsetShiftScore;
        this.slotCount = slotCount;
        this.maxClusters = maxClusters;
        this.maxClusterIndels = maxClusterIndels;
        this.floatingLeftBound = floatingLeftBound;
        this.floatingRightBound = floatingRightBound;
        this.stat = stat;
    }

    /**
     * Factory method to create KMapper2 using parameters specified in the {@link KAlignerParameters2}
     * object.
     *
     * @param parameters parameters instance
     * @return new KMapper
     */
    public static KMapper2 createFromParameters(KAlignerParameters2 parameters) {
        return createFromParameters(parameters, null);
    }

    /**
     * Factory method to create KMapper2 using parameters specified in the {@link KAlignerParameters2}
     * object.
     *
     * @param parameters parameters instance
     * @param stat       stat
     * @return new KMapper
     */
    public static KMapper2 createFromParameters(KAlignerParameters2 parameters, KAligner2Statistics stat) {
        return new KMapper2(parameters.getMapperNValue(), parameters.getMapperKValue(), parameters.getMapperMinSeedsDistance(),
                parameters.getMapperMaxSeedsDistance(), parameters.getMapperAbsoluteMinClusterScore(),
                parameters.getMapperExtraClusterScore(),
                parameters.getMapperAbsoluteMinScore(),
                parameters.getMapperRelativeMinScore(),
                parameters.getMapperMatchScore(), parameters.getMapperMismatchScore(),
                parameters.getMapperOffsetShiftScore(), parameters.getMapperSlotCount(),
                parameters.getMapperMaxClusters(),
                parameters.getMapperMaxClusterIndels(), parameters.getMapperKMersPerPosition(),
                parameters.isFloatingLeftBound(), parameters.isFloatingRightBound(), stat);
    }

    /**
     * Encodes and adds individual kMer to the base.
     */
    private void addKmer(int holesMask, int kmer, int id, int offset) {
        if (base[holesMask][kmer] == null)
            base[holesMask][kmer] = new int[10];
        else if (base[holesMask][kmer].length == lengths[holesMask][kmer])
            base[holesMask][kmer] = copyOf(base[holesMask][kmer], base[holesMask][kmer].length * 3 / 2 + 1);

        if ((offset & offsetMask) != offset)
            throw new IllegalArgumentException("Record is too long.");

        assert lengths[holesMask][kmer] == 0 || index(base[holesMask][kmer][lengths[holesMask][kmer] - 1]) != id
                || offset(base[holesMask][kmer][lengths[holesMask][kmer] - 1]) < offset;

        base[holesMask][kmer][lengths[holesMask][kmer]++] = record(offset, id);
    }


    /**
     * Adds new reference sequence to the base of this mapper and returns index assigned to it.
     *
     * @param sequence sequence
     * @return index assigned to the sequence
     */
    public int addReference(NucleotideSequence sequence) {
        if (built)
            throw new IllegalStateException("Already in use.");

        // Checking parameters
        if (sequencesInBase >= (1 << bitsForIndex))
            throw new IllegalArgumentException("Maximum number of records reached.");

        //Resetting built flag
        built = false;

        int id = sequencesInBase++;

        //Calculating min and max reference sequences lengths
        maxReferenceLength = max(maxReferenceLength, sequence.size());
        minReferenceLength = Math.min(minReferenceLength, sequence.size());

        int kmer;
        int tMask = 0xFFFFFFFF >>> (34 - nValue * 2);

        int to = sequence.size() - nValue;
        IntCombinations combinations = new IntCombinations(nValue, kValue);
        for (int[] combination : CUtils.it(combinations)) {
            int holesMask = getCombinationMask(combination);

            kmer = 0;
            for (int j = 0; j < nValue; ++j)
                if (((holesMask >> j) & 1) == 0)
                    kmer = (kmer << 2 | sequence.codeAt(j));
            addKmer(holesMask, kmer, id, 0);

            for (int i = 1; i <= to; ++i) {
                //Next kMer
                kmer = 0;
                for (int j = 0; j < nValue; ++j)
                    if (((holesMask >> j) & 1) == 0)
                        kmer = (kmer << 2 | sequence.codeAt(i + j));

                //Detecting homopolymeric kMers and dropping them
                //TODO:::!!!!
//                if (((kmer ^ (kmer >>> 2)) & tMask) == 0 && ((kmer ^ (kmer << 2)) & (tMask << 2)) == 0)
//                    continue;

                addKmer(holesMask, kmer, id, i);
            }
        }

        return id;
    }

    /**
     * Builds additional data fields used by this mapper. Invoked automatically if this mapper is not yet built by
     * {@link #align(NucleotideSequence, int, int)} method.
     */
    void ensureBuilt() {
        if (!built)
            synchronized (this) {
                if (!built) {
                    int[] zero = new int[0];
                    IntCombinations combinations = new IntCombinations(nValue, kValue);
                    for (int[] combination : CUtils.it(combinations)) {
                        int holeMask = getCombinationMask(combination);
                        for (int kMer = 0; kMer < base[holeMask].length; ++kMer)
                            if (base[holeMask][kMer] != null) {
                                base[holeMask][kMer] = copyOf(base[holeMask][kMer], lengths[holeMask][kMer]);
                                Arrays.sort(base[holeMask][kMer]);
                            } else
                                base[holeMask][kMer] = zero;
                    }

                    allFilter = new BitArray(sequencesInBase);
                    allFilter.setAll();

                    built = true;
                }
            }
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
    public KMappingResult2 align(NucleotideSequence sequence) {
        return align(sequence, 0, sequence.size());
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
    public KMappingResult2 align(final NucleotideSequence sequence, final int from, final int to) {
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
    public KMappingResult2 align(final NucleotideSequence sequence, final int from, final int to, BitArray filter) {
        ensureBuilt();

        if (filter == null)
            filter = allFilter;

        ThreadLocalCache cache = memoryCache.get();
        cache.reset();

        final ArrList<KMappingHit2> result = new ArrList<>();

        // Sequence is shorter than k values
        if (to - from <= nValue) {
            KMappingResult2 kMappingResult2 = new KMappingResult2(null, result);

            // Collecting statistics
            if (stat != null)
                stat.kMappingResults(kMappingResult2);

            return kMappingResult2;
        }

        // Positions of first nucleotides of seed k-mers in query sequence
        final IntArrayList seedPositions = cache.seedPositions;
        int seedPosition = from;

        // Adding firs possible position
        seedPositions.add(seedPosition);

        // Generating random positions of seeds
        RandomGenerator random = RandomUtil.getThreadLocalRandom();
        while ((seedPosition += random.nextInt(maxDistance + 1 - minDistance) + minDistance) < to - nValue)
            seedPositions.add(seedPosition);

        // Adding last possible position to the lis of seed positions
        seedPositions.add(to - nValue);

        int kmer;
        final IntArrayList[] candidates = cache.candidates;

        // Building list of records for all target sequences
        // By querying db for each seed kmer from query sequence
        int id, positionInTarget;
        IntArrayList allRecords = cache.cachedIntArray1;

        final int allPositionsMask = 0xFFFFFFFF >>> (32 - nValue);
        final int nValue2 = nValue / 2;
        int holesMask;

        for (int i = 0; i < seedPositions.size(); ++i) {
            allRecords.clear();

            int notForbidden = allPositionsMask;

            for (int holesMaskIter = 0; holesMaskIter < kMersPerPosition; ++holesMaskIter) {
                if (nValue2 <= bitCount(notForbidden)) {
                    holesMask = 0;
                    while (bitCount(holesMask) != kValue) {
                        holesMask |= 1 << random.nextInt(nValue);
                        holesMask &= notForbidden;
                    }
                } else {
                    holesMask = notForbidden;
                    while (bitCount(holesMask) != kValue)
                        holesMask &= ~(1 << random.nextInt(nValue));
                }

                notForbidden &= ~holesMask;

                kmer = 0;
                for (int j = 0; j < nValue; ++j)
                    if (((holesMask >> j) & 1) == 0)
                        kmer = kmer << 2 | sequence.codeAt(seedPositions.get(i) + j);

                allRecords.addAll(base[holesMask][kmer]);
            }

            // Adding each records for it's corresponding candidate

            allRecords.sort();
            for (int i1 = 0; i1 < allRecords.size(); i1++) {
                int record = allRecords.get(i1);
                if (i1 > 0 && record == allRecords.get(i1 - 1))
                    continue;

                // Id of target sequence, where the kMer was found
                id = index(record);

                // Apply filter
                if (!filter.get(id))
                    continue;

                // Position of the kMer in target sequence
                positionInTarget = offset(record);

                // Lazy initialization of candidate lists
                //if (candidates[id] == null)
                //    candidates[id] = new IntArrayList();

                // Records for the same target in DB are sorted in descending order by positions
                assert candidates[id].isEmpty() || index(candidates[id].last()) != i
                        || offset(candidates[id].last()) < positionInTarget - seedPositions.get(i);

                // Adding restructured record to candidate list
                candidates[id].add(record(positionInTarget - seedPositions.get(i), i));
            }
        }

        // If stat object is set write statistics
        if (stat != null)
            stat.afterCandidatesArrayDone(candidates);

        // Minimal number of records that can possible give scoring above threshold
        final int possibleMinKmers = (int) Math.ceil(absoluteMinClusterScore / matchScore);

        // Calculating hits for each candidate
        // Truncation & Untangling of clusters happens here
        for (int i = 0; i < candidates.length; i++) {
            // No records
            if (candidates[i] == null)
                continue;

            // Early termination of calculations for this candidate
            if (candidates[i].size() - 1 < possibleMinKmers)
                continue;

            // Performing main algorithms on records extracted from DB
            KMappingHit2 e = calculateHit(i, candidates[i], seedPositions);

            // Adding result to hits list if it was successful
            if (e != null)
                result.add(e);
        }

        Collections.sort(result, SCORE_COMPARATOR);

        if (!result.isEmpty()) {
            int threshold = max((int) (result.get(0).score * relativeMinScore), absoluteMinScore);
            int i = 0;
            for (; i < result.size(); ++i)
                if (result.get(i).score <= threshold)
                    break;
            result.removeRange(i, result.size());
        }

        KMappingResult2 kMappingResult2 = new KMappingResult2(seedPositions, result);

        // Collecting statistics
        if (stat != null)
            stat.kMappingResults(kMappingResult2);

        return kMappingResult2;
    }

    private static final Comparator<KMappingHit2> SCORE_COMPARATOR = new Comparator<KMappingHit2>() {
        @Override
        public int compare(final KMappingHit2 o1, final KMappingHit2 o2) {
            return Integer.compare(o2.score, o1.score);
        }
    };

    /**
     * Array list wrapper for {@link #calculateHit(int, int[], int, int, IntArrayList)}.
     */
    public KMappingHit2 calculateHit(int id, IntArrayList data, IntArrayList seedPositions) {
        return calculateHit(id, IntArrayList.getArrayReference(data), 0, data.size(), seedPositions);
    }

    /**
     * Performs truncation of cluster from the right side.
     *
     * - if byIndex = true  : till the index of last cluster record will be < truncationPoint
     * - if byIndex = false : till the positionInTarget of last cluster record will be < truncationPoint
     *
     * @param byIndex         determine truncation type
     * @param seedPositions   seed positions
     * @param results         cluster data
     * @param data            records data
     * @param dataTo          use only [0..dataTo] records from data
     * @param clusterPointer  pointer to target cluster
     * @param truncationPoint main truncation threshold
     * @return {@literal true} if cluster remains to have score above the threshold (absoluteMinClusterScore);
     * {@literal false} if cluster was removed due to too low score
     */
    private boolean truncateClusterFromRight(
            boolean byIndex,
            final IntArrayList seedPositions,
            final IntArrayList results, final int[] data,
            final int dataTo, final int clusterPointer, final int truncationPoint) {
        // Collecting stat
        if (stat != null)
            stat.trimmingEvent(byIndex ? TrimRightQuery : TrimRightTarget);

        int recordId = results.get(clusterPointer + FIRST_RECORD_ID), // Going from the left side of the cluster
                lastRecordId = results.get(clusterPointer + LAST_RECORD_ID),
                record = data[recordId],
                prevOffset = offset(record),
                prevIndex = index(record),
                score = matchScore;

        // Last record id is inside the working range of data array
        assert lastRecordId < dataTo;

        // Roll right in stretch
        int i = recordId;
        while (++i <= lastRecordId && prevIndex == index(data[i])) ;

        // Calculate score and searching for truncation point
        int offset, index;
        for (; i <= lastRecordId; ++i) {
            // Detecting truncation point in case of by-index truncation
            if (byIndex && index(data[i]) >= truncationPoint)
                break;

            index = index(data[i]);
            offset = offset(data[i]);
            if (inDelta(prevOffset, offset, maxClusterIndels)) {
                // Processing exceptional cases for self-correlated K-Mers
                // If next record has same index and better offset
                // (closer to current island LAST_VALUE)
                if (i < lastRecordId
                        && index == index(data[i + 1])
                        && abs(prevOffset - offset) > abs(prevOffset - offset(data[i + 1])))
                    // Skip current record
                    continue;

                // Indices are sorted in ascending order
                assert index - prevIndex - 1 >= 0;

                // Detecting truncation point for by-position-in-target truncation
                if (!byIndex && positionInTarget(seedPositions, data[i]) >= truncationPoint)
                    break;

                // Score for this point
                int scoreDelta = matchScore + (index - prevIndex - 1) * mismatchScore +
                        abs(prevOffset - offset) * offsetShiftScore;

                // Count record only if it adds score to this cluster
                if (scoreDelta > 0) {
                    score += scoreDelta;
                    prevOffset = offset;
                    prevIndex = index;
                    recordId = i;
                }

                // Skipping other records with the same index (they must have worse offsets)
                while (++i <= lastRecordId && prevIndex == index(data[i])) ;
            }
        }

        // Adjusting cluster data
        results.set(clusterPointer + LAST_RECORD_ID, recordId);
        results.set(clusterPointer + SCORE, score);

        // Testing if cluster still have enough score
        if (score < absoluteMinClusterScore) {
            // dropping cluster if not
            results.set(clusterPointer + FIRST_RECORD_ID, DROPPED_CLUSTER);
            // and returning false
            return false;
        }

        // Score is still above the threshold
        return true;
    }

    /**
     * Performs truncation of cluster from the left side.
     *
     * - if byIndex = true  : till the index of last cluster record will be < truncationPoint
     * - if byIndex = false : till the positionInTarget of last cluster record will be < truncationPoint
     *
     * @param byIndex         determine truncation type
     * @param seedPositions   seed positions
     * @param results         cluster data
     * @param data            records data
     * @param dataFrom        use only [dataFrom..] records from data
     * @param clusterPointer  pointer to target cluster
     * @param truncationPoint main truncation threshold
     * @return {@literal true} if cluster remains to have score above the threshold (absoluteMinClusterScore);
     * {@literal false} if cluster was removed due to too low score
     */
    private boolean truncateClusterFromLeft(
            boolean byIndex,
            final IntArrayList seedPositions,
            final IntArrayList results, final int[] data,
            final int dataFrom, final int clusterPointer, final int truncationPoint) {
        // Collecting stat
        if (stat != null)
            stat.trimmingEvent(byIndex ? TrimLeftQuery : TrimLeftTarget);

        int firstRecordId = results.get(clusterPointer + FIRST_RECORD_ID),
                recordId = results.get(clusterPointer + LAST_RECORD_ID), // Going from the right side of the cluster
                record = data[recordId],
                prevOffset = offset(record),
                prevIndex = index(record),
                score = matchScore;

        // Last record id is inside the working range of data array
        assert firstRecordId >= dataFrom;

        // Roll right in stretch
        int i = recordId;
        while (--i >= firstRecordId && prevIndex == index(data[i])) ;

        // Calculate score and searching for truncation point
        int offset, index;
        for (; i >= firstRecordId; --i) {
            // Detecting truncation point in case of by-index truncation
            if (byIndex && index(data[i]) <= truncationPoint)
                break;

            index = index(data[i]);
            offset = offset(data[i]);
            if (inDelta(prevOffset, offset, maxClusterIndels)) {
                // Processing exceptional cases for self-correlated K-Mers
                // If next record has same index and better offset
                // (closer to current island LAST_VALUE)
                if (i > firstRecordId
                        && index == index(data[i - 1])
                        && abs(prevOffset - offset) > abs(prevOffset - offset(data[i - 1])))
                    // Skip current record
                    continue;

                // Indices are sorted in ascending order
                assert prevIndex - index - 1 >= 0;

                // Detecting truncation point for by-position-in-target truncation
                if (!byIndex && positionInTarget(seedPositions, data[i]) <= truncationPoint)
                    break;

                // Score for this point
                int scoreDelta = matchScore + (prevIndex - index - 1) * mismatchScore +
                        abs(prevOffset - offset) * offsetShiftScore;

                // Count record only if it adds score to this cluster
                if (scoreDelta > 0) {
                    score += scoreDelta;
                    prevOffset = offset;
                    prevIndex = index;
                    recordId = i;
                }

                // Skipping other records with the same index (they must have worse offsets)
                while (--i >= firstRecordId && prevIndex == index(data[i])) ;
            }
        }

        // Adjusting cluster data
        results.set(clusterPointer + FIRST_RECORD_ID, recordId);
        results.set(clusterPointer + SCORE, score);

        // Testing if cluster still have enough score
        if (score < absoluteMinClusterScore) {
            // dropping cluster if not
            results.set(clusterPointer + FIRST_RECORD_ID, DROPPED_CLUSTER);
            // and returning false
            return false;
        }

        // Score is still above the threshold
        return true;
    }

    private KMappingHit2 calculateHit(final int id, final int[] data,
                                      final int dataFrom, final int dataTo,
                                      final IntArrayList seedPositions) {
        ThreadLocalCache cache = memoryCache.get();
        OffsetPacksAccumulator accumulator = cache.offsetPacksAccumulator;
        accumulator.calculateInitialPartitioning(data, dataFrom, dataTo);

        IntArrayList results = accumulator.results;
        if (accumulator.results.size() == 0 || accumulator.totalScore < absoluteMinScore)
            return null;

        // Collecting statistics
        if (stat != null)
            stat.initialClusters(id, results);

        //A1: correcting intersections, step 1
        OUT:
        for (int i = 0; i < results.size(); i += OUTPUT_RECORD_SIZE)
            if (results.get(i + FIRST_RECORD_ID) != DROPPED_CLUSTER)
                for (int j = i + OUTPUT_RECORD_SIZE; j < results.size(); j += OUTPUT_RECORD_SIZE) {
                    if (results.get(i + FIRST_RECORD_ID) == DROPPED_CLUSTER)
                        continue OUT;
                    if (results.get(j + FIRST_RECORD_ID) == DROPPED_CLUSTER)
                        continue;
                    //intersecting clusters in query
                    int a = i, b = j;
                    int aStartIndex = index(data[results.get(a + FIRST_RECORD_ID)]);
                    int aEndIndex = index(data[results.get(a + LAST_RECORD_ID)]);
                    int bStartIndex = index(data[results.get(b + FIRST_RECORD_ID)]);
                    int bEndIndex = index(data[results.get(b + LAST_RECORD_ID)]);

                    if (aStartIndex > bStartIndex) {
                        //swap by xor
                        aStartIndex ^= bStartIndex;
                        bStartIndex ^= aStartIndex;
                        aStartIndex ^= bStartIndex;
                        aEndIndex ^= bEndIndex;
                        bEndIndex ^= aEndIndex;
                        aEndIndex ^= bEndIndex;
                        a ^= b;
                        b ^= a;
                        a ^= b;
                    }

                    if (aEndIndex >= bStartIndex) {
                        if (bEndIndex <= aEndIndex) {
                            if (results.get(a + SCORE) < results.get(b + SCORE)) {
                                results.set(a + FIRST_RECORD_ID, DROPPED_CLUSTER);
                                continue;
                            } else {
                                results.set(b + FIRST_RECORD_ID, DROPPED_CLUSTER);
                                continue;
                            }
                        } else {
                            if (results.get(a + SCORE) < results.get(b + SCORE)) {
                                if (!truncateClusterFromRight(true, null, results, data, dataTo, a, bStartIndex))
                                    continue;
                            } else if (!truncateClusterFromLeft(true, null, results, data, dataFrom, b, aEndIndex))
                                continue;
                        }
                    }

                    //intersecting clusters in target
                    a = i;
                    b = j;
                    int aStart = positionInTarget(seedPositions, data[results.get(a + FIRST_RECORD_ID)]);
                    int aEnd = positionInTarget(seedPositions, data[results.get(a + LAST_RECORD_ID)]);
                    int bStart = positionInTarget(seedPositions, data[results.get(b + FIRST_RECORD_ID)]);
                    int bEnd = positionInTarget(seedPositions, data[results.get(b + LAST_RECORD_ID)]);

                    if (aStart > bStart) {
                        //swap by xor
                        aStart ^= bStart;
                        bStart ^= aStart;
                        aStart ^= bStart;
                        aEnd ^= bEnd;
                        bEnd ^= aEnd;
                        aEnd ^= bEnd;
                        a ^= b;
                        b ^= a;
                        a ^= b;
                    }


                    if (aEnd >= bStart) {
                        if (bEnd <= aEnd) {
                            if (results.get(a + SCORE) < results.get(b + SCORE))
                                results.set(a + FIRST_RECORD_ID, DROPPED_CLUSTER);
                            else
                                results.set(b + FIRST_RECORD_ID, DROPPED_CLUSTER);
                        } else {
                            if (results.get(a + SCORE) < results.get(b + SCORE))
                                truncateClusterFromRight(false, seedPositions, results, data, dataTo, a, bStart);
                            else
                                truncateClusterFromLeft(false, seedPositions, results, data, dataFrom, b, aEnd);
                        }
                    }
                }

        // Collecting statistics
        if (stat != null)
            stat.afterTrimming(id, results);

        //A2: correcting intersections, step 2 untangling
        int bestScore = 0, currentScore;

        int numberOfClusters = results.size() / OUTPUT_RECORD_SIZE;
        for (int i = 0; i < results.size(); i += OUTPUT_RECORD_SIZE) {
            if (results.get(i + FIRST_RECORD_ID) == DROPPED_CLUSTER
                    || crosses(seedPositions, data, results.get(i + FIRST_RECORD_ID), results.get(i + LAST_RECORD_ID))) {
                --numberOfClusters;
                results.set(i + FIRST_RECORD_ID, DROPPED_CLUSTER);
                results.set(i + SCORE, Integer.MIN_VALUE);
            }
        }

        if (numberOfClusters == 0)
            return null;

        final long[] forPreFiltering = new long[numberOfClusters];
        int j = 0;
        for (int i = 0; i < results.size(); i += OUTPUT_RECORD_SIZE)
            if (results.get(i + FIRST_RECORD_ID) != DROPPED_CLUSTER)
                forPreFiltering[j++] = i | (((long) (-results.get(i + SCORE))) << 33);
        assert j == numberOfClusters;
        Arrays.sort(forPreFiltering);

        numberOfClusters = min(numberOfClusters, maxClusters);

        IntArrayList untangled = cache.cachedIntArray1,
                current = cache.cachedIntArray2;
        untangled.clear();
        current.clear();

        OUTER:
        for (long it = 0, size = (1L << numberOfClusters); it < size; ++it) {
            current.clear();
            currentScore = 0;
            for (int ai = numberOfClusters - 1; ai >= 0; --ai) {
                //int a = ai * OUTPUT_RECORD_SIZE;
                int a = (int) forPreFiltering[ai];
                if (((it >> ai) & 1) == 1) {
                    if (results.get(a + FIRST_RECORD_ID) == DROPPED_CLUSTER) {
                        it += ((1 << ai) - 1);
                        continue OUTER;
                    }

                    for (int i = 0; i < current.size(); i++) {
                        int b = current.get(i);
                        if (results.get(b + FIRST_RECORD_ID) == DROPPED_CLUSTER)
                            continue;
                        if (crosses(seedPositions, data, results.get(a + FIRST_RECORD_ID), results.get(b + FIRST_RECORD_ID)) ||
                                crosses(seedPositions, data, results.get(a + LAST_RECORD_ID), results.get(b + LAST_RECORD_ID))) {
                            assert crosses(seedPositions, data, results.get(a + FIRST_RECORD_ID), results.get(b + LAST_RECORD_ID))
                                    || crosses(seedPositions, data, results.get(a + LAST_RECORD_ID), results.get(b + FIRST_RECORD_ID));
                            //it += ((1 << ai) - 1);
                            continue OUTER;
                        }
                    }
                    current.add(a);
                    currentScore += results.get(a + SCORE);
                }
            }
            if (bestScore < currentScore) {
                untangled.copyFrom(current);
                bestScore = currentScore;
            }
        }

        current.clear();//economy
        IntArrayList seedRecords = current;
        IntArrayList packBoundaries = cache.cachedIntArray3;
        packBoundaries.clear();
        int score = 0;

        // Collecting statistics
        if (stat != null)
            stat.afterUntangling(untangled);

        final long[] untangledForSort = new long[untangled.size()];
        for (int i = 0; i < untangled.size(); ++i) {
            int pointer = untangled.get(i);
            untangledForSort[i] = pointer | (((long) index(data[results.get(pointer + FIRST_RECORD_ID)])) << 32);
        }
        Arrays.sort(untangledForSort);

        for (int i = 0; i < untangledForSort.length; ++i) {
            int pointer = (int) (untangledForSort[i]);
            if (i != 0) {
                packBoundaries.add(seedRecords.size());
                score += extraClusterScore;
            }

            int recordId = results.get(pointer + FIRST_RECORD_ID);
            assert recordId >= dataFrom;

            int lastRecordId = results.get(pointer + LAST_RECORD_ID);
            assert lastRecordId < dataTo;

            int record, index, offset, delta;
            int clusterScore = matchScore;

            int previousIndex = index = index(record = data[recordId]);
            seedRecords.add(record);

            int previousOffset = offset(record);

            while (++recordId < dataTo && index(data[recordId]) == index) ;

            --recordId;
            while (++recordId <= lastRecordId) {
                record = data[recordId];
                index = index(record);
                offset = offset(record);

                int minRecord = record;
                int minDelta = abs(offset - previousOffset);

                if (minDelta > maxClusterIndels)
                    continue;

                boolean $ = false;
                while (recordId < lastRecordId && index(record = data[recordId + 1]) == index) {
                    ++recordId;
                    if ((delta = abs(offset(record) - previousOffset)) < minDelta) {
                        minDelta = delta;
                        minRecord = record;
                        $ = true;
                    }
                }
                if ($) offset = offset(minRecord);

                // Detection of micro-tangling (between adjacent seeds)
                if (positionInTarget(seedPositions, minRecord) <= positionInTarget(seedPositions, seedRecords.last())) {
                    // Removing right seed from micro-tangle
                    int minRecordId = recordId + 1;
                    while (data[--minRecordId] != minRecord) ;
                    System.arraycopy(data, minRecordId + 1, data, minRecordId, dataTo - minRecordId - 1);

                    // Collecting statistics about rerun event
                    if (stat != null)
                        stat.reRunBecauseOfMicroTangling();

                    // Restarting whole algorithm
                    return calculateHit(id, data, dataFrom, dataTo - 1, seedPositions);
                }

                int scoreDelta = matchScore + (index - previousIndex - 1) * mismatchScore +
                        minDelta * offsetShiftScore;
                if (scoreDelta > 0) {
                    clusterScore += scoreDelta;
                    seedRecords.add(minRecord);
                    previousIndex = index;
                    previousOffset = offset;
                }
            }

            // TODO point for counter : clusterScore == results.get(pointer + SCORE)
            clusterScore = max(clusterScore, results.get(pointer + SCORE));
            score += clusterScore;
        }

        if (floatingLeftBound)
            score -= index(seedRecords.get(0)) * mismatchScore;

        if (floatingRightBound)
            score -= (seedPositions.size() - 1 - index(seedRecords.last())) * mismatchScore;

        if (score < absoluteMinScore)
            return null;

        return new KMappingHit2(id, seedRecords.toArray(), packBoundaries.toArray(), score);
    }

    private static boolean crosses(final IntArrayList seedPositions, final int[] data, final int a, final int b) {
        return (index(data[a]) < index(data[b])) ^
                (positionInTarget(seedPositions, data[a]) < positionInTarget(seedPositions, data[b]));
    }

    public static int positionInTarget(final IntArrayList seedPositions,
                                       final int record) {
        return seedPositions.get(index(record)) + offset(record);
    }

    /**
     * Returns number of nucleotides in kMer (value of k)
     *
     * @return number of nucleotides in kMer (value of k)
     */

    public int getNValue() {
        return nValue;
    }

    public int getKValue() {
        return kValue;
    }

    /**
     * Returns minimal score for the cluster
     *
     * @return minimal score for the cluster
     */
    public int getAbsoluteMinClusterScore() {
        return absoluteMinClusterScore;
    }

    public int getExtraClusterScore() {
        return extraClusterScore;
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
     * Returns maximal ratio between best hit score and other hits scores in returned result
     *
     * @return maximal ratio between best hit score and other hits scores in returned result
     */
    public float getRelativeMinScore() {
        return relativeMinScore;
    }

    static int index(final int record) {
        return record & indexMask;
    }

    static int offset(final int record) {
        return record >> bitsForIndex;
    }

    static int record(final int offset, final int index) {
        return (offset << bitsForIndex) | index;
    }

    static String recordToString(int record, IntArrayList seedPositions) {
        return "O=" + offset(record) + " Q" + seedPositions.get(index(record)) + "->T" + positionInTarget(seedPositions, record);
    }

    static boolean inDelta(final int a, final int b, final int maxAllowedDelta) {
        int diff = a - b;
        return -maxAllowedDelta <= diff && diff <= maxAllowedDelta;
    }

    private static int getCombinationMask(final int[] combination) {
        int c = 0;
        for (int a : combination)
            c |= (1 << a);
        return c;
    }

    /**
     * Method used internally.
     */
    public SummaryStatistics getRecordSizeSummaryStatistics() {
        SummaryStatistics ss = new SummaryStatistics();
        for (int[] length : lengths)
            for (int len : length)
                ss.addValue(len);
        return ss;
    }

    @Override
    public String toString() {
        SummaryStatistics ss = getRecordSizeSummaryStatistics();
        return "K=" + nValue + "; Avr=" + ss.getMean() + "; SD=" + ss.getStandardDeviation();
    }

    static final class ArrList<T> extends ArrayList<T> {
        public ArrList() {
        }

        @Override
        public void removeRange(int fromIndex, int toIndex) {
            super.removeRange(fromIndex, toIndex);
        }
    }

    private static final class ThreadLocalCache {
        final IntArrayList seedPositions;
        final IntArrayList cachedIntArray1, cachedIntArray2, cachedIntArray3;
        final IntArrayList[] candidates;
        final OffsetPacksAccumulator offsetPacksAccumulator;

        public ThreadLocalCache(int sequencesInBase, int slotCount, int maxClusterIndels, int matchScore,
                                int mismatchScore, int offsetShiftScore, int absoluteMinClusterScore) {
            this.seedPositions = new IntArrayList();
            this.cachedIntArray1 = new IntArrayList();
            this.cachedIntArray2 = new IntArrayList();
            this.cachedIntArray3 = new IntArrayList();

            this.candidates = new IntArrayList[sequencesInBase];

            for (int i = 0; i < sequencesInBase; i++)
                this.candidates[i] = new IntArrayList();

            this.offsetPacksAccumulator = new OffsetPacksAccumulator(
                    slotCount, maxClusterIndels, matchScore,
                    mismatchScore, offsetShiftScore, absoluteMinClusterScore);
        }

        public void reset() {
            seedPositions.clear();
            cachedIntArray1.clear();
            cachedIntArray2.clear();
            cachedIntArray3.clear();
            for (IntArrayList candidate : candidates)
                candidate.clear();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
