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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.webfirmframework.wffweb.InvalidValueException;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class PaddingTopTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testPaddingTop() {
        final PaddingTop paddingTop = new PaddingTop();
        assertEquals("0px", paddingTop.getCssValue());
    }

    @Test
    public void testPaddingTopString() {
        {
            final PaddingTop paddingTop = new PaddingTop(PaddingTop.INITIAL);
            assertEquals(PaddingTop.INITIAL, paddingTop.getCssValue());
        }
        {
            final PaddingTop paddingTop = new PaddingTop("50px");
            assertEquals("50px", paddingTop.getCssValue());
        }
    }

    @Test
    public void testPaddingTopPaddingTop() {
        final PaddingTop paddingTop = new PaddingTop("50px");
        final PaddingTop paddingTop1 = new PaddingTop(paddingTop);
        assertEquals("50px", paddingTop1.getCssValue());
    }

    @Test
    public void testPaddingTopFloat() {
        final PaddingTop paddingTop = new PaddingTop(75);
        assertEquals("75.0%", paddingTop.getCssValue());
    }

    @Test
    public void testPaddingTopFloatCssLengthUnit() {
        {
            final PaddingTop paddingTop = new PaddingTop(75, CssLengthUnit.PER);
            assertEquals("75.0%", paddingTop.getCssValue());
            assertEquals(CssLengthUnit.PER, paddingTop.getUnit());
            assertTrue(paddingTop.getValue() == 75);
        }
        {
            final PaddingTop paddingTop = new PaddingTop(75, CssLengthUnit.CH);
            assertEquals("75.0ch", paddingTop.getCssValue());
        }
        {
            final PaddingTop paddingTop = new PaddingTop(75, CssLengthUnit.EM);
            assertEquals("75.0em", paddingTop.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            final PaddingTop paddingTop = new PaddingTop();
            paddingTop.setPercent(75);
            assertEquals("75.0%", paddingTop.getCssValue());
            assertEquals(CssLengthUnit.PER, paddingTop.getUnit());
            assertTrue(paddingTop.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        final PaddingTop paddingTop = new PaddingTop();
        assertEquals(CssNameConstants.PADDING_TOP, paddingTop.getCssName());
    }

    @Test
    public void testGetCssValue() {
        final PaddingTop paddingTop = new PaddingTop();
        paddingTop.setAsInherit();
        assertEquals(PaddingTop.INHERIT, paddingTop.getCssValue());
        paddingTop.setAsInitial();
        assertEquals(PaddingTop.INITIAL, paddingTop.getCssValue());
    }

    @Test
    public void testToString() {
        final PaddingTop paddingTop = new PaddingTop(75, CssLengthUnit.EM);
        assertEquals(paddingTop.getCssName() + ": 75.0em",
                paddingTop.toString());
    }

    @Test
    public void testGetValue() {
        final PaddingTop paddingTop = new PaddingTop();
        paddingTop.setPercent(75);
        assertTrue(paddingTop.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        final PaddingTop paddingTop = new PaddingTop();
        paddingTop.setPercent(75);
        assertEquals(CssLengthUnit.PER, paddingTop.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        final PaddingTop paddingTop = new PaddingTop();
        paddingTop.setCssValue("75%");
        assertEquals("75%", paddingTop.getCssValue());
        assertEquals(CssLengthUnit.PER, paddingTop.getUnit());
        assertTrue(paddingTop.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        final PaddingTop paddingTop = new PaddingTop();
        paddingTop.setAsInitial();
        assertEquals(PaddingTop.INITIAL, paddingTop.getCssValue());
       assertNull(paddingTop.getValue());
       assertNull(paddingTop.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        final PaddingTop paddingTop = new PaddingTop();
        paddingTop.setAsInherit();
        assertEquals(PaddingTop.INHERIT, paddingTop.getCssValue());
       assertNull(paddingTop.getValue());
       assertNull(paddingTop.getUnit());
    }

    @Test
    public void testIsValid() {
        {
            final boolean valid = PaddingTop.isValid("45px");
            assertTrue(valid);
            final boolean invalid = PaddingTop.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingTop.isValid("45em");
            assertTrue(valid);
            final boolean invalid = PaddingTop.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingTop.isValid("45%");
            assertTrue(valid);
            final boolean invalid = PaddingTop.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingTop.isValid("45em");
            assertTrue(valid);
            final boolean invalid = PaddingTop.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = PaddingTop.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = PaddingTop.isValid("-1px");
           assertTrue(valid);
        }
    }

    @Test
    public void testInvalidValueForSetCssValue() throws Exception {
        exception.expect(InvalidValueException.class);
        exception.expectMessage(
                "dfdfd1 is an invalid value. The value format should be as for example 75px, 85%, initial, inherit etc..");
        final PaddingTop padding = new PaddingTop();
        padding.setCssValue("dfdfd1");

    }

}
