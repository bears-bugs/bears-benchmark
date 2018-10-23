package com.milaboratory.core.mutations;

import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.test.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import static com.milaboratory.core.sequence.NucleotideSequence.ALPHABET;

/**
 * Created by poslavsky on 01/02/16.
 */
public class MutationsEnumeratorTest {
    @Test
    public void test1() throws Exception {
        for (int c = 0; c < TestUtil.its(100, 1000); c++) {
            Mutations<NucleotideSequence> mutations = MutationsGenerator.generateMutations(TestUtil.randomSequence(ALPHABET, 10, 100), MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(500));
            MutationsEnumerator enumerator = new MutationsEnumerator(mutations);
            MutationsBuilder<NucleotideSequence> check = new MutationsBuilder<>(ALPHABET);
            while (enumerator.next()) {
                int offset = enumerator.getOffset();
                int length = enumerator.getLength();
                switch (enumerator.getType()) {
                    case Substitution:
                        Assert.assertEquals(1, length);
                        check.appendSubstitution(
                                mutations.getPositionByIndex(offset),
                                mutations.getFromAsCodeByIndex(offset),
                                mutations.getToAsCodeByIndex(offset));
                        break;
                    case Deletion:
                        Assert.assertEquals(1, length);
                        check.appendDeletion(
                                mutations.getPositionByIndex(offset),
                                mutations.getFromAsCodeByIndex(offset));
                        break;
                    case Insertion:
                        for (int i = 0; i < length; i++) {
                            if (i != 0)
                                Assert.assertEquals(
                                        mutations.getPositionByIndex(offset),
                                        mutations.getPositionByIndex(offset + i));
                            check.appendInsertion(
                                    mutations.getPositionByIndex(offset + i),
                                    mutations.getToAsCodeByIndex(offset + i));
                        }
                        break;
                }
            }
            Assert.assertEquals(mutations, check.createAndDestroy());
        }
    }
}