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
import com.milaboratory.core.sequence.SequenceQuality;
import org.junit.Test;

/**
 * Created by dbolotin on 03/09/15.
 */
public class MultiAlignmentHelperTest {
    @Test
    public void test1() throws Exception {
        SequenceQuality seq0qual = new SequenceQuality("GIHGIIHIHFHGHGIIIGKHK");
        NucleotideSequence seq0 = new NucleotideSequence("GATACATTAGACACAGATACA");
        NucleotideSequence seq1 = new NucleotideSequence("AGACACATATACACAG");
        NucleotideSequence seq2 = new NucleotideSequence("GATACGATACATTAGAGACCACAGATACA");

        Alignment<NucleotideSequence>[] alignments = new Alignment[]{
                Aligner.alignLocalAffine(AffineGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq1),
                Aligner.alignGlobalAffine(AffineGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq1),
                Aligner.alignLocalAffine(AffineGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq2),
                Aligner.alignGlobalAffine(AffineGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq2),
        };

        for (Alignment<NucleotideSequence> alignment : alignments) {
            System.out.println(alignment.getAlignmentHelper());
            System.out.println();
        }

        MultiAlignmentHelper helper = MultiAlignmentHelper.build(MultiAlignmentHelper.DEFAULT_SETTINGS, new Range(0, seq0.size()),
                alignments);
        helper.addSubjectQuality("Quality", seq0qual);
        helper.setSubjectLeftTitle("Subject");
        for (int i = 0; i < 4; i++)
            helper.setQueryLeftTitle(i, "Query" + i);

        System.out.println(helper);

        for (MultiAlignmentHelper spl : helper.split(5)) {
            System.out.println();
            System.out.println(spl);
        }

        System.out.println();
        System.out.println(MultiAlignmentHelper.build(MultiAlignmentHelper.DOT_MATCH_SETTINGS, new Range(0, seq0.size()),
                alignments));

        System.out.println();
        System.out.println(MultiAlignmentHelper.build(MultiAlignmentHelper.DOT_MATCH_SETTINGS, new Range(0, seq0.size()),
                seq0,
                new Alignment[0]).addSubjectQuality("", seq0qual));
    }
}