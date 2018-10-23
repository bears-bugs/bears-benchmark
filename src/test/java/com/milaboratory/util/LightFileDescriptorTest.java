/*
 * Copyright 2018 MiLaboratory.com
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

import com.milaboratory.core.io.sequence.fastq.SingleFastqReaderTest;
import com.milaboratory.test.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LightFileDescriptorTest {
    @Test
    public void test1() throws URISyntaxException {
        Path f = Paths.get(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());
        LightFileDescriptor d1 = LightFileDescriptor.calculate(f);
        LightFileDescriptor d2 = LightFileDescriptor.calculate(f, true, false);
        LightFileDescriptor d3 = LightFileDescriptor.calculate(f, false, true);
        LightFileDescriptor d4 = LightFileDescriptor.calculate(f, false, false);
        Assert.assertFalse(d1.checkModified(d2));
        Assert.assertFalse(d1.checkModified(d3));
        Assert.assertTrue(d2.checkModified(d3));
        Assert.assertTrue(d1.checkModified(d4));
        Assert.assertTrue(d2.checkModified(d4));
        Assert.assertTrue(d3.checkModified(d4));
        Assert.assertTrue(d4.checkModified(d4));
        TestUtil.assertPrimitivIO(d1);
        TestUtil.assertPrimitivIO(d2);
        TestUtil.assertPrimitivIO(d3);
        TestUtil.assertPrimitivIO(d4);
    }

    @Test
    public void test2() throws URISyntaxException {
        Path f = Paths.get(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());
        LightFileDescriptor d1 = LightFileDescriptor.calculate(f, true, true, 1000000);
        LightFileDescriptor d2 = LightFileDescriptor.calculate(f, true, false, 1000000);
        LightFileDescriptor d3 = LightFileDescriptor.calculate(f, false, true, 1000000);
        Assert.assertFalse(d1.checkModified(d2));
        Assert.assertFalse(d1.checkModified(d3));
        Assert.assertTrue(d2.checkModified(d3));
    }

    @Test
    public void test3() throws URISyntaxException, IOException {
        Path f = Paths.get(SingleFastqReaderTest.class.getClassLoader().getResource("sequences/sample_r1.fastq").toURI());
        LightFileDescriptor d = LightFileDescriptor.calculate(f, true, true, 1000000);
        String serialized = GlobalObjectMappers.PRETTY.writeValueAsString(d);
        LightFileDescriptor deserialized = GlobalObjectMappers.PRETTY.readValue(serialized, LightFileDescriptor.class);
        Assert.assertEquals(d, deserialized);
    }
}
