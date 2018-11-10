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
public class MozFlexShrinkTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#MozFlexShrink()}.
     */
    @Test
    public void testMozFlexShrink() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink();
        assertEquals(Float.valueOf(1F), mozFlexShrink.getValue());
        assertEquals("1.0", mozFlexShrink.getCssValue());
        
        mozFlexShrink.setAsInherit();
        assertEquals(MozFlexShrink.INHERIT, mozFlexShrink.getCssValue());
        assertNull(mozFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#MozFlexShrink(java.lang.String)}.
     */
    @Test
    public void testMozFlexShrinkString() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink("0.5");
        assertEquals(Float.valueOf(0.5F), mozFlexShrink.getValue());
        assertEquals("0.5", mozFlexShrink.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#MozFlexShrink(com.webfirmframework.wffweb.css.css3.MozFlexShrink)}.
     */
    @Test
    public void testMozFlexShrinkMozFlexShrink() {
        final MozFlexShrink mozFlexShrink1 = new MozFlexShrink("0.5");
        MozFlexShrink mozFlexShrink = new MozFlexShrink(mozFlexShrink1);
        assertEquals(Float.valueOf(0.5F), mozFlexShrink.getValue());
        assertEquals("0.5", mozFlexShrink.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#MozFlexShrink(float)}.
     */
    @Test
    public void testMozFlexShrinkFloat() {
        {
            MozFlexShrink mozFlexShrink = new MozFlexShrink(0.5F);
            assertEquals(Float.valueOf(0.5F), mozFlexShrink.getValue());
            assertEquals("0.5", mozFlexShrink.getCssValue());
            
        }
        {
            final MozFlexShrink mozFlexShrink1 = new MozFlexShrink(0.5F);
            MozFlexShrink mozFlexShrink = new MozFlexShrink(mozFlexShrink1);
            assertEquals(Float.valueOf(0.5F), mozFlexShrink.getValue());
            assertEquals("0.5", mozFlexShrink.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink(0.5F);
        assertEquals(CssNameConstants.MOZ_FLEX_SHRINK, mozFlexShrink.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink();
        assertEquals(Float.valueOf(1F), mozFlexShrink.getValue());
        assertEquals("1.0", mozFlexShrink.getCssValue());

        mozFlexShrink.setAsInherit();
        assertEquals(MozFlexShrink.INHERIT, mozFlexShrink.getCssValue());
        assertNull(mozFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#toString()}.
     */
    @Test
    public void testToString() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink();
        assertEquals(Float.valueOf(1F), mozFlexShrink.getValue());
        assertEquals(CssNameConstants.MOZ_FLEX_SHRINK + ": 1.0", mozFlexShrink.toString());

        mozFlexShrink.setAsInherit();
        assertEquals(CssNameConstants.MOZ_FLEX_SHRINK + ": " + MozFlexShrink.INHERIT,
                mozFlexShrink.toString());
        assertNull(mozFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#getValue()}.
     */
    @Test
    public void testGetValue() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink();
        assertEquals(Float.valueOf(1F), mozFlexShrink.getValue());
        mozFlexShrink.setAsInherit();
        assertNull(mozFlexShrink.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#setValue(float)}.
     */
    @Test
    public void testSetValue() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink();
        mozFlexShrink.setValue(0.5F);
        assertEquals(Float.valueOf(0.5F), mozFlexShrink.getValue());
        mozFlexShrink.setAsInherit();
        assertNull(mozFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink();
        mozFlexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), mozFlexShrink.getValue());
        mozFlexShrink.setAsInherit();
        assertNull(mozFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink();
        mozFlexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), mozFlexShrink.getValue());
        mozFlexShrink.setAsInitial();
        assertEquals(MozFlexShrink.INITIAL, mozFlexShrink.getCssValue());
        assertNull(mozFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        MozFlexShrink mozFlexShrink = new MozFlexShrink();
        mozFlexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), mozFlexShrink.getValue());
        mozFlexShrink.setAsInherit();
        assertEquals(MozFlexShrink.INHERIT, mozFlexShrink.getCssValue());
        assertNull(mozFlexShrink.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new MozFlexShrink().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozFlexShrink#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(MozFlexShrink.isValid(".5"));
        assertTrue(MozFlexShrink.isValid("0.5"));
        assertTrue(MozFlexShrink.isValid("0.1"));
        assertTrue(MozFlexShrink.isValid("1"));
        assertTrue(MozFlexShrink.isValid("0"));
        assertTrue(MozFlexShrink.isValid(MozFlexShrink.INITIAL));
        assertTrue(MozFlexShrink.isValid(MozFlexShrink.INHERIT));
        assertFalse(MozFlexShrink.isValid("dfd"));
        assertFalse(MozFlexShrink.isValid("1px"));
        assertFalse(MozFlexShrink.isValid(""));
        assertFalse(MozFlexShrink.isValid("1 1"));
        assertTrue(MozFlexShrink.isValid("1.1"));
//        assertFalse(MozFlexShrink.isValid("-0.5"));
//        assertFalse(MozFlexShrink.isValid("-0"));
//        assertFalse(MozFlexShrink.isValid("-0.0"));
//        assertFalse(MozFlexShrink.isValid("+0.0"));
    }

}
