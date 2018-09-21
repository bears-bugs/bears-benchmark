/*
 * Copyright 2017-2018 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.opentracing.contrib.p6spy;

import io.opentracing.Scope;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracerTestUtil;
import io.opentracing.util.ThreadLocalScopeManager;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static io.opentracing.contrib.p6spy.SpanChecker.checkSameTrace;
import static io.opentracing.contrib.p6spy.SpanChecker.checkTags;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SpringTest {

  private static final MockTracer mockTracer = new MockTracer(new ThreadLocalScopeManager(),
      MockTracer.Propagator.TEXT_MAP);

  @BeforeClass
  public static void init() {
    GlobalTracerTestUtil.setGlobalTracerUnconditionally(mockTracer);
  }

  @Before
  public void before() throws Exception {
    mockTracer.reset();
  }

  @Test
  public void test() throws SQLException {
    BasicDataSource dataSource = getDataSource("");

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.execute("CREATE TABLE employee (id INTEGER)");

    dataSource.close();

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(1, finishedSpans.size());
    checkTags(finishedSpans, "myservice", "jdbc:hsqldb:mem:spring");
    checkSameTrace(finishedSpans);

    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void testWithSpanOnlyNoParent() throws SQLException {
    BasicDataSource dataSource = getDataSource(";traceWithActiveSpanOnly=true");

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.execute("CREATE TABLE skip_new_spans (id INTEGER)");

    dataSource.close();

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(0, finishedSpans.size());

    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void testWithSpanOnlyWithParent() throws SQLException {
    try (Scope activeSpan = mockTracer.buildSpan("parent").startActive(true)) {
      BasicDataSource dataSource = getDataSource(";traceWithActiveSpanOnly=true");

      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      jdbcTemplate.execute("CREATE TABLE with_parent_skip (id INTEGER)");

      dataSource.close();
    }

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(2, finishedSpans.size());
    checkSameTrace(finishedSpans);
    assertNull(mockTracer.scopeManager().active());
  }

  private BasicDataSource getDataSource(String options) {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setUrl("jdbc:p6spy:hsqldb:mem:spring" + options);
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    return dataSource;
  }

}
