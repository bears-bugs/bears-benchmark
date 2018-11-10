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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class BorderLeftColorTest {

    @Test
    public void testBorderLeftColor() {
        BorderLeftColor borderLeftColor = new BorderLeftColor();
        assertEquals(BorderLeftColor.INITIAL, borderLeftColor.getCssValue());
    }

    @Test
    public void testBorderLeftColorString() {
        BorderLeftColor borderLeftColor = new BorderLeftColor("#0000ff");
        assertEquals("#0000ff", borderLeftColor.getCssValue());   
    }

    @Test
    public void testBorderLeftColorBorderLeftColor() {
        BorderLeftColor borderLeftColor = new BorderLeftColor("#0000ff");
        BorderLeftColor borderLeftColor1 = new BorderLeftColor(borderLeftColor);
        assertEquals("#0000ff", borderLeftColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        BorderLeftColor borderLeftColor = new BorderLeftColor();
        borderLeftColor.setValue("#0000ff");
        assertEquals("#0000ff", borderLeftColor.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        BorderLeftColor borderLeftColor = new BorderLeftColor();
        assertEquals(CssNameConstants.BORDER_LEFT_COLOR, borderLeftColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderLeftColor borderLeftColor = new BorderLeftColor("#0000ff");
        assertEquals("#0000ff", borderLeftColor.getCssValue());   
    }

    @Test
    public void testToString() {
        BorderLeftColor borderLeftColor = new BorderLeftColor("#0000ff");
        assertEquals(CssNameConstants.BORDER_LEFT_COLOR + ": #0000ff",
                borderLeftColor.toString());
    }

    @Test
    public void testGetValue() {
        BorderLeftColor borderLeftColor = new BorderLeftColor("#0000ff");
        assertEquals("#0000ff", borderLeftColor.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        BorderLeftColor borderLeftColor = new BorderLeftColor();
        borderLeftColor.setCssValue("#0000ff");
        assertEquals("#0000ff", borderLeftColor.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        BorderLeftColor borderLeftColor = new BorderLeftColor();
        borderLeftColor.setAsInitial();
        assertEquals(BorderLeftColor.INITIAL, borderLeftColor.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        BorderLeftColor borderLeftColor = new BorderLeftColor();
        borderLeftColor.setAsInherit();
        assertEquals(BorderLeftColor.INHERIT, borderLeftColor.getCssValue());   
    }
    
    @Test
    public void testSetAsTransparent() {
        BorderLeftColor borderLeftColor = new BorderLeftColor();
        borderLeftColor.setAsTransparent();
        assertEquals(BorderLeftColor.TRANSPARENT, borderLeftColor.getCssValue());   
    }
    
    @Test
    public void testSetRgbCssValue() {
        try {
            BorderLeftColor borderLeftColor = new BorderLeftColor();
            RgbCssValue rgbCssValue = new RgbCssValue("rgb(15, 25, 255)");
            borderLeftColor.setRgbCssValue(rgbCssValue);
            borderLeftColor.setRgbCssValue(rgbCssValue);
            assertEquals(CssNameConstants.BORDER_LEFT_COLOR + ": rgb(15, 25, 255)", borderLeftColor.toString());
            assertEquals("rgb(15, 25, 255)", borderLeftColor.getCssValue());
            
            BorderLeftColor borderLeftColor2 = new BorderLeftColor();
            borderLeftColor2.setRgbCssValue(rgbCssValue);
            RgbCssValue rgbCssValueClone = borderLeftColor2.getRgbCssValue();
           assertNotEquals(borderLeftColor.getRgbCssValue(), rgbCssValueClone);
            
            RgbCssValue rgbCssValue2 = new RgbCssValue("rgb(55, 5, 255)");
            borderLeftColor2.setRgbCssValue(rgbCssValue2);
           assertNotEquals(rgbCssValueClone, borderLeftColor2.getRgbCssValue());
           assertEquals(rgbCssValue2, borderLeftColor2.getRgbCssValue());
            
            borderLeftColor2.setAsTransparent();
            assertEquals(BorderLeftColor.TRANSPARENT, borderLeftColor2.getCssValue());
           assertNull(borderLeftColor2.getRgbCssValue());
           assertFalse(rgbCssValue2.isAlreadyInUse());
            
        } catch (Exception e) {
            e.printStackTrace();
           fail();
        }
        
        {
            BorderLeftColor color = new BorderLeftColor();
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
