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

import com.milaboratory.core.Range;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.sequence.NucleotideSequence;
import org.junit.Assert;
import org.junit.Test;

import static com.milaboratory.core.mutations.Mutation.*;
import static com.milaboratory.core.sequence.NucleotideAlphabet.*;

public class AlignmentIteratorTest {
    @Test
    public void test1() throws Exception {
        NucleotideSequence seq0 = new NucleotideSequence("ATTAGACA");

        Alignment<NucleotideSequence> al;
        NucleotideSequence seq1 = new NucleotideSequence("AATTGGGAATT");
        al = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq1);
        System.out.println(al.getAlignmentHelper());
        System.out.println(al.getAbsoluteMutations().encode());

        /*
            0 -ATT-AGACA-- 8
               |||  || |
            0 AATTGGGA-ATT 10

            I0A I3G SA3G DC6 I8T I8T
         */

        AlignmentIteratorTestCase case1 = new AlignmentIteratorTestCase("I0A I3G SA3G DC6 I8T I8T",
                new int[]{0, 0, 1, 2, 3, 3, 4, 5, 6, 7, 8, 8},
                new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10},
                new int[]{0, 1, 1, 1, 1, 2, 3, 3, 3, 4, 4, 5},
                new int[]{createInsertion(0, A),
                        NON_MUTATION, NON_MUTATION, NON_MUTATION,
                        createInsertion(3, G), createSubstitution(3, A, G),
                        NON_MUTATION, NON_MUTATION,
                        createDeletion(6, C),
                        NON_MUTATION,
                        createInsertion(8, T),
                        createInsertion(8, T)
                });

        performTest(seq0, case1);

        //NucleotideSequence seq2 = new NucleotideSequence("AATTGGGAA");
        //al = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq2);
        //System.out.println(al.getAlignmentHelper());
        //System.out.println(al.getAbsoluteMutations().encode());

        /*
            0 -ATT-AGACA 7
               |||  || |
            0 AATTGGGA-A 10

            I0A I3G SA3G DC6
         */

        AlignmentIteratorTestCase case2F = new AlignmentIteratorTestCase("I0A I3G SA3G DC6",
                new int[]{0, 0, 1, 2, 3, 3, 4, 5, 6, 7},
                new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 8},
                new int[]{0, 1, 1, 1, 1, 2, 3, 3, 3, 4},
                new int[]{createInsertion(0, A),
                        NON_MUTATION, NON_MUTATION, NON_MUTATION,
                        createInsertion(3, G), createSubstitution(3, A, G),
                        NON_MUTATION, NON_MUTATION,
                        createDeletion(6, C),
                        NON_MUTATION
                });

        performTest(seq0, case2F);

        //NucleotideSequence seq3 = new NucleotideSequence("ATTGGGAA");
        //al = Aligner.alignGlobal(LinearGapAlignmentScoring.getNucleotideBLASTScoring(), seq0, seq3);
        //System.out.println(al.getAlignmentHelper());
        //System.out.println(al.getAbsoluteMutations().encode());

        /*
            0 ATT-AGACA 7
              |||  || |
            0 ATTGGGA-A 8

            I3G SA3G DC6
         */

        AlignmentIteratorTestCase case3 = new AlignmentIteratorTestCase("I3G SA3G DC6",
                new int[]{0, 1, 2, 3, 3, 4, 5, 6, 7},
                new int[]{0, 1, 2, 3, 4, 5, 6, 7, 7},
                new int[]{0, 0, 0, 0, 1, 2, 2, 2, 3},
                new int[]{NON_MUTATION, NON_MUTATION, NON_MUTATION,
                        createInsertion(3, G), createSubstitution(3, A, G),
                        NON_MUTATION, NON_MUTATION,
                        createDeletion(6, C),
                        NON_MUTATION
                });

        performTest(seq0, case3);
    }

    public static void performTest(NucleotideSequence seq1, AlignmentIteratorTestCase testCase) {
        Assert.assertTrue(testCase.alignment.isCompatibleWith(seq1));

        // Testing forward iterator
        AlignmentIteratorForward<NucleotideSequence> itf = new AlignmentIteratorForward<>(testCase.alignment,
                new Range(0, seq1.size()));

        int i = 0;
        while (itf.advance()) {
            Assert.assertEquals(testCase.seq1Position[i], itf.getSeq1Position());
            Assert.assertEquals(testCase.seq2Position[i], itf.getSeq2Position());
            Assert.assertEquals(testCase.mutationsPointer[i], itf.getMutationsPointer());
            Assert.assertEquals(testCase.currentMutation[i], itf.getCurrentMutation());
            ++i;
        }
        Assert.assertEquals(testCase.currentMutation.length, i);

        i = testCase.currentMutation.length - 1;
        AlignmentIteratorReverse<NucleotideSequence> itr = new AlignmentIteratorReverse<>(testCase.alignment,
                new Range(0, seq1.size()), testCase.seq2Position[i] + 1);

        while (itr.advance()) {
            Assert.assertEquals(testCase.seq1Position[i], itr.getSeq1Position());
            Assert.assertEquals(testCase.seq2Position[i], itr.getSeq2Position());
            Assert.assertEquals(testCase.mutationsPointer[i], itr.getMutationsPointer());
            Assert.assertEquals(testCase.currentMutation[i], itr.getCurrentMutation());
            --i;
        }
        Assert.assertEquals(-1, i);
    }

    static final class AlignmentIteratorTestCase {
        final Mutations<NucleotideSequence> alignment;
        final int[] seq1Position;
        final int[] seq2Position;
        final int[] mutationsPointer;
        final int[] currentMutation;

        public AlignmentIteratorTestCase(String alignment,
                                         int[] seq1Position, int[] seq2Position,
                                         int[] mutationsPointer, int[] currentMutation) {
            this.alignment = Mutations.decode(alignment, NucleotideSequence.ALPHABET);
            this.seq1Position = seq1Position;
            this.seq2Position = seq2Position;
            this.mutationsPointer = mutationsPointer;
            this.currentMutation = currentMutation;
        }
    }
}