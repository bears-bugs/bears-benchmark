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
public class ColumnRuleWidthTest {

    @Test
    public void testColumnRuleWidth() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        assertEquals(ColumnRuleWidth.MEDIUM, columnRuleWidth.getCssValue());
    }

    @Test
    public void testColumnRuleWidthString() {
        {
            ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth(ColumnRuleWidth.INITIAL);
            assertEquals(ColumnRuleWidth.INITIAL, columnRuleWidth.getCssValue());
        }
        {
            ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth("50px");
            assertEquals("50px", columnRuleWidth.getCssValue());
        }
    }

    @Test
    public void testColumnRuleWidthColumnRuleWidth() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth("50px");
        ColumnRuleWidth columnRuleWidth1 = new ColumnRuleWidth(columnRuleWidth);
        assertEquals("50px", columnRuleWidth1.getCssValue());
    }

    @Test
    public void testColumnRuleWidthFloat() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth(75);
        assertEquals("75.0%", columnRuleWidth.getCssValue());
    }

    @Test
    public void testColumnRuleWidthFloatCssLengthUnit() {
        {
            ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", columnRuleWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, columnRuleWidth.getUnit());
            assertTrue(columnRuleWidth.getValue() == 75);
        }
        {
            ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", columnRuleWidth.getCssValue());
        }
        {
            ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", columnRuleWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
            columnRuleWidth.setPercent(75);
            assertEquals("75.0%", columnRuleWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, columnRuleWidth.getUnit());
            assertTrue(columnRuleWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        assertEquals(CssNameConstants.COLUMN_RULE_WIDTH, columnRuleWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setAsInherit();
        assertEquals(ColumnRuleWidth.INHERIT, columnRuleWidth.getCssValue());
        columnRuleWidth.setAsMedium();
        assertEquals(ColumnRuleWidth.MEDIUM, columnRuleWidth.getCssValue());
    }

    @Test
    public void testToString() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth(75, CssLengthUnit.EM);
        assertEquals(columnRuleWidth.getCssName()+": 75.0em", columnRuleWidth.toString());
    }

    @Test
    public void testGetValue() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setPercent(75);
        assertTrue(columnRuleWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, columnRuleWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setCssValue("75%");
        assertEquals("75%", columnRuleWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, columnRuleWidth.getUnit());
        assertTrue(columnRuleWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setAsInitial();
        assertEquals(ColumnRuleWidth.INITIAL, columnRuleWidth.getCssValue());
       assertNull(columnRuleWidth.getValue());
       assertNull(columnRuleWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setAsInherit();
        assertEquals(ColumnRuleWidth.INHERIT, columnRuleWidth.getCssValue());
       assertNull(columnRuleWidth.getValue());
       assertNull(columnRuleWidth.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setAsInherit();
        columnRuleWidth.setAsMedium();
        assertEquals(ColumnRuleWidth.MEDIUM, columnRuleWidth.getCssValue());
       assertNull(columnRuleWidth.getValue());
       assertNull(columnRuleWidth.getUnit());
    }
    
    @Test
    public void testSetAsThick() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setAsInherit();
        columnRuleWidth.setAsThick();
        assertEquals(ColumnRuleWidth.THICK, columnRuleWidth.getCssValue());
       assertNull(columnRuleWidth.getValue());
       assertNull(columnRuleWidth.getUnit());
    }
    
    @Test
    public void testSetAsThin() {
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth();
        columnRuleWidth.setAsInherit();
        try {
            columnRuleWidth.setAsThin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(ColumnRuleWidth.THIN, columnRuleWidth.getCssValue());
    }

}
