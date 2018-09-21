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

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.StatementInformation;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracerTestUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdbcTest {

  private static final MockTracer mockTracer = new MockTracer();

  @BeforeClass
  public static void init() {
    GlobalTracerTestUtil.setGlobalTracerUnconditionally(mockTracer);
  }

  @Before
  public void before() {
    mockTracer.reset();
  }

  @Test
  public void should_report_one_span() throws Exception {
    Connection connection = DriverManager.getConnection("jdbc:p6spy:hsqldb:mem:jdbc");
    Statement statement = connection.createStatement();
    statement.executeUpdate("CREATE TABLE employer (id INTEGER)");
    connection.close();
    List<MockSpan> spans = mockTracer.finishedSpans();
    assertEquals(1, spans.size());
  }

  @Test
  public void should_report_one_span_with_null_url() throws Exception {
    final Connection connection = createConnection();
    Mockito.when(connection.getMetaData().getURL()).thenReturn(null);
    simulateExecuteQuery(connection);

    List<MockSpan> spans = mockTracer.finishedSpans();
    assertEquals(1, spans.size());
    final MockSpan span = spans.get(0);
    assertNull(span.tags().get("peer.address"));
    assertNull(span.tags().get(Tags.DB_TYPE.getKey()));
  }

  @Test
  public void should_report_one_span_with_null_user() throws Exception {
    final Connection connection = createConnection();
    Mockito.when(connection.getMetaData().getUserName()).thenReturn(null);
    simulateExecuteQuery(connection);

    List<MockSpan> spans = mockTracer.finishedSpans();
    assertEquals(1, spans.size());
    final MockSpan span = spans.get(0);
    assertNull(span.tags().get(Tags.DB_USER.getKey()));
  }

  @Test
  public void should_report_one_span_with_null_catalog() throws Exception {
    final Connection connection = createConnection();
    Mockito.when(connection.getCatalog()).thenReturn(null);
    simulateExecuteQuery(connection);

    List<MockSpan> spans = mockTracer.finishedSpans();
    assertEquals(1, spans.size());
    final MockSpan span = spans.get(0);
    assertNull(span.tags().get(Tags.DB_INSTANCE.getKey()));
  }

  @Test
  public void should_not_report_span_with_sql_exception() throws Exception {
    final Connection connection = createConnection();
    Mockito.when(connection.getCatalog()).thenThrow(new SQLException());
    simulateExecuteQuery(connection);

    List<MockSpan> spans = mockTracer.finishedSpans();
    assertEquals(0, spans.size());
  }

  private static Connection createConnection() throws SQLException {
    final Connection connection = DriverManager.getConnection("jdbc:p6spy:hsqldb:mem:jdbc");
    return deepDelegates(connection);
  }

  private static void simulateExecuteQuery(Connection connection) {
    final StatementInformation statementInformation = new StatementInformation(ConnectionInformation.fromTestConnection(connection));
    final TracingP6SpyListener p6SpyListener = new TracingP6SpyListener("", false);
    p6SpyListener.onBeforeAnyExecute(statementInformation);
    p6SpyListener.onAfterAnyExecute(statementInformation, 1, null);
  }

  private static <T> T deepDelegates(final T t) {
    final T mocked =
        (T) Mockito.mock(t.getClass(), new Answer() {
          Map<InvocationOnMock, Object> memoizedCalls = new HashMap<>();

          @Override public Object answer(InvocationOnMock invocation) throws Throwable {
            final Object memoizedReturnedValue = memoizedCalls.get(invocation);
            if (memoizedReturnedValue != null) {
              return memoizedReturnedValue;
            }
            Object returnedValue = invocation.getMethod().invoke(t, invocation.getArguments());
            if (returnedValue != null) {
              try {
                returnedValue = deepDelegates(returnedValue);
              } catch (Throwable e){}
            }
            memoizedCalls.put(invocation, returnedValue);
            return returnedValue;
          }
        });
    return mocked;
  }
}
