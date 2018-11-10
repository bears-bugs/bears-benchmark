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
public class BorderRightWidthTest {

    @Test
    public void testBorderRightWidth() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        assertEquals(BorderRightWidth.MEDIUM, borderRightWidth.getCssValue());
    }

    @Test
    public void testBorderRightWidthString() {
        {
            BorderRightWidth borderRightWidth = new BorderRightWidth(BorderRightWidth.INITIAL);
            assertEquals(BorderRightWidth.INITIAL, borderRightWidth.getCssValue());
        }
        {
            BorderRightWidth borderRightWidth = new BorderRightWidth("50px");
            assertEquals("50px", borderRightWidth.getCssValue());
        }
    }

    @Test
    public void testBorderRightWidthBorderRightWidth() {
        BorderRightWidth borderRightWidth = new BorderRightWidth("50px");
        BorderRightWidth borderRightWidth1 = new BorderRightWidth(borderRightWidth);
        assertEquals("50px", borderRightWidth1.getCssValue());
    }

    @Test
    public void testBorderRightWidthFloat() {
        BorderRightWidth borderRightWidth = new BorderRightWidth(75);
        assertEquals("75.0%", borderRightWidth.getCssValue());
    }

    @Test
    public void testBorderRightWidthFloatCssLengthUnit() {
        {
            BorderRightWidth borderRightWidth = new BorderRightWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", borderRightWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, borderRightWidth.getUnit());
            assertTrue(borderRightWidth.getValue() == 75);
        }
        {
            BorderRightWidth borderRightWidth = new BorderRightWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", borderRightWidth.getCssValue());
        }
        {
            BorderRightWidth borderRightWidth = new BorderRightWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", borderRightWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            BorderRightWidth borderRightWidth = new BorderRightWidth();
            borderRightWidth.setPercent(75);
            assertEquals("75.0%", borderRightWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, borderRightWidth.getUnit());
            assertTrue(borderRightWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        assertEquals(CssNameConstants.BORDER_RIGHT_WIDTH, borderRightWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setAsInherit();
        assertEquals(BorderRightWidth.INHERIT, borderRightWidth.getCssValue());
        borderRightWidth.setAsMedium();
        assertEquals(BorderRightWidth.MEDIUM, borderRightWidth.getCssValue());
    }

    @Test
    public void testToString() {
        BorderRightWidth borderRightWidth = new BorderRightWidth(75, CssLengthUnit.EM);
        assertEquals(borderRightWidth.getCssName()+": 75.0em", borderRightWidth.toString());
    }

    @Test
    public void testGetValue() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setPercent(75);
        assertTrue(borderRightWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, borderRightWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setCssValue("75%");
        assertEquals("75%", borderRightWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, borderRightWidth.getUnit());
        assertTrue(borderRightWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setAsInitial();
        assertEquals(BorderRightWidth.INITIAL, borderRightWidth.getCssValue());
        assertNull(borderRightWidth.getValue());
        assertNull(borderRightWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setAsInherit();
        assertEquals(BorderRightWidth.INHERIT, borderRightWidth.getCssValue());
        assertNull(borderRightWidth.getValue());
        assertNull(borderRightWidth.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setAsInherit();
        borderRightWidth.setAsMedium();
        assertEquals(BorderRightWidth.MEDIUM, borderRightWidth.getCssValue());
        assertNull(borderRightWidth.getValue());
        assertNull(borderRightWidth.getUnit());
    }
    
    @Test
    public void testSetAsThick() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setAsInherit();
        borderRightWidth.setAsThick();
        assertEquals(BorderRightWidth.THICK, borderRightWidth.getCssValue());
        assertNull(borderRightWidth.getValue());
        assertNull(borderRightWidth.getUnit());
    }
    
    @Test
    public void testSetAsThin() {
        BorderRightWidth borderRightWidth = new BorderRightWidth();
        borderRightWidth.setAsInherit();
        try {
            borderRightWidth.setAsThin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(BorderRightWidth.THIN, borderRightWidth.getCssValue());
        assertNull(borderRightWidth.getValue());
        assertNull(borderRightWidth.getUnit());
    }

}
