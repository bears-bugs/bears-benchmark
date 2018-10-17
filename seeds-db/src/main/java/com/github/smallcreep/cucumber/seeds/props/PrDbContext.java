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

package com.github.smallcreep.cucumber.seeds.props;

import com.github.smallcreep.cucumber.seeds.Context;
import com.github.smallcreep.cucumber.seeds.Props;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Database property from context.
 * @since 0.1.1
 */
@EqualsAndHashCode(of = {"base", "ctx"})
@ToString(of = {"base", "ctx"})
public final class PrDbContext implements Props<String> {

    /**
     * Context.
     */
    private final Context ctx;

    /**
     * DataBase name.
     */
    private final String base;

    /**
     * Ctor.
     * @param ctx Context
     * @param name DataBase name
     */
    PrDbContext(final Context ctx, final String name) {
        this.ctx = ctx;
        this.base = name;
    }

    @Override
    public String property(final String name) {
        final String value;
        final String property = String.format(
            "cucumber.seeds.db.%s.%s",
            this.base,
            name
        );
        if (this.ctx.contains(property)) {
            value = (String) this.ctx.value(property);
        } else {
            value = (String) this.ctx.value(
                String.format(
                    "cucumber.seeds.db.%s",
                    name
                )
            );
        }
        return value;
    }

}
