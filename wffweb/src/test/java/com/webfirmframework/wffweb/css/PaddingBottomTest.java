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
package com.webfirmframework.wffweb.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class PaddingBottomTest {

    @Test
    public void testPaddingBottom() {
        PaddingBottom paddingBottom = new PaddingBottom();
        assertEquals("0px", paddingBottom.getCssValue());
    }

    @Test
    public void testPaddingBottomString() {
        {
            PaddingBottom paddingBottom = new PaddingBottom(PaddingBottom.INITIAL);
            assertEquals(PaddingBottom.INITIAL, paddingBottom.getCssValue());
        }
        {
            PaddingBottom paddingBottom = new PaddingBottom("50px");
            assertEquals("50px", paddingBottom.getCssValue());
        }
    }

    @Test
    public void testPaddingBottomPaddingBottom() {
        PaddingBottom paddingBottom = new PaddingBottom("50px");
        PaddingBottom paddingBottom1 = new PaddingBottom(paddingBottom);
        assertEquals("50px", paddingBottom1.getCssValue());
    }

    @Test
    public void testPaddingBottomFloat() {
        PaddingBottom paddingBottom = new PaddingBottom(75);
        assertEquals("75.0%", paddingBottom.getCssValue());
    }

    @Test
    public void testPaddingBottomFloatCssLengthUnit() {
        {
            PaddingBottom paddingBottom = new PaddingBottom(75, CssLengthUnit.PER);
            assertEquals("75.0%", paddingBottom.getCssValue());
            assertEquals(CssLengthUnit.PER, paddingBottom.getUnit());
            assertTrue(paddingBottom.getValue() == 75);
        }
        {
            PaddingBottom paddingBottom = new PaddingBottom(75, CssLengthUnit.CH);
            assertEquals("75.0ch", paddingBottom.getCssValue());
        }
        {
            PaddingBottom paddingBottom = new PaddingBottom(75, CssLengthUnit.EM);
            assertEquals("75.0em", paddingBottom.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            PaddingBottom paddingBottom = new PaddingBottom();
            paddingBottom.setPercent(75);
            assertEquals("75.0%", paddingBottom.getCssValue());
            assertEquals(CssLengthUnit.PER, paddingBottom.getUnit());
            assertTrue(paddingBottom.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        PaddingBottom paddingBottom = new PaddingBottom();
        assertEquals(CssNameConstants.PADDING_BOTTOM, paddingBottom.getCssName());
    }

    @Test
    public void testGetCssValue() {
        PaddingBottom paddingBottom = new PaddingBottom();
        paddingBottom.setAsInherit();
        assertEquals(PaddingBottom.INHERIT, paddingBottom.getCssValue());
        paddingBottom.setAsInitial();
        assertEquals(PaddingBottom.INITIAL, paddingBottom.getCssValue());
    }

    @Test
    public void testToString() {
        PaddingBottom paddingBottom = new PaddingBottom(75, CssLengthUnit.EM);
        assertEquals(paddingBottom.getCssName()+": 75.0em", paddingBottom.toString());
    }

    @Test
    public void testGetValue() {
        PaddingBottom paddingBottom = new PaddingBottom();
        paddingBottom.setPercent(75);
        assertTrue(paddingBottom.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        PaddingBottom paddingBottom = new PaddingBottom();
        paddingBottom.setPercent(75);
        assertEquals(CssLengthUnit.PER, paddingBottom.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        PaddingBottom paddingBottom = new PaddingBottom();
        paddingBottom.setCssValue("75%");
        assertEquals("75%", paddingBottom.getCssValue());
        assertEquals(CssLengthUnit.PER, paddingBottom.getUnit());
        assertTrue(paddingBottom.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        PaddingBottom paddingBottom = new PaddingBottom();
        paddingBottom.setAsInitial();
        assertEquals(PaddingBottom.INITIAL, paddingBottom.getCssValue());
       assertNull(paddingBottom.getValue());
       assertNull(paddingBottom.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        PaddingBottom paddingBottom = new PaddingBottom();
        paddingBottom.setAsInherit();
        assertEquals(PaddingBottom.INHERIT, paddingBottom.getCssValue());
       assertNull(paddingBottom.getValue());
       assertNull(paddingBottom.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = PaddingBottom.isValid("45px");
            assertTrue(valid);
            final boolean invalid = PaddingBottom.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingBottom.isValid("45em");
            assertTrue(valid);
            final boolean invalid = PaddingBottom.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingBottom.isValid("45%");
            assertTrue(valid);
            final boolean invalid = PaddingBottom.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingBottom.isValid("45em");
            assertTrue(valid);
            final boolean invalid = PaddingBottom.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingBottom.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = PaddingBottom.isValid("-1px");
           assertTrue(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        PaddingBottom padding = new PaddingBottom();
        padding.setCssValue("dfdpx");
    }

}
