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
import java.util.Collection;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public abstract class ForEach<EACHTYPE> implements Serializable, Cloneable {

    private static final long serialVersionUID = 1_0_0L;

    @SuppressWarnings("unused")
    private ForEach() {
    }

    /**
     * @param base
     *            the parent object i.e. the outer tag, usually this (current
     *            object). This argument cannot be null.
     * @param values
     *            from which to do the iteration
     */
    public ForEach(final AbstractHtml base, final Collection<EACHTYPE> values) {
        if (base != null && values != null) {
            for (final EACHTYPE obj : values) {
                each(base, values, obj);
            }
        }
    }

    /**
     * @param base
     *            the parent object i.e. the outer tag, usually this (current
     *            object). This argument cannot be null.
     * @param values
     *            from which to do the iteration
     */
    @SafeVarargs
    public ForEach(final AbstractHtml base, final EACHTYPE... values) {
        if (base != null && values != null) {
            for (final EACHTYPE obj : values) {
                each(base, values, obj);
            }
        }
    }

    /**
     * invokes for each looping.
     *
     * @param base
     *            the base object given as the argument.
     * @param values
     *            the values object passed as a constructor argument. Type cast
     *            to the corresponding type.
     * @param eachValue
     *            each value from the given values passed in constructor.
     * @since 1.0.0
     * @author WFF
     */
    public abstract void each(final AbstractHtml base, final Object values,
            final EACHTYPE eachValue);

}
