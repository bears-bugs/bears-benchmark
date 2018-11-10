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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class BorderColorCssValuesTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#BorderColorCssValues()}.
     */
    @Test
    public void testBorderColorCssValues() {
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues();
        assertEquals("black", borderColorCssValues.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#BorderColorCssValues(java.lang.String)}.
     */
    @Test
    public void testBorderColorCssValuesString() {
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues(BorderColorCssValues.TRANSPARENT);
        assertEquals(BorderColorCssValues.TRANSPARENT, borderColorCssValues.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#BorderColorCssValues(com.webfirmframework.wffweb.css.BorderColorCssValues)}.
     */
    @Test
    public void testBorderColorCssValuesBorderColorCssValues() {
        BorderColorCssValues borderColorCssValues1 = new BorderColorCssValues(BorderColorCssValues.TRANSPARENT);
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues(borderColorCssValues1);
        
        assertEquals(BorderColorCssValues.TRANSPARENT, borderColorCssValues.getValue());
        
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#BorderColorCssValues(com.webfirmframework.wffweb.css.RgbCssValue)}.
     */
    @Test
    public void testBorderColorCssValuesRgbCssValue() {
        RgbCssValue rgbCssValue = new RgbCssValue();
        rgbCssValue.setR(55);
        rgbCssValue.setG(155);
        rgbCssValue.setB(75);
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues(rgbCssValue);
        assertEquals("rgb(55, 155, 75)", borderColorCssValues.getValue());
        
        rgbCssValue.setR(0);
        rgbCssValue.setG(25);
        rgbCssValue.setB(255);
        
        assertEquals("rgb(0, 25, 255)", borderColorCssValues.getValue());
        
        assertEquals(rgbCssValue, borderColorCssValues.getRgbCssValue());
        assertTrue(rgbCssValue.isAlreadyInUse());
        borderColorCssValues.setValue(BorderColorCssValues.TRANSPARENT);
        assertNull(borderColorCssValues.getRgbCssValue());
        
        assertFalse(rgbCssValue.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#BorderColorCssValues(com.webfirmframework.wffweb.css.RgbaCssValue)}.
     */
    @Test
    public void testBorderColorCssValuesRgbaCssValue() {
        RgbaCssValue rgbaCssValue = new RgbaCssValue();
        rgbaCssValue.setR(55);
        rgbaCssValue.setG(155);
        rgbaCssValue.setB(75);
        rgbaCssValue.setA(0.5F);
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues(rgbaCssValue);
        assertEquals("rgba(55, 155, 75, 0.5)", borderColorCssValues.getValue());
        
        rgbaCssValue.setR(0);
        rgbaCssValue.setG(25);
        rgbaCssValue.setB(255);
        rgbaCssValue.setA(1F);
        
        assertEquals("rgba(0, 25, 255, 1.0)", borderColorCssValues.getValue());
        
        assertEquals(rgbaCssValue, borderColorCssValues.getRgbaCssValue());
        assertTrue(rgbaCssValue.isAlreadyInUse());
        borderColorCssValues.setValue(BorderColorCssValues.TRANSPARENT);
        assertNull(borderColorCssValues.getRgbaCssValue());
        assertFalse(rgbaCssValue.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#setRgbaCssValue(com.webfirmframework.wffweb.css.RgbaCssValue)}.
     */
    @Test
    public void testSetRgbaCssValue() {
        RgbaCssValue rgbaCssValue = new RgbaCssValue();
        rgbaCssValue.setR(55);
        rgbaCssValue.setG(155);
        rgbaCssValue.setB(75);
        rgbaCssValue.setA(0.5F);
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues();
        borderColorCssValues.setRgbaCssValue(rgbaCssValue);
        assertEquals("rgba(55, 155, 75, 0.5)", borderColorCssValues.getValue());
        
        rgbaCssValue.setR(0);
        rgbaCssValue.setG(25);
        rgbaCssValue.setB(255);
        rgbaCssValue.setA(1F);
        
        assertEquals("rgba(0, 25, 255, 1.0)", borderColorCssValues.getValue());
        
        assertEquals(rgbaCssValue, borderColorCssValues.getRgbaCssValue());
        assertTrue(rgbaCssValue.isAlreadyInUse());
        borderColorCssValues.setValue(BorderColorCssValues.TRANSPARENT);
        assertNull(borderColorCssValues.getRgbaCssValue());
        assertFalse(rgbaCssValue.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#toString()}.
     */
    @Test
    public void testToString() {
        RgbaCssValue rgbaCssValue = new RgbaCssValue();
        rgbaCssValue.setR(55);
        rgbaCssValue.setG(155);
        rgbaCssValue.setB(75);
        rgbaCssValue.setA(0.5F);
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues();
        assertEquals("black", borderColorCssValues.toString());
        borderColorCssValues.setRgbaCssValue(rgbaCssValue);
        assertEquals("rgba(55, 155, 75, 0.5)", borderColorCssValues.toString());
        
        rgbaCssValue.setR(0);
        rgbaCssValue.setG(25);
        rgbaCssValue.setB(255);
        rgbaCssValue.setA(1F);
        
        assertEquals("rgba(0, 25, 255, 1.0)", borderColorCssValues.toString());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#getValue()}.
     */
    @Test
    public void testGetValue() {
        RgbaCssValue rgbaCssValue = new RgbaCssValue();
        rgbaCssValue.setR(55);
        rgbaCssValue.setG(155);
        rgbaCssValue.setB(75);
        rgbaCssValue.setA(0.5F);
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues();
        assertEquals("black", borderColorCssValues.getValue());
        borderColorCssValues.setRgbaCssValue(rgbaCssValue);
        assertEquals("rgba(55, 155, 75, 0.5)", borderColorCssValues.getValue());
        
        rgbaCssValue.setR(0);
        rgbaCssValue.setG(25);
        rgbaCssValue.setB(255);
        rgbaCssValue.setA(1F);
        
        assertEquals("rgba(0, 25, 255, 1.0)", borderColorCssValues.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#setValue(java.lang.String)}.
     */
    @Test
    public void testSetValue() {
        RgbaCssValue rgbaCssValue = new RgbaCssValue();
        rgbaCssValue.setR(55);
        rgbaCssValue.setG(155);
        rgbaCssValue.setB(75);
        rgbaCssValue.setA(0.5F);
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues();
        assertEquals("black", borderColorCssValues.getValue());
        borderColorCssValues.setRgbaCssValue(rgbaCssValue);
        assertEquals("rgba(55, 155, 75, 0.5)", borderColorCssValues.getValue());
        
        rgbaCssValue.setR(0);
        rgbaCssValue.setG(25);
        rgbaCssValue.setB(255);
        rgbaCssValue.setA(1F);
        
        assertEquals("rgba(0, 25, 255, 1.0)", borderColorCssValues.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#setAsTransparent()}.
     */
    @Test
    public void testSetAsTransparent() {
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues();
        assertEquals("black", borderColorCssValues.getValue());
        borderColorCssValues.setAsTransparent();
        assertEquals(BorderColorCssValues.TRANSPARENT, borderColorCssValues.getValue());
        
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(BorderColorCssValues.isValid("rgba(0, 25, 255, 1.0)"));
        assertTrue(BorderColorCssValues.isValid("rgb(55, 155, 75)"));
        assertTrue(BorderColorCssValues.isValid(BorderColorCssValues.TRANSPARENT));
        assertTrue(BorderColorCssValues.isValid(CssColorName.AQUA.getColorName()));
        
        assertFalse(BorderColorCssValues.isValid("rgba(a0, 25, 255, 1.0)"));
        assertFalse(BorderColorCssValues.isValid("rgba(55, 155, 75)"));
        assertFalse(BorderColorCssValues.isValid("dfd"));
        assertFalse(BorderColorCssValues.isValid("inherit"));
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#stateChanged(com.webfirmframework.wffweb.data.Bean)}.
     *//*
    @Test
    public void testStateChanged() {
       
        fail("Not yet implemented");
    }*/

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#getRgbCssValue()}.
     */
    @Test
    public void testGetRgbCssValue() {
//        testSetRgbCssValue();
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#getRgbCssValue()}.
     */
    @Test
    public void testGetRgbaCssValue() {
        testSetRgbaCssValue();
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderColorCssValues#setRgbCssValue(com.webfirmframework.wffweb.css.RgbCssValue)}.
     */
    @Test
    public void testSetRgbCssValue() {
        {
            RgbCssValue rgbCssValue = new RgbCssValue();
            rgbCssValue.setR(55);
            rgbCssValue.setG(155);
            rgbCssValue.setB(75);
            BorderColorCssValues borderColorCssValues = new BorderColorCssValues();
            borderColorCssValues.setRgbCssValue(rgbCssValue);
            assertEquals("rgb(55, 155, 75)", borderColorCssValues.getValue());
            
            rgbCssValue.setR(0);
            rgbCssValue.setG(25);
            rgbCssValue.setB(255);
            
            assertEquals("rgb(0, 25, 255)", borderColorCssValues.getValue());
            
            assertEquals(rgbCssValue, borderColorCssValues.getRgbCssValue());
            assertTrue(rgbCssValue.isAlreadyInUse());
            borderColorCssValues.setValue(BorderColorCssValues.TRANSPARENT);
            assertNull(borderColorCssValues.getRgbCssValue());
            assertFalse(rgbCssValue.isAlreadyInUse());
        }
        
        {
            BorderColorCssValues color = new BorderColorCssValues();
            RgbCssValue rgbCssValue = new RgbCssValue("rgb(15, 25, 255)");
           assertFalse(rgbCssValue.isAlreadyInUse());
            color.setRgbCssValue(rgbCssValue);
           assertTrue(rgbCssValue.isAlreadyInUse());
            
            RgbaCssValue rgbaCssValue = new RgbaCssValue("rgba(15, 25, 100, 1)");
           assertFalse(rgbaCssValue.isAlreadyInUse());
            color.setRgbaCssValue(rgbaCssValue);
           assertTrue(rgbaCssValue.isAlreadyInUse());
            
           assertFalse(rgbCssValue.isAlreadyInUse());
           assertNull(color.getRgbCssValue());
            color.setAsTransparent();
           assertNull(color.getRgbaCssValue());
           assertNull(color.getRgbCssValue());
           assertNull(color.getHslCssValue());
           assertNull(color.getHslaCssValue());
            
            HslCssValue hslCssValue = new HslCssValue("hsl(15, 25%, 100%)");
           assertFalse(hslCssValue.isAlreadyInUse());
            color.setHslCssValue(hslCssValue);
           assertTrue(hslCssValue.isAlreadyInUse());
            
           assertNull(color.getRgbaCssValue());
           assertNull(color.getRgbCssValue());
           assertNotNull(color.getHslCssValue());
           assertNull(color.getHslaCssValue());
            
            HslaCssValue hslaCssValue = new HslaCssValue("hsla(15, 25%, 100%, 1)");
           assertFalse(hslaCssValue.isAlreadyInUse());
            color.setHslaCssValue(hslaCssValue);
           assertTrue(hslaCssValue.isAlreadyInUse());
            
           assertNull(color.getRgbaCssValue());
           assertNull(color.getRgbCssValue());
           assertNull(color.getHslCssValue());
           assertNotNull(color.getHslaCssValue());
            
            color.setAsTransparent();
           assertNull(color.getRgbaCssValue());
           assertNull(color.getRgbCssValue());
           assertNull(color.getHslCssValue());
           assertNull(color.getHslaCssValue());
        }
 
    }

}
