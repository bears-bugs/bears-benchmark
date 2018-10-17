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

import com.github.smallcreep.cucumber.seeds.context.CxSimple;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Test Case for {@link PrDbContext}.
 * @since 0.1.1
 */
public final class PrDbContextTest {

    /**
     * Check Default value for property returned.
     */
    @Test
    public void checkDefaultValueProperty() {
        final String value = "default";
        MatcherAssert.assertThat(
            new PrDbContext(
                new CxSimple(
                    new MapOf<String, Object>(
                        new MapEntry<String, Object>(
                            "cucumber.seeds.db.user",
                            value
                        )
                    )
                ),
                "def"
            ).property("user"),
            CoreMatchers.equalTo(value)
        );
    }

    /**
     * Check value for property returned.
     */
    @Test
    public void checkValueProperty() {
        final String value = "value";
        MatcherAssert.assertThat(
            new PrDbContext(
                new CxSimple(
                    new MapOf<String, Object>(
                        new MapEntry<String, Object>(
                            "cucumber.seeds.db.master.password",
                            value
                        )
                    )
                ),
                "master"
            ).property("password"),
            CoreMatchers.equalTo(value)
        );
    }

    /**
     * Check {@link PrDbContext#equals(Object)} and
     * {@link PrDbContext#hashCode()}.
     */
    @Test
    public void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(PrDbContext.class).verify();
    }

    /**
     * Check {@link PrDbContext#toString()}.
     */
    @Test
    public void checkToString() {
        final CxSimple ctx = new CxSimple(
            new MapOf<String, Object>()
        );
        MatcherAssert.assertThat(
            new PrDbContext(
                ctx,
                "first"
            ).toString(),
            CoreMatchers.equalTo(
                String.format(
                    "PrDbContext(ctx=%s, base=first)",
                    ctx.toString()
                )
            )
        );
    }
}
