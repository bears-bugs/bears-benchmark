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
import java.util.Objects;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.CssLengthUtil;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * border-spacing: <i>length</i>|initial|inherit;
 *
 * The border-spacing property sets the distance between the borders of adjacent cells (only for the "separated borders" model).
 * Default value:  0
 * Inherited:      yes
 * Animatable:     yes
 * Version:        CSS2
 * JavaScript syntax:      object.style.borderSpacing="15px"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderSpacing extends AbstractCssProperty<BorderSpacing> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;
    private Float horizontalValue;
    private CssLengthUnit horizontalCssLengthUnit;

    private Float verticalValue;
    private CssLengthUnit verticalCssLengthUnit;

    /**
     * The {@code 0px} will be set as the value
     */
    public BorderSpacing() {
        setCssValue("0px");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderSpacing(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderSpacing
     *            the {@code borderSpacing} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderSpacing(final BorderSpacing borderSpacing) {
        if (borderSpacing == null) {
            throw new NullValueException("borderSpacing can not be null");
        }
        setCssValue(borderSpacing.getCssValue());
    }

    /**
     * @param horizontalVerticalValue
     *            the value to set.
     * @param cssLengthUnit
     *            the value unit to set.
     * @since 1.0.0
     * @author WFF
     */
    public BorderSpacing(final float horizontalVerticalValue,
            final CssLengthUnit cssLengthUnit) {

        horizontalValue = verticalValue = Float
                .valueOf(horizontalVerticalValue);
        horizontalCssLengthUnit = verticalCssLengthUnit = cssLengthUnit;

        cssValue = String.valueOf(horizontalVerticalValue) + cssLengthUnit;
    }

    /**
     * @param horizontalValue
     * @param horizontalCssLengthUnit
     * @param verticalValue
     * @param verticalCssLengthUnit
     * @author WFF
     * @since 1.0.0
     */
    public BorderSpacing(final float horizontalValue,
            final CssLengthUnit horizontalCssLengthUnit,
            final float verticalValue,
            final CssLengthUnit verticalCssLengthUnit) {

        this.horizontalValue = Float.valueOf(horizontalValue);
        this.verticalValue = Float.valueOf(verticalValue);

        this.horizontalCssLengthUnit = horizontalCssLengthUnit;
        this.verticalCssLengthUnit = verticalCssLengthUnit;

        if (horizontalValue == verticalValue && Objects
                .equals(horizontalCssLengthUnit, verticalCssLengthUnit)) {
            cssValue = String.valueOf(horizontalValue)
                    + horizontalCssLengthUnit;
        } else {
            cssValue = new StringBuilder().append(horizontalValue)
                    .append(horizontalCssLengthUnit).append(' ')
                    .append(verticalValue).append(verticalCssLengthUnit)
                    .toString();
        }
    }

    /**
     * @param horizontalValue
     * @param horizontalCssLengthUnit
     * @param verticalValue
     * @param verticalCssLengthUnit
     * @return
     * @author WFF
     * @since 1.0.0
     */
    public BorderSpacing setValue(final float horizontalValue,
            final CssLengthUnit horizontalCssLengthUnit,
            final float verticalValue,
            final CssLengthUnit verticalCssLengthUnit) {

        this.horizontalValue = Float.valueOf(horizontalValue);
        this.verticalValue = Float.valueOf(verticalValue);

        this.horizontalCssLengthUnit = horizontalCssLengthUnit;
        this.verticalCssLengthUnit = verticalCssLengthUnit;

        if (horizontalValue == verticalValue && Objects
                .equals(horizontalCssLengthUnit, verticalCssLengthUnit)) {
            cssValue = String.valueOf(horizontalValue)
                    + horizontalCssLengthUnit;
        } else {
            cssValue = new StringBuilder().append(horizontalValue)
                    .append(horizontalCssLengthUnit).append(' ')
                    .append(verticalValue).append(verticalCssLengthUnit)
                    .toString();
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    /**
     * @param horizontalVerticalValue
     * @param cssLengthUnit
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public BorderSpacing setValue(final float horizontalVerticalValue,
            final CssLengthUnit cssLengthUnit) {

        horizontalValue = verticalValue = Float
                .valueOf(horizontalVerticalValue);
        horizontalCssLengthUnit = verticalCssLengthUnit = cssLengthUnit;

        cssValue = String.valueOf(horizontalVerticalValue) + cssLengthUnit;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
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
        return CssNameConstants.BORDER_SPACING;
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
     * gets the horizontal value in float value.
     * {@code BorderSpacing#getHorizontalUnit()} should be used to get the
     * cssLengthUnit for this value.
     *
     * @return the horizontal value in float or {@code null} if the value is any
     *         inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public Float getHorizontalValue() {
        return horizontalValue;
    }

    /**
     * gets the vertical value in float value.
     * {@code BorderSpacing#getVerticalUnit()} should be used to get the
     * cssLengthUnit for this value.
     *
     * @return the vertical value in float or {@code null} if the value is any
     *         inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public Float getVerticalValue() {
        return verticalValue;
    }

    /**
     * @return the cssLengthUnit {@code PX}/{@code PER}, or {@code null} if the
     *         value is any inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public CssLengthUnit getHorizontalUnit() {
        return horizontalCssLengthUnit;
    }

    /**
     * @return the cssLengthUnit {@code PX}/{@code PER}, or {@code null} if the
     *         value is any inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public CssLengthUnit getVerticalUnit() {
        return verticalCssLengthUnit;
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
    public BorderSpacing setCssValue(final String cssValue) {
        try {
            if (cssValue == null) {
                throw new NullValueException(
                        "null is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit.");
            } else {
                final String trimmedCssValue = TagStringUtil
                        .toLowerCase(StringUtil.strip(cssValue));
                boolean invalidValue = true;

                final String[] lengthValues = StringUtil
                        .splitBySpace(trimmedCssValue);

                if (lengthValues.length == 1) {
                    if (trimmedCssValue.equalsIgnoreCase(INITIAL)
                            || trimmedCssValue.equalsIgnoreCase(INHERIT)) {
                        this.cssValue = trimmedCssValue;
                        horizontalValue = null;
                        verticalValue = null;
                        horizontalCssLengthUnit = null;
                        verticalCssLengthUnit = null;
                        invalidValue = false;
                    } else {
                        final Object[] lengthValueAndUnitHorizontalVertical = CssLengthUtil
                                .getLengthValueAndUnit(lengthValues[0]);

                        if (lengthValueAndUnitHorizontalVertical.length == 2) {
                            horizontalValue = verticalValue = (Float) lengthValueAndUnitHorizontalVertical[0];
                            horizontalCssLengthUnit = verticalCssLengthUnit = (CssLengthUnit) lengthValueAndUnitHorizontalVertical[1];
                            this.cssValue = trimmedCssValue;
                            invalidValue = false;
                        }
                    }
                } else if (lengthValues.length == 2) {
                    final Object[] lengthValueAndUnitHorizontal = CssLengthUtil
                            .getLengthValueAndUnit(lengthValues[0]);

                    final Object[] lengthValueAndUnitVertical = CssLengthUtil
                            .getLengthValueAndUnit(lengthValues[1]);

                    if (lengthValueAndUnitHorizontal.length == 2
                            && lengthValueAndUnitVertical.length == 2) {
                        horizontalValue = (Float) lengthValueAndUnitHorizontal[0];
                        horizontalCssLengthUnit = (CssLengthUnit) lengthValueAndUnitHorizontal[1];
                        verticalValue = (Float) lengthValueAndUnitVertical[0];
                        verticalCssLengthUnit = (CssLengthUnit) lengthValueAndUnitVertical[1];

                        // nano optimized way to check if both horizontalValue
                        // and verticalValue are equal.
                        if (Float.floatToIntBits(
                                horizontalValue.floatValue()) == Float
                                        .floatToIntBits(
                                                horizontalValue.floatValue())
                                && Objects.equals(horizontalCssLengthUnit,
                                        verticalCssLengthUnit)

                        ) {
                            this.cssValue = String.valueOf(horizontalValue)
                                    + verticalCssLengthUnit;
                        } else {
                            this.cssValue = trimmedCssValue;
                        }
                        invalidValue = false;
                    }
                } else {
                    throw new InvalidValueException(
                            "The given cssValue should not contain more that 2 length values.");
                }

                if (invalidValue) {
                    throw new InvalidValueException(cssValue
                            + " is an invalid value. The value format should be as for example 75px, 85%, 125px 10px, initial, inherit etc..");
                }
            }
            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        } catch (final NumberFormatException e) {
            throw new InvalidValueException(
                    cssValue + " is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit.",
                    e);
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

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);
        if (cssValueParts.length > 2) {
            return false;
        }
        if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
            return true;
        }

        boolean valid = false;
        for (final String each : cssValueParts) {
            final Object[] lengthValueAndUnit = CssLengthUtil
                    .getLengthValueAsPremitiveAndUnit(each);
            if (lengthValueAndUnit.length != 2) {
                return false;
            }
            valid = true;
        }
        return valid;
    }
}
