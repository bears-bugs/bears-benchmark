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
 */
package com.webfirmframework.wffweb.util;

import com.webfirmframework.wffweb.css.CssLengthUnit;

/**
 * a utility class for css length value manipulations.
 *
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public final class CssLengthUtil {

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    private CssLengthUtil() {
        throw new AssertionError();
    }

    /**
     * gets the length value and unit as an array. For a cssValue
     * <code>555px</code>, the returned array may be used as
     *
     * <pre>
     * Object[] lengthValueAsPremitiveAndUnit = CssLengthUtil
     *         .getLengthValueAsPremitiveAndUnit(&quot;555px&quot;);
     * float value = (float) lengthValueAsPremitiveAndUnit[0];
     *
     * // the object will be equal to CssLengthUnit.PX
     * CssLengthUnit unit = (CssLengthUnit) lengthValueAsPremitiveAndUnit[1];
     *
     * Auto-boxing is done when the primitive value is stored in an object array
     * therefore there is no much advantage with this method.
     * [This method is left for future modification.]
     *
     * </pre>
     *
     * @param cssValue
     *            the value from which the length value and unit required to be
     *            parsed, Eg:- <code>555px</code>.
     * @return an array containing length and unit. The length will be in the
     *         zeroth index as {@code float} (primitive type) type and its unit
     *         in the first index as an object of {@code CssLengthUnit}. If the
     *         given cssValue doesn't contain unit but contains value then an
     *         array containing only length value (i.e. array having length one)
     *         will be returned. For any invalid value it will return an empty
     *         array (having length zero), specifically it will never return
     *         null.
     *
     * @author WFF
     * @since 1.0.0
     */
    public static Object[] getLengthValueAsPremitiveAndUnit(
            final String cssValue) {

        final String trimmedCssValue = StringUtil.strip(cssValue);
        final char[] cssValueChars = trimmedCssValue.toCharArray();

        int lengthSeparationIndex = -1;

        for (int i = cssValueChars.length - 1; i > -1; i--) {

            final char c = cssValueChars[i];

            if (!(c < '0' || c > '9')) {
                // is a number
                lengthSeparationIndex = i;
                break;
            }

        }

        final String value = StringUtil
                .strip(trimmedCssValue.substring(0, lengthSeparationIndex + 1));

        try {
            if (lengthSeparationIndex == (cssValueChars.length - 1)) {
                return new Object[] { Float.parseFloat(value) };
            }

            String unit = StringUtil.strip(
                    trimmedCssValue.substring(lengthSeparationIndex + 1));

            if (unit.length() == 1 && unit.charAt(0) == '%') {
                return new Object[] { Float.parseFloat(value),
                        CssLengthUnit.PER };
            } else {
                unit = unit.toUpperCase();
            }

            return new Object[] { Float.parseFloat(value),
                    CssLengthUnit.valueOf(unit) };
        } catch (final IllegalArgumentException e) {
            // there will be IllegalArgumentException if the CssLengthUtil is
            // invalid
            // there will be NumberFormatException if the value is invalid
            // if the given css value is invalid it should return an array with
            // length zero
            return new Object[0];
        }

    }

    /**
     * gets the length value and unit as an array. For a cssValue
     * <code>555px</code>, the returned array may be used as
     *
     * <pre>
     * Object[] lengthValueAndUnit = CssLengthUtil
     *         .getLengthValueAndUnit(&quot;555px&quot;);
     * Float value = (Float) lengthValueAndUnit[0];
     *
     * // the object will be equal to CssLengthUnit.PX
     * CssLengthUnit unit = (CssLengthUnit) lengthValueAndUnit[1];
     * </pre>
     *
     * @param cssValue
     *            the value from which the length value and unit required to be
     *            parsed, Eg:- <code>555px</code>.
     * @return an array containing length and unit. The length will be in the
     *         zeroth index as {@code Float} (wrapper type) type and its unit in
     *         the first index as an object of {@code CssLengthUnit}. If the
     *         given cssValue doesn't contain unit but contains value then an
     *         array containing only length value (i.e. array having length one)
     *         will be returned. For any invalid value it will return an empty
     *         array (having length zero), specifically it will never return
     *         null.
     *
     * @author WFF
     * @since 1.0.0
     */
    public static Object[] getLengthValueAndUnit(final String cssValue) {

        final String trimmedCssValue = StringUtil.strip(cssValue);
        final char[] cssValueChars = trimmedCssValue.toCharArray();

        int lengthSeparationIndex = -1;

        for (int i = cssValueChars.length - 1; i > -1; i--) {

            final char c = cssValueChars[i];

            if (!(c < '0' || c > '9')) {
                // is a number
                lengthSeparationIndex = i;
                break;
            }

        }

        final String value = StringUtil
                .strip(trimmedCssValue.substring(0, lengthSeparationIndex + 1));

        try {
            if (lengthSeparationIndex == (cssValueChars.length - 1)) {
                return new Object[] { Float.valueOf(value) };
            }

            String unit = StringUtil.strip(
                    trimmedCssValue.substring(lengthSeparationIndex + 1));

            if (unit.length() == 1 && unit.charAt(0) == '%') {
                return new Object[] { Float.valueOf(value), CssLengthUnit.PER };
            } else {
                unit = unit.toUpperCase();
            }

            return new Object[] { Float.valueOf(value),
                    CssLengthUnit.valueOf(unit) };
        } catch (final IllegalArgumentException e) {
            // there will be IllegalArgumentException if the CssLengthUtil is
            // invalid
            // there will be NumberFormatException if the value is invalid
            // if the given css value is invalid it should return an array with
            // length zero
            return new Object[0];
        }

    }

}
