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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class BorderRightTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#BorderRight()}.
     */
    @Test
    public void testBorderRight() {
        BorderRight border = new BorderRight();
        assertEquals("medium none black", border.getCssValue());
        final BorderRightWidth borderRightWidth = border.getBorderRightWidth();
        assertTrue(borderRightWidth.isAlreadyInUse());
        assertEquals("medium", borderRightWidth.getCssValue());
        final BorderRightStyle borderRightStyle = border.getBorderRightStyle();
        assertEquals("none", borderRightStyle.getCssValue());
        final BorderRightColor borderRightColor = border.getBorderRightColor();
        assertTrue(borderRightColor.isAlreadyInUse());
        assertEquals("black", borderRightColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderRightWidth.isAlreadyInUse());
        assertFalse(borderRightColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#BorderRight(java.lang.String)}.
     */
    @Test
    public void testBorderRightString() {
        BorderRight border = new BorderRight("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderRightWidth borderRightWidth = border.getBorderRightWidth();
        assertTrue(borderRightWidth.isAlreadyInUse());
        assertEquals("thick", borderRightWidth.getCssValue());
        final BorderRightStyle borderRightStyle = border.getBorderRightStyle();
        assertEquals("solid", borderRightStyle.getCssValue());
        final BorderRightColor borderRightColor = border.getBorderRightColor();
        assertTrue(borderRightColor.isAlreadyInUse());
        assertEquals("green", borderRightColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderRightWidth.isAlreadyInUse());
        assertFalse(borderRightColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#BorderRight(com.webfirmframework.wffweb.css.BorderRight)}.
     */
    @Test
    public void testBorderRightBorderRight() {

        BorderRight border1 = new BorderRight("thick solid green");
        BorderRight border = new BorderRight(border1);
        assertEquals("thick solid green", border.getCssValue());
        final BorderRightWidth borderRightWidth = border.getBorderRightWidth();
        assertTrue(borderRightWidth.isAlreadyInUse());
        assertEquals("thick", borderRightWidth.getCssValue());
        final BorderRightStyle borderRightStyle = border.getBorderRightStyle();
        assertEquals("solid", borderRightStyle.getCssValue());
        final BorderRightColor borderRightColor = border.getBorderRightColor();
        assertTrue(borderRightColor.isAlreadyInUse());
        assertEquals("green", borderRightColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderRightWidth.isAlreadyInUse());
        assertFalse(borderRightColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        BorderRight border = new BorderRight();
        assertEquals(CssNameConstants.BORDER_RIGHT, border.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {

        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderRightWidth borderRightWidth = border.getBorderRightWidth();
        assertTrue(borderRightWidth.isAlreadyInUse());
        assertEquals("thick", borderRightWidth.getCssValue());
        final BorderRightStyle borderRightStyle = border.getBorderRightStyle();
        assertEquals("solid", borderRightStyle.getCssValue());
        final BorderRightColor borderRightColor = border.getBorderRightColor();
        assertTrue(borderRightColor.isAlreadyInUse());
        assertEquals("green", borderRightColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderRightWidth.isAlreadyInUse());
        assertFalse(borderRightColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#toString()}.
     */
    @Test
    public void testToString() {

        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals(CssNameConstants.BORDER_RIGHT + ": thick solid green",
                border.toString());

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        {
            BorderRight border = new BorderRight();
            border.setCssValue("thick solid green");
            assertEquals("thick solid green", border.getCssValue());
            try {
                border.setCssValue("ddfdfdf");
                fail("testSetCssValueString :- setCssValue should not accept invalid values");
            } catch (InvalidValueException e) {
                assertEquals("thick solid green", border.getCssValue());
            }
        
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setCssValue(java.lang.String)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueStringForInvalidValue() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        border.setCssValue("ddfdfdf");
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(BorderRight.isValid("55px solid green"));
        assertTrue(BorderRight.isValid("55px solid rgb(25, 255, 155)"));

        assertTrue(BorderRight.isValid("55px"));
        assertTrue(BorderRight.isValid("solid"));
        assertTrue(BorderRight.isValid("green"));
        assertTrue(BorderRight.isValid(BorderRightWidth.MEDIUM + " "
                + BorderRightStyle.DASHED.getCssValue() + " " + CssColorName.AZURE));

        assertTrue(BorderRight.isValid(BorderRightWidth.THICK + " "
                + BorderRightStyle.DOUBLE.getCssValue() + " " + CssColorName.AZURE));

        assertFalse(BorderRight.isValid("cir cle inside url(Test.png)"));
        assertFalse(BorderRight.isValid("dmedium dfdf dfdfd"));
        assertFalse(BorderRight.isValid("medium medium medium "));
        assertFalse(BorderRight.isValid("55 solid green"));
        assertFalse(BorderRight.isValid("solid solid solid"));
        assertFalse(BorderRight.isValid("55px solid green green"));
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        {
            BorderRight border = new BorderRight();
            border.setCssValue("thick solid green");
            assertEquals("thick solid green", border.getCssValue());
            border.setAsInitial();
            assertEquals("initial", border.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        border.setAsInherit();
        assertEquals("inherit", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#getBorderRightColor()}.
     */
    @Test
    public void testGetBorderRightColor() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderRightColor borderRightColor = border.getBorderRightColor();
        assertTrue(borderRightColor.isAlreadyInUse());
        assertEquals("green", borderRightColor.getValue());
        
        border.setCssValue("thick solid");
        assertNull(border.getBorderRightColor());
        assertFalse(borderRightColor.isAlreadyInUse());
        assertEquals("thick solid", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#getBorderRightStyle()}.
     */
    @Test
    public void testGetBorderRightStyle() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderRightStyle borderRightWidth = border.getBorderRightStyle();
        assertEquals("solid", borderRightWidth.getCssValue());
        
        border.setCssValue("thick green");
        assertNull(border.getBorderRightStyle());
        assertEquals("thick green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#getBorderRightWidth()}.
     */
    @Test
    public void testGetBorderRightWidth() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderRightWidth borderRightWidth = border.getBorderRightWidth();
        assertTrue(borderRightWidth.isAlreadyInUse());
        assertEquals("thick", borderRightWidth.getCssValue());
        
        border.setCssValue("solid green");
        assertNull(border.getBorderRightWidth());
        assertFalse(borderRightWidth.isAlreadyInUse());
        assertEquals("solid green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setBorderRightWidth(com.webfirmframework.wffweb.css.BorderRightWidth)}.
     */
    @Test
    public void testSetBorderRightWidth() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderRightWidth borderRightWidth = border.getBorderRightWidth();
        assertTrue(borderRightWidth.isAlreadyInUse());
        assertEquals("thick", borderRightWidth.getCssValue());
        
        border.setBorderRightWidth(new BorderRightWidth());
        
        border.setCssValue("solid green");
        assertNull(border.getBorderRightWidth());
        assertFalse(borderRightWidth.isAlreadyInUse());
        assertEquals("solid green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setBorderRightStyle(com.webfirmframework.wffweb.css.BorderRightStyle)}.
     */
    @Test
    public void testSetBorderRightStyle() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        border.setBorderRightStyle(null);
        
        assertEquals("thick green", border.getCssValue());
        
        border.setBorderRightStyle(BorderRightStyle.GROOVE);
        
        assertEquals("thick groove green", border.getCssValue());
        
        border.setCssValue("thick red");
        
        assertNull(border.getBorderRightStyle());
        
        assertEquals("thick red", border.getCssValue());
    }
    

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setBorderRightStyle(com.webfirmframework.wffweb.css.BorderRightStyle)}.
     */
    @Test
    public void testSetBorderRightWidthClone() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        final BorderRightWidth borderRightWidth = border.getBorderRightWidth();
        
        assertEquals("thick", borderRightWidth.getCssValue());
        
        BorderRight border2 = new BorderRight("thin solid green");
        final BorderRightWidth borderRightWidth2 = border2.getBorderRightWidth();
        
        assertEquals("thin", borderRightWidth2.getCssValue());
        
        border.setBorderRightWidth(borderRightWidth2);
        
        assertEquals("thin", borderRightWidth.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setBorderRightColor(com.webfirmframework.wffweb.css.BorderRightColor)}.
     */
    @Test
    public void testSetBorderRightColor() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderRightColor borderRightColor = border.getBorderRightColor();
        assertTrue(borderRightColor.isAlreadyInUse());
        assertEquals("green", borderRightColor.getCssValue());
        
        border.setBorderRightColor(new BorderRightColor("blue"));
        assertEquals("thick solid blue", border.getCssValue());
        
        border.setCssValue("thick solid");
        
        assertNull(border.getBorderRightColor());
        assertFalse(borderRightColor.isAlreadyInUse());
        assertEquals("thick solid", border.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setBorderRightStyle(com.webfirmframework.wffweb.css.BorderRightStyle)}.
     */
    @Test
    public void testSetBorderRightColorClone() {
        BorderRight border = new BorderRight();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        final BorderRightColor borderRightColor = border.getBorderRightColor();
        
        assertEquals("green", borderRightColor.getCssValue());
        
        BorderRight border2 = new BorderRight("thick solid blue");
        final BorderRightColor borderRightColor2 = border2.getBorderRightColor();
        
        assertEquals("blue", borderRightColor2.getCssValue());
        
        border.setBorderRightColor(borderRightColor2);
        
        assertEquals("blue", borderRightColor.getCssValue());
    }

    
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setBorderRightColor(com.webfirmframework.wffweb.css.BorderRightColor)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetBorderRightColorInvalidValue() {
        BorderRight border = new BorderRight();
        border.setBorderRightColor(new BorderRightColor());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderRight#setBorderRightWidth(com.webfirmframework.wffweb.css.BorderRightWidth)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetBorderRightWidthInvalidValue() {
        BorderRight border = new BorderRight();
        border.setBorderRightWidth(new BorderRightWidth("initial"));
    }


}
