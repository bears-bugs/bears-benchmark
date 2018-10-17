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
import com.github.smallcreep.cucumber.seeds.suit.StSmart;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test Case for {@link PrDbsSuit}.
 * @since 0.1.1
 */
public final class PrDbsSuitTest {

    /**
     * Name property that contains all databases name.
     */
    private static final String DATABASES = "cucumber.seeds.db";

    /**
     * A rule for handling an exception.
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Check threw exception if property databases does not exists.
     */
    @Test
    public void checkExceptionIfNotExistDataBasesProperties() {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage(
            "Not found property cucumber.seeds.db"
        );
        new PrDbsSuit(
            new StSmart(
                new CxSimple()
            )
        ).property("first");
    }

    /**
     * Check threw exception if database with name does not exists.
     */
    @Test
    public void checkExceptionIfNotExistDataBase() {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage(
            "Not found dbs with name 'second' in property cucumber.seeds.db"
        );
        new PrDbsSuit(
            new StSmart(
                new CxSimple(
                    new MapOf<String, Object>(
                        new MapEntry<>(
                            PrDbsSuitTest.DATABASES,
                            "value,value2"
                        )
                    )
                )
            )
        ).property("second");
    }

    /**
     * Check return correct PrDbContext.
     */
    @Test
    public void checkReturnCorrectPrDbContext() {
        final CxSimple context = new CxSimple(
            new MapOf<String, Object>(
                new MapEntry<>(
                    PrDbsSuitTest.DATABASES,
                    "master,value2"
                )
            )
        );
        final String master = "master";
        MatcherAssert.assertThat(
            new PrDbsSuit(
                new StSmart(
                    context
                )
            ).property(master),
            CoreMatchers.equalTo(
                new PrDbContext(
                    context,
                    master
                )
            )
        );
    }
}
