/*
 * Copyright 2014-2018 Web Firm Framework
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
package com.webfirmframework.wffweb.streamer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.webfirmframework.wffweb.util.data.NameValue;

public class WffBinaryMessageOutputStreamerTest {

    //@formatter:off
    @Test
    public void testWithoutChunkSize() {
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WffBinaryMessageOutputStreamer streamer = new WffBinaryMessageOutputStreamer(os);
        
        
        int total = 0;
        try {
            List<NameValue> nameValues = new LinkedList<NameValue>();
            nameValues.add(new NameValue("key1".getBytes(), new byte[][]{"value1".getBytes()}));
            nameValues.add(new NameValue("key3".getBytes(), new byte[][]{"value3".getBytes(), "value41".getBytes()}));
            nameValues.add(new NameValue("key4".getBytes(), new byte[0][0]));
            nameValues.add(new NameValue("key2".getBytes(), new byte[][]{"value2".getBytes()}));
            nameValues.add(new NameValue("key5".getBytes(), new byte[0][0]));
            for (NameValue nameValue : nameValues) {
                total += streamer.write(nameValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] actualMessage = os.toByteArray();
     
        
        byte[] expectedMessage = { 4, 4,
                //key length                    value length
                0, 0, 0, 4, 'k', 'e', 'y', '1', 0, 0, 0, 10, 0, 0, 0, 6, 'v', 'a', 'l', 'u', 'e', '1', 
                0, 0, 0, 4, 'k', 'e', 'y', '3', 0, 0, 0, 21, 0, 0, 0, 6, 'v', 'a', 'l', 'u', 'e', '3', 0, 0, 0, 7, 'v', 'a', 'l', 'u', 'e', '4', '1', 
                0, 0, 0, 4, 'k', 'e', 'y', '4', 0, 0, 0, 0,
                0, 0, 0, 4, 'k', 'e', 'y', '2', 0, 0, 0, 10, 0, 0, 0, 6, 'v', 'a', 'l', 'u', 'e', '2',
                0, 0, 0, 4, 'k', 'e', 'y', '5', 0, 0, 0, 0,
                };
        
       assertArrayEquals(expectedMessage, actualMessage);
       assertEquals(expectedMessage.length, total);
    }
    
    @Test
    public void testChunkSize() {
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WffBinaryMessageOutputStreamer streamer = new WffBinaryMessageOutputStreamer(os);
        streamer.setChunkSize(1);
        
        int total = 0;
        try {
            List<NameValue> nameValues = new LinkedList<NameValue>();
            nameValues.add(new NameValue("key1".getBytes(), new byte[][]{"value1".getBytes()}));
            nameValues.add(new NameValue("key3".getBytes(), new byte[][]{"value3".getBytes(), "value41".getBytes()}));
            nameValues.add(new NameValue("key4".getBytes(), new byte[0][0]));
            nameValues.add(new NameValue("key2".getBytes(), new byte[][]{"value2".getBytes()}));
            nameValues.add(new NameValue("key5".getBytes(), new byte[0][0]));
            for (NameValue nameValue : nameValues) {
                total += streamer.write(nameValue);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] actualMessage = os.toByteArray();
     
        
        byte[] expectedMessage = { 4, 4,
                //key length                    value length
                0, 0, 0, 4, 'k', 'e', 'y', '1', 0, 0, 0, 10, 0, 0, 0, 6, 'v', 'a', 'l', 'u', 'e', '1', 
                0, 0, 0, 4, 'k', 'e', 'y', '3', 0, 0, 0, 21, 0, 0, 0, 6, 'v', 'a', 'l', 'u', 'e', '3', 0, 0, 0, 7, 'v', 'a', 'l', 'u', 'e', '4', '1', 
                0, 0, 0, 4, 'k', 'e', 'y', '4', 0, 0, 0, 0,
                0, 0, 0, 4, 'k', 'e', 'y', '2', 0, 0, 0, 10, 0, 0, 0, 6, 'v', 'a', 'l', 'u', 'e', '2',
                0, 0, 0, 4, 'k', 'e', 'y', '5', 0, 0, 0, 0,
                };
        
        
       assertArrayEquals(expectedMessage, actualMessage);
       assertEquals(expectedMessage.length, total);
    }
    //@formatter:on

}
