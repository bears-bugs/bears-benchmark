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
 *
 * @author WFF
 * @since 1.0.0
 */
public class WidthCss extends AbstractCssProperty<WidthCss> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String AUTO = "auto";
    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String FIT_CONTENT = "fit-content";
    public static final String AVAILABLE = "available";
    public static final String MIN_CONTENT = "min-content";
    public static final String MAX_CONTENT = "max-content";
    public static final String FILL = "fill";
    public static final String CONTENT_BOX = "content-box";
    public static final String BORDER_BOX = "border-box";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays.asList(
            INITIAL, INHERIT, AUTO, FIT_CONTENT, AVAILABLE, MIN_CONTENT,
            MAX_CONTENT, FILL, CONTENT_BOX, BORDER_BOX);

    private String cssValue;
    private Float value;
    private CssLengthUnit cssLengthUnit;

    /**
     * The {@code auto} will be set as the value
     */
    public WidthCss() {
        setCssValue(AUTO);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public WidthCss(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param widthCss
     *            the {@code WidthCss} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public WidthCss(final WidthCss widthCss) {
        if (widthCss == null) {
            throw new NullValueException("widthCss can not be null");
        }
        setCssValue(widthCss.getCssValue());
    }

    /**
     * @param percent
     *            the percentage value to set. The cssLengthUnit will
     *            automatically set to %.
     * @since 1.0.0
     * @author WFF
     */
    public WidthCss(final float percent) {
        cssLengthUnit = CssLengthUnit.PER;
        value = percent;
        cssValue = String.valueOf(percent) + cssLengthUnit;
    }

    /**
     * @param value
     * @param cssLengthUnit
     */
    public WidthCss(final float value, final CssLengthUnit cssLengthUnit) {
        this.value = value;
        this.cssLengthUnit = cssLengthUnit;
        cssValue = String.valueOf(value) + cssLengthUnit;
    }

    /**
     * @param value
     * @param cssLengthUnit
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public WidthCss setValue(final float value,
            final CssLengthUnit cssLengthUnit) {
        this.value = value;
        this.cssLengthUnit = cssLengthUnit;
        cssValue = String.valueOf(value) + cssLengthUnit;
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
        return CssNameConstants.WIDTH;
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
     * gets the width in float value. {@code WidthCss#getUnit()} should be used
     * to get the cssLengthUnit for this value.
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
    public WidthCss setCssValue(final String cssValue) {
        try {
            if (cssValue == null) {
                throw new NullValueException(
                        "null is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit.");
            } else {
                final String trimmedCssValue = StringUtil.strip(cssValue);
                boolean invalidValue = true;
                for (final CssLengthUnit cssLengthUnit : CssLengthUnit
                        .values()) {
                    final String unit = cssLengthUnit.getUnit();
                    if (trimmedCssValue.endsWith(unit)) {
                        final String valueOnly = trimmedCssValue
                                .replaceFirst(unit, "");
                        try {
                            value = Float.parseFloat(valueOnly);
                        } catch (final NumberFormatException e) {
                            break;
                        }
                        this.cssLengthUnit = cssLengthUnit;
                        this.cssValue = cssValue;
                        invalidValue = false;
                        break;
                    }
                }
                if (PREDEFINED_CONSTANTS
                        .contains(TagStringUtil.toLowerCase(trimmedCssValue))) {
                    this.cssValue = trimmedCssValue.toLowerCase();
                    cssLengthUnit = null;
                    value = null;
                    invalidValue = false;
                }
                if (invalidValue) {
                    throw new InvalidValueException(cssValue
                            + " is an invalid value. The value format should be as for example 75px, 85%, initial, inherit etc..");
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
     * sets as {@code auto}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsAuto() {
        setCssValue(AUTO);
    }

    /**
     * sets as {@code available}.
     *
     * @since 2.1.14
     * @author WFF
     */
    public void setAsAvailable() {
        setCssValue(AVAILABLE);
    }

    /**
     * sets as {@code min-content}.
     *
     * @since 2.1.14
     * @author WFF
     */
    public void setAsMinContent() {
        setCssValue(MIN_CONTENT);
    }

    /**
     * sets as {@code max-content}.
     *
     * @since 2.1.14
     * @author WFF
     */
    public void setAsMaxContent() {
        setCssValue(MAX_CONTENT);
    }

    /**
     * sets as {@code fill}.
     *
     * @since 2.1.14
     * @author WFF
     */
    public void setAsFill() {
        setCssValue(FILL);
    }

    /**
     * sets as {@code content-box}.
     *
     * @since 2.1.14
     * @author WFF
     */
    public void setAsContentBox() {
        setCssValue(CONTENT_BOX);
    }

    /**
     * sets as {@code border-box}.
     *
     * @since 2.1.14
     * @author WFF
     */
    public void setAsBorderBox() {
        setCssValue(BORDER_BOX);
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

        for (final CssLengthUnit cssLengthUnit : CssLengthUnit.values()) {
            final String unit = cssLengthUnit.getUnit();
            if (trimmedCssValue.endsWith(unit)) {
                final String valueOnly = trimmedCssValue.replaceFirst(unit, "");
                try {
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
