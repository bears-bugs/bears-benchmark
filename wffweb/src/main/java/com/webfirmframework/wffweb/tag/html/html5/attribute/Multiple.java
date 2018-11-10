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
import com.webfirmframework.wffweb.tag.html.identifier.BooleanAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.InputAttributable;

/**
 * <code>multiple</code> attribute for the element.<br>
 *
 * <i>multiple</i> attribute for <i>input</i> element :- <br>
 *
 * This Boolean attribute indicates whether the user can enter more than one
 * value. This attribute applies when the type attribute is set to email or
 * file; otherwise it is ignored.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Multiple extends AbstractAttribute
        implements BooleanAttribute, InputAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.MULTIPLE);
        init();
    }

    /**
     * sets the default value as <code>multiple</code> (since 3.0.1). If value
     * is not required then use <code>new Multiple(null)</code>. Note : There
     * might be some issue in dynamically changing state of tag if there is no
     * default value.
     */
    public Multiple() {
        setAttributeValue(AttributeNameConstants.MULTIPLE);
    }

    public Multiple(final String value) {
        setAttributeValue(value);
    }

    public void setValue(final String value) {
        setAttributeValue(value);
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

    public String getValue() {
        return getAttributeValue();
    }

    /**
     * invokes only once per object
     *
     * @since 1.0.0
     */
    protected void init() {
        // to override and use this method
    }

}
