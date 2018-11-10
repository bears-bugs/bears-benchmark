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
public class MarginRightTest {

    @Test
    public void testMarginRight() {
        MarginRight marginRight = new MarginRight();
        assertEquals("0px", marginRight.getCssValue());
    }

    @Test
    public void testMarginRightString() {
        {
            MarginRight marginRight = new MarginRight(MarginRight.INITIAL);
            assertEquals(MarginRight.INITIAL, marginRight.getCssValue());
        }
        {
            MarginRight marginRight = new MarginRight("50px");
            assertEquals("50px", marginRight.getCssValue());
        }
    }

    @Test
    public void testMarginRightMarginRight() {
        MarginRight marginRight = new MarginRight("50px");
        MarginRight marginRight1 = new MarginRight(marginRight);
        assertEquals("50px", marginRight1.getCssValue());
    }

    @Test
    public void testMarginRightFloat() {
        MarginRight marginRight = new MarginRight(75);
        assertEquals("75.0%", marginRight.getCssValue());
    }

    @Test
    public void testMarginRightFloatCssLengthUnit() {
        {
            MarginRight marginRight = new MarginRight(75, CssLengthUnit.PER);
            assertEquals("75.0%", marginRight.getCssValue());
            assertEquals(CssLengthUnit.PER, marginRight.getUnit());
            assertTrue(marginRight.getValue() == 75);
        }
        {
            MarginRight marginRight = new MarginRight(75, CssLengthUnit.CH);
            assertEquals("75.0ch", marginRight.getCssValue());
        }
        {
            MarginRight marginRight = new MarginRight(75, CssLengthUnit.EM);
            assertEquals("75.0em", marginRight.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            MarginRight marginRight = new MarginRight();
            marginRight.setPercent(75);
            assertEquals("75.0%", marginRight.getCssValue());
            assertEquals(CssLengthUnit.PER, marginRight.getUnit());
            assertTrue(marginRight.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        MarginRight marginRight = new MarginRight();
        assertEquals(CssNameConstants.MARGIN_RIGHT, marginRight.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MarginRight marginRight = new MarginRight();
        marginRight.setAsInherit();
        assertEquals(MarginRight.INHERIT, marginRight.getCssValue());
        marginRight.setAsInitial();
        assertEquals(MarginRight.INITIAL, marginRight.getCssValue());
    }

    @Test
    public void testToString() {
        MarginRight marginRight = new MarginRight(75, CssLengthUnit.EM);
        assertEquals(marginRight.getCssName()+": 75.0em", marginRight.toString());
    }

    @Test
    public void testGetValue() {
        MarginRight marginRight = new MarginRight();
        marginRight.setPercent(75);
        assertTrue(marginRight.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        MarginRight marginRight = new MarginRight();
        marginRight.setPercent(75);
        assertEquals(CssLengthUnit.PER, marginRight.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        MarginRight marginRight = new MarginRight();
        marginRight.setCssValue("75%");
        assertEquals("75%", marginRight.getCssValue());
        assertEquals(CssLengthUnit.PER, marginRight.getUnit());
        assertTrue(marginRight.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        MarginRight marginRight = new MarginRight();
        marginRight.setAsInitial();
        assertEquals(MarginRight.INITIAL, marginRight.getCssValue());
       assertNull(marginRight.getValue());
       assertNull(marginRight.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        MarginRight marginRight = new MarginRight();
        marginRight.setAsInherit();
        assertEquals(MarginRight.INHERIT, marginRight.getCssValue());
       assertNull(marginRight.getValue());
       assertNull(marginRight.getUnit());
    }
    
    @Test
    public void testSetAsAuto() {
        MarginRight marginRight = new MarginRight();
        marginRight.setAsAuto();
        assertEquals(MarginRight.AUTO, marginRight.getCssValue());
       assertNull(marginRight.getValue());
       assertNull(marginRight.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = MarginRight.isValid("45px");
            assertTrue(valid);
            final boolean invalid = MarginRight.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginRight.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MarginRight.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginRight.isValid("45%");
            assertTrue(valid);
            final boolean invalid = MarginRight.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginRight.isValid("45em");
            assertTrue(valid);
            final boolean invalid = MarginRight.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = MarginRight.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = MarginRight.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        MarginRight margin = new MarginRight();
        margin.setCssValue("--1px");
    }

}
