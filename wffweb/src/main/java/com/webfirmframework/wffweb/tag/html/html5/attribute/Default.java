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
import com.webfirmframework.wffweb.tag.html.html5.identifier.TrackAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.BooleanAttribute;

/**
 * {@code <element default> }
 *
 * <br>
 * <i><code>default</code></i> is a boolean attribute which can be used in
 * <i><code>track</code></i> tag to show the details to the user.
 * <i><code>default</code></i> in <i><code>track</code></i> enables the track if
 * the user's preferences do not indicate that another track would be more
 * appropriate.
 *
 * @author WFF
 * @since 2.1.9
 */
public class Default extends AbstractAttribute
        implements BooleanAttribute, TrackAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.DEFAULT);
        init();
    }

    /**
     * sets the default value as <code>default</code> . If value is not required
     * then use <code>new Default(null)</code>.
     *
     * @since 2.1.9
     * @author WFF
     */
    public Default() {
        setAttributeValue(AttributeNameConstants.DEFAULT);
    }

    public Default(final String value) {
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
