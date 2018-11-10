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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.webfirmframework.wffweb.css.core.CssEnumUtil;
import com.webfirmframework.wffweb.util.TagStringUtil;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public enum CssColorName {

    ALICE_BLUE("aliceblue", "#F0F8FF"),

    ANTIQUE_WHITE("antiquewhite", "#FAEBD7"),

    AQUA("Aqua", "#00FFFF"),

    AQUAMARINE("aquamarine", "#7FFFD4"),

    AZURE("azure", "#F0FFFF"),

    BEIGE("beige", "#F5F5DC"),

    BISQUE("bisque", "#FFE4C4"),

    BLACK("black", "#000000"),

    BLANCHED_ALMOND("blanchedalmond", "#FFEBCD"),

    BLUE("blue", "#0000FF"),

    BLUE_VIOLET("blueviolet", "#8A2BE2"),

    BROWN("brown", "#A52A2A"),

    BURLY_WOOD("burlywood", "#DEB887"),

    CADET_BLUE("cadetblue", "#5F9EA0"),

    CHARTREUSE("chartreuse", "#7FFF00"),

    CHOCOLATE("chocolate", "#D2691E"),

    CORAL("coral", "#FF7F50"),

    CORNFLOWER_BLUE("cornflowerblue", "#6495ED"),

    CORNSILK("cornsilk", "#FFF8DC"),

    CRIMSON("crimson", "#DC143C"),

    CYAN("cyan", "#00FFFF"),

    DARK_BLUE("darkblue", "#00008B"),

    DARK_CYAN("darkcyan", "#008B8B"),

    DARK_GOLDEN_ROD("darkgoldenrod", "#B8860B"),

    DARK_GRAY("darkgray", "#A9A9A9"),

    DARK_GREEN("darkgreen", "#006400"),

    DARK_KHAKI("darkkhaki", "#BDB76B"),

    DARK_MAGENTA("darkmagenta", "#8B008B"),

    DARK_OLIVE_GREEN("darkolivegreen", "#556B2F"),

    DARK_ORANGE("darkorange", "#FF8C00"),

    DARK_ORCHID("darkorchid", "#9932CC"),

    DARK_RED("darkred", "#8B0000"),

    DARK_SALMON("darksalmon", "#E9967A"),

    DARK_SEA_GREEN("darkseagreen", "#8FBC8F"),

    DARK_SLATE_BLUE("darkslateblue", "#483D8B"),

    DARK_SLATE_GRAY("darkslategray", "#2F4F4F"),

    DARK_TURQUOISE("darkturquoise", "#00CED1"),

    DARK_VIOLET("darkviolet", "#9400D3"),

    DEEP_PINK("deeppink", "#FF1493"),

    DEEP_SKY_BLUE("deepskyblue", "#00BFFF"),

    DIM_GRAY("dimgray", "#696969"),

    DODGER_BLUE("dodgerblue", "#1E90FF"),

    FIRE_BRICK("firebrick", "#B22222"),

    FLORAL_WHITE("floralwhite", "#FFFAF0"),

    FOREST_GREEN("forestgreen", "#228B22"),

    FUCHSIA("fuchsia", "#FF00FF"),

    GAINSBORO("gainsboro", "#DCDCDC"),

    GHOST_WHITE("ghostwhite", "#F8F8FF"),

    GOLD("gold", "#FFD700"),

    GOLDEN_ROD("goldenrod", "#DAA520"),

    GRAY("gray", "#808080"),

    GREEN("green", "#008000"),

    GREEN_YELLOW("greenyellow", "#ADFF2F"),

    HONEY_DEW("honeydew", "#F0FFF0"),

    HOT_PINK("hotpink", "#FF69B4"),

    INDIAN_RED("indianred", "#CD5C5C"),

    INDIGO("indigo", "#4B0082"),

    IVORY("ivory", "#FFFFF0"),

    KHAKI("khaki", "#F0E68C"),

    LAVENDER("lavender", "#E6E6FA"),

    LAVENDER_BLUSH("lavenderblush", "#FFF0F5"),

    LAWN_GREEN("lawngreen", "#7CFC00"),

    LEMON_CHIFFON("lemonchiffon", "#FFFACD"),

    LIGHT_BLUE("lightblue", "#ADD8E6"),

    LIGHT_CORAL("lightcoral", "#F08080"),

    LIGHT_CYAN("lightcyan", "#E0FFFF"),

    LIGHT_GOLDEN_ROD_YELLOW("lightgoldenrodyellow", "#FAFAD2"),

    LIGHT_GRAY("lightgray", "#D3D3D3"),

    LIGHT_GREEN("lightgreen", "#90EE90"),

    LIGHT_PINK("lightpink", "#FFB6C1"),

    LIGHT_SALMON("lightsalmon", "#FFA07A"),

    LIGHT_SEA_GREEN("lightseagreen", "#20B2AA"),

    LIGHT_SKY_BLUE("lightskyblue", "#87CEFA"),

    LIGHT_SLATE_GRAY("lightslategray", "#778899"),

    LIGHT_STEEL_BLUE("lightsteelblue", "#B0C4DE"),

    LIGHT_YELLOW("lightyellow", "#FFFFE0"),

    LIME("lime", "#00FF00"),

    LIME_GREEN("limegreen", "#32CD32"),

    LINEN("linen", "#FAF0E6"),

    MAGENTA("magenta", "#FF00FF"),

    MAROON("maroon", "#800000"),

    MEDIUM_AQUA_MARINE("mediumaquamarine", "#66CDAA"),

    MEDIUM_BLUE("mediumblue", "#0000CD"),

    MEDIUM_ORCHID("mediumorchid", "#BA55D3"),

    MEDIUM_PURPLE("mediumpurple", "#9370DB"),

    MEDIUM_SEA_GREEN("mediumseagreen", "#3CB371"),

    MEDIUM_SLATE_BLUE("mediumslateblue", "#7B68EE"),

    MEDIUM_SPRING_GREEN("mediumspringgreen", "#00FA9A"),

    MEDIUM_TURQUOISE("mediumturquoise", "#48D1CC"),

    MEDIUM_VIOLET_RED("mediumvioletred", "#C71585"),

    MIDNIGHT_BLUE("midnightblue", "#191970"),

    MINT_CREAM("mintcream", "#F5FFFA"),

    MISTY_ROSE("mistyrose", "#FFE4E1"),

    MOCCASIN("moccasin", "#FFE4B5"),

    NAVY("navy", "#000080"),

    OLD_LACE("oldlace", "#FDF5E6"),

    OLIVE("olive", "#808000"),

    OLIVE_DRAB("olivedrab", "#6B8E23"),

    ORANGE("orange", "#FFA500"),

    ORANGE_RED("orangered", "#FF4500"),

    ORCHID("orchid", "#DA70D6"),

    PALE_GREEN("palegreen", "#98FB98"),

    PALE_GOLDEN_ROD("palegoldenrod", "#EEE8AA"),

    PALE_TURQUOISE("paleturquoise", "#AFEEEE"),

    PALE_VIOLET_RED("palevioletred", "#DB7093"),

    REBECCA_PURPLE("rebeccapurple", "#663399"),

    NAVAJO_WHITE("navajowhite", "#FFDEAD"),

    SPRING_GREEN("springgreen", "#00FF7F"),

    YELLOW_GREEN("yellowgreen", "#9ACD32"),

    PAPAYA_WHIP("papayawhip", "#FFEFD5"),

    PEACH_PUFF("peachpuff", "#FFDAB9"),

    PERU("peru", "#CD853F"),

    PINK("pink", "#FFC0CB"),

    PLUM("plum", "#DDA0DD"),

    POWDER_BLUE("powderblue", "#B0E0E6"),

    PURPLE("purple", "#800080"),

    RED("red", "#FF0000"),

    ROSY_BROWN("rosybrown", "#BC8F8F"),

    ROYAL_BLUE("royalblue", "#4169E1"),

    SADDLE_BROWN("saddlebrown", "#8B4513"),

    SALMON("salmon", "#FA8072"),

    SANDY_BROWN("sandybrown", "#F4A460"),

    SEA_GREEN("seagreen", "#2E8B57"),

    SEA_SHELL("seashell", "#FFF5EE"),

    SIENNA("sienna", "#A0522D"),

    SILVER("silver", "#C0C0C0"),

    SKY_BLUE("skyblue", "#87CEEB"),

    SLATE_BLUE("slateblue", "#6A5ACD"),

    SLATE_GRAY("slategray", "#708090"),

    SNOW("snow", "#FFFAFA"),

    STEEL_BLUE("steelblue", "#4682B4"),

    TAN("tan", "#D2B48C"),

    TEAL("teal", "#008080"),

    THISTLE("thistle", "#D8BFD8"),

    TOMATO("tomato", "#FF6347"),

    TURQUOISE("turquoise", "#40E0D0"),

    VIOLET("violet", "#EE82EE"),

    WHEAT("wheat", "#F5DEB3"),

    WHITE("white", "#FFFFFF"),

    WHITE_SMOKE("whitesmoke", "#F5F5F5"),

    YELLOW("yellow", "#FFFF00"),

    DARK_GREY("darkgrey", "#A9A9A9"),

    DARK_SLATE_GREY("darkslategrey", "#2F4F4F"),

    DIM_GREY("dimgrey", "#696969"),

    GREY("grey", "#808080"),

    LIGHT_GREY("lightgrey", "#D3D3D3"),

    LIGHT_SLATE_GREY("lightslategrey", "#778899"),

    SLATE_GREY("slategrey", "#708090");

    private static final Collection<String> UPPER_CASE_SUPER_TO_STRINGS;

    private static final Map<String, CssColorName> ALL_OBJECTS;

    // the lowest length value
    private static final int LOWEST_LENGTH;
    // the highest length value
    private static final int HIGHEST_LENGTH;

    static {
        ALL_OBJECTS = new HashMap<>();
        Collection<String> upperCaseSuperToStringsTemp = new ArrayList<>();
        int min = values()[0].colorName.length();
        int max = 0;
        for (int i = 0; i < values().length; i++) {
            final int length = values()[i].colorName.length();
            if (max < length) {
                max = length;
            }
            if (min > length) {
                min = length;
            }
            final String upperCaseCssValue = TagStringUtil
                    .toUpperCase(values()[i].colorName);
            upperCaseSuperToStringsTemp.add(upperCaseCssValue);
            ALL_OBJECTS.put(upperCaseCssValue, values()[i]);
        }
        LOWEST_LENGTH = min;
        HIGHEST_LENGTH = max;
        if (values().length > 10) {
            upperCaseSuperToStringsTemp = new HashSet<>(
                    upperCaseSuperToStringsTemp);
        }
        UPPER_CASE_SUPER_TO_STRINGS = upperCaseSuperToStringsTemp;
    }

    private final String colorName;

    private final String hex;

    private final int r;

    private final int g;

    private final int b;

    private CssColorName(final String colorName, final String hex) {
        this.colorName = colorName;
        this.hex = hex;
        if (hex.length() > 7) {
            throw new IllegalArgumentException("Invalid hex value");
        }
        r = Integer.parseInt(hex.substring(1, 3), 16);
        g = Integer.parseInt(hex.substring(3, 5), 16);
        b = Integer.parseInt(hex.substring(5, 7), 16);
    }

    /**
     * @return the colorName
     * @since 1.0.0
     * @author WFF
     */
    public String getColorName() {
        return colorName;
    }

    /**
     * @return the hex value of the color, eg: for white it will return
     *         {@code #FFFFFF}
     * @since 3.0.1
     * @author WFF
     */
    public String getHex() {
        return hex;
    }

    /**
     * @return r value
     * @since 3.0.1
     * @author WFF
     */
    public int getR() {
        return r;
    }

    /**
     * @return g value
     * @since 3.0.1
     * @author WFF
     */
    public int getG() {
        return g;
    }

    /**
     * @return b value
     * @since 3.0.1
     * @author WFF
     */
    public int getB() {
        return b;
    }

    /**
     * @return the name of this enum.
     * @since 1.0.0
     * @author WFF
     */
    public String getEnumName() {
        return super.toString();
    }

    @Override
    public String toString() {
        return colorName;
    }

    /**
     * checks whether the given given {@code colorName} is valid , i.e. whether
     * it can have a corresponding object from it.
     *
     * @param colorName
     * @return true if the given {@code cssColorName} has a corresponding
     *         object.
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isValid(final String colorName) {
        return CssEnumUtil.contains(colorName, UPPER_CASE_SUPER_TO_STRINGS,
                LOWEST_LENGTH, HIGHEST_LENGTH);
    }

    /**
     * gets the corresponding object for the given {@code colorName} or null for
     * invalid colorName.
     *
     * @param colorName
     *            the inbuilt color name as per w3 standard.
     * @return the corresponding object for the given {@code colorName} or null
     *         for invalid colorName.
     * @since 1.0.0
     * @author WFF
     */
    public static CssColorName getThis(final String colorName) {
        final String enumString = TagStringUtil.toUpperCase(colorName);
        return ALL_OBJECTS.get(enumString);
    }

}
