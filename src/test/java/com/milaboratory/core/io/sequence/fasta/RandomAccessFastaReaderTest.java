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

import cc.redberry.pipe.CUtils;
import com.milaboratory.core.Range;
import com.milaboratory.core.io.sequence.fastq.SingleFastqReaderTest;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.AminoAcidSequence;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.test.TestUtil;
import com.milaboratory.util.AbstractLongProcessReporter;
import com.milaboratory.util.LongProcessReporter;
import com.milaboratory.util.RandomUtil;
import com.milaboratory.util.TempFileManager;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomAccessFastaReaderTest {
    @Before
    public void setUp() throws Exception {
        LongProcessReporter.DefaultLongProcessReporter.INSTANCE = AbstractLongProcessReporter.stderrReporter();
    }


    @Test
    public void test1() throws Exception {
        Path path = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/some_fasta.fasta").toURI()).toPath();
        List<FastaRecord<AminoAcidSequence>> seqs = new ArrayList<>();

        try (FastaReader<AminoAcidSequence> r = new FastaReader<>(path.toFile(), AminoAcidSequence.ALPHABET)) {
            for (FastaRecord<AminoAcidSequence> rec : CUtils.it(r))
                seqs.add(rec);
        }

        assertRA(seqs, path, AminoAcidSequence.ALPHABET, false);
    }

    @Test
    public void test2() throws Exception {
        File tempFile = TempFileManager.getTempFile();
        List<FastaRecord<NucleotideSequence>> seqs = new ArrayList<>();
        Well19937c r = RandomUtil.getThreadLocalRandom();
        try (FastaWriter<NucleotideSequence> writer = new FastaWriter<>(tempFile)) {
            for (int i = 0; i < 100; i++) {
                FastaRecord<NucleotideSequence> rec = new FastaRecord<>(i, UUID.randomUUID().toString(), TestUtil.randomSequence(NucleotideSequence.ALPHABET, 1000, 100000));
                seqs.add(rec);
                writer.write(rec);
            }
        }

        assertRA(seqs, tempFile.toPath(), NucleotideSequence.ALPHABET, true);
    }

    public static <S extends Sequence<S>> void assertRA(List<FastaRecord<S>> seqs, Path path, Alphabet<S> alphabet, boolean allowReverse) throws Exception {
        try (RandomAccessFastaReader<S> raReader = new RandomAccessFastaReader<>(path, alphabet)) {
            ThreadLocalRandom r = ThreadLocalRandom.current();

            for (int i = 0; i < 1000; i++) {
                FastaRecord<S> rec = seqs.get(r.nextInt(seqs.size()));
                int from = r.nextInt(rec.getSequence().size() - 1);
                int to = allowReverse ? r.nextInt(rec.getSequence().size() - 1) : r.nextInt(from, rec.getSequence().size());
                Range range = new Range(from, to);
                Assert.assertEquals(rec.getSequence().getRange(range), raReader.getSequence((int) rec.getId(), range));
                Assert.assertEquals(rec.getSequence().getRange(range), raReader.getSequence(rec.getDescription(), range));
            }
        }
    }
}