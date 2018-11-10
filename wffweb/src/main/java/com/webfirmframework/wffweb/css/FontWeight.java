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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.webfirmframework.wffweb.css.core.CssEnumUtil;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * font-weight: normal|bold|bolder|lighter|<i>number</i>|initial|inherit;
 * <i>number : 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900</i>
 *
 * <i>font-weight: 100</i> is {@code FontWeight.ONE_HUNDRED}, <i>font-weight: 200</i> is {@code FontWeight.TWO_HUNDRED} and so on.
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public enum FontWeight implements CssProperty {

    NORMAL("normal"), BOLD("bold"), BOLDER("bolder"), LIGHTER("lighter"),

    ONE_HUNDRED("100"), TWO_HUNDRED("200"), THREE_HUNDRED("300"),

    FOUR_HUNDRED("400"), FIVE_HUNDRED("500"), SIX_HUNDRED("600"),

    SEVEN_HUNDRED("700"), EIGHT_HUNDRED("800"), NINE_HUNDRED("900"),

    INITIAL("initial"), INHERIT("inherit");

    private final String toString;

    private static final Collection<String> UPPER_CASE_SUPER_TO_STRINGS;

    private static final Map<String, FontWeight> ALL_OBJECTS;

    // the lowest length value
    private static final int LOWEST_LENGTH;
    // the highest length value
    private static final int HIGHEST_LENGTH;

    static {
        ALL_OBJECTS = new HashMap<>();
        Collection<String> upperCaseSuperToStringsTemp = new ArrayList<>();
        int min = values()[0].cssValue.length();
        int max = 0;
        for (int i = 0; i < values().length; i++) {
            final int length = values()[i].cssValue.length();
            if (max < length) {
                max = length;
            }
            if (min > length) {
                min = length;
            }
            final String upperCaseCssValue = TagStringUtil
                    .toUpperCase(values()[i].cssValue);
            upperCaseSuperToStringsTemp.add(upperCaseCssValue);
            ALL_OBJECTS.put(upperCaseCssValue, values()[i]);
        }
        LOWEST_LENGTH = min;
        HIGHEST_LENGTH = max;
        if (values().length > 10) {
            upperCaseSuperToStringsTemp = new HashSet<>(
                    upperCaseSuperToStringsTemp);
        }
        UPPER_CASE_SUPER_TO_STRINGS = upperCaseSuperToStringsTemp;
    }

    private final String cssValue;

    private FontWeight(final String cssValue) {
        this.cssValue = cssValue;
        toString = getCssName() + ": " + cssValue;
    }

    /**
     * @return the name of this enum.
     * @since 1.0.0
     * @author WFF
     */
    public String getEnumName() {
        return super.toString();
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * checks whether the given given {@code colorName} is valid , i.e. whether
     * it can have a corresponding object from it.
     *
     * @param colorName
     * @return true if the given {@code cssColorName} has a corresponding
     *         object.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String colorName) {
        return CssEnumUtil.contains(colorName, UPPER_CASE_SUPER_TO_STRINGS,
                LOWEST_LENGTH, HIGHEST_LENGTH);
    }

    /**
     * gets the corresponding object for the given {@code cssValue} or null for
     * invalid cssValue.
     *
     * @param cssValue
     *            the inbuilt cssValue as per w3 standard.
     * @return the corresponding object for the given {@code cssValue} or null
     *         for invalid cssValue.
     * @since 1.0.0
     * @author WFF
     */
    public static FontWeight getThis(final String cssValue) {
        final String enumString = TagStringUtil.toUpperCase(cssValue);
        return ALL_OBJECTS.get(enumString);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.core.CssProperty#getCssName()
     */
    @Override
    public String getCssName() {
        return CssNameConstants.FONT_WEIGHT;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.core.CssProperty#getCssValue()
     */
    @Override
    public String getCssValue() {
        return cssValue;
    }

}
