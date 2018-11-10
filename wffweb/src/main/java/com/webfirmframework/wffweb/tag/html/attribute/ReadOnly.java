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
import com.webfirmframework.wffweb.tag.html.html5.identifier.AudioAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.BooleanAttribute;

/**
 * <code>readonly</code> attribute for the element.<br>
 *
 * <i>readonly</i> attribute for <i>input</i> element :- <br>
 * This Boolean attribute indicates that the user cannot modify the value of the
 * control. It is ignored if the value of the type attribute is hidden, range,
 * color, checkbox, radio, file, or a button type (such as button or submit).
 *
 * @author WFF
 * @since 1.0.0
 */
public class ReadOnly extends AbstractAttribute
        implements AudioAttributable, BooleanAttribute {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.READONLY);
        init();
    }

    /**
     * sets the default value as <code>readonly</code> (since 2.1.5). If value
     * is not required then use <code>new ReadOnly(null)</code>.
     *
     * @since 1.0.0
     * @author WFF
     */
    public ReadOnly() {
        setAttributeValue(AttributeNameConstants.READONLY);
    }

    public ReadOnly(final String value) {
        setAttributeValue(value);
    }

    public void setValue(final String value) {
        setAttributeValue(value);
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
