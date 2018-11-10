/*
 * Copyright 2014-2018 Web Firm Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless requi2px by applicable law or agreed to in writing, software
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
public class PaddingTest {

    @Test
    public void testPadding() {
        Padding padding = new Padding();
        assertEquals(Padding.INITIAL, padding.getCssValue());
    }

    @Test
    public void testPaddingString() {
        Padding padding = new Padding("2cm 4cm 3cm 4cm");
        assertEquals("2cm 4cm 3cm 4cm", padding.getCssValue());   
    }

    @Test
    public void testPaddingPadding() {
        Padding padding = new Padding("9px");
        Padding padding1 = new Padding(padding);
        assertEquals("9px", padding1.getCssValue());
    }

    @Test
    public void testSetValue() {
        Padding padding = new Padding();
        padding.setValue("9px");
        assertEquals("9px", padding.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        Padding padding = new Padding();
        assertEquals(CssNameConstants.PADDING, padding.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Padding padding = new Padding("9px");
        assertEquals("9px", padding.getCssValue());   
    }

    @Test
    public void testToString() {
        Padding padding = new Padding("2px");
        assertEquals(CssNameConstants.PADDING + ": 2px",
                padding.toString());
    }

    @Test
    public void testGetValue() {
        Padding padding = new Padding("2cm 4cm 3cm 4cm");
        assertEquals("2cm 4cm 3cm 4cm", padding.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        Padding padding = new Padding();
        padding.setCssValue("9px");
        assertEquals("9px", padding.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        Padding padding = new Padding();
        padding.setAsInitial();
        assertEquals(Padding.INITIAL, padding.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        Padding padding = new Padding();
        padding.setAsInherit();
        assertEquals(Padding.INHERIT, padding.getCssValue());
    }
    
    
    @Test(expected = NullValueException.class)
    public void testSetPaddingForNullValues() {
        Padding padding = new Padding();
        
        PaddingTop paddingTop = new PaddingTop("55px");
        PaddingRight paddingRight = new PaddingRight("65px");
        PaddingBottom paddingBottom = new PaddingBottom("75px");
        PaddingLeft paddingLeft = new PaddingLeft("85px");
        
        
        try {
            padding.setPadding(null, null, null, null);   
        } catch (NullValueException e) {
            try {
                padding.setPadding(null, paddingRight, paddingBottom, paddingLeft);
            } catch (NullValueException e2) {
                try {
                    padding.setPadding(paddingTop, null, paddingBottom, paddingLeft);
                } catch (NullValueException e3) {
                    try {
                        padding.setPadding(paddingTop, paddingRight, null, paddingLeft);
                    } catch (NullValueException e4) {
                        try {
                            padding.setPadding(paddingTop, paddingRight, paddingBottom, null);
                        } catch (NullValueException e5) {
                            throw e4;
                        }  
                    }   
                }   
            }
        }

    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetPaddingForInvalidValues() {
        Padding padding = new Padding();
        
        PaddingTop paddingTop = new PaddingTop("2px");
        PaddingRight paddingRight = new PaddingRight("3px");
        PaddingBottom paddingBottom = new PaddingBottom("7px");
        PaddingLeft paddingLeft = new PaddingLeft("4px");
        
        try {
            paddingTop.setAsInitial();
            padding.setPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);
        } catch (InvalidValueException e) {
            try {
                paddingRight.setAsInitial();
                padding.setPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);
            } catch (InvalidValueException e2) {
                try {
                    paddingBottom.setAsInitial();
                    padding.setPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);
                } catch (InvalidValueException e3) {
                    try {
                        paddingLeft.setAsInitial();
                        padding.setPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);
                    } catch (InvalidValueException e4) {
                        throw e4;
                    }   
                }   
            }
        }
        
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetPaddingForRuntimeInvalidValues() {

        Padding padding = new Padding();
        PaddingTop paddingTop = new PaddingTop("1px");
        PaddingRight paddingRight = new PaddingRight("2px");
        PaddingBottom paddingBottom = new PaddingBottom("3px");
        PaddingLeft paddingLeft = new PaddingLeft("4px");
        padding.setPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);   
        
        try {
            paddingTop.setAsInherit();
        } catch (InvalidValueException e) {
            try {
                paddingRight.setAsInherit();
            } catch (InvalidValueException e2) {
                try {
                    paddingBottom.setAsInherit();
                } catch (InvalidValueException e3) {
                    try {
                        paddingLeft.setAsInherit();
                    } catch (InvalidValueException e4) {
                        throw e4;
                    }   
                }   
            }
        }
        
    }
    
    @Test
    public void testSetPaddingForRuntimeInvalidValuesWithRollback() {
        
        Padding padding = new Padding();
        PaddingTop paddingTop = new PaddingTop("1px");
        PaddingRight paddingRight = new PaddingRight("2px");
        PaddingBottom paddingBottom = new PaddingBottom("3px");
        PaddingLeft paddingLeft = new PaddingLeft("4px");
        padding.setPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);   
        
        {
            try {
                paddingTop.setAsInitial();
            } catch (InvalidValueException e) {
                assertEquals("1px", paddingTop.getCssValue());
            }
            try {
                paddingRight.setAsInitial();
            } catch (InvalidValueException e2) {
                assertEquals("2px", paddingRight.getCssValue());
            }
            try {
                paddingBottom.setAsInitial();
            } catch (InvalidValueException e3) {
                assertEquals("3px", paddingBottom.getCssValue());
            }   
            try {
                paddingLeft.setAsInitial();
            } catch (InvalidValueException e4) {
                assertEquals("4px", paddingLeft.getCssValue());
            }   
        }
        {
            try {
                paddingTop.setCssValue("inherit");
            } catch (InvalidValueException e) {
                assertEquals("1px", paddingTop.getCssValue());
            }
            try {
                paddingRight.setCssValue("inherit");
            } catch (InvalidValueException e2) {
                assertEquals("2px", paddingRight.getCssValue());
            }
            try {
                paddingBottom.setCssValue("inherit");
            } catch (InvalidValueException e3) {
                assertEquals("3px", paddingBottom.getCssValue());
            }   
            try {
                paddingLeft.setCssValue("inherit");
            } catch (InvalidValueException e4) {
                assertEquals("4px", paddingLeft.getCssValue());
            }   
        }
    }
    
    @Test
    public void testGetPadding() {
        {
            Padding padding = new Padding("2px 3px 7px 4px");
            
            assertEquals("2px 3px 7px 4px", padding.getCssValue());
            
            final PaddingTop paddingTop = padding.getPaddingTop();
            final PaddingRight paddingRight = padding.getPaddingRight();
            final PaddingBottom paddingBottom = padding.getPaddingBottom();
            final PaddingLeft paddingLeft = padding.getPaddingLeft();
            
            assertNotNull(paddingTop);
            assertNotNull(paddingRight);
            assertNotNull(paddingBottom);
            assertNotNull(paddingLeft);
            
            assertTrue(paddingTop.isAlreadyInUse());
            assertTrue(paddingRight.isAlreadyInUse());
            assertTrue(paddingBottom.isAlreadyInUse());
            assertTrue(paddingLeft.isAlreadyInUse());
            
            assertEquals("2px", paddingTop.getCssValue());
            assertEquals("3px", paddingRight.getCssValue());
            assertEquals("7px", paddingBottom.getCssValue());
            assertEquals("4px", paddingLeft.getCssValue());
            assertEquals("2px 3px 7px 4px", padding.getCssValue());
            
            paddingTop.setCssValue("4px");
            assertEquals("4px 3px 7px 4px", padding.getCssValue());
            
            paddingRight.setCssValue("5px");
            assertEquals("4px 5px 7px 4px", padding.getCssValue());
            
            paddingBottom.setCssValue("6px");
            assertEquals("4px 5px 6px 4px", padding.getCssValue());
            
            paddingLeft.setCssValue("2px");
            assertEquals("4px 5px 6px 2px", padding.getCssValue());
            
            
            padding.setCssValue("2px 3px 7px");
            assertEquals("2px", paddingTop.getCssValue());
            assertEquals("3px", paddingRight.getCssValue());
            assertEquals("7px", paddingBottom.getCssValue());
            assertEquals("3px", paddingLeft.getCssValue());
            
            padding.setCssValue("3px 7px");
            assertEquals("3px", paddingTop.getCssValue());
            assertEquals("7px", paddingRight.getCssValue());
            assertEquals("3px", paddingBottom.getCssValue());
            assertEquals("7px", paddingLeft.getCssValue());
            
            padding.setCssValue("3px");
            assertEquals("3px", paddingTop.getCssValue());
            assertEquals("3px", paddingRight.getCssValue());
            assertEquals("3px", paddingBottom.getCssValue());
            assertEquals("3px", paddingLeft.getCssValue());
            
            paddingTop.setCssValue("8px");
            paddingBottom.setCssValue("8px");
            
            paddingRight.setCssValue("7px");
            paddingLeft.setCssValue("7px");
            
            assertEquals("8px 7px", padding.getCssValue());
            
            paddingTop.setCssValue("2px");
            paddingBottom.setCssValue("3px");
            
            paddingRight.setCssValue("7px");
            paddingLeft.setCssValue("7px");
            
            assertEquals("2px 7px 3px", padding.getCssValue());
            
            paddingTop.setCssValue("8px");
            paddingBottom.setCssValue("8px");
            
            paddingRight.setCssValue("8px");
            paddingLeft.setCssValue("8px");
            
            assertEquals("8px", padding.getCssValue());
            
            padding.setAsInherit();
            assertNull(padding.getPaddingTop());
            assertNull(padding.getPaddingRight());
            assertNull(padding.getPaddingBottom());
            assertNull(padding.getPaddingLeft());
            
            assertFalse(paddingTop.isAlreadyInUse());
            assertFalse(paddingRight.isAlreadyInUse());
            assertFalse(paddingBottom.isAlreadyInUse());
            assertFalse(paddingLeft.isAlreadyInUse());
        }
    }
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = Padding.isValid("45px");
            assertTrue(valid);
            final boolean invalid = Padding.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = Padding.isValid("45PX");
            assertTrue(valid);
        }
        {
            final boolean valid = Padding.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Padding.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = Padding.isValid("45%");
            assertTrue(valid);
            final boolean invalid = Padding.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = Padding.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Padding.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = Padding.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = Padding.isValid("-1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Padding.isValid("25px 23px 17px -1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Padding.isValid("-25px 23px 17px 1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Padding.isValid("25px -23px 17px 1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Padding.isValid("25px 23px -17px 1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Padding.isValid("25px 23px 17px -1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Padding.isValid("25px 23px 17px initial");
           assertFalse(valid);
        }
        {
            final boolean valid = Padding.isValid("25px 23px 17PX initial");
           assertFalse(valid);
        }
        {
            final boolean valid = Padding.isValid("25px 23px 17PX initial");
           assertFalse(valid);
        }
        {
            final boolean valid = Padding.isValid("25px 23px 17px INHERIT");
           assertFalse(valid);
        }
    }
    
    @Test
    public void testSetPaddingAlreadyInUse() throws Exception {
        
        Padding padding = new Padding("25px 45px 85px 155px");
        final PaddingTop paddingTop = padding.getPaddingTop();
        final PaddingRight paddingRight = padding.getPaddingRight();
        final PaddingBottom paddingBottom = padding.getPaddingBottom();
        final PaddingLeft paddingLeft = padding.getPaddingLeft();
        
        assertTrue(paddingTop.isAlreadyInUse());
        assertTrue(paddingRight.isAlreadyInUse());
        assertTrue(paddingBottom.isAlreadyInUse());
        assertTrue(paddingLeft.isAlreadyInUse());
        
        final PaddingTop paddingTop2 = new PaddingTop();
        final PaddingRight paddingRight2 = new PaddingRight();
        final PaddingBottom paddingBottom2 = new PaddingBottom();
        final PaddingLeft paddingLeft2 = new PaddingLeft();
        
        Padding padding2 = new Padding();
        assertNull(padding2.getPaddingTop());
        assertNull(padding2.getPaddingRight());
        assertNull(padding2.getPaddingBottom());
        assertNull(padding2.getPaddingLeft());
        
        assertFalse(paddingTop2.isAlreadyInUse());
        assertFalse(paddingRight2.isAlreadyInUse());
        assertFalse(paddingBottom2.isAlreadyInUse());
        assertFalse(paddingLeft2.isAlreadyInUse());
        
        padding2.setPadding(paddingTop2, paddingRight2, paddingBottom2, paddingLeft2);
        
        assertTrue(paddingTop2.isAlreadyInUse());
        assertTrue(paddingRight2.isAlreadyInUse());
        assertTrue(paddingBottom2.isAlreadyInUse());
        assertTrue(paddingLeft2.isAlreadyInUse());
        
        assertEquals(paddingTop2, padding2.getPaddingTop());
        assertEquals(paddingRight2, padding2.getPaddingRight());
        assertEquals(paddingBottom2, padding2.getPaddingBottom());
        assertEquals(paddingLeft2, padding2.getPaddingLeft());
        
        assertEquals(paddingTop, padding.getPaddingTop());
        assertEquals(paddingRight, padding.getPaddingRight());
        assertEquals(paddingBottom, padding.getPaddingBottom());
        assertEquals(paddingLeft, padding.getPaddingLeft());
        
        padding.setPadding(paddingTop2, paddingRight2, paddingBottom2, paddingLeft2);
        
        assertNotEquals(paddingTop2, padding.getPaddingTop());
        assertNotEquals(paddingRight2, padding.getPaddingRight());
        assertNotEquals(paddingBottom2, padding.getPaddingBottom());
        assertNotEquals(paddingLeft2, padding.getPaddingLeft());
        
        assertTrue(paddingTop.isAlreadyInUse());
        assertTrue(paddingRight.isAlreadyInUse());
        assertTrue(paddingBottom.isAlreadyInUse());
        assertTrue(paddingLeft.isAlreadyInUse());
       
    }
}
