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
package com.webfirmframework.wffweb.tag.html.html5.attribute.global;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * {@code <element contenteditable="true|false">}
 *
 * <pre>
 *
 * The contenteditable attribute specifies whether the content of an element is editable or not.
 *
 * </pre>
 *
 * @author WFF
 *
 */
public class ContentEditable extends AbstractAttribute
        implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;
    private boolean editable;

    {
        setAttributeName(AttributeNameConstants.CONTENTEDITABLE);
        init();
    }

    /**
     * the default value <code>false</code> will be set.
     *
     * @author WFF
     * @since 1.0.0
     */
    public ContentEditable() {
        super.setAttributeValue("false");
    }

    /**
     *
     *
     * @param value
     *            the value should be either true or false
     * @author WFF
     * @since 1.1.4
     */
    public ContentEditable(final String value) {
        if ("true".equals(value) || "false".equals(value)) {
            editable = Boolean.parseBoolean(value);
        } else {
            throw new InvalidValueException(
                    "the value should be either true or false");
        }
        super.setAttributeValue(value);
    }

    /**
     * @param editable
     * @author WFF
     * @since 1.0.0
     */
    public ContentEditable(final boolean editable) {
        this.editable = editable;
        super.setAttributeValue(String.valueOf(editable));
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

    /**
     * @return the editable
     * @author WFF
     * @since 1.0.0
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable
     *            the editable to set
     * @author WFF
     * @since 1.0.0
     */
    public void setEditable(final boolean editable) {
        this.editable = editable;
        super.setAttributeValue(String.valueOf(editable));
    }
}
