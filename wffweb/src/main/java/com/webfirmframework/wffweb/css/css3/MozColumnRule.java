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
 * -moz-column-rule: <i>-moz-column-rule-width -moz-column-rule-style -moz-column-rule-color</i>|initial|inherit;
 *
 * The -moz-column-rule property is a shorthand property for setting all the -moz-column-rule-* properties.
 *
 * The -moz-column-rule property sets the width, style, and color of the rule between columns.
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
public class MozColumnRule extends AbstractCssProperty<MozColumnRule>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private String cssValue;

    private MozColumnRuleWidth mozColumnRuleWidth;

    private MozColumnRuleStyle mozColumnRuleStyle;

    private MozColumnRuleColor mozColumnRuleColor;

    /**
     * The {@code initial} will be set as the value
     */
    public MozColumnRule() {
        setCssValue("medium none white");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public MozColumnRule(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param mozColumnRule
     *            the {@code MozColumnRule} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public MozColumnRule(final MozColumnRule mozColumnRule) {
        if (mozColumnRule == null) {
            throw new NullValueException("mozColumnRule can not be null");
        }
        setCssValue(mozColumnRule.getCssValue());
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
    public MozColumnRule setValue(final String value) {
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
        return CssNameConstants.MOZ_COLUMN_RULE;
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
    public MozColumnRule setCssValue(final String cssValue) {
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
                MozColumnRuleWidth mozColumnRuleWidth = null;
                MozColumnRuleStyle mozColumnRuleStyle = null;
                MozColumnRuleColor mozColumnRuleColor = null;
                for (final String eachPart : cssValueParts) {
                    if (mozColumnRuleWidth == null
                            && MozColumnRuleWidth.isValid(eachPart)) {
                        if (this.mozColumnRuleWidth == null) {
                            mozColumnRuleWidth = new MozColumnRuleWidth(
                                    eachPart);
                            mozColumnRuleWidth.setStateChangeInformer(this);
                            mozColumnRuleWidth.setAlreadyInUse(true);
                        } else {
                            this.mozColumnRuleWidth.setCssValue(eachPart);
                            mozColumnRuleWidth = this.mozColumnRuleWidth;
                        }
                    } else if (mozColumnRuleStyle == null
                            && MozColumnRuleStyle.isValid(eachPart)) {
                        mozColumnRuleStyle = MozColumnRuleStyle
                                .getThis(eachPart);
                    } else if (mozColumnRuleColor == null
                            && MozColumnRuleColor.isValid(eachPart)) {
                        mozColumnRuleColor = new MozColumnRuleColor(eachPart);
                        mozColumnRuleColor.setStateChangeInformer(this);
                        mozColumnRuleColor.setAlreadyInUse(true);
                    }
                }
                final StringBuilder cssValueBuilder = new StringBuilder();
                boolean invalid = true;
                if (mozColumnRuleWidth != null) {
                    cssValueBuilder.append(mozColumnRuleWidth.getCssValue())
                            .append(' ');
                    invalid = false;
                } else if (this.mozColumnRuleWidth != null) {
                    this.mozColumnRuleWidth.setAlreadyInUse(false);
                }
                if (mozColumnRuleStyle != null) {
                    cssValueBuilder.append(mozColumnRuleStyle.getCssValue())
                            .append(' ');
                    invalid = false;
                }
                if (mozColumnRuleColor != null) {
                    cssValueBuilder.append(mozColumnRuleColor.getCssValue())
                            .append(' ');
                    invalid = false;
                } else if (this.mozColumnRuleColor != null) {
                    this.mozColumnRuleColor.setAlreadyInUse(false);
                }
                if (invalid) {
                    throw new InvalidValueException(cssValue
                            + " is an invalid value. The value format should be as for example '25px dotted green'. Or, initial/inherit.");
                }
                this.cssValue = StringBuilderUtil
                        .getTrimmedString(cssValueBuilder);
                this.mozColumnRuleWidth = mozColumnRuleWidth;
                this.mozColumnRuleStyle = mozColumnRuleStyle;
                this.mozColumnRuleColor = mozColumnRuleColor;
            } else {
                if (mozColumnRuleWidth != null) {
                    mozColumnRuleWidth.setAlreadyInUse(false);
                    mozColumnRuleWidth = null;
                }
                if (mozColumnRuleColor != null) {
                    mozColumnRuleColor.setAlreadyInUse(false);
                    mozColumnRuleColor = null;
                }
                mozColumnRuleStyle = null;
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
     * @return the mozColumnRuleWidth
     * @since 1.0.0
     * @author WFF
     */
    public MozColumnRuleWidth getMozColumnRuleWidth() {
        return mozColumnRuleWidth;
    }

    /**
     * @param mozColumnRuleWidth
     *            the mozColumnRuleWidth to set
     * @since 1.0.0
     * @author WFF
     */
    public MozColumnRule setMozColumnRuleWidth(
            final MozColumnRuleWidth mozColumnRuleWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (mozColumnRuleWidth != null) {
            cssValueBuilder.append(mozColumnRuleWidth.getCssValue())
                    .append(' ');
        }

        if (mozColumnRuleStyle != null) {
            cssValueBuilder.append(mozColumnRuleStyle.getCssValue())
                    .append(' ');
        }

        if (mozColumnRuleColor != null) {
            cssValueBuilder.append(mozColumnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (this.mozColumnRuleWidth != null) {
            this.mozColumnRuleWidth.setAlreadyInUse(false);
        }

        this.mozColumnRuleWidth = mozColumnRuleWidth;

        if (this.mozColumnRuleWidth != null) {
            this.mozColumnRuleWidth.setStateChangeInformer(this);
            this.mozColumnRuleWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    /**
     * @return the mozColunmRuleStyle
     * @since 1.0.0
     * @author WFF
     */
    public MozColumnRuleStyle getMozColumnRuleStyle() {
        return mozColumnRuleStyle;
    }

    /**
     * @param mozColunmRuleStyle
     *            the mozColunmRuleStyle to set
     * @since 1.0.0
     * @author WFF
     * @return
     */
    public MozColumnRule setMozColumnRuleStyle(
            final MozColumnRuleStyle mozColunmRuleStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (mozColumnRuleWidth != null) {
            cssValueBuilder.append(mozColumnRuleWidth.getCssValue())
                    .append(' ');
        }

        if (mozColunmRuleStyle != null) {
            cssValueBuilder.append(mozColunmRuleStyle.getCssValue())
                    .append(' ');
        }

        if (mozColumnRuleColor != null) {
            cssValueBuilder.append(mozColumnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        mozColumnRuleStyle = mozColunmRuleStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    /**
     * @return the mozColumnRuleColor
     * @since 1.0.0
     * @author WFF
     */
    public MozColumnRuleColor getMozColumnRuleColor() {
        return mozColumnRuleColor;
    }

    /**
     * @param mozColumnRuleColor
     *            the mozColumnRuleColor to set
     * @since 1.0.0
     * @author WFF
     * @return the current instance.
     */
    public MozColumnRule setMozColumnRuleColor(
            final MozColumnRuleColor mozColumnRuleColor) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (mozColumnRuleWidth != null) {
            cssValueBuilder.append(mozColumnRuleWidth.getCssValue())
                    .append(' ');
        }

        if (mozColumnRuleStyle != null) {
            cssValueBuilder.append(mozColumnRuleStyle.getCssValue())
                    .append(' ');
        }

        if (mozColumnRuleColor != null) {
            cssValueBuilder.append(mozColumnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (this.mozColumnRuleColor != null) {
            this.mozColumnRuleColor.setAlreadyInUse(false);
        }

        this.mozColumnRuleColor = mozColumnRuleColor;

        if (this.mozColumnRuleColor != null) {
            this.mozColumnRuleColor.setStateChangeInformer(this);
            this.mozColumnRuleColor.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof MozColumnRuleColor) {
            setMozColumnRuleColor((MozColumnRuleColor) stateChangedObject);
        }

        if (stateChangedObject instanceof MozColumnRuleWidth) {
            setMozColumnRuleWidth((MozColumnRuleWidth) stateChangedObject);
        }
    }

}
