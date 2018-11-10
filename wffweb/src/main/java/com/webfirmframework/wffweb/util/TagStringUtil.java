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
package com.webfirmframework.wffweb.util;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public final class TagStringUtil {

    private static final char[] UPPER_CASE_CHARS = CharsetUtil
            .getUpperCaseCharset(512);

    private static final char[] LOWER_CASE_CHARS = CharsetUtil
            .getLowerCaseCharset(512);

    private TagStringUtil() {
        throw new AssertionError();
    }

    /**
     * In html, the tag characters are supposed to use a limited number of
     * characters like 'a'-'z', 'A'-'Z', '0'-'9', '.', ',', '-' etc... only. *
     * This method gains performance than the native
     * {@code String.toLowerCase()} method.
     *
     * @param value
     *            The String to lower case
     * @return The lower case string
     * @author WFF
     */
    public static String toLowerCase(final String value) {
        final char[] chars = value.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            chars[i] = LOWER_CASE_CHARS[chars[i]];
        }

        return new String(chars);
    }

    /**
     * In html, the tag characters are supposed to use a limited number of
     * characters like 'a'-'z', 'A'-'Z', '0'-'9', '.', ',', '-' etc...<br>
     * This method gains performance than the native
     * {@code String.toUpperCase()} method.
     *
     * @param value
     *            The String to lower case
     * @return The upper case string
     * @author WFF
     */
    public static String toUpperCase(final String value) {
        final char[] chars = value.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            chars[i] = UPPER_CASE_CHARS[chars[i]];
        }

        return new String(chars);
    }

}
