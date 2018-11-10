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
public class WebkitFlexTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#WebkitFlex()}.
     */
    @Test
    public void testWebkitFlex() {
        final WebkitFlex flex = new WebkitFlex();
        assertEquals("0.0 1.0 auto", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#WebkitFlex(java.lang.String)}.
     */
    @Test
    public void testWebkitFlexString() {
        final WebkitFlex flex = new WebkitFlex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#WebkitFlex(com.webfirmframework.wffweb.css.css3.WebkitFlex)}.
     */
    @Test
    public void testWebkitFlexWebkitFlex() {
        final WebkitFlex flex = new WebkitFlex(new WebkitFlex("2 5 25px"));
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        final WebkitFlex flex = new WebkitFlex();
        assertEquals(CssNameConstants.WEBKIT_FLEX, flex.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        final WebkitFlex flex = new WebkitFlex(new WebkitFlex("2 5 25px"));
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#toString()}.
     */
    @Test
    public void testToString() {
        final WebkitFlex flex = new WebkitFlex("2 5 25px");
        assertEquals(CssNameConstants.WEBKIT_FLEX+": "+flex.getCssValue(),
                flex.getCssName() + ": " + flex.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        final WebkitFlex flex = new WebkitFlex();
        flex.setCssValue("2 5 25px");
        assertEquals(CssNameConstants.WEBKIT_FLEX+": "+flex.getCssValue(),
                flex.getCssName() + ": " + flex.getCssValue());
        assertEquals("2.0 5.0 25px", flex.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#setCssValue(java.lang.String)}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueStringInvalid() {
        final WebkitFlex flex = new WebkitFlex();
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
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(WebkitFlex.isValid("1"));
        assertTrue(WebkitFlex.isValid("1px"));
        assertTrue(WebkitFlex.isValid("1 1"));
        assertTrue(WebkitFlex.isValid("1 2 3px"));
        
        assertFalse(WebkitFlex.isValid("1 2 3"));
        assertFalse(WebkitFlex.isValid("1 df"));
        assertFalse(WebkitFlex.isValid("kdfidk"));
        assertFalse(WebkitFlex.isValid("1 2 2 2"));
        
        
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        final WebkitFlex flex = new WebkitFlex("2 5 25px");
        
        final WebkitFlexGrow flexGrow = flex.getWebkitFlexGrow();
        assertNotNull(flexGrow);
        assertTrue(flexGrow.isAlreadyInUse());
        
        final WebkitFlexShrink flexShrink = flex.getWebkitFlexShrink();
        assertNotNull(flexShrink);
        assertTrue(flexShrink.isAlreadyInUse());
        
        final WebkitFlexBasis flexBasis = flex.getWebkitFlexBasis();
        assertNotNull(flexBasis);
        assertTrue(flexBasis.isAlreadyInUse());
        
        flex.setAsInherit();
        
        assertNull(flex.getWebkitFlexGrow());
        assertNull(flex.getWebkitFlexShrink());
        assertNull(flex.getWebkitFlexBasis());
        
        assertFalse(flexGrow.isAlreadyInUse());
        assertFalse(flexShrink.isAlreadyInUse());
        assertFalse(flexBasis.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        final WebkitFlex flex = new WebkitFlex("2 5 25px");
        
        final WebkitFlexGrow flexGrow = flex.getWebkitFlexGrow();
        assertNotNull(flexGrow);
        assertTrue(flexGrow.isAlreadyInUse());
        
        final WebkitFlexShrink flexShrink = flex.getWebkitFlexShrink();
        assertNotNull(flexShrink);
        assertTrue(flexShrink.isAlreadyInUse());
        
        final WebkitFlexBasis flexBasis = flex.getWebkitFlexBasis();
        assertNotNull(flexBasis);
        assertTrue(flexBasis.isAlreadyInUse());
        
        flex.setAsInherit();
        
        assertNull(flex.getWebkitFlexGrow());
        assertNull(flex.getWebkitFlexShrink());
        assertNull(flex.getWebkitFlexBasis());
        
        assertFalse(flexGrow.isAlreadyInUse());
        assertFalse(flexShrink.isAlreadyInUse());
        assertFalse(flexBasis.isAlreadyInUse());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#getWebkitFlexBasis()}.
     */
    @Test
    public void testGetWebkitFlexBasis() {
        final WebkitFlex flex = new WebkitFlex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getWebkitFlexBasis());
        assertEquals("25px", flex.getWebkitFlexBasis().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#getWebkitFlexShrink()}.
     */
    @Test
    public void testGetWebkitFlexShrink() {
        final WebkitFlex flex = new WebkitFlex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getWebkitFlexShrink());
        assertEquals("5.0", flex.getWebkitFlexShrink().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#getWebkitFlexGrow()}.
     */
    @Test
    public void testGetWebkitFlexGrow() {
        final WebkitFlex flex = new WebkitFlex("2 5 25px");
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        assertNotNull(flex.getWebkitFlexGrow());
        assertEquals("2.0", flex.getWebkitFlexGrow().getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#setWebkitFlexGrow(com.webfirmframework.wffweb.css.css3.WebkitFlexGrow)}.
     */
    @Test
    public void testSetWebkitFlexGrow() {
        final WebkitFlex flex = new WebkitFlex("6 5 25px");
        
        final WebkitFlexGrow flexGrow1 = flex.getWebkitFlexGrow();
        final WebkitFlexGrow flexGrow2 = new WebkitFlexGrow(2);
        
        flex.setWebkitFlexGrow(flexGrow2);// here it will show a warning in the console
        assertEquals("2.0 5.0 25px", flex.getCssValue());
        
        assertNotNull(flex.getWebkitFlexGrow());
        assertEquals("2.0", flex.getWebkitFlexGrow().getCssValue());
        
        assertNotNull(flex.getWebkitFlexShrink());
        assertEquals("5.0", flex.getWebkitFlexShrink().getCssValue());
        
        assertNotNull(flex.getWebkitFlexBasis());
        assertEquals("25px", flex.getWebkitFlexBasis().getCssValue());
        
        assertNotEquals(flexGrow1, flexGrow2);
        assertNotEquals(flexGrow2, flex.getWebkitFlexGrow());
        assertEquals(flexGrow1, flex.getWebkitFlexGrow());
        
        flex.setWebkitFlexGrow(flexGrow1);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#setWebkitFlexShrink(com.webfirmframework.wffweb.css.css3.WebkitFlexShrink)}.
     */
    @Test
    public void testSetWebkitFlexShrink() {
        final WebkitFlex flex = new WebkitFlex("6 5 25px");
        
        final WebkitFlexShrink flexShrink1 = flex.getWebkitFlexShrink();
        final WebkitFlexShrink flexShrink2 = new WebkitFlexShrink(2);
        
        flex.setWebkitFlexShrink(flexShrink2);// here it will show a warning in the console
        assertEquals("6.0 2.0 25px", flex.getCssValue());
        
        assertNotNull(flex.getWebkitFlexGrow());
        assertEquals("6.0", flex.getWebkitFlexGrow().getCssValue());
        
        assertNotNull(flex.getWebkitFlexShrink());
        assertEquals("2.0", flex.getWebkitFlexShrink().getCssValue());
        
        assertNotNull(flex.getWebkitFlexBasis());
        assertEquals("25px", flex.getWebkitFlexBasis().getCssValue());
        
        assertNotEquals(flexShrink1, flexShrink2);
        assertNotEquals(flexShrink2, flex.getWebkitFlexShrink());
        assertEquals(flexShrink1, flex.getWebkitFlexShrink());
        
        flex.setWebkitFlexShrink(flexShrink1);

    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#setWebkitFlexBasis(com.webfirmframework.wffweb.css.css3.WebkitFlexBasis)}.
     */
    @Test
    public void testSetWebkitFlexBasis() {
        final WebkitFlex flex = new WebkitFlex("6 5 25px");
        
        final WebkitFlexBasis flexBasis1 = flex.getWebkitFlexBasis();
        final WebkitFlexBasis flexBasis2 = new WebkitFlexBasis(2, CssLengthUnit.PX);
        
        flex.setWebkitFlexBasis(flexBasis2);// here it will show a warning in the console
        assertEquals("6.0 5.0 2.0px", flex.getCssValue());
        
        assertNotNull(flex.getWebkitFlexGrow());
        assertEquals("6.0", flex.getWebkitFlexGrow().getCssValue());
        
        assertNotNull(flex.getWebkitFlexShrink());
        assertEquals("5.0", flex.getWebkitFlexShrink().getCssValue());
        
        assertNotNull(flex.getWebkitFlexBasis());
        assertEquals("2.0px", flex.getWebkitFlexBasis().getCssValue());
        
        assertNotEquals(flexBasis1, flexBasis2);
        assertNotEquals(flexBasis2, flex.getWebkitFlexBasis());
        assertEquals(flexBasis1, flex.getWebkitFlexBasis());
        
        flex.setWebkitFlexBasis(flexBasis1);
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#stateChanged(com.webfirmframework.wffweb.css.core.CssProperty)}.
     */
    @Test
    public void testStateChanged() {
        //NOP
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlex#addPredefinedConstant(java.lang.String)}.
     */
    @Test
    public void testAddPredefinedConstant() {
        //TODO not implemented
    }

}
