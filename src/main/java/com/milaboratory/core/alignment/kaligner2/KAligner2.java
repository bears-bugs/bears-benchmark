package com.milaboratory.core.alignment.kaligner2;

import cc.redberry.primitives.Filter;
import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.*;
import com.milaboratory.core.alignment.batch.BatchAlignerWithBaseWithFilter;
import com.milaboratory.core.alignment.kaligner2.KMapper2.ArrList;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.MutationsBuilder;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.BitArray;
import com.milaboratory.util.IntArrayList;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class KAligner2<P> implements BatchAlignerWithBaseWithFilter<NucleotideSequence, P, KAlignmentHit2<P>> {
    /**
     * Link to KMapper
     */
    final KMapper2 mapper;
    /**
     * Parameters of alignment
     */
    final KAlignerParameters2 parameters;
    /**
     * Base records for reference sequences
     */
    final List<NucleotideSequence> sequences = new ArrayList<>();
    /**
     * Record payloads.
     */
    final TIntObjectHashMap<P> payloads = new TIntObjectHashMap<>();
    /**
     * Statistics aggregator
     */
    private final KAligner2Statistics stat;

    public KAligner2(KAlignerParameters2 parameters) {
        this(parameters, null);
    }

    public KAligner2(KAlignerParameters2 parameters, KAligner2Statistics stat) {
        this.parameters = parameters;
        this.mapper = KMapper2.createFromParameters(parameters, stat);
        this.stat = stat;
    }

    /**
     * Adds new reference sequence to the base of this mapper and returns index assigned to it.
     *
     * @param sequence sequence
     * @return index assigned to the sequence
     */
    public int addReference(NucleotideSequence sequence) {
        if (sequence.containWildcards())
            throw new IllegalArgumentException("Reference sequences with wildcards not supported.");
        int id = mapper.addReference(sequence);
        assert sequences.size() == id;
        sequences.add(sequence);
        return id;
    }

    @Override
    public BitArray createFilter(Filter<P> filter) {
        BitArray ret = new BitArray(sequences.size());
        TIntObjectIterator<P> it = payloads.iterator();
        while (it.hasNext()) {
            it.advance();
            if (filter.accept(it.value()))
                ret.set(it.key());
        }
        return ret;
    }

    /**
     * Returns sequence by its id (order number) in a base.
     *
     * @param id id of sequence to be returned
     * @return sequence
     */
    public NucleotideSequence getReference(int id) {
        return sequences.get(id);
    }

    @Override
    public void addReference(NucleotideSequence sequence, P payload) {
        payloads.put(addReference(sequence), payload);
    }

    @Override
    public KAlignmentResult2<P> align(NucleotideSequence sequence) {
        return align(sequence, 0, sequence.size());
    }

    @Override
    public KAlignmentResult2<P> align(NucleotideSequence sequence, int from, int to) {
        return align(sequence, from, to, null);
    }

    @Override
    public KAlignmentResult2<P> align(final NucleotideSequence query, final int from, final int to, BitArray filter) {
        if (stat != null)
            stat.nextQuery();

        final BandedAffineAligner.MatrixCache cache = new BandedAffineAligner.MatrixCache();

        final AffineGapAlignmentScoring<NucleotideSequence> scoring = parameters.getScoring();

        // Saving to local variables for performance
        final KMappingResult2 mapping = mapper.align(query, from, to, filter);
        final IntArrayList seeds = mapping.seeds;

        ArrList<KAlignmentHit2<P>> hits = new ArrList<>();

        final int maxIndels = parameters.getMapperMaxClusterIndels();
        final int nValue = mapper.getNValue();
        final boolean kIsZero = (mapper.getKValue() == 0);

        final int halfN = nValue / 2;
        final int leftBoundaryOffset = kIsZero ? 0 : halfN;
        final int rightBoundaryOffset = kIsZero ? nValue : halfN;
        final int rightLeftDeltaBoundaryOffset = rightBoundaryOffset - leftBoundaryOffset;

        KAlignmentResult2<P> kAlignmentResult = new KAlignmentResult2<>(mapping, hits, query, from, to);
        if (mapping.getHits().isEmpty()) {
            if (stat != null)
                stat.kAlignerResult(kAlignmentResult);

            return kAlignmentResult;
        }

        int length1, length2, added1, added2, offset1, offset2, delta;
        int seq1From, seq1To, seq2From, seq2To;

        for (int hitIndex = 0; hitIndex < mapping.getHits().size(); hitIndex++) {
            final KMappingHit2 mappingHit = mapping.getHits().get(hitIndex);
            final NucleotideSequence target = sequences.get(mappingHit.id);
            final MutationsBuilder<NucleotideSequence> mutations =
                    new MutationsBuilder<>(NucleotideSequence.ALPHABET);

            //Left edge alignment
            int seedPosition2 = seeds.get(mappingHit.indexById(0)) + leftBoundaryOffset;
            int seedPosition1 = seedPosition2 + mappingHit.offsetById(0);

            length1 = seedPosition1;
            length2 = seedPosition2 - from;
            assert length2 >= 0;

            if (length1 >= length2) {
                delta = Math.min(length1 - length2, maxIndels);
                added1 = maxIndels + delta;
                added2 = maxIndels - delta;
                if (length1 > length2 + maxIndels)
                    length1 = length2 + maxIndels;
            } else {
                delta = Math.min(length2 - length1, maxIndels);
                added1 = maxIndels - delta;
                added2 = maxIndels + delta;
                if (length2 > length1 + maxIndels)
                    length2 = length1 + maxIndels;
            }

            offset1 = seedPosition1 - length1;
            offset2 = seedPosition2 - length2;

            BandedSemiLocalResult br;
            if (parameters.isFloatingLeftBound()) {
                br = BandedAffineAligner.semiLocalLeft0(parameters.getScoring(), target, query,
                        offset1, length1,
                        offset2, length2,
                        maxIndels, mutations, cache);
            } else {
                br = BandedAffineAligner.semiGlobalLeft0(parameters.getScoring(), target, query,
                        offset1, length1, added1,
                        offset2, length2, added2,
                        maxIndels, mutations, cache);
            }
            seq1From = br.sequence1Stop;
            seq2From = br.sequence2Stop;

            int previousSeedPosition2 = seedPosition2 + rightLeftDeltaBoundaryOffset,
                    previousSeedPosition1 = seedPosition1 + rightLeftDeltaBoundaryOffset;

            //boolean first = true;
            for (int seedId = 1; seedId < mappingHit.seedRecords.length; seedId++) {
                seedPosition2 = seeds.get(mappingHit.indexById(seedId)) + leftBoundaryOffset;
                seedPosition1 = seedPosition2 + mappingHit.offsetById(seedId);

                offset1 = previousSeedPosition1;
                length1 = seedPosition1 - offset1;

                offset2 = previousSeedPosition2;
                length2 = seedPosition2 - offset2;

                assert !kIsZero || target.getRange(offset1 - nValue, offset1).equals(query.getRange(offset2 - nValue, offset2));

                if (length2 < 0 || length1 < 0)
                    continue;

                assert length1 >= 0 && length2 >= 0;

                //if (!kIsZero)
                //    Aligner.alignOnlySubstitutions0(target, query, previousSeedPosition1, nValue, previousSeedPosition2, nValue,
                //            scoring, mutations);
                //first = false;

                BandedAffineAligner.align0(scoring, target, query,
                        offset1, length1,
                        offset2, length2,
                        maxIndels, mutations, cache);

                previousSeedPosition1 = seedPosition1 + rightLeftDeltaBoundaryOffset;
                previousSeedPosition2 = seedPosition2 + rightLeftDeltaBoundaryOffset;
            }

            //Right edge
            //if (!kIsZero && !first)
            //    Aligner.alignOnlySubstitutions0(target, query, previousSeedPosition1, nValue, previousSeedPosition2, nValue,
            //            scoring, mutations);

            offset2 = previousSeedPosition2;
            offset1 = previousSeedPosition1;

            length1 = target.size() - offset1;
            length2 = to - offset2;

            if (length1 >= length2) {
                delta = Math.min(length1 - length2, maxIndels);
                added1 = maxIndels + delta;
                added2 = maxIndels - delta;
                if (length1 > length2 + maxIndels)
                    length1 = length2 + maxIndels;
            } else {
                delta = Math.min(length2 - length1, maxIndels);
                added1 = maxIndels - delta;
                added2 = maxIndels + delta;
                if (length2 > length1 + maxIndels)
                    length2 = length1 + maxIndels;
            }

            if (parameters.isFloatingRightBound()) {
                br = BandedAffineAligner.semiLocalRight0(parameters.getScoring(), target, query,
                        offset1, length1,
                        offset2, length2,
                        maxIndels, mutations, cache);
            } else {
                br = BandedAffineAligner.semiGlobalRight0(parameters.getScoring(), target, query,
                        offset1, length1, added1,
                        offset2, length2, added2,
                        maxIndels, mutations, cache);
            }

            seq1To = br.sequence1Stop + 1;
            seq2To = br.sequence2Stop + 1;

            Mutations<NucleotideSequence> muts = mutations.createAndDestroy();
            hits.add(new KAlignmentHit2<>(kAlignmentResult, mappingHit.id,
                    new Alignment<>(target, muts,
                            new Range(seq1From, seq1To),
                            new Range(seq2From, seq2To),
                            parameters.getScoring()),
                    payloads.get(mappingHit.id)));
        }

        Collections.sort(hits, SCORE_COMPARATOR);
        int threshold = (int) Math.max(parameters.getAbsoluteMinScore(),
                parameters.getRelativeMinScore() * hits.get(0).getAlignment().getScore());
        int i = 0;
        for (; i < parameters.getMaxHits() && i < hits.size(); ++i)
            if (hits.get(i).getAlignment().getScore() < threshold)
                break;
        if (i < hits.size())
            hits.removeRange(i, hits.size());

        if (stat != null)
            stat.kAlignerResult(kAlignmentResult);

        return kAlignmentResult;
    }

    private static final Comparator<KAlignmentHit2> SCORE_COMPARATOR = new Comparator<KAlignmentHit2>() {
        @Override
        public int compare(KAlignmentHit2 o1, KAlignmentHit2 o2) {
            return Double.compare(o2.alignment.getScore(), o1.alignment.getScore());
        }
    };
}
