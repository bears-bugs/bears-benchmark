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
package com.webfirmframework.wffweb.css.temp;

import com.webfirmframework.wffweb.util.StringUtil;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class TestPrint {

    public static void main(String[] args) {
        String cssValue = "italic small-caps bold 12px/1.4 arial,sans-serif";
        
        final String[] cssValueParts = getExtractedSubCssValues(cssValue);
        for (String string : cssValueParts) {
            System.out.println(string);
        }
        
    }

    /**
     * @param input
     * @return the sub properties
     * @author WFF
     * @since 1.0.0
     */
    protected static String[] getExtractedSubCssValues(final String input) {
        final String convertedToSingleSpace = StringUtil.convertToSingleSpace(input);
        final String[] subCssValues = convertedToSingleSpace.replace(", ", ",").split("[ /]");
        return subCssValues;
    }

}
