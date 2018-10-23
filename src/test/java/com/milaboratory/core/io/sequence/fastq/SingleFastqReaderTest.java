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

import cc.redberry.pipe.CUtils;
import com.milaboratory.core.io.CompressionType;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.util.HashFunctions;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class SingleFastqReaderTest {

    @Test
    public void test1() throws IOException, URISyntaxException {
        assertReaderOnSampleWithGZ("sample_r1.fastq");
    }

    @Test
    public void test2() throws IOException, URISyntaxException {
        assertReaderOnSampleWithGZ("sample_r2.fastq");
    }

    @Test
    public void test3() throws IOException, URISyntaxException {
        assertReaderOnSample("solexa1.fastq.gz");
    }

    @Test
    public void test4() throws IOException, URISyntaxException {
        assertReaderOnSample("solexa2.fastq.gz");
    }

    @Test
    public void test5() throws IOException, URISyntaxException {
        assertReaderOnSample("solexa3.fastq.gz");
    }

    private static void assertReaderOnSampleWithGZ(String file) throws IOException, URISyntaxException {
        for (int wc = 0; wc < 2; ++wc) {
            boolean replaceWildcards = (wc == 1);

            File sample = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/" + file).toURI());
            File sampleGZIP = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/" + file + ".gz").toURI());
            // Without wildcards replacement
            TreeSet<SingleRead> set = new TreeSet<>(SINGLE_READ_COMPARATOR);

            try (SingleFastqReader reader = new SingleFastqReader(
                    new FileInputStream(sample),
                    QualityFormat.Phred33,
                    CompressionType.None,
                    false,
                    253,
                    replaceWildcards, false)) {
                for (SingleRead read : CUtils.it(reader))
                    set.add(read);
            }

            for (int bufferSize = 253; bufferSize < 5000; ) {
                assertSameReadContent(set, new SingleFastqReader(
                        new FileInputStream(sample),
                        QualityFormat.Phred33,
                        CompressionType.None,
                        false,
                        bufferSize,
                        replaceWildcards, false));
                assertSameReadContent(set, new SingleFastqReader(
                        new FileInputStream(sample),
                        QualityFormat.Phred33,
                        CompressionType.None,
                        false,
                        bufferSize,
                        replaceWildcards, true));
                assertSameReadContent(set, new SingleFastqReader(
                        new FileInputStream(sampleGZIP),
                        QualityFormat.Phred33,
                        CompressionType.detectCompressionType(sampleGZIP),
                        false,
                        bufferSize,
                        replaceWildcards, true));
                assertSameReadContent(set, new SingleFastqReader(
                        new FileInputStream(sampleGZIP),
                        QualityFormat.Phred33,
                        CompressionType.detectCompressionType(sampleGZIP),
                        false,
                        bufferSize,
                        replaceWildcards, false));

                bufferSize += (HashFunctions.JenkinWang32shift(bufferSize) & 15) + 1;
            }
            assertSameReadContent(set, new SingleFastqReader(sample, replaceWildcards, true));
            assertSameReadContent(set, new SingleFastqReader(sampleGZIP, replaceWildcards, true));
            assertSameReadContent(set, new SingleFastqReader(sample, replaceWildcards, false));
            assertSameReadContent(set, new SingleFastqReader(sampleGZIP, replaceWildcards, false));
        }
    }

    private static void assertReaderOnSample(String file) throws IOException, URISyntaxException {
        for (int wc = 0; wc < 2; ++wc) {
            boolean replaceWildcards = (wc == 1);

            File sample = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/" + file).toURI());
            TreeSet<SingleRead> set = new TreeSet<>(SINGLE_READ_COMPARATOR);

            try (SingleFastqReader reader = new SingleFastqReader(
                    new FileInputStream(sample),
                    QualityFormat.Phred64,
                    CompressionType.GZIP,
                    false,
                    253,
                    replaceWildcards, false)) {
                for (SingleRead read : CUtils.it(reader))
                    set.add(read);
            }

            for (int bufferSize = 253; bufferSize < 5000; ) {
                assertSameReadContent(set, new SingleFastqReader(
                        new FileInputStream(sample),
                        QualityFormat.Phred64,
                        CompressionType.GZIP,
                        false,
                        bufferSize,
                        replaceWildcards, false));
                assertSameReadContent(set, new SingleFastqReader(
                        new FileInputStream(sample),
                        QualityFormat.Phred64,
                        CompressionType.GZIP,
                        false,
                        bufferSize,
                        replaceWildcards, true));

                bufferSize += (HashFunctions.JenkinWang32shift(bufferSize) & 15) + 1;
            }
            assertSameReadContent(set, new SingleFastqReader(sample, replaceWildcards, true));
            assertSameReadContent(set, new SingleFastqReader(sample, replaceWildcards, false));
        }
    }

    private static void assertSameReadContent(TreeSet<SingleRead> expected, SingleFastqReader reader) {
        try (SingleFastqReader r = reader) {
            TreeSet<SingleRead> set = new TreeSet<>(SINGLE_READ_COMPARATOR);
            for (SingleRead read : CUtils.it(r))
                set.add(read);
            Assert.assertEquals(expected, set);
        }
    }

    public static final Comparator<SingleRead> SINGLE_READ_COMPARATOR = new Comparator<SingleRead>() {
        @Override
        public int compare(SingleRead o1, SingleRead o2) {
            int c;
            if ((c = Long.compare(o1.getId(), o2.getId())) != 0)
                return c;
            if ((c = o1.getData().getSequence().compareTo(o2.getData().getSequence())) != 0)
                return c;
            if ((c = Integer.compare(o1.getData().getQuality().hashCode(), o2.getData().getQuality().hashCode())) != 0)
                return c;
            if ((c = o1.getDescription().compareTo(o2.getDescription())) != 0)
                return c;
            return 0;
        }
    };
}