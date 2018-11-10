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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.StringBuilderUtil;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 * font-family: <i>font</i>|initial|inherit;
 *
 * Value:       {@code [[ <family-name> | <generic-family> ] [, <family-name>| <generic-family>]* ] | inherit}
 * Initial:        depends on user agent
 * Applies to:     all elements
 * Inherited:      yes
 * Percentages:    N/A
 * Media:          visual
 * Computed value:         as specified
 *
 * Support Classes :-
 * 1. GenericFontFamilyNameContants
 * </pre>
 *
 *
 * @author WFF
 * @version 1.0.0
 * @since 1.0.0
 */
public class FontFamily extends AbstractCssProperty<FontFamily> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(FontFamily.class.getName());

    private static final Set<String> FONT_FAMILY_NAMES = new HashSet<String>() {

        private static final long serialVersionUID = 1_0_0L;

        @Override
        public boolean add(final String value) {
            return super.add(TagStringUtil.toLowerCase(value));
        }

        @Override
        public boolean addAll(final Collection<? extends String> values) {
            boolean modified = false;
            for (final String each : values) {
                if (add(each)) {
                    modified = true;
                }
            }
            return modified;
        }

        @Override
        public boolean remove(final Object o) {
            return super.remove(TagStringUtil.toLowerCase(o.toString()));
        };

    };

    static {
        FONT_FAMILY_NAMES.add(GenericFontFamilyNameContants.CURSIVE);
        FONT_FAMILY_NAMES.add(GenericFontFamilyNameContants.FANTASY);
        FONT_FAMILY_NAMES.add(GenericFontFamilyNameContants.MONOSPACE);
        FONT_FAMILY_NAMES.add(GenericFontFamilyNameContants.SANS_SERIF);
        FONT_FAMILY_NAMES.add(GenericFontFamilyNameContants.SERIF);
    }

    private boolean validateFontFamilyName;
    private static boolean validateFontFamilyNameGlobally;

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    public static final String[] EMPTY_ARRAY = new String[0];

    private String cssValue;

    private String[] fontFamilyNames;

    /**
     * The {@code initial} will be set as the value
     */
    public FontFamily() {
        setCssValue(INITIAL);
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public FontFamily(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * <pre>
     * Eg:-
     * String[] fontFamilyNames = {"Times New Roman", "Georgia", GenericFontFamilyNameContants.SERIF};
     *
     * </pre>
     *
     * @param fontFamilyNames
     *            the font family names.
     */
    public FontFamily(final String... fontFamilyNames) {
        this.fontFamilyNames = StringUtil.cloneArray(fontFamilyNames);
        cssValue = getBuiltCssValue(this.fontFamilyNames);
    }

    /**
     * @param fontFamilyNames
     * @return the built cssValue string from the given fontFamilyNames.
     * @author WFF
     * @since 1.0.0
     */
    protected static String getBuiltCssValue(final String... fontFamilyNames) {
        final StringBuilder cssValueSB = new StringBuilder();
        int count = 1;
        for (final String fontFamilyName : fontFamilyNames) {
            if (StringUtil.containsSpace(fontFamilyName)) {
                cssValueSB.append('\"').append(fontFamilyName).append('\"');
            } else {
                cssValueSB.append(fontFamilyName);
            }

            if (count != fontFamilyNames.length) {
                cssValueSB.append(", ");
            } else {
                break;
            }
            count++;
        }
        return StringBuilderUtil.getTrimmedString(cssValueSB);
    }

    /**
     * @param fontFamily
     *            the {@code FontFamily} object from which the cssValue to
     *            set.And, {@code null} will throw {@code NullValueException}
     */
    public FontFamily(final FontFamily fontFamily) {
        if (fontFamily == null) {
            throw new NullValueException("fontFamily can not be null");
        }
        setCssValue(fontFamily.getCssValue());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.CssProperty#getCssName()
     *
     * @since 1.0.0
     *
     * @author WFF
     */
    @Override
    public String getCssName() {
        return CssNameConstants.FONT_FAMILY;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.CssProperty#getCssValue()
     *
     * @since 1.0.0
     *
     * @author WFF
     */
    @Override
    public String getCssValue() {
        return cssValue;
    }

    @Override
    public String toString() {
        return getCssName() + ": " + getCssValue();
    }

    /**
     * @param cssValue
     *            The value should be a fontFamilies sequence for example \
     *            "Times New Roman\", Georgia, Serif Or initial/inherit.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public FontFamily setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value should be a fontFamilies sequence for example \"Times New Roman\", Georgia, Serif Or initial/inherit.");
        } else {
            final String trimmedCssValue = StringUtil.strip(cssValue);

            if (INITIAL.equals(cssValue) || INHERIT.equals(cssValue)) {
                fontFamilyNames = null;
            } else {
                fontFamilyNames = getExtractedFamilyNames(trimmedCssValue,
                        validateFontFamilyName
                                || validateFontFamilyNameGlobally);
            }
            this.cssValue = trimmedCssValue;

            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        }
        return this;
    }

    /**
     * @param familyNames
     * @param validate
     *            TODO
     * @return an array containing extracted family names from the given input.
     * @author WFF
     * @since 1.0.0
     */
    protected static String[] getExtractedFamilyNames(final String familyNames,
            final boolean validate) {
        final String[] parts = StringUtil.splitByComma(familyNames);

        int count = 0;

        for (final String each : parts) {
            final String trimmed = StringUtil.strip(each);

            final char firstChar;
            final char lastChar;

            if (trimmed.length() > 0) {
                firstChar = trimmed.charAt(0);
                lastChar = trimmed.charAt(trimmed.length() - 1);
            } else {
                firstChar = 0;
                lastChar = 0;
            }

            final boolean startsWith = firstChar == '"' || firstChar == '\'';
            int begin = 0;
            if (startsWith) {
                begin = 1;
            }

            final boolean endsWith = lastChar == '"' || lastChar == '\'';
            int end = trimmed.length();
            if (endsWith) {
                end = end - 1;
            }

            final String familyName = trimmed.substring(begin, end);

            if (validate && !FONT_FAMILY_NAMES
                    .contains(TagStringUtil.toLowerCase(familyName))) {
                throw new InvalidValueException("font-family name " + familyName
                        + " is not valid against the family names added by addFontFamilyName and addFontFamilyNames methods");
            }

            parts[count] = familyName;

            count++;
        }
        return parts;
    }

    /**
     * <pre>
     * Eg:-
     * String[] fontFamilyNames = {"Times New Roman", "Georgia", GenericFontFamilyNameContants.SERIF};
     *
     * </pre>
     *
     * @param fontFamilyNames
     *            the fontFamilyNames to set
     * @author WFF
     * @since 1.0.0
     */
    public void setFontFamilyNames(final String[] fontFamilyNames) {
        if (fontFamilyNames != null) {
            this.fontFamilyNames = StringUtil.cloneArray(fontFamilyNames);
            cssValue = getBuiltCssValue(this.fontFamilyNames);
        } else {
            this.fontFamilyNames = null;
            cssValue = INITIAL;
        }
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
    }

    /**
     * gets the font family names as an array and if here is no names set then
     * returns an empty array.
     *
     * <pre>
     * Note: it will never return null.
     * </pre>
     *
     * @return the fontFamilyNames. If there is no family name set then returns
     *         an empty array instead of null.
     * @author WFF
     * @since 1.0.0
     */
    public String[] getFontFamilyNames() {
        if (fontFamilyNames == null) {
            return EMPTY_ARRAY;
        }
        return StringUtil.cloneArray(fontFamilyNames);
    }

    /**
     * sets as {@code initial}
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInitial() {
        setCssValue(INITIAL);
    }

    /**
     * sets as {@code inherit}
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInherit() {
        setCssValue(INHERIT);
    }

    /**
     *
     * @param fontFamilyName
     *            the supported font-family.
     * @author WFF
     * @since 1.0.0
     */
    public static void addFontFamilyName(final String fontFamilyName) {
        FONT_FAMILY_NAMES.add(fontFamilyName);
    }

    /**
     * @param fontFamilyNames
     *            the supported font-family names.
     * @author WFF
     * @since 1.0.0
     */
    public static void addFontFamilyNames(
            final Collection<String> fontFamilyNames) {
        FONT_FAMILY_NAMES.addAll(fontFamilyNames);
    }

    /**
     *
     * @author WFF
     * @return the set of font-family names added by
     *         {@code FontFamily#addFontFamilyName(String)} and
     *         {@code FontFamily#addFontFamilyNames(Collection)} methods.
     * @since 1.0.0
     */
    public Set<String> getAddedFontFamilyNames() {
        return FONT_FAMILY_NAMES;
    }

    /**
     * @return the validateFontFamilyName
     * @author WFF
     * @since 1.0.0
     */
    public boolean isValidateFontFamilyName() {
        return validateFontFamilyName;
    }

    /**
     * @param validateFontFamilyName
     *            the validateFontFamilyName to set
     * @author WFF
     * @since 1.0.0
     */
    public void setValidateFontFamilyName(
            final boolean validateFontFamilyName) {
        this.validateFontFamilyName = validateFontFamilyName;
    }

    /**
     * @return the validateFontFamilyNameGlobally
     * @author WFF
     * @since 1.0.0
     */
    public static boolean isValidateFontFamilyNameGlobally() {
        return validateFontFamilyNameGlobally;
    }

    /**
     * @param validateFontFamilyNameGlobally
     *            the validateFontFamilyNameGlobally to set
     * @author WFF
     * @since 1.0.0
     */
    public static void setValidateFontFamilyNameGlobally(
            final boolean validateFontFamilyNameGlobally) {
        FontFamily.validateFontFamilyNameGlobally = validateFontFamilyNameGlobally;
    }

    /**
     * @return true if the given cssValue is validate. It also checks whether
     *         the font-family names added by
     *         {@code FontFamily#addFontFamilyName(String)} and
     *         {@code FontFamily#addFontFamilyNames(Collection)} methods contain
     *         the given cssValue.
     * @author WFF
     * @since 1.0.0
     */
    public static boolean isValid(final String cssValue) {
        final String cssValueLC = TagStringUtil
                .toLowerCase(StringUtil.strip(cssValue));
        return INITIAL.equals(cssValueLC) || INHERIT.equals(cssValueLC)
                || FONT_FAMILY_NAMES.contains(cssValueLC);
    }
}
