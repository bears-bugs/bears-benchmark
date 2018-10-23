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
package com.milaboratory.core.io.util;

import gnu.trove.list.array.TLongArrayList;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Index of file that contains sequential records.
 */
public final class FileIndex {
    /**
     * For serialization
     */
    public static final int MAGIC = 0x9D17935B;
    /**
     * Step between records in file.
     */
    final long step;
    /**
     * Metadata recorded in the index
     */
    final Map<String, String> metadata;
    /**
     * Stored positions in file (measured in bytes)
     */
    final TLongArrayList index;
    /**
     * Starting record number
     */
    final long startingRecordNumber;
    /**
     * Last record number
     */
    final long lastRecordNumber;

    FileIndex(long step, Map<String, String> metadata,
              TLongArrayList index, long startingRecordNumber, long lastRecordNumber) {
        this.step = step;
        this.metadata = metadata;
        this.index = index;
        this.startingRecordNumber = startingRecordNumber;
        this.lastRecordNumber = lastRecordNumber;
    }

    /**
     * Returns step of current index
     *
     * @return step of current index
     */
    public long getStep() {
        return step;
    }

    /**
     * Returns the first record number indexed by this index.
     *
     * @return the first record number indexed by this index
     */
    public long getStartingRecordNumber() {
        return startingRecordNumber;
    }

    /**
     * Returns the last record number indexed by this index.
     *
     * @return the last record number indexed by this index
     */
    public long getLastRecordNumber() {
        return lastRecordNumber;
    }

    /**
     * Returns a metadata record.
     *
     * @param key metadata key
     * @return metadata record
     */
    public String getMetadata(String key) {
        return metadata.get(key);
    }

    /**
     * Returns the nearest (from the left side, i.e. smaller) position (in bytes) to specified record number.
     *
     * @param recordNumber number of record
     * @return nearest (from the left side, i.e. smaller) position (in bytes) to specified record number
     */
    public long getNearestPosition(long recordNumber) {
        if (recordNumber < startingRecordNumber || recordNumber > lastRecordNumber)
            throw new IndexOutOfBoundsException();
        return index.get((int) ((recordNumber - startingRecordNumber) / step));
    }

    /**
     * Returns the record number, which is nearest (from the left side, i.e. smaller) to the specified record number.
     *
     * @param recordNumber number of record
     * @return record number, which is nearest (from the left side, i.e. smaller) to the specified record number
     */
    public long getNearestRecordNumber(long recordNumber) {
        if (recordNumber < startingRecordNumber || recordNumber > lastRecordNumber)
            throw new IndexOutOfBoundsException();
        return (startingRecordNumber + step * ((recordNumber - startingRecordNumber) / step));
    }

    /**
     * Writes this index to specified file.
     *
     * @param fileName file name
     * @throws IOException
     */
    public void write(String fileName) throws IOException {
        write(new File(fileName));
    }

    /**
     * Writes this index to specified file.
     *
     * @param file file
     * @throws IOException
     */
    public void write(File file) throws IOException {
        write(new FileOutputStream(file));
    }

    /**
     * Writes this index to specified output stream.
     *
     * @param stream output stream
     * @throws IOException
     */
    public void write(OutputStream stream) throws IOException {
        //Creating buffer
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        //Creating compressed output data stream
        DeflaterOutputStream deflate = new DeflaterOutputStream(bos);
        DataOutputStream dataStream = new DataOutputStream(deflate);

        //Writing index step
        dataStream.writeLong(step);

        //Writing boundaries of index
        dataStream.writeLong(getStartingRecordNumber());
        dataStream.writeLong(getLastRecordNumber());

        //Writing number of meta-records
        dataStream.writeInt(metadata.size());

        //Writing meta records
        for (Map.Entry<String, String> e : metadata.entrySet()) {
            dataStream.writeUTF(e.getKey());
            dataStream.writeUTF(e.getValue());
        }

        //Writing number of entries
        dataStream.writeInt(index.size());

        //Writing index
        long lastValue = 0, v;
        for (int i = 0; i < index.size(); ++i) {
            IOUtil.writeRawVarint64(dataStream, (v = index.get(i)) - lastValue);
            lastValue = v;
        }

        //Flushing gzip output stream to underlying stream
        deflate.finish();

        //Creating raw output stream
        DataOutputStream raw = new DataOutputStream(stream);

        //Writing non-compressed magic number
        raw.writeInt(MAGIC);

        //Writing index size
        raw.writeInt(bos.size());

        //Writes index
        raw.write(bos.toByteArray());
    }

    /**
     * Reads index from file.
     *
     * @param fileName input file name
     * @return {@code FileIndex}
     * @throws IOException
     */
    public static FileIndex read(String fileName) throws IOException {
        return read(new File(fileName));
    }

    /**
     * Reads index from file.
     *
     * @param file input file
     * @return {@code FileIndex}
     * @throws IOException
     */
    public static FileIndex read(File file) throws IOException {
        return read(new FileInputStream(file));
    }

    /**
     * Reads index from input stream.
     *
     * @param stream input stream
     * @return {@code FileIndex}
     * @throws IOException
     */
    public static FileIndex read(InputStream stream) throws IOException {
        //Creating raw input stream
        DataInputStream raw = new DataInputStream(stream);

        //Reading magic number
        int magic = raw.readInt();
        if (magic != MAGIC)
            throw new IOException("Wrong magic number");

        //Reading length
        int length = raw.readInt();

        //Reading whole index
        byte[] buffer = new byte[length];
        raw.read(buffer);

        //Creating uncompressed stream
        InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(buffer));
        DataInputStream dataStream = new DataInputStream(inflater);

        //Reading step
        long step = dataStream.readLong();
        long startingRecordNumner = dataStream.readLong();
        long lastRecordNumber = dataStream.readLong();

        //Reading meta record count
        int metaRecordsCount = dataStream.readInt();

        //Reading meta records
        Map<String, String> metadata = new HashMap<>();
        String key, value;
        while (--metaRecordsCount >= 0) {
            key = dataStream.readUTF();
            value = dataStream.readUTF();
            metadata.put(key, value);
        }

        //Reading entries number
        int size = dataStream.readInt();

        //Creating array for index
        TLongArrayList index = new TLongArrayList(size);

        //Reading index
        long last = 0, val;
        for (int i = 0; i < size; ++i) {
            val = IOUtil.readRawVarint64(dataStream, -1);
            if (val == -1)
                throw new IOException("Wrong file format");
            last += val;
            index.add(last);
        }

        return new FileIndex(step, metadata, index, startingRecordNumner, lastRecordNumber);
    }
}
