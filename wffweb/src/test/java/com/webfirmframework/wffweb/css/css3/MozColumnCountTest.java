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
public class MozColumnCountTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#MozColumnCount()}.
     */
    @Test
    public void testMozColumnCount() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        assertNull(mozColumnCount.getValue());
        assertEquals(MozColumnCount.AUTO, mozColumnCount.getCssValue());
        
        mozColumnCount.setAsInherit();
        assertEquals(MozColumnCount.INHERIT, mozColumnCount.getCssValue());
        assertNull(mozColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#MozColumnCount(java.lang.String)}.
     */
    @Test
    public void testMozColumnCountString() {
        MozColumnCount mozColumnCount = new MozColumnCount("2");
        assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
        assertEquals("2", mozColumnCount.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#MozColumnCount(com.webfirmframework.wffweb.css.css3.MozColumnCount)}.
     */
    @Test
    public void testMozColumnCountMozColumnCount() {
        final MozColumnCount mozColumnCount1 = new MozColumnCount("2");
        MozColumnCount mozColumnCount = new MozColumnCount(mozColumnCount1);
        assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
        assertEquals("2", mozColumnCount.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#MozColumnCount(integer)}.
     */
    @Test
    public void testMozColumnCountInteger() {
        {
            MozColumnCount mozColumnCount = new MozColumnCount(2);
            assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
            assertEquals("2", mozColumnCount.getCssValue());
            
        }
        {
            final MozColumnCount mozColumnCount1 = new MozColumnCount(2);
            MozColumnCount mozColumnCount = new MozColumnCount(mozColumnCount1);
            assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
            assertEquals("2", mozColumnCount.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        MozColumnCount mozColumnCount = new MozColumnCount(2);
        assertEquals(CssNameConstants.MOZ_COLUMN_COUNT, mozColumnCount.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        assertNull(mozColumnCount.getValue());
        assertEquals(MozColumnCount.AUTO, mozColumnCount.getCssValue());

        mozColumnCount.setAsInherit();
        assertEquals(MozColumnCount.INHERIT, mozColumnCount.getCssValue());
        assertNull(mozColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#toString()}.
     */
    @Test
    public void testToString() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        assertNull(mozColumnCount.getValue());
        assertEquals(MozColumnCount.AUTO, mozColumnCount.getCssValue());
        assertEquals(CssNameConstants.MOZ_COLUMN_COUNT + ": " + MozColumnCount.AUTO,
                mozColumnCount.toString());

        mozColumnCount.setAsInherit();
        assertEquals(
                CssNameConstants.MOZ_COLUMN_COUNT + ": " + MozColumnCount.INHERIT,
                mozColumnCount.toString());
        assertNull(mozColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#getValue()}.
     */
    @Test
    public void testGetValue() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        assertNull(mozColumnCount.getValue());
        assertEquals(MozColumnCount.AUTO, mozColumnCount.getCssValue());
        mozColumnCount.setAsInherit();
        assertNull(mozColumnCount.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#setValue(integer)}.
     */
    @Test
    public void testSetValue() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        mozColumnCount.setValue(2);
        assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
        mozColumnCount.setAsInherit();
        assertNull(mozColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        mozColumnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
        mozColumnCount.setAsInherit();
        assertNull(mozColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        mozColumnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
        mozColumnCount.setAsInitial();
        assertEquals(MozColumnCount.INITIAL, mozColumnCount.getCssValue());
        assertNull(mozColumnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        mozColumnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
        mozColumnCount.setAsInherit();
        assertEquals(MozColumnCount.INHERIT, mozColumnCount.getCssValue());
        assertNull(mozColumnCount.getValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#setAsAuto()}.
     */
    @Test
    public void testSetAsAuto() {
        MozColumnCount mozColumnCount = new MozColumnCount();
        mozColumnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), mozColumnCount.getValue());
        mozColumnCount.setAsAuto();
        assertEquals(MozColumnCount.AUTO, mozColumnCount.getCssValue());
        assertNull(mozColumnCount.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new MozColumnCount().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue2() throws Exception {
        try {
            new MozColumnCount().setCssValue("2px");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.MozColumnCount#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        
        assertTrue(MozColumnCount.isValid("1"));
        assertTrue(MozColumnCount.isValid("0"));
        assertTrue(MozColumnCount.isValid(MozColumnCount.INITIAL));
        assertTrue(MozColumnCount.isValid(MozColumnCount.INHERIT));
        assertTrue(MozColumnCount.isValid(MozColumnCount.AUTO));
        
        assertFalse(MozColumnCount.isValid(".5"));
        assertFalse(MozColumnCount.isValid("0.5"));
        assertFalse(MozColumnCount.isValid("0.1"));
        assertFalse(MozColumnCount.isValid("dfd"));
        assertFalse(MozColumnCount.isValid("1px"));
        assertFalse(MozColumnCount.isValid(""));
        assertFalse(MozColumnCount.isValid("1 1"));
        assertFalse(MozColumnCount.isValid("1.1"));
        assertFalse(MozColumnCount.isValid("-0.5"));
        assertFalse(MozColumnCount.isValid("-0"));
        assertFalse(MozColumnCount.isValid("-0.0"));
        assertFalse(MozColumnCount.isValid("+0.0"));
    }

}
