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
public class MozFlexTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#MozFlex()}.
     */
    @Test
    public void testMozFlex() {
        final MozFlex flex = new MozFlex();
        assertEquals("0.0 1.0 auto", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#MozFlex(java.lang.String)}.
     */
    @Test
    public void testMozFlexString() {
        final MozFlex flex = new MozFlex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#MozFlex(com.webfirmframework.wffweb.css.css3.MozFlex)}.
     */
    @Test
    public void testMozFlexMozFlex() {
        final MozFlex flex = new MozFlex(new MozFlex("2 5 25px"));
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        final MozFlex flex = new MozFlex();
        assertEquals(CssNameConstants.MOZ_FLEX, flex.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        final MozFlex flex = new MozFlex(new MozFlex("2 5 25px"));
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#toString()}.
     */
    @Test
    public void testToString() {
        final MozFlex flex = new MozFlex("2 5 25px");
        assertEquals(CssNameConstants.MOZ_FLEX+": "+flex.getCssValue(),
                flex.getCssName() + ": " + flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        final MozFlex flex = new MozFlex();
        flex.setCssValue("2 5 25px");
        assertEquals(CssNameConstants.MOZ_FLEX+": "+flex.getCssValue(),
                flex.getCssName() + ": " + flex.getCssValue());
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#setCssValue(java.lang.String)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueStringInvalid() {
        final MozFlex flex = new MozFlex();
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
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(MozFlex.isValid("1"));
        assertTrue(MozFlex.isValid("1px"));
        assertTrue(MozFlex.isValid("1 1"));
        assertTrue(MozFlex.isValid("1 2 3px"));
        
        assertFalse(MozFlex.isValid("1 2 3"));
        assertFalse(MozFlex.isValid("1 df"));
        assertFalse(MozFlex.isValid("kdfidk"));
        assertFalse(MozFlex.isValid("1 2 2 2"));
        
        
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        final MozFlex flex = new MozFlex("2 5 25px");
        
        final MozFlexGrow flexGrow = flex.getMozFlexGrow();
        assertNotNull(flexGrow);
        assertTrue(flexGrow.isAlreadyInUse());
        
        final MozFlexShrink flexShrink = flex.getMozFlexShrink();
        assertNotNull(flexShrink);
        assertTrue(flexShrink.isAlreadyInUse());
        
        final MozFlexBasis flexBasis = flex.getMozFlexBasis();
        assertNotNull(flexBasis);
        assertTrue(flexBasis.isAlreadyInUse());
        
        flex.setAsInherit();
        
        assertNull(flex.getMozFlexGrow());
        assertNull(flex.getMozFlexShrink());
        assertNull(flex.getMozFlexBasis());
        
        assertFalse(flexGrow.isAlreadyInUse());
        assertFalse(flexShrink.isAlreadyInUse());
        assertFalse(flexBasis.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        final MozFlex flex = new MozFlex("2 5 25px");
        
        final MozFlexGrow flexGrow = flex.getMozFlexGrow();
        assertNotNull(flexGrow);
        assertTrue(flexGrow.isAlreadyInUse());
        
        final MozFlexShrink flexShrink = flex.getMozFlexShrink();
        assertNotNull(flexShrink);
        assertTrue(flexShrink.isAlreadyInUse());
        
        final MozFlexBasis flexBasis = flex.getMozFlexBasis();
        assertNotNull(flexBasis);
        assertTrue(flexBasis.isAlreadyInUse());
        
        flex.setAsInherit();
        
        assertNull(flex.getMozFlexGrow());
        assertNull(flex.getMozFlexShrink());
        assertNull(flex.getMozFlexBasis());
        
        assertFalse(flexGrow.isAlreadyInUse());
        assertFalse(flexShrink.isAlreadyInUse());
        assertFalse(flexBasis.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#getMozFlexBasis()}.
     */
    @Test
    public void testGetMozFlexBasis() {
        final MozFlex flex = new MozFlex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getMozFlexBasis());
        assertEquals("25px", flex.getMozFlexBasis().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#getMozFlexShrink()}.
     */
    @Test
    public void testGetMozFlexShrink() {
        final MozFlex flex = new MozFlex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getMozFlexShrink());
        assertEquals("5.0", flex.getMozFlexShrink().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#getMozFlexGrow()}.
     */
    @Test
    public void testGetMozFlexGrow() {
        final MozFlex flex = new MozFlex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getMozFlexGrow());
        assertEquals("2.0", flex.getMozFlexGrow().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#setMozFlexGrow(com.webfirmframework.wffweb.css.css3.MozFlexGrow)}.
     */
    @Test
    public void testSetMozFlexGrow() {
        final MozFlex flex = new MozFlex("6 5 25px");
        
        final MozFlexGrow flexGrow1 = flex.getMozFlexGrow();
        final MozFlexGrow flexGrow2 = new MozFlexGrow(2);
        
        flex.setMozFlexGrow(flexGrow2);// here it will show a warning in the console
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        
        assertNotNull(flex.getMozFlexGrow());
        assertEquals("2.0", flex.getMozFlexGrow().getCssValue());
        
        assertNotNull(flex.getMozFlexShrink());
        assertEquals("5.0", flex.getMozFlexShrink().getCssValue());
        
        assertNotNull(flex.getMozFlexBasis());
        assertEquals("25px", flex.getMozFlexBasis().getCssValue());
        
        assertNotEquals(flexGrow1, flexGrow2);
        assertNotEquals(flexGrow2, flex.getMozFlexGrow());
        assertEquals(flexGrow1, flex.getMozFlexGrow());
        
        flex.setMozFlexGrow(flexGrow1);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#setMozFlexShrink(com.webfirmframework.wffweb.css.css3.MozFlexShrink)}.
     */
    @Test
    public void testSetMozFlexShrink() {
        final MozFlex flex = new MozFlex("6 5 25px");
        
        final MozFlexShrink flexShrink1 = flex.getMozFlexShrink();
        final MozFlexShrink flexShrink2 = new MozFlexShrink(2);
        
        flex.setMozFlexShrink(flexShrink2);// here it will show a warning in the console
        assertEquals("6.0 2.0 25px", flex.getCssValue());
        
        assertNotNull(flex.getMozFlexGrow());
        assertEquals("6.0", flex.getMozFlexGrow().getCssValue());
        
        assertNotNull(flex.getMozFlexShrink());
        assertEquals("2.0", flex.getMozFlexShrink().getCssValue());
        
        assertNotNull(flex.getMozFlexBasis());
        assertEquals("25px", flex.getMozFlexBasis().getCssValue());
        
        assertNotEquals(flexShrink1, flexShrink2);
        assertNotEquals(flexShrink2, flex.getMozFlexShrink());
        assertEquals(flexShrink1, flex.getMozFlexShrink());
        
        flex.setMozFlexShrink(flexShrink1);

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#setMozFlexBasis(com.webfirmframework.wffweb.css.css3.MozFlexBasis)}.
     */
    @Test
    public void testSetMozFlexBasis() {
        final MozFlex flex = new MozFlex("6 5 25px");
        
        final MozFlexBasis flexBasis1 = flex.getMozFlexBasis();
        final MozFlexBasis flexBasis2 = new MozFlexBasis(2, CssLengthUnit.PX);
        
        flex.setMozFlexBasis(flexBasis2);// here it will show a warning in the console
        assertEquals("6.0 5.0 2.0px", flex.getCssValue());
        
        assertNotNull(flex.getMozFlexGrow());
        assertEquals("6.0", flex.getMozFlexGrow().getCssValue());
        
        assertNotNull(flex.getMozFlexShrink());
        assertEquals("5.0", flex.getMozFlexShrink().getCssValue());
        
        assertNotNull(flex.getMozFlexBasis());
        assertEquals("2.0px", flex.getMozFlexBasis().getCssValue());
        
        assertNotEquals(flexBasis1, flexBasis2);
        assertNotEquals(flexBasis2, flex.getMozFlexBasis());
        assertEquals(flexBasis1, flex.getMozFlexBasis());
        
        flex.setMozFlexBasis(flexBasis1);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#stateChanged(com.webfirmframework.wffweb.css.core.CssProperty)}.
     */
    @Test
    public void testStateChanged() {
        //NOP
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlex#addPredefinedConstant(java.lang.String)}.
     */
    @Test
    public void testAddPredefinedConstant() {
        //TODO not implemented
    }

}
