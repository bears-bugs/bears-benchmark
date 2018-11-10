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
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * <pre>
 *
 * border-width: medium|thin|thick|length|initial|inherit;
 *
 * The border-width property sets the width of an element's four borders. This property can have from one to four values.
 *
 * Examples:
 *
 *     border-width:thin medium thick 10px;
 *         top border is thin
 *         right border is medium
 *         bottom border is thick
 *         left border is 10px
 *
 *     border-width:thin medium thick;
 *         top border is thin
 *         right and left borders are medium
 *         bottom border is thick
 *
 *     border-width:thin medium;
 *         top and bottom borders are thin
 *         right and left borders are medium
 *
 *     border-width:thin;
 *         all four borders are thin
 *
 * Note: Always declare the border-style property before the border-width property. An element must have borders before you can set the width.
 * </pre>
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderWidth extends AbstractCssProperty<BorderWidth>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(BorderWidth.class.getName());

    public static final String MEDIUM = "medium";
    public static final String THIN = "thin";
    public static final String THICK = "thick";
    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, MEDIUM, THIN, THICK);

    private String cssValue;

    private BorderTopWidth borderTopWidth;
    private BorderRightWidth borderRightWidth;
    private BorderBottomWidth borderBottomWidth;
    private BorderLeftWidth borderLeftWidth;

    /**
     * The {@code medium} will be set as the value
     */
    public BorderWidth() {
        setCssValue(MEDIUM);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderWidth(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderBottomWidth
     *            the {@code BorderBottomWidth} object from which the cssValue
     *            to set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderWidth(final BorderWidth borderBottomWidth) {
        if (borderBottomWidth == null) {
            throw new NullValueException("borderBottomWidth can not be null");
        }
        setCssValue(borderBottomWidth.getCssValue());
    }

    /**
     * @param percent
     *            the percentage value to set. The cssLengthUnit will
     *            automatically set to %.
     * @since 1.0.0
     * @author WFF
     */
    public BorderWidth(final float percent) {
        setCssValue(String.valueOf(percent) + CssLengthUnit.PER);
    }

    /**
     * @param value
     * @param cssLengthUnit
     */
    public BorderWidth(final float value, final CssLengthUnit cssLengthUnit) {
        setCssValue(String.valueOf(value) + cssLengthUnit);
    }

    /**
     * @param value
     * @param cssLengthUnit
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public BorderWidth setValue(final float value,
            final CssLengthUnit cssLengthUnit) {
        setCssValue(String.valueOf(value) + cssLengthUnit);
        return this;
    }

    /**
     * @param percent
     *            the percent to set
     * @since 1.0.0
     * @author WFF
     */
    public void setPercent(final float percent) {
        setCssValue(String.valueOf(percent) + CssLengthUnit.PER);
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
        return CssNameConstants.BORDER_WIDTH;
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
    public BorderWidth setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit/medium/thin/thick.");
        } else if (StringUtil.isBlank(cssValue)) {
            throw new InvalidValueException(
                    "blank string is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit/medium/thin/thick.");
        } else {
            final String trimmedCssValue = StringUtil.strip(cssValue);

            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                this.cssValue = trimmedCssValue;
                if (borderTopWidth != null) {
                    borderTopWidth.setAlreadyInUse(false);
                    borderTopWidth = null;
                }
                if (borderRightWidth != null) {
                    borderRightWidth.setAlreadyInUse(false);
                    borderRightWidth = null;
                }
                if (borderBottomWidth != null) {
                    borderBottomWidth.setAlreadyInUse(false);
                    borderBottomWidth = null;
                }
                if (borderLeftWidth != null) {
                    borderLeftWidth.setAlreadyInUse(false);
                    borderLeftWidth = null;
                }
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }

                return this;
            }

            final String borderWidthString = StringUtil
                    .convertToSingleSpace(trimmedCssValue);

            final String[] extractedWidths = StringUtil
                    .splitBySpace(borderWidthString);

            if (extractedWidths.length == 1) {
                if (borderTopWidth == null) {
                    borderTopWidth = new BorderTopWidth(extractedWidths[0]);
                    borderTopWidth.setStateChangeInformer(this);
                    borderTopWidth.setAlreadyInUse(true);
                } else {
                    borderTopWidth.setCssValue(extractedWidths[0]);
                }
                if (borderRightWidth == null) {
                    borderRightWidth = new BorderRightWidth(extractedWidths[0]);
                    borderRightWidth.setStateChangeInformer(this);
                    borderRightWidth.setAlreadyInUse(true);
                } else {
                    borderRightWidth.setCssValue(extractedWidths[0]);
                }
                if (borderBottomWidth == null) {
                    borderBottomWidth = new BorderBottomWidth(
                            extractedWidths[0]);
                    borderBottomWidth.setStateChangeInformer(this);
                    borderBottomWidth.setAlreadyInUse(true);
                } else {
                    borderBottomWidth.setCssValue(extractedWidths[0]);
                }
                if (borderLeftWidth == null) {
                    borderLeftWidth = new BorderLeftWidth(extractedWidths[0]);
                    borderLeftWidth.setStateChangeInformer(this);
                    borderLeftWidth.setAlreadyInUse(true);
                } else {
                    borderLeftWidth.setCssValue(extractedWidths[0]);
                }
            } else if (extractedWidths.length == 2) {
                if (borderTopWidth == null) {
                    borderTopWidth = new BorderTopWidth(extractedWidths[0]);
                    borderTopWidth.setStateChangeInformer(this);
                    borderTopWidth.setAlreadyInUse(true);
                } else {
                    borderTopWidth.setCssValue(extractedWidths[0]);
                }
                if (borderBottomWidth == null) {
                    borderBottomWidth = new BorderBottomWidth(
                            extractedWidths[0]);
                    borderBottomWidth.setStateChangeInformer(this);
                    borderBottomWidth.setAlreadyInUse(true);
                } else {
                    borderBottomWidth.setCssValue(extractedWidths[0]);
                }
                if (borderRightWidth == null) {
                    borderRightWidth = new BorderRightWidth(extractedWidths[1]);
                    borderRightWidth.setStateChangeInformer(this);
                    borderRightWidth.setAlreadyInUse(true);
                } else {
                    borderRightWidth.setCssValue(extractedWidths[1]);
                }
                if (borderLeftWidth == null) {
                    borderLeftWidth = new BorderLeftWidth(extractedWidths[1]);
                    borderLeftWidth.setStateChangeInformer(this);
                    borderLeftWidth.setAlreadyInUse(true);
                } else {
                    borderLeftWidth.setCssValue(extractedWidths[1]);
                }
            } else if (extractedWidths.length == 3) {
                if (borderTopWidth == null) {
                    borderTopWidth = new BorderTopWidth(extractedWidths[0]);
                    borderTopWidth.setStateChangeInformer(this);
                    borderTopWidth.setAlreadyInUse(true);
                } else {
                    borderTopWidth.setCssValue(extractedWidths[0]);
                }
                if (borderRightWidth == null) {
                    borderRightWidth = new BorderRightWidth(extractedWidths[1]);
                    borderRightWidth.setStateChangeInformer(this);
                    borderRightWidth.setAlreadyInUse(true);
                } else {
                    borderRightWidth.setCssValue(extractedWidths[1]);
                }
                if (borderLeftWidth == null) {
                    borderLeftWidth = new BorderLeftWidth(extractedWidths[1]);
                    borderLeftWidth.setStateChangeInformer(this);
                    borderLeftWidth.setAlreadyInUse(true);
                } else {
                    borderLeftWidth.setCssValue(extractedWidths[1]);
                }
                if (borderBottomWidth == null) {
                    borderBottomWidth = new BorderBottomWidth(
                            extractedWidths[2]);
                    borderBottomWidth.setStateChangeInformer(this);
                    borderBottomWidth.setAlreadyInUse(true);
                } else {
                    borderBottomWidth.setCssValue(extractedWidths[2]);
                }
            } else if (extractedWidths.length == 4) {
                if (borderTopWidth == null) {
                    borderTopWidth = new BorderTopWidth(extractedWidths[0]);
                    borderTopWidth.setStateChangeInformer(this);
                    borderTopWidth.setAlreadyInUse(true);
                } else {
                    borderTopWidth.setCssValue(extractedWidths[0]);
                }
                if (borderRightWidth == null) {
                    borderRightWidth = new BorderRightWidth(extractedWidths[1]);
                    borderRightWidth.setStateChangeInformer(this);
                    borderRightWidth.setAlreadyInUse(true);
                } else {
                    borderRightWidth.setCssValue(extractedWidths[1]);
                }
                if (borderBottomWidth == null) {
                    borderBottomWidth = new BorderBottomWidth(
                            extractedWidths[2]);
                    borderBottomWidth.setStateChangeInformer(this);
                    borderBottomWidth.setAlreadyInUse(true);
                } else {
                    borderBottomWidth.setCssValue(extractedWidths[2]);
                }
                if (borderLeftWidth == null) {
                    borderLeftWidth = new BorderLeftWidth(extractedWidths[3]);
                    borderLeftWidth.setStateChangeInformer(this);
                    borderLeftWidth.setAlreadyInUse(true);
                } else {
                    borderLeftWidth.setCssValue(extractedWidths[3]);
                }
            } else {
                throw new InvalidValueException(
                        "the given cssValue is invalid");
            }

            this.cssValue = borderWidthString;
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
     * sets as {@code medium}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsMedium() {
        setCssValue(MEDIUM);
    }

    /**
     * sets as {@code thin}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsThin() {
        setCssValue(THIN);
    }

    /**
     * sets as {@code thick}.
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsThick() {
        setCssValue(THICK);
    }

    /**
     * sets the top, right, bottom and left width in {@code BorderWidth}. If the
     * given argument is already used by another object, then the
     * existing/cloned object will be used. And throws
     * {@code NullValueException} if any of the given argument is null.
     *
     * @param borderTopWidth
     * @param borderRightWidth
     * @param borderBottomWidth
     * @param borderLeftWidth
     * @author WFF
     * @since 1.0.0
     */
    public void setBorderWidth(final BorderTopWidth borderTopWidth,
            final BorderRightWidth borderRightWidth,
            final BorderBottomWidth borderBottomWidth,
            final BorderLeftWidth borderLeftWidth) {

        if (borderTopWidth != null && borderRightWidth != null
                && borderBottomWidth != null && borderLeftWidth != null) {

            if (BorderTopWidth.INITIAL.equals(borderTopWidth.getCssValue())
                    || BorderTopWidth.INHERIT
                            .equals(borderTopWidth.getCssValue())
                    || BorderRightWidth.INITIAL
                            .equals(borderRightWidth.getCssValue())
                    || BorderRightWidth.INHERIT
                            .equals(borderRightWidth.getCssValue())
                    || BorderBottomWidth.INITIAL
                            .equals(borderBottomWidth.getCssValue())
                    || BorderBottomWidth.INHERIT
                            .equals(borderBottomWidth.getCssValue())
                    || BorderLeftWidth.INITIAL
                            .equals(borderLeftWidth.getCssValue())
                    || BorderLeftWidth.INHERIT
                            .equals(borderLeftWidth.getCssValue())) {
                throw new InvalidValueException(
                        "Any or all of the given arguments have initial/inherit constant value as its cssValue");
            }

            try {
                final BorderTopWidth borderTopWidthTemp;
                final BorderRightWidth borderRightWidthTemp;
                final BorderBottomWidth borderBottomWidthTemp;
                final BorderLeftWidth borderLeftWidthTemp;

                if (this.borderTopWidth != null) {
                    this.borderTopWidth.setAlreadyInUse(false);
                    this.borderRightWidth.setAlreadyInUse(false);
                    this.borderBottomWidth.setAlreadyInUse(false);
                    this.borderLeftWidth.setAlreadyInUse(false);
                }

                if (borderTopWidth.isAlreadyInUse()
                        && this.borderTopWidth != borderTopWidth) {
                    if (this.borderTopWidth != null) {
                        borderTopWidthTemp = this.borderTopWidth
                                .setCssValue(borderTopWidth.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderTopWidth is already used by another object so the existing object is used");
                        }
                    } else {
                        borderTopWidthTemp = borderTopWidth.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderTopWidth is already used by another object so its clone is assigned");
                        }
                    }
                } else {
                    borderTopWidthTemp = borderTopWidth;
                }

                if (borderRightWidth.isAlreadyInUse()
                        && this.borderRightWidth != borderRightWidth) {
                    if (this.borderRightWidth != null) {
                        borderRightWidthTemp = this.borderRightWidth
                                .setCssValue(borderTopWidth.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderRightWidth is already used by another object so the existing object is used");
                        }
                    } else {
                        borderRightWidthTemp = borderRightWidth.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderRightWidth is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    borderRightWidthTemp = borderRightWidth;
                }

                if (borderBottomWidth.isAlreadyInUse()
                        && this.borderBottomWidth != borderBottomWidth) {
                    if (this.borderBottomWidth != null) {
                        borderBottomWidthTemp = this.borderBottomWidth
                                .setCssValue(borderTopWidth.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderBottomWidth is already used by another object so the existing object is used");
                        }
                    } else {
                        borderBottomWidthTemp = borderBottomWidth.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderBottomWidth is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    borderBottomWidthTemp = borderBottomWidth;
                }

                if (borderLeftWidth.isAlreadyInUse()
                        && this.borderLeftWidth != borderLeftWidth) {
                    if (this.borderLeftWidth != null) {
                        borderLeftWidthTemp = this.borderLeftWidth
                                .setCssValue(borderTopWidth.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderLeftWidth is already used by another object so the existing object is used");
                        }
                    } else {
                        borderLeftWidthTemp = borderLeftWidth.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given borderLeftWidth is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    borderLeftWidthTemp = borderLeftWidth;
                }

                borderTopWidthTemp.setAlreadyInUse(true);
                borderTopWidthTemp.setStateChangeInformer(this);

                borderRightWidthTemp.setAlreadyInUse(true);
                borderRightWidthTemp.setStateChangeInformer(this);

                borderBottomWidthTemp.setAlreadyInUse(true);
                borderBottomWidthTemp.setStateChangeInformer(this);

                borderLeftWidthTemp.setAlreadyInUse(true);
                borderLeftWidthTemp.setStateChangeInformer(this);

                assignProducedCssValue(borderTopWidth, borderRightWidth,
                        borderBottomWidth, borderLeftWidth);

                this.borderTopWidth = borderTopWidthTemp;
                this.borderRightWidth = borderRightWidthTemp;
                this.borderBottomWidth = borderBottomWidthTemp;
                this.borderLeftWidth = borderLeftWidthTemp;

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
     * @return the borderTopWidth
     * @author WFF
     * @since 1.0.0
     */
    public BorderTopWidth getBorderTopWidth() {
        return borderTopWidth;
    }

    /**
     * @return the borderRightWidth
     * @author WFF
     * @since 1.0.0
     */
    public BorderRightWidth getBorderRightWidth() {
        return borderRightWidth;
    }

    /**
     * @return the borderBottomWidth
     * @author WFF
     * @since 1.0.0
     */
    public BorderBottomWidth getBorderBottomWidth() {
        return borderBottomWidth;
    }

    /**
     * @return the borderLeftWidth
     * @author WFF
     * @since 1.0.0
     */
    public BorderLeftWidth getBorderLeftWidth() {
        return borderLeftWidth;
    }

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    private void assignProducedCssValue(final BorderTopWidth borderTopWidth,
            final BorderRightWidth borderRightWidth,
            final BorderBottomWidth borderBottomWidth,
            final BorderLeftWidth borderLeftWidth) {

        final String borderTopWidthCssValue = borderTopWidth.getCssValue();
        final String borderRightWidthCssValue = borderRightWidth.getCssValue();
        final String borderBottomWidthCssValue = borderBottomWidth
                .getCssValue();
        final String borderLeftWidthCssValue = borderLeftWidth.getCssValue();

        if (borderTopWidthCssValue.equals(borderRightWidthCssValue)
                && borderRightWidthCssValue.equals(borderBottomWidthCssValue)
                && borderBottomWidthCssValue.equals(borderLeftWidthCssValue)) {

            cssValue = borderTopWidthCssValue;

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        } else if (borderTopWidthCssValue.equals(borderBottomWidthCssValue)
                && borderRightWidthCssValue.equals(borderLeftWidthCssValue)) {

            final StringBuilder cssValueBuilder = new StringBuilder(
                    borderTopWidthCssValue);
            cssValueBuilder.append(' ').append(borderRightWidthCssValue);

            cssValue = cssValueBuilder.toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }

        } else if (borderRightWidthCssValue.equals(borderLeftWidthCssValue)) {
            cssValue = new StringBuilder(borderTopWidthCssValue).append(' ')
                    .append(borderRightWidthCssValue).append(' ')
                    .append(borderBottomWidthCssValue).toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        } else {

            cssValue = new StringBuilder(borderTopWidthCssValue).append(' ')
                    .append(borderRightWidthCssValue).append(' ')
                    .append(borderBottomWidthCssValue).append(' ')
                    .append(borderLeftWidthCssValue).toString();

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

        if (stateChangedObject instanceof BorderTopWidth) {
            final BorderTopWidth borderTopWidth = (BorderTopWidth) stateChangedObject;
            if (BorderTopWidth.INITIAL.equals(borderTopWidth.getCssValue())
                    || BorderTopWidth.INHERIT
                            .equals(borderTopWidth.getCssValue())) {
                throw new InvalidValueException(
                        "borderTopWidth cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof BorderRightWidth) {
            final BorderRightWidth borderRightWidth = (BorderRightWidth) stateChangedObject;
            if (BorderRightWidth.INITIAL.equals(borderRightWidth.getCssValue())
                    || BorderRightWidth.INHERIT
                            .equals(borderRightWidth.getCssValue())) {
                throw new InvalidValueException(
                        "borderRightWidth cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof BorderBottomWidth) {
            final BorderBottomWidth borderBottomWidth = (BorderBottomWidth) stateChangedObject;
            if (BorderBottomWidth.INITIAL
                    .equals(borderBottomWidth.getCssValue())
                    || BorderBottomWidth.INHERIT
                            .equals(borderBottomWidth.getCssValue())) {
                throw new InvalidValueException(
                        "borderBottomWidth cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof BorderLeftWidth) {
            final BorderLeftWidth borderLeftWidth = (BorderLeftWidth) stateChangedObject;
            if (BorderLeftWidth.INITIAL.equals(borderLeftWidth.getCssValue())
                    || BorderLeftWidth.INHERIT
                            .equals(borderLeftWidth.getCssValue())) {
                throw new InvalidValueException(
                        "borderLeftWidth cannot have initial/inherit as its cssValue");
            }
        }

        assignProducedCssValue(borderTopWidth, borderRightWidth,
                borderBottomWidth, borderLeftWidth);

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
        final String trimmedCssValue = StringUtil.strip(cssValue).toLowerCase();
        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);
        if (cssValueParts.length > 4) {
            return false;
        }
        for (final String cssValuePart : cssValueParts) {
            boolean invalidValue = true;
            for (final CssLengthUnit cssLengthUnit : CssLengthUnit.values()) {
                final String unit = cssLengthUnit.getUnit();
                if (cssValuePart.endsWith(unit)) {
                    final String valueOnly = cssValuePart.replaceFirst(unit,
                            "");
                    try {
                        Float.parseFloat(valueOnly);
                    } catch (final NumberFormatException e) {
                        break;
                    }
                    invalidValue = false;
                    break;
                }
            }
            if (PREDEFINED_CONSTANTS.contains(cssValuePart)) {
                invalidValue = false;
            }
            if (invalidValue) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if its cssValue is any of the values
     *         <code> initial or inherit</code>.
     * @author WFF
     * @since 1.0.0
     */
    public boolean hasPredefinedConstantValue() {
        return PREDEFINED_CONSTANTS.contains(cssValue);
    }

}
