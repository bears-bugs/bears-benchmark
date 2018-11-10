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

import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.FormAttributable;

/**
 * <pre>
 *
 * The enctype property sets or returns the value of the enctype attribute in a form.
 *
 * The enctype attribute specifies how form-data should be encoded before sending it to the server.
 *
 * The form-data is encoded to "application/x-www-form-urlencoded" by default. This means that all characters are encoded before they are sent to the server (spaces are converted to "+" symbols, and special characters are converted to ASCII HEX values).
 * </pre>
 *
 * <code>enctype</code> attribute for <code>form</code> element .
 *
 * @author WFF
 * @since 1.1.4
 */
public class EncType extends AbstractAttribute implements FormAttributable {

    private static final long serialVersionUID = 1_0_0L;

    /**
     * All characters are encoded before sent (this is default). It represents
     * <code>application/x-www-form-urlencoded</code>.
     */
    public static final String URL_ENCODED = "application/x-www-form-urlencoded";

    /**
     * No characters are encoded. This value is required when you are using
     * forms that have a file upload control. It represents
     * <code>multipart/form-data</code>.
     */
    public static final String MULTIPART = "multipart/form-data";

    /**
     * Spaces are converted to "+" symbols, but no special characters are
     * encoded. It represents <code>text/plain</code>
     *
     */
    public static final String TEXT_PLAIN = "text/plain";

    {
        super.setAttributeName(AttributeNameConstants.ENCTYPE);
        init();
    }

    /**
     * <code>application/x-www-form-urlencoded</code> will be set as value.
     *
     * @since 1.1.4
     * @author WFF
     */
    public EncType() {
        setAttributeValue(URL_ENCODED);
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.1.4
     * @author WFF
     */
    public EncType(final String value) {
        setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 1.1.4
     * @author WFF
     */
    public void setValue(final String value) {
        super.setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param value
     *            the value for the attribute.
     * @since 2.1.15
     * @author WFF
     */
    public void setValue(final boolean updateClient, final String value) {
        super.setAttributeValue(updateClient, value);
    }

    /**
     * gets the value of this attribute
     *
     * @return the value of the attribute
     * @since 1.1.4
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 1.1.4
     */
    protected void init() {
        // to override and use this method
    }

}
