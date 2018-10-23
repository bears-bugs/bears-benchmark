package com.milaboratory.core.alignment.kaligner2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.milaboratory.core.alignment.AffineGapAlignmentScoring;
import com.milaboratory.core.alignment.kaligner1.*;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.GlobalObjectMappers;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class KAlignerParameters2 implements AbstractKAlignerParameters, Cloneable, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /* MAPPER PARAMETERS BEGIN */

    /**
     * Nucleotides in kMer (value of k; kMer length)
     */
    private int mapperNValue;
    /**
     * Allowed mutations in kMers
     */
    private int mapperKValue;
    /**
     * Defines floating bounds of alignment
     */
    private boolean floatingLeftBound, floatingRightBound;
    /**
     * Minimal allowed absolute hit score obtained by {@link KMapper} to
     * consider hit as reliable candidate
     */
    private int mapperAbsoluteMinClusterScore,
    /**
     * Penalty value for starting new cluster
     */
    mapperExtraClusterScore,
    /**
     * Reward for mapped seed, must be > 0
     */
    mapperMatchScore,
    /**
     * Penalty for not mapped seed, must be < 0
     */
    mapperMismatchScore,
    /**
     * Penalty for different offset between adjacent seeds, must be < 0
     */
    mapperOffsetShiftScore,
    /**
     * Number of simultaneously constructed clusters in one-pass initial cluster detection algorithm
     */
    mapperSlotCount,
    /**
     * Max allowed clusters
     */
    mapperMaxClusters,
    /**
     * Max indels inside a cluster (if indel is bigger, alignment will be divided into several clusters)
     */
    mapperMaxClusterIndels,
    /**
     * If mapperKValue > 0, it is possible to map several seeds with holes in different places to the same position in
     * target sequence.
     *
     * e.g.
     *
     * if mapperKValue = 1 and mapperNValue = 5
     *
     * there ara 5 different sets of KMers:
     * X = must match; 0 = allow mismatch:
     * 0XXXX
     * X0XXX
     * XX0XX
     * XXX0X
     * XXXX0
     *
     * and if mapperKMersPerPosition == 5 (which means: try to map all kMer variants listed above to each seed position
     * in target sequence)
     *
     * mapping will be made even if 5-met in the target seed position has 1 substitution compared to database.
     *
     * Constraint: kMersPerPosition <= nValue / kValue
     */
    mapperKMersPerPosition;
    /**
     * Minimal value of total mapping score (sum of cluster scores and mapperExtraClusterScore * (nClusters - 1))
     */
    private int mapperAbsoluteMinScore;
    /**
     * Minimal allowed ratio between best hit score and other hits obtained by {@link
     * KMapper} to consider hit as reliable candidate
     */
    private float mapperRelativeMinScore;

    /**
     * Minimal and maximal distance between kMer seed positions in target sequence
     */
    private int mapperMinSeedsDistance, mapperMaxSeedsDistance;

    /* MAPPER PARAMETERS END */

    /* ALIGNER PARAMETERS BEGIN */

    /**
     * Penalty score to stop alignment extension.
     */
    private int alignmentStopPenalty;
    /**
     * Minimal allowed score value to consider hit as reliable candidate
     */
    private int absoluteMinScore;
    /**
     * Maximal ratio between best hit score and other hits scores in returned result to consider hit as reliable
     * candidate
     */
    private float relativeMinScore;
    /**
     * Maximal number of hits to be stored as result
     */
    private int maxHits;
    /**
     * Scoring system
     */
    private AffineGapAlignmentScoring<NucleotideSequence> scoring;

    /* ALIGNER PARAMETERS END */

    public KAlignerParameters2() {
    }

    public KAlignerParameters2(int mapperNValue, int mapperKValue, boolean floatingLeftBound, boolean floatingRightBound,
                               int mapperAbsoluteMinClusterScore, int mapperExtraClusterScore, int mapperAbsoluteMinScore, float mapperRelativeMinScore,
                               int mapperMatchScore, int mapperMismatchScore, int mapperOffsetShiftScore, int mapperMinSeedsDistance,
                               int mapperMaxSeedsDistance, int mapperSlotCount, int mapperMaxClusters, int mapperMaxClusterIndels, int mapperKMersPerPosition,
                               int alignmentStopPenalty, int absoluteMinScore, float relativeMinScore,
                               int maxHits, AffineGapAlignmentScoring scoring) {
        if (scoring != null && !scoring.uniformBasicMatchScore())
            throw new IllegalArgumentException("Use scoring with common match score.");
        this.mapperNValue = mapperNValue;
        this.mapperKValue = mapperKValue;
        this.floatingLeftBound = floatingLeftBound;
        this.floatingRightBound = floatingRightBound;
        this.mapperAbsoluteMinClusterScore = mapperAbsoluteMinClusterScore;
        this.mapperExtraClusterScore = mapperExtraClusterScore;
        this.mapperAbsoluteMinScore = mapperAbsoluteMinScore;
        this.mapperRelativeMinScore = mapperRelativeMinScore;
        this.mapperMatchScore = mapperMatchScore;
        this.mapperMismatchScore = mapperMismatchScore;
        this.mapperOffsetShiftScore = mapperOffsetShiftScore;
        this.mapperMinSeedsDistance = mapperMinSeedsDistance;
        this.mapperMaxSeedsDistance = mapperMaxSeedsDistance;
        this.mapperSlotCount = mapperSlotCount;
        this.mapperMaxClusters = mapperMaxClusters;
        this.mapperMaxClusterIndels = mapperMaxClusterIndels;
        this.mapperKMersPerPosition = mapperKMersPerPosition;
        this.alignmentStopPenalty = alignmentStopPenalty;
        this.absoluteMinScore = absoluteMinScore;
        this.relativeMinScore = relativeMinScore;
        this.maxHits = maxHits;
        this.scoring = scoring;
    }

    @Override
    public <P> KAligner2<P> createAligner() {
        return new KAligner2<>(this);
    }

    /**
     * Returns kValue (length of kMers or seeds) used by {@link KMapper}
     *
     * @return kValue (length of kMers or seeds)
     */
    public int getMapperNValue() {
        return mapperNValue;
    }

    /**
     * Sets kValue (length of kMers or seeds) used by {@link KMapper}
     *
     * @param kValue
     * @return parameters object
     */
    public KAlignerParameters2 setMapperNValue(int kValue) {
        this.mapperNValue = kValue;
        return this;
    }

    public int getMapperKValue() {
        return mapperKValue;
    }

    public KAlignerParameters2 setMapperKValue(int mapperKValue) {
        this.mapperKValue = mapperKValue;
        return this;
    }

    /**
     * Returns minimal allowed absolute hit score obtained by {@link KMapper}
     * to consider hit as reliable candidate
     *
     * @return minimal allowed absolute hit score obtained by {@link KMapper}
     */
    public int getMapperAbsoluteMinClusterScore() {
        return mapperAbsoluteMinClusterScore;
    }

    /**
     * Sets minimal allowed absolute hit score obtained by {@link KMapper} to
     * consider hit as reliable candidate
     *
     * @param mapperAbsoluteMinScore minimal allowed absolute hit score value
     * @return parameters object
     */
    public KAlignerParameters2 setMapperAbsoluteMinClusterScore(int mapperAbsoluteMinScore) {
        this.mapperAbsoluteMinClusterScore = mapperAbsoluteMinScore;
        return this;
    }

    /**
     * Returns minimal allowed ratio between best hit score and other hits obtained by {@link
     * KMapper} to consider hit as reliable candidate
     *
     * @return minimal allowed ratio between best hit score and other hits obtained by {@link
     * KMapper}
     */
    public float getMapperRelativeMinScore() {
        return mapperRelativeMinScore;
    }

    /**
     * Sets minimal allowed ratio between best hit score and other hits obtained by {@link
     * KMapper} to consider hit as reliable candidate
     *
     * @param mapperRelativeMinScore minimal allowed ratio between best hit score and other hits
     * @return parameters object
     */
    public KAlignerParameters2 setMapperRelativeMinScore(float mapperRelativeMinScore) {
        this.mapperRelativeMinScore = mapperRelativeMinScore;
        return this;
    }

    /**
     * Returns reward for successfully mapped seeds (used in {@link KMapper})
     *
     * @return reward score for mapped seed
     */
    public int getMapperMatchScore() {
        return mapperMatchScore;
    }

    /**
     * Sets for successfully mapped seeds (used in {@link KMapper})
     *
     * @param mapperMatchScore reward for successfully mapped seeds (used in {@link KMapper}),
     *                         must be > 0
     * @return parameters object
     */
    public KAlignerParameters2 setMapperMatchScore(int mapperMatchScore) {
        this.mapperMatchScore = mapperMatchScore;
        return this;
    }

    /**
     * Returns penalty score for not mapped seeds (used in {@link KMapper})
     *
     * @return penalty score for not mapped seed
     */
    public int getMapperMismatchScore() {
        return mapperMismatchScore;
    }

    /**
     * Sets penalty score for not mapped seed
     *
     * @param mapperMismatchScore penalty for not mapped seed (used in {@link KMapper}),
     *                            must be < 0
     * @return penalty for not mapped seed
     */
    public KAlignerParameters2 setMapperMismatchScore(int mapperMismatchScore) {
        this.mapperMismatchScore = mapperMismatchScore;
        return this;
    }

    /**
     * Returns penalty for different offset between adjacent seeds (used in {@link KMapper})
     *
     * @return penalty for different offset between adjacent seeds
     */
    public int getMapperOffsetShiftScore() {
        return mapperOffsetShiftScore;
    }

    /**
     * Sets penalty for different offset between adjacent seeds (used in {@link KMapper}),
     *
     * @param mapperOffsetShiftScore penalty for different offset between adjacent seeds, must be < 0
     * @return parameters object
     */
    public KAlignerParameters2 setMapperOffsetShiftScore(int mapperOffsetShiftScore) {
        this.mapperOffsetShiftScore = mapperOffsetShiftScore;
        return this;
    }

    //TODO
    public int getMapperExtraClusterScore() {
        return mapperExtraClusterScore;
    }

    //TODO
    public KAlignerParameters2 setMapperExtraClusterScore(int mapperExtraClusterScore) {
        this.mapperExtraClusterScore = mapperExtraClusterScore;
        return this;
    }

    /**
     * Returns minimal distance between randomly chosen seeds during alignment in {@link
     * KMapper}
     *
     * @return minimal distance between randomly chosen seeds
     */
    public int getMapperMinSeedsDistance() {
        return mapperMinSeedsDistance;
    }

    /**
     * Sets minimal distance between randomly chosen seeds during alignment in {@link
     * KMapper}
     *
     * @param mapperMinSeedsDistance minimal distance between randomly chosen seeds
     * @return parameters object
     */
    public KAlignerParameters2 setMapperMinSeedsDistance(int mapperMinSeedsDistance) {
        this.mapperMinSeedsDistance = mapperMinSeedsDistance;
        return this;
    }

    /**
     * Returns maximal distance between randomly chosen seeds during alignment in {@link
     * KMapper}
     *
     * @return maximal distance between randomly chosen seeds
     */
    public int getMapperMaxSeedsDistance() {
        return mapperMaxSeedsDistance;
    }

    /**
     * Sets maximal distance between randomly chosen seeds during alignment in {@link
     * KMapper}
     *
     * @param mapperMaxSeedsDistance maximal distance between randomly chosen seeds
     * @return parameters object
     */
    public KAlignerParameters2 setMapperMaxSeedsDistance(int mapperMaxSeedsDistance) {
        this.mapperMaxSeedsDistance = mapperMaxSeedsDistance;
        return this;
    }

    /**
     * Number of simultaneously constructed clusters in one-pass initial cluster detection algorithm
     */
    public int getMapperSlotCount() {
        return mapperSlotCount;
    }

    /**
     * Number of simultaneously constructed clusters in one-pass initial cluster detection algorithm
     */
    public KAlignerParameters2 setMapperSlotCount(int mapperSlotCount) {
        this.mapperSlotCount = mapperSlotCount;
        return this;
    }

    /**
     * Max indels inside a cluster (if indel is bigger, alignment will be divided into several clusters)
     */
    public int getMapperMaxClusterIndels() {
        return mapperMaxClusterIndels;
    }

    /**
     * Max indels inside a cluster (if indel is bigger, alignment will be divided into several clusters)
     */
    public KAlignerParameters2 setMapperMaxClusterIndels(int mapperMaxClusterIndels) {
        this.mapperMaxClusterIndels = mapperMaxClusterIndels;
        return this;
    }

    /**
     * If mapperKValue > 0, it is possible to map several seeds with holes in different places to the same position in
     * target sequence.
     *
     * e.g.
     *
     * if mapperKValue = 1 and mapperNValue = 5
     *
     * there ara 5 different sets of KMers:
     * X = must match; 0 = allow mismatch:
     * 0XXXX
     * X0XXX
     * XX0XX
     * XXX0X
     * XXXX0
     *
     * and if mapperKMersPerPosition == 5 (which means: try to map all kMer variants listed above to each seed position
     * in target sequence)
     *
     * mapping will be made even if 5-met in the target seed position has 1 substitution compared to database.
     *
     * Constraint: kMersPerPosition <= nValue / kValue
     */

    public int getMapperKMersPerPosition() {
        return mapperKMersPerPosition;
    }

    /**
     * If mapperKValue > 0, it is possible to map several seeds with holes in different places to the same position in
     * target sequence.
     *
     * e.g.
     *
     * if mapperKValue = 1 and mapperNValue = 5
     *
     * there ara 5 different sets of KMers:
     * X = must match; 0 = allow mismatch:
     * 0XXXX
     * X0XXX
     * XX0XX
     * XXX0X
     * XXXX0
     *
     * and if mapperKMersPerPosition == 5 (which means: try to map all kMer variants listed above to each seed position
     * in target sequence)
     *
     * mapping will be made even if 5-met in the target seed position has 1 substitution compared to database.
     *
     * Constraint: kMersPerPosition <= nValue / kValue
     */
    public KAlignerParameters2 setMapperKMersPerPosition(int mapperKMersPerPosition) {
        this.mapperKMersPerPosition = mapperKMersPerPosition;
        return this;
    }

    /**
     * Max allowed clusters
     */
    public int getMapperMaxClusters() {
        return mapperMaxClusters;
    }

    /**
     * Max allowed clusters
     */
    public KAlignerParameters2 setMapperMaxClusters(int mapperMaxClusters) {
        this.mapperMaxClusters = mapperMaxClusters;
        return this;
    }

    /**
     * Returns penalty score defining when to stop alignment procedure performed by {@link
     * KAlignmentHit#calculateAlignment()}
     *
     * @return penalty score
     */
    public int getAlignmentStopPenalty() {
        return alignmentStopPenalty;
    }

    /**
     * Sets penalty score defining when to stop alignment procedure performed by {@link
     * KAlignmentHit#calculateAlignment()}
     *
     * @param alignmentStopPenalty penalty score
     * @return parameters object
     */
    public KAlignerParameters2 setAlignmentStopPenalty(int alignmentStopPenalty) {
        this.alignmentStopPenalty = alignmentStopPenalty;
        return this;
    }

    /**
     * Returns scoring system used for building alignments
     *
     * @return scoring system
     */
    @Override
    public AffineGapAlignmentScoring<NucleotideSequence> getScoring() {
        return scoring;
    }

    /**
     * Sets scoring system used for building alignments
     *
     * @param scoring scoring system
     * @return parameters object
     */
    public KAlignerParameters2 setScoring(AffineGapAlignmentScoring<NucleotideSequence> scoring) {
        if (scoring != null && !scoring.uniformBasicMatchScore())
            throw new IllegalArgumentException("Use scoring with common match score.");
        this.scoring = scoring;
        return this;
    }

    /**
     * Checks if left bound of alignment is floating
     *
     * @return {@code true} if left bound of alignment is floating
     */
    @Override
    public boolean isFloatingLeftBound() {
        return floatingLeftBound;
    }

    /**
     * Sets left left bound of alignment
     *
     * @param floatingLeftBound {@code true} if left bound of alignment could be floating
     * @return parameters object
     */
    @Override
    public KAlignerParameters2 setFloatingLeftBound(boolean floatingLeftBound) {
        this.floatingLeftBound = floatingLeftBound;
        return this;
    }

    /**
     * Checks if right bound of alignment is floating
     *
     * @return {@code true} if right bound of alignment is floating
     */
    @Override
    public boolean isFloatingRightBound() {
        return floatingRightBound;
    }

    /**
     * Sets right left bound of alignment
     *
     * @param floatingRightBound {@code true} if right bound of alignment could be floating
     * @return parameters object
     */
    @Override
    public KAlignerParameters2 setFloatingRightBound(boolean floatingRightBound) {
        this.floatingRightBound = floatingRightBound;
        return this;
    }

    /**
     * Minimal value of total mapping score (sum of cluster scores and mapperExtraClusterScore * (nClusters - 1))
     */
    public int getMapperAbsoluteMinScore() {
        return mapperAbsoluteMinScore;
    }

    /**
     * Minimal value of total mapping score (sum of cluster scores and mapperExtraClusterScore * (nClusters - 1))
     */
    public KAlignerParameters2 setMapperAbsoluteMinScore(int mapperAbsoluteMinScore) {
        this.mapperAbsoluteMinScore = mapperAbsoluteMinScore;
        return this;
    }

    /**
     * Returns minimal absolute score of a hit obtained by {@link KAligner}
     *
     * @return minimal absolute score
     */
    public int getAbsoluteMinScore() {
        return absoluteMinScore;
    }

    /**
     * Sets minimal absolute score of a hit obtained by {@link KAligner}
     *
     * @param absoluteMinScore minimal absolute score of a hit
     * @return parameters object
     */
    public KAlignerParameters2 setAbsoluteMinScore(int absoluteMinScore) {
        this.absoluteMinScore = absoluteMinScore;
        return this;
    }

    /**
     * Returns maximal ratio between best hit score and scores of other hits obtained by {@link
     * KAligner}
     *
     * @return maximal ratio between best hit score and scores of other hits
     */
    public float getRelativeMinScore() {
        return relativeMinScore;
    }

    /**
     * Sets maximal ratio between best hit score and scores of other hits obtained by {@link
     * KAligner}
     *
     * @param relativeMinScore maximal ratio between best hit score and scores of other hits
     * @return parameters object
     */
    public KAlignerParameters2 setRelativeMinScore(float relativeMinScore) {
        this.relativeMinScore = relativeMinScore;
        return this;
    }

    /**
     * Returns maximal number of hits stored by {@link KAlignmentResult}
     *
     * @return maximal number of stored hits
     */
    @Override
    public int getMaxHits() {
        return maxHits;
    }

    /**
     * Sets maximal number of hits stored by {@link KAlignmentResult}
     *
     * @param maxHits maximal number of stored hits
     * @return parameters object
     */
    public KAlignerParameters2 setMaxHits(int maxHits) {
        this.maxHits = maxHits;
        return this;
    }

    @Override
    public KAlignerParameters2 clone() {
        try {
            KAlignerParameters2 c = (KAlignerParameters2) super.clone();
            if (this.scoring != null)
                c.setScoring(this.scoring);
            return c;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KAlignerParameters2 that = (KAlignerParameters2) o;

        if (mapperNValue != that.mapperNValue) return false;
        if (floatingLeftBound != that.floatingLeftBound) return false;
        if (floatingRightBound != that.floatingRightBound) return false;
        if (mapperAbsoluteMinClusterScore != that.mapperAbsoluteMinClusterScore) return false;
        if (mapperExtraClusterScore != that.mapperExtraClusterScore) return false;
        if (mapperMatchScore != that.mapperMatchScore) return false;
        if (mapperMismatchScore != that.mapperMismatchScore) return false;
        if (mapperOffsetShiftScore != that.mapperOffsetShiftScore) return false;
        if (mapperSlotCount != that.mapperSlotCount) return false;
        if (mapperMaxClusterIndels != that.mapperMaxClusterIndels) return false;
        if (mapperAbsoluteMinScore != that.mapperAbsoluteMinScore) return false;
        if (Float.compare(that.mapperRelativeMinScore, mapperRelativeMinScore) != 0) return false;
        if (mapperMinSeedsDistance != that.mapperMinSeedsDistance) return false;
        if (mapperMaxSeedsDistance != that.mapperMaxSeedsDistance) return false;
        if (alignmentStopPenalty != that.alignmentStopPenalty) return false;
        if (absoluteMinScore != that.absoluteMinScore) return false;
        if (Float.compare(that.relativeMinScore, relativeMinScore) != 0) return false;
        if (maxHits != that.maxHits) return false;
        return !(scoring != null ? !scoring.equals(that.scoring) : that.scoring != null);

    }

    @Override
    public int hashCode() {
        int result = mapperNValue;
        result = 31 * result + (floatingLeftBound ? 1 : 0);
        result = 31 * result + (floatingRightBound ? 1 : 0);
        result = 31 * result + mapperAbsoluteMinClusterScore;
        result = 31 * result + mapperExtraClusterScore;
        result = 31 * result + mapperMatchScore;
        result = 31 * result + mapperMismatchScore;
        result = 31 * result + mapperOffsetShiftScore;
        result = 31 * result + mapperSlotCount;
        result = 31 * result + mapperMaxClusterIndels;
        result = 31 * result + mapperAbsoluteMinScore;
        result = 31 * result + (mapperRelativeMinScore != +0.0f ? Float.floatToIntBits(mapperRelativeMinScore) : 0);
        result = 31 * result + mapperMinSeedsDistance;
        result = 31 * result + mapperMaxSeedsDistance;
        result = 31 * result + alignmentStopPenalty;
        result = 31 * result + absoluteMinScore;
        result = 31 * result + (relativeMinScore != +0.0f ? Float.floatToIntBits(relativeMinScore) : 0);
        result = 31 * result + maxHits;
        result = 31 * result + (scoring != null ? scoring.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        try {
            return "KAlignerParameters" + GlobalObjectMappers.PRETTY.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Error...";
        }
    }
}
