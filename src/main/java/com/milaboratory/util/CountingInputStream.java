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
package com.milaboratory.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Stream that tracks the number of bytes read.
 *
 * @NotThreadSafe
 * @since Apache Commons Compress 1.3
 */
public final class CountingInputStream extends FilterInputStream {
    private long bytesRead;
    private volatile boolean closed = false;

    public CountingInputStream(final InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int r = in.read();
        if (r >= 0) {
            count(1);
        }
        return r;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int r = in.read(b, off, len);
        if (r >= 0) {
            count(r);
        }
        return r;
    }

    @Override
    public void close() throws IOException {
        closed = true;
        super.close();
    }

    /**
     * Increments the counter of already read bytes. Doesn't increment if the EOF has been hit (read == -1)
     *
     * @param read the number of bytes read
     */
    protected final void count(long read) {
        if (read != -1) {
            bytesRead += read;
        }
    }

    /**
     * Returns the current number of bytes read from this stream.
     *
     * @return the number of read bytes
     */
    public long getBytesRead() {
        return bytesRead;
    }

    public CanReportProgress getProgressReporter(final long totalSize) {
        return new CanReportProgress() {
            @Override
            public double getProgress() {
                return 1.0 * bytesRead / totalSize;
            }

            @Override
            public boolean isFinished() {
                return bytesRead == totalSize || closed;
            }
        };
    }
}
