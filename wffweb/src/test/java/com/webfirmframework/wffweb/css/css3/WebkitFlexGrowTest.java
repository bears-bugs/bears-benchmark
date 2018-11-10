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
public class WebkitFlexGrowTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#WebkitFlexGrow()}.
     */
    @Test
    public void testWebkitFlexGrow() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow();
        assertEquals(Float.valueOf(0F), webkitFlexGrow.getValue());
        assertEquals("0.0", webkitFlexGrow.getCssValue());
        
        webkitFlexGrow.setAsInherit();
        assertEquals(WebkitFlexGrow.INHERIT, webkitFlexGrow.getCssValue());
        assertNull(webkitFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#WebkitFlexGrow(java.lang.String)}.
     */
    @Test
    public void testWebkitFlexGrowString() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow("0.5");
        assertEquals(Float.valueOf(0.5F), webkitFlexGrow.getValue());
        assertEquals("0.5", webkitFlexGrow.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#WebkitFlexGrow(com.webfirmframework.wffweb.css.css3.WebkitFlexGrow)}.
     */
    @Test
    public void testWebkitFlexGrowWebkitFlexGrow() {
        final WebkitFlexGrow webkitFlexGrow1 = new WebkitFlexGrow("0.5");
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow(webkitFlexGrow1);
        assertEquals(Float.valueOf(0.5F), webkitFlexGrow.getValue());
        assertEquals("0.5", webkitFlexGrow.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#WebkitFlexGrow(float)}.
     */
    @Test
    public void testWebkitFlexGrowFloat() {
        {
            WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow(0.5F);
            assertEquals(Float.valueOf(0.5F), webkitFlexGrow.getValue());
            assertEquals("0.5", webkitFlexGrow.getCssValue());
            
        }
        {
            final WebkitFlexGrow webkitFlexGrow1 = new WebkitFlexGrow(0.5F);
            WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow(webkitFlexGrow1);
            assertEquals(Float.valueOf(0.5F), webkitFlexGrow.getValue());
            assertEquals("0.5", webkitFlexGrow.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow(0.5F);
        assertEquals(CssNameConstants.WEBKIT_FLEX_GROW, webkitFlexGrow.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow();
        assertEquals(Float.valueOf(0F), webkitFlexGrow.getValue());
        assertEquals("0.0", webkitFlexGrow.getCssValue());

        webkitFlexGrow.setAsInherit();
        assertEquals(WebkitFlexGrow.INHERIT, webkitFlexGrow.getCssValue());
        assertNull(webkitFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#toString()}.
     */
    @Test
    public void testToString() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow();
        assertEquals(Float.valueOf(0F), webkitFlexGrow.getValue());
        assertEquals(CssNameConstants.WEBKIT_FLEX_GROW + ": 0.0", webkitFlexGrow.toString());

        webkitFlexGrow.setAsInherit();
        assertEquals(CssNameConstants.WEBKIT_FLEX_GROW + ": " + WebkitFlexGrow.INHERIT,
                webkitFlexGrow.toString());
        assertNull(webkitFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#getValue()}.
     */
    @Test
    public void testGetValue() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow();
        assertEquals(Float.valueOf(0F), webkitFlexGrow.getValue());
        webkitFlexGrow.setAsInherit();
        assertNull(webkitFlexGrow.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#setValue(float)}.
     */
    @Test
    public void testSetValue() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow();
        webkitFlexGrow.setValue(0.5F);
        assertEquals(Float.valueOf(0.5F), webkitFlexGrow.getValue());
        webkitFlexGrow.setAsInherit();
        assertNull(webkitFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow();
        webkitFlexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), webkitFlexGrow.getValue());
        webkitFlexGrow.setAsInherit();
        assertNull(webkitFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow();
        webkitFlexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), webkitFlexGrow.getValue());
        webkitFlexGrow.setAsInitial();
        assertEquals(WebkitFlexGrow.INITIAL, webkitFlexGrow.getCssValue());
        assertNull(webkitFlexGrow.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        WebkitFlexGrow webkitFlexGrow = new WebkitFlexGrow();
        webkitFlexGrow.setCssValue("0.5");
        assertEquals(Float.valueOf(0.5F), webkitFlexGrow.getValue());
        webkitFlexGrow.setAsInherit();
        assertEquals(WebkitFlexGrow.INHERIT, webkitFlexGrow.getCssValue());
        assertNull(webkitFlexGrow.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new WebkitFlexGrow().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitFlexGrow#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(WebkitFlexGrow.isValid(".5"));
        assertTrue(WebkitFlexGrow.isValid("0.5"));
        assertTrue(WebkitFlexGrow.isValid("0.1"));
        assertTrue(WebkitFlexGrow.isValid("1"));
        assertTrue(WebkitFlexGrow.isValid("0"));
        assertTrue(WebkitFlexGrow.isValid(WebkitFlexGrow.INITIAL));
        assertTrue(WebkitFlexGrow.isValid(WebkitFlexGrow.INHERIT));
        assertFalse(WebkitFlexGrow.isValid("dfd"));
        assertFalse(WebkitFlexGrow.isValid("1px"));
        assertFalse(WebkitFlexGrow.isValid(""));
        assertFalse(WebkitFlexGrow.isValid("1 1"));
        assertTrue(WebkitFlexGrow.isValid("1.1"));
//        assertFalse(WebkitFlexGrow.isValid("-0.5"));
//        assertFalse(WebkitFlexGrow.isValid("-0"));
//        assertFalse(WebkitFlexGrow.isValid("-0.0"));
//        assertFalse(WebkitFlexGrow.isValid("+0.0"));
    }

}
