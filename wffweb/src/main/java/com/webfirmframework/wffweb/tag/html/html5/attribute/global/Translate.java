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
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * {@code <element translate="yes|no">  }
 *
 * <pre>
 * The translate attribute specifies whether the content of an element should be translated or not.
 * </pre>
 *
 * @author WFF
 *
 */
public class Translate extends AbstractAttribute implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private boolean translation;

    {
        super.setAttributeName(AttributeNameConstants.TRANSLATE);
        init();
    }

    /**
     * <code>false</code> will be set as the value.
     *
     * @author WFF
     * @since 1.0.0
     */
    public Translate() {
        super.setAttributeValue(translation ? "yes" : "no");
    }

    /**
     * @param translation
     *            the translation to set. The argument {@code true } or
     *            {@code false } will set {@code yes} or {@code no}
     *            respectively.
     * @author WFF
     * @since 1.0.0
     */
    public Translate(final boolean translation) {
        super.setAttributeValue(translation ? "yes" : "no");
        this.translation = translation;
    }

    /**
     * @param value
     *            the translation to set. The argument should be {@code yes } or
     *            {@code no }.
     * @author WFF
     * @since 1.1.4
     */
    public Translate(final String value) {
        final String trimmedValue = StringUtil.strip(value);
        if ("yes".equals(trimmedValue)) {
            translation = true;
            super.setAttributeValue("yes");
        } else if ("no".equals(trimmedValue)) {
            translation = false;
            super.setAttributeValue("no");
        } else {
            throw new InvalidValueException("the value must be yes or no");
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
     * @return the translation
     * @author WFF
     * @since 1.0.0
     */
    public boolean isTranslation() {
        return translation;
    }

    /**
     * @param translation
     *            the translation to set. The argument {@code true } or
     *            {@code false } will set {@code yes} or {@code no}
     *            respectively.
     * @author WFF
     * @since 1.0.0
     */
    public void setTranslation(final boolean translation) {
        super.setAttributeValue(translation ? "yes" : "no");
        this.translation = translation;
    }

}
