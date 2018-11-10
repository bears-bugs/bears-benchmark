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
 * The border-top shorthand property sets all the top border properties in one declaration.
 *
 * The properties that can be set, are (in order): border-top-width, border-top-style, and border-top-color.
 *
 * If one of the values above are missing, e.g. border-top:solid #ff0000, the default value for the missing property will be inserted, if any.
 * Default value:  medium none color
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS1
 * JavaScript syntax:      object.style.borderTop="3px dashed blue"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderTop extends AbstractCssProperty<BorderTop>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(BorderTop.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private BorderTopWidth borderTopWidth;

    private BorderTopStyle borderTopStyle;

    private BorderTopColor borderTopColor;

    /**
     * The value <code>medium none black</code> will be assigned as the
     * cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public BorderTop() {
        setCssValue("medium none black");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderTop(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderTop
     *            the {@code BorderTop} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderTop(final BorderTop borderTop) {
        if (borderTop == null) {
            throw new NullValueException("borderTop can not be null");
        }
        setCssValue(borderTop.getCssValue());
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
        return CssNameConstants.BORDER_TOP;
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
    public BorderTop setCssValue(final String cssValue) {
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
            if (trimmedCssValue.contains(BorderTopColor.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(BorderTopColor.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {

                if (borderTopWidth != null) {
                    borderTopWidth.setAlreadyInUse(false);
                    borderTopWidth = null;
                }

                borderTopStyle = null;

                if (borderTopColor != null) {
                    borderTopColor.setAlreadyInUse(false);
                    borderTopColor = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        BorderTopWidth borderTopWidth = null;
        BorderTopStyle borderTopStyle = null;
        BorderTopColor borderTopColor = null;

        for (final String eachPart : cssValueParts) {
            if (borderTopWidth == null && BorderTopWidth.isValid(eachPart)) {
                if (this.borderTopWidth == null) {
                    borderTopWidth = new BorderTopWidth(eachPart);
                    borderTopWidth.setStateChangeInformer(this);
                    borderTopWidth.setAlreadyInUse(true);
                } else {
                    this.borderTopWidth.setCssValue(eachPart);
                    borderTopWidth = this.borderTopWidth;
                }
            } else if (borderTopStyle == null
                    && BorderTopStyle.isValid(eachPart)) {
                borderTopStyle = BorderTopStyle.getThis(eachPart);
            } else if (borderTopColor == null
                    && BorderTopColor.isValid(eachPart)) {
                if (this.borderTopColor == null) {
                    borderTopColor = new BorderTopColor(eachPart);
                    borderTopColor.setStateChangeInformer(this);
                    borderTopColor.setAlreadyInUse(true);
                } else {
                    this.borderTopColor.setCssValue(eachPart);
                    borderTopColor = this.borderTopColor;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (borderTopWidth != null) {
            cssValueBuilder.append(borderTopWidth.getCssValue()).append(' ');
            invalid = false;
        } else if (this.borderTopWidth != null) {
            this.borderTopWidth.setAlreadyInUse(false);
        }
        if (borderTopStyle != null) {
            cssValueBuilder.append(borderTopStyle.getCssValue()).append(' ');
            invalid = false;
        }
        if (borderTopColor != null) {
            cssValueBuilder.append(borderTopColor.getCssValue()).append(' ');
            invalid = false;
        } else if (this.borderTopColor != null) {
            this.borderTopColor.setAlreadyInUse(false);
        }
        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example medium none color Or initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.borderTopWidth = borderTopWidth;
        this.borderTopStyle = borderTopStyle;
        this.borderTopColor = borderTopColor;

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

        BorderTopWidth borderTopWidth = null;
        BorderTopStyle borderTopStyle = null;
        BorderTopColor borderTopColor = null;

        for (final String eachPart : cssValueParts) {

            boolean invalid = true;
            if (borderTopWidth == null && BorderTopWidth.isValid(eachPart)) {
                borderTopWidth = new BorderTopWidth(eachPart);
                invalid = false;
            } else if (borderTopStyle == null
                    && BorderTopStyle.isValid(eachPart)) {
                borderTopStyle = BorderTopStyle.getThis(eachPart);
                invalid = false;
            } else if (borderTopColor == null
                    && BorderTopColor.isValid(eachPart)) {
                borderTopColor = new BorderTopColor(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return borderTopWidth != null || borderTopStyle != null
                || borderTopColor != null;
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

    public BorderTopColor getBorderTopColor() {
        return borderTopColor;
    }

    public BorderTopStyle getBorderTopStyle() {
        return borderTopStyle;
    }

    public BorderTopWidth getBorderTopWidth() {
        return borderTopWidth;
    }

    public BorderTop setBorderTopWidth(final BorderTopWidth borderTopWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderTopWidth != null) {

            final String borderTopWidthCssValue = borderTopWidth.getCssValue();
            if (BorderTopWidth.INITIAL.equals(borderTopWidthCssValue)
                    || BorderTopWidth.INHERIT.equals(borderTopWidthCssValue)) {
                throw new InvalidValueException(
                        "borderTopWidth cannot have initial/inherit as its cssValue");
            }

            cssValueBuilder.append(borderTopWidth.getCssValue()).append(' ');
        }

        if (borderTopStyle != null) {
            cssValueBuilder.append(borderTopStyle.getCssValue()).append(' ');
        }

        if (borderTopColor != null) {
            cssValueBuilder.append(borderTopColor.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderTopWidth != null && borderTopWidth.isAlreadyInUse()
                && !Objects.equals(this.borderTopWidth, borderTopWidth)) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderTopWidth is already used by another object so a new object or the previous object (if it exists) of BorderTopWidth will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderTopWidth != null) {
            this.borderTopWidth.setAlreadyInUse(false);
        }

        this.borderTopWidth = borderTopWidth;

        if (this.borderTopWidth != null) {
            this.borderTopWidth.setStateChangeInformer(this);
            this.borderTopWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public BorderTop setBorderTopStyle(final BorderTopStyle borderTopStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderTopWidth != null) {
            cssValueBuilder.append(borderTopWidth.getCssValue()).append(' ');
        }

        if (borderTopStyle != null) {
            cssValueBuilder.append(borderTopStyle.getCssValue()).append(' ');
        }

        if (borderTopColor != null) {
            cssValueBuilder.append(borderTopColor.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.borderTopStyle = borderTopStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public BorderTop setBorderTopColor(final BorderTopColor borderTopColor) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderTopWidth != null) {
            cssValueBuilder.append(borderTopWidth.getCssValue()).append(' ');
        }

        if (borderTopStyle != null) {
            cssValueBuilder.append(borderTopStyle.getCssValue()).append(' ');
        }

        if (borderTopColor != null) {
            final String borderTopColorCssValue = borderTopColor.getCssValue();
            if (BorderTopColor.INITIAL.equals(borderTopColorCssValue)
                    || BorderTopColor.INHERIT.equals(borderTopColorCssValue)) {
                throw new InvalidValueException(
                        "borderTopColor cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(borderTopColorCssValue);
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderTopColor != null && borderTopColor.isAlreadyInUse()
                && !Objects.equals(this.borderTopColor, borderTopColor)) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderTopColor is already used by another object so a new object or the previous object (if it exists) of BorderTopColor will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderTopColor != null) {
            this.borderTopColor.setAlreadyInUse(false);
        }

        this.borderTopColor = borderTopColor;

        if (this.borderTopColor != null) {
            this.borderTopColor.setStateChangeInformer(this);
            this.borderTopColor.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof BorderTopColor) {

            final BorderTopColor borderTopColor = (BorderTopColor) stateChangedObject;

            final String cssValue = borderTopColor.getCssValue();

            if (BorderTopColor.INITIAL.equals(cssValue)
                    || BorderTopColor.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as borderTopColor cssValue");
            }

            setBorderTopColor(borderTopColor);
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
