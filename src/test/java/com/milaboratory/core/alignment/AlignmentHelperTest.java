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

import com.milaboratory.core.sequence.NucleotideSequence;
import org.junit.Test;

public class AlignmentHelperTest {
    @Test
    public void test1() throws Exception {
        NucleotideSequence seq1 = new NucleotideSequence("GAGGTGCAGCTGGTGGAGTCTGGGGGAGGCTTGGTACAGCCTGGGGGGTCCCTGAGACTCTCCTGTGCAGCCTCTGGATTCACCTTCAGTAGCTATAGCATGAACTGGGTCCGCCAGGCTCCAGGGAAGGGGCTGGAGTGGGTTTCATACATTAGTAGTAGTAGTAGTACCATATACTACGCAGACTCTGTGAAGGGCCGATTCACCATCTCCAGAGACAATGCCAAGAACTCACTGTATCTGCAAATGAACAGCCTGAGAGACGAGGACACGGCTGTGTATTACTGTGC");
        NucleotideSequence seq2 = new NucleotideSequence("GAGGTGCAGCTGGTGGAGTCTGGGGGAGGCCTGGTCAAGCCTGGGGGGTCCATGAGACACTCCTGTGCAGCCTCTGGATTCCCCTTCAGTACTTATAGCATGAACTGGGTCCGCCAGGCTCCAGGGAAGGGGCTGGAGTGGGTCTCATCCATTAGTAGTGGTAGTAGTTACATATATTACGCAGACTCCGTGAAGGGCCGATTCACCATCTCCAGAGACAACGCCAAGAACTCACTGTATCTGCAAATGAACAGCCTGAGAGCCGAGGACACGGCTGTGTATTACTGTGC");
        Alignment<NucleotideSequence> alignemnt = Aligner.alignLocal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq1, seq2);
        for (AlignmentHelper alignmentHelper : alignemnt.getAlignmentHelper().split(30, 5)) {
            System.out.println(alignmentHelper);
            System.out.println();
        }
    }
}