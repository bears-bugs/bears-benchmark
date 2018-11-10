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
public class FlexBasisTest {

    @Test
    public void testFlexBasis() {
        FlexBasis flexBasis = new FlexBasis();
        assertEquals(FlexBasis.AUTO, flexBasis.getCssValue());
    }

    @Test
    public void testFlexBasisString() {
        {
            FlexBasis flexBasis = new FlexBasis(FlexBasis.INITIAL);
            assertEquals(FlexBasis.INITIAL, flexBasis.getCssValue());
        }
        {
            FlexBasis flexBasis = new FlexBasis("50px");
            assertEquals("50px", flexBasis.getCssValue());
        }
    }

    @Test
    public void testFlexBasisFlexBasis() {
        FlexBasis flexBasis = new FlexBasis("50px");
        FlexBasis flexBasis1 = new FlexBasis(flexBasis);
        assertEquals("50px", flexBasis1.getCssValue());
    }

    @Test
    public void testFlexBasisFloat() {
        FlexBasis flexBasis = new FlexBasis(75);
        assertEquals("75.0%", flexBasis.getCssValue());
    }

    @Test
    public void testFlexBasisFloatCssLengthUnit() {
        {
            FlexBasis flexBasis = new FlexBasis(75, CssLengthUnit.PER);
            assertEquals("75.0%", flexBasis.getCssValue());
            assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
            assertTrue(flexBasis.getValue() == 75);
        }
        {
            FlexBasis flexBasis = new FlexBasis(75, CssLengthUnit.CH);
            assertEquals("75.0ch", flexBasis.getCssValue());
        }
        {
            FlexBasis flexBasis = new FlexBasis(75, CssLengthUnit.EM);
            assertEquals("75.0em", flexBasis.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            FlexBasis flexBasis = new FlexBasis();
            flexBasis.setPercent(75);
            assertEquals("75.0%", flexBasis.getCssValue());
            assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
            assertTrue(flexBasis.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        FlexBasis flexBasis = new FlexBasis();
        assertEquals(CssNameConstants.FLEX_BASIS, flexBasis.getCssName());
    }

    @Test
    public void testGetCssValue() {
        FlexBasis flexBasis = new FlexBasis();
        flexBasis.setAsInherit();
        assertEquals(FlexBasis.INHERIT, flexBasis.getCssValue());
        flexBasis.setAsInitial();
        assertEquals(FlexBasis.INITIAL, flexBasis.getCssValue());
    }

    @Test
    public void testToString() {
        FlexBasis flexBasis = new FlexBasis(75, CssLengthUnit.EM);
        assertEquals(flexBasis.getCssName() + ": 75.0em", flexBasis.toString());
    }

    @Test
    public void testGetValue() {
        FlexBasis flexBasis = new FlexBasis();
        flexBasis.setPercent(75);
        assertTrue(flexBasis.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        FlexBasis flexBasis = new FlexBasis();
        flexBasis.setPercent(75);
        assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        FlexBasis flexBasis = new FlexBasis();
        flexBasis.setCssValue("75%");
        assertEquals("75%", flexBasis.getCssValue());
        assertEquals(CssLengthUnit.PER, flexBasis.getUnit());
        assertTrue(flexBasis.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        FlexBasis flexBasis = new FlexBasis();
        flexBasis.setAsInitial();
        assertEquals(FlexBasis.INITIAL, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        FlexBasis flexBasis = new FlexBasis();
        flexBasis.setAsInherit();
        assertEquals(FlexBasis.INHERIT, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testSetAsAuto() {
        FlexBasis flexBasis = new FlexBasis();
        flexBasis.setAsInherit();
        flexBasis.setAsAuto();
        assertEquals(FlexBasis.AUTO, flexBasis.getCssValue());
       assertNull(flexBasis.getValue());
       assertNull(flexBasis.getUnit());
    }

    @Test
    public void testIsValid() {
        {
            final boolean valid = FlexBasis.isValid("45px");
            assertTrue(valid);
            final boolean invalid = FlexBasis.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = FlexBasis.isValid("45em");
            assertTrue(valid);
            final boolean invalid = FlexBasis.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = FlexBasis.isValid("45%");
            assertTrue(valid);
            final boolean invalid = FlexBasis.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = FlexBasis.isValid("45em");
            assertTrue(valid);
            final boolean invalid = FlexBasis.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = FlexBasis.isValid("45rem");
            assertTrue(valid);
        }
    }

}
