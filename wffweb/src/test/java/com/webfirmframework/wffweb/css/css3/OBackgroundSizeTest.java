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
public class OBackgroundSizeTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#OBackgroundSize()}.
     */
    @Test
    public void testOBackgroundSize() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        assertEquals("0px", backgroundSize.getCssValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#OBackgroundSize(java.lang.String)}
     * .
     */
    @Test
    public void testOBackgroundSizeString() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        assertEquals("0px", backgroundSize.getCssValue());
        backgroundSize.setCssValue("25px");
        assertEquals("25px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        assertTrue(backgroundSize.getHeight() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#OBackgroundSize(com.webfirmframework.wffweb.css.OBackgroundSize)}
     * .
     */
    @Test
    public void testOBackgroundSizeOBackgroundSize() {
        OBackgroundSize backgroundSize = new OBackgroundSize(new OBackgroundSize(
                "25px"));

        assertEquals("25px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        assertTrue(backgroundSize.getHeight() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#OBackgroundSize(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testOBackgroundSizeFloatCssLengthUnit() {
        OBackgroundSize backgroundSize = new OBackgroundSize(25, CssLengthUnit.PX);

        assertEquals("25.0px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        assertTrue(backgroundSize.getHeight() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit, float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnitFloatCssLengthUnit() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        backgroundSize.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        assertTrue(backgroundSize.getHeight() == 50);
        assertEquals(CssLengthUnit.CM, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
        
        backgroundSize.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        assertTrue(backgroundSize.getHeight() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnit() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        backgroundSize.setValue(30, CssLengthUnit.PX);
        
        assertEquals("30.0px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        assertTrue(backgroundSize.getHeight() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        assertEquals(CssNameConstants.O_BACKGROUND_SIZE, backgroundSize.getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        OBackgroundSize backgroundSize = new OBackgroundSize(OBackgroundSize.INHERIT);
        assertEquals(OBackgroundSize.INHERIT, backgroundSize.getCssValue());
        backgroundSize.setCssValue("30px");
        assertEquals("30px", backgroundSize.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.OBackgroundSize#toHtmlString()}
     * .
     */
    @Test
    public void testToString() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        backgroundSize.setCssValue("30px");
        assertEquals(CssNameConstants.O_BACKGROUND_SIZE+": 30px", backgroundSize.toString());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#getWidth()}.
     */
    @Test
    public void testGetHorizontalValue() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        backgroundSize.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        
        backgroundSize.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());


        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#getHeight()}.
     */
    @Test
    public void testGetVerticalValue() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        backgroundSize.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", backgroundSize.getCssValue());


        assertTrue(backgroundSize.getHeight() == 50);
        assertEquals(CssLengthUnit.CM, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getHeight());
        
        backgroundSize.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getHeight() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getHeight());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#getWidthUnit()}.
     */
    @Test
    public void testGetHorizontalUnit() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        backgroundSize.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", backgroundSize.getCssValue());

        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidthUnit());
        
        backgroundSize.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());


        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidthUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#getHeightUnit()}.
     */
    @Test
    public void testGetVerticalUnit() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        backgroundSize.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", backgroundSize.getCssValue());

        assertEquals(CssLengthUnit.CM, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getHeightUnit());
        
        backgroundSize.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", backgroundSize.getCssValue());

        assertEquals(CssLengthUnit.PX, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        backgroundSize.setValue(25, CssLengthUnit.PX, 50, CssLengthUnit.CM);

        assertEquals("25.0px 50.0cm", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 25);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        assertTrue(backgroundSize.getHeight() == 50);
        assertEquals(CssLengthUnit.CM, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
        
        backgroundSize.setValue(30, CssLengthUnit.PX, 30, CssLengthUnit.PX);
        
        assertEquals("30.0px", backgroundSize.getCssValue());

        assertTrue(backgroundSize.getWidth() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getWidthUnit());

        assertTrue(backgroundSize.getHeight() == 30);
        assertEquals(CssLengthUnit.PX, backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();

        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        assertNotNull(backgroundSize.getWidth());
        assertNotNull(backgroundSize.getHeight());
        assertNotNull(backgroundSize.getWidthUnit());
        assertNotNull(backgroundSize.getHeightUnit());

        backgroundSize.setAsInitial();
        
        assertEquals(OBackgroundSize.INITIAL, backgroundSize.getCssValue());
        
        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        OBackgroundSize backgroundSize = new OBackgroundSize();
        assertNotNull(backgroundSize.getWidth());
        assertNotNull(backgroundSize.getHeight());
        assertNotNull(backgroundSize.getWidthUnit());
        assertNotNull(backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();
        
        assertEquals(OBackgroundSize.INHERIT, backgroundSize.getCssValue());
        
        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.OBackgroundSize#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(OBackgroundSize.isValid("55px"));
        assertTrue(OBackgroundSize.isValid("55px 85cm"));
        assertTrue(OBackgroundSize.isValid("55% 85cm"));
        assertTrue(OBackgroundSize.isValid("12% 12%"));
        
        assertFalse(OBackgroundSize.isValid("12"));
        assertFalse(OBackgroundSize.isValid("12 25"));
        assertFalse(OBackgroundSize.isValid("sdfdf"));
    }

}
