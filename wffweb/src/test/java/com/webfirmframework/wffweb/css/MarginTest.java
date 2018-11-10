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
public class MarginTest {

    @Test
    public void testMargin() {
        Margin margin = new Margin();
        assertEquals(Margin.INITIAL, margin.getCssValue());
    }

    @Test
    public void testMarginString() {
        Margin margin = new Margin("2cm 4cm 3cm 4cm");
        assertEquals("2cm 4cm 3cm 4cm", margin.getCssValue());   
    }

    @Test
    public void testMarginMargin() {
        Margin margin = new Margin("9px");
        Margin margin1 = new Margin(margin);
        assertEquals("9px", margin1.getCssValue());
    }

    @Test
    public void testSetValue() {
        Margin margin = new Margin();
        margin.setValue("9px");
        assertEquals("9px", margin.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        Margin margin = new Margin();
        assertEquals(CssNameConstants.MARGIN, margin.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Margin margin = new Margin("9px");
        assertEquals("9px", margin.getCssValue());   
    }

    @Test
    public void testToString() {
        Margin margin = new Margin("2px");
        assertEquals(CssNameConstants.MARGIN + ": 2px",
                margin.toString());
    }

    @Test
    public void testGetValue() {
        Margin margin = new Margin("2cm 4cm 3cm 4cm");
        assertEquals("2cm 4cm 3cm 4cm", margin.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        Margin margin = new Margin();
        margin.setCssValue("9px");
        assertEquals("9px", margin.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        Margin margin = new Margin();
        margin.setAsInitial();
        assertEquals(Margin.INITIAL, margin.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        Margin margin = new Margin();
        margin.setAsInherit();
        assertEquals(Margin.INHERIT, margin.getCssValue());
    }
    
    
    @Test(expected = NullValueException.class)
    public void testSetMarginForNullValues() {
        Margin margin = new Margin();
        
        MarginTop marginTop = new MarginTop("55px");
        MarginRight marginRight = new MarginRight("65px");
        MarginBottom marginBottom = new MarginBottom("75px");
        MarginLeft marginLeft = new MarginLeft("85px");
        
        
        try {
            margin.setMargin(null, null, null, null);   
        } catch (NullValueException e) {
            try {
                margin.setMargin(null, marginRight, marginBottom, marginLeft);
            } catch (NullValueException e2) {
                try {
                    margin.setMargin(marginTop, null, marginBottom, marginLeft);
                } catch (NullValueException e3) {
                    try {
                        margin.setMargin(marginTop, marginRight, null, marginLeft);
                    } catch (NullValueException e4) {
                        try {
                            margin.setMargin(marginTop, marginRight, marginBottom, null);
                        } catch (NullValueException e5) {
                            throw e4;
                        }  
                    }   
                }   
            }
        }

    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetMarginForInvalidValues() {
        Margin margin = new Margin();
        
        MarginTop marginTop = new MarginTop("2px");
        MarginRight marginRight = new MarginRight("3px");
        MarginBottom marginBottom = new MarginBottom("7px");
        MarginLeft marginLeft = new MarginLeft("4px");
        
        try {
            marginTop.setAsInitial();
            margin.setMargin(marginTop, marginRight, marginBottom, marginLeft);
        } catch (InvalidValueException e) {
            try {
                marginRight.setAsInitial();
                margin.setMargin(marginTop, marginRight, marginBottom, marginLeft);
            } catch (InvalidValueException e2) {
                try {
                    marginBottom.setAsInitial();
                    margin.setMargin(marginTop, marginRight, marginBottom, marginLeft);
                } catch (InvalidValueException e3) {
                    try {
                        marginLeft.setAsInitial();
                        margin.setMargin(marginTop, marginRight, marginBottom, marginLeft);
                    } catch (InvalidValueException e4) {
                        throw e4;
                    }   
                }   
            }
        }
        
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetMarginForRuntimeInvalidValues() {

        Margin margin = new Margin();
        MarginTop marginTop = new MarginTop("1px");
        MarginRight marginRight = new MarginRight("2px");
        MarginBottom marginBottom = new MarginBottom("3px");
        MarginLeft marginLeft = new MarginLeft("4px");
        margin.setMargin(marginTop, marginRight, marginBottom, marginLeft);   
        
        try {
            marginTop.setAsInherit();
        } catch (InvalidValueException e) {
            try {
                marginRight.setAsInherit();
            } catch (InvalidValueException e2) {
                try {
                    marginBottom.setAsInherit();
                } catch (InvalidValueException e3) {
                    try {
                        marginLeft.setAsInherit();
                    } catch (InvalidValueException e4) {
                        throw e4;
                    }   
                }   
            }
        }
        
    }
    
    @Test
    public void testSetMarginForRuntimeInvalidValuesWithRollback() {
        
        Margin margin = new Margin();
        MarginTop marginTop = new MarginTop("1px");
        MarginRight marginRight = new MarginRight("2px");
        MarginBottom marginBottom = new MarginBottom("3px");
        MarginLeft marginLeft = new MarginLeft("4px");
        margin.setMargin(marginTop, marginRight, marginBottom, marginLeft);   
        
        {
            try {
                marginTop.setAsInitial();
            } catch (InvalidValueException e) {
                assertEquals("1px", marginTop.getCssValue());
            }
            try {
                marginRight.setAsInitial();
            } catch (InvalidValueException e2) {
                assertEquals("2px", marginRight.getCssValue());
            }
            try {
                marginBottom.setAsInitial();
            } catch (InvalidValueException e3) {
                assertEquals("3px", marginBottom.getCssValue());
            }   
            try {
                marginLeft.setAsInitial();
            } catch (InvalidValueException e4) {
                assertEquals("4px", marginLeft.getCssValue());
            }   
        }
        {
            try {
                marginTop.setCssValue("inherit");
            } catch (InvalidValueException e) {
                assertEquals("1px", marginTop.getCssValue());
            }
            try {
                marginRight.setCssValue("inherit");
            } catch (InvalidValueException e2) {
                assertEquals("2px", marginRight.getCssValue());
            }
            try {
                marginBottom.setCssValue("inherit");
            } catch (InvalidValueException e3) {
                assertEquals("3px", marginBottom.getCssValue());
            }   
            try {
                marginLeft.setCssValue("inherit");
            } catch (InvalidValueException e4) {
                assertEquals("4px", marginLeft.getCssValue());
            }   
        }
    }
    
    @Test
    public void testGetMargin() {
        {
            Margin margin = new Margin("2px 3px 7px 4px");
            
            assertEquals("2px 3px 7px 4px", margin.getCssValue());
            
            final MarginTop marginTop = margin.getMarginTop();
            final MarginRight marginRight = margin.getMarginRight();
            final MarginBottom marginBottom = margin.getMarginBottom();
            final MarginLeft marginLeft = margin.getMarginLeft();
            
            assertNotNull(marginTop);
            assertNotNull(marginRight);
            assertNotNull(marginBottom);
            assertNotNull(marginLeft);
            
            assertTrue(marginTop.isAlreadyInUse());
            assertTrue(marginRight.isAlreadyInUse());
            assertTrue(marginBottom.isAlreadyInUse());
            assertTrue(marginLeft.isAlreadyInUse());
            
            assertEquals("2px", marginTop.getCssValue());
            assertEquals("3px", marginRight.getCssValue());
            assertEquals("7px", marginBottom.getCssValue());
            assertEquals("4px", marginLeft.getCssValue());
            assertEquals("2px 3px 7px 4px", margin.getCssValue());
            
            marginTop.setCssValue("4px");
            assertEquals("4px 3px 7px 4px", margin.getCssValue());
            
            marginRight.setCssValue("5px");
            assertEquals("4px 5px 7px 4px", margin.getCssValue());
            
            marginBottom.setCssValue("6px");
            assertEquals("4px 5px 6px 4px", margin.getCssValue());
            
            marginLeft.setCssValue("2px");
            assertEquals("4px 5px 6px 2px", margin.getCssValue());
            
            
            margin.setCssValue("2px 3px 7px");
            assertEquals("2px", marginTop.getCssValue());
            assertEquals("3px", marginRight.getCssValue());
            assertEquals("7px", marginBottom.getCssValue());
            assertEquals("3px", marginLeft.getCssValue());
            
            margin.setCssValue("3px 7px");
            assertEquals("3px", marginTop.getCssValue());
            assertEquals("7px", marginRight.getCssValue());
            assertEquals("3px", marginBottom.getCssValue());
            assertEquals("7px", marginLeft.getCssValue());
            
            margin.setCssValue("3px");
            assertEquals("3px", marginTop.getCssValue());
            assertEquals("3px", marginRight.getCssValue());
            assertEquals("3px", marginBottom.getCssValue());
            assertEquals("3px", marginLeft.getCssValue());
            
            marginTop.setCssValue("8px");
            marginBottom.setCssValue("8px");
            
            marginRight.setCssValue("7px");
            marginLeft.setCssValue("7px");
            
            assertEquals("8px 7px", margin.getCssValue());
            
            marginTop.setCssValue("2px");
            marginBottom.setCssValue("3px");
            
            marginRight.setCssValue("7px");
            marginLeft.setCssValue("7px");
            
            assertEquals("2px 7px 3px", margin.getCssValue());
            
            marginTop.setCssValue("8px");
            marginBottom.setCssValue("8px");
            
            marginRight.setCssValue("8px");
            marginLeft.setCssValue("8px");
            
            assertEquals("8px", margin.getCssValue());
            
            margin.setAsInherit();
            assertNull(margin.getMarginTop());
            assertNull(margin.getMarginRight());
            assertNull(margin.getMarginBottom());
            assertNull(margin.getMarginLeft());
            
            assertFalse(marginTop.isAlreadyInUse());
            assertFalse(marginRight.isAlreadyInUse());
            assertFalse(marginBottom.isAlreadyInUse());
            assertFalse(marginLeft.isAlreadyInUse());
        }
    }
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = Margin.isValid("45px");
            assertTrue(valid);
            final boolean invalid = Margin.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = Margin.isValid("45PX");
            assertTrue(valid);
        }
        {
            final boolean valid = Margin.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Margin.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = Margin.isValid("45%");
            assertTrue(valid);
            final boolean invalid = Margin.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = Margin.isValid("45em");
            assertTrue(valid);
            final boolean invalid = Margin.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = Margin.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = Margin.isValid("--1px");
           assertFalse(valid);
        }
        {
            final boolean valid = Margin.isValid("25px 23px 17px -1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Margin.isValid("-25px 23px 17px 1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Margin.isValid("25px -23px 17px 1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Margin.isValid("25px 23px -17px 1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Margin.isValid("25px 23px 17px -1px");
           assertTrue(valid);
        }
        {
            final boolean valid = Margin.isValid("25px 23px 17px initial");
           assertFalse(valid);
        }
        {
            final boolean valid = Margin.isValid("25px 23px 17PX initial");
           assertFalse(valid);
        }
        {
            final boolean valid = Margin.isValid("25px 23px 17PX initial");
           assertFalse(valid);
        }
        {
            final boolean valid = Margin.isValid("25px 23px 17px INHERIT");
           assertFalse(valid);
        }
    }
    
    @Test
    public void testSetMarginAlreadyInUse() throws Exception {
        
        Margin margin = new Margin("25px 45px 85px 155px");
        final MarginTop marginTop = margin.getMarginTop();
        final MarginRight marginRight = margin.getMarginRight();
        final MarginBottom marginBottom = margin.getMarginBottom();
        final MarginLeft marginLeft = margin.getMarginLeft();
        
        assertTrue(marginTop.isAlreadyInUse());
        assertTrue(marginRight.isAlreadyInUse());
        assertTrue(marginBottom.isAlreadyInUse());
        assertTrue(marginLeft.isAlreadyInUse());
        
        final MarginTop marginTop2 = new MarginTop();
        final MarginRight marginRight2 = new MarginRight();
        final MarginBottom marginBottom2 = new MarginBottom();
        final MarginLeft marginLeft2 = new MarginLeft();
        
        Margin margin2 = new Margin();
        assertNull(margin2.getMarginTop());
        assertNull(margin2.getMarginRight());
        assertNull(margin2.getMarginBottom());
        assertNull(margin2.getMarginLeft());
        
        assertFalse(marginTop2.isAlreadyInUse());
        assertFalse(marginRight2.isAlreadyInUse());
        assertFalse(marginBottom2.isAlreadyInUse());
        assertFalse(marginLeft2.isAlreadyInUse());
        
        margin2.setMargin(marginTop2, marginRight2, marginBottom2, marginLeft2);
        
        assertTrue(marginTop2.isAlreadyInUse());
        assertTrue(marginRight2.isAlreadyInUse());
        assertTrue(marginBottom2.isAlreadyInUse());
        assertTrue(marginLeft2.isAlreadyInUse());
        
        assertEquals(marginTop2, margin2.getMarginTop());
        assertEquals(marginRight2, margin2.getMarginRight());
        assertEquals(marginBottom2, margin2.getMarginBottom());
        assertEquals(marginLeft2, margin2.getMarginLeft());
        
        assertEquals(marginTop, margin.getMarginTop());
        assertEquals(marginRight, margin.getMarginRight());
        assertEquals(marginBottom, margin.getMarginBottom());
        assertEquals(marginLeft, margin.getMarginLeft());
        
        margin.setMargin(marginTop2, marginRight2, marginBottom2, marginLeft2);
        
        assertNotEquals(marginTop2, margin.getMarginTop());
        assertNotEquals(marginRight2, margin.getMarginRight());
        assertNotEquals(marginBottom2, margin.getMarginBottom());
        assertNotEquals(marginLeft2, margin.getMarginLeft());
        
        assertTrue(marginTop.isAlreadyInUse());
        assertTrue(marginRight.isAlreadyInUse());
        assertTrue(marginBottom.isAlreadyInUse());
        assertTrue(marginLeft.isAlreadyInUse());
       
    }
}
