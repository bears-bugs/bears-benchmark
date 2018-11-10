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
package com.webfirmframework.wffweb.css;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.StringBuilderUtil;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * <pre>
 * unicode-range: <i>unicode-range</i>;
 * </pre>
 *
 * Optional. Defines the range of unicode characters the font supports. Default
 * value is "U+0-10FFFF"
 *
 * @author WFF
 * @version 1.1.2
 * @since 1.1.2
 */
public class UnicodeRange extends AbstractCssProperty<UnicodeRange> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(UnicodeRange.class.getName());

    public static final String DEFAULT_VALUE = "U+0-10FFFF";

    public static final String[] EMPTY_ARRAY = new String[0];

    private String cssValue;

    private String[] unicodeChars;

    public UnicodeRange() {
        setCssValue("");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public UnicodeRange(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * <pre>
     * Eg:-
     * String[] unicodeChars = {"U+0400-045F", "U+0490-0491", "U+04B0-04B1", "U+2116"};
     *
     * </pre>
     *
     * @param unicodeChars
     *            the unicodeChars to set
     * @author WFF
     * @since 1.1.2
     */
    public UnicodeRange(final String... unicodeChars) {
        this.unicodeChars = StringUtil.cloneArray(unicodeChars);
        cssValue = getBuiltCssValue(this.unicodeChars);
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * @param unicodeChars
     * @return Defines the range of unicode characters the font supports.
     *         Default value is "U+0-10FFFF"
     * @author WFF
     * @since 1.1.2
     */
    protected static String getBuiltCssValue(final String... unicodeChars) {
        final StringBuilder cssValueSB = new StringBuilder();
        int count = 1;
        for (final String unicodeChar : unicodeChars) {
            cssValueSB.append(unicodeChar);

            if (count != unicodeChars.length) {
                cssValueSB.append(", ");
            }
            count++;
        }
        return StringBuilderUtil.getTrimmedString(cssValueSB);
    }

    /**
     * @param fontFamily
     *            the {@code FontFamily} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public UnicodeRange(final UnicodeRange fontFamily) {
        if (fontFamily == null) {
            throw new NullValueException("fontFamily can not be null");
        }
        setCssValue(fontFamily.getCssValue());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.CssProperty#getCssName()
     *
     * @since 1.1.2
     *
     * @author WFF
     */
    @Override
    public String getCssName() {
        return CssNameConstants.UNICODE_RANGE;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.CssProperty#getCssValue()
     *
     * @since 1.1.2
     *
     * @author WFF
     */
    @Override
    public String getCssValue() {
        return cssValue;
    }

    @Override
    public String toString() {
        return getCssName() + ": " + getCssValue();
    }

    /**
     * @param cssValue
     *            The value should be a unicodeChars sequence.
     * @since 1.1.2
     * @author WFF
     */
    @Override
    public UnicodeRange setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value should be a unicodeChars sequence.");
        } else {

            final String trimmedCssValue = StringUtil.strip(cssValue);

            unicodeChars = StringUtil.splitByComma(cssValue);
            for (int i = 0; i < unicodeChars.length; i++) {
                unicodeChars[i] = StringUtil.strip(unicodeChars[i]);
            }

            this.cssValue = trimmedCssValue;

            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        }
        return this;
    }

    /**
     * <pre>
     * Eg:-
     * String[] unicodeChars = {"U+0400-045F", "U+0490-0491", "U+04B0-04B1", "U+2116"};
     *
     * </pre>
     *
     * @param unicodeChars
     *            the unicodeChars to set
     * @author WFF
     * @since 1.1.2
     */
    public void setUnicodeChars(final String[] unicodeChars) {
        if (unicodeChars != null) {
            this.unicodeChars = StringUtil.cloneArray(unicodeChars);
            cssValue = getBuiltCssValue(this.unicodeChars);
        } else {
            this.unicodeChars = null;
            cssValue = DEFAULT_VALUE;
        }
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * gets the unicodeChars as an array and if here is no value set then
     * returns an empty array.
     *
     * <pre>
     * Note: it will never return null.
     * </pre>
     *
     * @return the unicodeChars. If there is no unicodeChars set then returns an
     *         empty array instead of null.
     * @author WFF
     * @since 1.1.2
     */
    public String[] getUnicodeChars() {
        if (unicodeChars == null) {
            return EMPTY_ARRAY;
        }
        return StringUtil.cloneArray(unicodeChars);
    }

}
