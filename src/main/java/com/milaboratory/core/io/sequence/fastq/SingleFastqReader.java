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

import com.milaboratory.core.io.CompressionType;
import com.milaboratory.core.io.sequence.SequenceReaderCloseable;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleReader;
import com.milaboratory.util.CanReportProgress;
import com.milaboratory.util.CountingInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */

public final class SingleFastqReader implements SingleReader, CanReportProgress, SequenceReaderCloseable<SingleRead> {
    public static final int DEFAULT_BUFFER_SIZE = 524288;
    /**
     * If FastqReader fails to guess file format, this value will be used by default.
     *
     * Old value (version <= 1.1.2) was {@literal null}, which leads to {@link WrongQualityFormat} exception in case of
     * failed format guessing.
     */
    public static final QualityFormat DEFAULT_QUALITY_FORMAT = QualityFormat.Phred33;
    /**
     * Used to estimate progress
     */
    private long totalSize;
    private final QualityFormat format;
    private final CountingInputStream countingInputStream;
    long idCounter;
    final FastqRecordsReader recordsReader;


    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file             file with reads
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set
     *                         to a minimum possible values
     * @param lazyReads        allow lazy initialization of single reads
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(String file, boolean replaceWildcards, boolean lazyReads) throws IOException {
        this(new FileInputStream(file), DEFAULT_QUALITY_FORMAT, CompressionType.detectCompressionType(file),
                true, DEFAULT_BUFFER_SIZE, replaceWildcards, lazyReads);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file             file with reads
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set
     *                         to a minimum possible values
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(String file, boolean replaceWildcards) throws IOException {
        this(file, replaceWildcards, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file file with reads
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(String file) throws IOException {
        this(file, false);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file file with reads
     * @param ct   type of compression (NONE, GZIP, etc)
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(String file, CompressionType ct) throws IOException {
        this(new FileInputStream(file), DEFAULT_QUALITY_FORMAT, ct, true, DEFAULT_BUFFER_SIZE, false, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file   file with reads
     * @param format read quality encoding format (use {@literal null} to guess format automatically)
     * @param ct     type of compression (NONE, GZIP, etc)
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(String file, QualityFormat format, CompressionType ct) throws IOException {
        this(new FileInputStream(file), format, ct, format == null, DEFAULT_BUFFER_SIZE, false, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file             file with reads
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set
     *                         to a minimum possible values
     * @param lazyReads        allow lazy initialization of single reads
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(File file, boolean replaceWildcards, boolean lazyReads) throws IOException {
        this(new FileInputStream(file), DEFAULT_QUALITY_FORMAT, CompressionType.detectCompressionType(file),
                true, DEFAULT_BUFFER_SIZE, replaceWildcards, lazyReads);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file             file with reads
     * @param replaceWildcards if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                         matching corresponding wildcards, and their corresponding quality scores will be set
     *                         to a minimum possible values
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(File file, boolean replaceWildcards) throws IOException {
        this(file, replaceWildcards, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file file with reads
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(File file) throws IOException {
        this(file, false);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file file with reads
     * @param ct   type of compression (NONE, GZIP, etc)
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(File file, CompressionType ct) throws IOException {
        this(new FileInputStream(file), DEFAULT_QUALITY_FORMAT, ct, true, DEFAULT_BUFFER_SIZE, false, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param file   file with reads
     * @param format read quality encoding format (use {@literal null} to guess format automatically)
     * @param ct     type of compression (NONE, GZIP, etc)
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(File file, QualityFormat format, CompressionType ct) throws IOException {
        this(new FileInputStream(file), format, ct, format == null, DEFAULT_BUFFER_SIZE, false, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ stream with single-end reads data
     *
     * @param stream stream with reads
     * @param ct     type of compression (NONE, GZIP, etc)
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(InputStream stream, CompressionType ct) throws IOException {
        this(stream, DEFAULT_QUALITY_FORMAT, ct, true, DEFAULT_BUFFER_SIZE, false, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ stream with single-end reads data
     *
     * @param stream stream with reads
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(InputStream stream) throws IOException {
        this(stream, DEFAULT_QUALITY_FORMAT, CompressionType.None, true, DEFAULT_BUFFER_SIZE, false, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ stream with single-end reads data
     *
     * @param stream stream with reads
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(InputStream stream, boolean replaceWildcards) throws IOException {
        this(stream, DEFAULT_QUALITY_FORMAT, CompressionType.None, true, DEFAULT_BUFFER_SIZE, replaceWildcards, true);
    }

    /**
     * Creates a {@link SingleRead} stream from a FASTQ files with single-end read data
     *
     * @param stream stream with reads
     * @param format read quality encoding format
     * @param ct     type of compression (NONE, GZIP, etc)
     * @throws IOException in case there is problem with reading from files
     */
    public SingleFastqReader(InputStream stream, QualityFormat format, CompressionType ct) throws IOException {
        this(stream, format, ct, false, DEFAULT_BUFFER_SIZE, false, true);
    }

    /**
     * Creates a {@link SingleFastqReader} stream from a FASTQ files with single-end read data
     *
     * @param stream             stream with reads
     * @param format             read quality encoding format, if {@code guessQualityFormat} is true this value is used
     *                           as a default format
     * @param ct                 type of compression (NONE, GZIP, etc)
     * @param guessQualityFormat if true reader will try to guess quality string format, if guess fails {@code format}
     *                           will be used as a default quality string format, if {@code format==null} exception
     *                           will be thrown
     * @param bufferSize         size of buffer
     * @param replaceWildcards   if {@literal true}, all wildcards (like N) will be converted to a random basic letters
     *                           matching corresponding wildcards, and their corresponding quality scores will be set
     *                           to a minimum possible values
     * @param lazyReads          specifies whether created reads should be lazy initialized  @throws
     *                           java.io.IOException
     */
    public SingleFastqReader(InputStream stream, QualityFormat format, CompressionType ct,
                             boolean guessQualityFormat, int bufferSize, boolean replaceWildcards,
                             boolean lazyReads) throws IOException {
        //Check for null
        if (stream == null)
            throw new NullPointerException();

        if (stream instanceof FileInputStream)
            totalSize = ((FileInputStream) stream).getChannel().size();
        else
            totalSize = -1L;

        countingInputStream = new CountingInputStream(stream);
        //Initialization
        //Wrapping stream if un-compression needed
        stream = ct.createInputStream(countingInputStream, Math.max(bufferSize / 2, 2048));
        this.recordsReader = new FastqRecordsReader(lazyReads, stream, bufferSize, replaceWildcards, true);

        //Guessing quality format
        if (guessQualityFormat) {
            recordsReader.fillBuffer(DEFAULT_BUFFER_SIZE);
            QualityFormat f = guessFormat(); //Buffer minus ~ one read.
            this.recordsReader.pointer = 0;

            if (f != null)
                format = f;
        }

        if (format == null)
            if (guessQualityFormat)
                throw new RuntimeException("Format guess failed.");
            else
                throw new NullPointerException();

        this.format = format;
    }

    public SingleFastqReader setTotalSize(long totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public QualityFormat getQualityFormat() {
        assert format != null;
        return format;
    }

    @Override
    public double getProgress() {
        return totalSize == -1 ? Double.NaN : (1.0 * countingInputStream.getBytesRead() / totalSize);
    }

    @Override
    public boolean isFinished() {
        return recordsReader.closed.get();
    }

    @Override
    public synchronized SingleRead take() {
        if (recordsReader.closed.get())
            return null;

        try {
            if (!recordsReader.nextRecord(true))
                return null;
        } catch (IOException e) {
            throw new RuntimeException("While reading fastq record with id=" + idCounter +
                    " (line number = " + (idCounter * 4) + ")",
                    e);
        }
        return recordsReader.createRead(idCounter++, format);
    }

    @Override
    public synchronized long getNumberOfReads() {
        return idCounter;
    }

    /**
     * Closes the output port
     */
    @Override
    public void close() {
        //already synchronized
        recordsReader.close();
    }

    private QualityFormat guessFormat() throws IOException {
        boolean signal33 = false, signal64 = false;
        int k, chr;

        while (recordsReader.nextRecord(false)) {

            for (k = recordsReader.qualityBegin; k < recordsReader.qualityEnd; ++k) {
                chr = (int) recordsReader.buffer[k];
                signal33 |= (chr - 64) < QualityFormat.Phred64.getMinValue();
                signal64 |= (chr - 33) > QualityFormat.Phred33.getMaxValue();
            }
        }
        //The file has bad format.
        //If any of formats is applicable file contains out of range values in any way.
        if (signal33 && signal64)
            return null;

        if (signal33)
            return QualityFormat.Phred33;
        if (signal64)
            return QualityFormat.Phred64;

        return null;
    }
}
