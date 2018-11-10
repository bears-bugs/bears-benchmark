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
import com.webfirmframework.wffweb.tag.html.identifier.TextAreaAttributable;

/**
 * <pre>
 * &lt;textarea cols=&quot;number&quot;&gt;
 * Specifies the width of the text area (in average character width). Default value is 20.
 *
 * </pre>
 *
 * @author WFF
 * @since 2.1.4
 *
 */
public class Cols extends AbstractAttribute implements TextAreaAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private int value = 20;

    {
        super.setAttributeName(AttributeNameConstants.COLS);
        init();
    }

    /**
     * will be initialized with default value as 20.
     *
     * @since 2.1.4
     * @author WFF
     */
    public Cols() {
        setAttributeValue(String.valueOf(value));
    }

    /**
     * @param value
     *            the the number of columns to span
     * @since 2.1.4
     * @author WFF
     */
    public Cols(final String value) {
        this.value = Integer.parseInt(value);
        setAttributeValue(value);
    }

    /**
     * @param value
     *            the the number of columns to span
     * @since 2.1.4
     * @author WFF
     */
    public Cols(final int value) {
        setAttributeValue(String.valueOf(value));
        this.value = value;
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 2.1.4
     */
    protected void init() {
        // NOP
        // to override and use this method
    }

    /**
     * @return the the number of columns spanned
     * @author WFF
     * @since 2.1.4
     */
    public int getValue() {
        return value;
    }

    /**
     * @param index
     *            the the number of columns to span
     * @author WFF
     * @since 2.1.4
     */
    public void setValue(final int index) {
        setAttributeValue(String.valueOf(index));
        value = index;
    }

}
