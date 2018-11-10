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

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.webfirmframework.wffweb.css.BackgroundColor;
import com.webfirmframework.wffweb.css.FontStyle;
import com.webfirmframework.wffweb.css.FontWeight;
import com.webfirmframework.wffweb.css.core.CssProperty;

@SuppressWarnings({ "serial", "unused" })
public class SampleCssFile extends CssFile {

    private Set<FontWeight> fontWeights;

    public Set<FontWeight> getFontWeights() {
        return fontWeights;
    }

    public SampleCssFile(FontWeight... fontWeights) {

        this.fontWeights = fontWeights == null
                ? Collections.<FontWeight> emptySet()
                : new HashSet<FontWeight>(Arrays.asList(fontWeights));

        setOptimizeCssString(false);
    }

    @ImportCssFile
    private SampleCssFile1 block1 = new SampleCssFile1(
            new BackgroundColor("green"));

    private CssBlock block2 = new CssBlock("test-class2") {

        @Override
        protected void load(Set<CssProperty> cssProperties) {
            cssProperties.add(FontStyle.OBLIQUE);
        }
    };

    private CssBlock block3 = new CssBlock("test-class3") {

        @Override
        protected void load(Set<CssProperty> cssProperties) {

            if (!fontWeights.contains(FontWeight.NINE_HUNDRED)) {
                setExcludeCssBlock(true);
            }

            if (isExcludeCssBlock()) {
                // not to execute the remaining statements if this block is
                // excluded
                return;
            }
            cssProperties.add(FontStyle.OBLIQUE);
        }
    };

}
