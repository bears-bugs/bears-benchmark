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
package com.webfirmframework.wffweb.tag.html.html5.attribute;

import java.util.regex.PatternSyntaxException;

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.InputAttributable;

/**
 *
 * <code>pattern</code> attribute for the element.<br>
 *
 * <i>pattern</i> attribute for <i>input</i> element :- <br>
 * A regular expression that the control's value is checked against. The pattern
 * must match the entire value, not just some subset. Use the title attribute to
 * describe the pattern to help the user. This attribute applies when the value
 * of the type attribute is text, search, tel, url, email or password; otherwise
 * it is ignored. The regular expression language is the same as JavaScript's.
 * The pattern is not surrounded by forward slashes.
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class Pattern extends AbstractAttribute implements InputAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.PATTERN);
        init();
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.0.0
     * @author WFF
     */
    public Pattern(final String value) {
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

    /**
     * checks the value in this attribute contains a valid regex.
     *
     * @return true if this attribute contains a valid regex
     * @since 3.0.1
     * @author WFF
     */
    public boolean containsValidRegEx() {

        try {
            java.util.regex.Pattern.compile(super.getAttributeValue());
            return true;
        } catch (final PatternSyntaxException e) {
            // NOP
        }

        return false;
    }

}
