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
public class RgbCssValue extends AbstractBean<RgbCssValue> {

    private static final long serialVersionUID = 1_0_0L;

    private String rgb = "rgb(0, 0, 0)";
    private int r = 0;
    private int g = 0;
    private int b = 0;

    public RgbCssValue() {
    }

    /**
     * @param rgbCssValue
     *            eg:- <code> rgb(15, 55, 155) </code>
     */
    public RgbCssValue(final String rgbCssValue) {
        super();
        extractAndAssign(rgbCssValue);
    }

    public RgbCssValue(final RgbCssValue rgbCssValue) {
        super();
        extractAndAssign(rgbCssValue.getValue());
    }

    /**
     * @param rgb
     *            eg:- <code> rgb(15, 55, 155) </code>
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setRgb(final String rgb) {
        extractAndAssign(rgb);
    }

    /**
     * @param rgbString
     * @since 1.0.0
     * @author WFF
     */
    private void extractAndAssign(final String rgbString) {

        final String rgbStringLowerCase = rgbString.replace(" ", "")
                .toLowerCase();

        if (rgbStringLowerCase.startsWith("rgb(")
                && rgbStringLowerCase.contains(")")) {
            final String rgb = rgbStringLowerCase.replace(",", ", ");

            final String[] rgbStringParts = StringUtil
                    .splitByComma(rgbStringLowerCase.substring(
                            rgbStringLowerCase.indexOf('(')
                                    + 1,
                            rgbStringLowerCase.lastIndexOf(')')));

            if (rgbStringParts.length == 3) {
                r = Integer.parseInt(rgbStringParts[0]);
                if (r < 0 || r > 255) {
                    throw new InvalidValueException(rgbStringLowerCase
                            + " is not a valid rgb string. r value in rgbString should be inbetween 0 to 255, eg: rgb(255, 25, 15)");
                }
                g = Integer.parseInt(rgbStringParts[1]);
                if (g < 0 || g > 255) {
                    throw new InvalidValueException(rgbStringLowerCase
                            + " is not a valid rgb string. g value in rgbString should be inbetween 0 to 255, eg: rgb(255, 25, 15)");
                }
                b = Integer.parseInt(rgbStringParts[2]);
                if (b < 0 || b > 255) {
                    throw new InvalidValueException(rgbStringLowerCase
                            + " is not a valid rgb string. b value in rgbString should be inbetween 0 to 255, eg: rgb(255, 25, 15)");
                }
                this.rgb = rgb;
            } else {
                throw new InvalidValueException(rgbStringLowerCase
                        + " is not a valid rgb string. It should be in the format of rgb(255, 25, 15)");
            }
        } else {
            throw new InvalidValueException(rgbStringLowerCase
                    + " is not a valid rgb string. It should be in the format of rgb(255, 25, 15)");
        }
    }

    /**
     * @param r
     *            red value. accepts values only from 0 to 255.
     * @param g
     *            green value. accepts values only from 0 to 255.
     * @param b
     *            blue value. accepts values only from 0 to 255.
     */
    public RgbCssValue(final int r, final int g, final int b) {
        super();
        if ((r < 0 || r > 255) || (g < 0 || g > 255) || (b < 0 || b > 255)) {
            throw new InvalidValueException(
                    "r, b and g paramater accept values only from 0 to 255.");
        }
        this.r = r;
        this.g = g;
        this.b = b;
        rgb = "rgb(" + r + ", " + g + ", " + b + ")";
    }

    public int getR() {
        return r;
    }

    public void setR(final int r) {
        if (r < 0 || r > 255) {
            throw new InvalidValueException(
                    "r paramater accept values only from 0 to 255.");
        }
        this.r = r;
        rgb = "rgb(" + r + ", " + g + ", " + b + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    public int getG() {
        return g;
    }

    public void setG(final int g) {
        if (g < 0 || g > 255) {
            throw new InvalidValueException(
                    "g paramater accept values only from 0 to 255.");
        }
        this.g = g;
        rgb = "rgb(" + r + ", " + g + ", " + b + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    public int getB() {
        return b;
    }

    public void setB(final int b) {
        if (b < 0 || b > 255) {
            throw new InvalidValueException(
                    "b paramater accept values only from 0 to 255.");
        }
        this.b = b;
        rgb = "rgb(" + r + ", " + g + ", " + b + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    @Override
    public String toString() {
        return rgb;
    }

    /**
     * @return the print format of these values as a css value.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public String getValue() {
        return rgb;
    }

    /**
     * @param rgbString
     *            eg:- rgb(25, 155, 55)
     * @return true if valid and false for invalid.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String rgbString) {
        try {
            final String rgbStringLowerCase = rgbString.replace(" ", "")
                    .toLowerCase();
            if (rgbStringLowerCase.startsWith("rgb(")
                    && rgbStringLowerCase.contains(")")) {

                final String[] rgbStringParts = StringUtil
                        .splitByComma(rgbStringLowerCase.substring(
                                rgbStringLowerCase.indexOf('(') + 1,
                                rgbStringLowerCase.lastIndexOf(')')));

                if (rgbStringParts.length == 3) {
                    final int r = Integer.parseInt(rgbStringParts[0]);
                    if (r < 0 || r > 255) {
                        return false;
                    }
                    final int g = Integer.parseInt(rgbStringParts[1]);
                    if (g < 0 || g > 255) {
                        return false;
                    }

                    final int b = Integer.parseInt(rgbStringParts[2]);

                    return !(b < 0 || b > 255);
                }
                return false;
            }
        } catch (final NumberFormatException e) {
        }
        return false;
    }
}
