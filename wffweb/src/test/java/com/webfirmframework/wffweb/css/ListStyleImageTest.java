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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class ListStyleImageTest {

    @Test
    public void testSetCssValue() {
        ListStyleImage listStyleImage = new ListStyleImage();
        listStyleImage.setUrl("http://project.com/images/TestImage.png");
        assertEquals(
                "list-style-image: url(\"http://project.com/images/TestImage.png\")",
                listStyleImage.toString());
        assertEquals("http://project.com/images/TestImage.png",
                listStyleImage.getUrl());
        listStyleImage
                .setCssValue("url(\"http://project.com/images/TestImage.png\")");
        assertEquals("http://project.com/images/TestImage.png",
                listStyleImage.getUrl());

        System.out.println(listStyleImage);
        // fail("Not yet implemented");
    }

    @Test
    public void testListStyleImage() {
        ListStyleImage listStyleImage = new ListStyleImage();
        assertEquals(ListStyleImage.NONE, listStyleImage.getCssValue());
        // fail("Not yet implemented");
    }

    @Test
    public void testListStyleImageString() {
        ListStyleImage listStyleImage = new ListStyleImage(
                ListStyleImage.INHERIT, false);

        assertEquals(ListStyleImage.INHERIT, listStyleImage.getCssValue());

        listStyleImage = new ListStyleImage(
                "url(\"http://project.com/images/TestImage.png\")", false);

        assertEquals("http://project.com/images/TestImage.png",
                listStyleImage.getUrl());
    }

    @Test
    public void testListStyleImageListStyleImage() {
        ListStyleImage listStyleImage = new ListStyleImage(
                ListStyleImage.INITIAL, false);
        assertEquals(ListStyleImage.INITIAL, listStyleImage.getCssValue());
        ListStyleImage listStyleImage2 = new ListStyleImage(listStyleImage);
        assertEquals(ListStyleImage.INITIAL, listStyleImage2.getCssValue());
        // fail("Not yet implemented");
    }

    @Test
    public void testGetCssName() {
        ListStyleImage listStyleImage = new ListStyleImage(
                ListStyleImage.INITIAL, false);
        assertEquals(CssNameConstants.LIST_STYLE_IMAGE,
                listStyleImage.getCssName());
        // fail("Not yet implemented");
    }

    @Test
    public void testGetCssValue() {
        ListStyleImage listStyleImage = new ListStyleImage(
                ListStyleImage.INITIAL);
        assertEquals(ListStyleImage.INITIAL, listStyleImage.getCssValue());
        // fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        ListStyleImage listStyleImage = new ListStyleImage();
        listStyleImage.setUrl("http://project.com/images/TestImage.png");
        assertEquals(
                "list-style-image: url(\"http://project.com/images/TestImage.png\")",
                listStyleImage.toString());
        // fail("Not yet implemented");
    }

    @Test
    public void testSetUrl() {
        ListStyleImage listStyleImage = new ListStyleImage();
        listStyleImage.setUrl("http://project.com/images/TestImage.png");
        assertEquals("url(\"http://project.com/images/TestImage.png\")",
                listStyleImage.getCssValue());
    }

    @Test
    public void testGetUrl() {
        ListStyleImage listStyleImage = new ListStyleImage();
        listStyleImage.setUrl("http://project.com/images/TestImage.png");
        assertEquals("url(\"http://project.com/images/TestImage.png\")",
                listStyleImage.getCssValue());
    }

    @Test
    public void testSetAsInitial() {
        ListStyleImage listStyleImage = new ListStyleImage();
        listStyleImage.setAsInitial();
        assertEquals("initial", listStyleImage.getCssValue());
    }

    @Test
    public void testSetAsInherit() {
        ListStyleImage listStyleImage = new ListStyleImage();
        listStyleImage.setAsInherit();
        assertEquals("inherit", listStyleImage.getCssValue());
    }

    @Test
    public void testSetAsNone() {
        ListStyleImage listStyleImage = new ListStyleImage();
        listStyleImage.setAsNone();
        assertEquals("none", listStyleImage.getCssValue());
    }

    @Test
    public void testHasInbuiltValue() {
        ListStyleImage listStyleImage = new ListStyleImage();
        assertTrue(listStyleImage.hasInbuiltValue());
        listStyleImage.setUrl("test/TestImage.png");
        assertFalse(listStyleImage.hasInbuiltValue());

        // fail("Not yet implemented");
    }

}
