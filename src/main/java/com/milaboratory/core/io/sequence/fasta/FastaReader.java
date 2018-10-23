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

import cc.redberry.pipe.OutputPortCloseable;
import com.milaboratory.core.io.sequence.IllegalFileFormatException;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.util.CanReportProgress;
import com.milaboratory.util.CountingInputStream;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Reads amino acid or nucleotide sequence from FASTA formatted file
 *
 * @param <S> sequence type
 */
public class FastaReader<S extends Sequence<S>> implements CanReportProgress,
        OutputPortCloseable<FastaRecord<S>>, AutoCloseable {
    /**
     * For atomic close.
     */
    private final AtomicBoolean closed = new AtomicBoolean(false);
    //lets read line by line
    private final BufferedReader reader;
    private String bufferedLine;
    /**
     * Id counter
     */
    private long id;
    /**
     * Used to calculate progress in percent
     */
    private final long size;
    private final CountingInputStream countingInputStream;
    private final Alphabet<S> alphabet;

    /**
     * Creates reader from the specified input stream.
     *
     * @param inputStream input stream
     * @param size        file size
     */
    public FastaReader(InputStream inputStream, Alphabet<S> alphabet, long size) {
        if (inputStream == null)
            throw new NullPointerException();
        this.size = size;
        this.alphabet = alphabet;
        this.countingInputStream = new CountingInputStream(inputStream);
        this.reader = new BufferedReader(new InputStreamReader(countingInputStream));
    }

    /**
     * Creates reader for stream with unknown size.
     *
     * @param inputStream input stream
     * @param alphabet    alphabet
     */
    public FastaReader(InputStream inputStream, Alphabet<S> alphabet) {
        this(inputStream, alphabet, 0);
    }

    /**
     * Creates FASTA reader for file
     *
     * @param file     file name
     * @param alphabet alphabet
     * @throws FileNotFoundException
     */
    public FastaReader(String file, Alphabet<S> alphabet)
            throws FileNotFoundException {
        this(new File(file), alphabet);
    }

    /**
     * Creates FASTA reader for file
     *
     * @param file     file
     * @param alphabet alphabet
     * @throws FileNotFoundException
     */
    public FastaReader(File file, Alphabet<S> alphabet)
            throws FileNotFoundException {
        this(new FileInputStream(file), alphabet, file.length());
    }

    @Override
    public synchronized double getProgress() {
        if (size == 0)
            return Double.NaN;
        return countingInputStream.getBytesRead() * 1.0 / size;
    }

    // Used only for reporting finished state for progress monitor
    private volatile boolean isFinished = false;

    @Override
    public synchronized boolean isFinished() {
        return isFinished;
    }

    /**
     * Return next FASTA record or {@literal null} if end of stream is reached.
     *
     * <p>This method is thread-safe.</p>
     *
     * @return next FASTA record or {@literal null} if end of stream is reached
     */
    public FastaRecord<S> take() {
        RawFastaRecord rawRecord = takeRawRecord();

        // On EOF
        if (rawRecord == null)
            return null;

        return new FastaRecord<>(id++, rawRecord.description,
                alphabet.parse(rawRecord.sequence));
    }

    /**
     * Return next raw FASTA record or {@literal null} if end of stream is reached.
     *
     * <p>This method is thread-safe.</p>
     *
     * @return next raw FASTA record or {@literal null} if end of stream is reached
     */
    public synchronized RawFastaRecord takeRawRecord() {
        RawFastaRecord rawFastaRecord;
        try {
            rawFastaRecord = nextRawRecord();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (rawFastaRecord == null)
            isFinished = true;
        return rawFastaRecord;
    }

    /**
     * Returns output port of raw records.
     *
     * @return output port of raw records
     */
    public OutputPortCloseable<RawFastaRecord> asRawRecordsPort() {
        return new OutputPortCloseable<RawFastaRecord>() {
            @Override
            public void close() {
                FastaReader.this.close();
            }

            @Override
            public RawFastaRecord take() {
                return takeRawRecord();
            }
        };
    }

    private RawFastaRecord nextRawRecord() throws IOException {
        String description;
        if (bufferedLine != null)
            description = bufferedLine;
        else {
            description = reader.readLine();
            if (description == null)
                return null;
            if (description.charAt(0) != '>')
                throw new IllegalFileFormatException("Wrong FASTA format.");
        }
        StringBuilder sequence = new StringBuilder();
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null)
                break;
            line = line.trim();
            if (!line.isEmpty() && line.charAt(0) == '>')
                break;
            sequence.append(line);
        }
        bufferedLine = line;
        return new RawFastaRecord(description.substring(1), sequence.toString());
    }

    /**
     * Closes the reader
     */
    @Override
    public void close() {
        if (!closed.compareAndSet(false, true))
            return;

        //is synchronized with itself and _next calls,
        //so no synchronization on innerReader is needed
        try {
            synchronized (reader) {
                reader.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * For sequential readers returns the number of reads read till this moment, after reader is exhausted returns
     * total
     * number of reads.
     *
     * <p>This method is thread-safe.</p>
     *
     * @return number of reads read till this moment for sequential readers
     */
    public synchronized long getNumberOfReads() {
        return id;
    }

    /**
     * Used internally
     */
    public static final class RawFastaRecord {
        public final String description;
        public final String sequence;

        private RawFastaRecord(String description, String sequence) {
            this.description = description;
            this.sequence = sequence;
        }
    }
}
