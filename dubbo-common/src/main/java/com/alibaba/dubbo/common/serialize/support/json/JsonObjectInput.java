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
package com.alibaba.dubbo.common.serialize.support.json;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.utils.PojoUtils;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * JsonObjectInput
 */
@Deprecated
public class JsonObjectInput implements ObjectInput {

    private final BufferedReader reader;

    public JsonObjectInput(InputStream in) {
        this(new InputStreamReader(in));
    }

    public JsonObjectInput(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public boolean readBool() throws IOException {
        try {
            return readObject(boolean.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public byte readByte() throws IOException {
        try {
            return readObject(byte.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public short readShort() throws IOException {
        try {
            return readObject(short.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public int readInt() throws IOException {
        try {
            return readObject(int.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public long readLong() throws IOException {
        try {
            return readObject(long.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public float readFloat() throws IOException {
        try {
            return readObject(float.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public double readDouble() throws IOException {
        try {
            return readObject(double.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String readUTF() throws IOException {
        try {
            return readObject(String.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public byte[] readBytes() throws IOException {
        return readLine().getBytes();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        try {
            String json = readLine();
            if (json.startsWith("{")) {
                return JSON.parse(json, Map.class);
            } else {
                json = "{\"value\":" + json + "}";

                @SuppressWarnings("unchecked")
                Map<String, Object> map = JSON.parse(json, Map.class);
                return map.get("value");
            }
        } catch (ParseException e) {
            throw new IOException(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        Object value = readObject();
        return (T) PojoUtils.realize(value, cls);
        /*try {
            return JSON.parse(readLine(), cls);
        } catch (ParseException e) {
            throw new IOException(e.getMessage());
        }*/
    }

    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        Object value = readObject();
        return (T) PojoUtils.realize(value, cls, type);
    }

    private String readLine() throws IOException, EOFException {
        String line = reader.readLine();
        if (line == null || line.trim().length() == 0) throw new EOFException();
        return line;
    }
}