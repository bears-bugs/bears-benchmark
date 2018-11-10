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
 */
package com.webfirmframework.wffweb.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class HslCssValueTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslCssValue#HslCssValue()}.
     */
    @Test
    public void testHslCssValue() {
        HslCssValue hslCssValue = new HslCssValue();
        assertEquals("hsl(0, 0%, 0%)", hslCssValue.getValue());
        assertEquals(0F, hslCssValue.getH(), 0);
        assertEquals(0F, hslCssValue.getS(), 0);
        assertEquals(0F, hslCssValue.getL(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslCssValue#HslCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testHslCssValueString() {
        HslCssValue hslCssValue = new HslCssValue("hsl(120, 10%, 100%)");
        assertEquals("hsl(120, 10%, 100%)", hslCssValue.getValue());
        assertEquals(120F, hslCssValue.getH(), 0);
        assertEquals(10F, hslCssValue.getS(), 0);
        assertEquals(100F, hslCssValue.getL(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslCssValue#HslCssValue(com.webfirmframework.wffweb.css.HslCssValue)}
     * .
     */
    @Test
    public void testHslCssValueHslCssValue() {
        HslCssValue hslCssValue1 = new HslCssValue("hsl(120, 10%, 100%)");
        HslCssValue hslCssValue = new HslCssValue(hslCssValue1);
        assertEquals("hsl(120, 10%, 100%)", hslCssValue.getValue());
        assertEquals(120F, hslCssValue.getH(), 0);
        assertEquals(10F, hslCssValue.getS(), 0);
        assertEquals(100F, hslCssValue.getL(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslCssValue#setHsl(java.lang.String)}.
     */
    @Test
    public void testSetHsl() {
        HslCssValue hslCssValue = new HslCssValue();
        assertEquals("hsl(0, 0%, 0%)", hslCssValue.getValue());
        hslCssValue.setHsl("hsl(120, 10%, 100%)");
        assertEquals(120F, hslCssValue.getH(), 0);
        assertEquals(10F, hslCssValue.getS(), 0);
        assertEquals(100F, hslCssValue.getL(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslCssValue#HslCssValue(int, int, int)}.
     */
    @Test
    public void testHslCssValueIntIntInt() {
        HslCssValue hslCssValue = new HslCssValue(120, 10, 100);
        assertEquals("hsl(120, 10.0%, 100.0%)", hslCssValue.getValue());
        assertEquals(120F, hslCssValue.getH(), 0);
        assertEquals(10F, hslCssValue.getS(), 0);
        assertEquals(100F, hslCssValue.getL(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#getH()}.
     */
    @Test
    public void testGetH() {
        HslCssValue hslCssValue = new HslCssValue(120, 10, 100);
        assertEquals("hsl(120, 10.0%, 100.0%)", hslCssValue.getValue());
        assertEquals(120F, hslCssValue.getH(), 0);
        hslCssValue.setH(360);
        assertEquals(360F, hslCssValue.getH(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#setH(int)}.
     */
    @Test
    public void testSetH() {
        testGetH();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#getS()}.
     */
    @Test
    public void testGetS() {
        HslCssValue hslCssValue = new HslCssValue(120, 10, 100);
        assertEquals("hsl(120, 10.0%, 100.0%)", hslCssValue.getValue());
        assertEquals(10F, hslCssValue.getS(), 0);
        hslCssValue.setS(55);
        assertEquals(55F, hslCssValue.getS(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#setS(int)}.
     */
    @Test
    public void testSetS() {
        testGetS();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#getL()}.
     */
    @Test
    public void testGetL() {
        HslCssValue hslCssValue = new HslCssValue(120, 10, 100);
        assertEquals("hsl(120, 10.0%, 100.0%)", hslCssValue.getValue());
        assertEquals(100F, hslCssValue.getL(), 0);
        hslCssValue.setL(55);
        assertEquals(55F, hslCssValue.getL(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#getSUnit()}.
     */
    @Test
    public void testGetSUnit() {
        assertEquals(CssLengthUnit.PER, HslCssValue.getSUnit());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#getLUnit()}.
     */
    @Test
    public void testGetLUnit() {
        assertEquals(CssLengthUnit.PER, HslCssValue.getLUnit());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#setL(int)}.
     */
    @Test
    public void testSetL() {
        testGetL();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#toString()}.
     */
    @Test
    public void testToString() {
        HslCssValue hslCssValue1 = new HslCssValue("hsl(120, 10%, 100%)");
        HslCssValue hslCssValue = new HslCssValue(hslCssValue1);
        assertEquals("hsl(120, 10%, 100%)", hslCssValue.toString());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.HslCssValue#getValue()}.
     */
    @Test
    public void testGetValue() {
        HslCssValue hslCssValue = new HslCssValue("hsl(120, 10%, 100%)");
        assertEquals("hsl(120, 10%, 100%)", hslCssValue.getValue());
        hslCssValue.setH(220);
        hslCssValue.setS(55);
        hslCssValue.setL(75);
        assertEquals("hsl(220, 55.0%, 75.0%)", hslCssValue.getValue());

    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetHInvalidValue() {
        HslCssValue hslCssValue = new HslCssValue();
        try {
            hslCssValue.setH(361);
        } catch (InvalidValueException e) {
            hslCssValue.setH(-1);
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetSInvalidValue() {
        HslCssValue hslCssValue = new HslCssValue();
        try {
            hslCssValue.setS(101);
        } catch (InvalidValueException e) {
            hslCssValue.setS(-1);
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetLInvalidValue() {
        HslCssValue hslCssValue = new HslCssValue();
        try {
            hslCssValue.setL(101);
        } catch (InvalidValueException e) {
            hslCssValue.setL(-1);
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslCssValue#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(HslCssValue.isValid("hsl(0, 0%, 0%)"));
        assertTrue(HslCssValue.isValid("hsl(0, 0.0%, 0.0%)"));
        assertTrue(HslCssValue.isValid("hsl(0, 0.0%, 0%)"));
        assertTrue(HslCssValue.isValid("hsl(0, 0%, 0.0%)"));
        assertTrue(HslCssValue.isValid("hsl(0, 0%, 100%)"));
        assertTrue(HslCssValue.isValid("hsl(0, 10%, 100%)"));
        assertTrue(HslCssValue.isValid("hsl(120, 100%, 10%)"));

        assertFalse(HslCssValue.isValid("hsl(0, 10, 100%)"));
        assertFalse(HslCssValue.isValid("hsl(0, 10%, 100)"));
        assertFalse(HslCssValue.isValid("hsl(0, 10%, 101%)"));
        assertFalse(HslCssValue.isValid("hsl(0, 101%, 100%)"));
        assertFalse(HslCssValue.isValid("hsl(-1, 100%, 100%)"));
        assertFalse(HslCssValue.isValid("hsl(361, 100%, 100%)"));
        assertFalse(HslCssValue.isValid("hsl(36df1, 100%, 100%)"));
        assertFalse(HslCssValue.isValid("hsl(120, 10df0%, 10%)"));
        assertFalse(HslCssValue.isValid("hsl(120, 100%, 1dfd0%)"));
    }

}
