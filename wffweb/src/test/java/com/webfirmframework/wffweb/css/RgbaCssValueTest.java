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
public class RgbaCssValueTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbaCssValue#RgbaCssValue()}.
     */
    @Test
    public void testRgbaCssValue() {
        RgbaCssValue rgbCssValue = new RgbaCssValue();
        assertEquals("rgba(0, 0, 0, 1)", rgbCssValue.getValue());
        assertEquals(0F, rgbCssValue.getR(), 0);
        assertEquals(0F, rgbCssValue.getG(), 0);
        assertEquals(0F, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbaCssValue#RgbaCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testRgbaCssValueString() {
        RgbaCssValue rgbCssValue = new RgbaCssValue("rgba(120, 10, 100, 0.5)");
        assertEquals("rgba(120, 10, 100, 0.5)", rgbCssValue.getValue());
        assertEquals(120F, rgbCssValue.getR(), 0);
        assertEquals(10F, rgbCssValue.getG(), 0);
        assertEquals(100F, rgbCssValue.getB(), 0);
        assertEquals(0.5F, rgbCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbaCssValue#RgbaCssValue(com.webfirmframework.wffweb.css.RgbaCssValue)}
     * .
     */
    @Test
    public void testRgbaCssValueRgbaCssValue() {
        RgbaCssValue rgbCssValue1 = new RgbaCssValue("rgba(120, 10, 100, 0.5)");
        RgbaCssValue rgbCssValue = new RgbaCssValue(rgbCssValue1);
        assertEquals("rgba(120, 10, 100, 0.5)", rgbCssValue.getValue());
        assertEquals(120F, rgbCssValue.getR(), 0);
        assertEquals(10F, rgbCssValue.getG(), 0);
        assertEquals(100F, rgbCssValue.getB(), 0);
        assertEquals(0.5F, rgbCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbaCssValue#setRgba(java.lang.String)}.
     */
    @Test
    public void testSetRsl() {
        RgbaCssValue rgbCssValue = new RgbaCssValue();
        assertEquals("rgba(0, 0, 0, 1)", rgbCssValue.getValue());
        assertEquals(1F, rgbCssValue.getA(), 0);
        rgbCssValue.setRgba("rgba(120, 10, 100, .5)");
        assertEquals(120F, rgbCssValue.getR(), 0);
        assertEquals(10F, rgbCssValue.getG(), 0);
        assertEquals(100F, rgbCssValue.getB(), 0);
        assertEquals(0.5F, rgbCssValue.getA(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbaCssValue#RgbaCssValue(int, int, int)}.
     */
    @Test
    public void testRgbaCssValueIntIntInt() {
        RgbaCssValue rgbCssValue = new RgbaCssValue(120, 10, 100, 0.5F);
        assertEquals("rgba(120, 10, 100, 0.5)", rgbCssValue.getValue());
        assertEquals(120F, rgbCssValue.getR(), 0);
        assertEquals(10F, rgbCssValue.getG(), 0);
        assertEquals(100F, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbaCssValue#getR()}.
     */
    @Test
    public void testGetR() {
        RgbaCssValue rgbCssValue = new RgbaCssValue(120, 10, 100, 0.5F);
        assertEquals("rgba(120, 10, 100, 0.5)", rgbCssValue.getValue());
        assertEquals(120F, rgbCssValue.getR(), 0);
        rgbCssValue.setR(255);
        assertEquals(255, rgbCssValue.getR(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbaCssValue#setR(int)}.
     */
    @Test
    public void testSetR() {
        testGetR();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbaCssValue#getG()}.
     */
    @Test
    public void testGetG() {
        RgbaCssValue rgbCssValue = new RgbaCssValue(120, 10, 100, 0.5F);
        assertEquals("rgba(120, 10, 100, 0.5)", rgbCssValue.getValue());
        assertEquals(10, rgbCssValue.getG(), 0);
        rgbCssValue.setG(55);
        assertEquals(55, rgbCssValue.getG(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbaCssValue#setG(int)}.
     */
    @Test
    public void testSetG() {
        testGetG();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbaCssValue#getB()}.
     */
    @Test
    public void testGetB() {
        RgbaCssValue rgbCssValue = new RgbaCssValue(120, 10, 100, 0.5F);
        assertEquals("rgba(120, 10, 100, 0.5)", rgbCssValue.getValue());
        assertEquals(100, rgbCssValue.getB(), 0);
        rgbCssValue.setB(55);
        assertEquals(55, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbaCssValue#setB(int)}.
     */
    @Test
    public void testSetB() {
        testGetB();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbaCssValue#toString()}.
     */
    @Test
    public void testToString() {
        RgbaCssValue rgbCssValue1 = new RgbaCssValue("rgba(120, 10, 100, 0.5)");
        RgbaCssValue rgbCssValue = new RgbaCssValue(rgbCssValue1);
        assertEquals("rgba(120, 10, 100, 0.5)", rgbCssValue.toString());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbaCssValue#getValue()}.
     */
    @Test
    public void testGetValue() {
        RgbaCssValue rgbCssValue = new RgbaCssValue("rgba(120, 10, 100, 0.5)");
        assertEquals("rgba(120, 10, 100, 0.5)", rgbCssValue.getValue());
        rgbCssValue.setR(220);
        rgbCssValue.setG(55);
        rgbCssValue.setB(75);
        rgbCssValue.setA(0.25F);
        assertEquals("rgba(220, 55, 75, 0.25)", rgbCssValue.getValue());

    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetRInvalidValue() {
        RgbaCssValue rgbCssValue = new RgbaCssValue();
        try {
            rgbCssValue.setR(256);
        } catch (InvalidValueException e) {
            rgbCssValue.setR(-1);
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetGInvalidValue() {
        RgbaCssValue rgbCssValue = new RgbaCssValue();
        try {
            rgbCssValue.setG(256);
        } catch (InvalidValueException e) {
            rgbCssValue.setG(-1);
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetBInvalidValue() {
        RgbaCssValue rgbCssValue = new RgbaCssValue();
        try {
            rgbCssValue.setB(256);
        } catch (InvalidValueException e) {
            rgbCssValue.setB(-1);
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbaCssValue#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(RgbaCssValue.isValid("rgba(0, 0, 0, 1)"));
        assertTrue(RgbaCssValue.isValid("rgba(0, 0, 0, 1)"));
        assertTrue(RgbaCssValue.isValid("rgba(0, 0, 0, 1)"));
        assertTrue(RgbaCssValue.isValid("rgba(0, 0, 0, 1)"));
        assertTrue(RgbaCssValue.isValid("rgba(0, 0, 100, 1)"));
        assertTrue(RgbaCssValue.isValid("rgba(0, 10, 100, 1)"));
        assertTrue(RgbaCssValue.isValid("rgba(120, 100, 10, 1)"));

        assertFalse(RgbaCssValue.isValid("rgba(120, 100, 10)"));
        assertFalse(RgbaCssValue.isValid("rgba(256, 100, 100)"));
        assertFalse(RgbaCssValue.isValid("rgba(100, 256, 100, 1)"));
        assertFalse(RgbaCssValue.isValid("rgba(100, 100, 100, 1.5)"));
        assertFalse(RgbaCssValue.isValid("rgba(100, 100, 100, -1)"));
        assertFalse(RgbaCssValue.isValid("rgba(100, 200, 256, 1)"));
        assertFalse(RgbaCssValue.isValid("rgba(-1, 100, 100, 1)"));
        assertFalse(RgbaCssValue.isValid("rgba(361, 100, 100, 1)"));
        assertFalse(RgbaCssValue.isValid("rgba(36df1, 100, 100, 1)"));
        assertFalse(RgbaCssValue.isValid("rgba(120, 10df0, 10, 1)"));
        assertFalse(RgbaCssValue.isValid("rgba(120, 100, 1dfd0, 1)"));
    }

}
