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
public class IconTest {

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#Icon()}.
     */
    @Test
    public void testIcon() {
        
        Icon icon = new Icon();
        assertEquals(Icon.NONE, icon.getCssValue());
        icon.setCssValue("url(/images/HelloDesign.jpg)");
        assertEquals("url(/images/HelloDesign.jpg)", icon.getCssValue());
        
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#Icon(java.lang.String)}.
     */
    @Test
    public void testIconString() {
        Icon icon = new Icon("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        icon.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", icon.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#Icon(com.webfirmframework.wffweb.css.css3.Icon)}.
     */
    @Test
    public void testIconIcon() {
        Icon icon = new Icon(new Icon("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)"));
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        icon.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", icon.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#Icon(java.lang.String[])}.
     */
    @Test
    public void testIconStringArray() {
        Icon icon = new Icon("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#Icon(com.webfirmframework.wffweb.css.css3.UrlCss3Value[])}.
     */
    @Test
    public void testIconUrlCss3ValueArray() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        Icon icon = new Icon(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#Icon(com.webfirmframework.wffweb.css.css3.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testIconUrlCss3ValueArrayInvalidValueAssignment1() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        Icon icon = new Icon(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#Icon(com.webfirmframework.wffweb.css.css3.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testIconUrlCss3ValueArrayInvalidValueAssignment2() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        Icon icon = new Icon(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        try {
            urlCss3Value1.setY(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getY());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#getCssName()}.
     */
    @Test
    public void testGetCssName() {
        assertEquals(CssNameConstants.ICON, new Icon().getCssName());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#getCssValue()}.
     */
    @Test
    public void testGetCssValue() {
        Icon icon = new Icon();
        icon.setCssValue("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        icon.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", icon.getCssValue());    
        icon.setAsInherit();
        assertNull(icon.getUrlCss3Values());
        assertEquals(Icon.INHERIT, icon.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#toString()}.
     */
    @Test
    public void testToString() {
        Icon icon = new Icon("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals(icon.getCssName()+": url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.toString());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#setCssValue(java.lang.String)}.
     */
    @Test
    public void testSetCssValueString() {
        Icon icon = new Icon();
        icon.setCssValue("url(/images/HelloDesign.jpg), url(/images/HelloDesign2.jpg)");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        icon.setCssValue("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")");
        assertEquals("url(\"/images/HelloDesign3.jpg\"), url(\"/images/HelloDesign4.jpg\")", icon.getCssValue());    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#setImageUrls(java.lang.String[])}.
     */
    @Test
    public void testSetImageUrlsStringArray() {
        Icon icon = new Icon();
        icon.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());    
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#setImageUrls(com.webfirmframework.wffweb.css.css3.UrlCss3Value[])}.
     */
    @Test
    public void testSetImageUrlsUrlCss3ValueArray() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        Icon icon = new Icon(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#setImageUrls(com.webfirmframework.wffweb.css.css3.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetImageUrlsUrlCss3ValueArrayInvalidValueAssignment1() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        Icon icon = new Icon(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }
    
    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#setImageUrls(com.webfirmframework.wffweb.css.css3.UrlCss3Value[])}.
     */
    @Test(expected = InvalidValueException.class)
    public void testSetImageUrlsUrlCss3ValueArrayInvalidValueAssignment2() {
        final UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(\"/images/HelloDesign2.jpg\")");
        final UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"/images/HelloDesign.jpg\")");
        Icon icon = new Icon(urlCss3Value1, urlCss3Value2);
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        try {
            urlCss3Value1.setX(50);
        } catch (InvalidValueException e) {
            assertEquals(-1, urlCss3Value1.getX());
            assertEquals("url(\"/images/HelloDesign.jpg\")", urlCss3Value1.getValue());
            throw e;
        }
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#getUrlCss3Values()}.
     */
    @Test
    public void testGetUrlCss3Values() {
        {
            Icon cursor = new Icon();
            String cssValue = "url(\"Test.png\") , url(sample)";
            cursor.setCssValue(cssValue);

            assertEquals(
                    CssNameConstants.ICON+": url(\"Test.png\"), url(\"sample\")",
                    cursor.toString());
            List<UrlCss3Value> urlCss3ValuesFirst = cursor.getUrlCss3Values();
            assertEquals(2, urlCss3ValuesFirst.size());
            assertEquals("Test.png", urlCss3ValuesFirst.get(0).getUrl());
            assertEquals("sample", urlCss3ValuesFirst.get(1).getUrl());
            assertEquals(-1, urlCss3ValuesFirst.get(1).getX());
            assertEquals(-1, urlCss3ValuesFirst.get(1).getY());

        }
        {
            Icon cursor = new Icon();
            UrlCss3Value urlCss3Value1 = new UrlCss3Value("url(\"Test.png\")");
            UrlCss3Value urlCss3Value2 = new UrlCss3Value("url(sample)");
            cursor.setImageUrls(urlCss3Value1, urlCss3Value2);

            assertEquals(CssNameConstants.ICON +
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
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#setAsInitial()}.
     */
    @Test
    public void testSetAsInitial() {
        Icon icon = new Icon();
        icon.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        assertNotNull(icon.getUrlCss3Values());
        assertEquals(2, icon.getUrlCss3Values().size());
        icon.setAsInitial();
        assertNull(icon.getUrlCss3Values());
        assertEquals(Icon.INITIAL, icon.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#setAsInherit()}.
     */
    @Test
    public void testSetAsInherit() {
        Icon icon = new Icon();
        icon.setImageUrls("/images/HelloDesign.jpg", "/images/HelloDesign2.jpg");
        assertEquals("url(\"/images/HelloDesign.jpg\"), url(\"/images/HelloDesign2.jpg\")", icon.getCssValue());
        assertNotNull(icon.getUrlCss3Values());
        assertEquals(2, icon.getUrlCss3Values().size());
        icon.setAsInherit();
        assertNull(icon.getUrlCss3Values());
        assertEquals(Icon.INHERIT, icon.getCssValue());
    }

    /**
     * Test method for {@link com.webfirmframework.wffweb.css.css3.Icon#stateChanged(com.webfirmframework.wffweb.data.Bean)}.
     */
//    @Test
//    public void testStateChanged() {
//        fail("Not yet implemented");
//    }

}
