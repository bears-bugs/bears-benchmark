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
import com.milaboratory.core.io.sequence.PairedRead;
import com.milaboratory.core.io.sequence.PairedSequenceWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class PairedFastqWriter implements PairedSequenceWriter {
    SingleFastqWriter[] writers;

    public PairedFastqWriter(File file1, File file2) throws IOException {
        this(new SingleFastqWriter(file1), new SingleFastqWriter(file2));
    }

    public PairedFastqWriter(String file1, String file2) throws IOException {
        this(new SingleFastqWriter(file1), new SingleFastqWriter(file2));
    }

    public PairedFastqWriter(String file1, String file2, QualityFormat qualityFormat, CompressionType ct) throws IOException {
        this(new SingleFastqWriter(new FileOutputStream(file1), qualityFormat, ct, SingleFastqWriter.DEFAULT_BUFFER_SIZE),
                new SingleFastqWriter(new FileOutputStream(file2), qualityFormat, ct, SingleFastqWriter.DEFAULT_BUFFER_SIZE));
    }

    public PairedFastqWriter(SingleFastqWriter writer1, SingleFastqWriter writer2) {
        this.writers = new SingleFastqWriter[]{writer1, writer2};
    }

    @Override
    public void write(PairedRead read) {
        writers[0].write(read.getR1());
        writers[1].write(read.getR2());
    }

    @Override
    public void flush() {
        RuntimeException ex = null;

        for (SingleFastqWriter writer : writers)
            try {
                writer.flush();
            } catch (RuntimeException e) {
                ex = e;
            }

        if (ex != null)
            throw ex;
    }

    @Override
    public void close() {
        RuntimeException ex = null;

        for (SingleFastqWriter writer : writers)
            try {
                writer.close();
            } catch (RuntimeException e) {
                ex = e;
            }

        if (ex != null)
            throw ex;
    }
}
