package com.ctrip.framework.apollo.configservice.service.config;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ctrip.framework.apollo.biz.entity.Release;
import com.ctrip.framework.apollo.biz.grayReleaseRule.GrayReleaseRulesHolder;
import com.ctrip.framework.apollo.biz.service.ReleaseService;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.dto.ApolloNotificationMessages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultConfigServiceTest {
  private DefaultConfigService configService;
  private String someClientAppId;
  private String someConfigAppId;
  private String someClusterName;
  private String defaultClusterName;
  private String defaultNamespaceName;
  private String someDataCenter;
  private String someClientIp;
  @Mock
  private ApolloNotificationMessages someNotificationMessages;
  @Mock
  private ReleaseService releaseService;
  @Mock
  private GrayReleaseRulesHolder grayReleaseRulesHolder;

  @Mock
  private Release someRelease;

  @Before
  public void setUp() throws Exception {
    configService = new DefaultConfigService();
    ReflectionTestUtils.setField(configService, "releaseService", releaseService);
    ReflectionTestUtils.setField(configService, "grayReleaseRulesHolder", grayReleaseRulesHolder);

    someClientAppId = "1234";
    someConfigAppId = "1";
    someClusterName = "someClusterName";
    defaultClusterName = ConfigConsts.CLUSTER_NAME_DEFAULT;
    defaultNamespaceName = ConfigConsts.NAMESPACE_APPLICATION;
    someDataCenter = "someDC";
    someClientIp = "someClientIp";

    when(grayReleaseRulesHolder.findReleaseIdFromGrayReleaseRule(anyString(), anyString(),
        anyString(), anyString(), anyString())).thenReturn(null);
  }

  @Test
  public void testLoadConfig() throws Exception {
    when(releaseService.findLatestActiveRelease(someConfigAppId, someClusterName, defaultNamespaceName))
        .thenReturn(someRelease);

    Release release = configService
        .loadConfig(someClientAppId, someClientIp, someConfigAppId, someClusterName, defaultNamespaceName, someDataCenter,
            someNotificationMessages);

    verify(releaseService, times(1)).findLatestActiveRelease(someConfigAppId, someClusterName, defaultNamespaceName);

    assertEquals(someRelease, release);
  }

  @Test
  public void testLoadConfigWithGrayRelease() throws Exception {
    Release grayRelease = mock(Release.class);
    long grayReleaseId = 999;

    when(grayReleaseRulesHolder.findReleaseIdFromGrayReleaseRule(someClientAppId, someClientIp,
        someConfigAppId, someClusterName, defaultNamespaceName)).thenReturn(grayReleaseId);
    when(releaseService.findActiveOne(grayReleaseId)).thenReturn(grayRelease);
    when(releaseService.findLatestActiveRelease(someConfigAppId, someClusterName, defaultNamespaceName))
        .thenReturn(someRelease);

    Release release = configService
        .loadConfig(someClientAppId, someClientIp, someConfigAppId, someClusterName, defaultNamespaceName, someDataCenter,
            someNotificationMessages);

    verify(releaseService, times(1)).findActiveOne(grayReleaseId);
    verify(releaseService, never()).findLatestActiveRelease(someConfigAppId, someClusterName, defaultNamespaceName);

    assertEquals(grayRelease, release);
  }

  @Test
  public void testLoadConfigWithReleaseNotFound() throws Exception {
    when(releaseService.findLatestActiveRelease(someConfigAppId, someClusterName, defaultNamespaceName))
        .thenReturn(null);

    Release release = configService
        .loadConfig(someClientAppId, someClientIp, someConfigAppId, someClusterName, defaultNamespaceName, someDataCenter,
            someNotificationMessages);

    assertNull(release);
  }

  @Test
  public void testLoadConfigWithDefaultClusterWithDataCenterRelease() throws Exception {
    when(releaseService.findLatestActiveRelease(someConfigAppId, someDataCenter, defaultNamespaceName))
        .thenReturn(someRelease);

    Release release = configService
        .loadConfig(someClientAppId, someClientIp, someConfigAppId, defaultClusterName, defaultNamespaceName, someDataCenter,
            someNotificationMessages);

    verify(releaseService, times(1)).findLatestActiveRelease(someConfigAppId, someDataCenter, defaultNamespaceName);

    assertEquals(someRelease, release);
  }

  @Test
  public void testLoadConfigWithDefaultClusterWithNoDataCenterRelease() throws Exception {
    when(releaseService.findLatestActiveRelease(someConfigAppId, someDataCenter, defaultNamespaceName))
        .thenReturn(null);
    when(releaseService.findLatestActiveRelease(someConfigAppId, defaultClusterName, defaultNamespaceName))
        .thenReturn(someRelease);

    Release release = configService
        .loadConfig(someClientAppId, someClientIp, someConfigAppId, defaultClusterName, defaultNamespaceName, someDataCenter,
            someNotificationMessages);

    verify(releaseService, times(1)).findLatestActiveRelease(someConfigAppId, someDataCenter, defaultNamespaceName);
    verify(releaseService, times(1))
        .findLatestActiveRelease(someConfigAppId, defaultClusterName, defaultNamespaceName);

    assertEquals(someRelease, release);
  }
}
