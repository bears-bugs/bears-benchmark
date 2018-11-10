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
package com.webfirmframework.wffweb.css.css3;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 *
 * -moz-flex: <i>-moz-flex-grow -moz-flex-shrink -moz-flex-basis</i>|auto|initial|inherit;
 *
 * The flex property specifies the length of the item, relative to the rest of the flexible items inside the same container.
 *
 * The flex property is a shorthand for the -moz-flex-grow, -moz-flex-shrink, and the -moz-flex-basis properties.
 *
 * Note: If the element is not a flexible item, the flex property has no effect.
 * Default value:  0 1 auto
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS3
 * JavaScript syntax:      object.style.mozFlex="1"
 *
 * <i>
 * The undefined value for -moz-flex-grow, -moz-flex-shrink and -moz-flex-basis in -moz-flex property is 1, 1 and 0% respectively.
 * </i>
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class MozFlex extends AbstractCssProperty<MozFlex>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    // NB : it should not override equals and hashcode methods as its objects
    // are equalized by Objects.equals(obj1, obj2) method in some places.

    public static final Logger LOGGER = Logger
            .getLogger(MozFlex.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String AUTO = "auto";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, AUTO);

    private String cssValue;

    private MozFlexGrow mozFlexGrow;

    private MozFlexShrink mozFlexShrink;

    private MozFlexBasis mozFlexBasis;

    /**
     * The value <code>0 1 auto</code> will be assigned as the cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public MozFlex() {
        setCssValue("0 1 auto");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public MozFlex(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param mozFlex
     *            the {@code Flex} object from which the cssValue to set.And,
     *            {@code null} will throw {@code NullValueException}
     */
    public MozFlex(final MozFlex mozFlex) {
        if (mozFlex == null) {
            throw new NullValueException("mozFlex can not be null");
        }
        setCssValue(mozFlex.getCssValue());
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
        return CssNameConstants.MOZ_FLEX;
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
     *            the value should be in the format of <code>1 1 auto</code> or
     *            <code>1 1 15px</code>. {@code null} is considered as an
     *            invalid value and it will throw {@code NullValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public MozFlex setCssValue(final String cssValue) {
        final String trimmedCssValue;
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example \"0 1 auto\" or initial/inherit.");
        } else if ((trimmedCssValue = TagStringUtil
                .toLowerCase(StringUtil.strip(cssValue))).isEmpty()) {
            throw new NullValueException(cssValue
                    + " is an invalid value. The value format should be as for example \"0 1 auto\" or initial/inherit.");
        }

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);

        if (cssValueParts.length > 1) {
            if (trimmedCssValue.contains(MozFlexBasis.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(MozFlexBasis.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                if (mozFlexGrow != null) {
                    mozFlexGrow.setAlreadyInUse(false);
                    mozFlexGrow = null;
                }
                if (mozFlexShrink != null) {
                    mozFlexShrink.setAlreadyInUse(false);
                    mozFlexShrink = null;
                }
                if (mozFlexBasis != null) {
                    mozFlexBasis.setAlreadyInUse(false);
                    mozFlexBasis = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        MozFlexGrow mozFlexGrow = null;
        MozFlexShrink mozFlexShrink = null;
        MozFlexBasis mozFlexBasis = null;

        for (final String eachPart : cssValueParts) {
            if (mozFlexGrow == null && MozFlexGrow.isValid(eachPart)) {
                if (this.mozFlexGrow == null) {
                    mozFlexGrow = new MozFlexGrow(eachPart);
                    mozFlexGrow.setStateChangeInformer(this);
                    mozFlexGrow.setAlreadyInUse(true);
                } else {
                    this.mozFlexGrow.setCssValue(eachPart);
                    mozFlexGrow = this.mozFlexGrow;
                }
            } else if (mozFlexShrink == null
                    && MozFlexShrink.isValid(eachPart)) {
                if (this.mozFlexGrow == null) {
                    mozFlexShrink = new MozFlexShrink(eachPart);
                    mozFlexShrink.setStateChangeInformer(this);
                    mozFlexShrink.setAlreadyInUse(true);
                } else {
                    this.mozFlexShrink.setCssValue(eachPart);
                    mozFlexShrink = this.mozFlexShrink;
                }
            } else if (mozFlexBasis == null && MozFlexBasis.isValid(eachPart)) {
                if (this.mozFlexBasis == null) {
                    mozFlexBasis = new MozFlexBasis(eachPart);
                    mozFlexBasis.setStateChangeInformer(this);
                    mozFlexBasis.setAlreadyInUse(true);
                } else {
                    this.mozFlexBasis.setCssValue(eachPart);
                    mozFlexBasis = this.mozFlexBasis;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (mozFlexGrow == null) {
            if (this.mozFlexGrow == null) {
                mozFlexGrow = new MozFlexGrow(1);
                mozFlexGrow.setStateChangeInformer(this);
                mozFlexGrow.setAlreadyInUse(true);
            } else {
                this.mozFlexGrow.setValue(0);
                mozFlexGrow = this.mozFlexGrow;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(mozFlexGrow.getCssValue()).append(' ');

        if (mozFlexShrink == null) {
            if (this.mozFlexShrink == null) {
                mozFlexShrink = new MozFlexShrink(1);
                mozFlexShrink.setStateChangeInformer(this);
                mozFlexShrink.setAlreadyInUse(true);
            } else {
                this.mozFlexShrink.setValue(1);
                mozFlexShrink = this.mozFlexShrink;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(mozFlexShrink.getCssValue()).append(' ');

        if (mozFlexBasis == null) {
            if (cssValueParts.length == 3) {
                throw new InvalidValueException(
                        "the given cssValue contains invalid flex-basis value.");
            }
            if (this.mozFlexBasis == null) {
                mozFlexBasis = new MozFlexBasis(0);
                mozFlexBasis.setStateChangeInformer(this);
                mozFlexBasis.setAlreadyInUse(true);
            } else {
                this.mozFlexBasis.setAsAuto();
                mozFlexBasis = this.mozFlexBasis;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(mozFlexBasis.getCssValue());

        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example \"0 1 auto\" or initial/inherit.");
        }
        this.cssValue = cssValueBuilder.toString();

        mozFlexGrow.setStateChangeInformer(this);
        mozFlexGrow.setAlreadyInUse(true);

        mozFlexShrink.setStateChangeInformer(this);
        mozFlexShrink.setAlreadyInUse(true);

        mozFlexBasis.setStateChangeInformer(this);
        mozFlexBasis.setAlreadyInUse(true);

        this.mozFlexGrow = mozFlexGrow;
        this.mozFlexShrink = mozFlexShrink;
        this.mozFlexBasis = mozFlexBasis;

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

        final String[] cssValueParts = StringUtil.splitBySpace(cssValue);

        MozFlexGrow mozFlexGrow = null;
        MozFlexShrink mozFlexShrink = null;
        MozFlexBasis mozFlexBasis = null;

        for (final String eachPart : cssValueParts) {
            boolean invalid = true;

            if (mozFlexGrow == null && MozFlexGrow.isValid(eachPart)) {
                mozFlexGrow = new MozFlexGrow(eachPart);
                invalid = false;
            } else if (mozFlexShrink == null
                    && MozFlexShrink.isValid(eachPart)) {
                mozFlexShrink = new MozFlexShrink(eachPart);
                invalid = false;
            } else if (mozFlexBasis == null && MozFlexBasis.isValid(eachPart)) {
                mozFlexBasis = new MozFlexBasis(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return mozFlexGrow != null || mozFlexShrink != null
                || mozFlexBasis != null;
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
     * sets as auto
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsAuto() {
        setCssValue(AUTO);
    }

    public MozFlexBasis getMozFlexBasis() {
        return mozFlexBasis;
    }

    public MozFlexShrink getMozFlexShrink() {
        return mozFlexShrink;
    }

    public MozFlexGrow getMozFlexGrow() {
        return mozFlexGrow;
    }

    public MozFlex setMozFlexGrow(final MozFlexGrow mozFlexGrow) {
        if (mozFlexGrow == null) {
            throw new InvalidValueException("mozFlexGrow cannot be null");
        }

        if (Objects.equals(this.mozFlexGrow, mozFlexGrow)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (this.mozFlexGrow != null) {
            this.mozFlexGrow.setCssValue(mozFlexGrow.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given mozFlexGrow to the existing mozFlexGrow object.");
            }
        } else {
            if (mozFlexGrow.isAlreadyInUse()) {
                this.mozFlexGrow = new MozFlexGrow(mozFlexGrow);
                this.mozFlexGrow.setAlreadyInUse(true);
                this.mozFlexGrow.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of MozFlexGrow as the given mozFlexGrow object is already used by another object");
                }
            } else {
                this.mozFlexGrow = mozFlexGrow;
                this.mozFlexGrow.setAlreadyInUse(true);
                this.mozFlexGrow.setStateChangeInformer(this);
            }
        }

        cssValueBuilder.append(mozFlexGrow.getCssValue()).append(' ');

        if (mozFlexShrink == null) {
            mozFlexShrink = new MozFlexShrink(1);
            mozFlexShrink.setStateChangeInformer(this);
            mozFlexShrink.setAlreadyInUse(true);
        }

        cssValueBuilder.append(mozFlexShrink.getCssValue()).append(' ');

        if (mozFlexBasis == null) {
            mozFlexBasis = new MozFlexBasis(0);
            mozFlexBasis.setStateChangeInformer(this);
            mozFlexBasis.setAlreadyInUse(true);
        }

        cssValueBuilder.append(mozFlexBasis.getCssValue());

        cssValue = cssValueBuilder.toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public MozFlex setMozFlexShrink(final MozFlexShrink mozFlexShrink) {
        if (mozFlexShrink == null) {
            throw new InvalidValueException("mozFlexShrink cannot be null");
        }

        if (Objects.equals(this.mozFlexShrink, mozFlexShrink)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (mozFlexGrow == null) {
            mozFlexGrow = new MozFlexGrow(1);
            mozFlexGrow.setStateChangeInformer(this);
            mozFlexGrow.setAlreadyInUse(true);
        }

        cssValueBuilder.append(mozFlexGrow.getCssValue()).append(' ');

        if (this.mozFlexShrink != null) {
            this.mozFlexShrink.setCssValue(mozFlexShrink.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given mozFlexShrink to the existing mozFlexShrink object.");
            }
        } else {
            if (mozFlexShrink.isAlreadyInUse()) {
                this.mozFlexShrink = new MozFlexShrink(mozFlexShrink);
                this.mozFlexShrink.setAlreadyInUse(true);
                this.mozFlexShrink.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of MozFlexShrink as the given mozFlexShrink object is already used by another object");
                }
            } else {
                this.mozFlexShrink = mozFlexShrink;
                this.mozFlexShrink.setAlreadyInUse(true);
                this.mozFlexShrink.setStateChangeInformer(this);
            }
        }

        cssValueBuilder.append(mozFlexShrink.getCssValue()).append(' ');

        if (mozFlexBasis == null) {
            mozFlexBasis = new MozFlexBasis(0);
            mozFlexBasis.setStateChangeInformer(this);
            mozFlexBasis.setAlreadyInUse(true);
        }

        cssValueBuilder.append(mozFlexBasis.getCssValue());

        cssValue = cssValueBuilder.toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public MozFlex setMozFlexBasis(final MozFlexBasis mozFlexBasis) {
        if (mozFlexBasis == null) {
            throw new InvalidValueException("mozFlexBasis cannot be null");
        }

        if (Objects.equals(this.mozFlexBasis, mozFlexBasis)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (mozFlexGrow == null) {
            mozFlexGrow = new MozFlexGrow(1);
            mozFlexGrow.setStateChangeInformer(this);
            mozFlexGrow.setAlreadyInUse(true);
        }

        cssValueBuilder.append(mozFlexGrow.getCssValue()).append(' ');

        if (mozFlexShrink == null) {
            mozFlexShrink = new MozFlexShrink(1);
            mozFlexShrink.setStateChangeInformer(this);
            mozFlexShrink.setAlreadyInUse(true);
        }

        cssValueBuilder.append(mozFlexShrink.getCssValue()).append(' ');

        if (this.mozFlexBasis != null) {
            this.mozFlexBasis.setCssValue(mozFlexBasis.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given mozFlexBasis to the existing mozFlexBasis object.");
            }
        } else {
            if (mozFlexBasis.isAlreadyInUse()) {
                this.mozFlexBasis = new MozFlexBasis(mozFlexBasis);
                this.mozFlexBasis.setAlreadyInUse(true);
                this.mozFlexBasis.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of MozFlexBasis as the given mozFlexBasis object is already used by another object");
                }
            } else {
                this.mozFlexBasis = mozFlexBasis;
                this.mozFlexBasis.setAlreadyInUse(true);
                this.mozFlexBasis.setStateChangeInformer(this);
            }
        }

        cssValueBuilder.append(mozFlexBasis.getCssValue());

        cssValue = cssValueBuilder.toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof MozFlexGrow) {

            final MozFlexGrow mozFlexGrow = (MozFlexGrow) stateChangedObject;

            final String cssValue = mozFlexGrow.getCssValue();

            if (MozFlexGrow.INITIAL.equals(cssValue)
                    || MozFlexGrow.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as mozFlexGrow cssValue");
            }
            this.cssValue = new StringBuilder(mozFlexGrow.getCssValue())
                    .append(' ').append(mozFlexShrink.getCssValue()).append(' ')
                    .append(mozFlexBasis.getCssValue()).toString();
        } else if (stateChangedObject instanceof MozFlexShrink) {

            final MozFlexShrink mozFlexShrink = (MozFlexShrink) stateChangedObject;

            final String cssValue = mozFlexShrink.getCssValue();

            if (MozFlexShrink.INITIAL.equals(cssValue)
                    || MozFlexShrink.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as mozFlexShrink cssValue");
            }
            this.cssValue = new StringBuilder(mozFlexGrow.getCssValue())
                    .append(' ').append(mozFlexShrink.getCssValue()).append(' ')
                    .append(mozFlexBasis.getCssValue()).toString();

        } else if (stateChangedObject instanceof MozFlexBasis) {

            final MozFlexBasis mozFlexBasis = (MozFlexBasis) stateChangedObject;

            final String cssValue = mozFlexBasis.getCssValue();

            if (MozFlexBasis.INITIAL.equals(cssValue)
                    || MozFlexBasis.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as mozFlexBasis cssValue");
            }

            this.cssValue = new StringBuilder(mozFlexGrow.getCssValue())
                    .append(' ').append(mozFlexShrink.getCssValue()).append(' ')
                    .append(mozFlexBasis.getCssValue()).toString();
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
