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
public class PaddingLeftTest {

    @Test
    public void testPaddingLeft() {
        PaddingLeft paddingLeft = new PaddingLeft();
        assertEquals("0px", paddingLeft.getCssValue());
    }

    @Test
    public void testPaddingLeftString() {
        {
            PaddingLeft paddingLeft = new PaddingLeft(PaddingLeft.INITIAL);
            assertEquals(PaddingLeft.INITIAL, paddingLeft.getCssValue());
        }
        {
            PaddingLeft paddingLeft = new PaddingLeft("50px");
            assertEquals("50px", paddingLeft.getCssValue());
        }
    }

    @Test
    public void testPaddingLeftPaddingLeft() {
        PaddingLeft paddingLeft = new PaddingLeft("50px");
        PaddingLeft paddingLeft1 = new PaddingLeft(paddingLeft);
        assertEquals("50px", paddingLeft1.getCssValue());
    }

    @Test
    public void testPaddingLeftFloat() {
        PaddingLeft paddingLeft = new PaddingLeft(75);
        assertEquals("75.0%", paddingLeft.getCssValue());
    }

    @Test
    public void testPaddingLeftFloatCssLengthUnit() {
        {
            PaddingLeft paddingLeft = new PaddingLeft(75, CssLengthUnit.PER);
            assertEquals("75.0%", paddingLeft.getCssValue());
            assertEquals(CssLengthUnit.PER, paddingLeft.getUnit());
            assertTrue(paddingLeft.getValue() == 75);
        }
        {
            PaddingLeft paddingLeft = new PaddingLeft(75, CssLengthUnit.CH);
            assertEquals("75.0ch", paddingLeft.getCssValue());
        }
        {
            PaddingLeft paddingLeft = new PaddingLeft(75, CssLengthUnit.EM);
            assertEquals("75.0em", paddingLeft.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            PaddingLeft paddingLeft = new PaddingLeft();
            paddingLeft.setPercent(75);
            assertEquals("75.0%", paddingLeft.getCssValue());
            assertEquals(CssLengthUnit.PER, paddingLeft.getUnit());
            assertTrue(paddingLeft.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        PaddingLeft paddingLeft = new PaddingLeft();
        assertEquals(CssNameConstants.PADDING_LEFT, paddingLeft.getCssName());
    }

    @Test
    public void testGetCssValue() {
        PaddingLeft paddingLeft = new PaddingLeft();
        paddingLeft.setAsInherit();
        assertEquals(PaddingLeft.INHERIT, paddingLeft.getCssValue());
        paddingLeft.setAsInitial();
        assertEquals(PaddingLeft.INITIAL, paddingLeft.getCssValue());
    }

    @Test
    public void testToString() {
        PaddingLeft paddingLeft = new PaddingLeft(75, CssLengthUnit.EM);
        assertEquals(paddingLeft.getCssName()+": 75.0em", paddingLeft.toString());
    }

    @Test
    public void testGetValue() {
        PaddingLeft paddingLeft = new PaddingLeft();
        paddingLeft.setPercent(75);
        assertTrue(paddingLeft.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        PaddingLeft paddingLeft = new PaddingLeft();
        paddingLeft.setPercent(75);
        assertEquals(CssLengthUnit.PER, paddingLeft.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        PaddingLeft paddingLeft = new PaddingLeft();
        paddingLeft.setCssValue("75%");
        assertEquals("75%", paddingLeft.getCssValue());
        assertEquals(CssLengthUnit.PER, paddingLeft.getUnit());
        assertTrue(paddingLeft.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        PaddingLeft paddingLeft = new PaddingLeft();
        paddingLeft.setAsInitial();
        assertEquals(PaddingLeft.INITIAL, paddingLeft.getCssValue());
       assertNull(paddingLeft.getValue());
       assertNull(paddingLeft.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        PaddingLeft paddingLeft = new PaddingLeft();
        paddingLeft.setAsInherit();
        assertEquals(PaddingLeft.INHERIT, paddingLeft.getCssValue());
       assertNull(paddingLeft.getValue());
       assertNull(paddingLeft.getUnit());
    }
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = PaddingLeft.isValid("45px");
            assertTrue(valid);
            final boolean invalid = PaddingLeft.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingLeft.isValid("45em");
            assertTrue(valid);
            final boolean invalid = PaddingLeft.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingLeft.isValid("45%");
            assertTrue(valid);
            final boolean invalid = PaddingLeft.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingLeft.isValid("45em");
            assertTrue(valid);
            final boolean invalid = PaddingLeft.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingLeft.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = PaddingLeft.isValid("-1px");
           assertTrue(valid);
        }
    }

    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        PaddingLeft padding = new PaddingLeft();
        padding.setCssValue("dfdfpx");
    }

}
