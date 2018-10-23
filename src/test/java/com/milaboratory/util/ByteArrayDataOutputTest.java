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

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well44497b;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteArrayDataOutputTest {
    @Test
    public void randomTest0() throws IOException {
        Object[] values = new Object[100000];
        RandomGenerator rg = new Well44497b();
        RandomDataGenerator rdg = new RandomDataGenerator(rg);
        ByteArrayDataOutput d = new ByteArrayDataOutput();

        for (int i = 0; i < values.length; i++) {
            switch (rg.nextInt(9)) {
                case 0:
                    double v = rg.nextDouble();
                    values[i] = v;
                    d.writeDouble(v);
                    break;
                case 1:
                    float v1 = (float) rg.nextDouble();
                    values[i] = v1;
                    d.writeFloat(v1);
                    break;
                case 2:
                    long l = rg.nextLong();
                    values[i] = l;
                    d.writeLong(l);
                    break;
                case 3:
                    int i1 = rg.nextInt();
                    values[i] = i1;
                    d.writeInt(i1);
                    break;
                case 4:
                    short i2 = (short) rg.nextInt();
                    values[i] = i2;
                    d.writeShort(i2);
                    break;
                case 5:
                    byte b = (byte) rg.nextInt();
                    values[i] = b;
                    d.writeByte(b);
                    break;
                case 6:
                    char c = (char) rg.nextInt();
                    values[i] = c;
                    d.writeChar(c);
                    break;
                case 7:
                    boolean b1 = rg.nextBoolean();
                    values[i] = b1;
                    d.writeBoolean(b1);
                    break;
                case 8:
                    String s = rdg.nextHexString(rg.nextInt(600) + 1) + " Юникод наше все!";
                    values[i] = s;
                    d.writeUTF(s);
                    break;
            }
        }

        ByteBufferDataInputAdapter a = new ByteBufferDataInputAdapter(ByteBuffer.wrap(d.getBuffer()));

        for (int i = 0; i < values.length; i++) {
            Object o = values[i];
            if (o instanceof Double)
                Assert.assertEquals(o, a.readDouble());
            else if (o instanceof Float)
                Assert.assertEquals(o, a.readFloat());
            else if (o instanceof Long)
                Assert.assertEquals(o, a.readLong());
            else if (o instanceof Integer)
                Assert.assertEquals(o, a.readInt());
            else if (o instanceof Short)
                Assert.assertEquals(o, a.readShort());
            else if (o instanceof Byte)
                Assert.assertEquals(o, a.readByte());
            else if (o instanceof Character)
                Assert.assertEquals(o, a.readChar());
            else if (o instanceof Boolean)
                Assert.assertEquals(o, a.readBoolean());
            else if (o instanceof String)
                Assert.assertEquals(o, a.readUTF());
            else
                throw new IllegalArgumentException();
        }
    }
}