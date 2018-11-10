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
public class WebkitColumnWidthTest {

    @Test
    public void testWebkitColumnWidth() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        assertEquals(WebkitColumnWidth.AUTO, webkitColumnWidth.getCssValue());
    }

    @Test
    public void testWebkitColumnWidthString() {
        {
            WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth(WebkitColumnWidth.INITIAL);
            assertEquals(WebkitColumnWidth.INITIAL, webkitColumnWidth.getCssValue());
        }
        {
            WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth("50px");
            assertEquals("50px", webkitColumnWidth.getCssValue());
        }
    }

    @Test
    public void testWebkitColumnWidthWebkitColumnWidth() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth("50px");
        WebkitColumnWidth webkitColumnWidth1 = new WebkitColumnWidth(webkitColumnWidth);
        assertEquals("50px", webkitColumnWidth1.getCssValue());
    }

    @Test
    public void testWebkitColumnWidthFloat() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth(75);
        assertEquals("75.0%", webkitColumnWidth.getCssValue());
    }

    @Test
    public void testWebkitColumnWidthFloatCssLengthUnit() {
        {
            WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", webkitColumnWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, webkitColumnWidth.getUnit());
            assertTrue(webkitColumnWidth.getValue() == 75);
        }
        {
            WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", webkitColumnWidth.getCssValue());
        }
        {
            WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", webkitColumnWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
            webkitColumnWidth.setPercent(75);
            assertEquals("75.0%", webkitColumnWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, webkitColumnWidth.getUnit());
            assertTrue(webkitColumnWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        assertEquals(CssNameConstants.WEBKIT_COLUMN_WIDTH, webkitColumnWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        webkitColumnWidth.setAsInherit();
        assertEquals(WebkitColumnWidth.INHERIT, webkitColumnWidth.getCssValue());
        webkitColumnWidth.setAsAuto();
        assertEquals(WebkitColumnWidth.AUTO, webkitColumnWidth.getCssValue());
    }

    @Test
    public void testToString() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth(75, CssLengthUnit.EM);
        assertEquals(webkitColumnWidth.getCssName()+": 75.0em", webkitColumnWidth.toString());
    }

    @Test
    public void testGetValue() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        webkitColumnWidth.setPercent(75);
        assertTrue(webkitColumnWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        webkitColumnWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, webkitColumnWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        webkitColumnWidth.setCssValue("75%");
        assertEquals("75%", webkitColumnWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, webkitColumnWidth.getUnit());
        assertTrue(webkitColumnWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        webkitColumnWidth.setAsInitial();
        assertEquals(WebkitColumnWidth.INITIAL, webkitColumnWidth.getCssValue());
       assertNull(webkitColumnWidth.getValue());
       assertNull(webkitColumnWidth.getUnit());
    }

    @Test
    public void testSetAsInherit() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        webkitColumnWidth.setAsInherit();
        assertEquals(WebkitColumnWidth.INHERIT, webkitColumnWidth.getCssValue());
       assertNull(webkitColumnWidth.getValue());
       assertNull(webkitColumnWidth.getUnit());
    }

    @Test
    public void testSetAsAuto() {
        WebkitColumnWidth webkitColumnWidth = new WebkitColumnWidth();
        webkitColumnWidth.setAsInherit();
        webkitColumnWidth.setAsAuto();
        assertEquals(WebkitColumnWidth.AUTO, webkitColumnWidth.getCssValue());
       assertNull(webkitColumnWidth.getValue());
       assertNull(webkitColumnWidth.getUnit());
    }

}
