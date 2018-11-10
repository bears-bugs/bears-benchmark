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
* @author WFF
*/
package com.webfirmframework.wffweb.css.css3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class AlignContentTest {

    @Test
    public void testGetCssName() {
        assertEquals("align-content", AlignContent.CENTER.getCssName());
    }

    @Test
    public void testGetCssValue() {
        assertEquals("center", AlignContent.CENTER.getCssValue());
    }

    @Test
    public void testToString() {
        assertEquals("align-content: center", AlignContent.CENTER.toString());
    }

    @Test
    public void testIsValid() {
        
        try {
            AlignContent[] values = AlignContent.values();
            for (AlignContent alignContent : values) {
                assertTrue(AlignContent.isValid(alignContent.getCssValue()));
            }
            assertFalse(AlignContent.isValid("one"));
            assertFalse(AlignContent.isValid("another"));
            assertFalse(AlignContent.isValid("testtttttttttttttttttttttttt"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not yet implemented");
        }
    }

    @Test
    public void testGetThis() {
        AlignContent center = AlignContent.getThis("center");
        assertNotNull(center);
        assertEquals("center", center.getCssValue());
    }

}
