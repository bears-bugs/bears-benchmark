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

import com.milaboratory.core.io.util.IOTestUtil;
import com.milaboratory.core.sequence.AminoAcidSequence;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.SequenceBuilder;
import com.milaboratory.core.sequence.Wildcard;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MotifTest {
    @Test
    public void test1() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("ATTAGACA").toMotif();
        NucleotideSequence seq = new NucleotideSequence("ACTGCGATAAATTAGACAGTACGTA");
        assertFalse(motif.matches(seq, 9));
        assertTrue(motif.matches(seq, 10));
        assertFalse(motif.matches(seq, 11));
    }

    @Test
    public void test2() throws Exception {
        Motif<NucleotideSequence> motif = new NucleotideSequence("NNNNNNNN").toMotif();
        NucleotideSequence seq = new NucleotideSequence("ACTGCGATAAATTAGACAGTACGTA");
        for (int i = 0; i < seq.size() - motif.size(); ++i)
            assertTrue(motif.matches(seq, i));
    }

    @Test
    public void test3() throws Exception {
        Motif<AminoAcidSequence> motif = new AminoAcidSequence("CASSLAP").toMotif();
        AminoAcidSequence seq = new AminoAcidSequence("LAPGATCASSLAPGAT");
        assertFalse(motif.matches(seq, 5));
        assertTrue(motif.matches(seq, 6));
        assertFalse(motif.matches(seq, 7));
        assertTrue(new Motif<>(seq).matches(seq, 0));
    }

    @Test
    public void testRandom1() throws Exception {
        RandomGenerator rg = new Well19937c();
        for (Wildcard wildcard : NucleotideSequence.ALPHABET.getAllWildcards()) {
            int seqLength = 20 + rg.nextInt(100);
            int motifSize = rg.nextInt(20);
            StringBuilder builder = new StringBuilder(motifSize);
            for (int i = 0; i < motifSize; ++i)
                builder.append(wildcard.getSymbol());
            Motif<NucleotideSequence> motif = new NucleotideSequence(builder.toString()).toMotif();
            SequenceBuilder<NucleotideSequence> seqBuilder = NucleotideSequence.ALPHABET.createBuilder().ensureCapacity(seqLength);
            for (int i = 0; i < seqLength; ++i)
                seqBuilder.append(wildcard.getUniformlyDistributedBasicCode(rg.nextLong()));
            NucleotideSequence seq = seqBuilder.createAndDestroy();
            for (int i = 0; i < seq.size() - motif.size(); ++i)
                assertTrue(motif.matches(seq, i));
        }
    }

    @Test
    public void test4() throws Exception {
        Motif<AminoAcidSequence> se = new AminoAcidSequence("CASSLAP").toMotif();
        IOTestUtil.assertJavaSerialization(se);
    }
}