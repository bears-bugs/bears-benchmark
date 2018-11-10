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
import com.webfirmframework.wffweb.tag.html.html5.identifier.DetailsAttributable;
import com.webfirmframework.wffweb.tag.html.html5.identifier.DialogAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.BooleanAttribute;

/**
 * {@code <element open> }
 *
 * <br>
 * <i><code>open</code></i> is a boolean attribute which can be used in
 * <i><code>details</code></i> tag to show the details to the user. The presence
 * of <i><code>open</code></i> attribute in details indicates that the details
 * to be visible in the browser. This attribute can also be used on other tags
 * like <i><code>dialog</code></i>.
 *
 * @author WFF
 * @since 2.1.9
 */
public class Open extends AbstractAttribute
        implements BooleanAttribute, DetailsAttributable, DialogAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.OPEN);
        init();
    }

    /**
     * sets the default value as <code>open</code> . If value is not required
     * then use <code>new Open(null)</code>.
     *
     * @since 2.1.9
     * @author WFF
     */
    public Open() {
        setAttributeValue(AttributeNameConstants.OPEN);
    }

    public Open(final String value) {
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
     * @since 2.1.9
     */
    protected void init() {
        // to override and use this method
    }

}
