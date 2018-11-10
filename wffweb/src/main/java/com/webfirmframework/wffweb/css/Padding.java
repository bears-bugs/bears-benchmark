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
 * The padding shorthand property sets all the padding properties in one declaration. This property can have from one to four values.
 *
 * Examples:
 *
 *     padding:10px 5px 15px 20px;
 *         top padding is 10px
 *         right padding is 5px
 *         bottom padding is 15px
 *         left padding is 20px
 *
 *     padding:10px 5px 15px;
 *         top padding is 10px
 *         right and left padding are 5px
 *         bottom padding is 15px
 *
 *     padding:10px 5px;
 *         top and bottom padding are 10px
 *         right and left padding are 5px
 *
 *     padding:10px;
 *         all four paddings are 10px
 *
 * Note: Negative values are not allowed.
 * Default value:  0
 * Inherited:      no
 * Animatable:     yes
 *
 * Version:        CSS1
 * JavaScript syntax:      object.style.padding="100px 20px"
 * </pre>
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class Padding extends AbstractCssProperty<Padding>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(Padding.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private PaddingTop paddingTop;
    private PaddingRight paddingRight;
    private PaddingBottom paddingBottom;
    private PaddingLeft paddingLeft;

    /**
     * The {@code initial} will be set as the value
     */
    public Padding() {
        setCssValue(INITIAL);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public Padding(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param paddingBottom
     *            the {@code PaddingBottom} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public Padding(final Padding paddingBottom) {
        if (paddingBottom == null) {
            throw new NullValueException("paddingBottom can not be null");
        }
        setCssValue(paddingBottom.getCssValue());
    }

    /**
     * the padding length to set. The alternative method {@code setCssValue} can
     * also be used.
     *
     * @param value
     * @return the current object
     * @since 1.0.0
     * @author WFF
     */
    public Padding setValue(final String value) {
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
        return CssNameConstants.PADDING;
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
    public Padding setCssValue(final String cssValue) {
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
                if (paddingTop != null) {
                    paddingTop.setAlreadyInUse(false);
                    paddingTop = null;
                }
                if (paddingRight != null) {
                    paddingRight.setAlreadyInUse(false);
                    paddingRight = null;
                }
                if (paddingBottom != null) {
                    paddingBottom.setAlreadyInUse(false);
                    paddingBottom = null;
                }
                if (paddingLeft != null) {
                    paddingLeft.setAlreadyInUse(false);
                    paddingLeft = null;
                }
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }

                return this;
            }

            final String paddingString = StringUtil
                    .convertToSingleSpace(trimmedCssValue);

            final String[] extractedPaddings = StringUtil
                    .splitBySpace(paddingString);

            if (extractedPaddings.length == 1) {
                if (paddingTop == null) {
                    paddingTop = new PaddingTop(extractedPaddings[0]);
                    paddingTop.setStateChangeInformer(this);
                    paddingTop.setAlreadyInUse(true);
                } else {
                    paddingTop.setCssValue(extractedPaddings[0]);
                }
                if (paddingRight == null) {
                    paddingRight = new PaddingRight(extractedPaddings[0]);
                    paddingRight.setStateChangeInformer(this);
                    paddingRight.setAlreadyInUse(true);
                } else {
                    paddingRight.setCssValue(extractedPaddings[0]);
                }
                if (paddingBottom == null) {
                    paddingBottom = new PaddingBottom(extractedPaddings[0]);
                    paddingBottom.setStateChangeInformer(this);
                    paddingBottom.setAlreadyInUse(true);
                } else {
                    paddingBottom.setCssValue(extractedPaddings[0]);
                }
                if (paddingLeft == null) {
                    paddingLeft = new PaddingLeft(extractedPaddings[0]);
                    paddingLeft.setStateChangeInformer(this);
                    paddingLeft.setAlreadyInUse(true);
                } else {
                    paddingLeft.setCssValue(extractedPaddings[0]);
                }
            } else if (extractedPaddings.length == 2) {
                if (paddingTop == null) {
                    paddingTop = new PaddingTop(extractedPaddings[0]);
                    paddingTop.setStateChangeInformer(this);
                    paddingTop.setAlreadyInUse(true);
                } else {
                    paddingTop.setCssValue(extractedPaddings[0]);
                }
                if (paddingBottom == null) {
                    paddingBottom = new PaddingBottom(extractedPaddings[0]);
                    paddingBottom.setStateChangeInformer(this);
                    paddingBottom.setAlreadyInUse(true);
                } else {
                    paddingBottom.setCssValue(extractedPaddings[0]);
                }
                if (paddingRight == null) {
                    paddingRight = new PaddingRight(extractedPaddings[1]);
                    paddingRight.setStateChangeInformer(this);
                    paddingRight.setAlreadyInUse(true);
                } else {
                    paddingRight.setCssValue(extractedPaddings[1]);
                }
                if (paddingLeft == null) {
                    paddingLeft = new PaddingLeft(extractedPaddings[1]);
                    paddingLeft.setStateChangeInformer(this);
                    paddingLeft.setAlreadyInUse(true);
                } else {
                    paddingLeft.setCssValue(extractedPaddings[1]);
                }
            } else if (extractedPaddings.length == 3) {
                if (paddingTop == null) {
                    paddingTop = new PaddingTop(extractedPaddings[0]);
                    paddingTop.setStateChangeInformer(this);
                    paddingTop.setAlreadyInUse(true);
                } else {
                    paddingTop.setCssValue(extractedPaddings[0]);
                }
                if (paddingRight == null) {
                    paddingRight = new PaddingRight(extractedPaddings[1]);
                    paddingRight.setStateChangeInformer(this);
                    paddingRight.setAlreadyInUse(true);
                } else {
                    paddingRight.setCssValue(extractedPaddings[1]);
                }
                if (paddingLeft == null) {
                    paddingLeft = new PaddingLeft(extractedPaddings[1]);
                    paddingLeft.setStateChangeInformer(this);
                    paddingLeft.setAlreadyInUse(true);
                } else {
                    paddingLeft.setCssValue(extractedPaddings[1]);
                }
                if (paddingBottom == null) {
                    paddingBottom = new PaddingBottom(extractedPaddings[2]);
                    paddingBottom.setStateChangeInformer(this);
                    paddingBottom.setAlreadyInUse(true);
                } else {
                    paddingBottom.setCssValue(extractedPaddings[2]);
                }
            } else if (extractedPaddings.length == 4) {
                if (paddingTop == null) {
                    paddingTop = new PaddingTop(extractedPaddings[0]);
                    paddingTop.setStateChangeInformer(this);
                    paddingTop.setAlreadyInUse(true);
                } else {
                    paddingTop.setCssValue(extractedPaddings[0]);
                }
                if (paddingRight == null) {
                    paddingRight = new PaddingRight(extractedPaddings[1]);
                    paddingRight.setStateChangeInformer(this);
                    paddingRight.setAlreadyInUse(true);
                } else {
                    paddingRight.setCssValue(extractedPaddings[1]);
                }
                if (paddingBottom == null) {
                    paddingBottom = new PaddingBottom(extractedPaddings[2]);
                    paddingBottom.setStateChangeInformer(this);
                    paddingBottom.setAlreadyInUse(true);
                } else {
                    paddingBottom.setCssValue(extractedPaddings[2]);
                }
                if (paddingLeft == null) {
                    paddingLeft = new PaddingLeft(extractedPaddings[3]);
                    paddingLeft.setStateChangeInformer(this);
                    paddingLeft.setAlreadyInUse(true);
                } else {
                    paddingLeft.setCssValue(extractedPaddings[3]);
                }
            } else {
                throw new InvalidValueException(
                        "the given cssValue is invalid");
            }

            this.cssValue = paddingString;
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
     * @return the paddingTop
     * @author WFF
     * @since 1.0.0
     */
    public PaddingTop getPaddingTop() {
        return paddingTop;
    }

    /**
     * sets the top, right, bottom and left width in {@code Padding}. If the
     * given argument is already used by another object, then the
     * existing/cloned object will be used. And throws
     * {@code NullValueException} if any of the given argument is null.
     *
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     * @param paddingLeft
     * @author WFF
     * @since 1.0.0
     */
    public void setPadding(final PaddingTop paddingTop,
            final PaddingRight paddingRight, final PaddingBottom paddingBottom,
            final PaddingLeft paddingLeft) {

        if (paddingTop != null && paddingRight != null && paddingBottom != null
                && paddingLeft != null) {

            if (PaddingTop.INITIAL.equals(paddingTop.getCssValue())
                    || PaddingTop.INHERIT.equals(paddingTop.getCssValue())
                    || PaddingRight.INITIAL.equals(paddingRight.getCssValue())
                    || PaddingRight.INHERIT.equals(paddingRight.getCssValue())
                    || PaddingBottom.INITIAL.equals(paddingBottom.getCssValue())
                    || PaddingBottom.INHERIT.equals(paddingBottom.getCssValue())
                    || PaddingLeft.INITIAL.equals(paddingLeft.getCssValue())
                    || PaddingLeft.INHERIT.equals(paddingLeft.getCssValue())) {
                throw new InvalidValueException(
                        "Any or all of the given arguments have initial/inherit constant value as its cssValue");
            }

            try {
                final PaddingTop paddingTopTemp;
                final PaddingRight paddingRightTemp;
                final PaddingBottom paddingBottomTemp;
                final PaddingLeft paddingLeftTemp;

                if (this.paddingTop != null) {
                    this.paddingTop.setAlreadyInUse(false);
                    this.paddingRight.setAlreadyInUse(false);
                    this.paddingBottom.setAlreadyInUse(false);
                    this.paddingLeft.setAlreadyInUse(false);
                }

                if (paddingTop.isAlreadyInUse()
                        && this.paddingTop != paddingTop) {
                    if (this.paddingTop != null) {
                        paddingTopTemp = this.paddingTop
                                .setCssValue(paddingTop.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given paddingTop is already used by another object so the existing object is used");
                        }
                    } else {
                        paddingTopTemp = paddingTop.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given paddingTop is already used by another object so its clone is assigned");
                        }
                    }
                } else {
                    paddingTopTemp = paddingTop;
                }

                if (paddingRight.isAlreadyInUse()
                        && this.paddingRight != paddingRight) {
                    if (this.paddingRight != null) {
                        paddingRightTemp = this.paddingRight
                                .setCssValue(paddingTop.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given paddingRight is already used by another object so the existing object is used");
                        }
                    } else {
                        paddingRightTemp = paddingRight.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given paddingRight is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    paddingRightTemp = paddingRight;
                }

                if (paddingBottom.isAlreadyInUse()
                        && this.paddingBottom != paddingBottom) {
                    if (this.paddingBottom != null) {
                        paddingBottomTemp = this.paddingBottom
                                .setCssValue(paddingTop.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given paddingBottom is already used by another object so the existing object is used");
                        }
                    } else {
                        paddingBottomTemp = paddingBottom.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given paddingBottom is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    paddingBottomTemp = paddingBottom;
                }

                if (paddingLeft.isAlreadyInUse()
                        && this.paddingLeft != paddingLeft) {
                    if (this.paddingLeft != null) {
                        paddingLeftTemp = this.paddingLeft
                                .setCssValue(paddingTop.getCssValue());
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given paddingLeft is already used by another object so the existing object is used");
                        }
                    } else {
                        paddingLeftTemp = paddingLeft.clone();
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning(
                                    "the given paddingLeft is already used by another object so its clone is assigned");
                        }
                    }

                } else {
                    paddingLeftTemp = paddingLeft;
                }

                paddingTopTemp.setAlreadyInUse(true);
                paddingTopTemp.setStateChangeInformer(this);

                paddingRightTemp.setAlreadyInUse(true);
                paddingRightTemp.setStateChangeInformer(this);

                paddingBottomTemp.setAlreadyInUse(true);
                paddingBottomTemp.setStateChangeInformer(this);

                paddingLeftTemp.setAlreadyInUse(true);
                paddingLeftTemp.setStateChangeInformer(this);

                assignProducedCssValue(paddingTop, paddingRight, paddingBottom,
                        paddingLeft);

                this.paddingTop = paddingTopTemp;
                this.paddingRight = paddingRightTemp;
                this.paddingBottom = paddingBottomTemp;
                this.paddingLeft = paddingLeftTemp;

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
     * @return the paddingRight
     * @author WFF
     * @since 1.0.0
     */
    public PaddingRight getPaddingRight() {
        return paddingRight;
    }

    /**
     * @return the paddingBottom
     * @author WFF
     * @since 1.0.0
     */
    public PaddingBottom getPaddingBottom() {
        return paddingBottom;
    }

    /**
     * @return the paddingLeft
     * @author WFF
     * @since 1.0.0
     */
    public PaddingLeft getPaddingLeft() {
        return paddingLeft;
    }

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    private void assignProducedCssValue(final PaddingTop paddingTop,
            final PaddingRight paddingRight, final PaddingBottom paddingBottom,
            final PaddingLeft paddingLeft) {

        final String paddingTopCssValue = paddingTop.getCssValue();
        final String paddingRightCssValue = paddingRight.getCssValue();
        final String paddingBottomCssValue = paddingBottom.getCssValue();
        final String paddingLeftCssValue = paddingLeft.getCssValue();

        if (paddingTopCssValue.equals(paddingRightCssValue)
                && paddingRightCssValue.equals(paddingBottomCssValue)
                && paddingBottomCssValue.equals(paddingLeftCssValue)) {

            cssValue = paddingTopCssValue;

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        } else if (paddingTopCssValue.equals(paddingBottomCssValue)
                && paddingRightCssValue.equals(paddingLeftCssValue)) {

            final StringBuilder cssValueBuilder = new StringBuilder(
                    paddingTopCssValue);
            cssValueBuilder.append(' ').append(paddingRightCssValue);

            cssValue = cssValueBuilder.toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }

        } else if (paddingRightCssValue.equals(paddingLeftCssValue)) {
            cssValue = new StringBuilder(paddingTopCssValue).append(' ')
                    .append(paddingRightCssValue).append(' ')
                    .append(paddingBottomCssValue).toString();

            final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
            if (stateChangeInformer != null) {
                stateChangeInformer.stateChanged(this);
            }
        } else {

            cssValue = new StringBuilder(paddingTopCssValue).append(' ')
                    .append(paddingRightCssValue).append(' ')
                    .append(paddingBottomCssValue).append(' ')
                    .append(paddingLeftCssValue).toString();

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

        if (stateChangedObject instanceof PaddingTop) {
            final PaddingTop paddingTop = (PaddingTop) stateChangedObject;
            if (PaddingTop.INITIAL.equals(paddingTop.getCssValue())
                    || PaddingTop.INHERIT.equals(paddingTop.getCssValue())) {
                throw new InvalidValueException(
                        "paddingTop cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof PaddingRight) {
            final PaddingRight paddingRight = (PaddingRight) stateChangedObject;
            if (PaddingRight.INITIAL.equals(paddingRight.getCssValue())
                    || PaddingRight.INHERIT
                            .equals(paddingRight.getCssValue())) {
                throw new InvalidValueException(
                        "paddingRight cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof PaddingBottom) {
            final PaddingBottom paddingBottom = (PaddingBottom) stateChangedObject;
            if (PaddingBottom.INITIAL.equals(paddingBottom.getCssValue())
                    || PaddingBottom.INHERIT
                            .equals(paddingBottom.getCssValue())) {
                throw new InvalidValueException(
                        "paddingBottom cannot have initial/inherit as its cssValue");
            }
        } else if (stateChangedObject instanceof PaddingLeft) {
            final PaddingLeft paddingLeft = (PaddingLeft) stateChangedObject;
            if (PaddingLeft.INITIAL.equals(paddingLeft.getCssValue())
                    || PaddingLeft.INHERIT.equals(paddingLeft.getCssValue())) {
                throw new InvalidValueException(
                        "paddingLeft cannot have initial/inherit as its cssValue");
            }
        }

        assignProducedCssValue(paddingTop, paddingRight, paddingBottom,
                paddingLeft);

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

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);

        for (final String eachPart : cssValueParts) {
            final boolean valid = PaddingTop.isValid(eachPart);
            if ((valid
                    && (INITIAL.equals(eachPart) || INHERIT.equals(eachPart)))
                    || !valid) {
                return false;
            }
        }

        return true;
    }
}
