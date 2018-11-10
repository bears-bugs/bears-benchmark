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
public class LetterSpacingTest {

    @Test
    public void testLetterSpacing() {
        LetterSpacing letterSpacing = new LetterSpacing();
        assertEquals(LetterSpacing.NORMAL, letterSpacing.getCssValue());
    }

    @Test
    public void testLetterSpacingString() {
        {
            LetterSpacing letterSpacing = new LetterSpacing(LetterSpacing.INITIAL);
            assertEquals(LetterSpacing.INITIAL, letterSpacing.getCssValue());
        }
        {
            LetterSpacing letterSpacing = new LetterSpacing("50px");
            assertEquals("50px", letterSpacing.getCssValue());
        }
    }

    @Test
    public void testLetterSpacingLetterSpacing() {
        LetterSpacing letterSpacing = new LetterSpacing("50px");
        LetterSpacing letterSpacing1 = new LetterSpacing(letterSpacing);
        assertEquals("50px", letterSpacing1.getCssValue());
    }

    @Test
    public void testLetterSpacingFloat() {
        LetterSpacing letterSpacing = new LetterSpacing(75);
        assertEquals("75.0%", letterSpacing.getCssValue());
    }

    @Test
    public void testLetterSpacingFloatCssLengthUnit() {
        {
            LetterSpacing letterSpacing = new LetterSpacing(75, CssLengthUnit.PER);
            assertEquals("75.0%", letterSpacing.getCssValue());
            assertEquals(CssLengthUnit.PER, letterSpacing.getUnit());
            assertTrue(letterSpacing.getValue() == 75);
        }
        {
            LetterSpacing letterSpacing = new LetterSpacing(75, CssLengthUnit.CH);
            assertEquals("75.0ch", letterSpacing.getCssValue());
        }
        {
            LetterSpacing letterSpacing = new LetterSpacing(75, CssLengthUnit.EM);
            assertEquals("75.0em", letterSpacing.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            LetterSpacing letterSpacing = new LetterSpacing();
            letterSpacing.setPercent(75);
            assertEquals("75.0%", letterSpacing.getCssValue());
            assertEquals(CssLengthUnit.PER, letterSpacing.getUnit());
            assertTrue(letterSpacing.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        LetterSpacing letterSpacing = new LetterSpacing();
        assertEquals(CssNameConstants.LETTER_SPACING, letterSpacing.getCssName());
    }

    @Test
    public void testGetCssValue() {
        LetterSpacing letterSpacing = new LetterSpacing();
        letterSpacing.setAsInherit();
        assertEquals(LetterSpacing.INHERIT, letterSpacing.getCssValue());
        letterSpacing.setAsInitial();
        assertEquals(LetterSpacing.INITIAL, letterSpacing.getCssValue());
    }

    @Test
    public void testToString() {
        LetterSpacing letterSpacing = new LetterSpacing(75, CssLengthUnit.EM);
        assertEquals(letterSpacing.getCssName()+": 75.0em", letterSpacing.toString());
    }

    @Test
    public void testGetValue() {
        LetterSpacing letterSpacing = new LetterSpacing();
        letterSpacing.setPercent(75);
        assertTrue(letterSpacing.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        LetterSpacing letterSpacing = new LetterSpacing();
        letterSpacing.setPercent(75);
        assertEquals(CssLengthUnit.PER, letterSpacing.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        LetterSpacing letterSpacing = new LetterSpacing();
        letterSpacing.setCssValue("75%");
        assertEquals("75%", letterSpacing.getCssValue());
        assertEquals(CssLengthUnit.PER, letterSpacing.getUnit());
        assertTrue(letterSpacing.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        LetterSpacing letterSpacing = new LetterSpacing();
        letterSpacing.setAsInitial();
        assertEquals(LetterSpacing.INITIAL, letterSpacing.getCssValue());
       assertNull(letterSpacing.getValue());
       assertNull(letterSpacing.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        LetterSpacing letterSpacing = new LetterSpacing();
        letterSpacing.setAsInherit();
        assertEquals(LetterSpacing.INHERIT, letterSpacing.getCssValue());
       assertNull(letterSpacing.getValue());
       assertNull(letterSpacing.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = LetterSpacing.isValid("45px");
            assertTrue(valid);
            final boolean invalid = LetterSpacing.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = LetterSpacing.isValid("45em");
            assertTrue(valid);
            final boolean invalid = LetterSpacing.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = LetterSpacing.isValid("45%");
            assertTrue(valid);
            final boolean invalid = LetterSpacing.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = LetterSpacing.isValid("45em");
            assertTrue(valid);
            final boolean invalid = LetterSpacing.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = LetterSpacing.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = LetterSpacing.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        LetterSpacing padding = new LetterSpacing();
        padding.setCssValue("--1px");
    }

}
