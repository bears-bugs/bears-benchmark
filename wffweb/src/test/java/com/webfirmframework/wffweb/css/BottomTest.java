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
public class BottomTest {

    @Test
    public void testBottom() {
        Bottom bottom = new Bottom();
        assertEquals(Bottom.AUTO, bottom.getCssValue());
    }

    @Test
    public void testBottomString() {
        {
            Bottom bottom = new Bottom(Bottom.INITIAL);
            assertEquals(Bottom.INITIAL, bottom.getCssValue());
        }
        {
            Bottom bottom = new Bottom("50px");
            assertEquals("50px", bottom.getCssValue());
        }
    }

    @Test
    public void testBottomBottom() {
        Bottom bottom = new Bottom("50px");
        Bottom bottom1 = new Bottom(bottom);
        assertEquals("50px", bottom1.getCssValue());
    }

    @Test
    public void testBottomFloat() {
        Bottom bottom = new Bottom(75);
        assertEquals("75.0%", bottom.getCssValue());
    }

    @Test
    public void testBottomFloatCssLengthUnit() {
        {
            Bottom bottom = new Bottom(75, CssLengthUnit.PER);
            assertEquals("75.0%", bottom.getCssValue());
            assertEquals(CssLengthUnit.PER, bottom.getUnit());
            assertTrue(bottom.getValue() == 75);
        }
        {
            Bottom bottom = new Bottom(75, CssLengthUnit.CH);
            assertEquals("75.0ch", bottom.getCssValue());
        }
        {
            Bottom bottom = new Bottom(75, CssLengthUnit.EM);
            assertEquals("75.0em", bottom.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            Bottom bottom = new Bottom();
            bottom.setPercent(75);
            assertEquals("75.0%", bottom.getCssValue());
            assertEquals(CssLengthUnit.PER, bottom.getUnit());
            assertTrue(bottom.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        Bottom bottom = new Bottom();
        assertEquals(CssNameConstants.BOTTOM, bottom.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Bottom bottom = new Bottom();
        bottom.setAsInherit();
        assertEquals(Bottom.INHERIT, bottom.getCssValue());
        bottom.setAsInitial();
        assertEquals(Bottom.INITIAL, bottom.getCssValue());
    }

    @Test
    public void testToString() {
        Bottom bottom = new Bottom(75, CssLengthUnit.EM);
        assertEquals(bottom.getCssName()+": 75.0em", bottom.toString());
    }

    @Test
    public void testGetValue() {
        Bottom bottom = new Bottom();
        bottom.setPercent(75);
        assertTrue(bottom.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        Bottom bottom = new Bottom();
        bottom.setPercent(75);
        assertEquals(CssLengthUnit.PER, bottom.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        Bottom bottom = new Bottom();
        bottom.setCssValue("75%");
        assertEquals("75%", bottom.getCssValue());
        assertEquals(CssLengthUnit.PER, bottom.getUnit());
        assertTrue(bottom.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        Bottom bottom = new Bottom();
        bottom.setAsInitial();
        assertEquals(Bottom.INITIAL, bottom.getCssValue());
       assertNull(bottom.getValue());
       assertNull(bottom.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        Bottom bottom = new Bottom();
        bottom.setAsInherit();
        assertEquals(Bottom.INHERIT, bottom.getCssValue());
       assertNull(bottom.getValue());
       assertNull(bottom.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = Bottom.isValid("45px");
            assertTrue(valid);
            final boolean invalid = Bottom.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = Bottom.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Bottom.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = Bottom.isValid("45%");
            assertTrue(valid);
            final boolean invalid = Bottom.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = Bottom.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Bottom.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = Bottom.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = Bottom.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        Bottom padding = new Bottom();
        padding.setCssValue("--1px");
    }

}
