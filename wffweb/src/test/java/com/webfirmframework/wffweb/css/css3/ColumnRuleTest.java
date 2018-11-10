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

import org.junit.Test;

import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class ColumnRuleTest {

    @Test
    public void testColumnRule() {
        ColumnRule columnRule = new ColumnRule();
        ColumnRuleColor columnRuleColor = columnRule.getColumnRuleColor();
        ColumnRuleWidth columnRuleWidth = columnRule.getColumnRuleWidth();
        ColumnRuleStyle colunmRuleStyle = columnRule.getColumnRuleStyle();
        
        assertEquals("medium", columnRuleWidth.getCssValue());
        assertEquals("none", colunmRuleStyle.getCssValue());
        assertEquals("white", columnRuleColor.getCssValue());
    }

    @Test
    public void testColumnRuleString() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleColor columnRuleColor = columnRule.getColumnRuleColor();
        ColumnRuleWidth columnRuleWidth = columnRule.getColumnRuleWidth();
        ColumnRuleStyle colunmRuleStyle = columnRule.getColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testColumnRuleColumnRule() {
        ColumnRule columnRule1 = new ColumnRule("50px dashed green");
        ColumnRule columnRule = new ColumnRule(columnRule1);
        ColumnRuleColor columnRuleColor = columnRule.getColumnRuleColor();
        ColumnRuleWidth columnRuleWidth = columnRule.getColumnRuleWidth();
        ColumnRuleStyle colunmRuleStyle = columnRule.getColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testSetValue() {
        ColumnRule columnRule = new ColumnRule();
        columnRule.setValue("50px dashed green");
        ColumnRuleColor columnRuleColor = columnRule.getColumnRuleColor();
        ColumnRuleWidth columnRuleWidth = columnRule.getColumnRuleWidth();
        ColumnRuleStyle colunmRuleStyle = columnRule.getColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testGetCssName() {
        ColumnRule columnRule = new ColumnRule();
        assertEquals(CssNameConstants.COLUMN_RULE, columnRule.getCssName());
    }

    @Test
    public void testGetCssValue() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("yellow");
        columnRule.setColumnRuleColor(columnRuleColor);
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth("150px");
        columnRule.setColumnRuleWidth(columnRuleWidth);
        ColumnRuleStyle colunmRuleStyle = ColumnRuleStyle.GROOVE;
        columnRule.setColumnRuleStyle(colunmRuleStyle);
        
        assertEquals("150px groove yellow", columnRule.getCssValue());
    }

    @Test
    public void testToString() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("yellow");
        columnRule.setColumnRuleColor(columnRuleColor);
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth("150px");
        columnRule.setColumnRuleWidth(columnRuleWidth);
        ColumnRuleStyle colunmRuleStyle = ColumnRuleStyle.GROOVE;
        columnRule.setColumnRuleStyle(colunmRuleStyle);
        
        assertEquals(CssNameConstants.COLUMN_RULE + ": 150px groove yellow",
                columnRule.toString());
    }

    @Test
    public void testGetValue() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("yellow");
        columnRule.setColumnRuleColor(columnRuleColor);
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth("150px");
        columnRule.setColumnRuleWidth(columnRuleWidth);
        ColumnRuleStyle colunmRuleStyle = ColumnRuleStyle.GROOVE;
        columnRule.setColumnRuleStyle(colunmRuleStyle);
        
        assertEquals("150px groove yellow", columnRule.getValue());
    }

    @Test
    public void testSetCssValueString() {
        ColumnRule columnRule = new ColumnRule();
        columnRule.setCssValue("50px dashed green");
        
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testSetAsInitial() {
        ColumnRule columnRule = new ColumnRule();
        columnRule.setAsInitial();
        ColumnRuleColor columnRuleColor = columnRule.getColumnRuleColor();
        ColumnRuleWidth columnRuleWidth = columnRule.getColumnRuleWidth();
        ColumnRuleStyle colunmRuleStyle = columnRule.getColumnRuleStyle();
        assertNull(columnRuleColor);
        assertNull(columnRuleWidth);
        assertNull(colunmRuleStyle);
        assertEquals("initial", columnRule.getCssValue());
        
    }

    @Test
    public void testSetAsInherit() {
        ColumnRule columnRule = new ColumnRule();
        columnRule.setAsInherit();
        ColumnRuleColor columnRuleColor = columnRule.getColumnRuleColor();
        ColumnRuleWidth columnRuleWidth = columnRule.getColumnRuleWidth();
        ColumnRuleStyle colunmRuleStyle = columnRule.getColumnRuleStyle();
        assertNull(columnRuleColor);
        assertNull(columnRuleWidth);
        assertNull(colunmRuleStyle);
        assertEquals("inherit", columnRule.getCssValue());
    }

    @Test
    public void testGetColumnRuleWidth() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleWidth columnRuleWidth = columnRule.getColumnRuleWidth();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
    }

    @Test
    public void testSetColumnRuleWidth() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleWidth columnRuleWidth = new ColumnRuleWidth("150px");
        columnRule.setColumnRuleWidth(columnRuleWidth);
        assertEquals("150px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testGetColumnRuleStyle() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleStyle columnRuleStyle = columnRule.getColumnRuleStyle();
        
        assertEquals("dashed", columnRuleStyle.getCssValue());
    }

    @Test
    public void testSetColumnRuleStyle() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleStyle columnRuleStyle = ColumnRuleStyle.RIDGE;
        columnRule.setColumnRuleStyle(columnRuleStyle);
        assertEquals("50px ridge green", columnRule.getCssValue());
    }

    @Test
    public void testGetColumnRuleColor() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleColor columnRuleColor = columnRule.getColumnRuleColor();
        
        assertEquals("green", columnRuleColor.getCssValue());
//        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testSetColumnRuleColor() {
        ColumnRule columnRule = new ColumnRule("50px dashed green");
        ColumnRuleColor columnRuleColor = new ColumnRuleColor("blue");
        columnRule.setColumnRuleColor(columnRuleColor);
        assertEquals("50px dashed blue", columnRule.getCssValue());
    }

    @Test
    public void testStateChanged() {
//        fail("Not yet implemented"); // TODO
    }
    
    @Test
    public void testColumnRuleColorWidthStyleNull() {
        try {
            ColumnRule columnRule = new ColumnRule("50px dashed green");
            columnRule.setColumnRuleColor(null);
            columnRule.setColumnRuleWidth(null);
            columnRule.setColumnRuleStyle(null);
            assertEquals("inherit", columnRule.getCssValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
