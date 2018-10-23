package com.milaboratory.core.alignment;

import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.test.TestUtil;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.milaboratory.test.TestUtil.randomSequence;

/**
 *
 */
public class BandedAlignerTest {
    @Test
    public void testGlobalRandomCheckNucleotideScoring() {
        NucleotideMutationModel model = MutationModels.getEmpiricalNucleotideMutationModel()
                .multiplyProbabilities(15);

        Well19937c rand = new Well19937c(System.nanoTime());
        for (AlignmentScoring<NucleotideSequence> sc : Arrays.asList(
                AffineGapAlignmentScoring.getNucleotideBLASTScoring(),//new AffineGapAlignmentScoring<>(NucleotideSequence.ALPHABET, 5, -4, -10, -1),
                LinearGapAlignmentScoring.getNucleotideBLASTScoring())) {

            int its = TestUtil.its(1000, 5000);
            for (int i = 0; i < its; ++i) {
                NucleotideSequence sequence = randomSequence(NucleotideSequence.ALPHABET, rand, 30, 300);

                model.reseed(rand.nextLong());
                Mutations<NucleotideSequence> mut = MutationsGenerator.generateMutations(sequence, model);
                float mutScore = AlignmentUtils.calculateScore(sequence, mut, sc);
                NucleotideSequence mutated = mut.mutate(sequence);

                Alignment<NucleotideSequence> r = BandedAligner.alignGlobal(sc, sequence, mutated, 0, sequence.size(), 0, mutated.size(), sequence.size());

                Assert.assertEquals(r.getRelativeMutations().mutate(sequence.getRange(r.getSequence1Range())),
                        mutated.getRange(r.getSequence2Range()));

                AlignerTest.assertAlignment(r, mutated, sc);
                Assert.assertTrue(mutScore <= r.calculateScore(sc));

                r = Aligner.alignGlobal(sc, mutated, sequence);
                AlignerTest.assertAlignment(r, sequence, sc);

                Assert.assertEquals(r.getRelativeMutations().mutate(mutated.getRange(r.getSequence1Range())),
                        sequence.getRange(r.getSequence2Range()));

                Assert.assertTrue("Scoring type = " + sc.getClass().getName(),
                        mutScore <= r.calculateScore(sc));
            }
        }
    }
}