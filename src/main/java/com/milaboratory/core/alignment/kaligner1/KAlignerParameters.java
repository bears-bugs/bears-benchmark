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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.milaboratory.core.alignment.LinearGapAlignmentScoring;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.GlobalObjectMappers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class KAlignerParameters implements Cloneable, java.io.Serializable, AbstractKAlignerParameters {
    private static final long serialVersionUID = 1L;
    /**
     * List of known parameters presets
     */
    private static final Map<String, KAlignerParameters> knownParameters;

    static {
        Map<String, KAlignerParameters> map = null;
        try {
            InputStream is = KAlignerParameters.class.getClassLoader().getResourceAsStream("parameters/kaligner_parameters.json");
            TypeReference<HashMap<String, KAlignerParameters>> typeRef
                    = new TypeReference<
                    HashMap<String, KAlignerParameters>
                    >() {
            };
            map = GlobalObjectMappers.ONE_LINE.readValue(is, typeRef);
        } catch (IOException ioe) {
            System.out.println("ERROR!");
            ioe.printStackTrace();
        }
        knownParameters = map;
    }

    /**
     * Returns parameters by specified preset name
     *
     * @param name parameters preset name
     * @return parameters with specified preset name
     */
    public static KAlignerParameters getByName(String name) {
        KAlignerParameters params = knownParameters.get(name);
        if (params == null)
            return null;
        return params.clone();
    }

    /**
     * Returns all available parameters presets
     *
     * @return all available parameters presets
     */
    public static Set<String> getAvailableNames() {
        return knownParameters.keySet();
    }

    /**
     * Nucleotides in kMer (value of k; kMer length)
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
    private float mapperAbsoluteMinScore,
    /**
     * Minimal allowed ratio between best hit score and other hits obtained by {@link
     * KMapper} to consider hit as reliable candidate
     */
    mapperRelativeMinScore,
    /**
     * Reward for mapped seed, must be > 0
     */
    mapperMatchScore,
    /**
     * Penalty for not mapped seed, must be < 0
     */
    mapperMismatchPenalty,
    /**
     * Penalty for different offset between adjacent seeds, must be < 0
     */
    mapperOffsetShiftPenalty;
    /**
     * Minimal and maximal distance between kMer seed positions in target sequence
     */
    private int mapperMinSeedsDistance, mapperMaxSeedsDistance;
    /**
     * Minimal allowed alignment length
     */
    private int minAlignmentLength;
    /**
     * Maximal allowed number of insertions and deletions between 2 kMers
     */
    private int maxAdjacentIndels;
    /**
     * Penalty score to stop alignment extension.
     */
    private int alignmentStopPenalty;
    /**
     * Minimal allowed score value to consider hit as reliable candidate
     */
    private float absoluteMinScore,
    /**
     * Maximal ratio between best hit score and other hits scores in returned result to consider hit as reliable
     * candidate
     */
    relativeMinScore;
    /**
     * Maximal number of hits to be stored as result
     */
    private int maxHits;
    /**
     * Scoring system
     */
    private LinearGapAlignmentScoring<NucleotideSequence> scoring;

    public KAlignerParameters() {
    }

    /**
     * Creates new KAligner
     *
     * @param mapperKValue             length of k-mers (seeds) used by {@link KMapper}
     * @param floatingLeftBound        {@code true} if left bound of alignment could be floating
     * @param floatingRightBound       {@code true} if right bound of alignment could be floating
     * @param mapperAbsoluteMinScore   minimal allowed absolute hit score obtained by {@link KMapper}
     *                                 to consider hit as reliable candidate
     * @param mapperRelativeMinScore   minimal allowed ratio between best hit score and scores of other hits obtained
     *                                 by
     *                                 {@link KMapper} to consider hit as
     *                                 reliable candidate
     * @param mapperMatchScore         reward for successfully mapped seeds (used in {@link KMapper}),
     *                                 must be > 0
     * @param mapperMismatchPenalty    penalty for not mapped seed (used in {@link KMapper}),
     *                                 must be < 0
     * @param mapperOffsetShiftPenalty penalty for different offset between adjacent seeds (used in {@link
     *                                 KMapper}), must be < 0
     * @param mapperMinSeedsDistance   minimal distance between randomly chosen seeds during alignment in {@link
     *                                 KMapper}
     * @param mapperMaxSeedsDistance   maximal distance between randomly chosen seeds during alignment in {@link
     *                                 KMapper}
     * @param minAlignmentLength       minimal allowed alignment length
     * @param maxAdjacentIndels        maximal allowed number of insertions and deletions between 2 kMers
     * @param alignmentStopPenalty     penalty score defining when to stop alignment procedure performed by {@link
     *                                 KAlignmentHit#calculateAlignment()}
     * @param absoluteMinScore         minimal absolute score of a hit obtained by {@link KAligner}
     * @param relativeMinScore         maximal ratio between best hit score and scores of other hits obtained by {@link
     *                                 KAligner}
     * @param maxHits                  maximal number of hits stored by {@link KAlignmentResult}
     * @param scoring                  scoring system used for building alignments
     */
    public KAlignerParameters(int mapperKValue, boolean floatingLeftBound, boolean floatingRightBound,
                              float mapperAbsoluteMinScore, float mapperRelativeMinScore,
                              float mapperMatchScore, float mapperMismatchPenalty, float mapperOffsetShiftPenalty,
                              int mapperMinSeedsDistance, int mapperMaxSeedsDistance, int minAlignmentLength,
                              int maxAdjacentIndels, int alignmentStopPenalty, float absoluteMinScore,
                              float relativeMinScore, int maxHits, LinearGapAlignmentScoring<NucleotideSequence> scoring) {
        this.mapperKValue = mapperKValue;
        this.floatingLeftBound = floatingLeftBound;
        this.floatingRightBound = floatingRightBound;
        this.mapperAbsoluteMinScore = mapperAbsoluteMinScore;
        this.mapperRelativeMinScore = mapperRelativeMinScore;
        this.mapperMatchScore = mapperMatchScore;
        this.mapperMismatchPenalty = mapperMismatchPenalty;
        this.mapperOffsetShiftPenalty = mapperOffsetShiftPenalty;
        this.mapperMinSeedsDistance = mapperMinSeedsDistance;
        this.mapperMaxSeedsDistance = mapperMaxSeedsDistance;
        this.minAlignmentLength = minAlignmentLength;
        this.maxAdjacentIndels = maxAdjacentIndels;
        this.alignmentStopPenalty = alignmentStopPenalty;
        this.absoluteMinScore = absoluteMinScore;
        this.relativeMinScore = relativeMinScore;
        this.maxHits = maxHits;
        this.scoring = scoring;
        if (scoring != null && !scoring.uniformBasicMatchScore())
            throw new IllegalArgumentException("Use scoring with common match score.");
    }

    @Override
    public <P> KAligner<P> createAligner() {
        return new KAligner<>(this);
    }

    /**
     * Returns kValue (length of kMers or seeds) used by {@link KMapper}
     *
     * @return kValue (length of kMers or seeds)
     */
    public int getMapperKValue() {
        return mapperKValue;
    }

    /**
     * Sets kValue (length of kMers or seeds) used by {@link KMapper}
     *
     * @param kValue
     * @return parameters object
     */
    public KAlignerParameters setMapperKValue(int kValue) {
        this.mapperKValue = kValue;
        return this;
    }

    /**
     * Returns minimal allowed absolute hit score obtained by {@link KMapper}
     * to consider hit as reliable candidate
     *
     * @return minimal allowed absolute hit score obtained by {@link KMapper}
     */
    public float getMapperAbsoluteMinScore() {
        return mapperAbsoluteMinScore;
    }

    /**
     * Sets minimal allowed absolute hit score obtained by {@link KMapper} to
     * consider hit as reliable candidate
     *
     * @param mapperAbsoluteMinScore minimal allowed absolute hit score value
     * @return parameters object
     */
    public KAlignerParameters setMapperAbsoluteMinScore(float mapperAbsoluteMinScore) {
        this.mapperAbsoluteMinScore = mapperAbsoluteMinScore;
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
    public KAlignerParameters setMapperRelativeMinScore(float mapperRelativeMinScore) {
        this.mapperRelativeMinScore = mapperRelativeMinScore;
        return this;
    }

    /**
     * Returns reward for successfully mapped seeds (used in {@link KMapper})
     *
     * @return reward score for mapped seed
     */
    public float getMapperMatchScore() {
        return mapperMatchScore;
    }

    /**
     * Sets for successfully mapped seeds (used in {@link KMapper})
     *
     * @param mapperMatchScore reward for successfully mapped seeds (used in {@link KMapper}),
     *                         must be > 0
     * @return parameters object
     */
    public KAlignerParameters setMapperMatchScore(float mapperMatchScore) {
        this.mapperMatchScore = mapperMatchScore;
        return this;
    }

    /**
     * Returns penalty score for not mapped seeds (used in {@link KMapper})
     *
     * @return penalty score for not mapped seed
     */
    public float getMapperMismatchPenalty() {
        return mapperMismatchPenalty;
    }

    /**
     * Sets penalty score for not mapped seed
     *
     * @param mapperMismatchPenalty penalty for not mapped seed (used in {@link KMapper}),
     *                              must be < 0
     * @return penalty for not mapped seed
     */
    public KAlignerParameters setMapperMismatchPenalty(float mapperMismatchPenalty) {
        this.mapperMismatchPenalty = mapperMismatchPenalty;
        return this;
    }

    /**
     * Returns minimal allowed alignment length
     *
     * @return minimal allowed alignment length
     */
    public int getMinAlignmentLength() {
        return minAlignmentLength;
    }

    /**
     * Sets minimal allowed alignment length
     *
     * @param minAlignmentLength minimal allowed alignment length
     * @return parameters object
     */
    public KAlignerParameters setMinAlignmentLength(int minAlignmentLength) {
        this.minAlignmentLength = minAlignmentLength;
        return this;
    }

    /**
     * Returns maximal allowed number of insertions and deletions between 2 kMers
     *
     * @return maximal allowed number of insertions and deletions between 2 kMers
     */
    public int getMaxAdjacentIndels() {
        return maxAdjacentIndels;
    }

    /**
     * Sets maximal allowed number of insertions and deletions between 2 kMers
     *
     * @param maxAdjacentIndels maximal allowed number of insertions and deletions between 2 kMers
     * @return parameters object
     */
    public KAlignerParameters setMaxAdjacentIndels(int maxAdjacentIndels) {
        this.maxAdjacentIndels = maxAdjacentIndels;
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
    public KAlignerParameters setMapperMinSeedsDistance(int mapperMinSeedsDistance) {
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
    public KAlignerParameters setMapperMaxSeedsDistance(int mapperMaxSeedsDistance) {
        this.mapperMaxSeedsDistance = mapperMaxSeedsDistance;
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
    public KAlignerParameters setAlignmentStopPenalty(int alignmentStopPenalty) {
        this.alignmentStopPenalty = alignmentStopPenalty;
        return this;
    }

    /**
     * Returns scoring system used for building alignments
     *
     * @return scoring system
     */
    @Override
    public LinearGapAlignmentScoring<NucleotideSequence> getScoring() {
        return scoring;
    }

    /**
     * Sets scoring system used for building alignments
     *
     * @param scoring scoring system
     * @return parameters object
     */
    public KAlignerParameters setScoring(LinearGapAlignmentScoring scoring) {
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
    public KAlignerParameters setFloatingLeftBound(boolean floatingLeftBound) {
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
    public KAlignerParameters setFloatingRightBound(boolean floatingRightBound) {
        this.floatingRightBound = floatingRightBound;
        return this;
    }

    /**
     * Returns penalty for different offset between adjacent seeds (used in {@link KMapper})
     *
     * @return penalty for different offset between adjacent seeds
     */
    public float getMapperOffsetShiftPenalty() {
        return mapperOffsetShiftPenalty;
    }

    /**
     * Sets penalty for different offset between adjacent seeds (used in {@link KMapper}),
     *
     * @param mapperOffsetShiftPenalty penalty for different offset between adjacent seeds, must be < 0
     * @return parameters object
     */
    public KAlignerParameters setMapperOffsetShiftPenalty(float mapperOffsetShiftPenalty) {
        this.mapperOffsetShiftPenalty = mapperOffsetShiftPenalty;
        return this;
    }

    /**
     * Returns minimal absolute score of a hit obtained by {@link KAligner}
     *
     * @return minimal absolute score
     */
    public float getAbsoluteMinScore() {
        return absoluteMinScore;
    }

    /**
     * Sets minimal absolute score of a hit obtained by {@link KAligner}
     *
     * @param absoluteMinScore minimal absolute score of a hit
     * @return parameters object
     */
    public KAlignerParameters setAbsoluteMinScore(float absoluteMinScore) {
        this.absoluteMinScore = absoluteMinScore;
        return this;
    }

    /**
     * Returns maximal ratio between best hit score and scores of other hits obtained by {@link
     * KAligner}
     *
     * @return maximal ratio between best hit score and scores of other hits
     */
    @Override
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
    public KAlignerParameters setRelativeMinScore(float relativeMinScore) {
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
    public KAlignerParameters setMaxHits(int maxHits) {
        this.maxHits = maxHits;
        return this;
    }

    @Override
    public KAlignerParameters clone() {
        try {
            KAlignerParameters c = (KAlignerParameters) super.clone();
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

        KAlignerParameters that = (KAlignerParameters) o;

        if (floatingLeftBound != that.floatingLeftBound) return false;
        if (floatingRightBound != that.floatingRightBound) return false;
        if (Float.compare(that.mapperRelativeMinScore, mapperRelativeMinScore) != 0) return false;
        if (mapperKValue != that.mapperKValue) return false;
        if (Float.compare(that.mapperMatchScore, mapperMatchScore) != 0) return false;
        if (maxAdjacentIndels != that.maxAdjacentIndels) return false;
        if (mapperMaxSeedsDistance != that.mapperMaxSeedsDistance) return false;
        if (minAlignmentLength != that.minAlignmentLength) return false;
        if (Float.compare(that.mapperAbsoluteMinScore, mapperAbsoluteMinScore) != 0) return false;
        if (mapperMinSeedsDistance != that.mapperMinSeedsDistance) return false;
        if (Float.compare(that.mapperMismatchPenalty, mapperMismatchPenalty) != 0) return false;
        if (Float.compare(that.mapperOffsetShiftPenalty, mapperOffsetShiftPenalty) != 0) return false;
        if (alignmentStopPenalty != that.alignmentStopPenalty) return false;
        if (!scoring.equals(that.scoring)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mapperKValue;
        result = 31 * result + (floatingLeftBound ? 1 : 0);
        result = 31 * result + (floatingRightBound ? 1 : 0);
        result = 31 * result + (mapperAbsoluteMinScore != +0.0f ? Float.floatToIntBits(mapperAbsoluteMinScore) : 0);
        result = 31 * result + (mapperRelativeMinScore != +0.0f ? Float.floatToIntBits(mapperRelativeMinScore) : 0);
        result = 31 * result + (mapperMatchScore != +0.0f ? Float.floatToIntBits(mapperMatchScore) : 0);
        result = 31 * result + (mapperMismatchPenalty != +0.0f ? Float.floatToIntBits(mapperMismatchPenalty) : 0);
        result = 31 * result + (mapperOffsetShiftPenalty != +0.0f ? Float.floatToIntBits(mapperOffsetShiftPenalty) : 0);
        result = 31 * result + minAlignmentLength;
        result = 31 * result + maxAdjacentIndels;
        result = 31 * result + mapperMinSeedsDistance;
        result = 31 * result + mapperMaxSeedsDistance;
        result = 31 * result + alignmentStopPenalty;
        result = 31 * result + scoring.hashCode();
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
