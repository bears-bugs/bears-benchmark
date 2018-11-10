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
package com.webfirmframework.wffweb.interpreter;

import java.io.Serializable;

import com.webfirmframework.wffweb.NullValueException;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public abstract class CodeExecutor implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 1_0_0L;

    @SuppressWarnings("unused")
    private CodeExecutor() {
    }

    /**
     * @param base
     *            the base tag object, usually it is <code>this</code> (i.e.
     *            current object).
     */
    public CodeExecutor(final AbstractHtml base) {
        if (base == null) {
            throw new NullValueException("base cannot be null");
        }
        execute(base);
    }

    /**
     * To execute any piece of code.
     *
     * @param base
     *            the base tag object
     * @since 1.0.0
     * @author WFF
     */
    public abstract void execute(final AbstractHtml base);

}
