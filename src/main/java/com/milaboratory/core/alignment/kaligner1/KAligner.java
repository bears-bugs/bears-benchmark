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

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.OutputPort;
import cc.redberry.pipe.Processor;
import cc.redberry.pipe.blocks.ParallelProcessor;
import cc.redberry.primitives.Filter;
import com.milaboratory.core.alignment.batch.*;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.BitArray;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>KAligner is a comprehensive aligner for nucleotide sequences.
 *
 * <p>Complete alignment of sequence performed using {@link KMapper#addReference(com.milaboratory.core.sequence.NucleotideSequence,
 * int, int)}
 * method from which preliminary hits are obtained and used by {@link #align(com.milaboratory.core.sequence.NucleotideSequence)},
 * {@link #align(com.milaboratory.core.sequence.NucleotideSequence, int, int)},
 * {@link #align(com.milaboratory.core.sequence.NucleotideSequence, int, int, boolean, BitArray)}
 * methods.</p>
 *
 * <p>All settings are stored in {@link #parameters} property.</p>
 */
public class KAligner<P> implements PipedBatchAlignerWithBase<NucleotideSequence, P, KAlignmentHit<P>>,
        BatchAlignerWithBaseWithFilter<NucleotideSequence, P, KAlignmentHit<P>>,
        java.io.Serializable {
    /**
     * Link to KMapper
     */
    final KMapper mapper;
    /**
     * Parameters of alignment
     */
    final KAlignerParameters parameters;
    /**
     * Base records for reference sequences
     */
    final List<NucleotideSequence> sequences = new ArrayList<>();
    /**
     * Record payloads.
     */
    final TIntObjectHashMap<P> payloads = new TIntObjectHashMap<>();
    /**
     * Flag indicating how to load final alignments - at first request or immediately after obtaining {@link
     * KAlignmentResult}
     */
    final boolean lazyResults;
    /**
     * Number fo threads to use in piped processing.
     */
    volatile int threads = 1;

    /**
     * <p>Creates new KAligner.</p>
     *
     * <p>Sets {@link #lazyResults} flag to {@code false}, which means that all alignments inside {@link
     * KAlignmentResult}
     * obtained by {@link KAligner#align(com.milaboratory.core.sequence.NucleotideSequence,
     * int, int, boolean, BitArray)} method
     * will be loaded immediately.
     * </p>
     *
     * @param parameters parameters from which new KAligner needs to be created
     */
    public KAligner(KAlignerParameters parameters) {
        this(parameters, false);
    }

    /**
     * <p>Creates new KAligner.</p>
     *
     * @param parameters  parameters from which new KAligner needs to be created
     * @param lazyResults {@code true} if all alignments inside {@link KAlignmentResult}
     *                    obtained by {@link KAligner#align(com.milaboratory.core.sequence.NucleotideSequence,
     *                    int, int, boolean, BitArray)} method
     *                    need to be loaded at first request
     */
    public KAligner(KAlignerParameters parameters, boolean lazyResults) {
        this.mapper = KMapper.createFromParameters(parameters);
        this.parameters = parameters.clone();
        this.lazyResults = lazyResults;
    }

    /**
     * Sets number of threads to be used in piped processing.<br><br>
     *
     * 0    -> Runtime.getRuntime().availableProcessors()<br>
     * 1    -> process in the same thread as take() method call<br>
     * 2... -> parallel processor<br>
     */
    public void setThreadCount(int threads) {
        this.threads = threads;
    }

    /**
     * Adds new reference sequence to the base of this aligner and returns index assigned to it.
     *
     * @param sequence sequence
     * @return index assigned to the sequence
     */
    public int addReference(NucleotideSequence sequence) {
        return addReference(sequence, 0, sequence.size());
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
     * Adds new reference sequence to the base of this mapper and returns index assigned to it.
     * <p/>
     * <p>User can specify a part of a sequence to be indexed by {@link KMapper},
     * but {@link KAligner} stores whole adding sequences.</p>
     *
     * @param sequence sequence
     * @param offset   offset of subsequence to be indexed by {@link KMapper}
     * @param length   length of subsequence to be indexed by {@link KMapper}
     * @return index assigned to the sequence
     */
    public int addReference(NucleotideSequence sequence, int offset, int length) {
        if (sequence.containWildcards())
            throw new IllegalArgumentException("Reference sequences with wildcards not supported.");
        int id = mapper.addReference(sequence, offset, length);
        assert sequences.size() == id;
        sequences.add(sequence);
        return id;
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

    /**
     * Performs a comprehensive alignment of a sequence.
     * <p/>
     * <p>The procedure consists of 2 steps:</p>
     * <ul>
     * <li>1. Obtaining {@link KMappingResult} from {@link
     * KMapper}
     * using {@link KMapper#align(com.milaboratory.core.sequence.NucleotideSequence)}
     * which contains preliminary hits
     * </li>
     * <li>2. Using {@link KMappingResult} from step 1, obtaining {@link
     * KAlignmentResult}
     * by {@link #align(com.milaboratory.core.sequence.NucleotideSequence, int, int, boolean, BitArray)} method,
     * where all hit alignments may be loaded lazily (at first request) or immediately (depends on {@link #lazyResults}
     * flag value)
     * </li>
     * </ul>
     *
     * @param sequence sequence to be aligned
     * @return a list of hits found in target sequence
     */
    public KAlignmentResult<P> align(NucleotideSequence sequence) {
        return align(sequence, 0, sequence.size());
    }

    /**
     * Performs a comprehensive alignment of a sequence.
     * <p/>
     * <p>The procedure consists of 2 steps:</p>
     * <ul>
     * <li>1. Obtaining {@link KMappingResult} from {@link
     * KMapper}
     * using {@link KMapper#align(com.milaboratory.core.sequence.NucleotideSequence)}
     * which contains preliminary hits
     * </li>
     * <li>2. Using {@link KMappingResult} from step 1, obtaining {@link
     * KAlignmentResult}
     * by {@link #align(com.milaboratory.core.sequence.NucleotideSequence, int, int, boolean, BitArray)} method,
     * where all hit alignments may be loaded lazily (at first request) or immediately (depends on {@link #lazyResults}
     * flag value)
     * </li>
     * </ul>
     *
     * @param sequence sequence to be aligned
     * @param from     first nucleotide to be aligned (inclusive)
     * @param to       last nucleotide to be aligned (exclusive)
     * @return a list of hits found in target sequence
     */
    @Override
    public KAlignmentResult<P> align(NucleotideSequence sequence, int from, int to) {
        return align(sequence, from, to, true, null);
    }

    /**
     * Performs a comprehensive alignment of a sequence.
     * <p/>
     * <p>The procedure consists of 2 steps:</p>
     * <ul>
     * <li>1. Obtaining {@link KMappingResult} from {@link
     * KMapper}
     * using {@link KMapper#align(com.milaboratory.core.sequence.NucleotideSequence)}
     * which contains preliminary hits
     * </li>
     * <li>2. Using {@link KMappingResult} from step 1, obtaining {@link
     * KAlignmentResult}
     * by {@link #align(com.milaboratory.core.sequence.NucleotideSequence, int, int, boolean, BitArray)} method,
     * where all hit alignments may be loaded lazily (at first request) or immediately (depends on {@link #lazyResults}
     * flag value)
     * </li>
     * </ul>
     *
     * @param sequence        sequence to be aligned
     * @param from            first nucleotide to be aligned by {@link KMapper}
     *                        (inclusive)
     * @param to              last nucleotide to be aligned by {@link KMapper}
     *                        (exclusive)
     * @param restrictToRange {@code} true if hits alignments from obtained {@link KAlignmentResult}
     *                        should be
     *                        restricted by the same range ({@code from} - {@code to})
     * @return a list of hits found in target sequence
     */
    public KAlignmentResult<P> align(NucleotideSequence sequence, int from, int to, boolean restrictToRange, BitArray filter) {
        KMappingResult kResult = mapper.align(sequence, from, to, filter);

        KAlignmentResult<P> result;
        if (restrictToRange)
            result = new KAlignmentResult<>(this, kResult, sequence, from, to);
        else
            result = new KAlignmentResult<>(this, kResult, sequence, 0, sequence.size());

        if (!lazyResults)
            result.calculateAllAlignments();
        else
            result.sortAccordingToMapperScores();

        return result;
    }

    @Override
    public AlignmentResult<KAlignmentHit<P>> align(NucleotideSequence sequence, int from, int to, BitArray filter) {
        return align(sequence, from, to, true, filter);
    }

    @Override
    public <Q> OutputPort<KAlignmentResultP<P, Q>> align(OutputPort<Q> input,
                                                         final SequenceExtractor<Q, NucleotideSequence> extractor) {
        if (lazyResults)
            throw new IllegalStateException("Piped processing is supported for lazy results.");

        Processor<Q, KAlignmentResultP<P, Q>> proc = new Processor<Q, KAlignmentResultP<P, Q>>() {
            @Override
            public KAlignmentResultP<P, Q> process(Q input) {
                NucleotideSequence seq = extractor.extract(input);
                KMappingResult kResult = mapper.align(seq);
                return new KAlignmentResultP<>(input, KAligner.this, kResult, seq, 0, seq.size());
            }
        };


        return wrapPipe(proc, input);
    }

    @Override
    public <Q extends HasSequence<NucleotideSequence>> OutputPort<KAlignmentResultP<P, Q>> align(OutputPort<Q> input) {
        if (lazyResults)
            throw new IllegalStateException("Piped processing is supported for lazy results.");

        Processor<Q, KAlignmentResultP<P, Q>> proc = new Processor<Q, KAlignmentResultP<P, Q>>() {
            @Override
            public KAlignmentResultP<P, Q> process(Q input) {
                NucleotideSequence seq = input.getSequence();
                KMappingResult kResult = mapper.align(seq);
                return new KAlignmentResultP<>(input, KAligner.this, kResult, seq, 0, seq.size());
            }
        };

        return wrapPipe(proc, input);
    }

    @Override
    public void addReference(NucleotideSequence sequence, P payload) {
        int id = addReference(sequence);
        payloads.put(id, payload);
    }

    private <Q> OutputPort<KAlignmentResultP<P, Q>> wrapPipe(Processor<Q, KAlignmentResultP<P, Q>> proc, OutputPort<Q> input) {
        if (threads == 1)
            return CUtils.wrap(input, proc);

        int t = (threads == 0 ? Runtime.getRuntime().availableProcessors() : threads);

        return new ParallelProcessor<>(input, proc, t);
    }
}
