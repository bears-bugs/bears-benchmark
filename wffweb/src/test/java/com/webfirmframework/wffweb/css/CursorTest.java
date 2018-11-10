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

import java.util.List;
import java.util.Objects;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;

/**
 * 
 * @author WFF
 * @since 1.0.0
 */
public class CursorTest {

    @Test
    public void testSetCssValue() {
        Cursor cursor = new Cursor();
        String cssValue = "url(\"Test.png\") , url(sample) 7 8  ,    auto";
        cursor.setCssValue(cssValue);
        assertEquals("cursor: url(\"Test.png\"), url(\"sample\") 7 8, auto",
                cursor.toString());
        assertEquals(Cursor.AUTO, cursor.getCursorType());
        List<UrlCss3Value> urlCss3ValuesFirst = cursor.getUrlCss3Values();
        assertEquals(2, urlCss3ValuesFirst.size());
        assertEquals("Test.png", urlCss3ValuesFirst.get(0).getUrl());
        assertEquals("sample", urlCss3ValuesFirst.get(1).getUrl());
        assertEquals(7, urlCss3ValuesFirst.get(1).getX());
        assertEquals(8, urlCss3ValuesFirst.get(1).getY());

        cssValue = "url(\"Another.png\") , url(yes.png) 7 8  ,    default";
        cursor.setCssValue(cssValue);
        List<UrlCss3Value> urlCss3ValuesSecond = cursor.getUrlCss3Values();//each time returns a new unmodifiable List
        assertFalse(urlCss3ValuesSecond == urlCss3ValuesFirst);
        cssValue = "url(\"No.png\") 77 98  ,    default";
        cursor.setCssValue(cssValue);
        List<UrlCss3Value> urlCss3ValuesThird = cursor.getUrlCss3Values();

        assertEquals(1, urlCss3ValuesThird.size());
        assertEquals("No.png", urlCss3ValuesThird.get(0).getUrl());
        assertEquals(77, urlCss3ValuesFirst.get(0).getX());
        assertEquals(98, urlCss3ValuesFirst.get(0).getY());

        assertFalse(Objects.equals(urlCss3ValuesSecond, urlCss3ValuesThird));
        assertEquals(Cursor.DEFAULT, cursor.getCursorType());

        // TODO add code to check state change informer
    }

    @Test
    public void testCursor() {
        Cursor cursor = new Cursor();
        assertEquals(Cursor.DEFAULT, cursor.getCssValue());
        assertEquals(Cursor.DEFAULT, cursor.getCursorType());
        assertEquals("cursor: default", cursor.toString());
    }

    @Test
    public void testCursorString() {
        Cursor cursor = new Cursor(Cursor.GRAB);
        assertEquals(Cursor.GRAB, cursor.getCssValue());
        assertEquals(Cursor.GRAB, cursor.getCursorType());
        assertEquals("cursor: grab", cursor.toString());
    }

    @Test
    public void testCursorCursor() {
        Cursor cursorToCopy = new Cursor(Cursor.GRAB);
        Cursor cursor = new Cursor(cursorToCopy);
        assertEquals(Cursor.GRAB, cursor.getCssValue());
        assertEquals(Cursor.GRAB, cursor.getCursorType());
        assertEquals("cursor: grab", cursor.toString());
        // fail("Not yet implemented");
    }

    @Test
    public void testGetCssName() {
        Cursor cursor = new Cursor(Cursor.GRAB);
        assertEquals(CssNameConstants.CURSOR, cursor.getCssName());
    }

    @Test
    public void testGetCssValue() {
        Cursor cursor = new Cursor(Cursor.GRAB);
        assertEquals(Cursor.GRAB, cursor.getCssValue());
    }

    @Test
    public void testToString() {
        Cursor cursor = new Cursor(Cursor.CROSSHAIR);
        assertEquals("cursor: crosshair", cursor.toString());
    }

    @Test
    public void testSetCursorType() {
        Cursor cursor = new Cursor();
        cursor.setCursorType(Cursor.GRAB);
        assertEquals(Cursor.GRAB, cursor.getCursorType());
        assertEquals("cursor: grab", cursor.toString());
    }

    @Test
    public void testGetCursorType() {
        Cursor cursor = new Cursor();
        assertEquals(Cursor.DEFAULT, cursor.getCursorType());
        cursor.setCursorType(Cursor.GRAB);
        assertEquals(Cursor.GRAB, cursor.getCursorType());
    }

    @Test
    public void testSetCursorUrlsStringStringArray() {
        {
            Cursor cursor = new Cursor();
            cursor.setCursorUrls(Cursor.CELL, "/cursors/One.png",
                    "/cursors/Two.svg", "/cursors/Three.jpg");
            assertEquals(Cursor.CELL, cursor.getCursorType());
            assertEquals(
                    "cursor: url(\"/cursors/One.png\"), url(\"/cursors/Two.svg\"), url(\"/cursors/Three.jpg\"), cell",
                    cursor.toString());
        }
        
    }
    // there must be a cursorType set.
    @Test(expected = InvalidValueException.class)
    public void testSetCursorUrlsStringStringArrayInvalidValue() {
        Cursor cursor = new Cursor();
        cursor.setCursorUrls("/cursors/One.png",
                "/cursors/Two.svg", "/cursors/Three.jpg");
    }

    @Test
    public void testSetCursorUrlsStringUrlCss3ValueArray() {
        try {
            Cursor cursor = new Cursor();
            {
                UrlCss3Value urlCss3Value1 = new UrlCss3Value(
                        "url(\"/cursors/One.png\") 75 150");
                UrlCss3Value urlCss3Value2 = new UrlCss3Value(
                        "url(\"/cursors/Two.png\")");
                UrlCss3Value urlCss3Value3 = new UrlCss3Value(
                        "url(\"/cursors/Three.svg\") 175 50");
                cursor.setCursorUrls(Cursor.COPY, urlCss3Value1, urlCss3Value2,
                        urlCss3Value3);
                assertEquals(
                        "cursor: url(\"/cursors/One.png\") 75 150, url(\"/cursors/Two.png\"), url(\"/cursors/Three.svg\") 175 50, copy",
                        cursor.toString());
                urlCss3Value1.setX(120);
                urlCss3Value1.setY(550);
                assertEquals(
                        "cursor: url(\"/cursors/One.png\") 120 550, url(\"/cursors/Two.png\"), url(\"/cursors/Three.svg\") 175 50, copy",
                        cursor.toString());

                System.out.println(cursor.toString());

                UrlCss3Value urlCss3Value4 = new UrlCss3Value(
                        "url(\"Test.png\")");
                UrlCss3Value urlCss3Value5 = new UrlCss3Value("url(sample) 7 8");
                cursor.setCursorUrls(Cursor.AUTO, urlCss3Value4, urlCss3Value5);
                assertNotNull(urlCss3Value4.getStateChangeInformer());
                assertNotNull(urlCss3Value5.getStateChangeInformer());
                assertNull(urlCss3Value1.getStateChangeInformer());
                assertNull(urlCss3Value2.getStateChangeInformer());
                assertEquals(
                        "cursor: url(\"Test.png\"), url(\"sample\") 7 8, auto",
                        cursor.toString());
                assertEquals(Cursor.AUTO, cursor.getCursorType());
                List<UrlCss3Value> urlCss3ValuesFirst = cursor.getUrlCss3Values();
                assertEquals(2, urlCss3ValuesFirst.size());
                assertEquals("Test.png", urlCss3ValuesFirst.get(0).getUrl());
                assertEquals("sample", urlCss3ValuesFirst.get(1).getUrl());
                assertEquals(7, urlCss3ValuesFirst.get(1).getX());
                assertEquals(8, urlCss3ValuesFirst.get(1).getY());

                cursor.setCssValue("url(\"/cursors/Three.svg\") 175 50, copy");
                assertNotNull(urlCss3Value4.getStateChangeInformer());
                assertTrue(urlCss3Value4.isAlreadyInUse());
                assertNull(urlCss3Value5.getStateChangeInformer());
                assertFalse(urlCss3Value5.isAlreadyInUse());
                Cursor cursor2 = new Cursor();
                // gives WARNING: cloned urlCss3Value url("/cursors/Three.svg")
                // 175 50(hashcode: 1254328772) as it is already used by another
                // object
                cursor2.setCursorUrls(Cursor.ALL_SCROLL, urlCss3Value4);

            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("testSetCursorUrlsStringUrlCss3ValueArray failed");
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            fail("testSetCursorUrlsStringUrlCss3ValueArray failed");
            throw e;
        }
    }

    @Test
    public void testGetUrlCss3Values() {
        {
            Cursor cursor = new Cursor();
            String cssValue = "url(\"Test.png\") , url(sample) 7 8  ,    auto";
            cursor.setCssValue(cssValue);

            assertEquals(
                    "cursor: url(\"Test.png\"), url(\"sample\") 7 8, auto",
                    cursor.toString());
            assertEquals(Cursor.AUTO, cursor.getCursorType());
            List<UrlCss3Value> urlCss3ValuesFirst = cursor.getUrlCss3Values();
            assertEquals(2, urlCss3ValuesFirst.size());
            assertEquals("Test.png", urlCss3ValuesFirst.get(0).getUrl());
            assertEquals("sample", urlCss3ValuesFirst.get(1).getUrl());
            assertEquals(7, urlCss3ValuesFirst.get(1).getX());
            assertEquals(8, urlCss3ValuesFirst.get(1).getY());

        }
        {
            Cursor cursor = new Cursor();
            UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"Test.png\")");
            UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(sample) 7 8");
            cursor.setCursorUrls(Cursor.AUTO, urlCss3Value1, urlCss3Value2);

            assertEquals(
                    "cursor: url(\"Test.png\"), url(\"sample\") 7 8, auto",
                    cursor.toString());
            assertEquals(Cursor.AUTO, cursor.getCursorType());
            List<UrlCss3Value> urlCss3ValuesFirst = cursor.getUrlCss3Values();
            assertEquals(2, urlCss3ValuesFirst.size());
            assertEquals("Test.png", urlCss3ValuesFirst.get(0).getUrl());
            assertEquals("sample", urlCss3ValuesFirst.get(1).getUrl());
            assertEquals(7, urlCss3ValuesFirst.get(1).getX());
            assertEquals(8, urlCss3ValuesFirst.get(1).getY());

        }
    }

}
