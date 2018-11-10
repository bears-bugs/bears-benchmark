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

import com.webfirmframework.wffweb.InvalidValueException;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class MarginTopTest {

    @Test
    public void testMarginTop() {
        MarginTop marginTop = new MarginTop();
        assertEquals("0px", marginTop.getCssValue());
    }

    @Test
    public void testMarginTopString() {
        {
            MarginTop marginTop = new MarginTop(MarginTop.INITIAL);
            assertEquals(MarginTop.INITIAL, marginTop.getCssValue());
        }
        {
            MarginTop marginTop = new MarginTop("50px");
            assertEquals("50px", marginTop.getCssValue());
        }
    }

    @Test
    public void testMarginTopMarginTop() {
        MarginTop marginTop = new MarginTop("50px");
        MarginTop marginTop1 = new MarginTop(marginTop);
        assertEquals("50px", marginTop1.getCssValue());
    }

    @Test
    public void testMarginTopFloat() {
        MarginTop marginTop = new MarginTop(75);
        assertEquals("75.0%", marginTop.getCssValue());
    }

    @Test
    public void testMarginTopFloatCssLengthUnit() {
        {
            MarginTop marginTop = new MarginTop(75, CssLengthUnit.PER);
            assertEquals("75.0%", marginTop.getCssValue());
            assertEquals(CssLengthUnit.PER, marginTop.getUnit());
            assertTrue(marginTop.getValue() == 75);
        }
        {
            MarginTop marginTop = new MarginTop(75, CssLengthUnit.CH);
            assertEquals("75.0ch", marginTop.getCssValue());
        }
        {
            MarginTop marginTop = new MarginTop(75, CssLengthUnit.EM);
            assertEquals("75.0em", marginTop.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            MarginTop marginTop = new MarginTop();
            marginTop.setPercent(75);
            assertEquals("75.0%", marginTop.getCssValue());
            assertEquals(CssLengthUnit.PER, marginTop.getUnit());
            assertTrue(marginTop.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        MarginTop marginTop = new MarginTop();
        assertEquals(CssNameConstants.MARGIN_TOP, marginTop.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MarginTop marginTop = new MarginTop();
        marginTop.setAsInherit();
        assertEquals(MarginTop.INHERIT, marginTop.getCssValue());
        marginTop.setAsInitial();
        assertEquals(MarginTop.INITIAL, marginTop.getCssValue());
    }

    @Test
    public void testToString() {
        MarginTop marginTop = new MarginTop(75, CssLengthUnit.EM);
        assertEquals(marginTop.getCssName()+": 75.0em", marginTop.toString());
    }

    @Test
    public void testGetValue() {
        MarginTop marginTop = new MarginTop();
        marginTop.setPercent(75);
        assertTrue(marginTop.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        MarginTop marginTop = new MarginTop();
        marginTop.setPercent(75);
        assertEquals(CssLengthUnit.PER, marginTop.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        MarginTop marginTop = new MarginTop();
        marginTop.setCssValue("75%");
        assertEquals("75%", marginTop.getCssValue());
        assertEquals(CssLengthUnit.PER, marginTop.getUnit());
        assertTrue(marginTop.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        MarginTop marginTop = new MarginTop();
        marginTop.setAsInitial();
        assertEquals(MarginTop.INITIAL, marginTop.getCssValue());
       assertNull(marginTop.getValue());
       assertNull(marginTop.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        MarginTop marginTop = new MarginTop();
        marginTop.setAsInherit();
        assertEquals(MarginTop.INHERIT, marginTop.getCssValue());
       assertNull(marginTop.getValue());
       assertNull(marginTop.getUnit());
    }
    
    @Test
    public void testSetAsAuto() {
        MarginTop marginTop = new MarginTop();
        marginTop.setAsAuto();
        assertEquals(MarginTop.AUTO, marginTop.getCssValue());
       assertNull(marginTop.getValue());
       assertNull(marginTop.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = MarginTop.isValid("45px");
            assertTrue(valid);
            final boolean invalid = MarginTop.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginTop.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MarginTop.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginTop.isValid("45%");
            assertTrue(valid);
            final boolean invalid = MarginTop.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginTop.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MarginTop.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginTop.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = MarginTop.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        MarginTop margin = new MarginTop();
        margin.setCssValue("dfdpx");
    }

}
