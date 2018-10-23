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
package com.milaboratory.core.io.sequence.fasta;

import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleReadImpl;
import com.milaboratory.core.io.sequence.SingleReader;
import com.milaboratory.core.sequence.NSequenceWithQuality;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.SequenceQuality;
import com.milaboratory.core.sequence.SequencesUtils;
import com.milaboratory.util.CanReportProgress;

import static com.milaboratory.core.sequence.NucleotideSequence.ALPHABET;
import static com.milaboratory.core.sequence.SequenceQuality.BAD_QUALITY_VALUE;
import static com.milaboratory.core.sequence.SequenceQuality.GOOD_QUALITY_VALUE;

/**
 * Converts {@link FastaReader}<{@link NucleotideSequence}> to
 * {@link com.milaboratory.core.io.sequence.SingleReader}.
 */
public class FastaSequenceReaderWrapper implements SingleReader, CanReportProgress {
    private final FastaReader<NucleotideSequence> internalReader;
    private final boolean replaceWildcards;

    public FastaSequenceReaderWrapper(FastaReader<NucleotideSequence> internalReader) {
        this(internalReader, false);
    }

    public FastaSequenceReaderWrapper(FastaReader<NucleotideSequence> internalReader,
                                      boolean replaceWildcards) {
        this.internalReader = internalReader;
        this.replaceWildcards = replaceWildcards;
    }

    @Override
    public double getProgress() {
        return internalReader.getProgress();
    }

    @Override
    public boolean isFinished() {
        return internalReader.isFinished();
    }

    @Override
    public void close() {
        internalReader.close();
    }

    @Override
    public long getNumberOfReads() {
        return internalReader.getNumberOfReads();
    }

    @Override
    public SingleRead take() {
        FastaRecord<NucleotideSequence> record = internalReader.take();
        if(record == null)
            return null;
        NucleotideSequence sequence = record.getSequence();
        NSequenceWithQuality seq;

        if (replaceWildcards) {
            byte[] quality = new byte[sequence.size()];
            for (int i = 0; i < quality.length; ++i)
                quality[i] = ALPHABET.isWildcard(sequence.codeAt(i)) ?
                        BAD_QUALITY_VALUE : GOOD_QUALITY_VALUE;
            seq = new NSequenceWithQuality(SequencesUtils.wildcardsToRandomBasic(sequence, record.getId()),
                    new SequenceQuality(quality));
        } else
            seq = new NSequenceWithQuality(sequence,
                    SequenceQuality.getUniformQuality(GOOD_QUALITY_VALUE, sequence.size()));

        return new SingleReadImpl(record.getId(), seq,
                record.getDescription());
    }
}
