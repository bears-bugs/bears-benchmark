package com.milaboratory.core.alignment.blast;

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.OutputPort;
import com.milaboratory.core.alignment.AlignmentUtils;
import com.milaboratory.core.alignment.batch.HasSequence;
import com.milaboratory.core.alignment.batch.PipedAlignmentResult;
import com.milaboratory.core.mutations.Mutations;
import com.milaboratory.core.mutations.generator.MutationModels;
import com.milaboratory.core.mutations.generator.MutationsGenerator;
import com.milaboratory.core.mutations.generator.NucleotideMutationModel;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.test.TestUtil;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static cc.redberry.pipe.CUtils.asOutputPort;
import static cc.redberry.pipe.CUtils.it;

public class BlastAlignerTest extends BlastTest {
    @Test
    public void test1() throws Exception {
        NBlastAligner<Integer> ba = new NBlastAligner<>();
        NucleotideSequence ns1 = new NucleotideSequence("ATTAGACGAATCCGATGCTGACTGCGCGATGATGCTAGTCGTGCTAGTACTAGCTGGCGCGGATTC");
        NucleotideSequence ns2 = new NucleotideSequence("TATTACCTGCTGCGCGCGCTAGATCGGTACTACGTTGCTAGCTAGCTTCGTATACGTCGTGCTAGTATCGATCGCTAG");

        ba.addReference(ns1, 1);
        ba.addReference(ns2, 2);

        NucleotideSequence nsq = new NucleotideSequence("TAGACGAATCCGATGCTGACTGCGCGATGAACCTAGTCGTGCTAGTACTA");

        PipedAlignmentResult<NBlastHit<Integer>, NucleotideSequence> result = ba.align(asOutputPort(nsq)).take();
        Assert.assertEquals((Integer) 1, result.getHits().get(0).getRecordPayload());
        Assert.assertEquals(nsq, AlignmentUtils.getAlignedSequence2Part(result.getHits().get(0).getAlignment()));
    }

    @Test
    public void simpleRandomTestT1() throws Exception {
        simpleRandomTest(1);
    }

    @Test
    public void simpleRandomTestT2() throws Exception {
        simpleRandomTest(2);
    }

    @Test
    public void simpleRandomTestT3() throws Exception {
        simpleRandomTest(3);
    }

    public void simpleRandomTest(int threads) {
        int recordsInBase = 1000;

        int baseLengtFrom = 100;
        int baseLengtTo = 200;

        int queryLengthFrom = 90;
        int queryLengthTo = 150;

        int trys = 1000;

        Well19937c rg = new Well19937c();
        RandomDataGenerator rdg = new RandomDataGenerator(rg);

        List<NucleotideSequence> base = new ArrayList<>();

        NBlastAligner<Integer> ba = new NBlastAligner<>();
        ba.setConcurrentBlastProcessCount(threads);

        for (int i = 0; i < recordsInBase; i++) {
            NucleotideSequence seq = TestUtil.randomSequence(NucleotideSequence.ALPHABET, baseLengtFrom, baseLengtTo);
            base.add(seq);
            ba.addReference(seq, i);
        }

        List<QueryObject<NucleotideSequence>> queries = new ArrayList<>();

        NucleotideMutationModel mutationModel = MutationModels.getEmpiricalNucleotideMutationModel().multiplyProbabilities(2.0);
        mutationModel.reseed(12343L);

        for (int i = 0; i < trys; i++) {
            int hit = rg.nextInt(base.size());
            NucleotideSequence sFromBase = base.get(hit);
            int qLength = rdg.nextInt(queryLengthFrom, Math.min(queryLengthTo, sFromBase.size() - 1));
            int qFrom = rg.nextInt(sFromBase.size() - qLength);
            NucleotideSequence query = sFromBase.getRange(qFrom, qFrom + qLength);
            Mutations<NucleotideSequence> muts = MutationsGenerator.generateMutations(query, mutationModel);
            query = muts.mutate(query);
            queries.add(new QueryObject<>(query, hit, muts.move(qFrom)));
        }

        OutputPort<PipedAlignmentResult<NBlastHit<Integer>, QueryObject<NucleotideSequence>>> results = ba.align(CUtils.asOutputPort(queries));

        int noHit = 0;
        int wrongHit = 0;
        int wrongMutations = 0;
        for (PipedAlignmentResult<NBlastHit<Integer>, QueryObject<NucleotideSequence>> result : it(results)) {
            if (!result.hasHits()) {
                ++noHit;
                continue;
            }
            if (result.getQuery().expectedHit != result.getHits().get(0).getRecordPayload())
                wrongHit++;
            if (!result.getQuery().expectedMutations.equals(result.getHits().get(0).getAlignment().getAbsoluteMutations())) {
                wrongMutations++;
            }
        }
        Assert.assertTrue(noHit < 30);
        Assert.assertTrue(wrongHit < 5);
        Assert.assertTrue(wrongMutations < 500);
    }

    public static class QueryObject<S extends Sequence<S>> implements HasSequence<S> {
        final S query;
        final int expectedHit;
        final Mutations<S> expectedMutations;

        public QueryObject(S query, int expectedHit, Mutations<S> expectedMutations) {
            this.query = query;
            this.expectedHit = expectedHit;
            this.expectedMutations = expectedMutations;
        }

        @Override
        public S getSequence() {
            return query;
        }
    }
}