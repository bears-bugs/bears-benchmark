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
package com.milaboratory.core.io.sequence.fastq;

import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleReader;
import com.milaboratory.core.io.util.AbstractRandomAccessReader;
import com.milaboratory.core.io.util.FileIndex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * FASTQ file reader with random access.
 */
public final class RandomAccessFastqReader
        extends AbstractRandomAccessReader<SingleRead>
        implements SingleReader, AutoCloseable {
    private final QualityFormat qualityFormat;
    private final FastqRecordsReader recordsReader;

    /**
     * Creates reader of specified FASTQ file with specified index.
     *
     * @param file      FASTQ file
     * @param fileIndex file index
     */
    public RandomAccessFastqReader(String file, String fileIndex)
            throws IOException {
        this(new RandomAccessFile(file, "r"), FileIndex.read(fileIndex), false, true);
    }

    /**
     * Creates reader of specified FASTQ file with specified index.
     *
     * @param file             FASTQ file
     * @param fileIndex        file index
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set to
     */
    public RandomAccessFastqReader(String file, String fileIndex, boolean replaceWildcards)
            throws IOException {
        this(new RandomAccessFile(file, "r"), FileIndex.read(fileIndex), replaceWildcards, true);
    }

    /**
     * Creates reader of specified FASTQ file with specified index.
     *
     * @param file             FASTQ file
     * @param fileIndex        file index
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set to
     * @param lazyReads        create reads data on demand
     */
    public RandomAccessFastqReader(String file, String fileIndex, boolean replaceWildcards, boolean lazyReads)
            throws IOException {
        this(new RandomAccessFile(file, "r"), FileIndex.read(fileIndex), replaceWildcards, lazyReads);
    }

    /**
     * Creates reader of specified FASTQ file with specified index.
     *
     * @param file             FASTQ file
     * @param fileIndex        file index
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set to
     * @param lazyReads        create reads data on demand
     */
    public RandomAccessFastqReader(File file, FileIndex fileIndex, boolean replaceWildcards, boolean lazyReads)
            throws FileNotFoundException {
        this(new RandomAccessFile(file, "r"), fileIndex, replaceWildcards, lazyReads);
    }

    /**
     * Creates reader of specified FASTQ file with specified index.
     *
     * @param file             FASTQ file
     * @param fileIndex        file index
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set to
     * @param lazyReads        create reads data on demand
     */
    public RandomAccessFastqReader(RandomAccessFile file, FileIndex fileIndex, boolean replaceWildcards, boolean lazyReads) {
        super(fileIndex, file);
        this.qualityFormat = QualityFormat.fromName(fileIndex.getMetadata("format"));
        this.recordsReader = new FastqRecordsReader(lazyReads, file, SingleFastqReader.DEFAULT_BUFFER_SIZE,
                replaceWildcards, false);
    }

    @Override
    public long getNumberOfReads() {
        return fileIndex.getLastRecordNumber() + 1;
    }

    @Override
    public synchronized SingleRead take() {
        if (recordsReader.closed.get())
            return null;
        return super.take();
    }

    @Override
    public synchronized SingleRead take(long recordNumber) {
        if (recordsReader.closed.get())
            return null;
        try {
            return super.take(recordNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected SingleRead take0() {
        try {
            if (!recordsReader.nextRecord(true))
                return null;
        } catch (IOException e) {
            throw new RuntimeException("While reading fastq record with id=" + currentRecordNumber +
                    " (line number = " + (currentRecordNumber * 4) + ")",
                    e);
        }
        return recordsReader.createRead(currentRecordNumber, qualityFormat);
    }

    @Override
    protected void skip() {
        try {
            recordsReader.nextRecord(true);
        } catch (IOException e) {
            throw new RuntimeException("While reading fastq record with id=" + currentRecordNumber +
                    " (line number = " + (currentRecordNumber * 4) + ")",
                    e);
        }
    }

    @Override
    protected void resetBufferOnSeek() {
        try {
            recordsReader.resetBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        recordsReader.close();
    }
}
