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
 * font-size: medium | xx-small | x-small | small | large | x-large | xx-large | smaller | larger | <i>length</i> | initial | inherit;
 *
 * Initial:        medium
 * Applies to:     all elements
 * Inherited:      yes
 * Percentages:    refer to inherited font size
 * Media:          visual
 * Computed value:         absolute length
 *
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class FontSize extends AbstractCssProperty<FontSize> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String MEDIUM = "medium";
    public static final String XX_SMALL = "xx-small";
    public static final String X_SMALL = "x-small";
    public static final String SMALL = "small";
    public static final String LARGE = "large";
    public static final String X_LARGE = "x-large";
    public static final String XX_LARGE = "xx-large";
    public static final String SMALLER = "smaller";
    public static final String LARGER = "larger";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays.asList(
            INITIAL, INHERIT, MEDIUM, XX_SMALL, X_SMALL, SMALL, LARGE, X_LARGE,
            XX_LARGE, SMALLER, LARGER);

    private String cssValue;
    private Float value;
    private CssLengthUnit cssLengthUnit;

    /**
     * The {@code auto} will be set as the value
     */
    public FontSize() {
        setCssValue(MEDIUM);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public FontSize(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param fontSizeCss
     *            the {@code FontSize} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public FontSize(final FontSize fontSizeCss) {
        if (fontSizeCss == null) {
            throw new NullValueException("fontSizeCss can not be null");
        }
        setCssValue(fontSizeCss.getCssValue());
    }

    /**
     * @param percent
     *            the percentage value to set. The cssLengthUnit will
     *            automatically set to %.
     * @since 1.0.0
     * @author WFF
     */
    public FontSize(final float percent) {
        cssLengthUnit = CssLengthUnit.PER;
        value = percent;
        cssValue = String.valueOf(percent) + cssLengthUnit;
    }

    /**
     * @param value
     * @param cssLengthUnit
     */
    public FontSize(final float value, final CssLengthUnit cssLengthUnit) {
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
    public FontSize setValue(final float value,
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
        return CssNameConstants.FONT_SIZE;
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
     * gets the fontSize in float value. {@code FontSize#getUnit()} should be
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
    public FontSize setCssValue(final String cssValue) {
        try {
            if (cssValue == null) {
                throw new NullValueException(
                        "null is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit.");
            } else {
                final String trimmedCssValue = TagStringUtil
                        .toLowerCase(StringUtil.strip(cssValue));
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
                if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                    this.cssValue = trimmedCssValue;
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
     * sets as {@code medium}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsMedium() {
        setCssValue(MEDIUM);
    }

    /**
     * sets as {@code xx-small}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsXxSmall() {
        setCssValue(XX_SMALL);
    }

    /**
     * sets as {@code x-small}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsXSmall() {
        setCssValue(X_SMALL);
    }

    /**
     * sets as {@code small}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsSmall() {
        setCssValue(SMALL);
    }

    /**
     * sets as {@code large}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsLarge() {
        setCssValue(LARGE);
    }

    /**
     * sets as {@code x-large}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsXLarge() {
        setCssValue(X_LARGE);
    }

    /**
     * sets as {@code xx-large}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsXxLarge() {
        setCssValue(XX_LARGE);
    }

    /**
     * sets as {@code smaller}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsSmaller() {
        setCssValue(SMALLER);
    }

    /**
     * sets as {@code larger}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsLarger() {
        setCssValue(LARGER);
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
