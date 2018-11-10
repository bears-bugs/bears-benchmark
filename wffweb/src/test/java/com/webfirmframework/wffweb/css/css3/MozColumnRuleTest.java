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
public class MozColumnRuleTest {

    @Test
    public void testMozColumnRule() {
        MozColumnRule columnRule = new MozColumnRule();
        MozColumnRuleColor columnRuleColor = columnRule.getMozColumnRuleColor();
        MozColumnRuleWidth columnRuleWidth = columnRule.getMozColumnRuleWidth();
        MozColumnRuleStyle colunmRuleStyle = columnRule.getMozColumnRuleStyle();
        
        assertEquals("medium", columnRuleWidth.getCssValue());
        assertEquals("none", colunmRuleStyle.getCssValue());
        assertEquals("white", columnRuleColor.getCssValue());
    }

    @Test
    public void testMozColumnRuleString() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleColor columnRuleColor = columnRule.getMozColumnRuleColor();
        MozColumnRuleWidth columnRuleWidth = columnRule.getMozColumnRuleWidth();
        MozColumnRuleStyle colunmRuleStyle = columnRule.getMozColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testMozColumnRuleMozColumnRule() {
        MozColumnRule columnRule1 = new MozColumnRule("50px dashed green");
        MozColumnRule columnRule = new MozColumnRule(columnRule1);
        MozColumnRuleColor columnRuleColor = columnRule.getMozColumnRuleColor();
        MozColumnRuleWidth columnRuleWidth = columnRule.getMozColumnRuleWidth();
        MozColumnRuleStyle colunmRuleStyle = columnRule.getMozColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testSetValue() {
        MozColumnRule columnRule = new MozColumnRule();
        columnRule.setValue("50px dashed green");
        MozColumnRuleColor columnRuleColor = columnRule.getMozColumnRuleColor();
        MozColumnRuleWidth columnRuleWidth = columnRule.getMozColumnRuleWidth();
        MozColumnRuleStyle colunmRuleStyle = columnRule.getMozColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testGetCssName() {
        MozColumnRule columnRule = new MozColumnRule();
        assertEquals(CssNameConstants.MOZ_COLUMN_RULE, columnRule.getCssName());
    }

    @Test
    public void testGetCssValue() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleColor columnRuleColor = new MozColumnRuleColor("yellow");
        columnRule.setMozColumnRuleColor(columnRuleColor);
        MozColumnRuleWidth columnRuleWidth = new MozColumnRuleWidth("150px");
        columnRule.setMozColumnRuleWidth(columnRuleWidth);
        MozColumnRuleStyle colunmRuleStyle = MozColumnRuleStyle.GROOVE;
        columnRule.setMozColumnRuleStyle(colunmRuleStyle);
        
        assertEquals("150px groove yellow", columnRule.getCssValue());
    }

    @Test
    public void testToString() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleColor columnRuleColor = new MozColumnRuleColor("yellow");
        columnRule.setMozColumnRuleColor(columnRuleColor);
        MozColumnRuleWidth columnRuleWidth = new MozColumnRuleWidth("150px");
        columnRule.setMozColumnRuleWidth(columnRuleWidth);
        MozColumnRuleStyle colunmRuleStyle = MozColumnRuleStyle.GROOVE;
        columnRule.setMozColumnRuleStyle(colunmRuleStyle);
        
        assertEquals(CssNameConstants.MOZ_COLUMN_RULE + ": 150px groove yellow",
                columnRule.toString());
    }

    @Test
    public void testGetValue() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleColor columnRuleColor = new MozColumnRuleColor("yellow");
        columnRule.setMozColumnRuleColor(columnRuleColor);
        MozColumnRuleWidth columnRuleWidth = new MozColumnRuleWidth("150px");
        columnRule.setMozColumnRuleWidth(columnRuleWidth);
        MozColumnRuleStyle colunmRuleStyle = MozColumnRuleStyle.GROOVE;
        columnRule.setMozColumnRuleStyle(colunmRuleStyle);
        
        assertEquals("150px groove yellow", columnRule.getValue());
    }

    @Test
    public void testSetCssValueString() {
        MozColumnRule columnRule = new MozColumnRule();
        columnRule.setCssValue("50px dashed green");
        
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testSetAsInitial() {
        MozColumnRule columnRule = new MozColumnRule();
        columnRule.setAsInitial();
        MozColumnRuleColor columnRuleColor = columnRule.getMozColumnRuleColor();
        MozColumnRuleWidth columnRuleWidth = columnRule.getMozColumnRuleWidth();
        MozColumnRuleStyle colunmRuleStyle = columnRule.getMozColumnRuleStyle();
        assertNull(columnRuleColor);
        assertNull(columnRuleWidth);
        assertNull(colunmRuleStyle);
        assertEquals("initial", columnRule.getCssValue());
        
    }

    @Test
    public void testSetAsInherit() {
        MozColumnRule columnRule = new MozColumnRule();
        columnRule.setAsInherit();
        MozColumnRuleColor columnRuleColor = columnRule.getMozColumnRuleColor();
        MozColumnRuleWidth columnRuleWidth = columnRule.getMozColumnRuleWidth();
        MozColumnRuleStyle colunmRuleStyle = columnRule.getMozColumnRuleStyle();
        assertNull(columnRuleColor);
        assertNull(columnRuleWidth);
        assertNull(colunmRuleStyle);
        assertEquals("inherit", columnRule.getCssValue());
    }

    @Test
    public void testGetMozColumnRuleWidth() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleWidth columnRuleWidth = columnRule.getMozColumnRuleWidth();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
    }

    @Test
    public void testSetMozColumnRuleWidth() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleWidth columnRuleWidth = new MozColumnRuleWidth("150px");
        columnRule.setMozColumnRuleWidth(columnRuleWidth);
        assertEquals("150px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testGetMozColumnRuleStyle() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleStyle columnRuleStyle = columnRule.getMozColumnRuleStyle();
        
        assertEquals("dashed", columnRuleStyle.getCssValue());
    }

    @Test
    public void testSetMozColumnRuleStyle() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleStyle columnRuleStyle = MozColumnRuleStyle.RIDGE;
        columnRule.setMozColumnRuleStyle(columnRuleStyle);
        assertEquals("50px ridge green", columnRule.getCssValue());
    }

    @Test
    public void testGetMozColumnRuleColor() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleColor columnRuleColor = columnRule.getMozColumnRuleColor();
        
        assertEquals("green", columnRuleColor.getCssValue());
//        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testSetMozColumnRuleColor() {
        MozColumnRule columnRule = new MozColumnRule("50px dashed green");
        MozColumnRuleColor columnRuleColor = new MozColumnRuleColor("blue");
        columnRule.setMozColumnRuleColor(columnRuleColor);
        assertEquals("50px dashed blue", columnRule.getCssValue());
    }

    @Test
    public void testStateChanged() {
//        fail("Not yet implemented"); // TODO
    }
    
    @Test
    public void testColumn() {
        try {
            MozColumnRule columnRule = new MozColumnRule("50px dashed green");
            columnRule.setMozColumnRuleColor(null);
            columnRule.setMozColumnRuleWidth(null);
            columnRule.setMozColumnRuleStyle(null);
            assertEquals("inherit", columnRule.getCssValue());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
