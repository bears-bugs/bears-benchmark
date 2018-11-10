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
package com.webfirmframework.wffweb.tag.html.html5.attribute.global;

import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * @author WFF
 *
 */
public class DataAttribute extends AbstractAttribute
        implements GlobalAttributable {

    // NB: this class should always super keyword to refer super class methods
    // otherwise DataWffId will make bug, see its implementation
    private static final long serialVersionUID = 1_0_0L;

    {
        init();
    }

    public DataAttribute(final String attributeNameExension) {
        if (attributeNameExension == null) {
            throw new NullValueException(
                    "attributeNameExension can not be null");
        }
        super.setAttributeName(
                AttributeNameConstants.DATA.concat(attributeNameExension));
        super.setAttributeValue(null);
    }

    public DataAttribute(final String attributeNameExension,
            final String value) {
        if (attributeNameExension == null) {
            throw new NullValueException(
                    "attributeNameExension can not be null");
        }
        super.setAttributeName(
                AttributeNameConstants.DATA.concat(attributeNameExension));
        super.setAttributeValue(value);
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
        return super.getAttributeValue();
    }

    /**
     * @param value
     *            the value to set
     * @author WFF
     * @since 1.0.0
     */
    public void setValue(final String value) {
        super.setAttributeValue(value);
    }

}
