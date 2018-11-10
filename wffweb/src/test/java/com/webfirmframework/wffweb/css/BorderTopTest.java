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
public class BorderTopTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#BorderTop()}.
     */
    @Test
    public void testBorderTop() {
        BorderTop border = new BorderTop();
        assertEquals("medium none black", border.getCssValue());
        final BorderTopWidth borderTopWidth = border.getBorderTopWidth();
        assertTrue(borderTopWidth.isAlreadyInUse());
        assertEquals("medium", borderTopWidth.getCssValue());
        final BorderTopStyle borderTopStyle = border.getBorderTopStyle();
        assertEquals("none", borderTopStyle.getCssValue());
        final BorderTopColor borderTopColor = border.getBorderTopColor();
        assertTrue(borderTopColor.isAlreadyInUse());
        assertEquals("black", borderTopColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderTopWidth.isAlreadyInUse());
        assertFalse(borderTopColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#BorderTop(java.lang.String)}.
     */
    @Test
    public void testBorderTopString() {
        BorderTop border = new BorderTop("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderTopWidth borderTopWidth = border.getBorderTopWidth();
        assertTrue(borderTopWidth.isAlreadyInUse());
        assertEquals("thick", borderTopWidth.getCssValue());
        final BorderTopStyle borderTopStyle = border.getBorderTopStyle();
        assertEquals("solid", borderTopStyle.getCssValue());
        final BorderTopColor borderTopColor = border.getBorderTopColor();
        assertTrue(borderTopColor.isAlreadyInUse());
        assertEquals("green", borderTopColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderTopWidth.isAlreadyInUse());
        assertFalse(borderTopColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#BorderTop(com.webfirmframework.wffweb.css.BorderTop)}.
     */
    @Test
    public void testBorderTopBorderTop() {

        BorderTop border1 = new BorderTop("thick solid green");
        BorderTop border = new BorderTop(border1);
        assertEquals("thick solid green", border.getCssValue());
        final BorderTopWidth borderTopWidth = border.getBorderTopWidth();
        assertTrue(borderTopWidth.isAlreadyInUse());
        assertEquals("thick", borderTopWidth.getCssValue());
        final BorderTopStyle borderTopStyle = border.getBorderTopStyle();
        assertEquals("solid", borderTopStyle.getCssValue());
        final BorderTopColor borderTopColor = border.getBorderTopColor();
        assertTrue(borderTopColor.isAlreadyInUse());
        assertEquals("green", borderTopColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderTopWidth.isAlreadyInUse());
        assertFalse(borderTopColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        BorderTop border = new BorderTop();
        assertEquals(CssNameConstants.BORDER_TOP, border.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {

        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderTopWidth borderTopWidth = border.getBorderTopWidth();
        assertTrue(borderTopWidth.isAlreadyInUse());
        assertEquals("thick", borderTopWidth.getCssValue());
        final BorderTopStyle borderTopStyle = border.getBorderTopStyle();
        assertEquals("solid", borderTopStyle.getCssValue());
        final BorderTopColor borderTopColor = border.getBorderTopColor();
        assertTrue(borderTopColor.isAlreadyInUse());
        assertEquals("green", borderTopColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderTopWidth.isAlreadyInUse());
        assertFalse(borderTopColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#toString()}.
     */
    @Test
    public void testToString() {

        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals(CssNameConstants.BORDER_TOP + ": thick solid green",
                border.toString());

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        {
            BorderTop border = new BorderTop();
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
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setCssValue(java.lang.String)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueStringForInvalidValue() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        border.setCssValue("ddfdfdf");
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(BorderTop.isValid("55px solid green"));
        assertTrue(BorderTop.isValid("55px solid rgb(25, 255, 155)"));

        assertTrue(BorderTop.isValid("55px"));
        assertTrue(BorderTop.isValid("solid"));
        assertTrue(BorderTop.isValid("green"));
        assertTrue(BorderTop.isValid(BorderTopWidth.MEDIUM + " "
                + BorderTopStyle.DASHED.getCssValue() + " " + CssColorName.AZURE));

        assertTrue(BorderTop.isValid(BorderTopWidth.THICK + " "
                + BorderTopStyle.DOUBLE.getCssValue() + " " + CssColorName.AZURE));

        assertFalse(BorderTop.isValid("cir cle inside url(Test.png)"));
        assertFalse(BorderTop.isValid("dmedium dfdf dfdfd"));
        assertFalse(BorderTop.isValid("medium medium medium "));
        assertFalse(BorderTop.isValid("55 solid green"));
        assertFalse(BorderTop.isValid("solid solid solid"));
        assertFalse(BorderTop.isValid("55px solid green green"));
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        {
            BorderTop border = new BorderTop();
            border.setCssValue("thick solid green");
            assertEquals("thick solid green", border.getCssValue());
            border.setAsInitial();
            assertEquals("initial", border.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        border.setAsInherit();
        assertEquals("inherit", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#getBorderTopColor()}.
     */
    @Test
    public void testGetBorderTopColor() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderTopColor borderTopColor = border.getBorderTopColor();
        assertTrue(borderTopColor.isAlreadyInUse());
        assertEquals("green", borderTopColor.getValue());
        
        border.setCssValue("thick solid");
        assertNull(border.getBorderTopColor());
        assertFalse(borderTopColor.isAlreadyInUse());
        assertEquals("thick solid", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#getBorderTopStyle()}.
     */
    @Test
    public void testGetBorderTopStyle() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderTopStyle borderTopWidth = border.getBorderTopStyle();
        assertEquals("solid", borderTopWidth.getCssValue());
        
        border.setCssValue("thick green");
        assertNull(border.getBorderTopStyle());
        assertEquals("thick green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#getBorderTopWidth()}.
     */
    @Test
    public void testGetBorderTopWidth() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderTopWidth borderTopWidth = border.getBorderTopWidth();
        assertTrue(borderTopWidth.isAlreadyInUse());
        assertEquals("thick", borderTopWidth.getCssValue());
        
        border.setCssValue("solid green");
        assertNull(border.getBorderTopWidth());
        assertFalse(borderTopWidth.isAlreadyInUse());
        assertEquals("solid green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setBorderTopWidth(com.webfirmframework.wffweb.css.BorderTopWidth)}.
     */
    @Test
    public void testSetBorderTopWidth() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderTopWidth borderTopWidth = border.getBorderTopWidth();
        assertTrue(borderTopWidth.isAlreadyInUse());
        assertEquals("thick", borderTopWidth.getCssValue());
        
        border.setBorderTopWidth(new BorderTopWidth());
        
        border.setCssValue("solid green");
        assertNull(border.getBorderTopWidth());
        assertFalse(borderTopWidth.isAlreadyInUse());
        assertEquals("solid green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setBorderTopStyle(com.webfirmframework.wffweb.css.BorderTopStyle)}.
     */
    @Test
    public void testSetBorderTopStyle() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        border.setBorderTopStyle(null);
        
        assertEquals("thick green", border.getCssValue());
        
        border.setBorderTopStyle(BorderTopStyle.GROOVE);
        
        assertEquals("thick groove green", border.getCssValue());
        
        border.setCssValue("thick red");
        
        assertNull(border.getBorderTopStyle());
        
        assertEquals("thick red", border.getCssValue());
    }
    

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setBorderTopStyle(com.webfirmframework.wffweb.css.BorderTopStyle)}.
     */
    @Test
    public void testSetBorderTopWidthClone() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        final BorderTopWidth borderTopWidth = border.getBorderTopWidth();
        
        assertEquals("thick", borderTopWidth.getCssValue());
        
        BorderTop border2 = new BorderTop("thin solid green");
        final BorderTopWidth borderTopWidth2 = border2.getBorderTopWidth();
        
        assertEquals("thin", borderTopWidth2.getCssValue());
        
        border.setBorderTopWidth(borderTopWidth2);
        
        assertEquals("thin", borderTopWidth.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setBorderTopColor(com.webfirmframework.wffweb.css.BorderTopColor)}.
     */
    @Test
    public void testSetBorderTopColor() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderTopColor borderTopColor = border.getBorderTopColor();
        assertTrue(borderTopColor.isAlreadyInUse());
        assertEquals("green", borderTopColor.getCssValue());
        
        border.setBorderTopColor(new BorderTopColor("blue"));
        assertEquals("thick solid blue", border.getCssValue());
        
        border.setCssValue("thick solid");
        
        assertNull(border.getBorderTopColor());
        assertFalse(borderTopColor.isAlreadyInUse());
        assertEquals("thick solid", border.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setBorderTopStyle(com.webfirmframework.wffweb.css.BorderTopStyle)}.
     */
    @Test
    public void testSetBorderTopColorClone() {
        BorderTop border = new BorderTop();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        final BorderTopColor borderTopColor = border.getBorderTopColor();
        
        assertEquals("green", borderTopColor.getCssValue());
        
        BorderTop border2 = new BorderTop("thick solid blue");
        final BorderTopColor borderTopColor2 = border2.getBorderTopColor();
        
        assertEquals("blue", borderTopColor2.getCssValue());
        
        border.setBorderTopColor(borderTopColor2);
        
        assertEquals("blue", borderTopColor.getCssValue());
    }

    
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setBorderTopColor(com.webfirmframework.wffweb.css.BorderTopColor)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetBorderTopColorInvalidValue() {
        BorderTop border = new BorderTop();
        border.setBorderTopColor(new BorderTopColor());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderTop#setBorderTopWidth(com.webfirmframework.wffweb.css.BorderTopWidth)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetBorderTopWidthInvalidValue() {
        BorderTop border = new BorderTop();
        border.setBorderTopWidth(new BorderTopWidth("initial"));
    }


}
