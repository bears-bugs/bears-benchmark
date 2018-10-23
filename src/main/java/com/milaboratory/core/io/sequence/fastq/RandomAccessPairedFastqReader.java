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

import com.milaboratory.core.io.sequence.PairedRead;
import com.milaboratory.core.io.sequence.SequenceReader;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.util.FileIndex;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * FASTQ file reader for paired reads with random access.
 */
public final class RandomAccessPairedFastqReader
        implements SequenceReader<PairedRead>, AutoCloseable {
    private final RandomAccessFastqReader reader1, reader2;

    public RandomAccessPairedFastqReader(String file1, String index1,
                                         String file2, String index2) throws IOException {
        this(file1, index1, file2, index2, false, true);
    }

    public RandomAccessPairedFastqReader(RandomAccessFile file1, FileIndex index1,
                                         RandomAccessFile file2, FileIndex index2) throws IOException {
        this(file1, index1, file2, index2, false, true);
    }

    public RandomAccessPairedFastqReader(String file1, String index1,
                                         String file2, String index2,
                                         boolean replaceWildcards, boolean lazyReads) throws IOException {
        this(new RandomAccessFastqReader(file1, index1, replaceWildcards, lazyReads),
                new RandomAccessFastqReader(file2, index2, replaceWildcards, lazyReads));
    }

    public RandomAccessPairedFastqReader(RandomAccessFile file1, FileIndex index1,
                                         RandomAccessFile file2, FileIndex index2,
                                         boolean replaceWildcards, boolean lazyReads) throws IOException {
        this(new RandomAccessFastqReader(file1, index1, replaceWildcards, lazyReads),
                new RandomAccessFastqReader(file2, index2, replaceWildcards, lazyReads));
    }

    public RandomAccessPairedFastqReader(RandomAccessFastqReader reader1, RandomAccessFastqReader reader2) {
        if (reader1.getCurrentRecordNumber() != reader2.getCurrentRecordNumber() ||
                reader1.getNumberOfReads() != reader2.getNumberOfReads())
            throw new IllegalArgumentException("Random access readers must have same pointers.");
        this.reader1 = reader1;
        this.reader2 = reader2;
    }

    @Override
    public long getNumberOfReads() {
        return reader1.getNumberOfReads();
    }

    public void seekToRecord(long recordNumber) throws IOException {
        reader1.seekToRecord(recordNumber);
        reader2.seekToRecord(recordNumber);
    }

    public synchronized PairedRead take(long recordNumber) {
        SingleRead read1 = reader1.take(recordNumber);
        SingleRead read2 = reader2.take(recordNumber);
        if (read1 == null && read2 == null)
            return null;
        assert read1 != null && read2 != null;
        return new PairedRead(read1, read2);
    }

    @Override
    public synchronized PairedRead take() {
        SingleRead read1 = reader1.take();
        SingleRead read2 = reader2.take();
        if (read1 == null && read2 == null)
            return null;
        assert read1 != null && read2 != null;
        return new PairedRead(read1, read2);
    }

    @Override
    public void close() {
        reader1.close();
        reader2.close();
    }
}
