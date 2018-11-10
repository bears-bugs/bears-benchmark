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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class FlexGrowTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#FlexGrow()}.
     */
    @Test
    public void testFlexGrow() {
        FlexGrow flexGrow = new FlexGrow();
        assertEquals(Float.valueOf(0F), flexGrow.getValue());
        assertEquals("0.0", flexGrow.getCssValue());
        
        flexGrow.setAsInherit();
        assertEquals(FlexGrow.INHERIT, flexGrow.getCssValue());
        assertNull(flexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#FlexGrow(java.lang.String)}.
     */
    @Test
    public void testFlexGrowString() {
        FlexGrow flexGrow = new FlexGrow("0.5");
        assertEquals(Float.valueOf(0.5F), flexGrow.getValue());
        assertEquals("0.5", flexGrow.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#FlexGrow(com.webfirmframework.wffweb.css.css3.FlexGrow)}.
     */
    @Test
    public void testFlexGrowFlexGrow() {
        final FlexGrow flexGrow1 = new FlexGrow("0.5");
        FlexGrow flexGrow = new FlexGrow(flexGrow1);
        assertEquals(Float.valueOf(0.5F), flexGrow.getValue());
        assertEquals("0.5", flexGrow.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#FlexGrow(float)}.
     */
    @Test
    public void testFlexGrowFloat() {
        {
            FlexGrow flexGrow = new FlexGrow(0.5F);
            assertEquals(Float.valueOf(0.5F), flexGrow.getValue());
            assertEquals("0.5", flexGrow.getCssValue());
            
        }
        {
            final FlexGrow flexGrow1 = new FlexGrow(0.5F);
            FlexGrow flexGrow = new FlexGrow(flexGrow1);
            assertEquals(Float.valueOf(0.5F), flexGrow.getValue());
            assertEquals("0.5", flexGrow.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        FlexGrow flexGrow = new FlexGrow(0.5F);
        assertEquals(CssNameConstants.FLEX_GROW, flexGrow.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        FlexGrow flexGrow = new FlexGrow();
        assertEquals(Float.valueOf(0F), flexGrow.getValue());
        assertEquals("0.0", flexGrow.getCssValue());

        flexGrow.setAsInherit();
        assertEquals(FlexGrow.INHERIT, flexGrow.getCssValue());
        assertNull(flexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#toString()}.
     */
    @Test
    public void testToString() {
        FlexGrow flexGrow = new FlexGrow();
        assertEquals(Float.valueOf(0F), flexGrow.getValue());
        assertEquals(CssNameConstants.FLEX_GROW + ": 0.0", flexGrow.toString());

        flexGrow.setAsInherit();
        assertEquals(CssNameConstants.FLEX_GROW + ": " + FlexGrow.INHERIT,
                flexGrow.toString());
        assertNull(flexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#getValue()}.
     */
    @Test
    public void testGetValue() {
        FlexGrow flexGrow = new FlexGrow();
        assertEquals(Float.valueOf(0F), flexGrow.getValue());
        flexGrow.setAsInherit();
        assertNull(flexGrow.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.FlexGrow#setValue(float)}.
     */
    @Test
    public void testSetValue() {
        FlexGrow flexGrow = new FlexGrow();
        flexGrow.setValue(0.5F);
        assertEquals(Float.valueOf(0.5F), flexGrow.getValue());
        flexGrow.setAsInherit();
        assertNull(flexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        FlexGrow flexGrow = new FlexGrow();
        flexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), flexGrow.getValue());
        flexGrow.setAsInherit();
        assertNull(flexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        FlexGrow flexGrow = new FlexGrow();
        flexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), flexGrow.getValue());
        flexGrow.setAsInitial();
        assertEquals(FlexGrow.INITIAL, flexGrow.getCssValue());
        assertNull(flexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        FlexGrow flexGrow = new FlexGrow();
        flexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), flexGrow.getValue());
        flexGrow.setAsInherit();
        assertEquals(FlexGrow.INHERIT, flexGrow.getCssValue());
        assertNull(flexGrow.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new FlexGrow().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexGrow#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(FlexGrow.isValid(".5"));
        assertTrue(FlexGrow.isValid("0.5"));
        assertTrue(FlexGrow.isValid("0.1"));
        assertTrue(FlexGrow.isValid("1"));
        assertTrue(FlexGrow.isValid("0"));
        assertTrue(FlexGrow.isValid(FlexGrow.INITIAL));
        assertTrue(FlexGrow.isValid(FlexGrow.INHERIT));
        assertFalse(FlexGrow.isValid("dfd"));
        assertFalse(FlexGrow.isValid("1px"));
        assertFalse(FlexGrow.isValid(""));
        assertFalse(FlexGrow.isValid("1 1"));
        assertTrue(FlexGrow.isValid("1.1"));
//        assertFalse(FlexGrow.isValid("-0.5"));
//        assertFalse(FlexGrow.isValid("-0"));
//        assertFalse(FlexGrow.isValid("-0.0"));
//        assertFalse(FlexGrow.isValid("+0.0"));
    }

}
