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

import java.util.Arrays;
import java.util.List;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * The line-height property specifies the line height.
 *
 * Note: Negative values are not allowed.
 * Default value:  normal
 * Inherited:      yes
 * Animatable:     yes
 * Version:        CSS1
 * JavaScript syntax:      object.style.lineHeight="30px"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class LineHeight extends AbstractCssProperty<LineHeight> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String NORMAL = "normal";
    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, NORMAL);

    private String cssValue;
    private Float value;
    private CssLengthUnit cssLengthUnit;

    /**
     * The {@code normal} will be set as the value
     */
    public LineHeight() {
        setCssValue(NORMAL);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public LineHeight(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param lineHeight
     *            the {@code LineHeight} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public LineHeight(final LineHeight lineHeight) {
        if (lineHeight == null) {
            throw new NullValueException("lineHeight can not be null");
        }
        setCssValue(lineHeight.getCssValue());
    }

    /**
     * @param value
     *            the \value to set without unit.
     * @since 1.0.0
     * @author WFF
     */
    public LineHeight(final float value) {
        this.value = value;
        cssValue = String.valueOf(value);
    }

    /**
     * @param value
     * @param cssLengthUnit
     */
    public LineHeight(final float value, final CssLengthUnit cssLengthUnit) {
        this.value = value;
        this.cssLengthUnit = cssLengthUnit;
        cssValue = String.valueOf(value)
                + (cssLengthUnit == null ? "" : cssLengthUnit);
    }

    /**
     * @param value
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public LineHeight setValue(final float value) {
        this.value = value;
        cssLengthUnit = null;
        cssValue = String.valueOf(value);
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    /**
     * @param value
     * @param cssLengthUnit
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public LineHeight setValue(final float value,
            final CssLengthUnit cssLengthUnit) {
        this.value = value;
        this.cssLengthUnit = cssLengthUnit;
        cssValue = String.valueOf(value)
                + (cssLengthUnit == null ? "" : cssLengthUnit);
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    /**
     * @param percent
     *            the percent to set
     * @since 1.0.0
     * @author WFF
     */
    public void setPercent(final float percent) {
        value = percent;
        cssLengthUnit = CssLengthUnit.PER;
        cssValue = String.valueOf(percent) + cssLengthUnit;
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
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
        return CssNameConstants.LINE_HEIGHT;
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
     * gets the width in float value. {@code LineHeight#getUnit()} should be
     * used to get the cssLengthUnit for this value.
     *
     * @return the value in float or null if the cssValue is
     *         <code>initial</code> or <code>inherit</code>.
     * @since 1.0.0
     * @author WFF
     */
    public Float getValue() {
        return value;
    }

    /**
     * @return the cssLengthUnit {@code PX}/{@code PER}, or {@code null} if the
     *         value is any inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public CssLengthUnit getUnit() {
        return cssLengthUnit;
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
    public LineHeight setCssValue(final String cssValue) {
        final String previousCssValue = this.cssValue;
        try {
            if (cssValue == null) {
                throw new NullValueException(
                        "null is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit.");
            } else {
                final String trimmedCssValue = TagStringUtil
                        .toLowerCase(StringUtil.strip(cssValue));

                if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                    this.cssValue = trimmedCssValue;
                    cssLengthUnit = null;
                    value = null;

                    if (getStateChangeInformer() != null) {
                        getStateChangeInformer().stateChanged(this);
                    }
                    return this;
                }

                try {
                    value = Float.parseFloat(trimmedCssValue);
                    // it could be -ve when it comes as sub
                    // if (value < 0) {
                    // throw new InvalidValueException(
                    // "lineHeight cannot be a negative value");
                    // }
                    cssLengthUnit = null;
                    this.cssValue = trimmedCssValue;

                    if (getStateChangeInformer() != null) {
                        getStateChangeInformer().stateChanged(this);
                    }
                    return this;
                } catch (final NumberFormatException e) {
                    // NOP
                }

                boolean invalidValue = true;
                for (final CssLengthUnit cssLengthUnit : CssLengthUnit
                        .values()) {
                    final String unit = cssLengthUnit.getUnit();
                    if (trimmedCssValue.endsWith(unit)) {
                        final String valueOnly = trimmedCssValue
                                .replaceFirst(unit, "");
                        try {
                            value = Float.parseFloat(valueOnly);
                            // it could be -ve when it comes as sub
                            // if (value < 0) {
                            // throw new InvalidValueException(
                            // "lineHeight cannot be a negative value");
                            // }
                        } catch (final NumberFormatException e) {
                            break;
                        }
                        this.cssLengthUnit = cssLengthUnit;
                        this.cssValue = trimmedCssValue;
                        invalidValue = false;
                        break;
                    }
                }

                if (invalidValue) {
                    throw new InvalidValueException(cssValue
                            + " is an invalid value. The value format should be as for example 75px, 85%, initial, inherit etc..");
                }

                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
            }

        } catch (final NumberFormatException e) {
            this.cssValue = previousCssValue;
            throw new InvalidValueException(
                    cssValue + " is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit.",
                    e);
        } catch (final InvalidValueException e) {
            this.cssValue = previousCssValue;
            throw e;
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
     * sets as {@code normal}
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsNormal() {
        setCssValue(NORMAL);
    }

    /**
     * validates if the given cssValue is valid for this class.
     *
     * @param cssValue
     *            the value to check.
     * @return true if valid and false if invalid.
     * @author WFF
     * @since 1.0.0
     */
    public static boolean isValid(final String cssValue) {

        final String trimmedCssValue = TagStringUtil
                .toLowerCase(StringUtil.strip(cssValue));

        if (StringUtil.containsSpace(trimmedCssValue)) {
            return false;
        }

        try {
            Float.parseFloat(trimmedCssValue);
            return true;
        } catch (final NumberFormatException e) {
            // NOP
        }

        for (final CssLengthUnit cssLengthUnit : CssLengthUnit.values()) {
            final String unit = cssLengthUnit.getUnit();
            if (trimmedCssValue.endsWith(unit)) {
                final String valueOnly = trimmedCssValue.replaceFirst(unit, "");
                try {
                    // it could be -ve when it comes as sub
                    Float.parseFloat(valueOnly);
                } catch (final NumberFormatException e) {
                    break;
                }
                return true;
            }
        }

        return PREDEFINED_CONSTANTS.contains(trimmedCssValue);
    }
}
