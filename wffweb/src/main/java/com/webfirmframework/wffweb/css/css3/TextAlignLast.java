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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.CssEnumUtil;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * text-align-last: auto|left|right|center|justify|start|end|initial|inherit;
 *
 * @author WFF
 *
 */
public enum TextAlignLast implements CssProperty {

    AUTO, LEFT, RIGHT, CENTER, JUSTIFY, START, END, INITIAL, INHERIT;

    private final String upperCaseSuperToString = super.toString().replace('_',
            '-');

    private final String superToString = TagStringUtil
            .toLowerCase(upperCaseSuperToString);

    private final String toString = getCssName() + ": " + getCssValue();

    private static final Collection<String> UPPER_CASE_SUPER_TO_STRINGS;

    // the lowest length value
    private static final int LOWEST_LENGTH;
    // the highest length value
    private static final int HIGHEST_LENGTH;

    static {
        Collection<String> upperCaseSuperToStringsTemp = new ArrayList<>();
        int min = values()[0].upperCaseSuperToString.length();
        int max = 0;
        for (int i = 0; i < values().length; i++) {
            final int length = values()[i].upperCaseSuperToString.length();
            if (max < length) {
                max = length;
            }
            if (min > length) {
                min = length;
            }
            upperCaseSuperToStringsTemp.add(values()[i].upperCaseSuperToString);
        }
        LOWEST_LENGTH = min;
        HIGHEST_LENGTH = max;
        if (values().length > 10) {
            upperCaseSuperToStringsTemp = new HashSet<>(
                    upperCaseSuperToStringsTemp);
        }
        UPPER_CASE_SUPER_TO_STRINGS = upperCaseSuperToStringsTemp;
    }

    private TextAlignLast() {
    }

    @Override
    public String getCssName() {
        return CssNameConstants.TEXT_ALIGN_LAST;
    }

    @Override
    public String getCssValue() {
        return superToString;
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * checks whether the given given {@code cssValue} is valid for this css
     * property, i.e. whether it can have a corresponding object from it.
     *
     * @param cssValue
     * @return true if the given {@code cssValue} has a corresponding object.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String cssValue) {
        return CssEnumUtil.contains(cssValue, UPPER_CASE_SUPER_TO_STRINGS,
                LOWEST_LENGTH, HIGHEST_LENGTH);
    }

    /**
     * gets the corresponding object for the given {@code cssValue} or null for
     * invalid cssValue.
     *
     * @param cssValue
     *            the css property value without including
     *            <code>!important</code> in it.
     * @return the corresponding object for the given {@code cssValue} or null
     *         for invalid cssValue.
     * @since 1.0.0
     * @author WFF
     */
    public static TextAlignLast getThis(final String cssValue) {
        final String enumString = TagStringUtil.toUpperCase(cssValue)
                .replace('-', '_');

        TextAlignLast correspondingObject = null;
        try {
            correspondingObject = valueOf(enumString);
        } catch (final IllegalArgumentException e) {
        }

        return correspondingObject;
    }
}
