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
public class MozBackgroundSizeTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#MozBackgroundSize()}.
     */
    @Test
    public void testMozBackgroundSize() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
        assertEquals("0px", backgroundSize.getCssValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#MozBackgroundSize(java.lang.String)}
     * .
     */
    @Test
    public void testMozBackgroundSizeString() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#MozBackgroundSize(com.webfirmframework.wffweb.css.MozBackgroundSize)}
     * .
     */
    @Test
    public void testMozBackgroundSizeMozBackgroundSize() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize(new MozBackgroundSize(
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#MozBackgroundSize(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testMozBackgroundSizeFloatCssLengthUnit() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize(25, CssLengthUnit.PX);

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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit, float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnitFloatCssLengthUnit() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnit() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
        assertEquals(CssNameConstants.MOZ_BACKGROUND_SIZE, backgroundSize.getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize(MozBackgroundSize.INHERIT);
        assertEquals(MozBackgroundSize.INHERIT, backgroundSize.getCssValue());
        backgroundSize.setCssValue("30px");
        assertEquals("30px", backgroundSize.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.MozBackgroundSize#toHtmlString()}
     * .
     */
    @Test
    public void testToString() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
        backgroundSize.setCssValue("30px");
        assertEquals(CssNameConstants.MOZ_BACKGROUND_SIZE+": 30px", backgroundSize.toString());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#getWidth()}.
     */
    @Test
    public void testGetHorizontalValue() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#getHeight()}.
     */
    @Test
    public void testGetVerticalValue() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#getWidthUnit()}.
     */
    @Test
    public void testGetHorizontalUnit() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#getHeightUnit()}.
     */
    @Test
    public void testGetVerticalUnit() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
        assertNotNull(backgroundSize.getWidth());
        assertNotNull(backgroundSize.getHeight());
        assertNotNull(backgroundSize.getWidthUnit());
        assertNotNull(backgroundSize.getHeightUnit());

        backgroundSize.setAsInitial();
        
        assertEquals(MozBackgroundSize.INITIAL, backgroundSize.getCssValue());
        
        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        MozBackgroundSize backgroundSize = new MozBackgroundSize();
        assertNotNull(backgroundSize.getWidth());
        assertNotNull(backgroundSize.getHeight());
        assertNotNull(backgroundSize.getWidthUnit());
        assertNotNull(backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();
        
        assertEquals(MozBackgroundSize.INHERIT, backgroundSize.getCssValue());
        
        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.MozBackgroundSize#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(MozBackgroundSize.isValid("55px"));
        assertTrue(MozBackgroundSize.isValid("55px 85cm"));
        assertTrue(MozBackgroundSize.isValid("55% 85cm"));
        assertTrue(MozBackgroundSize.isValid("12% 12%"));
        
        assertFalse(MozBackgroundSize.isValid("12"));
        assertFalse(MozBackgroundSize.isValid("12 25"));
        assertFalse(MozBackgroundSize.isValid("sdfdf"));
    }

}
