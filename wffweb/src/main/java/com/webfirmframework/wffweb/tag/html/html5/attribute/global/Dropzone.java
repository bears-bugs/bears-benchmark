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

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * {@code <element dropzone="copy|move|link">}
 *
 * <pre>
 * The dropzone attribute specifies whether the dragged data is copied, moved,
 * or linked, when it is dropped on an element.
 * </pre>
 *
 *
 * @author WFF
 *
 */
public class Dropzone extends AbstractAttribute implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;

    /**
     * Dropping the data will result in a copy of the dragged data
     */
    public static final String COPY = "copy";
    /**
     * Dropping the data will result in that the dragged data is moved to the
     * new location
     */
    public static final String MOVE = "move";

    /**
     * Dropping the data will result in a link to the original data
     */
    public static final String LINK = "link";

    {
        super.setAttributeName(AttributeNameConstants.DROPZONE);
        init();
    }

    /**
     * the default value <code>copy</code> will be set.
     *
     * @author WFF
     * @since 1.0.0
     */
    public Dropzone() {
        setAttributeValue(COPY);
    }

    public Dropzone(final String value) {
        setAttributeValue(value);
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
     * @return the value
     * @author WFF
     * @since 1.0.0
     */
    public String getValue() {
        return getAttributeValue();
    }

    /**
     * @param value
     *            the value to set
     * @author WFF
     * @since 1.0.0
     */
    public void setValue(final String value) {
        setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param value
     *            the value for the attribute.
     * @since 2.1.15
     * @author WFF
     */
    public void setValue(final boolean updateClient, final String value) {
        super.setAttributeValue(updateClient, value);
    }

}
