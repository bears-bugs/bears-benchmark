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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.CssNameConstants;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.css.core.CssProperty;
import com.webfirmframework.wffweb.informer.StateChangeInformer;
import com.webfirmframework.wffweb.util.StringUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 * <pre>
 *
 * -webkit-flex: <i>-webkit-flex-grow -webkit-flex-shrink -webkit-flex-basis</i>|auto|initial|inherit;
 *
 * The flex property specifies the length of the item, relative to the rest of the flexible items inside the same container.
 *
 * The flex property is a shorthand for the -webkit-flex-grow, -webkit-flex-shrink, and the -webkit-flex-basis properties.
 *
 * Note: If the element is not a flexible item, the flex property has no effect.
 * Default value:  0 1 auto
 * Inherited:      no
 * Animatable:     yes
 * Version:        CSS3
 * JavaScript syntax:      object.style.webkitFlex="1"
 *
 * <i>
 * The undefined value for -webkit-flex-grow, -webkit-flex-shrink and -webkit-flex-basis in -webkit-flex property is 1, 1 and 0% respectively.
 * </i>
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class WebkitFlex extends AbstractCssProperty<WebkitFlex>
        implements StateChangeInformer<CssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(WebkitFlex.class.getName());

    public static final String INITIAL = "initial";
    public static final String INHERIT = "inherit";
    public static final String AUTO = "auto";

    private static final List<String> PREDEFINED_CONSTANTS = Arrays
            .asList(INITIAL, INHERIT, AUTO);

    private String cssValue;

    private WebkitFlexGrow webkitFlexGrow;

    private WebkitFlexShrink webkitFlexShrink;

    private WebkitFlexBasis webkitFlexBasis;

    /**
     * The value <code>0 1 auto</code> will be assigned as the cssValue.
     *
     * @author WFF
     * @since 1.0.0
     */
    public WebkitFlex() {
        setCssValue("0 1 auto");
    }

    /**
     * @param cssValue
     *            the css value to set.
     */
    public WebkitFlex(final String cssValue) {
        setCssValue(cssValue);
    }

    /**
     * @param webkitFlex
     *            the {@code Flex} object from which the cssValue to set.And,
     *            {@code null} will throw {@code NullValueException}
     */
    public WebkitFlex(final WebkitFlex webkitFlex) {
        if (webkitFlex == null) {
            throw new NullValueException("webkitFlex can not be null");
        }
        setCssValue(webkitFlex.getCssValue());
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
        return CssNameConstants.WEBKIT_FLEX;
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
     *            the value should be in the format of <code>1 1 auto</code> or
     *            <code>1 1 15px</code>. {@code null} is considered as an
     *            invalid value and it will throw {@code NullValueException}.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public WebkitFlex setCssValue(final String cssValue) {
        final String trimmedCssValue;
        if (cssValue == null) {
            throw new NullValueException(
                    "null is an invalid value. The value format should be as for example \"0 1 auto\" or initial/inherit.");
        } else if ((trimmedCssValue = TagStringUtil
                .toLowerCase(StringUtil.strip(cssValue))).isEmpty()) {
            throw new NullValueException(cssValue
                    + " is an invalid value. The value format should be as for example \"0 1 auto\" or initial/inherit.");
        }

        final String[] cssValueParts = StringUtil.splitBySpace(trimmedCssValue);

        if (cssValueParts.length > 1) {
            if (trimmedCssValue.contains(WebkitFlexBasis.INITIAL)) {
                throw new InvalidValueException(
                        "The string 'initial' makes the given cssValue invalid, please remove 'initial' from it and try.");
            }
            if (trimmedCssValue.contains(WebkitFlexBasis.INHERIT)) {
                throw new InvalidValueException(
                        "The string 'inherit' makes the given cssValue invalid, please remove 'inherit' from it and try.");
            }
        } else {
            if (PREDEFINED_CONSTANTS.contains(trimmedCssValue)) {
                if (webkitFlexGrow != null) {
                    webkitFlexGrow.setAlreadyInUse(false);
                    webkitFlexGrow = null;
                }
                if (webkitFlexShrink != null) {
                    webkitFlexShrink.setAlreadyInUse(false);
                    webkitFlexShrink = null;
                }
                if (webkitFlexBasis != null) {
                    webkitFlexBasis.setAlreadyInUse(false);
                    webkitFlexBasis = null;
                }

                this.cssValue = trimmedCssValue;
                if (getStateChangeInformer() != null) {
                    getStateChangeInformer().stateChanged(this);
                }
                return this;
            }
        }

        WebkitFlexGrow webkitFlexGrow = null;
        WebkitFlexShrink webkitFlexShrink = null;
        WebkitFlexBasis webkitFlexBasis = null;

        for (final String eachPart : cssValueParts) {
            if (webkitFlexGrow == null && WebkitFlexGrow.isValid(eachPart)) {
                if (this.webkitFlexGrow == null) {
                    webkitFlexGrow = new WebkitFlexGrow(eachPart);
                    webkitFlexGrow.setStateChangeInformer(this);
                    webkitFlexGrow.setAlreadyInUse(true);
                } else {
                    this.webkitFlexGrow.setCssValue(eachPart);
                    webkitFlexGrow = this.webkitFlexGrow;
                }
            } else if (webkitFlexShrink == null
                    && WebkitFlexShrink.isValid(eachPart)) {
                if (this.webkitFlexGrow == null) {
                    webkitFlexShrink = new WebkitFlexShrink(eachPart);
                    webkitFlexShrink.setStateChangeInformer(this);
                    webkitFlexShrink.setAlreadyInUse(true);
                } else {
                    this.webkitFlexShrink.setCssValue(eachPart);
                    webkitFlexShrink = this.webkitFlexShrink;
                }
            } else if (webkitFlexBasis == null
                    && WebkitFlexBasis.isValid(eachPart)) {
                if (this.webkitFlexBasis == null) {
                    webkitFlexBasis = new WebkitFlexBasis(eachPart);
                    webkitFlexBasis.setStateChangeInformer(this);
                    webkitFlexBasis.setAlreadyInUse(true);
                } else {
                    this.webkitFlexBasis.setCssValue(eachPart);
                    webkitFlexBasis = this.webkitFlexBasis;
                }
            }
        }

        final StringBuilder cssValueBuilder = new StringBuilder();
        boolean invalid = true;
        if (webkitFlexGrow == null) {
            if (this.webkitFlexGrow == null) {
                webkitFlexGrow = new WebkitFlexGrow(1);
                webkitFlexGrow.setStateChangeInformer(this);
                webkitFlexGrow.setAlreadyInUse(true);
            } else {
                this.webkitFlexGrow.setValue(0);
                webkitFlexGrow = this.webkitFlexGrow;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(webkitFlexGrow.getCssValue()).append(' ');

        if (webkitFlexShrink == null) {
            if (this.webkitFlexShrink == null) {
                webkitFlexShrink = new WebkitFlexShrink(1);
                webkitFlexShrink.setStateChangeInformer(this);
                webkitFlexShrink.setAlreadyInUse(true);
            } else {
                this.webkitFlexShrink.setValue(1);
                webkitFlexShrink = this.webkitFlexShrink;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(webkitFlexShrink.getCssValue()).append(' ');

        if (webkitFlexBasis == null) {
            if (cssValueParts.length == 3) {
                throw new InvalidValueException(
                        "the given cssValue contains invalid flex-basis value.");
            }
            if (this.webkitFlexBasis == null) {
                webkitFlexBasis = new WebkitFlexBasis(0);
                webkitFlexBasis.setStateChangeInformer(this);
                webkitFlexBasis.setAlreadyInUse(true);
            } else {
                this.webkitFlexBasis.setAsAuto();
                webkitFlexBasis = this.webkitFlexBasis;
            }
        } else {
            invalid = false;
        }

        cssValueBuilder.append(webkitFlexBasis.getCssValue());

        if (invalid) {
            throw new InvalidValueException(cssValue
                    + " is an invalid value. The value format should be as for example \"0 1 auto\" or initial/inherit.");
        }
        this.cssValue = cssValueBuilder.toString();

        webkitFlexGrow.setStateChangeInformer(this);
        webkitFlexGrow.setAlreadyInUse(true);

        webkitFlexShrink.setStateChangeInformer(this);
        webkitFlexShrink.setAlreadyInUse(true);

        webkitFlexBasis.setStateChangeInformer(this);
        webkitFlexBasis.setAlreadyInUse(true);

        this.webkitFlexGrow = webkitFlexGrow;
        this.webkitFlexShrink = webkitFlexShrink;
        this.webkitFlexBasis = webkitFlexBasis;

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }

        return this;
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
        // TODO modify to make a strict validation
        if (cssValue == null || StringUtil.isBlank(cssValue)) {
            return false;
        }

        final String[] cssValueParts = StringUtil.splitBySpace(cssValue);

        WebkitFlexGrow webkitFlexGrow = null;
        WebkitFlexShrink webkitFlexShrink = null;
        WebkitFlexBasis webkitFlexBasis = null;

        for (final String eachPart : cssValueParts) {
            boolean invalid = true;

            if (webkitFlexGrow == null && WebkitFlexGrow.isValid(eachPart)) {
                webkitFlexGrow = new WebkitFlexGrow(eachPart);
                invalid = false;
            } else if (webkitFlexShrink == null
                    && WebkitFlexShrink.isValid(eachPart)) {
                webkitFlexShrink = new WebkitFlexShrink(eachPart);
                invalid = false;
            } else if (webkitFlexBasis == null
                    && WebkitFlexBasis.isValid(eachPart)) {
                webkitFlexBasis = new WebkitFlexBasis(eachPart);
                invalid = false;
            }
            if (invalid) {
                return false;
            }
        }

        return webkitFlexGrow != null || webkitFlexShrink != null
                || webkitFlexBasis != null;
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
     * sets as auto
     *
     * @since 1.0.0
     * @author WFF
     */
    public void setAsAuto() {
        setCssValue(AUTO);
    }

    public WebkitFlexBasis getWebkitFlexBasis() {
        return webkitFlexBasis;
    }

    public WebkitFlexShrink getWebkitFlexShrink() {
        return webkitFlexShrink;
    }

    public WebkitFlexGrow getWebkitFlexGrow() {
        return webkitFlexGrow;
    }

    public WebkitFlex setWebkitFlexGrow(final WebkitFlexGrow webkitFlexGrow) {
        if (webkitFlexGrow == null) {
            throw new InvalidValueException("webkitFlexGrow cannot be null");
        }

        if (Objects.equals(this.webkitFlexGrow, webkitFlexGrow)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (this.webkitFlexGrow != null) {
            this.webkitFlexGrow.setCssValue(webkitFlexGrow.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given webkitFlexGrow to the existing webkitFlexGrow object.");
            }
        } else {
            if (webkitFlexGrow.isAlreadyInUse()) {
                this.webkitFlexGrow = new WebkitFlexGrow(webkitFlexGrow);
                this.webkitFlexGrow.setAlreadyInUse(true);
                this.webkitFlexGrow.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of WebkitFlexGrow as the given webkitFlexGrow object is already used by another object");
                }
            } else {
                this.webkitFlexGrow = webkitFlexGrow;
                this.webkitFlexGrow.setAlreadyInUse(true);
                this.webkitFlexGrow.setStateChangeInformer(this);
            }
        }

        cssValueBuilder.append(webkitFlexGrow.getCssValue()).append(' ');

        if (webkitFlexShrink == null) {
            webkitFlexShrink = new WebkitFlexShrink(1);
            webkitFlexShrink.setStateChangeInformer(this);
            webkitFlexShrink.setAlreadyInUse(true);
        }

        cssValueBuilder.append(webkitFlexShrink.getCssValue()).append(' ');

        if (webkitFlexBasis == null) {
            webkitFlexBasis = new WebkitFlexBasis(0);
            webkitFlexBasis.setStateChangeInformer(this);
            webkitFlexBasis.setAlreadyInUse(true);
        }

        cssValueBuilder.append(webkitFlexBasis.getCssValue());

        cssValue = cssValueBuilder.toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public WebkitFlex setWebkitFlexShrink(
            final WebkitFlexShrink webkitFlexShrink) {
        if (webkitFlexShrink == null) {
            throw new InvalidValueException("webkitFlexShrink cannot be null");
        }

        if (Objects.equals(this.webkitFlexShrink, webkitFlexShrink)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (webkitFlexGrow == null) {
            webkitFlexGrow = new WebkitFlexGrow(1);
            webkitFlexGrow.setStateChangeInformer(this);
            webkitFlexGrow.setAlreadyInUse(true);
        }

        cssValueBuilder.append(webkitFlexGrow.getCssValue()).append(' ');

        if (this.webkitFlexShrink != null) {
            this.webkitFlexShrink.setCssValue(webkitFlexShrink.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given webkitFlexShrink to the existing webkitFlexShrink object.");
            }
        } else {
            if (webkitFlexShrink.isAlreadyInUse()) {
                this.webkitFlexShrink = new WebkitFlexShrink(webkitFlexShrink);
                this.webkitFlexShrink.setAlreadyInUse(true);
                this.webkitFlexShrink.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of WebkitFlexShrink as the given webkitFlexShrink object is already used by another object");
                }
            } else {
                this.webkitFlexShrink = webkitFlexShrink;
                this.webkitFlexShrink.setAlreadyInUse(true);
                this.webkitFlexShrink.setStateChangeInformer(this);
            }
        }

        cssValueBuilder.append(webkitFlexShrink.getCssValue()).append(' ');

        if (webkitFlexBasis == null) {
            webkitFlexBasis = new WebkitFlexBasis(0);
            webkitFlexBasis.setStateChangeInformer(this);
            webkitFlexBasis.setAlreadyInUse(true);
        }

        cssValueBuilder.append(webkitFlexBasis.getCssValue());

        cssValue = cssValueBuilder.toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    public WebkitFlex setWebkitFlexBasis(
            final WebkitFlexBasis webkitFlexBasis) {
        if (webkitFlexBasis == null) {
            throw new InvalidValueException("webkitFlexBasis cannot be null");
        }

        if (Objects.equals(this.webkitFlexBasis, webkitFlexBasis)) {
            return this;
        }

        final StringBuilder cssValueBuilder = new StringBuilder();

        if (webkitFlexGrow == null) {
            webkitFlexGrow = new WebkitFlexGrow(1);
            webkitFlexGrow.setStateChangeInformer(this);
            webkitFlexGrow.setAlreadyInUse(true);
        }

        cssValueBuilder.append(webkitFlexGrow.getCssValue()).append(' ');

        if (webkitFlexShrink == null) {
            webkitFlexShrink = new WebkitFlexShrink(1);
            webkitFlexShrink.setStateChangeInformer(this);
            webkitFlexShrink.setAlreadyInUse(true);
        }

        cssValueBuilder.append(webkitFlexShrink.getCssValue()).append(' ');

        if (this.webkitFlexBasis != null) {
            this.webkitFlexBasis.setCssValue(webkitFlexBasis.getCssValue());
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "copied the cssValue from the given webkitFlexBasis to the existing webkitFlexBasis object.");
            }
        } else {
            if (webkitFlexBasis.isAlreadyInUse()) {
                this.webkitFlexBasis = new WebkitFlexBasis(webkitFlexBasis);
                this.webkitFlexBasis.setAlreadyInUse(true);
                this.webkitFlexBasis.setStateChangeInformer(this);
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "created a new object of WebkitFlexBasis as the given webkitFlexBasis object is already used by another object");
                }
            } else {
                this.webkitFlexBasis = webkitFlexBasis;
                this.webkitFlexBasis.setAlreadyInUse(true);
                this.webkitFlexBasis.setStateChangeInformer(this);
            }
        }

        cssValueBuilder.append(webkitFlexBasis.getCssValue());

        cssValue = cssValueBuilder.toString();

        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

    @Override
    public void stateChanged(final CssProperty stateChangedObject) {
        if (stateChangedObject instanceof WebkitFlexGrow) {

            final WebkitFlexGrow webkitFlexGrow = (WebkitFlexGrow) stateChangedObject;

            final String cssValue = webkitFlexGrow.getCssValue();

            if (WebkitFlexGrow.INITIAL.equals(cssValue)
                    || WebkitFlexGrow.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as webkitFlexGrow cssValue");
            }
            this.cssValue = new StringBuilder(webkitFlexGrow.getCssValue())
                    .append(' ').append(webkitFlexShrink.getCssValue())
                    .append(' ').append(webkitFlexBasis.getCssValue())
                    .toString();
        } else if (stateChangedObject instanceof WebkitFlexShrink) {

            final WebkitFlexShrink webkitFlexShrink = (WebkitFlexShrink) stateChangedObject;

            final String cssValue = webkitFlexShrink.getCssValue();

            if (WebkitFlexShrink.INITIAL.equals(cssValue)
                    || WebkitFlexShrink.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as webkitFlexShrink cssValue");
            }
            this.cssValue = new StringBuilder(webkitFlexGrow.getCssValue())
                    .append(' ').append(webkitFlexShrink.getCssValue())
                    .append(' ').append(webkitFlexBasis.getCssValue())
                    .toString();

        } else if (stateChangedObject instanceof WebkitFlexBasis) {

            final WebkitFlexBasis webkitFlexBasis = (WebkitFlexBasis) stateChangedObject;

            final String cssValue = webkitFlexBasis.getCssValue();

            if (WebkitFlexBasis.INITIAL.equals(cssValue)
                    || WebkitFlexBasis.INHERIT.equals(cssValue)) {
                throw new InvalidValueException(
                        "initial/inherit cannot be set as webkitFlexBasis cssValue");
            }

            this.cssValue = new StringBuilder(webkitFlexGrow.getCssValue())
                    .append(' ').append(webkitFlexShrink.getCssValue())
                    .append(' ').append(webkitFlexBasis.getCssValue())
                    .toString();
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
