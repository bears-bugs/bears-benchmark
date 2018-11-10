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

/**
 * <pre>
 * src: <i>URL</i>;
 * </pre>
 *
 * Defines the URL(s) where the font needs to be downloaded from. This css
 * property is applicable inside {@code @font-face} css selector.
 *
 * This {@code SrcCssProperty} is using {@link Src} css property. This class is
 * introduced to avoid conflicts with
 * {@link com.webfirmframework.wffweb.tag.html.attribute.Src} attribute class.
 *
 * @author WFF
 * @since 2.1.11
 */
public class SrcCssProperty extends Src {

    private static final long serialVersionUID = 1_0_0L;

    public SrcCssProperty(final String cssValue) {
        super(cssValue);
    }

}
