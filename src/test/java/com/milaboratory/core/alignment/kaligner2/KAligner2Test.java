package com.milaboratory.core.alignment.kaligner2;

import com.milaboratory.core.alignment.AffineGapAlignmentScoring;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.alignment.AlignmentUtils;
import com.milaboratory.core.alignment.benchmark.*;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.test.TestUtil;
import com.milaboratory.util.GlobalObjectMappers;
import com.milaboratory.util.RandomUtil;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static com.milaboratory.core.alignment.AffineGapAlignmentScoring.IGBLAST_NUCLEOTIDE_SCORING;
import static com.milaboratory.core.alignment.AffineGapAlignmentScoring.IGBLAST_NUCLEOTIDE_SCORING_THRESHOLD;
import static com.milaboratory.core.alignment.benchmark.ChallengeProvider.getParamsOneCluster;
import static com.milaboratory.test.TestUtil.its;

/**
 * Created by poslavsky on 26/10/15.
 */
public class KAligner2Test {
    public static final AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
            NucleotideSequence.ALPHABET, 10, -7, -11, -2);
    public static final KAlignerParameters2 gParams = new KAlignerParameters2(
            9, 3, true, true,
            15, -10, 15, 0f, 13, -7, -3,
            3, 6, 4, 3, 3, 3,
            0, 70, 0.8f, 5, scoring);

    @Test
    public void test1() throws Exception {
        KAligner2Statistics stat = new KAligner2Statistics();
        KAligner2<Object> aligner = new KAligner2<>(gParams, stat);
        aligner.addReference(new NucleotideSequence("atgcgtcgatcgtagctagctgatcgatcgactgactagcataggatgtagagctagctagctac"));
        aligner.addReference(new NucleotideSequence("atgcgtcgatcgtagctagctgatcgatcgactgactagcatcagcatcaggatgtagagctagctagctac"));
        aligner.addReference(new NucleotideSequence("atgcgtcgatcgtagctagctgtagtagatgatgatagtagatagtagtagtgatgacgatcgactgaatgtagagctagctagctac"));

        NucleotideSequence query = new NucleotideSequence("atgcgtcgatcgtagctagctgtcgatcgactgaatgtagagctagctagctac");
        KAlignmentResult2<Object> al = aligner.align(query);
        // System.out.println(GlobalObjectMappers.PRETTY.writeValueAsString(stat));
        // System.out.println(al.hasHits());

        Alignment<NucleotideSequence> val = al.getHits().get(0).getAlignment();
        Assert.assertEquals(query.getRange(val.getSequence2Range()), AlignmentUtils.getAlignedSequence2Part(val));
        // System.out.println(val.getScore());
        // System.out.println(val);

        val = al.getHits().get(1).getAlignment();
        Assert.assertEquals(query.getRange(val.getSequence2Range()), AlignmentUtils.getAlignedSequence2Part(val));
        // System.out.println(val.getScore());
        // System.out.println(val);


        //val = al.getHits().get(2).getAlignment();
        //Assert.assertEquals(query.getRange(val.getSequence2Range()), AlignmentUtils.getAlignedSequence2Part(val));
        //System.out.println(val.getScore());
        //System.out.println(val);
    }

    @Test
    public void testSimpleRandomTest() throws Exception {
        RandomUtil.reseedThreadLocal(12342345L);
        AffineGapAlignmentScoring<NucleotideSequence> scoring = IGBLAST_NUCLEOTIDE_SCORING;
        int absoluteMinScore = IGBLAST_NUCLEOTIDE_SCORING_THRESHOLD;
        Challenge challenge = new ChallengeProvider(getParamsOneCluster(scoring, absoluteMinScore, Integer.MAX_VALUE, 20.0).setQueryCount(its(5000, 100000)), 123).take();
        Benchmark bm = new Benchmark(its(10_000_000_000L, 150_000_000_000L));
        KAlignerParameters2 alParams = new KAlignerParameters2(9, 3,
                true, true,
                75, -50, 115, 0.87f, 45, -10, -15,
                2, 5, 5, 3, 3, 3,
                0, absoluteMinScore, 0.87f, 5,
                scoring);
        alParams.setMapperNValue(9);
        alParams.setMapperKValue(1);
        alParams.setMapperKMersPerPosition(9);
        alParams.setMapperOffsetShiftScore(-22);
        alParams.setMapperMaxSeedsDistance(4);
        alParams.setMapperAbsoluteMinScore(100);
        alParams.setFloatingLeftBound(true);
        alParams.setAbsoluteMinScore(150);
        alParams.setMaxHits(3);
        alParams.setMapperMismatchScore(-36);
        alParams.setMapperAbsoluteMinClusterScore(128);
        alParams.setFloatingRightBound(true);
        alParams.setMapperSlotCount(1);
        alParams.setMapperMaxClusters(1);
        alParams.setAlignmentStopPenalty(0);
        alParams.setRelativeMinScore(0.8f);
        alParams.setMapperExtraClusterScore(-78);
        alParams.setMapperMatchScore(90);
        alParams.setMapperRelativeMinScore(0.8f);
        alParams.setMapperMaxClusterIndels(3);
        alParams.setMapperMinSeedsDistance(4);
        BenchmarkInput bi = new BenchmarkInput(alParams, challenge);
        BenchmarkResults result = bm.process(bi);
        System.out.println("Time per query: " + TestUtil.time(result.getAverageTiming()));
        System.out.println("Processed queries: " + result.getProcessedGoodQueries());
        System.out.println("Bad percent: " + result.getBadFraction() * 100);
        System.out.println("False positive percent: " + result.getFalsePositiveFraction() * 100);
        System.out.println("Scoring error percent: " + result.getScoreErrorFraction() * 100);
        Assert.assertTrue("Bad fraction = " + result.getBadFraction(),
                result.getBadFraction() < 0.005);
        Assert.assertTrue("False positive fraction = " + result.getFalsePositiveFraction(),
                result.getFalsePositiveFraction() < 0.01);
        Assert.assertTrue("Score error fraction = " + result.getScoreErrorFraction(),
                result.getScoreErrorFraction() < 0.015);
    }

    @Test
    public void testBoundaries() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring = IGBLAST_NUCLEOTIDE_SCORING;
        int absoluteMinScore = IGBLAST_NUCLEOTIDE_SCORING_THRESHOLD;
        KAlignerParameters2 alParams = new KAlignerParameters2(9, 3,
                true, true,
                75, -50, 115, 0.87f, 45, -10, -15,
                2, 5, 5, 3, 3, 3,
                0, absoluteMinScore, 0.87f, 5,
                scoring);
        alParams.setMapperNValue(9);
        alParams.setMapperKValue(1);
        alParams.setMapperKMersPerPosition(9);
        alParams.setMapperOffsetShiftScore(-22);
        alParams.setMapperMaxSeedsDistance(4);
        alParams.setMapperAbsoluteMinScore(100);
        alParams.setFloatingLeftBound(true);
        alParams.setAbsoluteMinScore(150);
        alParams.setMaxHits(3);
        alParams.setMapperMismatchScore(-36);
        alParams.setMapperAbsoluteMinClusterScore(128);
        alParams.setFloatingRightBound(true);
        alParams.setMapperSlotCount(1);
        alParams.setMapperMaxClusters(1);
        alParams.setAlignmentStopPenalty(0);
        alParams.setRelativeMinScore(0.8f);
        alParams.setMapperExtraClusterScore(-78);
        alParams.setMapperMatchScore(90);
        alParams.setMapperRelativeMinScore(0.8f);
        alParams.setMapperMaxClusterIndels(3);
        alParams.setMapperMinSeedsDistance(4);

        KAligner2<Integer> aligner = new KAligner2<Integer>(alParams);
        aligner.addReference(new NucleotideSequence("TAGATCGATATCGTGCTCAGTAGTCGCTATATAGCTGTCGGTAGTATAGATGATGTGAAATCGTGCTGTCGTGCAT"));
        aligner.addReference(new NucleotideSequence("TATGGGCGTAAGTGATGCTGCTGATGTAATCGCGCGCTAGAACCCCGATGTGTAGCTGCCACCGTACCCGATCCGTGACCCC"));
        aligner.addReference(new NucleotideSequence("ATAGTTCGTCGATGGGTCGCTGCGGCGCAGCAGCGCATATACACAGCTAGCGA"));

        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel();

        Well19937c r = RandomUtil.getThreadLocalRandom();

        int checked = 0;
        for (int i = 0; i < its(5000, 50000); i++) {
            int n = r.nextInt(3);
            NucleotideSequence ref = aligner.getReference(n);
            int b = ref.size() / 3;
            int from = r.nextInt(b);
            int to = from + b + r.nextInt(b);
            Mutations<NucleotideSequence> muts = MutationsGenerator.generateMutations(ref, model);
            NucleotideSequence q = muts.mutate(ref);
            from = muts.convertToSeq2Position(from);
            to = muts.convertToSeq2Position(to);
            from = from < 0 ? ~from : from;
            to = to < 0 ? ~to + 1 : to;

            KAlignmentResult2<Integer> res = aligner.align(q, from, to);
            for (KAlignmentHit2<Integer> hit2 : res.getHits()) {
                Assert.assertTrue(hit2.getAlignment().getSequence2Range().getFrom() >= from);
                Assert.assertTrue(hit2.getAlignment().getSequence2Range().getTo() <= to);
                ++checked;
            }
        }

        System.out.println(checked);
    }

    static NucleotideSequence nt(String str) {
        return new NucleotideSequence(str.replace(" ", ""));
    }

    @Test
    @Ignore
    public void testCase0() throws Exception {
        // Tests no Assertion errors inside KAligner2/KMapper2 code

        KAlignerParameters2 parameters = GlobalObjectMappers.ONE_LINE.readValue("{\n" +
                " \"type\" : \"kaligner2\",\n" +
                " \"mapperNValue\" : 9,\n" +
                " \"mapperKValue\" : 3,\n" +
                " \"floatingLeftBound\" : true,\n" +
                " \"floatingRightBound\" : true,\n" +
                " \"mapperAbsoluteMinClusterScore\" : 101,\n" +
                " \"mapperExtraClusterScore\" : -100,\n" +
                " \"mapperMatchScore\" : 100,\n" +
                " \"mapperMismatchScore\" : -1,\n" +
                " \"mapperOffsetShiftScore\" : -1,\n" +
                " \"mapperSlotCount\" : 1,\n" +
                " \"mapperMaxClusters\" : 2,\n" +
                " \"mapperMaxClusterIndels\" : 4,\n" +
                " \"mapperKMersPerPosition\" : 3,\n" +
                " \"mapperAbsoluteMinScore\" : 100,\n" +
                " \"mapperRelativeMinScore\" : 0.8,\n" +
                " \"mapperMinSeedsDistance\" : 1,\n" +
                " \"mapperMaxSeedsDistance\" : 1,\n" +
                " \"alignmentStopPenalty\" : 0,\n" +
                " \"absoluteMinScore\" : 150,\n" +
                " \"relativeMinScore\" : 0.8,\n" +
                " \"maxHits\" : 3,\n" +
                " \"scoring\" : {\n" +
                " \"type\" : \"affine\",\n" +
                " \"subsMatrix\" : \"raw(10, -30, -30, -30, -20, -10, -30, -30, -10, -30, -10, -30, -16, -16, -16, -30, 10, -30, -30, -20, -10, -30, -10, -30, -10, -30, -16, -16, -30, -16, -30, -30, 10, -30, -20, -30, -10, -10, -30, -30, -10, -16, -30, -16, -16, -30, -30, -30, 10, -20, -30, -10, -30, -10, -10, -30, -16, -16, -16, -30, -20, -20, -20, -20, -20, -20, -20, -20, -20, -20, -20, -20, -20, -20, -20, -10, -10, -30, -30, -20, -10, -30, -20, -20, -20, -20, -23, -16, -23, -16, -30, -30, -10, -10, -20, -30, -10, -20, -20, -20, -20, -16, -23, -16, -23, -30, -10, -10, -30, -20, -20, -20, -10, -30, -20, -20, -16, -23, -23, -16, -10, -30, -30, -10, -20, -20, -20, -30, -10, -20, -20, -23, -16, -16, -23, -30, -10, -30, -10, -20, -20, -20, -20, -20, -10, -30, -16, -16, -23, -23, -10, -30, -10, -30, -20, -20, -20, -20, -20, -30, -10, -23, -23, -16, -16, -30, -16, -16, -16, -20, -23, -16, -16, -23, -16, -23, -16, -21, -21, -21, -16, -16, -30, -16, -20, -16, -23, -23, -16, -16, -23, -21, -16, -21, -21, -16, -30, -16, -16, -20, -23, -16, -23, -16, -23, -16, -21, -21, -16, -21, -16, -16, -16, -30, -20, -16, -23, -16, -23, -23, -16, -21, -21, -21, -16)\",\n" +
                " \"gapOpenPenalty\" : -40,\n" +
                " \"gapExtensionPenalty\" : -10,\n" +
                " \"uniformBasicMatch\" : true\n" +
                " }\n" +
                "}", KAlignerParameters2.class);
        Challenge ch = new ChallengeProvider(getParamsOneCluster(IGBLAST_NUCLEOTIDE_SCORING, IGBLAST_NUCLEOTIDE_SCORING_THRESHOLD, Integer.MAX_VALUE, 30), 11).take();

        Benchmark bm = new Benchmark(100_000_000_000L, 1000);
        BenchmarkInput bi = new BenchmarkInput(parameters, ch);
        BenchmarkResults result = bm.process(bi);
        System.out.println("Time per query: " + TestUtil.time(result.getAverageTiming()));
        System.out.println("Processed queries: " + result.getProcessedGoodQueries());
        System.out.println("Bad percent: " + result.getBadFraction() * 100);
        System.out.println("False positive percent: " + result.getFalsePositiveFraction() * 100);
        System.out.println("Scoring error percent: " + result.getScoreErrorFraction() * 100);
    }

    @Test
    public void testFloatingBounds() throws Exception {
        NucleotideSequence query = nt("TCCCTGAGACTCCCTGAGACTCTCCTGTGCAGCCTCTGGATTCACCTTCAGTAGCTATAGCATGAACTGGGTCCGCCAG" +
                "GCTCCAGGGAAGGGGCTGGAGTGGGTCTCATCCATTAGTAGTAATAATAATTACATATACTACGCAGACTCAGTGAAGGGCCGATTCACCATCTCCAGAGA" +
                "CAACGCCAAGAACTCACTGTATCTGCAAGACTCACTGTATCTGCAAATGAACAGCCTGAGAGCCGAGGACACGGCTGTGTATTATTGTGCGAGAGATACAG" +
                "ATGGTATGGACGTCTGGGGCCAAGGGACCACGGTCACCGTCTCCTCAGGGAGTGCATCCGCCCCAACCCTTTTCCCCCTCTCTGCGTTGATACCACTGTGA" +
                "TACCACT");
        AffineGapAlignmentScoring<NucleotideSequence> scoring = IGBLAST_NUCLEOTIDE_SCORING;
        int absoluteMinScore = IGBLAST_NUCLEOTIDE_SCORING_THRESHOLD;
        KAlignerParameters2 params = new KAlignerParameters2(9, 3,
                true, true,
                75, -50, 115, 0.87f, 45, -10, -15,
                2, 5, 5, 3, 3, 3,
                0, absoluteMinScore, 0.87f, 5,
                scoring);
        params.setMapperOffsetShiftScore(-78);
        params.setMapperMaxSeedsDistance(3);
        params.setMapperKValue(3);
        params.setMapperAbsoluteMinScore(100);
        params.setFloatingLeftBound(true);
        params.setAbsoluteMinScore(150);
        params.setMaxHits(3);
        params.setMapperMismatchScore(-38);
        params.setMapperAbsoluteMinClusterScore(41);
        params.setFloatingRightBound(true);
        params.setMapperSlotCount(4);
        params.setMapperMaxClusters(6);
        params.setMapperMatchScore(28);
        params.setAlignmentStopPenalty(0);
        params.setRelativeMinScore(0.8f);
        params.setMapperExtraClusterScore(-3);
        params.setMapperKMersPerPosition(2);
        params.setMapperNValue(11);
        params.setMapperRelativeMinScore(0.8f);
        params.setMapperMaxClusterIndels(4);
        params.setMapperMinSeedsDistance(2);

        params.setFloatingLeftBound(false);
        params.setFloatingRightBound(false);

        KAligner2<Integer> aligner = new KAligner2<>(params);
        aligner.addReference(nt("GAGGTGCAGCTGGTGGAGTCTGGGGGAGGCCTGGTCAAGCCTGGGGGGTCCCTGAGACTCTCCTGTGCAGCCTCTGGATTCACCT" +
                "TCAGTAGCTATAGCATGAACTGGGTCCGCCAGGCTCCAGGGAAGGGGCTGGAGTGGGTCTCATCCATTAGTAGTAGTAGTAGTTACATATACTACGCAGAC" +
                "TCAGTGAAGGGCCGATTCACCATCTCCAGAGACAACGCCAAGAACTCACTGTATCTGCAAATGAACAGCCTGAGAGCCGAGGACACGGCTGTGTATTACTG" +
                "TGCGAGAGATTATATGCGTCGTAGCTCGACGATC"));

        KAlignmentResult2<Integer> res = aligner.align(query);
        Assert.assertEquals(0, res.getBestHit().getAlignment().getSequence2Range().getFrom());
        Assert.assertEquals(aligner.getReference(0).size(), res.getBestHit().getAlignment().getSequence1Range().getTo());
    }

    @Test
    public void testCase1() throws Exception {
        NucleotideSequence query = nt("TCCCTGAGACTCCCTGAGACTCTCCTGTGCAGCCTCTGGATTCACCTTCAGTAGCTATAGCATGAACTGGGTCCGCCAG" +
                "GCTCCAGGGAAGGGGCTGGAGTGGGTCTCATCCATTAGTAGTAATAATAATTACATATACTACGCAGACTCAGTGAAGGGCCGATTCACCATCTCCAGAGA" +
                "CAACGCCAAGAACTCACTGTATCTGCAAGACTCACTGTATCTGCAAATGAACAGCCTGAGAGCCGAGGACACGGCTGTGTATTATTGTGCGAGAGATACAG" +
                "ATGGTATGGACGTCTGGGGCCAAGGGACCACGGTCACCGTCTCCTCAGGGAGTGCATCCGCCCCAACCCTTTTCCCCCTCTCTGCGTTGATACCACTGTGA" +
                "TACCACT");
        AffineGapAlignmentScoring<NucleotideSequence> scoring = IGBLAST_NUCLEOTIDE_SCORING;
        int absoluteMinScore = IGBLAST_NUCLEOTIDE_SCORING_THRESHOLD;
        KAlignerParameters2 params = new KAlignerParameters2(9, 3,
                true, true,
                75, -50, 115, 0.87f, 45, -10, -15,
                2, 5, 5, 3, 3, 3,
                0, absoluteMinScore, 0.87f, 5,
                scoring);
        params.setMapperOffsetShiftScore(-78);
        params.setMapperMaxSeedsDistance(3);
        params.setMapperKValue(3);
        params.setMapperAbsoluteMinScore(100);
        params.setFloatingLeftBound(true);
        params.setAbsoluteMinScore(150);
        params.setMaxHits(3);
        params.setMapperMismatchScore(-38);
        params.setMapperAbsoluteMinClusterScore(41);
        params.setFloatingRightBound(true);
        params.setMapperSlotCount(4);
        params.setMapperMaxClusters(6);
        params.setMapperMatchScore(28);
        params.setAlignmentStopPenalty(0);
        params.setRelativeMinScore(0.8f);
        params.setMapperExtraClusterScore(-3);
        params.setMapperKMersPerPosition(2);
        params.setMapperNValue(11);
        params.setMapperRelativeMinScore(0.8f);
        params.setMapperMaxClusterIndels(4);
        params.setMapperMinSeedsDistance(2);

        KAligner2<Integer> aligner = new KAligner2<>(params);
        aligner.addReference(nt("GAGGTGCAGCTGGTGGAGTCTGGGGGAGGCCTGGTCAAGCCTGGGGGGTCCCTGAGACTCTCCTGTGCAGCCTCTGGATTCACCTTCAGTAGCTATAGCATGAACTGGGTCCGCCAGGCTCCAGGGAAGGGGCTGGAGTGGGTCTCATCCATTAGTAGTAGTAGTAGTTACATATACTACGCAGACTCAGTGAAGGGCCGATTCACCATCTCCAGAGACAACGCCAAGAACTCACTGTATCTGCAAATGAACAGCCTGAGAGCCGAGGACACGGCTGTGTATTACTGTGCGAGAGA"));

        int correct = 0;
        for (int i = 0; i < 100; i++) {
            KAlignmentResult2<Integer> res = aligner.align(query);
            if (res.getBestHit().alignment.getSequence2Range().getTo() == 276)
                ++correct;
        }
        Assert.assertEquals(correct, 100);
    }

    @Test
    public void testCase2() throws Exception {
        NucleotideSequence query = nt("TCTGAGAGAGGAGCCTTTCTGAGAGAGGAGCCTTAGCCCTGGATTCCAAGGCCTATCCACTTGGTGATCAGCACTGAGCACCGAGGATTCACCATGGAACTGGGGCTCCGCTGGGTTTTCCTTGTTGCTATTTTAGAAGGTGTCCAGTGTGAGGTGCAGCTGGTGGAGTCTGGGGGAGGCCTGGTCAAGCCTGGGGGGTCCCTGAGACTCTCCTGTGTAGCCTCTGGATTCACCTTCAGTAGCTATAGCATGAACTGGGTCGGCCAGGCTGCGGGGAAGGGGGTGGATTGGGTCTCATCCATTAGTAGTAAAAATAATTACATATACTACACAGACTCAGTGAAGGGCCGATTCACCAACTCCAGAGACAACGCCAAGAACTCACTGTATCTGGAAGACAAACTGTATCTACAAATGAACAGACTTAGAGCTGAGGACACGGCTGTGTATTATTGTGCGAGAGATATAGATGGTATGGACGACTGGGGCCAAGGGACCATGGTCACAGTGTCCTCAGGGAGTGCATCCGTCCCAACCCTTTTCCCCCTCACTGCGTTGATACCACTGTGAGTTGATACCACTG");
        AffineGapAlignmentScoring<NucleotideSequence> scoring = IGBLAST_NUCLEOTIDE_SCORING;
        int absoluteMinScore = IGBLAST_NUCLEOTIDE_SCORING_THRESHOLD;
        KAlignerParameters2 params = new KAlignerParameters2(9, 3,
                true, true,
                75, -50, 115, 0.87f, 45, -10, -15,
                2, 5, 5, 3, 3, 3,
                0, absoluteMinScore, 0.87f, 5,
                scoring);
        params.setMapperOffsetShiftScore(-91);
        params.setMapperMaxSeedsDistance(5);
        params.setMapperKValue(0);
        params.setMapperAbsoluteMinScore(100);
        params.setFloatingLeftBound(true);
        params.setAbsoluteMinScore(150);
        params.setMaxHits(3);
        params.setMapperMismatchScore(-8);
        params.setMapperAbsoluteMinClusterScore(118);
        params.setFloatingRightBound(true);
        params.setMapperSlotCount(6);
        params.setMapperMaxClusters(3);
        params.setMapperMatchScore(71);
        params.setAlignmentStopPenalty(0);
        params.setRelativeMinScore(0.8f);
        params.setMapperExtraClusterScore(-38);
        params.setMapperKMersPerPosition(1);
        params.setMapperNValue(8);
        params.setMapperRelativeMinScore(0.8f);
        params.setMapperMaxClusterIndels(4);
        params.setMapperMinSeedsDistance(3);

        // System.out.println(GlobalObjectMappers.PRETTY.writeValueAsString(params));

        KAligner2<Integer> aligner = new KAligner2<>(params);
        aligner.addReference(nt("GAGGTGCAGCTGGTGGAGTCTGGGGGAGGCCTGGTCAAGCCTGGGGGGTCCCTGAGACTCTCCTGTGCAGCCTCTGGATTCACCTTCAGTAGCTATAGCATGAACTGGGTCCGCCAGGCTCCAGGGAAGGGGCTGGAGTGGGTCTCATCCATTAGTAGTAGTAGTAGTTACATATACTACGCAGACTCAGTGAAGGGCCGATTCACCATCTCCAGAGACAACGCCAAGAACTCACTGTATCTGCAAATGAACAGCCTGAGAGCCGAGGACACGGCTGTGTATTACTGTGCGAGAGA"));

        int correct = 0;
        for (int i = 0; i < 100; i++) {
            //System.out.println(RandomUtil.reseedThreadLocal(-983489203720096202L));
            KAlignmentResult2<Integer> res = aligner.align(query);
            if (res.getBestHit().alignment.getSequence2Range().getTo() == 464)
                ++correct;
            //System.out.println(res.getBestHit().alignment.getAlignmentHelper());
        }
        Assert.assertEquals(correct, 100);
    }

    @Test
    public void testCase3() throws Exception {
        NucleotideSequence query = nt("TCTGAGAGAGGAGCCTTTCTGAGAGAGGAGCCTTAGCCCTGGATTCCAAGGCCTATCCACTTGGTGATCAGCACTGAGCACCGAGGATTCACCATGGAACTGGGGCTCCGCTGGGTTTTCCTTGTTGCTATTTTAGAAGGTGTCCAGTGTGAGGTGCAGCTGGTGGAGTCTGGGGGAGGCCTGGTCAAGCCTGGGGGGTCCCTGAGACTCTCCTGTGTAGCCTCTGGATTCACCTTCAGTAGCTATAGCATGAACTGGGTCGGCCAGGCTGCGGGGAAGGGGGTGGATTGGGTCTCATCCATTAGTAGTAAAAATAATTACATATACTACACAGACTCAGTGAAGGGCCGATTCACCAACTCCAGAGACAACGCCAAGAACTCACTGTATCTGGAAGACAAACTGTATCTACAAATGAACAGACTTAGAGCTGAGGACACGGCTGTGTATTATTGTGCGAGAGATATAGATGGTATGGACGACTGGGGCCAAGGGACCATGGTCACAGTGTCCTCAGGGAGTGCATCCGTCCCAACCCTTTTCCCCCTCACTGCGTTGATACCACTGTGAGTTGATACCACTG");
        AffineGapAlignmentScoring<NucleotideSequence> scoring = IGBLAST_NUCLEOTIDE_SCORING;
        int absoluteMinScore = IGBLAST_NUCLEOTIDE_SCORING_THRESHOLD;
        KAlignerParameters2 params = new KAlignerParameters2(9, 3,
                true, true,
                75, -50, 115, 0.87f, 45, -10, -15,
                2, 5, 5, 3, 3, 3,
                0, absoluteMinScore, 0.87f, 5,
                scoring);
        params.setMapperOffsetShiftScore(-82);
        params.setMapperMaxSeedsDistance(5);
        params.setMapperKValue(0);
        params.setMapperAbsoluteMinScore(100);
        params.setFloatingLeftBound(true);
        params.setAbsoluteMinScore(150);
        params.setMaxHits(3);
        params.setMapperMismatchScore(-14);
        params.setMapperAbsoluteMinClusterScore(102);
        params.setFloatingRightBound(true);
        params.setMapperSlotCount(6);
        params.setMapperMaxClusters(4);
        params.setMapperMatchScore(95);
        params.setAlignmentStopPenalty(0);
        params.setRelativeMinScore(0.8f);
        params.setMapperExtraClusterScore(-38);
        params.setMapperKMersPerPosition(1);
        params.setMapperNValue(8);
        params.setMapperRelativeMinScore(0.8f);
        params.setMapperMaxClusterIndels(4);
        params.setMapperMinSeedsDistance(5);

        //System.out.println(GlobalObjectMappers.PRETTY.writeValueAsString(params));

        KAligner2<Integer> aligner = new KAligner2<>(params);
        aligner.addReference(nt("GAGGTGCAGCTGGTGGAGTCTGGGGGAGGCCTGGTCAAGCCTGGGGGGTCCCTGAGACTCTCCTGTGCAGCCTCTGGATTCACCTTCAGTAGCTATAGCATGAACTGGGTCCGCCAGGCTCCAGGGAAGGGGCTGGAGTGGGTCTCATCCATTAGTAGTAGTAGTAGTTACATATACTACGCAGACTCAGTGAAGGGCCGATTCACCATCTCCAGAGACAACGCCAAGAACTCACTGTATCTGCAAATGAACAGCCTGAGAGCCGAGGACACGGCTGTGTATTACTGTGCGAGAGA"));

        //Alignment<NucleotideSequence> al = Aligner.alignLocalAffine(scoring, aligner.getReference(0), query);
        //System.out.println(al.getAlignmentHelper());

        int correct = 0;
        for (int i = 0; i < 100; i++) {
            //System.out.println(RandomUtil.reseedThreadLocal(-983489203720096202L));
            KAlignmentResult2<Integer> res = aligner.align(query);
            if (res.getBestHit().alignment.getSequence2Range().getTo() == 464)
                ++correct;
            //System.out.println(res.getBestHit().alignment.getAlignmentHelper());
        }
        Assert.assertEquals(correct, 100);
    }

    @Test
    public void caseJ1() throws Exception {
        String paramsS = "{\"type\":\"kaligner2\",\"mapperNValue\":8," +
                "\"mapperKValue\":1,\"floatingLeftBound\":true," +
                "\"floatingRightBound\":false,\"mapperAbsoluteMinClusterScore\":102," +
                "\"mapperExtraClusterScore\":-38,\"mapperMatchScore\":95," +
                "\"mapperMismatchScore\":-14,\"mapperOffsetShiftScore\":-82," +
                "\"mapperSlotCount\":6,\"mapperMaxClusters\":4,\"mapperMaxClusterIndels\":4," +
                "\"mapperKMersPerPosition\":4,\"mapperAbsoluteMinScore\":100," +
                "\"mapperRelativeMinScore\":0.8,\"mapperMinSeedsDistance\":5," +
                "\"mapperMaxSeedsDistance\":5,\"alignmentStopPenalty\":0," +
                "\"absoluteMinScore\":150,\"relativeMinScore\":0.8," +
                "\"maxHits\":3,\"scoring\":{\"type\":\"affine\"," +
                "\"subsMatrix\":\"simple(match = 10, mismatch = -19)\"," +
                "\"gapOpenPenalty\":-40,\"gapExtensionPenalty\":-11}}";

        KAlignerParameters2 params = GlobalObjectMappers.ONE_LINE.readValue(paramsS, KAlignerParameters2.class);

        KAligner2<Integer> aligner = new KAligner2<>(params);
        NucleotideSequence j = nt("tgatgcttttgatatctggggccaagggacaatggtcaccgtctcttcagga");
        System.out.println(j.size());
        aligner.addReference(j);
        NucleotideSequence target = nt("CGAAAGATCGGGGTTCTCCTCGGATCCCCCTGCTGTGGTTCGGGGAGTTGGGGGATGATGCTTTTGATATCTG" +
                "GGGCCAAGGGACAATGGTCACCGTCTCTTCAGGGAGTGCATCCGCCCCAACCCTTTTCCCCCT");
        KAlignmentResult2<Integer> align = aligner.align(target);
        Alignment<NucleotideSequence> alignment = align.getBestHit().getAlignment();
        System.out.println(alignment.getAlignmentHelper());
        System.out.println(alignment.getAbsoluteMutations());
        System.out.println(alignment.getSequence1Range());
        System.out.println(alignment.getSequence2Range());
    }
    //@Test
    //public void testSpeed1() throws Exception {
    //    new BufferedReader(new InputStreamReader(System.in)).readLine();
    //    KAlignerParameters2 params = GlobalObjectMappers.ONE_LINE.readValue("{\"mapperAbsoluteMinScore\": 65, \"scoring\": {\"subsMatrixActual\": \"raw(6, -2, -2, -2, 0, 2, -2, -2, 2, -2, 2, -2, 0, 0, 0, -2, 6, -2, -2, 0, 2, -2, 2, -2, 2, -2, 0, 0, -2, 0, -2, -2, 6, -2, 0, -2, 2, 2, -2, -2, 2, 0, -2, 0, 0, -2, -2, -2, 6, 0, -2, 2, -2, 2, 2, -2, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, -2, -2, 0, 2, -2, 0, 0, 0, 0, 0, 0, 0, 0, -2, -2, 2, 2, 0, -2, 2, 0, 0, 0, 0, 0, 0, 0, 0, -2, 2, 2, -2, 0, 0, 0, 2, -2, 0, 0, 0, 0, 0, 0, 2, -2, -2, 2, 0, 0, 0, -2, 2, 0, 0, 0, 0, 0, 0, -2, 2, -2, 2, 0, 0, 0, 0, 0, 2, -2, 0, 0, 0, 0, 2, -2, 2, -2, 0, 0, 0, 0, 0, -2, 2, 0, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)\", \"uniformBasicMatch\": true, \"type\": \"affine\", \"gapOpenPenalty\": -23, \"gapExtensionPenalty\": -1}, \"floatingLeftBound\": true, \"mapperMaxClusterIndels\": 2, \"mapperNValue\": 9, \"mapperRelativeMinScore\": 0.8, \"mapperMatchScore\": 25, \"mapperMaxSeedsDistance\": 2, \"maxHits\": 5, \"mapperOffsetShiftScore\": -30, \"mapperMismatchScore\": -20, \"relativeMinScore\": 0.8, \"alignmentStopPenalty\": 0, \"mapperAbsoluteMinClusterScore\": 45, \"floatingRightBound\": true, \"mapperSlotCount\": 3, \"absoluteMinScore\": 20, \"mapperMinSeedsDistance\": 2, \"mapperExtraClusterScore\": -20, \"mapperKValue\": 3, \"mapperKMersPerPosition\": 3}", KAlignerParameters2.class);
    //    //params.setMapperKMersPerPosition(1);
    //    //Challenge challenge = new ChallengeProvider(ChallengeProvider.getParamsOneCluster(30), 10).take();
    //    new Benchmark(1_000_000_000L).process(new BenchmarkInput(params, new ChallengeProvider(ChallengeProvider.getParams1(30), 10).take()));
    //
    //    BenchmarkResults result = new Benchmark(50_000_000_000L).process(
    //            new BenchmarkInput(params, new ChallengeProvider(
    //                    ChallengeProvider.getParams1(30), 10).take()));
    //    printResult("Multicluster30:", result);
    //
    //    result = new Benchmark(50_000_000_000L).process(
    //            new BenchmarkInput(params, new ChallengeProvider(
    //                    ChallengeProvider.getParamsOneCluster(30), 10).take()));
    //    printResult("Onecluster30:", result);
    //
    //    /*
    //        Step 1
    //        ======
    //
    //        Multicluster30:
    //        Avr. total timing 1: 252.50us
    //        Avr. total timing 2: 251.2907758155163us
    //        Avr. seed extraction: 112.81350313503135us
    //        Avr. hit calculation: 123.52711027110271us
    //        Avr. total mapper: 236.3587271745435us
    //        Avr. aligner: 15.4655us
    //        Bad fraction: 0.481%
    //
    //        Onecluster30:
    //        Avr. total timing 1: 217.16us
    //        Avr. total timing 2: 216.21628716287162us
    //        Avr. seed extraction: 99.01561515615157us
    //        Avr. hit calculation: 105.91975us
    //        Avr. total mapper: 204.90042400424005us
    //        Avr. aligner: 14.15875us
    //        Bad fraction: 0.07100000000000001%
    //
    //        Step 2 (ThreadLocal cache)
    //        ======
    //
    //        Multicluster30:
    //        Avr. total timing 1: 246.77us
    //        Avr. total timing 2: 245.61991239824798us
    //        Avr. seed extraction: 105.60968109681097us
    //        Avr. hit calculation: 124.84087340873408us
    //        Avr. total mapper: 230.41685833716673us
    //        Avr. aligner: 15.64975us
    //        Bad fraction: 0.481%
    //
    //        Onecluster30:
    //        Avr. total timing 1: 213.75us
    //        Avr. total timing 2: 212.84250342503424us
    //        Avr. seed extraction: 93.51256012560125us
    //        Avr. hit calculation: 107.6565us
    //        Avr. total mapper: 201.19363693636936us
    //        Avr. aligner: 14.4725us
    //        Bad fraction: 0.07100000000000001%
    //     */
    //}

    public String recordsToString(int[] records) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < records.length; i++) {
            int record = records[i];
            builder.append(i).append(": i")
                    .append(KMapper2.index(record))
                    .append(" o")
                    .append(KMapper2.offset(record))
                    .append("\n");
        }
        return builder.toString();
    }

    public static void printResult(String title, BenchmarkResults result) {
        System.out.println(title);
        System.out.println("Avr. total timing 1: " + TestUtil.time(result.getAverageTiming()));
        System.out.println("Avr. total timing 2: " + result.getStat().totalTime.mean() + "us");
        System.out.println("Avr. seed extraction: " + result.getStat().seedExtractionTime.mean() + "us");
        System.out.println("Avr. hit calculation: " + result.getStat().hitCalculationTime.mean() + "us");
        System.out.println("Avr. total mapper: " + result.getStat().mapperTotalTime.mean() + "us");
        System.out.println("Avr. aligner: " + result.getStat().alignerTime.mean() + "us");
        System.out.println("Bad fraction: " + (result.getBadFraction() * 100) + "%");
        System.out.println();
    }
}