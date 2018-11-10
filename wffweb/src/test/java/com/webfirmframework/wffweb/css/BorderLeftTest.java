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
public class BorderLeftTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#BorderLeft()}.
     */
    @Test
    public void testBorderLeft() {
        BorderLeft border = new BorderLeft();
        assertEquals("medium none black", border.getCssValue());
        final BorderLeftWidth borderLeftWidth = border.getBorderLeftWidth();
        assertTrue(borderLeftWidth.isAlreadyInUse());
        assertEquals("medium", borderLeftWidth.getCssValue());
        final BorderLeftStyle borderLeftStyle = border.getBorderLeftStyle();
        assertEquals("none", borderLeftStyle.getCssValue());
        final BorderLeftColor borderLeftColor = border.getBorderLeftColor();
        assertTrue(borderLeftColor.isAlreadyInUse());
        assertEquals("black", borderLeftColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderLeftWidth.isAlreadyInUse());
        assertFalse(borderLeftColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#BorderLeft(java.lang.String)}.
     */
    @Test
    public void testBorderLeftString() {
        BorderLeft border = new BorderLeft("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderLeftWidth borderLeftWidth = border.getBorderLeftWidth();
        assertTrue(borderLeftWidth.isAlreadyInUse());
        assertEquals("thick", borderLeftWidth.getCssValue());
        final BorderLeftStyle borderLeftStyle = border.getBorderLeftStyle();
        assertEquals("solid", borderLeftStyle.getCssValue());
        final BorderLeftColor borderLeftColor = border.getBorderLeftColor();
        assertTrue(borderLeftColor.isAlreadyInUse());
        assertEquals("green", borderLeftColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderLeftWidth.isAlreadyInUse());
        assertFalse(borderLeftColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#BorderLeft(com.webfirmframework.wffweb.css.BorderLeft)}.
     */
    @Test
    public void testBorderLeftBorderLeft() {

        BorderLeft border1 = new BorderLeft("thick solid green");
        BorderLeft border = new BorderLeft(border1);
        assertEquals("thick solid green", border.getCssValue());
        final BorderLeftWidth borderLeftWidth = border.getBorderLeftWidth();
        assertTrue(borderLeftWidth.isAlreadyInUse());
        assertEquals("thick", borderLeftWidth.getCssValue());
        final BorderLeftStyle borderLeftStyle = border.getBorderLeftStyle();
        assertEquals("solid", borderLeftStyle.getCssValue());
        final BorderLeftColor borderLeftColor = border.getBorderLeftColor();
        assertTrue(borderLeftColor.isAlreadyInUse());
        assertEquals("green", borderLeftColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderLeftWidth.isAlreadyInUse());
        assertFalse(borderLeftColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        BorderLeft border = new BorderLeft();
        assertEquals(CssNameConstants.BORDER_LEFT, border.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {

        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderLeftWidth borderLeftWidth = border.getBorderLeftWidth();
        assertTrue(borderLeftWidth.isAlreadyInUse());
        assertEquals("thick", borderLeftWidth.getCssValue());
        final BorderLeftStyle borderLeftStyle = border.getBorderLeftStyle();
        assertEquals("solid", borderLeftStyle.getCssValue());
        final BorderLeftColor borderLeftColor = border.getBorderLeftColor();
        assertTrue(borderLeftColor.isAlreadyInUse());
        assertEquals("green", borderLeftColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderLeftWidth.isAlreadyInUse());
        assertFalse(borderLeftColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#toString()}.
     */
    @Test
    public void testToString() {

        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals(CssNameConstants.BORDER_LEFT + ": thick solid green",
                border.toString());

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        {
            BorderLeft border = new BorderLeft();
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
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setCssValue(java.lang.String)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueStringForInvalidValue() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        border.setCssValue("ddfdfdf");
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(BorderLeft.isValid("55px solid green"));
        assertTrue(BorderLeft.isValid("55px solid rgb(25, 255, 155)"));

        assertTrue(BorderLeft.isValid("55px"));
        assertTrue(BorderLeft.isValid("solid"));
        assertTrue(BorderLeft.isValid("green"));
        assertTrue(BorderLeft.isValid(BorderLeftWidth.MEDIUM + " "
                + BorderLeftStyle.DASHED.getCssValue() + " " + CssColorName.AZURE));

        assertTrue(BorderLeft.isValid(BorderLeftWidth.THICK + " "
                + BorderLeftStyle.DOUBLE.getCssValue() + " " + CssColorName.AZURE));

        assertFalse(BorderLeft.isValid("cir cle inside url(Test.png)"));
        assertFalse(BorderLeft.isValid("dmedium dfdf dfdfd"));
        assertFalse(BorderLeft.isValid("medium medium medium "));
        assertFalse(BorderLeft.isValid("55 solid green"));
        assertFalse(BorderLeft.isValid("solid solid solid"));
        assertFalse(BorderLeft.isValid("55px solid green green"));
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        {
            BorderLeft border = new BorderLeft();
            border.setCssValue("thick solid green");
            assertEquals("thick solid green", border.getCssValue());
            border.setAsInitial();
            assertEquals("initial", border.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        border.setAsInherit();
        assertEquals("inherit", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#getBorderLeftColor()}.
     */
    @Test
    public void testGetBorderLeftColor() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderLeftColor borderLeftColor = border.getBorderLeftColor();
        assertTrue(borderLeftColor.isAlreadyInUse());
        assertEquals("green", borderLeftColor.getValue());
        
        border.setCssValue("thick solid");
        assertNull(border.getBorderLeftColor());
        assertFalse(borderLeftColor.isAlreadyInUse());
        assertEquals("thick solid", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#getBorderLeftStyle()}.
     */
    @Test
    public void testGetBorderLeftStyle() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderLeftStyle borderLeftWidth = border.getBorderLeftStyle();
        assertEquals("solid", borderLeftWidth.getCssValue());
        
        border.setCssValue("thick green");
        assertNull(border.getBorderLeftStyle());
        assertEquals("thick green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#getBorderLeftWidth()}.
     */
    @Test
    public void testGetBorderLeftWidth() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderLeftWidth borderLeftWidth = border.getBorderLeftWidth();
        assertTrue(borderLeftWidth.isAlreadyInUse());
        assertEquals("thick", borderLeftWidth.getCssValue());
        
        border.setCssValue("solid green");
        assertNull(border.getBorderLeftWidth());
        assertFalse(borderLeftWidth.isAlreadyInUse());
        assertEquals("solid green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setBorderLeftWidth(com.webfirmframework.wffweb.css.BorderLeftWidth)}.
     */
    @Test
    public void testSetBorderLeftWidth() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderLeftWidth borderLeftWidth = border.getBorderLeftWidth();
        assertTrue(borderLeftWidth.isAlreadyInUse());
        assertEquals("thick", borderLeftWidth.getCssValue());
        
        border.setBorderLeftWidth(new BorderLeftWidth());
        
        border.setCssValue("solid green");
        assertNull(border.getBorderLeftWidth());
        assertFalse(borderLeftWidth.isAlreadyInUse());
        assertEquals("solid green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setBorderLeftStyle(com.webfirmframework.wffweb.css.BorderLeftStyle)}.
     */
    @Test
    public void testSetBorderLeftStyle() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        border.setBorderLeftStyle(null);
        
        assertEquals("thick green", border.getCssValue());
        
        border.setBorderLeftStyle(BorderLeftStyle.GROOVE);
        
        assertEquals("thick groove green", border.getCssValue());
        
        border.setCssValue("thick red");
        
        assertNull(border.getBorderLeftStyle());
        
        assertEquals("thick red", border.getCssValue());
    }
    

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setBorderLeftStyle(com.webfirmframework.wffweb.css.BorderLeftStyle)}.
     */
    @Test
    public void testSetBorderLeftWidthClone() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        final BorderLeftWidth borderLeftWidth = border.getBorderLeftWidth();
        
        assertEquals("thick", borderLeftWidth.getCssValue());
        
        BorderLeft border2 = new BorderLeft("thin solid green");
        final BorderLeftWidth borderLeftWidth2 = border2.getBorderLeftWidth();
        
        assertEquals("thin", borderLeftWidth2.getCssValue());
        
        border.setBorderLeftWidth(borderLeftWidth2);
        
        assertEquals("thin", borderLeftWidth.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setBorderLeftColor(com.webfirmframework.wffweb.css.BorderLeftColor)}.
     */
    @Test
    public void testSetBorderLeftColor() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderLeftColor borderLeftColor = border.getBorderLeftColor();
        assertTrue(borderLeftColor.isAlreadyInUse());
        assertEquals("green", borderLeftColor.getCssValue());
        
        border.setBorderLeftColor(new BorderLeftColor("blue"));
        assertEquals("thick solid blue", border.getCssValue());
        
        border.setCssValue("thick solid");
        
        assertNull(border.getBorderLeftColor());
        assertFalse(borderLeftColor.isAlreadyInUse());
        assertEquals("thick solid", border.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setBorderLeftStyle(com.webfirmframework.wffweb.css.BorderLeftStyle)}.
     */
    @Test
    public void testSetBorderLeftColorClone() {
        BorderLeft border = new BorderLeft();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        final BorderLeftColor borderLeftColor = border.getBorderLeftColor();
        
        assertEquals("green", borderLeftColor.getCssValue());
        
        BorderLeft border2 = new BorderLeft("thick solid blue");
        final BorderLeftColor borderLeftColor2 = border2.getBorderLeftColor();
        
        assertEquals("blue", borderLeftColor2.getCssValue());
        
        border.setBorderLeftColor(borderLeftColor2);
        
        assertEquals("blue", borderLeftColor.getCssValue());
    }

    
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setBorderLeftColor(com.webfirmframework.wffweb.css.BorderLeftColor)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetBorderLeftColorInvalidValue() {
        BorderLeft border = new BorderLeft();
        border.setBorderLeftColor(new BorderLeftColor());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderLeft#setBorderLeftWidth(com.webfirmframework.wffweb.css.BorderLeftWidth)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetBorderLeftWidthInvalidValue() {
        BorderLeft border = new BorderLeft();
        border.setBorderLeftWidth(new BorderLeftWidth("initial"));
    }


}
