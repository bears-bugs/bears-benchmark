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
 *
 * outline: outline-color outline-style outline-width|initial|inherit;
 *
 * An outline is a line that is drawn around elements (outside the borders) to make the element "stand out".
 *
 * The outline shorthand property sets all the outline properties in one declaration.
 *
 * The properties that can be set, are (in order): outline-color, outline-style, outline-width.
 *
 * If one of the values above are missing, e.g. "outline:solid #ff0000;", the default value for the missing property will be inserted, if any.
 *
 * Note: The outline is not a part of the element's dimensions, therefore the element's width and height properties do not contain the width of the outline.
 * Default value:  invert none medium
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS2
 * JavaScript syntax:      object.style.outline="#0000FF dotted 5px"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class Outline extends AbstractCssProperty<Outline>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(Outline.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private OutlineColor outlineColor;

    private OutlineStyle outlineStyle;

    private OutlineWidth outlineWidth;

    /**
     * The value <code>medium none black</code> will be assigned as the
     * cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public Outline() {
        setCssValue("medium none black");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public Outline(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param outline
     *            the {@code Outline} object from which the cssValue to set.And,
     *            {@code null} will throw {@code NullValueException}
     */
    public Outline(final Outline outline) {
        if (outline == null) {
            throw new NullValueException("outline can not be null");
        }
        setCssValue(outline.getCssValue());
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
        return CssNameConstants.OUTLINE;
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
    public Outline setCssValue(final String cssValue) {
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
            if (trimmedCssValue.contains(OutlineColor.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(OutlineColor.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                if (outlineColor != null) {
                    outlineColor.setAlreadyInUse(false);
                    outlineColor = null;
                }

                outlineStyle = null;

                if (outlineWidth != null) {
                    outlineWidth.setAlreadyInUse(false);
                    outlineWidth = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        OutlineColor outlineColor = null;
        OutlineStyle outlineStyle = null;
        OutlineWidth outlineWidth = null;

        for (final String eachPart : cssValueParts) {
            if (outlineColor == null && OutlineColor.isValid(eachPart)) {
                if (this.outlineColor == null) {
                    outlineColor = new OutlineColor(eachPart);
                    outlineColor.setStateChangeInformer(this);
                    outlineColor.setAlreadyInUse(true);
                } else {
                    this.outlineColor.setCssValue(eachPart);
                    outlineColor = this.outlineColor;
                }
            } else if (outlineStyle == null && OutlineStyle.isValid(eachPart)) {
                outlineStyle = OutlineStyle.getThis(eachPart);
            } else if (outlineWidth == null && OutlineWidth.isValid(eachPart)) {
                if (this.outlineWidth == null) {
                    outlineWidth = new OutlineWidth(eachPart);
                    outlineWidth.setStateChangeInformer(this);
                    outlineWidth.setAlreadyInUse(true);
                } else {
                    this.outlineWidth.setCssValue(eachPart);
                    outlineWidth = this.outlineWidth;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;

        if (outlineColor != null) {
            cssValueBuilder.append(outlineColor.getCssValue()).append(' ');
            invalid = false;
        } else if (this.outlineColor != null) {
            this.outlineColor.setAlreadyInUse(false);
        }

        if (outlineStyle != null) {
            cssValueBuilder.append(outlineStyle.getCssValue()).append(' ');
            invalid = false;
        }

        if (outlineWidth != null) {
            cssValueBuilder.append(outlineWidth.getCssValue()).append(' ');
            invalid = false;
        } else if (this.outlineWidth != null) {
            this.outlineWidth.setAlreadyInUse(false);
        }

        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example medium none color Or initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.outlineColor = outlineColor;
        this.outlineWidth = outlineWidth;
        this.outlineStyle = outlineStyle;

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

        OutlineColor outlineColor = null;
        OutlineStyle outlineStyle = null;
        OutlineWidth outlineWidth = null;

        for (final String eachPart : cssValueParts) {

            boolean invalid = true;
            if (outlineColor == null && OutlineColor.isValid(eachPart)) {
                outlineColor = new OutlineColor(eachPart);
                invalid = false;
            } else if (outlineStyle == null && OutlineStyle.isValid(eachPart)) {
                outlineStyle = OutlineStyle.getThis(eachPart);
                invalid = false;
            } else if (outlineWidth == null && OutlineWidth.isValid(eachPart)) {
                outlineWidth = new OutlineWidth(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return outlineWidth != null || outlineStyle != null
                || outlineColor != null;
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

    public OutlineColor getOutlineColor() {
        return outlineColor;
    }

    public OutlineStyle getOutlineStyle() {
        return outlineStyle;
    }

    public OutlineWidth getOutlineWidth() {
        return outlineWidth;
    }

    public Outline setOutlineWidth(final OutlineWidth outlineWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (outlineColor != null) {
            cssValueBuilder.append(outlineColor.getCssValue()).append(' ');
        }

        if (outlineStyle != null) {
            cssValueBuilder.append(outlineStyle.getCssValue()).append(' ');
        }

        if (outlineWidth != null) {

            final String outlineWidthCssValue = outlineWidth.getCssValue();
            if (OutlineWidth.INITIAL.equals(outlineWidthCssValue)
                    || OutlineWidth.INHERIT.equals(outlineWidthCssValue)) {
                throw new InvalidValueException(
                        "outlineWidth cannot have initial/inherit as its cssValue");
            }

            cssValueBuilder.append(outlineWidth.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (outlineWidth != null && outlineWidth.isAlreadyInUse()
                && this.outlineWidth != outlineWidth) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given outlineWidth is already used by another object so a new object or the previous object (if it exists) of OutlineWidth will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.outlineWidth != null) {
            this.outlineWidth.setAlreadyInUse(false);
        }

        this.outlineWidth = outlineWidth;

        if (this.outlineWidth != null) {
            this.outlineWidth.setStateChangeInformer(this);
            this.outlineWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public Outline setOutlineStyle(final OutlineStyle outlineStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (outlineColor != null) {
            cssValueBuilder.append(outlineColor.getCssValue()).append(' ');
        }

        if (outlineStyle != null) {
            cssValueBuilder.append(outlineStyle.getCssValue()).append(' ');
        }

        if (outlineWidth != null) {
            cssValueBuilder.append(outlineWidth.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.outlineStyle = outlineStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public Outline setOutlineColor(final OutlineColor outlineColor) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (outlineColor != null) {
            final String outlineColorCssValue = outlineColor.getCssValue();
            if (OutlineColor.INITIAL.equals(outlineColorCssValue)
                    || OutlineColor.INHERIT.equals(outlineColorCssValue)) {
                throw new InvalidValueException(
                        "outlineColor cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(outlineColorCssValue).append(' ');
        }

        if (outlineStyle != null) {
            cssValueBuilder.append(outlineStyle.getCssValue()).append(' ');
        }
        if (outlineWidth != null) {
            cssValueBuilder.append(outlineWidth.getCssValue());
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (outlineColor != null && outlineColor.isAlreadyInUse()
                && this.outlineColor != outlineColor) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given outlineColor is already used by another object so a new object or the previous object (if it exists) of OutlineColor will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.outlineColor != null) {
            this.outlineColor.setAlreadyInUse(false);
        }

        this.outlineColor = outlineColor;

        if (this.outlineColor != null) {
            this.outlineColor.setStateChangeInformer(this);
            this.outlineColor.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof OutlineColor) {

            final OutlineColor outlineColor = (OutlineColor) stateChangedObject;

            final String cssValue = outlineColor.getCssValue();

            if (OutlineColor.INITIAL.equals(cssValue)
                    || OutlineColor.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as outlineColor cssValue");
            }

            setOutlineColor(outlineColor);
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
