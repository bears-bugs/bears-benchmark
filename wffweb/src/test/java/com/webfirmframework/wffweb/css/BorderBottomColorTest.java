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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class BorderBottomColorTest {

    @Test
    public void testBorderBottomColor() {
        BorderBottomColor borderBottomColor = new BorderBottomColor();
        assertEquals(BorderBottomColor.INITIAL, borderBottomColor.getCssValue());
    }

    @Test
    public void testBorderBottomColorString() {
        BorderBottomColor borderBottomColor = new BorderBottomColor("#0000ff");
        assertEquals("#0000ff", borderBottomColor.getCssValue());   
    }

    @Test
    public void testBorderBottomColorBorderBottomColor() {
        BorderBottomColor borderBottomColor = new BorderBottomColor("#0000ff");
        BorderBottomColor borderBottomColor1 = new BorderBottomColor(borderBottomColor);
        assertEquals("#0000ff", borderBottomColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        BorderBottomColor borderBottomColor = new BorderBottomColor();
        borderBottomColor.setValue("#0000ff");
        assertEquals("#0000ff", borderBottomColor.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        BorderBottomColor borderBottomColor = new BorderBottomColor();
        assertEquals(CssNameConstants.BORDER_BOTTOM_COLOR, borderBottomColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderBottomColor borderBottomColor = new BorderBottomColor("#0000ff");
        assertEquals("#0000ff", borderBottomColor.getCssValue());   
    }

    @Test
    public void testToString() {
        BorderBottomColor borderBottomColor = new BorderBottomColor("#0000ff");
        assertEquals(CssNameConstants.BORDER_BOTTOM_COLOR + ": #0000ff",
                borderBottomColor.toString());
    }

    @Test
    public void testGetValue() {
        BorderBottomColor borderBottomColor = new BorderBottomColor("#0000ff");
        assertEquals("#0000ff", borderBottomColor.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        BorderBottomColor borderBottomColor = new BorderBottomColor();
        borderBottomColor.setCssValue("#0000ff");
        assertEquals("#0000ff", borderBottomColor.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        BorderBottomColor borderBottomColor = new BorderBottomColor();
        borderBottomColor.setAsInitial();
        assertEquals(BorderBottomColor.INITIAL, borderBottomColor.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        BorderBottomColor borderBottomColor = new BorderBottomColor();
        borderBottomColor.setAsInherit();
        assertEquals(BorderBottomColor.INHERIT, borderBottomColor.getCssValue());   
    }
    
    @Test
    public void testSetAsTransparent() {
        BorderBottomColor borderBottomColor = new BorderBottomColor();
        borderBottomColor.setAsTransparent();
        assertEquals(BorderBottomColor.TRANSPARENT, borderBottomColor.getCssValue());   
    }
    
    @Test
    public void testSetRgbCssValue() {
        try {
            BorderBottomColor borderBottomColor = new BorderBottomColor();
            RgbCssValue rgbCssValue = new RgbCssValue("rgb(15, 25, 255)");
            borderBottomColor.setRgbCssValue(rgbCssValue);
            borderBottomColor.setRgbCssValue(rgbCssValue);
            assertEquals(CssNameConstants.BORDER_BOTTOM_COLOR + ": rgb(15, 25, 255)", borderBottomColor.toString());
            assertEquals("rgb(15, 25, 255)", borderBottomColor.getCssValue());
            
            BorderBottomColor borderBottomColor2 = new BorderBottomColor();
            borderBottomColor2.setRgbCssValue(rgbCssValue);
            RgbCssValue rgbCssValueClone = borderBottomColor2.getRgbCssValue();
           assertNotEquals(borderBottomColor.getRgbCssValue(), rgbCssValueClone);
            
            RgbCssValue rgbCssValue2 = new RgbCssValue("rgb(55, 5, 255)");
            borderBottomColor2.setRgbCssValue(rgbCssValue2);
           assertNotEquals(rgbCssValueClone, borderBottomColor2.getRgbCssValue());
           assertEquals(rgbCssValue2, borderBottomColor2.getRgbCssValue());
            
            borderBottomColor2.setAsTransparent();
            assertEquals(BorderBottomColor.TRANSPARENT, borderBottomColor2.getCssValue());
           assertNull(borderBottomColor2.getRgbCssValue());
           assertFalse(rgbCssValue2.isAlreadyInUse());
            
        } catch (Exception e) {
            e.printStackTrace();
           fail();
        }
    }
}
