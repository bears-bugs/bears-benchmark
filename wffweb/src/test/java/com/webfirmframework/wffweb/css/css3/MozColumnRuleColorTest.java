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
public class MozColumnRuleColorTest {

    @Test
    public void testMozColumnRuleColor() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor();
        assertEquals(MozColumnRuleColor.INITIAL, mozColumnRuleColor.getCssValue());
    }

    @Test
    public void testMozColumnRuleColorString() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor("#0000ff");
        assertEquals("#0000ff", mozColumnRuleColor.getCssValue());   
    }

    @Test
    public void testMozColumnRuleColorMozColumnRuleColor() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor("#0000ff");
        MozColumnRuleColor mozColumnRuleColor1 = new MozColumnRuleColor(mozColumnRuleColor);
        assertEquals("#0000ff", mozColumnRuleColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor();
        mozColumnRuleColor.setValue("#0000ff");
        assertEquals("#0000ff", mozColumnRuleColor.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor();
        assertEquals(CssNameConstants.MOZ_COLUMN_RULE_COLOR, mozColumnRuleColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor("#0000ff");
        assertEquals("#0000ff", mozColumnRuleColor.getCssValue());   
    }

    @Test
    public void testToString() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor(
                "#0000ff");
        assertEquals(CssNameConstants.MOZ_COLUMN_RULE_COLOR + ": #0000ff",
                mozColumnRuleColor.toString());
    }

    @Test
    public void testGetValue() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor("#0000ff");
        assertEquals("#0000ff", mozColumnRuleColor.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor();
        mozColumnRuleColor.setCssValue("#0000ff");
        assertEquals("#0000ff", mozColumnRuleColor.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor();
        mozColumnRuleColor.setAsInitial();
        assertEquals(MozColumnRuleColor.INITIAL, mozColumnRuleColor.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        MozColumnRuleColor mozColumnRuleColor = new MozColumnRuleColor();
        mozColumnRuleColor.setAsInherit();
        assertEquals(MozColumnRuleColor.INHERIT, mozColumnRuleColor.getCssValue());   
    }

}
