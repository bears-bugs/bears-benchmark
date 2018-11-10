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

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.WffRuntimeException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * list-style-image: none|url|initial|inherit;
 *
 * @author WFF
 * @since 1.0.0
 */
public class ListStyleImage extends AbstractCssProperty<ListStyleImage> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String NONE = "none";
    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private String cssValue;

    private String url;

    /**
     * The {@code auto} will be set as the value
     */
    public ListStyleImage() {
        setCssValue(NONE);
    }

    /**
     * @param cssValue
     *            the css value to set.
     *
     */
    public ListStyleImage(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param value
     *            the css value or url value to set.
     * @param urlValue
     *            images/TestImage.png
     */
    public ListStyleImage(final String value, final boolean urlValue) {
        if (urlValue) {
            setUrl(value);
        } else {
            setCssValue(value);
        }
    }

    /**
     * @param listStyleImage
     *            the {@code ListStyleImage} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public ListStyleImage(final ListStyleImage listStyleImage) {
        if (listStyleImage == null) {
            throw new NullValueException("listStyleImage can not be null");
        }
        setCssValue(listStyleImage.getCssValue());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.CssProperty#getCssName()
     *
     * @since 1.0.0
     *
     * @author WFF
     */
    @Override
    public String getCssName() {
        return CssNameConstants.LIST_STYLE_IMAGE;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.CssProperty#getCssValue()
     *
     * @since 1.0.0
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
     *            the value should be in the format of <code>55px</code> or
     *            <code>95%</code>. {@code null} is considered as an invalid
     *            value and it will throw {@code NullValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public ListStyleImage setCssValue(final String cssValue) {
        final String previousCssValue = this.cssValue;
        try {
            String trimmedCssValue = null;
            if (cssValue == null) {
                throw new NullValueException(
                        "null is an invalid value. The value format should be as for example url(/images/Test.png). Or, initial/inherit.");
            } else if ((trimmedCssValue = StringUtil.strip(cssValue))
                    .equalsIgnoreCase(INITIAL)
                    || trimmedCssValue.equalsIgnoreCase(INHERIT)
                    || trimmedCssValue.equalsIgnoreCase(NONE)) {
                this.cssValue = StringUtil.strip(cssValue).toLowerCase();
                url = null;
            } else if (trimmedCssValue.toLowerCase().startsWith("url(")
                    && trimmedCssValue.endsWith(")")) {
                url = trimmedCssValue.substring(
                        trimmedCssValue.toLowerCase().indexOf("url(") + 4,
                        trimmedCssValue.indexOf(')'));
                if (url.length() > 0 && url.charAt(0) == '"'
                        && url.charAt(url.length() - 1) == '"') {
                    url = url.substring(url.indexOf('\"') + 1,
                            url.lastIndexOf('\"'));
                }

                this.cssValue = "url(\"" + url + "\")";
            } else {
                throw new InvalidValueException(cssValue
                        + " is an invalid value. The value format should be as for example url(/images/Test.png). Or, initial/inherit.");
            }

            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        } catch (final WffRuntimeException e) {
            this.cssValue = previousCssValue;
            throw new InvalidValueException(e.getMessage(), e);
        } catch (final NumberFormatException e) {
            this.cssValue = previousCssValue;
            throw new InvalidValueException(
                    cssValue + " is an invalid value. The value format should be as for example url(/images/Test.png). Or, initial/inherit.",
                    e);
        }
        return this;
    }

    /**
     * @param url
     *            eg:- images/Test.png
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public ListStyleImage setUrl(final String url) {
        this.url = url;
        setCssValue("url(\"" + url + "\")");
        return this;
    }

    /**
     * @return the url value set. Or, {@code null} if it is set with any inbuilt
     *         value like {@code inherit}
     * @since 1.0.0
     * @author WFF
     */
    public String getUrl() {
        return url;
    }

    /*
     * public String getValue() { return url != null ? url : getCssValue(); }
     */

    /**
     * sets as initial
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInitial() {
        setCssValue(INITIAL);
    }

    /**
     * sets as inherit
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInherit() {
        setCssValue(INHERIT);
    }

    /**
     * sets as {@code none}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsNone() {
        setCssValue(NONE);
    }

    public boolean hasInbuiltValue() {
        if (getCssValue() == null) {
            return false;
        }
        final String trimmedCssValue = StringUtil.strip(getCssValue());
        return trimmedCssValue.equalsIgnoreCase(INITIAL)
                || trimmedCssValue.equalsIgnoreCase(INHERIT)
                || trimmedCssValue.equalsIgnoreCase(NONE);
    }

    /**
     * To check whether the given {@code cssValue} is valid for this class.
     *
     * @param cssValue
     * @return true if the given {@code cssValue} is valid for this class.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String cssValue) {
        String trimmedCssValue = null;
        if (cssValue == null) {
            return false;
        } else if ((trimmedCssValue = StringUtil.strip(cssValue))
                .equalsIgnoreCase(INITIAL)
                || trimmedCssValue.equalsIgnoreCase(INHERIT)
                || trimmedCssValue.equalsIgnoreCase(NONE)) {
            return true;
        } else if (trimmedCssValue.toLowerCase().startsWith("url(")
                && trimmedCssValue.endsWith(")")) {
            return true;
        }
        return false;
    }
}
