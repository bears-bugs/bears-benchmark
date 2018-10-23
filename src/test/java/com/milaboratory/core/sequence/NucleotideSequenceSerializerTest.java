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
package com.milaboratory.core.sequence;

import com.milaboratory.core.io.util.IOTestUtil;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.test.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class NucleotideSequenceSerializerTest {
    @Test
    public void test1() throws Exception {
        NucleotideSequence[] seqs = new NucleotideSequence[100];
        for (int i = 0; i < seqs.length; i++)
            seqs[i] = TestUtil.randomSequence(NucleotideSequence.ALPHABET, 100, 200);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        for (int i = 0; i < seqs.length; i++)
            po.writeObject(seqs[i]);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);

        for (int i = 0; i < seqs.length; i++)
            Assert.assertEquals(seqs[i], pi.readObject(NucleotideSequence.class));
    }

    @Test
    public void test2() throws Exception {
        Object se = new NucleotideSequence("AACCTTAAACC");
        IOTestUtil.assertJavaSerialization(se);
    }

    @Test
    public void test3() throws Exception {
        NucleotideSequence se = new NucleotideSequence("AACCTTAAACC");
        TestUtil.assertJson(se);
    }
}