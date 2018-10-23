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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.*;
import com.milaboratory.core.io.util.IOTestUtil;
import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.AminoAcidSequence;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.test.TestUtil;
import com.milaboratory.util.IntArrayList;
import org.junit.Assert;
import org.junit.Test;

import static com.milaboratory.core.mutations.Mutations.EMPTY_NUCLEOTIDE_MUTATIONS;
import static com.milaboratory.core.mutations.Mutations.decode;
import static com.milaboratory.util.RandomUtil.getThreadLocalRandom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
@SuppressWarnings("Duplicates")
public class MutationsTest {

    @Test
    public void testCombine1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("CATTACCA"),
                seq3 = new NucleotideSequence("CATAGCCA");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations(),
                m2 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq2, seq3).getAbsoluteMutations();

        checkMutations(m1);
        checkMutations(m2);
        Mutations<NucleotideSequence> m3 = m1.combineWith(m2);
        assertTrue(MutationsUtil.check(m3));
        Assert.assertEquals(seq3, m3.mutate(seq1));
    }

    @Test
    public void testCombine2() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ACGTGTTACCGGTGATT"),
                seq2 = new NucleotideSequence("AGTTCTTGTTTTTTCCGTAC"),
                seq3 = new NucleotideSequence("ATCCGTAAATTACGTGCTGT");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations(),
                m2 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq2, seq3).getAbsoluteMutations();

        Mutations<NucleotideSequence> m3 = m1.combineWith(m2);

        assertTrue(MutationsUtil.check(m3));
        checkMutations(m1);
        checkMutations(m2);
        checkMutations(m3);

        Assert.assertEquals(seq3, m3.mutate(seq1));
    }

    @Test
    public void testCombine3() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("AACTGCTAACTCGA"),
                seq2 = new NucleotideSequence("CGAACGTTAAGCACAAA"),
                seq3 = new NucleotideSequence("CAAATGTGAGATC");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations(),
                m2 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq2, seq3).getAbsoluteMutations();

        Mutations<NucleotideSequence> m3 = m1.combineWith(m2);

        assertTrue(MutationsUtil.check(m3));
        checkMutations(m1);
        checkMutations(m2);
        checkMutations(m3);

        Assert.assertEquals(seq3, m3.mutate(seq1));
    }

    @Test
    public void testExtract0() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGAGA"),
                seq2 = new NucleotideSequence("TTTAGACA");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations();

        Mutations<NucleotideSequence> extracted = m1.extractRelativeMutationsForRange(0, seq1.size());

        Assert.assertEquals(m1, extracted);
        Assert.assertTrue(extracted == m1);

        for (int i = 0; i < m1.size(); i++) {
            extracted = m1.extractRelativeMutationsForRange(i, i);
            Assert.assertEquals(decode("", NucleotideSequence.ALPHABET), extracted);
        }
    }

    @Test
    public void testExtract1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("AACTGCTAACTCGA"),
                seq2 = new NucleotideSequence("AACGTCTACCTCGA");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations();

        Mutations<NucleotideSequence> extracted = m1.extractRelativeMutationsForRange(3, seq1.size());
        Assert.assertEquals(decode("DG1 SA5C", NucleotideSequence.ALPHABET), extracted);

        extracted = m1.extractRelativeMutationsForRange(2, seq1.size());
        Assert.assertEquals(decode("I1G DG2 SA6C", NucleotideSequence.ALPHABET), extracted);

        for (int i = 0; i < m1.size(); i++) {
            extracted = m1.extractRelativeMutationsForRange(i, i);
            Assert.assertEquals(decode("", NucleotideSequence.ALPHABET), extracted);
        }
    }

    @Test
    public void testRemove0() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGAGA"),
                seq2 = new NucleotideSequence("TTTAGACA");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations();

        Mutations<NucleotideSequence> extracted = m1.removeMutationsInRange(0, seq1.size());

        Assert.assertEquals(EMPTY_NUCLEOTIDE_MUTATIONS, extracted);

        for (int i = 0; i < m1.size(); i++) {
            extracted = m1.removeMutationsInRange(i, i);
            Assert.assertEquals(m1, extracted);
            Assert.assertTrue(m1 == extracted);
        }
    }

    @Test
    public void testRemove1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("AACTGCTAACTCGA"),
                seq2 = new NucleotideSequence("AACGTCTACCTCGA");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations();

        Mutations<NucleotideSequence> extracted = m1.removeMutationsInRange(3, seq1.size());
        Assert.assertEquals(decode("I3G", NucleotideSequence.ALPHABET), extracted);


        extracted = m1.removeMutationsInRange(3, 5);
        Assert.assertEquals(decode("I3G SA6C", NucleotideSequence.ALPHABET), extracted);

        extracted = m1.removeMutationsInRange(2, seq1.size());
        Assert.assertEquals(EMPTY_NUCLEOTIDE_MUTATIONS, extracted);
        Assert.assertTrue(EMPTY_NUCLEOTIDE_MUTATIONS == extracted);

        for (int i = 0; i < m1.size(); i++) {
            extracted = m1.removeMutationsInRange(i, i);
            Assert.assertEquals(m1, extracted);
            Assert.assertTrue(m1 == extracted);
        }
    }

    @Test
    public void testRemove2() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("AACTGCTAACTCGA"),
                seq2 = new NucleotideSequence("AACGTCTACCTCGA");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations();

        Mutations<NucleotideSequence> extracted = m1.removeMutationsInRanges(new Range(3, 5), new Range(5, 6));
        Assert.assertEquals(decode("I3G SA5C", NucleotideSequence.ALPHABET), extracted);
    }

    @Test
    public void testConvert1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGGCCTAGAATCGCGATCGTTGATCACA"),
                seq2 = new NucleotideSequence("ATTAGGATCTGCCGTAGAAATCGTAGCTTGATCACA");

        String p1s = "012345     67 890123456789012    345678901";
        //          0 attagg-----cc-tagaaTCGCGatcgt----tgatcaca 30
        //          0 attaggATCTGccGtagaa-----atcgtAGCTtgatcaca 35
        String p2s = "0123456789012345678     901234567890123456";

        int[] p1 = psToP(p1s, p2s);
        int[] p2 = psToP(p2s, p1s);

        Assert.assertEquals(seq1.size(), p2.length - 1);
        Assert.assertEquals(seq2.size(), p1.length - 1);

        Alignment<NucleotideSequence> a = Aligner.alignGlobal(AffineGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2);
        //System.out.println(a);
        Mutations<NucleotideSequence> m = a.getAbsoluteMutations();
        for (int i = 0; i <= seq2.size(); i++)
            assertEquals("Position = " + i, p1[i], m.convertToSeq1Position(i));

        for (int i = 0; i <= seq1.size(); i++)
            assertEquals("Position = " + i, p2[i], m.convertToSeq2Position(i));
    }

    @Test
    public void testConvert2() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("TTAATTAGGCCTAGAATCGCGATCGTTGATCACA"),
                seq2 = new NucleotideSequence("ATTAGGATCTGCCGTAGAAATCGTAGCTTGATCACATT");

        String p1s = "XXX012345     67 890123456789012    34567890  1";
        //          0 TTAattagg-----cc-tagaaTCGCGatcgt----tgatcaca-- 34
        //          0 ---attaggATCTGccGtagaa-----atcgtAGCTtgatcacaTT 37
        String p2s = "   0123456789012345678     90123456789012345678";

        int[] p1 = psToP(p1s, p2s);
        int[] p2 = psToP(p2s, p1s);

        Assert.assertEquals(seq1.size(), p2.length - 1);
        Assert.assertEquals(seq2.size(), p1.length - 1);

        Alignment<NucleotideSequence> a = Aligner.alignGlobal(AffineGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2);
        //System.out.println(a);
        Mutations<NucleotideSequence> m = a.getAbsoluteMutations();
        for (int i = 0; i <= seq2.size(); i++)
            assertEquals("Position = " + i, p1[i], m.convertToSeq1Position(i));

        for (int i = 0; i <= seq1.size(); i++)
            assertEquals("Position = " + i, p2[i], m.convertToSeq2Position(i));
    }

    @Test
    public void testConvert3() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGGCCTAGAATCGCGATCGTTGATCACATT"),
                seq2 = new NucleotideSequence("GACATTAGGATCTGCCGTAGAAATCGTAGCTTGATCACA");

        String p1s = "   012345     67 890123456789012    34567890123";
        //          0 ---attagg-----cc-tagaaTCGCGatcgt----tgatcacaTT 32
        //          0 GACattaggATCTGccGtagaa-----atcgtAGCTtgatcaca-- 39
        String p2s = "XXX0123456789012345678     90123456789012345  6";

        int[] p1 = psToP(p1s, p2s);
        int[] p2 = psToP(p2s, p1s);

        Assert.assertEquals(seq1.size(), p2.length - 1);
        Assert.assertEquals(seq2.size(), p1.length - 1);

        Alignment<NucleotideSequence> a = Aligner.alignGlobal(AffineGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2);
        //System.out.println(a);
        Mutations<NucleotideSequence> m = a.getAbsoluteMutations();
        for (int i = 0; i <= seq2.size(); i++)
            assertEquals("Position = " + i, p1[i], m.convertToSeq1Position(i));

        for (int i = 0; i <= seq1.size(); i++)
            assertEquals("Position = " + i, p2[i], m.convertToSeq2Position(i));
    }

    public int[] psToP(String ps1, String ps2) {
        IntArrayList result = new IntArrayList();
        int u = -1;
        int v;
        for (int i = 0; i < ps1.length(); i++) {
            if (ps1.charAt(i) == ' ')
                v = -2 - u;
            else
                v = ++u;
            if (ps2.charAt(i) != ' ')
                result.add(v);
        }
        return result.toArray();
    }

    @Test
    public void testBS() throws Exception {
        NucleotideSequence
                seq1 = new NucleotideSequence("TGACCCGTAACCCCCCGGT"),
                seq2 = new NucleotideSequence("CGTAACTTCAGCCT");

        Alignment<NucleotideSequence> alignment = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2);

//        AlignmentHelper helper = alignment.getAlignmentHelper();
//        System.out.println(helper);
//
//        int p;
//        for (int i = helper.size() - 1; i >= 0; --i) {
//            if ((p = helper.getSequence1PositionAt(i)) > 0)
//                assertEquals(NucleotideSequence.ALPHABET.codeToSymbol(seq1.codeAt(p)),
//                        helper.getLine1().charAt(i));
//            if ((p = helper.getSequence2PositionAt(i)) > 0)
//                assertEquals(NucleotideSequence.ALPHABET.codeToSymbol(seq2.codeAt(p)),
//                        helper.getLine3().charAt(i));
//        }

        Mutations<NucleotideSequence> mutations = alignment.getAbsoluteMutations();
        checkMutations(mutations);

        Assert.assertEquals(-1, mutations.firstMutationWithPosition(-1));
        Assert.assertEquals(3, mutations.firstMutationWithPosition(3));
        Assert.assertEquals(4, mutations.firstMutationWithPosition(4));
        Assert.assertEquals(-6, mutations.firstMutationWithPosition(5));
        Assert.assertEquals(5, mutations.firstMutationWithPosition(11));
        Assert.assertEquals(7, mutations.firstMutationWithPosition(12));
        Assert.assertEquals(8, mutations.firstMutationWithPosition(13));
    }

    public static void checkMutations(Mutations mutations) {
        assertEquals("Encode/Decode", mutations, decode(mutations.encode(), NucleotideSequence.ALPHABET));
    }

    @Test
    public void testEncodeDecode() throws Exception {
        checkMutations("I2_", AminoAcidSequence.ALPHABET);
    }

    public static void checkMutations(String mutationsString, Alphabet<?> alphabet) {
        assertEquals("Decode/Encode", mutationsString, decode(mutationsString, alphabet).encode());
    }

    @Test
    public void test1() throws Exception {
        MutationsBuilder<NucleotideSequence> builder = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        builder.appendDeletion(1, 2);
        builder.appendDeletion(2, 1);
        builder.appendSubstitution(7, 3, 1);
        builder.appendInsertion(9, 2);
        builder.appendSubstitution(10, 3, 1);
        Mutations<NucleotideSequence> mutations = builder.createAndDestroy();
        assertEquals(1, mutations.getPositionByIndex(0));
        assertEquals(1, mutations.getFromAsCodeByIndex(1));
        assertEquals(3, mutations.getFromAsCodeByIndex(2));
        assertEquals(1, mutations.getToAsCodeByIndex(2));
        assertEquals(9, mutations.getPositionByIndex(3));
        assertEquals(2, mutations.getToAsCodeByIndex(3));
        assertEquals(1, mutations.getToAsCodeByIndex(4));
        assertEquals(10, mutations.getPositionByIndex(4));
    }

    @Test
    public void test2() throws Exception {
        MutationsBuilder<NucleotideSequence> builder = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        builder.appendDeletion(1, 2);
        builder.appendDeletion(2, 1);
        builder.appendSubstitution(7, 3, 1);
        builder.appendInsertion(9, 2);
        builder.appendSubstitution(10, 3, 1);
        Mutations<NucleotideSequence> mutations = builder.createAndDestroy();
        assertEquals(1, mutations.firsMutationPosition());
        assertEquals(10, mutations.lastMutationPosition());
    }

    @Test
    public void test3() throws Exception {
        MutationsBuilder<NucleotideSequence> builder = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        builder.appendDeletion(1, 2);
        builder.appendDeletion(2, 1);
        builder.appendSubstitution(7, 3, 1);
        builder.appendInsertion(9, 2);
        builder.appendSubstitution(10, 3, 1);
        Mutations<NucleotideSequence> se = builder.createAndDestroy();
        IOTestUtil.assertJavaSerialization(se);
    }

    @Test
    public void testRandom1() throws Exception {
        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(30);
        for (int i = 0; i < 100; i++) {
            NucleotideSequence seq0 = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 400, 800);
            Mutations<NucleotideSequence> muts = MutationsGenerator.generateMutations(seq0, model);
            int from = getThreadLocalRandom().nextInt(seq0.size());
            int to = (from == seq0.size() - 1) ? from : from + getThreadLocalRandom().nextInt(seq0.size() - from);
            Mutations<NucleotideSequence> inRangeMuts = muts.extractRelativeMutationsForRange(from, to);
            Mutations<NucleotideSequence> outOfRangeMuts = muts.removeMutationsInRange(from, to);
            NucleotideSequence inRangeSeq = seq0.getRange(from, to);
            NucleotideSequence outOfRangeSeq = seq0.getRange(0, from).concatenate(seq0.getRange(to, seq0.size()));
            assertTrue(inRangeMuts.isCompatibleWith(inRangeSeq));
            assertTrue(outOfRangeMuts.isCompatibleWith(outOfRangeSeq));
        }
    }

    @Test
    public void testCanonical1() throws Exception {
        NucleotideSequence seq0 = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 400, 800);
        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(10);
        Mutations<NucleotideSequence> mutsA = MutationsGenerator.generateMutations(seq0, model);
        NucleotideSequence seqA = mutsA.mutate(seq0);
        Mutations<NucleotideSequence> mutsB = MutationsGenerator.generateMutations(seqA, model);
        NucleotideSequence seqAB = mutsB.mutate(seqA);
        System.out.println(seq0);
        System.out.println(seqA);
        System.out.println(seqAB);

        Alignment<NucleotideSequence> alignment = Aligner.alignGlobalLinear(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seqAB, seq0);
        System.out.println(alignment.getAlignmentHelper());

        Mutations<NucleotideSequence> mutsAB = mutsA.combineWith(mutsB);
        Mutations<NucleotideSequence> mutsnAB = alignment.getAbsoluteMutations();
        Mutations<NucleotideSequence> mutsABnAB = mutsAB.combineWith(mutsnAB);

        Assert.assertEquals(0, mutsABnAB.getLengthDelta());

        Alignment<NucleotideSequence> alignment0 = new Alignment<>(seq0, mutsABnAB, new Range(0, seq0.size()),
                new Range(0, seq0.size()), 0.0f);
        System.out.println(mutsABnAB);
        System.out.println(alignment0.getAlignmentHelper());
    }

    @Test
    public void testSerialize1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("CATTACCA"),
                seq3 = new NucleotideSequence("CATAGCCA");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations(),
                m2 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq2, seq3).getAbsoluteMutations();

        TestUtil.assertJson(m1, TypeFactory.defaultInstance().constructParametricType(Mutations.class, NucleotideSequence.class));
        TestUtil.assertJson(m2, TypeFactory.defaultInstance().constructParametricType(Mutations.class, NucleotideSequence.class));
        TestUtil.assertJson(m1);
        TestUtil.assertJson(m2);
    }

    @Test
    public void testSerializeField1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("CATTACCA"),
                seq3 = new NucleotideSequence("CATAGCCA");

        Mutations<NucleotideSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2).getAbsoluteMutations(),
                m2 = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq2, seq3).getAbsoluteMutations();

        TestNClass t1 = new TestNClass(m1), t2 = new TestNClass(m2), t3 = new TestNClass(null);
        TestXClass x1 = new TestXClass(m1), x2 = new TestXClass(m2), x3 = new TestXClass(null);

        TestUtil.assertJson(t1);
        TestUtil.assertJson(t2);
        TestUtil.assertJson(t3);
        TestUtil.assertJson(x1);
        TestUtil.assertJson(x2);
        TestUtil.assertJson(x3);
    }

    @Test
    public void testSerializeField2() throws Exception {
        AminoAcidSequence seq1 = new AminoAcidSequence("CASSLAPGATNEAKFL"),
                seq2 = new AminoAcidSequence("CASSGAPTNEAKFL"),
                seq3 = new AminoAcidSequence("CASLAPGATNAKRL");

        Mutations<AminoAcidSequence> m1 = Aligner.alignGlobal(LinearGapAlignmentScoring.getAminoAcidBLASTScoring(BLASTMatrix.BLOSUM62), seq1, seq2).getAbsoluteMutations(),
                m2 = Aligner.alignGlobal(LinearGapAlignmentScoring.getAminoAcidBLASTScoring(BLASTMatrix.BLOSUM62), seq2, seq3).getAbsoluteMutations();

        TestAAClass t1 = new TestAAClass(m1), t2 = new TestAAClass(m2), t3 = new TestAAClass(null);
        TestXClass x1 = new TestXClass(m1), x2 = new TestXClass(m2), x3 = new TestXClass(null);

        TestUtil.assertJson(t1);
        TestUtil.assertJson(t2);
        TestUtil.assertJson(t3);
        TestUtil.assertJson(x1);
        TestUtil.assertJson(x2);
        TestUtil.assertJson(x3);
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static final class TestNClass {
        final Mutations<NucleotideSequence> muts;

        @JsonCreator
        public TestNClass(@JsonProperty("muts") Mutations<NucleotideSequence> muts) {
            this.muts = muts;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestNClass)) return false;

            TestNClass that = (TestNClass) o;

            return muts != null ? muts.equals(that.muts) : that.muts == null;

        }

        @Override
        public int hashCode() {
            return muts != null ? muts.hashCode() : 0;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static final class TestAAClass {
        final Mutations<AminoAcidSequence> muts;

        @JsonCreator
        public TestAAClass(@JsonProperty("muts") Mutations<AminoAcidSequence> muts) {
            this.muts = muts;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestAAClass)) return false;

            TestAAClass that = (TestAAClass) o;

            return muts != null ? muts.equals(that.muts) : that.muts == null;

        }

        @Override
        public int hashCode() {
            return muts != null ? muts.hashCode() : 0;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static final class TestXClass {
        final Mutations<?> muts;

        @JsonCreator
        public TestXClass(@JsonProperty("muts") Mutations<?> muts) {
            this.muts = muts;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestXClass)) return false;

            TestXClass that = (TestXClass) o;

            return muts != null ? muts.equals(that.muts) : that.muts == null;

        }

        @Override
        public int hashCode() {
            return muts != null ? muts.hashCode() : 0;
        }
    }
}