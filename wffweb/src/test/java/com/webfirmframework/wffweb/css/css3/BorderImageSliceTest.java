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

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class BorderImageSliceTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#BorderImageSlice()}
     * .
     */
    @Test
    public void testBorderImageSlice() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        assertEquals("100.0%", borderImageSlice.getCssValue());

        assertTrue(100F == borderImageSlice.getTop());
        assertTrue(100F == borderImageSlice.getRight());
        assertTrue(100F == borderImageSlice.getBottom());
        assertTrue(100F == borderImageSlice.getLeft());
        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#BorderImageSlice(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageSliceString1() {
        BorderImageSlice borderImageSlice = new BorderImageSlice("5 15 25 35");
        assertEquals("5.0 15.0 25.0 35.0", borderImageSlice.getCssValue());

        assertTrue(5F == borderImageSlice.getTop());
        assertTrue(15F == borderImageSlice.getRight());
        assertTrue(25F == borderImageSlice.getBottom());
        assertTrue(35F == borderImageSlice.getLeft());
        assertNull(borderImageSlice.getTopUnit());
        assertNull(borderImageSlice.getRightUnit());
        assertNull(borderImageSlice.getBottomUnit());
        assertNull(borderImageSlice.getLeftUnit());

    }

    @Test
    public void testBorderImageSliceString2() {
        BorderImageSlice borderImageSlice = new BorderImageSlice(
                "5px 15px 25px 35px");
        assertEquals("5.0px 15.0px 25.0px 35.0px",
                borderImageSlice.getCssValue());

        assertTrue(5F == borderImageSlice.getTop());
        assertTrue(15F == borderImageSlice.getRight());
        assertTrue(25F == borderImageSlice.getBottom());
        assertTrue(35F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.PX, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.PX, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.PX, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.PX, borderImageSlice.getLeftUnit());

    }

    @Test
    public void testBorderImageSliceString3() {
        BorderImageSlice borderImageSlice = new BorderImageSlice(
                "25px 25px 25px 25px");
        assertEquals("25.0px", borderImageSlice.getCssValue());
        assertTrue(25F == borderImageSlice.getTop());
        assertTrue(25F == borderImageSlice.getRight());
        assertTrue(25F == borderImageSlice.getBottom());
        assertTrue(25F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.PX, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.PX, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.PX, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.PX, borderImageSlice.getLeftUnit());

    }

    @Test
    public void testBorderImageSliceString4() {
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 25px 55px");
            assertEquals("25.0px 25.0px 25.0px 55.0px",
                    borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 25px 25px");
            assertEquals("25.0px", borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "35px 25px 45px 25px ");
            assertEquals("35.0px 25.0px 45.0px",
                    borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "35px 25px 35px 25px ");
            assertEquals("35.0px 25.0px", borderImageSlice.getCssValue());
        }

    }

    @Test
    public void testBorderImageSliceString5() {
        BorderImageSlice borderImageSlice = new BorderImageSlice(
                BorderImageSlice.INHERIT);
        assertEquals(BorderImageSlice.INHERIT, borderImageSlice.getCssValue());

        assertNull(borderImageSlice.getTop());
        assertNull(borderImageSlice.getRight());
        assertNull(borderImageSlice.getBottom());
        assertNull(borderImageSlice.getLeft());

        assertNull(borderImageSlice.getTopUnit());
        assertNull(borderImageSlice.getRightUnit());
        assertNull(borderImageSlice.getBottomUnit());
        assertNull(borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#BorderImageSlice(com.webfirmframework.wffweb.css.css3.BorderImageSlice)}
     * .
     */
    @Test
    public void testBorderImageSliceBorderImageSlice() {
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "35px 25px 35px 25px ");
            assertEquals("35.0px 25.0px", borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    new BorderImageSlice("35px 25px 35px 25px "));
            assertEquals("35.0px 25.0px", borderImageSlice.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#BorderImageSlice(float)}
     * .
     */
    @Test
    public void testBorderImageSliceFloat() {
        BorderImageSlice borderImageSlice = new BorderImageSlice(55F);
        assertEquals("55.0%", borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(55F == borderImageSlice.getTop());
        assertTrue(55F == borderImageSlice.getRight());
        assertTrue(55F == borderImageSlice.getBottom());
        assertTrue(55F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#BorderImageSlice(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testBorderImageSliceFloatCssLengthUnit() {
        BorderImageSlice borderImageSlice = new BorderImageSlice(55F,
                CssLengthUnit.EM);
        assertEquals("55.0em", borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(55F == borderImageSlice.getTop());
        assertTrue(55F == borderImageSlice.getRight());
        assertTrue(55F == borderImageSlice.getBottom());
        assertTrue(55F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.EM, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.EM, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.EM, borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setPercent(float)}
     * .
     */
    @Test
    public void testSetPercent() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setPercent(55F);
        assertEquals("55.0%", borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(55F == borderImageSlice.getTop());
        assertTrue(55F == borderImageSlice.getRight());
        assertTrue(55F == borderImageSlice.getBottom());
        assertTrue(55F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getCssName()}
     * .
     */
    @Test
    public void testGetCssName() {
        assertEquals(CssNameConstants.BORDER_IMAGE_SLICE,
                new BorderImageSlice().getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getCssValue()}
     * .
     */
    @Test
    public void testGetCssValue() {
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 25px 55px");
            assertEquals("25.0px 25.0px 25.0px 55.0px",
                    borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 25px 25px");
            assertEquals("25.0px", borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "35px 25px 45px 25px ");
            assertEquals("35.0px 25.0px 45.0px",
                    borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "35px 25px 35px 25px ");
            assertEquals("35.0px 25.0px", borderImageSlice.getCssValue());
        }

        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "35px 25px 35px 25px fill");
            assertEquals("35.0px 25.0px fill", borderImageSlice.getCssValue());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "fill 35px 25px 35px 25px");
            assertEquals("35.0px 25.0px fill", borderImageSlice.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#toString()}.
     */
    @Test
    public void testToString() {
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 55px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_SLICE
                            + ": 25.0px 25.0px 55.0px 55.0px",
                    borderImageSlice.toString());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 25px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_SLICE
                            + ": 25.0px 25.0px 25.0px 55.0px",
                    borderImageSlice.toString());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 55px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_SLICE
                            + ": 25.0px 25.0px 55.0px 55.0px",
                    borderImageSlice.toString());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "25px 25px 25px 25px");
            assertEquals(CssNameConstants.BORDER_IMAGE_SLICE + ": 25.0px",
                    borderImageSlice.toString());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "35px 25px 45px 25px ");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_SLICE
                            + ": 35.0px 25.0px 45.0px",
                    borderImageSlice.toString());
        }
        {
            BorderImageSlice borderImageSlice = new BorderImageSlice(
                    "35px 25px 35px 25px ");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_SLICE + ": 35.0px 25.0px",
                    borderImageSlice.toString());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setCssValue("55%");
        assertEquals("55.0%", borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(55F == borderImageSlice.getTop());
        assertTrue(55F == borderImageSlice.getRight());
        assertTrue(55F == borderImageSlice.getBottom());
        assertTrue(55F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setAsInitial()}
     * .
     */
    @Test
    public void testSetAsInitial() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setCssValue("55%");
        assertEquals("55.0%", borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(55F == borderImageSlice.getTop());
        assertTrue(55F == borderImageSlice.getRight());
        assertTrue(55F == borderImageSlice.getBottom());
        assertTrue(55F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getLeftUnit());

        borderImageSlice.setAsInitial();

        assertNull(borderImageSlice.getTop());
        assertNull(borderImageSlice.getRight());
        assertNull(borderImageSlice.getBottom());
        assertNull(borderImageSlice.getLeft());

        assertNull(borderImageSlice.getTopUnit());
        assertNull(borderImageSlice.getRightUnit());
        assertNull(borderImageSlice.getBottomUnit());
        assertNull(borderImageSlice.getLeftUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setAsInherit()}
     * .
     */
    @Test
    public void testSetAsInherit() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setCssValue("55%");
        assertEquals("55.0%", borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(55F == borderImageSlice.getTop());
        assertTrue(55F == borderImageSlice.getRight());
        assertTrue(55F == borderImageSlice.getBottom());
        assertTrue(55F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageSlice.getLeftUnit());

        borderImageSlice.setAsInherit();

        assertNull(borderImageSlice.getTop());
        assertNull(borderImageSlice.getRight());
        assertNull(borderImageSlice.getBottom());
        assertNull(borderImageSlice.getLeft());

        assertNull(borderImageSlice.getTopUnit());
        assertNull(borderImageSlice.getRightUnit());
        assertNull(borderImageSlice.getBottomUnit());
        assertNull(borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setAsAuto()}
     * .
     */
    @Test
    public void testSetAsAuto() {
        BorderImageWidth borderImageWidth = new BorderImageWidth();
        borderImageWidth.setCssValue("55%");
        assertEquals("55.0%", borderImageWidth.getCssValue());

        assertNotNull(borderImageWidth.getTop());
        assertNotNull(borderImageWidth.getRight());
        assertNotNull(borderImageWidth.getBottom());
        assertNotNull(borderImageWidth.getLeft());

        assertTrue(55F == borderImageWidth.getTop());
        assertTrue(55F == borderImageWidth.getRight());
        assertTrue(55F == borderImageWidth.getBottom());
        assertTrue(55F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageWidth.getLeftUnit());

        borderImageWidth.setAsAuto();

        assertNull(borderImageWidth.getTop());
        assertNull(borderImageWidth.getRight());
        assertNull(borderImageWidth.getBottom());
        assertNull(borderImageWidth.getLeft());

        assertNull(borderImageWidth.getTopUnit());
        assertNull(borderImageWidth.getRightUnit());
        assertNull(borderImageWidth.getBottomUnit());
        assertNull(borderImageWidth.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setBorderImageTopRightBottomLeft(java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBorderImageTopRightBottomLeftFloatFloatFloatFloatCssLengthUnit() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setBorderImageTopRightBottomLeft(5F, 10F, 15F, 20F,
                CssLengthUnit.IN);
        assertEquals("5.0in 10.0in 15.0in 20.0in",
                borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(5F == borderImageSlice.getTop());
        assertTrue(10F == borderImageSlice.getRight());
        assertTrue(15F == borderImageSlice.getBottom());
        assertTrue(20F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.IN, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.IN, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.IN, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.IN, borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setBorderImageTopRightBottomLeft(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBorderImageTopRightBottomLeftFloatCssLengthUnitFloatCssLengthUnitFloatCssLengthUnitFloatCssLengthUnit() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(5F == borderImageSlice.getTop());
        assertTrue(10F == borderImageSlice.getRight());
        assertTrue(15F == borderImageSlice.getBottom());
        assertTrue(20F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setTop(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetTop() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(5F == borderImageSlice.getTop());
        assertTrue(10F == borderImageSlice.getRight());
        assertTrue(15F == borderImageSlice.getBottom());
        assertTrue(20F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageSlice.getLeftUnit());

        borderImageSlice.setTop(55F, CssLengthUnit.PT);

        assertEquals("55.0pt 10.0cm 15.0em 20.0ex",
                borderImageSlice.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageSlice.getTopUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setRight(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetRight() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(5F == borderImageSlice.getTop());
        assertTrue(10F == borderImageSlice.getRight());
        assertTrue(15F == borderImageSlice.getBottom());
        assertTrue(20F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageSlice.getLeftUnit());

        borderImageSlice.setRight(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 55.0pt 15.0em 20.0ex",
                borderImageSlice.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageSlice.getRightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setBottom(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBottom() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(5F == borderImageSlice.getTop());
        assertTrue(10F == borderImageSlice.getRight());
        assertTrue(15F == borderImageSlice.getBottom());
        assertTrue(20F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageSlice.getLeftUnit());

        borderImageSlice.setBottom(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 10.0cm 55.0pt 20.0ex",
                borderImageSlice.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageSlice.getBottomUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#setLeft(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetLeft() {
        BorderImageSlice borderImageSlice = new BorderImageSlice();
        borderImageSlice.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageSlice.getCssValue());

        assertNotNull(borderImageSlice.getTop());
        assertNotNull(borderImageSlice.getRight());
        assertNotNull(borderImageSlice.getBottom());
        assertNotNull(borderImageSlice.getLeft());

        assertTrue(5F == borderImageSlice.getTop());
        assertTrue(10F == borderImageSlice.getRight());
        assertTrue(15F == borderImageSlice.getBottom());
        assertTrue(20F == borderImageSlice.getLeft());

        assertNotNull(borderImageSlice.getTopUnit());
        assertNotNull(borderImageSlice.getRightUnit());
        assertNotNull(borderImageSlice.getBottomUnit());
        assertNotNull(borderImageSlice.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageSlice.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageSlice.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageSlice.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageSlice.getLeftUnit());

        borderImageSlice.setLeft(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 10.0cm 15.0em 55.0pt",
                borderImageSlice.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageSlice.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getTop()}.
     */
    /*
     * @Test public void testGetTop() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getTopUnit()}
       * .
       */
    /*
     * @Test public void testGetTopUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getRight()}
       * .
       */
    /*
     * @Test public void testGetRight() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getRightUnit()}
       * .
       */
    /*
     * @Test public void testGetRightUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getBottom()}
       * .
       */
    /*
     * @Test public void testGetBottom() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getBottomUnit()}
       * .
       */
    /*
     * @Test public void testGetBottomUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getLeft()}
       * .
       */
    /*
     * @Test public void testGetLeft() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getLeftUnit()}
       * .
       */
    /*
     * @Test public void testGetLeftUnit() { fail("Not yet implemented"); }
     */

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#getProducedCssValue(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, String)}
     * .
     */
    @Test
    public void testGetProducedCssValue() {
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex fill",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.CH, 10F,
                        CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                        CssLengthUnit.EX, BorderImageSlice.FILL));

        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.CH, 10F,
                        CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                        CssLengthUnit.EX, null));

        assertEquals("5.0px fill",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, BorderImageSlice.FILL));

        assertEquals("5.0px",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, null));

        assertEquals("5.0px 15.0px fill",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, BorderImageSlice.FILL));

        assertEquals("5.0px 15.0px",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, null));

        assertEquals("5.0px 15.0px 25.0px fill",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 25F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, BorderImageSlice.FILL));

        assertEquals("5.0px 15.0px 25.0px",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 25F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, null));
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid() {

        {
            final boolean valid = BorderImageSlice.isValid("45px");
            assertTrue(valid);
           assertTrue(BorderImageSlice.isValid("55"));
        }
        {
            final boolean valid = BorderImageSlice.isValid("45em 45px");
            assertTrue(valid);
            final boolean invalid = BorderImageSlice.isValid("45em dfd 45px");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderImageSlice.isValid("45%");
            assertTrue(valid);
            final boolean invalid = BorderImageSlice.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderImageSlice.isValid("45em");
            assertTrue(valid);
            final boolean invalid = BorderImageSlice.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderImageSlice.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageSlice
                    .isValid("45px  25px 15px 45rem fill");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageSlice
                    .isValid("45px 25px 15px 45rem fill");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageSlice.isValid("45rem fill");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageSlice.isValid("fill 45rem fill");
            assertFalse(valid);
        }
        {
            final boolean valid = BorderImageSlice
                    .isValid("45rem fill 45rem fill");
            assertFalse(valid);
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#hasPredefinedConstantValue()}
     * .
     */
    @Test
    public void testHasPredefinedConstantValue() {
        assertTrue(new BorderImageSlice(BorderImageSlice.INHERIT)
                .hasPredefinedConstantValue());
        assertTrue(new BorderImageSlice(BorderImageSlice.INITIAL)
                .hasPredefinedConstantValue());
        assertFalse(new BorderImageSlice().hasPredefinedConstantValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#addFill()} .
     */
    @Test
    public void testAddFill() {

        BorderImageSlice borderImageSlice = new BorderImageSlice(
                "5.0ch 10.0cm 15.0em 20.0ex fill");

        assertEquals("5.0ch 10.0cm 15.0em 20.0ex fill",
                borderImageSlice.getCssValue());

        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.CH, 10F,
                        CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                        CssLengthUnit.EX, null));

        assertEquals("5.0px fill",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, BorderImageSlice.FILL));

        assertEquals("5.0px",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, null));

        assertEquals("5.0px 15.0px fill",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, BorderImageSlice.FILL));

        assertEquals("5.0px 15.0px",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, null));

        assertEquals("5.0px 15.0px 25.0px fill",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 25F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, BorderImageSlice.FILL));

        assertEquals("5.0px 15.0px 25.0px",
                BorderImageSlice.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 25F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, null));
        
        final BorderImageSlice borderImageSlice2 = new BorderImageSlice("5.0px 15.0px 25.0px");
        borderImageSlice2.addFill();
        assertEquals("5.0px 15.0px 25.0px fill",
                borderImageSlice2.getCssValue());

    }

    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue1() throws Exception {
        new BorderImageSlice().setCssValue("fill 45px fill");
    }

    @Test(expected = InvalidValueException.class)
    public void testSetCssValueInvalidValue2() throws Exception {
        new BorderImageSlice().setCssValue("15px fill 45px fill");
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageSlice#removeFill()}
     * .
     */
    @Test
    public void testRemoveFill() {
        final BorderImageSlice borderImageSlice2 = new BorderImageSlice("5.0px 15.0px 25.0px fill");
        borderImageSlice2.removeFill();
        assertEquals("5.0px 15.0px 25.0px",
                borderImageSlice2.getCssValue());
    }

}
