/*
 * MIT License
 *
 * Copyright (c) 2018 Ilia Rogozhin (@smallcreep) <ilia.rogozhin@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.smallcreep.cucumber.seeds.context;

import com.github.smallcreep.cucumber.seeds.Context;
import java.util.HashMap;
import java.util.Map;
import javax.management.openmbean.KeyAlreadyExistsException;

/**
 * Simple implementation of {@link Context}.
 * This implementation use {@link HashMap} for store values.
 * @since 0.1.1
 */
public final class CxSimple implements Context {

    /**
     * Values map.
     */
    private final Map<String, Object> values;

    /**
     * Ctor.
     */
    public CxSimple() {
        this(new HashMap<>());
    }

    /**
     * Ctor.
     * @param values Values map
     */
    public CxSimple(final Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public Object value(final String key) {
        return this.values.get(key);
    }

    @Override
    public void add(final String key, final Object value) {
        if (this.contains(key)) {
            throw new KeyAlreadyExistsException(
                String.format(
                    "In scenario already exist key '%s'",
                    key
                )
            );
        }
        this.values.put(key, value);
    }

    @Override
    public boolean contains(final String key) {
        return this.values.containsKey(key);
    }
}
