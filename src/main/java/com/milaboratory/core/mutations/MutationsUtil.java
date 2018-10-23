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
package com.milaboratory.core.mutations;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.AlignmentIteratorForward;
import com.milaboratory.core.sequence.*;
import com.milaboratory.core.sequence.AminoAcidSequence.AminoAcidSequencePosition;
import com.milaboratory.util.IntArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.milaboratory.core.mutations.Mutation.*;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */

public final class MutationsUtil {
    static final Map<Alphabet, Pattern> mutationPatterns = Collections.synchronizedMap(new HashMap<Alphabet, Pattern>());

    private MutationsUtil() {
    }

    public static NSequenceWithQuality mutate(NSequenceWithQuality seq, Mutations<NucleotideSequence> mutations) {
        NucleotideSequence sequence = seq.getSequence();
        SequenceQuality quality = seq.getQuality();
        int size = seq.size();
        int newSize = seq.size() + mutations.getLengthDelta();
        SequenceQualityBuilder qualityBuilder = new SequenceQualityBuilder().ensureCapacity(newSize);
        SequenceBuilder<NucleotideSequence> sequenceBuilder = NucleotideSequence.ALPHABET.createBuilder().ensureCapacity(newSize);
        int pointer = 0;
        int mutPointer = 0;
        int mut;
        while (pointer < size || mutPointer < mutations.size()) {
            if (mutPointer < mutations.size() && ((mut = mutations.getMutation(mutPointer)) >>> POSITION_OFFSET) <= pointer)
                switch (mut & MUTATION_TYPE_MASK) {
                    case RAW_MUTATION_TYPE_SUBSTITUTION:
                        if (((mut >> FROM_OFFSET) & LETTER_MASK) != sequence.codeAt(pointer))
                            throw new IllegalArgumentException("Mutation = " + Mutation.toString(sequence.getAlphabet(), mut) +
                                    " but seq[" + pointer + "]=" + sequence.symbolAt(pointer));

                        sequenceBuilder.append((byte) (mut & LETTER_MASK));
                        qualityBuilder.append(quality.value(pointer));
                        ++pointer;
                        ++mutPointer;
                        break;
                    case RAW_MUTATION_TYPE_DELETION:
                        if (((mut >> FROM_OFFSET) & LETTER_MASK) != sequence.codeAt(pointer))
                            throw new IllegalArgumentException("Mutation = " + Mutation.toString(sequence.getAlphabet(), mut) +
                                    " but seq[" + pointer + "]=" + sequence.symbolAt(pointer));

                        ++pointer;
                        ++mutPointer;
                        break;
                    case RAW_MUTATION_TYPE_INSERTION:
                        sequenceBuilder.append((byte) (mut & LETTER_MASK));
                        if (pointer == 0)
                            qualityBuilder.append(quality.value(pointer));
                        else
                            qualityBuilder.append((byte) ((quality.value(pointer - 1) + quality.value(pointer)) / 2));
                        ++mutPointer;
                        break;
                }
            else {
                qualityBuilder.append(quality.value(pointer));
                sequenceBuilder.append(sequence.codeAt(pointer++));
            }
        }
        return new NSequenceWithQuality(sequenceBuilder.createAndDestroy(), qualityBuilder.createAndDestroy());
    }

    /**
     * This one shifts indels to the left at homopolymer regions Applicable to KAligner data, which normally put indels
     * randomly along such regions Required for filterMutations algorithm to work correctly Works inplace
     *
     * @param seq1      reference sequence for the mutations
     * @param mutations array of mutations
     */
    public static <S extends Sequence<S>> Mutations<S> shiftIndelsAtHomopolymers(S seq1, Mutations<S> mutations) {
        return shiftIndelsAtHomopolymers(seq1, 0, mutations);
    }

    /**
     * This one shifts indels to the left at homopolymer regions Applicable to KAligner data, which normally put indels
     * randomly along such regions Required for filterMutations algorithm to work correctly Works inplace
     *
     * @param seq1      reference sequence for the mutations
     * @param seq1From  seq1 from
     * @param mutations array of mutations
     */
    public static <S extends Sequence<S>> Mutations<S> shiftIndelsAtHomopolymers(S seq1, int seq1From, Mutations<S> mutations) {
        int[] muts = mutations.mutations.clone();
        shiftIndelsAtHomopolymers(seq1, seq1From, muts);
        return new Mutations<>(mutations.alphabet, muts);
    }

    /**
     * This one shifts indels to the left at homopolymer regions Applicable to KAligner data, which normally put indels
     * randomly along such regions Required for filterMutations algorithm to work correctly Works inplace
     *
     * @param seq1      reference sequence for the mutations
     * @param mutations array of mutations
     */
    public static void shiftIndelsAtHomopolymers(Sequence seq1, int[] mutations) {
        shiftIndelsAtHomopolymers(seq1, 0, mutations);
    }

    /**
     * This one shifts indels to the left at homopolymer regions Applicable to KAligner data, which normally put indels
     * randomly along such regions Required for filterMutations algorithm to work correctly Works inplace
     *
     * @param seq1      reference sequence for the mutations
     * @param seq1From  seq1 from
     * @param mutations array of mutations
     */
    public static void shiftIndelsAtHomopolymers(Sequence seq1, int seq1From, int[] mutations) {
        int prevPos = seq1From;

        for (int i = 0; i < mutations.length; i++) {
            int code = mutations[i];
            if (!isSubstitution(code)) {
                int pos = getPosition(code), offset = 0;
                if (pos < seq1From)
                    throw new IllegalArgumentException();
                int nt = isDeletion(code) ? getFrom(code) : getTo(code);
                while (pos > prevPos && seq1.codeAt(pos - 1) == nt) {
                    pos--;
                    offset--;
                }
                mutations[i] = move(code, offset);
                prevPos = getPosition(mutations[i]);
                if (isDeletion(mutations[i]))
                    prevPos++;
            } else {
                prevPos = getPosition(mutations[i]) + 1;
            }
        }
    }

    public static boolean check(Mutations mutations) {
        return check(mutations.mutations);
    }

    //TODO add more checks
    public static boolean check(int[] mutations) {
        for (int i = 0; i < mutations.length; ++i) {
            if (i > 0) {
                if (isDeletion(mutations[i - 1]) && isInsertion(mutations[i]) &&
                        getPosition(mutations[i - 1]) == getPosition(mutations[i]) - 1)
                    return false;
                if (isDeletion(mutations[i]) && isInsertion(mutations[i - 1]) &&
                        getPosition(mutations[i - 1]) == getPosition(mutations[i]))
                    return false;
            }
            if (isSubstitution(mutations[i]) && getFrom(mutations[i]) == getTo(mutations[i]))
                return false;
        }
        return true;
    }

    public static boolean isSorted(int[] mutations) {
        if (mutations.length == 0)
            return true;
        int position = getPosition(mutations[0]);
        int p;
        for (int i = 1; i < mutations.length; ++i) {
            if ((p = getPosition(mutations[i])) < position)
                return false;
            position = p;
        }
        return true;
    }

    public static <S extends Sequence> boolean isCompatibleWithSequence(S sequence, int[] mutations) {
        for (int mutation : mutations) {
            int position = getPosition(mutation);
            if (isInsertion(mutation)) {
                if (position >= sequence.size() + 1)
                    return false;
            } else if (position >= sequence.size() || sequence.codeAt(position) != getFrom(mutation))
                return false;
        }
        return true;
    }

    public static <S extends Sequence> void assertCompatibleWithSequence(S sequence, int[] mutations) {
        for (int mutation : mutations) {
            int position = getPosition(mutation);
            Alphabet al = sequence.getAlphabet();
            Mutations muts = new Mutations<>(al, mutations);
            if (isInsertion(mutation)) {
                if (position >= sequence.size() + 1) {
                    throw new IllegalArgumentException("Incompatible mutations (" + muts + ") and seq (" + sequence + "): position (" + position + ") in mutation (" + Mutation.encode(mutation, al) + ") is larger than seq size (" + sequence.size() + ")");
                }
            } else {
                if (position >= sequence.size())
                    throw new IllegalArgumentException("Incompatible mutations (" + muts + ") and seq (" + sequence + "): position (" + position + ") in mutation (" + Mutation.encode(mutation, al) + ") is larger than seq size (" + sequence.size() + ")");
                if (sequence.codeAt(position) != getFrom(mutation))
                    throw new IllegalArgumentException("Incompatible mutations (" + muts + ") and seq (" + sequence + "): from letter (" + al.codeToSymbol(getFrom(mutation)) + ") in mutation (" + Mutation.encode(mutation, al) + ") is not equal to letter in seq (" + al.codeToSymbol(sequence.codeAt(position)) + ")");
            }
        }
    }

    static String getMutationPatternStringForAlphabet(Alphabet alphabet) {
        StringBuilder sb = new StringBuilder();
        StringBuilder t = new StringBuilder("([\\Q");
        for (byte i = 0; i < alphabet.size(); ++i)
            t.append(alphabet.codeToSymbol(i));
        t.append("\\E])");
        sb.append("S").append(t).append("(\\d+)").append(t);
        sb.append("|");
        sb.append("D").append(t).append("(\\d+)");
        sb.append("|");
        sb.append("I").append("(\\d+)").append(t);
        return sb.toString();
    }

    static Pattern getMutationPatternForAlphabet(Alphabet alphabet) {
        Pattern pattern = mutationPatterns.get(alphabet);
        if (pattern == null) {
            mutationPatterns.put(alphabet, pattern = Pattern.compile(getMutationPatternStringForAlphabet(alphabet)));
        }
        return pattern;
    }

    /**
     * Encodes mutations in compact human-readable string, that can be decoded by method {@link #decode(String,
     * com.milaboratory.core.sequence.Alphabet)}.
     *
     * <p>For format see {@link com.milaboratory.core.mutations.Mutation#encode(int,
     * com.milaboratory.core.sequence.Alphabet)}.</p>
     *
     * <p>Mutations are just concatenated. The following RegExp can be used for simple parsing of resulting string for
     * nucleotide sequences: {@code ([SDI])([ATGC]?)(\d+)([ATGC]?)} .</p>
     *
     * @param mutations mutations to encode
     * @return mutations in a human-readable string
     */
    public static String encode(int[] mutations, Alphabet alphabet) {
        return encode(mutations, alphabet, "");
    }

    /**
     * Encodes mutations in compact human-readable string, that can be decoded by method {@link #decode(String,
     * com.milaboratory.core.sequence.Alphabet)}.
     *
     * <p>For format see {@link com.milaboratory.core.mutations.Mutation#encode(int,
     * com.milaboratory.core.sequence.Alphabet)}.</p>
     *
     * <p>Mutations are just concatenated with given separator. The following RegExp can be used for simple parsing of
     * resulting string for nucleotide sequences: {@code ([SDI])([ATGC]?)(\d+)([ATGC]?)} .</p>
     *
     * @param mutations mutations to encode
     * @param separator separator
     * @return mutations in a human-readable string
     */
    public static String encode(int[] mutations, Alphabet alphabet, String separator) {
        if (mutations.length == 0)
            return "";

        StringBuilder builder = new StringBuilder();

        for (int mut : mutations)
            builder.append(Mutation.encode(mut, alphabet))
                    .append(separator);

        builder.delete(builder.length() - separator.length(), builder.length());

        return builder.toString();
    }

    public static String encodeFixed(int[] mutations, Alphabet alphabet) {
        StringBuilder builder = new StringBuilder();
        for (int mut : mutations) {
            builder.append(Mutation.encodeFixed(mut, alphabet));
            builder.append(":");
        }
        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /**
     * Decodes mutations encoded using format described in {@link com.milaboratory.core.mutations.Mutation#encode(int,
     * com.milaboratory.core.sequence.Alphabet)}.
     */
    public static int[] decode(String mutations, Alphabet alphabet) {
        Matcher matcher = getMutationPatternForAlphabet(alphabet).matcher(mutations);
        IntArrayList list = new IntArrayList();
        while (matcher.find()) {
            switch (matcher.group(0).charAt(0)) {
                //TODO codeFromSymbolWithError ?
                case 'S':
                    list.add(createSubstitution(Integer.parseInt(matcher.group(2)),
                            alphabet.symbolToCode(matcher.group(1).charAt(0)),
                            alphabet.symbolToCode(matcher.group(3).charAt(0))));
                    break;
                case 'D':
                    list.add(createDeletion(Integer.parseInt(matcher.group(5)),
                            alphabet.symbolToCode(matcher.group(4).charAt(0))));
                    break;
                case 'I':
                    list.add(createInsertion(Integer.parseInt(matcher.group(6)),
                            alphabet.symbolToCode(matcher.group(7).charAt(0))));
                    break;
            }
        }
        return list.toArray();
    }

    private static final Pattern btopPattern = Pattern.compile("([a-zA-Z]{2}|-[a-zA-Z]|[a-zA-Z]-|[0-9]+)");

    /**
     * Decodes btop-encoded mutations.
     *
     * @param alignment btop-encoded mutations
     * @return MiLib mutations
     */
    public static int[] btopDecode(String alignment, Alphabet alphabet) {
        Matcher matcher = btopPattern.matcher(alignment);
        IntArrayList mutations = new IntArrayList();
        int sPosition = 0;
        while (matcher.find()) {
            String g = matcher.group();
            if (isPositiveInteger(g))
                sPosition += Integer.parseInt(g);
            else if (g.charAt(0) == '-') {
                mutations.add(createDeletion(sPosition, alphabet.symbolToCodeWithException(g.charAt(1))));
                ++sPosition;
            } else if (g.charAt(1) == '-')
                mutations.add(createInsertion(sPosition, alphabet.symbolToCodeWithException(g.charAt(0))));
            else {
                mutations.add(createSubstitution(sPosition, alphabet.symbolToCodeWithException(g.charAt(1)), alphabet.symbolToCodeWithException(g.charAt(0))));
                ++sPosition;
            }
        }
        return mutations.toArray();
    }

    private static boolean isPositiveInteger(String str) {
        if (str.isEmpty())
            return false;
        for (int i = 0, length = str.length(); i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':')
                return false;
        }
        return true;
    }

    public static Mutations<AminoAcidSequence> nt2aa(NucleotideSequence seq1, Mutations<NucleotideSequence> mutations,
                                                     TranslationParameters translationParameters) {
        return nt2aa(seq1, mutations, translationParameters, Integer.MAX_VALUE);
    }

    public static Mutations<AminoAcidSequence> nt2aa(NucleotideSequence seq1, Mutations<NucleotideSequence> mutations,
                                                     TranslationParameters translationParameters,
                                                     int maxShiftedTriplets) {
        AminoAcidSequence aaSeq1 = AminoAcidSequence.translate(seq1, translationParameters);
        NucleotideSequence seq2 = mutations.mutate(seq1);
        AminoAcidSequence aaSeq2 = AminoAcidSequence.translate(seq2, translationParameters);
        MutationsBuilder<AminoAcidSequence> result = new MutationsBuilder<>(AminoAcidSequence.ALPHABET);

        int aaP2, prevAAP2 = -1;
        int ntP1, ntP2;
        int shiftedTriplets = 0;
        for (int aaP1 = 0; aaP1 <= aaSeq1.size(); aaP1++) {
            if (aaP1 != aaSeq1.size()) {
                ntP1 = AminoAcidSequence.convertAAPositionToNt(aaP1, seq1.size(), translationParameters);
                ntP2 = mutations.convertToSeq2Position(ntP1);
                if (ntP2 < 0)
                    ntP2 = -ntP2 - 2;

                if (ntP2 < 0)
                    aaP2 = -1;
                else {
                    AminoAcidSequencePosition pos = AminoAcidSequence.convertNtPositionToAA(ntP2, seq2.size(), translationParameters);
                    if (pos == null)
                        continue;

                    // Detecting shifted triplets
                    if (pos.positionInTriplet != 0)
                        if (shiftedTriplets++ > maxShiftedTriplets)
                            return null;

                    aaP2 = pos.aminoAcidPosition;
                }
            } else {
                aaP2 = aaSeq2.size();
            }

            if (aaP2 == prevAAP2) { // Deletion
                if (aaP1 < aaSeq1.size())
                    result.appendDeletion(aaP1, aaSeq1.codeAt(aaP1));
            } else {
                if (aaP2 > prevAAP2 + 1) // Insertion
                    for (int i = prevAAP2 + 1; i < aaP2; i++) {
                        int m;
                        // Special case
                        if (result.size() > 0 && Mutation.isSubstitution(m = result.getLast()) && Mutation.getPosition(m) == aaP1 - 1 &&
                                Mutation.getFrom(m) == aaSeq2.codeAt(i)) {
                            result.removeLast();
                            result.appendInsertion(aaP1 - 1, Mutation.getTo(m));
                        } else
                            result.appendInsertion(aaP1, aaSeq2.codeAt(i));
                    }
                if (aaP1 < aaSeq1.size() && aaSeq2.codeAt(aaP2) != aaSeq1.codeAt(aaP1)) // Substitution
                    result.appendSubstitution(aaP1, aaSeq1.codeAt(aaP1), aaSeq2.codeAt(aaP2));
                prevAAP2 = aaP2;
            }
        }
        return result.createAndDestroy();
    }

    public static int[] nt2IndividualAA(NucleotideSequence seq1, Mutations<NucleotideSequence> mutations,
                                        TranslationParameters translationParameters) {
        int[] result = new int[mutations.size()];
        for (int i = 0; i < mutations.size(); i++) {
            if (mutations.getRawTypeByIndex(i) != RAW_MUTATION_TYPE_SUBSTITUTION)
                result[i] = NON_MUTATION;
            else {
                Mutations<AminoAcidSequence> aaMuts
                        = nt2aa(seq1, mutations.getRange(i, i + 1), translationParameters);
                result[i] = aaMuts.size() == 0 ? NON_MUTATION : aaMuts.getMutation(0);
            }
        }
        return result;
    }

    public static MutationsWitMapping nt2aaWithMapping(NucleotideSequence seq1, Mutations<NucleotideSequence> mutations,
                                                       TranslationParameters translationParameters,
                                                       int maxShiftedTriplets) {
        Mutations<AminoAcidSequence> aaMutations = nt2aa(seq1, mutations, translationParameters, maxShiftedTriplets);

        if (aaMutations == null)
            return null;

        int[] mapping = new int[mutations.size()];
        Arrays.fill(mapping, -1);

        // Already calculates in nt2aa()
        AminoAcidSequence aaSeq1 = AminoAcidSequence.translate(seq1, translationParameters);
        NucleotideSequence seq2 = mutations.mutate(seq1);
        AminoAcidSequence aaSeq2 = AminoAcidSequence.translate(seq2, translationParameters);

        AlignmentIteratorForward<NucleotideSequence> ntIterator = new AlignmentIteratorForward<>(mutations,
                new Range(0, seq1.size()));

        AlignmentIteratorForward<AminoAcidSequence> aaIterator = new AlignmentIteratorForward<>(aaMutations,
                new Range(0, aaSeq2.size()));
        boolean activeAAIterator = aaIterator.advance();

        while (ntIterator.advance()) {
            if (ntIterator.getCurrentMutation() == NON_MUTATION)
                continue;

            AminoAcidSequencePosition aaSeq1Position = AminoAcidSequence.convertNtPositionToAA(
                    ntIterator.getSeq1Position(), seq1.size(), translationParameters);
            AminoAcidSequencePosition aaSeq2Position = AminoAcidSequence.convertNtPositionToAA(
                    ntIterator.getSeq2Position(), seq2.size(), translationParameters);

            if (aaSeq1Position == null || aaSeq2Position == null)
                continue;

            if (activeAAIterator)
                do {

                    if (((aaSeq1Position.aminoAcidPosition == aaIterator.getSeq1Position() && aaSeq2Position.aminoAcidPosition <= aaIterator.getSeq2Position()) ||
                            (aaSeq1Position.aminoAcidPosition <= aaIterator.getSeq1Position() && aaSeq2Position.aminoAcidPosition == aaIterator.getSeq2Position())
                    ) && aaIterator.getCurrentMutation() != NON_MUTATION)
                        mapping[ntIterator.getMutationsPointer()] = aaIterator.getMutationsPointer();

                } while (!(aaSeq1Position.aminoAcidPosition <= aaIterator.getSeq1Position() &&
                        aaSeq2Position.aminoAcidPosition <= aaIterator.getSeq2Position()) &&
                        (activeAAIterator = aaIterator.advance()));
        }

        return new MutationsWitMapping(aaMutations, mapping);
    }

    /**
     * Performs a comprehensive translation of mutations present in a nucleotide sequence to effective mutations in
     * corresponding amino acid sequence.
     *
     * <p>The resulting array contains:</p>
     *
     * <ul> <li>the original nucleotide mutation</li> <li>"individual" amino acid mutation, i.e. an expected amino acid
     * mutation given no other mutations have occurred</li> <li>"cumulative" amino acid mutation, i.e. the observed
     * amino acid mutation combining effect from all other nucleotide mutations</li> </ul>
     *
     * @param seq1                  the reference nucleotide sequence
     * @param mutations             nucleotide mutations in the reference nucleotide sequence
     * @param translationParameters translation parameters
     * @param maxShiftedTriplets    max number of shifted triplets for computing the cumulative effect from indels
     * @return an array of nucleotide mutations with their amino acid translations
     */
    public static MutationNt2AADescriptor[] nt2aaDetailed(NucleotideSequence seq1, Mutations<NucleotideSequence> mutations,
                                                          TranslationParameters translationParameters,
                                                          int maxShiftedTriplets) {
        MutationsWitMapping mutationsWitMapping = nt2aaWithMapping(seq1, mutations, translationParameters, maxShiftedTriplets);
        int[] individualMutations = nt2IndividualAA(seq1, mutations, translationParameters);
        MutationNt2AADescriptor[] result = new MutationNt2AADescriptor[mutations.size()];
        for (int i = 0; i < mutations.size(); i++) {
            result[i] = new MutationNt2AADescriptor(mutations.getMutation(i), individualMutations[i],
                    mutationsWitMapping.mapping[i] == -1 ? NON_MUTATION :
                            mutationsWitMapping.mutations.getMutation(mutationsWitMapping.mapping[i]));
        }
        return result;
    }

    public static final class MutationNt2AADescriptor {
        public final int originalNtMutation,
                individualAAMutation,
                cumulativeAAMutation;

        public MutationNt2AADescriptor(int originalNtMutation, int individualAAMutation, int cumulativeAAMutation) {
            this.originalNtMutation = originalNtMutation;
            this.individualAAMutation = individualAAMutation;
            this.cumulativeAAMutation = cumulativeAAMutation;
        }

        @Override
        public String toString() {
            return Mutation.encode(originalNtMutation, NucleotideSequence.ALPHABET) + ":" +
                    (individualAAMutation == NON_MUTATION ? "" : Mutation.encode(individualAAMutation, AminoAcidSequence.ALPHABET)) + ":" +
                    (cumulativeAAMutation == NON_MUTATION ? "" : Mutation.encode(cumulativeAAMutation, AminoAcidSequence.ALPHABET));
        }
    }

    public static final class MutationsWitMapping {
        public final Mutations<AminoAcidSequence> mutations;
        public final int[] mapping;

        public MutationsWitMapping(Mutations<AminoAcidSequence> mutations, int[] mapping) {
            this.mutations = mutations;
            this.mapping = mapping;
        }
    }
}
