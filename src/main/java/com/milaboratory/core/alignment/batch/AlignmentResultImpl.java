package com.milaboratory.core.alignment.batch;

import java.util.Collections;
import java.util.List;

public class AlignmentResultImpl<H extends AlignmentHit<?, ?>> implements AlignmentResult<H> {
    final List<H> hits;

    public AlignmentResultImpl() {
        this.hits = Collections.emptyList();
    }

    public AlignmentResultImpl(List<H> hits) {
        this.hits = hits;
    }

    @Override
    public List<H> getHits() {
        return hits;
    }

    @Override
    public final H getBestHit() {
        return hits.isEmpty() ? null : hits.get(0);
    }

    @Override
    public boolean hasHits() {
        return !hits.isEmpty();
    }

    @Override
    public String toString() {
        return !hasHits() ? "Empty result." : (hits.size() + " hits.");
    }
}
