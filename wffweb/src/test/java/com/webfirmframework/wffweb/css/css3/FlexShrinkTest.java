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
public class FlexShrinkTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#FlexShrink()}.
     */
    @Test
    public void testFlexShrink() {
        FlexShrink flexShrink = new FlexShrink();
        assertEquals(Float.valueOf(1F), flexShrink.getValue());
        assertEquals("1.0", flexShrink.getCssValue());
        
        flexShrink.setAsInherit();
        assertEquals(FlexShrink.INHERIT, flexShrink.getCssValue());
        assertNull(flexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#FlexShrink(java.lang.String)}.
     */
    @Test
    public void testFlexShrinkString() {
        FlexShrink flexShrink = new FlexShrink("0.5");
        assertEquals(Float.valueOf(0.5F), flexShrink.getValue());
        assertEquals("0.5", flexShrink.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#FlexShrink(com.webfirmframework.wffweb.css.css3.FlexShrink)}.
     */
    @Test
    public void testFlexShrinkFlexShrink() {
        final FlexShrink flexShrink1 = new FlexShrink("0.5");
        FlexShrink flexShrink = new FlexShrink(flexShrink1);
        assertEquals(Float.valueOf(0.5F), flexShrink.getValue());
        assertEquals("0.5", flexShrink.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#FlexShrink(float)}.
     */
    @Test
    public void testFlexShrinkFloat() {
        {
            FlexShrink flexShrink = new FlexShrink(0.5F);
            assertEquals(Float.valueOf(0.5F), flexShrink.getValue());
            assertEquals("0.5", flexShrink.getCssValue());
            
        }
        {
            final FlexShrink flexShrink1 = new FlexShrink(0.5F);
            FlexShrink flexShrink = new FlexShrink(flexShrink1);
            assertEquals(Float.valueOf(0.5F), flexShrink.getValue());
            assertEquals("0.5", flexShrink.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        FlexShrink flexShrink = new FlexShrink(0.5F);
        assertEquals(CssNameConstants.FLEX_SHRINK, flexShrink.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        FlexShrink flexShrink = new FlexShrink();
        assertEquals(Float.valueOf(1F), flexShrink.getValue());
        assertEquals("1.0", flexShrink.getCssValue());

        flexShrink.setAsInherit();
        assertEquals(FlexShrink.INHERIT, flexShrink.getCssValue());
        assertNull(flexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#toString()}.
     */
    @Test
    public void testToString() {
        FlexShrink flexShrink = new FlexShrink();
        assertEquals(Float.valueOf(1F), flexShrink.getValue());
        assertEquals(CssNameConstants.FLEX_SHRINK + ": 1.0", flexShrink.toString());

        flexShrink.setAsInherit();
        assertEquals(CssNameConstants.FLEX_SHRINK + ": " + FlexShrink.INHERIT,
                flexShrink.toString());
        assertNull(flexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#getValue()}.
     */
    @Test
    public void testGetValue() {
        FlexShrink flexShrink = new FlexShrink();
        assertEquals(Float.valueOf(1F), flexShrink.getValue());
        flexShrink.setAsInherit();
        assertNull(flexShrink.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.FlexShrink#setValue(float)}.
     */
    @Test
    public void testSetValue() {
        FlexShrink flexShrink = new FlexShrink();
        flexShrink.setValue(0.5F);
        assertEquals(Float.valueOf(0.5F), flexShrink.getValue());
        flexShrink.setAsInherit();
        assertNull(flexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        FlexShrink flexShrink = new FlexShrink();
        flexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), flexShrink.getValue());
        flexShrink.setAsInherit();
        assertNull(flexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        FlexShrink flexShrink = new FlexShrink();
        flexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), flexShrink.getValue());
        flexShrink.setAsInitial();
        assertEquals(FlexShrink.INITIAL, flexShrink.getCssValue());
        assertNull(flexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        FlexShrink flexShrink = new FlexShrink();
        flexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), flexShrink.getValue());
        flexShrink.setAsInherit();
        assertEquals(FlexShrink.INHERIT, flexShrink.getCssValue());
        assertNull(flexShrink.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new FlexShrink().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.FlexShrink#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(FlexShrink.isValid(".5"));
        assertTrue(FlexShrink.isValid("0.5"));
        assertTrue(FlexShrink.isValid("0.1"));
        assertTrue(FlexShrink.isValid("1"));
        assertTrue(FlexShrink.isValid("0"));
        assertTrue(FlexShrink.isValid(FlexShrink.INITIAL));
        assertTrue(FlexShrink.isValid(FlexShrink.INHERIT));
        assertFalse(FlexShrink.isValid("dfd"));
        assertFalse(FlexShrink.isValid("1px"));
        assertFalse(FlexShrink.isValid(""));
        assertFalse(FlexShrink.isValid("1 1"));
        assertTrue(FlexShrink.isValid("1.1"));
//        assertFalse(FlexShrink.isValid("-0.5"));
//        assertFalse(FlexShrink.isValid("-0"));
//        assertFalse(FlexShrink.isValid("-0.0"));
//        assertFalse(FlexShrink.isValid("+0.0"));
    }

}
