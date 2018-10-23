/*
 * Copyright 2017 MiLaboratory.com
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

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.OutputPort;
import cc.redberry.pipe.OutputPortCloseable;
import cc.redberry.pipe.util.Chunk;
import cc.redberry.pipe.util.CountLimitingOutputPort;
import gnu.trove.list.array.TLongArrayList;
import org.apache.commons.io.output.CloseShieldOutputStream;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.io.*;
import java.util.*;

/**
 * Created by dbolotin on 04/04/2017.
 */
public class Randomizer<T> {
    private final OutputPort<T> initialSource;
    private final RandomDataGenerator random;
    private final int chunkSize;
    private final ObjectSerializer<T> serializer;
    private final File tempFile;
    private final TLongArrayList chunkOffsets = new TLongArrayList();
    private int lastChunkSize = -1;

    Randomizer(OutputPort<T> initialSource, RandomDataGenerator random, int chunkSize,
               ObjectSerializer<T> serializer, File tempFile) {
        this.initialSource = initialSource;
        this.random = random;
        this.chunkSize = chunkSize;
        this.serializer = serializer;
        this.tempFile = tempFile;
    }

    public static <T> OutputPortCloseable<T> randomize(
            OutputPort<T> initialSource,
            RandomDataGenerator random,
            int chunkSize,
            ObjectSerializer<T> serializer,
            File tempFile) throws IOException {
        Randomizer<T> sorter = new Randomizer<>(initialSource, random, chunkSize, serializer, tempFile);
        sorter.build();
        return sorter.getRandomized();
    }

    void build() throws IOException {
        try (CountingOutputStream output = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile), 1024 * 1024))) {
            OutputPort<Chunk<T>> chunked = CUtils.buffered(CUtils.chunked(initialSource, chunkSize), 1);
            Chunk<T> chunk;
            while ((chunk = chunked.take()) != null) {
                Object[] data = chunk.toArray();
                data = random.nextSample(Arrays.asList(data), data.length);
                chunkOffsets.add(output.getByteCount());
                serializer.write((Collection) Arrays.asList(data), new CloseShieldOutputStream(output));
                lastChunkSize = data.length;
            }
        }
    }

    OutputPortCloseable<T> getRandomized() throws IOException {
        return new RandomizingPort();
    }

    private final class RandomizingPort implements OutputPortCloseable<T> {
        final List<OutputPortCloseable<T>> blocks = new ArrayList<>();

        public RandomizingPort() throws IOException {
            for (int i = 0; i < chunkOffsets.size(); i++) {
                final FileInputStream fo = new FileInputStream(tempFile);
                // Setting file position to the beginning of the chunkId-th chunk
                fo.getChannel().position(chunkOffsets.get(i));
                OutputPortCloseable<T> block = new CountLimitingOutputPort<>(
                        serializer.read(new DataInputStream(new BufferedInputStream(fo, 16384))),
                        i == chunkOffsets.size() - 1 ? lastChunkSize : chunkSize);
                blocks.add(block);
            }
        }

        @Override
        public synchronized T take() {
            while (true) {
                if (blocks.isEmpty()) {
                    close();
                    return null;
                }

                int id = blocks.size() == 1 ? 0 : random.nextInt(0, blocks.size() - 1);
                T obj = blocks.get(id).take();
                if (obj == null) {
                    blocks.remove(id);
                    continue;
                }
                return obj;
            }
        }

        private boolean closed = false;

        @Override
        public synchronized void close() {
            if (closed)
                return;
            for (OutputPortCloseable<T> block : blocks)
                block.close();
            blocks.clear();

            tempFile.delete();
            closed = true;
        }
    }
}
