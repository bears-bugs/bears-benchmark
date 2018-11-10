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
package com.webfirmframework.wffweb.css.css3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class WebkitColumnGapTest {

    @Test
    public void testColumnGap() {
        ColumnGap columnGap = new ColumnGap();
        assertEquals(ColumnGap.NORMAL, columnGap.getCssValue());
    }

    @Test
    public void testWebkitColumnGapString() {
        {
            WebkitColumnGap columnGap = new WebkitColumnGap(WebkitColumnGap.INITIAL);
            assertEquals(WebkitColumnGap.INITIAL, columnGap.getCssValue());
        }
        {
            WebkitColumnGap columnGap = new WebkitColumnGap("50px");
            assertEquals("50px", columnGap.getCssValue());
        }
    }

    @Test
    public void testWebkitColumnGapWebkitColumnGap() {
        WebkitColumnGap columnGap = new WebkitColumnGap("50px");
        WebkitColumnGap columnGap1 = new WebkitColumnGap(columnGap);
        assertEquals("50px", columnGap1.getCssValue());
    }

    @Test
    public void testWebkitColumnGapFloat() {
        WebkitColumnGap columnGap = new WebkitColumnGap(75);
        assertEquals("75.0%", columnGap.getCssValue());
    }

    @Test
    public void testWebkitColumnGapFloatCssLengthUnit() {
        {
            WebkitColumnGap columnGap = new WebkitColumnGap(75, CssLengthUnit.PER);
            assertEquals("75.0%", columnGap.getCssValue());
            assertEquals(CssLengthUnit.PER, columnGap.getUnit());
            assertTrue(columnGap.getValue() == 75);
        }
        {
            WebkitColumnGap columnGap = new WebkitColumnGap(75, CssLengthUnit.CH);
            assertEquals("75.0ch", columnGap.getCssValue());
        }
        {
            WebkitColumnGap columnGap = new WebkitColumnGap(75, CssLengthUnit.EM);
            assertEquals("75.0em", columnGap.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            WebkitColumnGap columnGap = new WebkitColumnGap();
            columnGap.setPercent(75);
            assertEquals("75.0%", columnGap.getCssValue());
            assertEquals(CssLengthUnit.PER, columnGap.getUnit());
            assertTrue(columnGap.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        WebkitColumnGap columnGap = new WebkitColumnGap();
        assertEquals(CssNameConstants.WEBKIT_COLUMN_GAP, columnGap.getCssName());
    }

    @Test
    public void testGetCssValue() {
        WebkitColumnGap columnGap = new WebkitColumnGap();
        columnGap.setAsInherit();
        assertEquals(WebkitColumnGap.INHERIT, columnGap.getCssValue());
        columnGap.setAsInitial();
        assertEquals(WebkitColumnGap.INITIAL, columnGap.getCssValue());
    }

    @Test
    public void testToString() {
        WebkitColumnGap columnGap = new WebkitColumnGap(75, CssLengthUnit.EM);
        assertEquals(columnGap.getCssName()+": 75.0em", columnGap.toString());
    }

    @Test
    public void testGetValue() {
        WebkitColumnGap columnGap = new WebkitColumnGap();
        columnGap.setPercent(75);
        assertTrue(columnGap.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        WebkitColumnGap columnGap = new WebkitColumnGap();
        columnGap.setPercent(75);
        assertEquals(CssLengthUnit.PER, columnGap.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        WebkitColumnGap columnGap = new WebkitColumnGap();
        columnGap.setCssValue("75%");
        assertEquals("75%", columnGap.getCssValue());
        assertEquals(CssLengthUnit.PER, columnGap.getUnit());
        assertTrue(columnGap.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        WebkitColumnGap columnGap = new WebkitColumnGap();
        columnGap.setAsInitial();
        assertEquals(WebkitColumnGap.INITIAL, columnGap.getCssValue());
       assertNull(columnGap.getValue());
       assertNull(columnGap.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        WebkitColumnGap columnGap = new WebkitColumnGap();
        columnGap.setAsInherit();
        assertEquals(WebkitColumnGap.INHERIT, columnGap.getCssValue());
       assertNull(columnGap.getValue());
       assertNull(columnGap.getUnit());
    }
    
    @Test
    public void testSetAsNormal() {
        WebkitColumnGap columnGap = new WebkitColumnGap();
        columnGap.setAsInherit();
        columnGap.setAsNormal();
        assertEquals(WebkitColumnGap.NORMAL, columnGap.getCssValue());
       assertNull(columnGap.getValue());
       assertNull(columnGap.getUnit());
    }
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = WebkitColumnGap.isValid("45px");
            assertTrue(valid);
            final boolean invalid = WebkitColumnGap.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = WebkitColumnGap.isValid("45em");
            assertTrue(valid);
            final boolean invalid = WebkitColumnGap.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = WebkitColumnGap.isValid("45%");
            assertTrue(valid);
            final boolean invalid = WebkitColumnGap.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = WebkitColumnGap.isValid("45em");
            assertTrue(valid);
            final boolean invalid = WebkitColumnGap.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = WebkitColumnGap.isValid("45rem");
            assertTrue(valid);
        }
    }

}
