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
package com.webfirmframework.wffweb.tag.html.attribute.core;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.attribute.Name;
import com.webfirmframework.wffweb.tag.html.attribute.global.ClassAttribute;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;

public class AbstractAttributeTest {

    @SuppressWarnings("serial")
    @Test
    public void testTestOwnerTag() {

        final Name name = new Name("somename");

        Html html = new Html(null, name) {
            {
                Div div1 = new Div(this, name);
                Div div2 = new Div(this, name);
                assertTrue(Arrays.asList(name.getOwnerTags()).contains(div1));
                assertTrue(Arrays.asList(name.getOwnerTags()).contains(div2));
            }
        };
        assertTrue(Arrays.asList(name.getOwnerTags()).contains(html));

    }

    @Test
    public void testGetWffPrintStructure() {

        ClassAttribute classAttribute = new ClassAttribute(
                "cl1 cl2 cl3 cl-col-4");

        System.out.println(classAttribute.getWffPrintStructure());
        assertEquals("class=cl1 cl2 cl3 cl-col-4",
                classAttribute.getWffPrintStructure());

        Style style = new Style("color:green;background:blue");
        assertEquals("style=color:green;background:blue;",
                style.getWffPrintStructure());

        Name name = new Name("somename");
        assertEquals("name=somename", name.getWffPrintStructure());
    }

    @Test
    public void testToWffString() {

        ClassAttribute classAttribute = new ClassAttribute(
                "cl1 cl2 cl3 cl-col-4");

        System.out.println(classAttribute.getWffPrintStructure());
        assertEquals("class=cl1 cl2 cl3 cl-col-4",
                classAttribute.toWffString());

        Style style = new Style("color:green;background:blue");
        assertEquals("style=color:green;background:blue;", style.toWffString());

        Name name = new Name("somename");
        assertEquals("name=somename", name.toWffString());
    }

}
