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

import com.milaboratory.core.io.sequence.IllegalFileFormatException;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleReadImpl;
import com.milaboratory.core.io.sequence.SingleReadLazy;
import com.milaboratory.core.sequence.UnsafeFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * High performance reader of FASTQ records.
 *
 * Used internally in all FastqReaders.
 */
public final class FastqRecordsReader implements AutoCloseable {
    private static final byte DELIMITER = '\n';
    final boolean lazyReads;
    final AtomicBoolean closed = new AtomicBoolean(false);
    final InputDataWrapper inputStream;
    final int bufferSize;
    byte[] buffer;
    int currentBufferSize;
    int pointer;
    int descriptionBegin, sequenceBegin, sequenceEnd, qualityBegin, qualityEnd;
    /**
     * If true, then this reader will be automatically closed after reading the last record
     */
    final boolean autoClose;
    /**
     * If this parameter is {@literal true}, then all wildcards (like N) will be converted to a random basic letters
     * matching corresponding wildcards, and their corresponding quality scores will be set to a minimum possible
     * values.
     */
    final boolean replaceWildcards;

    /**
     * See main constructor for parameters.
     */
    public FastqRecordsReader(boolean lazyReads, RandomAccessFile inputStream, int bufferSize, boolean replaceWildcards,
                       boolean autoClose) {
        this(lazyReads, create(inputStream), bufferSize, replaceWildcards, autoClose);
    }

    /**
     * See main constructor for parameters.
     */
    public FastqRecordsReader(boolean lazyReads, InputStream inputStream, int bufferSize, boolean replaceWildcards,
                       boolean autoClose) {
        this(lazyReads, create(inputStream), bufferSize, replaceWildcards, autoClose);
    }

    /**
     * @param lazyReads        use lazy implementation of reads (increases performance if reads processed in parallel)
     * @param inputStream      input stream
     * @param bufferSize       buffer size to use
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set to
     * @param autoClose        if {@literal true}, then this reader will be automatically closed after reading the last
     *                         record
     */
    private FastqRecordsReader(boolean lazyReads, InputDataWrapper inputStream, int bufferSize,
                               boolean replaceWildcards, boolean autoClose) {
        this.lazyReads = lazyReads;
        this.inputStream = inputStream;
        this.bufferSize = bufferSize;
        this.replaceWildcards = replaceWildcards;
        this.autoClose = autoClose;
    }

    public SingleRead createRead(long id, QualityFormat format) {
        if (lazyReads)
            return SingleReadLazy.create(format,
                    id,
                    buffer,
                    descriptionBegin,
                    (short) (sequenceBegin - descriptionBegin),
                    (short) (qualityBegin - descriptionBegin),
                    (short) (sequenceEnd - sequenceBegin),
                    (short) (sequenceBegin - descriptionBegin - 1),
                    replaceWildcards);
        else
            return new SingleReadImpl(id,
                    UnsafeFactory.fastqParse(buffer, sequenceBegin, qualityBegin,
                            sequenceEnd - sequenceBegin, format.getOffset(), id, replaceWildcards),
                    new String(buffer, descriptionBegin,
                            sequenceBegin - descriptionBegin - 1));
    }

    void fillBuffer(int size) throws IOException {
        if (closed.get())
            return;
        byte[] newBuffer;
        if (lazyReads)
            newBuffer = new byte[size];//if lazy reads, we shall not overwrite buffer content!
        else {
            if (buffer == null)
                buffer = new byte[size];
            if (buffer.length != size) //needed after automatic quality format guessing to shrink buffer size
                newBuffer = new byte[size];
            else
                newBuffer = buffer;
        }
        if (buffer != null) //buffer == null after initialization (in case of non lazy reads)
            System.arraycopy(buffer, pointer, newBuffer, 0, currentBufferSize - pointer);
        int readBytes = inputStream.readFully(newBuffer, currentBufferSize - pointer, newBuffer.length - currentBufferSize + pointer);
        currentBufferSize = (readBytes == -1 ? 0 : readBytes) + currentBufferSize - pointer;
        pointer = 0;
        buffer = newBuffer;
        if (readBytes == -1 && autoClose)
            close();
    }

    public boolean nextRecord(boolean refillBuffer) throws IOException {
        int pass = -1; //number of tries to fillBuffer

        while (true) {
            ++pass;
            if (pass == 2)// tried to fill buffer 2 times
                if (closed.get()) //no more data in file (close was invoked by fillBuffer)
                    throw new IllegalFileFormatException("Unexpected end of file.");
                else //buffer is smaller than length of record (seq + qual + descript )!
                    throw new IllegalFileFormatException("Too small buffer.");

            if (buffer == null) { //only after initialization
                if (!refillBuffer)
                    throw new RuntimeException();
                fillBuffer(bufferSize);
                continue;
            }

            if (currentBufferSize == 0)
                return false; //empty buffer (EOF reached)

            if (currentBufferSize == pointer) //all data in buffer is already processed
                if (refillBuffer) {
                    fillBuffer(bufferSize);
                    continue;
                } else return false;

            if (buffer[pointer] != '@') // fastq specification
                throw new IllegalFileFormatException("No '@' character found in the beginning of fastq description line." );

            //standard fastq reading:

            int pointer = this.pointer;
            ++pointer;
            descriptionBegin = pointer;
            for (; pointer < currentBufferSize && buffer[pointer] != DELIMITER; ++pointer) ;
            if (pointer == buffer.length) {
                if (refillBuffer) {
                    fillBuffer(bufferSize);
                    continue;
                } else return false;
            }
            sequenceBegin = ++pointer;
            for (; pointer < currentBufferSize && buffer[pointer] != DELIMITER; ++pointer) ;
            if (pointer == buffer.length) {
                if (refillBuffer) {
                    fillBuffer(bufferSize);
                    continue;
                } else return false;
            }
            sequenceEnd = pointer++;
            if (pointer == buffer.length) {
                if (refillBuffer) {
                    fillBuffer(bufferSize);
                    continue;
                } else return false;
            }

            if (buffer[pointer] != '+') // fastq specification
                throw new IllegalFileFormatException("No '+' character found in the beginning of the third line of the fastq record.");

            for (; pointer < currentBufferSize && buffer[pointer] != DELIMITER; ++pointer) ;
            if (pointer == buffer.length) {
                if (refillBuffer) {
                    fillBuffer(bufferSize);
                    continue;
                } else return false;
            }
            qualityBegin = ++pointer;

            for (; pointer < currentBufferSize && buffer[pointer] != DELIMITER; ++pointer) ;
            if (pointer == buffer.length) {
                if (refillBuffer) {
                    fillBuffer(bufferSize);
                    continue;
                } else return false;
            }
            qualityEnd = pointer;
            if (qualityEnd - qualityBegin != sequenceEnd - sequenceBegin)
                throw new IllegalFileFormatException("Quality and sequence have different sizes.");

            this.pointer = pointer + 1;
            return true;
        }
    }

    /**
     * Closes the output port
     */
    @Override
    public void close() {
        if (!closed.compareAndSet(false, true))
            return;

        //is synchronized with itself and _next calls,
        //so no synchronization on inner reader is needed
        try {
            synchronized (inputStream) {
                inputStream.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    void resetBuffer() throws IOException {
        pointer = currentBufferSize = 0;
        fillBuffer(bufferSize);
    }

    private static abstract class InputDataWrapper {
        abstract int read(byte[] buffer, int off, int len) throws IOException;

        abstract void close() throws IOException;

        int readFully(byte[] buffer, int off, int len) throws IOException {
            int total = 0;
            do {
                int read = read(buffer, off, len);
                if (read == -1)
                    break;
                total += read;
                off += read;
                len -= read;
            } while (len != 0);
            return total;
        }
    }

    private static final InputDataWrapper create(final InputStream stream) {
        return new InputDataWrapper() {
            @Override
            public int read(byte[] buffer, int off, int len)
                    throws IOException {
                return stream.read(buffer, off, len);
            }

            @Override
            public void close() throws IOException {
                stream.close();
            }
        };
    }

    private static final InputDataWrapper create(final RandomAccessFile stream) {
        return new InputDataWrapper() {
            @Override
            public int read(byte[] buffer, int off, int len)
                    throws IOException {
                return stream.read(buffer, off, len);
            }

            @Override
            public void close() throws IOException {
                stream.close();
            }
        };
    }

}
