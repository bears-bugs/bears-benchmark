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

import java.util.HashMap;
import java.util.Map;

/**
 * FileIndex builder.
 */
public final class FileIndexBuilder {
    /**
     * Step between records in file.
     */
    private final long step;
    /**
     * Metadata recorded in the index
     */
    private final Map<String, String> metadata = new HashMap<>();
    /**
     * Stored positions in file (measured in bytes)
     */
    private final TLongArrayList index = new TLongArrayList();
    /**
     * Current position (in bytes) in indexing file
     */
    private long currentByte = 0;
    /**
     * Current record number
     */
    private long currentRecord = 0;
    /**
     * Starting record number
     */
    private long startingRecordNumber;
    /**
     * Starting position (in bytes)
     */
    private long startingByte;
    /**
     * Specifies whether this builder is destroyed
     */
    private boolean destroyed = false;

    /**
     * Creates index builder with specified step between records, i.e. if some record with number <i>n</i> is indexed,
     * then the next indexed record number will be <i>n + step</i>.
     *
     * @param step step between records
     */
    public FileIndexBuilder(long step) {
        if (step == 0)
            throw new IllegalArgumentException("step must be > 0.");
        this.step = step;
        this.startingByte = 0;
        this.index.add(0);
    }

    /**
     * Sets the starting record to a specified pointer (in bytes).
     *
     * @param recordPosition starting record position in file (in bytes)
     * @return this
     */
    public FileIndexBuilder setStartingRecordPosition(long recordPosition) {
        checkIfDestroyed();
        if (index.size() != 1)
            throw new IllegalStateException("Initial record is already initialised to " + index.get(0));
        index.set(0, startingByte = recordPosition);
        return this;
    }

    /**
     * Sets the starting record number to a specified one.
     *
     * @param recordNumber starting record number
     * @return this
     */
    public FileIndexBuilder setStartingRecordNumber(long recordNumber) {
        checkIfDestroyed();
        if (index.size() != 1)
            throw new IllegalStateException("Initial record is already initialised to " + index.get(0));
        startingRecordNumber = recordNumber;
        return this;
    }

    /**
     * Puts metadata.
     *
     * @param key   metadata key
     * @param value metadata value
     * @return this
     */
    public FileIndexBuilder putMetadata(String key, String value) {
        checkIfDestroyed();
        metadata.put(key, value);
        return this;
    }

    /**
     * Appends next record to this index builder and returns this.
     *
     * @param recordSize size of record measured in bytes
     * @return this
     */
    public FileIndexBuilder appendNextRecord(long recordSize) {
        checkIfDestroyed();
        if (recordSize < 0)
            throw new IllegalArgumentException("Size cannot be negative.");
        if (currentRecord == step) {
            index.add(startingByte + currentByte);
            currentRecord = 0;
        }
        currentByte += recordSize;
        ++currentRecord;
        return this;
    }

    /**
     * Returns the number of the last record that is indexed by this index builder.
     *
     * @return the number of the last record that is indexed by this index builder
     */
    public long lastAccessibleRecordNumber() {
        return startingRecordNumber + step * (index.size() - 1);
    }

    /**
     * Creates {@code FileIndex} assembled by this builder.
     *
     * @return {@code FileIndex} assembled by this builder
     */
    public FileIndex createAndDestroy() {
        checkIfDestroyed();
        destroyed = true;
        return new FileIndex(step, metadata, index, startingRecordNumber, lastAccessibleRecordNumber());
    }

    private void checkIfDestroyed() {
        if (destroyed)
            throw new IllegalStateException("This builder is destryed.");
    }
}
