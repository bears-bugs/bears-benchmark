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
public class WebkitFlexShrinkTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#WebkitFlexShrink()}.
     */
    @Test
    public void testWebkitFlexShrink() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink();
        assertEquals(Float.valueOf(1F), webkitFlexShrink.getValue());
        assertEquals("1.0", webkitFlexShrink.getCssValue());
        
        webkitFlexShrink.setAsInherit();
        assertEquals(WebkitFlexShrink.INHERIT, webkitFlexShrink.getCssValue());
        assertNull(webkitFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#WebkitFlexShrink(java.lang.String)}.
     */
    @Test
    public void testWebkitFlexShrinkString() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink("0.5");
        assertEquals(Float.valueOf(0.5F), webkitFlexShrink.getValue());
        assertEquals("0.5", webkitFlexShrink.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#WebkitFlexShrink(com.webfirmframework.wffweb.css.css3.WebkitFlexShrink)}.
     */
    @Test
    public void testWebkitFlexShrinkWebkitFlexShrink() {
        final WebkitFlexShrink webkitFlexShrink1 = new WebkitFlexShrink("0.5");
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink(webkitFlexShrink1);
        assertEquals(Float.valueOf(0.5F), webkitFlexShrink.getValue());
        assertEquals("0.5", webkitFlexShrink.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#WebkitFlexShrink(float)}.
     */
    @Test
    public void testWebkitFlexShrinkFloat() {
        {
            WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink(0.5F);
            assertEquals(Float.valueOf(0.5F), webkitFlexShrink.getValue());
            assertEquals("0.5", webkitFlexShrink.getCssValue());
            
        }
        {
            final WebkitFlexShrink webkitFlexShrink1 = new WebkitFlexShrink(0.5F);
            WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink(webkitFlexShrink1);
            assertEquals(Float.valueOf(0.5F), webkitFlexShrink.getValue());
            assertEquals("0.5", webkitFlexShrink.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink(0.5F);
        assertEquals(CssNameConstants.WEBKIT_FLEX_SHRINK, webkitFlexShrink.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink();
        assertEquals(Float.valueOf(1F), webkitFlexShrink.getValue());
        assertEquals("1.0", webkitFlexShrink.getCssValue());

        webkitFlexShrink.setAsInherit();
        assertEquals(WebkitFlexShrink.INHERIT, webkitFlexShrink.getCssValue());
        assertNull(webkitFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#toString()}.
     */
    @Test
    public void testToString() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink();
        assertEquals(Float.valueOf(1F), webkitFlexShrink.getValue());
        assertEquals(CssNameConstants.WEBKIT_FLEX_SHRINK + ": 1.0", webkitFlexShrink.toString());

        webkitFlexShrink.setAsInherit();
        assertEquals(CssNameConstants.WEBKIT_FLEX_SHRINK + ": " + WebkitFlexShrink.INHERIT,
                webkitFlexShrink.toString());
        assertNull(webkitFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#getValue()}.
     */
    @Test
    public void testGetValue() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink();
        assertEquals(Float.valueOf(1F), webkitFlexShrink.getValue());
        webkitFlexShrink.setAsInherit();
        assertNull(webkitFlexShrink.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#setValue(float)}.
     */
    @Test
    public void testSetValue() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink();
        webkitFlexShrink.setValue(0.5F);
        assertEquals(Float.valueOf(0.5F), webkitFlexShrink.getValue());
        webkitFlexShrink.setAsInherit();
        assertNull(webkitFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink();
        webkitFlexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), webkitFlexShrink.getValue());
        webkitFlexShrink.setAsInherit();
        assertNull(webkitFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink();
        webkitFlexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), webkitFlexShrink.getValue());
        webkitFlexShrink.setAsInitial();
        assertEquals(WebkitFlexShrink.INITIAL, webkitFlexShrink.getCssValue());
        assertNull(webkitFlexShrink.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        WebkitFlexShrink webkitFlexShrink = new WebkitFlexShrink();
        webkitFlexShrink.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), webkitFlexShrink.getValue());
        webkitFlexShrink.setAsInherit();
        assertEquals(WebkitFlexShrink.INHERIT, webkitFlexShrink.getCssValue());
        assertNull(webkitFlexShrink.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new WebkitFlexShrink().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexShrink#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(WebkitFlexShrink.isValid(".5"));
        assertTrue(WebkitFlexShrink.isValid("0.5"));
        assertTrue(WebkitFlexShrink.isValid("0.1"));
        assertTrue(WebkitFlexShrink.isValid("1"));
        assertTrue(WebkitFlexShrink.isValid("0"));
        assertTrue(WebkitFlexShrink.isValid(WebkitFlexShrink.INITIAL));
        assertTrue(WebkitFlexShrink.isValid(WebkitFlexShrink.INHERIT));
        assertFalse(WebkitFlexShrink.isValid("dfd"));
        assertFalse(WebkitFlexShrink.isValid("1px"));
        assertFalse(WebkitFlexShrink.isValid(""));
        assertFalse(WebkitFlexShrink.isValid("1 1"));
        assertTrue(WebkitFlexShrink.isValid("1.1"));
//        assertFalse(WebkitFlexShrink.isValid("-0.5"));
//        assertFalse(WebkitFlexShrink.isValid("-0"));
//        assertFalse(WebkitFlexShrink.isValid("-0.0"));
//        assertFalse(WebkitFlexShrink.isValid("+0.0"));
    }

}
