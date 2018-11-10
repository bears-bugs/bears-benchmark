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
package com.webfirmframework.wffweb.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.webfirmframework.wffweb.InvalidValueException;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class BackgroundImageTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#BackgroundImage()}.
     */
    @Test
    public void testBackgroundImage() {
        
        BackgroundImage backgroundImage = new BackgroundImage();
        assertEquals(BackgroundImage.NONE, backgroundImage.getCssValue());
        backgroundImage.setCssValue("url(/images/HelloDesign.jpg)");
        assertEquals("url(/images/HelloDesign.jpg)", backgroundImage.getCssValue());
        
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#BackgroundImage(java.lang.String)}.
     */
    @Test
    public void testBackgroundImageString() {
        BackgroundImage backgroundImage = new BackgroundImage("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        backgroundImage.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", backgroundImage.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#BackgroundImage(com.webfirmframework.wffweb.css.BackgroundImage)}.
     */
    @Test
    public void testBackgroundImageBackgroundImage() {
        BackgroundImage backgroundImage = new BackgroundImage(new BackgroundImage("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)"));
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        backgroundImage.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", backgroundImage.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#BackgroundImage(java.lang.String[])}.
     */
    @Test
    public void testBackgroundImageStringArray() {
        BackgroundImage backgroundImage = new BackgroundImage("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#BackgroundImage(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test
    public void testBackgroundImageUrlCss3ValueArray() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BackgroundImage backgroundImage = new BackgroundImage(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#BackgroundImage(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testBackgroundImageUrlCss3ValueArrayInvalidValueAssignment1() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BackgroundImage backgroundImage = new BackgroundImage(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#BackgroundImage(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testBackgroundImageUrlCss3ValueArrayInvalidValueAssignment2() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BackgroundImage backgroundImage = new BackgroundImage(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        try {
            urlCss3Value1.setY(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getY());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        assertEquals(CssNameConstants.BACKGROUND_IMAGE, new BackgroundImage().getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        BackgroundImage backgroundImage = new BackgroundImage();
        backgroundImage.setCssValue("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        backgroundImage.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", backgroundImage.getCssValue());    
        backgroundImage.setAsInherit();
        assertNull(backgroundImage.getUrlCss3Values());
        assertEquals(BackgroundImage.INHERIT, backgroundImage.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#toString()}.
     */
    @Test
    public void testToString() {
        BackgroundImage backgroundImage = new BackgroundImage("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals(backgroundImage.getCssName()+": url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.toString());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        BackgroundImage backgroundImage = new BackgroundImage();
        backgroundImage.setCssValue("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        backgroundImage.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", backgroundImage.getCssValue());    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#setImageUrls(java.lang.String[])}.
     */
    @Test
    public void testSetImageUrlsStringArray() {
        BackgroundImage backgroundImage = new BackgroundImage();
        backgroundImage.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#setImageUrls(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test
    public void testSetImageUrlsUrlCss3ValueArray() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BackgroundImage backgroundImage = new BackgroundImage(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#setImageUrls(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetImageUrlsUrlCss3ValueArrayInvalidValueAssignment1() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BackgroundImage backgroundImage = new BackgroundImage(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#setImageUrls(com.webfirmframework.wffweb.css.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetImageUrlsUrlCss3ValueArrayInvalidValueAssignment2() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        BackgroundImage backgroundImage = new BackgroundImage(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#getUrlCss3Values()}.
     */
    @Test
    public void testGetUrlCss3Values() {
        {
            BackgroundImage cursor = new BackgroundImage();
            String cssValue = "url(\"Test.png\") , url(sample)";
            cursor.setCssValue(cssValue);

            assertEquals(
                    CssNameConstants.BACKGROUND_IMAGE+": url(\"Test.png\"), url(\"sample\")",
                    cursor.toString());
            List<UrlCss3Value> urlCss3ValuesFirst = cursor.getUrlCss3Values();
            assertEquals(2, urlCss3ValuesFirst.size());
            assertEquals("Test.png", urlCss3ValuesFirst.get(0).getUrl());
            assertEquals("sample", urlCss3ValuesFirst.get(1).getUrl());
            assertEquals(-1, urlCss3ValuesFirst.get(1).getX());
            assertEquals(-1, urlCss3ValuesFirst.get(1).getY());

        }
        {
            BackgroundImage cursor = new BackgroundImage();
            UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"Test.png\")");
            UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(sample)");
            cursor.setImageUrls(urlCss3Value1, urlCss3Value2);

            assertEquals(CssNameConstants.BACKGROUND_IMAGE +
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
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        BackgroundImage backgroundImage = new BackgroundImage();
        backgroundImage.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        assertNotNull(backgroundImage.getUrlCss3Values());
        assertEquals(2, backgroundImage.getUrlCss3Values().size());
        backgroundImage.setAsInitial();
        assertNull(backgroundImage.getUrlCss3Values());
        assertEquals(BackgroundImage.INITIAL, backgroundImage.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        BackgroundImage backgroundImage = new BackgroundImage();
        backgroundImage.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", backgroundImage.getCssValue());
        assertNotNull(backgroundImage.getUrlCss3Values());
        assertEquals(2, backgroundImage.getUrlCss3Values().size());
        backgroundImage.setAsInherit();
        assertNull(backgroundImage.getUrlCss3Values());
        assertEquals(BackgroundImage.INHERIT, backgroundImage.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.BackgroundImage#stateChanged(com.webfirmframework.wffweb.data.Bean)}.
     */
//    @Test
//    public void testStateChanged() {
//        fail("Not yet implemented");
//    }

}
