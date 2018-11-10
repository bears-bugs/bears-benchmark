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
public class WebkitFlexBasisTest {

    @Test
    public void testWebkitFlexBasis() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        assertEquals(WebkitFlexBasis.AUTO, flexBasis.getCssValue());
    }

    @Test
    public void testWebkitFlexBasisString() {
        {
            WebkitFlexBasis flexBasis = new WebkitFlexBasis(WebkitFlexBasis.INITIAL);
            assertEquals(WebkitFlexBasis.INITIAL, flexBasis.getCssValue());
        }
        {
            WebkitFlexBasis flexBasis = new WebkitFlexBasis("50px");
            assertEquals("50px", flexBasis.getCssValue());
        }
    }

    @Test
    public void testWebkitFlexBasisWebkitFlexBasis() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis("50px");
        WebkitFlexBasis flexBasis1 = new WebkitFlexBasis(flexBasis);
        assertEquals("50px", flexBasis1.getCssValue());
    }

    @Test
    public void testWebkitFlexBasisFloat() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis(75);
        assertEquals("75.0%", flexBasis.getCssValue());
    }

    @Test
    public void testWebkitFlexBasisFloatCssLengthUnit() {
        {
            WebkitFlexBasis flexBasis = new WebkitFlexBasis(75, CssLengthUnit.PER);
            assertEquals("75.0%", flexBasis.getCssValue());
            assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
            assertTrue(flexBasis.getValue() == 75);
        }
        {
            WebkitFlexBasis flexBasis = new WebkitFlexBasis(75, CssLengthUnit.CH);
            assertEquals("75.0ch", flexBasis.getCssValue());
        }
        {
            WebkitFlexBasis flexBasis = new WebkitFlexBasis(75, CssLengthUnit.EM);
            assertEquals("75.0em", flexBasis.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            WebkitFlexBasis flexBasis = new WebkitFlexBasis();
            flexBasis.setPercent(75);
            assertEquals("75.0%", flexBasis.getCssValue());
            assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
            assertTrue(flexBasis.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        assertEquals(CssNameConstants.WEBKIT_FLEX_BASIS, flexBasis.getCssName());
    }

    @Test
    public void testGetCssValue() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        flexBasis.setAsInherit();
        assertEquals(WebkitFlexBasis.INHERIT, flexBasis.getCssValue());
        flexBasis.setAsInitial();
        assertEquals(WebkitFlexBasis.INITIAL, flexBasis.getCssValue());
    }

    @Test
    public void testToString() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis(75, CssLengthUnit.EM);
        assertEquals(flexBasis.getCssName() + ": 75.0em", flexBasis.toString());
    }

    @Test
    public void testGetValue() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        flexBasis.setPercent(75);
        assertTrue(flexBasis.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        flexBasis.setPercent(75);
        assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        flexBasis.setCssValue("75%");
        assertEquals("75%", flexBasis.getCssValue());
        assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
        assertTrue(flexBasis.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        flexBasis.setAsInitial();
        assertEquals(WebkitFlexBasis.INITIAL, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        flexBasis.setAsInherit();
        assertEquals(WebkitFlexBasis.INHERIT, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testSetAsAuto() {
        WebkitFlexBasis flexBasis = new WebkitFlexBasis();
        flexBasis.setAsInherit();
        flexBasis.setAsAuto();
        assertEquals(WebkitFlexBasis.AUTO, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testIsValid() {
        {
            final boolean valid = WebkitFlexBasis.isValid("45px");
            assertTrue(valid);
            final boolean invalid = WebkitFlexBasis.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = WebkitFlexBasis.isValid("45em");
            assertTrue(valid);
            final boolean invalid = WebkitFlexBasis.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = WebkitFlexBasis.isValid("45%");
            assertTrue(valid);
            final boolean invalid = WebkitFlexBasis.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = WebkitFlexBasis.isValid("45em");
            assertTrue(valid);
            final boolean invalid = WebkitFlexBasis.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = WebkitFlexBasis.isValid("45rem");
            assertTrue(valid);
        }
    }

}
