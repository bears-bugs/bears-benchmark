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
package com.webfirmframework.wffweb.tag.htmlwff;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;

@SuppressWarnings("serial")
public class NoTagTest {

    @Test
    public void testNoTagAbstractHtmlAbstractHtmlArray() {

        Div div = new Div(null, new Id("one")) {

            {
                new NoTag(this, new Div(null, new Id("two")));
            }

        };

        Assert.assertEquals("<div id=\"one\"><div id=\"two\"></div></div>",
                div.toHtmlString());
    }

    @Test
    public void testNoTagAbstractHtmlCollectionOfAbstractHtml() {

        Div div = new Div(null, new Id("one")) {

            {
                List<Div> children = new LinkedList<Div>();
                children.add(new Div(null, new Id("two")));
                new NoTag(this, children);
            }

        };

        Assert.assertEquals("<div id=\"one\"><div id=\"two\"></div></div>",
                div.toHtmlString());

        // fail("Not yet implemented");
    }

    @Test
    public void testNoTagAbstractHtmlString() {
        Div div = new Div(null, new Id("one")) {

            {
                new NoTag(this, "<div id=\"two\"></div>");
            }

        };

        Assert.assertEquals("<div id=\"one\"><div id=\"two\"></div></div>",
                div.toHtmlString());

    }

    // @Test
    public void testInit() {
        // fail("Not yet implemented");
    }

    // @Test
    public void testAddChildren() {
        // fail("Not yet implemented");
    }

    @Test
    public void testAddChildAbstractHtml() {
        Div div = new Div(null, new Id("one")) {

            {
                NoTag noTag = new NoTag(this);

                Div child = new Div(null, new Id("two")) {
                    {
                        new Div(this, new Id("three"));
                    }
                };
                noTag.addChild(child);
            }

        };

        Assert.assertEquals(
                "<div id=\"one\"><div id=\"two\"><div id=\"three\"></div></div></div>",
                div.toHtmlString());

        // fail("Not yet implemented");
    }

    // @Test
    public void testRemoveChildren() {
        // fail("Not yet implemented");
    }

    // @Test
    public void testRemoveChildAbstractHtml() {
        // fail("Not yet implemented");
    }

    // @Test
    public void testRemoveChildString() {
        // fail("Not yet implemented");
    }

    // @Test
    public void testAddChildString() {
        // fail("Not yet implemented");
    }

    // @Test
    public void testGetChildContent() {
        // fail("Not yet implemented");
    }

}
