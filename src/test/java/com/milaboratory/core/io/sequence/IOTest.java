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
package com.milaboratory.core.io.sequence;

import cc.redberry.pipe.CUtils;
import com.milaboratory.core.io.sequence.fastq.PairedFastqReader;
import com.milaboratory.core.io.sequence.fastq.SingleFastqReader;
import com.milaboratory.core.io.sequence.fastq.SingleFastqReaderTest;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IOTest {
    @Test
    public void sequenceReadIOTestS() throws Exception {
        File r1 = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());

        List<SequenceRead> reads = new ArrayList<>();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO o = new PrimitivO(bos);
        try (SingleFastqReader reader = new SingleFastqReader(r1)) {
            for (SingleRead r : CUtils.it(reader)) {
                reads.add(r);
                o.writeObject(r);
            }
        }

        Assert.assertTrue(bos.size() > 1000);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI i = new PrimitivI(bis);
        try (SingleFastqReader reader = new SingleFastqReader(r1)) {
            for (SingleRead r : CUtils.it(reader)) {
                reads.add(r);
                SingleRead read = i.readObject(SingleRead.class);
                Assert.assertEquals(r.getData(), read.getData());
                Assert.assertEquals(r.getDescription(), read.getDescription());
            }
        }
    }

    @Test
    public void sequenceReadIOTestP() throws Exception {
        File r1 = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());
        File r2 = new File(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r2.fastq").toURI());

        List<SequenceRead> reads = new ArrayList<>();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO o = new PrimitivO(bos);
        try (PairedFastqReader reader = new PairedFastqReader(r1, r2)) {
            for (PairedRead r : CUtils.it(reader)) {
                reads.add(r);
                o.writeObject(r);
            }
        }

        Assert.assertTrue(bos.size() > 1000);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI i = new PrimitivI(bis);
        try (PairedFastqReader reader = new PairedFastqReader(r1, r2)) {
            for (PairedRead r : CUtils.it(reader)) {
                reads.add(r);
                PairedRead read = i.readObject(PairedRead.class);
                Assert.assertEquals(r.getR1().getData(), read.getR1().getData());
                Assert.assertEquals(r.getR1().getDescription(), read.getR1().getDescription());
                Assert.assertEquals(r.getR2().getData(), read.getR2().getData());
                Assert.assertEquals(r.getR2().getDescription(), read.getR2().getDescription());
            }
        }
    }
}