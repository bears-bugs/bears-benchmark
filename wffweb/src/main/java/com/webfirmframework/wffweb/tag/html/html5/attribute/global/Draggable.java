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
 * {@code <element draggable="true|false|auto">}
 *
 * <pre>
 *
 * The draggable attribute specifies whether an element is draggable or not.
 *
 * </pre>
 *
 * @author WFF
 *
 */
public class Draggable extends AbstractAttribute implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;

    public static final String AUTO = "auto";

    private Boolean draggable;

    {
        setAttributeName(AttributeNameConstants.DRAGGABLE);
        init();
    }

    /**
     * the default value <code>auto</code> will be set.
     *
     * @author WFF
     * @since 1.0.0
     */
    public Draggable() {
        setAttributeValue(AUTO);
    }

    /**
     *
     *
     * @param value
     *            the value should be either true or false
     * @author WFF
     * @since 1.1.4
     */
    public Draggable(final String value) {
        if ("true".equals(value) || "false".equals(value)) {
            draggable = Boolean.parseBoolean(value);
        } else {
            throw new InvalidValueException(
                    "the value should be either true or false");
        }
        setAttributeValue(value);
    }

    /**
     * @param draggable
     *            the draggable to set, true/false/null. The null will set
     *            <code>auto</code> for draggable value.
     * @author WFF
     * @since 1.0.0
     */
    public Draggable(final Boolean draggable) {
        this.draggable = draggable;
        if (draggable == null) {
            setAttributeValue(AUTO);
        } else {
            setAttributeValue(String.valueOf(draggable));
        }
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
     * @return the draggable true/false/null. <code>null</code> means the value
     *         is <code>auto</code>.
     * @author WFF
     * @since 1.0.0
     */
    public Boolean isDraggable() {
        return draggable;
    }

    /**
     * @param draggable
     *            the draggable to set, true/false/null. The null will set
     *            <code>auto</code> for draggable value.
     * @author WFF
     * @since 1.0.0
     */
    public void setDraggable(final Boolean draggable) {
        this.draggable = draggable;
        if (draggable == null) {
            setAttributeValue(AUTO);
        } else {
            setAttributeValue(String.valueOf(draggable));
        }
    }
}
