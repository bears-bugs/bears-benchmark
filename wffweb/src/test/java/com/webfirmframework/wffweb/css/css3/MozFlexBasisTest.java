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
public class MozFlexBasisTest {

    @Test
    public void testMozFlexBasis() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        assertEquals(MozFlexBasis.AUTO, flexBasis.getCssValue());
    }

    @Test
    public void testMozFlexBasisString() {
        {
            MozFlexBasis flexBasis = new MozFlexBasis(MozFlexBasis.INITIAL);
            assertEquals(MozFlexBasis.INITIAL, flexBasis.getCssValue());
        }
        {
            MozFlexBasis flexBasis = new MozFlexBasis("50px");
            assertEquals("50px", flexBasis.getCssValue());
        }
    }

    @Test
    public void testMozFlexBasisMozFlexBasis() {
        MozFlexBasis flexBasis = new MozFlexBasis("50px");
        MozFlexBasis flexBasis1 = new MozFlexBasis(flexBasis);
        assertEquals("50px", flexBasis1.getCssValue());
    }

    @Test
    public void testMozFlexBasisFloat() {
        MozFlexBasis flexBasis = new MozFlexBasis(75);
        assertEquals("75.0%", flexBasis.getCssValue());
    }

    @Test
    public void testMozFlexBasisFloatCssLengthUnit() {
        {
            MozFlexBasis flexBasis = new MozFlexBasis(75, CssLengthUnit.PER);
            assertEquals("75.0%", flexBasis.getCssValue());
            assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
            assertTrue(flexBasis.getValue() == 75);
        }
        {
            MozFlexBasis flexBasis = new MozFlexBasis(75, CssLengthUnit.CH);
            assertEquals("75.0ch", flexBasis.getCssValue());
        }
        {
            MozFlexBasis flexBasis = new MozFlexBasis(75, CssLengthUnit.EM);
            assertEquals("75.0em", flexBasis.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            MozFlexBasis flexBasis = new MozFlexBasis();
            flexBasis.setPercent(75);
            assertEquals("75.0%", flexBasis.getCssValue());
            assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
            assertTrue(flexBasis.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        assertEquals(CssNameConstants.MOZ_FLEX_BASIS, flexBasis.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        flexBasis.setAsInherit();
        assertEquals(MozFlexBasis.INHERIT, flexBasis.getCssValue());
        flexBasis.setAsInitial();
        assertEquals(MozFlexBasis.INITIAL, flexBasis.getCssValue());
    }

    @Test
    public void testToString() {
        MozFlexBasis flexBasis = new MozFlexBasis(75, CssLengthUnit.EM);
        assertEquals(flexBasis.getCssName() + ": 75.0em", flexBasis.toString());
    }

    @Test
    public void testGetValue() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        flexBasis.setPercent(75);
        assertTrue(flexBasis.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        flexBasis.setPercent(75);
        assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        flexBasis.setCssValue("75%");
        assertEquals("75%", flexBasis.getCssValue());
        assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
        assertTrue(flexBasis.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        flexBasis.setAsInitial();
        assertEquals(MozFlexBasis.INITIAL, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        flexBasis.setAsInherit();
        assertEquals(MozFlexBasis.INHERIT, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testSetAsAuto() {
        MozFlexBasis flexBasis = new MozFlexBasis();
        flexBasis.setAsInherit();
        flexBasis.setAsAuto();
        assertEquals(MozFlexBasis.AUTO, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testIsValid() {
        {
            final boolean valid = MozFlexBasis.isValid("45px");
            assertTrue(valid);
            final boolean invalid = MozFlexBasis.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = MozFlexBasis.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MozFlexBasis.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = MozFlexBasis.isValid("45%");
            assertTrue(valid);
            final boolean invalid = MozFlexBasis.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = MozFlexBasis.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MozFlexBasis.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = MozFlexBasis.isValid("45rem");
            assertTrue(valid);
        }
    }

}
