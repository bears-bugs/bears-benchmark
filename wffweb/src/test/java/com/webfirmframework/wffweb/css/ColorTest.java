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
public class ColorTest {

    @Test
    public void testColumnRuleColor() {
        Color color = new Color();
        assertEquals(Color.INITIAL, color.getCssValue());
    }

    @Test
    public void testColorString() {
        Color color = new Color("#0000ff");
        assertEquals("#0000ff", color.getCssValue());   
    }

    @Test
    public void testColorColor() {
        Color color = new Color("#0000ff");
        Color color1 = new Color(color);
        assertEquals("#0000ff", color1.getCssValue());
    }

    @Test
    public void testSetValue() {
        Color color = new Color();
        color.setValue("#0000ff");
        assertEquals("#0000ff", color.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        Color color = new Color();
        assertEquals(CssNameConstants.COLOR, color.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Color color = new Color("#0000ff");
        assertEquals("#0000ff", color.getCssValue());   
    }

    @Test
    public void testToString() {
        Color color = new Color("#0000ff");
        assertEquals(CssNameConstants.COLOR + ": #0000ff",
                color.toString());
    }

    @Test
    public void testGetValue() {
        Color color = new Color("#0000ff");
        assertEquals("#0000ff", color.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        Color color = new Color();
        color.setCssValue("#0000ff");
        assertEquals("#0000ff", color.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        Color color = new Color();
        color.setAsInitial();
        assertEquals(Color.INITIAL, color.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        Color color = new Color();
        color.setAsInherit();
        assertEquals(Color.INHERIT, color.getCssValue());   
    }
    
    @Test
    public void testSetRgbCssValue() {
        try {
            Color color = new Color();
            RgbCssValue rgbCssValue = new RgbCssValue("rgb(15, 25, 255)");
            color.setRgbCssValue(rgbCssValue);
            color.setRgbCssValue(rgbCssValue);
            assertEquals(CssNameConstants.COLOR + ": rgb(15, 25, 255)", color.toString());
            assertEquals("rgb(15, 25, 255)", color.getCssValue());
            
            Color color2 = new Color();
            color2.setRgbCssValue(rgbCssValue);
            RgbCssValue rgbCssValueClone = color2.getRgbCssValue();
           assertNotEquals(color.getRgbCssValue(), rgbCssValueClone);
            
            RgbCssValue rgbCssValue2 = new RgbCssValue("rgb(55, 5, 255)");
            color2.setRgbCssValue(rgbCssValue2);
           assertNotEquals(rgbCssValueClone, color2.getRgbCssValue());
           assertEquals(rgbCssValue2, color2.getRgbCssValue());
            
            
        } catch (Exception e) {
            e.printStackTrace();
           fail();
        }
        
        {
            Color color = new Color();
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
            color.setAsInherit();
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
            
            color.setAsInitial();
           assertNull(color.getRgbaCssValue());
           assertNull(color.getRgbCssValue());
           assertNull(color.getHslCssValue());
           assertNull(color.getHslaCssValue());
        }
    }

}
