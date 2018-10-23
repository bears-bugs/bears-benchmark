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
package com.milaboratory.core.tree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.TextNode;
import com.milaboratory.primitivio.annotations.Serializable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@JsonSerialize(using = TreeSearchParameters.Serializer.class)
@JsonDeserialize(using = TreeSearchParameters.Deserializer.class)
@Serializable(asJson = true)
public final class TreeSearchParameters
        implements java.io.Serializable {
    public static final double[] DEFAULT_PENALTY = {
            0.1, // Mismatch penalty
            0.21, // Deletion penalty
            0.32}; // Insertion penalty
    private static final double UNIFORM_PENALTY_VALUE = 1.;
    private static final double[] UNIFORM_PENALTY = {
            UNIFORM_PENALTY_VALUE, // Mismatch penalty
            UNIFORM_PENALTY_VALUE, // Deletion penalty
            UNIFORM_PENALTY_VALUE}; // Insertion penalty
    public static final TreeSearchParameters ONE_MISMATCH = new TreeSearchParameters(1, 0, 0, 1);
    public static final TreeSearchParameters ONE_INDEL = new TreeSearchParameters(0, 1, 1, 1);
    public static final TreeSearchParameters ONE_MISMATCH_OR_INDEL = new TreeSearchParameters(1, 1, 1, 1);

    public static final TreeSearchParameters TWO_MISMATCHES = new TreeSearchParameters(2, 0, 0, 2);
    public static final TreeSearchParameters TWO_INDELS = new TreeSearchParameters(0, 2, 2, 2);
    public static final TreeSearchParameters TWO_MISMATCHES_OR_INDELS = new TreeSearchParameters(2, 2, 2, 2);

    public static final TreeSearchParameters THREE_MISMATCHES = new TreeSearchParameters(3, 0, 0, 3);
    public static final TreeSearchParameters THREE_INDELS = new TreeSearchParameters(0, 3, 3, 3);
    public static final TreeSearchParameters THREE_MISMATCHES_OR_INDELS = new TreeSearchParameters(3, 3, 3, 3);

    public static final TreeSearchParameters FOUR_MISMATCHES = new TreeSearchParameters(4, 0, 0, 4);
    public static final TreeSearchParameters FOUR_INDELS = new TreeSearchParameters(0, 4, 4, 4);
    public static final TreeSearchParameters FOUR_MISMATCHES_OR_INDELS = new TreeSearchParameters(4, 4, 4, 4);

    private final int[] maxErrors;
    private final double[] penalty;
    private final double maxPenalty;
    private final byte[][] differencesCombination;
    private final boolean greedy;

    public TreeSearchParameters(int[] maxErrors, double[] penalty, double maxPenalty) {
        this(maxErrors, penalty, maxPenalty, true);
    }

    public TreeSearchParameters(int[] maxErrors, double[] penalty, double maxPenalty, boolean greedy) {
        if (penalty.length != 3 || maxErrors.length != 3)
            throw new IllegalArgumentException();
        this.maxErrors = maxErrors.clone();
        this.penalty = penalty.clone();
        this.maxPenalty = maxPenalty;
        this.greedy = greedy;
        this.differencesCombination = PenaltyUtils.getDifferencesCombination(maxPenalty, penalty, maxErrors);
    }

    /**
     * Parameters to search with limited number of each mutation type. <p/> <p>Ordering of search is according to
     * {@link
     * #DEFAULT_PENALTY}.</p>
     *
     * @param mismatches maximum number of mismatches
     * @param deletions  maximum number of deletions
     * @param insertions maximum number of insertions
     */
    public TreeSearchParameters(int mismatches, int deletions, int insertions) {
        this(mismatches, deletions, insertions, true);
    }

    /**
     * Parameters to search with limited number of each mutation type. <p/> <p>Ordering of search is according to
     * {@link
     * #DEFAULT_PENALTY}.</p>
     *
     * @param mismatches maximum number of mismatches
     * @param deletions  maximum number of deletions
     * @param insertions maximum number of insertions
     * @param greedy     speed up search by not considering substitutions right after indels (forcing them to be before indels)
     */
    public TreeSearchParameters(int mismatches, int deletions, int insertions, boolean greedy) {
        this(new int[]{mismatches, deletions, insertions},
                DEFAULT_PENALTY,
                maxPenaltyFor(mismatches, deletions, insertions),
                greedy);
    }

    public TreeSearchParameters(int mismatches, int deletions, int insertions,
                                int totalMutations, boolean greedy) {
        this(new int[]{mismatches, deletions, insertions}, UNIFORM_PENALTY, UNIFORM_PENALTY_VALUE * totalMutations, greedy);
    }

    public TreeSearchParameters(int mismatches, int deletions, int insertions,
                                int totalMutations) {
        this(mismatches, deletions, insertions, totalMutations, true);
    }

    public TreeSearchParameters(int maxSubstitutions, int maxDeletions, int maxInsertions,
                                double substitutionPenalty, double deletionPenalty, double insertionPenalty,
                                double maxPenalty, boolean greedy) {
        this.maxErrors = new int[]{maxSubstitutions, maxDeletions, maxInsertions};
        this.penalty = new double[]{substitutionPenalty, deletionPenalty, insertionPenalty};
        this.maxPenalty = maxPenalty;
        this.differencesCombination = PenaltyUtils.getDifferencesCombination(maxPenalty, penalty, maxErrors);
        this.greedy = greedy;
    }

    public TreeSearchParameters(int maxSubstitutions, int maxDeletions, int maxInsertions,
                                double substitutionPenalty, double deletionPenalty, double insertionPenalty,
                                double maxPenalty) {
        this(maxSubstitutions, maxDeletions, maxInsertions, substitutionPenalty, deletionPenalty, insertionPenalty, maxPenalty, true);
    }

    private static double maxPenaltyFor(int mismatches, int deletions, int insertions) {
        double maxPenalty = .1;
        maxPenalty += mismatches * DEFAULT_PENALTY[0];
        maxPenalty += deletions * DEFAULT_PENALTY[1];
        maxPenalty += insertions * DEFAULT_PENALTY[2];
        return maxPenalty;
    }

    public int getMaxErrors(int errorType) {
        return maxErrors[errorType];
    }

    public double getPenalty(int errorType) {
        return penalty[errorType];
    }

    public int getMaxSubstitutions() {
        return maxErrors[0];
    }

    public int getMaxDeletions() {
        return maxErrors[1];
    }

    public int getMaxInsertions() {
        return maxErrors[2];
    }

    public double getSubstitutionPenalty() {
        return penalty[0];
    }

    public double getDeletionPenalty() {
        return penalty[1];
    }

    public double getInsertionPenalty() {
        return penalty[2];
    }

    public double getMaxPenalty() {
        return maxPenalty;
    }

    public boolean isGreedy() {
        return greedy;
    }

    byte[][] getDifferencesCombination() {
        return differencesCombination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeSearchParameters)) return false;

        TreeSearchParameters that = (TreeSearchParameters) o;

        if (Double.compare(that.maxPenalty, maxPenalty) != 0) return false;
        if (greedy != that.greedy) return false;
        if (!Arrays.equals(maxErrors, that.maxErrors)) return false;
        if (!Arrays.equals(penalty, that.penalty)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = Arrays.hashCode(maxErrors);
        result = 31 * result + Arrays.hashCode(penalty);
        temp = Double.doubleToLongBits(maxPenalty);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (greedy ? 1 : 0);
        return result;
    }

    private static final HashMap<String, TreeSearchParameters> parametersByName;
    private static final HashMap<TreeSearchParameters, String> nameByParameters;

    private static void addKnown(String name, TreeSearchParameters params) {
        parametersByName.put(name.toLowerCase(), params);
        nameByParameters.put(params, name);
    }

    static {
        parametersByName = new HashMap<>();
        nameByParameters = new HashMap<>();

        addKnown("oneMismatch", ONE_MISMATCH);
        addKnown("oneIndel", ONE_INDEL);
        addKnown("oneMismatchOrIndel", ONE_MISMATCH_OR_INDEL);

        addKnown("twoMismatches", TWO_MISMATCHES);
        addKnown("twoIndels", TWO_INDELS);
        addKnown("twoMismatchesOrIndels", TWO_MISMATCHES_OR_INDELS);

        addKnown("threeMismatches", THREE_MISMATCHES);
        addKnown("threeIndels", THREE_INDELS);
        addKnown("threeMismatchesOrIndels", THREE_MISMATCHES_OR_INDELS);

        addKnown("fourMismatches", FOUR_MISMATCHES);
        addKnown("fourIndels", FOUR_INDELS);
        addKnown("fourMismatchesOrIndels", FOUR_MISMATCHES_OR_INDELS);
    }

    public static final class Deserializer extends JsonDeserializer<TreeSearchParameters> {
        @Override
        public TreeSearchParameters deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = jp.readValueAsTree(), tmp;

            if (node instanceof TextNode) {
                TreeSearchParameters params = parametersByName.get(node.textValue().toLowerCase());
                if (params == null)
                    throw new IllegalArgumentException("Unknown parameter set: " + node.textValue());
                return params;
            }

            boolean greedy = true;

            if ((tmp = node.get("greedy")) != null)
                greedy = tmp.asBoolean();

            int[] maxErrors = new int[3];

            if ((tmp = node.get("maxSubstitutions")) != null)
                maxErrors[0] = tmp.asInt();
            if ((tmp = node.get("maxDeletions")) != null)
                maxErrors[1] = tmp.asInt();
            if ((tmp = node.get("maxInsertions")) != null)
                maxErrors[2] = tmp.asInt();

            JsonNode maxPenaltyNode = node.get("maxPenalty");
            if (maxPenaltyNode == null) {
                JsonNode totalMutationsNode = node.get("totalMutations");
                if (totalMutationsNode == null)
                    if (node.get("substitutionPenalty") != null || node.get("deletionPenalty") != null ||
                            node.get("insertionPenalty") != null)
                        throw new IllegalArgumentException("Cannot set totalErrors and penalty related field simultaneously.");
                    else
                        return new TreeSearchParameters(maxErrors[0], maxErrors[1], maxErrors[2]);
                else if (node.get("substitutionPenalty") != null || node.get("deletionPenalty") != null ||
                        node.get("insertionPenalty") != null)
                    throw new IllegalArgumentException("maxPenaltyNode is absent.");
                else
                    return new TreeSearchParameters(maxErrors[0], maxErrors[1], maxErrors[2], totalMutationsNode.asInt());
            } else {
                double maxPenalty = maxPenaltyNode.asDouble();

                double[] penalty = new double[3];

                if ((tmp = node.get("substitutionPenalty")) != null)
                    penalty[0] = tmp.asDouble();
                else if (maxErrors[0] != 0)
                    throw new IllegalArgumentException("substitutionPenalty is absent.");

                if ((tmp = node.get("deletionPenalty")) != null)
                    penalty[1] = tmp.asDouble();
                else if (maxErrors[1] != 0)
                    throw new IllegalArgumentException("deletionPenalty is absent.");

                if ((tmp = node.get("insertionPenalty")) != null)
                    penalty[2] = tmp.asDouble();
                else if (maxErrors[2] != 0)
                    throw new IllegalArgumentException("insertionPenalty is absent.");

                return new TreeSearchParameters(maxErrors, penalty, maxPenalty, greedy);
            }
        }
    }

    public static final class Serializer extends JsonSerializer<TreeSearchParameters> {
        @Override
        public void serialize(TreeSearchParameters value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            String knownName = nameByParameters.get(value);
            if (knownName != null)
                jgen.writeString(knownName);
            else {
                jgen.writeStartObject();

                jgen.writeObjectField("greedy", value.greedy);

                if (value.maxErrors[0] != 0)
                    jgen.writeObjectField("maxSubstitutions", value.maxErrors[0]);
                if (value.maxErrors[1] != 0)
                    jgen.writeObjectField("maxDeletions", value.maxErrors[1]);
                if (value.maxErrors[2] != 0)
                    jgen.writeObjectField("maxInsertions", value.maxErrors[2]);

                if (Arrays.equals(value.penalty, UNIFORM_PENALTY))
                    jgen.writeObjectField("totalMutations", (int) value.maxPenalty);
                else if (!Arrays.equals(value.penalty, DEFAULT_PENALTY)) {
                    jgen.writeObjectField("substitutionPenalty", value.penalty[0]);
                    jgen.writeObjectField("deletionPenalty", value.penalty[1]);
                    jgen.writeObjectField("insertionPenalty", value.penalty[2]);
                    jgen.writeObjectField("maxPenalty", value.maxPenalty);
                }
                jgen.writeEndObject();
            }
        }
    }
}
