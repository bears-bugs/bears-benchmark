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

import org.junit.Test;

import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class WebkitColumnRuleColorTest {

    @Test
    public void testWebkitColumnRuleColor() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor();
        assertEquals(WebkitColumnRuleColor.INITIAL, webkitColumnRuleColor.getCssValue());
    }

    @Test
    public void testWebkitColumnRuleColorString() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor("#0000ff");
        assertEquals("#0000ff", webkitColumnRuleColor.getCssValue());   
    }

    @Test
    public void testWebkitColumnRuleColorWebkitColumnRuleColor() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor("#0000ff");
        WebkitColumnRuleColor webkitColumnRuleColor1 = new WebkitColumnRuleColor(webkitColumnRuleColor);
        assertEquals("#0000ff", webkitColumnRuleColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor();
        webkitColumnRuleColor.setValue("#0000ff");
        assertEquals("#0000ff", webkitColumnRuleColor.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor();
        assertEquals(CssNameConstants.WEBKIT_COLUMN_RULE_COLOR, webkitColumnRuleColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor("#0000ff");
        assertEquals("#0000ff", webkitColumnRuleColor.getCssValue());   
    }

    @Test
    public void testToString() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor(
                "#0000ff");
        assertEquals(CssNameConstants.WEBKIT_COLUMN_RULE_COLOR + ": #0000ff",
                webkitColumnRuleColor.toString());
    }

    @Test
    public void testGetValue() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor("#0000ff");
        assertEquals("#0000ff", webkitColumnRuleColor.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor();
        webkitColumnRuleColor.setCssValue("#0000ff");
        assertEquals("#0000ff", webkitColumnRuleColor.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor();
        webkitColumnRuleColor.setAsInitial();
        assertEquals(WebkitColumnRuleColor.INITIAL, webkitColumnRuleColor.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        WebkitColumnRuleColor webkitColumnRuleColor = new WebkitColumnRuleColor();
        webkitColumnRuleColor.setAsInherit();
        assertEquals(WebkitColumnRuleColor.INHERIT, webkitColumnRuleColor.getCssValue());   
    }

}
