package com.ctrip.framework.apollo.integration;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.ctrip.framework.apollo.BaseIntegrationTest;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.dto.ApolloConfig;
import com.ctrip.framework.apollo.core.dto.ApolloConfigNotification;
import com.ctrip.framework.apollo.core.utils.ClassLoaderUtil;
import com.ctrip.framework.apollo.internals.RemoteConfigLongPollService;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.SettableFuture;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ConfigIntegrationTest extends BaseIntegrationTest {
  private String someReleaseKey;
  private File configDir;
  private String defaultNamespace;
  private String someOtherNamespace;
  private RemoteConfigLongPollService remoteConfigLongPollService;

  @Before
  public void setUp() throws Exception {
    super.setUp();

    defaultNamespace = ConfigConsts.NAMESPACE_APPLICATION;
    someOtherNamespace = "someOtherNamespace";
    someReleaseKey = "1";
    configDir = new File(ClassLoaderUtil.getClassPath() + "config-cache");
    if (configDir.exists()) {
      configDir.delete();
    }
    configDir.mkdirs();
    remoteConfigLongPollService = ApolloInjector.getInstance(RemoteConfigLongPollService.class);
  }

  @Override
  @After
  public void tearDown() throws Exception {
    ReflectionTestUtils.invokeMethod(remoteConfigLongPollService, "stopLongPollingRefresh");
    recursiveDelete(configDir);
    super.tearDown();
  }

  private void recursiveDelete(File file) {
    if (!file.exists()) {
      return;
    }
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        recursiveDelete(f);
      }
    }
    try {
      Files.deleteIfExists(file.toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testGetConfigWithNoLocalFileButWithRemoteConfig() throws Exception {
    String someKey = "someKey";
    String someValue = "someValue";
    String someNonExistedKey = "someNonExistedKey";
    String someDefaultValue = "someDefaultValue";
    ApolloConfig apolloConfig = assembleApolloConfig(ImmutableMap.of(someKey, someValue));
    ContextHandler handler = mockConfigServerHandler(HttpServletResponse.SC_OK, apolloConfig);
    startServerWithHandlers(handler);

    Config config = ConfigService.getAppConfig();

    assertEquals(someValue, config.getProperty(someKey, null));
    assertEquals(someDefaultValue, config.getProperty(someNonExistedKey, someDefaultValue));
  }

  @Test
  public void testGetConfigWithLocalFileAndWithRemoteConfig() throws Exception {
    String someKey = "someKey";
    String someValue = "someValue";
    String anotherValue = "anotherValue";
    Properties properties = new Properties();
    properties.put(someKey, someValue);
    createLocalCachePropertyFile(properties);

    ApolloConfig apolloConfig = assembleApolloConfig(ImmutableMap.of(someKey, anotherValue));
    ContextHandler handler = mockConfigServerHandler(HttpServletResponse.SC_OK, apolloConfig);
    startServerWithHandlers(handler);

    Config config = ConfigService.getAppConfig();

    assertEquals(anotherValue, config.getProperty(someKey, null));
  }

  @Test
  public void testGetConfigWithNoLocalFileAndRemoteConfigError() throws Exception {
    ContextHandler handler =
        mockConfigServerHandler(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
    startServerWithHandlers(handler);

    Config config = ConfigService.getAppConfig();

    String someKey = "someKey";
    String someDefaultValue = "defaultValue" + Math.random();

    assertEquals(someDefaultValue, config.getProperty(someKey, someDefaultValue));
  }

  @Test
  public void testGetConfigWithLocalFileAndRemoteConfigError() throws Exception {
    String someKey = "someKey";
    String someValue = "someValue";
    Properties properties = new Properties();
    properties.put(someKey, someValue);
    createLocalCachePropertyFile(properties);

    ContextHandler handler =
        mockConfigServerHandler(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
    startServerWithHandlers(handler);

    Config config = ConfigService.getAppConfig();
    assertEquals(someValue, config.getProperty(someKey, null));
  }

  @Test
  public void testGetConfigWithNoLocalFileAndRemoteMetaServiceRetry() throws Exception {
    String someKey = "someKey";
    String someValue = "someValue";
    ApolloConfig apolloConfig = assembleApolloConfig(ImmutableMap.of(someKey, someValue));
    ContextHandler configHandler = mockConfigServerHandler(HttpServletResponse.SC_OK, apolloConfig);
    boolean failAtFirstTime = true;
    ContextHandler metaServerHandler = mockMetaServerHandler(failAtFirstTime);
    startServerWithHandlers(metaServerHandler, configHandler);

    Config config = ConfigService.getAppConfig();

    assertEquals(someValue, config.getProperty(someKey, null));
  }

  @Test
  public void testGetConfigWithNoLocalFileAndRemoteConfigServiceRetry() throws Exception {
    String someKey = "someKey";
    String someValue = "someValue";
    ApolloConfig apolloConfig = assembleApolloConfig(ImmutableMap.of(someKey, someValue));
    boolean failedAtFirstTime = true;
    ContextHandler handler =
        mockConfigServerHandler(HttpServletResponse.SC_OK, apolloConfig, failedAtFirstTime);
    startServerWithHandlers(handler);

    Config config = ConfigService.getAppConfig();

    assertEquals(someValue, config.getProperty(someKey, null));
  }

  @Test
  public void testRefreshConfig() throws Exception {
    final String someKey = "someKey";
    final String someValue = "someValue";
    final String anotherValue = "anotherValue";

    int someRefreshInterval = 500;
    TimeUnit someRefreshTimeUnit = TimeUnit.MILLISECONDS;

    setRefreshInterval(someRefreshInterval);
    setRefreshTimeUnit(someRefreshTimeUnit);

    Map<String, String> configurations = Maps.newHashMap();
    configurations.put(someKey, someValue);
    ApolloConfig apolloConfig = assembleApolloConfig(configurations);
    ContextHandler handler = mockConfigServerHandler(HttpServletResponse.SC_OK, apolloConfig);
    startServerWithHandlers(handler);

    Config config = ConfigService.getAppConfig();
    final List<ConfigChangeEvent> changeEvents = Lists.newArrayList();

    final SettableFuture<Boolean> refreshFinished = SettableFuture.create();
    config.addChangeListener(new ConfigChangeListener() {
      AtomicInteger counter = new AtomicInteger(0);

      @Override
      public void onChange(ConfigChangeEvent changeEvent) {
        //only need to assert once
        if (counter.incrementAndGet() > 1) {
          return;
        }
        assertEquals(1, changeEvent.changedKeys().size());
        assertTrue(changeEvent.isChanged(someKey));
        assertEquals(someValue, changeEvent.getChange(someKey).getOldValue());
        assertEquals(anotherValue, changeEvent.getChange(someKey).getNewValue());
        // if there is any assertion failed above, this line won't be executed
        changeEvents.add(changeEvent);
        refreshFinished.set(true);
      }
    });

    apolloConfig.getConfigurations().put(someKey, anotherValue);

    refreshFinished.get(someRefreshInterval * 5, someRefreshTimeUnit);

    assertThat(
        "Change event's size should equal to one or there must be some assertion failed in change listener",
        1, equalTo(changeEvents.size()));
    assertEquals(anotherValue, config.getProperty(someKey, null));
  }

  @Test
  public void testLongPollRefresh() throws Exception {
    final String someKey = "someKey";
    final String someValue = "someValue";
    final String anotherValue = "anotherValue";
    long someNotificationId = 1;

    long pollTimeoutInMS = 50;
    Map<String, String> configurations = Maps.newHashMap();
    configurations.put(someKey, someValue);
    ApolloConfig apolloConfig = assembleApolloConfig(configurations);
    ContextHandler configHandler = mockConfigServerHandler(HttpServletResponse.SC_OK, apolloConfig);
    ContextHandler pollHandler =
        mockPollNotificationHandler(pollTimeoutInMS, HttpServletResponse.SC_OK,
            Lists.newArrayList(
                new ApolloConfigNotification(apolloConfig.getNamespaceName(), someNotificationId)),
            false);

    startServerWithHandlers(configHandler, pollHandler);

    Config config = ConfigService.getAppConfig();
    assertEquals(someValue, config.getProperty(someKey, null));

    final SettableFuture<Boolean> longPollFinished = SettableFuture.create();

    config.addChangeListener(new ConfigChangeListener() {
      @Override
      public void onChange(ConfigChangeEvent changeEvent) {
        longPollFinished.set(true);
      }
    });

    apolloConfig.getConfigurations().put(someKey, anotherValue);

    longPollFinished.get(pollTimeoutInMS * 20, TimeUnit.MILLISECONDS);

    assertEquals(anotherValue, config.getProperty(someKey, null));
  }

  @Test
  public void testLongPollRefreshWithMultipleNamespacesAndOnlyOneNamespaceNotified() throws Exception {
    final String someKey = "someKey";
    final String someValue = "someValue";
    final String anotherValue = "anotherValue";
    long someNotificationId = 1;

    long pollTimeoutInMS = 50;
    Map<String, String> configurations = Maps.newHashMap();
    configurations.put(someKey, someValue);
    ApolloConfig apolloConfig = assembleApolloConfig(configurations);
    ContextHandler configHandler = mockConfigServerHandler(HttpServletResponse.SC_OK, apolloConfig);
    ContextHandler pollHandler =
        mockPollNotificationHandler(pollTimeoutInMS, HttpServletResponse.SC_OK,
            Lists.newArrayList(
                new ApolloConfigNotification(apolloConfig.getNamespaceName(), someNotificationId)),
            false);

    startServerWithHandlers(configHandler, pollHandler);

    Config someOtherConfig = ConfigService.getConfig(someOtherNamespace);
    Config config = ConfigService.getAppConfig();
    assertEquals(someValue, config.getProperty(someKey, null));
    assertEquals(someValue, someOtherConfig.getProperty(someKey, null));

    final SettableFuture<Boolean> longPollFinished = SettableFuture.create();

    config.addChangeListener(new ConfigChangeListener() {
      @Override
      public void onChange(ConfigChangeEvent changeEvent) {
        longPollFinished.set(true);
      }
    });

    apolloConfig.getConfigurations().put(someKey, anotherValue);

    longPollFinished.get(5000, TimeUnit.MILLISECONDS);

    assertEquals(anotherValue, config.getProperty(someKey, null));

    TimeUnit.MILLISECONDS.sleep(pollTimeoutInMS * 10);
    assertEquals(someValue, someOtherConfig.getProperty(someKey, null));
  }

  @Test
  public void testLongPollRefreshWithMultipleNamespacesAndMultipleNamespaceNotified() throws Exception {
    final String someKey = "someKey";
    final String someValue = "someValue";
    final String anotherValue = "anotherValue";
    long someNotificationId = 1;

    long pollTimeoutInMS = 50;
    Map<String, String> configurations = Maps.newHashMap();
    configurations.put(someKey, someValue);
    ApolloConfig apolloConfig = assembleApolloConfig(configurations);
    ContextHandler configHandler = mockConfigServerHandler(HttpServletResponse.SC_OK, apolloConfig);
    ContextHandler pollHandler =
        mockPollNotificationHandler(pollTimeoutInMS, HttpServletResponse.SC_OK,
            Lists.newArrayList(
                new ApolloConfigNotification(apolloConfig.getNamespaceName(), someNotificationId),
                new ApolloConfigNotification(someOtherNamespace, someNotificationId)),
            false);

    startServerWithHandlers(configHandler, pollHandler);

    Config config = ConfigService.getAppConfig();
    Config someOtherConfig = ConfigService.getConfig(someOtherNamespace);
    assertEquals(someValue, config.getProperty(someKey, null));
    assertEquals(someValue, someOtherConfig.getProperty(someKey, null));

    final SettableFuture<Boolean> longPollFinished = SettableFuture.create();
    final SettableFuture<Boolean> someOtherNamespacelongPollFinished = SettableFuture.create();

    config.addChangeListener(new ConfigChangeListener() {
      @Override
      public void onChange(ConfigChangeEvent changeEvent) {
        longPollFinished.set(true);
      }
    });
    someOtherConfig.addChangeListener(new ConfigChangeListener() {
      @Override
      public void onChange(ConfigChangeEvent changeEvent) {
        someOtherNamespacelongPollFinished.set(true);
      }
    });

    apolloConfig.getConfigurations().put(someKey, anotherValue);

    longPollFinished.get(5000, TimeUnit.MILLISECONDS);
    someOtherNamespacelongPollFinished.get(5000, TimeUnit.MILLISECONDS);

    assertEquals(anotherValue, config.getProperty(someKey, null));
    assertEquals(anotherValue, someOtherConfig.getProperty(someKey, null));

  }

  private ContextHandler mockPollNotificationHandler(final long pollResultTimeOutInMS,
                                                     final int statusCode,
                                                     final List<ApolloConfigNotification> result,
                                                     final boolean failedAtFirstTime) {
    ContextHandler context = new ContextHandler("/notifications/v2");
    context.setHandler(new AbstractHandler() {
      AtomicInteger counter = new AtomicInteger(0);

      @Override
      public void handle(String target, Request baseRequest, HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        if (failedAtFirstTime && counter.incrementAndGet() == 1) {
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          baseRequest.setHandled(true);
          return;
        }

        try {
          TimeUnit.MILLISECONDS.sleep(pollResultTimeOutInMS);
        } catch (InterruptedException e) {
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        response.getWriter().println(gson.toJson(result));
        baseRequest.setHandled(true);
      }
    });

    return context;
  }

  private ContextHandler mockConfigServerHandler(final int statusCode, final ApolloConfig result,
                                                 final boolean failedAtFirstTime) {
    ContextHandler context = new ContextHandler("/configs/*");
    context.setHandler(new AbstractHandler() {
      AtomicInteger counter = new AtomicInteger(0);

      @Override
      public void handle(String target, Request baseRequest, HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        if (failedAtFirstTime && counter.incrementAndGet() == 1) {
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          baseRequest.setHandled(true);
          return;
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        response.getWriter().println(gson.toJson(result));
        baseRequest.setHandled(true);
      }
    });
    return context;
  }


  private ContextHandler mockConfigServerHandler(int statusCode, ApolloConfig result) {
    return mockConfigServerHandler(statusCode, result, false);
  }

  private ApolloConfig assembleApolloConfig(Map<String, String> configurations) {
    ApolloConfig apolloConfig =
        new ApolloConfig(someAppId, someClusterName, defaultNamespace, someReleaseKey);

    apolloConfig.setConfigurations(configurations);

    return apolloConfig;
  }

  private File createLocalCachePropertyFile(Properties properties) throws IOException {
    File file = new File(configDir, assembleLocalCacheFileName());
    FileOutputStream in = null;
    try {
      in = new FileOutputStream(file);
      properties.store(in, "Persisted by ConfigIntegrationTest");
    } finally {
      if (in != null) {
        in.close();
      }
    }
    return file;
  }

  private String assembleLocalCacheFileName() {
    return String.format("%s.properties", Joiner.on(ConfigConsts.CLUSTER_NAMESPACE_SEPARATOR)
        .join(someAppId, someClusterName, defaultNamespace));
  }
}
