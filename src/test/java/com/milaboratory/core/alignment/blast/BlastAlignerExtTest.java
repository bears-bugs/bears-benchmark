package com.milaboratory.core.alignment.blast;

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.OutputPort;
import com.milaboratory.core.alignment.batch.PipedAlignmentResult;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.test.TestUtil;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BlastAlignerExtTest extends BlastTest {
    @Test
    public void test1() throws Exception {
        List<NucleotideSequence> seqs = new ArrayList<>();
        seqs.add(new NucleotideSequence("ATTAGACACAGACACA"));
        seqs.add(new NucleotideSequence("GATACACCACCGATGCTGGAGATGCATGCTAGCGGCGCGGATAGCTGCATG"));

        BlastDB db = BlastDBBuilder.build(seqs);

        NBlastAlignerExt aligner = new NBlastAlignerExt(db);
        OutputPort<PipedAlignmentResult<NBlastHitExt, NucleotideSequence>> results = aligner.align(CUtils.asOutputPort(seqs.get(1)));
        for (PipedAlignmentResult<NBlastHitExt, NucleotideSequence> result : CUtils.it(results)) {
            System.out.println(result);
        }
    }

    @Test
    public void test16SMicrobial1() throws Exception {
        BlastDB db = BlastDB.get(TestUtil.getBigTestResource("16SMicrobial", "16SMicrobial.nsq"));
        String sseq = "ACTCACCTTCCGGTGGGGGATAACTGTCCCAAAGGGCGGCTAATACCCCGTATGCTCCCTGACCGCCGGGTCAGTGAGGAAAGTGGGCTTCG" +
                "TAAGAAGCTCATGCCAGAAGAGAGGCTCGCGCCCCATCAGCTAGTTGGCGAGGTAACGGCTCACCAAGGCAATGACGGGTAGCTGGTCTGAGAGGATG" +
                "GTCAGCCACACTGGGACTGAGACACGGCCCAGACTCCTACGGGAGGCAGCAGTGAGGAATTTTGGGCAATGGGCGAAAGCCTGACCCAGCGACGCCGC" +
                "GTGGAGGATGAAGGCCTTCGGGTCGTAAACTCCTGTTCTGGGGGAAGAAAACGGGATGCGTGAATAATTCATCCCGCTGACGGTACCCCAGGAGAAAG" +
                "CTCCG";
        NucleotideSequence seq = new NucleotideSequence(sseq);
        NBlastAlignerExt blast = new NBlastAlignerExt(db);
        PipedAlignmentResult<NBlastHitExt, NucleotideSequence> result =
                blast.align(CUtils.asOutputPort(seq)).take();
        BlastHitExt<NucleotideSequence> topHit = result.getHits().get(0);
        Assert.assertNotNull(db.retriveSequenceById(topHit.getSubjectId()));
    }
}