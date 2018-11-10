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
public class LeftTest {

    @Test
    public void testLeft() {
        Left left = new Left();
        assertEquals(Left.AUTO, left.getCssValue());
    }

    @Test
    public void testLeftString() {
        {
            Left left = new Left(Left.INITIAL);
            assertEquals(Left.INITIAL, left.getCssValue());
        }
        {
            Left left = new Left("50px");
            assertEquals("50px", left.getCssValue());
        }
    }

    @Test
    public void testLeftLeft() {
        Left left = new Left("50px");
        Left left1 = new Left(left);
        assertEquals("50px", left1.getCssValue());
    }

    @Test
    public void testLeftFloat() {
        Left left = new Left(75);
        assertEquals("75.0%", left.getCssValue());
    }

    @Test
    public void testLeftFloatCssLengthUnit() {
        {
            Left left = new Left(75, CssLengthUnit.PER);
            assertEquals("75.0%", left.getCssValue());
            assertEquals(CssLengthUnit.PER, left.getUnit());
            assertTrue(left.getValue() == 75);
        }
        {
            Left left = new Left(75, CssLengthUnit.CH);
            assertEquals("75.0ch", left.getCssValue());
        }
        {
            Left left = new Left(75, CssLengthUnit.EM);
            assertEquals("75.0em", left.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            Left left = new Left();
            left.setPercent(75);
            assertEquals("75.0%", left.getCssValue());
            assertEquals(CssLengthUnit.PER, left.getUnit());
            assertTrue(left.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        Left left = new Left();
        assertEquals(CssNameConstants.LEFT, left.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Left left = new Left();
        left.setAsInherit();
        assertEquals(Left.INHERIT, left.getCssValue());
        left.setAsInitial();
        assertEquals(Left.INITIAL, left.getCssValue());
    }

    @Test
    public void testToString() {
        Left left = new Left(75, CssLengthUnit.EM);
        assertEquals(left.getCssName()+": 75.0em", left.toString());
    }

    @Test
    public void testGetValue() {
        Left left = new Left();
        left.setPercent(75);
        assertTrue(left.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        Left left = new Left();
        left.setPercent(75);
        assertEquals(CssLengthUnit.PER, left.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        Left left = new Left();
        left.setCssValue("75%");
        assertEquals("75%", left.getCssValue());
        assertEquals(CssLengthUnit.PER, left.getUnit());
        assertTrue(left.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        Left left = new Left();
        left.setAsInitial();
        assertEquals(Left.INITIAL, left.getCssValue());
       assertNull(left.getValue());
       assertNull(left.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        Left left = new Left();
        left.setAsInherit();
        assertEquals(Left.INHERIT, left.getCssValue());
       assertNull(left.getValue());
       assertNull(left.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = Left.isValid("45px");
            assertTrue(valid);
            final boolean invalid = Left.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = Left.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Left.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = Left.isValid("45%");
            assertTrue(valid);
            final boolean invalid = Left.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = Left.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Left.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = Left.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = Left.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        Left padding = new Left();
        padding.setCssValue("--1px");
    }

}
