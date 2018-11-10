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
package com.webfirmframework.wffweb.css.core;

import java.util.Collection;

import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * A utility class for css enum classes.
 *
 * @author WFF
 * @since 1.0.0
 */
public final class CssEnumUtil {

    private CssEnumUtil() {
        throw new AssertionError();
    }

    /**
     * checks whether the given {@code cssValues} contains the given
     * {@code cssValue}.
     *
     * @param cssValue
     *            the value to be checked.
     * @param cssValues
     *            the {@code cssValue} will be checked in this {@code cssValues}
     *            .
     * @param lowestLength
     *            the lowest length of the string contained in the given
     *            {@code cssValues}.
     * @param highestLength
     *            the highest length of the string contained in the given
     *            {@code cssValues}.
     * @return true if the given {@code cssValues} contains the given
     *         {@code cssValue}, and the length of {@code cssValue} is not less
     *         than {@code lowestLength} and not greater than
     *         {@code highestLength}.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean contains(final String cssValue,
            final Collection<String> cssValues, final int lowestLength,
            final int highestLength) {
        if (cssValue == null || cssValue.length() < lowestLength
                || cssValue.length() > highestLength) {
            return false;
        }
        final boolean contains = cssValues
                .contains(TagStringUtil.toUpperCase(cssValue));
        return contains;
    }

}
