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
package com.webfirmframework.wffweb.css.temp;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.webfirmframework.wffweb.css.CssColorName;
import com.webfirmframework.wffweb.css.FontWeight;
import com.webfirmframework.wffweb.css.css3.ColumnRule;
import com.webfirmframework.wffweb.css.css3.ColumnRuleColor;
import com.webfirmframework.wffweb.css.css3.ColumnRuleWidth;
import com.webfirmframework.wffweb.tag.html.html5.attribute.global.Draggable;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.util.StringBuilderUtil;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class Test {

    public static void main(String[] args) {
        
        
        Div div = new Div(null, new Draggable(false));
        
        System.out.println(div);


        System.out.println(FontWeight.isValid("100"));
        System.out.println(CssColorName.isValid("lime"));

        String familyNames = "\"Times New Roman\", Georgia, Serif";
        final String[] parts = getExtractFamilyNames(familyNames);
        for (String string : parts) {
            System.out.println(string);
        }

        System.out.println(getBuiltCssValue(new String[] { "Times New Roman",
                "Georgia", "Serif" }));
    }

    /**
     * @param familyNames
     * @return an array containing extracted family names from the given input.
     * @author WFF
     * @since 1.0.0
     */
    public static String[] getExtractFamilyNames(String familyNames) {
        final String[] parts = familyNames.split(",");

        int count = 0;

        for (String each : parts) {
            final String trimmed = each.trim();

            final boolean startsWith = trimmed.startsWith("\"")
                    || trimmed.startsWith("'");
            int begin = 0;
            if (startsWith) {
                begin = 1;
            }

            final boolean endsWith = trimmed.endsWith("\"")
                    || trimmed.endsWith("'");
            int end = trimmed.length();
            if (endsWith) {
                end = end - 1;
            }

            parts[count] = trimmed.substring(begin, end);
            count++;
        }
        return parts;
    }

    /**
     * @param fontFamilyNames
     * @return the built cssValue string from the given fontFamilyNames.
     * @author WFF
     * @since 1.0.0
     */
    private static String getBuiltCssValue(final String... fontFamilyNames) {
        StringBuilder cssValueSB = new StringBuilder();
        int count = 1;
        for (String fontFamilyName : fontFamilyNames) {
            if (fontFamilyName.contains(" ")) {
                cssValueSB.append("\"");
                cssValueSB.append(fontFamilyName);
                cssValueSB.append("\"");
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

    public static void main1(final String[] args) {

        // System.out.println(Overflow.HIDDEN);
        // System.out.println(Overflow.HIDDEN.toString());
        // System.out.println(Overflow.HIDDEN.getCssName());
        // System.out.println(Overflow.HIDDEN.getCssValue());
        //
        // System.out.println(FontStyle.ITALIC);
        // System.out.println(FontStyle.ITALIC.toString());
        // System.out.println(FontStyle.ITALIC.getCssName());
        // System.out.println(FontStyle.ITALIC.getCssValue());
        // FontVariant[] values = FontVariant.values();
        // for (FontVariant fontVariant : values) {
        // System.out.println(fontVariant.getCssValue());
        // }
        // boolean valid = FontVariant.isValid("normalddddddddddddddddd");
        // System.out.println(valid);

        final ColumnRule columnRule = new ColumnRule("75px dotted green");
        System.out.println(columnRule.getColumnRuleWidth());
        System.out.println(columnRule.getColumnRuleStyle());
        System.out.println(columnRule.getColumnRuleColor());

        final boolean validColor = ColumnRuleColor.isValid("#FFFFFF");
        System.out.println("validColor " + validColor);

        final boolean validLength = ColumnRuleWidth.isValid("5px");
        System.out.println("validLength " + validLength);

        CssColorName.values();
        
        final boolean valid = CssColorName.isValid("steelblue");
        System.out.println("valid SteelBlue " + valid);
        System.out.println("object "
                + CssColorName.getThis("steelblue").getEnumName());

        final Set<String> set = new LinkedHashSet<String>();

        set.add("t");
        set.add("zzzzzzzz");
        set.add("c");
        set.add("z");
        System.out.println(set);
        for (final String string : set) {
            System.out.println(string);
        }
        System.out.println("-----------");
        {
            final Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                final String next = iterator.next();
                System.out.println(next);
            }
        }
        set.remove("c");
        set.add("another");
    }

}
