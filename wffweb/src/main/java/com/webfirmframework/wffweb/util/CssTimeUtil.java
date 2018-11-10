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

import com.webfirmframework.wffweb.css.CssTimeUnit;

/**
 * a utility class for css time value manipulations.
 *
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public final class CssTimeUtil {

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    private CssTimeUtil() {
        throw new AssertionError();
    }

    /**
     * gets the time value and unit as an array. For a cssValue
     * <code>555ms</code>, the returned array may be used as
     *
     * <pre>
     * Object[] timeValueAsPremitiveAndUnit = CssTimeUtil
     *         .getTimeValueAsPremitiveAndUnit(&quot;555ms&quot;);
     * float value = (float) timeValueAsPremitiveAndUnit[0];
     *
     * // the object will be equal to CssTimeUnit.MS
     * CssTimeUnit unit = (CssTimeUnit) timeValueAsPremitiveAndUnit[1];
     * </pre>
     *
     * @param cssValue
     *            the value from which the time value and unit required to be
     *            parsed, Eg:- <code>555ms</code>.
     * @return an array containing time and unit. The time will be in the zeroth
     *         index as {@code float} (primitive type) type and its unit in the
     *         first index as an object of {@code CssTimeUnit}. If the given
     *         cssValue is not a combination of time value and unit then an
     *         empty array (having time zero) will be returned. It will never
     *         return null.
     *
     * @author WFF
     * @since 1.0.0
     */
    public static Object[] getTimeValueAsPremitiveAndUnit(
            final String cssValue) {
        for (final CssTimeUnit cssTimeUnit : CssTimeUnit.values()) {
            final String unit = cssTimeUnit.getUnit();
            if (cssValue.endsWith(unit)) {
                final String valueOnly = cssValue.replaceFirst(unit, "");
                try {
                    return new Object[] { Float.parseFloat(valueOnly),
                            cssTimeUnit };
                } catch (final NumberFormatException e) {
                    return new Object[0];
                }
            }
        }
        return new Object[0];
    }

    /**
     * gets the time value and unit as an array. For a cssValue
     * <code>555ms</code>, the returned array may be used as
     *
     * <pre>
     * Object[] timeValueAndUnit = CssTimeUtil.getTimeValueAndUnit(&quot;555ms&quot;);
     * Float value = (Float) timeValueAndUnit[0];
     *
     * // the object will be equal to CssTimeUnit.MS
     * CssTimeUnit unit = (CssTimeUnit) timeValueAndUnit[1];
     * </pre>
     *
     * @param cssValue
     *            the value from which the time value and unit required to be
     *            parsed, Eg:- <code>555ms</code>.
     * @return an array containing time and unit. The time will be in the zeroth
     *         index as {@code Float} (wrapper type) type and its unit in the
     *         first index as an object of {@code CssTimeUnit}. If the given
     *         cssValue is not a combination of time value and unit then an
     *         empty array (having time zero) will be returned. It will never
     *         return null.
     *
     * @author WFF
     * @since 1.0.0
     */
    public static Object[] getTimeValueAndUnit(final String cssValue) {
        for (final CssTimeUnit cssTimeUnit : CssTimeUnit.values()) {
            final String unit = cssTimeUnit.getUnit();
            if (cssValue.endsWith(unit)) {
                final String valueOnly = cssValue.replaceFirst(unit, "");
                try {
                    return new Object[] { Float.valueOf(valueOnly),
                            cssTimeUnit };
                } catch (final NumberFormatException e) {
                    return new Object[0];
                }
            }
        }
        return new Object[0];
    }

}
