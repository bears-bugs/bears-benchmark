package com.milaboratory.core.alignment.benchmark;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.milaboratory.core.alignment.kaligner2.KAligner2Statistics;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public class BenchmarkResults {
    @JsonIgnore
    public final BenchmarkInput input;
    public final KAligner2Statistics stat;
    public final long executionTime;
    public final int processedQueries;
    public final int processedGoodQueries;
    public final int falsePositives;
    public final int mismatched;
    public final int noHits;
    public final int scoreError;

    public BenchmarkResults(BenchmarkInput input, KAligner2Statistics stat, long executionTime, int processedQueries,
                            int processedGoodQueries, int falsePositives, int mismatched, int noHits, int scoreError) {
        this.input = input;
        this.stat = stat;
        this.executionTime = executionTime;
        this.processedQueries = processedQueries;
        this.processedGoodQueries = processedGoodQueries;
        this.falsePositives = falsePositives;
        this.mismatched = mismatched;
        this.noHits = noHits;
        this.scoreError = scoreError;
    }

    public BenchmarkInput getInput() {
        return input;
    }

    public KAligner2Statistics getStat() {
        return stat;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public int getProcessedGoodQueries() {
        return processedGoodQueries;
    }

    public int getProcessedQueries() { return processedQueries; }

    public int getMismatched() {
        return mismatched;
    }

    public int getNoHits() {
        return noHits;
    }

    public double getNoHitsFraction() {
        return 1.0 * noHits / processedGoodQueries;
    }

    public double getMismatchedFraction() {
        return 1.0 * mismatched / processedGoodQueries;
    }

    public double getBadFraction() {
        return 1.0 * (noHits + mismatched) / processedGoodQueries;
    }

    public double getFalsePositiveFraction() {
        return 1.0 * falsePositives / (processedQueries - processedGoodQueries);
    }

    public double getScoreErrorFraction() {
        return 1.0 * scoreError / (processedGoodQueries - noHits - mismatched);
    }

    public long getAverageTiming() {
        return executionTime / processedQueries;
    }
}
