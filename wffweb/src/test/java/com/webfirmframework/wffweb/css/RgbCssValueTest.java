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
public class RgbCssValueTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbCssValue#RgbCssValue()}.
     */
    @Test
    public void testRgbCssValue() {
        RgbCssValue rgbCssValue = new RgbCssValue();
        assertEquals("rgb(0, 0, 0)", rgbCssValue.getValue());
        assertEquals(0F, rgbCssValue.getR(), 0);
        assertEquals(0F, rgbCssValue.getG(), 0);
        assertEquals(0F, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbCssValue#RgbCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testRgbCssValueString() {
        RgbCssValue rgbCssValue = new RgbCssValue("rgb(120, 10, 100)");
        assertEquals("rgb(120, 10, 100)", rgbCssValue.getValue());
        assertEquals(120F, rgbCssValue.getR(), 0);
        assertEquals(10F, rgbCssValue.getG(), 0);
        assertEquals(100F, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbCssValue#RgbCssValue(com.webfirmframework.wffweb.css.RgbCssValue)}
     * .
     */
    @Test
    public void testRgbCssValueRgbCssValue() {
        RgbCssValue rgbCssValue1 = new RgbCssValue("rgb(120, 10, 100)");
        RgbCssValue rgbCssValue = new RgbCssValue(rgbCssValue1);
        assertEquals("rgb(120, 10, 100)", rgbCssValue.getValue());
        assertEquals(120F, rgbCssValue.getR(), 0);
        assertEquals(10F, rgbCssValue.getG(), 0);
        assertEquals(100F, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbCssValue#setRgb(java.lang.String)}.
     */
    @Test
    public void testSetRsl() {
        RgbCssValue rgbCssValue = new RgbCssValue();
        assertEquals("rgb(0, 0, 0)", rgbCssValue.getValue());
        rgbCssValue.setRgb("rgb(120, 10, 100)");
        assertEquals(120F, rgbCssValue.getR(), 0);
        assertEquals(10F, rgbCssValue.getG(), 0);
        assertEquals(100F, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbCssValue#RgbCssValue(int, int, int)}.
     */
    @Test
    public void testRgbCssValueIntIntInt() {
        RgbCssValue rgbCssValue = new RgbCssValue(120, 10, 100);
        assertEquals("rgb(120, 10, 100)", rgbCssValue.getValue());
        assertEquals(120F, rgbCssValue.getR(), 0);
        assertEquals(10F, rgbCssValue.getG(), 0);
        assertEquals(100F, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbCssValue#getR()}.
     */
    @Test
    public void testGetR() {
        RgbCssValue rgbCssValue = new RgbCssValue(120, 10, 100);
        assertEquals("rgb(120, 10, 100)", rgbCssValue.getValue());
        assertEquals(120F, rgbCssValue.getR(), 0);
        rgbCssValue.setR(255);
        assertEquals(255, rgbCssValue.getR(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbCssValue#setR(int)}.
     */
    @Test
    public void testSetR() {
        testGetR();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbCssValue#getG()}.
     */
    @Test
    public void testGetG() {
        RgbCssValue rgbCssValue = new RgbCssValue(120, 10, 100);
        assertEquals("rgb(120, 10, 100)", rgbCssValue.getValue());
        assertEquals(10, rgbCssValue.getG(), 0);
        rgbCssValue.setG(55);
        assertEquals(55, rgbCssValue.getG(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbCssValue#setG(int)}.
     */
    @Test
    public void testSetG() {
        testGetG();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbCssValue#getB()}.
     */
    @Test
    public void testGetB() {
        RgbCssValue rgbCssValue = new RgbCssValue(120, 10, 100);
        assertEquals("rgb(120, 10, 100)", rgbCssValue.getValue());
        assertEquals(100, rgbCssValue.getB(), 0);
        rgbCssValue.setB(55);
        assertEquals(55, rgbCssValue.getB(), 0);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbCssValue#setB(int)}.
     */
    @Test
    public void testSetB() {
        testGetB();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbCssValue#toString()}.
     */
    @Test
    public void testToString() {
        RgbCssValue rgbCssValue1 = new RgbCssValue("rgb(120, 10, 100)");
        RgbCssValue rgbCssValue = new RgbCssValue(rgbCssValue1);
        assertEquals("rgb(120, 10, 100)", rgbCssValue.toString());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.RgbCssValue#getValue()}.
     */
    @Test
    public void testGetValue() {
        RgbCssValue rgbCssValue = new RgbCssValue("rgb(120, 10, 100)");
        assertEquals("rgb(120, 10, 100)", rgbCssValue.getValue());
        rgbCssValue.setR(220);
        rgbCssValue.setG(55);
        rgbCssValue.setB(75);
        assertEquals("rgb(220, 55, 75)", rgbCssValue.getValue());

    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetRInvalidValue() {
        RgbCssValue rgbCssValue = new RgbCssValue();
        try {
            rgbCssValue.setR(256);
        } catch (InvalidValueException e) {
            rgbCssValue.setR(-1);
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetGInvalidValue() {
        RgbCssValue rgbCssValue = new RgbCssValue();
        try {
            rgbCssValue.setG(256);
        } catch (InvalidValueException e) {
            rgbCssValue.setG(-1);
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetBInvalidValue() {
        RgbCssValue rgbCssValue = new RgbCssValue();
        try {
            rgbCssValue.setB(256);
        } catch (InvalidValueException e) {
            rgbCssValue.setB(-1);
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.RgbCssValue#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(RgbCssValue.isValid("rgb(0, 0, 0)"));
        assertTrue(RgbCssValue.isValid("rgb(0, 0, 0)"));
        assertTrue(RgbCssValue.isValid("rgb(0, 0, 0)"));
        assertTrue(RgbCssValue.isValid("rgb(0, 0, 0)"));
        assertTrue(RgbCssValue.isValid("rgb(0, 0, 100)"));
        assertTrue(RgbCssValue.isValid("rgb(0, 10, 100)"));
        assertTrue(RgbCssValue.isValid("rgb(120, 100, 10)"));

        assertFalse(RgbCssValue.isValid("rgb(256, 100, 100)"));
        assertFalse(RgbCssValue.isValid("rgb(100, 256, 100)"));
        assertFalse(RgbCssValue.isValid("rgb(100, 200, 256)"));
        assertFalse(RgbCssValue.isValid("rgb(-1, 100, 100)"));
        assertFalse(RgbCssValue.isValid("rgb(361, 100, 100)"));
        assertFalse(RgbCssValue.isValid("rgb(36df1, 100, 100)"));
        assertFalse(RgbCssValue.isValid("rgb(120, 10df0, 10)"));
        assertFalse(RgbCssValue.isValid("rgb(120, 100, 1dfd0)"));
    }

}
