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
 * <pre>
 *  animation-iteration-count: number|initial|inherit;
 *
 * The animation-iteration-count property specifies the number of times an animation should be played.
 * Default value:  1
 * Inherited:      no
 * Animatable:     no
 * Version:        CSS3
 * JavaScript syntax:      object.style.animationIterationCount="infinite"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class AnimationIterationCount
        extends AbstractCssProperty<AnimationIterationCount> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String INFINITE = "infinite";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, INFINITE);

    private String cssValue;
    private Integer value;

    /**
     * The default value 1 will be set as the cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public AnimationIterationCount() {
        value = Integer.valueOf(1);
        cssValue = value.toString();
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public AnimationIterationCount(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param animationIterationCount
     *            the {@code Opacity} object from which the cssValue to set.And,
     *            {@code null} will throw {@code NullValueException}
     */
    public AnimationIterationCount(
            final AnimationIterationCount animationIterationCount) {
        if (animationIterationCount == null) {
            throw new NullValueException(
                    "animationIterationCount can not be null");
        }
        setCssValue(animationIterationCount.getCssValue());
    }

    /**
     * @param value
     */
    public AnimationIterationCount(final int value) {
        this.value = value;
        cssValue = String.valueOf(value);
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
        return CssNameConstants.ANIMATION_ITERATION_COUNT;
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
     * gets the animationIterationCount in {@code Integer} value.
     *
     * @return the value in int or null if the cssValue is <code>initial</code>
     *         or <code>inherit</code>.
     * @since 1.0.0
     * @author WFF
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     * @author WFF
     * @since 1.0.0
     */
    public void setValue(final int value) {
        this.value = value;
        cssValue = String.valueOf(value);
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * @param cssValue
     *            the value should be in the format of <code>0.5</code>,
     *            <code>initial/inherit</code>. {@code null} is considered as an
     *            invalid value and it will throw {@code NullValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public AnimationIterationCount setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example 0.5, initial/inherit.");
        } else {

            final String trimmedCssValue = TagStringUtil
                    .toLowerCase(StringUtil.strip(cssValue));

            if (INITIAL.equals(trimmedCssValue)
                    || INHERIT.equals(trimmedCssValue)
                    || INFINITE.equals(trimmedCssValue)) {
                this.cssValue = trimmedCssValue;
                value = null;
            } else {
                try {
                    value = Integer.valueOf(trimmedCssValue);
                    this.cssValue = value.toString();
                } catch (final NumberFormatException e) {
                    throw new InvalidValueException(cssValue
                            + " is an invalid value. The value format should be as for example 0.5, initial, inherit etc..");
                }
            }

        }
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
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
     * sets as {@code infinite}
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInfinite() {
        setCssValue(INFINITE);
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
        if (StringUtil.containsSpace(trimmedCssValue)) {
            return false;
        }
        try {
            final int parsedValue = Integer.parseInt(trimmedCssValue);

            return !(parsedValue == 0
                    && (StringUtil.containsMinus(trimmedCssValue)
                            || StringUtil.containsPlus(trimmedCssValue)));
        } catch (final NumberFormatException e) {
        }

        return PREDEFINED_CONSTANTS.contains(trimmedCssValue);
    }
}
