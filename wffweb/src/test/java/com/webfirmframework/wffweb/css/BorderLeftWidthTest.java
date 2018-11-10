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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class BorderLeftWidthTest {

    @Test
    public void testBorderLeftWidth() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        assertEquals(BorderLeftWidth.MEDIUM, borderLeftWidth.getCssValue());
    }

    @Test
    public void testBorderLeftWidthString() {
        {
            BorderLeftWidth borderLeftWidth = new BorderLeftWidth(BorderLeftWidth.INITIAL);
            assertEquals(BorderLeftWidth.INITIAL, borderLeftWidth.getCssValue());
        }
        {
            BorderLeftWidth borderLeftWidth = new BorderLeftWidth("50px");
            assertEquals("50px", borderLeftWidth.getCssValue());
        }
    }

    @Test
    public void testBorderLeftWidthBorderLeftWidth() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth("50px");
        BorderLeftWidth borderLeftWidth1 = new BorderLeftWidth(borderLeftWidth);
        assertEquals("50px", borderLeftWidth1.getCssValue());
    }

    @Test
    public void testBorderLeftWidthFloat() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth(75);
        assertEquals("75.0%", borderLeftWidth.getCssValue());
    }

    @Test
    public void testBorderLeftWidthFloatCssLengthUnit() {
        {
            BorderLeftWidth borderLeftWidth = new BorderLeftWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", borderLeftWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, borderLeftWidth.getUnit());
            assertTrue(borderLeftWidth.getValue() == 75);
        }
        {
            BorderLeftWidth borderLeftWidth = new BorderLeftWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", borderLeftWidth.getCssValue());
        }
        {
            BorderLeftWidth borderLeftWidth = new BorderLeftWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", borderLeftWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
            borderLeftWidth.setPercent(75);
            assertEquals("75.0%", borderLeftWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, borderLeftWidth.getUnit());
            assertTrue(borderLeftWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        assertEquals(CssNameConstants.BORDER_LEFT_WIDTH, borderLeftWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setAsInherit();
        assertEquals(BorderLeftWidth.INHERIT, borderLeftWidth.getCssValue());
        borderLeftWidth.setAsMedium();
        assertEquals(BorderLeftWidth.MEDIUM, borderLeftWidth.getCssValue());
    }

    @Test
    public void testToString() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth(75, CssLengthUnit.EM);
        assertEquals(borderLeftWidth.getCssName()+": 75.0em", borderLeftWidth.toString());
    }

    @Test
    public void testGetValue() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setPercent(75);
        assertTrue(borderLeftWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, borderLeftWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setCssValue("75%");
        assertEquals("75%", borderLeftWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, borderLeftWidth.getUnit());
        assertTrue(borderLeftWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setAsInitial();
        assertEquals(BorderLeftWidth.INITIAL, borderLeftWidth.getCssValue());
        assertNull(borderLeftWidth.getValue());
        assertNull(borderLeftWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setAsInherit();
        assertEquals(BorderLeftWidth.INHERIT, borderLeftWidth.getCssValue());
        assertNull(borderLeftWidth.getValue());
        assertNull(borderLeftWidth.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setAsInherit();
        borderLeftWidth.setAsMedium();
        assertEquals(BorderLeftWidth.MEDIUM, borderLeftWidth.getCssValue());
        assertNull(borderLeftWidth.getValue());
        assertNull(borderLeftWidth.getUnit());
    }
    
    @Test
    public void testSetAsThick() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setAsInherit();
        borderLeftWidth.setAsThick();
        assertEquals(BorderLeftWidth.THICK, borderLeftWidth.getCssValue());
        assertNull(borderLeftWidth.getValue());
        assertNull(borderLeftWidth.getUnit());
    }
    
    @Test
    public void testSetAsThin() {
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth();
        borderLeftWidth.setAsInherit();
        try {
            borderLeftWidth.setAsThin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(BorderLeftWidth.THIN, borderLeftWidth.getCssValue());
        assertNull(borderLeftWidth.getValue());
        assertNull(borderLeftWidth.getUnit());
    }

}
