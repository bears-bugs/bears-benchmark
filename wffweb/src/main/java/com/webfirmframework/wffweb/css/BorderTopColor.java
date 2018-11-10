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
import com.webfirmframework.wffweb.clone.CloneUtil;
import com.webfirmframework.wffweb.core.constants.CommonConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.data.Bean;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * border-top-color: <i>color</i>|transparent|initial|inherit;
 *
 *  The border-top-color property sets the color of an element's top border.
 *
 * Note: Always declare the border-style property before the border-top-color property. An element must have borders before you can change the color.
 * Default value:  The current color of the element
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS1
 * JavaScript syntax:      <i>object</i>.style.borderTopColor="blue"
 * </pre>
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderTopColor extends AbstractCssProperty<BorderTopColor>
        implements StateChangeInformer<Bean> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(BorderTopColor.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String TRANSPARENT = "transparent";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, TRANSPARENT);

    private String cssValue;

    private RgbCssValue rgbCssValue;

    private RgbaCssValue rgbaCssValue;

    private HslCssValue hslCssValue;

    private HslaCssValue hslaCssValue;

    /**
     * The {@code initial} will be set as the value
     */
    public BorderTopColor() {
        setCssValue(INITIAL);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderTopColor(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderTopColor
     *            the {@code BorderTopColor} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderTopColor(final BorderTopColor borderTopColor) {
        if (borderTopColor == null) {
            throw new NullValueException("borderTopColor can not be null");
        }
        setCssValue(borderTopColor.getCssValue());
    }

    /**
     * @param rgbCssValue
     */
    public BorderTopColor(final RgbCssValue rgbCssValue) {
        super();
        setRgbCssValue(rgbCssValue);
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
    public BorderTopColor setValue(final String value) {
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
        return CssNameConstants.BORDER_TOP_COLOR;
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
     *            the value should be a color/color code/rgb, for example
     *            <code>#0000ff</code>. {@code null} is considered as an invalid
     *            value and it will throw {@code NullValueException}.And an
     *            empty string is also considered as an invalid value and it
     *            will throw {@code InvalidValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public BorderTopColor setCssValue(final String cssValue) {
        final String previousCssValue = this.cssValue;
        try {
            if (cssValue == null) {
                throw new NullValueException(
                        "null is an invalid value. The value should be any color for example #0000ff, rgb(15, 25, 155) Or, initial/inherit/transparent.");
            } else if (!isValid(cssValue)) {
                throw new InvalidValueException(cssValue
                        + " is an invalid value. The value should be any color for example #0000ff, rgb(15, 25, 155) Or, initial/inherit/transparent.");
            } else {
                this.cssValue = StringUtil.strip(cssValue);
                if (RgbCssValue.isValid(cssValue)) {
                    if (rgbCssValue == null) {
                        rgbCssValue = new RgbCssValue(cssValue);
                    } else {
                        rgbCssValue.setRgb(this.cssValue);
                    }
                    rgbCssValue.setStateChangeInformer(this);
                } else {
                    makeRgbCssValueNull();
                }

                if (RgbaCssValue.isValid(cssValue)) {
                    if (rgbaCssValue == null) {
                        rgbaCssValue = new RgbaCssValue(cssValue);
                    } else {
                        rgbaCssValue.setRgba(this.cssValue);
                    }
                    rgbaCssValue.setStateChangeInformer(this);
                } else {
                    makeRgbaCssValueNull();
                }

                if (HslCssValue.isValid(cssValue)) {
                    if (hslCssValue == null) {
                        hslCssValue = new HslCssValue(cssValue);
                    } else {
                        hslCssValue.setHsl(this.cssValue);
                    }
                    hslCssValue.setStateChangeInformer(this);
                } else {
                    makeHslCssValueNull();
                }

                if (HslaCssValue.isValid(cssValue)) {
                    if (hslaCssValue == null) {
                        hslaCssValue = new HslaCssValue(cssValue);
                    } else {
                        hslaCssValue.setHsla(this.cssValue);
                    }
                    hslaCssValue.setStateChangeInformer(this);
                } else {
                    makeHslaCssValueNull();
                }

                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
            }
        } catch (final InvalidValueException e) {
            this.cssValue = previousCssValue;
            throw e;
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
     * @param cssValue
     * @return true if the given {@code cssValue} is valid.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String cssValue) {

        if (cssValue == null) {
            return false;
        }

        final String trimmedCssValue = StringUtil.strip(cssValue);

        if (trimmedCssValue.isEmpty()) {
            return false;
        }

        final String trimmedCssValueLowerCase = TagStringUtil
                .toLowerCase(trimmedCssValue);

        if (INITIAL.equals(trimmedCssValueLowerCase)
                || INHERIT.equals(trimmedCssValueLowerCase)
                || TRANSPARENT.equals(trimmedCssValueLowerCase)) {
            return true;
        }

        if (CssColorName.isValid(trimmedCssValue)) {
            return true;
        }

        if (RgbCssValue.isValid(trimmedCssValue)) {
            return true;
        }
        if (RgbaCssValue.isValid(trimmedCssValue)) {
            return true;
        }
        if (HslCssValue.isValid(trimmedCssValue)) {
            return true;
        }
        if (HslaCssValue.isValid(trimmedCssValue)) {
            return true;
        }

        try {

            if (trimmedCssValue.length() == 0
                    || trimmedCssValue.charAt(0) != '#') {
                return false;
            }

            final long value = Long.parseLong(trimmedCssValue.substring(1), 16);

            return !(value > CommonConstants.FFFFFF_HEX_VALUE || value < 0);
        } catch (final NumberFormatException ex) {
        }

        return false;
    }

    @Override
    public void stateChanged(final Bean stateChangedObject) {
        cssValue = stateChangedObject.getValue();
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * @return the rgbCssValue
     * @since 1.0.0
     * @author WFF
     */
    public RgbCssValue getRgbCssValue() {
        return rgbCssValue;
    }

    /**
     * @return the rgbaCssValue
     * @author WFF
     * @since 1.0.0
     */
    public RgbaCssValue getRgbaCssValue() {
        return rgbaCssValue;
    }

    /**
     * @return the hslCssValue
     * @author WFF
     * @since 1.0.0
     */
    public HslCssValue getHslCssValue() {
        return hslCssValue;
    }

    /**
     * @return the hslaCssValue
     * @author WFF
     * @since 1.0.0
     */
    public HslaCssValue getHslaCssValue() {
        return hslaCssValue;
    }

    /**
     * @param rgbCssValue
     *            the rgbCssValue to set
     * @since 1.0.0
     * @author WFF
     */
    public void setRgbCssValue(final RgbCssValue rgbCssValue) {
        if (rgbCssValue == null) {
            throw new NullValueException("rgbCssValue can not be null");
        }
        if (this.rgbCssValue != null) {
            if (rgbCssValue.isAlreadyInUse()
                    && this.rgbCssValue.getStateChangeInformer() != rgbCssValue
                            .getStateChangeInformer()) {
                try {
                    final RgbCssValue rgbCssValueClone = CloneUtil
                            .deepClone(rgbCssValue);
                    this.rgbCssValue.setAlreadyInUse(false);
                    this.rgbCssValue = rgbCssValueClone;
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("cloned rgbCssValue " + rgbCssValue
                                + "(hashcode: " + rgbCssValue.hashCode()
                                + ") as it is already used by another object");
                    }
                } catch (final CloneNotSupportedException e) {
                    throw new InvalidValueException(e.toString());
                }
            } else {
                this.rgbCssValue.setAlreadyInUse(false);
                this.rgbCssValue = rgbCssValue;
            }
        } else {
            if (rgbCssValue.isAlreadyInUse()) {
                try {
                    this.rgbCssValue = CloneUtil.deepClone(rgbCssValue);
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("cloned rgbCssValue " + rgbCssValue
                                + "(hashcode: " + rgbCssValue.hashCode()
                                + ") as it is already used by another object");
                    }
                } catch (final CloneNotSupportedException e) {
                    throw new InvalidValueException(e.toString());
                }
            } else {
                this.rgbCssValue = rgbCssValue;
            }
        }
        cssValue = this.rgbCssValue.getValue();
        this.rgbCssValue.setStateChangeInformer(this);
        this.rgbCssValue.setAlreadyInUse(true);
        makeRgbaCssValueNull();
        makeHslCssValueNull();
        makeHslaCssValueNull();
    }

    /**
     * @param rgbaCssValue
     *            the rgbaCssValue to set
     * @since 1.0.0
     * @author WFF
     */
    public void setRgbaCssValue(final RgbaCssValue rgbaCssValue) {
        if (rgbaCssValue == null) {
            throw new NullValueException("rgbaCssValue can not be null");
        }
        if (this.rgbaCssValue != null) {
            if (rgbaCssValue.isAlreadyInUse() && this.rgbaCssValue
                    .getStateChangeInformer() != rgbaCssValue
                            .getStateChangeInformer()) {
                try {
                    final RgbaCssValue rgbaCssValueClone = CloneUtil
                            .deepClone(rgbaCssValue);
                    this.rgbaCssValue.setAlreadyInUse(false);
                    this.rgbaCssValue = rgbaCssValueClone;
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("cloned rgbaCssValue " + rgbaCssValue
                                + "(hashcode: " + rgbaCssValue.hashCode()
                                + ") as it is already used by another object");
                    }
                } catch (final CloneNotSupportedException e) {
                    throw new InvalidValueException(e.toString());
                }
            } else {
                this.rgbaCssValue.setAlreadyInUse(false);
                this.rgbaCssValue = rgbaCssValue;
            }
        } else {
            if (rgbaCssValue.isAlreadyInUse()) {
                try {
                    this.rgbaCssValue = CloneUtil.deepClone(rgbaCssValue);
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("cloned rgbaCssValue " + rgbaCssValue
                                + "(hashcode: " + rgbaCssValue.hashCode()
                                + ") as it is already used by another object");
                    }
                } catch (final CloneNotSupportedException e) {
                    throw new InvalidValueException(e.toString());
                }
            } else {
                this.rgbaCssValue = rgbaCssValue;
            }
        }
        cssValue = this.rgbaCssValue.getValue();
        this.rgbaCssValue.setStateChangeInformer(this);
        this.rgbaCssValue.setAlreadyInUse(true);
        makeRgbCssValueNull();
        makeHslCssValueNull();
        makeHslaCssValueNull();
    }

    /**
     * @param hslCssValue
     *            the hslCssValue to set
     * @since 1.0.0
     * @author WFF
     */
    public void setHslCssValue(final HslCssValue hslCssValue) {
        if (hslCssValue == null) {
            throw new NullValueException("hslCssValue can not be null");
        }
        if (this.hslCssValue != null) {
            if (hslCssValue.isAlreadyInUse()
                    && this.hslCssValue.getStateChangeInformer() != hslCssValue
                            .getStateChangeInformer()) {
                try {
                    final HslCssValue hslCssValueClone = CloneUtil
                            .deepClone(hslCssValue);
                    this.hslCssValue.setAlreadyInUse(false);
                    this.hslCssValue = hslCssValueClone;
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("cloned hslCssValue " + hslCssValue
                                + "(hashcode: " + hslCssValue.hashCode()
                                + ") as it is already used by another object");
                    }
                } catch (final CloneNotSupportedException e) {
                    throw new InvalidValueException(e.toString());
                }
            } else {
                this.hslCssValue.setAlreadyInUse(false);
                this.hslCssValue = hslCssValue;
            }
        } else {
            if (hslCssValue.isAlreadyInUse()) {
                try {
                    this.hslCssValue = CloneUtil.deepClone(hslCssValue);
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("cloned hslCssValue " + hslCssValue
                                + "(hashcode: " + hslCssValue.hashCode()
                                + ") as it is already used by another object");
                    }
                } catch (final CloneNotSupportedException e) {
                    throw new InvalidValueException(e.toString());
                }
            } else {
                this.hslCssValue = hslCssValue;
            }
        }
        cssValue = this.hslCssValue.getValue();
        this.hslCssValue.setStateChangeInformer(this);
        this.hslCssValue.setAlreadyInUse(true);
        makeRgbCssValueNull();
        makeRgbaCssValueNull();
        makeHslaCssValueNull();
    }

    /**
     * @param hslaCssValue
     *            the hslaCssValue to set
     * @since 1.0.0
     * @author WFF
     */
    public void setHslaCssValue(final HslaCssValue hslaCssValue) {
        if (hslaCssValue == null) {
            throw new NullValueException("hslaCssValue can not be null");
        }
        if (this.hslaCssValue != null) {
            if (hslaCssValue.isAlreadyInUse() && this.hslaCssValue
                    .getStateChangeInformer() != hslaCssValue
                            .getStateChangeInformer()) {
                try {
                    final HslaCssValue hslaCssValueClone = CloneUtil
                            .deepClone(hslaCssValue);
                    this.hslaCssValue.setAlreadyInUse(false);
                    this.hslaCssValue = hslaCssValueClone;
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("cloned hslaCssValue " + hslaCssValue
                                + "(hashcode: " + hslaCssValue.hashCode()
                                + ") as it is already used by another object");
                    }
                } catch (final CloneNotSupportedException e) {
                    throw new InvalidValueException(e.toString());
                }
            } else {
                this.hslaCssValue.setAlreadyInUse(false);
                this.hslaCssValue = hslaCssValue;
            }
        } else {
            if (hslaCssValue.isAlreadyInUse()) {
                try {
                    this.hslaCssValue = CloneUtil.deepClone(hslaCssValue);
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.warning("cloned hslaCssValue " + hslaCssValue
                                + "(hashcode: " + hslaCssValue.hashCode()
                                + ") as it is already used by another object");
                    }
                } catch (final CloneNotSupportedException e) {
                    throw new InvalidValueException(e.toString());
                }
            } else {
                this.hslaCssValue = hslaCssValue;
            }
        }
        cssValue = this.hslaCssValue.getValue();
        this.hslaCssValue.setStateChangeInformer(this);
        this.hslaCssValue.setAlreadyInUse(true);
        makeRgbCssValueNull();
        makeRgbaCssValueNull();
        makeHslCssValueNull();
    }

    private void makeRgbCssValueNull() {
        if (rgbCssValue != null) {
            rgbCssValue.setAlreadyInUse(false);
            rgbCssValue = null;
        }
    }

    private void makeRgbaCssValueNull() {
        if (rgbaCssValue != null) {
            rgbaCssValue.setAlreadyInUse(false);
            rgbaCssValue = null;
        }
    }

    private void makeHslCssValueNull() {
        if (hslCssValue != null) {
            hslCssValue.setAlreadyInUse(false);
            hslCssValue = null;
        }
    }

    private void makeHslaCssValueNull() {
        if (hslaCssValue != null) {
            hslaCssValue.setAlreadyInUse(false);
            hslaCssValue = null;
        }
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
}
