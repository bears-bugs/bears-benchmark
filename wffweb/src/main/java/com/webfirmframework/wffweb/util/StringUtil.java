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

import java.util.Collection;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public final class StringUtil {

    private StringUtil() {
        throw new AssertionError();
    }

    /**
     * Converts all continues multiple spaces to a single space so that the
     * words will have a single space as separator.
     *
     * @param input
     *            the string in which all continues multiple spaces required to
     *            be converted to single space.
     * @return the converted string having a single space in between words.
     * @author WFF
     * @since 1.0.0
     */
    public static String convertToSingleSpace(final String input) {
        if (!input.contains("  ")) {
            return input;
        }
        return input.replaceAll("\\s+", " ");
    }

    /**
     * gets the first substring which starts and ends with the given values.
     *
     * @param inputString
     *            the string from which the substring will be extracted.
     * @param startingWith
     *            to match the starting substring
     * @param endingWith
     *            to match the ending substring
     * @return the substring which starts and ends with the given
     *         {@code startingWith} and {@code endingWith} values. Or returns
     *         null if it doesn't contain.
     * @author WFF
     * @since 1.0.0
     */
    public static String getFirstSubstring(final String inputString,
            final String startingWith, final String endingWith) {
        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return null;
        }
        final int startIndex = inputString.indexOf(startingWith);

        if (!((startIndex + 1) < inputString.length())) {
            return null;
        }

        final int endIndex = inputString.indexOf(endingWith, startIndex + 1)
                + 1;
        if (startIndex > endIndex || startIndex < 0 || endIndex < 0) {
            return null;
        }
        return inputString.substring(startIndex, endIndex);
    }

    /**
     * gets the first substring which starts and ends with the given values.
     *
     * @param inputString
     *            the string from which the substring will be extracted.
     * @param startingWith
     *            to match the starting substring
     * @param endingWith
     *            to match the ending substring
     * @param searchFromIndex
     *            the starting index from where the substring should be
     *            searched.
     *
     * @return the substring which starts and ends with the given
     *         {@code startingWith} and {@code endingWith} values. Or returns
     *         null if it doesn't contain.
     * @author WFF
     * @since 1.0.0
     */
    public static String getFirstSubstring(final String inputString,
            final String startingWith, final String endingWith,
            final int searchFromIndex) {
        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return null;
        }
        final int startIndex = inputString.indexOf(startingWith,
                searchFromIndex);

        if (!((startIndex + 1) < inputString.length())) {
            return null;
        }

        final int endIndex = inputString.indexOf(endingWith, startIndex + 1)
                + 1;
        if (startIndex > endIndex || startIndex < 0 || endIndex < 0) {
            return null;
        }
        return inputString.substring(startIndex, endIndex);
    }

    /**
     * gets all substrings as an array which starts and ends with the given
     * values.<br>
     * Note:- it will never return null instead it will return an empty array
     * (having length zero).
     *
     * <pre>
     *
     * final String[] allSubstrings = getAllSubstrings(&quot;abcd&quot;, &quot;ab&quot;, &quot;cd&quot;);
     * for (String each : allSubstrings) {
     *     System.out.println(each);
     * }
     * the output will be : <i>abcd</i>
     * </pre>
     *
     * @param inputString
     *            the string from which the substrings will be extracted.
     * @param startingWith
     *            to match the starting substring
     * @param endingWith
     *            to match the ending substring
     * @return the array of substrings which contains strings starting and
     *         ending with the given {@code startingWith} and {@code endingWith}
     *         values. Or returns an empty array (i.e an array having length
     *         zero) if it doesn't contain.
     * @author WFF
     * @since 1.0.0
     */
    public static String[] getAllSubstrings(final String inputString,
            final String startingWith, final String endingWith) {
        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return new String[0];
        }

        final int inputStringLength = inputString.length();

        final int endingWithLength = endingWith.length();
        final int maxArrayLength = inputStringLength
                / (startingWith.length() + endingWithLength);

        if (maxArrayLength == 0) {
            return new String[0];
        }

        final String[] maxSubstrings = new String[maxArrayLength];
        int startSearchFromIndex = 0;
        int count = 0;
        do {
            final int startIndex = inputString.indexOf(startingWith,
                    startSearchFromIndex);
            if (!((startIndex + 1) < inputStringLength)) {
                break;
            }
            final int indexOfEndingWith = inputString.indexOf(endingWith,
                    startIndex + 1);

            if (startIndex < 0 || indexOfEndingWith < 0) {
                break;
            }

            final int endIndex = indexOfEndingWith + endingWithLength;
            startSearchFromIndex = endIndex;
            if ((startIndex > endIndex)) {
                startSearchFromIndex = inputStringLength;
            } else {
                maxSubstrings[count] = inputString.substring(startIndex,
                        endIndex);
                count++;
            }
        } while (startSearchFromIndex < inputStringLength);

        final String[] substrings = new String[count];

        // TODO check later if System.arraycopy is better than small sized
        // arrays.
        System.arraycopy(maxSubstrings, 0, substrings, 0, count);

        return substrings;
    }

    /**
     * @param inputString
     *            the string from the index should be found.
     * @param startingWith
     *            the substring to check whether it comes in the given
     *            inputString before {@code endingWith} substring.
     * @param endingWith
     *            the substring to check whether it comes in the given
     *            inputString after {@code startingWith} substring.
     * @return the index of {@code startingWith} substring if the given
     *         {@code inputString} contains a substring starting with
     *         {@code startingWith} string and ending with {@code endingWith}
     *         string, otherwise returns -1.
     * @author WFF
     * @since 1.0.0
     */
    public static int startIndexOf(final String inputString,
            final String startingWith, final String endingWith) {

        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return -1;
        }
        final int startIndex = inputString.indexOf(startingWith);
        if (!((startIndex + 1) < inputString.length())) {
            return -1;
        }
        final int endIndex = inputString.indexOf(endingWith, startIndex + 1);
        if (startIndex > endIndex || startIndex < 0 || endIndex < 0) {
            return -1;
        }
        return startIndex;
    }

    /**
     * @param inputString
     *            the string from the index should be found.
     * @param startingWith
     *            the substring to check whether it comes in the given
     *            inputString before {@code endingWith} substring.
     * @param endingWith
     *            the substring to check whether it comes in the given
     *            inputString after {@code startingWith} substring.
     * @return the index of {@code endingWith} substring if the given
     *         {@code inputString} contains a substring starting with
     *         {@code startingWith} string and ending with {@code endingWith}
     *         string, otherwise returns -1.
     * @author WFF
     * @since 1.0.0
     */
    public static int endIndexOf(final String inputString,
            final String startingWith, final String endingWith) {

        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return -1;
        }
        final int startIndex = inputString.indexOf(startingWith);
        if (!((startIndex + 1) < inputString.length())) {
            return -1;
        }
        final int endIndex = inputString.indexOf(endingWith, startIndex + 1);
        if (startIndex > endIndex || startIndex < 0 || endIndex < 0) {
            return -1;
        }
        return endIndex;
    }

    /**
     * @param inputString
     *            the string from the index should be found.
     * @param startingWith
     *            the substring to check whether it comes in the given
     *            inputString before {@code endingWith} substring.
     * @param endingWith
     *            the substring to check whether it comes in the given
     *            inputString after {@code startingWith} substring.
     * @param searchFromIndex
     *            the starting index from where the substring should be
     *            searched.
     * @return the index of {@code startingWith} substring if the given
     *         {@code inputString} contains a substring starting with
     *         {@code startingWith} string and ending with {@code endingWith}
     *         string, otherwise returns -1.
     * @author WFF
     * @since 1.0.0
     */
    public static int startIndexOf(final String inputString,
            final String startingWith, final String endingWith,
            final int searchFromIndex) {

        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return -1;
        }
        final int startIndex = inputString.indexOf(startingWith,
                searchFromIndex);
        if (!((startIndex + 1) < inputString.length())) {
            return -1;
        }
        final int endIndex = inputString.indexOf(endingWith, startIndex + 1);
        if (startIndex > endIndex || startIndex < 0 || endIndex < 0) {
            return -1;
        }
        return startIndex;
    }

    /**
     * @param inputString
     *            the string from the index should be found.
     * @param startingWith
     *            the substring to check whether it comes in the given
     *            inputString before {@code endingWith} substring.
     * @param endingWith
     *            the substring to check whether it comes in the given
     *            inputString after {@code startingWith} substring.
     * @param searchFromIndex
     *            the starting index from where the substring should be
     *            searched.
     * @return the index of {@code endingWith} substring if the given
     *         {@code inputString} contains a substring starting with
     *         {@code startingWith} string and ending with {@code endingWith}
     *         string, otherwise returns -1.
     * @author WFF
     * @since 1.0.0
     */
    public static int endIndexOf(final String inputString,
            final String startingWith, final String endingWith,
            final int searchFromIndex) {

        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return -1;
        }
        final int startIndex = inputString.indexOf(startingWith,
                searchFromIndex);

        if (!((startIndex + 1) < inputString.length())) {
            return -1;
        }

        final int endIndex = inputString.indexOf(endingWith, startIndex + 1);
        if (startIndex > endIndex || startIndex < 0 || endIndex < 0) {
            return -1;
        }
        return endIndex;
    }

    /**
     * @param inputString
     *            the string from the index should be found.
     * @param startingWith
     *            the substring to check whether it comes in the given
     *            inputString before {@code endingWith} substring.
     * @param endingWith
     *            the substring to check whether it comes in the given
     *            inputString after {@code startingWith} substring.
     * @return an array containing the index of {@code startingWith} substring
     *         if the given {@code inputString} contains a substring starting
     *         with {@code startingWith} string and ending with
     *         {@code endingWith} string, otherwise returns an empty array.
     * @author WFF
     * @since 1.0.0
     */
    public static int[] startIndexAndEndIndexOf(final String inputString,
            final String startingWith, final String endingWith) {

        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return new int[] {};
        }

        final int startIndex = inputString.indexOf(startingWith);

        if (!((startIndex + 1) < inputString.length())) {
            return new int[] {};
        }

        final int endIndex = inputString.indexOf(endingWith, startIndex + 1);
        if (startIndex > endIndex || startIndex < 0 || endIndex < 0) {
            return new int[] {};
        }
        return new int[] { startIndex, endIndex };
    }

    /**
     * @param inputString
     *            the string from the index should be found.
     * @param startingWith
     *            the substring to check whether it comes in the given
     *            inputString before {@code endingWith} substring.
     * @param endingWith
     *            the substring to check whether it comes in the given
     *            inputString after {@code startingWith} substring.
     * @param searchFromIndex
     *            the starting index from where the substring should be
     *            searched.
     * @return an array containing the index of {@code startingWith} substring
     *         if the given {@code inputString} contains a substring starting
     *         with {@code startingWith} string and ending with
     *         {@code endingWith} string, otherwise returns an empty array.
     * @author WFF
     * @since 1.0.0
     */
    public static int[] startIndexAndEndIndexOf(final String inputString,
            final String startingWith, final String endingWith,
            final int searchFromIndex) {

        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return new int[] {};
        }

        final int startIndex = inputString.indexOf(startingWith,
                searchFromIndex);

        if (!((startIndex + 1) < inputString.length())) {
            return new int[0];
        }

        final int endIndex = inputString.indexOf(endingWith, startIndex + 1);
        if (startIndex > endIndex || startIndex < 0 || endIndex < 0) {
            return new int[] {};
        }
        return new int[] { startIndex, endIndex };
    }

    /**
     * gets all start and end indexes as an array of array which start and end
     * with the given values.<br>
     * Note:- it will never return null instead it will return an empty array
     * (having length zero).
     *
     * <pre>
     *
     *         final int[][] startAndEndIndexesOf = startAndEndIndexesOf(
     *                 "121 131 141 151 161 171 181 191 101", "1", "1");
     *
     *         for (final int[] startIndexAndEndIndex : startAndEndIndexesOf) {
     *             System.out.println(startIndexAndEndIndex[0] + " - "
     *                     + startIndexAndEndIndex[1]);
     *         }
     *
     * the output will be :
     * <i>
     * 0 - 3
     * 4 - 7
     * 8 - 11
     * 12 - 15
     * 16 - 19
     * 20 - 23
     * 24 - 27
     * 28 - 31
     * 32 - 35
     * </i>
     *
     *         int[] startIndexAndEndIndex = startAndEndIndexesOf[0]; // 1, 2 etc..
     *         System.out.println(startIndexAndEndIndex[0] + " - "
     *                 + startIndexAndEndIndex[1]);
     *
     * the output will be : <i>0 - 3</i>
     * </pre>
     *
     * @param inputString
     *            the string from which the substrings will be extracted.
     * @param startingWith
     *            to match the starting substring
     * @param endingWith
     *            to match the ending substring
     * @return the array of substrings which contains strings starting and
     *         ending with the given {@code startingWith} and {@code endingWith}
     *         values. Or returns an empty array (i.e an array having length
     *         zero) if it doesn't contain.
     * @author WFF
     * @since 1.0.0
     */
    public static int[][] startAndEndIndexesOf(final String inputString,
            final String startingWith, final String endingWith) {
        if (!inputString.contains(startingWith)
                || !inputString.contains(endingWith)) {
            return new int[0][0];
        }

        final int inputStringLength = inputString.length();

        final int endingWithLength = endingWith.length();
        final int maxArrayLength = inputStringLength
                / (startingWith.length() + endingWithLength);

        if (maxArrayLength == 0) {
            return new int[0][0];
        }

        final int[][] maxIndexes = new int[maxArrayLength][0];
        int startSearchFromIndex = 0;
        int count = 0;
        do {
            final int startIndex = inputString.indexOf(startingWith,
                    startSearchFromIndex);

            if (!((startIndex + 1) < inputStringLength)) {
                return new int[0][0];
            }

            final int indexOfEndingWith = inputString.indexOf(endingWith,
                    startIndex + 1);

            if (startIndex < 0 || indexOfEndingWith < 0) {
                break;
            }

            final int endIndex = indexOfEndingWith + endingWithLength - 1;
            startSearchFromIndex = endIndex;
            if ((startIndex > endIndex)) {
                startSearchFromIndex = inputStringLength;
            } else {
                final int[] is = { startIndex, endIndex };
                maxIndexes[count] = is;
                count++;
            }
        } while (startSearchFromIndex < inputStringLength);

        final int[][] indexes = new int[count][0];

        // TODO check later if System.arraycopy is better than small sized
        // arrays.
        System.arraycopy(maxIndexes, 0, indexes, 0, count);

        return indexes;
    }

    /**
     * To make the clone copy of the given String array. This method is faster
     * than the clone method of String array.
     *
     * @param inputArray
     * @return the cloned array of the given string array.
     * @author WFF
     * @since 1.0.0
     */
    public static String[] cloneArray(final String[] inputArray) {
        final String[] array = new String[inputArray.length];
        System.arraycopy(inputArray, 0, array, 0, inputArray.length);
        return array;
    }

    /**
     *
     * @author WFF
     * @return true if all strings are equal or null.
     * @since 1.0.0
     */
    public static boolean isEqual(final String... strings) {
        final Object[] objects = strings;
        return ObjectUtil.isEqual(objects);
    }

    /**
     *
     * @author WFF
     * @return true if two strings are equal or null.
     * @since 1.0.0
     */
    public static boolean isEqual(final String string1, final String string2) {
        return ObjectUtil.isEqual(string1, string2);
    }

    /**
     * @param value
     * @return true if the given value is starting with a white space
     * @since 2.1.8
     * @author WFF
     */
    public static boolean startsWithWhitespace(final String value) {
        if (value.length() == 0) {
            return false;
        }
        return Character.isWhitespace(value.charAt(0));
    }

    /**
     * @param value
     * @return true if the given value is ending with a white space
     * @since 2.1.8
     * @author WFF
     */
    public static boolean endsWithWhitespace(final String value) {
        if (value.length() == 0) {
            return false;
        }
        return Character.isWhitespace(value.charAt(value.length() - 1));
    }

    /**
     * @param string
     *            the string to split.
     * @param delim
     *            to by which the given string to be split.
     * @return the array of strings split by the given char.
     * @since 3.0.0 public
     * @author WFF
     */
    public static String[] split(final String string, final char delim) {

        final CharSequence[] tmp = new CharSequence[(string.length() / 2) + 1];
        int subCount = 0;
        int i = 0;
        // first substring
        int j = string.indexOf(delim, 0);

        while (j >= 0) {
            tmp[subCount++] = string.substring(i, j);
            i = j + 1;
            // rest of substrings
            j = string.indexOf(delim, i);
        }
        // last substring
        tmp[subCount++] = string.substring(i);

        final String[] finalArray = new String[subCount];
        System.arraycopy(tmp, 0, finalArray, 0, subCount);
        return finalArray;
    }

    /**
     * Splits the given string by space.
     *
     * @param string
     * @return the array of strings
     * @since 2.1.15
     * @author WFF
     */
    public static String[] splitBySpace(final String string) {
        return split(string, ' ');
    }

    /**
     * Splits the given string by comma (,).
     *
     * @param string
     * @return the array of strings
     * @since 2.1.15
     * @author WFF
     */
    public static String[] splitByComma(final String string) {
        return split(string, ',');
    }

    /**
     * Splits the given string by semicolon (;).
     *
     * @param string
     * @return the array of strings
     * @since 2.1.15
     * @author WFF
     */
    public static String[] splitBySemicolon(final String string) {
        return split(string, ';');
    }

    /**
     * Splits the given string by colon (:).
     *
     * @param string
     * @return the array of strings
     * @since 2.1.15
     * @author WFF
     */
    public static String[] splitByColon(final String string) {
        return split(string, ':');
    }

    private static boolean endsWith(final String string, final char c) {
        if (string.length() == 0) {
            return false;
        }
        return string.charAt(string.length() - 1) == c;
    }

    private static boolean startsWith(final String string, final char c) {
        if (string.length() == 0) {
            return false;
        }
        return string.charAt(0) == c;
    }

    /**
     * Checks if the last char is a space char
     *
     * @param string
     * @return true if the last character is a space char
     * @since 2.1.15
     * @author WFF
     */
    public static boolean endsWithSpace(final String string) {
        return endsWith(string, ' ');
    }

    /**
     * Checks if the last char is a colon (:) char
     *
     * @param string
     * @return true if the last character is a colon (:) char
     * @since 2.1.15
     * @author WFF
     */
    public static boolean endsWithColon(final String string) {
        return endsWith(string, ':');
    }

    /**
     * Checks if the first char is a space char
     *
     * @param string
     * @return true if the first character is a space char
     * @since 2.1.15
     * @author WFF
     */
    public static boolean startsWithSpace(final String string) {
        return startsWith(string, ' ');
    }

    private static boolean contains(final String string, final char c) {
        return string.indexOf(c) != -1;
    }

    /**
     * Checks if the given string contains space.
     *
     * @param string
     * @return true if the given string contains space char.
     * @since 2.1.15
     * @author WFF
     */
    public static boolean containsSpace(final String string) {
        return contains(string, ' ');
    }

    /**
     * Checks if the given string contains whitespace.
     *
     * @param string
     * @return true if the given string contains space char.
     * @since 3.0.1
     * @author WFF
     */
    public static boolean containsWhitespace(final String string) {

        for (int i = 0; i < string.length(); i++) {
            if (Character.isWhitespace(string.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the given string contains minus (-).
     *
     * @param string
     * @return true if the given string contains minus (-) char.
     * @since 2.1.15
     * @author WFF
     */
    public static boolean containsMinus(final String string) {
        return contains(string, '-');
    }

    /**
     * Checks if the given string contains plus (+) char.
     *
     * @param string
     * @return true if the given string contains plus (+) char.
     * @since 2.1.15
     * @author WFF
     */
    public static boolean containsPlus(final String string) {
        return contains(string, '+');
    }

    /**
     *
     * Eg:
     *
     * <pre>
     * String join = StringUtil.join(',', ':', ';', "one", "two", "three", "four");
     * prints <i>:one,two,three,four;</i>
     * </pre>
     *
     * This method must be faster than Java 8's StringJoiner.
     *
     * @param delimiter
     * @param prefix
     * @param suffix
     * @param items
     * @return String
     * @since 3.0.1
     */
    public static String join(final char delimiter, final char prefix,
            final char suffix, final String... items) {

        // total delimiters: items.length -1
        // prefix and suffix: 2
        int capacity = (items.length - 1) + 2;
        for (final String item : items) {
            capacity += item.length();
        }

        final StringBuilder builder = new StringBuilder(capacity);
        builder.append(prefix);

        for (final String item : items) {
            builder.append(item).append(delimiter);
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append(suffix);

        return builder.toString();
    }

    /**
     * Eg:
     *
     * <pre>
     * String join = StringUtil.join(',', "one", "two", "three", "four");
     * prints <i>one,two,three,four</i>
     * </pre>
     *
     * This method must be faster than Java 8's String.join method.
     *
     * @param delimiter
     * @param items
     * @return String
     * @since 3.0.1
     */
    public static String join(final char delimiter, final String... items) {

        // total delimiters: items.length -1
        int capacity = (items.length - 1);
        for (final String item : items) {
            capacity += item.length();
        }

        final StringBuilder builder = new StringBuilder(capacity);

        for (final String item : items) {
            builder.append(item).append(delimiter);
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    /**
     * Eg:
     *
     * <pre>
     * String join = StringUtil.join(',', "one", "two", "three", "four");
     * prints <i>one,two,three,four</i>
     * </pre>
     *
     * This method must be faster than Java 8's String.join method.
     *
     * @param delimiter
     * @param items
     * @return String
     * @since 3.0.1
     */
    public static String join(final char delimiter,
            final Collection<String> items) {

        // total delimiters: items.length -1
        int capacity = (items.size() - 1);
        for (final String item : items) {
            capacity += item.length();
        }

        final StringBuilder builder = new StringBuilder(capacity);

        for (final String item : items) {
            builder.append(item).append(delimiter);
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    /**
     * @param s
     *            String to check
     * @return true if the given string doesn't contain any char other than
     *         whitespace.
     * @since 3.0.1
     */
    public static boolean isBlank(final String s) {

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Removes the trailing and leading whitespaces
     *
     * @param s
     * @return the striped string
     * @since 3.0.1
     */
    public static String strip(final String s) {

        int first;
        int last;

        for (first = 0; first < s.length(); first++) {
            if (!Character.isWhitespace(s.charAt(first))) {
                break;
            }
        }

        for (last = s.length(); last > first; last--) {
            if (!Character.isWhitespace(s.charAt(last - 1))) {
                break;
            }
        }

        return s.substring(first, last);
    }

}
