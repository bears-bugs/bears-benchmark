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
public class BorderRightColorTest {

    @Test
    public void testBorderRightColor() {
        BorderRightColor borderRightColor = new BorderRightColor();
        assertEquals(BorderRightColor.INITIAL, borderRightColor.getCssValue());
    }

    @Test
    public void testBorderRightColorString() {
        BorderRightColor borderRightColor = new BorderRightColor("#0000ff");
        assertEquals("#0000ff", borderRightColor.getCssValue());   
    }

    @Test
    public void testBorderRightColorBorderRightColor() {
        BorderRightColor borderRightColor = new BorderRightColor("#0000ff");
        BorderRightColor borderRightColor1 = new BorderRightColor(borderRightColor);
        assertEquals("#0000ff", borderRightColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        BorderRightColor borderRightColor = new BorderRightColor();
        borderRightColor.setValue("#0000ff");
        assertEquals("#0000ff", borderRightColor.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        BorderRightColor borderRightColor = new BorderRightColor();
        assertEquals(CssNameConstants.BORDER_RIGHT_COLOR, borderRightColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderRightColor borderRightColor = new BorderRightColor("#0000ff");
        assertEquals("#0000ff", borderRightColor.getCssValue());   
    }

    @Test
    public void testToString() {
        BorderRightColor borderRightColor = new BorderRightColor("#0000ff");
        assertEquals(CssNameConstants.BORDER_RIGHT_COLOR + ": #0000ff",
                borderRightColor.toString());
    }

    @Test
    public void testGetValue() {
        BorderRightColor borderRightColor = new BorderRightColor("#0000ff");
        assertEquals("#0000ff", borderRightColor.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        BorderRightColor borderRightColor = new BorderRightColor();
        borderRightColor.setCssValue("#0000ff");
        assertEquals("#0000ff", borderRightColor.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        BorderRightColor borderRightColor = new BorderRightColor();
        borderRightColor.setAsInitial();
        assertEquals(BorderRightColor.INITIAL, borderRightColor.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        BorderRightColor borderRightColor = new BorderRightColor();
        borderRightColor.setAsInherit();
        assertEquals(BorderRightColor.INHERIT, borderRightColor.getCssValue());   
    }
    
    @Test
    public void testSetAsTransparent() {
        BorderRightColor borderRightColor = new BorderRightColor();
        borderRightColor.setAsTransparent();
        assertEquals(BorderRightColor.TRANSPARENT, borderRightColor.getCssValue());   
    }
    
    @Test
    public void testSetRgbCssValue() {
        try {
            BorderRightColor borderRightColor = new BorderRightColor();
            RgbCssValue rgbCssValue = new RgbCssValue("rgb(15, 25, 255)");
            borderRightColor.setRgbCssValue(rgbCssValue);
            borderRightColor.setRgbCssValue(rgbCssValue);
            assertEquals(CssNameConstants.BORDER_RIGHT_COLOR + ": rgb(15, 25, 255)", borderRightColor.toString());
            assertEquals("rgb(15, 25, 255)", borderRightColor.getCssValue());
            
            BorderRightColor borderRightColor2 = new BorderRightColor();
            borderRightColor2.setRgbCssValue(rgbCssValue);
            RgbCssValue rgbCssValueClone = borderRightColor2.getRgbCssValue();
           assertNotEquals(borderRightColor.getRgbCssValue(), rgbCssValueClone);
            
            RgbCssValue rgbCssValue2 = new RgbCssValue("rgb(55, 5, 255)");
            borderRightColor2.setRgbCssValue(rgbCssValue2);
           assertNotEquals(rgbCssValueClone, borderRightColor2.getRgbCssValue());
           assertEquals(rgbCssValue2, borderRightColor2.getRgbCssValue());
            
            borderRightColor2.setAsTransparent();
            assertEquals(BorderRightColor.TRANSPARENT, borderRightColor2.getCssValue());
           assertNull(borderRightColor2.getRgbCssValue());
           assertFalse(rgbCssValue2.isAlreadyInUse());
            
        } catch (Exception e) {
            e.printStackTrace();
           fail();
        }
        
        {
            BorderRightColor color = new BorderRightColor();
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
