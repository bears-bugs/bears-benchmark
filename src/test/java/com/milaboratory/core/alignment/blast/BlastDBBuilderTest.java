package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.sequence.NucleotideSequence;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BlastDBBuilderTest extends BlastTest {
    @Test
    public void test1() throws Exception {
        List<NucleotideSequence> seqs = new ArrayList<>();
        seqs.add(new NucleotideSequence("ATTAGACACAGACACA"));
        seqs.add(new NucleotideSequence("GATACACCACCGATGCTGGAGATGCATGCTAGCGGCGCGGATAGCTGCATG"));
        long bases = 0;
        for (NucleotideSequence seq : seqs)
            bases += seq.size();

        BlastDB db = BlastDBBuilder.build(seqs, true);

        assertEquals(NucleotideSequence.ALPHABET, db.getAlphabet());
        assertEquals(bases, db.getLettersCount());
        assertEquals(seqs.size(), db.getRecordsCount());

        assertEquals(seqs.get(1), db.retriveSequenceById(BlastDBBuilder.getIdFasta(1)));
    }
}