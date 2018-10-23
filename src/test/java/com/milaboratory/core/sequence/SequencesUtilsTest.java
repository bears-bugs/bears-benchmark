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
package com.milaboratory.core.sequence;

import com.milaboratory.core.motif.Motif;
import com.milaboratory.test.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static com.milaboratory.core.sequence.SequencesUtils.*;
import static org.junit.Assert.*;

public class SequencesUtilsTest {
    @Test
    public void testCat() throws Exception {
        String seq1s = "gttacagc",
                seq2s = "gctatacgatgc";
        NucleotideSequence seq1 = new NucleotideSequence(seq1s),
                seq2 = new NucleotideSequence(seq2s),
                catAssert = new NucleotideSequence(seq1s + seq2s);

        assertEquals(catAssert, concatenate(seq1, seq2));
        assertEquals(catAssert.hashCode(), concatenate(seq1, seq2).hashCode()); //Just in case
    }

    @Test
    public void testBelongs1() throws Exception {
        assertTrue(belongsToAlphabet(NucleotideSequence.ALPHABET, "ATTAGagacca"));
        assertFalse(belongsToAlphabet(NucleotideSequence.ALPHABET, "ATTAGagaccaQ"));
    }

    @Test
    public void testBelongs2() throws Exception {
        assertTrue(belongsToAlphabet(AminoAcidSequence.ALPHABET, "CAsSL*~GAT"));
        assertTrue(belongsToAlphabet(AminoAcidSequence.ALPHABET, "CAsSL*_GAT"));
        assertFalse(belongsToAlphabet(AminoAcidSequence.ALPHABET, "CAsSL*_XAOT"));
    }

    @Test
    public void testSet1() throws Exception {
        Set<Alphabet<?>> set = possibleAlphabets("ATTAGagacca");
        assertTrue(set.contains(NucleotideSequence.ALPHABET));
    }

    @Test
    public void testWildcardToRandomBasics1() throws Exception {
        for (int i = 0; i < 100; i++) {
            NucleotideSequence seq1 = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 40, 100);
            NucleotideSequence seq2 = SequencesUtils.wildcardsToRandomBasic(seq1, 132);
            NucleotideSequence seq3 = SequencesUtils.wildcardsToRandomBasic(seq1, 132);
            NucleotideSequence seq4 = SequencesUtils.wildcardsToRandomBasic(seq1, 135);
            Motif<NucleotideSequence> motif1 = seq1.toMotif();
            assertTrue(motif1.matches(seq2, 0));
            assertEquals(seq2, seq3);
            assertTrue(motif1.matches(seq4, 0));
        }
    }

    @Test
    public void testSet2() throws Exception {
        Set<Alphabet<?>> set = possibleAlphabets("CAsSL*_GAT");
        assertTrue(set.contains(AminoAcidSequence.ALPHABET));
        assertFalse(set.contains(NucleotideSequence.ALPHABET));
    }
}
