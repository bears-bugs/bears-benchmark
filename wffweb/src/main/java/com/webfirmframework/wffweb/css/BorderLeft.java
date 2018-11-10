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
 * The border-left shorthand property sets all the left border properties in one declaration.
 *
 * The properties that can be set, are (in order): border-left-width, border-left-style, and border-left-color.
 *
 * If one of the values above are missing, e.g. border-left:solid #ff0000, the default value for the missing property will be inserted, if any.
 * Default value:  medium none color
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS1
 * JavaScript syntax:      object.style.borderLeft="3px dashed blue"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderLeft extends AbstractCssProperty<BorderLeft>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(BorderLeft.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private BorderLeftWidth borderLeftWidth;

    private BorderLeftStyle borderLeftStyle;

    private BorderLeftColor borderLeftColor;

    /**
     * The value <code>medium none black</code> will be assigned as the
     * cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public BorderLeft() {
        setCssValue("medium none black");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderLeft(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderLeft
     *            the {@code BorderLeft} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderLeft(final BorderLeft borderLeft) {
        if (borderLeft == null) {
            throw new NullValueException("borderLeft can not be null");
        }
        setCssValue(borderLeft.getCssValue());
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
        return CssNameConstants.BORDER_LEFT;
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
    public BorderLeft setCssValue(final String cssValue) {
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
            if (trimmedCssValue.contains(BorderLeftColor.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(BorderLeftColor.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {

                if (borderLeftWidth != null) {
                    borderLeftWidth.setAlreadyInUse(false);
                    borderLeftWidth = null;
                }

                borderLeftStyle = null;

                if (borderLeftColor != null) {
                    borderLeftColor.setAlreadyInUse(false);
                    borderLeftColor = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        BorderLeftWidth borderLeftWidth = null;
        BorderLeftStyle borderLeftStyle = null;
        BorderLeftColor borderLeftColor = null;

        for (final String eachPart : cssValueParts) {
            if (borderLeftWidth == null && BorderLeftWidth.isValid(eachPart)) {
                if (this.borderLeftWidth == null) {
                    borderLeftWidth = new BorderLeftWidth(eachPart);
                    borderLeftWidth.setStateChangeInformer(this);
                    borderLeftWidth.setAlreadyInUse(true);
                } else {
                    this.borderLeftWidth.setCssValue(eachPart);
                    borderLeftWidth = this.borderLeftWidth;
                }
            } else if (borderLeftStyle == null
                    && BorderLeftStyle.isValid(eachPart)) {
                borderLeftStyle = BorderLeftStyle.getThis(eachPart);
            } else if (borderLeftColor == null
                    && BorderLeftColor.isValid(eachPart)) {
                if (this.borderLeftColor == null) {
                    borderLeftColor = new BorderLeftColor(eachPart);
                    borderLeftColor.setStateChangeInformer(this);
                    borderLeftColor.setAlreadyInUse(true);
                } else {
                    this.borderLeftColor.setCssValue(eachPart);
                    borderLeftColor = this.borderLeftColor;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (borderLeftWidth != null) {
            cssValueBuilder.append(borderLeftWidth.getCssValue()).append(' ');
            invalid = false;
        } else if (this.borderLeftWidth != null) {
            this.borderLeftWidth.setAlreadyInUse(false);
        }
        if (borderLeftStyle != null) {
            cssValueBuilder.append(borderLeftStyle.getCssValue()).append(' ');
            invalid = false;
        }
        if (borderLeftColor != null) {
            cssValueBuilder.append(borderLeftColor.getCssValue()).append(' ');
            invalid = false;
        } else if (this.borderLeftColor != null) {
            this.borderLeftColor.setAlreadyInUse(false);
        }
        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example medium none color Or initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.borderLeftWidth = borderLeftWidth;
        this.borderLeftStyle = borderLeftStyle;
        this.borderLeftColor = borderLeftColor;

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

        BorderLeftWidth borderLeftWidth = null;
        BorderLeftStyle borderLeftStyle = null;
        BorderLeftColor borderLeftColor = null;

        for (final String eachPart : cssValueParts) {

            boolean invalid = true;
            if (borderLeftWidth == null && BorderLeftWidth.isValid(eachPart)) {
                borderLeftWidth = new BorderLeftWidth(eachPart);
                invalid = false;
            } else if (borderLeftStyle == null
                    && BorderLeftStyle.isValid(eachPart)) {
                borderLeftStyle = BorderLeftStyle.getThis(eachPart);
                invalid = false;
            } else if (borderLeftColor == null
                    && BorderLeftColor.isValid(eachPart)) {
                borderLeftColor = new BorderLeftColor(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return borderLeftWidth != null || borderLeftStyle != null
                || borderLeftColor != null;
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

    public BorderLeftColor getBorderLeftColor() {
        return borderLeftColor;
    }

    public BorderLeftStyle getBorderLeftStyle() {
        return borderLeftStyle;
    }

    public BorderLeftWidth getBorderLeftWidth() {
        return borderLeftWidth;
    }

    public BorderLeft setBorderLeftWidth(
            final BorderLeftWidth borderLeftWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderLeftWidth != null) {

            final String borderLeftWidthCssValue = borderLeftWidth
                    .getCssValue();
            if (BorderLeftWidth.INITIAL.equals(borderLeftWidthCssValue)
                    || BorderLeftWidth.INHERIT
                            .equals(borderLeftWidthCssValue)) {
                throw new InvalidValueException(
                        "borderLeftWidth cannot have initial/inherit as its cssValue");
            }

            cssValueBuilder.append(borderLeftWidth.getCssValue()).append(' ');
        }

        if (borderLeftStyle != null) {
            cssValueBuilder.append(borderLeftStyle.getCssValue()).append(' ');
        }

        if (borderLeftColor != null) {
            cssValueBuilder.append(borderLeftColor.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderLeftWidth != null && borderLeftWidth.isAlreadyInUse()
                && this.borderLeftWidth != borderLeftWidth) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderLeftWidth is already used by another object so a new object or the previous object (if it exists) of BorderLeftWidth will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderLeftWidth != null) {
            this.borderLeftWidth.setAlreadyInUse(false);
        }

        this.borderLeftWidth = borderLeftWidth;

        if (this.borderLeftWidth != null) {
            this.borderLeftWidth.setStateChangeInformer(this);
            this.borderLeftWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public BorderLeft setBorderLeftStyle(
            final BorderLeftStyle borderLeftStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderLeftWidth != null) {
            cssValueBuilder.append(borderLeftWidth.getCssValue()).append(' ');
        }

        if (borderLeftStyle != null) {
            cssValueBuilder.append(borderLeftStyle.getCssValue()).append(' ');
        }

        if (borderLeftColor != null) {
            cssValueBuilder.append(borderLeftColor.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.borderLeftStyle = borderLeftStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public BorderLeft setBorderLeftColor(
            final BorderLeftColor borderLeftColor) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (borderLeftWidth != null) {
            cssValueBuilder.append(borderLeftWidth.getCssValue()).append(' ');
        }

        if (borderLeftStyle != null) {
            cssValueBuilder.append(borderLeftStyle.getCssValue()).append(' ');
        }

        if (borderLeftColor != null) {
            final String borderLeftColorCssValue = borderLeftColor
                    .getCssValue();
            if (BorderLeftColor.INITIAL.equals(borderLeftColorCssValue)
                    || BorderLeftColor.INHERIT
                            .equals(borderLeftColorCssValue)) {
                throw new InvalidValueException(
                        "borderLeftColor cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(borderLeftColorCssValue);
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (borderLeftColor != null && borderLeftColor.isAlreadyInUse()
                && this.borderLeftColor != borderLeftColor) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given borderLeftColor is already used by another object so a new object or the previous object (if it exists) of BorderLeftColor will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.borderLeftColor != null) {
            this.borderLeftColor.setAlreadyInUse(false);
        }

        this.borderLeftColor = borderLeftColor;

        if (this.borderLeftColor != null) {
            this.borderLeftColor.setStateChangeInformer(this);
            this.borderLeftColor.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof BorderLeftColor) {

            final BorderLeftColor borderLeftColor = (BorderLeftColor) stateChangedObject;

            final String cssValue = borderLeftColor.getCssValue();

            if (BorderLeftColor.INITIAL.equals(cssValue)
                    || BorderLeftColor.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as borderLeftColor cssValue");
            }

            setBorderLeftColor(borderLeftColor);
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
