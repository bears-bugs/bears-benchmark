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
public class ColumnCountTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#ColumnCount()}.
     */
    @Test
    public void testColumnCount() {
        ColumnCount columnCount = new ColumnCount();
        assertNull(columnCount.getValue());
        assertEquals(ColumnCount.AUTO, columnCount.getCssValue());
        
        columnCount.setAsInherit();
        assertEquals(ColumnCount.INHERIT, columnCount.getCssValue());
        assertNull(columnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#ColumnCount(java.lang.String)}.
     */
    @Test
    public void testColumnCountString() {
        ColumnCount columnCount = new ColumnCount("2");
        assertEquals(Integer.valueOf(2), columnCount.getValue());
        assertEquals("2", columnCount.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#ColumnCount(com.webfirmframework.wffweb.css.css3.ColumnCount)}.
     */
    @Test
    public void testColumnCountColumnCount() {
        final ColumnCount columnCount1 = new ColumnCount("2");
        ColumnCount columnCount = new ColumnCount(columnCount1);
        assertEquals(Integer.valueOf(2), columnCount.getValue());
        assertEquals("2", columnCount.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#ColumnCount(integer)}.
     */
    @Test
    public void testColumnCountInteger() {
        {
            ColumnCount columnCount = new ColumnCount(2);
            assertEquals(Integer.valueOf(2), columnCount.getValue());
            assertEquals("2", columnCount.getCssValue());
            
        }
        {
            final ColumnCount columnCount1 = new ColumnCount(2);
            ColumnCount columnCount = new ColumnCount(columnCount1);
            assertEquals(Integer.valueOf(2), columnCount.getValue());
            assertEquals("2", columnCount.getCssValue());
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        ColumnCount columnCount = new ColumnCount(2);
        assertEquals(CssNameConstants.COLUMN_COUNT, columnCount.getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        ColumnCount columnCount = new ColumnCount();
        assertNull(columnCount.getValue());
        assertEquals(ColumnCount.AUTO, columnCount.getCssValue());

        columnCount.setAsInherit();
        assertEquals(ColumnCount.INHERIT, columnCount.getCssValue());
        assertNull(columnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#toString()}.
     */
    @Test
    public void testToString() {
        ColumnCount columnCount = new ColumnCount();
        assertNull(columnCount.getValue());
        assertEquals(ColumnCount.AUTO, columnCount.getCssValue());
        assertEquals(CssNameConstants.COLUMN_COUNT + ": " + ColumnCount.AUTO,
                columnCount.toString());

        columnCount.setAsInherit();
        assertEquals(
                CssNameConstants.COLUMN_COUNT + ": " + ColumnCount.INHERIT,
                columnCount.toString());
        assertNull(columnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#getValue()}.
     */
    @Test
    public void testGetValue() {
        ColumnCount columnCount = new ColumnCount();
        assertNull(columnCount.getValue());
        assertEquals(ColumnCount.AUTO, columnCount.getCssValue());
        columnCount.setAsInherit();
        assertNull(columnCount.getValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.ColumnCount#setValue(integer)}.
     */
    @Test
    public void testSetValue() {
        ColumnCount columnCount = new ColumnCount();
        columnCount.setValue(2);
        assertEquals(Integer.valueOf(2), columnCount.getValue());
        columnCount.setAsInherit();
        assertNull(columnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        ColumnCount columnCount = new ColumnCount();
        columnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), columnCount.getValue());
        columnCount.setAsInherit();
        assertNull(columnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        ColumnCount columnCount = new ColumnCount();
        columnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), columnCount.getValue());
        columnCount.setAsInitial();
        assertEquals(ColumnCount.INITIAL, columnCount.getCssValue());
        assertNull(columnCount.getValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        ColumnCount columnCount = new ColumnCount();
        columnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), columnCount.getValue());
        columnCount.setAsInherit();
        assertEquals(ColumnCount.INHERIT, columnCount.getCssValue());
        assertNull(columnCount.getValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#setAsAuto()}.
     */
    @Test
    public void testSetAsAuto() {
        ColumnCount columnCount = new ColumnCount();
        columnCount.setCssValue("2");
        assertEquals(Integer.valueOf(2), columnCount.getValue());
        columnCount.setAsAuto();
        assertEquals(ColumnCount.AUTO, columnCount.getCssValue());
        assertNull(columnCount.getValue());
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        try {
            new ColumnCount().setCssValue("1px");
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue2() throws Exception {
        try {
            new ColumnCount().setCssValue("2px");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.ColumnCount#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        
        assertTrue(ColumnCount.isValid("1"));
        assertTrue(ColumnCount.isValid("0"));
        assertTrue(ColumnCount.isValid(ColumnCount.INITIAL));
        assertTrue(ColumnCount.isValid(ColumnCount.INHERIT));
        assertTrue(ColumnCount.isValid(ColumnCount.AUTO));
        
        assertFalse(ColumnCount.isValid(".5"));
        assertFalse(ColumnCount.isValid("0.5"));
        assertFalse(ColumnCount.isValid("0.1"));
        assertFalse(ColumnCount.isValid("dfd"));
        assertFalse(ColumnCount.isValid("1px"));
        assertFalse(ColumnCount.isValid(""));
        assertFalse(ColumnCount.isValid("1 1"));
        assertFalse(ColumnCount.isValid("1.1"));
        assertFalse(ColumnCount.isValid("-0.5"));
        assertFalse(ColumnCount.isValid("-0"));
        assertFalse(ColumnCount.isValid("-0.0"));
        assertFalse(ColumnCount.isValid("+0.0"));
    }

}
