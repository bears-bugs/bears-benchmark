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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractValueSetAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.AAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.AreaAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.LinkAttributable;

/**
 * For anchors containing the href attribute, this attribute specifies the
 * relationship of the target object to the link object. The value is a
 * space-separated list of link types values. The values and their semantics
 * will be registered by some authority that might have meaning to the document
 * author. The default relationship, if no other is given, is void. Use this
 * attribute only if the href attribute is present. <code>rel</code> attribute
 * for the element.
 *
 * @author WFF
 * @since 1.0.0
 *
 */
public class Rel extends AbstractValueSetAttribute
        implements AAttributable, AreaAttributable, LinkAttributable {

    private static final long serialVersionUID = 1_0_0L;

    /**
     * Links to an alternate version of the document (i.e. print page,
     * translated or mirror)
     */
    public static final String ALTERNATE = "alternative";

    /**
     * Links to the author of the document
     */
    public static final String AUTHOR = "author";

    /**
     * Permanent URL used for bookmarking
     */
    public static final String BOOKMARK = "bookmark";

    /**
     * Links to a help document
     */
    public static final String HELP = "help";

    /**
     * Links to copyright information for the document
     */
    public static final String LICENSE = "license";

    /**
     * The next document in a selection
     */
    public static final String NEXT = "next";

    /**
     * Links to an unendorsed document, like a paid link. ("nofollow" is used by
     * Google, to specify that the Google search spider should not follow that
     * link)
     */
    public static final String NOFOLLOW = "nofollow";

    /**
     * Specifies that the browser should not send a HTTP referer header if the
     * user follows the hyperlink
     */
    public static final String NOREFERRER = "noreferrer";

    /**
     * Specifies that the browsing context created by its related url must not
     * have an opener browsing context.
     */
    public static final String NOOPENER = "noopener";

    /**
     * Specifies that the target document should be cached
     */
    public static final String PREFETCH = "prefetch";

    /**
     * The previous document in a selection
     */
    public static final String PREV = "prev";

    /**
     * Links to a search tool for the document
     */
    public static final String SEARCH = "search";

    /**
     * A tag (keyword) for the current document
     */
    public static final String TAG = "tag";

    /**
     * Specifies that the related tag is for style sheet.
     */
    public static final String STYLESHEET = "stylesheet";

    /**
     * preloads the resource from <i>href</i> attribute as the attribute value
     * given in <i>as</i> attribute
     */
    public static final String PRELOAD = "preload";

    /**
     * preemptively connects to the target resource's origin.
     */
    public static final String PRECONNECT = "preconnect";

    /**
     * gives pingback server address which handles the pingback to the current
     * document.
     */
    public static final String PINGBACK = "pingback";

    /**
     * icon url to be used in <i>href</i> attribute
     */
    public static final String ICON = "icon";

    /**
     * Represents the browser should preemptively perform DNS resolution for the
     * target resource's origin.
     */
    public static final String DNS_PREFETCH = "dns-prefetch";

    /**
     * Represents that the referenced document is from an external site
     */
    public static final String EXTERNAL = "external";

    {
        // This class may to be re-implemented just like ClassAttribute because
        // this class is also taking multiple values separated by space just as
        // in ClassAttribute so many features can be reused from ClassAttribute.
        super.setAttributeName(AttributeNameConstants.REL);
        init();
    }

    /**
     *
     * @param value
     *            the value for the attribute. If there are multiple values it
     *            can be separated by space.
     * @since 1.0.0
     * @author WFF
     */
    public Rel(final String value) {
        super.addAllToAttributeValueSet(value);
    }

    /**
     *
     * @param values
     *            the values for the attribute. If there are multiple values it
     *            can be separated by space.
     * @since 1.0.0
     * @author WFF
     */
    public Rel(final String... values) {
        super.addAllToAttributeValueSet(values);
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
     * @return a new copy of set of values
     * @since 2.1.15
     * @author WFF
     */
    public Set<String> getValueSet() {
        return new LinkedHashSet<>(super.getAttributeValueSet());
    }

    /**
     * removes the value
     *
     * @param value
     * @since 2.1.15
     * @author WFF
     */
    public void removeValue(final String value) {
        super.removeFromAttributeValueSet(value);
    }

    /**
     * removes the values
     *
     * @param values
     * @since 2.1.15
     * @author WFF
     */
    public void removeValues(final Collection<String> values) {
        super.removeAllFromAttributeValueSet(values);
    }

    /**
     * adds the values to the last
     *
     * @param values
     * @since 2.1.15
     * @author WFF
     */
    public void addValues(final Collection<String> values) {
        super.addAllToAttributeValueSet(values);
    }

    /**
     * adds the value to the last
     *
     * @param value
     * @since 2.1.15
     * @author WFF
     */
    public void addValue(final String value) {
        super.addToAttributeValueSet(value);
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

    public void setValue(final boolean updateClient, final String value) {
        super.setAttributeValue(updateClient, value);
    }

}
