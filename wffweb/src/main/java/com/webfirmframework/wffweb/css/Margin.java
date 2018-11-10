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
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * The margin shorthand property sets all the margin properties in one declaration. This property can have from one to four values.
 *
 * Examples:
 *
 *     margin:10px 5px 15px 20px;
 *         top margin is 10px
 *         right margin is 5px
 *         bottom margin is 15px
 *         left margin is 20px
 *
 *     margin:10px 5px 15px;
 *         top margin is 10px
 *         right and left margin are 5px
 *         bottom margin is 15px
 *
 *     margin:10px 5px;
 *         top and bottom margin are 10px
 *         right and left margin are 5px
 *
 *     margin:10px;
 *         all four margins are 10px
 *
 * Note: Negative values are not allowed.
 * Default value:  0
 * Inherited:      no
 * Animatable:     yes
 *
 * Version:        CSS1
 * JavaScript syntax:      object.style.margin="100px 20px"
 * </pre>
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class Margin extends AbstractCssProperty<Margin>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(Margin.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    private static final String AUTO = "auto";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, AUTO);

    private String cssValue;

    private MarginTop marginTop;
    private MarginRight marginRight;
    private MarginBottom marginBottom;
    private MarginLeft marginLeft;

    /**
     * The {@code initial} will be set as the value
     */
    public Margin() {
        setCssValue(INITIAL);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public Margin(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param marginBottom
     *            the {@code MarginBottom} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public Margin(final Margin marginBottom) {
        if (marginBottom == null) {
            throw new NullValueException("marginBottom can not be null");
        }
        setCssValue(marginBottom.getCssValue());
    }

    /**
     * the margin length to set. The alternative method {@code setCssValue} can
     * also be used.
     *
     * @param value
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public Margin setValue(final String value) {
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
        return CssNameConstants.MARGIN;
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
     *            the value should be a length value, for example
     *            <code>5px</code>. {@code null} is considered as an invalid
     *            value and it will throw {@code NullValueException}.And an
     *            empty string is also considered as an invalid value and it
     *            will throw {@code InvalidValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public Margin setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value should be css length for example 2px. Or, initial/inherit.");
        } else if (StringUtil.isBlank(cssValue)) {
            throw new InvalidValueException(
                    "blank string is an invalid value. The value should be css length for example 5px. Or, initial/inherit.");
        } else {
            final String trimmedCssValue = StringUtil.strip(cssValue);

            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                this.cssValue = trimmedCssValue;
                if (marginTop != null) {
                    marginTop.setAlreadyInUse(false);
                    marginTop = null;
                }
                if (marginRight != null) {
                    marginRight.setAlreadyInUse(false);
                    marginRight = null;
                }
                if (marginBottom != null) {
                    marginBottom.setAlreadyInUse(false);
                    marginBottom = null;
                }
                if (marginLeft != null) {
                    marginLeft.setAlreadyInUse(false);
                    marginLeft = null;
                }
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }

                return this;
            }

            final String marginString = StringUtil
                    .convertToSingleSpace(trimmedCssValue);

            final String[] extractedMargins = StringUtil
                    .splitBySpace(marginString);

            if (extractedMargins.length == 1) {
                if (marginTop == null) {
                    marginTop = new MarginTop(extractedMargins[0]);
                    marginTop.setStateChangeInformer(this);
                    marginTop.setAlreadyInUse(true);
                } else {
                    marginTop.setCssValue(extractedMargins[0]);
                }
                if (marginRight == null) {
                    marginRight = new MarginRight(extractedMargins[0]);
                    marginRight.setStateChangeInformer(this);
                    marginRight.setAlreadyInUse(true);
                } else {
                    marginRight.setCssValue(extractedMargins[0]);
                }
                if (marginBottom == null) {
                    marginBottom = new MarginBottom(extractedMargins[0]);
                    marginBottom.setStateChangeInformer(this);
                    marginBottom.setAlreadyInUse(true);
                } else {
                    marginBottom.setCssValue(extractedMargins[0]);
                }
                if (marginLeft == null) {
                    marginLeft = new MarginLeft(extractedMargins[0]);
                    marginLeft.setStateChangeInformer(this);
                    marginLeft.setAlreadyInUse(true);
                } else {
                    marginLeft.setCssValue(extractedMargins[0]);
                }
            } else if (extractedMargins.length == 2) {
                if (marginTop == null) {
                    marginTop = new MarginTop(extractedMargins[0]);
                    marginTop.setStateChangeInformer(this);
                    marginTop.setAlreadyInUse(true);
                } else {
                    marginTop.setCssValue(extractedMargins[0]);
                }
                if (marginBottom == null) {
                    marginBottom = new MarginBottom(extractedMargins[0]);
                    marginBottom.setStateChangeInformer(this);
                    marginBottom.setAlreadyInUse(true);
                } else {
                    marginBottom.setCssValue(extractedMargins[0]);
                }
                if (marginRight == null) {
                    marginRight = new MarginRight(extractedMargins[1]);
                    marginRight.setStateChangeInformer(this);
                    marginRight.setAlreadyInUse(true);
                } else {
                    marginRight.setCssValue(extractedMargins[1]);
                }
                if (marginLeft == null) {
                    marginLeft = new MarginLeft(extractedMargins[1]);
                    marginLeft.setStateChangeInformer(this);
                    marginLeft.setAlreadyInUse(true);
                } else {
                    marginLeft.setCssValue(extractedMargins[1]);
                }
            } else if (extractedMargins.length == 3) {
                if (marginTop == null) {
                    marginTop = new MarginTop(extractedMargins[0]);
                    marginTop.setStateChangeInformer(this);
                    marginTop.setAlreadyInUse(true);
                } else {
                    marginTop.setCssValue(extractedMargins[0]);
                }
                if (marginRight == null) {
                    marginRight = new MarginRight(extractedMargins[1]);
                    marginRight.setStateChangeInformer(this);
                    marginRight.setAlreadyInUse(true);
                } else {
                    marginRight.setCssValue(extractedMargins[1]);
                }
                if (marginLeft == null) {
                    marginLeft = new MarginLeft(extractedMargins[1]);
                    marginLeft.setStateChangeInformer(this);
                    marginLeft.setAlreadyInUse(true);
                } else {
                    marginLeft.setCssValue(extractedMargins[1]);
                }
                if (marginBottom == null) {
                    marginBottom = new MarginBottom(extractedMargins[2]);
                    marginBottom.setStateChangeInformer(this);
                    marginBottom.setAlreadyInUse(true);
                } else {
                    marginBottom.setCssValue(extractedMargins[2]);
                }
            } else if (extractedMargins.length == 4) {
                if (marginTop == null) {
                    marginTop = new MarginTop(extractedMargins[0]);
                    marginTop.setStateChangeInformer(this);
                    marginTop.setAlreadyInUse(true);
                } else {
                    marginTop.setCssValue(extractedMargins[0]);
                }
                if (marginRight == null) {
                    marginRight = new MarginRight(extractedMargins[1]);
                    marginRight.setStateChangeInformer(this);
                    marginRight.setAlreadyInUse(true);
                } else {
                    marginRight.setCssValue(extractedMargins[1]);
                }
                if (marginBottom == null) {
                    marginBottom = new MarginBottom(extractedMargins[2]);
                    marginBottom.setStateChangeInformer(this);
                    marginBottom.setAlreadyInUse(true);
                } else {
                    marginBottom.setCssValue(extractedMargins[2]);
                }
                if (marginLeft == null) {
                    marginLeft = new MarginLeft(extractedMargins[3]);
                    marginLeft.setStateChangeInformer(this);
                    marginLeft.setAlreadyInUse(true);
                } else {
                    marginLeft.setCssValue(extractedMargins[3]);
                }
            } else {
                throw new InvalidValueException(
                        "the given cssValue is invalid");
            }

            this.cssValue = marginString;
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
     * sets as {@code inherit}
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsAuto() {
        setCssValue(AUTO);
    }

    /**
     * @return the marginTop
     * @author WFF
     * @since 1.0.0
     */
    public MarginTop getMarginTop() {
        return marginTop;
    }

    /**
     * sets the top, right, bottom and left width in {@code Margin}. If the
     * given argument is already used by another object, then the
     * existing/cloned object will be used. And throws
     * {@code NullValueException} if any of the given argument is null.
     *
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     * @param marginLeft
     * @author WFF
     * @since 1.0.0
     */
    public void setMargin(final MarginTop marginTop,
            final MarginRight marginRight, final MarginBottom marginBottom,
            final MarginLeft marginLeft) {

        if (marginTop != null && marginRight != null && marginBottom != null
                && marginLeft != null) {

            if (MarginTop.INITIAL.equals(marginTop.getCssValue())
                    || MarginTop.INHERIT.equals(marginTop.getCssValue())
                    || MarginRight.INITIAL.equals(marginRight.getCssValue())
                    || MarginRight.INHERIT.equals(marginRight.getCssValue())
                    || MarginBottom.INITIAL.equals(marginBottom.getCssValue())
                    || MarginBottom.INHERIT.equals(marginBottom.getCssValue())
                    || MarginLeft.INITIAL.equals(marginLeft.getCssValue())
                    || MarginLeft.INHERIT.equals(marginLeft.getCssValue())) {
                throw new InvalidValueException(
                        "Any or all of the given arguments have initial/inherit constant value as its cssValue");
            }

            try {
                final MarginTop marginTopTemp;
                final MarginRight marginRightTemp;
                final MarginBottom marginBottomTemp;
                final MarginLeft marginLeftTemp;

                if (this.marginTop != null) {
                    this.marginTop.setAlreadyInUse(false);
                    this.marginRight.setAlreadyInUse(false);
                    this.marginBottom.setAlreadyInUse(false);
                    this.marginLeft.setAlreadyInUse(false);
                }

                if (marginTop.isAlreadyInUse() && this.marginTop != marginTop) {
                    if (this.marginTop != null) {
                        marginTopTemp = this.marginTop
                                .setCssValue(marginTop.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given marginTop is already used by another object so the existing object is used");
                        }
                    } else {
                        marginTopTemp = marginTop.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given marginTop is already used by another object so its clone is assigned");
                        }
                    }
                } else {
                    marginTopTemp = marginTop;
                }

                if (marginRight.isAlreadyInUse()
                        && this.marginRight != marginRight) {
                    if (this.marginRight != null) {
                        marginRightTemp = this.marginRight
                                .setCssValue(marginTop.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given marginRight is already used by another object so the existing object is used");
                        }
                    } else {
                        marginRightTemp = marginRight.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given marginRight is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    marginRightTemp = marginRight;
                }

                if (marginBottom.isAlreadyInUse()
                        && this.marginBottom != marginBottom) {
                    if (this.marginBottom != null) {
                        marginBottomTemp = this.marginBottom
                                .setCssValue(marginTop.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given marginBottom is already used by another object so the existing object is used");
                        }
                    } else {
                        marginBottomTemp = marginBottom.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given marginBottom is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    marginBottomTemp = marginBottom;
                }

                if (marginLeft.isAlreadyInUse()
                        && this.marginLeft != marginLeft) {
                    if (this.marginLeft != null) {
                        marginLeftTemp = this.marginLeft
                                .setCssValue(marginTop.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given marginLeft is already used by another object so the existing object is used");
                        }
                    } else {
                        marginLeftTemp = marginLeft.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given marginLeft is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    marginLeftTemp = marginLeft;
                }

                marginTopTemp.setAlreadyInUse(true);
                marginTopTemp.setStateChangeInformer(this);

                marginRightTemp.setAlreadyInUse(true);
                marginRightTemp.setStateChangeInformer(this);

                marginBottomTemp.setAlreadyInUse(true);
                marginBottomTemp.setStateChangeInformer(this);

                marginLeftTemp.setAlreadyInUse(true);
                marginLeftTemp.setStateChangeInformer(this);

                assignProducedCssValue(marginTop, marginRight, marginBottom,
                        marginLeft);

                this.marginTop = marginTopTemp;
                this.marginRight = marginRightTemp;
                this.marginBottom = marginBottomTemp;
                this.marginLeft = marginLeftTemp;

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
     * @return the marginRight
     * @author WFF
     * @since 1.0.0
     */
    public MarginRight getMarginRight() {
        return marginRight;
    }

    /**
     * @return the marginBottom
     * @author WFF
     * @since 1.0.0
     */
    public MarginBottom getMarginBottom() {
        return marginBottom;
    }

    /**
     * @return the marginLeft
     * @author WFF
     * @since 1.0.0
     */
    public MarginLeft getMarginLeft() {
        return marginLeft;
    }

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    private void assignProducedCssValue(final MarginTop marginTop,
            final MarginRight marginRight, final MarginBottom marginBottom,
            final MarginLeft marginLeft) {

        final String marginTopCssValue = marginTop.getCssValue();
        final String marginRightCssValue = marginRight.getCssValue();
        final String marginBottomCssValue = marginBottom.getCssValue();
        final String marginLeftCssValue = marginLeft.getCssValue();

        if (marginTopCssValue.equals(marginRightCssValue)
                && marginRightCssValue.equals(marginBottomCssValue)
                && marginBottomCssValue.equals(marginLeftCssValue)) {

            cssValue = marginTopCssValue;

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        } else if (marginTopCssValue.equals(marginBottomCssValue)
                && marginRightCssValue.equals(marginLeftCssValue)) {

            final StringBuilder cssValueBuilder = new StringBuilder(
                    marginTopCssValue);
            cssValueBuilder.append(' ').append(marginRightCssValue);

            cssValue = cssValueBuilder.toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }

        } else if (marginRightCssValue.equals(marginLeftCssValue)) {
            final StringBuilder cssValueBuilder = new StringBuilder(
                    marginTopCssValue);
            cssValueBuilder.append(' ').append(marginRightCssValue).append(' ')
                    .append(marginBottomCssValue);

            cssValue = cssValueBuilder.toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        } else {
            cssValue = new StringBuilder(marginTopCssValue).append(' ')
                    .append(marginRightCssValue).append(' ')
                    .append(marginBottomCssValue).append(' ')
                    .append(marginLeftCssValue).toString();

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

        if (stateChangedObject instanceof MarginTop) {
            final MarginTop marginTop = (MarginTop) stateChangedObject;
            if (MarginTop.INITIAL.equals(marginTop.getCssValue())
                    || MarginTop.INHERIT.equals(marginTop.getCssValue())) {
                throw new InvalidValueException(
                        "marginTop cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof MarginRight) {
            final MarginRight marginRight = (MarginRight) stateChangedObject;
            if (MarginRight.INITIAL.equals(marginRight.getCssValue())
                    || MarginRight.INHERIT.equals(marginRight.getCssValue())) {
                throw new InvalidValueException(
                        "marginRight cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof MarginBottom) {
            final MarginBottom marginBottom = (MarginBottom) stateChangedObject;
            if (MarginBottom.INITIAL.equals(marginBottom.getCssValue())
                    || MarginBottom.INHERIT
                            .equals(marginBottom.getCssValue())) {
                throw new InvalidValueException(
                        "marginBottom cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof MarginLeft) {
            final MarginLeft marginLeft = (MarginLeft) stateChangedObject;
            if (MarginLeft.INITIAL.equals(marginLeft.getCssValue())
                    || MarginLeft.INHERIT.equals(marginLeft.getCssValue())) {
                throw new InvalidValueException(
                        "marginLeft cannot have initial/inherit as its cssValue");
            }
        }

        assignProducedCssValue(marginTop, marginRight, marginBottom,
                marginLeft);

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

        if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
            return true;
        }

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);

        for (final String eachPart : cssValueParts) {
            final boolean valid = MarginTop.isValid(eachPart);
            if ((valid
                    && (INITIAL.equals(eachPart) || INHERIT.equals(eachPart)))
                    || !valid) {
                return false;
            }
        }

        return true;
    }
}
