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
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.CssLengthUtil;
import com.webfirmframework.wffweb.util.ObjectUtil;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 *
 * border-image-outset: length|number|initial|inherit;
 *
 * The border-image-outset property specifies the amount by which the border image area extends beyond the border box.
 *
 * Default value:  0
 * Inherited:      no
 * Animatable:     no
 * Version:        CSS3
 * JavaScript syntax:      object.style.borderImageOutset="10px"
 *
 * Examples:
 *
 *     border-image-outset:5px 10px 15px 10px;
 *         top border is 5px
 *         right border is 10px
 *         bottom border is 15px
 *         border is 10px
 *
 *     border-image-outset:5px 10px 15px;
 *         top border is 5px
 *         right and left borders are 10px
 *         bottom border is 15px
 *
 *     border-image-outset:5px 10px;
 *         top and bottom borders are 5px
 *         right and left borders are 10px
 *
 *     border-image-outset:5px;
 *         all four borders are 5px
 *
 * </pre>
 *
 *
 * @author WFF
 * @since 1.0.0
 */
public class BorderImageOutset extends AbstractCssProperty<BorderImageOutset> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(BorderImageOutset.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    protected static final String DEFAULT_VALUE = "0";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT);

    private String cssValue;

    private CssLengthUnit topUnit;
    private CssLengthUnit rightUnit;
    private CssLengthUnit bottomUnit;
    private CssLengthUnit leftUnit;

    private Float top;
    private Float right;
    private Float bottom;
    private Float left;

    /**
     * The {@code 0} will be set as the value
     */
    public BorderImageOutset() {
        setCssValue(DEFAULT_VALUE);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public BorderImageOutset(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param bottom
     *            the {@code BorderBottomWidth} object from which the cssValue
     *            to set.And, {@code null} will throw {@code NullValueException}
     */
    public BorderImageOutset(final BorderImageOutset bottom) {
        if (bottom == null) {
            throw new NullValueException("bottom can not be null");
        }
        setCssValue(bottom.getCssValue());
    }

    /**
     * @param percent
     *            the percentage value to set. The cssLengthUnit will
     *            automatically set to %.
     * @since 1.0.0
     * @author WFF
     */
    public BorderImageOutset(final float percent) {
        setCssValue(String.valueOf(percent) + CssLengthUnit.PER);
    }

    /**
     * @param value
     * @param cssLengthUnit
     */
    public BorderImageOutset(final float value,
            final CssLengthUnit cssLengthUnit) {
        setCssValue(String.valueOf(value) + cssLengthUnit);
    }

    /**
     * @param percent
     *            the percent to set
     * @since 1.0.0
     * @author WFF
     */
    public void setPercent(final float percent) {
        setCssValue(String.valueOf(percent) + CssLengthUnit.PER);
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
        return CssNameConstants.BORDER_IMAGE_OUTSET;
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
    public BorderImageOutset setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit/medium/thin/thick.");
        } else if (StringUtil.isBlank(cssValue)) {
            throw new InvalidValueException(
                    "blank string is an invalid value. The value format should be as for example 75px or 85%. Or, initial/inherit/medium/thin/thick.");
        } else {
            final String trimmedCssValue = TagStringUtil
                    .toLowerCase(StringUtil.strip(cssValue));

            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                this.cssValue = trimmedCssValue;
                top = right = bottom = left = null;
                topUnit = rightUnit = bottomUnit = leftUnit = null;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }

            final String borderImageWidthString = StringUtil
                    .convertToSingleSpace(trimmedCssValue);

            final String[] extractedWidths = StringUtil
                    .splitBySpace(borderImageWidthString);

            if (extractedWidths.length == 1) {
                final Object[] lengthValueAndUnitAll = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[0]);
                if (lengthValueAndUnitAll.length == 2) {
                    top = right = bottom = left = (Float) lengthValueAndUnitAll[0];
                    topUnit = rightUnit = bottomUnit = leftUnit = (CssLengthUnit) lengthValueAndUnitAll[1];
                } else if (lengthValueAndUnitAll.length == 1) {
                    top = right = bottom = left = (Float) lengthValueAndUnitAll[0];
                    topUnit = rightUnit = bottomUnit = leftUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[0] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

            } else if (extractedWidths.length == 2) {

                final Object[] lengthValueAndUnitTopBottom = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[0]);

                if (lengthValueAndUnitTopBottom.length == 2) {
                    top = bottom = (Float) lengthValueAndUnitTopBottom[0];
                    topUnit = bottomUnit = (CssLengthUnit) lengthValueAndUnitTopBottom[1];
                } else if (lengthValueAndUnitTopBottom.length == 1) {
                    top = bottom = (Float) lengthValueAndUnitTopBottom[0];
                    topUnit = bottomUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[0] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

                final Object[] lengthValueAndUnitRightLeft = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[1]);

                if (lengthValueAndUnitRightLeft.length == 2) {
                    right = left = (Float) lengthValueAndUnitRightLeft[0];
                    rightUnit = leftUnit = (CssLengthUnit) lengthValueAndUnitRightLeft[1];
                } else if (lengthValueAndUnitRightLeft.length == 1) {
                    right = left = (Float) lengthValueAndUnitRightLeft[0];
                    rightUnit = leftUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[1] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

            } else if (extractedWidths.length == 3) {

                final Object[] lengthValueAndUnitTop = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[0]);
                if (lengthValueAndUnitTop.length == 2) {
                    top = (Float) lengthValueAndUnitTop[0];
                    topUnit = (CssLengthUnit) lengthValueAndUnitTop[1];
                } else if (lengthValueAndUnitTop.length == 1) {
                    top = (Float) lengthValueAndUnitTop[0];
                    topUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[0] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

                final Object[] lengthValueAndUnitRightLeft = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[1]);
                if (lengthValueAndUnitRightLeft.length == 2) {
                    right = left = (Float) lengthValueAndUnitRightLeft[0];
                    rightUnit = leftUnit = (CssLengthUnit) lengthValueAndUnitRightLeft[1];
                } else if (lengthValueAndUnitRightLeft.length == 1) {
                    right = left = (Float) lengthValueAndUnitRightLeft[0];
                    rightUnit = leftUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[1] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

                final Object[] lengthValueAndUnitBottom = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[2]);
                if (lengthValueAndUnitBottom.length == 2) {
                    bottom = (Float) lengthValueAndUnitBottom[0];
                    bottomUnit = (CssLengthUnit) lengthValueAndUnitBottom[1];
                } else if (lengthValueAndUnitBottom.length == 1) {
                    bottom = (Float) lengthValueAndUnitBottom[0];
                    bottomUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[2] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

            } else if (extractedWidths.length == 4) {

                final Object[] lengthValueAndUnitTop = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[0]);
                if (lengthValueAndUnitTop.length == 2) {
                    top = (Float) lengthValueAndUnitTop[0];
                    topUnit = (CssLengthUnit) lengthValueAndUnitTop[1];
                } else if (lengthValueAndUnitTop.length == 1) {
                    top = (Float) lengthValueAndUnitTop[0];
                    topUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[0] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

                final Object[] lengthValueAndUnitRight = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[1]);
                if (lengthValueAndUnitRight.length == 2) {
                    right = (Float) lengthValueAndUnitRight[0];
                    rightUnit = (CssLengthUnit) lengthValueAndUnitRight[1];
                } else if (lengthValueAndUnitRight.length == 1) {
                    right = (Float) lengthValueAndUnitRight[0];
                    rightUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[1] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }
                final Object[] lengthValueAndUnitBottom = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[2]);
                if (lengthValueAndUnitBottom.length == 2) {
                    bottom = (Float) lengthValueAndUnitBottom[0];
                    bottomUnit = (CssLengthUnit) lengthValueAndUnitBottom[1];
                } else if (lengthValueAndUnitBottom.length == 1) {
                    bottom = (Float) lengthValueAndUnitBottom[0];
                    bottomUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[2] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

                final Object[] lengthValueAndUnitLeft = CssLengthUtil
                        .getLengthValueAndUnit(extractedWidths[3]);
                if (lengthValueAndUnitLeft.length == 2) {
                    left = (Float) lengthValueAndUnitLeft[0];
                    leftUnit = (CssLengthUnit) lengthValueAndUnitLeft[1];
                } else if (lengthValueAndUnitLeft.length == 1) {
                    left = (Float) lengthValueAndUnitLeft[0];
                    leftUnit = null;
                } else {
                    throw new InvalidValueException(
                            "'" + extractedWidths[3] + "' is invalid in '"
                                    + borderImageWidthString + "'");
                }

            } else {
                throw new InvalidValueException(
                        "the given cssValue is invalid");
            }

            this.cssValue = getProducedCssValue(top, topUnit, right, rightUnit,
                    bottom, bottomUnit, left, leftUnit);
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
     * sets the top, right, bottom and left in {@code BorderImageWidth}.
     *
     * @param top
     *            the value for top. The value {@code auto} will be assigned for
     *            null value.
     * @param right
     *            the value for right. The value {@code auto} will be assigned
     *            for null value.
     * @param bottom
     *            the value for bottom. The value {@code auto} will be assigned
     *            for null value.
     * @param left
     *            the value for left. The value {@code auto} will be assigned
     *            for null value.
     * @param unit
     *            the {@code CssLengthUnit} for all of the given top, right,
     *            bottom and left values.
     * @author WFF
     * @since 1.0.0
     */
    public void setBorderImageTopRightBottomLeft(final Float top,
            final Float right, final Float bottom, final Float left,
            final CssLengthUnit unit) {

        cssValue = getProducedCssValue(top, unit, right, unit, bottom, unit,
                left, unit);

        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        topUnit = rightUnit = bottomUnit = leftUnit = unit;

        final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
        if (stateChangeInformer != null) {
            stateChangeInformer.stateChanged(this);
        }
    }

    public void setBorderImageTopRightBottomLeft(final Float top,
            final CssLengthUnit topUnit, final Float right,
            final CssLengthUnit rightUnit, final Float bottom,
            final CssLengthUnit bottomUnit, final Float left,
            final CssLengthUnit leftUnit) {

        cssValue = getProducedCssValue(top, topUnit, right, rightUnit, bottom,
                bottomUnit, left, leftUnit);

        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;

        this.topUnit = topUnit;
        this.rightUnit = rightUnit;
        this.bottomUnit = bottomUnit;
        this.leftUnit = leftUnit;

        final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
        if (stateChangeInformer != null) {
            stateChangeInformer.stateChanged(this);
        }
    }

    /**
     * @param top
     *            the top to set
     * @author WFF
     * @since 1.0.0
     */
    public void setTop(final Float top, final CssLengthUnit topUnit) {
        cssValue = getProducedCssValue(top, topUnit, right, rightUnit, bottom,
                bottomUnit, left, leftUnit);
        this.top = top;
        this.topUnit = topUnit;
        final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
        if (stateChangeInformer != null) {
            stateChangeInformer.stateChanged(this);
        }
    }

    /**
     * @param right
     *            the right to set
     * @author WFF
     * @param rightUnit
     * @since 1.0.0
     */
    public void setRight(final Float right, final CssLengthUnit rightUnit) {
        cssValue = getProducedCssValue(top, topUnit, right, rightUnit, bottom,
                bottomUnit, left, leftUnit);
        this.right = right;
        this.rightUnit = rightUnit;
        final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
        if (stateChangeInformer != null) {
            stateChangeInformer.stateChanged(this);
        }
    }

    /**
     * @param bottom
     *            the bottom to set
     * @author WFF
     * @param bottomUnit
     * @since 1.0.0
     */
    public void setBottom(final Float bottom, final CssLengthUnit bottomUnit) {
        cssValue = getProducedCssValue(top, topUnit, right, rightUnit, bottom,
                bottomUnit, left, leftUnit);
        this.bottom = bottom;
        this.bottomUnit = bottomUnit;
        final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
        if (stateChangeInformer != null) {
            stateChangeInformer.stateChanged(this);
        }
    }

    /**
     * @param left
     *            the left to set
     * @author WFF
     * @param leftUnit
     * @since 1.0.0
     */
    public void setLeft(final Float left, final CssLengthUnit leftUnit) {
        cssValue = getProducedCssValue(top, topUnit, right, rightUnit, bottom,
                bottomUnit, left, leftUnit);
        this.left = left;
        this.leftUnit = leftUnit;
        final StateChangeInformer<CssProperty> stateChangeInformer = getStateChangeInformer();
        if (stateChangeInformer != null) {
            stateChangeInformer.stateChanged(this);
        }
    }

    /**
     * @return the top. It will return null for auto or the cssValue is
     *         predefined constant.
     * @author WFF
     * @since 1.0.0
     */
    public Float getTop() {
        return top;
    }

    /**
     * @return the topUnit
     * @author WFF
     * @since 1.0.0
     */
    public CssLengthUnit getTopUnit() {
        return topUnit;
    }

    /**
     * @return the right. It will return null for auto or the cssValue is
     *         predefined constant.
     * @author WFF
     * @since 1.0.0
     */
    public Float getRight() {
        return right;
    }

    /**
     * @return the rightUnit
     * @author WFF
     * @since 1.0.0
     */
    public CssLengthUnit getRightUnit() {
        return rightUnit;
    }

    /**
     * @return the bottom. It will return null for auto or the cssValue is
     *         predefined constant.
     * @author WFF
     * @since 1.0.0
     */
    public Float getBottom() {
        return bottom;
    }

    /**
     * @return the bottomUnit
     * @author WFF
     * @since 1.0.0
     */
    public CssLengthUnit getBottomUnit() {
        return bottomUnit;
    }

    /**
     * @return the left. It will return null for auto or the cssValue is
     *         predefined constant.
     * @author WFF
     * @since 1.0.0
     */
    public Float getLeft() {
        return left;
    }

    /**
     * @return the leftUnit
     * @author WFF
     * @since 1.0.0
     */
    public CssLengthUnit getLeftUnit() {
        return leftUnit;
    }

    /**
     *
     * @param top
     *            the value for top. The value {@code auto} will be assigned for
     *            null value.
     * @param topUnit
     * @param right
     *            the value for right. The value {@code auto} will be assigned
     *            for null value.
     * @param rightUnit
     * @param bottom
     *            the value for bottom. The value {@code auto} will be assigned
     *            for null value.
     * @param bottomUnit
     * @param left
     *            the value for left. The value {@code auto} will be assigned
     *            for null value.
     * @param leftUnit
     * @author WFF
     * @return
     * @since 1.0.0
     */
    protected static String getProducedCssValue(final Float top,
            final CssLengthUnit topUnit, final Float right,
            final CssLengthUnit rightUnit, final Float bottom,
            final CssLengthUnit bottomUnit, final Float left,
            final CssLengthUnit leftUnit) {

        if ((ObjectUtil.isEqual(top, right)
                && Objects.equals(topUnit, rightUnit))
                && (ObjectUtil.isEqual(right, bottom)
                        && ObjectUtil.isEqual(rightUnit, bottomUnit))
                && (ObjectUtil.isEqual(bottom, left)
                        && Objects.equals(bottomUnit, leftUnit))) {

            return top != null && topUnit != null
                    ? String.valueOf(top).concat(topUnit.getUnit())
                    : top != null ? String.valueOf(top) : DEFAULT_VALUE;

        } else if ((ObjectUtil.isEqual(top, bottom)
                && Objects.equals(topUnit, bottomUnit))
                && (ObjectUtil.isEqual(right, left)
                        && Objects.equals(rightUnit, leftUnit))) {

            final StringBuilder cssValueBuilder = new StringBuilder();
            if (top != null) {
                cssValueBuilder.append(top);
                if (topUnit != null) {
                    cssValueBuilder.append(topUnit.getUnit());
                }
            }

            cssValueBuilder.append(' ');

            if (right != null) {
                cssValueBuilder.append(right);
                if (rightUnit != null) {
                    cssValueBuilder.append(rightUnit.getUnit());
                }
            }

            return cssValueBuilder.toString();

        } else if ((ObjectUtil.isEqual(right, left)
                && Objects.equals(rightUnit, leftUnit))) {
            final StringBuilder cssValueBuilder = new StringBuilder();
            if (top != null) {
                cssValueBuilder.append(top);
                if (topUnit != null) {
                    cssValueBuilder.append(topUnit.getUnit());
                }
            }

            cssValueBuilder.append(' ');

            if (right != null) {
                cssValueBuilder.append(right);
                if (rightUnit != null) {
                    cssValueBuilder.append(rightUnit.getUnit());
                }
            }

            cssValueBuilder.append(' ');

            if (bottom != null) {
                cssValueBuilder.append(bottom);
                if (bottomUnit != null) {
                    cssValueBuilder.append(bottomUnit.getUnit());
                }
            }

            return cssValueBuilder.toString();

        } else {
            final StringBuilder cssValueBuilder = new StringBuilder();
            if (top != null) {
                cssValueBuilder.append(top);
                if (topUnit != null) {
                    cssValueBuilder.append(topUnit.getUnit());
                }
            }

            cssValueBuilder.append(' ');

            if (right != null) {
                cssValueBuilder.append(right);
                if (rightUnit != null) {
                    cssValueBuilder.append(rightUnit.getUnit());
                }
            }

            cssValueBuilder.append(' ');

            if (bottom != null) {
                cssValueBuilder.append(bottom);
                if (bottomUnit != null) {
                    cssValueBuilder.append(bottomUnit.getUnit());
                }
            }

            cssValueBuilder.append(' ');

            if (left != null) {
                cssValueBuilder.append(left);
                if (leftUnit != null) {
                    cssValueBuilder.append(leftUnit.getUnit());
                }
            }
            return cssValueBuilder.toString();
        }
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

        if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
            return true;
        }

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);
        if (cssValueParts.length > 4) {
            return false;
        }

        for (final String cssValuePart : cssValueParts) {
            final Object[] lengthValueAndUnit = CssLengthUtil
                    .getLengthValueAndUnit(cssValuePart);
            if (lengthValueAndUnit.length == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if its cssValue is any of the values
     *         <code> auto, initial or inherit</code>.
     * @author WFF
     * @since 1.0.0
     */
    public boolean hasPredefinedConstantValue() {
        return PREDEFINED_CONSTANTS.contains(cssValue);
    }

}
