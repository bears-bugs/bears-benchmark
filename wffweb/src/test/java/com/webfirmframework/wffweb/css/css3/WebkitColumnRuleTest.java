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
public class WebkitColumnRuleTest {

    @Test
    public void testWebkitColumnRule() {
        WebkitColumnRule columnRule = new WebkitColumnRule();
        WebkitColumnRuleColor columnRuleColor = columnRule.getWebkitColumnRuleColor();
        WebkitColumnRuleWidth columnRuleWidth = columnRule.getWebkitColumnRuleWidth();
        WebkitColumnRuleStyle colunmRuleStyle = columnRule.getWebkitColumnRuleStyle();
        
        assertEquals("medium", columnRuleWidth.getCssValue());
        assertEquals("none", colunmRuleStyle.getCssValue());
        assertEquals("white", columnRuleColor.getCssValue());
    }

    @Test
    public void testWebkitColumnRuleString() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleColor columnRuleColor = columnRule.getWebkitColumnRuleColor();
        WebkitColumnRuleWidth columnRuleWidth = columnRule.getWebkitColumnRuleWidth();
        WebkitColumnRuleStyle colunmRuleStyle = columnRule.getWebkitColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testWebkitColumnRuleWebkitColumnRule() {
        WebkitColumnRule columnRule1 = new WebkitColumnRule("50px dashed green");
        WebkitColumnRule columnRule = new WebkitColumnRule(columnRule1);
        WebkitColumnRuleColor columnRuleColor = columnRule.getWebkitColumnRuleColor();
        WebkitColumnRuleWidth columnRuleWidth = columnRule.getWebkitColumnRuleWidth();
        WebkitColumnRuleStyle colunmRuleStyle = columnRule.getWebkitColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testSetValue() {
        WebkitColumnRule columnRule = new WebkitColumnRule();
        columnRule.setValue("50px dashed green");
        WebkitColumnRuleColor columnRuleColor = columnRule.getWebkitColumnRuleColor();
        WebkitColumnRuleWidth columnRuleWidth = columnRule.getWebkitColumnRuleWidth();
        WebkitColumnRuleStyle colunmRuleStyle = columnRule.getWebkitColumnRuleStyle();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
        assertEquals("dashed", colunmRuleStyle.getCssValue());
        assertEquals("green", columnRuleColor.getCssValue());
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testGetCssName() {
        WebkitColumnRule columnRule = new WebkitColumnRule();
        assertEquals(CssNameConstants.WEBKIT_COLUMN_RULE, columnRule.getCssName());
    }

    @Test
    public void testGetCssValue() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleColor columnRuleColor = new WebkitColumnRuleColor("yellow");
        columnRule.setWebkitColumnRuleColor(columnRuleColor);
        WebkitColumnRuleWidth columnRuleWidth = new WebkitColumnRuleWidth("150px");
        columnRule.setWebkitColumnRuleWidth(columnRuleWidth);
        WebkitColumnRuleStyle colunmRuleStyle = WebkitColumnRuleStyle.GROOVE;
        columnRule.setWebkitColumnRuleStyle(colunmRuleStyle);
        
        assertEquals("150px groove yellow", columnRule.getCssValue());
    }

    @Test
    public void testToString() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleColor columnRuleColor = new WebkitColumnRuleColor("yellow");
        columnRule.setWebkitColumnRuleColor(columnRuleColor);
        WebkitColumnRuleWidth columnRuleWidth = new WebkitColumnRuleWidth("150px");
        columnRule.setWebkitColumnRuleWidth(columnRuleWidth);
        WebkitColumnRuleStyle colunmRuleStyle = WebkitColumnRuleStyle.GROOVE;
        columnRule.setWebkitColumnRuleStyle(colunmRuleStyle);
        
        assertEquals(CssNameConstants.WEBKIT_COLUMN_RULE + ": 150px groove yellow",
                columnRule.toString());
    }

    @Test
    public void testGetValue() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleColor columnRuleColor = new WebkitColumnRuleColor("yellow");
        columnRule.setWebkitColumnRuleColor(columnRuleColor);
        WebkitColumnRuleWidth columnRuleWidth = new WebkitColumnRuleWidth("150px");
        columnRule.setWebkitColumnRuleWidth(columnRuleWidth);
        WebkitColumnRuleStyle colunmRuleStyle = WebkitColumnRuleStyle.GROOVE;
        columnRule.setWebkitColumnRuleStyle(colunmRuleStyle);
        
        assertEquals("150px groove yellow", columnRule.getValue());
    }

    @Test
    public void testSetCssValueString() {
        WebkitColumnRule columnRule = new WebkitColumnRule();
        columnRule.setCssValue("50px dashed green");
        
        assertEquals("50px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testSetAsInitial() {
        WebkitColumnRule columnRule = new WebkitColumnRule();
        columnRule.setAsInitial();
        WebkitColumnRuleColor columnRuleColor = columnRule.getWebkitColumnRuleColor();
        WebkitColumnRuleWidth columnRuleWidth = columnRule.getWebkitColumnRuleWidth();
        WebkitColumnRuleStyle colunmRuleStyle = columnRule.getWebkitColumnRuleStyle();
        assertNull(columnRuleColor);
        assertNull(columnRuleWidth);
        assertNull(colunmRuleStyle);
        assertEquals("initial", columnRule.getCssValue());
        
    }

    @Test
    public void testSetAsInherit() {
        WebkitColumnRule columnRule = new WebkitColumnRule();
        columnRule.setAsInherit();
        WebkitColumnRuleColor columnRuleColor = columnRule.getWebkitColumnRuleColor();
        WebkitColumnRuleWidth columnRuleWidth = columnRule.getWebkitColumnRuleWidth();
        WebkitColumnRuleStyle colunmRuleStyle = columnRule.getWebkitColumnRuleStyle();
        assertNull(columnRuleColor);
        assertNull(columnRuleWidth);
        assertNull(colunmRuleStyle);
        assertEquals("inherit", columnRule.getCssValue());
    }

    @Test
    public void testGetWebkitColumnRuleWidth() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleWidth columnRuleWidth = columnRule.getWebkitColumnRuleWidth();
        
        assertEquals("50px", columnRuleWidth.getCssValue());
    }

    @Test
    public void testSetWebkitColumnRuleWidth() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleWidth columnRuleWidth = new WebkitColumnRuleWidth("150px");
        columnRule.setWebkitColumnRuleWidth(columnRuleWidth);
        assertEquals("150px dashed green", columnRule.getCssValue());
    }

    @Test
    public void testGetWebkitColumnRuleStyle() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleStyle columnRuleStyle = columnRule.getWebkitColumnRuleStyle();
        
        assertEquals("dashed", columnRuleStyle.getCssValue());
    }

    @Test
    public void testSetWebkitColumnRuleStyle() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleStyle columnRuleStyle = WebkitColumnRuleStyle.RIDGE;
        columnRule.setWebkitColumnRuleStyle(columnRuleStyle);
        assertEquals("50px ridge green", columnRule.getCssValue());
    }

    @Test
    public void testGetWebkitColumnRuleColor() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleColor columnRuleColor = columnRule.getWebkitColumnRuleColor();
        
        assertEquals("green", columnRuleColor.getCssValue());
//        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testSetWebkitColumnRuleColor() {
        WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
        WebkitColumnRuleColor columnRuleColor = new WebkitColumnRuleColor("blue");
        columnRule.setWebkitColumnRuleColor(columnRuleColor);
        assertEquals("50px dashed blue", columnRule.getCssValue());
    }

    @Test
    public void testStateChanged() {
//        fail("Not yet implemented"); // TODO
    }
    
    @Test
    public void testColumn() {
        try {
            WebkitColumnRule columnRule = new WebkitColumnRule("50px dashed green");
            columnRule.setWebkitColumnRuleColor(null);
            columnRule.setWebkitColumnRuleWidth(null);
            columnRule.setWebkitColumnRuleStyle(null);
            assertEquals("inherit", columnRule.getCssValue());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
