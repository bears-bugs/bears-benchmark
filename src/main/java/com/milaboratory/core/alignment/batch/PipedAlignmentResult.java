package com.milaboratory.core.alignment.batch;

public interface PipedAlignmentResult<H extends AlignmentHit<?, ?>, Q> extends AlignmentResult<H> {
    Q getQuery();
}
