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
 * -ms-flex: <i>-flex-grow -flex-shrink -flex-basis</i>|auto|initial|inherit;
 *
 * The flex property specifies the length of the item, relative to the rest of the flexible items inside the same container.
 *
 * The flex property is a shorthand for the -ms-flex-grow, -ms-flex-shrink, and the -ms-flex-basis properties.
 *
 * Note: If the element is not a flexible item, the flex property has no effect.
 * Default value:  0 1 auto
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS3
 * JavaScript syntax:      object.style.msFlex="1"
 *
 * <i>
 * The undefined value for -flex-grow, -flex-shrink and -flex-basis in -ms-flex property is 1, 1 and 0% respectively.
 * </i>
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class MsFlex extends AbstractCssProperty<MsFlex>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(MsFlex.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String AUTO = "auto";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, AUTO);

    private String cssValue;

    private FlexGrow flexGrow;

    private FlexShrink flexShrink;

    private FlexBasis flexBasis;

    /**
     * The value <code>0 1 auto</code> will be assigned as the cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public MsFlex() {
        setCssValue("0 1 auto");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public MsFlex(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param msFlex
     *            the {@code Flex} object from which the cssValue to set.And,
     *            {@code null} will throw {@code NullValueException}
     */
    public MsFlex(final MsFlex msFlex) {
        if (msFlex == null) {
            throw new NullValueException("msFlex can not be null");
        }
        setCssValue(msFlex.getCssValue());
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
        return CssNameConstants.MS_FLEX;
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
    public MsFlex setCssValue(final String cssValue) {
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
            if (trimmedCssValue.contains(FlexBasis.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(FlexBasis.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                if (flexGrow != null) {
                    flexGrow.setAlreadyInUse(false);
                    flexGrow = null;
                }
                if (flexShrink != null) {
                    flexShrink.setAlreadyInUse(false);
                    flexShrink = null;
                }
                if (flexBasis != null) {
                    flexBasis.setAlreadyInUse(false);
                    flexBasis = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        FlexGrow flexGrow = null;
        FlexShrink flexShrink = null;
        FlexBasis flexBasis = null;

        for (final String eachPart : cssValueParts) {
            if (flexGrow == null && FlexGrow.isValid(eachPart)) {
                if (this.flexGrow == null) {
                    flexGrow = new FlexGrow(eachPart);
                    flexGrow.setStateChangeInformer(this);
                    flexGrow.setAlreadyInUse(true);
                } else {
                    this.flexGrow.setCssValue(eachPart);
                    flexGrow = this.flexGrow;
                }
            } else if (flexShrink == null && FlexShrink.isValid(eachPart)) {
                if (this.flexGrow == null) {
                    flexShrink = new FlexShrink(eachPart);
                    flexShrink.setStateChangeInformer(this);
                    flexShrink.setAlreadyInUse(true);
                } else {
                    this.flexShrink.setCssValue(eachPart);
                    flexShrink = this.flexShrink;
                }
            } else if (flexBasis == null && FlexBasis.isValid(eachPart)) {
                if (this.flexBasis == null) {
                    flexBasis = new FlexBasis(eachPart);
                    flexBasis.setStateChangeInformer(this);
                    flexBasis.setAlreadyInUse(true);
                } else {
                    this.flexBasis.setCssValue(eachPart);
                    flexBasis = this.flexBasis;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (flexGrow == null) {
            if (this.flexGrow == null) {
                flexGrow = new FlexGrow(1);
                flexGrow.setStateChangeInformer(this);
                flexGrow.setAlreadyInUse(true);
            } else {
                this.flexGrow.setValue(0);
                flexGrow = this.flexGrow;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(flexGrow.getCssValue()).append(' ');

        if (flexShrink == null) {
            if (this.flexShrink == null) {
                flexShrink = new FlexShrink(1);
                flexShrink.setStateChangeInformer(this);
                flexShrink.setAlreadyInUse(true);
            } else {
                this.flexShrink.setValue(1);
                flexShrink = this.flexShrink;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(flexShrink.getCssValue()).append(' ');

        if (flexBasis == null) {
            if (cssValueParts.length == 3) {
                throw new InvalidValueException(
                        "the given cssValue contains invalid flex-basis value.");
            }
            if (this.flexBasis == null) {
                flexBasis = new FlexBasis(0);
                flexBasis.setStateChangeInformer(this);
                flexBasis.setAlreadyInUse(true);
            } else {
                this.flexBasis.setAsAuto();
                flexBasis = this.flexBasis;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(flexBasis.getCssValue());

        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example \"0 1 auto\" or initial/inherit.");
        }
        this.cssValue = cssValueBuilder.toString();

        flexGrow.setStateChangeInformer(this);
        flexGrow.setAlreadyInUse(true);

        flexShrink.setStateChangeInformer(this);
        flexShrink.setAlreadyInUse(true);

        flexBasis.setStateChangeInformer(this);
        flexBasis.setAlreadyInUse(true);

        this.flexGrow = flexGrow;
        this.flexShrink = flexShrink;
        this.flexBasis = flexBasis;

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

        FlexGrow flexGrow = null;
        FlexShrink flexShrink = null;
        FlexBasis flexBasis = null;

        for (final String eachPart : cssValueParts) {
            boolean invalid = true;

            if (flexGrow == null && FlexGrow.isValid(eachPart)) {
                flexGrow = new FlexGrow(eachPart);
                invalid = false;
            } else if (flexShrink == null && FlexShrink.isValid(eachPart)) {
                flexShrink = new FlexShrink(eachPart);
                invalid = false;
            } else if (flexBasis == null && FlexBasis.isValid(eachPart)) {
                flexBasis = new FlexBasis(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return flexGrow != null || flexShrink != null || flexBasis != null;
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

    public FlexBasis getFlexBasis() {
        return flexBasis;
    }

    public FlexShrink getFlexShrink() {
        return flexShrink;
    }

    public FlexGrow getFlexGrow() {
        return flexGrow;
    }

    public MsFlex setFlexGrow(final FlexGrow flexGrow) {
        if (flexGrow == null) {
            throw new InvalidValueException("flexGrow cannot be null");
        }

        if (Objects.equals(this.flexGrow, flexGrow)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (this.flexGrow != null) {
            this.flexGrow.setCssValue(flexGrow.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given flexGrow to the existing flexGrow object.");
            }
        } else {
            if (flexGrow.isAlreadyInUse()) {
                this.flexGrow = new FlexGrow(flexGrow);
                this.flexGrow.setAlreadyInUse(true);
                this.flexGrow.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of FlexGrow as the given flexGrow object is already used by another object");
                }
            } else {
                this.flexGrow = flexGrow;
                this.flexGrow.setAlreadyInUse(true);
                this.flexGrow.setStateChangeInformer(this);
            }
        }

        cssValueBuilder.append(flexGrow.getCssValue()).append(' ');

        if (flexShrink == null) {
            flexShrink = new FlexShrink(1);
            flexShrink.setStateChangeInformer(this);
            flexShrink.setAlreadyInUse(true);
        }

        cssValueBuilder.append(flexShrink.getCssValue()).append(' ');

        if (flexBasis == null) {
            flexBasis = new FlexBasis(0);
            flexBasis.setStateChangeInformer(this);
            flexBasis.setAlreadyInUse(true);
        }

        cssValueBuilder.append(flexBasis.getCssValue());

        cssValue = cssValueBuilder.toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public MsFlex setFlexShrink(final FlexShrink flexShrink) {
        if (flexShrink == null) {
            throw new InvalidValueException("flexShrink cannot be null");
        }

        if (Objects.equals(this.flexShrink, flexShrink)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (flexGrow == null) {
            flexGrow = new FlexGrow(1);
            flexGrow.setStateChangeInformer(this);
            flexGrow.setAlreadyInUse(true);
        }

        cssValueBuilder.append(flexGrow.getCssValue()).append(' ');

        if (this.flexShrink != null) {
            this.flexShrink.setCssValue(flexShrink.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given flexShrink to the existing flexShrink object.");
            }
        } else {
            if (flexShrink.isAlreadyInUse()) {
                this.flexShrink = new FlexShrink(flexShrink);
                this.flexShrink.setAlreadyInUse(true);
                this.flexShrink.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of FlexShrink as the given flexShrink object is already used by another object");
                }
            } else {
                this.flexShrink = flexShrink;
                this.flexShrink.setAlreadyInUse(true);
                this.flexShrink.setStateChangeInformer(this);
            }
        }

        cssValueBuilder.append(flexShrink.getCssValue()).append(' ');

        if (flexBasis == null) {
            flexBasis = new FlexBasis(0);
            flexBasis.setStateChangeInformer(this);
            flexBasis.setAlreadyInUse(true);
        }

        cssValueBuilder.append(flexBasis.getCssValue());

        cssValue = cssValueBuilder.toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public MsFlex setFlexBasis(final FlexBasis flexBasis) {
        if (flexBasis == null) {
            throw new InvalidValueException("flexBasis cannot be null");
        }

        if (Objects.equals(this.flexBasis, flexBasis)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (flexGrow == null) {
            flexGrow = new FlexGrow(1);
            flexGrow.setStateChangeInformer(this);
            flexGrow.setAlreadyInUse(true);
        }

        cssValueBuilder.append(flexGrow.getCssValue()).append(' ');

        if (flexShrink == null) {
            flexShrink = new FlexShrink(1);
            flexShrink.setStateChangeInformer(this);
            flexShrink.setAlreadyInUse(true);
        }

        cssValueBuilder.append(flexShrink.getCssValue()).append(' ');

        if (this.flexBasis != null) {
            this.flexBasis.setCssValue(flexBasis.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given flexBasis to the existing flexBasis object.");
            }
        } else {
            if (flexBasis.isAlreadyInUse()) {
                this.flexBasis = new FlexBasis(flexBasis);
                this.flexBasis.setAlreadyInUse(true);
                this.flexBasis.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of FlexBasis as the given flexBasis object is already used by another object");
                }
            } else {
                this.flexBasis = flexBasis;
                this.flexBasis.setAlreadyInUse(true);
                this.flexBasis.setStateChangeInformer(this);
            }
        }

        cssValue = cssValueBuilder.append(flexBasis.getCssValue()).toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof FlexGrow) {

            final FlexGrow flexGrow = (FlexGrow) stateChangedObject;

            final String cssValue = flexGrow.getCssValue();

            if (FlexGrow.INITIAL.equals(cssValue)
                    || FlexGrow.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as flexGrow cssValue");
            }
            this.cssValue = new StringBuilder(flexGrow.getCssValue())
                    .append(' ').append(flexShrink.getCssValue()).append(' ')
                    .append(flexBasis.getCssValue()).toString();
        } else if (stateChangedObject instanceof FlexShrink) {

            final FlexShrink flexShrink = (FlexShrink) stateChangedObject;

            final String cssValue = flexShrink.getCssValue();

            if (FlexShrink.INITIAL.equals(cssValue)
                    || FlexShrink.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as flexShrink cssValue");
            }
            this.cssValue = new StringBuilder(flexGrow.getCssValue())
                    .append(' ').append(flexShrink.getCssValue()).append(' ')
                    .append(flexBasis.getCssValue()).toString();

        } else if (stateChangedObject instanceof FlexBasis) {

            final FlexBasis flexBasis = (FlexBasis) stateChangedObject;

            final String cssValue = flexBasis.getCssValue();

            if (FlexBasis.INITIAL.equals(cssValue)
                    || FlexBasis.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as flexBasis cssValue");
            }

            this.cssValue = new StringBuilder(flexGrow.getCssValue())
                    .append(' ').append(flexShrink.getCssValue()).append(' ')
                    .append(flexBasis.getCssValue()).toString();
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
