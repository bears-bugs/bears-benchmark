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
import com.webfirmframework.wffweb.tag.html.identifier.BooleanAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.OlAttributable;

/**
 * {@code <element open> }
 *
 * <br>
 * <i><code>Reversed</code></i> is a boolean attribute which can be used in
 * <i><code>Ol</code></i> tag. The presence of <i><code>Reversed</code></i>
 * attribute in <i><code>Ol</code></i> tag shows the items in reverse order.
 *
 * @author WFF
 * @since 2.1.9
 */
public class Reversed extends AbstractAttribute
        implements BooleanAttribute, OlAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.REVERSED);
        init();
    }

    /**
     * sets the default value as <code>reversed</code> . If value is not
     * required then use <code>new Reversed(null)</code>.
     *
     * @since 2.1.9
     * @author WFF
     */
    public Reversed() {
        setAttributeValue(AttributeNameConstants.REVERSED);
    }

    public Reversed(final String value) {
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
