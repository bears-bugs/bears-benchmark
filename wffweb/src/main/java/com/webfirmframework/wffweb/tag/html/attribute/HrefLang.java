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
package com.webfirmframework.wffweb.tag.html.attribute;

import java.util.Locale;

import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.AAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.AreaAttributable;

/**
 * This attribute indicates the language of the linked resource. It is purely
 * advisory. Allowed values are determined by BCP47 for HTML5 and by RFC1766 for
 * HTML4. Use this attribute only if the href attribute is present.
 *
 * @author WFF
 * @since 1.0.0
 */
public class HrefLang extends AbstractAttribute
        implements AAttributable, AreaAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private Locale locale;

    {
        super.setAttributeName(AttributeNameConstants.HREFLANG);
        init();
    }

    /**
     * This attribute indicates the language of the linked resource. It is
     * purely advisory. Allowed values are determined by BCP47 for HTML5 and by
     * RFC1766 for HTML4. Use this attribute only if the href attribute is
     * present.
     *
     * @param languageCode
     *            the languageCode.
     * @since 1.0.0
     * @author WFF
     */
    public HrefLang(final String languageCode) {
        locale = null;
        setAttributeValue(languageCode);
    }

    /**
     * This attribute indicates the language of the linked resource. It is
     * purely advisory. Allowed values are determined by BCP47 for HTML5 and by
     * RFC1766 for HTML4. Use this attribute only if the href attribute is
     * present.
     *
     * @param language
     *            the {@code Locale} object for language.
     * @since 1.0.0
     * @author WFF
     */
    public HrefLang(final Locale language) {
        locale = language;
        setAttributeValue(language.getCountry());
    }

    /**
     * sets the value for this attribute
     *
     * @param languageCode
     *            the languageCode.
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final String languageCode) {
        locale = null;
        super.setAttributeValue(languageCode);
    }

    /**
     * sets the value for this attribute
     *
     * @param language
     *            the {@code Locale} object for language.
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final Locale language) {
        locale = language;
        super.setAttributeValue(language.getLanguage());
    }

    public Locale getLocale() {
        if (locale == null) {
            locale = new Locale(getAttributeValue());
        }
        return locale;
    }

    /**
     * gets the value of this attribute
     *
     * @return the languageCode.
     * @since 1.0.0
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 1.0.0
     */
    protected void init() {
        // to override and use this method
    }

}
