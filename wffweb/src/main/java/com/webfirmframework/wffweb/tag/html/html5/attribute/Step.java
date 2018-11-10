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
import com.webfirmframework.wffweb.tag.html.identifier.InputAttributable;

/**
 * <code>step</code> attribute for the element.<br>
 *
 * <i>step</i> attribute for <i>input</i> element :- <br>
 *
 * Works with the min and max attributes to limit the increments at which a
 * numeric or date-time value can be set. It can be the string any or a positive
 * floating point number. If this attribute is not set to any, the control
 * accepts only values at multiples of the step value greater than the minimum.
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class Step extends AbstractAttribute implements InputAttributable {

    private static final long serialVersionUID = 1_0_0L;

    /**
     * The value <code>any</code> allows to accept any floating point numbers as
     * value in the relevant tag.
     */
    public static final String ANY = "any";

    {
        super.setAttributeName(AttributeNameConstants.STEP);
        init();
    }

    /**
     * initializes attribute with initial value as <code>1</code>
     *
     * @since 2.1.5
     * @author WFF
     */
    public Step() {
        super.setAttributeValue("1");
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.0.0
     * @author WFF
     */
    public Step(final String value) {
        super.setAttributeValue(value);
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.0.0
     * @author WFF
     */
    public Step(final int value) {
        super.setAttributeValue(String.valueOf(value));
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 1.0.0
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
