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
public class MozFlexGrowTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#MozFlexGrow()}.
     */
    @Test
    public void testMozFlexGrow() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow();
        assertEquals(Float.valueOf(0F), mozFlexGrow.getValue());
        assertEquals("0.0", mozFlexGrow.getCssValue());
        
        mozFlexGrow.setAsInherit();
        assertEquals(MozFlexGrow.INHERIT, mozFlexGrow.getCssValue());
        assertNull(mozFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#MozFlexGrow(java.lang.String)}.
     */
    @Test
    public void testMozFlexGrowString() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow("0.5");
        assertEquals(Float.valueOf(0.5F), mozFlexGrow.getValue());
        assertEquals("0.5", mozFlexGrow.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#MozFlexGrow(com.webfirmframework.wffweb.css.css3.MozFlexGrow)}.
     */
    @Test
    public void testMozFlexGrowMozFlexGrow() {
        final MozFlexGrow mozFlexGrow1 = new MozFlexGrow("0.5");
        MozFlexGrow mozFlexGrow = new MozFlexGrow(mozFlexGrow1);
        assertEquals(Float.valueOf(0.5F), mozFlexGrow.getValue());
        assertEquals("0.5", mozFlexGrow.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#MozFlexGrow(float)}.
     */
    @Test
    public void testMozFlexGrowFloat() {
        {
            MozFlexGrow mozFlexGrow = new MozFlexGrow(0.5F);
            assertEquals(Float.valueOf(0.5F), mozFlexGrow.getValue());
            assertEquals("0.5", mozFlexGrow.getCssValue());
            
        }
        {
            final MozFlexGrow mozFlexGrow1 = new MozFlexGrow(0.5F);
            MozFlexGrow mozFlexGrow = new MozFlexGrow(mozFlexGrow1);
            assertEquals(Float.valueOf(0.5F), mozFlexGrow.getValue());
            assertEquals("0.5", mozFlexGrow.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow(0.5F);
        assertEquals(CssNameConstants.MOZ_FLEX_GROW, mozFlexGrow.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow();
        assertEquals(Float.valueOf(0F), mozFlexGrow.getValue());
        assertEquals("0.0", mozFlexGrow.getCssValue());

        mozFlexGrow.setAsInherit();
        assertEquals(MozFlexGrow.INHERIT, mozFlexGrow.getCssValue());
        assertNull(mozFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#toString()}.
     */
    @Test
    public void testToString() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow();
        assertEquals(Float.valueOf(0F), mozFlexGrow.getValue());
        assertEquals(CssNameConstants.MOZ_FLEX_GROW + ": 0.0", mozFlexGrow.toString());

        mozFlexGrow.setAsInherit();
        assertEquals(CssNameConstants.MOZ_FLEX_GROW + ": " + MozFlexGrow.INHERIT,
                mozFlexGrow.toString());
        assertNull(mozFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#getValue()}.
     */
    @Test
    public void testGetValue() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow();
        assertEquals(Float.valueOf(0F), mozFlexGrow.getValue());
        mozFlexGrow.setAsInherit();
        assertNull(mozFlexGrow.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#setValue(float)}.
     */
    @Test
    public void testSetValue() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow();
        mozFlexGrow.setValue(0.5F);
        assertEquals(Float.valueOf(0.5F), mozFlexGrow.getValue());
        mozFlexGrow.setAsInherit();
        assertNull(mozFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow();
        mozFlexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), mozFlexGrow.getValue());
        mozFlexGrow.setAsInherit();
        assertNull(mozFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow();
        mozFlexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), mozFlexGrow.getValue());
        mozFlexGrow.setAsInitial();
        assertEquals(MozFlexGrow.INITIAL, mozFlexGrow.getCssValue());
        assertNull(mozFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        MozFlexGrow mozFlexGrow = new MozFlexGrow();
        mozFlexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), mozFlexGrow.getValue());
        mozFlexGrow.setAsInherit();
        assertEquals(MozFlexGrow.INHERIT, mozFlexGrow.getCssValue());
        assertNull(mozFlexGrow.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new MozFlexGrow().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexGrow#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(MozFlexGrow.isValid(".5"));
        assertTrue(MozFlexGrow.isValid("0.5"));
        assertTrue(MozFlexGrow.isValid("0.1"));
        assertTrue(MozFlexGrow.isValid("1"));
        assertTrue(MozFlexGrow.isValid("0"));
        assertTrue(MozFlexGrow.isValid(MozFlexGrow.INITIAL));
        assertTrue(MozFlexGrow.isValid(MozFlexGrow.INHERIT));
        assertFalse(MozFlexGrow.isValid("dfd"));
        assertFalse(MozFlexGrow.isValid("1px"));
        assertFalse(MozFlexGrow.isValid(""));
        assertFalse(MozFlexGrow.isValid("1 1"));
        assertTrue(MozFlexGrow.isValid("1.1"));
//        assertFalse(MozFlexGrow.isValid("-0.5"));
//        assertFalse(MozFlexGrow.isValid("-0"));
//        assertFalse(MozFlexGrow.isValid("-0.0"));
//        assertFalse(MozFlexGrow.isValid("+0.0"));
    }

}
