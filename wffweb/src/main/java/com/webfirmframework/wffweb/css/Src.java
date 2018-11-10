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

import java.util.logging.Logger;

import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.css.core.AbstractCssProperty;
import com.webfirmframework.wffweb.util.StringUtil;

/**
 * <pre>
 * src: <i>URL</i>;
 * </pre>
 *
 * Defines the URL(s) where the font needs to be downloaded from. This css
 * property is applicable inside {@code @font-face} css selector.
 *
 * @author WFF
 * @version 1.1.2
 * @since 1.1.2
 */
public class Src extends AbstractCssProperty<Src> {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger.getLogger(Src.class.getName());

    private String cssValue;

    /**
     *
     * @param cssValue
     *            the cssValue to set
     * @author WFF
     * @since 1.1.2
     */
    public Src(final String cssValue) {
        setCssValue(cssValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.CssProperty#getCssName()
     *
     * @since 1.1.2
     *
     * @author WFF
     */
    @Override
    public String getCssName() {
        return CssNameConstants.SRC;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.css.CssProperty#getCssValue()
     *
     * @since 1.1.2
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
     *            The value should be a unicodeChars sequence.
     * @since 1.1.2
     * @author WFF
     */
    @Override
    public Src setCssValue(final String cssValue) {
        if (cssValue == null) {
            throw new NullValueException("null is an invalid value");
        } else {

            this.cssValue = StringUtil.strip(cssValue);

            if (getStateChangeInformer() != null) {
                getStateChangeInformer().stateChanged(this);
            }
        }
        return this;
    }

}
