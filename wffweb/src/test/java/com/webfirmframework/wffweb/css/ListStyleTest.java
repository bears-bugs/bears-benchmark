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
* @author WFF
*/
package com.webfirmframework.wffweb.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.util.StringBuilderUtil;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class ListStyleTest {

    @Test
    public void testListStyle() {
        final ListStyle listStyle = new ListStyle();
        assertEquals("disc outside none", listStyle.getCssValue());
        assertEquals(ListStyleType.DISC, listStyle.getListStyleType());
        assertEquals(ListStylePosition.OUTSIDE,
                listStyle.getListStylePosition());
        assertTrue(listStyle.getListStyleImage().hasInbuiltValue());
        assertEquals(ListStyleImage.NONE,
                listStyle.getListStyleImage().getCssValue());
        // fail("Not yet implemented");
    }

    @Test
    public void testListStyleString() {
        final ListStyle listStyle = new ListStyle(
                "circle inside url(Test.png)");
        assertEquals("circle inside url(\"Test.png\")",
                listStyle.getCssValue());
        assertEquals(ListStyleType.CIRCLE, listStyle.getListStyleType());
        assertEquals(ListStylePosition.INSIDE,
                listStyle.getListStylePosition());
        assertEquals("url(\"Test.png\")",
                listStyle.getListStyleImage().getCssValue());
        // fail("Not yet implemented");
    }

    @Test
    public void testListStyleListStyle() {
        final ListStyle listStyle = new ListStyle(
                "circle inside url(Test.png)");
        final ListStyle listStyle1 = new ListStyle(listStyle);
        assertEquals("circle inside url(\"Test.png\")",
                listStyle1.getCssValue());
        assertEquals(ListStyleType.CIRCLE, listStyle1.getListStyleType());
        assertEquals(ListStylePosition.INSIDE,
                listStyle1.getListStylePosition());
        assertEquals("url(\"Test.png\")",
                listStyle1.getListStyleImage().getCssValue());
        // fail("Not yet implemented");
    }

    @Test
    public void testGetCssName() {
        final ListStyle listStyle = new ListStyle();
        assertEquals(CssNameConstants.LIST_STYLE_TYPE, listStyle.getCssName());
        // fail("Not yet implemented");
    }

    @Test
    public void testGetCssValue() {
        final ListStyle listStyle = new ListStyle();
        assertEquals("disc outside none", listStyle.getCssValue());
        final ListStyle listStyle1 = new ListStyle(
                "circle inside url(Test.png)");
        assertEquals("circle inside url(\"Test.png\")",
                listStyle1.getCssValue());
        // fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        final ListStyle listStyle = new ListStyle(
                "circle inside url(Test.png)");
        assertEquals("list-style-type: circle inside url(\"Test.png\")",
                listStyle.toString());
        // fail("Not yet implemented");
    }

    @Test
    public void testSetCssValueString() {
        try {
            final ListStyle listStyle = new ListStyle();
            listStyle.setCssValue("circle inside url(Test.png)");
            assertEquals("circle inside url(\"Test.png\")",
                    listStyle.getCssValue());
            assertEquals(ListStyleType.CIRCLE, listStyle.getListStyleType());
            assertEquals(ListStylePosition.INSIDE,
                    listStyle.getListStylePosition());
            assertEquals("url(\"Test.png\")",
                    listStyle.getListStyleImage().getCssValue());
        } catch (final Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testIsValid() {
        assertTrue(ListStyle.isValid("circle inside url(Test.png)"));

        assertTrue(ListStyle.isValid("circle"));
        assertTrue(ListStyle.isValid("inside url(Test.png)"));
        assertTrue(ListStyle.isValid("url(Test.png)"));
        assertTrue(ListStyle.isValid(ListStyleType.ARMENIAN.getCssValue() + " "
                + ListStylePosition.OUTSIDE.getCssValue() + " url(Test.png)"));
        assertTrue(ListStyle.isValid(ListStyleType.CJK_IDEOGRAPHIC.getCssValue()
                + " " + ListStylePosition.INITIAL.getCssValue()
                + " url(Test.png)"));

        assertFalse(ListStyle.isValid("cir cle inside url(Test.png)"));
        assertFalse(ListStyle.isValid("dircle inside url(Test.png)"));
        assertFalse(ListStyle.isValid("circle ins ide url(Test.png)"));
        assertFalse(ListStyle.isValid("circle insside url(Test.png)"));
        assertFalse(ListStyle.isValid("circle inside ur l(Test.png)"));
        assertFalse(ListStyle.isValid("circle inside urll(Test.png)"));
        // fail("Not yet implemented");
    }

    @Test
    public void testSetAsInitial() {
        final ListStyle listStyle = new ListStyle();
        assertNotNull(listStyle.getListStyleType());
        final ListStylePosition listStylePosition = listStyle
                .getListStylePosition();
        assertNotNull(listStylePosition);
        final ListStyleImage listStyleImage = listStyle.getListStyleImage();
        assertNotNull(listStyleImage);
        listStyle.setAsInitial();
        assertEquals(ListStyle.INITIAL, listStyle.getCssValue());
        assertNull(listStyle.getListStyleType());
        assertNull(listStyle.getListStylePosition());
        assertNull(listStyle.getListStyleImage());
        assertFalse(listStyleImage.isAlreadyInUse());
    }

    @Test
    public void testSetAsInherit() {
        final ListStyle listStyle = new ListStyle();
        listStyle.setAsInherit();
        assertEquals(ListStyle.INHERIT, listStyle.getCssValue());
    }

    @Test
    public void testGetListStyleImage() {
        try {
            final ListStyle listStyle = new ListStyle();
            listStyle.setCssValue("circle inside url(Test.png)");
            assertEquals("circle inside url(\"Test.png\")",
                    listStyle.getCssValue());
            assertNotNull(listStyle.getListStyleImage());
            assertEquals("url(\"Test.png\")",
                    listStyle.getListStyleImage().getCssValue());
        } catch (final Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testGetListStylePosition() {
        try {
            final ListStyle listStyle = new ListStyle();
            listStyle.setCssValue("circle inside url(Test.png)");
            assertEquals("circle inside url(\"Test.png\")",
                    listStyle.getCssValue());
            assertNotNull(listStyle.getListStylePosition());
            assertEquals(ListStylePosition.INSIDE,
                    listStyle.getListStylePosition());
        } catch (final Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testGetListStyleType() {
        try {
            final ListStyle listStyle = new ListStyle();
            listStyle.setCssValue("circle inside url(Test.png)");
            assertEquals("circle inside url(\"Test.png\")",
                    listStyle.getCssValue());
            assertNotNull(listStyle.getListStyleType());
            assertEquals(ListStyleType.CIRCLE, listStyle.getListStyleType());
        } catch (final Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }

    }

    @Test
    public void testSetListStyleType() {
        try {
            final ListStyle listStyle = new ListStyle();
            listStyle.setCssValue("circle inside url(Test.png)");
            listStyle.setListStyleType(ListStyleType.DECIMAL);
            assertEquals("decimal inside url(\"Test.png\")",
                    listStyle.getCssValue());
            assertNotNull(listStyle.getListStyleType());
            assertEquals(ListStyleType.DECIMAL, listStyle.getListStyleType());
        } catch (final Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testSetListStylePosition() {
        try {
            final ListStyle listStyle = new ListStyle();
            listStyle.setCssValue("circle inside url(Test.png)");
            listStyle.setListStyleType(ListStyleType.DECIMAL);
            listStyle.setListStylePosition(ListStylePosition.OUTSIDE);
            assertEquals("decimal outside url(\"Test.png\")",
                    listStyle.getCssValue());
            assertNotNull(listStyle.getListStylePosition());
            assertEquals(ListStylePosition.OUTSIDE,
                    listStyle.getListStylePosition());
        } catch (final Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testSetListStyleImage() {
        try {
            final ListStyle listStyle = new ListStyle();
            listStyle.setCssValue("circle inside url(Test.png)");
            listStyle.setListStyleImage(
                    new ListStyleImage().setUrl("Another.jpg"));
            assertEquals("circle inside url(\"Another.jpg\")",
                    listStyle.getCssValue());
            assertNotNull(listStyle.getListStyleImage());
            assertEquals("url(\"Another.jpg\")",
                    listStyle.getListStyleImage().getCssValue());
        } catch (final Exception e) {
            e.printStackTrace();
            fail("testSetCssValueString failed");
        }
    }

    @Test
    public void testName() throws Exception {
        // type position image
        String sample = "cjk-ideographic inside url(\"Sqpurple.gif\")";
        sample = sample.trim();
        final String[] sampleParts = sample.split(" ");

        ListStyleType listStyleType = null;
        ListStylePosition listStylePosition = null;
        ListStyleImage listStyleImage = null;

        for (final String eachPart : sampleParts) {
            if (listStyleType == null && ListStyleType.isValid(eachPart)) {
                listStyleType = ListStyleType.getThis(eachPart);
                // System.out.println(listStyleType);
            } else if (listStylePosition == null
                    && ListStylePosition.isValid(eachPart)) {
                listStylePosition = ListStylePosition.getThis(eachPart);
                // System.out.println(listStylePosition);
            } else if (listStyleImage == null
                    && ListStyleImage.isValid(eachPart)) {
                listStyleImage = new ListStyleImage(eachPart);
                // System.out.println(listStyleImage);
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
         boolean invalid = true;
        if (listStyleType != null) {
            cssValueBuilder.append(listStyleType.getCssValue());
            cssValueBuilder.append(" ");
             invalid = false;
        }
        if (listStylePosition != null) {
            cssValueBuilder.append(listStylePosition.getCssValue());
            cssValueBuilder.append(" ");
             invalid = false;
        }
        if (listStyleImage != null) {
            cssValueBuilder.append(listStyleImage.getCssValue());
            cssValueBuilder.append(" ");
             invalid = false;
        }
         assertFalse(invalid);
         assertEquals("cjk-ideographic inside url(\"Sqpurple.gif\")", StringBuilderUtil.getTrimmedString(cssValueBuilder));
         assertTrue(ListStyle.isValid(sample));

        // System.out.println(part1);
        // System.out.println(part2);
        // System.out.println(part3);

    }

    @Test
    public void testListStyleNullValue() {
        try {
            final ListStyle listStyle = new ListStyle();
            listStyle.setListStyleImage(null);
            listStyle.setListStylePosition(null);
            listStyle.setListStyleType(null);
            assertEquals("inherit", listStyle.getCssValue());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = InvalidValueException.class)
    public void testListStyleInvalidValueValue() {

        final ListStyle listStyle = new ListStyle("inherit");
        final ListStyleType listStyleType = listStyle.getListStyleType();
        final ListStyleImage listStyleImage = listStyle.getListStyleImage();

        assertNull(listStyleImage);

        assertNull(listStyleType);

        assertEquals("inherit", listStyle.getCssValue());

        final ListStylePosition listStylePosition = listStyle
                .getListStylePosition();

        assertNull(listStylePosition);

        assertEquals("inherit", listStyle.getCssValue());

        final ListStyleImage listStyleImageTemp = new ListStyleImage("initial");
        try {
            listStyle.setListStyleImage(listStyleImageTemp);// must throw an
                                                            // InvalidValueException
        } catch (final InvalidValueException e) {

            assertNull(listStyle.getListStyleImage());

            listStyleImageTemp.setAsNone();
            listStyle.setListStyleImage(listStyleImageTemp);
            final String listStyleImageTempCssValue = listStyleImageTemp
                    .getCssValue();
            try {
                listStyleImageTemp.setAsInherit();// must throw an
                                                  // InvalidValueException
            } catch (final InvalidValueException e1) {
                assertEquals(listStyleImageTempCssValue,
                        listStyleImageTemp.getCssValue());
                throw e1;
            }

        }

    }

    @Test(expected = InvalidValueException.class)
    public void testListStyleSetCssValueInvalidValueValue() {
        try {
            new ListStyle("disc outside initial");
        } catch (final Exception e1) {
            try {
                new ListStyle("disc outside inherit");
            } catch (final Exception e2) {
                final ListStyle listStyle = new ListStyle();
                try {
                    listStyle.setCssValue("disc outside inherit");
                } catch (final Exception e) {
                    assertEquals("disc outside none", listStyle.getCssValue());
                    throw e;
                }
            }
        }
    }

}
