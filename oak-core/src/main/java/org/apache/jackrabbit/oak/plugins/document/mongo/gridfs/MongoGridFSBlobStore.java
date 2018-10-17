/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.plugins.document.mongo.gridfs;

import java.io.IOException;
import java.io.InputStream;

import org.apache.jackrabbit.oak.spi.blob.BlobStore;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import org.apache.jackrabbit.oak.spi.blob.BlobStoreInputStream;

/**
 * Implementation of {@link BlobStore} for MongoDB using GridFS. It does not
 * support garbage collection at the moment.
 */
public class MongoGridFSBlobStore implements BlobStore {

    private final CommandExecutor commandExecutor;
    private final GridFS gridFS;

    /**
     * Constructs a new {@code BlobStoreMongoGridFS}
     *
     * @param db The DB.
     */
    public MongoGridFSBlobStore(DB db) {
        commandExecutor = new DefaultCommandExecutor();
        gridFS = new GridFS(db);
    }

    @Override
    public long getBlobLength(String blobId) throws IOException {
        Command<Long> command = new GetBlobLengthCommandGridFS(gridFS, blobId);
        return commandExecutor.execute(command);
    }

    @Override
    public InputStream getInputStream(String blobId) throws IOException {
        return new BlobStoreInputStream(this, blobId, 0);
    }

    @Override
    public String getBlobId(String reference) {
        return null;
    }

    @Override
    public String getReference(String blobId) {
        return null;
    }

    @Override
    public int readBlob(String blobId, long blobOffset, byte[] buffer, int bufferOffset, int length) throws IOException {
        Command<Integer> command = new ReadBlobCommandGridFS(gridFS, blobId, blobOffset,
                buffer, bufferOffset, length);
        return commandExecutor.execute(command);
    }

    @Override
    public String writeBlob(InputStream is) throws IOException {
        Command<String> command = new WriteBlobCommandGridFS(gridFS, is);
        return commandExecutor.execute(command);
    }

}
