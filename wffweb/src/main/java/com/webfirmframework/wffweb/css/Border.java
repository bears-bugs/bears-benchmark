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

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.data.Bean;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.CssValueUtil;
import com.webfirmframework.wffweb.util.StringBuilderUtil;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * <pre>
 * border: border-width border-style border-color|initial|inherit;
 *
 * The border shorthand property sets all the border properties in one
 * declaration.
 *
 * The properties that can be set, are (in order): border-width, border-style,
 * and border-color.
 *
 * It does not matter if one of the values above are missing, e.g. border:solid
 * #ff0000; is allowed.
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class Border extends AbstractCssProperty<Border>
        implements StateChangeInformer<Bean> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(Border.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private String cssValue;

    private String borderWidthValue;

    private BorderStyle borderStyle;

    private BorderColorCssValues borderColorCssValues;

    /**
     * The value <code>medium none #000000</code> will be assigned as the
     * cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public Border() {
        setCssValue("medium none #000000");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public Border(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param border
     *            the {@code border} object from which the cssValue to set.And,
     *            {@code null} will throw {@code NullValueException}
     */
    public Border(final Border border) {
        if (border == null) {
            throw new NullValueException("border can not be null");
        }
        setCssValue(border.getCssValue());
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
        return CssNameConstants.BORDER;
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
    public Border setCssValue(final String cssValue) {
        final String trimmedCssValue;
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example cjk-ideographic inside url(\"Sqpurple.gif\"). Or, initial/inherit.");
        } else if ((trimmedCssValue = StringUtil.strip(cssValue)).isEmpty()) {
            throw new NullValueException(cssValue
                    + " is an invalid value. The value format should be as for example cjk-ideographic inside url(\"Sqpurple.gif\"). Or, initial/inherit.");
        }

        final List<String> cssValueParts = CssValueUtil.split(trimmedCssValue);

        if (cssValueParts.size() > 1) {
            if (trimmedCssValue.contains(INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else if (INITIAL.equals(trimmedCssValue)
                || INHERIT.equals(trimmedCssValue)) {
            this.cssValue = trimmedCssValue;
            if (borderColorCssValues != null) {
                borderColorCssValues.setAlreadyInUse(false);
                borderColorCssValues = null;
            }
            borderWidthValue = null;
            borderStyle = null;

            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }

            return this;
        }

        String borderWidthValue = null;
        BorderStyle borderStyle = null;
        BorderColorCssValues borderColorCssValues = null;

        for (final String eachPart : cssValueParts) {
            if (borderWidthValue == null && BorderTopWidth.isValid(eachPart)) {
                borderWidthValue = new BorderTopWidth(eachPart).getCssValue();
            } else if (borderStyle == null && BorderStyle.isValid(eachPart)) {
                borderStyle = BorderStyle.getThis(eachPart);
            } else if (borderColorCssValues == null
                    && BorderColorCssValues.isValid(eachPart)) {
                if (this.borderColorCssValues == null) {
                    borderColorCssValues = new BorderColorCssValues(eachPart);
                    borderColorCssValues.setStateChangeInformer(this);
                } else {
                    this.borderColorCssValues.setValue(eachPart);
                    borderColorCssValues = this.borderColorCssValues;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (borderWidthValue != null) {
            cssValueBuilder.append(borderWidthValue).append(' ');
            invalid = false;
        }
        if (borderStyle != null) {
            cssValueBuilder.append(borderStyle.getCssValue()).append(' ');
            invalid = false;
        }
        if (borderColorCssValues != null) {
            cssValueBuilder.append(borderColorCssValues.getValue()).append(' ');
            invalid = false;
        }
        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example cjk-ideographic inside url(\"Sqpurple.gif\"). Or, initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.borderWidthValue = borderWidthValue;
        this.borderStyle = borderStyle;
        this.borderColorCssValues = borderColorCssValues;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    /**
     * checks the given css value is valid for this class. It does't do a strict
     * validation.
     *
     * @param cssValue
     * @return
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String cssValue) {
        // TODO modify to make a strict validation
        final String trimmedCssValue;
        if (cssValue == null
                || (trimmedCssValue = StringUtil.strip(cssValue)).isEmpty()) {
            return false;
        }

        if (INITIAL.equals(trimmedCssValue)
                || INHERIT.equals(trimmedCssValue)) {
            return true;
        }

        final List<String> cssValueParts = CssValueUtil.split(trimmedCssValue);

        if (cssValueParts.size() > 1 && (trimmedCssValue.contains(INITIAL)
                || trimmedCssValue.contains(INHERIT))) {
            return false;
        }

        BorderTopWidth width = null;
        BorderStyle borderStyle = null;
        BorderColorCssValues borderColorCssValues = null;

        for (final String eachPart : cssValueParts) {
            boolean invalid = true;
            if (width == null && BorderTopWidth.isValid(eachPart)) {
                width = new BorderTopWidth(eachPart);
                invalid = false;
            } else if (borderStyle == null && BorderStyle.isValid(eachPart)) {
                borderStyle = BorderStyle.getThis(eachPart);
                invalid = false;
            } else if (borderColorCssValues == null
                    && BorderColorCssValues.isValid(eachPart)) {
                borderColorCssValues = new BorderColorCssValues(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return true;
    }

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
     * @return the {@code BorderStyle} object or null if there is not.
     * @author WFF
     * @since 1.0.0
     */
    public BorderColorCssValues getBorderColorCssValues() {
        return borderColorCssValues;
    }

    /**
     * @return the {@code BorderStyle} object or null if there is not.
     * @author WFF
     * @since 1.0.0
     */
    public BorderStyle getBorderStyle() {
        return borderStyle;
    }

    /**
     * @param borderWidthValue
     * @return the current object..
     * @author WFF
     * @since 1.0.0
     */
    public Border setBorderWidthValue(final String borderWidthValue) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderWidthValue != null) {
            if (INITIAL.equals(borderWidthValue)
                    || INHERIT.equals(borderWidthValue)
                    || !BorderTopWidth.isValid(borderWidthValue)) {
                throw new InvalidValueException("The given value '"
                        + borderWidthValue
                        + "' is not valid, please try with a valid value like medium/thin/thick or 75px");
            }
            cssValueBuilder.append(borderWidthValue).append(' ');
        }
        if (borderStyle != null) {
            cssValueBuilder.append(borderStyle.getCssValue()).append(' ');
        }

        if (borderColorCssValues != null) {
            cssValueBuilder.append(borderColorCssValues.getValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.borderWidthValue = borderWidthValue;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    /**
     * to set the border style.
     *
     * @param borderStyle
     * @return the current object.
     * @author WFF
     * @since 1.0.0
     */
    public Border setBorderStyle(final BorderStyle borderStyle) {

        if (borderStyle == BorderStyle.INITIAL
                || borderStyle == BorderStyle.INHERIT) {
            throw new InvalidValueException("the given borderStyle cannot be "
                    + borderStyle.getCssValue());
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderWidthValue != null) {
            cssValueBuilder.append(borderWidthValue).append(' ');
        }

        if (borderStyle != null) {
            cssValueBuilder.append(borderStyle.getCssValue()).append(' ');
        }

        if (borderColorCssValues != null) {
            cssValueBuilder.append(borderColorCssValues.getValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.borderStyle = borderStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    /**
     * @param borderColorCssValues
     *            to set the border color.
     * @return the current object
     * @author WFF
     * @since 1.0.0
     */
    public Border setBorderColorCssValues(
            final BorderColorCssValues borderColorCssValues) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderWidthValue != null) {
            cssValueBuilder.append(borderWidthValue).append(' ');
        }

        if (borderStyle != null) {
            cssValueBuilder.append(borderStyle.getCssValue()).append(' ');
        }

        if (borderColorCssValues != null) {
            final String borderColorCssValuesCssValue = borderColorCssValues
                    .getValue();
            cssValueBuilder.append(borderColorCssValuesCssValue);
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderColorCssValues != null
                && borderColorCssValues.isAlreadyInUse()
                && !Objects.equals(this.borderColorCssValues,
                        borderColorCssValues)) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderColorCssValues is already used by another object so a new object or the previous object (if it exists) of BorderColorCssValues will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderColorCssValues != null) {
            this.borderColorCssValues.setAlreadyInUse(false);
        }

        this.borderColorCssValues = borderColorCssValues;

        if (this.borderColorCssValues != null) {
            this.borderColorCssValues.setStateChangeInformer(this);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    /**
     * @return the borderWidthValue
     * @author WFF
     * @since 1.0.0
     */
    public String getBorderWidthValue() {
        return borderWidthValue;
    }

    @Override
    public void stateChanged(final Bean stateChangedObject) {
        if (stateChangedObject instanceof BorderColorCssValues) {
            setBorderColorCssValues((BorderColorCssValues) stateChangedObject);
        }
    }

}
