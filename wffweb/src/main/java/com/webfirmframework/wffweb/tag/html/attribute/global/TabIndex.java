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
package com.webfirmframework.wffweb.tag.html.attribute.global;

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * {@code <element tabindex="number"> }
 *
 * <pre>
 * The tabindex attribute specifies the tab order of an element (when the "tab" button is used for navigating).
 * </pre>
 *
 * @author WFF
 *
 */
public class TabIndex extends AbstractAttribute implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private int index;

    {
        super.setAttributeName(AttributeNameConstants.TABINDEX);
        init();
    }

    @SuppressWarnings("unused")
    private TabIndex() {
    }

    /**
     * @param index
     *            the index
     * @since 1.1.3
     * @author WFF
     */
    public TabIndex(final String index) {
        this.index = Integer.parseInt(index);
        setAttributeValue(index);
    }

    /**
     * @param index
     *            the index
     * @since 1.1.3
     * @author WFF
     */
    public TabIndex(final int index) {
        setAttributeValue(String.valueOf(index));
        this.index = index;
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
     * @return the index
     * @author WFF
     * @since 1.0.0
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index
     *            the index to set
     * @author WFF
     * @since 1.0.0
     */
    public void setIndex(final int index) {
        setAttributeValue(String.valueOf(index));
        this.index = index;
    }

}
