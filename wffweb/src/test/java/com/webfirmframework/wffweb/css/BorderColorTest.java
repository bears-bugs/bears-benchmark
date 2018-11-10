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
public class BorderColorTest {

    @Test
    public void testBorderColor() {
        BorderColor borderColor = new BorderColor();
        assertEquals(BorderColor.INITIAL, borderColor.getCssValue());
    }

    @Test
    public void testBorderColorString() {
        BorderColor borderColor = new BorderColor("#0000ff");
        assertEquals("#0000ff", borderColor.getCssValue());   
    }

    @Test
    public void testBorderColorBorderColor() {
        BorderColor borderColor = new BorderColor("#0000ff");
        BorderColor borderColor1 = new BorderColor(borderColor);
        assertEquals("#0000ff", borderColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        BorderColor borderColor = new BorderColor();
        borderColor.setValue("#0000ff");
        assertEquals("#0000ff", borderColor.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        BorderColor borderColor = new BorderColor();
        assertEquals(CssNameConstants.BORDER_COLOR, borderColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        BorderColor borderColor = new BorderColor("#0000ff");
        assertEquals("#0000ff", borderColor.getCssValue());   
    }

    @Test
    public void testToString() {
        BorderColor borderColor = new BorderColor("#0000ff");
        assertEquals(CssNameConstants.BORDER_COLOR + ": #0000ff",
                borderColor.toString());
    }

    @Test
    public void testGetValue() {
        BorderColor borderColor = new BorderColor("#0000ff");
        assertEquals("#0000ff", borderColor.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        BorderColor borderColor = new BorderColor();
        borderColor.setCssValue("#0000ff");
        assertEquals("#0000ff", borderColor.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        BorderColor borderColor = new BorderColor();
        borderColor.setAsInitial();
        assertEquals(BorderColor.INITIAL, borderColor.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        BorderColor borderColor = new BorderColor();
        borderColor.setAsInherit();
        assertEquals(BorderColor.INHERIT, borderColor.getCssValue());
    }
    
    @Test
    public void testSetAsTransparent() {
        BorderColor borderColor = new BorderColor();
        borderColor.setAsTransparent();
        assertEquals(BorderColor.TRANSPARENT, borderColor.getCssValue());   
    }
    
    @Test(expected = NullValueException.class)
    public void testSetBorderColorForNullValues() {
        BorderColor borderColor = new BorderColor();
        
        BorderTopColor borderTopColor = new BorderTopColor("red");
        BorderRightColor borderRightColor = new BorderRightColor("green");
        BorderBottomColor borderBottomColor = new BorderBottomColor("blue");
        BorderLeftColor borderLeftColor = new BorderLeftColor("yellow");
        
        
        try {
            borderColor.setBorderColor(null, null, null, null);   
        } catch (NullValueException e) {
            try {
                borderColor.setBorderColor(null, borderRightColor, borderBottomColor, borderLeftColor);
            } catch (NullValueException e2) {
                try {
                    borderColor.setBorderColor(borderTopColor, null, borderBottomColor, borderLeftColor);
                } catch (NullValueException e3) {
                    try {
                        borderColor.setBorderColor(borderTopColor, borderRightColor, null, borderLeftColor);
                    } catch (NullValueException e4) {
                        try {
                            borderColor.setBorderColor(borderTopColor, borderRightColor, borderBottomColor, null);
                        } catch (NullValueException e5) {
                            throw e4;
                        }  
                    }   
                }   
            }
        }

    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetBorderColorForInvalidValues() {
        BorderColor borderColor = new BorderColor();
        
        BorderTopColor borderTopColor = new BorderTopColor("red");
        BorderRightColor borderRightColor = new BorderRightColor("green");
        BorderBottomColor borderBottomColor = new BorderBottomColor("blue");
        BorderLeftColor borderLeftColor = new BorderLeftColor("yellow");
        
        try {
            borderTopColor.setAsInitial();
            borderColor.setBorderColor(borderTopColor, borderRightColor, borderBottomColor, borderLeftColor);
        } catch (InvalidValueException e) {
            try {
                borderRightColor.setAsInitial();
                borderColor.setBorderColor(borderTopColor, borderRightColor, borderBottomColor, borderLeftColor);
            } catch (InvalidValueException e2) {
                try {
                    borderBottomColor.setAsInitial();
                    borderColor.setBorderColor(borderTopColor, borderRightColor, borderBottomColor, borderLeftColor);
                } catch (InvalidValueException e3) {
                    try {
                        borderLeftColor.setAsInitial();
                        borderColor.setBorderColor(borderTopColor, borderRightColor, borderBottomColor, borderLeftColor);
                    } catch (InvalidValueException e4) {
                        throw e4;
                    }   
                }   
            }
        }
        
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetBorderColorForRuntimeInvalidValues() {

        BorderColor borderColor = new BorderColor();
        BorderTopColor borderTopColor = new BorderTopColor("orange");
        BorderRightColor borderRightColor = new BorderRightColor("red");
        BorderBottomColor borderBottomColor = new BorderBottomColor("green");
        BorderLeftColor borderLeftColor = new BorderLeftColor("yellow");
        borderColor.setBorderColor(borderTopColor, borderRightColor, borderBottomColor, borderLeftColor);   
        
        try {
            borderTopColor.setAsInherit();
        } catch (InvalidValueException e) {
            try {
                borderRightColor.setAsInherit();
            } catch (InvalidValueException e2) {
                try {
                    borderBottomColor.setAsInherit();
                } catch (InvalidValueException e3) {
                    try {
                        borderLeftColor.setAsInherit();
                    } catch (InvalidValueException e4) {
                        throw e4;
                    }   
                }   
            }
        }
        
    }
    
    @Test
    public void testSetBorderColorForRuntimeInvalidValuesWithRollback() {
        
        BorderColor borderColor = new BorderColor();
        BorderTopColor borderTopColor = new BorderTopColor("orange");
        BorderRightColor borderRightColor = new BorderRightColor("red");
        BorderBottomColor borderBottomColor = new BorderBottomColor("green");
        BorderLeftColor borderLeftColor = new BorderLeftColor("yellow");
        borderColor.setBorderColor(borderTopColor, borderRightColor, borderBottomColor, borderLeftColor);   
        
        {
            try {
                borderTopColor.setAsInitial();
            } catch (InvalidValueException e) {
                assertEquals("orange", borderTopColor.getCssValue());
            }
            try {
                borderRightColor.setAsInitial();
            } catch (InvalidValueException e2) {
                assertEquals("red", borderRightColor.getCssValue());
            }
            try {
                borderBottomColor.setAsInitial();
            } catch (InvalidValueException e3) {
                assertEquals("green", borderBottomColor.getCssValue());
            }   
            try {
                borderLeftColor.setAsInitial();
            } catch (InvalidValueException e4) {
                assertEquals("yellow", borderLeftColor.getCssValue());
            }   
        }
        {
            try {
                borderTopColor.setCssValue("inherit");
            } catch (InvalidValueException e) {
                assertEquals("orange", borderTopColor.getCssValue());
            }
            try {
                borderRightColor.setCssValue("inherit");
            } catch (InvalidValueException e2) {
                assertEquals("red", borderRightColor.getCssValue());
            }
            try {
                borderBottomColor.setCssValue("inherit");
            } catch (InvalidValueException e3) {
                assertEquals("green", borderBottomColor.getCssValue());
            }   
            try {
                borderLeftColor.setCssValue("inherit");
            } catch (InvalidValueException e4) {
                assertEquals("yellow", borderLeftColor.getCssValue());
            }   
        }
    }
    
    @Test
    public void testGetBorderColor() {
        {
            BorderColor borderColor = new BorderColor("red green blue yellow");
            
            assertEquals("red green blue yellow", borderColor.getCssValue());
            
            final BorderTopColor borderTopColor = borderColor.getBorderTopColor();
            final BorderRightColor borderRightColor = borderColor.getBorderRightColor();
            final BorderBottomColor borderBottomColor = borderColor.getBorderBottomColor();
            final BorderLeftColor borderLeftColor = borderColor.getBorderLeftColor();
            
            assertNotNull(borderTopColor);
            assertNotNull(borderRightColor);
            assertNotNull(borderBottomColor);
            assertNotNull(borderLeftColor);
            
            assertTrue(borderTopColor.isAlreadyInUse());
            assertTrue(borderRightColor.isAlreadyInUse());
            assertTrue(borderBottomColor.isAlreadyInUse());
            assertTrue(borderLeftColor.isAlreadyInUse());
            
            assertEquals("red", borderTopColor.getCssValue());
            assertEquals("green", borderRightColor.getCssValue());
            assertEquals("blue", borderBottomColor.getCssValue());
            assertEquals("yellow", borderLeftColor.getCssValue());
            assertEquals("red green blue yellow", borderColor.getCssValue());
            
            borderTopColor.setCssValue("yellow");
            assertEquals("yellow green blue yellow", borderColor.getCssValue());
            
            borderRightColor.setCssValue("black");
            assertEquals("yellow black blue yellow", borderColor.getCssValue());
            
            borderBottomColor.setCssValue("silver");
            assertEquals("yellow black silver yellow", borderColor.getCssValue());
            
            borderLeftColor.setCssValue("red");
            assertEquals("yellow black silver red", borderColor.getCssValue());
            
            
            borderColor.setCssValue("red green blue");
            assertEquals("red", borderTopColor.getCssValue());
            assertEquals("green", borderRightColor.getCssValue());
            assertEquals("blue", borderBottomColor.getCssValue());
            assertEquals("green", borderLeftColor.getCssValue());
            
            borderColor.setCssValue("green blue");
            assertEquals("green", borderTopColor.getCssValue());
            assertEquals("blue", borderRightColor.getCssValue());
            assertEquals("green", borderBottomColor.getCssValue());
            assertEquals("blue", borderLeftColor.getCssValue());
            
            borderColor.setCssValue("green");
            assertEquals("green", borderTopColor.getCssValue());
            assertEquals("green", borderRightColor.getCssValue());
            assertEquals("green", borderBottomColor.getCssValue());
            assertEquals("green", borderLeftColor.getCssValue());
            
            borderTopColor.setCssValue("white");
            borderBottomColor.setCssValue("white");
            
            borderRightColor.setCssValue("blue");
            borderLeftColor.setCssValue("blue");
            
            assertEquals("white blue", borderColor.getCssValue());
            
            borderTopColor.setCssValue("red");
            borderBottomColor.setCssValue("green");
            
            borderRightColor.setCssValue("blue");
            borderLeftColor.setCssValue("blue");
            
            assertEquals("red blue green", borderColor.getCssValue());
            
            borderTopColor.setCssValue("white");
            borderBottomColor.setCssValue("white");
            
            borderRightColor.setCssValue("white");
            borderLeftColor.setCssValue("white");
            
            assertEquals("white", borderColor.getCssValue());
            
            borderColor.setAsTransparent();
            assertNull(borderColor.getBorderTopColor());
            assertNull(borderColor.getBorderRightColor());
            assertNull(borderColor.getBorderBottomColor());
            assertNull(borderColor.getBorderLeftColor());
            
            assertFalse(borderTopColor.isAlreadyInUse());
            assertFalse(borderRightColor.isAlreadyInUse());
            assertFalse(borderBottomColor.isAlreadyInUse());
            assertFalse(borderLeftColor.isAlreadyInUse());
        }
    }
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = BorderColor.isValid("green");
            assertTrue(valid);
            final boolean invalid = BorderColor.isValid("blu");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderColor.isValid("GREEN");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderColor.isValid("WHITE");
            assertTrue(valid);
            final boolean invalid = BorderColor.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderColor.isValid("TRANSPARENT");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderColor.isValid("transparent");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green blue transparent");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green blue white");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green blue dfdf");
           assertFalse(valid);
        }
        {
            final boolean valid = BorderColor.isValid("dfdf red green blue");
           assertFalse(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red dfdf green blue");
           assertFalse(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green -dfdf blue");
           assertFalse(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green blue dfdf");
           assertFalse(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green blue initial");
           assertFalse(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green blue inherit");
           assertFalse(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green blue INITIAL");
           assertFalse(valid);
        }
        {
            final boolean valid = BorderColor.isValid("red green blue INHERIT");
           assertFalse(valid);
        }
    }
    
}
