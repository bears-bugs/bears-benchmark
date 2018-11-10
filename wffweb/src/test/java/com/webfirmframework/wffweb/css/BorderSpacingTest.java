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
 */
package com.webfirmframework.wffweb.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class BorderSpacingTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#BorderSpacing()}.
     */
    @Test
    public void testBorderSpacing() {
        BorderSpacing borderSpacing = new BorderSpacing();
        assertEquals("0px", borderSpacing.getCssValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#BorderSpacing(java.lang.String)}
     * .
     */
    @Test
    public void testBorderSpacingString() {
        BorderSpacing borderSpacing = new BorderSpacing();
        assertEquals("0px", borderSpacing.getCssValue());
        borderSpacing.setCssValue("25px");
        assertEquals("25px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        assertTrue(borderSpacing.getVerticalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#BorderSpacing(com.webfirmframework.wffweb.css.BorderSpacing)}
     * .
     */
    @Test
    public void testBorderSpacingBorderSpacing() {
        BorderSpacing borderSpacing = new BorderSpacing(new BorderSpacing(
                "25px"));

        assertEquals("25px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        assertTrue(borderSpacing.getVerticalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#BorderSpacing(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testBorderSpacingFloatCssLengthUnit() {
        BorderSpacing borderSpacing = new BorderSpacing(25, CssLengthUnit.PX);

        assertEquals("25.0px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        assertTrue(borderSpacing.getVerticalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit, float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnitFloatCssLengthUnit() {
        BorderSpacing borderSpacing = new BorderSpacing();
        borderSpacing.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        assertTrue(borderSpacing.getVerticalValue() == 50);
        assertEquals(CssLengthUnit.CM, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
        
        borderSpacing.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        assertTrue(borderSpacing.getVerticalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnit() {
        BorderSpacing borderSpacing = new BorderSpacing();
        borderSpacing.setValue(30, CssLengthUnit.PX);
        
        assertEquals("30.0px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        assertTrue(borderSpacing.getVerticalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        BorderSpacing borderSpacing = new BorderSpacing();
        assertEquals(CssNameConstants.BORDER_SPACING, borderSpacing.getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        BorderSpacing borderSpacing = new BorderSpacing(BorderSpacing.INHERIT);
        assertEquals(BorderSpacing.INHERIT, borderSpacing.getCssValue());
        borderSpacing.setCssValue("30px");
        assertEquals("30px", borderSpacing.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderSpacing#toString()}
     * .
     */
    @Test
    public void testToString() {
        BorderSpacing borderSpacing = new BorderSpacing();
        borderSpacing.setCssValue("30px");
        assertEquals(CssNameConstants.BORDER_SPACING+": 30px", borderSpacing.toString());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#getHorizontalValue()}.
     */
    @Test
    public void testGetHorizontalValue() {
        BorderSpacing borderSpacing = new BorderSpacing();
        borderSpacing.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        
        borderSpacing.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());


        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#getVerticalValue()}.
     */
    @Test
    public void testGetVerticalValue() {
        BorderSpacing borderSpacing = new BorderSpacing();
        borderSpacing.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", borderSpacing.getCssValue());


        assertTrue(borderSpacing.getVerticalValue() == 50);
        assertEquals(CssLengthUnit.CM, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getVerticalValue());
        
        borderSpacing.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getVerticalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getVerticalValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#getHorizontalUnit()}.
     */
    @Test
    public void testGetHorizontalUnit() {
        BorderSpacing borderSpacing = new BorderSpacing();
        borderSpacing.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", borderSpacing.getCssValue());

        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalUnit());
        
        borderSpacing.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());


        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#getVerticalUnit()}.
     */
    @Test
    public void testGetVerticalUnit() {
        BorderSpacing borderSpacing = new BorderSpacing();
        borderSpacing.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", borderSpacing.getCssValue());

        assertEquals(CssLengthUnit.CM, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getVerticalUnit());
        
        borderSpacing.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", borderSpacing.getCssValue());

        assertEquals(CssLengthUnit.PX, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getVerticalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        BorderSpacing borderSpacing = new BorderSpacing();
        borderSpacing.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 25);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        assertTrue(borderSpacing.getVerticalValue() == 50);
        assertEquals(CssLengthUnit.CM, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
        
        borderSpacing.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", borderSpacing.getCssValue());

        assertTrue(borderSpacing.getHorizontalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getHorizontalUnit());

        assertTrue(borderSpacing.getVerticalValue() == 30);
        assertEquals(CssLengthUnit.PX, borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();

        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        BorderSpacing borderSpacing = new BorderSpacing();
        assertNotNull(borderSpacing.getHorizontalValue());
        assertNotNull(borderSpacing.getVerticalValue());
        assertNotNull(borderSpacing.getHorizontalUnit());
        assertNotNull(borderSpacing.getVerticalUnit());

        borderSpacing.setAsInitial();
        
        assertEquals(BorderSpacing.INITIAL, borderSpacing.getCssValue());
        
        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        BorderSpacing borderSpacing = new BorderSpacing();
        assertNotNull(borderSpacing.getHorizontalValue());
        assertNotNull(borderSpacing.getVerticalValue());
        assertNotNull(borderSpacing.getHorizontalUnit());
        assertNotNull(borderSpacing.getVerticalUnit());

        borderSpacing.setAsInherit();
        
        assertEquals(BorderSpacing.INHERIT, borderSpacing.getCssValue());
        
        assertNull(borderSpacing.getHorizontalValue());
        assertNull(borderSpacing.getVerticalValue());
        assertNull(borderSpacing.getHorizontalUnit());
        assertNull(borderSpacing.getVerticalUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BorderSpacing#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(BorderSpacing.isValid("55px"));
        assertTrue(BorderSpacing.isValid("55px 85cm"));
        assertTrue(BorderSpacing.isValid("55% 85cm"));
        assertTrue(BorderSpacing.isValid("12% 12%"));
        
        assertFalse(BorderSpacing.isValid("12"));
        assertFalse(BorderSpacing.isValid("12 25"));
        assertFalse(BorderSpacing.isValid("sdfdf"));
    }

}
