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

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class FontFamilyTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.FontFamily#FontFamily()}.
     */
    @Test
    public void testFontFamily() {
        FontFamily fontFamily = new FontFamily();
        assertEquals(0, fontFamily.getFontFamilyNames().length);
        assertEquals(FontFamily.INITIAL, fontFamily.getCssValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#FontFamily(java.lang.String)}.
     */
    @Test
    public void testFontFamilyString() {
        FontFamily fontFamily = new FontFamily("\"Times New Roman\", Georgia, "
                + GenericFontFamilyNameContants.SERIF);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals("Times New Roman", fontFamily.getFontFamilyNames()[0]);
        assertEquals("Georgia", fontFamily.getFontFamilyNames()[1]);
        assertEquals(GenericFontFamilyNameContants.SERIF,
                fontFamily.getFontFamilyNames()[2]);

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#FontFamily(java.lang.String[])}
     * .
     */
    @Test
    public void testFontFamilyStringArray() {
        FontFamily fontFamily = new FontFamily();
        String[] fontFamilyNames = { "Times New Roman", "Georgia",
                GenericFontFamilyNameContants.SERIF };
        fontFamily.setFontFamilyNames(fontFamilyNames);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals(fontFamilyNames[0], fontFamily.getFontFamilyNames()[0]);
        assertEquals(fontFamilyNames[1], fontFamily.getFontFamilyNames()[1]);
        assertEquals(fontFamilyNames[2], fontFamily.getFontFamilyNames()[2]);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#getBuiltCssValue(java.lang.String[])}
     * .
     */
    @Test
    public void testGetBuiltCssValue() {
        String cssValue = FontFamily.getBuiltCssValue("Times New Roman",
                "Georgia", GenericFontFamilyNameContants.SERIF);
        assertEquals("\"Times New Roman\", Georgia, serif", cssValue);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#FontFamily(com.webfirmframework.wffweb.css.FontFamily)}
     * .
     */
    @Test
    public void testFontFamilyFontFamily() {
        FontFamily fontFamily = new FontFamily(new FontFamily(
                "\"Times New Roman\", Georgia, "
                        + GenericFontFamilyNameContants.SERIF));
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals("Times New Roman", fontFamily.getFontFamilyNames()[0]);
        assertEquals("Georgia", fontFamily.getFontFamilyNames()[1]);
        assertEquals(GenericFontFamilyNameContants.SERIF,
                fontFamily.getFontFamilyNames()[2]);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.FontFamily#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        FontFamily fontFamily = new FontFamily("\"Times New Roman\", Georgia, "
                + GenericFontFamilyNameContants.SERIF);

        assertEquals("\"Times New Roman\", Georgia, "
                + GenericFontFamilyNameContants.SERIF, fontFamily.getCssValue());   
        
        fontFamily.setAsInherit();
        
        assertEquals(0, fontFamily.getFontFamilyNames().length);
        assertEquals(FontFamily.INHERIT, fontFamily.getCssValue());   
        }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.FontFamily#getCssValue()}
     * .
     */
    @Test
    public void testGetCssValue() {
        FontFamily fontFamily = new FontFamily();
        assertEquals(0, fontFamily.getFontFamilyNames().length);
        String[] fontFamilyNames = { "Times New Roman", "Georgia",
                GenericFontFamilyNameContants.SERIF };
        fontFamily.setFontFamilyNames(fontFamilyNames);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals(fontFamilyNames[0], fontFamily.getFontFamilyNames()[0]);
        assertEquals(fontFamilyNames[1], fontFamily.getFontFamilyNames()[1]);
        assertEquals(fontFamilyNames[2], fontFamily.getFontFamilyNames()[2]);

        assertEquals(CssNameConstants.FONT_FAMILY
                + ": \"Times New Roman\", Georgia, serif",
                fontFamily.toString());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.FontFamily#toString()}.
     */
    @Test
    public void testToString() {
        FontFamily fontFamily = new FontFamily();
        assertEquals(0, fontFamily.getFontFamilyNames().length);
        String[] fontFamilyNames = { "Times New Roman", "Georgia",
                GenericFontFamilyNameContants.SERIF };
        fontFamily.setFontFamilyNames(fontFamilyNames);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals(fontFamilyNames[0], fontFamily.getFontFamilyNames()[0]);
        assertEquals(fontFamilyNames[1], fontFamily.getFontFamilyNames()[1]);
        assertEquals(fontFamilyNames[2], fontFamily.getFontFamilyNames()[2]);

        assertEquals(CssNameConstants.FONT_FAMILY
                + ": \"Times New Roman\", Georgia, serif",
                fontFamily.toString());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        FontFamily fontFamily = new FontFamily();
        fontFamily.setCssValue("\"Times New Roman\", Georgia, "
                + GenericFontFamilyNameContants.SERIF);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals("Times New Roman", fontFamily.getFontFamilyNames()[0]);
        assertEquals("Georgia", fontFamily.getFontFamilyNames()[1]);
        assertEquals(GenericFontFamilyNameContants.SERIF,
                fontFamily.getFontFamilyNames()[2]);    
        }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#getExtractedFamilyNames(java.lang.String, boolean)}
     * .
     */
    @Test
    public void testGetExtractedFamilyNames() {
        String[] extractedFamilyNames = FontFamily.getExtractedFamilyNames("\"Times New Roman\", Georgia, "
                + GenericFontFamilyNameContants.SERIF, false);
        
        assertEquals(3, extractedFamilyNames.length);
        assertEquals("Times New Roman", extractedFamilyNames[0]);
        assertEquals("Georgia", extractedFamilyNames[1]);
        assertEquals(GenericFontFamilyNameContants.SERIF,
                extractedFamilyNames[2]);   
    }

    @Test(expected = InvalidValueException.class)
    public void testGetExtractedFamilyNamesInvalidValue() {
        FontFamily.addFontFamilyName("Times New Roman");
        try {
            FontFamily.setValidateFontFamilyNameGlobally(true);
            String[] extractedFamilyNames = FontFamily.getExtractedFamilyNames("\"Times New Roman\", \"new family name\", Georgia, "
                    + GenericFontFamilyNameContants.SERIF, true);
        } catch (InvalidValueException e) {
            FontFamily.setValidateFontFamilyNameGlobally(false);
            throw e;
        }
    }
        
    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#setFontFamilyNames(java.lang.String[])}
     * .
     */
    @Test
    public void testSetFontFamilyNames() {
        FontFamily fontFamily = new FontFamily();
        assertEquals(0, fontFamily.getFontFamilyNames().length);
        String[] fontFamilyNames = { "Times New Roman", "Georgia",
                GenericFontFamilyNameContants.SERIF };
        fontFamily.setFontFamilyNames(fontFamilyNames);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals(fontFamilyNames[0], fontFamily.getFontFamilyNames()[0]);
        assertEquals(fontFamilyNames[1], fontFamily.getFontFamilyNames()[1]);
        assertEquals(fontFamilyNames[2], fontFamily.getFontFamilyNames()[2]);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#getFontFamilyNames()}.
     */
    @Test
    public void testGetFontFamilyNames() {
        FontFamily fontFamily = new FontFamily();
        assertEquals(0, fontFamily.getFontFamilyNames().length);
        String[] fontFamilyNames = { "Times New Roman", "Georgia" };
        
        fontFamily.setFontFamilyNames(fontFamilyNames);
        
        fontFamilyNames = new String[]{"Times New Roman", "Georgia",
                GenericFontFamilyNameContants.SERIF };
        
        fontFamily.setFontFamilyNames(fontFamilyNames);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals(fontFamilyNames[0], fontFamily.getFontFamilyNames()[0]);
        assertEquals(fontFamilyNames[1], fontFamily.getFontFamilyNames()[1]);
        assertEquals(fontFamilyNames[2], fontFamily.getFontFamilyNames()[2]);
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        FontFamily fontFamily = new FontFamily("\"Times New Roman\", Georgia, "
                + GenericFontFamilyNameContants.SERIF);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals("Times New Roman", fontFamily.getFontFamilyNames()[0]);
        assertEquals("Georgia", fontFamily.getFontFamilyNames()[1]);
        assertEquals(GenericFontFamilyNameContants.SERIF,
                fontFamily.getFontFamilyNames()[2]);
        
        fontFamily.setAsInitial();
        
        assertEquals(0, fontFamily.getFontFamilyNames().length);
        assertEquals(FontFamily.INITIAL, fontFamily.getCssValue());    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.FontFamily#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        FontFamily fontFamily = new FontFamily("\"Times New Roman\", Georgia, "
                + GenericFontFamilyNameContants.SERIF);
        assertEquals("\"Times New Roman\", Georgia, serif",
                fontFamily.getCssValue());
        
        assertEquals(3, fontFamily.getFontFamilyNames().length);
        assertEquals("Times New Roman", fontFamily.getFontFamilyNames()[0]);
        assertEquals("Georgia", fontFamily.getFontFamilyNames()[1]);
        assertEquals(GenericFontFamilyNameContants.SERIF,
                fontFamily.getFontFamilyNames()[2]);
        
        fontFamily.setAsInherit();
        
        assertEquals(0, fontFamily.getFontFamilyNames().length);
        assertEquals(FontFamily.INHERIT, fontFamily.getCssValue());
    }

}
