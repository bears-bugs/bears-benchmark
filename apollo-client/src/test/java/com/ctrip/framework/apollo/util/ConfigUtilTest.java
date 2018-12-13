package com.ctrip.framework.apollo.util;

import com.ctrip.framework.apollo.core.ConfigConsts;

import java.io.File;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ConfigUtilTest {
  @After
  public void tearDown() throws Exception {
    System.clearProperty(ConfigConsts.APOLLO_CLUSTER_KEY);
    System.clearProperty("apollo.connectTimeout");
    System.clearProperty("apollo.readTimeout");
    System.clearProperty("apollo.refreshInterval");
    System.clearProperty("apollo.loadConfigQPS");
    System.clearProperty("apollo.longPollQPS");
    System.clearProperty("apollo.configCacheSize");
    System.clearProperty("apollo.longPollingInitialDelayInMills");
    System.clearProperty("apollo.autoUpdateInjectedSpringProperties");
    System.clearProperty("apollo.cacheDir");
  }

  @Test
  public void testApolloCluster() throws Exception {
    String someCluster = "someCluster";
    System.setProperty(ConfigConsts.APOLLO_CLUSTER_KEY, someCluster);

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someCluster, configUtil.getCluster());
  }

  @Test
  public void testCustomizeConnectTimeout() throws Exception {
    int someConnectTimeout = 1;
    System.setProperty("apollo.connectTimeout", String.valueOf(someConnectTimeout));

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someConnectTimeout, configUtil.getConnectTimeout());
  }

  @Test
  public void testCustomizeInvalidConnectTimeout() throws Exception {
    String someInvalidConnectTimeout = "a";
    System.setProperty("apollo.connectTimeout", someInvalidConnectTimeout);

    ConfigUtil configUtil = new ConfigUtil();

    assertTrue(configUtil.getConnectTimeout() > 0);
  }

  @Test
  public void testCustomizeReadTimeout() throws Exception {
    int someReadTimeout = 1;
    System.setProperty("apollo.readTimeout", String.valueOf(someReadTimeout));

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someReadTimeout, configUtil.getReadTimeout());
  }

  @Test
  public void testCustomizeInvalidReadTimeout() throws Exception {
    String someInvalidReadTimeout = "a";
    System.setProperty("apollo.readTimeout", someInvalidReadTimeout);

    ConfigUtil configUtil = new ConfigUtil();

    assertTrue(configUtil.getReadTimeout() > 0);
  }

  @Test
  public void testCustomizeRefreshInterval() throws Exception {
    int someRefreshInterval = 1;
    System.setProperty("apollo.refreshInterval", String.valueOf(someRefreshInterval));

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someRefreshInterval, configUtil.getRefreshInterval());
  }

  @Test
  public void testCustomizeInvalidRefreshInterval() throws Exception {
    String someInvalidRefreshInterval = "a";
    System.setProperty("apollo.refreshInterval", someInvalidRefreshInterval);

    ConfigUtil configUtil = new ConfigUtil();

    assertTrue(configUtil.getRefreshInterval() > 0);
  }

  @Test
  public void testCustomizeLoadConfigQPS() throws Exception {
    int someQPS = 1;
    System.setProperty("apollo.loadConfigQPS", String.valueOf(someQPS));

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someQPS, configUtil.getLoadConfigQPS());
  }

  @Test
  public void testCustomizeInvalidLoadConfigQPS() throws Exception {
    String someInvalidQPS = "a";
    System.setProperty("apollo.loadConfigQPS", someInvalidQPS);

    ConfigUtil configUtil = new ConfigUtil();

    assertTrue(configUtil.getLoadConfigQPS() > 0);
  }

  @Test
  public void testCustomizeLongPollQPS() throws Exception {
    int someQPS = 1;
    System.setProperty("apollo.longPollQPS", String.valueOf(someQPS));

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someQPS, configUtil.getLongPollQPS());
  }

  @Test
  public void testCustomizeInvalidLongPollQPS() throws Exception {
    String someInvalidQPS = "a";
    System.setProperty("apollo.longPollQPS", someInvalidQPS);

    ConfigUtil configUtil = new ConfigUtil();

    assertTrue(configUtil.getLongPollQPS() > 0);
  }

  @Test
  public void testCustomizeMaxConfigCacheSize() throws Exception {
    long someCacheSize = 1;
    System.setProperty("apollo.configCacheSize", String.valueOf(someCacheSize));

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someCacheSize, configUtil.getMaxConfigCacheSize());
  }

  @Test
  public void testCustomizeInvalidMaxConfigCacheSize() throws Exception {
    String someInvalidCacheSize = "a";
    System.setProperty("apollo.configCacheSize", someInvalidCacheSize);

    ConfigUtil configUtil = new ConfigUtil();

    assertTrue(configUtil.getMaxConfigCacheSize() > 0);
  }

  @Test
  public void testCustomizeLongPollingInitialDelayInMills() throws Exception {
    long someLongPollingDelayInMills = 1;
    System.setProperty("apollo.longPollingInitialDelayInMills", String.valueOf(someLongPollingDelayInMills));

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someLongPollingDelayInMills, configUtil.getLongPollingInitialDelayInMills());
  }

  @Test
  public void testCustomizeInvalidLongPollingInitialDelayInMills() throws Exception {
    String someInvalidLongPollingDelayInMills = "a";
    System.setProperty("apollo.longPollingInitialDelayInMills", someInvalidLongPollingDelayInMills);

    ConfigUtil configUtil = new ConfigUtil();

    assertTrue(configUtil.getLongPollingInitialDelayInMills() > 0);
  }

  @Test
  public void testCustomizeAutoUpdateInjectedSpringProperties() throws Exception {
    boolean someAutoUpdateInjectedSpringProperties = false;
    System.setProperty("apollo.autoUpdateInjectedSpringProperties",
        String.valueOf(someAutoUpdateInjectedSpringProperties));

    ConfigUtil configUtil = new ConfigUtil();

    assertEquals(someAutoUpdateInjectedSpringProperties,
        configUtil.isAutoUpdateInjectedSpringPropertiesEnabled());
  }

  @Test
  public void testLocalCacheDirWithSystemProperty() throws Exception {
    String someCacheDir = "someCacheDir";
    String someAppId = "someAppId";

    System.setProperty("apollo.cacheDir", someCacheDir);

    ConfigUtil configUtil = spy(new ConfigUtil());

    doReturn(someAppId).when(configUtil).getAppId();

    assertEquals(someCacheDir + File.separator + someAppId, configUtil.getDefaultLocalCacheDir());
  }

  @Test
  public void testDefaultLocalCacheDir() throws Exception {
    String someAppId = "someAppId";

    ConfigUtil configUtil = spy(new ConfigUtil());

    doReturn(someAppId).when(configUtil).getAppId();

    doReturn(true).when(configUtil).isOSWindows();

    assertEquals("C:\\opt\\data\\" + someAppId, configUtil.getDefaultLocalCacheDir());

    doReturn(false).when(configUtil).isOSWindows();

    assertEquals("/opt/data/" + someAppId, configUtil.getDefaultLocalCacheDir());
  }
}
