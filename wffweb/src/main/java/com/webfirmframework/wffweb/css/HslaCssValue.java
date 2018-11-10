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
public class HslaCssValue extends AbstractBean<HslaCssValue> {

    private static final long serialVersionUID = 1_0_0L;

    private String hsl = "hsla(0, 0%, 0%, 1)";
    private int h = 0;
    private float s = 0;
    private float l = 0;
    private float a = 1F;

    /**
     * The default value is hsla(0, 0%, 0%, 1).
     *
     * @author WFF
     * @since 1.0.0
     */
    public HslaCssValue() {
    }

    /**
     * @param hslCssValue
     *            eg:- <code>hsla(155, 55%, 75%, 1)</code>
     */
    public HslaCssValue(final String hslCssValue) {
        super();
        extractAndAssign(hslCssValue);
    }

    public HslaCssValue(final HslaCssValue hslCssValue) {
        super();
        extractAndAssign(hslCssValue.getValue());
    }

    /**
     * @param hsl
     *            eg:- <code> hsla(155, 55%, 75%, 1) </code>
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setHsla(final String hsl) {
        extractAndAssign(hsl);
    }

    /**
     * @param hslString
     * @since 1.0.0
     * @author WFF
     */
    private void extractAndAssign(final String hslString) {

        final String hslStringLowerCase = hslString.replace(" ", "")
                .toLowerCase();

        if (hslStringLowerCase.startsWith("hsla(")
                && hslStringLowerCase.contains(")")) {
            final String hsl = hslStringLowerCase.replace(",", ", ");

            final String[] hslStringParts = StringUtil
                    .splitByComma(hslStringLowerCase.substring(
                            hslStringLowerCase.indexOf('(')
                                    + 1,
                            hslStringLowerCase.lastIndexOf(')')));

            if (hslStringParts.length == 4) {
                h = Integer.parseInt(hslStringParts[0]);
                if (h < 0 || h > 360) {
                    throw new InvalidValueException(hslStringLowerCase
                            + " is not a valid hsl string. h value in hslString should be in between 0 to 360, eg: hsla(120, 25%, 15%, 1)");
                }
                s = Float.parseFloat(hslStringParts[1].replace("%", ""));
                if (s < 0 || s > 100) {
                    throw new InvalidValueException(hslStringLowerCase
                            + " is not a valid hsl string. s value in hslString should be in between 0 to 100, eg: hsla(120, 25%, 15%, 1)");
                }
                l = Float.parseFloat(hslStringParts[2].replace("%", ""));
                if (l < 0 || l > 100) {
                    throw new InvalidValueException(hslStringLowerCase
                            + " is not a valid hsl string. l value in hslString should be in between 0 to 100, eg: hsla(120, 25%, 15%, 1)");
                }
                a = Float.parseFloat(hslStringParts[3]);
                if (a < 0 || a > 1) {
                    throw new InvalidValueException(hslStringLowerCase
                            + " is not a valid hsl string. h value in hslString should be in between 0 to 360, eg: hsla(120, 25%, 15%, 1)");
                }
                this.hsl = hsl;
            } else {
                throw new InvalidValueException(hslStringLowerCase
                        + " is not a valid hsl string. It should be in the format of hsla(120, 25%, 15%, 1)");
            }
        } else {
            throw new InvalidValueException(hslStringLowerCase
                    + " is not a valid hsl string. It should be in the format of hsla(120, 25%, 15%, 1)");
        }
    }

    /**
     * @param h
     *            hue value. accepts values only from 0 to 360.
     * @param s
     *            saturation value. accepts values only from 0 to 100.
     * @param l
     *            lightness value. accepts values only from 0 to 100.
     * @param a
     *            alpha value. accepts values only from 0 to 1.
     */
    public HslaCssValue(final int h, final float s, final float l,
            final float a) {
        super();
        if ((h < 0 || h > 360) || (s < 0 || s > 100) || (l < 0 || l > 100)) {
            throw new InvalidValueException(
                    "h should be in between 0 - 360 and s & l should be in between 0 - 100.");
        }
        this.h = h;
        this.s = s;
        this.l = l;
        this.a = a;
        hsl = "hsla(" + h + ", " + s + "%, " + l + "%, " + a + ")";
    }

    public int getH() {
        return h;
    }

    /**
     * @param h
     *            the hue value, it should be in between 0 to 360.
     * @author WFF
     * @since 1.0.0
     */
    public void setH(final int h) {
        if (h < 0 || h > 360) {
            throw new InvalidValueException(
                    "h paramater accept values only from 0 to 360.");
        }
        this.h = h;
        hsl = "hsla(" + h + ", " + s + "%, " + l + "%, " + a + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * @return the saturation value. Its unit is always %.
     * @author WFF
     * @since 1.0.0
     */
    public float getS() {
        return s;
    }

    /**
     * @param s
     *            the saturation value, it should be in between 0 to 100.
     * @author WFF
     * @since 1.0.0
     */
    public void setS(final float s) {
        if (s < 0 || s > 100) {
            throw new InvalidValueException(
                    "s paramater accept values only from 0 to 100.");
        }
        this.s = s;
        hsl = "hsla(" + h + ", " + s + "%, " + l + "%, " + a + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * @return the lightness value. Its unit is always %.
     * @author WFF
     * @since 1.0.0
     */
    public float getL() {
        return l;
    }

    /**
     *
     * @author WFF
     * @return the unit for saturation s.
     * @since 1.0.0
     */
    public static CssLengthUnit getSUnit() {
        return CssLengthUnit.PER;
    }

    /**
     *
     * @author WFF
     * @return the unit for lightness l.
     * @since 1.0.0
     */
    public static CssLengthUnit getLUnit() {
        return CssLengthUnit.PER;
    }

    /**
     * @param l
     *            the lightness value, it should be in between 0 to 100.
     * @author WFF
     * @since 1.0.0
     */
    public void setL(final float l) {
        if (l < 0 || l > 100) {
            throw new InvalidValueException(
                    "l paramater accept values only from 0 to 100.");
        }
        this.l = l;
        hsl = "hsla(" + h + ", " + s + "%, " + l + "%, " + a + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * @param a
     *            the alpha value, it should be in between 0 to 1.
     * @author WFF
     * @since 1.0.0
     */
    public void setA(final float a) {
        if (a < 0 || a > 1) {
            throw new InvalidValueException(
                    "a paramater accept values only from 0 to 1.");
        }
        this.a = a;
        hsl = "hsla(" + h + ", " + s + "%, " + l + "%, " + a + ")";
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * To get the alpha value.
     *
     * @return the a
     * @author WFF
     * @since 1.0.0
     */
    public float getA() {
        return a;
    }

    @Override
    public String toString() {
        return hsl;
    }

    /**
     * @return the print format of these values as a css value.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public String getValue() {
        return hsl;
    }

    /**
     * @param hslString
     *            eg:- hsla(125, 55%, 75%, 1)
     * @return true if valid and false for invalid.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String hslString) {
        try {

            final String hslStringLowerCase = hslString.replace(" ", "")
                    .toLowerCase();

            if (hslStringLowerCase.startsWith("hsla(")
                    && hslStringLowerCase.contains(")")) {

                final String[] hslStringParts = StringUtil
                        .splitByComma(hslStringLowerCase.substring(
                                hslStringLowerCase.indexOf('(') + 1,
                                hslStringLowerCase.lastIndexOf(')')));

                if (hslStringParts.length == 4) {
                    final int r = Integer.parseInt(hslStringParts[0]);
                    if (r < 0 || r > 360) {
                        return false;
                    }
                    if (!hslStringParts[1].endsWith("%")) {
                        return false;
                    }
                    final float g = Float
                            .parseFloat(hslStringParts[1].replace("%", ""));
                    if (g < 0 || g > 100) {
                        return false;
                    }
                    if (!hslStringParts[2].endsWith("%")) {
                        return false;
                    }
                    final float b = Float
                            .parseFloat(hslStringParts[2].replace("%", ""));
                    if (b < 0 || b > 100) {
                        return false;
                    }

                    final float a = Float.parseFloat(hslStringParts[3]);

                    return !(a < 0 || a > 1);
                }
                return false;
            }
        } catch (final NumberFormatException e) {
        }
        return false;
    }

}
