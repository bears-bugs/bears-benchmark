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
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * border-color: <i>color</i>|transparent|initial|inherit;
 *
 * The border-color property sets the color of an element's four borders. This property can have from one to four values.
 *
 * Examples:
 *
 *     border-color:red green blue pink;
 *         top border is red
 *         right border is green
 *         bottom border is blue
 *         left border is pink
 *
 *     border-color:red green blue;
 *         top border is red
 *         right and left borders are green
 *         bottom border is blue
 *
 *     border-color:red green;
 *         top and bottom borders are red
 *         right and left borders are green
 *
 *     border-color:red;
 *         all four borders are red
 *
 * Note: Always declare the border-style property before the border-color property. An element must have borders before you can change the color.
 * Default value:  The current color of the element
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS1
 * JavaScript syntax:      object.style.borderColor="#FF0000 blue"
 * </pre>
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderColor extends AbstractCssProperty<BorderColor>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(BorderColor.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String TRANSPARENT = "transparent";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, TRANSPARENT);

    private String cssValue;

    private BorderTopColor borderTopColor;
    private BorderRightColor borderRightColor;
    private BorderBottomColor borderBottomColor;
    private BorderLeftColor borderLeftColor;

    /**
     * The {@code initial} will be set as the value
     */
    public BorderColor() {
        setCssValue(INITIAL);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderColor(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderBottomColor
     *            the {@code BorderBottomColor} object from which the cssValue
     *            to set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderColor(final BorderColor borderBottomColor) {
        if (borderBottomColor == null) {
            throw new NullValueException("borderBottomColor can not be null");
        }
        setCssValue(borderBottomColor.getCssValue());
    }

    /**
     * the color/color code to set. The alternative method {@code setCssValue}
     * can also be used.
     *
     * @param value
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public BorderColor setValue(final String value) {
        setCssValue(value);
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
        return CssNameConstants.BORDER_COLOR;
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
     * gets the value, {@code getCssValue} method can also be used to get the
     * same value.
     *
     * @return the value in String.
     * @since 1.0.0
     * @author WFF
     */
    public String getValue() {
        return getCssValue();
    }

    /**
     * @param cssValue
     *            the value should be a color/color code, for example
     *            <code>#0000ff</code>. {@code null} is considered as an invalid
     *            value and it will throw {@code NullValueException}.And an
     *            empty string is also considered as an invalid value and it
     *            will throw {@code InvalidValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public BorderColor setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value should be any color for example #0000ff. Or, initial/inherit/transparent.");
        } else if (StringUtil.isBlank(cssValue)) {
            throw new InvalidValueException(
                    "blank string is an invalid value. The value should be any color for example #0000ff. Or, initial/inherit/transparent.");
        } else {
            final String trimmedCssValue = StringUtil.strip(cssValue);

            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                this.cssValue = trimmedCssValue;
                if (borderTopColor != null) {
                    borderTopColor.setAlreadyInUse(false);
                    borderTopColor = null;
                }
                if (borderRightColor != null) {
                    borderRightColor.setAlreadyInUse(false);
                    borderRightColor = null;
                }
                if (borderBottomColor != null) {
                    borderBottomColor.setAlreadyInUse(false);
                    borderBottomColor = null;
                }
                if (borderLeftColor != null) {
                    borderLeftColor.setAlreadyInUse(false);
                    borderLeftColor = null;
                }
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }

                return this;
            }

            final String borderColorString = StringUtil
                    .convertToSingleSpace(trimmedCssValue);

            final List<String> extractedColors = CssValueUtil
                    .split(borderColorString);

            if (extractedColors.size() == 1) {
                if (borderTopColor == null) {
                    borderTopColor = new BorderTopColor(extractedColors.get(0));
                    borderTopColor.setStateChangeInformer(this);
                    borderTopColor.setAlreadyInUse(true);
                } else {
                    borderTopColor.setCssValue(extractedColors.get(0));
                }
                if (borderRightColor == null) {
                    borderRightColor = new BorderRightColor(
                            extractedColors.get(0));
                    borderRightColor.setStateChangeInformer(this);
                    borderRightColor.setAlreadyInUse(true);
                } else {
                    borderRightColor.setCssValue(extractedColors.get(0));
                }
                if (borderBottomColor == null) {
                    borderBottomColor = new BorderBottomColor(
                            extractedColors.get(0));
                    borderBottomColor.setStateChangeInformer(this);
                    borderBottomColor.setAlreadyInUse(true);
                } else {
                    borderBottomColor.setCssValue(extractedColors.get(0));
                }
                if (borderLeftColor == null) {
                    borderLeftColor = new BorderLeftColor(
                            extractedColors.get(0));
                    borderLeftColor.setStateChangeInformer(this);
                    borderLeftColor.setAlreadyInUse(true);
                } else {
                    borderLeftColor.setCssValue(extractedColors.get(0));
                }
            } else if (extractedColors.size() == 2) {
                if (borderTopColor == null) {
                    borderTopColor = new BorderTopColor(extractedColors.get(0));
                    borderTopColor.setStateChangeInformer(this);
                    borderTopColor.setAlreadyInUse(true);
                } else {
                    borderTopColor.setCssValue(extractedColors.get(0));
                }
                if (borderBottomColor == null) {
                    borderBottomColor = new BorderBottomColor(
                            extractedColors.get(0));
                    borderBottomColor.setStateChangeInformer(this);
                    borderBottomColor.setAlreadyInUse(true);
                } else {
                    borderBottomColor.setCssValue(extractedColors.get(0));
                }
                if (borderRightColor == null) {
                    borderRightColor = new BorderRightColor(
                            extractedColors.get(1));
                    borderRightColor.setStateChangeInformer(this);
                    borderRightColor.setAlreadyInUse(true);
                } else {
                    borderRightColor.setCssValue(extractedColors.get(1));
                }
                if (borderLeftColor == null) {
                    borderLeftColor = new BorderLeftColor(
                            extractedColors.get(1));
                    borderLeftColor.setStateChangeInformer(this);
                    borderLeftColor.setAlreadyInUse(true);
                } else {
                    borderLeftColor.setCssValue(extractedColors.get(1));
                }
            } else if (extractedColors.size() == 3) {
                if (borderTopColor == null) {
                    borderTopColor = new BorderTopColor(extractedColors.get(0));
                    borderTopColor.setStateChangeInformer(this);
                    borderTopColor.setAlreadyInUse(true);
                } else {
                    borderTopColor.setCssValue(extractedColors.get(0));
                }
                if (borderRightColor == null) {
                    borderRightColor = new BorderRightColor(
                            extractedColors.get(1));
                    borderRightColor.setStateChangeInformer(this);
                    borderRightColor.setAlreadyInUse(true);
                } else {
                    borderRightColor.setCssValue(extractedColors.get(1));
                }
                if (borderLeftColor == null) {
                    borderLeftColor = new BorderLeftColor(
                            extractedColors.get(1));
                    borderLeftColor.setStateChangeInformer(this);
                    borderLeftColor.setAlreadyInUse(true);
                } else {
                    borderLeftColor.setCssValue(extractedColors.get(1));
                }
                if (borderBottomColor == null) {
                    borderBottomColor = new BorderBottomColor(
                            extractedColors.get(2));
                    borderBottomColor.setStateChangeInformer(this);
                    borderBottomColor.setAlreadyInUse(true);
                } else {
                    borderBottomColor.setCssValue(extractedColors.get(2));
                }
            } else if (extractedColors.size() == 4) {
                if (borderTopColor == null) {
                    borderTopColor = new BorderTopColor(extractedColors.get(0));
                    borderTopColor.setStateChangeInformer(this);
                    borderTopColor.setAlreadyInUse(true);
                } else {
                    borderTopColor.setCssValue(extractedColors.get(0));
                }
                if (borderRightColor == null) {
                    borderRightColor = new BorderRightColor(
                            extractedColors.get(1));
                    borderRightColor.setStateChangeInformer(this);
                    borderRightColor.setAlreadyInUse(true);
                } else {
                    borderRightColor.setCssValue(extractedColors.get(1));
                }
                if (borderBottomColor == null) {
                    borderBottomColor = new BorderBottomColor(
                            extractedColors.get(2));
                    borderBottomColor.setStateChangeInformer(this);
                    borderBottomColor.setAlreadyInUse(true);
                } else {
                    borderBottomColor.setCssValue(extractedColors.get(2));
                }
                if (borderLeftColor == null) {
                    borderLeftColor = new BorderLeftColor(
                            extractedColors.get(3));
                    borderLeftColor.setStateChangeInformer(this);
                    borderLeftColor.setAlreadyInUse(true);
                } else {
                    borderLeftColor.setCssValue(extractedColors.get(3));
                }
            } else {
                throw new InvalidValueException(
                        "the given cssValue is invalid");
            }

            this.cssValue = borderColorString;
            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
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
     * sets as {@code transparent}
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsTransparent() {
        setCssValue(TRANSPARENT);
    }

    /**
     * @return the borderTopColor
     * @author WFF
     * @since 1.0.0
     */
    public BorderTopColor getBorderTopColor() {
        return borderTopColor;
    }

    /**
     * @param borderTopColor
     * @param borderRightColor
     * @param borderBottomColor
     * @param borderLeftColor
     * @author WFF
     * @since 1.0.0
     */
    public void setBorderColor(final BorderTopColor borderTopColor,
            final BorderRightColor borderRightColor,
            final BorderBottomColor borderBottomColor,
            final BorderLeftColor borderLeftColor) {

        if (borderTopColor != null && borderRightColor != null
                && borderBottomColor != null && borderLeftColor != null) {

            if (BorderTopColor.INITIAL.equals(borderTopColor.getCssValue())
                    || BorderTopColor.INHERIT
                            .equals(borderTopColor.getCssValue())
                    || BorderRightColor.INITIAL
                            .equals(borderRightColor.getCssValue())
                    || BorderRightColor.INHERIT
                            .equals(borderRightColor.getCssValue())
                    || BorderBottomColor.INITIAL
                            .equals(borderBottomColor.getCssValue())
                    || BorderBottomColor.INHERIT
                            .equals(borderBottomColor.getCssValue())
                    || BorderLeftColor.INITIAL
                            .equals(borderLeftColor.getCssValue())
                    || BorderLeftColor.INHERIT
                            .equals(borderLeftColor.getCssValue())) {
                throw new InvalidValueException(
                        "Any or all of the given arguments have initial/inherit constant value as its cssValue");
            }

            try {
                final BorderTopColor borderTopColorTemp;
                final BorderRightColor borderRightColorTemp;
                final BorderBottomColor borderBottomColorTemp;
                final BorderLeftColor borderLeftColorTemp;

                if (this.borderTopColor != null) {

                    assignProducedCssValue(borderTopColor, borderRightColor,
                            borderBottomColor, borderLeftColor);

                    this.borderTopColor.setAlreadyInUse(false);
                    this.borderRightColor.setAlreadyInUse(false);
                    this.borderBottomColor.setAlreadyInUse(false);
                    this.borderLeftColor.setAlreadyInUse(false);

                    borderTopColorTemp = borderTopColor;
                    borderRightColorTemp = borderRightColor;
                    borderBottomColorTemp = borderBottomColor;
                    borderLeftColorTemp = borderLeftColor;

                } else {
                    if (borderTopColor.isAlreadyInUse()) {
                        borderTopColorTemp = borderTopColor.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderTopColor is already used by another object so its clone is assigned");
                        }
                    } else {
                        borderTopColorTemp = borderTopColor;
                    }

                    if (borderRightColor.isAlreadyInUse()) {
                        borderRightColorTemp = borderRightColor.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderRightColor is already used by another object so its clone is assigned");
                        }
                    } else {
                        borderRightColorTemp = borderRightColor;
                    }

                    if (borderBottomColor.isAlreadyInUse()) {
                        borderBottomColorTemp = borderBottomColor.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderBottomColor is already used by another object so its clone is assigned");
                        }
                    } else {
                        borderBottomColorTemp = borderBottomColor;
                    }

                    if (borderLeftColor.isAlreadyInUse()) {
                        borderLeftColorTemp = borderLeftColor.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderLeftColor is already used by another object so its clone is assigned");
                        }
                    } else {
                        borderLeftColorTemp = borderLeftColor;
                    }
                }

                borderTopColorTemp.setAlreadyInUse(true);
                borderTopColorTemp.setStateChangeInformer(this);

                borderRightColorTemp.setAlreadyInUse(true);
                borderRightColorTemp.setStateChangeInformer(this);

                borderBottomColorTemp.setAlreadyInUse(true);
                borderBottomColorTemp.setStateChangeInformer(this);

                borderLeftColorTemp.setAlreadyInUse(true);
                borderLeftColorTemp.setStateChangeInformer(this);

                assignProducedCssValue(borderTopColor, borderRightColor,
                        borderBottomColor, borderLeftColor);

                this.borderTopColor = borderTopColorTemp;
                this.borderRightColor = borderRightColorTemp;
                this.borderBottomColor = borderBottomColorTemp;
                this.borderLeftColor = borderLeftColorTemp;

            } catch (final CloneNotSupportedException e) {
                throw new InvalidValueException(e);
            } catch (final Exception e) {
                throw new InvalidValueException(e);
            }

        } else {
            throw new NullValueException("cannot accept null arguments");
        }

    }

    /**
     * @return the borderRightColor
     * @author WFF
     * @since 1.0.0
     */
    public BorderRightColor getBorderRightColor() {
        return borderRightColor;
    }

    /**
     * @return the borderBottomColor
     * @author WFF
     * @since 1.0.0
     */
    public BorderBottomColor getBorderBottomColor() {
        return borderBottomColor;
    }

    /**
     * @return the borderLeftColor
     * @author WFF
     * @since 1.0.0
     */
    public BorderLeftColor getBorderLeftColor() {
        return borderLeftColor;
    }

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    private void assignProducedCssValue(final BorderTopColor borderTopColor,
            final BorderRightColor borderRightColor,
            final BorderBottomColor borderBottomColor,
            final BorderLeftColor borderLeftColor) {

        final String borderTopColorCssValue = borderTopColor.getCssValue();
        final String borderRightColorCssValue = borderRightColor.getCssValue();
        final String borderBottomColorCssValue = borderBottomColor
                .getCssValue();
        final String borderLeftColorCssValue = borderLeftColor.getCssValue();

        if (borderTopColorCssValue.equals(borderRightColorCssValue)
                && borderRightColorCssValue.equals(borderBottomColorCssValue)
                && borderBottomColorCssValue.equals(borderLeftColorCssValue)) {

            cssValue = borderTopColorCssValue;

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        } else if (borderTopColorCssValue.equals(borderBottomColorCssValue)
                && borderRightColorCssValue.equals(borderLeftColorCssValue)) {

            cssValue = new StringBuilder(borderTopColorCssValue).append(' ')
                    .append(borderRightColorCssValue).toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }

        } else if (borderRightColorCssValue.equals(borderLeftColorCssValue)) {

            cssValue = new StringBuilder(borderTopColorCssValue).append(' ')
                    .append(borderRightColorCssValue).append(' ')
                    .append(borderBottomColorCssValue).toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        } else {
            cssValue = new StringBuilder(borderTopColorCssValue).append(' ')
                    .append(borderRightColorCssValue).append(' ')
                    .append(borderBottomColorCssValue).append(' ')
                    .append(borderLeftColorCssValue).toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.webfirmframework.wffweb.informer.StateChangeInformer#stateChanged(
     * java.lang .Object)
     */
    @Override
    public void stateChanged(final CssProperty stateChangedObject) {

        if (stateChangedObject instanceof BorderTopColor) {
            final BorderTopColor borderTopColor = (BorderTopColor) stateChangedObject;
            if (BorderTopColor.INITIAL.equals(borderTopColor.getCssValue())
                    || BorderTopColor.INHERIT
                            .equals(borderTopColor.getCssValue())) {
                throw new InvalidValueException(
                        "borderTopColor cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof BorderRightColor) {
            final BorderRightColor borderRightColor = (BorderRightColor) stateChangedObject;
            if (BorderRightColor.INITIAL.equals(borderRightColor.getCssValue())
                    || BorderRightColor.INHERIT
                            .equals(borderRightColor.getCssValue())) {
                throw new InvalidValueException(
                        "borderRightColor cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof BorderBottomColor) {
            final BorderBottomColor borderBottomColor = (BorderBottomColor) stateChangedObject;
            if (BorderBottomColor.INITIAL
                    .equals(borderBottomColor.getCssValue())
                    || BorderBottomColor.INHERIT
                            .equals(borderBottomColor.getCssValue())) {
                throw new InvalidValueException(
                        "borderBottomColor cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof BorderLeftColor) {
            final BorderLeftColor borderLeftColor = (BorderLeftColor) stateChangedObject;
            if (BorderLeftColor.INITIAL.equals(borderLeftColor.getCssValue())
                    || BorderLeftColor.INHERIT
                            .equals(borderLeftColor.getCssValue())) {
                throw new InvalidValueException(
                        "borderLeftColor cannot have initial/inherit as its cssValue");
            }
        }

        assignProducedCssValue(borderTopColor, borderRightColor,
                borderBottomColor, borderLeftColor);

    }

    /**
     * @return true if its cssValue is any of the values
     *         <code> initial, inherit or transparent</code>.
     * @author WFF
     * @since 1.0.0
     */
    public boolean hasPredefinedConstantValue() {
        return PREDEFINED_CONSTANTS.contains(cssValue);
    }

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    public static boolean isValid(final String cssValue) {
        final String trimmedCssValue;
        if (cssValue == null || (trimmedCssValue = TagStringUtil
                .toLowerCase(StringUtil.strip(cssValue))).isEmpty()) {
            return false;
        }
        if (INITIAL.equalsIgnoreCase(trimmedCssValue)
                || INHERIT.equalsIgnoreCase(trimmedCssValue)) {
            return true;
        }

        final List<String> cssValueParts = CssValueUtil.split(trimmedCssValue);

        for (final String eachPart : cssValueParts) {
            final boolean valid = BorderTopColor.isValid(eachPart);
            if ((valid
                    && (INITIAL.equals(eachPart) || INHERIT.equals(eachPart)))
                    || !valid) {
                return false;
            }
        }

        return true;
    }
}
