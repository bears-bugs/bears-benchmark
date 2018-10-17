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

import com.github.smallcreep.cucumber.seeds.DataBase;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import java.sql.SQLException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

/**
 * Default implementation of {@link DataBase}.
 * Connect method request `SELECT 1;` from database.
 * @since 0.1.1
 */
public final class DbDefault implements DataBase {

    /**
     * Session.
     */
    private final JdbcSession session;

    /**
     * Ctor.
     * @param session JDBC session
     */
    DbDefault(final JdbcSession session) {
        this.session = session;
    }

    @Override
    public void connect() throws SQLException {
        MatcherAssert.assertThat(
            "Connection to DB response not expected",
            this.session.sql("SELECT 1;").select(
                new SingleOutcome<>(Long.class)
            ),
            CoreMatchers.equalTo(1L)
        );
    }
}
