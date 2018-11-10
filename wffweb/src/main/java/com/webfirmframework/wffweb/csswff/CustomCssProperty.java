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
package com.webfirmframework.wffweb.csswff;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class CustomCssProperty extends AbstractCssProperty<CustomCssProperty> {

    private static final long serialVersionUID = 1_0_0L;

    private String cssName;
    private String cssValue;

    @SuppressWarnings("unused")
    private CustomCssProperty() {
    }

    /**
     * @param cssName
     *            the css name to set.
     * @param cssValue
     *            the css value to set.
     * @author WFF
     */
    public CustomCssProperty(final String cssName, final String cssValue) {
        if (cssName == null) {
            throw new NullValueException("cssName can not be null");
        }
        if (cssValue == null) {
            throw new NullValueException("cssValue can not be null");
        }
        if (StringUtil.endsWithColon(StringUtil.strip(cssName))) {
            throw new InvalidValueException(
                    "cssName can not end with : (colon)");
        }
        this.cssName = StringUtil.strip(cssName);
        setCssValue(StringUtil.strip(cssValue));
    }

    /**
     * @param customCssProperty
     *            the {@code CustomCss} object from which the cssName and
     *            cssValue to set.
     * @author WFF
     */
    public CustomCssProperty(final CustomCssProperty customCssProperty) {
        if (customCssProperty == null) {
            throw new NullValueException("customCss can not be null");
        }
        if (customCssProperty.getCssName() == null) {
            throw new NullValueException(
                    "customCss.getCssName() can not be null");
        }
        if (StringUtil.endsWithColon(customCssProperty.getCssName())) {
            throw new InvalidValueException(
                    "customCss.getCssName() can not end with : (colon)");
        }
        cssName = customCssProperty.getCssName();
        setCssValue(customCssProperty.getCssValue());
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
        return cssName;
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
        return getCssValue();
    }

    /**
     * @param cssValue
     *            {@code null} is considered as an invalid value.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public CustomCssProperty setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException("the value can not be null");
        }
        final String trimmedCssValue = StringUtil.strip(cssValue);
        if (trimmedCssValue.length() > 0
                && (trimmedCssValue.charAt(0) == ':' || trimmedCssValue
                        .charAt(trimmedCssValue.length() - 1) == ';')) {
            throw new InvalidValueException(
                    "cssValue can not start with : (colon) or end with ; (semicolon)");
        }
        this.cssValue = cssValue;
        if (getStateChangeInformer() != null) {
            getStateChangeInformer().stateChanged(this);
        }
        return this;
    }

}
