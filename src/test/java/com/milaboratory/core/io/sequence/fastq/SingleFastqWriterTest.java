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
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SingleFastqWriterTest {
    @Test
    public void test1() throws Exception {
        File sample = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());

        List<SingleRead> reads = new ArrayList<>();
        try (SingleFastqReader reader = new SingleFastqReader(sample, false)) {
            for (SingleRead read : CUtils.it(reader))
                reads.add(read);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (SingleFastqWriter writer = new SingleFastqWriter(bos)) {
            for (SingleRead read : reads)
                writer.write(read);
        }

        assertTrue(bos.size() > 800);

        try (SingleFastqReader reader = new SingleFastqReader(new ByteArrayInputStream(bos.toByteArray()))) {
            for (SingleRead read : reads)
                assertReadsEquals(read, reader.take());
            assertNull(reader.take());
        }
    }

    @Test
    public void test2() throws Exception {
        File sample = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());

        List<SingleRead> reads = new ArrayList<>();
        try (SingleFastqReader reader = new SingleFastqReader(sample, false)) {
            for (SingleRead read : CUtils.it(reader))
                reads.add(read);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (SingleFastqWriter writer = new SingleFastqWriter(bos,
                QualityFormat.Phred33, CompressionType.GZIP)) {
            for (SingleRead read : reads)
                writer.write(read);
        }

        assertTrue(bos.size() > 800);

        try (SingleFastqReader reader = new SingleFastqReader(
                new ByteArrayInputStream(bos.toByteArray()),
                QualityFormat.Phred33, CompressionType.GZIP)) {
            for (SingleRead read : reads)
                assertReadsEquals(read, reader.take());
            assertNull(reader.take());
        }
    }

    public static void assertReadsEquals(SingleRead r1, SingleRead r2) {
        assertEquals(r1.getId(), r2.getId());
        assertEquals(r1.getData(), r2.getData());
        assertEquals(r1.getDescription(), r2.getDescription());
    }
}