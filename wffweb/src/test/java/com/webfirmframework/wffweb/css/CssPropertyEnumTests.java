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
package com.webfirmframework.wffweb.css;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.webfirmframework.wffweb.css.css3.AlignContent;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class CssPropertyEnumTests {

    @Test
    public void testAlignContent() {
      assertNotNull(AlignContent.getThis("stretch"));
      assertNotNull(AlignContent.getThis("center"));
      assertNotNull(AlignContent.getThis("flex-start"));
      assertNotNull(AlignContent.getThis("flex-end"));
      assertNotNull(AlignContent.getThis("space-between"));
      assertNotNull(AlignContent.getThis("space-around"));
      assertNotNull(AlignContent.getThis("initial"));
      assertNotNull(AlignContent.getThis("inherit"));
    }
    
    @Test
    public void testFontWeight() {
       assertNotNull(FontWeight.getThis("normal"));
       assertNotNull(FontWeight.getThis("bold"));
       assertNotNull(FontWeight.getThis("bolder"));
       assertNotNull(FontWeight.getThis("lighter"));
       assertNotNull(FontWeight.getThis("100"));
       assertNotNull(FontWeight.getThis("200"));
       assertNotNull(FontWeight.getThis("300"));
       assertNotNull(FontWeight.getThis("400"));
       assertNotNull(FontWeight.getThis("500"));
       assertNotNull(FontWeight.getThis("600"));
       assertNotNull(FontWeight.getThis("700"));
       assertNotNull(FontWeight.getThis("800"));
       assertNotNull(FontWeight.getThis("900"));
       assertNotNull(FontWeight.getThis("initial"));
       assertNotNull(FontWeight.getThis("inherit"));
    }
    
    @Test
    public void testCssColorName() {
//        CssColorName[] values = CssColorName.values();
//        for (CssColorName each : values) {
//            System.out.println(each.getColorName());
//        }
        
       assertNotNull(CssColorName.getThis("aliceblue"));
       assertNotNull(CssColorName.getThis("antiquewhite"));
       assertNotNull(CssColorName.getThis("Aqua"));
       assertNotNull(CssColorName.getThis("aquamarine"));
       assertNotNull(CssColorName.getThis("azure"));
       assertNotNull(CssColorName.getThis("beige"));
       assertNotNull(CssColorName.getThis("bisque"));
       assertNotNull(CssColorName.getThis("black"));
       assertNotNull(CssColorName.getThis("blanchedalmond"));
       assertNotNull(CssColorName.getThis("blue"));
       assertNotNull(CssColorName.getThis("blueviolet"));
       assertNotNull(CssColorName.getThis("brown"));
       assertNotNull(CssColorName.getThis("burlywood"));
       assertNotNull(CssColorName.getThis("cadetblue"));
       assertNotNull(CssColorName.getThis("chocolate"));
       assertNotNull(CssColorName.getThis("coral"));
       assertNotNull(CssColorName.getThis("cornflowerblue"));
       assertNotNull(CssColorName.getThis("cornsilk"));
       assertNotNull(CssColorName.getThis("crimson"));
       assertNotNull(CssColorName.getThis("cyan"));
       assertNotNull(CssColorName.getThis("darkblue"));
       assertNotNull(CssColorName.getThis("darkcyan"));
       assertNotNull(CssColorName.getThis("darkgoldenrod"));
       assertNotNull(CssColorName.getThis("darkgray"));
       assertNotNull(CssColorName.getThis("darkgreen"));
       assertNotNull(CssColorName.getThis("darkkhaki"));
       assertNotNull(CssColorName.getThis("darkmagenta"));
       assertNotNull(CssColorName.getThis("darkolivegreen"));
       assertNotNull(CssColorName.getThis("darkorange"));
       assertNotNull(CssColorName.getThis("darkorchid"));
       assertNotNull(CssColorName.getThis("darkred"));
       assertNotNull(CssColorName.getThis("darksalmon"));
       assertNotNull(CssColorName.getThis("darkseagreen"));
       assertNotNull(CssColorName.getThis("darkslateblue"));
       assertNotNull(CssColorName.getThis("darkslategray"));
       assertNotNull(CssColorName.getThis("darkturquoise"));
       assertNotNull(CssColorName.getThis("darkviolet"));
       assertNotNull(CssColorName.getThis("deeppink"));
       assertNotNull(CssColorName.getThis("deepskyblue"));
       assertNotNull(CssColorName.getThis("dimgray"));
       assertNotNull(CssColorName.getThis("dodgerblue"));
       assertNotNull(CssColorName.getThis("firebrick"));
       assertNotNull(CssColorName.getThis("floralwhite"));
       assertNotNull(CssColorName.getThis("forestgreen"));
       assertNotNull(CssColorName.getThis("fuchsia"));
       assertNotNull(CssColorName.getThis("gainsboro"));
       assertNotNull(CssColorName.getThis("ghostwhite"));
       assertNotNull(CssColorName.getThis("gold"));
       assertNotNull(CssColorName.getThis("goldenrod"));
       assertNotNull(CssColorName.getThis("gray"));
       assertNotNull(CssColorName.getThis("green"));
       assertNotNull(CssColorName.getThis("greenyellow"));
       assertNotNull(CssColorName.getThis("honeydew"));
       assertNotNull(CssColorName.getThis("hotpink"));
       assertNotNull(CssColorName.getThis("indianred"));
       assertNotNull(CssColorName.getThis("indigo"));
       assertNotNull(CssColorName.getThis("ivory"));
       assertNotNull(CssColorName.getThis("khaki"));
       assertNotNull(CssColorName.getThis("lavender"));
       assertNotNull(CssColorName.getThis("lavenderblush"));
       assertNotNull(CssColorName.getThis("lawngreen"));
       assertNotNull(CssColorName.getThis("lemonchiffon"));
       assertNotNull(CssColorName.getThis("lightblue"));
       assertNotNull(CssColorName.getThis("lightcoral"));
       assertNotNull(CssColorName.getThis("lightcyan"));
       assertNotNull(CssColorName.getThis("lightgoldenrodyellow"));
       assertNotNull(CssColorName.getThis("lightgray"));
       assertNotNull(CssColorName.getThis("lightgreen"));
       assertNotNull(CssColorName.getThis("lightpink"));
       assertNotNull(CssColorName.getThis("lightsalmon"));
       assertNotNull(CssColorName.getThis("lightseagreen"));
       assertNotNull(CssColorName.getThis("lightskyblue"));
       assertNotNull(CssColorName.getThis("lightslategray"));
       assertNotNull(CssColorName.getThis("lightsteelblue"));
       assertNotNull(CssColorName.getThis("lightyellow"));
       assertNotNull(CssColorName.getThis("lime"));
       assertNotNull(CssColorName.getThis("limegreen"));
       assertNotNull(CssColorName.getThis("linen"));
       assertNotNull(CssColorName.getThis("magenta"));
       assertNotNull(CssColorName.getThis("maroon"));
       assertNotNull(CssColorName.getThis("mediumaquamarine"));
       assertNotNull(CssColorName.getThis("mediumblue"));
       assertNotNull(CssColorName.getThis("mediumorchid"));
       assertNotNull(CssColorName.getThis("mediumpurple"));
       assertNotNull(CssColorName.getThis("mediumseagreen"));
       assertNotNull(CssColorName.getThis("mediumslateblue"));
       assertNotNull(CssColorName.getThis("mediumspringgreen"));
       assertNotNull(CssColorName.getThis("mediumturquoise"));
       assertNotNull(CssColorName.getThis("mediumvioletred"));
       assertNotNull(CssColorName.getThis("midnightblue"));
       assertNotNull(CssColorName.getThis("mintcream"));
       assertNotNull(CssColorName.getThis("mistyrose"));
       assertNotNull(CssColorName.getThis("moccasin"));
       assertNotNull(CssColorName.getThis("navy"));
       assertNotNull(CssColorName.getThis("oldlace"));
       assertNotNull(CssColorName.getThis("olive"));
       assertNotNull(CssColorName.getThis("olivedrab"));
       assertNotNull(CssColorName.getThis("orange"));
       assertNotNull(CssColorName.getThis("orangered"));
       assertNotNull(CssColorName.getThis("orchid"));
       assertNotNull(CssColorName.getThis("palegreen"));
       assertNotNull(CssColorName.getThis("palegoldenrod"));
       assertNotNull(CssColorName.getThis("paleturquoise"));
       assertNotNull(CssColorName.getThis("palevioletred"));
       assertNotNull(CssColorName.getThis("rebeccapurple"));
       assertNotNull(CssColorName.getThis("navajowhite"));
       assertNotNull(CssColorName.getThis("springgreen"));
       assertNotNull(CssColorName.getThis("yellowgreen"));
       assertNotNull(CssColorName.getThis("papayawhip"));
       assertNotNull(CssColorName.getThis("peachpuff"));
       assertNotNull(CssColorName.getThis("peru"));
       assertNotNull(CssColorName.getThis("pink"));
       assertNotNull(CssColorName.getThis("plum"));
       assertNotNull(CssColorName.getThis("powderblue"));
       assertNotNull(CssColorName.getThis("purple"));
       assertNotNull(CssColorName.getThis("red"));
       assertNotNull(CssColorName.getThis("rosybrown"));
       assertNotNull(CssColorName.getThis("royalblue"));
       assertNotNull(CssColorName.getThis("saddlebrown"));
       assertNotNull(CssColorName.getThis("salmon"));
       assertNotNull(CssColorName.getThis("sandybrown"));
       assertNotNull(CssColorName.getThis("seagreen"));
       assertNotNull(CssColorName.getThis("seashell"));
       assertNotNull(CssColorName.getThis("sienna"));
       assertNotNull(CssColorName.getThis("silver"));
       assertNotNull(CssColorName.getThis("skyblue"));
       assertNotNull(CssColorName.getThis("slateblue"));
       assertNotNull(CssColorName.getThis("slategray"));
       assertNotNull(CssColorName.getThis("snow"));
       assertNotNull(CssColorName.getThis("steelblue"));
       assertNotNull(CssColorName.getThis("tan"));
       assertNotNull(CssColorName.getThis("teal"));
       assertNotNull(CssColorName.getThis("thistle"));
       assertNotNull(CssColorName.getThis("tomato"));
       assertNotNull(CssColorName.getThis("turquoise"));
       assertNotNull(CssColorName.getThis("violet"));
       assertNotNull(CssColorName.getThis("wheat"));
       assertNotNull(CssColorName.getThis("white"));
       assertNotNull(CssColorName.getThis("whitesmoke"));
       assertNotNull(CssColorName.getThis("yellow"));
        
        
        
    }

}
