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

import java.util.Arrays;
import java.util.Collection;

import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractValueSetAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.TdAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.ThAttributable;

/**
 * @author WFF
 *
 */
public class Headers extends AbstractValueSetAttribute
        implements ThAttributable, TdAttributable {

    /**
     *
     */
    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.HEADERS);
        init();
    }

    public Headers() {
    }

    /**
     * one or more header id separated by space or as an array of header ids.
     *
     * @param headerIds
     */
    public Headers(final String... headerIds) {
        if (headerIds != null) {
            super.addAllToAttributeValueSet(headerIds);
        }
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 1.1.3
     */
    protected void init() {
        // to override and use this method
    }

    /**
     * adds the given header ids.
     *
     * @param headerIds
     *            one or more header ids separated by space or as an array of
     *            header ids.
     * @since 1.1.3
     * @author WFF
     */
    public void addHeaderIds(final String... headerIds) {
        if (headerIds != null) {
            super.addAllToAttributeValueSet(headerIds);
        }
    }

    /**
     * removed all current header ids and adds the given header ids.
     *
     * @param headerIds
     *            one or more header ids separated by space or an array of
     *            header ids.
     * @since 1.1.3
     * @author WFF
     */
    public void addNewHeaderIds(final String... headerIds) {
        if (headerIds != null) {
            super.replaceAllInAttributeValueSet(headerIds);
        }
    }

    /**
     * adds the given header ids in the class attribute
     *
     * @param headerIds
     * @since 1.1.3
     * @author WFF
     */
    public void addAllHeaderIds(final Collection<String> headerIds) {
        super.addAllToAttributeValueSet(headerIds);
    }

    /**
     * removes all header ids from the class attribute
     *
     * @param headerIds
     *            the header ids to remove
     * @since 1.1.3
     * @author WFF
     */
    public void removeAllHeaderIds(final Collection<String> headerIds) {
        super.removeAllFromAttributeValueSet(headerIds);
    }

    /**
     * removes the given header id
     *
     * @param headerId
     *            the header id to remove
     * @since 1.1.3
     * @author WFF
     * @deprecated this method will be removed in the future release, use
     *             removeHeaderId instead of it.
     */
    @Deprecated
    public void removeClassName(final String headerId) {
        super.removeFromAttributeValueSet(headerId);
    }

    /**
     * removes the given header id
     *
     * @param headerId
     *            the header id to remove
     * @since 3.0.1
     * @author WFF
     */
    public void removeHeaderId(final String headerId) {
        super.removeFromAttributeValueSet(headerId);
    }

    /**
     * removes the given header ids
     *
     * @param headerIds
     *            the header ids to remove
     * @since 3.0.1
     * @author WFF
     */
    public void removeHeaderIds(final Collection<String> headerIds) {
        super.removeAllFromAttributeValueSet(headerIds);
    }

    /**
     * removes the given header ids
     *
     * @param headerIds
     *            the header ids to remove
     * @since 3.0.1
     * @author WFF
     */
    public void removeHeaderIds(final String... headerIds) {
        super.removeAllFromAttributeValueSet(Arrays.asList(headerIds));
    }

    /**
     * Removes all header ids
     *
     * @since 3.0.1
     */
    public void removeAllHeaderIds() {
        super.removeAllFromAttributeValueSet();
    }
}
