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

import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AlphabetSerializerTest {
    @Test
    public void test1() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        int cc = 10;
        for (int i = 0; i < cc; i++)
            po.writeObject(NucleotideSequence.ALPHABET);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);

        for (int i = 0; i < cc; i++) {
            Alphabet actual = pi.readObject(Alphabet.class);
            Assert.assertEquals(NucleotideSequence.ALPHABET, actual);
        }
    }

    @Test
    public void test2() throws Exception {
        for (Alphabet se : Alphabets.getAll()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(128);
            new ObjectOutputStream(out).writeObject(se);
            Object de = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();
            Assert.assertTrue(se == de);
        }
    }
}