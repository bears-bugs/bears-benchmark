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
public class OutlineOffsetTest {

    @Test
    public void testOutlineWidth() {
        OutlineOffset outlineOffset = new OutlineOffset();
        assertEquals("0px", outlineOffset.getCssValue());
        assertTrue(outlineOffset.getValue() == 0);
        assertEquals(CssLengthUnit.PX, outlineOffset.getUnit());
    }

    @Test
    public void testOutlineOffsetString() {
        {
            OutlineOffset outlineOffset = new OutlineOffset(
                    OutlineOffset.INITIAL);
            assertEquals(OutlineOffset.INITIAL, outlineOffset.getCssValue());
        }
        {
            OutlineOffset outlineOffset = new OutlineOffset("50px");
            assertEquals("50px", outlineOffset.getCssValue());
        }
    }

    @Test
    public void testOutlineOffsetOutlineOffset() {
        OutlineOffset outlineOffset = new OutlineOffset("50px");
        OutlineOffset outlineOffset1 = new OutlineOffset(outlineOffset);
        assertEquals("50px", outlineOffset1.getCssValue());
    }

    @Test
    public void testOutlineOffsetFloat() {
        OutlineOffset outlineOffset = new OutlineOffset(75);
        assertEquals("75.0%", outlineOffset.getCssValue());
    }

    @Test
    public void testOutlineOffsetFloatCssLengthUnit() {
        {
            OutlineOffset outlineOffset = new OutlineOffset(75,
                    CssLengthUnit.PER);
            assertEquals("75.0%", outlineOffset.getCssValue());
            assertEquals(CssLengthUnit.PER, outlineOffset.getUnit());
            assertTrue(outlineOffset.getValue() == 75);
        }
        {
            OutlineOffset outlineOffset = new OutlineOffset(75,
                    CssLengthUnit.CH);
            assertEquals("75.0ch", outlineOffset.getCssValue());
        }
        {
            OutlineOffset outlineOffset = new OutlineOffset(75,
                    CssLengthUnit.EM);
            assertEquals("75.0em", outlineOffset.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            OutlineOffset outlineOffset = new OutlineOffset();
            outlineOffset.setPercent(75);
            assertEquals("75.0%", outlineOffset.getCssValue());
            assertEquals(CssLengthUnit.PER, outlineOffset.getUnit());
            assertTrue(outlineOffset.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        OutlineOffset outlineOffset = new OutlineOffset();
        assertEquals(CssNameConstants.OUTLINE_OFFSET, outlineOffset.getCssName());
    }

    @Test
    public void testGetCssValue() {
        OutlineOffset outlineOffset = new OutlineOffset();
        outlineOffset.setAsInherit();
        assertEquals(OutlineOffset.INHERIT, outlineOffset.getCssValue());
        outlineOffset.setAsInitial();
        assertEquals(OutlineOffset.INITIAL, outlineOffset.getCssValue());
    }

    @Test
    public void testToString() {
        OutlineOffset outlineOffset = new OutlineOffset(75, CssLengthUnit.EM);
        assertEquals(outlineOffset.getCssName() + ": 75.0em",
                outlineOffset.toString());
    }

    @Test
    public void testGetValue() {
        OutlineOffset outlineOffset = new OutlineOffset();
        outlineOffset.setPercent(75);
        assertTrue(outlineOffset.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        OutlineOffset outlineOffset = new OutlineOffset();
        outlineOffset.setPercent(75);
        assertEquals(CssLengthUnit.PER, outlineOffset.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        OutlineOffset outlineOffset = new OutlineOffset();
        outlineOffset.setCssValue("75%");
        assertEquals("75%", outlineOffset.getCssValue());
        assertEquals(CssLengthUnit.PER, outlineOffset.getUnit());
        assertTrue(outlineOffset.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        OutlineOffset outlineOffset = new OutlineOffset();
        outlineOffset.setAsInitial();
        assertEquals(OutlineOffset.INITIAL, outlineOffset.getCssValue());
       assertNull(outlineOffset.getValue());
       assertNull(outlineOffset.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        OutlineOffset outlineOffset = new OutlineOffset();
        outlineOffset.setAsInherit();
        assertEquals(OutlineOffset.INHERIT, outlineOffset.getCssValue());
       assertNull(outlineOffset.getValue());
       assertNull(outlineOffset.getUnit());
    }

}
