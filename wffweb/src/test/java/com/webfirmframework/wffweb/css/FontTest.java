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

import static org.junit.Assert.*;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class FontTest {

    @Test
    public void testFont() {
        Font font = new Font();
        assertEquals(Font.INITIAL, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getFontFamily());
    }

    @Test
    public void testFontString() {
        Font font = new Font("italic small-caps 17.0px");
        assertEquals("italic small-caps 17.0px", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals("17.0px", font.getFontSize().getCssValue());
    }

    @Test
    public void testFontFont() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif" );
        Font font1 = new Font(font);
        
        assertEquals("italic small-caps bold 12px/1.4 arial,sans-serif", font1.getCssValue());
        assertEquals(FontStyle.ITALIC, font1.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font1.getFontVariant());
        assertEquals("bold", font1.getFontWeight().getCssValue());
        assertEquals("12px", font1.getFontSize().getCssValue());
        assertEquals("1.4", font1.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font1.getFontFamily().getCssValue());
        
        font1.setCssValue("small-caps bold 12px/1.4 arial,sans-serif");
        assertEquals("small-caps bold 12px/1.4 arial,sans-serif", font1.getCssValue());
        assertNull(font1.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font1.getFontVariant());
        assertEquals("bold", font1.getFontWeight().getCssValue());
        assertEquals("12px", font1.getFontSize().getCssValue());
        assertEquals("1.4", font1.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font1.getFontFamily().getCssValue());
        
        font1.setCssValue("italic bold 12px/1.4 arial,sans-serif");
        assertEquals("italic bold 12px/1.4 arial,sans-serif", font1.getCssValue());
        assertEquals(FontStyle.ITALIC, font1.getFontStyle());
        assertNull(font1.getFontVariant());
        assertEquals("bold", font1.getFontWeight().getCssValue());
        assertEquals("12px", font1.getFontSize().getCssValue());
        assertEquals("1.4", font1.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font1.getFontFamily().getCssValue());
        
        font1.setCssValue("italic small-caps  12px/1.4 arial,sans-serif");
        assertEquals("italic small-caps 12px/1.4 arial,sans-serif", font1.getCssValue());
        assertEquals(FontStyle.ITALIC, font1.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font1.getFontVariant());
        assertNull(font1.getFontWeight());
        assertEquals("12px", font1.getFontSize().getCssValue());
        assertEquals("1.4", font1.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font1.getFontFamily().getCssValue());
        
        font1.setCssValue("italic small-caps bold 1.4 arial,sans-serif");
        assertEquals("italic small-caps bold 1.4 arial,sans-serif", font1.getCssValue());
        assertEquals(FontStyle.ITALIC, font1.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font1.getFontVariant());
        assertEquals("bold", font1.getFontWeight().getCssValue());
        assertNull(font1.getFontSize());
        assertEquals("1.4", font1.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font1.getFontFamily().getCssValue());
        
        font1.setCssValue("italic small-caps bold 12px arial,sans-serif");
        assertEquals("italic small-caps bold 12px arial,sans-serif", font1.getCssValue());
        assertEquals(FontStyle.ITALIC, font1.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font1.getFontVariant());
        assertEquals("bold", font1.getFontWeight().getCssValue());
        assertEquals("12px", font1.getFontSize().getCssValue());
        assertNull(font1.getLineHeight());
        assertEquals("arial,sans-serif", font1.getFontFamily().getCssValue());
        
        font1.setCssValue("italic small-caps bold 12px/1.4");
        assertEquals("italic small-caps bold 12px/1.4", font1.getCssValue());
        assertEquals(FontStyle.ITALIC, font1.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font1.getFontVariant());
        assertEquals("bold", font1.getFontWeight().getCssValue());
        assertEquals("12px", font1.getFontSize().getCssValue());
        assertEquals("1.4", font1.getLineHeight().getCssValue());
        assertNull(font1.getFontFamily());
    }

    @Test
    public void testGetCssName() {
        Font font = new Font();
        assertEquals(CssNameConstants.FONT, font.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Font font1 = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        
        Font font = new Font();
        assertEquals(Font.INITIAL, font.getCssValue());

        
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontWeight());
        assertNull(font.getFontSize());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        font.setFontStyle(font1.getFontStyle());
        
        assertEquals("italic", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontWeight());
        assertNull(font.getFontSize());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        font.setFontVariant(font1.getFontVariant());
        
        assertEquals("italic small-caps", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertNull(font.getFontWeight());
        assertNull(font.getFontSize());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        font.setFontWeight(font1.getFontWeight());
        
        assertEquals("italic small-caps bold", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals(FontWeight.BOLD, font.getFontWeight());
        assertNull(font.getFontSize());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        font.setFontSize(font1.getFontSize());
        
        assertEquals("italic small-caps bold 12px", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals(FontWeight.BOLD, font.getFontWeight());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        font.setLineHeight(font1.getLineHeight());
        
        assertEquals("italic small-caps bold 12px/1.4", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals(FontWeight.BOLD, font.getFontWeight());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertEquals("1.4", font.getLineHeight().getCssValue());
        assertNull(font.getFontFamily());
        
        font.setFontFamily(font1.getFontFamily());
        
        assertEquals("italic small-caps bold 12px/1.4 arial,sans-serif", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals(FontWeight.BOLD, font.getFontWeight());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertEquals("1.4", font.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font.getFontFamily().getCssValue());        
    }

    @Test
    public void testToString() {
        Font font = new Font("italic small-caps 17.0px");
        assertEquals(CssNameConstants.FONT + ": italic small-caps 17.0px",
                font.toString());
        
        font.setCssValue("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertEquals(CssNameConstants.FONT + ": italic small-caps bold 12px/1.4 arial,sans-serif",
                font.toString());
    }

    @Test
    public void testSetCssValueString() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        
        assertEquals("italic small-caps bold 12px/1.4 arial,sans-serif", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals("bold", font.getFontWeight().getCssValue());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertEquals("1.4", font.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font.getFontFamily().getCssValue());
        
        font.setCssValue("small-caps bold 12px/1.4 arial,sans-serif");
        assertEquals("small-caps bold 12px/1.4 arial,sans-serif", font.getCssValue());
        assertNull(font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals("bold", font.getFontWeight().getCssValue());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertEquals("1.4", font.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font.getFontFamily().getCssValue());
        
        font.setCssValue("italic bold 12px/1.4 arial,sans-serif");
        assertEquals("italic bold 12px/1.4 arial,sans-serif", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertNull(font.getFontVariant());
        assertEquals("bold", font.getFontWeight().getCssValue());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertEquals("1.4", font.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font.getFontFamily().getCssValue());
        
        font.setCssValue("italic small-caps  12px/1.4 arial,sans-serif");
        assertEquals("italic small-caps 12px/1.4 arial,sans-serif", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertNull(font.getFontWeight());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertEquals("1.4", font.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font.getFontFamily().getCssValue());
        
        font.setCssValue("italic small-caps bold 1.4 arial,sans-serif");
        assertEquals("italic small-caps bold 1.4 arial,sans-serif", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals("bold", font.getFontWeight().getCssValue());
        assertNull(font.getFontSize());
        assertEquals("1.4", font.getLineHeight().getCssValue());
        assertEquals("arial,sans-serif", font.getFontFamily().getCssValue());
        
        font.setCssValue("italic small-caps bold 12px arial,sans-serif");
        assertEquals("italic small-caps bold 12px arial,sans-serif", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals("bold", font.getFontWeight().getCssValue());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertNull(font.getLineHeight());
        assertEquals("arial,sans-serif", font.getFontFamily().getCssValue());
        
        font.setCssValue("italic small-caps bold 12px/1.4");
        assertEquals("italic small-caps bold 12px/1.4", font.getCssValue());
        assertEquals(FontStyle.ITALIC, font.getFontStyle());
        assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        assertEquals("bold", font.getFontWeight().getCssValue());
        assertEquals("12px", font.getFontSize().getCssValue());
        assertEquals("1.4", font.getLineHeight().getCssValue());
        assertNull(font.getFontFamily());
    }

    @Test
    public void testIsValid() {
        // isValid method is irrelevant.
        // assertTrue(Font.isValid("italic small-caps 17.0px"));
        //
        // assertTrue(Font.isValid("italic"));
        // assertTrue(Font.isValid("small-caps 17.0px"));
        // assertTrue(Font.isValid("17.0px"));
        // assertTrue(Font.isValid(FontStyle.ARMENIAN.getCssValue() + " "
        // + FontVariant.NORMAL.getCssValue() + " 17.0px"));
        // assertTrue(Font.isValid(FontStyle.CJK_IDEOGRAPHIC.getCssValue() + " "
        // + FontVariant.INITIAL.getCssValue() + " 17.0px"));
        //
        // assertFalse(Font.isValid("cir cle small-caps 17.0px"));
        // assertFalse(Font.isValid("dircle small-caps 17.0px"));
        // assertFalse(Font.isValid("italic ins ide 17.0px"));
        // assertFalse(Font.isValid("italic insside 17.0px"));
        // assertFalse(Font.isValid("italic small-caps ur l(Test.png)"));
        // assertFalse(Font.isValid("italic small-caps urll(Test.png)"));
        // fail("Not yet implemented");
    }

    @Test
    public void testSetAsInitial() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertNotNull(font.getFontStyle());
        final FontVariant fontVariant = font.getFontVariant();
        assertNotNull(fontVariant);
        final FontSize fontSize = font.getFontSize();
        assertNotNull(fontSize);
        font.setAsInitial();
        assertEquals(Font.INITIAL, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        assertFalse(fontSize.isAlreadyInUse());
    }

    @Test
    public void testSetAsInherit() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertNotNull(font.getFontStyle());
        final FontVariant fontVariant = font.getFontVariant();
        assertNotNull(fontVariant);
        final FontSize fontSize = font.getFontSize();
        assertNotNull(fontSize);
        font.setAsInherit();
        assertEquals(Font.INHERIT, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        assertFalse(fontSize.isAlreadyInUse());
    }
    
    @Test
    public void testSetAsCaption() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertNotNull(font.getFontStyle());
        final FontVariant fontVariant = font.getFontVariant();
        assertNotNull(fontVariant);
        final FontSize fontSize = font.getFontSize();
        assertNotNull(fontSize);
        font.setAsCaption();
        assertEquals(Font.CAPTION, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        assertFalse(fontSize.isAlreadyInUse());
    }
    
    @Test
    public void testSetAsIcon() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertNotNull(font.getFontStyle());
        final FontVariant fontVariant = font.getFontVariant();
        assertNotNull(fontVariant);
        final FontSize fontSize = font.getFontSize();
        assertNotNull(fontSize);
        font.setAsIcon();
        assertEquals(Font.ICON, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        assertFalse(fontSize.isAlreadyInUse());
    } 
    
    @Test
    public void testSetAsMenu() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertNotNull(font.getFontStyle());
        final FontVariant fontVariant = font.getFontVariant();
        assertNotNull(fontVariant);
        final FontSize fontSize = font.getFontSize();
        assertNotNull(fontSize);
        font.setAsMenu();
        assertEquals(Font.MENU, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        assertFalse(fontSize.isAlreadyInUse());
    } 
    
    @Test
    public void testSetAsMessageBox() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertNotNull(font.getFontStyle());
        final FontVariant fontVariant = font.getFontVariant();
        assertNotNull(fontVariant);
        final FontSize fontSize = font.getFontSize();
        assertNotNull(fontSize);
        font.setAsMessageBox();
        assertEquals(Font.MESSAGE_BOX, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());

        assertFalse(fontSize.isAlreadyInUse());
    } 
    
    @Test
    public void testSetAsSmallCaption() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertNotNull(font.getFontStyle());
        final FontVariant fontVariant = font.getFontVariant();
        assertNotNull(fontVariant);
        final FontSize fontSize = font.getFontSize();
        assertNotNull(fontSize);
        font.setAsSmallCaption();
        assertEquals(Font.SMALL_CAPTION, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        assertFalse(fontSize.isAlreadyInUse());
    } 
    
    @Test
    public void testSetAsStatusBar() {
        Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
        assertNotNull(font.getFontStyle());
        final FontVariant fontVariant = font.getFontVariant();
        assertNotNull(fontVariant);
        final FontSize fontSize = font.getFontSize();
        assertNotNull(fontSize);
        font.setAsStatusBar();
        assertEquals(Font.STATUS_BAR, font.getCssValue());
        assertNull(font.getFontStyle());
        assertNull(font.getFontVariant());
        assertNull(font.getFontSize());
        assertNull(font.getFontWeight());
        assertNull(font.getLineHeight());
        assertNull(font.getFontFamily());
        
        assertFalse(fontSize.isAlreadyInUse());
    } 

    @Test
    public void testGetFontSize() {
        try {
            Font font = new Font();
            font.setCssValue("italic small-caps 17.0px");
            assertEquals("italic small-caps 17.0px",
                    font.getCssValue());
            assertNotNull(font.getFontSize());
            assertEquals("17.0px", font.getFontSize().getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testGetFontVariant() {
        try {
            Font font = new Font();
            font.setCssValue("italic small-caps 17.0px");
            assertEquals("italic small-caps 17.0px",
                    font.getCssValue());
            assertNotNull(font.getFontVariant());
            assertEquals(FontVariant.SMALL_CAPS, font.getFontVariant());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testGetFontStyle() {
        try {
            Font font = new Font();
            font.setCssValue("italic small-caps 17.0px");
            assertEquals("italic small-caps 17.0px",
                    font.getCssValue());
            assertNotNull(font.getFontStyle());
            assertEquals(FontStyle.ITALIC, font.getFontStyle());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }

    }

    @Test
    public void testSetFontStyle() {
        try {
            Font font = new Font();
            font.setCssValue("italic small-caps 17.0px");
            font.setFontStyle(FontStyle.OBLIQUE);
            assertEquals("oblique small-caps 17.0px",
                    font.getCssValue());
            assertNotNull(font.getFontStyle());
            assertEquals(FontStyle.OBLIQUE, font.getFontStyle());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testSetFontVariant() {
        try {
            Font font = new Font();
            font.setCssValue("italic small-caps 17.0px");
            font.setFontStyle(FontStyle.OBLIQUE);
            font.setFontVariant(FontVariant.NORMAL);
            assertEquals("oblique normal 17.0px", font.getCssValue());
            assertNotNull(font.getFontVariant());
            assertEquals(FontVariant.NORMAL, font.getFontVariant());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testSetFontSize() {
        try {
            Font font = new Font();
            font.setCssValue("italic small-caps 55.0px");
            font.setFontSize(new FontSize().setCssValue("25px"));
            assertEquals("italic small-caps 25px", font.getCssValue());
            assertNotNull(font.getFontSize());
            assertEquals("25px", font.getFontSize().getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testName() throws Exception {
        // type position image
        String sample = "cjk-ideographic small-caps url(\"Sqpurple.gif\")";
        sample = sample.trim();
        final String[] sampleParts = sample.split(" ");

        FontStyle fontStyle = null;
        FontVariant fontVariant = null;
        FontSize fontSize = null;

        for (final String eachPart : sampleParts) {
            if (fontStyle == null && FontStyle.isValid(eachPart)) {
                fontStyle = FontStyle.getThis(eachPart);
                // System.out.println(fontStyle);
            } else if (fontVariant == null && FontVariant.isValid(eachPart)) {
                fontVariant = FontVariant.getThis(eachPart);
                // System.out.println(fontVariant);
            } else if (fontSize == null && FontSize.isValid(eachPart)) {
                fontSize = new FontSize(eachPart);
                // System.out.println(fontSize);
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        // boolean invalid = true;
        if (fontStyle != null) {
            cssValueBuilder.append(fontStyle.getCssValue());
            cssValueBuilder.append(" ");
            // invalid = false;
        }
        if (fontVariant != null) {
            cssValueBuilder.append(fontVariant.getCssValue());
            cssValueBuilder.append(" ");
            // invalid = false;
        }
        if (fontSize != null) {
            cssValueBuilder.append(fontSize.getCssValue());
            cssValueBuilder.append(" ");
            // invalid = false;
        }
        // System.out.println(invalid);
        // System.out.println(StringBuilderUtil.getTrimmedString(cssValueBuilder));
        // System.out.println(Font.isValid(sample));

        // System.out.println(part1);
        // System.out.println(part2);
        // System.out.println(part3);

    }

    @Test
    public void testFontNullValue() {
        try {
            Font font = new Font();
            font.setFontSize(null);
            font.setFontVariant(null);
            font.setFontStyle(null);
            assertEquals("inherit", font.getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test(expected = InvalidValueException.class)
    public void testFontInvalidValueValue() {
        //TODO invalid value case is irrelevant as the font family name can have any name (it is not predefined).
    }

    @Test(expected = InvalidValueException.class)
    public void testFontSetCssValueInvalidValueValue() {
        try {
            FontFamily.setValidateFontFamilyNameGlobally(true);
            new Font("disc normal initial");
        } catch (Exception e1) {
            try {
                new Font("disc normal inherit");
            } catch (Exception e2) {
                Font font = new Font();
                String cssValue = font.getCssValue();
                try {
                    font.setCssValue("disc normal inherit");
                } catch (Exception e) {
                    assertEquals(cssValue, font.getCssValue());
                    FontFamily.setValidateFontFamilyNameGlobally(false);
                    throw e;
                }
            }
        }
    }
    
    
    @Test
    public void testParsing() {
        //font-style: italic 
        //font-variant: small-caps 
        //font-weight: bold 
        //font-size/line-height: 12px/1.4 
        //font-family: arial,sans-serif
        
        //valid patterns
        //italic small-caps bold 12px/1.4 arial,sans-serif
        //small-caps bold 12px/1.4 arial,sans-serif
        //bold 12px/1.4 arial,sans-serif
        //12px/1.4 arial,sans-serif
        //12px arial,sans-serif
        

        //TODO
        //invalid patterns
        //arial,sans-serif
        //italic small-caps bold 12px/1.4
        //small-caps bold 12px/1.4
        //bold 12px/1.4
        //12px/1.4
        //12px

        
        //start valid patterns
        //italic small-caps bold 12px/1.4 arial,sans-serif
        {
            Font font = new Font("italic small-caps bold 12px/1.4 arial,sans-serif");
            final FontStyle fontStyle = font.getFontStyle();
            final FontVariant fontVariant = font.getFontVariant();
            final FontWeight fontWeight = font.getFontWeight();
            final FontSize fontSize = font.getFontSize();
            final LineHeight lineHeight = font.getLineHeight();
            final FontFamily fontFamily = font.getFontFamily();
            assertNotNull(fontStyle);
            assertNotNull(fontVariant);
            assertNotNull(fontWeight);
            assertNotNull(fontSize);
            assertNotNull(lineHeight);
            assertNotNull(fontFamily);
            
            assertEquals("italic", fontStyle.getCssValue());
            assertEquals("small-caps", fontVariant.getCssValue());
            assertEquals("bold", fontWeight.getCssValue());
            assertEquals("12px", fontSize.getCssValue());
            assertEquals("1.4", lineHeight.getCssValue());
            assertEquals("arial,sans-serif", fontFamily.getCssValue());
           
        }        
        //small-caps bold 12px/1.4 arial,sans-serif
        {
            Font font = new Font("small-caps bold 12px/1.4 arial,sans-serif");
            assertNull(font.getFontStyle());
            
            assertNotNull(font.getFontVariant());
            assertNotNull(font.getFontWeight());
            assertNotNull(font.getFontSize());
            assertNotNull(font.getLineHeight());
            assertNotNull(font.getFontFamily());
            
            final FontVariant fontVariant = font.getFontVariant();
            final FontWeight fontWeight = font.getFontWeight();
            final FontSize fontSize = font.getFontSize();
            final LineHeight lineHeight = font.getLineHeight();
            final FontFamily fontFamily = font.getFontFamily();
            
            assertEquals("small-caps", fontVariant.getCssValue());
            assertEquals("bold", fontWeight.getCssValue());
            assertEquals("12px", fontSize.getCssValue());
            assertEquals("1.4", lineHeight.getCssValue());
            assertEquals("arial,sans-serif", fontFamily.getCssValue());
        }
        //bold 12px/1.4 arial,sans-serif
        {
            Font font = new Font("bold 12px/1.4 arial,sans-serif");
            assertNull(font.getFontStyle());
            assertNull(font.getFontVariant());
            
            assertNotNull(font.getFontWeight());
            assertNotNull(font.getFontSize());
            assertNotNull(font.getLineHeight());
            assertNotNull(font.getFontFamily());
            
            final FontWeight fontWeight = font.getFontWeight();
            final FontSize fontSize = font.getFontSize();
            final LineHeight lineHeight = font.getLineHeight();
            final FontFamily fontFamily = font.getFontFamily();
            
            assertEquals("bold", fontWeight.getCssValue());
            assertEquals("12px", fontSize.getCssValue());
            assertEquals("1.4", lineHeight.getCssValue());
            assertEquals("arial,sans-serif", fontFamily.getCssValue());

        }
        
        //12px/1.4 arial,sans-serif
        {
            Font font = new Font("12px/1.4 arial,sans-serif");
            assertNull(font.getFontStyle());
            assertNull(font.getFontVariant());
            assertNull(font.getFontWeight());
            
            assertNotNull(font.getFontSize());
            assertNotNull(font.getLineHeight());
            assertNotNull(font.getFontFamily());
            
            final FontSize fontSize = font.getFontSize();
            final LineHeight lineHeight = font.getLineHeight();
            final FontFamily fontFamily = font.getFontFamily();
            
            assertEquals("12px", fontSize.getCssValue());
            assertEquals("1.4", lineHeight.getCssValue());
            assertEquals("arial,sans-serif", fontFamily.getCssValue());

        }
        
        //12px arial,sans-serif
        {
            Font font = new Font("12px arial, sans-serif");
            assertNull(font.getFontStyle());
            assertNull(font.getFontVariant());
            assertNull(font.getFontWeight());
            assertNull(font.getLineHeight());
            
            assertNotNull(font.getFontSize());
            assertNotNull(font.getFontFamily());
            
            final FontSize fontSize = font.getFontSize();
            final FontFamily fontFamily = font.getFontFamily();
            
            assertEquals("12px", fontSize.getCssValue());
            assertEquals("arial,sans-serif", fontFamily.getCssValue());

        }

        //stop valid patterns
        
        //start invalid patterns
        //arial,sans-serif
        {
            //TODO review and modify impl of Font
            Font font = new Font("arial,sans-serif");
            assertNull(font.getFontStyle());
            assertNull(font.getFontVariant());
            assertNull(font.getFontWeight());
            assertNull(font.getFontSize());
            assertNull(font.getLineHeight());
            
            assertNotNull(font.getFontFamily());
            
            final FontFamily fontFamily = font.getFontFamily();           
            
            assertEquals("arial,sans-serif", fontFamily.getCssValue());
        }
        
        
        
     
        
    } 
    
    @Test
    public void testFontInStyle() {
        Style stle = new Style(
                "font:italic small-caps bold 12px/1.4 arial,sans-serif");
        final CssProperty cssProperty = stle.getCssProperty("font");

        assertEquals("italic small-caps bold 12px/1.4 arial,sans-serif",
                cssProperty.getCssValue());
        assertEquals("font", cssProperty.getCssName());
        assertEquals(Font.class, cssProperty.getClass());

    }

}
