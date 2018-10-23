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
import com.milaboratory.core.sequence.NucleotideSequence;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import static com.milaboratory.test.TestUtil.its;
import static com.milaboratory.test.TestUtil.randomSequence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BandedLinearAlignerTest {

    @Test
    public void testGlobal() throws Exception {
        NucleotideSequence sequence1 = new NucleotideSequence("ATTAGACA");
        NucleotideSequence sequence2 = new NucleotideSequence("ATTGACA");
        Alignment<NucleotideSequence> alignment = BandedLinearAligner.align(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), sequence1, sequence2, 0);
        AlignerTest.assertAlignment(alignment, sequence2);
        //Mutations.printAlignment(sequence1, mut);

        sequence1 = new NucleotideSequence("ATTGACA");
        sequence2 = new NucleotideSequence("ATTAGACA");
        alignment = BandedLinearAligner.align(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), sequence1, sequence2, 0);
        AlignerTest.assertAlignment(alignment, sequence2);
        //Mutations.printAlignment(sequence1, mut);

        sequence1 = new NucleotideSequence("ATTGACA");
        sequence2 = new NucleotideSequence("AGTAGCCA");
        alignment = BandedLinearAligner.align(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), sequence1, sequence2, 0);
        AlignerTest.assertAlignment(alignment, sequence2);
        //Mutations.printAlignment(sequence1, mut);

        sequence1 = new NucleotideSequence("ATTGACAATTGACA");
        sequence2 = new NucleotideSequence("ATTGACATTGAA");
        alignment = BandedLinearAligner.align(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), sequence1, sequence2, 0);
        AlignerTest.assertAlignment(alignment, sequence2);
        //Mutations.printAlignment(sequence1, mut);

        sequence1 = new NucleotideSequence("ATTGACAATTGACA");
        sequence2 = new NucleotideSequence("ATGAAATTGCACA");
        alignment = BandedLinearAligner.align(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), sequence1, sequence2, 1);
        AlignerTest.assertAlignment(alignment, sequence2);
        //Mutations.printAlignment(sequence1, mut);
    }

    @Test
    public void testRandomGlobal() throws Exception {
        int its = its(100, 10000);
        RandomDataGenerator random = new RandomDataGenerator(new Well19937c());
        for (int i = 0; i < its; ++i) {
            NucleotideSequence seq1, seq2;
            seq1 = randomSequence(NucleotideSequence.ALPHABET, random, 10, 100);
            seq2 = randomSequence(NucleotideSequence.ALPHABET, random, 10, 100);
            Alignment<NucleotideSequence> alignment = BandedLinearAligner.align(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                    seq1, seq2,
                    random.nextInt(0, Math.min(seq1.size(), seq2.size()) - 1));
            AlignerTest.assertAlignment(alignment, seq2);
        }
    }

    @Test
    public void test5() {
        NucleotideSequence seq1 = new NucleotideSequence("A"), seq2 = new NucleotideSequence("ATTA");
        Alignment<NucleotideSequence> alignment = BandedLinearAligner.align(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2, 1);
        AlignerTest.assertAlignment(alignment, seq2);
    }


    @Test
    public void testRandomLeft() throws Exception {
        int its = its(100, 10000);
        RandomDataGenerator random = new RandomDataGenerator(new Well19937c());
        for (int i = 0; i < its; ++i) {
            NucleotideSequence seq1, seq2;
            seq1 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            seq2 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            Alignment<NucleotideSequence> r = BandedLinearAligner.alignSemiLocalLeft(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                    seq1, seq2,
                    random.nextInt(0, Math.min(seq1.size(), seq2.size()) - 1),
                    -10);
            AlignerTest.assertAlignment(r, seq2);
        }
    }

    @Test
    public void testRandomRight() throws Exception {
        int its = its(100, 10000);
        RandomDataGenerator random = new RandomDataGenerator(new Well19937c());
        for (int i = 0; i < its; ++i) {
            NucleotideSequence seq1, seq2;
            seq1 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            seq2 = randomSequence(NucleotideSequence.ALPHABET, random, 80, 84);
            Alignment<NucleotideSequence> r =
                    BandedLinearAligner.alignSemiLocalRight(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                            seq1, seq2,
                            random.nextInt(0, Math.min(seq1.size(), seq2.size()) - 1),
                            -10);
            AlignerTest.assertAlignment(r, seq2);
        }
    }

    @Test
    public void testLocalLeft() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA");
        NucleotideSequence seq2 = new NucleotideSequence("ATTACGC");
        Alignment<NucleotideSequence> r = BandedLinearAligner.alignSemiLocalLeft(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2, 0, -10);
        AlignerTest.assertAlignment(r, seq2);
        assertEquals(4, r.getSequence1Range().getTo());
        assertEquals(4, r.getSequence2Range().getTo());

        seq1 = new NucleotideSequence("ATTAGACA");
        seq2 = new NucleotideSequence("ATTGACGC");
        r = BandedLinearAligner.alignSemiLocalLeft(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2, 1, -10);
        //Mutations.printAlignment(seq1.getSubSequence(0, r.sequence1Stop + 1), r.mutations);
        AlignerTest.assertAlignment(r, seq2);
        assertEquals(7, r.getSequence1Range().getTo());
        assertEquals(6, r.getSequence2Range().getTo());

        seq1 = new NucleotideSequence("ATAGACAGGGAGACA");
        seq2 = new NucleotideSequence("ATTAGACATTAGACA");
        r = BandedLinearAligner.alignSemiLocalLeft(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2, 1, -10);
        //Mutations.printAlignment(seq1.getSubSequence(0, r.sequence1Stop + 1), r.mutations);
        AlignerTest.assertAlignment(r, seq2);
        assertEquals(7, r.getSequence1Range().getTo());
        assertEquals(8, r.getSequence2Range().getTo());
    }

    @Test
    public void testLocalRight() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA");
        NucleotideSequence seq2 = new NucleotideSequence("GACA");
        Alignment<NucleotideSequence> r = BandedLinearAligner.alignSemiLocalRight(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2, 0, -10);
        //Mutations.printAlignment(seq1.getSubSequence(r.sequence1Stop, seq1.size()), r.mutations);
        AlignerTest.assertAlignment(r, seq2);
        assertEquals(4, r.getSequence1Range().getFrom());
        assertEquals(0, r.getSequence2Range().getFrom());

        seq1 = new NucleotideSequence("ATTAGACAATTAGACA");
        seq2 = new NucleotideSequence("GCGAATAGACA");
        r = BandedLinearAligner.alignSemiLocalRight(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2, 0, -10);
        //Mutations.printAlignment(seq1.getSubSequence(r.sequence1Stop, seq1.size()), Mutations.move(r.mutations, -r.sequence1Stop));
        AlignerTest.assertAlignment(r, seq2);
        assertEquals(7, r.getSequence1Range().getFrom());
        assertEquals(3, r.getSequence2Range().getFrom());
    }

    @Test
    public void testAddedRight1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("ATTTAGACA");
        Alignment<NucleotideSequence> la = BandedLinearAligner.alignRightAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2, 0, seq1.size(), 0, 0, seq2.size(), 0, 1);
        assertEquals(seq1.size(), la.getSequence1Range().getTo());
        assertEquals(seq2.size(), la.getSequence2Range().getTo());
        AlignerTest.assertAlignment(la, seq2);
    }

    @Test
    public void testAddedRight2() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("ATTTAGACAC");
        Alignment<NucleotideSequence> la = BandedLinearAligner.alignRightAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2, 0, seq1.size(), 1, 0, seq2.size(), 1, 1);
        assertEquals(seq1.size(), la.getSequence1Range().getTo());
        assertEquals(seq2.size() - 1, la.getSequence2Range().getTo());
        AlignerTest.assertAlignment(la, seq2);
    }

    @Test
    public void testAddedRight3() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("ATTTAGACAC");
        Alignment<NucleotideSequence> la = BandedLinearAligner.alignRightAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2, 0, seq1.size(), 1, 0, seq2.size(), 0, 1);
        assertEquals(seq1.size(), la.getSequence1Range().getTo());
        assertEquals(seq2.size(), la.getSequence2Range().getTo());
        AlignerTest.assertAlignment(la, seq2);
    }

    @Test
    public void testAddedRightRandom1() throws Exception {
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
            la = BandedLinearAligner.alignRightAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                    seq1, seq2, offset1, length1, added1, offset2, length2, added2, 1);

            assertTrue(la.getSequence1Range().getTo() == offset1 + length1 ||
                    la.getSequence2Range().getTo() == offset2 + length2);

            assertTrue(la.getSequence1Range().getTo() >= offset1 + length1 - added1);
            assertTrue(la.getSequence2Range().getTo() >= offset2 + length2 - added2);

            AlignerTest.assertAlignment(la, seq2);
        }
    }

    @Test
    public void testAddedLeft1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("ATTTAGACA");
        Alignment<NucleotideSequence> la = BandedLinearAligner.alignLeftAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2, 0, seq1.size(), 0, 0, seq2.size(), 0, 1);
        assertEquals(0, la.getSequence1Range().getFrom());
        assertEquals(0, la.getSequence2Range().getFrom());
        AlignerTest.assertAlignment(la, seq2);
    }

    @Test
    public void testAddedLeft2() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("CTTAGACA"),
                seq2 = new NucleotideSequence("CATTTAGACA");
        Alignment<NucleotideSequence> la = BandedLinearAligner.alignLeftAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2, 0, seq1.size(), 0, 0, seq2.size(), 1, 1);
        assertEquals(0, la.getSequence1Range().getFrom());
        assertEquals(0, la.getSequence2Range().getFrom());
        AlignerTest.assertAlignment(la, seq2);
    }

    @Test
    public void testAddedLeft3() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("CTTAGACA"),
                seq2 = new NucleotideSequence("CATTTAGACA");
        Alignment<NucleotideSequence> la = BandedLinearAligner.alignLeftAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2, 0, seq1.size(), 0, 0, seq2.size(), 3, 1);
        assertEquals(0, la.getSequence1Range().getFrom());
        assertEquals(2, la.getSequence2Range().getFrom());
        AlignerTest.assertAlignment(la, seq2);
    }

    @Test
    public void testAddedLeft4() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("TACAGACA");
        Alignment<NucleotideSequence> la = BandedLinearAligner.alignLeftAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2, 0, seq1.size(), 0, 0, seq2.size(), 2, 2);
        assertEquals(0, la.getSequence1Range().getFrom());
        assertEquals(1, la.getSequence2Range().getFrom());
    }

    @Test
    public void testAddedLeft5() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("ATTAGACA"),
                seq2 = new NucleotideSequence("ATTAGGACA");
        Alignment<NucleotideSequence> la = BandedLinearAligner.alignLeftAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                seq1, seq2, 0, seq1.size(), 0, 0, seq2.size(), 1, 1);
        assertEquals(0, la.getSequence1Range().getFrom());
        assertEquals(new Range(0, 9), la.getSequence2Range());
    }

    @Test
    public void testAddedLeftRandom1() throws Exception {
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
            la = BandedLinearAligner.alignLeftAdded(LinearGapAlignmentScoring.getNucleotideBLASTScoring(),
                    seq1, seq2, offset1, length1, added1, offset2, length2, added2, 1);

            assertTrue(la.getSequence1Range().getFrom() == offset1 ||
                    la.getSequence2Range().getFrom() == offset2);
            assertTrue(la.getSequence1Range().getFrom() <= offset1 + added1);
            assertTrue(la.getSequence2Range().getFrom() <= offset2 + added2);

            AlignerTest.assertAlignment(la, seq2);
        }
    }
}
