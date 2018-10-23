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
package com.milaboratory.primitivio;

import com.milaboratory.primitivio.test.*;
import com.milaboratory.test.TestUtil;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static com.milaboratory.primitivio.test.TestEnum1.*;

public class PrimitivIOTest {
    @Test
    public void testVarInt1() throws Exception {
        RandomGenerator rg = new Well19937c();
        final int count = TestUtil.its(100, 1000);
        int[] values = new int[count];
        for (int i = 0; i < count; ++i) {
            int bits = rg.nextInt(31);
            values[i] = rg.nextInt(0x7FFFFFFF >>> (bits));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        for (int i = 0; i < count; ++i)
            po.writeVarInt(values[i]);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (int i = 0; i < count; ++i)
            Assert.assertEquals(values[i], pi.readVarInt());
    }

    @Test
    public void testSimpleSerialization1() throws Exception {
        TestClass1 obj1 = new TestClass1(1, "Surep");
        TestClass1 obj2 = new TestSubClass2(3, "Ref");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        po.writeObject(obj1);
        po.writeObject(obj2);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        TestClass1 dobj1 = pi.readObject(TestClass1.class);
        TestClass1 dobj2 = pi.readObject(TestClass1.class);
        Assert.assertEquals(obj1, dobj1);
        Assert.assertEquals(obj2, dobj2);
    }

    @Test
    public void testSimpleSerialization2() throws Exception {
        TestClass1 obj1 = new TestClass1(1, "Surep");
        TestClass1 obj2 = new TestSubClass2(3, "Ref", obj1, obj1, null, null, new TestSubClass1(2, "DERR"));
        obj2.subObjects[3] = obj2;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        int cc = 10;
        for (int i = 0; i < cc; ++i)
            po.writeObject(obj2);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        TestClass1 dobj2;
        for (int i = 0; i < cc; ++i) {
            Assert.assertTrue(pi.knownReferences.isEmpty());
            dobj2 = pi.readObject(TestClass1.class);
            Assert.assertEquals(dobj2.i, obj2.i);
            Assert.assertEquals(dobj2.k, obj2.k);
            Assert.assertTrue(dobj2 == dobj2.subObjects[3]);
            Assert.assertNull(dobj2.subObjects[2]);
            Assert.assertTrue(dobj2.subObjects[0] == dobj2.subObjects[1]);
            Assert.assertEquals(obj2.subObjects[0], dobj2.subObjects[0]);
            Assert.assertEquals(obj2.subObjects[4], dobj2.subObjects[4]);
            Assert.assertTrue(dobj2 == dobj2.subObjects[3]);
        }
    }

    @Test
    public void testSimpleSerialization3() throws Exception {
        TestSubClass1 objr = new TestSubClass1(2, "DERR");
        TestClass1 obj1 = new TestClass1(1, "Surep", objr);
        TestClass1 obj2 = new TestSubClass2(3, "Ref", obj1, obj1, null, null, objr);
        obj2.subObjects[3] = obj2;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        int cc = 10;
        for (int i = 0; i < cc; ++i)
            po.writeObject(obj2);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        TestClass1 dobj2;
        for (int i = 0; i < cc; ++i) {
            Assert.assertTrue(pi.knownReferences.isEmpty());
            dobj2 = pi.readObject(TestClass1.class);
            Assert.assertEquals(dobj2.i, obj2.i);
            Assert.assertEquals(dobj2.k, obj2.k);
            Assert.assertTrue(dobj2 == dobj2.subObjects[3]);
            Assert.assertNull(dobj2.subObjects[2]);
            Assert.assertTrue(dobj2.subObjects[0] == dobj2.subObjects[1]);
            Assert.assertEquals(obj2.subObjects[0], dobj2.subObjects[0]);
            Assert.assertEquals(obj2.subObjects[4], dobj2.subObjects[4]);
            Assert.assertTrue(dobj2.subObjects[0].subObjects[0] == dobj2.subObjects[1].subObjects[0]);
            Assert.assertTrue(dobj2.subObjects[4] == dobj2.subObjects[1].subObjects[0]);
            Assert.assertTrue(dobj2 == dobj2.subObjects[3]);
        }
    }

    @Test
    public void testDefaultSerialization3() throws Exception {
        TestSubClass1 objr = new TestSubClass1(2, "DERR");
        TestSubClass1[] array = new TestSubClass1[100];
        for (int i = 0; i < 100; i++) {
            array[i] = objr;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        int cc = 10;
        for (int i = 0; i < cc; ++i) {
            po.writeObject(array);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (int i = 0; i < cc; ++i) {
            TestSubClass1[] darray = pi.readObject(TestSubClass1[].class);
            Assert.assertArrayEquals(array, darray);
            Assert.assertTrue(darray[0] == darray[1]);
        }
    }

    @Test
    public void testKnownReference1() throws Exception {
        TestSubClass1 objr = new TestSubClass1(2, "DERR");
        TestClass1 obj1 = new TestClass1(1, "Surep", objr);
        TestClass1 obj2 = new TestSubClass2(3, "Ref", obj1, obj1, null, null, objr);
        obj2.subObjects[3] = obj2;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        po.putKnownReference(objr);
        int cc = 10;
        for (int i = 0; i < cc; ++i) {
            po.writeObject(obj2);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        pi.putKnownReference(objr);
        TestClass1 dobj2;
        for (int i = 0; i < cc; ++i) {
            Assert.assertTrue(pi.knownReferences.size() == 1);
            dobj2 = pi.readObject(TestClass1.class);
            Assert.assertEquals(dobj2.i, obj2.i);
            Assert.assertEquals(dobj2.k, obj2.k);
            Assert.assertTrue(dobj2 == dobj2.subObjects[3]);
            Assert.assertNull(dobj2.subObjects[2]);
            Assert.assertTrue(dobj2.subObjects[0] == dobj2.subObjects[1]);
            Assert.assertTrue(objr == dobj2.subObjects[4]);
            Assert.assertTrue(objr == dobj2.subObjects[0].subObjects[0]);
            Assert.assertEquals(obj2.subObjects[4], dobj2.subObjects[4]);
            Assert.assertTrue(dobj2.subObjects[0].subObjects[0] == dobj2.subObjects[1].subObjects[0]);
            Assert.assertTrue(dobj2.subObjects[4] == dobj2.subObjects[1].subObjects[0]);
            Assert.assertTrue(dobj2 == dobj2.subObjects[3]);
        }
    }

    public static class StringWrapper {
        final String str;

        public StringWrapper(String str) {
            this.str = str;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StringWrapper)) return false;
            StringWrapper that = (StringWrapper) o;
            return Objects.equals(str, that.str);
        }

        @Override
        public int hashCode() {
            return Objects.hash(str);
        }
    }

    @Test
    public void testKnownObject1() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        po.getSerializersManager().registerCustomSerializer(StringWrapper.class, PrimitivIO.dummySerializer());
        int cc = 10;

        for (int i = 0; i < cc; ++i)
            po.putKnownObject(new StringWrapper("HiThere" + i));

        for (int i = 0; i < cc; ++i)
            po.writeObject(new StringWrapper("HiThere" + i));

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        pi.getSerializersManager().registerCustomSerializer(StringWrapper.class, PrimitivIO.dummySerializer());
        for (int i = 0; i < cc; ++i)
            pi.putKnownObject(new StringWrapper("HiThere" + i));

        StringWrapper obj;
        for (int i = 0; i < cc; ++i) {
            obj = pi.readObject(StringWrapper.class);
            Assert.assertEquals(new StringWrapper("HiThere" + i), obj);
        }
    }

    @Test
    public void testNonReferenceSerialization1() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        int cc = 10;
        for (int i = 0; i < cc; ++i) {
            po.writeObject(2);
        }

        // 1 byte for "New Object" marker + 4 bytes for integer + 1 byte for reference id
        Assert.assertEquals(cc * 6, bos.size());

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (int i = 0; i < cc; ++i) {
            Assert.assertEquals((Integer) 2, pi.readObject(Integer.class));
        }
    }

    @Test
    public void testDefaultSerialization1() throws Exception {
        RandomGenerator rg = new Well19937c();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        byte[] bytes = new byte[100];
        rg.nextBytes(bytes);
        int[] ints = new int[100];
        for (int i = 0; i < ints.length; i++)
            ints[i] = rg.nextInt(Integer.MAX_VALUE);
        int cc = 10;
        for (int i = 0; i < cc; ++i) {
            po.writeObject(bytes);
            po.writeObject(ints);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (int i = 0; i < cc; ++i) {
            Assert.assertArrayEquals(bytes, pi.readObject(byte[].class));
            Assert.assertArrayEquals(ints, pi.readObject(int[].class));
        }
    }

    @Test
    public void testDefaultSerialization2() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        TestEnum1[] vals = new TestEnum1[]{A, B, F, E, C};
        int cc = 10;
        for (int i = 0; i < cc; ++i) {
            po.writeObject(vals);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (int i = 0; i < cc; ++i)
            Assert.assertArrayEquals(vals, pi.readObject(TestEnum1[].class));
    }

    @Test
    public void testDefaultSerialization4() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        String[] vals = new String[]{"A", "Bas", "F"};
        int cc = 10;
        for (int i = 0; i < cc; ++i) {
            po.writeObject(vals);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (int i = 0; i < cc; ++i)
            Assert.assertArrayEquals(vals, pi.readObject(String[].class));
    }

    @Test
    public void testJsonSerializer1() throws Exception {
        RandomGenerator rg = new Well19937c();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        TestJsonClass1[] objs = new TestJsonClass1[100];
        for (int i = 0; i < objs.length; i++)
            objs[i] = new TestJsonClass1(rg.nextInt(), "Rand" + rg.nextInt());

        int cc = 10;
        for (int i = 0; i < cc; ++i)
            po.writeObject(objs);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (int i = 0; i < cc; ++i)
            Assert.assertArrayEquals(objs, pi.readObject(TestJsonClass1[].class));
    }

    @Test
    public void testInts() throws Exception {
        int[] values = new int[10000];
        values[0] = 0;
        values[1] = Integer.MIN_VALUE;
        values[2] = Integer.MAX_VALUE;
        for (int i = 3; i < values.length; i++)
            values[i] = ThreadLocalRandom.current().nextInt();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        for (int value : values) {
            po.writeInt(value);
            po.writeVarInt(value);
            po.writeVarIntZigZag(value);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (int value : values) {
            Assert.assertEquals(value, pi.readInt());
            Assert.assertEquals(value, pi.readVarInt());
            Assert.assertEquals(value, pi.readVarIntZigZag());
        }
    }

    @Test
    public void testLongs() throws Exception {
        long[] values = new long[10000];
        values[0] = 0;
        values[1] = Long.MIN_VALUE;
        values[2] = Long.MAX_VALUE;
        for (int i = 3; i < values.length; i++)
            values[i] = ThreadLocalRandom.current().nextLong();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO po = new PrimitivO(bos);
        for (long value : values) {
            po.writeLong(value);
            po.writeVarLong(value);
            po.writeVarLongZigZag(value);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI pi = new PrimitivI(bis);
        for (long value : values) {
            Assert.assertEquals(value, pi.readLong());
            Assert.assertEquals(value, pi.readVarLong());
            Assert.assertEquals(value, pi.readVarLongZigZag());
        }
    }
}