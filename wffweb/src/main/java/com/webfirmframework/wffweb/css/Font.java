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

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.StringBuilderUtil;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * <pre>
 * font: font-style font-variant font-weight font-size/line-height font-family|caption|icon|menu|message-box|small-caption|status-bar|initial|inherit;
 *
 * The font shorthand property sets all the font properties in one declaration.
 *
 * The properties that can be set, are (in order): "font-style font-variant font-weight font-size/line-height font-family"
 *
 * The font-size and font-family values are required. If one of the other values are missing, the default values will be inserted, if any.
 *
 * Note: The line-height property sets the space between lines.
 * Default value:  The default value of all the font properties
 * Inherited:      yes
 * Animatable:     yes, see individual properties. Read about animatable
 * Version:        CSS1
 * JavaScript syntax:      object.style.font="italic small-caps bold 12px/1.4 arial,sans-serif"
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class Font extends AbstractCssProperty<Font>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger.getLogger(Font.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String CAPTION = "caption";
    public static final String ICON = "icon";
    public static final String MENU = "menu";
    public static final String MESSAGE_BOX = "message-box";
    public static final String SMALL_CAPTION = "small-caption";
    public static final String STATUS_BAR = "status-bar";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays.asList(
            INITIAL, INHERIT, CAPTION, ICON, MENU, MESSAGE_BOX, SMALL_CAPTION,
            STATUS_BAR);

    private String cssValue;

    private FontStyle fontStyle;

    private FontVariant fontVariant;

    private FontWeight fontWeight;

    private FontSize fontSize;

    private LineHeight lineHeight;

    private FontFamily fontFamily;

    /**
     * The value <code>initial</code> will be assigned as the cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public Font() {
        setCssValue("initial");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public Font(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param font
     *            the {@code Font} object from which the cssValue to set.And,
     *            {@code null} will throw {@code NullValueException}
     */
    public Font(final Font font) {
        if (font == null) {
            throw new NullValueException("font can not be null");
        }
        setCssValue(font.getCssValue());
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
        return CssNameConstants.FONT;
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
     *            the value should be in the format of
     *            <code>italic small-caps bold 12px arial,sans-serif</code> or
     *            <code>initial/inherit</code>. {@code null} is considered as an
     *            invalid value and it will throw {@code NullValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public Font setCssValue(final String cssValue) {
        // NB: cssValue can contain upper case so it should not be converted to
        // lower case.
        final String trimmedCssValue;
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example italic small-caps bold 12px arial,sans-serif Or initial/inherit.");
        } else if ((trimmedCssValue = StringUtil.strip(cssValue)).isEmpty()) {
            throw new NullValueException(cssValue
                    + " is an invalid value. The value format should be as for example italic small-caps bold 12px arial,sans-serif Or initial/inherit.");
        }

        final String[] cssValueParts = getExtractedSubCssValues(
                trimmedCssValue);

        // @formatter:off
        /*
        if (cssValueParts.length > 1) {
            may not be fine, if the predefined constant comes as a substring of font-family name in future.
             for (String constant : PREDEFINED_CONSTANTS) {
                if (trimmedCssValue.contains(constant)) {
                    throw new InvalidValueException(
                            "The string '"
                                    + constant
                                    + "' makes the given cssValue invalid, please remove '"
                                    + constant + "' from it and try.");
                }
            }

        } else
        */
        // @formatter:on

        if (cssValueParts.length == 1
                && PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {

            fontStyle = null;
            fontVariant = null;
            fontWeight = null;
            if (fontSize != null) {
                fontSize.setAlreadyInUse(false);
                fontSize = null;
            }
            if (lineHeight != null) {
                lineHeight.setAlreadyInUse(false);
                lineHeight = null;
            }
            if (fontFamily != null) {
                fontFamily.setAlreadyInUse(false);
                fontFamily = null;
            }

            this.cssValue = trimmedCssValue;
            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
            return this;
        }

        FontStyle fontStyle = null;
        FontVariant fontVariant = null;
        FontWeight fontWeight = null;
        FontSize fontSize = null;
        LineHeight lineHeight = null;
        FontFamily fontFamily = null;

        for (final String eachPart : cssValueParts) {
            if (fontStyle == null && FontStyle.isValid(eachPart)) {
                fontStyle = FontStyle.getThis(eachPart);
            } else if (fontVariant == null && FontVariant.isValid(eachPart)) {
                fontVariant = FontVariant.getThis(eachPart);
            } else if (fontWeight == null && FontWeight.isValid(eachPart)) {
                fontWeight = FontWeight.getThis(eachPart);
            } else if (fontSize == null && FontSize.isValid(eachPart)) {
                if (this.fontSize == null) {
                    fontSize = new FontSize(eachPart);
                    fontSize.setStateChangeInformer(this);
                    fontSize.setAlreadyInUse(true);
                } else {
                    this.fontSize.setCssValue(eachPart);
                    fontSize = this.fontSize;
                }
            } else if (lineHeight == null && LineHeight.isValid(eachPart)) {
                if (this.lineHeight == null) {
                    lineHeight = new LineHeight(eachPart);
                    lineHeight.setStateChangeInformer(this);
                    lineHeight.setAlreadyInUse(true);
                } else {
                    this.lineHeight.setCssValue(eachPart);
                    lineHeight = this.lineHeight;
                }
            } else if (fontFamily == null
                    && (!FontFamily.isValidateFontFamilyNameGlobally()
                            || FontFamily.isValid(eachPart))) {
                if (this.fontFamily == null) {
                    fontFamily = new FontFamily(eachPart);
                    fontFamily.setStateChangeInformer(this);
                    fontFamily.setAlreadyInUse(true);
                } else {
                    this.fontFamily.setCssValue(eachPart);
                    fontFamily = this.fontFamily;
                }
            } else {
                throw new InvalidValueException(cssValue
                        + " is an invalid value. The value format should be as for example italic small-caps bold 12px arial,sans-serif Or initial/inherit.");
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (fontStyle != null) {
            cssValueBuilder.append(fontStyle.getCssValue()).append(' ');
            invalid = false;
        }

        if (fontVariant != null) {
            cssValueBuilder.append(fontVariant.getCssValue()).append(' ');
            invalid = false;
        }

        if (fontWeight != null) {
            cssValueBuilder.append(fontWeight.getCssValue()).append(' ');
            invalid = false;
        }

        if (fontSize != null) {
            cssValueBuilder.append(fontSize.getCssValue());
            if (lineHeight == null) {
                cssValueBuilder.append(' ');
            }
            invalid = false;
        }

        if (lineHeight != null) {
            if (fontSize != null) {
                cssValueBuilder.append('/');
            }
            cssValueBuilder.append(lineHeight.getCssValue()).append(' ');
            invalid = false;
        }

        if (fontFamily != null) {
            cssValueBuilder.append(fontFamily.getCssValue()).append(' ');
            invalid = false;
        }
        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example italic small-caps bold 12px arial,sans-serif Or initial/inherit.");
        }
        this.cssValue = StringBuilderUtil.getTrimmedString(cssValueBuilder);
        this.fontStyle = fontStyle;
        this.fontVariant = fontVariant;
        this.fontWeight = fontWeight;
        this.fontSize = fontSize;
        this.lineHeight = lineHeight;
        this.fontFamily = fontFamily;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
    }

    /**
     * @param cssValue
     * @return the sub properties
     * @author WFF
     * @since 1.0.0
     */
    protected static String[] getExtractedSubCssValues(final String cssValue) {
        final String convertedToSingleSpace = StringUtil
                .convertToSingleSpace(cssValue);
        final String[] subCssValues = convertedToSingleSpace.replace(", ", ",")
                .split("[ /]");
        return subCssValues;
    }

    /**
     * checks the given css value is valid for this class. It does't do a strict
     * validation.
     *
     * @param cssValue
     * @return
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String cssValue) {
        // TODO verify this method. the fontFamily cannot be validated so this
        // method may be invalid. remove after verified

        if (cssValue == null || StringUtil.isBlank(cssValue)) {
            return false;
        }

        final String[] cssValueParts = getExtractedSubCssValues(cssValue);

        FontStyle fontStyle = null;
        FontVariant fontVariant = null;
        FontWeight fontWeight = null;
        FontSize fontSize = null;
        LineHeight lineHeight = null;
        FontFamily fontFamily = null;

        for (final String eachPart : cssValueParts) {
            boolean invalid = true;
            if (fontStyle == null && FontStyle.isValid(eachPart)) {
                fontStyle = FontStyle.getThis(eachPart);
                invalid = false;
            } else if (fontVariant == null && FontVariant.isValid(eachPart)) {
                fontVariant = FontVariant.getThis(eachPart);
                invalid = false;
            } else if (fontWeight == null && FontWeight.isValid(eachPart)) {
                fontWeight = FontWeight.getThis(eachPart);
                invalid = false;
            } else if (fontSize == null && FontSize.isValid(eachPart)) {
                fontSize = new FontSize(eachPart);
                invalid = false;
            } else if (lineHeight == null && LineHeight.isValid(eachPart)) {
                lineHeight = new LineHeight(eachPart);
                invalid = false;
            } else if (fontFamily == null
                    && (!FontFamily.isValidateFontFamilyNameGlobally()
                            || FontFamily.isValid(eachPart))) {
                fontFamily = new FontFamily(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return fontStyle != null || fontVariant != null || fontWeight != null
                || fontSize != null || lineHeight != null || fontFamily != null;
    }

    /**
     * sets as initial
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInitial() {
        setCssValue(INITIAL);
    }

    /**
     * sets as inherit
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsInherit() {
        setCssValue(INHERIT);
    }

    /**
     * sets as caption
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsCaption() {
        setCssValue(CAPTION);
    }

    /**
     * sets as icon
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsIcon() {
        setCssValue(ICON);
    }

    /**
     * sets as menu
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsMenu() {
        setCssValue(MENU);
    }

    /**
     * sets as message-box
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsMessageBox() {
        setCssValue(MESSAGE_BOX);
    }

    /**
     * sets as small-caption
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsSmallCaption() {
        setCssValue(SMALL_CAPTION);
    }

    /**
     * sets as status-bar
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsStatusBar() {
        setCssValue(STATUS_BAR);
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    /**
     * @return the lineHeight
     * @author WFF
     * @since 1.0.0
     */
    public LineHeight getLineHeight() {
        return lineHeight;
    }

    /**
     * @return the fontFamily
     * @author WFF
     * @since 1.0.0
     */
    public FontFamily getFontFamily() {
        return fontFamily;
    }

    public FontVariant getFontVariant() {
        return fontVariant;
    }

    /**
     * @return the fontWeight
     * @author WFF
     * @since 1.0.0
     */
    public FontWeight getFontWeight() {
        return fontWeight;
    }

    public FontStyle getFontStyle() {
        return fontStyle;
    }

    public Font setFontStyle(final FontStyle fontStyle) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (fontStyle != null) {
            cssValueBuilder.append(fontStyle.getCssValue()).append(' ');
        }
        if (fontVariant != null) {
            cssValueBuilder.append(fontVariant.getCssValue()).append(' ');
        }

        if (fontWeight != null) {
            cssValueBuilder.append(fontWeight.getCssValue()).append(' ');
        }

        if (fontSize != null) {
            cssValueBuilder.append(fontSize.getCssValue());
            if (lineHeight == null) {
                cssValueBuilder.append(' ');
            }
        }

        if (lineHeight != null) {
            if (fontSize != null) {
                cssValueBuilder.append('/');
            }
            cssValueBuilder.append(lineHeight.getCssValue()).append(' ');
        }

        if (fontFamily != null) {
            cssValueBuilder.append(fontFamily.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.fontStyle = fontStyle;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public Font setFontVariant(final FontVariant fontVariant) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (fontStyle != null) {
            cssValueBuilder.append(fontStyle.getCssValue()).append(' ');
        }

        if (fontVariant != null) {
            cssValueBuilder.append(fontVariant.getCssValue()).append(' ');
        }

        if (fontWeight != null) {
            cssValueBuilder.append(fontWeight.getCssValue()).append(' ');
        }

        if (fontSize != null) {
            cssValueBuilder.append(fontSize.getCssValue());
            if (lineHeight == null) {
                cssValueBuilder.append(' ');
            }
        }

        if (lineHeight != null) {
            if (fontSize != null) {
                cssValueBuilder.append('/');
            }
            cssValueBuilder.append(lineHeight.getCssValue()).append(' ');
        }

        if (fontFamily != null) {
            cssValueBuilder.append(fontFamily.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.fontVariant = fontVariant;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public Font setFontWeight(final FontWeight fontWeight) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (fontStyle != null) {
            cssValueBuilder.append(fontStyle.getCssValue()).append(' ');
        }

        if (fontVariant != null) {
            cssValueBuilder.append(fontVariant.getCssValue()).append(' ');
        }

        if (fontWeight != null) {
            cssValueBuilder.append(fontWeight.getCssValue()).append(' ');
        }

        if (fontSize != null) {
            cssValueBuilder.append(fontSize.getCssValue());
            if (lineHeight == null) {
                cssValueBuilder.append(' ');
            }
        }

        if (lineHeight != null) {
            if (fontSize != null) {
                cssValueBuilder.append('/');
            }
            cssValueBuilder.append(lineHeight.getCssValue()).append(' ');
        }

        if (fontFamily != null) {
            cssValueBuilder.append(fontFamily.getCssValue()).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        this.fontWeight = fontWeight;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public Font setFontSize(final FontSize fontSize) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (fontStyle != null) {
            cssValueBuilder.append(fontStyle.getCssValue()).append(' ');
        }

        if (fontVariant != null) {
            cssValueBuilder.append(fontVariant.getCssValue()).append(' ');
        }

        if (fontWeight != null) {
            cssValueBuilder.append(fontWeight.getCssValue()).append(' ');
        }

        if (fontSize != null) {
            final String fontSizeCssValue = fontSize.getCssValue();
            if (FontSize.INITIAL.equals(fontSizeCssValue)
                    || FontSize.INHERIT.equals(fontSizeCssValue)) {
                throw new InvalidValueException(
                        "fontSize cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(fontSizeCssValue);
            if (lineHeight == null) {
                cssValueBuilder.append(' ');
            }
        }

        if (lineHeight != null) {
            if (fontSize != null) {
                cssValueBuilder.append('/');
            }
            final String lineHeightCssValue = lineHeight.getCssValue();
            cssValueBuilder.append(lineHeightCssValue).append(' ');
        }

        if (fontFamily != null) {
            final String fontFamilyCssValue = fontFamily.getCssValue();
            cssValueBuilder.append(fontFamilyCssValue).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (fontSize != null && fontSize.isAlreadyInUse()
                && this.fontSize != fontSize) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given fontSize is already used by another object so a new object or the previous object (if it exists) of FontSize will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.fontSize != null) {
            this.fontSize.setAlreadyInUse(false);
        }

        this.fontSize = fontSize;

        if (this.fontSize != null) {
            this.fontSize.setStateChangeInformer(this);
            this.fontSize.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public Font setLineHeight(final LineHeight lineHeight) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (fontStyle != null) {
            cssValueBuilder.append(fontStyle.getCssValue()).append(' ');
        }

        if (fontVariant != null) {
            cssValueBuilder.append(fontVariant.getCssValue()).append(' ');
        }

        if (fontWeight != null) {
            cssValueBuilder.append(fontWeight.getCssValue()).append(' ');
        }

        if (fontSize != null) {
            final String fontSizeCssValue = fontSize.getCssValue();
            cssValueBuilder.append(fontSizeCssValue);
            if (lineHeight == null) {
                cssValueBuilder.append(' ');
            }
        }

        if (lineHeight != null) {
            final String lineHeightCssValue = lineHeight.getCssValue();
            if (FontSize.INITIAL.equals(lineHeightCssValue)
                    || FontSize.INHERIT.equals(lineHeightCssValue)) {
                throw new InvalidValueException(
                        "fontSize cannot have initial/inherit as its cssValue");
            }
            if (fontSize != null) {
                cssValueBuilder.append('/');
            }
            cssValueBuilder.append(lineHeightCssValue).append(' ');
        }

        if (fontFamily != null) {
            final String fontFamilyCssValue = fontFamily.getCssValue();
            cssValueBuilder.append(fontFamilyCssValue).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (lineHeight != null && lineHeight.isAlreadyInUse()
                && this.lineHeight != lineHeight) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given lineHeight is already used by another object so a new object or the previous object (if it exists) of FontSize will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.lineHeight != null) {
            this.lineHeight.setAlreadyInUse(false);
        }

        this.lineHeight = lineHeight;

        if (this.lineHeight != null) {
            this.lineHeight.setStateChangeInformer(this);
            this.lineHeight.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public Font setFontFamily(final FontFamily fontFamily) {

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (fontStyle != null) {
            cssValueBuilder.append(fontStyle.getCssValue()).append(' ');
        }

        if (fontVariant != null) {
            cssValueBuilder.append(fontVariant.getCssValue()).append(' ');
        }

        if (fontWeight != null) {
            cssValueBuilder.append(fontWeight.getCssValue()).append(' ');
        }

        if (fontSize != null) {
            final String fontSizeCssValue = fontSize.getCssValue();
            cssValueBuilder.append(fontSizeCssValue);
            if (lineHeight == null) {
                cssValueBuilder.append(' ');
            }
        }

        if (lineHeight != null) {
            final String lineHeightCssValue = lineHeight.getCssValue();
            if (fontSize != null) {
                cssValueBuilder.append('/');
            }
            cssValueBuilder.append(lineHeightCssValue).append(' ');
        }

        if (fontFamily != null) {
            final String fontFamilyCssValue = fontFamily.getCssValue();
            if (FontFamily.INITIAL.equals(fontFamilyCssValue)
                    || FontFamily.INHERIT.equals(fontFamilyCssValue)) {
                throw new InvalidValueException(
                        "fontFamily cannot have initial/inherit as its cssValue");
            }
            cssValueBuilder.append(fontFamilyCssValue).append(' ');
        }

        final String trimmedCssValue = StringBuilderUtil
                .getTrimmedString(cssValueBuilder).toString();
        cssValue = trimmedCssValue.isEmpty() ? INHERIT : trimmedCssValue;

        if (fontFamily != null && fontFamily.isAlreadyInUse()
                && this.fontFamily != fontFamily) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "the given fontFamily is already used by another object so a new object or the previous object (if it exists) of FontSize will be used");
            }
            return setCssValue(cssValue);
        }

        if (this.fontFamily != null) {
            this.fontFamily.setAlreadyInUse(false);
        }

        this.fontFamily = fontFamily;

        if (this.fontFamily != null) {
            this.fontFamily.setStateChangeInformer(this);
            this.fontFamily.setAlreadyInUse(true);
        }

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof FontSize) {

            final FontSize fontSize = (FontSize) stateChangedObject;

            final String cssValue = fontSize.getCssValue();

            if (FontSize.INITIAL.equals(cssValue)
                    || FontSize.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as fontSize cssValue");
            }

            setFontSize(fontSize);
        } else if (stateChangedObject instanceof LineHeight) {

            final LineHeight lineHeight = (LineHeight) stateChangedObject;

            final String cssValue = lineHeight.getCssValue();

            if (LineHeight.INITIAL.equals(cssValue)
                    || LineHeight.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as lineHeight cssValue");
            }

            setLineHeight(lineHeight);
        } else if (stateChangedObject instanceof FontFamily) {

            final FontFamily fontFamily = (FontFamily) stateChangedObject;

            final String cssValue = fontFamily.getCssValue();

            if (FontFamily.INITIAL.equals(cssValue)
                    || FontFamily.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as fontFamily cssValue");
            }

            setFontFamily(fontFamily);
        }
    }

    /**
     *
     * @author WFF
     * @since 1.0.0
     */
    protected void addPredefinedConstant(final String constant) {
        PREDEFINED_CONSTANTS.add(constant);
    }
}
