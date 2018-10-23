/*
 * Copyright 2017 MiLaboratory.com
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
package com.milaboratory.core.sequence.provider;

import com.milaboratory.core.Range;
import com.milaboratory.core.sequence.NucleotideSequence;
import org.junit.Test;

import static org.junit.Assert.*;

public class SequenceProviderUtilsTest {
    @Test
    public void subProviderTest() throws Exception {
        final NucleotideSequence sequence = new NucleotideSequence("ATTAGACAGCTGCATAGTGCTCGCTCGGCGATGACTGCGCGGCGCGC" +
                "ATGGATCGACTAGCTCTATCGAGCTTCTCTGAAGCGTATCGAT");
        SequenceProvider<NucleotideSequence> seq = SequenceProviderUtils.fromSequence(sequence);
        for (Range baseRange : new Range[]{new Range(23, 42), new Range(23, 42).reverse()}) {
            NucleotideSequence sseq = sequence.getRange(baseRange);
            SequenceProvider<NucleotideSequence> sseqp = SequenceProviderUtils.subProvider(seq, baseRange);
            assertEquals(sseq.getRange(new Range(1, 7)), sseqp.getRegion(new Range(1, 7)));
            assertEquals(sseq.getRange(new Range(1, 7).reverse()), sseqp.getRegion(new Range(1, 7).reverse()));
            assertEquals(sseq.getRange(new Range(3, 9)), sseqp.getRegion(new Range(3, 9)));
            assertEquals(sseq.getRange(new Range(3, 9).reverse()), sseqp.getRegion(new Range(3, 9).reverse()));
            assertEquals(baseRange.length(), sseqp.size());
        }
    }
}