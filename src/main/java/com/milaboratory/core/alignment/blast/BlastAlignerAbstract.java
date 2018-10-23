package com.milaboratory.core.alignment.blast;

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.OutputPort;
import cc.redberry.pipe.Processor;
import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.alignment.batch.*;
import com.milaboratory.core.sequence.Sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BlastAlignerAbstract<S extends Sequence<S>, P, H extends BlastHit<S, P>> implements PipedBatchAlignerWithBase<S, P, H> {
    private final List<S> sequenceList = new ArrayList<>();
    private final Map<String, S> sequences = new HashMap<>();
    private final Map<String, P> payloads = new HashMap<>();

    // Parameters
    private final BlastAlignerParameters parameters;

    // Not initialized -> null
    private volatile BlastDB db = null;
    private volatile BlastAlignerExt<S> aligner = null;
    private volatile int processCount = 1;

    public BlastAlignerAbstract() {
        this(null);
    }

    public BlastAlignerAbstract(BlastAlignerParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Sets the number of concurrent BLAST processes to serve a single alignment session (single {@link
     * #align(OutputPort)} or {@link #align(OutputPort, SequenceExtractor method invocation)}.
     *
     * @param processCount number of concurrent processes
     */
    public void setConcurrentBlastProcessCount(int processCount) {
        this.processCount = processCount;
    }

    @Override
    public <Q> OutputPort<? extends PipedAlignmentResult<H, Q>> align(OutputPort<Q> input, SequenceExtractor<Q, S> extractor) {
        ensureInit();
        OutputPort<PipedAlignmentResult<BlastHitExt<S>, Q>> iResults = aligner.align(input, extractor);
        return CUtils.wrap(iResults, new ResultsConverter<Q>());
    }

    @Override
    public <Q extends HasSequence<S>> OutputPort<PipedAlignmentResult<H, Q>> align(OutputPort<Q> input) {
        ensureInit();
        OutputPort<PipedAlignmentResult<BlastHitExt<S>, Q>> iResults = aligner.align(input);
        return CUtils.wrap(iResults, new ResultsConverter<Q>());
    }

    private synchronized void ensureInit() {
        if (db != null)
            return;

        db = BlastDBBuilder.build(new ArrayList<>(sequenceList));
        aligner = new BlastAlignerExt<>(db, parameters);
        aligner.setConcurrentBlastProcessCount(processCount);
    }

    @Override
    public synchronized void addReference(S sequence, P payload) {
        if (db != null)
            throw new IllegalStateException("Aligner is already in use, can't add sequence to database.");

        // See BlastDBBuilder sequence naming convention (see code)
        String key = BlastDBBuilder.getIdKey(sequenceList.size());
        // Adding to list for blastDB
        sequenceList.add(sequence);
        // Saving payload mapping
        payloads.put(key, payload);
        // Saving sequence mapping
        sequences.put(key, sequence);
    }

    protected abstract H createHit(Alignment<S> alignment, P recordPayload, BlastHit<S, ?> hit);
    //{
    //    return new BlastHit<>(alignment, recordPayload, hit);
    //}

    private class ResultsConverter<Q> implements Processor<PipedAlignmentResult<BlastHitExt<S>, Q>, PipedAlignmentResult<H, Q>> {
        @Override
        public PipedAlignmentResult<H, Q> process(PipedAlignmentResult<BlastHitExt<S>, Q> input) {
            List<H> hits = new ArrayList<>(input.getHits().size());
            for (BlastHitExt<S> iHit : input.getHits()) {
                String id = iHit.getSubjectId();
                S sequence = sequences.get(id);
                P payload = payloads.get(id);
                Alignment<S> alignment = iHit.getAlignment();
                Range subjectRange = iHit.getSubjectRange();
                alignment = new Alignment<>(sequence,
                        alignment.getAbsoluteMutations().move(subjectRange.getLower()), subjectRange, alignment.getSequence2Range(),
                        alignment.getScore());
                hits.add(createHit(alignment, payload, iHit));
            }
            return new PipedAlignmentResultImpl<>(hits, input.getQuery());
        }
    }
}
