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

import com.webfirmframework.wffweb.tag.html.attribute.global.Style;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class WidthCssTest {

    @Test
    public void testWidthCss() {
        WidthCss widthCss = new WidthCss();
        assertEquals(WidthCss.AUTO, widthCss.getCssValue());
    }

    @Test
    public void testWidthCssString() {
        {
            WidthCss widthCss = new WidthCss(WidthCss.INITIAL);
            assertEquals(WidthCss.INITIAL, widthCss.getCssValue());
        }
        {
            WidthCss widthCss = new WidthCss("50px");
            assertEquals("50px", widthCss.getCssValue());
        }
        {
            Style style = new Style("height:max-content");            
            new WidthCss("max-content");

        }
    }

    @Test
    public void testWidthCssWidthCss() {
        WidthCss widthCss = new WidthCss("50px");
        WidthCss widthCss1 = new WidthCss(widthCss);
        assertEquals("50px", widthCss1.getCssValue());
    }

    @Test
    public void testWidthCssFloat() {
        WidthCss widthCss = new WidthCss(75);
        assertEquals("75.0%", widthCss.getCssValue());
    }

    @Test
    public void testWidthCssFloatCssLengthUnit() {
        {
            WidthCss widthCss = new WidthCss(75, CssLengthUnit.PER);
            assertEquals("75.0%", widthCss.getCssValue());
            assertEquals(CssLengthUnit.PER, widthCss.getUnit());
            assertTrue(widthCss.getValue() == 75);
        }
        {
            WidthCss widthCss = new WidthCss(75, CssLengthUnit.CH);
            assertEquals("75.0ch", widthCss.getCssValue());
        }
        {
            WidthCss widthCss = new WidthCss(75, CssLengthUnit.EM);
            assertEquals("75.0em", widthCss.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            WidthCss widthCss = new WidthCss();
            widthCss.setPercent(75);
            assertEquals("75.0%", widthCss.getCssValue());
            assertEquals(CssLengthUnit.PER, widthCss.getUnit());
            assertTrue(widthCss.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        WidthCss widthCss = new WidthCss();
        assertEquals(CssNameConstants.WIDTH, widthCss.getCssName());
    }

    @Test
    public void testGetCssValue() {
        WidthCss widthCss = new WidthCss();
        widthCss.setAsInherit();
        assertEquals(WidthCss.INHERIT, widthCss.getCssValue());
        widthCss.setAsInitial();
        assertEquals(WidthCss.INITIAL, widthCss.getCssValue());
    }

    @Test
    public void testToString() {
        WidthCss widthCss = new WidthCss(75, CssLengthUnit.EM);
        assertEquals(widthCss.getCssName() + ": 75.0em", widthCss.toString());
    }

    @Test
    public void testGetValue() {
        WidthCss widthCss = new WidthCss();
        widthCss.setPercent(75);
        assertTrue(widthCss.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        WidthCss widthCss = new WidthCss();
        widthCss.setPercent(75);
        assertEquals(CssLengthUnit.PER, widthCss.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        WidthCss widthCss = new WidthCss();
        widthCss.setCssValue("75%");
        assertEquals("75%", widthCss.getCssValue());
        assertEquals(CssLengthUnit.PER, widthCss.getUnit());
        assertTrue(widthCss.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        WidthCss widthCss = new WidthCss();
        widthCss.setAsInitial();
        assertEquals(WidthCss.INITIAL, widthCss.getCssValue());
       assertNull(widthCss.getValue());
       assertNull(widthCss.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        WidthCss widthCss = new WidthCss();
        widthCss.setAsInherit();
        assertEquals(WidthCss.INHERIT, widthCss.getCssValue());
       assertNull(widthCss.getValue());
       assertNull(widthCss.getUnit());
    }

    @Test
    public void testSetAsAuto() {
        WidthCss widthCss = new WidthCss();
        widthCss.setAsInherit();
        widthCss.setAsAuto();
        assertEquals(WidthCss.AUTO, widthCss.getCssValue());
       assertNull(widthCss.getValue());
       assertNull(widthCss.getUnit());
    }

    @Test
    public void testIsValid() {
        {
            final boolean valid = WidthCss.isValid("45px");
            assertTrue(valid);
            final boolean invalid = WidthCss.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = WidthCss.isValid("45em");
            assertTrue(valid);
            final boolean invalid = WidthCss.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = WidthCss.isValid("45%");
            assertTrue(valid);
            final boolean invalid = WidthCss.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = WidthCss.isValid("45em");
            assertTrue(valid);
            final boolean invalid = WidthCss.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = WidthCss.isValid("45rem");
            assertTrue(valid);
        }
    }

}
