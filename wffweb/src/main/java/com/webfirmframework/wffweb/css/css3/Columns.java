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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.StringBuilderUtil;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * <pre>
 * columns: auto|<i>column-width column-count</i>|initial|inherit;
 *
 * The columns property is a shorthand property for setting column-width and column-count.
 * Default value:  auto auto
 * Inherited:      no
 * Animatable:     yes, see individual properties.
 * Version:        CSS3
 * JavaScript syntax:      object.style.columns="100px 3"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class Columns extends AbstractCssProperty<Columns>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(Columns.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private ColumnWidth columnWidth;

    private ColumnCount columnCount;

    /**
     * The value <code>auto auto</code> will be assigned as the cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public Columns() {
        setCssValue("auto auto");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public Columns(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param columns
     *            the {@code Columns} object from which the cssValue to set.And,
     *            {@code null} will throw {@code NullValueException}
     */
    public Columns(final Columns columns) {
        if (columns == null) {
            throw new NullValueException("columns can not be null");
        }
        setCssValue(columns.getCssValue());
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
        return CssNameConstants.COLUMNS;
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
    public Columns setCssValue(final String cssValue) {
        // NB: cssValue can contain upper case so it should not be converted to
        // lower case.
        final String trimmedCssValue;
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example 17px 2. Or, initial/inherit.");
        } else if ((trimmedCssValue = StringUtil.strip(cssValue)).isEmpty()) {
            throw new NullValueException(cssValue
                    + " is an invalid value. The value format should be as for example 17px 2. Or, initial/inherit.");
        }

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);

        if (cssValueParts.length > 1) {
            if (trimmedCssValue.contains(ColumnWidth.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(ColumnWidth.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                if (columnWidth != null) {
                    columnWidth.setAlreadyInUse(false);
                    columnWidth = null;
                }

                if (columnCount != null) {
                    columnCount.setAlreadyInUse(false);
                    columnCount = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        ColumnWidth columnWidth = null;
        ColumnCount columnCount = null;

        for (final String eachPart : cssValueParts) {
            if (columnWidth == null && ColumnWidth.isValid(eachPart)) {
                if (this.columnWidth == null) {
                    columnWidth = new ColumnWidth(eachPart);
                    columnWidth.setStateChangeInformer(this);
                    columnWidth.setAlreadyInUse(true);
                } else {
                    this.columnWidth.setCssValue(eachPart);
                    columnWidth = this.columnWidth;
                }
            } else if (columnCount == null && ColumnCount.isValid(eachPart)) {
                if (this.columnCount == null) {
                    columnCount = new ColumnCount(eachPart);
                    columnCount.setStateChangeInformer(this);
                    columnCount.setAlreadyInUse(true);
                } else {
                    this.columnCount.setCssValue(eachPart);
                    columnCount = this.columnCount;
                }
            } else {
                throw new InvalidValueException(cssValue
                        + " is an invalid value. The value format should be as for example 17px 2. Or, initial/inherit.");
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (columnWidth != null) {
            cssValueBuilder.append(columnWidth.getCssValue()).append(' ');
            invalid = false;
        }
        if (columnCount != null) {
            cssValueBuilder.append(columnCount.getCssValue()).append(' ');
            invalid = false;
        }
        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example 17px 2. Or, initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.columnWidth = columnWidth;
        this.columnCount = columnCount;

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
        if (cssValue == null || StringUtil.isBlank(cssValue)) {
            return false;
        }

        final String[] cssValueParts = StringUtil.splitBySpace(cssValue);

        ColumnWidth columnWidth = null;
        ColumnCount columnCount = null;

        for (final String eachPart : cssValueParts) {
            boolean invalid = true;
            if (columnWidth == null && ColumnWidth.isValid(eachPart)) {
                columnWidth = new ColumnWidth(eachPart);
                invalid = false;
            } else if (columnCount == null && ColumnCount.isValid(eachPart)) {
                columnCount = new ColumnCount(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return columnWidth != null || columnCount != null;
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

    public ColumnWidth getColumnWidth() {
        return columnWidth;
    }

    /**
     * @return the columnCount
     * @author WFF
     * @since 1.0.0
     */
    public ColumnCount getColumnCount() {
        return columnCount;
    }

    public Columns setColumnWidth(final ColumnWidth columnWidth) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (columnWidth != null) {
            final String columnWidthCssValue = columnWidth.getCssValue();
            if (ColumnWidth.INITIAL.equals(columnWidthCssValue)
                    || ColumnWidth.INHERIT.equals(columnWidthCssValue)) {
                throw new InvalidValueException(
                        "columnWidth cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(columnWidthCssValue).append(' ');
        }

        if (columnCount != null) {
            final String columnWidthCssValue = columnCount.getCssValue();
            cssValueBuilder.append(columnWidthCssValue);
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder);
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (columnWidth != null && columnWidth.isAlreadyInUse()
                && this.columnWidth != columnWidth) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given columnWidth is already used by another object so a new object or the previous object (if it exists) of ColumnWidth will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.columnWidth != null) {
            this.columnWidth.setAlreadyInUse(false);
        }

        this.columnWidth = columnWidth;

        if (this.columnWidth != null) {
            this.columnWidth.setStateChangeInformer(this);
            this.columnWidth.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public Columns setColumnCount(final ColumnCount columnCount) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (columnWidth != null) {
            final String columnWidthCssValue = columnWidth.getCssValue();
            cssValueBuilder.append(columnWidthCssValue).append(' ');
        }

        if (columnCount != null) {
            final String columnWidthCssValue = columnCount.getCssValue();
            if (ColumnCount.INITIAL.equals(columnWidthCssValue)
                    || ColumnCount.INHERIT.equals(columnWidthCssValue)) {
                throw new InvalidValueException(
                        "columnWidth cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(columnWidthCssValue);
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder);
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (columnCount != null && columnCount.isAlreadyInUse()
                && this.columnCount != columnCount) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given columnCount is already used by another object so a new object or the previous object (if it exists) of ColumnWidth will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.columnCount != null) {
            this.columnCount.setAlreadyInUse(false);
        }

        this.columnCount = columnCount;

        if (this.columnCount != null) {
            this.columnCount.setStateChangeInformer(this);
            this.columnCount.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof ColumnWidth) {

            final ColumnWidth columnWidth = (ColumnWidth) stateChangedObject;

            final String cssValue = columnWidth.getCssValue();

            if (ColumnWidth.INITIAL.equals(cssValue)
                    || ColumnWidth.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as columnWidth cssValue");
            }

            setColumnWidth(columnWidth);
        } else if (stateChangedObject instanceof ColumnCount) {

            final ColumnCount columnCount = (ColumnCount) stateChangedObject;

            final String cssValue = columnCount.getCssValue();

            if (ColumnCount.INITIAL.equals(cssValue)
                    || ColumnCount.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as columnCount cssValue");
            }

            setColumnCount(columnCount);
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
