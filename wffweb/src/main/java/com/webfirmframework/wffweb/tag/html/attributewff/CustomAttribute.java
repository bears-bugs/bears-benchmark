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
package com.webfirmframework.wffweb.tag.html.attributewff;

import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * This is a custom attribute to create an attribute with any name and value.
 *
 * @author WFF
 *
 */
public class CustomAttribute extends AbstractAttribute
        implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        init();
    }

    public CustomAttribute(final String attributeName) {
        if (attributeName == null) {
            throw new NullValueException("attributeName can not be null");
        }
        super.setAttributeName(attributeName);
        setAttributeValue(null);
    }

    public CustomAttribute(final String attributeName, final String value) {
        if (attributeName == null) {
            throw new NullValueException("attributeName can not be null");
        }
        super.setAttributeName(attributeName);
        setAttributeValue(value);
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
     * @return the value
     * @author WFF
     * @since 1.0.0
     */
    public String getValue() {
        return getAttributeValue();
    }

    /**
     * @param value
     *            the value to set
     * @author WFF
     * @since 1.0.0
     */
    public void setValue(final String value) {
        setAttributeValue(value);
    }

    /**
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param value
     *            the value to set
     * @author WFF
     * @since 2.1.15
     */
    public void setValue(final boolean updateClient, final String value) {
        setAttributeValue(updateClient, value);
    }

}
