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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class TopTest {

    @Test
    public void testTop() {
        Top top = new Top();
        assertEquals(Top.AUTO, top.getCssValue());
    }

    @Test
    public void testTopString() {
        {
            Top top = new Top(Top.INITIAL);
            assertEquals(Top.INITIAL, top.getCssValue());
        }
        {
            Top top = new Top("50px");
            assertEquals("50px", top.getCssValue());
        }
    }

    @Test
    public void testTopTop() {
        Top top = new Top("50px");
        Top top1 = new Top(top);
        assertEquals("50px", top1.getCssValue());
    }

    @Test
    public void testTopFloat() {
        Top top = new Top(75);
        assertEquals("75.0%", top.getCssValue());
    }

    @Test
    public void testTopFloatCssLengthUnit() {
        {
            Top top = new Top(75, CssLengthUnit.PER);
            assertEquals("75.0%", top.getCssValue());
            assertEquals(CssLengthUnit.PER, top.getUnit());
            assertTrue(top.getValue() == 75);
        }
        {
            Top top = new Top(75, CssLengthUnit.CH);
            assertEquals("75.0ch", top.getCssValue());
        }
        {
            Top top = new Top(75, CssLengthUnit.EM);
            assertEquals("75.0em", top.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            Top top = new Top();
            top.setPercent(75);
            assertEquals("75.0%", top.getCssValue());
            assertEquals(CssLengthUnit.PER, top.getUnit());
            assertTrue(top.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        Top top = new Top();
        assertEquals(CssNameConstants.TOP, top.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Top top = new Top();
        top.setAsInherit();
        assertEquals(Top.INHERIT, top.getCssValue());
        top.setAsInitial();
        assertEquals(Top.INITIAL, top.getCssValue());
    }

    @Test
    public void testToString() {
        Top top = new Top(75, CssLengthUnit.EM);
        assertEquals(top.getCssName()+": 75.0em", top.toString());
    }

    @Test
    public void testGetValue() {
        Top top = new Top();
        top.setPercent(75);
        assertTrue(top.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        Top top = new Top();
        top.setPercent(75);
        assertEquals(CssLengthUnit.PER, top.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        Top top = new Top();
        top.setCssValue("75%");
        assertEquals("75%", top.getCssValue());
        assertEquals(CssLengthUnit.PER, top.getUnit());
        assertTrue(top.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        Top top = new Top();
        top.setAsInitial();
        assertEquals(Top.INITIAL, top.getCssValue());
    }

    @Test
    public void testSetAsInherit() {
        Top top = new Top();
        top.setAsInherit();
        assertEquals(Top.INHERIT, top.getCssValue());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = Top.isValid("45px");
            assertTrue(valid);
            final boolean invalid = Top.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = Top.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Top.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = Top.isValid("45%");
            assertTrue(valid);
            final boolean invalid = Top.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = Top.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Top.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = Top.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = Top.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        Top padding = new Top();
        padding.setCssValue("--1px");
    }

}
