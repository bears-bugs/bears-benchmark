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
import com.webfirmframework.wffweb.tag.html.identifier.InputAttributable;

/**
 * {@code <element checked> }
 *
 * When the value of the type attribute is radio or checkbox, the presence of
 * this Boolean attribute indicates that the control is selected by default;
 * otherwise it is ignored.
 *
 * Firefox will, unlike other browsers, by default, persist the dynamic checked
 * state of an {@code <input>} across page loads. Use the autocomplete attribute
 * to control this feature.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Checked extends AbstractAttribute
        implements InputAttributable, BooleanAttribute {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.CHECKED);
        init();
    }

    /**
     * sets the default value as <code>checked</code> (since 2.1.5). If value is
     * not required then use <code>new Selected(null)</code>. Note : There might
     * be some issue in dynamically changing state of tag if there is no default
     * value.
     *
     * @since 1.0.0
     * @author WFF
     */
    public Checked() {
        setAttributeValue(AttributeNameConstants.CHECKED);
    }

    public Checked(final String value) {
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
