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
public class MozColumnWidthTest {

    @Test
    public void testMozColumnWidth() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        assertEquals(MozColumnWidth.AUTO, webkitColumnWidth.getCssValue());
    }

    @Test
    public void testMozColumnWidthString() {
        {
            MozColumnWidth webkitColumnWidth = new MozColumnWidth(MozColumnWidth.INITIAL);
            assertEquals(MozColumnWidth.INITIAL, webkitColumnWidth.getCssValue());
        }
        {
            MozColumnWidth webkitColumnWidth = new MozColumnWidth("50px");
            assertEquals("50px", webkitColumnWidth.getCssValue());
        }
    }

    @Test
    public void testMozColumnWidthMozColumnWidth() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth("50px");
        MozColumnWidth webkitColumnWidth1 = new MozColumnWidth(webkitColumnWidth);
        assertEquals("50px", webkitColumnWidth1.getCssValue());
    }

    @Test
    public void testMozColumnWidthFloat() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth(75);
        assertEquals("75.0%", webkitColumnWidth.getCssValue());
    }

    @Test
    public void testMozColumnWidthFloatCssLengthUnit() {
        {
            MozColumnWidth webkitColumnWidth = new MozColumnWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", webkitColumnWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, webkitColumnWidth.getUnit());
            assertTrue(webkitColumnWidth.getValue() == 75);
        }
        {
            MozColumnWidth webkitColumnWidth = new MozColumnWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", webkitColumnWidth.getCssValue());
        }
        {
            MozColumnWidth webkitColumnWidth = new MozColumnWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", webkitColumnWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            MozColumnWidth webkitColumnWidth = new MozColumnWidth();
            webkitColumnWidth.setPercent(75);
            assertEquals("75.0%", webkitColumnWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, webkitColumnWidth.getUnit());
            assertTrue(webkitColumnWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        assertEquals(CssNameConstants.MOZ_COLUMN_WIDTH, webkitColumnWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        webkitColumnWidth.setAsInherit();
        assertEquals(MozColumnWidth.INHERIT, webkitColumnWidth.getCssValue());
        webkitColumnWidth.setAsAuto();
        assertEquals(MozColumnWidth.AUTO, webkitColumnWidth.getCssValue());
    }

    @Test
    public void testToString() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth(75, CssLengthUnit.EM);
        assertEquals(webkitColumnWidth.getCssName()+": 75.0em", webkitColumnWidth.toString());
    }

    @Test
    public void testGetValue() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        webkitColumnWidth.setPercent(75);
        assertTrue(webkitColumnWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        webkitColumnWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, webkitColumnWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        webkitColumnWidth.setCssValue("75%");
        assertEquals("75%", webkitColumnWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, webkitColumnWidth.getUnit());
        assertTrue(webkitColumnWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        webkitColumnWidth.setAsInitial();
        assertEquals(MozColumnWidth.INITIAL, webkitColumnWidth.getCssValue());
       assertNull(webkitColumnWidth.getValue());
       assertNull(webkitColumnWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        webkitColumnWidth.setAsInherit();
        assertEquals(MozColumnWidth.INHERIT, webkitColumnWidth.getCssValue());
       assertNull(webkitColumnWidth.getValue());
       assertNull(webkitColumnWidth.getUnit());
    }

    @Test
    public void testSetAsAuto() {
        MozColumnWidth webkitColumnWidth = new MozColumnWidth();
        webkitColumnWidth.setAsInherit();
        webkitColumnWidth.setAsAuto();
        assertEquals(MozColumnWidth.AUTO, webkitColumnWidth.getCssValue());
       assertNull(webkitColumnWidth.getValue());
       assertNull(webkitColumnWidth.getUnit());
    }

}
