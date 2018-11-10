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
public class HslaCssValueTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#HslaCssValue()}.
     */
    @Test
    public void testHslaCssValue() {
        HslaCssValue hslaCssValue = new HslaCssValue();
        assertEquals("hsla(0, 0%, 0%, 1)", hslaCssValue.getValue());
        assertEquals(0F, hslaCssValue.getH(), 0);
        assertEquals(0F, hslaCssValue.getS(), 0);
        assertEquals(0F, hslaCssValue.getL(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#HslaCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testHslaCssValueString() {
        HslaCssValue hslaCssValue = new HslaCssValue("hsla(120, 10%, 100%, 1)");
        assertEquals("hsla(120, 10%, 100%, 1)", hslaCssValue.getValue());
        assertEquals(120F, hslaCssValue.getH(), 0);
        assertEquals(10F, hslaCssValue.getS(), 0);
        assertEquals(100F, hslaCssValue.getL(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#HslaCssValue(com.webfirmframework.wffweb.css.HslaCssValue)}
     * .
     */
    @Test
    public void testHslaCssValueHslaCssValue() {
        HslaCssValue hslaCssValue1 = new HslaCssValue(
                "hsla(120, 10%, 100%, 1)");
        HslaCssValue hslaCssValue = new HslaCssValue(hslaCssValue1);
        assertEquals("hsla(120, 10%, 100%, 1)", hslaCssValue.getValue());
        assertEquals(120F, hslaCssValue.getH(), 0);
        assertEquals(10F, hslaCssValue.getS(), 0);
        assertEquals(100F, hslaCssValue.getL(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#setHsla(java.lang.String)}
     * .
     */
    @Test
    public void testSetHsl() {
        HslaCssValue hslaCssValue = new HslaCssValue();
        assertEquals("hsla(0, 0%, 0%, 1)", hslaCssValue.getValue());
        hslaCssValue.setHsla("hsla(120, 10%, 100%, 0.5)");
        assertEquals(120F, hslaCssValue.getH(), 0);
        assertEquals(10F, hslaCssValue.getS(), 0);
        assertEquals(100F, hslaCssValue.getL(), 0);
        assertEquals(0.5F, hslaCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#HslaCssValue(int, int, int, float)}
     * .
     */
    @Test
    public void testHslaCssValueIntIntInt() {
        HslaCssValue hslaCssValue = new HslaCssValue(120, 10, 100, 0.5F);
        assertEquals("hsla(120, 10.0%, 100.0%, 0.5)", hslaCssValue.getValue());
        assertEquals(120F, hslaCssValue.getH(), 0);
        assertEquals(10F, hslaCssValue.getS(), 0);
        assertEquals(100F, hslaCssValue.getL(), 0);
        assertEquals(0.5F, hslaCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#getH()}.
     */
    @Test
    public void testGetH() {
        HslaCssValue hslaCssValue = new HslaCssValue(120, 10, 100, 0.5F);
        assertEquals("hsla(120, 10.0%, 100.0%, 0.5)", hslaCssValue.getValue());
        assertEquals(120F, hslaCssValue.getH(), 0);
        hslaCssValue.setH(360);
        assertEquals(360F, hslaCssValue.getH(), 0);
        assertEquals(0.5F, hslaCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#setH(int)}.
     */
    @Test
    public void testSetH() {
        testGetH();
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#getS()}.
     */
    @Test
    public void testGetS() {
        HslaCssValue hslaCssValue = new HslaCssValue(120, 10, 100, 0.5F);
        assertEquals("hsla(120, 10.0%, 100.0%, 0.5)", hslaCssValue.getValue());
        assertEquals(10F, hslaCssValue.getS(), 0);
        hslaCssValue.setS(55);
        assertEquals(55F, hslaCssValue.getS(), 0);
        assertEquals(0.5F, hslaCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#setS(int)}.
     */
    @Test
    public void testSetS() {
        testGetS();
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#getL()}.
     */
    @Test
    public void testGetL() {
        HslaCssValue hslaCssValue = new HslaCssValue(120, 10, 100, 0.5F);
        assertEquals("hsla(120, 10.0%, 100.0%, 0.5)", hslaCssValue.getValue());
        assertEquals(100F, hslaCssValue.getL(), 0);
        hslaCssValue.setL(55);
        assertEquals(55F, hslaCssValue.getL(), 0);
        assertEquals(0.5F, hslaCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#getSUnit()}.
     */
    @Test
    public void testGetSUnit() {
        assertEquals(CssLengthUnit.PER, HslaCssValue.getSUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#getLUnit()}.
     */
    @Test
    public void testGetLUnit() {
        assertEquals(CssLengthUnit.PER, HslaCssValue.getLUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#setL(int)}.
     */
    @Test
    public void testSetL() {
        HslaCssValue hslaCssValue = new HslaCssValue(120, 10, 100, 0.5F);
        assertEquals("hsla(120, 10.0%, 100.0%, 0.5)", hslaCssValue.getValue());
        assertEquals(100F, hslaCssValue.getL(), 0);
        hslaCssValue.setL(55);
        assertEquals(55F, hslaCssValue.getL(), 0);
        assertEquals(0.5F, hslaCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#toString()}.
     */
    @Test
    public void testToString() {
        HslaCssValue hslaCssValue1 = new HslaCssValue(
                "hsla(120, 10%, 100%, 1)");
        HslaCssValue hslaCssValue = new HslaCssValue(hslaCssValue1);
        assertEquals("hsla(120, 10%, 100%, 1)", hslaCssValue.toString());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#getValue()}.
     */
    @Test
    public void testGetValue() {
        HslaCssValue hslaCssValue = new HslaCssValue("hsla(120, 10%, 100%, 1)");
        assertEquals("hsla(120, 10%, 100%, 1)", hslaCssValue.getValue());
        hslaCssValue.setH(220);
        hslaCssValue.setS(55);
        hslaCssValue.setL(75);
        hslaCssValue.setA(0.5F);
        assertEquals(0.5F, hslaCssValue.getA(), 0);
        assertEquals("hsla(220, 55.0%, 75.0%, 0.5)", hslaCssValue.getValue());

    }

    @Test(expected = InvalidValueException.class)
    public void testSetHInvalidValue() {
        HslaCssValue hslaCssValue = new HslaCssValue();
        try {
            hslaCssValue.setH(361);
        } catch (InvalidValueException e) {
            hslaCssValue.setH(-1);
        }
    }

    @Test(expected = InvalidValueException.class)
    public void testSetSInvalidValue() {
        HslaCssValue hslaCssValue = new HslaCssValue();
        try {
            hslaCssValue.setS(101);
        } catch (InvalidValueException e) {
            hslaCssValue.setS(-1);
        }
    }

    @Test(expected = InvalidValueException.class)
    public void testSetLInvalidValue() {
        HslaCssValue hslaCssValue = new HslaCssValue();
        try {
            hslaCssValue.setL(101);
        } catch (InvalidValueException e) {
            hslaCssValue.setL(-1);
        }
    }

    @Test(expected = InvalidValueException.class)
    public void testSetAInvalidValue() {
        HslaCssValue hslaCssValue = new HslaCssValue();
        try {
            hslaCssValue.setA(1.5F);
        } catch (InvalidValueException e) {
            hslaCssValue.setL(-1.5F);
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.HslaCssValue#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid() {
        assertTrue(HslaCssValue.isValid("hsla(0, 0%, 0%, 1)"));
        assertTrue(HslaCssValue.isValid("hsla(0, 0.0%, 0.0%, 1)"));
        assertTrue(HslaCssValue.isValid("hsla(0, 0.0%, 0%, 1)"));
        assertTrue(HslaCssValue.isValid("hsla(0, 0%, 0.0%, 1)"));
        assertTrue(HslaCssValue.isValid("hsla(0, 0%, 100%, 1)"));
        assertTrue(HslaCssValue.isValid("hsla(0, 10%, 100%, 1)"));
        assertTrue(HslaCssValue.isValid("hsla(120, 100%, 10%, 1)"));

        assertFalse(HslaCssValue.isValid("hsla(0, 10, 100%)"));
        assertFalse(HslaCssValue.isValid("hsla(0, 10%, 100)"));
        assertFalse(HslaCssValue.isValid("hsla(0, 10%, 101%)"));
        assertFalse(HslaCssValue.isValid("hsla(0, 101%, 100%)"));
        assertFalse(HslaCssValue.isValid("hsla(-1, 100%, 100%)"));
        assertFalse(HslaCssValue.isValid("hsla(361, 100%, 100%)"));
        assertFalse(HslaCssValue.isValid("hsla(36df1, 100%, 100%)"));
        assertFalse(HslaCssValue.isValid("hsla(120, 10df0%, 10%)"));
        assertFalse(HslaCssValue.isValid("hsla(120, 100%, 1dfd0%)"));
    }

}
