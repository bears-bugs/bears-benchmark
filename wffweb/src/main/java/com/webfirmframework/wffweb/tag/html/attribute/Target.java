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
import com.webfirmframework.wffweb.tag.html.identifier.AAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.AreaAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.BaseAttributable;

/**
 * <pre>
 *
 * This attribute specifies where to display the linked resource. In HTML4, this is the name of, or a keyword for, a frame. In HTML5, it is a name of, or keyword for, a browsing context (for example, tab, window, or inline frame). The following keywords have special meanings:
 *
 *   _self: Load the response into the same HTML4 frame (or HTML5 browsing context) as the current one. This value is the default if the attribute is not specified.
 *  _blank: Load the response into a new unnamed HTML4 window or HTML5 browsing context.
 *  _parent: Load the response into the HTML4 frameset parent of the current frame or HTML5 parent browsing context of the current one. If there is no parent, this option behaves the same way as _self.
 *  _top: In HTML4: Load the response into the full, original window, canceling all other frames. In HTML5: Load the response into the top-level browsing context (that is, the browsing context that is an ancestor of the current one, and has no parent). If there is no parent, this option behaves the same way as _self.
 *
 * Use this attribute only if the href attribute is present.
 * </pre>
 *
 * <code>target</code> attribute for the element.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Target extends AbstractAttribute
        implements AAttributable, AreaAttributable, BaseAttributable {

    private static final long serialVersionUID = 1_0_0L;

    /**
     * Load the response into a new unnamed HTML4 window or HTML5 browsing
     * context.
     */
    public static final String BLANK = "_blank";

    /**
     * Load the response into the same HTML4 frame (or HTML5 browsing context)
     * as the current one. This value is the default if the attribute is not
     * specified.
     */
    public static final String SELF = "_self";

    /**
     * Load the response into the HTML4 frameset parent of the current frame or
     * HTML5 parent browsing context of the current one. If there is no parent,
     * this option behaves the same way as _self.
     */
    public static final String PARENT = "_parent";

    /**
     * In HTML4: Load the response into the full, original window, canceling all
     * other frames. In HTML5: Load the response into the top-level browsing
     * context (that is, the browsing context that is an ancestor of the current
     * one, and has no parent). If there is no parent, this option behaves the
     * same way as _self.
     */
    public static final String TOP = "_top";

    {
        super.setAttributeName(AttributeNameConstants.TARGET);
        init();
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.0.0
     * @author WFF
     */
    public Target(final String value) {
        setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final String value) {
        super.setAttributeValue(value);
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

    /**
     * gets the value of this attribute
     *
     * @return the value of the attribute
     * @since 1.0.0
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
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

}
