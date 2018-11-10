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
package com.webfirmframework.wffweb.security.object;

/**
 * Only for internal purpose.
 *
 * @author WFF
 *
 */
public final class SecurityClassConstants {

    public static final String ABSTRACT_JS_OBJECT = "com.webfirmframework.wffweb.tag.core.AbstractJsObject$Security";

    public static final String ABSTRACT_HTML = "com.webfirmframework.wffweb.tag.html.AbstractHtml$Security";

    public static final String BROWSER_PAGE = "com.webfirmframework.wffweb.server.page.BrowserPage$Security";

    public static final String ABSTRACT_ATTRIBUTE = "com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute$Security";

    public SecurityClassConstants() {
        throw new AssertionError();
    }
}
