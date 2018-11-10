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
 * This Boolean attribute indicates that the form control is not available for
 * interaction. In particular, the click event will not be dispatched on
 * disabled controls. Also, a disabled control's value isn't submitted with the
 * form.
 *
 * Firefox will, unlike other browsers, by default, persist the dynamic disabled
 * state of an {@code <input>} across page loads. Use the autocomplete attribute
 * to control this feature.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Disabled extends AbstractAttribute
        implements InputAttributable, BooleanAttribute {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.DISABLED);
        init();
    }

    /**
     * sets the default value as <code>disabled</code> (since 2.1.5). If value
     * is not required then use <code>new Disabled(null)</code>.
     *
     * @since 1.0.0
     * @author WFF
     */
    public Disabled() {
        setAttributeValue(AttributeNameConstants.DISABLED);
    }

    public Disabled(final String value) {
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
