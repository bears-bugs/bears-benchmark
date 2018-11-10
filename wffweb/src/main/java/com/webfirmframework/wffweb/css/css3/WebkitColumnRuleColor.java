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
package com.webfirmframework.wffweb.css.css3;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.core.constants.CommonConstants;
import com.webfirmframework.wffweb.css.CssColorName;
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * -webkit-column-rule-color: color|initial|inherit;
 *
 * The -webkit-column-rule-color property specifies the color of the rule between columns.
 * Default value:  The current color of the element (In this java class it will be as <i>initial</i>)
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS3
 * JavaScript syntax:      <i>object</i>.style.columnRuleColor="#0000ff"
 * </pre>
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class WebkitColumnRuleColor
        extends AbstractCssProperty<WebkitColumnRuleColor> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private String cssValue;

    /**
     * The {@code initial} will be set as the value
     */
    public WebkitColumnRuleColor() {
        setCssValue(INITIAL);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public WebkitColumnRuleColor(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param webkitColumnRuleColor
     *            the {@code WebkitColumnRuleColor} object from which the
     *            cssValue to set.And, {@code null} will throw
     *            {@code NullValueException}
     */
    public WebkitColumnRuleColor(
            final WebkitColumnRuleColor webkitColumnRuleColor) {
        if (webkitColumnRuleColor == null) {
            throw new NullValueException("columnRuleColor can not be null");
        }
        setCssValue(webkitColumnRuleColor.getCssValue());
    }

    /**
     * the color/color code to set. The alternative method {@code setCssValue}
     * can also be used.
     *
     * @param value
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public WebkitColumnRuleColor setValue(final String value) {
        setCssValue(value);
        return this;
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
        return CssNameConstants.WEBKIT_COLUMN_RULE_COLOR;
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
     * gets the value, {@code getCssValue} method can also be used to get the
     * same value.
     *
     * @return the value in String.
     * @since 1.0.0
     * @author WFF
     */
    public String getValue() {
        return getCssValue();
    }

    /**
     * @param cssValue
     *            the value should be a color/color code, for example
     *            <code>#0000ff</code>. {@code null} is considered as an invalid
     *            value and it will throw {@code NullValueException}.And an
     *            empty string is also considered as an invalid value and it
     *            will throw {@code InvalidValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public WebkitColumnRuleColor setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value should be any color for example #0000ff. Or, initial/inherit.");
        } else if (StringUtil.isBlank(cssValue)) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value should be any color for example #0000ff. Or, initial/inherit.");
        } else {
            this.cssValue = StringUtil.strip(cssValue);
            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        }
        return this;
    }

    /**
     * sets as {@code initial}
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInitial() {
        setCssValue(INITIAL);
    }

    /**
     * sets as {@code inherit}
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInherit() {
        setCssValue(INHERIT);
    }

    /**
     * @param cssValue
     * @return true if the given {@code cssValue} is valid.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String cssValue) {

        if (cssValue == null) {
            return false;
        }

        final String trimmedCssValue = StringUtil.strip(cssValue);

        if (trimmedCssValue.isEmpty()) {
            return false;
        }

        final String trimmedCssValueLowerCase = TagStringUtil
                .toLowerCase(trimmedCssValue);

        if (INITIAL.equals(trimmedCssValueLowerCase)
                || INHERIT.equals(trimmedCssValueLowerCase)) {
            return true;
        }

        if (CssColorName.isValid(trimmedCssValue)) {
            return true;
        }

        try {
            if (trimmedCssValue.length() == 0
                    || trimmedCssValue.charAt(0) != '#') {
                return false;
            }
            final long value = Long.parseLong(trimmedCssValue.substring(1), 16);

            return !(value > CommonConstants.FFFFFF_HEX_VALUE || value < 0);
        } catch (final NumberFormatException ex) {
        }

        return false;
    }

}
