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
package com.milaboratory.core.alignment;


import com.milaboratory.core.Range;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.generator.GenericNucleotideMutationModel;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.mutations.generator.SubstitutionModels;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.util.RandomUtil;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.milaboratory.test.TestUtil.randomSequence;

public class AlignmentTest {

    public static NucleotideSequence mutate(NucleotideSequence seq, int[] mut) {
        return new Mutations<NucleotideSequence>(NucleotideSequence.ALPHABET, mut).mutate(seq);
    }

    static int[] move(int[] mutations, int offset) {
        return new Mutations<NucleotideSequence>(NucleotideSequence.ALPHABET, mutations).move(offset).getRAWMutations();
    }

    @Before
    public void setUp() throws Exception {
        //0123456L bad
        RandomUtil.getThreadLocalRandom().setSeed(014343433L);
    }

    @Test
    public void test1() throws Exception {
        NucleotideSequence sequence1 = new NucleotideSequence("TACCGCCAT");
        NucleotideSequence sequence2 = new NucleotideSequence("CCTCAT");
        Alignment<NucleotideSequence> alignment = Aligner.alignLocal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                sequence1, sequence2);
        Assert.assertEquals(3, alignment.convertToSeq2Position(6));
        Assert.assertEquals(0, alignment.convertToSeq2Position(2));
    }

    @Test
    public void test2() throws Exception {
        NucleotideSequence sequence1 = new NucleotideSequence("TACCGCCATGACCA");
        NucleotideSequence sequence2 = new NucleotideSequence("CCTCATCTCTT");
        Alignment<NucleotideSequence> alignment = Aligner.alignLocal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                sequence1, sequence2);

        AlignmentHelper helper = alignment.getAlignmentHelper();

        Assert.assertEquals(alignment.getSequence1Range().getFrom(), helper.getSequence1PositionAt(0));
        Assert.assertEquals(alignment.getSequence1Range().getTo() - 1,
                helper.getSequence1PositionAt(helper.size() - 1));

        Assert.assertEquals(alignment.getSequence2Range().getFrom(), helper.getSequence2PositionAt(0));
        Assert.assertEquals(alignment.getSequence2Range().getTo() - 1,
                helper.getSequence2PositionAt(helper.size() - 1));
    }

    @Test
    public void testConvertPosition() throws Exception {
        Well19937c rand = new Well19937c();
        RandomDataGenerator rdi = new RandomDataGenerator(rand);

        GenericNucleotideMutationModel model = new GenericNucleotideMutationModel(
                SubstitutionModels.getUniformNucleotideSubstitutionModel(.2), .2, .2);

        model.reseed(rand.nextLong());

        for (int i = 0; i < 2000; i++) {
            NucleotideSequence sequence = randomSequence(NucleotideSequence.ALPHABET, rand, 100, 300);

            int length = rdi.nextInt(20, 40);
            int from = rdi.nextInt(0, sequence.size() - length - 1);
            Range seq1Range = new Range(from, from + length);
            int seq2From = rdi.nextInt(200, 400);

            NucleotideSequence subsequence = sequence.getRange(seq1Range);

            Mutations<NucleotideSequence> mut = MutationsGenerator.generateMutations(subsequence, model);

            Range seq2Range = new Range(seq2From, seq2From + subsequence.size() + mut.getLengthDelta());

            Alignment<NucleotideSequence> al = new Alignment<>(sequence, mut.move(from),
                    seq1Range, seq2Range,
                    0);

            for (int seq1Position = from; seq1Position < from + length; seq1Position++) {
                int seq2Position = al.convertToSeq2Position(seq1Position);

                if (seq2Position >= 0) {
                    int seq1PositionC = al.convertToSeq1Position(seq2Position);
                    Assert.assertEquals(seq1Position, seq1PositionC);
                } else {
                    int seq1PositionC = al.convertToSeq1Position(Alignment.aabs(seq2Position));
                    Assert.assertTrue(seq1PositionC > seq1Position);
                }
            }

            for (int seq2Position = seq2Range.getFrom(); seq2Position < seq2Range.getTo(); seq2Position++) {
                int seq1Position = al.convertToSeq1Position(seq2Position);
                if (seq1Position >= 0) {
                    int seq2PositionC = al.convertToSeq2Position(seq1Position);
                    Assert.assertEquals(seq2Position, seq2PositionC);
                } else {
                    int seq2PositionC = al.convertToSeq2Position(Alignment.aabs(seq1Position));
                    Assert.assertTrue(seq2PositionC > seq2Position);
                }
            }

        }
    }

    @Test
    public void testInvert() throws Exception {
        NucleotideSequence seq0 = new NucleotideSequence("GATACATTAGACACAGATACA");
        NucleotideSequence seq1 = new NucleotideSequence("AGACACATATACACAG");
        NucleotideSequence seq2 = new NucleotideSequence("GATACGATACATTAGAGACCACAGATACA");

        Alignment<NucleotideSequence>[] alignments = new Alignment[]{
                Aligner.alignLocalAffine(AffineGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq1),
                Aligner.alignGlobalAffine(AffineGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq1),
                Aligner.alignLocalAffine(AffineGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq2),
                Aligner.alignGlobalAffine(AffineGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq2),
        };

        NucleotideSequence[] originalSeq = new NucleotideSequence[]{seq1, seq1, seq2, seq2};

        for (int i = 0; i < 4; i++) {
            System.out.println(alignments[i].getAlignmentHelper());
            System.out.println(alignments[i].invert(originalSeq[i]).getAlignmentHelper());
            Assert.assertEquals(
                    alignments[i].getAlignmentHelper().toString(),
                    alignments[i].invert(originalSeq[i]).invert(seq0).getAlignmentHelper().toString()
            );
            System.out.println();
        }
    }

    @Test
    public void testSerialization1() throws Exception {
        NucleotideSequence sequence1 = new NucleotideSequence("TACCGCCATGACCA");
        NucleotideSequence sequence2 = new NucleotideSequence("CCTCATCTCTT");
        Alignment<NucleotideSequence> alignment = Aligner.alignLocal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                sequence1, sequence2);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        int cc = 10;
        for (int i = 0; i < cc; i++)
            po.writeObject(alignment);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);

        for (int i = 0; i < cc; i++) {
            Alignment actual = pi.readObject(Alignment.class);
            Assert.assertEquals(alignment, actual);
        }
    }
}
