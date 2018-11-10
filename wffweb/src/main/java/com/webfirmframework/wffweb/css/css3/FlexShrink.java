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
 *  flex-shrink: <i>number</i>|initial|inherit;
 * The flex-shrink property specifies how the item will shrink relative to the rest of the flexible items inside the same container.
 *
 * Note: If the element is not a flexible item, the flex-shrink property has no effect.
 * Default value:  1
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS3
 * JavaScript syntax:      object.style.flexShrink="5"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class FlexShrink extends AbstractCssProperty<FlexShrink> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;
    private Float value;

    /**
     * The default value 1 will be set as the cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public FlexShrink() {
        value = Float.valueOf(1);
        cssValue = value.toString();
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public FlexShrink(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param flexShrink
     *            the {@code FlexShrink} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public FlexShrink(final FlexShrink flexShrink) {
        if (flexShrink == null) {
            throw new NullValueException("flexShrink can not be null");
        }
        setCssValue(flexShrink.getCssValue());
    }

    /**
     * @param value
     */
    public FlexShrink(final float value) {
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
        return CssNameConstants.FLEX_SHRINK;
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
     * gets the flexShrink in {@code Float} value.
     *
     * @return the value in float or null if the cssValue is
     *         <code>initial</code> or <code>inherit</code>.
     * @since 1.0.0
     * @author WFF
     */
    public Float getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     * @author WFF
     * @since 1.0.0
     */
    public void setValue(final float value) {
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
    public FlexShrink setCssValue(final String cssValue) {
        final String previousCssValue = this.cssValue;
        try {
            if (cssValue == null) {
                throw new NullValueException(
                        "null is an invalid value. The value format should be as for example 0.5, initial/inherit.");
            } else {

                final String trimmedCssValue = TagStringUtil
                        .toLowerCase(StringUtil.strip(cssValue));

                if (INITIAL.equals(trimmedCssValue)
                        || INHERIT.equals(trimmedCssValue)) {
                    this.cssValue = trimmedCssValue;
                    value = null;
                } else {
                    try {
                        value = Float.valueOf(trimmedCssValue);
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
            Float.parseFloat(trimmedCssValue);
            return true;
        } catch (final NumberFormatException e) {
        }

        return PREDEFINED_CONSTANTS.contains(trimmedCssValue);
    }
}
