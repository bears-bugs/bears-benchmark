/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.jackrabbit.oak.upgrade.cli.blob;

import java.io.IOException;
import java.io.InputStream;

import org.apache.jackrabbit.oak.spi.blob.BlobStore;

/**
 * Utility BlobStore implementation to be used in tooling that can work with a
 * FileStore without the need of the DataStore being present locally
 */
public class MissingBlobStore implements BlobStore {

    @Override
    public String writeBlob(InputStream in) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int readBlob(String blobId, long pos, byte[] buff, int off,
            int length) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getBlobLength(String blobId) throws IOException {
        // best effort length extraction
        int indexOfSep = blobId.lastIndexOf("#");
        if (indexOfSep != -1) {
            return Long.valueOf(blobId.substring(indexOfSep + 1));
        }
        return -1;
    }

    @Override
    public InputStream getInputStream(String blobId) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBlobId(String reference) {
        return reference;
    }

    @Override
    public String getReference(String blobId) {
        return blobId;
    }
}
