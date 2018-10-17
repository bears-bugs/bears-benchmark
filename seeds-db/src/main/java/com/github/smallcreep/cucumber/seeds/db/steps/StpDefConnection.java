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
package com.github.smallcreep.cucumber.seeds.db.steps;

import com.github.smallcreep.cucumber.seeds.DataBases;
import com.github.smallcreep.cucumber.seeds.Suit;
import com.github.smallcreep.cucumber.seeds.suit.StSmart;
import cucumber.api.java.en.Given;
import java.sql.SQLException;

/**
 * Steps connection to the DB.
 * @since 0.1.1
 */
public final class StpDefConnection {

    /**
     * Current Suit.
     */
    private final Suit suit;

    /**
     * Ctor.
     */
    public StpDefConnection() {
        this(StSmart.instance());
    }

    /**
     * Ctor.
     * @param suit Current Suit
     */
    StpDefConnection(final Suit suit) {
        this.suit = suit;
    }

    /**
     * Connect to the database with alias.
     * @param alias Database alias
     * @throws SQLException If any error of connection
     */
    @Given("^The connection to the ([^,]+) database$")
    public void connect(final String alias) throws SQLException {
        ((DataBases) this.suit
            .scenario()
            .context()
            .value("databases")).database(alias).connect();
    }
}
