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
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.StringBuilderUtil;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * <pre>
 * -webkit-column-rule: <i>-webkit-column-rule-width -webkit-column-rule-style -webkit-column-rule-color</i>|initial|inherit;
 *
 * The -webkit-column-rule property is a shorthand property for setting all the -webkit-column-rule-* properties.
 *
 * The -webkit-column-rule property sets the width, style, and color of the rule between columns.
 * Default value:  medium none <i>color</i>
 * Inherited:      no
 * Animatable:     yes, see individual properties
 * Version:        CSS3
 * JavaScript syntax:      object.style.columnRule="3px outset blue"
 * </pre>
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class WebkitColumnRule extends AbstractCssProperty<WebkitColumnRule>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private String cssValue;

    private WebkitColumnRuleWidth webkitColumnRuleWidth;

    private WebkitColumnRuleStyle webkitColumnRuleStyle;

    private WebkitColumnRuleColor webkitColumnRuleColor;

    /**
     * The {@code initial} will be set as the value
     */
    public WebkitColumnRule() {
        setCssValue("medium none white");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public WebkitColumnRule(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param webkitColumnRule
     *            the {@code WebkitColumnRule} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public WebkitColumnRule(final WebkitColumnRule webkitColumnRule) {
        if (webkitColumnRule == null) {
            throw new NullValueException("webkitColumnRule can not be null");
        }
        setCssValue(webkitColumnRule.getCssValue());
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
    public WebkitColumnRule setValue(final String value) {
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
        return CssNameConstants.WEBKIT_COLUMN_RULE;
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
     *            the value should be for example
     *            <code>medium none #0000ff</code>. {@code null} is considered
     *            as an invalid value and it will throw
     *            {@code NullValueException}.And an empty string is also
     *            considered as an invalid value and it will throw
     *            {@code InvalidValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public WebkitColumnRule setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value should be any color for example medium none #0000ff. Or, initial/inherit.");
        } else if (StringUtil.isBlank(cssValue)) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value should be any color for example medium none #0000ff. Or, initial/inherit.");
        } else {

            final String trimmedCssValue = StringUtil.strip(cssValue);
            final String[] cssValueParts = StringUtil
                    .splitBySpace(trimmedCssValue);

            if (cssValueParts.length > 1) {
                WebkitColumnRuleWidth webkitColumnRuleWidth = null;
                WebkitColumnRuleStyle webkitColumnRuleStyle = null;
                WebkitColumnRuleColor webkitColumnRuleColor = null;
                for (final String eachPart : cssValueParts) {
                    if (webkitColumnRuleWidth == null
                            && WebkitColumnRuleWidth.isValid(eachPart)) {
                        if (this.webkitColumnRuleWidth == null) {
                            webkitColumnRuleWidth = new WebkitColumnRuleWidth(
                                    eachPart);
                            webkitColumnRuleWidth.setStateChangeInformer(this);
                            webkitColumnRuleWidth.setAlreadyInUse(true);
                        } else {
                            this.webkitColumnRuleWidth.setCssValue(eachPart);
                            webkitColumnRuleWidth = this.webkitColumnRuleWidth;
                        }
                    } else if (webkitColumnRuleStyle == null
                            && WebkitColumnRuleStyle.isValid(eachPart)) {
                        webkitColumnRuleStyle = WebkitColumnRuleStyle
                                .getThis(eachPart);
                    } else if (webkitColumnRuleColor == null
                            && WebkitColumnRuleColor.isValid(eachPart)) {
                        webkitColumnRuleColor = new WebkitColumnRuleColor(
                                eachPart);
                        webkitColumnRuleColor.setStateChangeInformer(this);
                        webkitColumnRuleColor.setAlreadyInUse(true);
                    }
                }
                final StringBuilder cssValueBuilder = new StringBuilder();
                boolean invalid = true;
                if (webkitColumnRuleWidth != null) {
                    cssValueBuilder.append(webkitColumnRuleWidth.getCssValue())
                            .append(' ');
                    invalid = false;
                } else if (this.webkitColumnRuleWidth != null) {
                    this.webkitColumnRuleWidth.setAlreadyInUse(false);
                }
                if (webkitColumnRuleStyle != null) {
                    cssValueBuilder.append(webkitColumnRuleStyle.getCssValue())
                            .append(' ');
                    invalid = false;
                }
                if (webkitColumnRuleColor != null) {
                    cssValueBuilder.append(webkitColumnRuleColor.getCssValue())
                            .append(' ');
                    invalid = false;
                } else if (this.webkitColumnRuleColor != null) {
                    this.webkitColumnRuleColor.setAlreadyInUse(false);
                }
                if (invalid) {
                    throw new InvalidValueException(cssValue
                            + " is an invalid value. The value format should be as for example '25px dotted green'. Or, initial/inherit.");
                }
                this.cssValue = StringBuilderUtil
                        .getTrimmedString(cssValueBuilder);
                this.webkitColumnRuleWidth = webkitColumnRuleWidth;
                this.webkitColumnRuleStyle = webkitColumnRuleStyle;
                this.webkitColumnRuleColor = webkitColumnRuleColor;
            } else {
                if (webkitColumnRuleWidth != null) {
                    webkitColumnRuleWidth.setAlreadyInUse(false);
                    webkitColumnRuleWidth = null;
                }
                if (webkitColumnRuleColor != null) {
                    webkitColumnRuleColor.setAlreadyInUse(false);
                    webkitColumnRuleColor = null;
                }
                webkitColumnRuleStyle = null;
                this.cssValue = trimmedCssValue;
            }
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
     * @return the webkitColumnRuleWidth
     * @since 1.0.0
     * @author WFF
     */
    public WebkitColumnRuleWidth getWebkitColumnRuleWidth() {
        return webkitColumnRuleWidth;
    }

    /**
     * @param webkitColumnRuleWidth
     *            the webkitColumnRuleWidth to set
     * @since 1.0.0
     * @author WFF
     */
    public WebkitColumnRule setWebkitColumnRuleWidth(
            final WebkitColumnRuleWidth webkitColumnRuleWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (webkitColumnRuleWidth != null) {
            cssValueBuilder.append(webkitColumnRuleWidth.getCssValue())
                    .append(' ');
        }

        if (webkitColumnRuleStyle != null) {
            cssValueBuilder.append(webkitColumnRuleStyle.getCssValue())
                    .append(' ');
        }

        if (webkitColumnRuleColor != null) {
            cssValueBuilder.append(webkitColumnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (this.webkitColumnRuleWidth != null) {
            this.webkitColumnRuleWidth.setAlreadyInUse(false);
        }

        this.webkitColumnRuleWidth = webkitColumnRuleWidth;

        if (this.webkitColumnRuleWidth != null) {
            this.webkitColumnRuleWidth.setStateChangeInformer(this);
            this.webkitColumnRuleWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    /**
     * @return the webkitColunmRuleStyle
     * @since 1.0.0
     * @author WFF
     */
    public WebkitColumnRuleStyle getWebkitColumnRuleStyle() {
        return webkitColumnRuleStyle;
    }

    /**
     * @param webkitColunmRuleStyle
     *            the webkitColunmRuleStyle to set
     * @since 1.0.0
     * @author WFF
     * @return
     */
    public WebkitColumnRule setWebkitColumnRuleStyle(
            final WebkitColumnRuleStyle webkitColunmRuleStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (webkitColumnRuleWidth != null) {
            cssValueBuilder.append(webkitColumnRuleWidth.getCssValue())
                    .append(' ');
        }

        if (webkitColunmRuleStyle != null) {
            cssValueBuilder.append(webkitColunmRuleStyle.getCssValue())
                    .append(' ');
        }

        if (webkitColumnRuleColor != null) {
            cssValueBuilder.append(webkitColumnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        webkitColumnRuleStyle = webkitColunmRuleStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    /**
     * @return the webkitColumnRuleColor
     * @since 1.0.0
     * @author WFF
     */
    public WebkitColumnRuleColor getWebkitColumnRuleColor() {
        return webkitColumnRuleColor;
    }

    /**
     * @param webkitColumnRuleColor
     *            the webkitColumnRuleColor to set
     * @since 1.0.0
     * @author WFF
     * @return the current instance.
     */
    public WebkitColumnRule setWebkitColumnRuleColor(
            final WebkitColumnRuleColor webkitColumnRuleColor) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (webkitColumnRuleWidth != null) {
            cssValueBuilder.append(webkitColumnRuleWidth.getCssValue())
                    .append(' ');
        }

        if (webkitColumnRuleStyle != null) {
            cssValueBuilder.append(webkitColumnRuleStyle.getCssValue())
                    .append(' ');
        }

        if (webkitColumnRuleColor != null) {
            cssValueBuilder.append(webkitColumnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (this.webkitColumnRuleColor != null) {
            this.webkitColumnRuleColor.setAlreadyInUse(false);
        }

        this.webkitColumnRuleColor = webkitColumnRuleColor;

        if (this.webkitColumnRuleColor != null) {
            this.webkitColumnRuleColor.setStateChangeInformer(this);
            this.webkitColumnRuleColor.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof WebkitColumnRuleColor) {
            setWebkitColumnRuleColor(
                    (WebkitColumnRuleColor) stateChangedObject);
        }

        if (stateChangedObject instanceof WebkitColumnRuleWidth) {
            setWebkitColumnRuleWidth(
                    (WebkitColumnRuleWidth) stateChangedObject);
        }
    }

}
