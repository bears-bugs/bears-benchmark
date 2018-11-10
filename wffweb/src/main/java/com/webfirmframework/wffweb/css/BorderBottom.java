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
 * The border-bottom shorthand property sets all the bottom border properties in one declaration.
 *
 * The properties that can be set, are (in order): border-bottom-width, border-bottom-style, and border-bottom-color.
 *
 * If one of the values above are missing, e.g. border-bottom:solid #ff0000, the default value for the missing property will be inserted, if any.
 * Default value:  medium none color
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS1
 * JavaScript syntax:      object.style.borderBottom="3px dashed blue"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderBottom extends AbstractCssProperty<BorderBottom>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(BorderBottom.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private BorderBottomWidth borderBottomWidth;

    private BorderBottomStyle borderBottomStyle;

    private BorderBottomColor borderBottomColor;

    /**
     * The value <code>medium none black</code> will be assigned as the
     * cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public BorderBottom() {
        setCssValue("medium none black");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderBottom(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderBottom
     *            the {@code BorderBottom} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderBottom(final BorderBottom borderBottom) {
        if (borderBottom == null) {
            throw new NullValueException("borderBottom can not be null");
        }
        setCssValue(borderBottom.getCssValue());
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
        return CssNameConstants.BORDER_BOTTOM;
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
    public BorderBottom setCssValue(final String cssValue) {
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
            if (trimmedCssValue.contains(BorderBottomColor.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(BorderBottomColor.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {

                if (borderBottomWidth != null) {
                    borderBottomWidth.setAlreadyInUse(false);
                    borderBottomWidth = null;
                }

                borderBottomStyle = null;

                if (borderBottomColor != null) {
                    borderBottomColor.setAlreadyInUse(false);
                    borderBottomColor = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        BorderBottomWidth borderBottomWidth = null;
        BorderBottomStyle borderBottomStyle = null;
        BorderBottomColor borderBottomColor = null;

        for (final String eachPart : cssValueParts) {
            if (borderBottomWidth == null
                    && BorderBottomWidth.isValid(eachPart)) {
                if (this.borderBottomWidth == null) {
                    borderBottomWidth = new BorderBottomWidth(eachPart);
                    borderBottomWidth.setStateChangeInformer(this);
                    borderBottomWidth.setAlreadyInUse(true);
                } else {
                    this.borderBottomWidth.setCssValue(eachPart);
                    borderBottomWidth = this.borderBottomWidth;
                }
            } else if (borderBottomStyle == null
                    && BorderBottomStyle.isValid(eachPart)) {
                borderBottomStyle = BorderBottomStyle.getThis(eachPart);
            } else if (borderBottomColor == null
                    && BorderBottomColor.isValid(eachPart)) {
                if (this.borderBottomColor == null) {
                    borderBottomColor = new BorderBottomColor(eachPart);
                    borderBottomColor.setStateChangeInformer(this);
                    borderBottomColor.setAlreadyInUse(true);
                } else {
                    this.borderBottomColor.setCssValue(eachPart);
                    borderBottomColor = this.borderBottomColor;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (borderBottomWidth != null) {
            cssValueBuilder.append(borderBottomWidth.getCssValue()).append(' ');
            invalid = false;
        } else if (this.borderBottomWidth != null) {
            this.borderBottomWidth.setAlreadyInUse(false);
        }
        if (borderBottomStyle != null) {
            cssValueBuilder.append(borderBottomStyle.getCssValue()).append(' ');
            invalid = false;
        }
        if (borderBottomColor != null) {
            cssValueBuilder.append(borderBottomColor.getCssValue()).append(' ');
            invalid = false;
        } else if (this.borderBottomColor != null) {
            this.borderBottomColor.setAlreadyInUse(false);
        }
        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example medium none color Or initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.borderBottomWidth = borderBottomWidth;
        this.borderBottomStyle = borderBottomStyle;
        this.borderBottomColor = borderBottomColor;

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

        BorderBottomWidth borderBottomWidth = null;
        BorderBottomStyle borderBottomStyle = null;
        BorderBottomColor borderBottomColor = null;

        for (final String eachPart : cssValueParts) {

            boolean invalid = true;
            if (borderBottomWidth == null
                    && BorderBottomWidth.isValid(eachPart)) {
                borderBottomWidth = new BorderBottomWidth(eachPart);
                invalid = false;
            } else if (borderBottomStyle == null
                    && BorderBottomStyle.isValid(eachPart)) {
                borderBottomStyle = BorderBottomStyle.getThis(eachPart);
                invalid = false;
            } else if (borderBottomColor == null
                    && BorderBottomColor.isValid(eachPart)) {
                borderBottomColor = new BorderBottomColor(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return borderBottomWidth != null || borderBottomStyle != null
                || borderBottomColor != null;
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

    public BorderBottomColor getBorderBottomColor() {
        return borderBottomColor;
    }

    public BorderBottomStyle getBorderBottomStyle() {
        return borderBottomStyle;
    }

    public BorderBottomWidth getBorderBottomWidth() {
        return borderBottomWidth;
    }

    public BorderBottom setBorderBottomWidth(
            final BorderBottomWidth borderBottomWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderBottomWidth != null) {

            final String borderBottomWidthCssValue = borderBottomWidth
                    .getCssValue();
            if (BorderBottomWidth.INITIAL.equals(borderBottomWidthCssValue)
                    || BorderBottomWidth.INHERIT
                            .equals(borderBottomWidthCssValue)) {
                throw new InvalidValueException(
                        "borderBottomWidth cannot have initial/inherit as its cssValue");
            }

            cssValueBuilder.append(borderBottomWidth.getCssValue()).append(' ');
        }

        if (borderBottomStyle != null) {
            cssValueBuilder.append(borderBottomStyle.getCssValue()).append(' ');
        }

        if (borderBottomColor != null) {
            cssValueBuilder.append(borderBottomColor.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderBottomWidth != null && borderBottomWidth.isAlreadyInUse()
                && !Objects.equals(this.borderBottomWidth, borderBottomWidth)) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderBottomWidth is already used by another object so a new object or the previous object (if it exists) of BorderBottomWidth will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderBottomWidth != null) {
            this.borderBottomWidth.setAlreadyInUse(false);
        }

        this.borderBottomWidth = borderBottomWidth;

        if (this.borderBottomWidth != null) {
            this.borderBottomWidth.setStateChangeInformer(this);
            this.borderBottomWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public BorderBottom setBorderBottomStyle(
            final BorderBottomStyle borderBottomStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderBottomWidth != null) {
            cssValueBuilder.append(borderBottomWidth.getCssValue()).append(' ');
        }

        if (borderBottomStyle != null) {
            cssValueBuilder.append(borderBottomStyle.getCssValue()).append(' ');
        }

        if (borderBottomColor != null) {
            cssValueBuilder.append(borderBottomColor.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.borderBottomStyle = borderBottomStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public BorderBottom setBorderBottomColor(
            final BorderBottomColor borderBottomColor) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderBottomWidth != null) {
            cssValueBuilder.append(borderBottomWidth.getCssValue()).append(' ');
        }

        if (borderBottomStyle != null) {
            cssValueBuilder.append(borderBottomStyle.getCssValue()).append(' ');
        }

        if (borderBottomColor != null) {
            final String borderBottomColorCssValue = borderBottomColor
                    .getCssValue();
            if (BorderBottomColor.INITIAL.equals(borderBottomColorCssValue)
                    || BorderBottomColor.INHERIT
                            .equals(borderBottomColorCssValue)) {
                throw new InvalidValueException(
                        "borderBottomColor cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(borderBottomColorCssValue);
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderBottomColor != null && borderBottomColor.isAlreadyInUse()
                && !Objects.equals(this.borderBottomColor, borderBottomColor)) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderBottomColor is already used by another object so a new object or the previous object (if it exists) of BorderBottomColor will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderBottomColor != null) {
            this.borderBottomColor.setAlreadyInUse(false);
        }

        this.borderBottomColor = borderBottomColor;

        if (this.borderBottomColor != null) {
            this.borderBottomColor.setStateChangeInformer(this);
            this.borderBottomColor.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof BorderBottomColor) {

            final BorderBottomColor borderBottomColor = (BorderBottomColor) stateChangedObject;

            final String cssValue = borderBottomColor.getCssValue();

            if (BorderBottomColor.INITIAL.equals(cssValue)
                    || BorderBottomColor.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as borderBottomColor cssValue");
            }

            setBorderBottomColor(borderBottomColor);
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
