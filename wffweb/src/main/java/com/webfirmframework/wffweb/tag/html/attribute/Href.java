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
import com.webfirmframework.wffweb.tag.html.identifier.LinkAttributable;

/**
 * This was the single required attribute for anchors defining a hypertext
 * source link, but is no longer required in HTML5. Omitting this attribute
 * creates a placeholder link. The href attribute indicates the link target,
 * either a URL or a URL fragment. A URL fragment is a name preceded by a hash
 * mark (#), which specifies an internal target location (an ID) within the
 * current document. URLs are not restricted to Web (HTTP)-based documents. URLs
 * might use any protocol supported by the browser. For example, file, ftp, and
 * mailto work in most user agents.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Href extends AbstractAttribute implements AAttributable,
        AreaAttributable, BaseAttributable, LinkAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.HREF);
        init();
    }

    /**
     * This was the single required attribute for anchors defining a hypertext
     * source link, but is no longer required in HTML5. Omitting this attribute
     * creates a placeholder link. The href attribute indicates the link target,
     * either a URL or a URL fragment. A URL fragment is a name preceded by a
     * hash mark (#), which specifies an internal target location (an ID) within
     * the current document. URLs are not restricted to Web (HTTP)-based
     * documents. URLs might use any protocol supported by the browser. For
     * example, file, ftp, and mailto work in most user agents.
     *
     * @param url
     *            the url.
     * @since 1.0.0
     * @author WFF
     */
    public Href(final String url) {
        setAttributeValue(url);
    }

    /**
     * sets the value for this attribute
     *
     * @param url
     *            the url.
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final String url) {
        super.setAttributeValue(url);
    }

    /**
     * gets the value of this attribute
     *
     * @return the url.
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
