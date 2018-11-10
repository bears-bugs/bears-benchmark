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
* @author WFF
*/
package com.webfirmframework.wffweb.css.css3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class ColumnWidthTest {

    @Test
    public void testColumnWidth() {
        ColumnWidth columnWidth = new ColumnWidth();
        assertEquals(ColumnWidth.AUTO, columnWidth.getCssValue());
    }

    @Test
    public void testColumnWidthString() {
        {
            ColumnWidth columnWidth = new ColumnWidth(ColumnWidth.INITIAL);
            assertEquals(ColumnWidth.INITIAL, columnWidth.getCssValue());
        }
        {
            ColumnWidth columnWidth = new ColumnWidth("50px");
            assertEquals("50px", columnWidth.getCssValue());
        }
    }

    @Test
    public void testColumnWidthColumnWidth() {
        ColumnWidth columnWidth = new ColumnWidth("50px");
        ColumnWidth columnWidth1 = new ColumnWidth(columnWidth);
        assertEquals("50px", columnWidth1.getCssValue());
    }

    @Test
    public void testColumnWidthFloat() {
        ColumnWidth columnWidth = new ColumnWidth(75);
        assertEquals("75.0%", columnWidth.getCssValue());
    }

    @Test
    public void testColumnWidthFloatCssLengthUnit() {
        {
            ColumnWidth columnWidth = new ColumnWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", columnWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, columnWidth.getUnit());
            assertTrue(columnWidth.getValue() == 75);
        }
        {
            ColumnWidth columnWidth = new ColumnWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", columnWidth.getCssValue());
        }
        {
            ColumnWidth columnWidth = new ColumnWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", columnWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            ColumnWidth columnWidth = new ColumnWidth();
            columnWidth.setPercent(75);
            assertEquals("75.0%", columnWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, columnWidth.getUnit());
            assertTrue(columnWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        ColumnWidth columnWidth = new ColumnWidth();
        assertEquals(CssNameConstants.COLUMN_WIDTH, columnWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        ColumnWidth columnWidth = new ColumnWidth();
        columnWidth.setAsInherit();
        assertEquals(ColumnWidth.INHERIT, columnWidth.getCssValue());
        columnWidth.setAsAuto();
        assertEquals(ColumnWidth.AUTO, columnWidth.getCssValue());
    }

    @Test
    public void testToString() {
        ColumnWidth columnWidth = new ColumnWidth(75, CssLengthUnit.EM);
        assertEquals(columnWidth.getCssName()+": 75.0em", columnWidth.toString());
    }

    @Test
    public void testGetValue() {
        ColumnWidth columnWidth = new ColumnWidth();
        columnWidth.setPercent(75);
        assertTrue(columnWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        ColumnWidth columnWidth = new ColumnWidth();
        columnWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, columnWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        ColumnWidth columnWidth = new ColumnWidth();
        columnWidth.setCssValue("75%");
        assertEquals("75%", columnWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, columnWidth.getUnit());
        assertTrue(columnWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        ColumnWidth columnWidth = new ColumnWidth();
        columnWidth.setAsInitial();
        assertEquals(ColumnWidth.INITIAL, columnWidth.getCssValue());
       assertNull(columnWidth.getValue());
       assertNull(columnWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        ColumnWidth columnWidth = new ColumnWidth();
        columnWidth.setAsInherit();
        assertEquals(ColumnWidth.INHERIT, columnWidth.getCssValue());
       assertNull(columnWidth.getValue());
       assertNull(columnWidth.getUnit());
    }

    @Test
    public void testSetAsAuto() {
        ColumnWidth columnWidth = new ColumnWidth();
        columnWidth.setAsInherit();
        columnWidth.setAsAuto();
        assertEquals(ColumnWidth.AUTO, columnWidth.getCssValue());
       assertNull(columnWidth.getValue());
       assertNull(columnWidth.getUnit());
    }
    
    @Test
    public void testIsValid() {
        {
            final boolean valid = ColumnWidth.isValid(ColumnWidth.AUTO);
            assertTrue(valid);
        }
        {
            final boolean valid = ColumnWidth.isValid("45px");
            assertTrue(valid);
            final boolean invalid = ColumnWidth.isValid("55");
           assertFalse(invalid);
        }
        {
            final boolean valid = ColumnWidth.isValid("45em");
            assertTrue(valid);
            final boolean invalid = ColumnWidth.isValid("dfd");
           assertFalse(invalid);
        }
        {
            final boolean valid = ColumnWidth.isValid("45%");
            assertTrue(valid);
            final boolean invalid = ColumnWidth.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = ColumnWidth.isValid("45em");
            assertTrue(valid);
            final boolean invalid = ColumnWidth.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = ColumnWidth.isValid("45rem");
            assertTrue(valid);
        }
    }


}
