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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.CssValueUtil;
import com.webfirmframework.wffweb.util.StringBuilderUtil;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * The border-right shorthand property sets all the right border properties in one declaration.
 *
 * The properties that can be set, are (in order): border-right-width, border-right-style, and border-right-color.
 *
 * If one of the values above are missing, e.g. border-right:solid #ff0000, the default value for the missing property will be inserted, if any.
 * Default value:  medium none color
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS1
 * JavaScript syntax:      object.style.borderRight="3px dashed blue"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderRight extends AbstractCssProperty<BorderRight>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(BorderRight.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private BorderRightWidth borderRightWidth;

    private BorderRightStyle borderRightStyle;

    private BorderRightColor borderRightColor;

    /**
     * The value <code>medium none black</code> will be assigned as the
     * cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public BorderRight() {
        setCssValue("medium none black");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderRight(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderRight
     *            the {@code BorderRight} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderRight(final BorderRight borderRight) {
        if (borderRight == null) {
            throw new NullValueException("borderRight can not be null");
        }
        setCssValue(borderRight.getCssValue());
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
        return CssNameConstants.BORDER_RIGHT;
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
     *            the value should be in the format of
     *            <code>medium none color</code>, <code>initial</code> or
     *            <code>inherit</code>. {@code null} is considered as an invalid
     *            value and it will throw {@code NullValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public BorderRight setCssValue(final String cssValue) {
        final String trimmedCssValue;
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example medium none color Or initial/inherit.");
        } else if ((trimmedCssValue = TagStringUtil
                .toLowerCase(StringUtil.strip(cssValue))).isEmpty()) {
            throw new NullValueException(cssValue
                    + " is an invalid value. The value format should be as for example medium none color Or initial/inherit.");
        }

        final List<String> cssValueParts = CssValueUtil.split(trimmedCssValue);

        if (cssValueParts.size() > 1) {
            if (trimmedCssValue.contains(BorderRightColor.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(BorderRightColor.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {

                if (borderRightWidth != null) {
                    borderRightWidth.setAlreadyInUse(false);
                    borderRightWidth = null;
                }

                borderRightStyle = null;

                if (borderRightColor != null) {
                    borderRightColor.setAlreadyInUse(false);
                    borderRightColor = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        BorderRightWidth borderRightWidth = null;
        BorderRightStyle borderRightStyle = null;
        BorderRightColor borderRightColor = null;

        for (final String eachPart : cssValueParts) {
            if (borderRightWidth == null
                    && BorderRightWidth.isValid(eachPart)) {
                if (this.borderRightWidth == null) {
                    borderRightWidth = new BorderRightWidth(eachPart);
                    borderRightWidth.setStateChangeInformer(this);
                    borderRightWidth.setAlreadyInUse(true);
                } else {
                    this.borderRightWidth.setCssValue(eachPart);
                    borderRightWidth = this.borderRightWidth;
                }
            } else if (borderRightStyle == null
                    && BorderRightStyle.isValid(eachPart)) {
                borderRightStyle = BorderRightStyle.getThis(eachPart);
            } else if (borderRightColor == null
                    && BorderRightColor.isValid(eachPart)) {
                if (this.borderRightColor == null) {
                    borderRightColor = new BorderRightColor(eachPart);
                    borderRightColor.setStateChangeInformer(this);
                    borderRightColor.setAlreadyInUse(true);
                } else {
                    this.borderRightColor.setCssValue(eachPart);
                    borderRightColor = this.borderRightColor;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (borderRightWidth != null) {
            cssValueBuilder.append(borderRightWidth.getCssValue()).append(' ');
            invalid = false;
        } else if (this.borderRightWidth != null) {
            this.borderRightWidth.setAlreadyInUse(false);
        }
        if (borderRightStyle != null) {
            cssValueBuilder.append(borderRightStyle.getCssValue()).append(' ');
            invalid = false;
        }
        if (borderRightColor != null) {
            cssValueBuilder.append(borderRightColor.getCssValue()).append(' ');
            invalid = false;
        } else if (this.borderRightColor != null) {
            this.borderRightColor.setAlreadyInUse(false);
        }
        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example medium none color Or initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.borderRightWidth = borderRightWidth;
        this.borderRightStyle = borderRightStyle;
        this.borderRightColor = borderRightColor;

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
        if (cssValue == null || StringUtil.isBlank(cssValue)) {
            return false;
        }

        final List<String> cssValueParts = CssValueUtil.split(cssValue);

        BorderRightWidth borderRightWidth = null;
        BorderRightStyle borderRightStyle = null;
        BorderRightColor borderRightColor = null;

        for (final String eachPart : cssValueParts) {

            boolean invalid = true;
            if (borderRightWidth == null
                    && BorderRightWidth.isValid(eachPart)) {
                borderRightWidth = new BorderRightWidth(eachPart);
                invalid = false;
            } else if (borderRightStyle == null
                    && BorderRightStyle.isValid(eachPart)) {
                borderRightStyle = BorderRightStyle.getThis(eachPart);
                invalid = false;
            } else if (borderRightColor == null
                    && BorderRightColor.isValid(eachPart)) {
                borderRightColor = new BorderRightColor(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return borderRightWidth != null || borderRightStyle != null
                || borderRightColor != null;
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

    public BorderRightColor getBorderRightColor() {
        return borderRightColor;
    }

    public BorderRightStyle getBorderRightStyle() {
        return borderRightStyle;
    }

    public BorderRightWidth getBorderRightWidth() {
        return borderRightWidth;
    }

    public BorderRight setBorderRightWidth(
            final BorderRightWidth borderRightWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderRightWidth != null) {

            final String borderRightWidthCssValue = borderRightWidth
                    .getCssValue();
            if (BorderRightWidth.INITIAL.equals(borderRightWidthCssValue)
                    || BorderRightWidth.INHERIT
                            .equals(borderRightWidthCssValue)) {
                throw new InvalidValueException(
                        "borderRightWidth cannot have initial/inherit as its cssValue");
            }

            cssValueBuilder.append(borderRightWidth.getCssValue()).append(' ');
        }

        if (borderRightStyle != null) {
            cssValueBuilder.append(borderRightStyle.getCssValue()).append(' ');
        }

        if (borderRightColor != null) {
            cssValueBuilder.append(borderRightColor.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderRightWidth != null && borderRightWidth.isAlreadyInUse()
                && !Objects.equals(this.borderRightWidth, borderRightWidth)) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderRightWidth is already used by another object so a new object or the previous object (if it exists) of BorderRightWidth will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderRightWidth != null) {
            this.borderRightWidth.setAlreadyInUse(false);
        }

        this.borderRightWidth = borderRightWidth;

        if (this.borderRightWidth != null) {
            this.borderRightWidth.setStateChangeInformer(this);
            this.borderRightWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public BorderRight setBorderRightStyle(
            final BorderRightStyle borderRightStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderRightWidth != null) {
            cssValueBuilder.append(borderRightWidth.getCssValue()).append(' ');
        }

        if (borderRightStyle != null) {
            cssValueBuilder.append(borderRightStyle.getCssValue()).append(' ');
        }

        if (borderRightColor != null) {
            cssValueBuilder.append(borderRightColor.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.borderRightStyle = borderRightStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public BorderRight setBorderRightColor(
            final BorderRightColor borderRightColor) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderRightWidth != null) {
            cssValueBuilder.append(borderRightWidth.getCssValue()).append(' ');
        }

        if (borderRightStyle != null) {
            cssValueBuilder.append(borderRightStyle.getCssValue()).append(' ');
        }

        if (borderRightColor != null) {
            final String borderRightColorCssValue = borderRightColor
                    .getCssValue();
            if (BorderRightColor.INITIAL.equals(borderRightColorCssValue)
                    || BorderRightColor.INHERIT
                            .equals(borderRightColorCssValue)) {
                throw new InvalidValueException(
                        "borderRightColor cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(borderRightColorCssValue);
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderRightColor != null && borderRightColor.isAlreadyInUse()
                && this.borderRightColor != borderRightColor) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderRightColor is already used by another object so a new object or the previous object (if it exists) of BorderRightColor will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderRightColor != null) {
            this.borderRightColor.setAlreadyInUse(false);
        }

        this.borderRightColor = borderRightColor;

        if (this.borderRightColor != null) {
            this.borderRightColor.setStateChangeInformer(this);
            this.borderRightColor.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof BorderRightColor) {

            final BorderRightColor borderRightColor = (BorderRightColor) stateChangedObject;

            final String cssValue = borderRightColor.getCssValue();

            if (BorderRightColor.INITIAL.equals(cssValue)
                    || BorderRightColor.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as borderRightColor cssValue");
            }

            setBorderRightColor(borderRightColor);
        }
    }

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    protected void addPredefinedConstant(final String constant) {
        PREDEFINED_CONSTANTS.add(constant);
    }

}
