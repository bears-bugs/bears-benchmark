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
package com.webfirmframework.wffweb.tag.html.formsandinputs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;

@SuppressWarnings("serial")
public class TextAreaTest {

    @Test
    public void testSetChildText() {
        TextArea textArea = new TextArea(null);
        textArea.setChildText("Sample Text Content");

        assertEquals("Sample Text Content", textArea.getChildText());
        assertEquals("<textarea>Sample Text Content</textarea>", textArea.toHtmlString());
    }

    @Test
    public void testGetChildText() {
        TextArea textArea = new TextArea(null) {
            {
                new NoTag(this, "one");
                new Div(this) {
                    {
                        new NoTag(this, "two");
                    }
                };
                new NoTag(this, "three");
            }
        };

        assertEquals("one<div>two</div>three", textArea.getChildText());
        assertEquals("<textarea>one<div>two</div>three</textarea>", textArea.toHtmlString());
        assertEquals("", new TextArea(null).getChildText());
    }

}
