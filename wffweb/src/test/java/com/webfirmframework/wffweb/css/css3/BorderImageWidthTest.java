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
public class BorderImageWidthTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth()}
     * .
     */
    @Test
    public void testBorderImageWidth() {

        BorderImageWidth borderImageWidth = new BorderImageWidth();
        assertEquals("1.0", borderImageWidth.getCssValue());

        assertTrue(1F == borderImageWidth.getTop());
        assertTrue(1F == borderImageWidth.getRight());
        assertTrue(1F == borderImageWidth.getBottom());
        assertTrue(1F == borderImageWidth.getLeft());
        assertNull(borderImageWidth.getTopUnit());
        assertNull(borderImageWidth.getRightUnit());
        assertNull(borderImageWidth.getBottomUnit());
        assertNull(borderImageWidth.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageWidthString1() {

        BorderImageWidth borderImageWidth = new BorderImageWidth("5 15 25 35");
        assertEquals("5.0 15.0 25.0 35.0", borderImageWidth.getCssValue());

        assertTrue(5F == borderImageWidth.getTop());
        assertTrue(15F == borderImageWidth.getRight());
        assertTrue(25F == borderImageWidth.getBottom());
        assertTrue(35F == borderImageWidth.getLeft());
        assertNull(borderImageWidth.getTopUnit());
        assertNull(borderImageWidth.getRightUnit());
        assertNull(borderImageWidth.getBottomUnit());
        assertNull(borderImageWidth.getLeftUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageWidthString2() {

        BorderImageWidth borderImageWidth = new BorderImageWidth(
                "5px 15px 25px 35px");
        assertEquals("5.0px 15.0px 25.0px 35.0px",
                borderImageWidth.getCssValue());

        assertTrue(5F == borderImageWidth.getTop());
        assertTrue(15F == borderImageWidth.getRight());
        assertTrue(25F == borderImageWidth.getBottom());
        assertTrue(35F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.PX, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.PX, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.PX, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.PX, borderImageWidth.getLeftUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageWidthString3() {

        BorderImageWidth borderImageWidth = new BorderImageWidth(
                "25px 25px 25px 25px");
        assertEquals("25.0px", borderImageWidth.getCssValue());
        assertTrue(25F == borderImageWidth.getTop());
        assertTrue(25F == borderImageWidth.getRight());
        assertTrue(25F == borderImageWidth.getBottom());
        assertTrue(25F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.PX, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.PX, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.PX, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.PX, borderImageWidth.getLeftUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageWidthString4() {
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 25px 55px");
            assertEquals("25.0px 25.0px 25.0px 55.0px",
                    borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 25px 25px");
            assertEquals("25.0px", borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "35px 25px 45px 25px ");
            assertEquals("35.0px 25.0px 45.0px",
                    borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "35px 25px 35px 25px ");
            assertEquals("35.0px 25.0px", borderImageWidth.getCssValue());
        }

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageWidthString5() {

        BorderImageWidth borderImageWidth = new BorderImageWidth(
                BorderImageWidth.AUTO);
        assertEquals(BorderImageWidth.AUTO, borderImageWidth.getCssValue());

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
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth(com.webfirmframework.wffweb.css.css3.BorderImageWidth)}
     * .
     */
    @Test
    public void testBorderImageWidthBorderImageWidth() {
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    new BorderImageWidth("35px 25px 35px 25px "));
            assertEquals("35.0px 25.0px", borderImageWidth.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth(float)}
     * .
     */
    @Test
    public void testBorderImageWidthFloat() {
        BorderImageWidth borderImageWidth = new BorderImageWidth(55F);
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
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#BorderImageWidth(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testBorderImageWidthFloatCssLengthUnit() {
        BorderImageWidth borderImageWidth = new BorderImageWidth(55F,
                CssLengthUnit.EM);
        assertEquals("55.0em", borderImageWidth.getCssValue());

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

        assertEquals(CssLengthUnit.EM, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.EM, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.EM, borderImageWidth.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setPercent(float)}
     * .
     */
    @Test
    public void testSetPercent() {
        BorderImageWidth borderImageWidth = new BorderImageWidth();
        borderImageWidth.setPercent(55F);
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
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getCssName()}
     * .
     */
    @Test
    public void testGetCssName() {
        assertEquals(CssNameConstants.BORDER_IMAGE_WIDTH,
                new BorderImageWidth().getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getCssValue()}
     * .
     */
    @Test
    public void testGetCssValue() {
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 25px 55px");
            assertEquals("25.0px 25.0px 25.0px 55.0px",
                    borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 25px 25px");
            assertEquals("25.0px", borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "35px 25px 45px 25px ");
            assertEquals("35.0px 25.0px 45.0px",
                    borderImageWidth.getCssValue());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "35px 25px 35px 25px ");
            assertEquals("35.0px 25.0px", borderImageWidth.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#toString()}.
     */
    @Test
    public void testToString() {
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 55px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_WIDTH
                            + ": 25.0px 25.0px 55.0px 55.0px",
                    borderImageWidth.toString());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 25px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_WIDTH
                            + ": 25.0px 25.0px 25.0px 55.0px",
                    borderImageWidth.toString());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 55px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_WIDTH
                            + ": 25.0px 25.0px 55.0px 55.0px",
                    borderImageWidth.toString());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "25px 25px 25px 25px");
            assertEquals(CssNameConstants.BORDER_IMAGE_WIDTH + ": 25.0px",
                    borderImageWidth.toString());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "35px 25px 45px 25px ");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_WIDTH
                            + ": 35.0px 25.0px 45.0px",
                    borderImageWidth.toString());
        }
        {
            BorderImageWidth borderImageWidth = new BorderImageWidth(
                    "35px 25px 35px 25px ");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_WIDTH + ": 35.0px 25.0px",
                    borderImageWidth.toString());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
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
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setAsInitial()}
     * .
     */
    @Test
    public void testSetAsInitial() {
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

        borderImageWidth.setAsInitial();

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
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setAsInherit()}
     * .
     */
    @Test
    public void testSetAsInherit() {
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

        borderImageWidth.setAsInherit();

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
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setBorderImageTopRightBottomLeft(java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBorderImageTopRightBottomLeftFloatFloatFloatFloatCssLengthUnit() {
        BorderImageWidth borderImageWidth = new BorderImageWidth();
        borderImageWidth.setBorderImageTopRightBottomLeft(5F, 10F, 15F, 20F,
                CssLengthUnit.IN);
        assertEquals("5.0in 10.0in 15.0in 20.0in",
                borderImageWidth.getCssValue());

        assertNotNull(borderImageWidth.getTop());
        assertNotNull(borderImageWidth.getRight());
        assertNotNull(borderImageWidth.getBottom());
        assertNotNull(borderImageWidth.getLeft());

        assertTrue(5F == borderImageWidth.getTop());
        assertTrue(10F == borderImageWidth.getRight());
        assertTrue(15F == borderImageWidth.getBottom());
        assertTrue(20F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.IN, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.IN, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.IN, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.IN, borderImageWidth.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setBorderImageTopRightBottomLeft(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBorderImageTopRightBottomLeftFloatCssLengthUnitFloatCssLengthUnitFloatCssLengthUnitFloatCssLengthUnit() {
        BorderImageWidth borderImageWidth = new BorderImageWidth();
        borderImageWidth.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageWidth.getCssValue());

        assertNotNull(borderImageWidth.getTop());
        assertNotNull(borderImageWidth.getRight());
        assertNotNull(borderImageWidth.getBottom());
        assertNotNull(borderImageWidth.getLeft());

        assertTrue(5F == borderImageWidth.getTop());
        assertTrue(10F == borderImageWidth.getRight());
        assertTrue(15F == borderImageWidth.getBottom());
        assertTrue(20F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageWidth.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setTop(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetTop() {
        BorderImageWidth borderImageWidth = new BorderImageWidth();
        borderImageWidth.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageWidth.getCssValue());

        assertNotNull(borderImageWidth.getTop());
        assertNotNull(borderImageWidth.getRight());
        assertNotNull(borderImageWidth.getBottom());
        assertNotNull(borderImageWidth.getLeft());

        assertTrue(5F == borderImageWidth.getTop());
        assertTrue(10F == borderImageWidth.getRight());
        assertTrue(15F == borderImageWidth.getBottom());
        assertTrue(20F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageWidth.getLeftUnit());

        borderImageWidth.setTop(55F, CssLengthUnit.PT);

        assertEquals("55.0pt 10.0cm 15.0em 20.0ex",
                borderImageWidth.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageWidth.getTopUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setRight(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetRight() {
        BorderImageWidth borderImageWidth = new BorderImageWidth();
        borderImageWidth.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageWidth.getCssValue());

        assertNotNull(borderImageWidth.getTop());
        assertNotNull(borderImageWidth.getRight());
        assertNotNull(borderImageWidth.getBottom());
        assertNotNull(borderImageWidth.getLeft());

        assertTrue(5F == borderImageWidth.getTop());
        assertTrue(10F == borderImageWidth.getRight());
        assertTrue(15F == borderImageWidth.getBottom());
        assertTrue(20F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageWidth.getLeftUnit());

        borderImageWidth.setRight(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 55.0pt 15.0em 20.0ex",
                borderImageWidth.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageWidth.getRightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setBottom(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBottom() {
        BorderImageWidth borderImageWidth = new BorderImageWidth();
        borderImageWidth.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageWidth.getCssValue());

        assertNotNull(borderImageWidth.getTop());
        assertNotNull(borderImageWidth.getRight());
        assertNotNull(borderImageWidth.getBottom());
        assertNotNull(borderImageWidth.getLeft());

        assertTrue(5F == borderImageWidth.getTop());
        assertTrue(10F == borderImageWidth.getRight());
        assertTrue(15F == borderImageWidth.getBottom());
        assertTrue(20F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageWidth.getLeftUnit());

        borderImageWidth.setBottom(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 10.0cm 55.0pt 20.0ex",
                borderImageWidth.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageWidth.getBottomUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#setLeft(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetLeft() {
        BorderImageWidth borderImageWidth = new BorderImageWidth();
        borderImageWidth.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageWidth.getCssValue());

        assertNotNull(borderImageWidth.getTop());
        assertNotNull(borderImageWidth.getRight());
        assertNotNull(borderImageWidth.getBottom());
        assertNotNull(borderImageWidth.getLeft());

        assertTrue(5F == borderImageWidth.getTop());
        assertTrue(10F == borderImageWidth.getRight());
        assertTrue(15F == borderImageWidth.getBottom());
        assertTrue(20F == borderImageWidth.getLeft());

        assertNotNull(borderImageWidth.getTopUnit());
        assertNotNull(borderImageWidth.getRightUnit());
        assertNotNull(borderImageWidth.getBottomUnit());
        assertNotNull(borderImageWidth.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageWidth.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageWidth.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageWidth.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageWidth.getLeftUnit());

        borderImageWidth.setLeft(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 10.0cm 15.0em 55.0pt",
                borderImageWidth.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageWidth.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getTop()}.
     */
    /*
     * @Test public void testGetTop() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getTopUnit()}
       * .
       */
    /*
     * @Test public void testGetTopUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getRight()}
       * .
       */
    /*
     * @Test public void testGetRight() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getRightUnit()}
       * .
       */
    /*
     * @Test public void testGetRightUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getBottom()}
       * .
       */
    /*
     * @Test public void testGetBottom() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getBottomUnit()}
       * .
       */
    /*
     * @Test public void testGetBottomUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getLeft()}
       * .
       */
    /*
     * @Test public void testGetLeft() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getLeftUnit()}
       * .
       */
    /*
     * @Test public void testGetLeftUnit() { fail("Not yet implemented"); }
     */

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#getProducedCssValue(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testGetProducedCssValue() {
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                BorderImageWidth.getProducedCssValue(5F, CssLengthUnit.CH, 10F,
                        CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                        CssLengthUnit.EX));

        assertEquals("5.0px",
                BorderImageWidth.getProducedCssValue(5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX));

        assertEquals("5.0px 15.0px",
                BorderImageWidth.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX));

        assertEquals("5.0px 15.0px 25.0px",
                BorderImageWidth.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 25F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX));
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid1() {

        {
            final boolean valid = BorderImageWidth.isValid("45px");
            assertTrue(valid);
           assertTrue(BorderImageWidth.isValid("55"));
        }
        {
            final boolean valid = BorderImageWidth.isValid("45em 45px");
            assertTrue(valid);
            final boolean invalid = BorderImageWidth.isValid("45em dfd 45px");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderImageWidth.isValid("45%");
            assertTrue(valid);
            final boolean invalid = BorderImageWidth.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderImageWidth.isValid("45em");
            assertTrue(valid);
            final boolean invalid = BorderImageWidth.isValid("45sem");
           assertFalse(invalid);
        }

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid2() {
        {
            final boolean valid = BorderImageWidth.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth.isValid("45rem auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth.isValid("auto auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth.isValid("auto auto auto");
            assertTrue(valid);
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid3() {
        {
            final boolean valid = BorderImageWidth
                    .isValid("auto auto auto auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth
                    .isValid("auto auto auto 34px");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth
                    .isValid("auto auto 45px auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth
                    .isValid("auto auto 45px 45px");
            assertTrue(valid);
        }

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid4() {
        {
            final boolean valid = BorderImageWidth
                    .isValid("45px 45px auto auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth.isValid("45px auto 45px");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth
                    .isValid("45px 45px 45px auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth
                    .isValid("auto 45px 45px 45px");
            assertTrue(valid);
        }

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid5() {
        {
            final boolean valid = BorderImageWidth
                    .isValid("auto 15px auto auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth
                    .isValid("auto 15px 15px auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth
                    .isValid("15px 15px auto auto");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageWidth
                    .isValid("12px auto auto auto");
            assertTrue(valid);
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageWidth#hasPredefinedConstantValue()}
     * .
     */
    @Test
    public void testHasPredefinedConstantValue() {
        assertTrue(new BorderImageWidth(BorderImageWidth.INHERIT)
                .hasPredefinedConstantValue());
        assertTrue(new BorderImageWidth(BorderImageWidth.INITIAL)
                .hasPredefinedConstantValue());
        assertTrue(new BorderImageWidth(BorderImageWidth.AUTO)
                .hasPredefinedConstantValue());
        assertFalse(new BorderImageWidth().hasPredefinedConstantValue());
        assertFalse(
                new BorderImageWidth("auto 10px").hasPredefinedConstantValue());
    }

}
