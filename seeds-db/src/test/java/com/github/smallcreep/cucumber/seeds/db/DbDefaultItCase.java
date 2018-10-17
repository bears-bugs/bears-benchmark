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

import com.jcabi.jdbc.JdbcSession;
import com.jolbox.bonecp.BoneCPDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Case for {@link DbDefault}.
 * @since 0.1.1
 */
public final class DbDefaultItCase {

    /**
     * Data Source.
     */
    private final DataSource src;

    /**
     * Ctor.
     */
    public DbDefaultItCase() {
        this(new BoneCPDataSource());
    }

    /**
     * Ctor.
     * @param src Data Source
     */
    private DbDefaultItCase(final DataSource src) {
        this.src = src;
    }

    /**
     * SetUp Data Source.
     */
    @Before
    public void setUp() {
        ((BoneCPDataSource) this.src).setDriverClass("org.postgresql.Driver");
        ((BoneCPDataSource) this.src).setJdbcUrl(
            System.getProperty("jdbs.url.postgres")
        );
        ((BoneCPDataSource) this.src).setUser("testuser");
        ((BoneCPDataSource) this.src).setPassword("mysecret");
    }

    /**
     * Check connection correct.
     * @todo #33:30m/DEV Move this test to integration tests.
     *  Need startup any Database (ex. Postgres) before run integration test.
     *  We can start DB in docker, for running docker images we can use
     *  fabric8io/docker-maven-plugin.
     *  For more info about this plugin go to the link
     *  http://dmp.fabric8.io/
     * @throws SQLException If any error of connection
     */
    @Test
    public void checkConnection() throws SQLException {
        new DbDefault(
            new JdbcSession(this.src)
        ).connect();
    }
}
