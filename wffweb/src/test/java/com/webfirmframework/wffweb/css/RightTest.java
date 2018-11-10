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
public class RightTest {

    @Test
    public void testRight() {
        Right right = new Right();
        assertEquals(Right.AUTO, right.getCssValue());
    }

    @Test
    public void testRightString() {
        {
            Right right = new Right(Right.INITIAL);
            assertEquals(Right.INITIAL, right.getCssValue());
        }
        {
            Right right = new Right("50px");
            assertEquals("50px", right.getCssValue());
        }
    }

    @Test
    public void testRightRight() {
        Right right = new Right("50px");
        Right right1 = new Right(right);
        assertEquals("50px", right1.getCssValue());
    }

    @Test
    public void testRightFloat() {
        Right right = new Right(75);
        assertEquals("75.0%", right.getCssValue());
    }

    @Test
    public void testRightFloatCssLengthUnit() {
        {
            Right right = new Right(75, CssLengthUnit.PER);
            assertEquals("75.0%", right.getCssValue());
            assertEquals(CssLengthUnit.PER, right.getUnit());
            assertTrue(right.getValue() == 75);
        }
        {
            Right right = new Right(75, CssLengthUnit.CH);
            assertEquals("75.0ch", right.getCssValue());
        }
        {
            Right right = new Right(75, CssLengthUnit.EM);
            assertEquals("75.0em", right.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            Right right = new Right();
            right.setPercent(75);
            assertEquals("75.0%", right.getCssValue());
            assertEquals(CssLengthUnit.PER, right.getUnit());
            assertTrue(right.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        Right right = new Right();
        assertEquals(CssNameConstants.RIGHT, right.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Right right = new Right();
        right.setAsInherit();
        assertEquals(Right.INHERIT, right.getCssValue());
        right.setAsInitial();
        assertEquals(Right.INITIAL, right.getCssValue());
    }

    @Test
    public void testToString() {
        Right right = new Right(75, CssLengthUnit.EM);
        assertEquals(right.getCssName()+": 75.0em", right.toString());
    }

    @Test
    public void testGetValue() {
        Right right = new Right();
        right.setPercent(75);
        assertTrue(right.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        Right right = new Right();
        right.setPercent(75);
        assertEquals(CssLengthUnit.PER, right.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        Right right = new Right();
        right.setCssValue("75%");
        assertEquals("75%", right.getCssValue());
        assertEquals(CssLengthUnit.PER, right.getUnit());
        assertTrue(right.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        Right right = new Right();
        right.setAsInitial();
        assertEquals(Right.INITIAL, right.getCssValue());
       assertNull(right.getValue());
       assertNull(right.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        Right right = new Right();
        right.setAsInherit();
        assertEquals(Right.INHERIT, right.getCssValue());
       assertNull(right.getValue());
       assertNull(right.getUnit());
    }
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = Right.isValid("45px");
            assertTrue(valid);
            final boolean invalid = Right.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = Right.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Right.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = Right.isValid("45%");
            assertTrue(valid);
            final boolean invalid = Right.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = Right.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Right.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = Right.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = Right.isValid("--1px");
           assertFalse(valid);
        }
    }
    
    @Test(expected =  InvalidValueException.class)
    public void testInvalidValueForSetCssValue() throws Exception {
        Right padding = new Right();
        padding.setCssValue("--1px");
    }

}
