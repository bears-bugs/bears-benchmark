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
 * column-rule: <i>column-rule-width column-rule-style column-rule-color</i>|initial|inherit;
 *
 * The column-rule property is a shorthand property for setting all the column-rule-* properties.
 *
 * The column-rule property sets the width, style, and color of the rule between columns.
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
public class ColumnRule extends AbstractCssProperty<ColumnRule>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private String cssValue;

    private ColumnRuleWidth columnRuleWidth;

    private ColumnRuleStyle columnRuleStyle;

    private ColumnRuleColor columnRuleColor;

    /**
     * The {@code initial} will be set as the value
     */
    public ColumnRule() {
        setCssValue("medium none white");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public ColumnRule(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param columnRule
     *            the {@code ColumnRule} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public ColumnRule(final ColumnRule columnRule) {
        if (columnRule == null) {
            throw new NullValueException("columnRule can not be null");
        }
        setCssValue(columnRule.getCssValue());
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
    public ColumnRule setValue(final String value) {
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
        return CssNameConstants.COLUMN_RULE;
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
    public ColumnRule setCssValue(final String cssValue) {
        final String trimmedCssValue;
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value should be any color for example medium none #0000ff. Or, initial/inherit.");
        } else if ((trimmedCssValue = StringUtil.strip(cssValue)).isEmpty()) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value should be any color for example medium none #0000ff. Or, initial/inherit.");
        }

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);

        if (cssValueParts.length > 1) {
            ColumnRuleWidth columnRuleWidth = null;
            ColumnRuleStyle columnRuleStyle = null;
            ColumnRuleColor columnRuleColor = null;
            for (final String eachPart : cssValueParts) {
                if (columnRuleWidth == null
                        && ColumnRuleWidth.isValid(eachPart)) {
                    if (this.columnRuleWidth == null) {
                        columnRuleWidth = new ColumnRuleWidth(eachPart);
                        columnRuleWidth.setStateChangeInformer(this);
                        columnRuleWidth.setAlreadyInUse(true);
                    } else {
                        this.columnRuleWidth.setCssValue(eachPart);
                        columnRuleWidth = this.columnRuleWidth;
                    }
                } else if (columnRuleStyle == null
                        && ColumnRuleStyle.isValid(eachPart)) {
                    columnRuleStyle = ColumnRuleStyle.getThis(eachPart);
                } else if (columnRuleColor == null
                        && ColumnRuleColor.isValid(eachPart)) {
                    columnRuleColor = new ColumnRuleColor(eachPart);
                    columnRuleColor.setStateChangeInformer(this);
                    columnRuleColor.setAlreadyInUse(true);
                }
            }
            final StringBuilder cssValueBuilder = new StringBuilder();
            boolean invalid = true;
            if (columnRuleWidth != null) {
                cssValueBuilder.append(columnRuleWidth.getCssValue())
                        .append(' ');
                invalid = false;
            } else if (this.columnRuleWidth != null) {
                this.columnRuleWidth.setAlreadyInUse(false);
            }
            if (columnRuleStyle != null) {
                cssValueBuilder.append(columnRuleStyle.getCssValue())
                        .append(' ');
                invalid = false;
            }
            if (columnRuleColor != null) {
                cssValueBuilder.append(columnRuleColor.getCssValue())
                        .append(' ');
                invalid = false;
            } else if (this.columnRuleColor != null) {
                this.columnRuleColor.setAlreadyInUse(false);
            }
            if (invalid) {
                throw new InvalidValueException(cssValue
                        + " is an invalid value. The value format should be as for example '25px dotted green'. Or, initial/inherit.");
            }
            this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
            this.columnRuleWidth = columnRuleWidth;
            this.columnRuleStyle = columnRuleStyle;
            this.columnRuleColor = columnRuleColor;
        } else {
            if (columnRuleWidth != null) {
                columnRuleWidth.setAlreadyInUse(false);
                columnRuleWidth = null;
            }
            if (columnRuleColor != null) {
                columnRuleColor.setAlreadyInUse(false);
                columnRuleColor = null;
            }
            columnRuleStyle = null;
            this.cssValue = trimmedCssValue;
        }
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
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
     * @return the columnRuleWidth
     * @since 1.0.0
     * @author WFF
     */
    public ColumnRuleWidth getColumnRuleWidth() {
        return columnRuleWidth;
    }

    /**
     * @param columnRuleWidth
     *            the columnRuleWidth to set
     * @since 1.0.0
     * @author WFF
     */
    public ColumnRule setColumnRuleWidth(
            final ColumnRuleWidth columnRuleWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (columnRuleWidth != null) {
            cssValueBuilder.append(columnRuleWidth.getCssValue());
            cssValueBuilder.append(' ');
        }

        if (columnRuleStyle != null) {
            cssValueBuilder.append(columnRuleStyle.getCssValue()).append(' ');
        }

        if (columnRuleColor != null) {
            cssValueBuilder.append(columnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (this.columnRuleWidth != null) {
            this.columnRuleWidth.setAlreadyInUse(false);
        }

        this.columnRuleWidth = columnRuleWidth;

        if (this.columnRuleWidth != null) {
            this.columnRuleWidth.setStateChangeInformer(this);
            this.columnRuleWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    /**
     * @return the colunmRuleStyle
     * @since 1.0.0
     * @author WFF
     */
    public ColumnRuleStyle getColumnRuleStyle() {
        return columnRuleStyle;
    }

    /**
     * @param columnRuleStyle
     *            the colunmRuleStyle to set
     * @since 1.0.0
     * @author WFF
     * @return
     */
    public ColumnRule setColumnRuleStyle(
            final ColumnRuleStyle columnRuleStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (columnRuleWidth != null) {
            cssValueBuilder.append(columnRuleWidth.getCssValue()).append(' ');
        }

        if (columnRuleStyle != null) {
            cssValueBuilder.append(columnRuleStyle.getCssValue()).append(' ');
        }

        if (columnRuleColor != null) {
            cssValueBuilder.append(columnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.columnRuleStyle = columnRuleStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    /**
     * @return the columnRuleColor
     * @since 1.0.0
     * @author WFF
     */
    public ColumnRuleColor getColumnRuleColor() {
        return columnRuleColor;
    }

    /**
     * @param columnRuleColor
     *            the columnRuleColor to set
     * @since 1.0.0
     * @author WFF
     * @return the current instance.
     */
    public ColumnRule setColumnRuleColor(
            final ColumnRuleColor columnRuleColor) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (columnRuleWidth != null) {
            cssValueBuilder.append(columnRuleWidth.getCssValue()).append(' ');
        }

        if (columnRuleStyle != null) {
            cssValueBuilder.append(columnRuleStyle.getCssValue()).append(' ');
        }

        if (columnRuleColor != null) {
            cssValueBuilder.append(columnRuleColor.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (this.columnRuleColor != null) {
            this.columnRuleColor.setAlreadyInUse(false);
        }

        this.columnRuleColor = columnRuleColor;

        if (this.columnRuleColor != null) {
            this.columnRuleColor.setStateChangeInformer(this);
            this.columnRuleColor.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof ColumnRuleColor) {
            setColumnRuleColor((ColumnRuleColor) stateChangedObject);
        }

        if (stateChangedObject instanceof ColumnRuleWidth) {
            setColumnRuleWidth((ColumnRuleWidth) stateChangedObject);
        }
    }

}
