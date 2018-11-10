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
public class OutlineColorTest {

    @Test
    public void testColumnRuleColor() {
        OutlineColor outlineColor = new OutlineColor();
        assertEquals(OutlineColor.INVERT, outlineColor.getCssValue());
    }

    @Test
    public void testOutlineColorString() {
        OutlineColor outlineColor = new OutlineColor("#0000ff");
        assertEquals("#0000ff", outlineColor.getCssValue());   
    }

    @Test
    public void testOutlineColorOutlineColor() {
        OutlineColor outlineColor = new OutlineColor("#0000ff");
        OutlineColor outlineColor1 = new OutlineColor(outlineColor);
        assertEquals("#0000ff", outlineColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        OutlineColor outlineColor = new OutlineColor();
        outlineColor.setValue("#0000ff");
        assertEquals("#0000ff", outlineColor.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        OutlineColor outlineColor = new OutlineColor();
        assertEquals(CssNameConstants.OUTLINE_COLOR, outlineColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        OutlineColor outlineColor = new OutlineColor("#0000ff");
        assertEquals("#0000ff", outlineColor.getCssValue());   
    }

    @Test
    public void testToString() {
        OutlineColor outlineColor = new OutlineColor("#0000ff");
        assertEquals(CssNameConstants.OUTLINE_COLOR + ": #0000ff",
                outlineColor.toString());
    }

    @Test
    public void testGetValue() {
        OutlineColor outlineColor = new OutlineColor("#0000ff");
        assertEquals("#0000ff", outlineColor.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        OutlineColor outlineColor = new OutlineColor();
        outlineColor.setCssValue("#0000ff");
        assertEquals("#0000ff", outlineColor.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        OutlineColor outlineColor = new OutlineColor();
        outlineColor.setAsInitial();
        assertEquals(OutlineColor.INITIAL, outlineColor.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        OutlineColor outlineColor = new OutlineColor();
        outlineColor.setAsInherit();
        assertEquals(OutlineColor.INHERIT, outlineColor.getCssValue());   
    }
    
    @Test
    public void testSetAsInvert() {
        OutlineColor outlineColor = new OutlineColor();
        outlineColor.setAsInvert();
        assertEquals(OutlineColor.INVERT, outlineColor.getCssValue());   
    }
    
    @Test
    public void testSetRgbCssValue() {
        try {
            OutlineColor outlineColor = new OutlineColor();
            RgbCssValue rgbCssValue = new RgbCssValue("rgb(15, 25, 255)");
            outlineColor.setRgbCssValue(rgbCssValue);
            outlineColor.setRgbCssValue(rgbCssValue);
            assertEquals(CssNameConstants.OUTLINE_COLOR + ": rgb(15, 25, 255)", outlineColor.toString());
            assertEquals("rgb(15, 25, 255)", outlineColor.getCssValue());
            
            OutlineColor outlineColor2 = new OutlineColor();
            outlineColor2.setRgbCssValue(rgbCssValue);
            RgbCssValue rgbCssValueClone = outlineColor2.getRgbCssValue();
           assertNotEquals(outlineColor.getRgbCssValue(), rgbCssValueClone);
            
            RgbCssValue rgbCssValue2 = new RgbCssValue("rgb(55, 5, 255)");
            outlineColor2.setRgbCssValue(rgbCssValue2);
           assertNotEquals(rgbCssValueClone, outlineColor2.getRgbCssValue());
           assertEquals(rgbCssValue2, outlineColor2.getRgbCssValue());
            
            outlineColor2.setAsInvert();
            assertEquals(OutlineColor.INVERT, outlineColor2.getCssValue());
           assertNull(outlineColor2.getRgbCssValue());
           assertFalse(rgbCssValue2.isAlreadyInUse());
            
        } catch (Exception e) {
            e.printStackTrace();
           fail();
        }
        {
            OutlineColor color = new OutlineColor();
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
