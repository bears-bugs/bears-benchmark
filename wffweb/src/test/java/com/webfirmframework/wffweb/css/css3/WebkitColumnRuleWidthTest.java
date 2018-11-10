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
public class WebkitColumnRuleWidthTest {

    @Test
    public void testWebkitColumnRuleWidth() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        assertEquals(WebkitColumnRuleWidth.MEDIUM, webkitColumnRuleWidth.getCssValue());
    }

    @Test
    public void testWebkitColumnRuleWidthString() {
        {
            WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth(WebkitColumnRuleWidth.INITIAL);
            assertEquals(WebkitColumnRuleWidth.INITIAL, webkitColumnRuleWidth.getCssValue());
        }
        {
            WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth("50px");
            assertEquals("50px", webkitColumnRuleWidth.getCssValue());
        }
    }

    @Test
    public void testWebkitColumnRuleWidthWebkitColumnRuleWidth() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth("50px");
        WebkitColumnRuleWidth webkitColumnRuleWidth1 = new WebkitColumnRuleWidth(webkitColumnRuleWidth);
        assertEquals("50px", webkitColumnRuleWidth1.getCssValue());
    }

    @Test
    public void testWebkitColumnRuleWidthFloat() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth(75);
        assertEquals("75.0%", webkitColumnRuleWidth.getCssValue());
    }

    @Test
    public void testWebkitColumnRuleWidthFloatCssLengthUnit() {
        {
            WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth(75, CssLengthUnit.PER);
            assertEquals("75.0%", webkitColumnRuleWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, webkitColumnRuleWidth.getUnit());
            assertTrue(webkitColumnRuleWidth.getValue() == 75);
        }
        {
            WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth(75, CssLengthUnit.CH);
            assertEquals("75.0ch", webkitColumnRuleWidth.getCssValue());
        }
        {
            WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth(75, CssLengthUnit.EM);
            assertEquals("75.0em", webkitColumnRuleWidth.getCssValue());
        }
    }

    @Test
    public void testSetPercent() {
        {
            WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
            webkitColumnRuleWidth.setPercent(75);
            assertEquals("75.0%", webkitColumnRuleWidth.getCssValue());
            assertEquals(CssLengthUnit.PER, webkitColumnRuleWidth.getUnit());
            assertTrue(webkitColumnRuleWidth.getValue() == 75);
        }
    }

    @Test
    public void testGetCssName() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        assertEquals(CssNameConstants.WEBKIT_COLUMN_RULE_WIDTH, webkitColumnRuleWidth.getCssName());
    }

    @Test
    public void testGetCssValue() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setAsInherit();
        assertEquals(WebkitColumnRuleWidth.INHERIT, webkitColumnRuleWidth.getCssValue());
        webkitColumnRuleWidth.setAsMedium();
        assertEquals(WebkitColumnRuleWidth.MEDIUM, webkitColumnRuleWidth.getCssValue());
    }

    @Test
    public void testToString() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth(75, CssLengthUnit.EM);
        assertEquals(webkitColumnRuleWidth.getCssName()+": 75.0em", webkitColumnRuleWidth.toString());
    }

    @Test
    public void testGetValue() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setPercent(75);
        assertTrue(webkitColumnRuleWidth.getValue() == 75);
    }

    @Test
    public void testGetUnit() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setPercent(75);
        assertEquals(CssLengthUnit.PER, webkitColumnRuleWidth.getUnit());
    }

    @Test
    public void testSetCssValueString() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setCssValue("75%");
        assertEquals("75%", webkitColumnRuleWidth.getCssValue());
        assertEquals(CssLengthUnit.PER, webkitColumnRuleWidth.getUnit());
        assertTrue(webkitColumnRuleWidth.getValue() == 75);
    }

    @Test
    public void testSetAsInitial() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setAsInitial();
        assertEquals(WebkitColumnRuleWidth.INITIAL, webkitColumnRuleWidth.getCssValue());
    }

    @Test
    public void testSetAsInherit() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setAsInherit();
        assertEquals(WebkitColumnRuleWidth.INHERIT, webkitColumnRuleWidth.getCssValue());
       assertNull(webkitColumnRuleWidth.getValue());
       assertNull(webkitColumnRuleWidth.getUnit());
    }

    @Test
    public void testSetAsMedium() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setAsInherit();
        webkitColumnRuleWidth.setAsMedium();
        assertEquals(WebkitColumnRuleWidth.MEDIUM, webkitColumnRuleWidth.getCssValue());
       assertNull(webkitColumnRuleWidth.getValue());
       assertNull(webkitColumnRuleWidth.getUnit());
    }
    
    @Test
    public void testSetAsThick() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setAsInherit();
        webkitColumnRuleWidth.setAsThick();
        assertEquals(WebkitColumnRuleWidth.THICK, webkitColumnRuleWidth.getCssValue());
       assertNull(webkitColumnRuleWidth.getValue());
       assertNull(webkitColumnRuleWidth.getUnit());
    }
    
    @Test
    public void testSetAsThin() {
        WebkitColumnRuleWidth webkitColumnRuleWidth = new WebkitColumnRuleWidth();
        webkitColumnRuleWidth.setAsInherit();
        try {
            webkitColumnRuleWidth.setAsThin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(WebkitColumnRuleWidth.THIN, webkitColumnRuleWidth.getCssValue());
       assertNull(webkitColumnRuleWidth.getValue());
       assertNull(webkitColumnRuleWidth.getUnit());
    }

}
