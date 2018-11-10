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
package com.webfirmframework.wffweb.css.file;

import java.util.Set;

import com.webfirmframework.wffweb.css.FontStyle;
import com.webfirmframework.wffweb.css.core.CssProperty;

@SuppressWarnings({ "unused", "serial" })
public class SampleCssFile1 extends CssFile {

    private CssProperty cssProperty;

    public SampleCssFile1(CssProperty cssProperty) {
        this.cssProperty = cssProperty;
    }

    private CssBlock block1 = new CssBlock("test-class1") {

        @Override
        protected void load(Set<CssProperty> cssProperties) {
            cssProperties.add(FontStyle.NORMAL);
            cssProperties.add(cssProperty);
        }
    };

}
