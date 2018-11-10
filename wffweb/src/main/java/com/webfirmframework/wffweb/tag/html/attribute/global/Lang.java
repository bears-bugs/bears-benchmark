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
package com.webfirmframework.wffweb.tag.html.attribute.global;

import java.util.Locale;

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * The lang attribute specifies the language of the element's content.<br>
 * <b>Sample html :-</b><br>
 * {@code
 *
<p>
This is a paragraph.
</p>
 * }<br>
 * {@code
 *
<p lang="fr">
Ceci est un paragraphe.
</p>
 * }
 *
 * @author WFF
 *
 */
public class Lang extends AbstractAttribute implements GlobalAttributable {

    /**
     *
     */
    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.LANG);
        init();
    }

    public Lang() {
        super.setAttributeValue("");
    }

    /**
     * @param lang
     *            eg: fr
     * @author WFF
     */
    public Lang(final String lang) {
        super.setAttributeValue(lang);
    }

    /**
     * @param locale
     *            eg: Locale.JAPAN
     * @author WFF
     */
    public Lang(final Locale locale) {
        super.setAttributeValue(locale.getLanguage());
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

    /**
     * @param lang
     *            eg: fr
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final String lang) {
        super.setAttributeValue(lang);
    }

    /**
     * @return the language name, eg: fr
     * @since 1.0.0
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * @return the corresponding locale for the language
     * @since 1.0.0
     * @author WFF
     */
    public Locale getLocale() {
        return new Locale(super.getAttributeValue());
    }

}
