package com.ctrip.framework.apollo;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ctrip.framework.apollo.build.MockInjector;
import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.utils.ClassLoaderUtil;
import com.ctrip.framework.apollo.internals.DefaultInjector;
import com.ctrip.framework.apollo.util.ConfigUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.gson.Gson;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public abstract class BaseIntegrationTest{
  private static final int PORT = findFreePort();
  private static final String metaServiceUrl = "http://localhost:" + PORT;
  private static final String someAppName = "someAppName";
  private static final String someInstanceId = "someInstanceId";
  private static final String configServiceURL = "http://localhost:" + PORT;
  protected static String someAppId;
  protected static String someClusterName;
  protected static String someDataCenter;
  protected static int refreshInterval;
  protected static TimeUnit refreshTimeUnit;
  private Server server;
  protected Gson gson = new Gson();

  @BeforeClass
  public static void beforeClass() throws Exception {
    File apolloEnvPropertiesFile = new File(ClassLoaderUtil.getClassPath(), "apollo-env.properties");
    Files.write("dev.meta=" + metaServiceUrl, apolloEnvPropertiesFile, Charsets.UTF_8);
    apolloEnvPropertiesFile.deleteOnExit();
  }

  @Before
  public void setUp() throws Exception {
    someAppId = "1003171";
    someClusterName = "someClusterName";
    someDataCenter = "someDC";
    refreshInterval = 5;
    refreshTimeUnit = TimeUnit.MINUTES;

    //as ConfigService is singleton, so we must manually clear its container
    ConfigService.reset();
    MockInjector.reset();
    MockInjector.setDelegate(new DefaultInjector());

    MockInjector.setInstance(ConfigUtil.class, new MockConfigUtil());
  }

  /**
   * init and start a jetty server, remember to call server.stop when the task is finished
   * @param handlers
   * @throws Exception
   */
  protected Server startServerWithHandlers(ContextHandler... handlers) throws Exception {
    server = new Server(PORT);

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(handlers);
    contexts.addHandler(mockMetaServerHandler());

    server.setHandler(contexts);
    server.start();

    return server;
  }

  @After
  public void tearDown() throws Exception {
    if (server != null && server.isStarted()) {
      server.stop();
    }
  }

  protected ContextHandler mockMetaServerHandler() {
    return mockMetaServerHandler(false);
  }

  protected ContextHandler mockMetaServerHandler(final boolean failedAtFirstTime) {
    final ServiceDTO someServiceDTO = new ServiceDTO();
    someServiceDTO.setAppName(someAppName);
    someServiceDTO.setInstanceId(someInstanceId);
    someServiceDTO.setHomepageUrl(configServiceURL);
    final AtomicInteger counter = new AtomicInteger(0);

    ContextHandler context = new ContextHandler("/services/config");
    context.setHandler(new AbstractHandler() {
      @Override
      public void handle(String target, Request baseRequest, HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        if (failedAtFirstTime && counter.incrementAndGet() == 1) {
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          baseRequest.setHandled(true);
          return;
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(gson.toJson(Lists.newArrayList(someServiceDTO)));

        baseRequest.setHandled(true);
      }
    });

    return context;
  }

  protected void setRefreshInterval(int refreshInterval) {
    BaseIntegrationTest.refreshInterval = refreshInterval;
  }

  protected void setRefreshTimeUnit(TimeUnit refreshTimeUnit) {
    BaseIntegrationTest.refreshTimeUnit = refreshTimeUnit;
  }

  public static class MockConfigUtil extends ConfigUtil {
    @Override
    public String getAppId() {
      return someAppId;
    }

    @Override
    public String getCluster() {
      return someClusterName;
    }

    @Override
    public int getRefreshInterval() {
      return refreshInterval;
    }

    @Override
    public TimeUnit getRefreshIntervalTimeUnit() {
      return refreshTimeUnit;
    }

    @Override
    public Env getApolloEnv() {
      return Env.DEV;
    }

    @Override
    public String getDataCenter() {
      return someDataCenter;
    }

    @Override
    public int getLoadConfigQPS() {
      return 200;
    }

    @Override
    public int getLongPollQPS() {
      return 200;
    }

    @Override
    public String getDefaultLocalCacheDir() {
      return ClassLoaderUtil.getClassPath();
    }

    @Override
    public long getOnErrorRetryInterval() {
      return 10;
    }

    @Override
    public TimeUnit getOnErrorRetryIntervalTimeUnit() {
      return TimeUnit.MILLISECONDS;
    }

    @Override
    public long getLongPollingInitialDelayInMills() {
      return 0;
    }
  }

  /**
   * Returns a free port number on localhost.
   *
   * Heavily inspired from org.eclipse.jdt.launching.SocketUtil (to avoid a dependency to JDT just because of this).
   * Slightly improved with close() missing in JDT. And throws exception instead of returning -1.
   *
   * @return a free port number on localhost
   * @throws IllegalStateException if unable to find a free port
   */
  private static int findFreePort() {
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(0);
      socket.setReuseAddress(true);
      int port = socket.getLocalPort();
      try {
        socket.close();
      } catch (IOException e) {
        // Ignore IOException on close()
      }
      return port;
    } catch (IOException e) {
    } finally {
      if (socket != null) {
        try {
          socket.close();
        } catch (IOException e) {
        }
      }
    }
    throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");
  }

}
