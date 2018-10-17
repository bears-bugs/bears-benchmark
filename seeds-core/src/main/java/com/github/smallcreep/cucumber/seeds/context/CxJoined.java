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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.management.openmbean.KeyAlreadyExistsException;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Joined;

/**
 * Joined context.
 * @since 0.1.1
 */
public final class CxJoined implements Context {

    /**
     * Contexts.
     */
    private final Iterable<Context> contexts;

    /**
     * Extra context.
     */
    private final Context extra;

    /**
     * Ctor.
     * @param ctx Context
     */
    public CxJoined(final Context... ctx) {
        this(
            new IterableOf<>(
                ctx
            )
        );
    }

    /**
     * Ctor.
     * @param contexts Contexts
     */
    public CxJoined(final Iterable<Context> contexts) {
        this(contexts, new CxSimple());
    }

    /**
     * Ctor.
     * @param contexts Contexts
     * @param extra Extra context
     */
    CxJoined(
        final Iterable<Context> contexts,
        final Context extra
    ) {
        this.contexts = contexts;
        this.extra = extra;
    }

    @Override
    public Object value(final String key) {
        final AtomicReference<Object> result = new AtomicReference<>();
        new Joined<>(
            this.extra,
            this.contexts
        ).forEach(
            context -> {
                if (context.contains(key) && result.get() == null) {
                    result.set(context.value(key));
                }
            }
        );
        return result.get();
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
        this.extra.add(key, value);
    }

    @Override
    public boolean contains(final String key) {
        final AtomicBoolean result = new AtomicBoolean(false);
        new Joined<>(
            this.extra,
            this.contexts
        ).forEach(
            context -> {
                if (context.contains(key) && !result.get()) {
                    result.set(true);
                }
            }
        );
        return result.get();
    }
}
