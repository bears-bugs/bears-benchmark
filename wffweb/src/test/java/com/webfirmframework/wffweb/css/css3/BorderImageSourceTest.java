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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.UrlCss3Value;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class BorderImageSourceTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#BorderImageSource()}.
     */
    @Test
    public void testBorderImageSource() {
        
        BorderImageSource borderImageSource = new BorderImageSource();
        assertEquals(BorderImageSource.NONE, borderImageSource.getCssValue());
        borderImageSource.setCssValue("url(/images/HelloDesign.jpg)");
        assertEquals("url(/images/HelloDesign.jpg)", borderImageSource.getCssValue());
        
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#BorderImageSource(java.lang.String)}.
     */
    @Test
    public void testBorderImageSourceString() {
        BorderImageSource borderImageSource = new BorderImageSource("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        borderImageSource.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", borderImageSource.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#BorderImageSource(com.webfirmframework.wffweb.css.BorderImageSource)}.
     */
    @Test
    public void testBorderImageSourceBorderImageSource() {
        BorderImageSource borderImageSource = new BorderImageSource(new BorderImageSource("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)"));
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        borderImageSource.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", borderImageSource.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#BorderImageSource(java.lang.String[])}.
     */
    @Test
    public void testBorderImageSourceStringArray() {
        BorderImageSource borderImageSource = new BorderImageSource("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#BorderImageSource(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test
    public void testBorderImageSourceUrlCss3ValueArray() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BorderImageSource borderImageSource = new BorderImageSource(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#BorderImageSource(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testBorderImageSourceUrlCss3ValueArrayInvalidValueAssignment1() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BorderImageSource borderImageSource = new BorderImageSource(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#BorderImageSource(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testBorderImageSourceUrlCss3ValueArrayInvalidValueAssignment2() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BorderImageSource borderImageSource = new BorderImageSource(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        try {
            urlCss3Value1.setY(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getY());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        assertEquals(CssNameConstants.BORDER_IMAGE_SOURCE, new BorderImageSource().getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        BorderImageSource borderImageSource = new BorderImageSource();
        borderImageSource.setCssValue("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        borderImageSource.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", borderImageSource.getCssValue());    
        borderImageSource.setAsInherit();
        assertNull(borderImageSource.getUrlCss3Values());
        assertEquals(BorderImageSource.INHERIT, borderImageSource.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#toHtmlString()}.
     */
    @Test
    public void testToString() {
        BorderImageSource borderImageSource = new BorderImageSource("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals(borderImageSource.getCssName()+": url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.toString());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        BorderImageSource borderImageSource = new BorderImageSource();
        borderImageSource.setCssValue("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        borderImageSource.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", borderImageSource.getCssValue());    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#setImageUrls(java.lang.String[])}.
     */
    @Test
    public void testSetImageUrlsStringArray() {
        BorderImageSource borderImageSource = new BorderImageSource();
        borderImageSource.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#setImageUrls(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test
    public void testSetImageUrlsUrlCss3ValueArray() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BorderImageSource borderImageSource = new BorderImageSource(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#setImageUrls(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetImageUrlsUrlCss3ValueArrayInvalidValueAssignment1() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BorderImageSource borderImageSource = new BorderImageSource(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#setImageUrls(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetImageUrlsUrlCss3ValueArrayInvalidValueAssignment2() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BorderImageSource borderImageSource = new BorderImageSource(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#getUrlCss3Values()}.
     */
    @Test
    public void testGetUrlCss3Values() {
        {
            BorderImageSource cursor = new BorderImageSource();
            String cssValue = "url(\"Test.png\") , url(sample)";
            cursor.setCssValue(cssValue);

            assertEquals(
                    CssNameConstants.BORDER_IMAGE_SOURCE+": url(\"Test.png\"), url(\"sample\")",
                    cursor.toString());
            List<UrlCss3Value> urlCss3ValuesFirst = cursor.getUrlCss3Values();
            assertEquals(2, urlCss3ValuesFirst.size());
            assertEquals("Test.png", urlCss3ValuesFirst.get(0).getUrl());
            assertEquals("sample", urlCss3ValuesFirst.get(1).getUrl());
            assertEquals(-1, urlCss3ValuesFirst.get(1).getX());
            assertEquals(-1, urlCss3ValuesFirst.get(1).getY());

        }
        {
            BorderImageSource cursor = new BorderImageSource();
            UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"Test.png\")");
            UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(sample)");
            cursor.setImageUrls(urlCss3Value1, urlCss3Value2);

            assertEquals(CssNameConstants.BORDER_IMAGE_SOURCE +
                    ": url(\"Test.png\"), url(\"sample\")",
                    cursor.toString());
            List<UrlCss3Value> urlCss3ValuesFirst = cursor.getUrlCss3Values();
            assertEquals(2, urlCss3ValuesFirst.size());
            assertEquals("Test.png", urlCss3ValuesFirst.get(0).getUrl());
            assertEquals("sample", urlCss3ValuesFirst.get(1).getUrl());
            assertEquals(-1, urlCss3ValuesFirst.get(1).getX());
            assertEquals(-1, urlCss3ValuesFirst.get(1).getY());

        }    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        BorderImageSource borderImageSource = new BorderImageSource();
        borderImageSource.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        assertNotNull(borderImageSource.getUrlCss3Values());
        assertEquals(2, borderImageSource.getUrlCss3Values().size());
        borderImageSource.setAsInitial();
        assertNull(borderImageSource.getUrlCss3Values());
        assertEquals(BorderImageSource.INITIAL, borderImageSource.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        BorderImageSource borderImageSource = new BorderImageSource();
        borderImageSource.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", borderImageSource.getCssValue());
        assertNotNull(borderImageSource.getUrlCss3Values());
        assertEquals(2, borderImageSource.getUrlCss3Values().size());
        borderImageSource.setAsInherit();
        assertNull(borderImageSource.getUrlCss3Values());
        assertEquals(BorderImageSource.INHERIT, borderImageSource.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BorderImageSource#stateChanged(com.webfirmframework.wffweb.data.Bean)}.
     */
//    @Test
//    public void testStateChanged() {
//        fail("Not yet implemented");
//    }

}
