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
public class BorderTopColorTest {

    @Test
    public void testBorderTopColor() {
        BorderTopColor borderTopColor = new BorderTopColor();
        assertEquals(BorderTopColor.INITIAL, borderTopColor.getCssValue());
    }

    @Test
    public void testBorderTopColorString() {
        BorderTopColor borderTopColor = new BorderTopColor("#0000ff");
        assertEquals("#0000ff", borderTopColor.getCssValue());
    }

    @Test
    public void testBorderTopColorBorderTopColor() {
        BorderTopColor borderTopColor = new BorderTopColor("#0000ff");
        BorderTopColor borderTopColor1 = new BorderTopColor(borderTopColor);
        assertEquals("#0000ff", borderTopColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        BorderTopColor borderTopColor = new BorderTopColor();
        borderTopColor.setValue("#0000ff");
        assertEquals("#0000ff", borderTopColor.getCssValue());
    }

    @Test
    public void testGetCssName() {
        BorderTopColor borderTopColor = new BorderTopColor();
        assertEquals(CssNameConstants.BORDER_TOP_COLOR,
                borderTopColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderTopColor borderTopColor = new BorderTopColor("#0000ff");
        assertEquals("#0000ff", borderTopColor.getCssValue());
    }

    @Test
    public void testToString() {
        BorderTopColor borderTopColor = new BorderTopColor("#0000ff");
        assertEquals(CssNameConstants.BORDER_TOP_COLOR + ": #0000ff",
                borderTopColor.toString());
    }

    @Test
    public void testGetValue() {
        BorderTopColor borderTopColor = new BorderTopColor("#0000ff");
        assertEquals("#0000ff", borderTopColor.getValue());
    }

    @Test
    public void testSetCssValueString() {
        BorderTopColor borderTopColor = new BorderTopColor();
        borderTopColor.setCssValue("#0000ff");
        assertEquals("#0000ff", borderTopColor.getCssValue());
    }

    @Test
    public void testSetAsInitial() {
        BorderTopColor borderTopColor = new BorderTopColor();
        borderTopColor.setAsInitial();
        assertEquals(BorderTopColor.INITIAL, borderTopColor.getCssValue());
    }

    @Test
    public void testSetAsInherit() {
        BorderTopColor borderTopColor = new BorderTopColor();
        borderTopColor.setAsInherit();
        assertEquals(BorderTopColor.INHERIT, borderTopColor.getCssValue());
    }

    @Test
    public void testSetAsTransparent() {
        BorderTopColor borderTopColor = new BorderTopColor();
        borderTopColor.setAsTransparent();
        assertEquals(BorderTopColor.TRANSPARENT, borderTopColor.getCssValue());
    }

    @Test
    public void testSetRgbCssValue() {
        try {
            BorderTopColor borderTopColor = new BorderTopColor();
            RgbCssValue rgbCssValue = new RgbCssValue("rgb(15, 25, 255)");
            borderTopColor.setRgbCssValue(rgbCssValue);
            borderTopColor.setRgbCssValue(rgbCssValue);
            assertEquals(
                    CssNameConstants.BORDER_TOP_COLOR + ": rgb(15, 25, 255)",
                    borderTopColor.toString());
            assertEquals("rgb(15, 25, 255)", borderTopColor.getCssValue());

            BorderTopColor borderTopColor2 = new BorderTopColor();
            borderTopColor2.setRgbCssValue(rgbCssValue);
            RgbCssValue rgbCssValueClone = borderTopColor2.getRgbCssValue();
            assertNotEquals(borderTopColor.getRgbCssValue(), rgbCssValueClone);

            RgbCssValue rgbCssValue2 = new RgbCssValue("rgb(55, 5, 255)");
            borderTopColor2.setRgbCssValue(rgbCssValue2);
            assertNotEquals(rgbCssValueClone, borderTopColor2.getRgbCssValue());
            assertEquals(rgbCssValue2, borderTopColor2.getRgbCssValue());

            borderTopColor2.setAsTransparent();
            assertEquals(BorderTopColor.TRANSPARENT,
                    borderTopColor2.getCssValue());
            assertNull(borderTopColor2.getRgbCssValue());
            assertFalse(rgbCssValue2.isAlreadyInUse());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        {
            BorderTopColor color = new BorderTopColor();
            RgbCssValue rgbCssValue = new RgbCssValue("rgb(15, 25, 255)");
            assertFalse(rgbCssValue.isAlreadyInUse());
            color.setRgbCssValue(rgbCssValue);
            assertTrue(rgbCssValue.isAlreadyInUse());

            RgbaCssValue rgbaCssValue = new RgbaCssValue(
                    "rgba(15, 25, 100, 1)");
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

            HslaCssValue hslaCssValue = new HslaCssValue(
                    "hsla(15, 25%, 100%, 1)");
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
