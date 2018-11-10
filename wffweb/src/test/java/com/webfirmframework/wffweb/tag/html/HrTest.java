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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.webfirmframework.wffweb.tag.html.AbstractHtml.TagType;

public class HrTest {

    @Test
    public void testHr1() {

        Hr hr = new Hr(null);
        assertEquals("<hr>", hr.toHtmlString());
        
        Hr.setTagType(TagType.OPENING_CLOSING);
        hr = new Hr(null);
        assertEquals("<hr></hr>", hr.toHtmlString());
        
        Hr.setTagType(TagType.SELF_CLOSING);
        hr = new Hr(null);
        assertEquals("<hr/>", hr.toHtmlString());
        
        Hr.setTagType(TagType.NON_CLOSING);
        hr = new Hr(null);
        assertEquals("<hr>", hr.toHtmlString());
    }

    @Test
    public void testHr2() {

        Hr.setTagType(TagType.SELF_CLOSING);
        Hr hr = new Hr(null);
        assertEquals("<hr/>", hr.toHtmlString());

    }

}
