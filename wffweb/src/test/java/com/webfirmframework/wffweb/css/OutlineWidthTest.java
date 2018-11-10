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
public class OutlineWidthTest {

    @Test
    public void testOutlineWidth() {
        OutlineWidth outlineWidth = new OutlineWidth();
        assertEquals(OutlineWidth.INITIAL, outlineWidth.getCssValue());
    }

    @Test
    public void testOutlineWidthString() {
        {
            OutlineWidth outlineWidth = new OutlineWidth(OutlineWidth.INITIAL);
            assertEquals(OutlineWidth.INITIAL, outlineWidth.getCssValue());
        }
        {
            OutlineWidth outlineWidth = new OutlineWidth("50px");
            assertEquals("50px", outlineWidth.getCssValue());
        }
    }

    @Test
    public void testOutlineWidthOutlineWidth() {
        OutlineWidth outlineWidth = new OutlineWidth("50px");
        OutlineWidth outlineWidth1 = new OutlineWidth(outlineWidth);
        assertEquals("50px", outlineWidth1.getCssValue());
    }

    @Test
    public void testOutlineWidthFloat() {
        OutlineWidth outlineWidth = new OutlineWidth(75);
        assertEquals("75.0%", outlineWidth.getCssValue());
    }

    @Test
    public void testOutlineWidthFloatCssLengthUnit() {
        {
            OutlineWidth outlineWidth = new OutlineWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", outlineWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, outlineWidth.getUnit());
            assertTrue(outlineWidth.getValue() == 75);
        }
        {
            OutlineWidth outlineWidth = new OutlineWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", outlineWidth.getCssValue());
        }
        {
            OutlineWidth outlineWidth = new OutlineWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", outlineWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            OutlineWidth outlineWidth = new OutlineWidth();
            outlineWidth.setPercent(75);
            assertEquals("75.0%", outlineWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, outlineWidth.getUnit());
            assertTrue(outlineWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        OutlineWidth outlineWidth = new OutlineWidth();
        assertEquals(CssNameConstants.OUTLINE_WIDTH, outlineWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setAsInherit();
        assertEquals(OutlineWidth.INHERIT, outlineWidth.getCssValue());
        outlineWidth.setAsMedium();
        assertEquals(OutlineWidth.MEDIUM, outlineWidth.getCssValue());
    }

    @Test
    public void testToString() {
        OutlineWidth outlineWidth = new OutlineWidth(75, CssLengthUnit.EM);
        assertEquals(outlineWidth.getCssName()+": 75.0em", outlineWidth.toString());
    }

    @Test
    public void testGetValue() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setPercent(75);
        assertTrue(outlineWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, outlineWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setCssValue("75%");
        assertEquals("75%", outlineWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, outlineWidth.getUnit());
        assertTrue(outlineWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setAsInitial();
        assertEquals(OutlineWidth.INITIAL, outlineWidth.getCssValue());
       assertNull(outlineWidth.getValue());
       assertNull(outlineWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setAsInherit();
        assertEquals(OutlineWidth.INHERIT, outlineWidth.getCssValue());
       assertNull(outlineWidth.getValue());
       assertNull(outlineWidth.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setAsInherit();
        outlineWidth.setAsMedium();
        assertEquals(OutlineWidth.MEDIUM, outlineWidth.getCssValue());
       assertNull(outlineWidth.getValue());
       assertNull(outlineWidth.getUnit());
    }
    
    @Test
    public void testSetAsThick() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setAsInherit();
        outlineWidth.setAsThick();
        assertEquals(OutlineWidth.THICK, outlineWidth.getCssValue());
       assertNull(outlineWidth.getValue());
       assertNull(outlineWidth.getUnit());
    }
    
    @Test
    public void testSetAsThin() {
        OutlineWidth outlineWidth = new OutlineWidth();
        outlineWidth.setAsInherit();
        outlineWidth.setAsThin();
        assertEquals(OutlineWidth.THIN, outlineWidth.getCssValue());
       assertNull(outlineWidth.getValue());
       assertNull(outlineWidth.getUnit());
    }

}
