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
public class LineHeightTest {

    @Test
    public void testSetCssValueWithoutUnit() {
        LineHeight lineHeight = new LineHeight();
        assertEquals(LineHeight.NORMAL, lineHeight.getCssValue());
        lineHeight.setCssValue("1.4");
       assertNull(lineHeight.getUnit());
        
    }
    
    @Test
    public void testLineHeight() {
        LineHeight lineHeight = new LineHeight();
        assertEquals(LineHeight.NORMAL, lineHeight.getCssValue());
    }

    @Test
    public void testLineHeightString() {
        {
            LineHeight lineHeight = new LineHeight(LineHeight.INITIAL);
            assertEquals(LineHeight.INITIAL, lineHeight.getCssValue());
        }
        {
            LineHeight lineHeight = new LineHeight("50px");
            assertEquals("50px", lineHeight.getCssValue());
        }
    }

    @Test
    public void testLineHeightLineHeight() {
        LineHeight lineHeight = new LineHeight("50px");
        LineHeight lineHeight1 = new LineHeight(lineHeight);
        assertEquals("50px", lineHeight1.getCssValue());
    }

    @Test
    public void testLineHeightFloat() {
        LineHeight lineHeight = new LineHeight(75);
        assertEquals("75.0", lineHeight.getCssValue());
        assertNull(lineHeight.getUnit());
    }

    @Test
    public void testLineHeightFloatCssLengthUnit() {
        {
            LineHeight lineHeight = new LineHeight(75, CssLengthUnit.PER);
            assertEquals("75.0%", lineHeight.getCssValue());
            assertEquals(CssLengthUnit.PER, lineHeight.getUnit());
            assertTrue(lineHeight.getValue() == 75);
        }
        {
            LineHeight lineHeight = new LineHeight(75, CssLengthUnit.CH);
            assertEquals("75.0ch", lineHeight.getCssValue());
        }
        {
            LineHeight lineHeight = new LineHeight(75, CssLengthUnit.EM);
            assertEquals("75.0em", lineHeight.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            LineHeight lineHeight = new LineHeight();
            lineHeight.setPercent(75);
            assertEquals("75.0%", lineHeight.getCssValue());
            assertEquals(CssLengthUnit.PER, lineHeight.getUnit());
            assertTrue(lineHeight.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        LineHeight lineHeight = new LineHeight();
        assertEquals(CssNameConstants.LINE_HEIGHT, lineHeight.getCssName());
    }

    @Test
    public void testGetCssValue() {
        LineHeight lineHeight = new LineHeight();
        lineHeight.setAsInherit();
        assertEquals(LineHeight.INHERIT, lineHeight.getCssValue());
        lineHeight.setAsInitial();
        assertEquals(LineHeight.INITIAL, lineHeight.getCssValue());
    }

    @Test
    public void testToString() {
        LineHeight lineHeight = new LineHeight(75, CssLengthUnit.EM);
        assertEquals(lineHeight.getCssName()+": 75.0em", lineHeight.toString());
    }

    @Test
    public void testGetValue() {
        LineHeight lineHeight = new LineHeight();
        lineHeight.setPercent(75);
        assertTrue(lineHeight.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        LineHeight lineHeight = new LineHeight();
        lineHeight.setPercent(75);
        assertEquals(CssLengthUnit.PER, lineHeight.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        LineHeight lineHeight = new LineHeight();
        lineHeight.setCssValue("75%");
        assertEquals("75%", lineHeight.getCssValue());
        assertEquals(CssLengthUnit.PER, lineHeight.getUnit());
        assertTrue(lineHeight.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        LineHeight lineHeight = new LineHeight();
        lineHeight.setAsInitial();
        assertEquals(LineHeight.INITIAL, lineHeight.getCssValue());
        assertNull(lineHeight.getValue());
        assertNull(lineHeight.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        LineHeight lineHeight = new LineHeight();
        lineHeight.setAsInherit();
        assertEquals(LineHeight.INHERIT, lineHeight.getCssValue());
       assertNull(lineHeight.getValue());
        assertNull(lineHeight.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = LineHeight.isValid("45px");
            assertTrue(valid);
            final boolean invalid = LineHeight.isValid("55");
            assertTrue(invalid);
        }
        {
            final boolean valid = LineHeight.isValid("45em");
            assertTrue(valid);
            final boolean invalid = LineHeight.isValid("dfd");
            assertFalse(invalid);
        }
        {
            final boolean valid = LineHeight.isValid("45%");
            assertTrue(valid);
            final boolean invalid = LineHeight.isValid("45 px");
            assertFalse(invalid);
        }
        {
            final boolean valid = LineHeight.isValid("45em");
            assertTrue(valid);
            final boolean invalid = LineHeight.isValid("45sem");
            assertFalse(invalid);
        }
        {
            final boolean valid = LineHeight.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = LineHeight.isValid("--1px");
            assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        LineHeight padding = new LineHeight();
        padding.setCssValue("--1px");
    }

}
