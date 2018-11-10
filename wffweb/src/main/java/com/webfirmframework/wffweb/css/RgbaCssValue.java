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

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.data.AbstractBean;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class RgbaCssValue extends AbstractBean<RgbaCssValue> {

    private static final long serialVersionUID = 1_0_0L;

    private String rgba = "rgba(0, 0, 0, 1)";
    private int r = 0;
    private int g = 0;
    private int b = 0;
    private float a = 1F;

    /**
     * The default value is rgba(0, 0, 0, 1)
     *
     * @author WFF
     * @since 1.0.0
     */
    public RgbaCssValue() {
    }

    /**
     * @param rgbaCssValue
     *            eg:- <code> rgba(15, 55, 155, 1) </code>
     */
    public RgbaCssValue(final String rgbaCssValue) {
        super();
        extractAndAssign(rgbaCssValue);
    }

    /**
     * @param rgbaCssValue
     * @author WFF
     * @since 1.0.0
     */
    public RgbaCssValue(final RgbaCssValue rgbaCssValue) {
        super();
        extractAndAssign(rgbaCssValue.getValue());
    }

    /**
     * @param rgba
     *            eg:- <code> rgba(15, 55, 155, 0.5) </code>
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setRgba(final String rgba) {
        extractAndAssign(rgba);
    }

    /**
     * @param rgbaString
     * @since 1.0.0
     * @author WFF
     */
    private void extractAndAssign(final String rgbaString) {

        final String rgbaStringLowerCase = rgbaString.replace(" ", "")
                .toLowerCase();

        if (rgbaStringLowerCase.startsWith("rgba(")
                && rgbaStringLowerCase.contains(")")) {
            final String rgba = rgbaStringLowerCase.replace(",", ", ");

            final String[] rgbaStringParts = StringUtil
                    .splitByComma(rgbaStringLowerCase.substring(
                            rgbaStringLowerCase.indexOf('(')
                                    + 1,
                            rgbaStringLowerCase.lastIndexOf(')')));

            if (rgbaStringParts.length == 4) {
                r = Integer.parseInt(rgbaStringParts[0]);
                if (r < 0 || r > 255) {
                    throw new InvalidValueException(rgbaStringLowerCase
                            + " is not a valid rgba string. r value in rgbaString should be inbetween 0 to 255, eg: rgba(255, 25, 15, 0.1)");
                }
                g = Integer.parseInt(rgbaStringParts[1]);
                if (g < 0 || g > 255) {
                    throw new InvalidValueException(rgbaStringLowerCase
                            + " is not a valid rgba string. g value in rgbaString should be inbetween 0 to 255, eg: rgba(255, 25, 15, 0.1)");
                }
                b = Integer.parseInt(rgbaStringParts[2]);
                if (b < 0 || b > 255) {
                    throw new InvalidValueException(rgbaStringLowerCase
                            + " is not a valid rgba string. b value in rgbaString should be inbetween 0 to 255, eg: rgba(255, 25, 15, 0.1)");
                }
                a = Float.parseFloat(rgbaStringParts[3]);
                if (a < 0 || a > 1) {
                    throw new InvalidValueException(rgbaStringLowerCase
                            + " is not a valid rgba string. a value (i.e. alpha) in rgbaString should be inbetween 0 to 1, eg: rgba(255, 25, 15, 0.1)");
                }
                this.rgba = rgba;
            } else {
                throw new InvalidValueException(rgbaStringLowerCase
                        + " is not a valid rgba string. It should be in the format of rgba(255, 25, 15, 0.1)");
            }
        } else {
            throw new InvalidValueException(rgbaStringLowerCase
                    + " is not a valid rgba string. It should be in the format of rgba(255, 25, 15, 0.1)");
        }
    }

    /**
     * @param r
     *            red value. accepts values only from 0 to 255.
     * @param g
     *            green value. accepts values only from 0 to 255.
     * @param b
     *            blue value. accepts values only from 0 to 255.
     * @param a
     *            alpha value. accepts values only from 0 to 1.
     */
    public RgbaCssValue(final int r, final int g, final int b, final float a) {
        super();
        if ((r < 0 || r > 255) || (g < 0 || g > 255) || (b < 0 || b > 255)
                || (a < 0 || a > 1)) {
            throw new InvalidValueException(
                    "r, b and g paramater accept values only from 0 to 255.");
        }

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        rgba = "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
    }

    /**
     * @return the red value.
     * @author WFF
     * @since 1.0.0
     */
    public int getR() {
        return r;
    }

    /**
     * to set the red value which is in between 0 to 255.
     *
     * @param r
     *            represents red
     * @author WFF
     * @since 1.0.0
     */
    public void setR(final int r) {
        if (r < 0 || r > 255) {
            throw new InvalidValueException(
                    "r paramater accept values only from 0 to 255.");
        }
        this.r = r;
        rgba = "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * @return the green value.
     * @author WFF
     * @since 1.0.0
     */
    public int getG() {
        return g;
    }

    /**
     * to set the green value which is in between 0 to 255.
     *
     * @param g
     *            represents green
     * @author WFF
     * @since 1.0.0
     */
    public void setG(final int g) {
        if (g < 0 || g > 255) {
            throw new InvalidValueException(
                    "g paramater accept values only from 0 to 255.");
        }
        this.g = g;
        rgba = "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * @return the blue value.
     * @author WFF
     * @since 1.0.0
     */
    public int getB() {
        return b;
    }

    /**
     * @return the alpha value.
     * @author WFF
     * @since 1.0.0
     */
    public float getA() {
        return a;
    }

    /**
     * to set the blue value which is in between 0 to 255.
     *
     * @param b
     * @author WFF
     * @since 1.0.0
     */
    public void setB(final int b) {
        if (b < 0 || b > 255) {
            throw new InvalidValueException(
                    "b paramater accept values only from 0 to 255.");
        }
        this.b = b;
        rgba = "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * to set alpha. The default value is 0.
     *
     * @param a
     * @author WFF
     * @since 1.0.0
     */
    public void setA(final float a) {
        if (a < 0 || a > 1) {
            throw new InvalidValueException(
                    "a paramater accept values only from 0 to 255.");
        }
        this.a = a;
        rgba = "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    @Override
    public String toString() {
        return rgba;
    }

    /**
     * @return the print format of these values as a css value.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public String getValue() {
        return rgba;
    }

    /**
     * @param rgbaString
     *            eg:- rgba(25, 155, 55, 0.2)
     * @return true if valid and false for invalid.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String rgbaString) {
        try {
            final String rgbaStringLowerCase = rgbaString.replace(" ", "")
                    .toLowerCase();
            if (rgbaStringLowerCase.startsWith("rgba(")
                    && rgbaStringLowerCase.contains(")")) {

                final String[] rgbaStringParts = StringUtil
                        .splitByComma(rgbaStringLowerCase.substring(
                                rgbaStringLowerCase.indexOf('(') + 1,
                                rgbaStringLowerCase.lastIndexOf(')')));

                if (rgbaStringParts.length == 4) {
                    final int r = Integer.parseInt(rgbaStringParts[0]);
                    if (r < 0 || r > 255) {
                        return false;
                    }
                    final int g = Integer.parseInt(rgbaStringParts[1]);
                    if (g < 0 || g > 255) {
                        return false;
                    }
                    final int b = Integer.parseInt(rgbaStringParts[2]);
                    if (b < 0 || b > 255) {
                        return false;
                    }

                    final float a = Float.parseFloat(rgbaStringParts[3]);

                    return !(a < 0 || a > 1);
                }
                return false;
            }
        } catch (final NumberFormatException e) {
        }
        return false;
    }
}
