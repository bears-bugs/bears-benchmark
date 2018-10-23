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

import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.util.FileIndex;
import junit.framework.Assert;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well1024a;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RandomAccessFastqReaderTest {
    @Test
    public void test1() throws Exception {
        File sample = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());
        SingleFastqReader reader = new SingleFastqReader(sample);

        SingleFastqIndexer indexer = new SingleFastqIndexer(reader, 3);
        while (indexer.take() != null)
            indexer.take();
        FileIndex index = indexer.createIndex();

        reader = new SingleFastqReader(sample);
        RandomAccessFastqReader rreader = new RandomAccessFastqReader(sample, index, false, false);
        SingleRead read;
        while ((read = reader.take()) != null) {
            SingleRead actual = rreader.take(read.getId());
            Assert.assertEquals(read.getId(), actual.getId());
            Assert.assertEquals(read.getData(), actual.getData());
            Assert.assertEquals(read.getDescription(), actual.getDescription());
        }
    }

    @Test
    public void test2() throws Exception {
        File sample = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());
        SingleFastqReader reader = new SingleFastqReader(sample);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int step = 1; step < 5; ++step) {
            SingleFastqIndexer indexer = new SingleFastqIndexer(reader, step);
            while (indexer.take() != null)
                indexer.take();
            FileIndex index = indexer.createIndex();
            index.write(outputStream);
            index = FileIndex.read(new ByteArrayInputStream(outputStream.toByteArray()));

            reader = new SingleFastqReader(sample);
            RandomAccessFastqReader rreader = new RandomAccessFastqReader(sample, index, false, false);
            SingleRead read;
            while ((read = reader.take()) != null) {
                SingleRead actual = rreader.take(read.getId());
                Assert.assertEquals(read.getId(), actual.getId());
                Assert.assertEquals(read.getData(), actual.getData());
                Assert.assertEquals(read.getDescription(), actual.getDescription());
            }
        }
    }

    @Test
    public void test3() throws Exception {
        File sample = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());
        RandomGenerator rnd = new Well1024a();
        for (int step = 1; step < 5; ++step) {
            SingleRead[] reads = allReads(sample);
            FileIndex index = buildeIndex(sample, step);
            RandomAccessFastqReader rreader = new RandomAccessFastqReader(sample, index, false, true);
            for (int i = 0; i < 100; ++i) {
                int p = rnd.nextInt(reads.length);
                SingleRead actual = rreader.take(p);
                Assert.assertEquals(reads[p].getId(), actual.getId());
                Assert.assertEquals(reads[p].getData(), actual.getData());
                Assert.assertEquals(reads[p].getDescription(), actual.getDescription());
            }
        }
    }

    @Test
    public void test4() throws Exception {
        File sample = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());
        for (int step = 1; step < 5; ++step) {
            SingleRead[] reads = allReads(sample);
            FileIndex index = buildeIndex(sample, step);
            RandomAccessFastqReader rreader = new RandomAccessFastqReader(sample, index, false, true);
            SingleRead actual = rreader.take(reads.length + 1);
            Assert.assertTrue(actual == null);
            actual = rreader.take(reads.length);
            Assert.assertTrue(actual == null);
            int p = reads.length - 1;
            actual = rreader.take(p);
            Assert.assertEquals(reads[p].getId(), actual.getId());
            Assert.assertEquals(reads[p].getData(), actual.getData());
            Assert.assertEquals(reads[p].getDescription(), actual.getDescription());
        }
    }

    private static FileIndex buildeIndex(File sample, long step) throws IOException {
        SingleFastqReader reader = new SingleFastqReader(sample);
        SingleFastqIndexer indexer = new SingleFastqIndexer(reader, step);
        while (indexer.take() != null)
            indexer.take();
        return indexer.createIndex();
    }

    private static SingleRead[] allReads(File sample) throws IOException {
        ArrayList<SingleRead> reads = new ArrayList<>();
        SingleFastqReader reader = new SingleFastqReader(sample);
        SingleRead read;
        while ((read = reader.take()) != null)
            reads.add(read);
        return reads.toArray(new SingleRead[reads.size()]);
    }
}