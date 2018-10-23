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

import com.milaboratory.core.mutations.Mutation;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.sequence.NucleotideSequence;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class AlignmentUtilsTest {
    @Test
    public void testScore() {
        NucleotideSequence s1 = new NucleotideSequence("CGTA");

        int[] mut = new int[2];
        mut[0] = Mutation.createInsertion(1, 1);
        mut[1] = Mutation.createSubstitution(3, 0, 1);
        float score = AlignmentUtils.calculateScore(s1,
                new Mutations(NucleotideSequence.ALPHABET, mut),
                LinearGapAlignmentScoring.getNucleotideBLASTScoring());

        //3 matches(15) - 1 mismatch(-4) - 1 insertion(-4);
        Assert.assertEquals(6, (int) score);
    }

    @Test
    public void testAffineScore() throws Exception {
        AffineGapAlignmentScoring<NucleotideSequence> scoring =
                new AffineGapAlignmentScoring<>(NucleotideSequence.ALPHABET, 10, -5, -11, -7);

        NucleotideSequence s1 = new NucleotideSequence("atcgcgatcgactgcatgca");
        NucleotideSequence s2 = new NucleotideSequence("atcgcgatcgactgactgcatgca");

        Alignment<NucleotideSequence> al = BandedAffineAligner.align(scoring, s1, s2, 0, s1.size(), 0, s2.size(), 0);
        Assert.assertEquals(168, (int) al.score);

        int score = AlignmentUtils.calculateScore(s1, al.mutations, scoring);
        Assert.assertEquals((int) al.score, score);

        al = BandedAffineAligner.align(scoring, s2, s1, 0, s2.size(), 0, s1.size(), 0);
        score = AlignmentUtils.calculateScore(s2, al.mutations, scoring);
        Assert.assertEquals((int) al.score, score);
    }
}