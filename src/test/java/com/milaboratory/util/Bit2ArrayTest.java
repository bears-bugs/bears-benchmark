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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milaboratory.util;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author dmitriybolotin
 */
public class Bit2ArrayTest {
    @Test
    public void generalTest() {
        Random r = new Random();
        for (int n = 0; n < 1000; ++n) {
            int length = r.nextInt(100);
            int[] values = new int[length];
            Bit2Array ba = new Bit2Array(length);
            for (int i = 0; i < length; ++i) {
                values[i] = r.nextInt(4);
                ba.set(i, values[i]);
            }
            assertTrue(ba.equals(ba.getRange(0, ba.size())));
            //Testing
            for (int i = 0; i < length; ++i)
                assertEquals(values[i], ba.get(i));
        }
    }

    @Test
    public void generalDoubleTest() {
        Random r = new Random();
        for (int n = 0; n < 1000; ++n) {
            int length = r.nextInt(100);
            int[] values = new int[length];
            Bit2Array ba = new Bit2Array(length);
            for (int i = 0; i < length; ++i)
                ba.set(i, r.nextInt(4));
            for (int i = 0; i < length; ++i) {
                values[i] = r.nextInt(4);
                ba.set(i, values[i]);
            }
            assertTrue(ba.equals(ba.getRange(0, ba.size())));
            //Testing
            for (int i = 0; i < length; ++i)
                assertEquals(values[i], ba.get(i));
        }
    }

    /*@Test
    public void generalTestPlusIO() throws IOException {
        Random r = new Random();
        for (int n = 0; n < 1000; ++n) {
            int length = r.nextInt(100);
            int[] values = new int[length];
            Bit2Array ba = new Bit2Array(length);
            for (int i = 0; i < length; ++i) {
                values[i] = r.nextInt(4);
                ba.set(i, values[i]);
            }
            //IO
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            Bit2ArrayIO.write(dos, ba);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            DataInputStream dis = new DataInputStream(is);
            ba = Bit2ArrayIO.read(dis);
            //Testing
            for (int i = 0; i < length; ++i)
                assertEquals(values[i], ba.get(i));
        }
    }*/
}
