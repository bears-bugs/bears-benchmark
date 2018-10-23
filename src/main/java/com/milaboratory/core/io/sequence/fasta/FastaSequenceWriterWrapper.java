package com.milaboratory.core.io.sequence.fasta;

import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleSequenceWriter;
import com.milaboratory.core.sequence.NucleotideSequence;

import java.io.FileNotFoundException;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class FastaSequenceWriterWrapper implements SingleSequenceWriter {
    final FastaWriter<NucleotideSequence> sequenceFastaWriter;

    public FastaSequenceWriterWrapper(String fileName) throws FileNotFoundException {
        sequenceFastaWriter = new FastaWriter<>(fileName);
    }

    @Override
    public void write(SingleRead read) {
        sequenceFastaWriter.write(read.getDescription(), read.getData().getSequence());
    }

    @Override
    public void flush() {
        sequenceFastaWriter.flush();
    }

    @Override
    public void close() {
        sequenceFastaWriter.close();
    }
}
