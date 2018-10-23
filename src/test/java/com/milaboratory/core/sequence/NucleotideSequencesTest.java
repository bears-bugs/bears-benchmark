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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class NucleotideSequencesTest {
    @Test
    public void test1() {
        NucleotideSequence sequence = new NucleotideSequence("ATTAGACATAGACA");
        assertEquals(sequence.toString(), "ATTAGACATAGACA");
        NucleotideSequence subSequence = sequence.getRange(0, sequence.size());
        assertEquals(subSequence.toString(), "ATTAGACATAGACA");
        assertEquals(subSequence.hashCode(), sequence.hashCode());
        assertEquals(subSequence, sequence);

        NucleotideSequence sequence1 = new NucleotideSequence("AGACATAGACA");
        NucleotideSequence subSequence1 = sequence.getRange(3, sequence.size());

        assertEquals(subSequence1.hashCode(), sequence1.hashCode());
        assertEquals(subSequence1, sequence1);
        assertEquals(NucleotideSequence.EMPTY, NucleotideSequence.EMPTY.getReverseComplement());
    }

    @Test
    public void testConcatenate1() throws Exception {
        NucleotideSequence s1 = new NucleotideSequence("ATTAGACA"),
                s2 = new NucleotideSequence("GACATATA");

        assertEquals(new NucleotideSequence("ATTAGACAGACATATA"), s1.concatenate(s2));
    }

    @Test
    public void testConcatenate2() throws Exception {
        NucleotideSequence s1 = new NucleotideSequence("ATTAGACA"),
                s2 = new NucleotideSequence("");

        assertEquals(new NucleotideSequence("ATTAGACA"), s1.concatenate(s2));
        assertEquals(new NucleotideSequence("ATTAGACA"), s2.concatenate(s1));
    }

    @Test
    public void testConcatenate3() throws Exception {
        NucleotideSequence s1 = new NucleotideSequence(""),
                s2 = new NucleotideSequence("");

        assertEquals(new NucleotideSequence(""), s1.concatenate(s2));
        assertEquals(new NucleotideSequence(""), s2.concatenate(s1));
    }

    @Test
    public void testRC1() {
        NucleotideSequence ns = new NucleotideSequence("atagagaattagataaggcagatacgatcgacgtgtactactagcta");
        NucleotideSequence rc = ns.getReverseComplement();
        NucleotideSequence rcrc = rc.getReverseComplement();
        assertEquals(rcrc, ns);
        assertEquals(rcrc.hashCode(), ns.hashCode());
        assertThat(rc, not(ns));
        assertThat(rc.hashCode(), not(ns.hashCode()));
    }

    public static final String forRC = "AGCTNRYSWKMBDHV";
    public static final String afterRC = "BDHVKMWSRYNAGCT";
    public static final String afterC = "TCGANYRSWMKVHDB";

    @Test
    public void testRC2Wildcards() {
        NucleotideSequence ns = new NucleotideSequence(forRC);
        NucleotideSequence rc = ns.getReverseComplement();
        NucleotideSequence rcrc = rc.getReverseComplement();
        assertEquals(new NucleotideSequence(afterRC), rc);
        assertEquals(rcrc, ns);
        assertEquals(rcrc.hashCode(), ns.hashCode());
        assertThat(rc, not(ns));
        assertThat(rc.hashCode(), not(ns.hashCode()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownSymbol1() throws Exception {
        new NucleotideSequence("ATTANQ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownSymbol2() throws Exception {
        new NucleotideSequence(new char[]{'a', 'n', 'q'});
    }
}
