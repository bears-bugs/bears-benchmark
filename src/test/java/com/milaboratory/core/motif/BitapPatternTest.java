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
package com.milaboratory.core.motif;

import com.milaboratory.core.alignment.Aligner;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.alignment.LinearGapAlignmentScoring;
import com.milaboratory.core.mutations.MutationType;
import com.milaboratory.core.mutations.generator.UniformMutationsGenerator;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.SequencesUtils;
import com.milaboratory.test.TestUtil;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BitapPatternTest {
    @Test
    public void testExact1() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("ATTAGACA").toMotif();
        NucleotideSequence seq = new NucleotideSequence("ACTGCGATAAATTAGACAGTACGTA");
        assertEquals(10, motif.getBitapPattern().exactSearch(seq));
    }

    @Test
    public void testExact2() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("ATTRGACA").toMotif();
        NucleotideSequence seq = new NucleotideSequence("ACTGCGATAAATTAGACAGTACGTA");
        assertEquals(10, motif.getBitapPattern().exactSearch(seq));
        seq = new NucleotideSequence("ACTGCGATAAATTGGACAGTACGTA");
        assertEquals(10, motif.getBitapPattern().exactSearch(seq));
    }

    @Test
    public void testExactWildcard2() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("ATTRGACA").toMotif();
        NucleotideSequence seq = new NucleotideSequence("ACTGCGATAAATTRGACAGTACGTA");
        assertEquals(10, motif.getBitapPattern().exactSearch(seq));
        seq = new NucleotideSequence("ACTGCGATAAATKMGACAGTACGTA");
        assertEquals(10, motif.getBitapPattern().exactSearch(seq));
        seq = new NucleotideSequence("ACTGCGATAAATKYGACAGTACGTA");
        assertEquals(-1, motif.getBitapPattern().exactSearch(seq));
    }

    @Test
    public void testExactWildcard3() throws Exception {
        for (int i = 0; i < 1000; i++) {
            NucleotideSequence seq = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 20, 30, false);
            NucleotideSequence seqR = SequencesUtils.wildcardsToRandomBasic(seq, 123);
            Motif<NucleotideSequence> motif = seq.toMotif();
            Motif<NucleotideSequence> motifR = seqR.toMotif();
            assertTrue(motif.matches(seq, 0));
            assertTrue(motif.matches(seqR, 0));
            assertTrue(motifR.matches(seq, 0));
            assertTrue(motifR.matches(seqR, 0));
            BitapPattern bp = motif.getBitapPattern();
            BitapPattern bpR = motifR.getBitapPattern();
            assertEquals(0, bp.exactSearch(seq));
            assertEquals(0, bp.exactSearch(seqR));
            assertEquals(0, bpR.exactSearch(seq));
            assertEquals(0, bpR.exactSearch(seqR));
        }
    }

    @Test
    public void ttt() throws Exception {
        NucleotideSequence seq0 = new NucleotideSequence("ATTWCCGACA");
        Motif<NucleotideSequence> motif = seq0.toMotif();
        NucleotideSequence seq;
        BitapMatcher bitapMatcher;

        //                                                       |27
        //                            |0        |10       |20       |30       |40       |50
        // Exact                                          --------
        seq = new NucleotideSequence("ATTAATGATCGAAGCTACGAATTTGACATCAGCTAGCAGCGATCAGCTGAGCTA".replace(" ", ""));
        // ATTTGACA
        // ATTWGACA

        // ATTTGACAT
        // ATTWGACA-

        // ATTTGACAT
        // ATTWGACA-

        bitapMatcher = new BitapMatcherFilter(motif.getBitapPattern().substitutionAndIndelMatcherLast(1, seq));
        Alignment<NucleotideSequence> alignment = Aligner.alignLocal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq);
        System.out.println(alignment.getAlignmentHelper());
        System.out.println(alignment.getAbsoluteMutations());
        System.out.println(alignment.convertToSeq2Position(6));
        System.out.println(alignment.convertToSeq2Position(5));

        int pos;
        while ((pos = bitapMatcher.findNext()) > 0){
            System.out.println(pos);
            System.out.println(bitapMatcher.getNumberOfErrors());
            System.out.println();
        }
    }

    @Test
    public void testMismatchIndel1() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("ATTAGACA").toMotif();
        NucleotideSequence seq;
        BitapMatcher bitapMatcher;

        // Exact
        seq = new NucleotideSequence("ACTGCGATAAATTAGACAGTACGTA");
        bitapMatcher = motif.getBitapPattern().substitutionAndIndelMatcherLast(1, seq);
        boolean t = false;
        int pos;
        while ((pos = bitapMatcher.findNext()) > 0)
            if (bitapMatcher.getNumberOfErrors() == 0) {
                t = true;
                break;
            }
        assertTrue(t);
        assertEquals(17, pos);

        // Deletion
        seq = new NucleotideSequence("ACTGCGATAAATAGACAGTACGTA");
        bitapMatcher = motif.getBitapPattern().substitutionAndIndelMatcherLast(1, seq);
        assertEquals(16, bitapMatcher.findNext());
        assertEquals(1, bitapMatcher.getNumberOfErrors());
        assertEquals(-1, bitapMatcher.findNext());

        // Insertion
        seq = new NucleotideSequence("ACTGCGATAAATTATGACAGTACGTA");
        bitapMatcher = motif.getBitapPattern().substitutionAndIndelMatcherLast(1, seq);
        assertEquals(18, bitapMatcher.findNext());
        assertEquals(1, bitapMatcher.getNumberOfErrors());
        assertEquals(-1, bitapMatcher.findNext());

        // Mismatch
        seq = new NucleotideSequence("ACTGCGATAAATTACACAGTACGTA");
        bitapMatcher = motif.getBitapPattern().substitutionAndIndelMatcherLast(1, seq);
        assertEquals(17, bitapMatcher.findNext());
        assertEquals(1, bitapMatcher.getNumberOfErrors());
        assertEquals(-1, bitapMatcher.findNext());
    }

    @Test
    public void testMismatchIndel2() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("ATTAGACA").toMotif();
        NucleotideSequence seq;
        BitapMatcher bitapMatcher;

        // Exact
        seq = new NucleotideSequence("ACTGCGATAAATTAGACAGTACGTA");
        bitapMatcher = motif.getBitapPattern().substitutionAndIndelMatcherFirst(1, seq);
        boolean t = false;
        int pos;
        while ((pos = bitapMatcher.findNext()) > 0)
            if (bitapMatcher.getNumberOfErrors() == 0) {
                t = true;
                break;
            }
        assertTrue(t);
        assertEquals(10, pos);

        // Deletion
        seq = new NucleotideSequence("ACTGCGATAAATAGACAGTACGTA");
        bitapMatcher = motif.getBitapPattern().substitutionAndIndelMatcherFirst(1, seq);
        assertEquals(10, bitapMatcher.findNext());
        assertEquals(1, bitapMatcher.getNumberOfErrors());
        assertEquals(9, bitapMatcher.findNext());
        assertEquals(1, bitapMatcher.getNumberOfErrors());
        assertEquals(-1, bitapMatcher.findNext());

        // Insertion
        seq = new NucleotideSequence("ACTGCGATAAATTATGACAGTACGTA");
        bitapMatcher = motif.getBitapPattern().substitutionAndIndelMatcherFirst(1, seq);
        assertEquals(10, bitapMatcher.findNext());
        assertEquals(1, bitapMatcher.getNumberOfErrors());
        assertEquals(-1, bitapMatcher.findNext());

        // Mismatch
        seq = new NucleotideSequence("ACTGCGATAAATTACACAGTACGTA");
        bitapMatcher = motif.getBitapPattern().substitutionAndIndelMatcherFirst(1, seq);
        assertEquals(10, bitapMatcher.findNext());
        assertEquals(1, bitapMatcher.getNumberOfErrors());
        assertEquals(-1, bitapMatcher.findNext());
    }

    @Test
    public void testMismatch1() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("ATTRGACA").toMotif();
        NucleotideSequence seq = new NucleotideSequence("ACTGCGATAAATTAGACAGTACGTA");
        BitapMatcher matcher = motif.getBitapPattern().substitutionOnlyMatcherFirst(1, seq);
        Assert.assertEquals(10, matcher.findNext());
        Assert.assertEquals(0, matcher.getNumberOfErrors());
    }

    @Test
    public void testMismatch2() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("ATTRGACA").toMotif();
        NucleotideSequence seq = new NucleotideSequence("ACTGCGATAAATCAGACAGTACGTA");
        BitapMatcher matcher = motif.getBitapPattern().substitutionOnlyMatcherFirst(1, seq);
        Assert.assertEquals(10, matcher.findNext());
        Assert.assertEquals(1, matcher.getNumberOfErrors());
    }

    @Test
    public void testRandomMM1() throws Exception {
        RandomGenerator rg = new Well19937c();
        long seed = rg.nextLong();
        rg = new Well19937c(seed);
        int its = TestUtil.its(1000, 100000);

        out:
        for (int i = 0; i < its; ++i) {
            NucleotideSequence seq = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 5, 60);

            NucleotideSequence seqM = seq;
            int mms = 1 + rg.nextInt(Math.min(10, seq.size()));
            for (int j = 0; j < mms; ++j)
                seqM = UniformMutationsGenerator.createUniformMutationAsObject(seqM, rg, MutationType.Substitution).mutate(seqM);

            int realMMs = SequencesUtils.mismatchCount(seq, 0, seqM, 0, seqM.size());

            NucleotideSequence seqLeft = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 0, 40);
            NucleotideSequence seqRight = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 0, 40);
            NucleotideSequence fullSeq = SequencesUtils.concatenate(seqLeft, seqM, seqRight);

            Motif<NucleotideSequence> motif = new Motif<>(seq);
            BitapPattern bitapPattern = motif.getBitapPattern();

            // Not filtered

            BitapMatcher bitapMatcher = bitapPattern.substitutionOnlyMatcherFirst(mms, fullSeq);

            boolean found = false;

            int pos;
            while ((pos = bitapMatcher.findNext()) >= 0) {
                if (pos == seqLeft.size()) {
                    found = true;
                    assertEquals(realMMs, bitapMatcher.getNumberOfErrors());
                }
                assertTrue("On iteration = " + i + " with seed " + seed, SequencesUtils.mismatchCount(fullSeq, pos, seq, 0, seq.size()) <= mms);
            }

            assertTrue("On iteration = " + i + " with seed " + seed, found);
        }
    }

    @Test
    public void testRandomMM2() throws Exception {
        RandomGenerator rg = new Well19937c();
        long seed = rg.nextLong();
        rg = new Well19937c(seed);
        int its = TestUtil.its(1000, 100000);

        int e = 0;

        out:
        for (int i = 0; i < its; ++i) {
            NucleotideSequence seq = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 10, 60);

            NucleotideSequence seqM = seq;
            int mms = 1 + rg.nextInt(3);
            for (int j = 0; j < mms; ++j)
                seqM = UniformMutationsGenerator.createUniformMutationAsObject(seqM, rg, MutationType.Substitution).mutate(seqM);

            int realMMs = SequencesUtils.mismatchCount(seq, 0, seqM, 0, seqM.size());

            NucleotideSequence seqLeft = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 0, 40);
            NucleotideSequence seqRight = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 0, 40);
            NucleotideSequence fullSeq = SequencesUtils.concatenate(seqLeft, seqM, seqRight);

            Motif<NucleotideSequence> motif = new Motif<>(seq);
            BitapPattern bitapPattern = motif.getBitapPattern();

            // Filtered

            BitapMatcherFilter bitapMatcher = new BitapMatcherFilter(bitapPattern.substitutionOnlyMatcherFirst(mms, fullSeq));

            boolean found = false;

            int pos;
            while ((pos = bitapMatcher.findNext()) >= 0) {
                if (pos == seqLeft.size()) {
                    found = true;
                    assertEquals(realMMs, bitapMatcher.getNumberOfErrors());
                }
                assertTrue("On iteration = " + i + " with seed " + seed, SequencesUtils.mismatchCount(fullSeq, pos, seq, 0, seq.size()) <= mms);
            }

            if (!found)
                ++e;
        }

        assertTrue(e <= Math.max(5E-5 * its, 1.0));
    }

    @Test
    public void testRandomMMIndelLast1() throws Exception {
        RandomGenerator rg = new Well19937c();
        long seed = rg.nextLong();
        rg = new Well19937c(seed);
        int its = TestUtil.its(1000, 100000);

        out:
        for (int i = 0; i < its; ++i) {
            NucleotideSequence seq = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 5, 60);

            NucleotideSequence seqM = seq;
            int muts = 1 + rg.nextInt(Math.min(10, seq.size()));
            for (int j = 0; j < muts; ++j)
                seqM = UniformMutationsGenerator.createUniformMutationAsObject(seqM, rg).mutate(seqM);

            NucleotideSequence seqLeft = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 0, 40);
            NucleotideSequence seqRight = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 0, 40);
            NucleotideSequence fullSeq = SequencesUtils.concatenate(seqLeft, seqM, seqRight);

            Motif<NucleotideSequence> motif = new Motif<>(seq);
            BitapPattern bitapPattern = motif.getBitapPattern();
            BitapMatcher bitapMatcher = bitapPattern.substitutionAndIndelMatcherLast(muts, fullSeq);

            boolean found = false;

            int pos;
            while ((pos = bitapMatcher.findNext()) >= 0) {
                if (pos == seqLeft.size() + seqM.size() - 1)
                    found = true;
            }

            assertTrue("On iteration = " + i + " with seed " + seed, found);
        }
    }

    @Test
    public void testRandomMMIndelFirst1() throws Exception {
        RandomGenerator rg = new Well19937c();
        long seed = rg.nextLong();
        rg = new Well19937c(seed);
        int its = TestUtil.its(1000, 100000);

        out:
        for (int i = 0; i < its; ++i) {
            NucleotideSequence seq = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 5, 60);

            NucleotideSequence seqM = seq;
            int muts = 1 + rg.nextInt(Math.min(10, seq.size()));
            for (int j = 0; j < muts; ++j)
                seqM = UniformMutationsGenerator.createUniformMutationAsObject(seqM, rg).mutate(seqM);

            NucleotideSequence seqLeft = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 0, 40);
            NucleotideSequence seqRight = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 0, 40);
            NucleotideSequence fullSeq = SequencesUtils.concatenate(seqLeft, seqM, seqRight);

            Motif<NucleotideSequence> motif = new Motif<>(seq);
            BitapPattern bitapPattern = motif.getBitapPattern();
            BitapMatcher bitapMatcher = bitapPattern.substitutionAndIndelMatcherFirst(muts, fullSeq);

            boolean found = false;

            int pos;
            while ((pos = bitapMatcher.findNext()) >= 0) {
                if (pos == seqLeft.size())
                    found = true;
            }

            assertTrue("On iteration = " + i + " with seed " + seed, found);
        }
    }
}