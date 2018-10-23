package com.milaboratory.core.alignment.benchmark;

import com.milaboratory.core.alignment.batch.BatchAlignerWithBaseParameters;

/**
 * Created by dbolotin on 27/10/15.
 */
public final class BenchmarkInput<T extends BatchAlignerWithBaseParameters> {
    public final T params;
    public final Challenge challenge;

    public BenchmarkInput(T params, Challenge challenge) {
        this.params = params;
        this.challenge = challenge;
    }
}
