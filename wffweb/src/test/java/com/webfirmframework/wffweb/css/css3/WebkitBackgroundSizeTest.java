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
public class WebkitBackgroundSizeTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#WebkitBackgroundSize()}.
     */
    @Test
    public void testWebkitBackgroundSize() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
        assertEquals("0px", backgroundSize.getCssValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#WebkitBackgroundSize(java.lang.String)}
     * .
     */
    @Test
    public void testWebkitBackgroundSizeString() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#WebkitBackgroundSize(com.webfirmframework.wffweb.css.WebkitBackgroundSize)}
     * .
     */
    @Test
    public void testWebkitBackgroundSizeWebkitBackgroundSize() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize(new WebkitBackgroundSize(
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#WebkitBackgroundSize(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testWebkitBackgroundSizeFloatCssLengthUnit() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize(25, CssLengthUnit.PX);

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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit, float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnitFloatCssLengthUnit() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#setValue(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetValueFloatCssLengthUnit() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
        assertEquals(CssNameConstants.WEBKIT_BACKGROUND_SIZE, backgroundSize.getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize(WebkitBackgroundSize.INHERIT);
        assertEquals(WebkitBackgroundSize.INHERIT, backgroundSize.getCssValue());
        backgroundSize.setCssValue("30px");
        assertEquals("30px", backgroundSize.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#toHtmlString()}
     * .
     */
    @Test
    public void testToString() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
        backgroundSize.setCssValue("30px");
        assertEquals(CssNameConstants.WEBKIT_BACKGROUND_SIZE+": 30px", backgroundSize.toString());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#getWidth()}.
     */
    @Test
    public void testGetHorizontalValue() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#getHeight()}.
     */
    @Test
    public void testGetVerticalValue() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#getWidthUnit()}.
     */
    @Test
    public void testGetHorizontalUnit() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#getHeightUnit()}.
     */
    @Test
    public void testGetVerticalUnit() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
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
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
        assertNotNull(backgroundSize.getWidth());
        assertNotNull(backgroundSize.getHeight());
        assertNotNull(backgroundSize.getWidthUnit());
        assertNotNull(backgroundSize.getHeightUnit());

        backgroundSize.setAsInitial();
        
        assertEquals(WebkitBackgroundSize.INITIAL, backgroundSize.getCssValue());
        
        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        WebkitBackgroundSize backgroundSize = new WebkitBackgroundSize();
        assertNotNull(backgroundSize.getWidth());
        assertNotNull(backgroundSize.getHeight());
        assertNotNull(backgroundSize.getWidthUnit());
        assertNotNull(backgroundSize.getHeightUnit());

        backgroundSize.setAsInherit();
        
        assertEquals(WebkitBackgroundSize.INHERIT, backgroundSize.getCssValue());
        
        assertNull(backgroundSize.getWidth());
        assertNull(backgroundSize.getHeight());
        assertNull(backgroundSize.getWidthUnit());
        assertNull(backgroundSize.getHeightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.WebkitBackgroundSize#isValid(java.lang.String)}.
     */
    @Test
    public void testIsValid() {
        assertTrue(WebkitBackgroundSize.isValid("55px"));
        assertTrue(WebkitBackgroundSize.isValid("55px 85cm"));
        assertTrue(WebkitBackgroundSize.isValid("55% 85cm"));
        assertTrue(WebkitBackgroundSize.isValid("12% 12%"));
        
        assertFalse(WebkitBackgroundSize.isValid("12"));
        assertFalse(WebkitBackgroundSize.isValid("12 25"));
        assertFalse(WebkitBackgroundSize.isValid("sdfdf"));
    }

}
