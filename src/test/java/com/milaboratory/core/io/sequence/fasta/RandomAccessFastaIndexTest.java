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

import com.milaboratory.util.AbstractLongProcessReporter;
import com.milaboratory.util.LongProcessReporter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class RandomAccessFastaIndexTest {
    @Test(expected = IllegalStateException.class)
    public void test1() throws Exception {
        RandomAccessFastaIndex.IndexBuilder builder = new RandomAccessFastaIndex.IndexBuilder(1024);
        builder.setLastRecordLength(12);
    }

    @Test(expected = IllegalStateException.class)
    public void test2() throws Exception {
        RandomAccessFastaIndex.IndexBuilder builder = new RandomAccessFastaIndex.IndexBuilder(1024);
        builder.addRecord("Record1", 343L);
        builder.addRecord("Record2", 3445L);
    }

    @Test
    public void test3() throws Exception {
        RandomAccessFastaIndex.IndexBuilder builder = new RandomAccessFastaIndex.IndexBuilder(1024);
        builder.addRecord("Record1", 13L);
        builder.addIndexPoint(13L + 1024 + 10);
        builder.setLastRecordLength(1700L);

        builder.addRecord("Record2", 2013L);
        builder.addIndexPoint(2013L + 1024 + 13);
        builder.addIndexPoint(2013L + 1024 * 2 + 24);
        builder.setLastRecordLength(2700L);

        RandomAccessFastaIndex index = builder.build();

        Assert.assertEquals("Record1", index.getRecordByIndex(0).getDescription());
        Assert.assertEquals("Record2", index.getRecordByIndex(1).getDescription());

        Assert.assertEquals(1700, index.getRecordByIndex(0).getLength());
        Assert.assertEquals(2700, index.getRecordByIndex(1).getLength());

        Assert.assertEquals((13L + 1024 + 10) << RandomAccessFastaIndex.FILE_POSITION_OFFSET, index.getRecordByIndex(0).queryPosition(1024));
        Assert.assertEquals(((13L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 1023, index.getRecordByIndex(0).queryPosition(1023));
        Assert.assertEquals(((13L + 1024 + 10) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 12, index.getRecordByIndex(0).queryPosition(1024 + 12));

        Assert.assertEquals((2013L + 1024 + 13) << RandomAccessFastaIndex.FILE_POSITION_OFFSET, index.getRecordByIndex(1).queryPosition(1024));
        Assert.assertEquals(((2013L + 1024 + 13) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 6, index.getRecordByIndex(1).queryPosition(1030));
        Assert.assertEquals(((2013L + 1024 * 2 + 24) << RandomAccessFastaIndex.FILE_POSITION_OFFSET), index.getRecordByIndex(1).queryPosition(2048));
    }

    @Test
    public void test4() throws Exception {
        RandomAccessFastaIndex.StreamIndexBuilder builder = new RandomAccessFastaIndex.StreamIndexBuilder(4);
        String lineBreak = "\r\n";
        // 0
        builder.processBuffer(">record1" + lineBreak);
        // 10
        builder.processBuffer("ATTAGACAGACATATATGCA" + lineBreak);
        // 32
        builder.processBuffer("ATTAGACAGACAACC" + lineBreak);
        // 49

        RandomAccessFastaIndex index = builder.build();

        Assert.assertEquals(1, index.size());
        Assert.assertEquals("record1", index.getRecordByIndex(0).getDescription());
        Assert.assertEquals(35, index.getRecordByIndex(0).getLength());

        Assert.assertEquals(((10L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 0, index.getRecordByIndex(0).queryPosition(0));
        Assert.assertEquals(((32L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 0, index.getRecordByIndex(0).queryPosition(20));
        Assert.assertEquals(((26L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 3, index.getRecordByIndex(0).queryPosition(19));
    }

    @Test
    public void test5() throws Exception {
        RandomAccessFastaIndex.StreamIndexBuilder builder = new RandomAccessFastaIndex.StreamIndexBuilder(4);
        String lineBreak = "\r\n";
        // 0
        builder.processBuffer(">record1" + lineBreak);
        // 10
        builder.processBuffer(lineBreak);
        // 12
        builder.processBuffer("ATTAGACAGACATATATGCA" + lineBreak);
        // 34
        builder.processBuffer("ATTAGACAGACAACC" + lineBreak);
        // 51

        RandomAccessFastaIndex index = builder.build();

        Assert.assertEquals(1, index.size());
        Assert.assertEquals("record1", index.getRecordByIndex(0).getDescription());
        Assert.assertEquals(35, index.getRecordByIndex(0).getLength());

        Assert.assertEquals(((12L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 0, index.getRecordByIndex(0).queryPosition(0));
        Assert.assertEquals(((34L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 0, index.getRecordByIndex(0).queryPosition(20));
        Assert.assertEquals(((28L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 3, index.getRecordByIndex(0).queryPosition(19));
    }

    @Test
    public void test6() throws Exception {
        RandomAccessFastaIndex.StreamIndexBuilder builder = new RandomAccessFastaIndex.StreamIndexBuilder(4);
        String lineBreak = "\r\n";
        // 0
        builder.processBuffer(">record1" + lineBreak);
        // 10
        builder.processBuffer("ATTAGACAGACATATATGCA" + lineBreak);
        // 32
        builder.processBuffer("ATTAGACAGACAACC" + lineBreak);
        // 49
        builder.processBuffer(">record2" + lineBreak);
        // 59
        builder.processBuffer("ATTAGACAGACATATATGCA" + lineBreak);
        // 71
        builder.processBuffer("ATTAGACAGAC" + lineBreak);

        RandomAccessFastaIndex index = builder.build();

        Assert.assertEquals(2, index.size());
        Assert.assertEquals("record1", index.getRecordByIndex(0).getDescription());
        Assert.assertEquals(35, index.getRecordByIndex(0).getLength());
        Assert.assertEquals("record2", index.getRecordByIndex(1).getDescription());
        Assert.assertEquals(31, index.getRecordByIndex(1).getLength());

        Assert.assertEquals(((10L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 0, index.getRecordByIndex(0).queryPosition(0));
        Assert.assertEquals(((32L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 0, index.getRecordByIndex(0).queryPosition(20));
        Assert.assertEquals(((26L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 3, index.getRecordByIndex(0).queryPosition(19));

        Assert.assertEquals(((49L + 10L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 0, index.getRecordByIndex(1).queryPosition(0));
        Assert.assertEquals(((49L + 32L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 0, index.getRecordByIndex(1).queryPosition(20));
        Assert.assertEquals(((49L + 26L) << RandomAccessFastaIndex.FILE_POSITION_OFFSET) | 3, index.getRecordByIndex(1).queryPosition(19));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        index.write(bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        RandomAccessFastaIndex deserialized = RandomAccessFastaIndex.read(bis);
        Assert.assertEquals(index, deserialized);
    }

    //@Test
    //public void name() throws Exception {
    //    long size = 1000000000;
    //    size = 100000000;
    //    long step = size / 131072;
    //    if (step < 128)
    //        step = 128;
    //    int iStep = 1 << (63 - numberOfLeadingZeros(step) + (bitCount(step) > 1 ? 1 : 0));
    //    System.out.println(iStep);
    //    System.out.println(size / iStep);
    //}
}