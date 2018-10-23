/*
 * Copyright 2016 MiLaboratory.com
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
package com.milaboratory.core.io.sequence.fasta;

import com.milaboratory.core.Range;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.core.sequence.SequenceBuilder;
import com.milaboratory.core.sequence.provider.SequenceProvider;
import com.milaboratory.core.sequence.provider.SequenceProviderIndexOutOfBoundsException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.milaboratory.core.io.sequence.fasta.RandomAccessFastaIndex.extractFilePosition;
import static com.milaboratory.core.io.sequence.fasta.RandomAccessFastaIndex.extractSkipLetters;
import static com.milaboratory.core.io.sequence.fasta.RandomAccessFastaIndex.index;

public final class RandomAccessFastaReader<S extends Sequence<S>> implements AutoCloseable {
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    private final ByteBuffer buffer;
    private final SeekableByteChannel channel;
    final RandomAccessFastaIndex index;
    final Alphabet<S> alphabet;

    public RandomAccessFastaReader(String file, Alphabet<S> alphabet) {
        this(Paths.get(file), alphabet);
    }

    public RandomAccessFastaReader(Path file, Alphabet<S> alphabet) {
        this(file, index(file), alphabet);
    }

    public RandomAccessFastaReader(String file, Alphabet<S> alphabet, boolean saveIndexFile) {
        this(Paths.get(file), alphabet, saveIndexFile);
    }

    public RandomAccessFastaReader(Path file, Alphabet<S> alphabet, boolean saveIndexFile) {
        this(file, index(file, saveIndexFile), alphabet);
    }

    public RandomAccessFastaReader(Path file, RandomAccessFastaIndex index, Alphabet<S> alphabet) {
        this(openChannel(file), index, alphabet, DEFAULT_BUFFER_SIZE);
    }

    public RandomAccessFastaReader(SeekableByteChannel channel, RandomAccessFastaIndex index, Alphabet<S> alphabet,
                                   int bufferSize) {
        this.channel = channel;
        this.index = index;
        this.alphabet = alphabet;
        this.buffer = ByteBuffer.allocate(bufferSize);
    }

    public Alphabet<S> getAlphabet() {
        return alphabet;
    }

    public RandomAccessFastaIndex getIndex() {
        return index;
    }

    public SequenceProvider<S> getSequenceProvider(int id) {
        return getSequenceProvider(index.getRecordByIndex(id));
    }

    public SequenceProvider<S> getSequenceProvider(String id) {
        return getSequenceProvider(index.getRecordByIdCheck(id));
    }

    public S getSequence(int id, Range range) {
        return read(index.getRecordByIndex(id), range);
    }

    public S getSequence(String id, Range range) {
        return read(index.getRecordByIdCheck(id), range);
    }

    private SequenceProvider<S> getSequenceProvider(final RandomAccessFastaIndex.IndexRecord record) {
        return new SequenceProvider<S>() {
            @Override
            public int size() {
                return (int) record.getLength();
            }

            @Override
            public S getRegion(Range range) {
                return read(record, range);
            }
        };
    }

    private synchronized S read(RandomAccessFastaIndex.IndexRecord record, Range range) {
        if (range.getUpper() > record.getLength())
            throw new SequenceProviderIndexOutOfBoundsException(range.intersection(new Range(0, (int) record.getLength())));
        try {
            long qResult = record.queryPosition(range.getLower());
            channel.position(extractFilePosition(qResult));
            SequenceBuilder<S> builder = alphabet.createBuilder().ensureCapacity(range.length());
            int toSkip = extractSkipLetters(qResult);
            int toRead = range.length();
            byte b;

            while (toSkip > 0 || toRead > 0) {
                // Reading chunk from file
                buffer.clear();
                channel.read(buffer);
                buffer.flip();
                while (buffer.hasRemaining() && (toSkip > 0 || toRead > 0)) {
                    b = buffer.get();

                    // Skipping delimiters
                    if (b == '\n' || b == '\r')
                        continue;

                    // Processing letters
                    if (toSkip > 0)
                        --toSkip;
                    else {
                        builder.append(alphabet.symbolToCode((char) b));
                        --toRead;
                    }
                }
            }

            assert builder.size() == range.length();

            S seq = builder.createAndDestroy();

            // Returning result, invert if required
            return seq.getRange(range.move(-range.getLower()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        this.channel.close();
    }

    private static SeekableByteChannel openChannel(Path file) {
        try {
            return FileChannel.open(file, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
