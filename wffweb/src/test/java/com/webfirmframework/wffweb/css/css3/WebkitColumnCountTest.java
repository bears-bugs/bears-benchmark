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
public class WebkitColumnCountTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#WebkitColumnCount()}.
     */
    @Test
    public void testWebkitColumnCount() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        assertNull(webkitColumnCount.getValue());
        assertEquals(WebkitColumnCount.AUTO, webkitColumnCount.getCssValue());
        
        webkitColumnCount.setAsInherit();
        assertEquals(WebkitColumnCount.INHERIT, webkitColumnCount.getCssValue());
        assertNull(webkitColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#WebkitColumnCount(java.lang.String)}.
     */
    @Test
    public void testWebkitColumnCountString() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount("2");
        assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
        assertEquals("2", webkitColumnCount.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#WebkitColumnCount(com.webfirmframework.wffweb.css.css3.WebkitColumnCount)}.
     */
    @Test
    public void testWebkitColumnCountWebkitColumnCount() {
        final WebkitColumnCount webkitColumnCount1 = new WebkitColumnCount("2");
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount(webkitColumnCount1);
        assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
        assertEquals("2", webkitColumnCount.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#WebkitColumnCount(integer)}.
     */
    @Test
    public void testWebkitColumnCountInteger() {
        {
            WebkitColumnCount webkitColumnCount = new WebkitColumnCount(2);
            assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
            assertEquals("2", webkitColumnCount.getCssValue());
            
        }
        {
            final WebkitColumnCount webkitColumnCount1 = new WebkitColumnCount(2);
            WebkitColumnCount webkitColumnCount = new WebkitColumnCount(webkitColumnCount1);
            assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
            assertEquals("2", webkitColumnCount.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount(2);
        assertEquals(CssNameConstants.WEBKIT_COLUMN_COUNT, webkitColumnCount.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        assertNull(webkitColumnCount.getValue());
        assertEquals(WebkitColumnCount.AUTO, webkitColumnCount.getCssValue());

        webkitColumnCount.setAsInherit();
        assertEquals(WebkitColumnCount.INHERIT, webkitColumnCount.getCssValue());
        assertNull(webkitColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#toString()}.
     */
    @Test
    public void testToString() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        assertNull(webkitColumnCount.getValue());
        assertEquals(WebkitColumnCount.AUTO, webkitColumnCount.getCssValue());
        assertEquals(CssNameConstants.WEBKIT_COLUMN_COUNT + ": " + WebkitColumnCount.AUTO,
                webkitColumnCount.toString());

        webkitColumnCount.setAsInherit();
        assertEquals(
                CssNameConstants.WEBKIT_COLUMN_COUNT + ": " + WebkitColumnCount.INHERIT,
                webkitColumnCount.toString());
        assertNull(webkitColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#getValue()}.
     */
    @Test
    public void testGetValue() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        assertNull(webkitColumnCount.getValue());
        assertEquals(WebkitColumnCount.AUTO, webkitColumnCount.getCssValue());
        webkitColumnCount.setAsInherit();
        assertNull(webkitColumnCount.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#setValue(integer)}.
     */
    @Test
    public void testSetValue() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        webkitColumnCount.setValue(2);
        assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
        webkitColumnCount.setAsInherit();
        assertNull(webkitColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        webkitColumnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
        webkitColumnCount.setAsInherit();
        assertNull(webkitColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        webkitColumnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
        webkitColumnCount.setAsInitial();
        assertEquals(WebkitColumnCount.INITIAL, webkitColumnCount.getCssValue());
        assertNull(webkitColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        webkitColumnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
        webkitColumnCount.setAsInherit();
        assertEquals(WebkitColumnCount.INHERIT, webkitColumnCount.getCssValue());
        assertNull(webkitColumnCount.getValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#setAsAuto()}.
     */
    @Test
    public void testSetAsAuto() {
        WebkitColumnCount webkitColumnCount = new WebkitColumnCount();
        webkitColumnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), webkitColumnCount.getValue());
        webkitColumnCount.setAsAuto();
        assertEquals(WebkitColumnCount.AUTO, webkitColumnCount.getCssValue());
        assertNull(webkitColumnCount.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new WebkitColumnCount().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue2() throws Exception {
        try {
            new WebkitColumnCount().setCssValue("2px");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.WebkitColumnCount#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        
        assertTrue(WebkitColumnCount.isValid("1"));
        assertTrue(WebkitColumnCount.isValid("0"));
        assertTrue(WebkitColumnCount.isValid(WebkitColumnCount.INITIAL));
        assertTrue(WebkitColumnCount.isValid(WebkitColumnCount.INHERIT));
        assertTrue(WebkitColumnCount.isValid(WebkitColumnCount.AUTO));
        
        assertFalse(WebkitColumnCount.isValid(".5"));
        assertFalse(WebkitColumnCount.isValid("0.5"));
        assertFalse(WebkitColumnCount.isValid("0.1"));
        assertFalse(WebkitColumnCount.isValid("dfd"));
        assertFalse(WebkitColumnCount.isValid("1px"));
        assertFalse(WebkitColumnCount.isValid(""));
        assertFalse(WebkitColumnCount.isValid("1 1"));
        assertFalse(WebkitColumnCount.isValid("1.1"));
        assertFalse(WebkitColumnCount.isValid("-0.5"));
        assertFalse(WebkitColumnCount.isValid("-0"));
        assertFalse(WebkitColumnCount.isValid("-0.0"));
        assertFalse(WebkitColumnCount.isValid("+0.0"));
    }

}
