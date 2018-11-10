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
public class BorderImageOutsetTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset()}
     * .
     */
    @Test
    public void testBorderImageOutset() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        assertEquals("0.0", borderImageOutset.getCssValue());

        assertTrue(0F == borderImageOutset.getTop());
        assertTrue(0F == borderImageOutset.getRight());
        assertTrue(0F == borderImageOutset.getBottom());
        assertTrue(0F == borderImageOutset.getLeft());
        assertNull(borderImageOutset.getTopUnit());
        assertNull(borderImageOutset.getRightUnit());
        assertNull(borderImageOutset.getBottomUnit());
        assertNull(borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageOutsetString1() {

        BorderImageOutset borderImageOutset = new BorderImageOutset(
                "5 15 25 35");
        assertEquals("5.0 15.0 25.0 35.0", borderImageOutset.getCssValue());

        assertTrue(5F == borderImageOutset.getTop());
        assertTrue(15F == borderImageOutset.getRight());
        assertTrue(25F == borderImageOutset.getBottom());
        assertTrue(35F == borderImageOutset.getLeft());
        assertNull(borderImageOutset.getTopUnit());
        assertNull(borderImageOutset.getRightUnit());
        assertNull(borderImageOutset.getBottomUnit());
        assertNull(borderImageOutset.getLeftUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageOutsetString2() {

        BorderImageOutset borderImageOutset = new BorderImageOutset(
                "5px 15px 25px 35px");
        assertEquals("5.0px 15.0px 25.0px 35.0px",
                borderImageOutset.getCssValue());

        assertTrue(5F == borderImageOutset.getTop());
        assertTrue(15F == borderImageOutset.getRight());
        assertTrue(25F == borderImageOutset.getBottom());
        assertTrue(35F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.PX, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.PX, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.PX, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.PX, borderImageOutset.getLeftUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageOutsetString3() {

        BorderImageOutset borderImageOutset = new BorderImageOutset(
                "25px 25px 25px 25px");
        assertEquals("25.0px", borderImageOutset.getCssValue());
        assertTrue(25F == borderImageOutset.getTop());
        assertTrue(25F == borderImageOutset.getRight());
        assertTrue(25F == borderImageOutset.getBottom());
        assertTrue(25F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.PX, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.PX, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.PX, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.PX, borderImageOutset.getLeftUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageOutsetString4() {
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 25px 55px");
            assertEquals("25.0px 25.0px 25.0px 55.0px",
                    borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 25px 25px");
            assertEquals("25.0px", borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "35px 25px 45px 25px ");
            assertEquals("35.0px 25.0px 45.0px",
                    borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "35px 25px 35px 25px ");
            assertEquals("35.0px 25.0px", borderImageOutset.getCssValue());
        }

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageOutsetString5() {

        BorderImageOutset borderImageOutset = new BorderImageOutset(
                BorderImageOutset.INHERIT);
        assertEquals(BorderImageOutset.INHERIT,
                borderImageOutset.getCssValue());

        assertNull(borderImageOutset.getTop());
        assertNull(borderImageOutset.getRight());
        assertNull(borderImageOutset.getBottom());
        assertNull(borderImageOutset.getLeft());

        assertNull(borderImageOutset.getTopUnit());
        assertNull(borderImageOutset.getRightUnit());
        assertNull(borderImageOutset.getBottomUnit());
        assertNull(borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset(com.webfirmframework.wffweb.css.css3.BorderImageOutset)}
     * .
     */
    @Test
    public void testBorderImageOutsetBorderImageOutset() {
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    new BorderImageOutset("35px 25px 35px 25px "));
            assertEquals("35.0px 25.0px", borderImageOutset.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset(float)}
     * .
     */
    @Test
    public void testBorderImageOutsetFloat() {

        BorderImageOutset borderImageOutset = new BorderImageOutset(55F);
        assertEquals("55.0%", borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(55F == borderImageOutset.getTop());
        assertTrue(55F == borderImageOutset.getRight());
        assertTrue(55F == borderImageOutset.getBottom());
        assertTrue(55F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#BorderImageOutset(float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testBorderImageOutsetFloatCssLengthUnit() {

        BorderImageOutset borderImageOutset = new BorderImageOutset(55F,
                CssLengthUnit.EM);
        assertEquals("55.0em", borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(55F == borderImageOutset.getTop());
        assertTrue(55F == borderImageOutset.getRight());
        assertTrue(55F == borderImageOutset.getBottom());
        assertTrue(55F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.EM, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.EM, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.EM, borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setPercent(float)}
     * .
     */
    @Test
    public void testSetPercent() {

        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setPercent(55F);
        assertEquals("55.0%", borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(55F == borderImageOutset.getTop());
        assertTrue(55F == borderImageOutset.getRight());
        assertTrue(55F == borderImageOutset.getBottom());
        assertTrue(55F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getCssName()}
     * .
     */
    @Test
    public void testGetCssName() {
        assertEquals(CssNameConstants.BORDER_IMAGE_OUTSET,
                new BorderImageOutset().getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getCssValue()}
     * .
     */
    @Test
    public void testGetCssValue() {
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 25px 55px");
            assertEquals("25.0px 25.0px 25.0px 55.0px",
                    borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 55px 55px");
            assertEquals("25.0px 25.0px 55.0px 55.0px",
                    borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 25px 25px");
            assertEquals("25.0px", borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "35px 25px 45px 25px ");
            assertEquals("35.0px 25.0px 45.0px",
                    borderImageOutset.getCssValue());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "35px 25px 35px 25px ");
            assertEquals("35.0px 25.0px", borderImageOutset.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#toString()}
     * .
     */
    @Test
    public void testToString() {
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 55px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_OUTSET
                            + ": 25.0px 25.0px 55.0px 55.0px",
                    borderImageOutset.toString());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 25px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_OUTSET
                            + ": 25.0px 25.0px 25.0px 55.0px",
                    borderImageOutset.toString());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 55px 55px");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_OUTSET
                            + ": 25.0px 25.0px 55.0px 55.0px",
                    borderImageOutset.toString());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "25px 25px 25px 25px");
            assertEquals(CssNameConstants.BORDER_IMAGE_OUTSET + ": 25.0px",
                    borderImageOutset.toString());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "35px 25px 45px 25px ");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_OUTSET
                            + ": 35.0px 25.0px 45.0px",
                    borderImageOutset.toString());
        }
        {
            BorderImageOutset borderImageOutset = new BorderImageOutset(
                    "35px 25px 35px 25px ");
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_OUTSET + ": 35.0px 25.0px",
                    borderImageOutset.toString());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setCssValue("55%");
        assertEquals("55.0%", borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(55F == borderImageOutset.getTop());
        assertTrue(55F == borderImageOutset.getRight());
        assertTrue(55F == borderImageOutset.getBottom());
        assertTrue(55F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setAsInitial()}
     * .
     */
    @Test
    public void testSetAsInitial() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setCssValue("55%");
        assertEquals("55.0%", borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(55F == borderImageOutset.getTop());
        assertTrue(55F == borderImageOutset.getRight());
        assertTrue(55F == borderImageOutset.getBottom());
        assertTrue(55F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getLeftUnit());

        borderImageOutset.setAsInitial();

        assertNull(borderImageOutset.getTop());
        assertNull(borderImageOutset.getRight());
        assertNull(borderImageOutset.getBottom());
        assertNull(borderImageOutset.getLeft());

        assertNull(borderImageOutset.getTopUnit());
        assertNull(borderImageOutset.getRightUnit());
        assertNull(borderImageOutset.getBottomUnit());
        assertNull(borderImageOutset.getLeftUnit());

    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setAsInherit()}
     * .
     */
    @Test
    public void testSetAsInherit() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setCssValue("55%");
        assertEquals("55.0%", borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(55F == borderImageOutset.getTop());
        assertTrue(55F == borderImageOutset.getRight());
        assertTrue(55F == borderImageOutset.getBottom());
        assertTrue(55F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.PER, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.PER, borderImageOutset.getLeftUnit());

        borderImageOutset.setAsInherit();

        assertNull(borderImageOutset.getTop());
        assertNull(borderImageOutset.getRight());
        assertNull(borderImageOutset.getBottom());
        assertNull(borderImageOutset.getLeft());

        assertNull(borderImageOutset.getTopUnit());
        assertNull(borderImageOutset.getRightUnit());
        assertNull(borderImageOutset.getBottomUnit());
        assertNull(borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setBorderImageTopRightBottomLeft(java.lang.Float, java.lang.Float, java.lang.Float, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBorderImageTopRightBottomLeftFloatFloatFloatFloatCssLengthUnit() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setBorderImageTopRightBottomLeft(5F, 10F, 15F, 20F,
                CssLengthUnit.IN);
        assertEquals("5.0in 10.0in 15.0in 20.0in",
                borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(5F == borderImageOutset.getTop());
        assertTrue(10F == borderImageOutset.getRight());
        assertTrue(15F == borderImageOutset.getBottom());
        assertTrue(20F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.IN, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.IN, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.IN, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.IN, borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setBorderImageTopRightBottomLeft(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBorderImageTopRightBottomLeftFloatCssLengthUnitFloatCssLengthUnitFloatCssLengthUnitFloatCssLengthUnit() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(5F == borderImageOutset.getTop());
        assertTrue(10F == borderImageOutset.getRight());
        assertTrue(15F == borderImageOutset.getBottom());
        assertTrue(20F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setTop(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetTop() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(5F == borderImageOutset.getTop());
        assertTrue(10F == borderImageOutset.getRight());
        assertTrue(15F == borderImageOutset.getBottom());
        assertTrue(20F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageOutset.getLeftUnit());

        borderImageOutset.setTop(55F, CssLengthUnit.PT);

        assertEquals("55.0pt 10.0cm 15.0em 20.0ex",
                borderImageOutset.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageOutset.getTopUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setRight(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetRight() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(5F == borderImageOutset.getTop());
        assertTrue(10F == borderImageOutset.getRight());
        assertTrue(15F == borderImageOutset.getBottom());
        assertTrue(20F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageOutset.getLeftUnit());

        borderImageOutset.setRight(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 55.0pt 15.0em 20.0ex",
                borderImageOutset.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageOutset.getRightUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setBottom(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetBottom() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(5F == borderImageOutset.getTop());
        assertTrue(10F == borderImageOutset.getRight());
        assertTrue(15F == borderImageOutset.getBottom());
        assertTrue(20F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageOutset.getLeftUnit());

        borderImageOutset.setBottom(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 10.0cm 55.0pt 20.0ex",
                borderImageOutset.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageOutset.getBottomUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#setLeft(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testSetLeft() {
        BorderImageOutset borderImageOutset = new BorderImageOutset();
        borderImageOutset.setBorderImageTopRightBottomLeft(5F, CssLengthUnit.CH,
                10F, CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                CssLengthUnit.EX);
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                borderImageOutset.getCssValue());

        assertNotNull(borderImageOutset.getTop());
        assertNotNull(borderImageOutset.getRight());
        assertNotNull(borderImageOutset.getBottom());
        assertNotNull(borderImageOutset.getLeft());

        assertTrue(5F == borderImageOutset.getTop());
        assertTrue(10F == borderImageOutset.getRight());
        assertTrue(15F == borderImageOutset.getBottom());
        assertTrue(20F == borderImageOutset.getLeft());

        assertNotNull(borderImageOutset.getTopUnit());
        assertNotNull(borderImageOutset.getRightUnit());
        assertNotNull(borderImageOutset.getBottomUnit());
        assertNotNull(borderImageOutset.getLeftUnit());

        assertEquals(CssLengthUnit.CH, borderImageOutset.getTopUnit());
        assertEquals(CssLengthUnit.CM, borderImageOutset.getRightUnit());
        assertEquals(CssLengthUnit.EM, borderImageOutset.getBottomUnit());
        assertEquals(CssLengthUnit.EX, borderImageOutset.getLeftUnit());

        borderImageOutset.setLeft(55F, CssLengthUnit.PT);

        assertEquals("5.0ch 10.0cm 15.0em 55.0pt",
                borderImageOutset.getCssValue());
        assertEquals(CssLengthUnit.PT, borderImageOutset.getLeftUnit());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getTop()}.
     */
    /*
     * @Test public void testGetTop() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getTopUnit()}
       * .
       */
    /*
     * @Test public void testGetTopUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getRight()}
       * .
       */
    /*
     * @Test public void testGetRight() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getRightUnit()}
       * .
       */
    /*
     * @Test public void testGetRightUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getBottom()}
       * .
       */
    /*
     * @Test public void testGetBottom() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getBottomUnit()}
       * .
       */
    /*
     * @Test public void testGetBottomUnit() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getLeft()}
       * .
       */
    /*
     * @Test public void testGetLeft() { fail("Not yet implemented"); }
     *//**
       * Test method for
       * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getLeftUnit()}
       * .
       */
    /*
     * @Test public void testGetLeftUnit() { fail("Not yet implemented"); }
     */

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#getProducedCssValue(java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit, java.lang.Float, com.webfirmframework.wffweb.css.CssLengthUnit)}
     * .
     */
    @Test
    public void testGetProducedCssValue() {
        assertEquals("5.0ch 10.0cm 15.0em 20.0ex",
                BorderImageOutset.getProducedCssValue(5F, CssLengthUnit.CH, 10F,
                        CssLengthUnit.CM, 15F, CssLengthUnit.EM, 20F,
                        CssLengthUnit.EX));

        assertEquals("5.0px",
                BorderImageOutset.getProducedCssValue(5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 5F,
                        CssLengthUnit.PX));

        assertEquals("5.0px 15.0px",
                BorderImageOutset.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX));

        assertEquals("5.0px 15.0px 25.0px",
                BorderImageOutset.getProducedCssValue(5F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX, 25F, CssLengthUnit.PX, 15F,
                        CssLengthUnit.PX));
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid() {

        {
            final boolean valid = BorderImageOutset.isValid("45px");
            assertTrue(valid);
           assertTrue(BorderImageOutset.isValid("55"));
        }
        {
            final boolean valid = BorderImageOutset.isValid("45em 45px");
            assertTrue(valid);
            final boolean invalid = BorderImageOutset.isValid("45em dfd 45px");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderImageOutset.isValid("45%");
            assertTrue(valid);
            final boolean invalid = BorderImageOutset.isValid("45 px");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderImageOutset.isValid("45em");
            assertTrue(valid);
            final boolean invalid = BorderImageOutset.isValid("45sem");
           assertFalse(invalid);
        }
        {
            final boolean valid = BorderImageOutset.isValid("45rem");
            assertTrue(valid);
        }
        {
            final boolean valid = BorderImageOutset.isValid("45rem auto");
            assertFalse(valid);
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageOutset#hasPredefinedConstantValue()}
     * .
     */
    @Test
    public void testHasPredefinedConstantValue() {
        assertTrue(new BorderImageOutset(BorderImageOutset.INHERIT)
                .hasPredefinedConstantValue());
        assertTrue(new BorderImageOutset(BorderImageOutset.INITIAL)
                .hasPredefinedConstantValue());
        assertFalse(new BorderImageOutset().hasPredefinedConstantValue());
    }

}
