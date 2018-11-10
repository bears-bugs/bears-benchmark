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
import com.webfirmframework.wffweb.tag.html.identifier.OptionAttributable;

/**
 * The selected attribute is a boolean attribute.
 *
 * When present, it specifies that an option should be pre-selected when the
 * page loads.
 *
 * The pre-selected option will be displayed first in the drop-down list.
 *
 *
 * @author WFF
 * @since 2.1.4
 */
public class Selected extends AbstractAttribute
        implements OptionAttributable, BooleanAttribute {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.SELECTED);
        init();
    }

    /**
     * sets the default value as <code>selected</code> (since 2.1.5). If value
     * is not required then use <code>new Selected(null)</code>. Note : There
     * might be some issue in dynamically changing state of tag if there is no
     * default value.
     *
     * @since 2.1.4
     * @author WFF
     */
    public Selected() {
        setAttributeValue(AttributeNameConstants.SELECTED);
    }

    public Selected(final String value) {
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
     * @since 2.1.4
     */
    protected void init() {
        // to override and use this method
    }

}
