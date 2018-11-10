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
package com.webfirmframework.wffweb.css.css3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class FontSizeAdjustTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#FontSizeAdjust()}.
     */
    @Test
    public void testFontSizeAdjust() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        assertNull(fontSizeAdjust.getValue());
        assertEquals(FontSizeAdjust.NONE, fontSizeAdjust.getCssValue());
        
        fontSizeAdjust.setAsInherit();
        assertEquals(FontSizeAdjust.INHERIT, fontSizeAdjust.getCssValue());
        assertNull(fontSizeAdjust.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#FontSizeAdjust(java.lang.String)}.
     */
    @Test
    public void testFontSizeAdjustString() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust("0.5");
        assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
        assertEquals("0.5", fontSizeAdjust.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#FontSizeAdjust(com.webfirmframework.wffweb.css.css3.FontSizeAdjust)}.
     */
    @Test
    public void testFontSizeAdjustFontSizeAdjust() {
        final FontSizeAdjust fontSizeAdjust1 = new FontSizeAdjust("0.5");
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust(fontSizeAdjust1);
        assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
        assertEquals("0.5", fontSizeAdjust.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#FontSizeAdjust(float)}.
     */
    @Test
    public void testFontSizeAdjustFloat() {
        {
            FontSizeAdjust fontSizeAdjust = new FontSizeAdjust(0.5F);
            assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
            assertEquals("0.5", fontSizeAdjust.getCssValue());
            
        }
        {
            final FontSizeAdjust fontSizeAdjust1 = new FontSizeAdjust(0.5F);
            FontSizeAdjust fontSizeAdjust = new FontSizeAdjust(fontSizeAdjust1);
            assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
            assertEquals("0.5", fontSizeAdjust.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust(0.5F);
        assertEquals(CssNameConstants.FONT_SIZE_ADJUST, fontSizeAdjust.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        assertNull(fontSizeAdjust.getValue());
        assertEquals(FontSizeAdjust.NONE, fontSizeAdjust.getCssValue());

        fontSizeAdjust.setAsInherit();
        assertEquals(FontSizeAdjust.INHERIT, fontSizeAdjust.getCssValue());
        assertNull(fontSizeAdjust.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#toString()}.
     */
    @Test
    public void testToString() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        assertNull(fontSizeAdjust.getValue());
        assertEquals(CssNameConstants.FONT_SIZE_ADJUST + ": "+FontSizeAdjust.NONE, fontSizeAdjust.toString());

        fontSizeAdjust.setAsInherit();
        assertEquals(CssNameConstants.FONT_SIZE_ADJUST + ": " + FontSizeAdjust.INHERIT,
                fontSizeAdjust.toString());
        assertNull(fontSizeAdjust.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#getValue()}.
     */
    @Test
    public void testGetValue() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        assertNull(fontSizeAdjust.getValue());
        assertEquals(FontSizeAdjust.NONE, fontSizeAdjust.getCssValue());
        fontSizeAdjust.setAsInherit();
        assertNull(fontSizeAdjust.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#setValue(float)}.
     */
    @Test
    public void testSetValue() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        fontSizeAdjust.setValue(0.5F);
        assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
        fontSizeAdjust.setAsInherit();
        assertNull(fontSizeAdjust.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        fontSizeAdjust.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
        fontSizeAdjust.setAsInherit();
        assertNull(fontSizeAdjust.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        fontSizeAdjust.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
        fontSizeAdjust.setAsInitial();
        assertEquals(FontSizeAdjust.INITIAL, fontSizeAdjust.getCssValue());
        assertNull(fontSizeAdjust.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        fontSizeAdjust.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
        fontSizeAdjust.setAsInherit();
        assertEquals(FontSizeAdjust.INHERIT, fontSizeAdjust.getCssValue());
        assertNull(fontSizeAdjust.getValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#setAsNone()}.
     */
    @Test
    public void testSetAsNone() {
        FontSizeAdjust fontSizeAdjust = new FontSizeAdjust();
        fontSizeAdjust.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), fontSizeAdjust.getValue());
        fontSizeAdjust.setAsNone();
        assertEquals(FontSizeAdjust.NONE, fontSizeAdjust.getCssValue());
        assertNull(fontSizeAdjust.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new FontSizeAdjust().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FontSizeAdjust#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(FontSizeAdjust.isValid(".5"));
        assertTrue(FontSizeAdjust.isValid("0.5"));
        assertTrue(FontSizeAdjust.isValid("0.1"));
        assertTrue(FontSizeAdjust.isValid("1"));
        assertTrue(FontSizeAdjust.isValid("0"));
        assertTrue(FontSizeAdjust.isValid(FontSizeAdjust.INITIAL));
        assertTrue(FontSizeAdjust.isValid(FontSizeAdjust.INHERIT));
        assertFalse(FontSizeAdjust.isValid("dfd"));
        assertFalse(FontSizeAdjust.isValid("1px"));
        assertFalse(FontSizeAdjust.isValid(""));
        assertFalse(FontSizeAdjust.isValid("1 1"));
        assertTrue(FontSizeAdjust.isValid("1.1"));
//        assertFalse(FontSizeAdjust.isValid("-0.5"));
//        assertFalse(FontSizeAdjust.isValid("-0"));
//        assertFalse(FontSizeAdjust.isValid("-0.0"));
//        assertFalse(FontSizeAdjust.isValid("+0.0"));
    }

}
