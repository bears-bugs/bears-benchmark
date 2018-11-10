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
package com.webfirmframework.wffweb.tag.html;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.webfirmframework.wffweb.tag.html.attribute.Name;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Span;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;

public class AbstractHtmlRepositoryTest {

    @SuppressWarnings("serial")
    @Test
    public void testGetAllNestedChildrenIncludingParent() {
        
        
        final Html html = new Html(null) {{
            new Body(this) {{
                new Div(this, new Id("one")) {{
                    new Span(this, new Id("two")) {{
                        new H1(this, new Id("three"));
                        new H2(this, new Id("three"));
                        new NoTag(this, "something");
                    }};

                    new H3(this, new Name("name1"));
                }};  
            }};
            
        }};
        
        {
            final Set<AbstractHtml> set =AbstractHtmlRepository.getAllNestedChildrenIncludingParent(false, html).collect(Collectors.toSet());       
            
            assertEquals(8, set.size());
        }
        {
            final Set<AbstractHtml> set =AbstractHtmlRepository.getAllNestedChildrenIncludingParent(true, html).collect(Collectors.toSet());       
            
            assertEquals(8, set.size());
        }
    }

}
