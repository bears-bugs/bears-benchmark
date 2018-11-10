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
import com.webfirmframework.wffweb.tag.html.identifier.TdAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.ThAttributable;

/**
 * <pre>
 * &lt;td colspan=&quot;number&quot;&gt;
 * The colspan attribute defines the number of columns a cell should span.
 *
 * </pre>
 *
 * @author WFF
 *
 */
public class ColSpan extends AbstractAttribute
        implements ThAttributable, TdAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private int value;

    {
        super.setAttributeName(AttributeNameConstants.COLSPAN);
        init();
    }

    @SuppressWarnings("unused")
    private ColSpan() {
    }

    /**
     * @param value
     *            the the number of columns to span
     * @since 1.1.3
     * @author WFF
     */
    public ColSpan(final String value) {
        this.value = Integer.parseInt(value);
        setAttributeValue(value);
    }

    /**
     * @param value
     *            the the number of columns to span
     * @since 1.1.3
     * @author WFF
     */
    public ColSpan(final int value) {
        setAttributeValue(String.valueOf(value));
        this.value = value;
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 1.1.3
     */
    protected void init() {
        // NOP
        // to override and use this method
    }

    /**
     * @return the the number of columns spanned
     * @author WFF
     * @since 1.1.3
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value
     *            the the number of columns to span
     * @author WFF
     * @since 1.1.3
     */
    public void setValue(final int value) {
        setAttributeValue(String.valueOf(value));
        this.value = value;
    }

}
