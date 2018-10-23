package com.milaboratory.core.alignment.kaligner2;

import com.milaboratory.core.Range;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.SequenceBuilder;
import com.milaboratory.test.TestUtil;
import com.milaboratory.util.RandomUtil;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well1024a;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poslavsky on 15/09/15.
 */
public class KMapper2Test {
    public static final KAlignerParameters2 gParams = new KAlignerParameters2(10, 2, true, true,
            99, -1, 100, 0.87f, 48, -3,
            -1, 3, 3, 4, 7, 2, 4, -1000, 150, 0.87f, 2, null);

//    @Test
//    public void testBestOffset1() throws Exception {
//        assertEquals(4, getBestOffset(new IntArrayList(-1, 2, 3, 3, 4, 4, 4), 10, 0, 0));
//        assertEquals(3, getBestOffset(new IntArrayList(-1, 2, 3, 3, 3, 4, 5), 10, 0, 0));
//        assertEquals(-1, getBestOffset(new IntArrayList(-1, -1, -1, 3, 3, 4, 5), 10, 0, 0));
//        assertEquals(3, getBestOffset(new IntArrayList(-1, -1, 3, 3, 3, 4, 5), 10, 0, 0));
//        assertEquals(-1, getBestOffset(new IntArrayList(-1, -1, 3, 3, 3, 4, 5), 3, 0, 0));
//        assertEquals(3, getBestOffset(new IntArrayList(-1, -1, -1, 3, 3, 4, 5), -1, 0, 0));
//        //assertEquals(23, getBestOffset(new IntArrayList(22, 23, 27, 54, 68, 93), Integer.MIN_VALUE, 0, 1));
//    }
//
//    @Test
//    public void testBestOffset2() {
//        //int v = -3358711;
//        int offset = -205;
//        int v = offset << 14 | 0;
//        IntArrayList list = new IntArrayList(1);
//        list.add(v);
//        //list.add((offset+1) << 14 | 0);
//        assertEquals(-205, getBestOffset(list, Integer.MIN_VALUE, 14, 3));
//        System.out.println(v >> 14);
//
//    }

    @Test
    public void testRecord() throws Exception {
        RandomDataGenerator random = new RandomDataGenerator(new Well1024a());
        for (int i = 0; i < 10000; ++i) {
            int offset = random.nextInt(-1000, 1000);
            int index = random.nextInt(0, 1000);
            int record = KMapper2.record(offset, index);
            Assert.assertEquals(index, KMapper2.index(record));
            Assert.assertEquals(offset, KMapper2.offset(record));
        }
    }

    @Test
    public void test1() throws Exception {
        KMapper2 aligner = KMapper2.createFromParameters(gParams);
        aligner.addReference(new NucleotideSequence("ATTAGACACAATATATCTATGATCCTCTATTAGCTACGTACGGCTGATGCTAGTGTCGAT"));
        aligner.addReference(new NucleotideSequence("ACTAGCTGAGCTGTGTAGCTAGTATCTCGATATGCTACATCGTGGGTCGATTAGCTACGT"));
        aligner.addReference(new NucleotideSequence("GCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGA"));

        for (int i = 0; i < TestUtil.its(1000, 50000); ++i) {            //GAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGA
            KMappingResult2 result = aligner.align(new NucleotideSequence("GAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGAAGTAGATGATGATGCAGCGTATG"));

            // System.out.println(result);

            List<KMappingHit2> hits = result.hits;

            Assert.assertEquals("On i = " + i, 1, hits.size());
            Assert.assertEquals(21, KMapper2.offset(hits.get(0).seedRecords[0]));
            Assert.assertEquals(2, hits.get(0).id);
        }
    }

    public static void assertGoodSequenceOfKInQuery(KMappingResult2 result2) {
        for (KMappingHit2 hit : result2.hits)
            assertGoodSequenceOfKInQuery(hit);
    }

    public static void assertGoodSequenceOfKInQuery(KMappingHit2 hit) {
        int[] seedRecords = hit.seedRecords;
        for (int i = 1; i < seedRecords.length; i++)
            if (KMapper2.index(seedRecords[i - 1]) > KMapper2.index(seedRecords[i]))
                throw new AssertionError("Wrong sequence of seeds:\n" + hit);
    }

    public static void assertGoodSequenceOfKInTarget(KMappingResult2 result) {
        for (KMappingHit2 hit : result.hits)
            assertGoodSequenceOfKInTarget(hit);
    }

    public static void assertGoodSequenceOfKInTarget(KMappingHit2 hit) {
        KMappingResult2 result = hit.result;
        int[] seedRecords = hit.seedRecords;
        for (int i = 1; i < seedRecords.length; i++)
            if (KMapper2.positionInTarget(result.seeds, seedRecords[i - 1]) >
                    KMapper2.positionInTarget(result.seeds, seedRecords[i]))
                throw new AssertionError("Wrong sequence of seeds:\n" + hit);
    }

    @Test
    public void testRandom1() throws Exception {
        // MicroCross: dbS: 3799493385881316089L ; S: -542603821215745096L

        ChallengeParameters cp = DEFAULT;
        long noHits = 0, noHits2 = 0, noHits3 = 0, wrongTopHit = 0, wrongTopHitS = 0, noCorrectHitInList = 0;
        DescriptiveStatistics timing = new DescriptiveStatistics(),
                clusters = new DescriptiveStatistics(), topDelta = new DescriptiveStatistics();
        long seed = 0, dbSeed = 0;

        try {
            for (int dbI = 0; dbI < 10; ++dbI) {
                dbSeed = RandomUtil.reseedThreadLocal();
                //System.out.println("DBSeed: " + dbSeed);
                cp.mutationModel.reseed(dbSeed);
                NucleotideSequence[] db = generateDB(RandomUtil.getThreadLocalRandomData(), cp);
                KMapper2 kMapper = KMapper2.createFromParameters(gParams);
                for (NucleotideSequence ns : db)
                    kMapper.addReference(ns);

                for (int i = 0; i < 10_000; ++i) {
                    seed = RandomUtil.reseedThreadLocal();
                    //System.out.println("" + i + ": " + seed);
                    cp.mutationModel.reseed(seed);
                    Challenge challenge = createChallenge(cp, RandomUtil.getThreadLocalRandomData(), db);
                    long start = System.nanoTime();
                    KMappingResult2 result = kMapper.align(challenge.query);
                    timing.addValue(System.nanoTime() - start);

                    assertGoodSequenceOfKInQuery(result);
                    assertGoodSequenceOfKInTarget(result);

                    if (result.getHits().size() == 0) {
                        ++noHits;
                        continue;
                        //result = kMapper.align(challenge.query);
                        //if (result.getHits().size() == 0) {
                        //    ++noHits2;
                        //    result = kMapper.align(challenge.query);
                        //    if (result.getHits().size() == 0) {
                        //        ++noHits3;
                        //        continue;
                        //    }
                        //}
                    }

                    KMappingHit2 top = result.getHits().get(0);

                    boolean containCorrect = false;
                    for (KMappingHit2 hit2 : result.getHits())
                        if (hit2.id == challenge.targetId) {
                            containCorrect = true;
                            topDelta.addValue(hit2.score - top.score);
                        }
                    if (!containCorrect) {
                        //System.out.println(seed);
                        //System.out.println(dbSeed);
                        //System.out.println(challenge.query);
                        //NucleotideSequence target = db[challenge.targetId];
                        //System.out.println(target);
                        //for (int ii = 0; ii < challenge.mutationsInTarget.size(); ii++) {
                        //    System.out.println(new Alignment<>(target, challenge.mutationsInTarget.get(ii),
                        //            challenge.targetClusters.get(ii),
                        //            challenge.queryClusters.get(ii), 0.0f).getAlignmentHelper());
                        //    System.out.println(challenge.mutationsInTarget.get(ii));
                        //}
                        //System.out.println("===================");
                        ++noCorrectHitInList;
                    }

                    if (top.id != challenge.targetId) {
                        ++wrongTopHit;
                        boolean topContainCorrect = false;
                        for (KMappingHit2 hit : result.hits) {
                            if (hit.score < top.score)
                                break;
                            topContainCorrect |= (hit.id == challenge.targetId);
                        }
                        if (!topContainCorrect) {
                            //for (KMappingHit2 hit : result.getHits()) {
                            //    System.out.println(db[hit.id]);
                            //    System.out.println(hit);
                            //}
                            ++wrongTopHitS;
                        }
                        continue;
                    }

                    clusters.addValue(top.boundaries.length + 1);
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("DBSeed: " + dbSeed + "L Seed:" + seed + "L", t);
        }

        System.out.println("noHits: " + noHits);
        System.out.println("noHits2: " + noHits2);
        System.out.println("noHits3: " + noHits3);
        System.out.println("wrongTopHit: " + wrongTopHit);
        System.out.println("wrongTopHitS: " + wrongTopHitS);
        System.out.println("noCorrectHitInList: " + noCorrectHitInList);

        System.out.println("\n\n\n");
        System.out.println("Timings:");
        System.out.println(timing);

        System.out.println("\n\n\n");
        System.out.println("Clusters basicSize");
        System.out.println(clusters);

        System.out.println("\n\n\n");
        System.out.println("Top Delta");
        System.out.println(topDelta);
    }

    @Test
    public void test1111() throws Exception {
        KMapper2 kMapper = KMapper2.createFromParameters(gParams);
        kMapper.addReference(new NucleotideSequence("TCGACCACTTAAGATGTCATCCTTGTGGTGAGTGCTGATGACCCAAGTCTAGCTTTCGGAGAGCC" +
                "AAATTAGTTCCCTACTAGCATACAATCTTCGACATTCTGCAATGTGTCTGGGTGAAGAACTTGCCCACCGTACATTCTGTGGTCGGCTCCTTTCCGGATCGT" +
                "GGTGCCAGTGGGACAGAATACAAGCCTATACCGGTGGTTCACATTTTTCCGCGCGCCAGGCTGTGTCTTTCTTTTGATTGCTCGACATAGAA"));
        KMappingResult2 result = kMapper.align(new NucleotideSequence("CCATTAAGTGTCATCCTTGTGGTGAGTGCTGATGACCCAAGCTATT" +
                "AGTCCTAACTTCCAGCTTTCGGAAGCCAAATTAAGTTCCCTACTAGCATTCAACTGTTTATGTACGACGAGATGGACATCTTCGACATTCTGCAATGTCGT" +
                "CTGGTGAAGAACGGTTTACCCACGTACATTCTGTGGTCGGCCCCTTTC"));
        System.out.println(result.getHits().get(0));
    }

    @Test
    public void test11112() throws Exception {
        KMapper2 kMapper = KMapper2.createFromParameters(gParams);
        kMapper.addReference(new NucleotideSequence("CGATAGAGAACGAGAACGCATGTATCAGGCAAGGAGCGGAGAAGGATGGTAAGAGCGGCGCTATGTT" +
                "TATATCGGCAATGTAACTGCACCGTCTAGCAGATTAGGTCAACCTTAGCGCAGACAGTACCTCATCTGGCGTTTGCGCTATGGGATTTTTAAGAACGTATTAG" +
                "CTCAAGATTGGTATATCAAGTGCTTCCCAGCGCCTAGCATGTGGCCGTATGCTATTCTCCTGTTTTCGGAGTTCTTGTAACTTCTCTCCTTTTATGGTGGCTA" +
                "ACTGCGTAAGACGCACTGTTGTTTATGTGGTAGTTGAAGCACAGAGGTCAATAAGGATTAGCAACTCGTCCTCGGCCACTTGCTTTGTTAAGAGTCGCGCCAGT" +
                "TTCAAGTGATGATTGCGCGTCTGGTACGGAA"));
        KMappingResult2 result = kMapper.align(new NucleotideSequence("ACTTTTTTGGCCTTGGGTAACACTAACACGCATGTATCGGCAAGGAGCG" +
                "GAGAAGGATGGTTGAAGATTATATGGTCGTGCATTAAGAGCGGCGCTATGTTTATATCGGAATTAACTGCACCGTCGTAAAACAAGGAAGACGGCGGGAAAGT" +
                "AGCAGATTAGGTCAACCTAGCTCAGACACCCACAACCCACACCTAAACTGTACCTCATCTGGCGTTGCGCTATGGGATTATTTAAGAACGTATTAGGTATATC" +
                "AAGTGCTTCCCAGCGCC"));
        System.out.println(result.getHits().get(0));
    }

    @Test
    public void test11111() throws Exception {
        KMapper2 kMapper = KMapper2.createFromParameters(gParams);

        kMapper.addReference(new NucleotideSequence("TCGACCACTTAAGATGTCATCCTTGTGGTGAGTGCTGATGACCCAAGTCTAGCTTTCGGAGAGCC" +
                "AAATTAGTTCCCTACTAGCATACAATCTTCGACATTCTGCAATGTGTCTGGGTGAAGAACTTGCCCACCGTACATTCTGTGGTCGGCTCCTTTCCGGATCGT" +
                "GGTGCCAGTGGGACAGAATACAAGCCTATACCGGTGGTTCACATTTTTCCGCGCGCCAGGCTGTGTCTTTCTTTTGATTGCTCGACATAGAA"));

        kMapper.addReference(new NucleotideSequence("AATCTCTATTCTCACATAGAGCGTAATTGTCCGCGATGGTTCAGTGGTGAAACAGGACGCCTCCA" +
                "AAGCGACAGGTATAACCGTTGAAGCTTGAGGTGGTCCTACTTGCAATATACGTTCATTCCGGCACCTTGTCTGTTCCGTCAATTCACCTCTCTTGGGGTAAC" +
                "GTGGATACCCATTCCATAGCTAGCGAATACCTTCGATCTCTGGGTTTACTATTAACAGATATGCTCGGCGGTGCCTTTTCCCATAGGAGGATGATATAGGGC" +
                "TGAGCTTCACTGCTCGAACATACTGAGGGACCTATTCCGTGGAATTGGCCCTGTTGTTTCGACCTCATTTTCAGGAGTTTACGGGAGCAGAAGTCGAGGGCCT" +
                "TTCGCTAGTTTTAGTTATCG"));

        kMapper.addReference(new NucleotideSequence("CAAATACCAGGTCCGAGTCTGTCAACCAGAATCTGTACTATGTGAGATCCGAGAGTCACCACCTTC" +
                "TGGATCGAGAAATAGTCAGTACCACATCTGATTGTAAGCGAGGAAAATGTTTCCATCTGAAGACCGTGGTATCGTACTTGGGGGGCGAGTGCTGCACAACTGC" +
                "ATGGGGCAATTTCAAGGGCAATCTCCGTTTATATATCCCTTATATGTACTCGCACGGGGACGGCAAAAA"));
        for (int i = 0; i < 1000; ++i) {
            KMappingResult2 result = kMapper.align(new NucleotideSequence("CCATTAAGTGTCATCCTTGTGGTGAGTGCTGATGACCCAAGCTATT" +
                    "AGTCCTAACTTCCAGCTTTCGGAAGCCAAATTAAGTTCCCTACTAGCATTCAACTGTTTATGTACGACGAGATGGACATCTTCGACATTCTGCAATGTCGT" +
                    "CTGGTGAAGAACGGTTTACCCACGTACATTCTGTGGTCGGCCCCTTTC"));

            // System.out.println(result.getHits().get(0).score);
        }
    }

    public static NucleotideSequence[] generateDB(RandomDataGenerator generator, ChallengeParameters params) {
        NucleotideSequence[] db = new NucleotideSequence[params.dbSize];
        for (int i = 0; i < params.dbSize; i++)
            db[i] = TestUtil.randomSequence(NucleotideSequence.ALPHABET, generator, params.dbMinSeqLength, params.dbMaxSeqLength);
        return db;
    }

    public static final ChallengeParameters DEFAULT = new ChallengeParameters(100, 100, 500,
            1, 4, 15, 50, 3, 30,
            0.45, 0.45, 0.5,
            MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(5));

    public static Challenge createChallenge(ChallengeParameters cp, RandomDataGenerator generator,
                                            NucleotideSequence[] db) {
        int targetId = generator.nextInt(0, db.length - 1);
        NucleotideSequence target = db[targetId];
        SequenceBuilder<NucleotideSequence> queryBuilder = NucleotideSequence.ALPHABET.createBuilder();
        if (generator.nextUniform(0, 1) < cp.boundaryInsertProbability)
            queryBuilder.append(TestUtil.randomSequence(NucleotideSequence.ALPHABET, generator, cp.minIndelLength, cp.maxIndelLength, true));

        List<Range> tRanges = new ArrayList<>(), qRanges = new ArrayList<>();
        List<Mutations<NucleotideSequence>> muts = new ArrayList<>();

        int tOffset = generator.nextInt(0, cp.maxIndelLength), qOffset = queryBuilder.size();
        Range r;
        Mutations<NucleotideSequence> m;
        NucleotideSequence ins;
        double v;
        for (int i = generator.nextInt(cp.minClusters, cp.maxClusters); i >= 0; --i)
            if (tRanges.isEmpty()) {
                r = new Range(tOffset, tOffset += generator.nextInt(cp.minClusterLength, cp.maxClusterLength));
                if (r.getTo() > target.size())
                    break;
                tRanges.add(r);
                muts.add(m = MutationsGenerator.generateMutations(target, cp.mutationModel, r));
                qRanges.add(new Range(qOffset, qOffset += r.length() + m.getLengthDelta()));
                queryBuilder.append(m.move(-r.getFrom()).mutate(target.getRange(r)));
            } else {
                if ((v = generator.nextUniform(0, 1.0)) < cp.insertionProbability)
                    ins = TestUtil.randomSequence(NucleotideSequence.ALPHABET, generator, cp.minIndelLength, cp.maxIndelLength, true);
                else if (v < cp.insertionProbability + cp.deletionProbability) {
                    tOffset += generator.nextInt(cp.minIndelLength, cp.maxIndelLength);
                    ins = NucleotideSequence.EMPTY;
                } else {
                    ins = TestUtil.randomSequence(NucleotideSequence.ALPHABET, generator, cp.minIndelLength, cp.maxIndelLength, true);
                    tOffset += generator.nextInt(cp.minIndelLength, cp.maxIndelLength);
                }
                r = new Range(tOffset, tOffset += generator.nextInt(cp.minClusterLength, cp.maxClusterLength));
                if (r.getTo() > target.size())
                    break;
                tRanges.add(r);
                muts.add(m = MutationsGenerator.generateMutations(target, cp.mutationModel, r));
                qRanges.add(new Range(qOffset += ins.size(), qOffset += r.length() + m.getLengthDelta()));
                queryBuilder.append(ins).append(m.move(-r.getFrom()).mutate(target.getRange(r)));
            }

        if (generator.nextUniform(0, 1) < cp.boundaryInsertProbability)
            queryBuilder.append(TestUtil.randomSequence(NucleotideSequence.ALPHABET, generator, cp.minIndelLength, cp.maxIndelLength, true));

        return new Challenge(targetId, qRanges, tRanges, muts, queryBuilder.createAndDestroy());
    }

    public static final class ChallengeParameters {
        final int dbSize, dbMinSeqLength, dbMaxSeqLength;
        final int minClusters, maxClusters,
                minClusterLength, maxClusterLength,
                minIndelLength, maxIndelLength;
        final double insertionProbability, deletionProbability, boundaryInsertProbability;
        final NucleotideMutationModel mutationModel;

        public ChallengeParameters(int dbSize, int dbMinSeqLength, int dbMaxSeqLength,
                                   int minClusters, int maxClusters, int minClusterLength,
                                   int maxClusterLength, int minIndelLength, int maxIndelLength,
                                   double insertionProbability, double deletionProbability, double boundaryInsertProbability,
                                   NucleotideMutationModel mutationModel) {
            this.dbSize = dbSize;
            this.dbMinSeqLength = dbMinSeqLength;
            this.dbMaxSeqLength = dbMaxSeqLength;
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
        }
    }

    public static final class Challenge {
        final int targetId;
        final List<Range> queryClusters, targetClusters;
        final List<Mutations<NucleotideSequence>> mutationsInTarget;
        final NucleotideSequence query;

        public Challenge(int targetId, List<Range> queryClusters, List<Range> targetClusters,
                         List<Mutations<NucleotideSequence>> mutationsInTarget, NucleotideSequence query) {
            this.targetId = targetId;
            this.queryClusters = queryClusters;
            this.targetClusters = targetClusters;
            this.mutationsInTarget = mutationsInTarget;
            this.query = query;
        }

        @Override
        public String toString() {
            return "Challenge{" +
                    "targetId=" + targetId +
                    ", queryClusters=" + queryClusters +
                    ", targetClusters=" + targetClusters +
                    ", mutationsInTarget=" + mutationsInTarget +
                    ", query=" + query +
                    '}';
        }
    }

//
//    @Test
//    public void test2() throws Exception {
//        com.milaboratory.core.alignment.KMapper aligner = com.milaboratory.core.alignment.KMapper.createFromParameters(gParams);
//        aligner.addReference(new NucleotideSequence("ATTAGACACAATATATCTATGATCCTCTATTAGCTACGTACGGCTGATGCTAGTGTCGAT"));
//        aligner.addReference(new NucleotideSequence("ACTAGCTGAGCTGTGTAGCTAGTATCTCGATATGCTACATCGTGGGTCGATTAGCTACGT"));
//        aligner.addReference(new NucleotideSequence("GCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGA"));
//
//        KMappingResult2 result =
//                aligner.align(new NucleotideSequence("GACATTATATACAGACATATAATAAATACGGATACGCTGTCGGCCTAGGCGCGTCGAACGCGC"));
//
//        Assert.assertEquals(1, result.hits.size());
//        Assert.assertEquals(2, result.hits.get(0).id);
//    }

//    @Test
//    public void testRandom1() throws Exception {
//        RandomDataGenerator rdi = new RandomDataGenerator(new Well19937c(127368647891L));
//        int baseSize = its(500, 2000);
//        List<NucleotideSequence> ncs = new ArrayList<>(baseSize);
//        for (int i = 0; i < baseSize; ++i)
//            ncs.add(randomSequence(NucleotideSequence.ALPHABET, rdi, 40, 60));
//
//        com.milaboratory.core.alignment.KMapper ka = com.milaboratory.core.alignment.KMapper.createFromParameters(gParams.clone().setMapperMaxSeedsDistance(2).setMapperMinSeedsDistance(1).setMapperKValue(6));
//        for (NucleotideSequence seq : ncs)
//            ka.addReference(seq);
//
//        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(3.0);
//        int total = its(1000, 100000);
//        int found = 0;
//        OUTER:
//        for (int i = 0; i < total; ++i) {
//            int id = rdi.nextInt(0, baseSize - 1);
//            NucleotideSequence seq = ncs.get(id);
//            int subSize = rdi.nextInt(15, seq.size());
//            boolean left = (rdi.nextInt(0, 1) == 0);
//            NucleotideSequence target;
//            if (left)
//                target = seq.getRange(seq.size() - subSize, seq.size()).concatenate(randomSequence(NucleotideSequence.ALPHABET, rdi, 20, 30));
//            else
//                target = randomSequence(NucleotideSequence.ALPHABET, rdi, 20, 30).concatenate(seq.getRange(0, subSize));
//
//            int[] muts = generateMutations(target, model).getAllMutations();
//            target = mutate(target, muts);
//
//            KMappingResult2 result = ka.align(target);
//            List<KMappingHit2> hits = result.hits;
//            for (KMappingHit2 hit : hits) {
//
//                int previousSeedHit = -1, seedHit, seedHitOffset;
//                for (int k = 0; k < hit.seedOffsets.length; ++k) {
//                    seedHitOffset = hit.seedOffsets[k];
//                    if (seedHitOffset != KMapper2.SEED_NOT_FOUND_OFFSET)
//                        if (previousSeedHit == -1)
//                            previousSeedHit = result.seeds.get(hit.from + k) - seedHitOffset;
//                        else {
//                            seedHit = result.seeds.get(hit.from + k) - seedHitOffset;
//                            assertTrue(previousSeedHit < seedHit);
//                            previousSeedHit = seedHit;
//                        }
//                }
//
//                if (hit.id == id) {
//                    //Test for kmers
//                    for (int k = 0; k < hit.seedOffsets.length; ++k) {
//                        if (hit.seedOffsets[k] == SEED_NOT_FOUND_OFFSET)
//                            continue;
//                        int kmer1 = 0, kmer2 = 0;
//                        //get kmer in target sequence
//                        int targetFrom = result.seeds.get(hit.from + k);
//                        for (int j = targetFrom; j < targetFrom + ka.getNValue(); ++j)
//                            kmer1 = kmer1 << 2 | target.codeAt(j);
//
//                        //kmer in ref sequence
//                        int refFrom = targetFrom - hit.seedOffsets[k];
//                        for (int j = refFrom; j < refFrom + ka.getNValue(); ++j)
//                            kmer2 = kmer2 << 2 | ncs.get(hit.id).codeAt(j);
//
//                        Assert.assertEquals(kmer1, kmer2);
//                    }
//                    ++found;
//                    continue OUTER;
//                }
//            }
//
//        }
//
//        Assert.assertTrue((float) found / total > 0.85f);
//    }


    static int kMerTrailingZeros(int kMer, int mask) {
        int i = Integer.numberOfTrailingZeros(mask);
        kMer = mask;
        mask >>= i;
        return kMer;
    }
}