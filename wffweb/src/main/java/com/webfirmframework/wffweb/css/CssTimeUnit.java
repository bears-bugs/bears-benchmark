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

import com.webfirmframework.wffweb.css.core.TimeUnit;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * @author WFF
 * @since 1.0.0
 *
 */
public enum CssTimeUnit implements TimeUnit {

    // should be in descending order of length of value

    /**
     * represents milliseconds.
     */
    MS("ms"),

    /**
     * represents seconds.
     */
    S("s");

    private String valueString;

    private CssTimeUnit(final String unit) {
        valueString = unit;
    }

    @Override
    public String toString() {
        return valueString;
    }

    @Override
    public String getUnit() {
        return valueString;
    }

    /**
     * gets the corresponding object for the given {@code unitName} or null for
     * invalid unit name. The {@code unitName} is case insensitive. If the
     * passing {@code unitName} is always in upper case then it will be better
     * to use {@code CssTimeUnit.valueOf(String)} method because this method
     * internally converts the {@code unitName} string to upper case (uses a bit
     * optimized way of conversion) and gets the object by
     * {@code CssTimeUnit.valueOf(String)} method.
     *
     * @param unitName
     *            the name of the unit eg:- <code>s</code> or <code>ms</code>.
     * @return the corresponding object for the given {@code unitName} or null
     *         for invalid unitName.
     * @since 1.0.0
     * @author WFF
     */
    public static CssTimeUnit getThis(final String unitName) {
        final String enumString = TagStringUtil.toUpperCase(unitName);

        CssTimeUnit correspondingObject = null;
        try {
            correspondingObject = valueOf(enumString);
        } catch (final IllegalArgumentException e) {
        }

        return correspondingObject;
    }
}
