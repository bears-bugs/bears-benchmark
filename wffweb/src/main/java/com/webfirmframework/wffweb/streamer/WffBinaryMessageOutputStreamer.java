/*
 * Copyright 2014-2018 Web Firm Framework
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
package com.webfirmframework.wffweb.streamer;

import java.io.IOException;
import java.io.OutputStream;

import com.webfirmframework.wffweb.util.WffBinaryMessageUtil;
import com.webfirmframework.wffweb.util.data.NameValue;

/**
 *
 * The default binary message version is 1.
 *
 * @author WFF
 * @since 1.1.2
 */
public class WffBinaryMessageOutputStreamer {

    private final OutputStream os;

    private boolean firstWrite = true;

    private final WffBinaryMessageVersion version;

    private int chunkSize;

    /**
     * @param os
     *            the output stream object to which the wff binary message bytes
     *            will be written
     * @since 1.0.0
     * @author WFF
     */
    public WffBinaryMessageOutputStreamer(final OutputStream os) {
        this.os = os;
        // by default it's considered as version 1.
        version = WffBinaryMessageVersion.VERSION_1;
    }

    /**
     * @param os
     *            the output stream object to which the wff binary message bytes
     *            will be written
     * @since 1.1.2
     * @author WFF
     */
    public WffBinaryMessageOutputStreamer(final OutputStream os,
            final WffBinaryMessageVersion version) {
        this.os = os;
        this.version = version;
        // by default it's considered as version 1.
    }

    /**
     * writes maximum bytes at chuckSize
     *
     * @param bytes
     * @throws IOException
     * @since 1.1.2
     * @author WFF
     */
    private void writeByChunk(final byte[] bytes) throws IOException {

        final int chunkSize = this.chunkSize;

        if (bytes.length == 0) {
            return;
        }

        if (chunkSize < bytes.length && chunkSize > 0) {

            int remaining = bytes.length;

            int offset = 0;

            while (remaining > 0) {
                if (chunkSize < remaining) {
                    os.write(bytes, offset, chunkSize);
                    remaining -= chunkSize;
                    offset += chunkSize;
                } else {
                    os.write(bytes, offset, remaining);
                    remaining = 0;
                }
            }

        } else {
            os.write(bytes);
        }
    }

    /**
     * @param nameValue
     *            the nameValue to be written to the output stream
     * @return the number of bytes written to the output stream
     * @throws IOException
     * @since 1.1.2
     * @author WFF
     */
    public int write(final NameValue nameValue) throws IOException {

        final byte maxNoNameLengthBytes = 4;
        final byte maxNoValueLengthBytes = 4;

        int totalBytesWritten = 0;

        if (firstWrite) {
            writeByChunk(
                    new byte[] { maxNoNameLengthBytes, maxNoValueLengthBytes });
            totalBytesWritten = 2;
            firstWrite = false;
        }

        final byte[] name = nameValue.getName();

        final byte[] nameLegthBytes = WffBinaryMessageUtil
                .getBytesFromInt(name.length);

        writeByChunk(nameLegthBytes);

        totalBytesWritten += nameLegthBytes.length;

        writeByChunk(name);

        totalBytesWritten += name.length;

        final byte[][] values = nameValue.getValues();

        if (values.length == 0) {
            writeByChunk(new byte[] { 0, 0, 0, 0 });
            totalBytesWritten += maxNoValueLengthBytes;
        } else {

            int valueLegth = 0;
            for (final byte[] value : values) {
                valueLegth += value.length;
            }

            valueLegth += (maxNoValueLengthBytes * values.length);

            byte[] valueLegthBytes = WffBinaryMessageUtil
                    .getBytesFromInt(valueLegth);

            writeByChunk(valueLegthBytes);

            totalBytesWritten += valueLegthBytes.length;

            for (final byte[] value : values) {

                valueLegthBytes = WffBinaryMessageUtil
                        .getBytesFromInt(value.length);

                writeByChunk(valueLegthBytes);

                totalBytesWritten += valueLegthBytes.length;

                writeByChunk(value);

                totalBytesWritten += value.length;
            }

        }

        return totalBytesWritten;
    }

    public WffBinaryMessageVersion getVersion() {
        return version;
    }

    /**
     * @param chunkSize
     *            the wff message bytes will be written maximum with the given
     *            chuck size.
     * @since 1.1.2
     * @author WFF
     */
    public void setChunkSize(final int chunkSize) {
        this.chunkSize = chunkSize;
    }

    /**
     * @return
     * @since 1.1.2
     * @author WFF
     */
    public int getChunkSize() {
        return chunkSize;
    }
}
