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
package com.webfirmframework.wffweb.css.core;

import java.io.Serializable;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public interface CssProperty extends Serializable, Cloneable {

    /**
     * @return the name portion in style, eg <code>align-content</code> for
     *         style <code>align-content: center</code>.
     * @since 1.0.0
     * @author WFF
     */
    String getCssName();

    /**
     * @return the value portion in style, eg <code>center</code> for style
     *         <code>align-content: center</code>.
     * @since 1.0.0
     * @author WFF
     */
    String getCssValue();

}
