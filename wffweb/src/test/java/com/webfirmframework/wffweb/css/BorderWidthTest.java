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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class BorderWidthTest {

    @Test
    public void testBorderWidth() {
        BorderWidth borderWidth = new BorderWidth();
        assertEquals(BorderWidth.MEDIUM, borderWidth.getCssValue());
    }

    @Test
    public void testBorderWidthString() {
        BorderWidth borderWidth = new BorderWidth("75px");
        assertEquals("75px", borderWidth.getCssValue());   
    }

    @Test
    public void testBorderWidthBorderWidth() {
        BorderWidth borderWidth = new BorderWidth("75px");
        BorderWidth borderWidth1 = new BorderWidth(borderWidth);
        assertEquals("75px", borderWidth1.getCssValue());
    }

    @Test
    public void testSetCssValue() {
        BorderWidth borderWidth = new BorderWidth();
        borderWidth.setCssValue("75px");
        assertEquals("75px", borderWidth.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        BorderWidth borderWidth = new BorderWidth();
        assertEquals(CssNameConstants.BORDER_WIDTH, borderWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderWidth borderWidth = new BorderWidth("75px");
        assertEquals("75px", borderWidth.getCssValue());   
    }

    @Test
    public void testToString() {
        BorderWidth borderWidth = new BorderWidth("75px");
        assertEquals(CssNameConstants.BORDER_WIDTH + ": 75px",
                borderWidth.toString());
    }

    @Test
    public void testSetCssValueString() {
        BorderWidth borderWidth = new BorderWidth();
        borderWidth.setCssValue("75px");
        assertEquals("75px", borderWidth.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        BorderWidth borderWidth = new BorderWidth();
        borderWidth.setAsInitial();
        assertEquals(BorderWidth.INITIAL, borderWidth.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        BorderWidth borderWidth = new BorderWidth();
        borderWidth.setAsInherit();
        assertEquals(BorderWidth.INHERIT, borderWidth.getCssValue());
    }
    
    @Test
    public void testSetAsTransparent() {
        BorderWidth borderWidth = new BorderWidth();
        borderWidth.setAsInitial();
        assertEquals(BorderWidth.INITIAL, borderWidth.getCssValue());   
    }
    
    @Test(expected = NullValueException.class)
    public void testSetBorderWidthForNullValues() {
        BorderWidth borderWidth = new BorderWidth();
        
        BorderTopWidth borderTopWidth = new BorderTopWidth("25px");
        BorderRightWidth borderRightWidth = new BorderRightWidth("45px");
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth("85px");
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth("155px");
        
        
        try {
            borderWidth.setBorderWidth(null, null, null, null);   
        } catch (NullValueException e) {
            try {
                borderWidth.setBorderWidth(null, borderRightWidth, borderBottomWidth, borderLeftWidth);
            } catch (NullValueException e2) {
                try {
                    borderWidth.setBorderWidth(borderTopWidth, null, borderBottomWidth, borderLeftWidth);
                } catch (NullValueException e3) {
                    try {
                        borderWidth.setBorderWidth(borderTopWidth, borderRightWidth, null, borderLeftWidth);
                    } catch (NullValueException e4) {
                        try {
                            borderWidth.setBorderWidth(borderTopWidth, borderRightWidth, borderBottomWidth, null);
                        } catch (NullValueException e5) {
                            throw e4;
                        }  
                    }   
                }   
            }
        }

    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetBorderWidthForInvalidValues() {
        BorderWidth borderWidth = new BorderWidth();
        
        BorderTopWidth borderTopWidth = new BorderTopWidth("25px");
        BorderRightWidth borderRightWidth = new BorderRightWidth("45px");
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth("85px");
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth("155px");
        
        try {
            borderTopWidth.setAsInitial();
            borderWidth.setBorderWidth(borderTopWidth, borderRightWidth, borderBottomWidth, borderLeftWidth);
        } catch (InvalidValueException e) {
            try {
                borderRightWidth.setAsInitial();
                borderWidth.setBorderWidth(borderTopWidth, borderRightWidth, borderBottomWidth, borderLeftWidth);
            } catch (InvalidValueException e2) {
                try {
                    borderBottomWidth.setAsInitial();
                    borderWidth.setBorderWidth(borderTopWidth, borderRightWidth, borderBottomWidth, borderLeftWidth);
                } catch (InvalidValueException e3) {
                    try {
                        borderLeftWidth.setAsInitial();
                        borderWidth.setBorderWidth(borderTopWidth, borderRightWidth, borderBottomWidth, borderLeftWidth);
                    } catch (InvalidValueException e4) {
                        throw e4;
                    }   
                }   
            }
        }
        
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetBorderWidthForRuntimeInvalidValues() {

        BorderWidth borderWidth = new BorderWidth();
        BorderTopWidth borderTopWidth = new BorderTopWidth("75px");
        BorderRightWidth borderRightWidth = new BorderRightWidth("25px");
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth("45px");
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth("155px");
        borderWidth.setBorderWidth(borderTopWidth, borderRightWidth, borderBottomWidth, borderLeftWidth);   
        
        try {
            borderTopWidth.setAsInherit();
        } catch (InvalidValueException e) {
            try {
                borderRightWidth.setAsInherit();
            } catch (InvalidValueException e2) {
                try {
                    borderBottomWidth.setAsInherit();
                } catch (InvalidValueException e3) {
                    try {
                        borderLeftWidth.setAsInherit();
                    } catch (InvalidValueException e4) {
                        throw e4;
                    }   
                }   
            }
        }
        
    }
    
    @Test
    public void testSetBorderWidthForRuntimeInvalidValuesWithRollback() {
        
        BorderWidth borderWidth = new BorderWidth();
        BorderTopWidth borderTopWidth = new BorderTopWidth("75px");
        BorderRightWidth borderRightWidth = new BorderRightWidth("25px");
        BorderBottomWidth borderBottomWidth = new BorderBottomWidth("45px");
        BorderLeftWidth borderLeftWidth = new BorderLeftWidth("155px");
        borderWidth.setBorderWidth(borderTopWidth, borderRightWidth, borderBottomWidth, borderLeftWidth);   
        
        {
            try {
                borderTopWidth.setAsInitial();
                org.junit.Assert.fail();
            } catch (InvalidValueException e) {
                assertEquals("75px", borderTopWidth.getCssValue());
            }
            try {
                borderRightWidth.setAsInitial();
                org.junit.Assert.fail();
            } catch (InvalidValueException e2) {
                assertEquals("25px", borderRightWidth.getCssValue());

            }
            try {
                borderBottomWidth.setAsInitial();
                org.junit.Assert.fail();
            } catch (InvalidValueException e3) {
                assertEquals("45px", borderBottomWidth.getCssValue());
            }   
            try {
                borderLeftWidth.setAsInitial();
                org.junit.Assert.fail();
            } catch (InvalidValueException e4) {
                assertEquals("155px", borderLeftWidth.getCssValue());
            }   
        }
        {
            try {
                borderTopWidth.setCssValue("inherit");
                org.junit.Assert.fail();
            } catch (InvalidValueException e) {
                assertEquals("75px", borderTopWidth.getCssValue());
            }
            try {
                borderRightWidth.setCssValue("inherit");
                org.junit.Assert.fail();
            } catch (InvalidValueException e2) {
                assertEquals("25px", borderRightWidth.getCssValue());
            }
            try {
                borderBottomWidth.setCssValue("inherit");
                org.junit.Assert.fail();
            } catch (InvalidValueException e3) {
                assertEquals("45px", borderBottomWidth.getCssValue());
            }   
            try {
                borderLeftWidth.setCssValue("inherit");
                org.junit.Assert.fail();
            } catch (InvalidValueException e4) {
                assertEquals("155px", borderLeftWidth.getCssValue());
            }   
        }
    }
    
    @Test
    public void testGetBorderWidth() {
        {
            BorderWidth borderWidth = new BorderWidth("25px 45px 85px 155px");
            
            assertEquals("25px 45px 85px 155px", borderWidth.getCssValue());
            
            final BorderTopWidth borderTopWidth = borderWidth.getBorderTopWidth();
            final BorderRightWidth borderRightWidth = borderWidth.getBorderRightWidth();
            final BorderBottomWidth borderBottomWidth = borderWidth.getBorderBottomWidth();
            final BorderLeftWidth borderLeftWidth = borderWidth.getBorderLeftWidth();
            
            assertNotNull(borderTopWidth);
            assertNotNull(borderRightWidth);
            assertNotNull(borderBottomWidth);
            assertNotNull(borderLeftWidth);
            
            assertTrue(borderTopWidth.isAlreadyInUse());
            assertTrue(borderRightWidth.isAlreadyInUse());
            assertTrue(borderBottomWidth.isAlreadyInUse());
            assertTrue(borderLeftWidth.isAlreadyInUse());
            
            assertEquals("25px", borderTopWidth.getCssValue());
            assertEquals("45px", borderRightWidth.getCssValue());
            assertEquals("85px", borderBottomWidth.getCssValue());
            assertEquals("155px", borderLeftWidth.getCssValue());
            assertEquals("25px 45px 85px 155px", borderWidth.getCssValue());
            
            borderTopWidth.setCssValue("155px");
            assertEquals("155px 45px 85px 155px", borderWidth.getCssValue());
            
            borderRightWidth.setCssValue("175px");
            assertEquals("155px 175px 85px 155px", borderWidth.getCssValue());
            
            borderBottomWidth.setCssValue("215px");
            assertEquals("155px 175px 215px 155px", borderWidth.getCssValue());
            
            borderLeftWidth.setCssValue("25px");
            assertEquals("155px 175px 215px 25px", borderWidth.getCssValue());
            
            
            borderWidth.setCssValue("25px 45px 85px");
            assertEquals("25px", borderTopWidth.getCssValue());
            assertEquals("45px", borderRightWidth.getCssValue());
            assertEquals("85px", borderBottomWidth.getCssValue());
            assertEquals("45px", borderLeftWidth.getCssValue());
            
            borderWidth.setCssValue("45px 85px");
            assertEquals("45px", borderTopWidth.getCssValue());
            assertEquals("85px", borderRightWidth.getCssValue());
            assertEquals("45px", borderBottomWidth.getCssValue());
            assertEquals("85px", borderLeftWidth.getCssValue());
            
            borderWidth.setCssValue("45px");
            assertEquals("45px", borderTopWidth.getCssValue());
            assertEquals("45px", borderRightWidth.getCssValue());
            assertEquals("45px", borderBottomWidth.getCssValue());
            assertEquals("45px", borderLeftWidth.getCssValue());
            
            borderTopWidth.setCssValue("5px");
            borderBottomWidth.setCssValue("5px");
            
            borderRightWidth.setCssValue("85px");
            borderLeftWidth.setCssValue("85px");
            
            assertEquals("5px 85px", borderWidth.getCssValue());
            
            borderTopWidth.setCssValue("25px");
            borderBottomWidth.setCssValue("45px");
            
            borderRightWidth.setCssValue("85px");
            borderLeftWidth.setCssValue("85px");
            
            assertEquals("25px 85px 45px", borderWidth.getCssValue());
            
            borderTopWidth.setCssValue("5px");
            borderBottomWidth.setCssValue("5px");
            
            borderRightWidth.setCssValue("5px");
            borderLeftWidth.setCssValue("5px");
            
            assertEquals("5px", borderWidth.getCssValue());
            
            borderWidth.setAsInherit();
            assertNull(borderWidth.getBorderTopWidth());
            assertNull(borderWidth.getBorderRightWidth());
            assertNull(borderWidth.getBorderBottomWidth());
            assertNull(borderWidth.getBorderLeftWidth());
            
            assertFalse(borderTopWidth.isAlreadyInUse());
            assertFalse(borderRightWidth.isAlreadyInUse());
            assertFalse(borderBottomWidth.isAlreadyInUse());
            assertFalse(borderLeftWidth.isAlreadyInUse());
            
            
        }
    }
    
    
    
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = BorderWidth.isValid("45px");
            assertTrue(valid);
            final boolean invalid = BorderWidth.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderWidth.isValid("45em 45px");
            assertTrue(valid);
            final boolean invalid = BorderWidth.isValid("45em dfd 45px");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderWidth.isValid("45%");
            assertTrue(valid);
            final boolean invalid = BorderWidth.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderWidth.isValid("45em");
            assertTrue(valid);
            final boolean invalid = BorderWidth.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderWidth.isValid("45rem");
            assertTrue(valid);
        }
    }
    
    @Test
    public void testSetBorderWidthAlreadyInUse() throws Exception {
        
        BorderWidth borderWidth = new BorderWidth("25px 45px 85px 155px");
        final BorderTopWidth borderTopWidth = borderWidth.getBorderTopWidth();
        final BorderRightWidth borderRightWidth = borderWidth.getBorderRightWidth();
        final BorderBottomWidth borderBottomWidth = borderWidth.getBorderBottomWidth();
        final BorderLeftWidth borderLeftWidth = borderWidth.getBorderLeftWidth();
        
        assertTrue(borderTopWidth.isAlreadyInUse());
        assertTrue(borderRightWidth.isAlreadyInUse());
        assertTrue(borderBottomWidth.isAlreadyInUse());
        assertTrue(borderLeftWidth.isAlreadyInUse());
        
        final BorderTopWidth borderTopWidth2 = new BorderTopWidth();
        final BorderRightWidth borderRightWidth2 = new BorderRightWidth();
        final BorderBottomWidth borderBottomWidth2 = new BorderBottomWidth();
        final BorderLeftWidth borderLeftWidth2 = new BorderLeftWidth();
        
        BorderWidth borderWidth2 = new BorderWidth();
        assertNull(borderWidth2.getBorderTopWidth());
        assertNull(borderWidth2.getBorderRightWidth());
        assertNull(borderWidth2.getBorderBottomWidth());
        assertNull(borderWidth2.getBorderLeftWidth());
        
        assertFalse(borderTopWidth2.isAlreadyInUse());
        assertFalse(borderRightWidth2.isAlreadyInUse());
        assertFalse(borderBottomWidth2.isAlreadyInUse());
        assertFalse(borderLeftWidth2.isAlreadyInUse());
        
        borderWidth2.setBorderWidth(borderTopWidth2, borderRightWidth2, borderBottomWidth2, borderLeftWidth2);
        
        assertTrue(borderTopWidth2.isAlreadyInUse());
        assertTrue(borderRightWidth2.isAlreadyInUse());
        assertTrue(borderBottomWidth2.isAlreadyInUse());
        assertTrue(borderLeftWidth2.isAlreadyInUse());
        
        assertEquals(borderTopWidth2, borderWidth2.getBorderTopWidth());
        assertEquals(borderRightWidth2, borderWidth2.getBorderRightWidth());
        assertEquals(borderBottomWidth2, borderWidth2.getBorderBottomWidth());
        assertEquals(borderLeftWidth2, borderWidth2.getBorderLeftWidth());
        
        assertEquals(borderTopWidth, borderWidth.getBorderTopWidth());
        assertEquals(borderRightWidth, borderWidth.getBorderRightWidth());
        assertEquals(borderBottomWidth, borderWidth.getBorderBottomWidth());
        assertEquals(borderLeftWidth, borderWidth.getBorderLeftWidth());
        
        borderWidth.setBorderWidth(borderTopWidth2, borderRightWidth2, borderBottomWidth2, borderLeftWidth2);
        
        assertNotEquals(borderTopWidth2, borderWidth.getBorderTopWidth());
        assertNotEquals(borderRightWidth2, borderWidth.getBorderRightWidth());
        assertNotEquals(borderBottomWidth2, borderWidth.getBorderBottomWidth());
        assertNotEquals(borderLeftWidth2, borderWidth.getBorderLeftWidth());
        
        assertTrue(borderTopWidth.isAlreadyInUse());
        assertTrue(borderRightWidth.isAlreadyInUse());
        assertTrue(borderBottomWidth.isAlreadyInUse());
        assertTrue(borderLeftWidth.isAlreadyInUse());
       
    }
    
}
