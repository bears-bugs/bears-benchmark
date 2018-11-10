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
public class BorderTopWidthTest {

    @Test
    public void testBorderTopWidth() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        assertEquals(BorderTopWidth.MEDIUM, borderTopWidth.getCssValue());
    }

    @Test
    public void testBorderTopWidthString() {
        {
            BorderTopWidth borderTopWidth = new BorderTopWidth(BorderTopWidth.INITIAL);
            assertEquals(BorderTopWidth.INITIAL, borderTopWidth.getCssValue());
        }
        {
            BorderTopWidth borderTopWidth = new BorderTopWidth("50px");
            assertEquals("50px", borderTopWidth.getCssValue());
        }
    }

    @Test
    public void testBorderTopWidthBorderTopWidth() {
        BorderTopWidth borderTopWidth = new BorderTopWidth("50px");
        BorderTopWidth borderTopWidth1 = new BorderTopWidth(borderTopWidth);
        assertEquals("50px", borderTopWidth1.getCssValue());
    }

    @Test
    public void testBorderTopWidthFloat() {
        BorderTopWidth borderTopWidth = new BorderTopWidth(75);
        assertEquals("75.0%", borderTopWidth.getCssValue());
    }

    @Test
    public void testBorderTopWidthFloatCssLengthUnit() {
        {
            BorderTopWidth borderTopWidth = new BorderTopWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", borderTopWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, borderTopWidth.getUnit());
            assertTrue(borderTopWidth.getValue() == 75);
        }
        {
            BorderTopWidth borderTopWidth = new BorderTopWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", borderTopWidth.getCssValue());
        }
        {
            BorderTopWidth borderTopWidth = new BorderTopWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", borderTopWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            BorderTopWidth borderTopWidth = new BorderTopWidth();
            borderTopWidth.setPercent(75);
            assertEquals("75.0%", borderTopWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, borderTopWidth.getUnit());
            assertTrue(borderTopWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        assertEquals(CssNameConstants.BORDER_TOP_WIDTH, borderTopWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setAsInherit();
        assertEquals(BorderTopWidth.INHERIT, borderTopWidth.getCssValue());
        borderTopWidth.setAsMedium();
        assertEquals(BorderTopWidth.MEDIUM, borderTopWidth.getCssValue());
    }

    @Test
    public void testToString() {
        BorderTopWidth borderTopWidth = new BorderTopWidth(75, CssLengthUnit.EM);
        assertEquals(borderTopWidth.getCssName()+": 75.0em", borderTopWidth.toString());
    }

    @Test
    public void testGetValue() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setPercent(75);
        assertTrue(borderTopWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, borderTopWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setCssValue("75%");
        assertEquals("75%", borderTopWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, borderTopWidth.getUnit());
        assertTrue(borderTopWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setAsInitial();
        assertEquals(BorderTopWidth.INITIAL, borderTopWidth.getCssValue());
        assertNull(borderTopWidth.getValue());
        assertNull(borderTopWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setAsInherit();
        assertEquals(BorderTopWidth.INHERIT, borderTopWidth.getCssValue());
        assertNull(borderTopWidth.getValue());
        assertNull(borderTopWidth.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setAsInherit();
        borderTopWidth.setAsMedium();
        assertEquals(BorderTopWidth.MEDIUM, borderTopWidth.getCssValue());
        assertNull(borderTopWidth.getValue());
        assertNull(borderTopWidth.getUnit());
    }
    
    @Test
    public void testSetAsThick() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setAsInherit();
        borderTopWidth.setAsThick();
        assertEquals(BorderTopWidth.THICK, borderTopWidth.getCssValue());
        assertNull(borderTopWidth.getValue());
        assertNull(borderTopWidth.getUnit());
    }
    
    @Test
    public void testSetAsThin() {
        BorderTopWidth borderTopWidth = new BorderTopWidth();
        borderTopWidth.setAsInherit();
        try {
            borderTopWidth.setAsThin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(BorderTopWidth.THIN, borderTopWidth.getCssValue());
        assertNull(borderTopWidth.getValue());
        assertNull(borderTopWidth.getUnit());
    }

}
