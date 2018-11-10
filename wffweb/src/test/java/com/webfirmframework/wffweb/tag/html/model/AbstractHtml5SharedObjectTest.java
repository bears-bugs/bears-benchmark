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
package com.webfirmframework.wffweb.tag.html.model;

import static org.junit.Assert.*;


import org.junit.Test;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.DocType;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;

public class AbstractHtml5SharedObjectTest {
    
    

    @Test
    public void testGetNewDataWffId() {
        
        BrowserPage browserPage = new BrowserPage() {
            
            @Override
            public String webSocketUrl() {
                return "wss://webfirmframework.com/ws-con";
            }
            
            @Override
            public AbstractHtml render() {
                Html html = new Html(null) {
                    {
                        new Head(this);
                        new Body(this);
                    }
                };
                html.setPrependDocType(true);
                return html;
            }
        };
        browserPage.toHtmlString();
        
        final Html html = browserPage.getTagRepository().findOneTagAssignableToTag(Html.class);
        final Head head = browserPage.getTagRepository().findOneTagAssignableToTag(Head.class);
        final Body body = browserPage.getTagRepository().findOneTagAssignableToTag(Body.class);
        final DocType doc = browserPage.getTagRepository().findOneTagAssignableToTag(DocType.class);
        
        assertEquals("data-wff-id=\"S0\"", doc.getDataWffId().toHtmlString());
        assertEquals("data-wff-id=\"S0\"", html.getDataWffId().toHtmlString());
        assertEquals("data-wff-id=\"S1\"", head.getDataWffId().toHtmlString());
        assertEquals("data-wff-id=\"S2\"", body.getDataWffId().toHtmlString());
        
        {
            Div div = new Div(null);
            html.appendChild(div);
            assertEquals("data-wff-id=\"S4\"", div.getDataWffId().toHtmlString());
            
        }
        {
            Div div = new Div(null);
            html.appendChild(div);
            assertEquals("data-wff-id=\"S5\"", div.getDataWffId().toHtmlString());
            
        }
       
    }

}
