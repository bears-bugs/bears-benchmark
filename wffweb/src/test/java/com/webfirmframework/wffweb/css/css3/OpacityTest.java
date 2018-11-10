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
public class OpacityTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#Opacity()}.
     */
    @Test
    public void testOpacity() {
        Opacity opacity = new Opacity();
        assertEquals(Float.valueOf(1F), opacity.getValue());
        assertEquals("1.0", opacity.getCssValue());
        
        opacity.setAsInherit();
        assertEquals(Opacity.INHERIT, opacity.getCssValue());
        assertNull(opacity.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#Opacity(java.lang.String)}.
     */
    @Test
    public void testOpacityString() {
        Opacity opacity = new Opacity("0.5");
        assertEquals(Float.valueOf(0.5F), opacity.getValue());
        assertEquals("0.5", opacity.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#Opacity(com.webfirmframework.wffweb.css.css3.Opacity)}.
     */
    @Test
    public void testOpacityOpacity() {
        final Opacity opacity1 = new Opacity("0.5");
        Opacity opacity = new Opacity(opacity1);
        assertEquals(Float.valueOf(0.5F), opacity.getValue());
        assertEquals("0.5", opacity.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#Opacity(float)}.
     */
    @Test
    public void testOpacityFloat() {
        {
            Opacity opacity = new Opacity(0.5F);
            assertEquals(Float.valueOf(0.5F), opacity.getValue());
            assertEquals("0.5", opacity.getCssValue());
            
        }
        {
            final Opacity opacity1 = new Opacity(0.5F);
            Opacity opacity = new Opacity(opacity1);
            assertEquals(Float.valueOf(0.5F), opacity.getValue());
            assertEquals("0.5", opacity.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        Opacity opacity = new Opacity(0.5F);
        assertEquals(CssNameConstants.OPACITY, opacity.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        Opacity opacity = new Opacity();
        assertEquals(Float.valueOf(1F), opacity.getValue());
        assertEquals("1.0", opacity.getCssValue());

        opacity.setAsInherit();
        assertEquals(Opacity.INHERIT, opacity.getCssValue());
        assertNull(opacity.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#toString()}.
     */
    @Test
    public void testToString() {
        Opacity opacity = new Opacity();
        assertEquals(Float.valueOf(1F), opacity.getValue());
        assertEquals(CssNameConstants.OPACITY + ": 1.0", opacity.toString());

        opacity.setAsInherit();
        assertEquals(CssNameConstants.OPACITY + ": " + Opacity.INHERIT,
                opacity.toString());
        assertNull(opacity.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#getValue()}.
     */
    @Test
    public void testGetValue() {
        Opacity opacity = new Opacity();
        assertEquals(Float.valueOf(1F), opacity.getValue());
        opacity.setAsInherit();
        assertNull(opacity.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.Opacity#setValue(float)}.
     */
    @Test
    public void testSetValue() {
        Opacity opacity = new Opacity();
        opacity.setValue(0.5F);
        assertEquals(Float.valueOf(0.5F), opacity.getValue());
        opacity.setAsInherit();
        assertNull(opacity.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        Opacity opacity = new Opacity();
        opacity.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), opacity.getValue());
        opacity.setAsInherit();
        assertNull(opacity.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        Opacity opacity = new Opacity();
        opacity.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), opacity.getValue());
        opacity.setAsInitial();
        assertEquals(Opacity.INITIAL, opacity.getCssValue());
        assertNull(opacity.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        Opacity opacity = new Opacity();
        opacity.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), opacity.getValue());
        opacity.setAsInherit();
        assertEquals(Opacity.INHERIT, opacity.getCssValue());
        assertNull(opacity.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new Opacity().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue2() throws Exception {
        try {
            new Opacity().setCssValue("2");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Opacity#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(Opacity.isValid(".5"));
        assertTrue(Opacity.isValid("0.5"));
        assertTrue(Opacity.isValid("0.1"));
        assertTrue(Opacity.isValid("1"));
        assertTrue(Opacity.isValid("0"));
        assertTrue(Opacity.isValid(Opacity.INITIAL));
        assertTrue(Opacity.isValid(Opacity.INHERIT));
        assertFalse(Opacity.isValid("dfd"));
        assertFalse(Opacity.isValid("1px"));
        assertFalse(Opacity.isValid(""));
        assertFalse(Opacity.isValid("1 1"));
        assertFalse(Opacity.isValid("1.1"));
        assertFalse(Opacity.isValid("-0.5"));
        assertFalse(Opacity.isValid("-0"));
        assertFalse(Opacity.isValid("-0.0"));
        assertFalse(Opacity.isValid("+0.0"));
    }

}
