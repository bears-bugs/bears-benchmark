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
package com.webfirmframework.wffweb.css.css3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class PerspectiveOriginTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#PerspectiveOrigin()}.
     */
    @Test
    public void testPerspectiveOrigin() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        assertEquals("0px", perspectiveOrigin.getCssValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#PerspectiveOrigin(java.lang.String)}
     * .
     */
    @Test
    public void testPerspectiveOriginString() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        assertEquals("0px", perspectiveOrigin.getCssValue());
        perspectiveOrigin.setCssValue("25px");
        assertEquals("25px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        assertTrue(perspectiveOrigin.getYAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#PerspectiveOrigin(com.webfirmframework.wffweb.css.PerspectiveOrigin)}
     * .
     */
    @Test
    public void testPerspectiveOriginPerspectiveOrigin() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin(new PerspectiveOrigin(
                "25px"));

        assertEquals("25px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        assertTrue(perspectiveOrigin.getYAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#PerspectiveOrigin(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testPerspectiveOriginFloatCssLengthUnit() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin(25, CssLengthUnit.PX);

        assertEquals("25.0px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        assertTrue(perspectiveOrigin.getYAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit, float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnitFloatCssLengthUnit() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        perspectiveOrigin.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        assertTrue(perspectiveOrigin.getYAxis() == 50);
        assertEquals(CssLengthUnit.CM, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
        
        perspectiveOrigin.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        assertTrue(perspectiveOrigin.getYAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnit() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        perspectiveOrigin.setValue(30, CssLengthUnit.PX);
        
        assertEquals("30.0px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        assertTrue(perspectiveOrigin.getYAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        assertEquals(CssNameConstants.PERSPECTIVE_ORIGIN, perspectiveOrigin.getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin(PerspectiveOrigin.INHERIT);
        assertEquals(PerspectiveOrigin.INHERIT, perspectiveOrigin.getCssValue());
        perspectiveOrigin.setCssValue("30px");
        assertEquals("30px", perspectiveOrigin.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#toHtmlString()}
     * .
     */
    @Test
    public void testToString() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        perspectiveOrigin.setCssValue("30px");
        assertEquals(CssNameConstants.PERSPECTIVE_ORIGIN+": 30px", perspectiveOrigin.toString());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#getXAxis()}.
     */
    @Test
    public void testGetHorizontalValue() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        perspectiveOrigin.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        
        perspectiveOrigin.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());


        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#getYAxis()}.
     */
    @Test
    public void testGetVerticalValue() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        perspectiveOrigin.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", perspectiveOrigin.getCssValue());


        assertTrue(perspectiveOrigin.getYAxis() == 50);
        assertEquals(CssLengthUnit.CM, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getYAxis());
        
        perspectiveOrigin.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getYAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getYAxis());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#getXAxisUnit()}.
     */
    @Test
    public void testGetHorizontalUnit() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        perspectiveOrigin.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", perspectiveOrigin.getCssValue());

        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxisUnit());
        
        perspectiveOrigin.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());


        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#getYAxisUnit()}.
     */
    @Test
    public void testGetVerticalUnit() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        perspectiveOrigin.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", perspectiveOrigin.getCssValue());

        assertEquals(CssLengthUnit.CM, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getYAxisUnit());
        
        perspectiveOrigin.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", perspectiveOrigin.getCssValue());

        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getYAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        perspectiveOrigin.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 25);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        assertTrue(perspectiveOrigin.getYAxis() == 50);
        assertEquals(CssLengthUnit.CM, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
        
        perspectiveOrigin.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", perspectiveOrigin.getCssValue());

        assertTrue(perspectiveOrigin.getXAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getXAxisUnit());

        assertTrue(perspectiveOrigin.getYAxis() == 30);
        assertEquals(CssLengthUnit.PX, perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();

        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        assertNotNull(perspectiveOrigin.getXAxis());
        assertNotNull(perspectiveOrigin.getYAxis());
        assertNotNull(perspectiveOrigin.getXAxisUnit());
        assertNotNull(perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInitial();
        
        assertEquals(PerspectiveOrigin.INITIAL, perspectiveOrigin.getCssValue());
        
        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        PerspectiveOrigin perspectiveOrigin = new PerspectiveOrigin();
        assertNotNull(perspectiveOrigin.getXAxis());
        assertNotNull(perspectiveOrigin.getYAxis());
        assertNotNull(perspectiveOrigin.getXAxisUnit());
        assertNotNull(perspectiveOrigin.getYAxisUnit());

        perspectiveOrigin.setAsInherit();
        
        assertEquals(PerspectiveOrigin.INHERIT, perspectiveOrigin.getCssValue());
        
        assertNull(perspectiveOrigin.getXAxis());
        assertNull(perspectiveOrigin.getYAxis());
        assertNull(perspectiveOrigin.getXAxisUnit());
        assertNull(perspectiveOrigin.getYAxisUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.PerspectiveOrigin#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(PerspectiveOrigin.isValid("55px"));
        assertTrue(PerspectiveOrigin.isValid("55px 85cm"));
        assertTrue(PerspectiveOrigin.isValid("55% 85cm"));
        assertTrue(PerspectiveOrigin.isValid("12% 12%"));
        
        assertFalse(PerspectiveOrigin.isValid("12"));
        assertFalse(PerspectiveOrigin.isValid("12 25"));
        assertFalse(PerspectiveOrigin.isValid("sdfdf"));
    }

}
