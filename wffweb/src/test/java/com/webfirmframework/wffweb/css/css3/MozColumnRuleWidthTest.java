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
public class MozColumnRuleWidthTest {

    @Test
    public void testMozColumnRuleWidth() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        assertEquals(MozColumnRuleWidth.MEDIUM, mozColumnRuleWidth.getCssValue());
    }

    @Test
    public void testMozColumnRuleWidthString() {
        {
            MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth(MozColumnRuleWidth.INITIAL);
            assertEquals(MozColumnRuleWidth.INITIAL, mozColumnRuleWidth.getCssValue());
        }
        {
            MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth("50px");
            assertEquals("50px", mozColumnRuleWidth.getCssValue());
        }
    }

    @Test
    public void testMozColumnRuleWidthMozColumnRuleWidth() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth("50px");
        MozColumnRuleWidth mozColumnRuleWidth1 = new MozColumnRuleWidth(mozColumnRuleWidth);
        assertEquals("50px", mozColumnRuleWidth1.getCssValue());
    }

    @Test
    public void testMozColumnRuleWidthFloat() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth(75);
        assertEquals("75.0%", mozColumnRuleWidth.getCssValue());
    }

    @Test
    public void testMozColumnRuleWidthFloatCssLengthUnit() {
        {
            MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", mozColumnRuleWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, mozColumnRuleWidth.getUnit());
            assertTrue(mozColumnRuleWidth.getValue() == 75);
        }
        {
            MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", mozColumnRuleWidth.getCssValue());
        }
        {
            MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", mozColumnRuleWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
            mozColumnRuleWidth.setPercent(75);
            assertEquals("75.0%", mozColumnRuleWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, mozColumnRuleWidth.getUnit());
            assertTrue(mozColumnRuleWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        assertEquals(CssNameConstants.MOZ_COLUMN_RULE_WIDTH, mozColumnRuleWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setAsInherit();
        assertEquals(MozColumnRuleWidth.INHERIT, mozColumnRuleWidth.getCssValue());
        mozColumnRuleWidth.setAsMedium();
        assertEquals(MozColumnRuleWidth.MEDIUM, mozColumnRuleWidth.getCssValue());
    }

    @Test
    public void testToString() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth(75, CssLengthUnit.EM);
        assertEquals(mozColumnRuleWidth.getCssName()+": 75.0em", mozColumnRuleWidth.toString());
    }

    @Test
    public void testGetValue() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setPercent(75);
        assertTrue(mozColumnRuleWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, mozColumnRuleWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setCssValue("75%");
        assertEquals("75%", mozColumnRuleWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, mozColumnRuleWidth.getUnit());
        assertTrue(mozColumnRuleWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setAsInitial();
        assertEquals(MozColumnRuleWidth.INITIAL, mozColumnRuleWidth.getCssValue());
    }

    @Test
    public void testSetAsInherit() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setAsInherit();
        assertEquals(MozColumnRuleWidth.INHERIT, mozColumnRuleWidth.getCssValue());
       assertNull(mozColumnRuleWidth.getValue());
       assertNull(mozColumnRuleWidth.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setAsInherit();
        mozColumnRuleWidth.setAsMedium();
        assertEquals(MozColumnRuleWidth.MEDIUM, mozColumnRuleWidth.getCssValue());
       assertNull(mozColumnRuleWidth.getValue());
       assertNull(mozColumnRuleWidth.getUnit());
    }
    
    @Test
    public void testSetAsThick() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setAsInherit();
        mozColumnRuleWidth.setAsThick();
        assertEquals(MozColumnRuleWidth.THICK, mozColumnRuleWidth.getCssValue());
       assertNull(mozColumnRuleWidth.getValue());
       assertNull(mozColumnRuleWidth.getUnit());
    }
    
    @Test
    public void testSetAsThin() {
        MozColumnRuleWidth mozColumnRuleWidth = new MozColumnRuleWidth();
        mozColumnRuleWidth.setAsInherit();
        try {
            mozColumnRuleWidth.setAsThin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(MozColumnRuleWidth.THIN, mozColumnRuleWidth.getCssValue());
    }

}
