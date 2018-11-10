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

/**
 * This attribute specifies a reverse link, the inverse relationship of the rel
 * attribute. It is useful for indicating where an object came from, such as the
 * author of a document.
 *
 * <code>rev</code> attribute for the element. NB: This attribute is not
 * supported by html5.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Rev extends AbstractAttribute implements AAttributable {

    private static final long serialVersionUID = 1_0_0L;

    /**
     * An alternate version of the document (i.e. print page, translated or
     * mirror)
     */
    public static final String ALTERNATE = "alternate";

    /**
     * An external style sheet for the document
     */
    public static final String STYLESHEET = "stylesheet";

    /**
     * The first document in a selection
     */
    public static final String START = "start";

    /**
     * The next document in a selection
     */
    public static final String NEXT = "next";

    /**
     * The previous document in a selection
     */
    public static final String PREV = "prev";

    /**
     * A table of contents for the document
     */
    public static final String CONTENTS = "contents";

    /**
     * An index for the document
     */
    public static final String INDEX = "index";

    /**
     * A glossary (explanation) of words used in the document
     */
    public static final String GLOSSARY = "glossary";

    /**
     * A document containing copyright information
     */
    public static final String COPYRIGHT = "copyright";

    /**
     * A chapter of the document
     */
    public static final String CHAPTER = "chapter";

    /**
     * A section of the document
     */
    public static final String SECTION = "section";

    /**
     * A subsection of the document
     */
    public static final String SUBSECTION = "subsection";

    /**
     * An appendix for the document
     */
    public static final String APPENDIX = "appendix";

    /**
     * A help document
     */
    public static final String HELP = "help";

    /**
     * A related document
     */
    public static final String BOOKMARK = "bookmark";

    /**
     * "nofollow" is used by Google, to specify that the Google search spider
     * should not follow that link (mostly used for paid links)
     */
    public static final String NOFOLLOW = "nofollow";

    public static final String LICENCE = "licence";

    public static final String TAG = "tag";

    public static final String FRIEND = "friend";

    {
        super.setAttributeName(AttributeNameConstants.REV);
        init();
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.0.0
     * @author WFF
     */
    public Rev(final String value) {
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
