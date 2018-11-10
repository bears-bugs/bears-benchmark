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

import com.webfirmframework.wffweb.css.CssNameConstants;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class BorderImageRepeatTest {

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#BorderImageRepeat()}
     * .
     */
    @Test
    public void testBorderImageRepeat() {
        BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
        assertNotNull(borderImageRepeat.getHorizontal());
        assertNotNull(borderImageRepeat.getVertical());
        assertEquals(BorderImageRepeat.STRETCH,
                borderImageRepeat.getHorizontal());
        assertEquals(BorderImageRepeat.STRETCH, borderImageRepeat.getVertical());
        assertEquals(BorderImageRepeat.STRETCH, borderImageRepeat.getCssValue());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#BorderImageRepeat(java.lang.String)}
     * .
     */
    @Test
    public void testBorderImageRepeatString() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat(
                    BorderImageRepeat.REPEAT);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getCssValue());
        }

        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat(
                    BorderImageRepeat.ROUND + " " + BorderImageRepeat.STRETCH);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#BorderImageRepeat(com.webfirmframework.wffweb.css.css3.BorderImageRepeat)}
     * .
     */
    @Test
    public void testBorderImageRepeatBorderImageRepeat() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat(
                    new BorderImageRepeat(BorderImageRepeat.REPEAT));
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getCssValue());
        }

        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat(
                    new BorderImageRepeat(BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH));
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
        assertEquals(CssNameConstants.BORDER_IMAGE_REPEAT,
                borderImageRepeat.getCssName());
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {

        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.REPEAT);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getCssValue());
        }

        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#toString()}.
     */
    @Test
    public void testToString() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.REPEAT);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getVertical());
            assertEquals(CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                    + BorderImageRepeat.REPEAT, borderImageRepeat.toString());
        }

        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                            + BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH,
                    borderImageRepeat.toString());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#setCssValue(java.lang.String)}
     * .
     */
    @Test
    public void testSetCssValueString() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.REPEAT);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getVertical());
            assertEquals(CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                    + BorderImageRepeat.REPEAT, borderImageRepeat.toString());
            assertEquals(BorderImageRepeat.REPEAT,
                    borderImageRepeat.getCssValue());
        }

        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                            + BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH,
                    borderImageRepeat.toString());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#setVertical(java.lang.String)}
     * .
     */
    @Test
    public void testSetVertical() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.ROUND);
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getVertical());
            borderImageRepeat.setVertical(BorderImageRepeat.STRETCH);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                            + BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH,
                    borderImageRepeat.toString());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#setHorizontal(java.lang.String)}
     * .
     */
    @Test
    public void testSetHorizontal() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.STRETCH);
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getHorizontal());
            borderImageRepeat.setHorizontal(BorderImageRepeat.ROUND);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                            + BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH,
                    borderImageRepeat.toString());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#getVertical()}.
     */
    @Test
    public void testGetVertical() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.ROUND);
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getVertical());
            borderImageRepeat.setVertical(BorderImageRepeat.STRETCH);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                            + BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH,
                    borderImageRepeat.toString());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#getHorizontal()}.
     */
    @Test
    public void testGetHorizontal() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.STRETCH);
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getHorizontal());
            borderImageRepeat.setHorizontal(BorderImageRepeat.ROUND);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                            + BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH,
                    borderImageRepeat.toString());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.STRETCH);
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getHorizontal());
            borderImageRepeat.setHorizontal(BorderImageRepeat.ROUND);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                            + BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH,
                    borderImageRepeat.toString());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());

            borderImageRepeat.setAsInitial();

            assertNull(borderImageRepeat.getHorizontal());
            assertNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.INITIAL,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        {
            BorderImageRepeat borderImageRepeat = new BorderImageRepeat();
            borderImageRepeat.setCssValue(BorderImageRepeat.STRETCH);
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getHorizontal());
            borderImageRepeat.setHorizontal(BorderImageRepeat.ROUND);
            assertNotNull(borderImageRepeat.getHorizontal());
            assertNotNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.ROUND,
                    borderImageRepeat.getHorizontal());
            assertEquals(BorderImageRepeat.STRETCH,
                    borderImageRepeat.getVertical());
            assertEquals(
                    CssNameConstants.BORDER_IMAGE_REPEAT + ": "
                            + BorderImageRepeat.ROUND + " "
                            + BorderImageRepeat.STRETCH,
                    borderImageRepeat.toString());
            assertEquals(BorderImageRepeat.ROUND + " "
                    + BorderImageRepeat.STRETCH,
                    borderImageRepeat.getCssValue());

            borderImageRepeat.setAsInherit();

            assertNull(borderImageRepeat.getHorizontal());
            assertNull(borderImageRepeat.getVertical());
            assertEquals(BorderImageRepeat.INHERIT,
                    borderImageRepeat.getCssValue());
        }
    }

    /**
     * Test method for
     * {@link com.webfirmframework.wffweb.css.css3.BorderImageRepeat#isValid(java.lang.String)}
     * .
     */
    @Test
    public void testIsValid() {
        assertTrue(BorderImageRepeat.isValid(BorderImageRepeat.INITIAL));
        assertTrue(BorderImageRepeat.isValid(BorderImageRepeat.INHERIT));
        assertTrue(BorderImageRepeat.isValid(BorderImageRepeat.REPEAT));
        assertTrue(BorderImageRepeat.isValid(BorderImageRepeat.ROUND));
        assertTrue(BorderImageRepeat.isValid(BorderImageRepeat.STRETCH));
        assertTrue(BorderImageRepeat.isValid(BorderImageRepeat.ROUND + " "
                + BorderImageRepeat.STRETCH));

        assertFalse(BorderImageRepeat.isValid(BorderImageRepeat.ROUND + " "
                + BorderImageRepeat.INHERIT));
        assertFalse(BorderImageRepeat.isValid(BorderImageRepeat.ROUND + " "
                + BorderImageRepeat.INITIAL));
        assertFalse(BorderImageRepeat.isValid(BorderImageRepeat.INHERIT + " "
                + BorderImageRepeat.STRETCH));
        assertFalse(BorderImageRepeat.isValid(BorderImageRepeat.INITIAL + " "
                + BorderImageRepeat.STRETCH));
        assertFalse(BorderImageRepeat.isValid(BorderImageRepeat.ROUND + " "
                + BorderImageRepeat.STRETCH + " " + BorderImageRepeat.REPEAT));
    }

}
