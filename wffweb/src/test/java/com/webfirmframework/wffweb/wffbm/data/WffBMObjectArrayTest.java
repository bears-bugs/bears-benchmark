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
package com.webfirmframework.wffweb.wffbm.data;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class WffBMObjectArrayTest {

    @Test
    public void testWffBMObject() {

        try {
            {
                WffBMObject outerBMObject = new WffBMObject();
            }
            WffBMObject outerBMObject = new WffBMObject();
            WffBMArray bmArrayInOuterBMObject = new WffBMArray(
                    BMValueType.BM_OBJECT);
            WffBMObject bmObjectInArray = new WffBMObject();
            bmObjectInArray.put("k", BMValueType.STRING, "v");

            bmArrayInOuterBMObject.add(bmObjectInArray);
            outerBMObject.put("r", BMValueType.BM_ARRAY,
                    bmArrayInOuterBMObject);

            final WffBMObject parsed = new WffBMObject(
                    outerBMObject.build(true), true);

            WffBMArray parsedArray = (WffBMArray) parsed.getValue("r");
            final WffBMObject arrayObj = (WffBMObject) parsedArray.get(0);

            assertEquals("v", arrayObj.getValue("k"));
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception " + e.getMessage());
        }

    }
    @Test
    public void testEmptyArray() {
        
        try {            
            
            {                
                //var vv = {'r' : []};
                byte[] bm = {1, 1, 1, 0, 0, 1, 114, 3, 1, 6, 0};
                WffBMObject outerBMObject = new WffBMObject(bm);
                WffBMArray bmArray = (WffBMArray) outerBMObject.getValue("r");
                assertEquals(0, bmArray.size());
                assertEquals(null, bmArray.getValueType());
            }
            {                
                //var vv = [] i.e [1, 1, 1, 1, 0];
                byte[] bm = {1, 1, 1, 1, 0};
                WffBMArray outerBMArray = new WffBMArray(bm);
               
                assertEquals(0, outerBMArray.size());
                assertEquals(null, outerBMArray.getValueType());
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception " + e.getMessage());
        }
        
    }

}
