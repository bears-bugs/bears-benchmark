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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.CssLengthUtil;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * perspective-origin: <i>x-axis y-axis</i>|initial|inherit;
 *
 * The perspective-origin property defines where a 3D element is based in the x- and the y-axis. This property allows you to change the bottom position of 3D elements.
 *
 * When defining the perspective-origin property for an element, it is the CHILD elements that are positioned, NOT the element itself.
 *
 * Note: This property must be used together with the perspective property, and only affects 3D transformed elements!
 *
 * To better understand the perspective-origin property, view a demo.
 * Default value:  50% 50%
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS3
 * JavaScript syntax:      object.style.perspectiveOrigin="10px 50%"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class PerspectiveOrigin extends AbstractCssProperty<PerspectiveOrigin> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;
    private Float xAxis;
    private CssLengthUnit xAxisCssLengthUnit;

    private Float yAxis;
    private CssLengthUnit yAxisCssLengthUnit;

    /**
     * The {@code 0px} will be set as the value
     */
    public PerspectiveOrigin() {
        setCssValue("0px");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public PerspectiveOrigin(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param backgroundSize
     *            the {@code PerspectiveOrigin} object from which the cssValue
     *            to set.And, {@code null} will throw {@code NullValueException}
     */
    public PerspectiveOrigin(final PerspectiveOrigin backgroundSize) {
        if (backgroundSize == null) {
            throw new NullValueException("backgroundSize can not be null");
        }
        setCssValue(backgroundSize.getCssValue());
    }

    /**
     * @param xyAxis
     *            the value to set.
     * @param cssLengthUnit
     *            the value unit to set.
     * @since 1.0.0
     * @author WFF
     */
    public PerspectiveOrigin(final float xyAxis,
            final CssLengthUnit cssLengthUnit) {

        xAxis = yAxis = Float.valueOf(xyAxis);
        xAxisCssLengthUnit = yAxisCssLengthUnit = cssLengthUnit;

        cssValue = String.valueOf(xyAxis) + cssLengthUnit;
    }

    /**
     * @param xAxis
     * @param xAxisCssLengthUnit
     * @param yAxis
     * @param yAxisCssLengthUnit
     * @author WFF
     * @since 1.0.0
     */
    public PerspectiveOrigin(final float xAxis,
            final CssLengthUnit xAxisCssLengthUnit, final float yAxis,
            final CssLengthUnit yAxisCssLengthUnit) {

        this.xAxis = Float.valueOf(xAxis);
        this.yAxis = Float.valueOf(yAxis);

        this.xAxisCssLengthUnit = xAxisCssLengthUnit;
        this.yAxisCssLengthUnit = yAxisCssLengthUnit;

        if (xAxis == yAxis
                && Objects.equals(xAxisCssLengthUnit, yAxisCssLengthUnit)) {
            cssValue = String.valueOf(xAxis)
                    .concat(xAxisCssLengthUnit.toString());
        } else {
            cssValue = new StringBuilder().append(xAxis)
                    .append(xAxisCssLengthUnit).append(' ').append(yAxis)
                    .append(yAxisCssLengthUnit).toString();

        }
    }

    /**
     * @param xAxis
     * @param xAxisCssLengthUnit
     * @param yAxis
     * @param yAxisCssLengthUnit
     * @return
     * @author WFF
     * @since 1.0.0
     */
    public PerspectiveOrigin setValue(final float xAxis,
            final CssLengthUnit xAxisCssLengthUnit, final float yAxis,
            final CssLengthUnit yAxisCssLengthUnit) {

        this.xAxis = Float.valueOf(xAxis);
        this.yAxis = Float.valueOf(yAxis);

        this.xAxisCssLengthUnit = xAxisCssLengthUnit;
        this.yAxisCssLengthUnit = yAxisCssLengthUnit;

        if (xAxis == yAxis
                && Objects.equals(xAxisCssLengthUnit, yAxisCssLengthUnit)) {
            cssValue = String.valueOf(xAxis)
                    .concat(xAxisCssLengthUnit.toString());
        } else {
            cssValue = new StringBuilder().append(xAxis)
                    .append(xAxisCssLengthUnit).append(' ').append(yAxis)
                    .append(yAxisCssLengthUnit).toString();
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    /**
     * @param xyAxis
     * @param cssLengthUnit
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public PerspectiveOrigin setValue(final float xyAxis,
            final CssLengthUnit cssLengthUnit) {

        xAxis = yAxis = Float.valueOf(xyAxis);
        xAxisCssLengthUnit = yAxisCssLengthUnit = cssLengthUnit;

        cssValue = String.valueOf(xyAxis) + cssLengthUnit;

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
        return CssNameConstants.PERSPECTIVE_ORIGIN;
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
     * gets the x-axis value in float value.
     * {@code PerspectiveOrigin#getXAxisUnit()} should be used to get the
     * cssLengthUnit for this value.
     *
     * @return the horizontal value in float or {@code null} if the value is any
     *         inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public Float getXAxis() {
        return xAxis;
    }

    /**
     * gets the y-axis value in float value.
     * {@code PerspectiveOrigin#getYAxisUnit()} should be used to get the
     * cssLengthUnit for this value.
     *
     * @return the vertical value in float or {@code null} if the value is any
     *         inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public Float getYAxis() {
        return yAxis;
    }

    /**
     * @return the cssLengthUnit {@code PX}/{@code PER}, or {@code null} if the
     *         value is any inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public CssLengthUnit getXAxisUnit() {
        return xAxisCssLengthUnit;
    }

    /**
     * @return the cssLengthUnit {@code PX}/{@code PER}, or {@code null} if the
     *         value is any inbuilt value like {@code inherit}.
     * @since 1.0.0
     * @author WFF
     */
    public CssLengthUnit getYAxisUnit() {
        return yAxisCssLengthUnit;
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
    public PerspectiveOrigin setCssValue(final String cssValue) {
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
                        xAxis = null;
                        yAxis = null;
                        xAxisCssLengthUnit = null;
                        yAxisCssLengthUnit = null;
                        invalidValue = false;
                    } else {
                        final Object[] lengthValueAndUnitHorizontalVertical = CssLengthUtil
                                .getLengthValueAndUnit(lengthValues[0]);

                        if (lengthValueAndUnitHorizontalVertical.length == 2) {
                            xAxis = yAxis = (Float) lengthValueAndUnitHorizontalVertical[0];
                            xAxisCssLengthUnit = yAxisCssLengthUnit = (CssLengthUnit) lengthValueAndUnitHorizontalVertical[1];
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
                        xAxis = (Float) lengthValueAndUnitHorizontal[0];
                        xAxisCssLengthUnit = (CssLengthUnit) lengthValueAndUnitHorizontal[1];
                        yAxis = (Float) lengthValueAndUnitVertical[0];
                        yAxisCssLengthUnit = (CssLengthUnit) lengthValueAndUnitVertical[1];

                        // nano optimized way to check if both xAxis
                        // and yAxis are equal.
                        if (Float.floatToIntBits(xAxis.floatValue()) == Float
                                .floatToIntBits(xAxis.floatValue())
                                && Objects.equals(xAxisCssLengthUnit,
                                        yAxisCssLengthUnit)) {
                            this.cssValue = String.valueOf(xAxis)
                                    + yAxisCssLengthUnit;
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
