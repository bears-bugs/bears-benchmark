package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.AminoAcidSequence;
import com.milaboratory.core.sequence.NucleotideSequence;

public enum BlastTask {
    BlastN(NucleotideSequence.ALPHABET, "blastn"),
    BlastNShort(NucleotideSequence.ALPHABET, "blastn-short"),
    MegaBlast(NucleotideSequence.ALPHABET, "megablast"),
    DCMegablast(NucleotideSequence.ALPHABET, "dc-megablast"),
    RMBlastN(NucleotideSequence.ALPHABET, "rmblastn"),
    BlastP(AminoAcidSequence.ALPHABET, "blastp"),
    BlastPShort(AminoAcidSequence.ALPHABET, "blastp-fast"),
    BlastPFast(AminoAcidSequence.ALPHABET, "blastp-short");

    final Alphabet<?> alphabet;
    final String value;

    BlastTask(Alphabet<?> alphabet, String value) {
        this.alphabet = alphabet;
        this.value = value;
    }
}
