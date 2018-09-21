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

import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.option.SpyDotProperties;
import io.opentracing.Scope;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;
import io.opentracing.util.ThreadLocalScopeManager;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.opentracing.contrib.p6spy.SpanChecker.checkSameTrace;
import static io.opentracing.contrib.p6spy.SpanChecker.checkTags;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class HibernateTest {

  private static final MockTracer mockTracer = new MockTracer(new ThreadLocalScopeManager(),
      MockTracer.Propagator.TEXT_MAP);

  @BeforeClass
  public static void init() {
    GlobalTracer.register(mockTracer);
  }

  @Before
  public void before() {
    mockTracer.reset();
  }

  @Test
  public void jpa() {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa");

    Employee employee = new Employee();
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(employee);
    entityManager.getTransaction().commit();
    entityManager.close();
    entityManagerFactory.close();

    assertNotNull(employee.id);

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(8, finishedSpans.size());
    checkTags(finishedSpans, "myservice", "jdbc:hsqldb:mem:jpa");
    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void jpaWithActiveSpanOnlyNoParent() {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa_active_span_only");

    Employee employee = new Employee();
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(employee);
    entityManager.getTransaction().commit();
    entityManager.close();
    entityManagerFactory.close();

    assertNotNull(employee.id);

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(0, finishedSpans.size());
    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void jpaWithActiveSpanOnlyWithParent() {
    try (Scope activeSpan = mockTracer.buildSpan("parent").startActive(true)) {
      EntityManagerFactory entityManagerFactory =
          Persistence.createEntityManagerFactory("jpa_active_span_only");

      Employee employee = new Employee();
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      entityManager.persist(employee);
      entityManager.getTransaction().commit();
      entityManager.close();
      entityManagerFactory.close();

      assertNotNull(employee.id);
    }

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(9, finishedSpans.size());
    checkSameTrace(finishedSpans);
    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void hibernate() {
    SessionFactory sessionFactory = createSessionFactory("");
    Session session = sessionFactory.openSession();

    Employee employee = new Employee();
    session.beginTransaction();
    session.save(employee);
    session.getTransaction().commit();
    session.close();
    sessionFactory.close();

    assertNotNull(employee.id);

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(8, finishedSpans.size());
    checkTags(finishedSpans, "myservice", "jdbc:hsqldb:mem:hibernate");
    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void withPeerNameInUrl() {
    SessionFactory sessionFactory = createSessionFactory(";tracingPeerService=inurl");
    Session session = sessionFactory.openSession();

    Employee employee = new Employee();
    session.beginTransaction();
    session.save(employee);
    session.getTransaction().commit();
    session.close();
    sessionFactory.close();

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(8, finishedSpans.size());

    checkTags(finishedSpans, "inurl", "jdbc:hsqldb:mem:hibernate;tracingPeerService=inurl");

    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void withActiveSpanOnlyNoParent() {
    SessionFactory sessionFactory = createSessionFactory(";traceWithActiveSpanOnly=true");
    Session session = sessionFactory.openSession();

    Employee employee = new Employee();
    session.beginTransaction();
    session.save(employee);
    session.getTransaction().commit();
    session.close();
    sessionFactory.close();

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(0, finishedSpans.size());

    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void withActiveSpanOnlyWithParent() {
    try (Scope activeSpan = mockTracer.buildSpan("parent").startActive(true)) {
      SessionFactory sessionFactory = createSessionFactory(";traceWithActiveSpanOnly=true");
      Session session = sessionFactory.openSession();

      Employee employee = new Employee();
      session.beginTransaction();
      session.save(employee);
      session.getTransaction().commit();
      session.close();
      sessionFactory.close();
    }

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(9, finishedSpans.size());
    checkSameTrace(finishedSpans);

    assertNull(mockTracer.scopeManager().active());

  }

  @Test
  public void withoutActiveSpanOnlyAndWithDefaultActiveSpanOnlyWithNoParent() throws Exception {
    try (AutoCloseable resetOptionsAfterTest = useActiveOnlyp6spyProperties()) {
      SessionFactory sessionFactory = createSessionFactory(";traceWithActiveSpanOnly=false");
      Session session = sessionFactory.openSession();

      Employee employee = new Employee();
      session.beginTransaction();
      session.save(employee);
      session.getTransaction().commit();
      session.close();
      sessionFactory.close();
    }

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(8, finishedSpans.size());

    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void withDefaultActiveSpanOnlyWithNoParent() throws Exception {
    try (AutoCloseable resetOptionsAfterTest = useActiveOnlyp6spyProperties()) {
      SessionFactory sessionFactory = createSessionFactory("");
      Session session = sessionFactory.openSession();

      Employee employee = new Employee();
      session.beginTransaction();
      session.save(employee);
      session.getTransaction().commit();
      session.close();
      sessionFactory.close();
    }

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(0, finishedSpans.size());

    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void withActiveSpanOnlyWithDefaultActiveSpanOnlyFalseWithNoParent() throws Exception {
    try (AutoCloseable resetOptionsAfterTest = useActiveOnlyFalsep6spyProperties()) {
      SessionFactory sessionFactory = createSessionFactory(";traceWithActiveSpanOnly=true");
      Session session = sessionFactory.openSession();

      Employee employee = new Employee();
      session.beginTransaction();
      session.save(employee);
      session.getTransaction().commit();
      session.close();
      sessionFactory.close();
    }

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(0, finishedSpans.size());

    assertNull(mockTracer.scopeManager().active());
  }

  @Test
  public void withoutAndWithActiveSpanOnlyAndWithDefaultActiveSpanOnlyWithNoParent() throws Exception {
    try (AutoCloseable resetOptionsAfterTest = useActiveOnlyp6spyProperties()) {
      SessionFactory sessionFactory = createSessionFactory(";traceWithActiveSpanOnly=false;traceWithActiveSpanOnly=true");
      Session session = sessionFactory.openSession();

      Employee employee = new Employee();
      session.beginTransaction();
      session.save(employee);
      session.getTransaction().commit();
      session.close();
      sessionFactory.close();
    }

    List<MockSpan> finishedSpans = mockTracer.finishedSpans();
    assertEquals(0, finishedSpans.size());

    assertNull(mockTracer.scopeManager().active());
  }

  private static AutoCloseable useActiveOnlyp6spyProperties() throws Exception {
    return usep6spyProperties("spy_active_only.properties");
  }

  private static AutoCloseable useActiveOnlyFalsep6spyProperties() throws Exception {
    return usep6spyProperties("spy_active_only_false.properties");
  }

  private static AutoCloseable usep6spyProperties(String fileName) throws Exception {
    final AutoCloseable resetp6spyOptions = new AutoCloseable() {
      @Override public void close() {
        System.clearProperty(SpyDotProperties.OPTIONS_FILE_PROPERTY);
        P6ModuleManager.getInstance().reload();
      }
    };
    try {
      System.setProperty(SpyDotProperties.OPTIONS_FILE_PROPERTY, fileName);
      P6ModuleManager.getInstance().reload();
    } catch (Exception e) {
      resetp6spyOptions.close();
      throw e;
    }
    return resetp6spyOptions;
  }

  private SessionFactory createSessionFactory(String options) {
    Configuration configuration = new Configuration();
    configuration.addAnnotatedClass(Employee.class);
    configuration.setProperty("hibernate.connection.url", "jdbc:p6spy:hsqldb:mem:hibernate" + options);
    configuration.setProperty("hibernate.connection.username", "sa");
    configuration.setProperty("hibernate.connection.password", "");
    configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    configuration.setProperty("hibernate.show_sql", "true");
    configuration.setProperty("hibernate.connection.pool_size", "10");

    StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
    SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
    return sessionFactory;
  }


  @Entity
  @Table(name = "Employee")
  public static class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
  }
}
