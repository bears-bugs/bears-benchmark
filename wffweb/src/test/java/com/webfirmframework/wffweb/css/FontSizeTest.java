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

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class FontSizeTest {

    @Test
    public void testFontSize() {
        FontSize fontSize = new FontSize();
        assertEquals(FontSize.MEDIUM, fontSize.getCssValue());
    }

    @Test
    public void testFontSizeString() {
        {
            FontSize fontSize = new FontSize(FontSize.INITIAL);
            assertEquals(FontSize.INITIAL, fontSize.getCssValue());
        }
        {
            FontSize fontSize = new FontSize("50px");
            assertEquals("50px", fontSize.getCssValue());
        }
    }

    @Test
    public void testFontSizeFontSize() {
        FontSize fontSize = new FontSize("50px");
        FontSize fontSize1 = new FontSize(fontSize);
        assertEquals("50px", fontSize1.getCssValue());
    }

    @Test
    public void testFontSizeFloat() {
        FontSize fontSize = new FontSize(75);
        assertEquals("75.0%", fontSize.getCssValue());
    }

    @Test
    public void testFontSizeFloatCssLengthUnit() {
        {
            FontSize fontSize = new FontSize(75, CssLengthUnit.PER);
            assertEquals("75.0%", fontSize.getCssValue());
            assertEquals(CssLengthUnit.PER, fontSize.getUnit());
            assertTrue(fontSize.getValue() == 75);
        }
        {
            FontSize fontSize = new FontSize(75, CssLengthUnit.CH);
            assertEquals("75.0ch", fontSize.getCssValue());
        }
        {
            FontSize fontSize = new FontSize(75, CssLengthUnit.EM);
            assertEquals("75.0em", fontSize.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            FontSize fontSize = new FontSize();
            fontSize.setPercent(75);
            assertEquals("75.0%", fontSize.getCssValue());
            assertEquals(CssLengthUnit.PER, fontSize.getUnit());
            assertTrue(fontSize.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        FontSize fontSize = new FontSize();
        assertEquals(CssNameConstants.FONT_SIZE, fontSize.getCssName());
    }

    @Test
    public void testGetCssValue() {
        FontSize fontSize = new FontSize();
        fontSize.setAsInherit();
        assertEquals(FontSize.INHERIT, fontSize.getCssValue());
        fontSize.setAsInitial();
        assertEquals(FontSize.INITIAL, fontSize.getCssValue());
    }

    @Test
    public void testToString() {
        FontSize fontSize = new FontSize(75, CssLengthUnit.EM);
        assertEquals(fontSize.getCssName() + ": 75.0em", fontSize.toString());
    }

    @Test
    public void testGetValue() {
        FontSize fontSize = new FontSize();
        fontSize.setPercent(75);
        assertTrue(fontSize.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        FontSize fontSize = new FontSize();
        fontSize.setPercent(75);
        assertEquals(CssLengthUnit.PER, fontSize.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        FontSize fontSize = new FontSize();
        fontSize.setCssValue("75%");
        assertEquals("75%", fontSize.getCssValue());
        assertEquals(CssLengthUnit.PER, fontSize.getUnit());
        assertTrue(fontSize.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        FontSize fontSize = new FontSize();
        fontSize.setAsInitial();
        assertEquals(FontSize.INITIAL, fontSize.getCssValue());
       assertNull(fontSize.getValue());
       assertNull(fontSize.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        FontSize fontSize = new FontSize();
        fontSize.setAsInherit();
        assertEquals(FontSize.INHERIT, fontSize.getCssValue());
       assertNull(fontSize.getValue());
       assertNull(fontSize.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        FontSize fontSize = new FontSize();
        fontSize.setAsInherit();
        fontSize.setAsMedium();
        assertEquals(FontSize.MEDIUM, fontSize.getCssValue());
       assertNull(fontSize.getValue());
       assertNull(fontSize.getUnit());
    }

    @Test
    public void testIsValid() {
        {
            final boolean valid = FontSize.isValid(FontSize.INHERIT);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.INITIAL);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.LARGE);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.LARGER);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.MEDIUM);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.SMALL);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.SMALLER);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.X_LARGE);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.X_SMALL);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.XX_LARGE);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid(FontSize.XX_SMALL);
            assertTrue(valid);
        }
        {
            final boolean valid = FontSize.isValid("45px");
            assertTrue(valid);
            final boolean invalid = FontSize.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = FontSize.isValid("45em");
            assertTrue(valid);
            final boolean invalid = FontSize.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = FontSize.isValid("45%");
            assertTrue(valid);
            final boolean invalid = FontSize.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = FontSize.isValid("45em");
            assertTrue(valid);
            final boolean invalid = FontSize.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = FontSize.isValid("45rem");
            assertTrue(valid);
        }
    }

}
