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

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * border-image-repeat: stretch|repeat|round|initial|inherit;
 *
 * <pre>
 * The border-image-repeat property specifies whether the border image should be repeated, rounded or stretched.
 *
 * Tip: Also look at the border-image property (a shorthand property for setting all the border-image-* properties).
 * Default value:  stretch
 * Inherited:      no
 * Animatable:     no
 * Version:        CSS3
 * JavaScript syntax:      object.style.borderImageRepeat="round"
 * JavaScript syntax:      object.style.borderImageRepeat="round stretch"
 *
 * </pre>
 *
 * @author WFF
 *
 */
public class BorderImageRepeat extends AbstractCssProperty<BorderImageRepeat> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String STRETCH = "stretch";
    public static final String REPEAT = "repeat";
    public static final String ROUND = "round";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, STRETCH, REPEAT, ROUND);

    private String cssValue;

    private String vertical;
    private String horizontal;

    /**
     * The {@code stretch} will be set as the value
     */
    public BorderImageRepeat() {
        setCssValue(STRETCH);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderImageRepeat(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param borderImageRepeat
     *            the {@code BorderImageRepeat} object from which the cssValue
     *            to set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderImageRepeat(final BorderImageRepeat borderImageRepeat) {
        if (borderImageRepeat == null) {
            throw new NullValueException("borderImageRepeat can not be null");
        }
        setCssValue(borderImageRepeat.getCssValue());
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
        return CssNameConstants.BORDER_IMAGE_REPEAT;
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
     *            <code>round stretch</code> or <code>round</code>. {@code null}
     *            is considered as an invalid value and it will throw
     *            {@code NullValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public BorderImageRepeat setCssValue(final String cssValue) {
        final String previousCssValue = this.cssValue;
        final String previousVertical = vertical;
        final String previousHorizontal = horizontal;
        try {
            if (cssValue == null) {
                throw new NullValueException(
                        "null is an invalid value. The value format should be as for example 'round stretch'/'round'. Or, initial/inherit.");
            } else {

                final String trimmedCssValue = StringUtil.convertToSingleSpace(
                        TagStringUtil.toLowerCase(StringUtil.strip(cssValue)));

                final String[] verticalHorzontal = StringUtil
                        .splitBySpace(trimmedCssValue);

                if (verticalHorzontal.length == 1) {
                    if (trimmedCssValue.equalsIgnoreCase(INITIAL)
                            || trimmedCssValue.equalsIgnoreCase(INHERIT)) {
                        this.cssValue = trimmedCssValue;
                        vertical = null;
                        horizontal = null;
                    } else if (PREDEFINED_CONSTANTS
                            .contains(verticalHorzontal[0])) {
                        vertical = horizontal = verticalHorzontal[0];
                        this.cssValue = trimmedCssValue;
                    } else {
                        throw new InvalidValueException("the given cssValue '"
                                + cssValue + "' is invalid");
                    }
                } else if (verticalHorzontal.length == 2) {

                    if (PREDEFINED_CONSTANTS.contains(verticalHorzontal[0])
                            && PREDEFINED_CONSTANTS
                                    .contains(verticalHorzontal[1])) {
                        horizontal = verticalHorzontal[0];
                        vertical = verticalHorzontal[1];
                        this.cssValue = trimmedCssValue;
                    } else {
                        throw new InvalidValueException("the given cssValue '"
                                + cssValue
                                + "' is invalid. The value format should be as for example 'round stretch'/'round'. Or, initial/inherit.");
                    }
                } else {
                    throw new InvalidValueException(
                            "The given cssValue should not contain more that 2 length values. The value format should be as for example 'round stretch'/'round'. Or, initial/inherit.");
                }

                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
            }
        } catch (final InvalidValueException e) {
            this.cssValue = previousCssValue;
            vertical = previousVertical;
            horizontal = previousHorizontal;
            throw e;
        }
        return this;

    }

    /**
     * @param vertical
     *            the vertical to set
     * @author WFF
     * @since 1.0.0
     */
    public void setVertical(final String vertical) {
        if (vertical == null) {
            throw new NullValueException("The vertical value cannot be null.");
        }
        final String inputVertical = TagStringUtil
                .toLowerCase(StringUtil.strip(vertical));
        if (!INITIAL.equals(vertical) && !INHERIT.equals(inputVertical)
                && PREDEFINED_CONSTANTS.contains(inputVertical)) {
            this.vertical = inputVertical;
            if (horizontal != null) {
                cssValue = horizontal.concat(" ").concat(inputVertical);
            } else {
                cssValue = inputVertical;
            }
        } else {
            throw new InvalidValueException(
                    "The given vertical value '" + vertical + "' is invalid.");
        }
    }

    /**
     * @param horizontal
     *            the horizontal to set
     * @author WFF
     * @since 1.0.0
     */
    public void setHorizontal(final String horizontal) {
        if (horizontal == null) {
            throw new NullValueException("The vertical value cannot be null.");
        }
        final String inputHorizontal = TagStringUtil
                .toLowerCase(StringUtil.strip(horizontal));
        if (!INITIAL.equals(vertical) && !INHERIT.equals(inputHorizontal)
                && PREDEFINED_CONSTANTS.contains(inputHorizontal)) {
            this.horizontal = inputHorizontal;
            if (vertical != null) {
                cssValue = inputHorizontal.concat(" ").concat(vertical);
            } else {
                cssValue = inputHorizontal;
            }
        } else {
            throw new InvalidValueException(
                    "The given vertical value '" + vertical + "' is invalid.");
        }
    }

    /**
     * @return the vertical
     * @author WFF
     * @since 1.0.0
     */
    public String getVertical() {
        return vertical;
    }

    /**
     * @return the horizontal
     * @author WFF
     * @since 1.0.0
     */
    public String getHorizontal() {
        return horizontal;
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
     * validates if the given cssValue is valid for this class.
     *
     * @param cssValue
     *            the value to check.
     * @return true if valid and false if invalid.
     * @author WFF
     * @since 1.0.0
     */
    public static boolean isValid(final String cssValue) {
        final String trimmedCssValue = TagStringUtil
                .toLowerCase(StringUtil.strip(cssValue));

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);
        if (cssValueParts.length > 2) {
            return false;
        }
        if (cssValueParts.length == 1) {
            return PREDEFINED_CONSTANTS.contains(trimmedCssValue);
        }

        boolean valid = false;
        for (final String each : cssValueParts) {

            if (!PREDEFINED_CONSTANTS.contains(each)) {
                return false;
            }
            if ((INITIAL.equals(each) || INHERIT.equals(each))
                    && cssValueParts.length == 2) {
                return false;

            }
            valid = true;
        }
        return valid;
    }
}
