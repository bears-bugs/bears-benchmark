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
 * {@code <element contextmenu="menu_id">}
 *
 * <pre>
 *
 * The contextmenu attribute specifies a context menu for an element. The context menu appears when a user right-clicks on the element.
 *
 * The value of the contextmenu attribute is the id of the &lt;menu&gt; element to open.
 *
 * </pre>
 *
 * @author WFF
 *
 */
public class ContextMenu extends AbstractAttribute
        implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        setAttributeName(AttributeNameConstants.CONTEXTMENU);
        init();
    }

    @SuppressWarnings("unused")
    private ContextMenu() {
    }

    /**
     * @param menuId
     * @author WFF
     * @since 1.0.0
     */
    public ContextMenu(final String menuId) {
        setAttributeValue(menuId);
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
     * @return the menuId
     * @author WFF
     * @since 1.0.0
     */
    public String getMenuId() {
        return getAttributeValue();
    }

    /**
     * @param menuId
     *            the menuId to set
     * @author WFF
     * @since 1.0.0
     */
    public void setMenuId(final String menuId) {
        setAttributeValue(menuId);
    }

}
