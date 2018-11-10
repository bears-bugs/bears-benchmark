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
public class BorderBottomWidthTest {

    @Test
    public void testBorderBottomWidth() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        assertEquals(BorderBottomWidth.MEDIUM, borderBottomWidth.getCssValue());
    }

    @Test
    public void testBorderBottomWidthString() {
        {
            BorderBottomWidth borderBottomWidth = new BorderBottomWidth(BorderBottomWidth.INITIAL);
            assertEquals(BorderBottomWidth.INITIAL, borderBottomWidth.getCssValue());
        }
        {
            BorderBottomWidth borderBottomWidth = new BorderBottomWidth("50px");
            assertEquals("50px", borderBottomWidth.getCssValue());
        }
    }

    @Test
    public void testBorderBottomWidthBorderBottomWidth() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth("50px");
        BorderBottomWidth borderBottomWidth1 = new BorderBottomWidth(borderBottomWidth);
        assertEquals("50px", borderBottomWidth1.getCssValue());
    }

    @Test
    public void testBorderBottomWidthFloat() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth(75);
        assertEquals("75.0%", borderBottomWidth.getCssValue());
    }

    @Test
    public void testBorderBottomWidthFloatCssLengthUnit() {
        {
            BorderBottomWidth borderBottomWidth = new BorderBottomWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", borderBottomWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, borderBottomWidth.getUnit());
            assertTrue(borderBottomWidth.getValue() == 75);
        }
        {
            BorderBottomWidth borderBottomWidth = new BorderBottomWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", borderBottomWidth.getCssValue());
        }
        {
            BorderBottomWidth borderBottomWidth = new BorderBottomWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", borderBottomWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
            borderBottomWidth.setPercent(75);
            assertEquals("75.0%", borderBottomWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, borderBottomWidth.getUnit());
            assertTrue(borderBottomWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        assertEquals(CssNameConstants.BORDER_BOTTOM_WIDTH, borderBottomWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setAsInherit();
        assertEquals(BorderBottomWidth.INHERIT, borderBottomWidth.getCssValue());
        borderBottomWidth.setAsMedium();
        assertEquals(BorderBottomWidth.MEDIUM, borderBottomWidth.getCssValue());
    }

    @Test
    public void testToString() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth(75, CssLengthUnit.EM);
        assertEquals(borderBottomWidth.getCssName()+": 75.0em", borderBottomWidth.toString());
    }

    @Test
    public void testGetValue() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setPercent(75);
        assertTrue(borderBottomWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, borderBottomWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setCssValue("75%");
        assertEquals("75%", borderBottomWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, borderBottomWidth.getUnit());
        assertTrue(borderBottomWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setAsInitial();
        assertEquals(BorderBottomWidth.INITIAL, borderBottomWidth.getCssValue());
        assertNull(borderBottomWidth.getValue());
        assertNull(borderBottomWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setAsInherit();
        assertEquals(BorderBottomWidth.INHERIT, borderBottomWidth.getCssValue());
        assertNull(borderBottomWidth.getValue());
        assertNull(borderBottomWidth.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setAsInherit();
        borderBottomWidth.setAsMedium();
        assertEquals(BorderBottomWidth.MEDIUM, borderBottomWidth.getCssValue());
        assertNull(borderBottomWidth.getValue());
        assertNull(borderBottomWidth.getUnit());
    }
    
    @Test
    public void testSetAsThick() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setAsInherit();
        borderBottomWidth.setAsThick();
        assertEquals(BorderBottomWidth.THICK, borderBottomWidth.getCssValue());
        assertNull(borderBottomWidth.getValue());
        assertNull(borderBottomWidth.getUnit());
    }
    
    @Test
    public void testSetAsThin() {
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth();
        borderBottomWidth.setAsInherit();
        try {
            borderBottomWidth.setAsThin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(BorderBottomWidth.THIN, borderBottomWidth.getCssValue());
        assertNull(borderBottomWidth.getValue());
        assertNull(borderBottomWidth.getUnit());
    }

}
