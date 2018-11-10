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
import com.webfirmframework.wffweb.tag.html.identifier.BooleanAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.ScriptAttributable;

/**
 *
 * <code>name</code> attribute for the element. This attribute is supported by
 * multiple tags.
 *
 * @since 2.1.5
 * @author WFF
 *
 */
public class Async extends AbstractAttribute
        implements ScriptAttributable, BooleanAttribute {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.ASYNC);
        init();
    }

    /**
     * It's a boolean attribute. It sets the default value as <code>async</code>
     * . If value is not required then use <code>new Async(null)</code>. Note :
     * There might be some issue in dynamically changing state of tag if there
     * is no default value.
     *
     * @since 1.1.5
     * @author WFF
     */
    public Async() {
        setAttributeValue(AttributeNameConstants.ASYNC);
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 2.1.5
     * @author WFF
     */
    public Async(final String value) {
        setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 2.1.5
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
     * @since 2.1.5
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 2.1.5
     */
    protected void init() {
        // to override and use this method
    }

}
