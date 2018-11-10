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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.clone.CloneUtil;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.data.Bean;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * cursor : alias | all-scroll | auto | cell | context-menu | col-resize | copy
 * | crosshair | default | e-resize | ew-resize | grab | grabbing | help | move
 * | n-resize | ne-resize | nesw-resize | ns-resize | nw-resize | nwse-resize |
 * no-drop | none | not-allowed | pointer | progress | row-resize | s-resize |
 * se-resize | sw-resize | text | URL | vertical-text | w-resize | wait |
 * zoom-in | zoom-out | initial | inherit;
 *
 * @author WFF
 * @since 1.0.0
 */
public class Cursor extends AbstractCssProperty<Cursor>
        implements StateChangeInformer<Bean> {

    private static final long serialVersionUID = 1_0_0L;

    public static final String ALIAS = "alias";
    public static final String ALL_SCROLL = "all-scroll";
    public static final String AUTO = "auto";
    public static final String CELL = "cell";
    public static final String CONTEXT_MENU = "context-menu";
    public static final String COL_RESIZE = "col-resize";
    public static final String COPY = "copy";
    public static final String CROSSHAIR = "crosshair";
    public static final String DEFAULT = "default";
    public static final String E_RESIZE = "e-resize";
    public static final String EW_RESIZE = "ew-resize";
    public static final String GRAB = "grab";
    public static final String GRABBING = "grabbing";
    public static final String HELP = "help";
    public static final String MOVE = "move";
    public static final String N_RESIZE = "n-resize";
    public static final String NE_RESIZE = "ne-resize";
    public static final String NESW_RESIZE = "nesw-resize";
    public static final String NS_RESIZE = "ns-resize";
    public static final String NW_RESIZE = "nw-resize";
    public static final String NWSE_RESIZE = "nwse-resize";
    public static final String NO_DROP = "no-drop";
    public static final String NONE = "none";
    public static final String NOT_ALLOWED = "not-allowed";
    public static final String POINTER = "pointer";
    public static final String PROGRESS = "progress";
    public static final String ROW_RESIZE = "row-resize";
    public static final String S_RESIZE = "s-resize";
    public static final String SE_RESIZE = "se-resize";
    public static final String SW_RESIZE = "sw-resize";
    public static final String TEXT = "text";
    public static final String VERTICAL_TEXT = "vertical-text";
    public static final String W_RESIZE = "w-resize";
    public static final String WAIT = "wait";
    public static final String ZOOM_IN = "zoom-in";
    public static final String ZOOM_OUT = "zoom-out";
    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";

    private static final List<String> ALL_CURSORTYPES;

    public static final Logger LOGGER = Logger
            .getLogger(Cursor.class.getName());

    static {
        ALL_CURSORTYPES = new ArrayList<>();
        ALL_CURSORTYPES.add(ALL_SCROLL);
        ALL_CURSORTYPES.add(AUTO);
        ALL_CURSORTYPES.add(CELL);
        ALL_CURSORTYPES.add(CONTEXT_MENU);
        ALL_CURSORTYPES.add(COL_RESIZE);
        ALL_CURSORTYPES.add(COPY);
        ALL_CURSORTYPES.add(CROSSHAIR);
        ALL_CURSORTYPES.add(DEFAULT);
        ALL_CURSORTYPES.add(E_RESIZE);
        ALL_CURSORTYPES.add(EW_RESIZE);
        ALL_CURSORTYPES.add(GRAB);
        ALL_CURSORTYPES.add(GRABBING);
        ALL_CURSORTYPES.add(HELP);
        ALL_CURSORTYPES.add(MOVE);
        ALL_CURSORTYPES.add(N_RESIZE);
        ALL_CURSORTYPES.add(NE_RESIZE);
        ALL_CURSORTYPES.add(NESW_RESIZE);
        ALL_CURSORTYPES.add(NS_RESIZE);
        ALL_CURSORTYPES.add(NW_RESIZE);
        ALL_CURSORTYPES.add(NWSE_RESIZE);
        ALL_CURSORTYPES.add(NO_DROP);
        ALL_CURSORTYPES.add(NONE);
        ALL_CURSORTYPES.add(NOT_ALLOWED);
        ALL_CURSORTYPES.add(POINTER);
        ALL_CURSORTYPES.add(PROGRESS);
        ALL_CURSORTYPES.add(ROW_RESIZE);
        ALL_CURSORTYPES.add(S_RESIZE);
        ALL_CURSORTYPES.add(SE_RESIZE);
        ALL_CURSORTYPES.add(SW_RESIZE);
        ALL_CURSORTYPES.add(TEXT);
        ALL_CURSORTYPES.add(VERTICAL_TEXT);
        ALL_CURSORTYPES.add(W_RESIZE);
        ALL_CURSORTYPES.add(WAIT);
        ALL_CURSORTYPES.add(ZOOM_IN);
        ALL_CURSORTYPES.add(ZOOM_OUT);
        ALL_CURSORTYPES.add(INITIAL);
        ALL_CURSORTYPES.add(INHERIT);
    }

    private String cssValue;

    private String[] cursorUrls;

    private String cursorType;

    private UrlCss3Value[] urlCss3Values;

    public Cursor() {
        setCssValue(DEFAULT);
    }

    /**
     * @param cssValue
     *            the cssValue to set. eg:- {@code Cursor.ALIAS}
     * @author WFF
     */
    public Cursor(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param cursor
     *            the {@code Cursor} object from which the cssName and cssValue
     *            to set.
     * @author WFF
     */
    public Cursor(final Cursor cursor) {
        if (cursor == null) {
            throw new NullValueException("customCss can not be null");
        }
        if (cursor.getCssName() == null) {
            throw new NullValueException(
                    "customCss.getCssName() can not be null");
        }
        if (StringUtil.endsWithColon(cursor.getCssName())) {
            throw new InvalidValueException(
                    "customCss.getCssName() can not end with : (colon)");
        }
        setCssValue(cursor.getCssValue());
    }

    /**
     * This constructor is for CSS 2.1 cursor syntax. <br>
     * sample code :- {@code new Cursor("auto", "Test.gif", "TestImage.png")}
     * creates <code>cursor: url("Test.gif"), url("TestImage.png"), auto;</code>
     * . For css3 syntax method please use
     * {@code new Cursor(String cursorType, UrlCss3Value... urlCss3Values)} or
     * {@code setCursorUrls(String cursorType, UrlCss3Value... urlCss3Values)}
     * method.
     *
     * @param cursorType
     *            This will be the last value, eg:- <code>auto</code> in
     *            <code>cursor: url(Test.gif), url(TestImage.png), auto;</code>.
     *            This value can not be null. And, it will throw
     *            {@code NullValueException} for null value.
     * @param cursorUrls
     *            an array of cursor urls, eg:-
     *            {@code cursor.setCursorUrls("auto", "Test.gif", "TestImage.png")}
     *            and the generated css will be
     *            <code>cursor: url("Test.gif"), url("TestImage.png"), auto;</code>
     * @since 1.0.0
     * @author WFF
     */
    public Cursor(final String cursorType, final String... cursorUrls) {
        setCursorUrls(cursorType, cursorUrls);
    }

    /**
     * @param cursorType
     *            the any of the inbuilt cursor types. It will come as the last
     *            one in the css value.
     * @param urlCss3Values
     *            an array of {@code UrlCss3Value} objects.
     * @author WFF
     */
    public Cursor(final String cursorType,
            final UrlCss3Value... urlCss3Values) {
        setCursorUrls(cursorType, urlCss3Values);
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
        return CssNameConstants.CURSOR;
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
     *            {@code null} is considered as an invalid value.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public Cursor setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException("the value can not be null");
        }
        final String trimmedCssValue = StringUtil.strip(cssValue);
        final int cssValueLength = trimmedCssValue.length();
        if (cssValueLength > 0 && (trimmedCssValue.charAt(0) == ':'
                || trimmedCssValue.charAt(cssValueLength - 1) == ';')) {
            throw new InvalidValueException(
                    "cssValue can not start with : (colon) or end with ; (semicolon)");
        }

        final String[] cssValueParts = StringUtil.splitByComma(trimmedCssValue);
        final String cursorType = StringUtil
                .strip(cssValueParts[cssValueParts.length - 1]).toLowerCase();
        if (!ALL_CURSORTYPES.contains(StringUtil.strip(cursorType))) {
            throw new InvalidValueException(
                    "The last cursor should be the inbuild cursor like auto, default etc..");
        }
        if (cssValueParts.length > 1) {
            final StringBuilder cssValueBuilder = new StringBuilder();
            if (urlCss3Values == null) {
                urlCss3Values = new UrlCss3Value[cssValueParts.length - 1];
                for (int i = 0; i < cssValueParts.length - 1; i++) {
                    final UrlCss3Value urlCss3Value = new UrlCss3Value(
                            cssValueParts[i]);
                    urlCss3Value.setAlreadyInUse(true);
                    urlCss3Value.setStateChangeInformer(this);
                    urlCss3Values[i] = urlCss3Value;

                    cssValueBuilder.append(urlCss3Value.getValue())
                            .append(", ");
                }
            } else {

                UrlCss3Value[] urlCss3ValuesTemp = null;
                if ((cssValueParts.length - 1) == urlCss3Values.length) {
                    urlCss3ValuesTemp = urlCss3Values;
                } else {
                    urlCss3ValuesTemp = new UrlCss3Value[cssValueParts.length
                            - 1];
                }
                for (int i = 0; i < cssValueParts.length - 1; i++) {
                    final UrlCss3Value urlCss3Value;
                    if (i < urlCss3Values.length) {
                        urlCss3Value = urlCss3Values[i];
                        urlCss3Value.setUrlCssValue(cssValueParts[i]);
                    } else {
                        urlCss3Value = new UrlCss3Value(cssValueParts[i]);
                    }

                    urlCss3Value.setAlreadyInUse(true);
                    urlCss3Value.setStateChangeInformer(this);
                    urlCss3ValuesTemp[i] = urlCss3Value;
                    cssValueBuilder.append(urlCss3Value.getValue())
                            .append(", ");
                }

                for (int i = cssValueParts.length
                        - 1; i < urlCss3Values.length; i++) {
                    urlCss3Values[i].setAlreadyInUse(false);
                }

                for (final UrlCss3Value urlCss3Value : urlCss3ValuesTemp) {
                    urlCss3Value.setStateChangeInformer(this);
                    urlCss3Value.setAlreadyInUse(true);
                }
                urlCss3Values = urlCss3ValuesTemp;
            }
            cssValueBuilder.append(cursorType);
            this.cssValue = cssValueBuilder.toString();
            this.cursorType = cursorType;
        } else {
            this.cssValue = trimmedCssValue;
            this.cursorType = cursorType;
        }
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    /**
     * @param cursorType
     *            it should be one of the following <code> Cursor.ALL_SCROLL ,
     *            Cursor.AUTO , Cursor.CELL , Cursor.CONTEXT_MENU ,
     *            Cursor.COL_RESIZE , Cursor.COPY , Cursor.CROSSHAIR ,
     *            Cursor.DEFAULT , Cursor.E_RESIZE , Cursor.EW_RESIZE ,
     *            Cursor.GRAB , Cursor.GRABBING , Cursor.HELP , Cursor.MOVE ,
     *            Cursor.N_RESIZE , Cursor.NE_RESIZE , Cursor.NESW_RESIZE ,
     *            Cursor.NS_RESIZE , Cursor.NW_RESIZE , Cursor.NWSE_RESIZE ,
     *            Cursor.NO_DROP , Cursor.NONE , Cursor.NOT_ALLOWED ,
     *            Cursor.POINTER , Cursor.PROGRESS , Cursor.ROW_RESIZE ,
     *            Cursor.S_RESIZE , Cursor.SE_RESIZE , Cursor.SW_RESIZE ,
     *            Cursor.TEXT , Cursor.URL , Cursor.VERTICAL_TEXT ,
     *            Cursor.W_RESIZE , Cursor.WAIT , Cursor.ZOOM_IN ,
     *            Cursor.ZOOM_OUT , Cursor.INITIAL or Cursor.INHERIT</code>.
     * @since 1.0.0
     * @author WFF
     */
    public void setCursorType(final String cursorType) {
        this.cursorType = cursorType;
        final String cursor = StringUtil.strip(cursorType).toLowerCase();
        if (ALL_CURSORTYPES.contains(cursor)) {
            setCssValue(cursor);
        } else {
            throw new InvalidValueException(cursorType
                    + " is invalid. use Cursor.DEFAULT or Cursor.WAIT etc..");
        }
    }

    /**
     * @return
     * @since 1.0.0
     * @author WFF
     */
    public String getCursorType() {
        return cursorType;
    }

    /**
     * @return the cursor urls as an array set by
     *         {@code Cursor#setCursorUrls(String, String...)} method. The
     *         returned array is just a copy of the cursor urls, modifying it
     *         will not affect the {@code Cursor} object.
     * @since 1.0.0
     * @author WFF
     */
    public String[] getCursorUrls() {
        return cursorUrls;
    }

    /**
     * This method is for CSS 2.1 cursor syntax. <br>
     * sample code :-
     * {@code cursor.setCursorUrls("auto", "Test.gif", "TestImage.png")} creates
     * <code>cursor: url("Test.gif"), url("TestImage.png"), auto;</code>. For
     * css3 syntax method please use
     * {@code setCursorUrls(String cursorType, UrlCss3Value... urlCss3Values)}
     * method.
     *
     * @param cursorType
     *            This will be the last value, eg:- <code>auto</code> in
     *            <code>cursor: url(Test.gif), url(TestImage.png), auto;</code>.
     *            This value can not be null. And, it will throw
     *            {@code NullValueException} for null value.
     * @param cursorUrls
     *            an array of cursor urls, eg:-
     *            {@code cursor.setCursorUrls("auto", "Test.gif", "TestImage.png")}
     *            and the generated css will be
     *            <code>cursor: url("Test.gif"), url("TestImage.png"), auto;</code>
     * @since 1.0.0
     * @author WFF
     */
    public void setCursorUrls(final String cursorType,
            final String... cursorUrls) {
        if (cursorType == null) {
            throw new NullValueException("cursorType is null");
        }
        if (cursorUrls == null) {
            throw new NullValueException("cursorUrls is null");
        }
        if (!ALL_CURSORTYPES
                .contains(StringUtil.strip(cursorType).toLowerCase())) {
            throw new InvalidValueException(cursorType
                    + " is invalid. The cursorType should be any predefined cursor type, eg default, auto etc..");
        }
        final StringBuilder sb = new StringBuilder();
        for (final String cursorUrl : cursorUrls) {
            final String urlString = "url(\"".concat(cursorUrl).concat("\"), ");
            sb.append(urlString);
        }
        String cssValue = sb.toString();

        cssValue = cssValue.endsWith(", ") ? cssValue.concat(cursorType)
                : cursorType;

        setCssValue(cssValue);
        this.cursorType = cursorType;
        this.cursorUrls = cursorUrls;
    }

    /**
     * @param cursorType
     *            * the any of the inbuilt cursor types. It will come as the
     *            last one in the css value.
     * @param urlCss3Values
     *            urlCss3Values an array of {@code UrlCss3Value} objects.
     * @since 1.0.0
     * @author WFF
     */
    public void setCursorUrls(final String cursorType,
            final UrlCss3Value... urlCss3Values) {
        if (cursorType == null) {
            throw new NullValueException("cursorType is null");
        }
        if (urlCss3Values == null) {
            throw new NullValueException("urlCss3Values is null");
        }
        if (!ALL_CURSORTYPES
                .contains(StringUtil.strip(cursorType).toLowerCase())) {
            throw new InvalidValueException(cursorType
                    + " is invalid. The cursorType should be any predefined cursor type, eg default, auto etc..");
        }
        if (this.urlCss3Values != null) {
            for (final UrlCss3Value urlCss3Value : this.urlCss3Values) {
                urlCss3Value.setAlreadyInUse(false);
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        for (UrlCss3Value urlCss3Value : urlCss3Values) {
            if (urlCss3Value.isAlreadyInUse()) {
                try {
                    final UrlCss3Value clonedUrlCss3Value = CloneUtil
                            .<UrlCss3Value> deepCloneOnlyIfDoesNotContain(
                                    urlCss3Value, this.urlCss3Values);

                    if (!Objects.equals(clonedUrlCss3Value, urlCss3Value)) {
                        urlCss3Value = clonedUrlCss3Value;
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.warning("cloned urlCss3Value " + urlCss3Value
                                    + "(hashcode: " + urlCss3Value.hashCode()
                                    + ") as it is already used by another object");
                        }
                    }
                } catch (final CloneNotSupportedException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            }
            cssValueBuilder.append(urlCss3Value.getValue()).append(", ");
            urlCss3Value.setAlreadyInUse(true);
            urlCss3Value.setStateChangeInformer(this);
        }
        cssValueBuilder.append(cursorType);

        if (this.urlCss3Values != null) {
            for (final UrlCss3Value urlCss3Value : this.urlCss3Values) {
                urlCss3Value.setAlreadyInUse(false);
            }
        }

        for (final UrlCss3Value urlCss3Value : urlCss3Values) {
            urlCss3Value.setAlreadyInUse(true);
            urlCss3Value.setStateChangeInformer(this);
        }

        this.cursorType = cursorType;
        this.urlCss3Values = urlCss3Values;
        cssValue = cssValueBuilder.toString();
    }

    /**
     * @return a new object of unmodifiable {@code List<UrlCss3Value>} whenever
     *         this method is called. Or null.
     *
     * @author WFF
     * @since 1.0.0
     */
    public List<UrlCss3Value> getUrlCss3Values() {
        // for security improvements for urlCss3Values without
        // compromising performance.
        // TODO the drawback of this the returning of new object each time even
        // if the urlCss3Values array has not been modified, as a minor
        // performance
        // improvement, return a new object only if the urlCss3Values is
        // modified.
        if (urlCss3Values == null) {
            return null;
        }
        return Collections.unmodifiableList(Arrays.asList(urlCss3Values));
    }

    /**
     * @return all inbuilt cursorTypes eg:- auto, default, grab etc..
     * @since 1.0.0
     * @author WFF
     */
    public static String[] getAllCursorTypes() {
        return ALL_CURSORTYPES.toArray(new String[ALL_CURSORTYPES.size()]);
    }

    @Override
    public void stateChanged(final Bean stateChangedObject) {
        if (cursorType != null && urlCss3Values != null) {
            setCursorUrls(cursorType, urlCss3Values);
            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        }
    }

    /**
     * To introduce additional cursor types, add those to this list.
     *
     * @return the allCursortypes
     * @author WFF
     * @since 1.0.0
     */
    protected static List<String> getAllCursortypes() {
        return ALL_CURSORTYPES;
    }
}
