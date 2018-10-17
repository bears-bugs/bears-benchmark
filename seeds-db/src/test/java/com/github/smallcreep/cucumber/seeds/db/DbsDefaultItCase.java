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

package com.github.smallcreep.cucumber.seeds.db;

import com.github.smallcreep.cucumber.seeds.Props;
import com.github.smallcreep.cucumber.seeds.props.PrMap;
import java.sql.SQLException;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.junit.Test;

/**
 * Integration Test Case for {@link DbsDefault}.
 * @since 0.1.1
 */
public final class DbsDefaultItCase {

    /**
     * Check correct database created.
     * @throws SQLException If connect exception
     * @checkstyle NonStaticMethodCheck (50 lines)
     */
    @Test
    public void checkCorrectDatabaseCreated() throws SQLException {
        final String name = "master";
        new DbsDefault(
            new PrMap<>(
                new MapOf<String, Props<String>>(
                    new MapEntry<>(
                        name,
                        new PrMap<>(
                            new MapOf<String, String>(
                                new MapEntry<>(
                                    "jdbs.driver", "org.postgresql.Driver"
                                ),
                                new MapEntry<>(
                                    "jdbs.url",
                                    System.getProperty("jdbs.url.postgres")
                                ),
                                new MapEntry<>(
                                    "user", "testuser"
                                ),
                                new MapEntry<>(
                                    "password", "mysecret"
                                )
                            )
                        )
                    )
                )
            )
        ).database(name).connect();
    }
}
