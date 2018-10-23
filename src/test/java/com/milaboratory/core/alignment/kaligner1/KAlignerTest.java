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

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.*;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.NucleotideSequence;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.milaboratory.core.mutations.Mutation.*;
import static com.milaboratory.core.mutations.generator.MutationsGenerator.generateMutations;
import static com.milaboratory.core.sequence.NucleotideSequence.EMPTY;
import static com.milaboratory.test.TestUtil.*;

public class KAlignerTest extends AlignmentTest {
    public static final KAlignerParameters gParams = new KAlignerParameters(5, false, false,
            1.5f, 0.75f, 1.0f, -0.1f, -0.3f, 4, 10, 15, 2, -10,
            40.0f, 0.87f, 7,
            LinearGapAlignmentScoring.getNucleotideBLASTScoring());

    @Test
    public void testDefault1() throws Exception {
        NucleotideSequence ref = new NucleotideSequence("TGATCTTGACGTTGTAGATGAGGCAGCCGTCCTGGAGGCTGGTGTCCTGGGTAGCGGTCAGCA" +
                "CGCCCCCGTCTTCGTATGTGGTGACTCTCTCCCATGTGAAGCCCTCGGGGAAGGACTGCTTAAAGA"),
                target = new NucleotideSequence("TGATCTTGACGTTGTAGATGAGGCAGCCGTTCTGGAGGCTGGTGTCCTGGGTAGCGGTCAGCACGCCCCCG" +
                        "TCTTCGTATGTGGTGACTCTCTCCCATGTGAAGCCCTCAGGGAAGGACTGCTTAAAGA");

        KAligner aligner = new KAligner(KAlignerParameters.getByName("default"));
        aligner.addReference(ref);

        for (int i = its(100000, 1000000); i >= 0; --i) {
            KAlignmentResult result = aligner.align(target);
            for (int mut : result.getBestHit().getAlignment().getRelativeMutations().getRAWMutations())
                Assert.assertFalse(isInDel(mut));
        }
    }

    @Test
    public void test1() throws Exception {
        KAligner<?> aligner = new KAligner(gParams.clone().setFloatingRightBound(true));
        aligner.addReference(new NucleotideSequence("ATTAGACACAATATATCTATGATCCTCTATTAGCTACGTACGGCTGATGCTAGTGTCGAT"));
        aligner.addReference(new NucleotideSequence("ACTAGCTGAGCTGTGTAGCTAGTATCTCGAACTATGCTACATCGTGGGTCGATTAGCTACGT"));
        aligner.addReference(new NucleotideSequence("GCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGACT"));
        aligner.addReference(new NucleotideSequence("GCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGAAATTCTCTGA"));

        NucleotideSequence target = new NucleotideSequence("CTGCGCGATGtATATCGCGATAACTCTCTGAaCtggatatcagtatagccagca");
        int[] onetwo = new int[3];
        int[] refs = new int[4];
        int n = 10000;
        for (int i = 0; i < n; ++i) {
            KAlignmentResult<?> result = aligner.align(target);
            result.calculateAllAlignments();
            for (KAlignmentHit hit : result.getHits()) {
                Alignment la = hit.getAlignment();
                NucleotideSequence ref = aligner.getReference(hit.getId());
                NucleotideSequence seq = result.getTarget();
                Assert.assertEquals(seq, target);
            }

            ++onetwo[result.hits.size()];
            if (result.hasHits())
                ++refs[result.getBestHit().getId()];
        }
        Assert.assertTrue(onetwo[0] < 200);
        Assert.assertEquals(n - onetwo[0], refs[2]);
    }

    @Test
    public void test2() throws Exception {
        KAligner<?> aligner = new KAligner(gParams.clone().setFloatingLeftBound(true));
        aligner.addReference(new NucleotideSequence("ATTAGACACAATATATCTATGATCCTCTATTAGCTACGTACGGCTGATGCTAGTGTCGAT"));
        aligner.addReference(new NucleotideSequence("ACTAGCTGAGCTGTGTAGCTAGTATCTCGATATGCTACATCGTGGGTCGATTAGCTACGT"));
        aligner.addReference(new NucleotideSequence("GCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGA"));

        KAlignmentResult<?> result = aligner.align(new NucleotideSequence("atgcgtagtcgcgtcgtagtgactcgGCTGGTCGGCCTAGGCGCGATCGAACCGCTGCTCGA"));
        Assert.assertEquals(1, result.hits.size());
        Assert.assertEquals(2, result.hits.get(0).getId());
    }

    @Test
    public void test3() throws Exception {
        KAlignerParameters parameters = gParams.clone();
        parameters
                .setScoring(new LinearGapAlignmentScoring(NucleotideSequence.ALPHABET,
                        ScoringUtils.getSymmetricMatrix(4, -4, NucleotideSequence.ALPHABET), -5))
                .setFloatingRightBound(true).setMaxAdjacentIndels(2);
        KAligner<?> aligner = new KAligner(parameters);
        aligner.addReference(new NucleotideSequence("ATTAGACACAATATATCTATGATCCTCTATTAGCTACGTACGGCTGATGCTAGTGTCGAT"));
        aligner.addReference(new NucleotideSequence("ACTAGCTGAGCTGTGTAGCTAGTATCTCGATATGCTACATCGTGGGTCGATTAGCTACGT"));
        aligner.addReference(new NucleotideSequence("CACAGCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGA"));

        //KAlignmentResult result = aligner.align(new NucleotideSequence("GTAGCTAGTATCTCGATATGCTACATCGTGGGTCGATTAGCattagacagacagcgctacgtcgta"));
        KAlignmentResult<?> result = aligner.align(new NucleotideSequence("TCTGTCGGCCTAGGCGCGATCGAacACGCGCTGCGCGATGATATATCGCGATAATTCTCattagaccgcgaggcgag"));
        result.calculateAllAlignments();
        //for (KAlignmentHit hit : result.hits) {
        //    printHitAlignment(hit);
        //}
        KAlignmentHit hit = result.hits.get(0);
        Assert.assertEquals(2, hit.getId());
        Assert.assertEquals(0, hit.getAlignment().getSequence2Range().getFrom());
        Assert.assertEquals(4, hit.getAlignment().getSequence1Range().getFrom());
    }

    @Test
    public void test4() throws Exception {
        KAlignerParameters parameters = KAlignerParameters.getByName("default");
        KAligner aligner = new KAligner(parameters);
        aligner.addReference(new NucleotideSequence("ATTAGACACAATATATCTATGATCCTCTATTAGCTACGTACGGCTGATGCTAGTGTCGAT"));
        aligner.addReference(new NucleotideSequence("ACTAGCTGAGCTGTGTAGCTAGTATCTCGATATGCTACATCGTGGGTCGATTAGCTACGT"));
        aligner.addReference(new NucleotideSequence("CACAGCTGTCGGCCTAGGCGCGATCGAACGCGCTGCGCGATGATATATCGCGATAATTCTCTGA"));

        NucleotideSequence target = new NucleotideSequence("CTACATCGTGGGTCGATTAGCTACGTAGTAGAGCATGGCTAGAGTACGTCGGCGATACG");


        KAlignmentResult result = aligner.align(target, 5, 40);
        result.calculateAllAlignments();
        Assert.assertEquals(new Range(5, 26), result.getBestHit().getAlignment().getSequence2Range());

        result = aligner.align(target, 5, 40, false, null);
        result.calculateAllAlignments();
        Assert.assertEquals(new Range(0, 26), result.getBestHit().getAlignment().getSequence2Range());
    }

    @Test
    public void testRandomCorrectness() throws Exception {
        KAlignerParameters p = gParams.clone().setMapperKValue(6).setAlignmentStopPenalty(Integer.MIN_VALUE)
                .setMapperAbsoluteMinScore(2.1f).setMapperMinSeedsDistance(4).setAbsoluteMinScore(100.0f).setMapperRelativeMinScore(0.8f);
        p.setScoring(new LinearGapAlignmentScoring(NucleotideSequence.ALPHABET,
                ScoringUtils.getSymmetricMatrix(4, -4, NucleotideSequence.ALPHABET), -5)).setMaxAdjacentIndels(2);

        KAlignerParameters[] params = new KAlignerParameters[]{p.clone(),
                p.clone().setFloatingLeftBound(true), p.clone().setFloatingRightBound(true),
                p.clone().setFloatingLeftBound(true).setFloatingRightBound(true)};

        RandomDataGenerator rdi = new RandomDataGenerator(new Well19937c(127368647891L));
        int baseSize = its(400, 2000);
        int total = its(3000, 30000);
        int i, id;

        NucleotideMutationModel mutationModel = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(20.0);
        mutationModel.reseed(12343L);

        for (KAlignerParameters parameters : params) {
            long time = 0, timestamp;
            KAligner<Integer> aligner = new KAligner<>(parameters);

            int correct = 0, incorrect = 0, miss = 0, scoreError = 0, random = 0;

            List<NucleotideSequence> ncs = new ArrayList<>(baseSize);
            for (i = 0; i < baseSize; ++i) {
                NucleotideSequence reference = randomSequence(NucleotideSequence.ALPHABET, rdi, 100, 300);
                ncs.add(reference);
                aligner.addReference(reference, i);
            }

            for (i = 0; i < total; ++i) {
                id = rdi.nextInt(0, baseSize - 1);
                NucleotideSequence ref = ncs.get(id);
                int trimRight, trimLeft;
                boolean addLeft, addRight;

                if (parameters.isFloatingLeftBound()) {
                    trimLeft = rdi.nextInt(0, ref.size() / 3);
                    addLeft = true;
                } else {
                    if (rdi.nextInt(0, 1) == 0) {
                        trimLeft = 0;
                        addLeft = true;
                    } else {
                        trimLeft = rdi.nextInt(0, ref.size() / 3);
                        addLeft = false;
                    }
                }

                if (parameters.isFloatingRightBound()) {
                    trimRight = rdi.nextInt(0, ref.size() / 3);
                    addRight = true;
                } else {
                    if (rdi.nextInt(0, 1) == 0) {
                        trimRight = 0;
                        addRight = true;
                    } else {
                        trimRight = rdi.nextInt(0, ref.size() / 3);
                        addRight = false;
                    }
                }

                NucleotideSequence subSeq = ref.getRange(trimLeft, ref.size() - trimRight);
                NucleotideSequence left = addLeft ? randomSequence(NucleotideSequence.ALPHABET, rdi, 10, 30) : EMPTY;
                NucleotideSequence right = addRight ? randomSequence(NucleotideSequence.ALPHABET, rdi, 10, 30) : EMPTY;

                Mutations<NucleotideSequence> nucleotideSequenceMutations = generateMutations(subSeq, mutationModel);
                int[] subSeqMutations = nucleotideSequenceMutations.getRAWMutations();
                float actionScore = AlignmentUtils.calculateScore(subSeq, new Range(0, subSeq.size()),
                        nucleotideSequenceMutations, parameters.getScoring());

                int indels = 0;
                for (int mut : subSeqMutations)
                    if (isDeletion(mut) || isInsertion(mut))
                        ++indels;

                NucleotideSequence target = left.concatenate(mutate(subSeq, subSeqMutations)).concatenate(right);

                timestamp = System.nanoTime();
                KAlignmentResult<Integer> result = aligner.align(target);
                result.calculateAllAlignments();
                time += System.nanoTime() - timestamp;

                boolean found = false;
                for (KAlignmentHit<Integer> hit : result.hits) {
                    Assert.assertEquals((Integer) hit.getId(), hit.getRecordPayload());
                    if (hit.getId() == id) {
                        //System.out.println(hit.getAlignmentScore());
                        found = true;
                        if (!parameters.isFloatingLeftBound())
                            Assert.assertTrue(hit.getAlignment().getSequence1Range().getFrom() == 0 ||
                                    hit.getAlignment().getSequence2Range().getFrom() == 0);
                        if (!parameters.isFloatingRightBound())
                            Assert.assertTrue(hit.getAlignment().getSequence1Range().getTo() == ref.size() ||
                                    hit.getAlignment().getSequence2Range().getTo() == target.size());
                        if (hit.getAlignment().getScore() < actionScore && indels <= parameters.getMaxAdjacentIndels()) {
                            ++scoreError;
                            //System.out.println(target);
                            //System.out.println(left);
                            //printAlignment(subSeq, subSeqMutations);
                            //System.out.println(right);
                            //printHitAlignment(hit);
                            ////printAlignment(ncs.get(hit.getId()).getRange(hit.getAlignment().getSequence1Range()),
                            ////        hit.getAlignment().getMutations());
                            //found = true;
                        }
                    } else {
                        //printHitAlignment(hit);
                        //System.out.println(hit.getAlignmentScore());
                        ++incorrect;
                    }
                }

                if (found)
                    ++correct;
                else {
                    if (indels <= parameters.getMaxAdjacentIndels()) {
                        ++miss;
                        //System.out.println(target);
                        //System.out.println(left);
                        //printAlignment(subSeq, subSeqMutations);
                        //System.out.println(right);
                    }
                }

                NucleotideSequence randomSequence = randomSequence(NucleotideSequence.ALPHABET, rdi, target.size() - 1, target.size());
                for (KAlignmentHit hit : aligner.align(randomSequence).hits) {
                    hit.calculateAlignment();
                    if (hit.getAlignment().getScore() >= 100.0)
                        ++random;
                }

                //if (aligner.align(randomSequence).hits.size() > 0)
                //    random++;
            }

            System.out.println("C=" + correct + ";I=" + incorrect + ";M=" + miss + ";ScE=" + scoreError + ";R=" + (1.0 * random / baseSize / total) + " AlignmentTime = " + time(time / total));
            //Assert.assertEquals(1.0, 1.0 * correct / total, 0.01);
            //Assert.assertEquals(0.0, 1.0 * incorrect / total, 0.001);
            //Assert.assertEquals(0.0, 1.0 * miss / total, 0.001);
            //Assert.assertEquals(0.0, 1.0 * scoreError / total, 0.001);
            //Assert.assertEquals(0.0, 1.0 * random / total / baseSize, 5E-6);
        }
    }

    @Test
    public void testRandomCorrectnessConcurrent() throws Exception {
        KAlignerParameters p = gParams.clone().setMapperKValue(6)
                .setAlignmentStopPenalty(Integer.MIN_VALUE)
                .setMapperAbsoluteMinScore(2.1f).setMapperMinSeedsDistance(4);
        p.setScoring(new LinearGapAlignmentScoring(NucleotideSequence.ALPHABET,
                ScoringUtils.getSymmetricMatrix(4, -4, NucleotideSequence.ALPHABET), -5)).setMaxAdjacentIndels(2);

        KAlignerParameters[] params = new KAlignerParameters[]{p.clone(),
                p.clone().setFloatingLeftBound(true), p.clone().setFloatingRightBound(true),
                p.clone().setFloatingLeftBound(true).setFloatingRightBound(true)};

        RandomDataGenerator rdi = new RandomDataGenerator(new Well19937c(127368647891L));
        final int baseSize = its(400, 2000);
        final int total = its(3000, 30000);
        final int threadCount = 20;
        int i, id;

        final NucleotideMutationModel mutationModel = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(4.0);
        mutationModel.reseed(12343L);

        for (final KAlignerParameters parameters : params) {
            final KAligner<?> aligner = new KAligner(parameters);

            final AtomicInteger correct = new AtomicInteger(0), incorrect = new AtomicInteger(0),
                    miss = new AtomicInteger(0), scoreError = new AtomicInteger(0), random = new AtomicInteger(0);

            final List<NucleotideSequence> ncs = new ArrayList<>(baseSize);
            for (i = 0; i < baseSize; ++i) {
                NucleotideSequence reference = randomSequence(NucleotideSequence.ALPHABET, rdi, 100, 300);
                ncs.add(reference);
                aligner.addReference(reference);
            }

            final AtomicInteger counter = new AtomicInteger(total);

            Thread[] threads = new Thread[threadCount];

            final AtomicLong time = new AtomicLong(0L);

            final AtomicLong seedCounter = new AtomicLong(1273L);
            for (i = 0; i < threadCount; ++i) {
                threads[i] = new Thread() {
                    @Override
                    public void run() {
                        long timestamp;
                        //Different seed for different thread.
                        RandomDataGenerator rdi = new RandomDataGenerator(new Well19937c(seedCounter.addAndGet(117L)));
                        while (counter.decrementAndGet() >= 0) {
                            int id = rdi.nextInt(0, baseSize - 1);
                            NucleotideSequence ref = ncs.get(id);
                            int trimRight, trimLeft;
                            boolean addLeft, addRight;

                            if (parameters.isFloatingLeftBound()) {
                                trimLeft = rdi.nextInt(0, ref.size() / 3);
                                addLeft = true;
                            } else {
                                if (rdi.nextInt(0, 1) == 0) {
                                    trimLeft = 0;
                                    addLeft = true;
                                } else {
                                    trimLeft = rdi.nextInt(0, ref.size() / 3);
                                    addLeft = false;
                                }
                            }

                            if (parameters.isFloatingRightBound()) {
                                trimRight = rdi.nextInt(0, ref.size() / 3);
                                addRight = true;
                            } else {
                                if (rdi.nextInt(0, 1) == 0) {
                                    trimRight = 0;
                                    addRight = true;
                                } else {
                                    trimRight = rdi.nextInt(0, ref.size() / 3);
                                    addRight = false;
                                }
                            }

                            NucleotideSequence subSeq = ref.getRange(trimLeft, ref.size() - trimRight);
                            NucleotideSequence left = addLeft ? randomSequence(NucleotideSequence.ALPHABET, rdi, 10, 30) : EMPTY;
                            NucleotideSequence right = addRight ? randomSequence(NucleotideSequence.ALPHABET, rdi, 10, 30) : EMPTY;

                            int[] subSeqMutations;
                            Mutations<NucleotideSequence> mmutations;
                            synchronized (mutationModel) {
                                mmutations = generateMutations(subSeq, mutationModel);
                                subSeqMutations = mmutations.getRAWMutations();
                            }
                            float actionScore = AlignmentUtils.calculateScore(subSeq, new Range(0, subSeq.size()), mmutations, parameters.getScoring());

                            int indels = 0;
                            for (int mut : subSeqMutations)
                                if (isDeletion(mut) || isInsertion(mut))
                                    ++indels;

                            NucleotideSequence target = left.concatenate(mutate(subSeq, subSeqMutations)).concatenate(right);

                            timestamp = System.nanoTime();
                            KAlignmentResult<?> result = aligner.align(target);
                            time.addAndGet(System.nanoTime() - timestamp);

                            boolean found = false;
                            for (KAlignmentHit hit : result.hits) {
                                if (hit.getId() == id) {
                                    //System.out.println(hit.getAlignmentScore());
                                    found = true;
                                    if (!parameters.isFloatingLeftBound())
                                        Assert.assertTrue(hit.getAlignment().getSequence1Range().getFrom() == 0 ||
                                                hit.getAlignment().getSequence2Range().getFrom() == 0);
                                    if (!parameters.isFloatingRightBound())
                                        Assert.assertTrue(hit.getAlignment().getSequence1Range().getTo() == ref.size() ||
                                                hit.getAlignment().getSequence2Range().getTo() == target.size());
                                    if (hit.getAlignment().getScore() < actionScore && indels <= parameters.getMaxAdjacentIndels()) {
                                        scoreError.incrementAndGet();
                                        //System.out.println(target);
                                        //System.out.println(left);
                                        //printAlignment(subSeq, subSeqMutations);
                                        //System.out.println(right);
                                        //printHitAlignment(hit);
                                        ////printAlignment(ncs.get(hit.getId()).getRange(hit.getAlignment().getSequence1Range()),
                                        ////        hit.getAlignment().getMutations());
                                        //found = true;
                                    }
                                } else {
                                    //printHitAlignment(hit);
                                    //System.out.println(hit.getAlignmentScore());
                                    incorrect.incrementAndGet();
                                }
                            }

                            if (found)
                                correct.incrementAndGet();
                            else {
                                if (indels <= parameters.getMaxAdjacentIndels()) {
                                    miss.incrementAndGet();
                                    //System.out.println(target);
                                    //System.out.println(left);
                                    //printAlignment(subSeq, subSeqMutations);
                                    //System.out.println(right);
                                }
                            }

                            NucleotideSequence randomSequence = randomSequence(NucleotideSequence.ALPHABET, rdi, target.size() - 1, target.size());
                            for (KAlignmentHit hit : aligner.align(randomSequence).hits) {
                                hit.calculateAlignment();
                                if (hit.getAlignment().getScore() >= 110.0)
                                    random.incrementAndGet();
                            }
                        }
                    }
                };
            }

            for (i = 0; i < threadCount; ++i)
                threads[i].start();

            for (i = 0; i < threadCount; ++i)
                threads[i].join();

            System.out.println("C=" + correct.get() + ";I=" + incorrect.get() + ";M=" + miss.get() + ";ScE=" + scoreError.get() + ";R=" + (1.0 * random.get() / baseSize / total) + " AlignmentTime = " + time(time.get() / total));
            Assert.assertEquals(1.0, 1.0 * correct.get() / total, 0.01);
            Assert.assertEquals(0.0, 1.0 * incorrect.get() / total, 0.001);
            Assert.assertEquals(0.0, 1.0 * miss.get() / total, 0.001);
            Assert.assertEquals(0.0, 1.0 * scoreError.get() / total, 0.003);
            Assert.assertEquals(0.0, 1.0 * random.get() / total / baseSize, 5E-6);
        }
    }

    @Test
    public void testRandom() {
        int its = its(100, 200);
        RandomDataGenerator rdi = new RandomDataGenerator(new Well19937c(12736861L));
        KAligner<?> aligner = new KAligner(gParams.clone().setMapperKValue(3));

        for (int i = 0; i < its; ++i)
            aligner.addReference(randomSequence(NucleotideSequence.ALPHABET, rdi, 50, 70));

        int numberOfTargetSequences = its(10000, 100000);
        for (int i = 0; i < numberOfTargetSequences; ++i) {
            NucleotideSequence sequence = randomSequence(NucleotideSequence.ALPHABET, rdi, 20, 30);
            KAlignmentResult<?> result = aligner.align(sequence);
            result.calculateAllAlignments();
            for (KAlignmentHit hit : result.hits) {
                Alignment<NucleotideSequence> la = hit.getAlignment();
                NucleotideSequence ref = aligner.getReference(hit.getId());
                NucleotideSequence seq = result.getTarget();

                Assert.assertEquals(seq.getRange(la.getSequence2Range()),
                        la.getRelativeMutations().mutate(ref.getRange(la.getSequence1Range())));
            }
        }

    }

    @Test
    public void testRandom1() throws Exception {
        RandomDataGenerator rdi = new RandomDataGenerator(new Well19937c(127368647891L));
        int baseSize = its(500, 2000);
        List<NucleotideSequence> ncs = new ArrayList<>(baseSize);
        for (int i = 0; i < baseSize; ++i)
            ncs.add(randomSequence(NucleotideSequence.ALPHABET, rdi, 40, 300));

        KAligner<?> aligner = new KAligner(gParams.clone().setMapperKValue(6).setMapperMaxSeedsDistance(2).setMapperMinSeedsDistance(1).setMapperAbsoluteMinScore(3.5f).setMapperMismatchPenalty(-0.5f).setMaxAdjacentIndels(3));
        for (NucleotideSequence seq : ncs)
            aligner.addReference(seq);

        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(2.0);
        model.reseed(123784L);
        int total = its(10000, 30000);
        int found = 0;
        int wrong = 0;
        long time = 0, timestamp;
        int randomHits = 0;
        OUTER:
        for (int i = 0; i < total; ++i) {
            int id = rdi.nextInt(0, baseSize - 1);
            NucleotideSequence seq = ncs.get(id);
            int subSize = rdi.nextInt(15, 30);
            boolean left = (rdi.nextInt(0, 1) == 0);
            NucleotideSequence target;
            if (left)
                target = seq.getRange(seq.size() - subSize, seq.size()).concatenate(randomSequence(NucleotideSequence.ALPHABET, rdi, 40, 70));
            else
                target = randomSequence(NucleotideSequence.ALPHABET, rdi, 40, 70).concatenate(seq.getRange(0, subSize));

            Mutations<NucleotideSequence> mmutations = generateMutations(target, model);
            int[] muts = mmutations.getRAWMutations();
            target = mutate(target, muts);

            timestamp = System.nanoTime();
            KAlignmentResult<?> result = aligner.align(target);
            result.calculateAllAlignments();
            for (KAlignmentHit res : result.hits) {
                res.getAlignment();
                if (res.getId() == id)
                    ++found;
                else
                    ++wrong;
            }
            time += System.nanoTime() - timestamp;

            NucleotideSequence randomSequence = randomSequence(NucleotideSequence.ALPHABET, rdi, 100, 150);
            result = aligner.align(randomSequence);
            result.calculateAllAlignments();
            for (KAlignmentHit res : result.hits)
                if (res.getAlignment().getRelativeMutations().size() < 4)
                    randomHits++;
        }

        System.out.println("##teamcity[buildStatisticValue key='kmFound' value='" + (((double) found) / total) + "']");
        System.out.println("##teamcity[buildStatisticValue key='kmWrong' value='" + (((double) wrong) / total) + "']");
        System.out.println("##teamcity[buildStatisticValue key='kmFalse' value='" + (((double) randomHits) / total) + "']");

        Assert.assertTrue((float) found / total > 0.85f);
        Assert.assertTrue((float) wrong / total < 0.05f);
        //System.out.println(1.0 * randomHits / total / baseSize);
        Assert.assertTrue((float) randomHits / total / baseSize < 2E-5f);
        //System.out.println(TestUtil.time(time / total));
    }

    @Test
    public void testAlignment() throws Exception {
        NucleotideSequence ref = new NucleotideSequence("GACCCCTGGTAAGCGCGTAATCTGTTTTCTATTAAATATAAGCTGGTTTTCGCGGCGTTCACTTACCGGGCGTTACGTCCCTGTCATCTACTGCCGGATAGGAATCTCACGCGGCGCTCGATACTGGCCCTGCGGGCCTATCTGAGGCCGACTACGGGACTTCCTGCATGGACGCGCGCCGTCTGCCGCGTCGGGAATTAATATATGTGCACTTGCGGCGCCATTCAGCTGCCTGCAGAACTTGGTTCACTGTCTCGGACACTGACTG");
        NucleotideSequence target = new NucleotideSequence("AACTCGTTCACTGTCTCGGACACTGACTGGTCGTCCATCCATGAACGAACTGTATCTAGACTCCACTTTCATGGAAGTGCAAAT");

    }

    public void testRandomCheckScore() throws Exception {
        // TODO: check this
        RandomDataGenerator rdi = new RandomDataGenerator(new Well19937c(127368647891L));
        int baseSize = its(500, 2000);
        List<NucleotideSequence> ncs = new ArrayList<>(baseSize);
        for (int i = 0; i < baseSize; ++i)
            ncs.add(randomSequence(NucleotideSequence.ALPHABET, rdi, 70, 100));

        KAligner<?> alignerLeft = new KAligner(gParams.clone().setMapperKValue(6).setMapperMaxSeedsDistance(2).setMapperMinSeedsDistance(1).
                setMapperAbsoluteMinScore(3.5f).setMapperMismatchPenalty(-0.5f).setMaxAdjacentIndels(3).setFloatingLeftBound(true).
                setAlignmentStopPenalty(-1000));

        KAligner<?> alignerRight = new KAligner(gParams.clone().setMapperKValue(6).setMapperMaxSeedsDistance(2).setMapperMinSeedsDistance(1).
                setMapperAbsoluteMinScore(3.5f).setMapperMismatchPenalty(-0.5f).setMaxAdjacentIndels(3).setFloatingRightBound(true).
                setAlignmentStopPenalty(-1000));

        for (NucleotideSequence seq : ncs) {
            alignerLeft.addReference(seq);
            alignerRight.addReference(seq);
        }

        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(3.0);
        model.reseed(123784L);

        int total = its(1000, 100000);
        int found = 0;
        int wrong = 0;

        OUTER:
        for (int i = 0; i < total; ++i) {
            int id = rdi.nextInt(0, baseSize - 1);
            NucleotideSequence ref = ncs.get(id);
            int subSize = rdi.nextInt(15, 30);
            boolean left = (rdi.nextInt(0, 1) == 0);
            NucleotideSequence target;
            if (left)
                target = ref.getRange(ref.size() - subSize, ref.size());//.concatenate(randomSequence(NucleotideSequence.ALPHABET, rdi, 40, 70));
            else
                target = ref.getRange(0, subSize);

            Mutations<NucleotideSequence> nucleotideSequenceMutations = generateMutations(target, model);
            int[] muts = nucleotideSequenceMutations.getRAWMutations();
            NucleotideSequence orig = mutate(target, muts);

            float actualScore = AlignmentUtils.calculateScore(target, new Range(0, subSize), nucleotideSequenceMutations, gParams.getScoring());

            if (left)
                target = orig.concatenate(randomSequence(NucleotideSequence.ALPHABET, rdi, 40, 70));
            else
                target = randomSequence(NucleotideSequence.ALPHABET, rdi, 40, 70).concatenate(orig);


            KAlignmentResult<?> result;
            if (left)
                result = alignerLeft.align(target);
            else
                result = alignerRight.align(target);

            result.calculateAllAlignments();

            for (KAlignmentHit res : result.hits) {
                Alignment<NucleotideSequence> la = res.getAlignment();

                if (res.getId() == id && res.getAlignment().getScore() >= actualScore)
                    ++found;
                else if (res.getId() == id && res.getAlignment().getScore() < actualScore
                        && !orig.equals(target.getRange(la.getSequence2Range()))) {
                    //System.out.println(actualScore);
                    //System.out.println(res.getAlignmentScore());
                    //
                    //System.out.println(ref);
                    //System.out.println(target);
                    //System.out.println(orig);
                    //System.out.println(target.getRange(la.getSequence2Range()));
                    //System.out.println(muts.length);
                    //System.out.println(la.getMutations().length);
                    ////System.out.println(orig);
                    ////
                    ////System.out.println(target.getRange(la.getSequence2Range()));
                    //
                    //System.out.println(subSize);
                    //System.out.println(la.getSequence2Range().length());
                    //printMutations(NucleotideSequence.ALPHABET, muts);
                    //printAlignment(ref.getRange(la.getSequence1Range()), la.getMutations());
                    //System.out.println();

                } else
                    ++wrong;

                //Assert.assertEquals(target.getRange(la.getSequence2Range()),
                //        Mutations.mutate(ref.getRange(la.getSequence1Range()), la.getMutations()));
            }

        }
        //Assert.assertTrue((float) found / total > 0.85f);
        //Assert.assertTrue((float) wrong / total < 0.05f);
    }

}
