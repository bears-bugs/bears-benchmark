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
public class OutlineTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#Outline()}.
     */
    @Test
    public void testOutline() {
        Outline border = new Outline();
        assertEquals("black none medium", border.getCssValue());
        final OutlineWidth outlineWidth = border.getOutlineWidth();
        assertTrue(outlineWidth.isAlreadyInUse());
        assertEquals("medium", outlineWidth.getCssValue());
        final OutlineStyle outlineStyle = border.getOutlineStyle();
        assertEquals("none", outlineStyle.getCssValue());
        final OutlineColor outlineColor = border.getOutlineColor();
        assertTrue(outlineColor.isAlreadyInUse());
        assertEquals("black", outlineColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(outlineWidth.isAlreadyInUse());
        assertFalse(outlineColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#Outline(java.lang.String)}.
     */
    @Test
    public void testOutlineString() {
        Outline border = new Outline("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        final OutlineWidth outlineWidth = border.getOutlineWidth();
        assertTrue(outlineWidth.isAlreadyInUse());
        assertEquals("thick", outlineWidth.getCssValue());
        final OutlineStyle outlineStyle = border.getOutlineStyle();
        assertEquals("solid", outlineStyle.getCssValue());
        final OutlineColor outlineColor = border.getOutlineColor();
        assertTrue(outlineColor.isAlreadyInUse());
        assertEquals("green", outlineColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(outlineWidth.isAlreadyInUse());
        assertFalse(outlineColor.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#Outline(com.webfirmframework.wffweb.css.Outline)}.
     */
    @Test
    public void testOutlineOutline() {

        Outline border1 = new Outline("green solid thick");
        Outline border = new Outline(border1);
        assertEquals("green solid thick", border.getCssValue());
        final OutlineWidth outlineWidth = border.getOutlineWidth();
        assertTrue(outlineWidth.isAlreadyInUse());
        assertEquals("thick", outlineWidth.getCssValue());
        final OutlineStyle outlineStyle = border.getOutlineStyle();
        assertEquals("solid", outlineStyle.getCssValue());
        final OutlineColor outlineColor = border.getOutlineColor();
        assertTrue(outlineColor.isAlreadyInUse());
        assertEquals("green", outlineColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(outlineWidth.isAlreadyInUse());
        assertFalse(outlineColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        Outline border = new Outline();
        assertEquals(CssNameConstants.OUTLINE, border.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {

        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        final OutlineWidth outlineWidth = border.getOutlineWidth();
        assertTrue(outlineWidth.isAlreadyInUse());
        assertEquals("thick", outlineWidth.getCssValue());
        final OutlineStyle outlineStyle = border.getOutlineStyle();
        assertEquals("solid", outlineStyle.getCssValue());
        final OutlineColor outlineColor = border.getOutlineColor();
        assertTrue(outlineColor.isAlreadyInUse());
        assertEquals("green", outlineColor.getCssValue());
        
        border.setAsInitial();
        
        assertFalse(outlineWidth.isAlreadyInUse());
        assertFalse(outlineColor.isAlreadyInUse());
    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#toString()}.
     */
    @Test
    public void testToString() {

        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals(CssNameConstants.OUTLINE + ": green solid thick",
                border.toString());

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        {
            Outline border = new Outline();
            border.setCssValue("green solid thick");
            assertEquals("green solid thick", border.getCssValue());
            try {
                border.setCssValue("ddfdfdf");
                fail("testSetCssValueString :- setCssValue should not accept invalid values");
            } catch (InvalidValueException e) {
                assertEquals("green solid thick", border.getCssValue());
            }
        
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setCssValue(java.lang.String)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueStringForInvalidValue() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        border.setCssValue("ddfdfdf");
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(Outline.isValid("55px solid green"));
        assertTrue(Outline.isValid("55px solid rgb(25, 255, 155)"));

        assertTrue(Outline.isValid("55px"));
        assertTrue(Outline.isValid("solid"));
        assertTrue(Outline.isValid("green"));
        assertTrue(Outline.isValid(OutlineWidth.MEDIUM + " "
                + OutlineStyle.DASHED.getCssValue() + " " + CssColorName.AZURE));

        assertTrue(Outline.isValid(OutlineWidth.THICK + " "
                + OutlineStyle.DOUBLE.getCssValue() + " " + CssColorName.AZURE));

        assertFalse(Outline.isValid("cir cle inside url(Test.png)"));
        assertFalse(Outline.isValid("dmedium dfdf dfdfd"));
        assertFalse(Outline.isValid("medium medium medium "));
        assertFalse(Outline.isValid("55 solid green"));
        assertFalse(Outline.isValid("solid solid solid"));
        assertFalse(Outline.isValid("55px solid green green"));
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        {
            Outline border = new Outline();
            border.setCssValue("green solid thick");
            assertEquals("green solid thick", border.getCssValue());
            border.setAsInitial();
            assertEquals("initial", border.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        border.setAsInherit();
        assertEquals("inherit", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#getOutlineColor()}.
     */
    @Test
    public void testGetOutlineColor() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        final OutlineColor outlineColor = border.getOutlineColor();
        assertTrue(outlineColor.isAlreadyInUse());
        assertEquals("green", outlineColor.getValue());
        
        border.setCssValue("solid thick");
        assertNull(border.getOutlineColor());
        assertFalse(outlineColor.isAlreadyInUse());
        assertEquals("solid thick", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#getOutlineStyle()}.
     */
    @Test
    public void testGetOutlineStyle() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        final OutlineStyle outlineWidth = border.getOutlineStyle();
        assertEquals("solid", outlineWidth.getCssValue());
        
        border.setCssValue("green thick");
        assertNull(border.getOutlineStyle());
        assertEquals("green thick", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#getOutlineWidth()}.
     */
    @Test
    public void testGetOutlineWidth() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        final OutlineWidth outlineWidth = border.getOutlineWidth();
        assertTrue(outlineWidth.isAlreadyInUse());
        assertEquals("thick", outlineWidth.getCssValue());
        
        border.setCssValue("green solid");
        assertNull(border.getOutlineWidth());
        assertFalse(outlineWidth.isAlreadyInUse());
        assertEquals("green solid", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setOutlineWidth(com.webfirmframework.wffweb.css.OutlineWidth)}.
     */
    @Test
    public void testSetOutlineWidth() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        final OutlineWidth outlineWidth = border.getOutlineWidth();
        assertTrue(outlineWidth.isAlreadyInUse());
        assertEquals("thick", outlineWidth.getCssValue());
        
        border.setOutlineWidth(new OutlineWidth(OutlineWidth.MEDIUM));
        
        border.setCssValue("solid green");
        assertNull(border.getOutlineWidth());
        assertFalse(outlineWidth.isAlreadyInUse());
        assertEquals("green solid", border.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setOutlineStyle(com.webfirmframework.wffweb.css.OutlineStyle)}.
     */
    @Test
    public void testSetOutlineStyle() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        
        border.setOutlineStyle(null);
        
        assertEquals("green thick", border.getCssValue());
        
        border.setOutlineStyle(OutlineStyle.GROOVE);
        
        assertEquals("green groove thick", border.getCssValue());
        
        border.setCssValue("red thick");
        
        assertNull(border.getOutlineStyle());
        
        assertEquals("red thick", border.getCssValue());
    }
    

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setOutlineStyle(com.webfirmframework.wffweb.css.OutlineStyle)}.
     */
    @Test
    public void testSetOutlineWidthClone() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        
        final OutlineWidth outlineWidth = border.getOutlineWidth();
        
        assertEquals("thick", outlineWidth.getCssValue());
        
        Outline border2 = new Outline("thin solid green");
        final OutlineWidth outlineWidth2 = border2.getOutlineWidth();
        
        assertEquals("thin", outlineWidth2.getCssValue());
        
        border.setOutlineWidth(outlineWidth2);
        
        assertEquals("thin", outlineWidth.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setOutlineColor(com.webfirmframework.wffweb.css.OutlineColor)}.
     */
    @Test
    public void testSetOutlineColor() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        final OutlineColor outlineColor = border.getOutlineColor();
        assertTrue(outlineColor.isAlreadyInUse());
        assertEquals("green", outlineColor.getCssValue());
        
        border.setOutlineColor(new OutlineColor("blue"));
        assertEquals("blue solid thick", border.getCssValue());
        
        border.setCssValue("solid thick");
        
        assertNull(border.getOutlineColor());
        assertFalse(outlineColor.isAlreadyInUse());
        assertEquals("solid thick", border.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setOutlineStyle(com.webfirmframework.wffweb.css.OutlineStyle)}.
     */
    @Test
    public void testSetOutlineColorClone() {
        Outline border = new Outline();
        border.setCssValue("green solid thick");
        assertEquals("green solid thick", border.getCssValue());
        
        final OutlineColor outlineColor = border.getOutlineColor();
        
        assertEquals("green", outlineColor.getCssValue());
        
        Outline border2 = new Outline("blue solid thick");
        final OutlineColor outlineColor2 = border2.getOutlineColor();
        
        assertEquals("blue", outlineColor2.getCssValue());
        
        border.setOutlineColor(outlineColor2);
        
        assertEquals("blue", outlineColor.getCssValue());
    }

    
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setOutlineColor(com.webfirmframework.wffweb.css.OutlineColor)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetOutlineColorInvalidValue() {
        Outline border = new Outline();
        border.setOutlineColor(new OutlineColor(OutlineColor.INITIAL));
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.Outline#setOutlineWidth(com.webfirmframework.wffweb.css.OutlineWidth)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetOutlineWidthInvalidValue() {
        Outline border = new Outline();
        border.setOutlineWidth(new OutlineWidth("initial"));
    }


}
