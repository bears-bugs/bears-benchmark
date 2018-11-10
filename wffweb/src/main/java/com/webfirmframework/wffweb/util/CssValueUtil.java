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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public final class CssValueUtil {

    private static final Map<String, String> CSS_VALUE_PART_START_END_VALUES;

    static {
        CSS_VALUE_PART_START_END_VALUES = new HashMap<>();
        CSS_VALUE_PART_START_END_VALUES.put("rgb(", ")");
        CSS_VALUE_PART_START_END_VALUES.put("rgba(", ")");
        CSS_VALUE_PART_START_END_VALUES.put("hsl(", ")");
        CSS_VALUE_PART_START_END_VALUES.put("hsla(", ")");
    }

    private CssValueUtil() {
        throw new AssertionError();
    }

    /**
     * The given value is used by Web Firm Framework Index Based Extraction
     * algorithm to extract css value parts, i.e. the
     * {@code CssValueUtil#split(String)} is extracting values based on the
     * given values. <br>
     * Eg :-
     *
     * <pre>
     * addCssValuePartStartEndValue(&quot;rgb(&quot;, &quot;)&quot;);
     * </pre>
     *
     * And the following <code>"rgb(", ")"</code>, <code>"rgba(", ")"</code>,
     * <code>"hsl(", ")"</code>, <code>"hsla(", ")"</code> values are already
     * added.
     *
     * @param start
     * @param end
     * @author WFF
     * @since 1.0.0
     */
    public void addCssValuePartStartEndValue(final String start,
            final String end) {
        CSS_VALUE_PART_START_END_VALUES.put(start, end);
    }

    /**
     * splits each cssValue part based on the css rule. <br>
     * This extraction is done based on the Web Firm Framework Index Based
     * Extraction algorithm. It will throw InvalidValueException if the given
     * cssValue contains no space in between any valid cssValue part, eg:-
     * <code>rgb(1, 2, 5)rgb(11, 12, 15)</code>. And, it doesn't validate the
     * extracted cssValue parts so the extracted cssValue parts (i.e. the
     * returned {@code List<String>}) may contain invalid cssValue part.<br>
     * <br>
     * Sample code to test
     *
     * <pre>
     * final List&lt;String&gt; splitTest = CssValueUtil.split(&quot;hsla(7, 8, 9, 1) red rgb(3, 5, 6) rgb(7, 8, 9) rgba(7, 8, 9, 1) middle hsl(10, 11, 12) green   blue&quot;);
     * for (final String each : splitTest) {
     *     System.out.println(each);
     * }
     *
     * gives out as :
     * <i>
     * hsla(7, 8, 9, 1)
     * red
     * rgb(3, 5, 6)
     * rgb(7, 8, 9)
     * rgba(7, 8, 9, 1)
     * middle
     * hsl(10, 11, 12)
     * green
     * blue</i>
     * </pre>
     *
     *
     * @param cssValue
     *            the value from which the css parts will be extracted
     * @return the list containing cssValue parts
     * @author WFF
     * @since 1.0.0
     */
    public static List<String> split(final String cssValue) {
        try {

            final Map<Integer, int[]> startAndEndIndexes = new TreeMap<>();

            for (final Entry<String, String> entry : CSS_VALUE_PART_START_END_VALUES
                    .entrySet()) {

                final int[][] rgbStartAndEndIndexesOf = StringUtil
                        .startAndEndIndexesOf(cssValue, entry.getKey(),
                                entry.getValue());
                for (final int[] each : rgbStartAndEndIndexesOf) {
                    startAndEndIndexes.put(each[0], each);
                }
            }

            if (startAndEndIndexes.size() == 0) {
                return Arrays.asList(StringUtil.splitBySpace(
                        StringUtil.convertToSingleSpace(cssValue)));
            }

            final List<String> cssValueParts = new ArrayList<>();

            int count = 0;
            final int startAndEndIndexesSize = startAndEndIndexes.size();

            int[] previousStartEndIndex = null;

            for (final Entry<Integer, int[]> entry : startAndEndIndexes
                    .entrySet()) {

                count++;

                final int[] currentStartEndIndex = entry.getValue();

                final int zerothSlotCurrent = currentStartEndIndex[0];
                final int firstSlotCurrent = currentStartEndIndex[1];

                if (previousStartEndIndex != null) {

                    final int firstSlotOfPrevious = previousStartEndIndex[1];

                    // the difference should be more than two, 1 for at least
                    // one char (css value having length one) + one for space
                    // char.
                    if ((zerothSlotCurrent - firstSlotOfPrevious) > 2) {

                        final String cssValuePart2 = cssValue.substring(
                                firstSlotOfPrevious + 1, zerothSlotCurrent);

                        final String trimmedCssValuePart2 = StringUtil
                                .strip(cssValuePart2);

                        if (StringUtil.startsWithWhitespace(cssValuePart2)
                                && StringUtil
                                        .endsWithWhitespace(cssValuePart2)) {
                            if (!trimmedCssValuePart2.isEmpty()) {
                                for (final String each : StringUtil
                                        .splitBySpace(trimmedCssValuePart2)) {
                                    final String trimmed;
                                    if (!(trimmed = StringUtil.strip(each))
                                            .isEmpty()) {
                                        cssValueParts.add(trimmed);
                                    }
                                }
                            }
                        } else {
                            throw new InvalidValueException(
                                    "there must be a space between each part of cssValue, a space is expected before and/or after '"
                                            + cssValuePart2 + "'");
                        }

                    } else if ((zerothSlotCurrent - firstSlotOfPrevious) == 1) {
                        throw new InvalidValueException(
                                "there must be a space between each part of cssValue, a space is expected before '"
                                        + cssValue.substring(zerothSlotCurrent)
                                        + "'");
                    }

                    cssValueParts
                            .add(cssValue.substring(currentStartEndIndex[0],
                                    currentStartEndIndex[1] + 1));

                } else {

                    // to check whether the starting cssValue part has at least
                    // two chars, 1 for at least one char (css value having
                    // length one) + one for space char.
                    // greater than 1 is enough since zerothSlotCurrent is an
                    // index number.

                    if ((zerothSlotCurrent) > 1) {
                        final String cssValuePart2 = cssValue.substring(0,
                                zerothSlotCurrent);

                        final String trimmedCssValuePart2 = StringUtil
                                .strip(cssValuePart2);

                        if (StringUtil.endsWithWhitespace(cssValuePart2)) {
                            for (final String each : StringUtil
                                    .splitBySpace(trimmedCssValuePart2)) {
                                final String trimmed;
                                if (!(trimmed = StringUtil.strip(each))
                                        .isEmpty()) {
                                    cssValueParts.add(trimmed);
                                }
                            }

                        } else {
                            throw new InvalidValueException(
                                    "there must be a space between each part of cssValue, a space is expected after '"
                                            + cssValuePart2 + "'");
                        }

                    }

                    cssValueParts
                            .add(cssValue.substring(currentStartEndIndex[0],
                                    currentStartEndIndex[1] + 1));

                }

                final int cssValueLength = cssValue.length();
                if (count == startAndEndIndexesSize
                        && (firstSlotCurrent < (cssValueLength - 1))) {

                    final String cssValuePart2 = cssValue
                            .substring(firstSlotCurrent + 1);

                    final String trimmedCssValuePart2 = StringUtil
                            .strip(cssValuePart2);

                    if (StringUtil.startsWithWhitespace(cssValuePart2)) {
                        for (final String each : StringUtil
                                .splitBySpace(trimmedCssValuePart2)) {
                            if (!each.isEmpty()) {
                                cssValueParts.add(each);
                            }
                        }

                    } else {
                        throw new InvalidValueException(
                                "there must be a space between each part of cssValue, a space is expected before '"
                                        + cssValuePart2 + "'");
                    }
                }

                previousStartEndIndex = currentStartEndIndex;

            }

            return cssValueParts;
        } catch (final RuntimeException e) {
            throw new InvalidValueException(e);
        }
    }

    /**
     * This method throws {@code NullValueException} if the given
     * {@code cssValue} is null or {@code InvalidValueException} if the given
     * {@code cssValue} is blank.
     *
     * @param cssValue
     * @throws NullValueException
     * @throws InvalidValueException
     * @author WFF
     * @since 1.0.0
     */
    public static void throwExceptionForInvalidValue(final String cssValue)
            throws NullValueException, InvalidValueException {
        if (cssValue == null) {
            throw new NullValueException("the cssValue should not be null.");
        } else if (StringUtil.isBlank(cssValue)) {
            throw new InvalidValueException(
                    "the cssValue should not be blank.");
        }
    }

}
