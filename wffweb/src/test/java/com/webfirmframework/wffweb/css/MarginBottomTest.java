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
public class MarginBottomTest {

    @Test
    public void testMarginBottom() {
        MarginBottom marginBottom = new MarginBottom();
        assertEquals("0px", marginBottom.getCssValue());
    }

    @Test
    public void testMarginBottomString() {
        {
            MarginBottom marginBottom = new MarginBottom(MarginBottom.INITIAL);
            assertEquals(MarginBottom.INITIAL, marginBottom.getCssValue());
        }
        {
            MarginBottom marginBottom = new MarginBottom("50px");
            assertEquals("50px", marginBottom.getCssValue());
        }
    }

    @Test
    public void testMarginBottomMarginBottom() {
        MarginBottom marginBottom = new MarginBottom("50px");
        MarginBottom marginBottom1 = new MarginBottom(marginBottom);
        assertEquals("50px", marginBottom1.getCssValue());
    }

    @Test
    public void testMarginBottomFloat() {
        MarginBottom marginBottom = new MarginBottom(75);
        assertEquals("75.0%", marginBottom.getCssValue());
    }

    @Test
    public void testMarginBottomFloatCssLengthUnit() {
        {
            MarginBottom marginBottom = new MarginBottom(75, CssLengthUnit.PER);
            assertEquals("75.0%", marginBottom.getCssValue());
            assertEquals(CssLengthUnit.PER, marginBottom.getUnit());
            assertTrue(marginBottom.getValue() == 75);
        }
        {
            MarginBottom marginBottom = new MarginBottom(75, CssLengthUnit.CH);
            assertEquals("75.0ch", marginBottom.getCssValue());
        }
        {
            MarginBottom marginBottom = new MarginBottom(75, CssLengthUnit.EM);
            assertEquals("75.0em", marginBottom.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            MarginBottom marginBottom = new MarginBottom();
            marginBottom.setPercent(75);
            assertEquals("75.0%", marginBottom.getCssValue());
            assertEquals(CssLengthUnit.PER, marginBottom.getUnit());
            assertTrue(marginBottom.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        MarginBottom marginBottom = new MarginBottom();
        assertEquals(CssNameConstants.MARGIN_BOTTOM, marginBottom.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MarginBottom marginBottom = new MarginBottom();
        marginBottom.setAsInherit();
        assertEquals(MarginBottom.INHERIT, marginBottom.getCssValue());
        marginBottom.setAsInitial();
        assertEquals(MarginBottom.INITIAL, marginBottom.getCssValue());
    }

    @Test
    public void testToString() {
        MarginBottom marginBottom = new MarginBottom(75, CssLengthUnit.EM);
        assertEquals(marginBottom.getCssName()+": 75.0em", marginBottom.toString());
    }

    @Test
    public void testGetValue() {
        MarginBottom marginBottom = new MarginBottom();
        marginBottom.setPercent(75);
        assertTrue(marginBottom.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        MarginBottom marginBottom = new MarginBottom();
        marginBottom.setPercent(75);
        assertEquals(CssLengthUnit.PER, marginBottom.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        MarginBottom marginBottom = new MarginBottom();
        marginBottom.setCssValue("75%");
        assertEquals("75%", marginBottom.getCssValue());
        assertEquals(CssLengthUnit.PER, marginBottom.getUnit());
        assertTrue(marginBottom.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        MarginBottom marginBottom = new MarginBottom();
        marginBottom.setAsInitial();
        assertEquals(MarginBottom.INITIAL, marginBottom.getCssValue());
       assertNull(marginBottom.getValue());
       assertNull(marginBottom.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        MarginBottom marginBottom = new MarginBottom();
        marginBottom.setAsInherit();
        assertEquals(MarginBottom.INHERIT, marginBottom.getCssValue());
       assertNull(marginBottom.getValue());
       assertNull(marginBottom.getUnit());
    }
    
    @Test
    public void testSetAsAuto() {
        MarginBottom marginBottom = new MarginBottom();
        marginBottom.setAsAuto();
        assertEquals(MarginBottom.AUTO, marginBottom.getCssValue());
       assertNull(marginBottom.getValue());
       assertNull(marginBottom.getUnit());
    }    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = MarginBottom.isValid("45px");
            assertTrue(valid);
            final boolean invalid = MarginBottom.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginBottom.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MarginBottom.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginBottom.isValid("45%");
            assertTrue(valid);
            final boolean invalid = MarginBottom.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginBottom.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MarginBottom.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginBottom.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = MarginBottom.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        MarginBottom margin = new MarginBottom();
        margin.setCssValue("--1px");
    }

}
