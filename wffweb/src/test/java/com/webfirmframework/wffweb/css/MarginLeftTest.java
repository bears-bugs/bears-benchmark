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
public class MarginLeftTest {

    @Test
    public void testMarginLeft() {
        MarginLeft marginLeft = new MarginLeft();
        assertEquals("0px", marginLeft.getCssValue());
    }

    @Test
    public void testMarginLeftString() {
        {
            MarginLeft marginLeft = new MarginLeft(MarginLeft.INITIAL);
            assertEquals(MarginLeft.INITIAL, marginLeft.getCssValue());
        }
        {
            MarginLeft marginLeft = new MarginLeft("50px");
            assertEquals("50px", marginLeft.getCssValue());
        }
    }

    @Test
    public void testMarginLeftMarginLeft() {
        MarginLeft marginLeft = new MarginLeft("50px");
        MarginLeft marginLeft1 = new MarginLeft(marginLeft);
        assertEquals("50px", marginLeft1.getCssValue());
    }

    @Test
    public void testMarginLeftFloat() {
        MarginLeft marginLeft = new MarginLeft(75);
        assertEquals("75.0%", marginLeft.getCssValue());
    }

    @Test
    public void testMarginLeftFloatCssLengthUnit() {
        {
            MarginLeft marginLeft = new MarginLeft(75, CssLengthUnit.PER);
            assertEquals("75.0%", marginLeft.getCssValue());
            assertEquals(CssLengthUnit.PER, marginLeft.getUnit());
            assertTrue(marginLeft.getValue() == 75);
        }
        {
            MarginLeft marginLeft = new MarginLeft(75, CssLengthUnit.CH);
            assertEquals("75.0ch", marginLeft.getCssValue());
        }
        {
            MarginLeft marginLeft = new MarginLeft(75, CssLengthUnit.EM);
            assertEquals("75.0em", marginLeft.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            MarginLeft marginLeft = new MarginLeft();
            marginLeft.setPercent(75);
            assertEquals("75.0%", marginLeft.getCssValue());
            assertEquals(CssLengthUnit.PER, marginLeft.getUnit());
            assertTrue(marginLeft.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        MarginLeft marginLeft = new MarginLeft();
        assertEquals(CssNameConstants.MARGIN_LEFT, marginLeft.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MarginLeft marginLeft = new MarginLeft();
        marginLeft.setAsInherit();
        assertEquals(MarginLeft.INHERIT, marginLeft.getCssValue());
        marginLeft.setAsInitial();
        assertEquals(MarginLeft.INITIAL, marginLeft.getCssValue());
    }

    @Test
    public void testToString() {
        MarginLeft marginLeft = new MarginLeft(75, CssLengthUnit.EM);
        assertEquals(marginLeft.getCssName()+": 75.0em", marginLeft.toString());
    }

    @Test
    public void testGetValue() {
        MarginLeft marginLeft = new MarginLeft();
        marginLeft.setPercent(75);
        assertTrue(marginLeft.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        MarginLeft marginLeft = new MarginLeft();
        marginLeft.setPercent(75);
        assertEquals(CssLengthUnit.PER, marginLeft.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        MarginLeft marginLeft = new MarginLeft();
        marginLeft.setCssValue("75%");
        assertEquals("75%", marginLeft.getCssValue());
        assertEquals(CssLengthUnit.PER, marginLeft.getUnit());
        assertTrue(marginLeft.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        MarginLeft marginLeft = new MarginLeft();
        marginLeft.setAsInitial();
        assertEquals(MarginLeft.INITIAL, marginLeft.getCssValue());
       assertNull(marginLeft.getValue());
       assertNull(marginLeft.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        MarginLeft marginLeft = new MarginLeft();
        marginLeft.setAsInherit();
        assertEquals(MarginLeft.INHERIT, marginLeft.getCssValue());
       assertNull(marginLeft.getValue());
       assertNull(marginLeft.getUnit());
    }
    
    @Test
    public void testSetAsAuto() {
        MarginLeft marginLeft = new MarginLeft();
        marginLeft.setAsAuto();
        assertEquals(MarginLeft.AUTO, marginLeft.getCssValue());
       assertNull(marginLeft.getValue());
       assertNull(marginLeft.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = MarginLeft.isValid("45px");
            assertTrue(valid);
            final boolean invalid = MarginLeft.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginLeft.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MarginLeft.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginLeft.isValid("45%");
            assertTrue(valid);
            final boolean invalid = MarginLeft.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginLeft.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MarginLeft.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginLeft.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = MarginLeft.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        MarginLeft margin = new MarginLeft();
        margin.setCssValue("--1px");
    }

}
