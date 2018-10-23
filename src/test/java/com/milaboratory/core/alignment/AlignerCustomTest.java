/*
 * Copyright 2016 MiLaboratory.com
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

import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.sequence.NucleotideSequence;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import java.util.Arrays;

import static com.milaboratory.test.TestUtil.its;
import static com.milaboratory.test.TestUtil.randomSequence;
import static org.junit.Assert.assertTrue;

public class AlignerCustomTest {
    @Test
    public void testSemiLocal0() throws Exception {
        //NucleotideSequence seq1 = new NucleotideSequence("GGGTTAGAACACATACATTATTATTATAT");
        //NucleotideSequence seq2 = new NucleotideSequence("AATTAGACACACATACACGCGCCGCGC");
        NucleotideSequence seq1 = new NucleotideSequence("GCCCCCTTGAATGTTAGTATAACTATCT");
        NucleotideSequence seq2 = new NucleotideSequence("CAGTTCCCGAAACCTGCTAAGCTCTTAACTACTTCTGGGGTTTACGGCCCCCTTGAATGTAGTGATATGTGGTGC");
        //NucleotideSequence seq1 = new NucleotideSequence("A");
        //NucleotideSequence seq2 = new NucleotideSequence("AA");
        AffineGapAlignmentScoring<NucleotideSequence> scoring = AffineGapAlignmentScoring.getNucleotideBLASTScoring();
        Alignment<NucleotideSequence> alignment = AlignerCustom.alignAffineSemiLocalLeft0(scoring,
                seq1, seq2,
                0, seq1.size(),
                0, seq2.size(),
                false, true, NucleotideSequence.ALPHABET, new AlignerCustom.AffineMatrixCache());
        //if (AlignmentUtils.calculateScore(scoring, alignment.getSequence1Range().length(), alignment.getAbsoluteMutations()) != alignment.getScore()) {
        System.out.println(alignment.getScore());
        //System.out.println(AlignmentUtils.calculateScore(scoring, alignment.getSequence1Range().length(), alignment.getAbsoluteMutations()));
        System.out.println(alignment);
        int i = 0;
        //}
        System.out.println(alignment.getScore());
        System.out.println(alignment);
    }
    //
    //@Test
    //public void testSemiLocalRight0() throws Exception {
    //    NucleotideSequence seq1 = new NucleotideSequence("GTTAGAACACATACAC");
    //    NucleotideSequence seq2 = new NucleotideSequence("AATTAGACACACATAC");
    //    Alignment<NucleotideSequence> alignment = AlignerCustom.alignLinearSemiLocalRight0(
    //            LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2,
    //            0, seq1.size(),
    //            0, seq2.size(),
    //            true, false, NucleotideSequence.ALPHABET, new AlignerCustom.LinearMatrixCache());
    //    System.out.println(alignment.getScore());
    //    System.out.println(alignment);
    //}

    @Test
    public void testSemiLocalLeft0Random() throws Exception {
        for (AlignmentScoring<NucleotideSequence> scoring : Arrays.asList(
                AffineGapAlignmentScoring.getNucleotideBLASTScoring(),
                LinearGapAlignmentScoring.getNucleotideBLASTScoring()
        ))
            for (boolean boundSeq1 : Arrays.asList(false, true))
                for (boolean boundSeq2 : Arrays.asList(false, true))
                    testSemiLocalLeft0Random(boundSeq1, boundSeq2, scoring);
    }

    public static void testSemiLocalLeft0Random(boolean boundSeq1, boolean boundSeq2, AlignmentScoring<NucleotideSequence> scoring) throws Exception {
        int its = its(1000, 50000);
        AlignerCustom.LinearMatrixCache cacheLinear = new AlignerCustom.LinearMatrixCache();
        AlignerCustom.AffineMatrixCache cacheAffine = new AlignerCustom.AffineMatrixCache();
        NucleotideSequence seq1, seq2;
        int offset1, offset2, length1, length2;
        Alignment<NucleotideSequence> la;
        RandomDataGenerator random = new RandomDataGenerator(new Well19937c());
        long totalAlLength = 0;
        for (int i = 0; i < its; ++i) {
            seq1 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            seq2 = MutationsGenerator.generateMutations(seq1, MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(15)).mutate(seq1);
            offset1 = random.nextInt(0, seq1.size() - 10);
            offset2 = random.nextInt(0, seq2.size() - 10);
            length1 = random.nextInt((seq1.size() - offset1) / 2, seq1.size() - offset1);
            length2 = random.nextInt((seq2.size() - offset2) / 2, seq2.size() - offset2);

            if (scoring instanceof LinearGapAlignmentScoring)
                la = AlignerCustom.alignLinearSemiLocalLeft0((LinearGapAlignmentScoring<NucleotideSequence>) scoring,
                        seq1, seq2, offset1, length1, offset2, length2, boundSeq1, boundSeq2,
                        NucleotideSequence.ALPHABET, cacheLinear);
            else
                la = AlignerCustom.alignAffineSemiLocalLeft0((AffineGapAlignmentScoring<NucleotideSequence>) scoring,
                        seq1, seq2, offset1, length1, offset2, length2, boundSeq1, boundSeq2,
                        NucleotideSequence.ALPHABET, cacheAffine);

            if (boundSeq1 && boundSeq2)
                assertTrue(la.getSequence1Range().getFrom() == offset1 &&
                        la.getSequence2Range().getFrom() == offset2);
            else if (boundSeq1)
                assertTrue(la.getSequence1Range().getFrom() == offset1);
            else if (boundSeq2)
                assertTrue(la.getSequence2Range().getFrom() == offset2);
            else
                assertTrue(la.getSequence1Range().getFrom() == offset1 ||
                        la.getSequence2Range().getFrom() == offset2);

            assertTrue(la.getSequence1Range().getTo() <= offset1 + length1);
            assertTrue(la.getSequence2Range().getTo() <= offset2 + length2);

            //System.out.println(seq1.getRange(offset1, offset1 + length1));
            //System.out.println(seq2.getRange(offset2, offset2 + length2));
            AlignerTest.assertAlignment(la, seq2, scoring);
            totalAlLength += la.getSequence1Range().length() + la.getSequence2Range().length();
        }
        //System.out.println(totalAlLength / its / 2);
    }

    @Test
    public void testSemiLocalRight0Random() throws Exception {
        for (AlignmentScoring<NucleotideSequence> scoring : Arrays.asList(
                AffineGapAlignmentScoring.getNucleotideBLASTScoring(),
                LinearGapAlignmentScoring.getNucleotideBLASTScoring()
        ))
            for (boolean boundSeq1 : Arrays.asList(false, true))
                for (boolean boundSeq2 : Arrays.asList(false, true))
                    testSemiLocalRight0Random(boundSeq1, boundSeq2, scoring);
    }

    public static void testSemiLocalRight0Random(boolean boundSeq1, boolean boundSeq2, AlignmentScoring<NucleotideSequence> scoring) throws Exception {
        int its = its(1000, 50000);
        AlignerCustom.LinearMatrixCache cacheLinear = new AlignerCustom.LinearMatrixCache();
        AlignerCustom.AffineMatrixCache cacheAffine = new AlignerCustom.AffineMatrixCache();
        NucleotideSequence seq1, seq2;
        int offset1, offset2, length1, length2;
        Alignment<NucleotideSequence> la;
        RandomDataGenerator random = new RandomDataGenerator(new Well19937c());
        long totalAlLength = 0;
        for (int i = 0; i < its; ++i) {
            seq1 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            seq2 = MutationsGenerator.generateMutations(seq1, MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(15)).mutate(seq1);
            offset1 = random.nextInt(0, seq1.size() - 10);
            offset2 = random.nextInt(0, seq2.size() - 10);
            length1 = random.nextInt((seq1.size() - offset1) / 2, seq1.size() - offset1);
            length2 = random.nextInt((seq2.size() - offset2) / 2, seq2.size() - offset2);

            if (scoring instanceof LinearGapAlignmentScoring)
                la = AlignerCustom.alignLinearSemiLocalRight0((LinearGapAlignmentScoring<NucleotideSequence>) scoring,
                        seq1, seq2, offset1, length1, offset2, length2, boundSeq1, boundSeq2,
                        NucleotideSequence.ALPHABET, cacheLinear);
            else
                la = AlignerCustom.alignAffineSemiLocalRight0((AffineGapAlignmentScoring<NucleotideSequence>) scoring,
                        seq1, seq2, offset1, length1, offset2, length2, boundSeq1, boundSeq2,
                        NucleotideSequence.ALPHABET, cacheAffine);

            if (boundSeq1 && boundSeq2)
                assertTrue(la.getSequence1Range().getTo() == offset1 + length1 &&
                        la.getSequence2Range().getTo() == offset2 + length2);
            else if (boundSeq1 || boundSeq2)
                assertTrue((la.getSequence1Range().getTo() == offset1 + length1 && boundSeq1) ||
                        (la.getSequence2Range().getTo() == offset2 + length2 && boundSeq2));
            else
                assertTrue(la.getSequence1Range().getTo() == offset1 + length1 ||
                        la.getSequence2Range().getTo() == offset2 + length2);

            assertTrue(la.getSequence1Range().getFrom() >= offset1);
            assertTrue(la.getSequence2Range().getFrom() >= offset2);

            AlignerTest.assertAlignment(la, seq2, scoring);
            totalAlLength += la.getSequence1Range().length() + la.getSequence2Range().length();
        }
        //System.out.println(totalAlLength / its / 2);
    }
}