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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 * <pre>
 * The undefined value for flex-grow, flex-shrink and flex-basis in flex property is 1, 1 and 0% respectively.
 * </pre>
 */
public class FlexTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#Flex()}.
     */
    @Test
    public void testFlex() {
        final Flex flex = new Flex();
        assertEquals("0.0 1.0 auto", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#Flex(java.lang.String)}.
     */
    @Test
    public void testFlexString() {
        final Flex flex = new Flex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#Flex(com.webfirmframework.wffweb.css.css3.Flex)}.
     */
    @Test
    public void testFlexFlex() {
        final Flex flex = new Flex(new Flex("2 5 25px"));
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        final Flex flex = new Flex();
        assertEquals(CssNameConstants.FLEX, flex.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        final Flex flex = new Flex(new Flex("2 5 25px"));
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#toString()}.
     */
    @Test
    public void testToString() {
        final Flex flex = new Flex("2 5 25px");
        assertEquals(CssNameConstants.FLEX+": "+flex.getCssValue(),
                flex.getCssName() + ": " + flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        final Flex flex = new Flex();
        flex.setCssValue("2 5 25px");
        assertEquals(CssNameConstants.FLEX+": "+flex.getCssValue(),
                flex.getCssName() + ": " + flex.getCssValue());
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#setCssValue(java.lang.String)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueStringInvalid() {
        final Flex flex = new Flex();
        try {
            flex.setCssValue("2 5 25");
        } catch (InvalidValueException e) {
            try {
                flex.setCssValue("fddf");
            } catch (InvalidValueException e1) {
                throw e1;
            }
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(Flex.isValid("1"));
        assertTrue(Flex.isValid("1px"));
        assertTrue(Flex.isValid("1 1"));
        assertTrue(Flex.isValid("1 2 3px"));
        
        assertFalse(Flex.isValid("1 2 3"));
        assertFalse(Flex.isValid("1 df"));
        assertFalse(Flex.isValid("kdfidk"));
        assertFalse(Flex.isValid("1 2 2 2"));
        
        
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        final Flex flex = new Flex("2 5 25px");
        
        final FlexGrow flexGrow = flex.getFlexGrow();
        assertNotNull(flexGrow);
        assertTrue(flexGrow.isAlreadyInUse());
        
        final FlexShrink flexShrink = flex.getFlexShrink();
        assertNotNull(flexShrink);
        assertTrue(flexShrink.isAlreadyInUse());
        
        final FlexBasis flexBasis = flex.getFlexBasis();
        assertNotNull(flexBasis);
        assertTrue(flexBasis.isAlreadyInUse());
        
        flex.setAsInherit();
        
        assertNull(flex.getFlexGrow());
        assertNull(flex.getFlexShrink());
        assertNull(flex.getFlexBasis());
        
        assertFalse(flexGrow.isAlreadyInUse());
        assertFalse(flexShrink.isAlreadyInUse());
        assertFalse(flexBasis.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        final Flex flex = new Flex("2 5 25px");
        
        final FlexGrow flexGrow = flex.getFlexGrow();
        assertNotNull(flexGrow);
        assertTrue(flexGrow.isAlreadyInUse());
        
        final FlexShrink flexShrink = flex.getFlexShrink();
        assertNotNull(flexShrink);
        assertTrue(flexShrink.isAlreadyInUse());
        
        final FlexBasis flexBasis = flex.getFlexBasis();
        assertNotNull(flexBasis);
        assertTrue(flexBasis.isAlreadyInUse());
        
        flex.setAsInherit();
        
        assertNull(flex.getFlexGrow());
        assertNull(flex.getFlexShrink());
        assertNull(flex.getFlexBasis());
        
        assertFalse(flexGrow.isAlreadyInUse());
        assertFalse(flexShrink.isAlreadyInUse());
        assertFalse(flexBasis.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#getFlexBasis()}.
     */
    @Test
    public void testGetFlexBasis() {
        final Flex flex = new Flex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getFlexBasis());
        assertEquals("25px", flex.getFlexBasis().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#getFlexShrink()}.
     */
    @Test
    public void testGetFlexShrink() {
        final Flex flex = new Flex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getFlexShrink());
        assertEquals("5.0", flex.getFlexShrink().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#getFlexGrow()}.
     */
    @Test
    public void testGetFlexGrow() {
        final Flex flex = new Flex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getFlexGrow());
        assertEquals("2.0", flex.getFlexGrow().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#setFlexGrow(com.webfirmframework.wffweb.css.css3.FlexGrow)}.
     */
    @Test
    public void testSetFlexGrow() {
        final Flex flex = new Flex("6 5 25px");
        
        final FlexGrow flexGrow1 = flex.getFlexGrow();
        final FlexGrow flexGrow2 = new FlexGrow(2);
        
        flex.setFlexGrow(flexGrow2);// here it will show a warning in the console
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        
        assertNotNull(flex.getFlexGrow());
        assertEquals("2.0", flex.getFlexGrow().getCssValue());
        
        assertNotNull(flex.getFlexShrink());
        assertEquals("5.0", flex.getFlexShrink().getCssValue());
        
        assertNotNull(flex.getFlexBasis());
        assertEquals("25px", flex.getFlexBasis().getCssValue());
        
        assertNotEquals(flexGrow1, flexGrow2);
        assertNotEquals(flexGrow2, flex.getFlexGrow());
        assertEquals(flexGrow1, flex.getFlexGrow());
        
        flex.setFlexGrow(flexGrow1);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#setFlexShrink(com.webfirmframework.wffweb.css.css3.FlexShrink)}.
     */
    @Test
    public void testSetFlexShrink() {
        final Flex flex = new Flex("6 5 25px");
        
        final FlexShrink flexShrink1 = flex.getFlexShrink();
        final FlexShrink flexShrink2 = new FlexShrink(2);
        
        flex.setFlexShrink(flexShrink2);// here it will show a warning in the console
        assertEquals("6.0 2.0 25px", flex.getCssValue());
        
        assertNotNull(flex.getFlexGrow());
        assertEquals("6.0", flex.getFlexGrow().getCssValue());
        
        assertNotNull(flex.getFlexShrink());
        assertEquals("2.0", flex.getFlexShrink().getCssValue());
        
        assertNotNull(flex.getFlexBasis());
        assertEquals("25px", flex.getFlexBasis().getCssValue());
        
        assertNotEquals(flexShrink1, flexShrink2);
        assertNotEquals(flexShrink2, flex.getFlexShrink());
        assertEquals(flexShrink1, flex.getFlexShrink());
        
        flex.setFlexShrink(flexShrink1);

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#setFlexBasis(com.webfirmframework.wffweb.css.css3.FlexBasis)}.
     */
    @Test
    public void testSetFlexBasis() {
        final Flex flex = new Flex("6 5 25px");
        
        final FlexBasis flexBasis1 = flex.getFlexBasis();
        final FlexBasis flexBasis2 = new FlexBasis(2, CssLengthUnit.PX);
        
        flex.setFlexBasis(flexBasis2);// here it will show a warning in the console
        assertEquals("6.0 5.0 2.0px", flex.getCssValue());
        
        assertNotNull(flex.getFlexGrow());
        assertEquals("6.0", flex.getFlexGrow().getCssValue());
        
        assertNotNull(flex.getFlexShrink());
        assertEquals("5.0", flex.getFlexShrink().getCssValue());
        
        assertNotNull(flex.getFlexBasis());
        assertEquals("2.0px", flex.getFlexBasis().getCssValue());
        
        assertNotEquals(flexBasis1, flexBasis2);
        assertNotEquals(flexBasis2, flex.getFlexBasis());
        assertEquals(flexBasis1, flex.getFlexBasis());
        
        flex.setFlexBasis(flexBasis1);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#stateChanged(com.webfirmframework.wffweb.css.core.CssProperty)}.
     */
    @Test
    public void testStateChanged() {
        //NOP
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Flex#addPredefinedConstant(java.lang.String)}.
     */
    @Test
    public void testAddPredefinedConstant() {
        //TODO not implemented
    }

}
