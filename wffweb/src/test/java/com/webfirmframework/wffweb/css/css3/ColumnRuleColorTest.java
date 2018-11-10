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
public class ColumnRuleColorTest {

    @Test
    public void testColumnRuleColor() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor();
        assertEquals(ColumnRuleColor.INITIAL, columnRuleColor.getCssValue());
    }

    @Test
    public void testColumnRuleColorString() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("#0000ff");
        assertEquals("#0000ff", columnRuleColor.getCssValue());   
    }

    @Test
    public void testColumnRuleColorColumnRuleColor() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("#0000ff");
        ColumnRuleColor columnRuleColor1 = new ColumnRuleColor(columnRuleColor);
        assertEquals("#0000ff", columnRuleColor1.getCssValue());
    }

    @Test
    public void testSetValue() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor();
        columnRuleColor.setValue("#0000ff");
        assertEquals("#0000ff", columnRuleColor.getCssValue());   
    }

    @Test
    public void testGetCssName() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor();
        assertEquals(CssNameConstants.COLUMN_RULE_COLOR, columnRuleColor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("#0000ff");
        assertEquals("#0000ff", columnRuleColor.getCssValue());   
    }

    @Test
    public void testToString() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("#0000ff");
        assertEquals(CssNameConstants.COLUMN_RULE_COLOR + ": #0000ff",
                columnRuleColor.toString());
    }

    @Test
    public void testGetValue() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("#0000ff");
        assertEquals("#0000ff", columnRuleColor.getValue());   
    }

    @Test
    public void testSetCssValueString() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor();
        columnRuleColor.setCssValue("#0000ff");
        assertEquals("#0000ff", columnRuleColor.getCssValue());   
    }

    @Test
    public void testSetAsInitial() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor();
        columnRuleColor.setAsInitial();
        assertEquals(columnRuleColor.INITIAL, columnRuleColor.getCssValue());   
    }

    @Test
    public void testSetAsInherit() {
        ColumnRuleColor columnRuleColor = new ColumnRuleColor();
        columnRuleColor.setAsInherit();
        assertEquals(columnRuleColor.INHERIT, columnRuleColor.getCssValue());   
    }

}
