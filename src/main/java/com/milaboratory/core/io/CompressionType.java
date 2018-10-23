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
package com.milaboratory.core.io;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public enum CompressionType {
    None, GZIP, BZIP2;

    public static CompressionType detectCompressionType(File file) {
        return detectCompressionType(file.getName());
    }

    public static CompressionType detectCompressionType(String fileName) {
        fileName = fileName.trim().toLowerCase();

        if (fileName.endsWith(".gz"))
            return GZIP;
        else if (fileName.endsWith(".bz2"))
            return BZIP2;
        return None;
    }

    public InputStream createInputStream(InputStream is) throws IOException {
        return createInputStream(this, is, 2048);
    }

    public InputStream createInputStream(InputStream is, int buffer) throws IOException {
        return createInputStream(this, is, buffer);
    }

    public OutputStream createOutputStream(OutputStream os) throws IOException {
        return createOutputStream(this, os, 2048);
    }

    public OutputStream createOutputStream(OutputStream os, int buffer) throws IOException {
        return createOutputStream(this, os, buffer);
    }

    private static InputStream createInputStream(CompressionType ct, InputStream is, int buffer) throws IOException {
        switch (ct) {
            case None:
                return is;
            case GZIP:
                return new GZIPInputStream(is, buffer);
            case BZIP2:
                CompressorStreamFactory factory = new CompressorStreamFactory();
                try {
                    return factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, new BufferedInputStream(is));
                } catch (CompressorException e) {
                    throw new IOException(e);
                }
        }
        throw new NullPointerException();
    }

    private static OutputStream createOutputStream(CompressionType ct, OutputStream os, int buffer) throws IOException {
        switch (ct) {
            case None:
                return os;
            case GZIP:
                return new GZIPOutputStream(os, buffer);
            case BZIP2:
                CompressorStreamFactory factory = new CompressorStreamFactory();
                try {
                    return factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, new BufferedOutputStream(os));
                } catch (CompressorException e) {
                    throw new IOException(e);
                }
        }
        throw new NullPointerException();
    }
}
