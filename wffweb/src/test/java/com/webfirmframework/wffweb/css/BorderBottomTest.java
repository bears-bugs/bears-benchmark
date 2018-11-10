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
public class BorderBottomTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#BorderBottom()}.
     */
    @Test
    public void testBorderBottom() {
        BorderBottom border = new BorderBottom();
        assertEquals("medium none black", border.getCssValue());
        final BorderBottomWidth borderBottomWidth = border.getBorderBottomWidth();
        assertTrue(borderBottomWidth.isAlreadyInUse());
        assertEquals("medium", borderBottomWidth.getCssValue());
        final BorderBottomStyle borderBottomStyle = border.getBorderBottomStyle();
        assertEquals("none", borderBottomStyle.getCssValue());
        final BorderBottomColor borderBottomColor = border.getBorderBottomColor();
        assertTrue(borderBottomColor.isAlreadyInUse());
        assertEquals("black", borderBottomColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderBottomWidth.isAlreadyInUse());
        assertFalse(borderBottomColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#BorderBottom(java.lang.String)}.
     */
    @Test
    public void testBorderBottomString() {
        BorderBottom border = new BorderBottom("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderBottomWidth borderBottomWidth = border.getBorderBottomWidth();
        assertTrue(borderBottomWidth.isAlreadyInUse());
        assertEquals("thick", borderBottomWidth.getCssValue());
        final BorderBottomStyle borderBottomStyle = border.getBorderBottomStyle();
        assertEquals("solid", borderBottomStyle.getCssValue());
        final BorderBottomColor borderBottomColor = border.getBorderBottomColor();
        assertTrue(borderBottomColor.isAlreadyInUse());
        assertEquals("green", borderBottomColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderBottomWidth.isAlreadyInUse());
        assertFalse(borderBottomColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#BorderBottom(com.webfirmframework.wffweb.css.BorderBottom)}.
     */
    @Test
    public void testBorderBottomBorderBottom() {

        BorderBottom border1 = new BorderBottom("thick solid green");
        BorderBottom border = new BorderBottom(border1);
        assertEquals("thick solid green", border.getCssValue());
        final BorderBottomWidth borderBottomWidth = border.getBorderBottomWidth();
        assertTrue(borderBottomWidth.isAlreadyInUse());
        assertEquals("thick", borderBottomWidth.getCssValue());
        final BorderBottomStyle borderBottomStyle = border.getBorderBottomStyle();
        assertEquals("solid", borderBottomStyle.getCssValue());
        final BorderBottomColor borderBottomColor = border.getBorderBottomColor();
        assertTrue(borderBottomColor.isAlreadyInUse());
        assertEquals("green", borderBottomColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderBottomWidth.isAlreadyInUse());
        assertFalse(borderBottomColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        BorderBottom border = new BorderBottom();
        assertEquals(CssNameConstants.BORDER_BOTTOM, border.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {

        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderBottomWidth borderBottomWidth = border.getBorderBottomWidth();
        assertTrue(borderBottomWidth.isAlreadyInUse());
        assertEquals("thick", borderBottomWidth.getCssValue());
        final BorderBottomStyle borderBottomStyle = border.getBorderBottomStyle();
        assertEquals("solid", borderBottomStyle.getCssValue());
        final BorderBottomColor borderBottomColor = border.getBorderBottomColor();
        assertTrue(borderBottomColor.isAlreadyInUse());
        assertEquals("green", borderBottomColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(borderBottomWidth.isAlreadyInUse());
        assertFalse(borderBottomColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#toString()}.
     */
    @Test
    public void testToString() {

        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals(CssNameConstants.BORDER_BOTTOM + ": thick solid green",
                border.toString());

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        {
            BorderBottom border = new BorderBottom();
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
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setCssValue(java.lang.String)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueStringForInvalidValue() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        border.setCssValue("ddfdfdf");
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(BorderBottom.isValid("55px solid green"));
        assertTrue(BorderBottom.isValid("55px solid rgb(25, 255, 155)"));

        assertTrue(BorderBottom.isValid("55px"));
        assertTrue(BorderBottom.isValid("solid"));
        assertTrue(BorderBottom.isValid("green"));
        assertTrue(BorderBottom.isValid(BorderBottomWidth.MEDIUM + " "
                + BorderBottomStyle.DASHED.getCssValue() + " " + CssColorName.AZURE));

        assertTrue(BorderBottom.isValid(BorderBottomWidth.THICK + " "
                + BorderBottomStyle.DOUBLE.getCssValue() + " " + CssColorName.AZURE));

        assertFalse(BorderBottom.isValid("cir cle inside url(Test.png)"));
        assertFalse(BorderBottom.isValid("dmedium dfdf dfdfd"));
        assertFalse(BorderBottom.isValid("medium medium medium "));
        assertFalse(BorderBottom.isValid("55 solid green"));
        assertFalse(BorderBottom.isValid("solid solid solid"));
        assertFalse(BorderBottom.isValid("55px solid green green"));
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        {
            BorderBottom border = new BorderBottom();
            border.setCssValue("thick solid green");
            assertEquals("thick solid green", border.getCssValue());
            border.setAsInitial();
            assertEquals("initial", border.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        border.setAsInherit();
        assertEquals("inherit", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#getBorderBottomColor()}.
     */
    @Test
    public void testGetBorderBottomColor() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderBottomColor borderBottomColor = border.getBorderBottomColor();
        assertTrue(borderBottomColor.isAlreadyInUse());
        assertEquals("green", borderBottomColor.getValue());
        
        border.setCssValue("thick solid");
        assertNull(border.getBorderBottomColor());
        assertFalse(borderBottomColor.isAlreadyInUse());
        assertEquals("thick solid", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#getBorderBottomStyle()}.
     */
    @Test
    public void testGetBorderBottomStyle() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderBottomStyle borderBottomWidth = border.getBorderBottomStyle();
        assertEquals("solid", borderBottomWidth.getCssValue());
        
        border.setCssValue("thick green");
        assertNull(border.getBorderBottomStyle());
        assertEquals("thick green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#getBorderBottomWidth()}.
     */
    @Test
    public void testGetBorderBottomWidth() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderBottomWidth borderBottomWidth = border.getBorderBottomWidth();
        assertTrue(borderBottomWidth.isAlreadyInUse());
        assertEquals("thick", borderBottomWidth.getCssValue());
        
        border.setCssValue("solid green");
        assertNull(border.getBorderBottomWidth());
        assertFalse(borderBottomWidth.isAlreadyInUse());
        assertEquals("solid green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setBorderBottomWidth(com.webfirmframework.wffweb.css.BorderBottomWidth)}.
     */
    @Test
    public void testSetBorderBottomWidth() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderBottomWidth borderBottomWidth = border.getBorderBottomWidth();
        assertTrue(borderBottomWidth.isAlreadyInUse());
        assertEquals("thick", borderBottomWidth.getCssValue());
        
        border.setBorderBottomWidth(new BorderBottomWidth());
        
        border.setCssValue("solid green");
        assertNull(border.getBorderBottomWidth());
        assertFalse(borderBottomWidth.isAlreadyInUse());
        assertEquals("solid green", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setBorderBottomStyle(com.webfirmframework.wffweb.css.BorderBottomStyle)}.
     */
    @Test
    public void testSetBorderBottomStyle() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        border.setBorderBottomStyle(null);
        
        assertEquals("thick green", border.getCssValue());
        
        border.setBorderBottomStyle(BorderBottomStyle.GROOVE);
        
        assertEquals("thick groove green", border.getCssValue());
        
        border.setCssValue("thick red");
        
        assertNull(border.getBorderBottomStyle());
        
        assertEquals("thick red", border.getCssValue());
    }
    

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setBorderBottomStyle(com.webfirmframework.wffweb.css.BorderBottomStyle)}.
     */
    @Test
    public void testSetBorderBottomWidthClone() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        final BorderBottomWidth borderBottomWidth = border.getBorderBottomWidth();
        
        assertEquals("thick", borderBottomWidth.getCssValue());
        
        BorderBottom border2 = new BorderBottom("thin solid green");
        final BorderBottomWidth borderBottomWidth2 = border2.getBorderBottomWidth();
        
        assertEquals("thin", borderBottomWidth2.getCssValue());
        
        border.setBorderBottomWidth(borderBottomWidth2);
        
        assertEquals("thin", borderBottomWidth.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setBorderBottomColor(com.webfirmframework.wffweb.css.BorderBottomColor)}.
     */
    @Test
    public void testSetBorderBottomColor() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        final BorderBottomColor borderBottomColor = border.getBorderBottomColor();
        assertTrue(borderBottomColor.isAlreadyInUse());
        assertEquals("green", borderBottomColor.getCssValue());
        
        border.setBorderBottomColor(new BorderBottomColor("blue"));
        assertEquals("thick solid blue", border.getCssValue());
        
        border.setCssValue("thick solid");
        
        assertNull(border.getBorderBottomColor());
        assertFalse(borderBottomColor.isAlreadyInUse());
        assertEquals("thick solid", border.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setBorderBottomStyle(com.webfirmframework.wffweb.css.BorderBottomStyle)}.
     */
    @Test
    public void testSetBorderBottomColorClone() {
        BorderBottom border = new BorderBottom();
        border.setCssValue("thick solid green");
        assertEquals("thick solid green", border.getCssValue());
        
        final BorderBottomColor borderBottomColor = border.getBorderBottomColor();
        
        assertEquals("green", borderBottomColor.getCssValue());
        
        BorderBottom border2 = new BorderBottom("thick solid blue");
        final BorderBottomColor borderBottomColor2 = border2.getBorderBottomColor();
        
        assertEquals("blue", borderBottomColor2.getCssValue());
        
        border.setBorderBottomColor(borderBottomColor2);
        
        assertEquals("blue", borderBottomColor.getCssValue());
    }

    
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setBorderBottomColor(com.webfirmframework.wffweb.css.BorderBottomColor)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetBorderBottomColorInvalidValue() {
        BorderBottom border = new BorderBottom();
        border.setBorderBottomColor(new BorderBottomColor());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderBottom#setBorderBottomWidth(com.webfirmframework.wffweb.css.BorderBottomWidth)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetBorderBottomWidthInvalidValue() {
        BorderBottom border = new BorderBottom();
        border.setBorderBottomWidth(new BorderBottomWidth("initial"));
    }


}
