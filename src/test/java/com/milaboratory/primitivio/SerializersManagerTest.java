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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SerializersManagerTest {
    @Test
    public void testRoot1() throws Exception {
        assertEquals(TestClass1.class, SerializersManager.findRoot(TestSubClass1.class));
        assertEquals(TestClass1.class, SerializersManager.findRoot(TestSubClass2.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRoot2() throws Exception {
        System.out.println(SerializersManager.findRoot(TestSubClass3.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRoot3() throws Exception {
        System.out.println(SerializersManager.findRoot(TestSubClass4.class));
    }

    @Test
    public void test1() throws Exception {
        SerializersManager manager = new SerializersManager();
        Serializer serializer = manager.getSerializer(TestSubClass2.class);
        assertEquals(CustomSerializerImpl.class, serializer.getClass());
        assertTrue(serializer == manager.getSerializer(TestSubSubClass1.class));
        assertTrue(serializer == manager.getSerializer(TestSubClass1.class));
    }
}