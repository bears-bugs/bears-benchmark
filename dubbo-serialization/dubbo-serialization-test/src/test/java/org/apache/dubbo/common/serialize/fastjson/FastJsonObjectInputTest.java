/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.common.serialize.fastjson;

import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.common.serialize.model.Person;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FastJsonObjectInputTest {
    private FastJsonObjectInput fastJsonObjectInput;

    @Test
    public void testReadBool() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new ByteArrayInputStream("true".getBytes()));
        boolean result = fastJsonObjectInput.readBool();

        assertThat(result, is(true));

        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("false"));
        result = fastJsonObjectInput.readBool();

        assertThat(result, is(false));
    }

    @Test
    public void testReadByte() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new ByteArrayInputStream("123".getBytes()));
        Byte result = fastJsonObjectInput.readByte();

        assertThat(result, is(Byte.parseByte("123")));
    }

    @Test
    public void testReadBytes() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new ByteArrayInputStream("123456".getBytes()));
        byte[] result = fastJsonObjectInput.readBytes();

        assertThat(result, is("123456".getBytes()));
    }

    @Test
    public void testReadShort() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("1"));
        short result = fastJsonObjectInput.readShort();

        assertThat(result, is((short) 1));
    }

    @Test
    public void testReadInt() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("1"));
        Integer result = fastJsonObjectInput.readInt();

        assertThat(result, is(1));
    }

    @Test
    public void testReadDouble() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("1.88"));
        Double result = fastJsonObjectInput.readDouble();

        assertThat(result, is(1.88d));
    }

    @Test
    public void testReadLong() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("10"));
        Long result = fastJsonObjectInput.readLong();

        assertThat(result, is(10L));
    }

    @Test
    public void testReadFloat() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("1.66"));
        Float result = fastJsonObjectInput.readFloat();

        assertThat(result, is(1.66F));
    }

    @Test
    public void testReadUTF() throws IOException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("\"wording\""));
        String result = fastJsonObjectInput.readUTF();

        assertThat(result, is("wording"));
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("{ \"name\":\"John\", \"age\":30 }"));
        Person result = fastJsonObjectInput.readObject(Person.class);

        assertThat(result, not(nullValue()));
        assertThat(result.getName(), is("John"));
        assertThat(result.getAge(), is(30));
    }

    @Test(expected = EOFException.class)
    public void testEmptyLine() throws IOException, ClassNotFoundException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader(""));

        fastJsonObjectInput.readObject();
    }

    @Test(expected = EOFException.class)
    public void testEmptySpace() throws IOException, ClassNotFoundException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("  "));

        fastJsonObjectInput.readObject();
    }

    @Test
    public void testReadObjectWithoutClass() throws IOException, ClassNotFoundException {
        fastJsonObjectInput = new FastJsonObjectInput(new StringReader("{ \"name\":\"John\", \"age\":30 }"));

        JSONObject readObject = (JSONObject) fastJsonObjectInput.readObject();

        assertThat(readObject, not(nullValue()));
        assertThat(readObject.getString("name"), is("John"));
        assertThat(readObject.getInteger("age"), is(30));
    }
}