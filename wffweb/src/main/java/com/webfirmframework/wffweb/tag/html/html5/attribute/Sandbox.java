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
package com.webfirmframework.wffweb.tag.html.html5.attribute;

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.IFrameAttributable;

/**
 *
 * sandbox attribute for iframe
 *
 * @author WFF
 * @since 3.0.1
 */
public class Sandbox extends AbstractAttribute implements IFrameAttributable {

    private static final long serialVersionUID = 1_0_0L;

    /**
     * allows form submission
     */
    public static final String ALLOW_FORMS = "allow-forms";

    /**
     * allows APIs
     */
    public static final String ALLOW_POINTER_LOCK = "allow-pointer-lock";

    /**
     * allows popups
     */
    public static final String ALLOW_POPUPS = "allow-popups";

    /**
     * allows content as from the same origin
     */
    public static final String ALLOW_SAME_ORIGIN = "allow-same-origin";

    /**
     * allows scripts
     */
    public static final String ALLOW_SCRIPTS = "allow-scripts";

    /**
     * allows content to top level navigation
     */
    public static final String ALLOW_TOP_NAVIGATION = "allow-top-navigation";

    {
        super.setAttributeName(AttributeNameConstants.SANDBOX);
        init();
    }

    /**
     * creates attribute with empty value
     *
     * @since 3.0.1
     */
    public Sandbox() {
        setAttributeValue("");
    }

    public Sandbox(final String value) {
        setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 3.0.1
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
     * @since 3.0.1
     * @author WFF
     */
    public void setValue(final boolean updateClient, final String value) {
        super.setAttributeValue(updateClient, value);
    }

    /**
     * gets the value of this attribute
     *
     * @return the value of the attribute
     * @since 3.0.1
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * invokes only once per object
     *
     * @since 3.0.1
     */
    protected void init() {
        // to override and use this method
    }

}
