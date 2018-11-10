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
public class PaddingRightTest {

    @Test
    public void testPaddingRight() {
        PaddingRight paddingRight = new PaddingRight();
        assertEquals("0px", paddingRight.getCssValue());
    }

    @Test
    public void testPaddingRightString() {
        {
            PaddingRight paddingRight = new PaddingRight(PaddingRight.INITIAL);
            assertEquals(PaddingRight.INITIAL, paddingRight.getCssValue());
        }
        {
            PaddingRight paddingRight = new PaddingRight("50px");
            assertEquals("50px", paddingRight.getCssValue());
        }
    }

    @Test
    public void testPaddingRightPaddingRight() {
        PaddingRight paddingRight = new PaddingRight("50px");
        PaddingRight paddingRight1 = new PaddingRight(paddingRight);
        assertEquals("50px", paddingRight1.getCssValue());
    }

    @Test
    public void testPaddingRightFloat() {
        PaddingRight paddingRight = new PaddingRight(75);
        assertEquals("75.0%", paddingRight.getCssValue());
    }

    @Test
    public void testPaddingRightFloatCssLengthUnit() {
        {
            PaddingRight paddingRight = new PaddingRight(75, CssLengthUnit.PER);
            assertEquals("75.0%", paddingRight.getCssValue());
            assertEquals(CssLengthUnit.PER, paddingRight.getUnit());
            assertTrue(paddingRight.getValue() == 75);
        }
        {
            PaddingRight paddingRight = new PaddingRight(75, CssLengthUnit.CH);
            assertEquals("75.0ch", paddingRight.getCssValue());
        }
        {
            PaddingRight paddingRight = new PaddingRight(75, CssLengthUnit.EM);
            assertEquals("75.0em", paddingRight.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            PaddingRight paddingRight = new PaddingRight();
            paddingRight.setPercent(75);
            assertEquals("75.0%", paddingRight.getCssValue());
            assertEquals(CssLengthUnit.PER, paddingRight.getUnit());
            assertTrue(paddingRight.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        PaddingRight paddingRight = new PaddingRight();
        assertEquals(CssNameConstants.PADDING_RIGHT, paddingRight.getCssName());
    }

    @Test
    public void testGetCssValue() {
        PaddingRight paddingRight = new PaddingRight();
        paddingRight.setAsInherit();
        assertEquals(PaddingRight.INHERIT, paddingRight.getCssValue());
        paddingRight.setAsInitial();
        assertEquals(PaddingRight.INITIAL, paddingRight.getCssValue());
    }

    @Test
    public void testToString() {
        PaddingRight paddingRight = new PaddingRight(75, CssLengthUnit.EM);
        assertEquals(paddingRight.getCssName()+": 75.0em", paddingRight.toString());
    }

    @Test
    public void testGetValue() {
        PaddingRight paddingRight = new PaddingRight();
        paddingRight.setPercent(75);
        assertTrue(paddingRight.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        PaddingRight paddingRight = new PaddingRight();
        paddingRight.setPercent(75);
        assertEquals(CssLengthUnit.PER, paddingRight.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        PaddingRight paddingRight = new PaddingRight();
        paddingRight.setCssValue("75%");
        assertEquals("75%", paddingRight.getCssValue());
        assertEquals(CssLengthUnit.PER, paddingRight.getUnit());
        assertTrue(paddingRight.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        PaddingRight paddingRight = new PaddingRight();
        paddingRight.setAsInitial();
        assertEquals(PaddingRight.INITIAL, paddingRight.getCssValue());
       assertNull(paddingRight.getValue());
       assertNull(paddingRight.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        PaddingRight paddingRight = new PaddingRight();
        paddingRight.setAsInherit();
        assertEquals(PaddingRight.INHERIT, paddingRight.getCssValue());
       assertNull(paddingRight.getValue());
       assertNull(paddingRight.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = PaddingRight.isValid("45px");
            assertTrue(valid);
            final boolean invalid = PaddingRight.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingRight.isValid("45em");
            assertTrue(valid);
            final boolean invalid = PaddingRight.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingRight.isValid("45%");
            assertTrue(valid);
            final boolean invalid = PaddingRight.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingRight.isValid("45em");
            assertTrue(valid);
            final boolean invalid = PaddingRight.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingRight.isValid("45rem");
            assertTrue(valid);
        }
        
        {
            final boolean valid = PaddingRight.isValid("-1px");
           assertTrue(valid);
        }
    }

    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        PaddingRight padding = new PaddingRight();
        padding.setCssValue("dfdpx");
    }

}
