package com.milaboratory.core.alignment.benchmark;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.milaboratory.core.alignment.AffineGapAlignmentScoring;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.NucleotideSequence;

/**
 * Created by dbolotin on 27/10/15.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class ChallengeParameters {
    final int dbSize, dbMinSeqLength, dbMaxSeqLength;
    final int queryCount;
    final int falseCount;
    final int minClusters, maxClusters,
            minClusterLength, maxClusterLength,
            minIndelLength, maxIndelLength;
    final double insertionProbability, deletionProbability, boundaryInsertProbability;
    final NucleotideMutationModel mutationModel;
    final int minAlignmentScoring, maxAlignmentScoring;
    final AffineGapAlignmentScoring<NucleotideSequence> scoring;

    public ChallengeParameters(int dbSize, int dbMinSeqLength, int dbMaxSeqLength, int queryCount, int falseCount,
                               int minClusters, int maxClusters, int minClusterLength, int maxClusterLength,
                               int minIndelLength, int maxIndelLength, double insertionProbability,
                               double deletionProbability, double boundaryInsertProbability,
                               NucleotideMutationModel mutationModel, int minAlignmentScoring,
                               int maxAlignmentScoring, AffineGapAlignmentScoring<NucleotideSequence> scoring) {
        this.dbSize = dbSize;
        this.dbMinSeqLength = dbMinSeqLength;
        this.dbMaxSeqLength = dbMaxSeqLength;
        this.queryCount = queryCount;
        this.falseCount = falseCount;
        this.minClusters = minClusters;
        this.maxClusters = maxClusters;
        this.minClusterLength = minClusterLength;
        this.maxClusterLength = maxClusterLength;
        this.minIndelLength = minIndelLength;
        this.maxIndelLength = maxIndelLength;
        this.insertionProbability = insertionProbability;
        this.deletionProbability = deletionProbability;
        this.boundaryInsertProbability = boundaryInsertProbability;
        this.mutationModel = mutationModel;
        this.minAlignmentScoring = minAlignmentScoring;
        this.maxAlignmentScoring = maxAlignmentScoring;
        this.scoring = scoring;
    }

    public ChallengeParameters setQueryCount(int newCount) {
        return new ChallengeParameters(dbSize, dbMinSeqLength,
                dbMaxSeqLength, newCount, falseCount,
                minClusters, maxClusters,
                minClusterLength, maxClusterLength,
                minIndelLength, maxIndelLength,
                insertionProbability, deletionProbability,
                boundaryInsertProbability, mutationModel,
                minAlignmentScoring, maxAlignmentScoring, scoring);
    }
}
