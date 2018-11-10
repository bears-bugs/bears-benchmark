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
import com.webfirmframework.wffweb.util.StringBuilderUtil;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * list-style: list-style-type list-style-position
 * list-style-image|initial|inherit;
 *
 *
 * Formal syntax: {@code <'list-style-type'> || <'list-style-position'> ||
 * <'list-style-image'>}
 *
 * @author WFF
 * @since 1.0.0
 */
public class ListStyle extends AbstractCssProperty<ListStyle>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(ListStyle.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private ListStyleType listStyleType;

    private ListStylePosition listStylePosition;

    private ListStyleImage listStyleImage;

    /**
     * The value <code>disc outside none</code> will be assigned as the
     * cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public ListStyle() {
        setCssValue("disc outside none");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public ListStyle(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param listStyle
     *            the {@code ListStyle} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public ListStyle(final ListStyle listStyle) {
        if (listStyle == null) {
            throw new NullValueException("listStyle can not be null");
        }
        setCssValue(listStyle.getCssValue());
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
        return CssNameConstants.LIST_STYLE_TYPE;
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
    public ListStyle setCssValue(final String cssValue) {
        // NB: cssValue can contain upper case so it should not be converted to
        // lower case.
        final String trimmedCssValue;
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example cjk-ideographic inside url(\"Sqpurple.gif\"). Or, initial/inherit.");
        } else if ((trimmedCssValue = StringUtil.strip(cssValue)).isEmpty()) {
            throw new NullValueException(cssValue
                    + " is an invalid value. The value format should be as for example cjk-ideographic inside url(\"Sqpurple.gif\"). Or, initial/inherit.");
        }

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);

        if (cssValueParts.length > 1) {
            if (trimmedCssValue.contains(ListStyleImage.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(ListStyleImage.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                listStyleType = null;
                listStylePosition = null;
                if (listStyleImage != null) {
                    listStyleImage.setAlreadyInUse(false);
                    listStyleImage = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        ListStyleType listStyleType = null;
        ListStylePosition listStylePosition = null;
        ListStyleImage listStyleImage = null;

        for (final String eachPart : cssValueParts) {
            if (listStyleType == null && ListStyleType.isValid(eachPart)) {
                listStyleType = ListStyleType.getThis(eachPart);
            } else if (listStylePosition == null
                    && ListStylePosition.isValid(eachPart)) {
                listStylePosition = ListStylePosition.getThis(eachPart);
            } else if (listStyleImage == null
                    && ListStyleImage.isValid(eachPart)) {
                if (this.listStyleImage == null) {
                    listStyleImage = new ListStyleImage(eachPart);
                    listStyleImage.setStateChangeInformer(this);
                    listStyleImage.setAlreadyInUse(true);
                } else {
                    this.listStyleImage.setCssValue(eachPart);
                    listStyleImage = this.listStyleImage;
                }
            } else {
                throw new InvalidValueException(cssValue
                        + " is an invalid value. The value format should be as for example cjk-ideographic inside url(\"Sqpurple.gif\"). Or, initial/inherit.");
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (listStyleType != null) {
            cssValueBuilder.append(listStyleType.getCssValue()).append(' ');
            invalid = false;
        }
        if (listStylePosition != null) {
            cssValueBuilder.append(listStylePosition.getCssValue()).append(' ');
            invalid = false;
        }
        if (listStyleImage != null) {
            cssValueBuilder.append(listStyleImage.getCssValue()).append(' ');
            invalid = false;
        }
        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example cjk-ideographic inside url(\"Sqpurple.gif\"). Or, initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.listStyleType = listStyleType;
        this.listStylePosition = listStylePosition;
        this.listStyleImage = listStyleImage;

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

        ListStyleType listStyleType = null;
        ListStylePosition listStylePosition = null;
        ListStyleImage listStyleImage = null;

        for (final String eachPart : cssValueParts) {
            boolean invalid = true;
            if (listStyleType == null && ListStyleType.isValid(eachPart)) {
                listStyleType = ListStyleType.getThis(eachPart);
                invalid = false;
            } else if (listStylePosition == null
                    && ListStylePosition.isValid(eachPart)) {
                listStylePosition = ListStylePosition.getThis(eachPart);
                invalid = false;
            } else if (listStyleImage == null
                    && ListStyleImage.isValid(eachPart)) {
                listStyleImage = new ListStyleImage(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return listStyleType != null || listStylePosition != null
                || listStyleImage != null;
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

    public ListStyleImage getListStyleImage() {
        return listStyleImage;
    }

    public ListStylePosition getListStylePosition() {
        return listStylePosition;
    }

    public ListStyleType getListStyleType() {
        return listStyleType;
    }

    public ListStyle setListStyleType(final ListStyleType listStyleType) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (listStyleType != null) {
            cssValueBuilder.append(listStyleType.getCssValue()).append(' ');
        }
        if (listStylePosition != null) {
            cssValueBuilder.append(listStylePosition.getCssValue()).append(' ');
        }

        if (listStyleImage != null) {
            cssValueBuilder.append(listStyleImage.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder);
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.listStyleType = listStyleType;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public ListStyle setListStylePosition(
            final ListStylePosition listStylePosition) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (listStyleType != null) {
            cssValueBuilder.append(listStyleType.getCssValue()).append(' ');
        }

        if (listStylePosition != null) {
            cssValueBuilder.append(listStylePosition.getCssValue()).append(' ');
        }

        if (listStyleImage != null) {
            cssValueBuilder.append(listStyleImage.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder);
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.listStylePosition = listStylePosition;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public ListStyle setListStyleImage(final ListStyleImage listStyleImage) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (listStyleType != null) {
            cssValueBuilder.append(listStyleType.getCssValue()).append(' ');
        }

        if (listStylePosition != null) {
            cssValueBuilder.append(listStylePosition.getCssValue()).append(' ');
        }

        if (listStyleImage != null) {
            final String listStyleImageCssValue = listStyleImage.getCssValue();
            if (ListStyleImage.INITIAL.equals(listStyleImageCssValue)
                    || ListStyleImage.INHERIT.equals(listStyleImageCssValue)) {
                throw new InvalidValueException(
                        "listStyleImage cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(listStyleImageCssValue);
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder);
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (listStyleImage != null && listStyleImage.isAlreadyInUse()
                && this.listStyleImage != listStyleImage) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given listStyleImage is already used by another object so a new object or the previous object (if it exists) of ListStyleImage will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.listStyleImage != null) {
            this.listStyleImage.setAlreadyInUse(false);
        }

        this.listStyleImage = listStyleImage;

        if (this.listStyleImage != null) {
            this.listStyleImage.setStateChangeInformer(this);
            this.listStyleImage.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof ListStyleImage) {

            final ListStyleImage listStyleImage = (ListStyleImage) stateChangedObject;

            final String cssValue = listStyleImage.getCssValue();

            if (ListStyleImage.INITIAL.equals(cssValue)
                    || ListStyleImage.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as listStyleImage cssValue");
            }

            setListStyleImage(listStyleImage);
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
