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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;


/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class BorderTest {

    @Test
    public void testBorder() {
        Border border = new Border();
        assertEquals("medium none #000000", border.getCssValue());
        assertEquals(BorderWidth.MEDIUM, border.getBorderWidthValue());
        assertEquals(BorderStyle.NONE, border.getBorderStyle());
        assertEquals("#000000", border.getBorderColorCssValues().getValue());
    }
    
    @Test
    public void testBorderString() {
        Border border = new Border("55px solid coral");
        assertEquals("55px solid coral", border.getCssValue());
        assertEquals("55px", border.getBorderWidthValue());
        assertEquals(BorderStyle.SOLID, border.getBorderStyle());
        assertEquals(CssColorName.CORAL.toString(), border.getBorderColorCssValues().getValue());
    }

    @Test
    public void testBorderBorder() {
        Border border1 = new Border("55px solid coral");
        Border border = new Border(border1);
        assertEquals("55px solid coral", border.getCssValue());
        assertEquals("55px", border.getBorderWidthValue());
        assertEquals(BorderStyle.SOLID, border.getBorderStyle());
        assertEquals(CssColorName.CORAL.toString(), border.getBorderColorCssValues().getValue());
    }

    @Test
    public void testGetCssName() {
        Border border = new Border();
        assertEquals(CssNameConstants.BORDER, border.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Border border = new Border();
        border.setBorderWidthValue("55px");
        BorderColorCssValues borderColorCssValues = new BorderColorCssValues("white");
        border.setBorderColorCssValues(borderColorCssValues);
        border.setBorderStyle(BorderStyle.GROOVE);
        assertEquals("55px groove white", border.getCssValue());
        border.setCssValue("155px groove blue");
        assertEquals(borderColorCssValues, border.getBorderColorCssValues());
        assertEquals("blue", border.getBorderColorCssValues().getValue());
        assertEquals("155px", border.getBorderWidthValue());
    }

    @Test
    public void testToString() {
        Border border = new Border();
        border.setCssValue("155px groove blue");
        assertEquals("border: 155px groove blue", border.toString());
    }

    @Test
    public void testSetCssValueString() {
        {
            Border border = new Border();
            border.setCssValue("155px groove blue");
            assertEquals("155px groove blue", border.getCssValue());
            assertEquals("155px", border.getBorderWidthValue());
            assertEquals(BorderStyle.GROOVE, border.getBorderStyle());
            assertEquals("blue", border.getBorderColorCssValues().getValue());
        }
        {
            Border border = new Border();
            border.setCssValue("groove blue");
            assertEquals("groove blue", border.getCssValue());
            assertNull(border.getBorderWidthValue());
            assertEquals(BorderStyle.GROOVE, border.getBorderStyle());
            assertEquals("blue", border.getBorderColorCssValues().getValue());
        }
        {
            Border border = new Border();
            border.setCssValue("155px blue");
            assertEquals("155px blue", border.getCssValue());
            assertEquals("155px", border.getBorderWidthValue());
            assertNull(border.getBorderStyle());
        }

        {
            Border border = new Border();
            border.setCssValue("155px groove");
            assertEquals("155px groove", border.getCssValue());
            assertEquals("155px", border.getBorderWidthValue());
            assertEquals(BorderStyle.GROOVE, border.getBorderStyle());
            assertNull(border.getBorderColorCssValues());
        }
        
        {
            Border border = new Border();
            border.setCssValue("155px groove blue");
            assertEquals("155px groove blue", border.getCssValue());
            assertEquals("155px", border.getBorderWidthValue());
            assertEquals(BorderStyle.GROOVE, border.getBorderStyle());
            final BorderColorCssValues borderColor = border.getBorderColorCssValues();
            assertEquals("blue", borderColor.getValue());
            
            border.setBorderWidthValue(null);
            assertEquals("groove blue", border.getCssValue());
            
            border.setBorderStyle(null);
            assertEquals("blue", border.getCssValue());
            
            border.setBorderColorCssValues(null);
            assertEquals("inherit", border.getCssValue());
            
            assertFalse(borderColor.isAlreadyInUse());
        }

    }

    @Test
    public void testIsValid() {
        assertTrue(Border.isValid("55px solid green"));

        assertTrue(Border.isValid("55px"));
        assertTrue(Border.isValid("solid"));
        assertTrue(Border.isValid("green"));
        assertTrue(Border.isValid(BorderWidth.MEDIUM + " "
                + BorderStyle.DASHED.getCssValue() + " " + CssColorName.AZURE));

        assertTrue(Border.isValid(BorderWidth.THICK + " "
                + BorderStyle.DOUBLE.getCssValue() + " " + CssColorName.AZURE));

        assertFalse(Border.isValid("cir cle inside url(Test.png)"));
        assertFalse(Border.isValid("dmedium dfdf dfdfd"));
        assertFalse(Border.isValid("medium medium medium "));
        assertFalse(Border.isValid("55 solid green"));
        assertFalse(Border.isValid("solid solid solid"));
        assertFalse(Border.isValid("55px solid green green"));
    }

    @Test
    public void testSetAsInitial() {
        Border border = new Border();
        assertNotNull(border.getBorderWidthValue());
        final BorderStyle borderPosition = border.getBorderStyle();
        assertNotNull(borderPosition);
        final BorderColorCssValues borderColor = border.getBorderColorCssValues();
        assertNotNull(borderColor);
        border.setAsInitial();
        assertEquals(Border.INITIAL, border.getCssValue());
        assertNull(border.getBorderWidthValue());
        assertNull(border.getBorderStyle());
        assertNull(border.getBorderColorCssValues());
        assertFalse(borderColor.isAlreadyInUse());
    }

    @Test
    public void testSetAsInherit() {
        Border border = new Border();
        border.setAsInherit();
        assertEquals(Border.INHERIT, border.getCssValue());
    }

    @Test
    public void testGetBorderColor() {
        try {
            Border border = new Border();
            border.setCssValue("thick " + BorderStyle.DOUBLE.getCssValue()
                    + " " + CssColorName.BEIGE);
            assertEquals("thick " + BorderStyle.DOUBLE.getCssValue() + " "
                    + CssColorName.BEIGE, border.getCssValue());
            assertNotNull(border.getBorderColorCssValues());
            assertEquals(CssColorName.BEIGE.toString(), border.getBorderColorCssValues().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testGetBorderStyle() {
        Border border = new Border();
        border.setCssValue("thick ridge blue");
        assertEquals("thick ridge blue", border.getCssValue());
        assertNotNull(border.getBorderStyle());
        assertEquals(BorderStyle.RIDGE, border.getBorderStyle());
    }

    @Test
    public void testGetBorderWidthValue() {
        Border border = new Border();
        border.setCssValue("thick ridge blue");
        assertEquals("thick ridge blue", border.getCssValue());
        assertNotNull(border.getBorderWidthValue());
        assertEquals("thick", border.getBorderWidthValue());
    }

    @Test
    public void testSetBorderWidthValue() {
        Border border = new Border();
        border.setCssValue("thin ridge blue");
        border.setBorderWidthValue("thick");
        assertEquals("thick ridge blue", border.getCssValue());
        assertNotNull(border.getBorderWidthValue());
        assertEquals("thick", border.getBorderWidthValue());
    }

    @Test
    public void testSetBorderStyle() {
        Border border = new Border();
        border.setCssValue("thin ridge blue");
        border.setBorderWidthValue("thick");
        border.setBorderStyle(BorderStyle.DOUBLE);
        assertEquals("thick double blue", border.getCssValue());
        assertNotNull(border.getBorderStyle());
        assertEquals(BorderStyle.DOUBLE, border.getBorderStyle());
    }

    @Test
    public void testSetBorderColor() {
        Border border = new Border();
        border.setCssValue("thin ridge green");
        border.setBorderColorCssValues(new BorderColorCssValues("blue"));
        assertEquals("thin ridge blue", border.getCssValue());
        assertNotNull(border.getBorderColorCssValues());
        assertEquals("blue", border.getBorderColorCssValues().getValue());
    }
    

    
    @Test
    public void testBorderNullValue() {
        Border border = new Border();
        border.setBorderColorCssValues(null);
        border.setBorderStyle(null);
        border.setBorderWidthValue(null);
        assertEquals("inherit", border.getCssValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testBorderInvalidValueValue() {

        Border border = new Border("inherit");
        final String borderWidthValue = border.getBorderWidthValue();
        
        final BorderColorCssValues borderColor = border.getBorderColorCssValues();

        assertNull(borderColor);
        
        assertNull(borderWidthValue);
        
        assertEquals("inherit", border.getCssValue());
        
        final BorderStyle borderPosition = border
                .getBorderStyle();
        
        assertNull(borderPosition);
        
        assertEquals("inherit", border.getCssValue());

        try {
            border.setBorderStyle(BorderStyle.INHERIT );
        } catch (InvalidValueException e) {
            try {
                border.setBorderStyle(BorderStyle.INITIAL );
            } catch (InvalidValueException e2) {
                throw e2;
            }
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testBorderSetCssValueInvalidValueValue() {
        try {
            new Border("155px groove initial");
        } catch (Exception e1) {
            try {
                new Border("155px groove inherit");
            } catch (Exception e2) {
                Border border = new Border();
                try {
                    border.setCssValue("155px groove inherit");
                } catch (Exception e) {
                    assertEquals("medium none #000000", border.getCssValue());
                    throw e;
                }
            }
        }
    }

}
