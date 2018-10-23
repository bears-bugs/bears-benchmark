package com.milaboratory.core.alignment;

import com.milaboratory.core.Range;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.MutationsBuilder;
import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.test.TestUtil;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.milaboratory.core.alignment.AlignerTest.assertAlignment;
import static com.milaboratory.core.alignment.BandedAffineAligner.semiLocalLeft;
import static com.milaboratory.test.TestUtil.its;
import static com.milaboratory.test.TestUtil.randomSequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by poslavsky on 20/10/15.
 */
public class BandedAffineAlignerTest {
    @Test
    public void test1() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 1, -10, -3, -1);

        NucleotideSequence a = new NucleotideSequence("ataaaaaaatgatcgacaaaaaaaatttttttt");
        NucleotideSequence b = new NucleotideSequence("agtcgttagcgacaaaaaaa");

        a = new NucleotideSequence("atcgagctagttttttttttt");
        b = new NucleotideSequence("ataaaaaaaaaaacgagctag");

        MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        BandedAffineAligner.align0(scoring, a, b, 0, a.size(), 0, b.size(), 151, mutations, new BandedAffineAligner.MatrixCache());

        System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
        System.out.println(Aligner.alignGlobalAffine(scoring, a, b));
        //System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
    }

    @Test
    public void test11() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 1, -10, -3, -1);

        NucleotideSequence a = new NucleotideSequence("ataaaaaaatgatcgacaaaaaaaatttttttt");
        NucleotideSequence b = new NucleotideSequence("agtcgttagcgacaaaaaaa");

        a = new NucleotideSequence("");
        b = new NucleotideSequence("ataaaaaaaaaaacgagctag");

        MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        BandedAffineAligner.align0(scoring, a, b, 0, a.size(), 0, b.size(), 151, mutations, new BandedAffineAligner.MatrixCache());

        System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
        System.out.println(Aligner.alignGlobalAffine(scoring, a, b));
        //System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
    }

    @Test
    public void test2() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 3, -1, -3, -1);

        NucleotideSequence a = new NucleotideSequence("ataaaaaaatgatcgacaaaaaaaatttttttt");
        NucleotideSequence b = new NucleotideSequence("agtcgttagcgacaaaaaaa");

        a = new NucleotideSequence("atgcggggatgc");
        b = new NucleotideSequence("atgctaatgcttttttttttt");

        MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        BandedSemiLocalResult res = BandedAffineAligner.semiLocalRight0(scoring, a, b, 0, a.size(), 0, b.size(), 2, mutations, new BandedAffineAligner.MatrixCache());

        //BandedAffineAligner.align0(scoring, a, b, 0, a.size(), 0, b.size(), 151, mutations, new BandedAffineAligner.MatrixCache());

        System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, res.sequence1Stop + 1), new Range(0, res.sequence2Stop + 1), 100));
        System.out.println(Aligner.alignGlobalAffine(scoring, a, b));
        //System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
    }

    @Test
    public void test23() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 3, -1, -3, -1);

        NucleotideSequence a = new NucleotideSequence("ataaaaaaatgatcgacaaaaaaaatttttttt");
        NucleotideSequence b = new NucleotideSequence("agtcgttagcgacaaaaaaa");

        a = new NucleotideSequence("atgcggggatgc");
        b = new NucleotideSequence("atgcggggatgc");

        MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        BandedSemiLocalResult res = BandedAffineAligner.semiLocalRight0(scoring, a, b, 0, a.size(), 0, b.size(), 2, mutations, new BandedAffineAligner.MatrixCache());
        //BandedSemiLocalResult res = BandedLinearAligner.alignSemiLocalLeft0(
        //        new LinearGapAlignmentScoring<>(NucleotideSequence.ALPHABET, 5, -2, -4), a, b, 0, a.size(), 0,
        //        b.size(), 2, -1000, mutations, new CachedIntArray());

        //BandedAffineAligner.align0(scoring, a, b, 0, a.size(), 0, b.size(), 151, mutations, new BandedAffineAligner.MatrixCache());

        System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, res.sequence1Stop + 1), new Range(0, res.sequence2Stop + 1), 100));

        //System.out.println(Aligner.alignGlobalAffine(scoring, a, b));
        //System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
    }

    @Test
    public void test3() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 3, -1, -3, -1);

        NucleotideSequence a = new NucleotideSequence("ataaaaaaatgatcgacaaaaaaaatttttttt");
        NucleotideSequence b = new NucleotideSequence("agtcgttagcgacaaaaaaa");

        a = new NucleotideSequence("cgtaggggcgta");
        b = new NucleotideSequence("tttttttttttcgtaatcgta");

        MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        BandedSemiLocalResult res = BandedAffineAligner.semiLocalLeft0(scoring, a, b, 0, a.size(), 0, b.size(), 2, mutations, new BandedAffineAligner.MatrixCache());

        //BandedAffineAligner.align0(scoring, a, b, 0, a.size(), 0, b.size(), 151, mutations, new BandedAffineAligner.MatrixCache());

        System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(res.sequence1Stop, a.size()), new Range(res.sequence2Stop, b.size()), 100));
        System.out.println(Aligner.alignGlobalAffine(scoring, a, b));
        //System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
    }

    @Test
    public void test4() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 3, -1, -3, -1);

        NucleotideSequence a = new NucleotideSequence("ataaaaaaatgatcgacaaaaaaaatttttttt");
        NucleotideSequence b = new NucleotideSequence("agtcgttagcgacaaaaaaa");

        //a = new NucleotideSequence("atgcggggatgc");
        //b = new NucleotideSequence("atgctaatgcttttttttttt");
        a = new NucleotideSequence("atgcggggatgttttttt");
        b = new NucleotideSequence("atgcggggatag");


        MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        BandedSemiLocalResult res = BandedAffineAligner.semiGlobalRight0(scoring, a, b,
                0, a.size(), 2,
                0, b.size(), 2,
                2, mutations, new BandedAffineAligner.MatrixCache());

        //BandedAffineAligner.align0(scoring, a, b, 0, a.size(), 0, b.size(), 151, mutations, new BandedAffineAligner.MatrixCache());

        System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, res.sequence1Stop + 1), new Range(0, res.sequence2Stop + 1), 100));
        System.out.println(Aligner.alignGlobalAffine(scoring, a, b));
        //System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
    }

    @Test
    public void test5() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 3, -1, -3, -1);

        NucleotideSequence a = new NucleotideSequence("ataaaaaaatgatcgacaaaaaaaatttttttt");
        NucleotideSequence b = new NucleotideSequence("agtcgttagcgacaaaaaaa");

        //a = new NucleotideSequence("atgcggggatgc");
        //b = new NucleotideSequence("atgctaatgcttttttttttt");
        a = new NucleotideSequence("tttttttgtaggggcgta");
        b = new NucleotideSequence("gataggggcgta");


        MutationsBuilder<NucleotideSequence> mutations = new MutationsBuilder<>(NucleotideSequence.ALPHABET);
        BandedSemiLocalResult res = BandedAffineAligner.semiGlobalLeft0(scoring, a, b,
                3, a.size() - 3, 6,
                0, b.size(), 2,
                2, mutations, new BandedAffineAligner.MatrixCache());

        //BandedAffineAligner.align0(scoring, a, b, 0, a.size(), 0, b.size(), 151, mutations, new BandedAffineAligner.MatrixCache());

        System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(res.sequence1Stop, a.size()), new Range(res.sequence2Stop, b.size()), 100));
        System.out.println();
        System.out.println(Aligner.alignGlobalAffine(scoring, a, b));
        //System.out.println(new Alignment<>(a, mutations.createAndDestroy(), new Range(0, a.size()), new Range(0, b.size()), 100));
    }

    @Test
    public void testSemiGlobalRightRandom1() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 3, -1, -3, -1);

        int its = its(10000, 100000);
        NucleotideSequence seq1, seq2;
        int offset1, offset2, length1, length2, added1, added2;
        Alignment<NucleotideSequence> la;
        RandomDataGenerator random = new RandomDataGenerator(new Well19937c());
        for (int i = 0; i < its; ++i) {
            seq1 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            seq2 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            offset1 = random.nextInt(0, seq1.size() - 10);
            offset2 = random.nextInt(0, seq2.size() - 10);
            length1 = random.nextInt(1, seq1.size() - offset1);
            length2 = random.nextInt(1, seq2.size() - offset2);
            added1 = random.nextInt(0, length1);
            added2 = random.nextInt(0, length2);
            la = BandedAffineAligner.semiGlobalRight(scoring,
                    seq1, seq2, offset1, length1, added1, offset2, length2, added2, 1);

            assertTrue(la.getSequence1Range().getTo() == offset1 + length1 ||
                    la.getSequence2Range().getTo() == offset2 + length2);

            assertTrue(la.getSequence1Range().getTo() >= offset1 + length1 - added1);
            assertTrue(la.getSequence2Range().getTo() >= offset2 + length2 - added2);

            assertAlignment(la, seq2, scoring);
            //int score = AlignmentUtils.calculateScore(scoring, la.getSequence1Range().length(), la.mutations);
            assertEquals(la.calculateScore(scoring), (int) la.score);
        }
    }

    @Test
    public void testSemiGlobalLeftRandom1() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
                NucleotideSequence.ALPHABET, 3, -1, -3, -1);

        int its = its(1000, 100000);
        NucleotideSequence seq1, seq2;
        int offset1, offset2, length1, length2, added1, added2;
        Alignment<NucleotideSequence> la;
        RandomDataGenerator random = new RandomDataGenerator(new Well19937c());
        for (int i = 0; i < its; ++i) {
            seq1 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            seq2 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            offset1 = random.nextInt(0, seq1.size() - 10);
            offset2 = random.nextInt(0, seq2.size() - 10);
            length1 = random.nextInt(1, seq1.size() - offset1);
            length2 = random.nextInt(1, seq2.size() - offset2);
            added1 = random.nextInt(0, length1);
            added2 = random.nextInt(0, length2);
            la = BandedAffineAligner.semiGlobalLeft(scoring,
                    seq1, seq2, offset1, length1, added1, offset2, length2, added2, 1);

            assertTrue(la.getSequence1Range().getFrom() == offset1 ||
                    la.getSequence2Range().getFrom() == offset2);
            assertTrue(la.getSequence1Range().getFrom() <= offset1 + added1);
            assertTrue(la.getSequence2Range().getFrom() <= offset2 + added2);

            assertAlignment(la, seq2, scoring);

            //int score = AlignmentUtils.calculateScore(scoring, la.getSequence1Range().length(), la.mutations);
            assertEquals(la.calculateScore(scoring), (int) la.score);
        }
    }

    @Test
    public void testScore() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring =
                new AffineGapAlignmentScoring<>(NucleotideSequence.ALPHABET, 10, -5, -11, -7);

        NucleotideSequence s1 = new NucleotideSequence("atcgcgatcgactgcatgca");
        NucleotideSequence s2 = new NucleotideSequence("atcgcgatcgactgactgcatgca");

        Alignment<NucleotideSequence> al = BandedAffineAligner.align(scoring, s1, s2, 0, s1.size(), 0, s2.size(), 0);
        Assert.assertEquals(168, (int) al.score);


        al = BandedAffineAligner.align(scoring, s2, s1, 0, s2.size(), 0, s1.size(), 0);
        Assert.assertEquals(168, (int) al.score);
    }

    @Test
    public void testAffineScoreRandom() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring =
                new AffineGapAlignmentScoring<>(NucleotideSequence.ALPHABET, 10, -5, -11, -7);

        NucleotideSequence s1, s2a, s2b;
        RandomDataGenerator gen = new RandomDataGenerator();
        NucleotideMutationModel nm = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(10);

        int c = its(10000, 200000);
        for (int i = 0; i < c; ++i) {
            s1 = randomSequence(NucleotideSequence.ALPHABET, gen, 50, 100);
            s2a = randomSequence(NucleotideSequence.ALPHABET, gen, 50, 100);
            s2b = MutationsGenerator.generateMutations(s1, nm).mutate(s1);
            for (NucleotideSequence s2 : Arrays.asList(s2a, s2b)) {
                Alignment<NucleotideSequence> la;

                la = BandedAffineAligner.align(scoring, s1, s2, 0, s1.size(), 0, s2.size(), 0);
                int score = AlignmentUtils.calculateScore(s1, la.mutations, scoring);
                if ((int) la.score != score) {
                    System.out.println(la.getAlignmentHelper());
                }
                Assert.assertEquals((int) la.score, score);
                assertAlignment(la, s2);

                la = BandedAffineAligner.semiGlobalLeft(scoring, s1, s2,
                        0, s1.size(), 10,
                        0, s2.size(), 10,
                        10);
                //score = AlignmentUtils.calculateScore(scoring, la.getSequence1Range().length(), la.mutations);
                Assert.assertEquals((int) la.score, la.calculateScore(scoring));
                assertAlignment(la, s2);

                la = semiLocalLeft(scoring, s1, s2,
                        0, s1.size(),
                        0, s2.size(),
                        10);
                //score = AlignmentUtils.calculateScore(scoring, la.getSequence1Range().length(), la.mutations);
                Assert.assertEquals((int) la.score, la.calculateScore(scoring));
                assertAlignment(la, s2);

                la = BandedAffineAligner.semiGlobalRight(scoring, s1, s2,
                        0, s1.size(), 10,
                        0, s2.size(), 10,
                        10);
                //score = AlignmentUtils.calculateScore(scoring, la.getSequence1Range().length(), la.mutations);
                Assert.assertEquals((int) la.score, la.calculateScore(scoring));
                assertAlignment(la, s2);

                la = BandedAffineAligner.semiLocalRight(scoring, s1, s2,
                        0, s1.size(),
                        0, s2.size(),
                        10);
                //score = AlignmentUtils.calculateScore(scoring, la.getSequence1Range().length(), la.mutations);
                Assert.assertEquals((int) la.score, la.calculateScore(scoring));
                assertAlignment(la, s2);
            }
        }
    }

    @Test
    public void test6() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("CTATGCTGGTAATTGCTCGGTCCATCTAAAACGGGTTACCCGATTACAGGACC");
        NucleotideSequence seq2 = new NucleotideSequence("GTATGCTGGTAATTGCTCTGTCCATCTAAAACGGGTTATCCTATTACAGGACC");

        AffineGapAlignmentScoring<NucleotideSequence> IGBLAST_SCORING = new AffineGapAlignmentScoring<>(NucleotideSequence.ALPHABET, 10, -30, -40, -10);
        System.out.println(semiLocalLeft(IGBLAST_SCORING, seq1.getRange(0, 1),
                seq2.getRange(0, 1), 4));
    }

    @Test
    public void test7() {
        Mutations<NucleotideSequence> mutations = Mutations.decode("I2CSA4TSG5TDT6DC7DG8SA11GST15C", NucleotideSequence.ALPHABET);
        NucleotideSequence seq1 = new NucleotideSequence("CAGCAGTCGTAACAGTTA");
        NucleotideSequence seq2 = mutations.mutate(seq1);

        AffineGapAlignmentScoring<NucleotideSequence> scoring = AffineGapAlignmentScoring.getNucleotideBLASTScoring();
        Alignment<NucleotideSequence> expected = new Alignment<>(seq1, mutations, scoring);

        Alignment<NucleotideSequence> actual = BandedAffineAligner.align(scoring, seq2, seq1, seq2.size());

        assertAlignment(actual, seq1);
        assertEquals(actual.calculateScore(scoring), actual.score, 0.001);
        assertTrue(actual.score >= expected.score);
    }

    @Test
    public void testRandomCheckNucleotideScoring() {
        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel()
                .multiplyProbabilities(15);

        Well19937c rand = new Well19937c(System.nanoTime());
        AffineGapAlignmentScoring<NucleotideSequence> sc = AffineGapAlignmentScoring.getNucleotideBLASTScoring();

        int its = TestUtil.its(1000, 5000);
        for (int i = 0; i < its; ++i) {
            NucleotideSequence sequence = randomSequence(NucleotideSequence.ALPHABET, rand, 30, 300);

            model.reseed(rand.nextLong());
            Mutations<NucleotideSequence> mut = MutationsGenerator.generateMutations(sequence, model);
            float mutScore = AlignmentUtils.calculateScore(sequence, mut, sc);
            NucleotideSequence mutated = mut.mutate(sequence);

            for (Alignment<NucleotideSequence> r : Arrays.asList(
                    BandedAffineAligner.semiLocalLeft(sc, sequence, mutated, 0, sequence.size(), 0, mutated.size(), sequence.size()),
                    BandedAffineAligner.semiGlobalLeft(sc, sequence, mutated, 0, sequence.size(), 0, 0, mutated.size(), 0, sequence.size()),
                    BandedAffineAligner.semiLocalRight(sc, sequence, mutated, 0, sequence.size(), 0, mutated.size(), sequence.size()),
                    BandedAffineAligner.semiGlobalRight(sc, sequence, mutated, 0, sequence.size(), 0, 0, mutated.size(), 0, sequence.size()))) {

                Assert.assertEquals(r.getRelativeMutations().mutate(sequence.getRange(r.getSequence1Range())),
                        mutated.getRange(r.getSequence2Range()));

                AlignerTest.assertAlignment(r, mutated, sc);
                Assert.assertTrue(mutScore <= r.calculateScore(sc));

                r = Aligner.alignGlobal(sc, mutated, sequence);
                AlignerTest.assertAlignment(r, sequence, sc);

                Assert.assertEquals(r.getRelativeMutations().mutate(mutated.getRange(r.getSequence1Range())),
                        sequence.getRange(r.getSequence2Range()));

                Assert.assertTrue("Scoring type = " + sc.getClass().getName(),
                        mutScore <= r.calculateScore(sc));
            }
        }
    }
}