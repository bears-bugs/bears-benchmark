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
public class BackgroundSizeTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#BackgroundSize()}.
     */
    @Test
    public void testBackgroundSize() {
        BackgroundSize backgroundSize = new BackgroundSize();
        assertEquals("0px", backgroundSize.getCssValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#BackgroundSize(java.lang.String)}
     * .
     */
    @Test
    public void testBackgroundSizeString() {
        BackgroundSize backgroundSize = new BackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#BackgroundSize(com.webfirmframework.wffweb.css.BackgroundSize)}
     * .
     */
    @Test
    public void testBackgroundSizeBackgroundSize() {
        BackgroundSize backgroundSize = new BackgroundSize(new BackgroundSize(
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#BackgroundSize(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testBackgroundSizeFloatCssLengthUnit() {
        BackgroundSize backgroundSize = new BackgroundSize(25, CssLengthUnit.PX);

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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit, float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnitFloatCssLengthUnit() {
        BackgroundSize backgroundSize = new BackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnit() {
        BackgroundSize backgroundSize = new BackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        BackgroundSize backgroundSize = new BackgroundSize();
        assertEquals(CssNameConstants.BACKGROUND_SIZE, backgroundSize.getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.INHERIT);
        assertEquals(BackgroundSize.INHERIT, backgroundSize.getCssValue());
        backgroundSize.setCssValue("30px");
        assertEquals("30px", backgroundSize.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundSize#toHtmlString()}
     * .
     */
    @Test
    public void testToString() {
        BackgroundSize backgroundSize = new BackgroundSize();
        backgroundSize.setCssValue("30px");
        assertEquals(CssNameConstants.BACKGROUND_SIZE+": 30px", backgroundSize.toString());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#getWidth()}.
     */
    @Test
    public void testGetHorizontalValue() {
        BackgroundSize backgroundSize = new BackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#getHeight()}.
     */
    @Test
    public void testGetVerticalValue() {
        BackgroundSize backgroundSize = new BackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#getWidthUnit()}.
     */
    @Test
    public void testGetHorizontalUnit() {
        BackgroundSize backgroundSize = new BackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#getHeightUnit()}.
     */
    @Test
    public void testGetVerticalUnit() {
        BackgroundSize backgroundSize = new BackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        BackgroundSize backgroundSize = new BackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        BackgroundSize backgroundSize = new BackgroundSize();
        assertNotNull(backgroundSize.getWidth());
        assertNotNull(backgroundSize.getHeight());
        assertNotNull(backgroundSize.getWidthUnit());
        assertNotNull(backgroundSize.getHeightUnit());

        backgroundSize.setAsInitial();
        
        assertEquals(BackgroundSize.INITIAL, backgroundSize.getCssValue());
        
        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        BackgroundSize backgroundSize = new BackgroundSize();
        assertNotNull(backgroundSize.getWidth());
        assertNotNull(backgroundSize.getHeight());
        assertNotNull(backgroundSize.getWidthUnit());
        assertNotNull(backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();
        
        assertEquals(BackgroundSize.INHERIT, backgroundSize.getCssValue());
        
        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.BackgroundSize#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(BackgroundSize.isValid("55px"));
        assertTrue(BackgroundSize.isValid("55px 85cm"));
        assertTrue(BackgroundSize.isValid("55% 85cm"));
        assertTrue(BackgroundSize.isValid("12% 12%"));
        
        assertFalse(BackgroundSize.isValid("12"));
        assertFalse(BackgroundSize.isValid("12 25"));
        assertFalse(BackgroundSize.isValid("sdfdf"));
    }

}
