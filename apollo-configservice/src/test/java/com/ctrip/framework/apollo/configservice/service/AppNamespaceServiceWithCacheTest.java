package com.ctrip.framework.apollo.configservice.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.ctrip.framework.apollo.biz.config.BizConfig;
import com.ctrip.framework.apollo.biz.repository.AppNamespaceRepository;
import com.ctrip.framework.apollo.common.entity.AppNamespace;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class AppNamespaceServiceWithCacheTest {
  private AppNamespaceServiceWithCache appNamespaceServiceWithCache;
  @Mock
  private AppNamespaceRepository appNamespaceRepository;

  @Mock
  private BizConfig bizConfig;

  private int scanInterval;
  private TimeUnit scanIntervalTimeUnit;
  private Comparator<AppNamespace> appNamespaceComparator = (o1, o2) -> (int) (o1.getId() -
      o2.getId());

  @Before
  public void setUp() throws Exception {
    appNamespaceServiceWithCache = new AppNamespaceServiceWithCache();
    ReflectionTestUtils.setField(appNamespaceServiceWithCache, "appNamespaceRepository",
        appNamespaceRepository);
    ReflectionTestUtils.setField(appNamespaceServiceWithCache, "bizConfig", bizConfig);

    scanInterval = 50;
    scanIntervalTimeUnit = TimeUnit.MILLISECONDS;
    when(bizConfig.appNamespaceCacheRebuildInterval()).thenReturn(scanInterval);
    when(bizConfig.appNamespaceCacheRebuildIntervalTimeUnit()).thenReturn(scanIntervalTimeUnit);
    when(bizConfig.appNamespaceCacheScanInterval()).thenReturn(scanInterval);
    when(bizConfig.appNamespaceCacheScanIntervalTimeUnit()).thenReturn(scanIntervalTimeUnit);
  }

  @Test
  public void testAppNamespace() throws Exception {
    String someAppId = "someAppId";
    String somePrivateNamespace = "somePrivateNamespace";
    String somePrivateNamespaceWithIncorrectCase = somePrivateNamespace.toUpperCase();
    long somePrivateNamespaceId = 1;
    String yetAnotherPrivateNamespace = "anotherPrivateNamespace";
    long yetAnotherPrivateNamespaceId = 4;
    String anotherPublicNamespace = "anotherPublicNamespace";
    long anotherPublicNamespaceId = 5;

    String somePublicAppId = "somePublicAppId";
    String somePublicNamespace = "somePublicNamespace";
    String somePublicNamespaceWithIncorrectCase = somePublicNamespace.toUpperCase();
    long somePublicNamespaceId = 2;
    String anotherPrivateNamespace = "anotherPrivateNamespace";
    long anotherPrivateNamespaceId = 3;

    int sleepInterval = scanInterval * 10;

    AppNamespace somePrivateAppNamespace = assembleAppNamespace(somePrivateNamespaceId,
        someAppId, somePrivateNamespace, false);
    AppNamespace somePublicAppNamespace = assembleAppNamespace(somePublicNamespaceId,
        somePublicAppId, somePublicNamespace, true);
    AppNamespace anotherPrivateAppNamespace = assembleAppNamespace(anotherPrivateNamespaceId,
        somePublicAppId, anotherPrivateNamespace, false);
    AppNamespace yetAnotherPrivateAppNamespace = assembleAppNamespace
        (yetAnotherPrivateNamespaceId, someAppId, yetAnotherPrivateNamespace, false);
    AppNamespace anotherPublicAppNamespace = assembleAppNamespace(anotherPublicNamespaceId,
        someAppId, anotherPublicNamespace, true);

    Set<String> someAppIdNamespaces = Sets.newHashSet
        (somePrivateNamespace, yetAnotherPrivateNamespace, anotherPublicNamespace);
    Set<String> someAppIdNamespacesWithIncorrectCase = Sets.newHashSet
        (somePrivateNamespaceWithIncorrectCase, yetAnotherPrivateNamespace, anotherPublicNamespace);
    Set<String> somePublicAppIdNamespaces = Sets.newHashSet(somePublicNamespace,
        anotherPrivateNamespace);
    Set<String> publicNamespaces = Sets.newHashSet(somePublicNamespace, anotherPublicNamespace);
    Set<String> publicNamespacesWithIncorrectCase = Sets.newHashSet(somePublicNamespaceWithIncorrectCase,
        anotherPublicNamespace);

    List<Long> appNamespaceIds = Lists.newArrayList(somePrivateNamespaceId,
        somePublicNamespaceId, anotherPrivateNamespaceId, yetAnotherPrivateNamespaceId,
        anotherPublicNamespaceId);
    List<AppNamespace> allAppNamespaces = Lists.newArrayList(somePrivateAppNamespace,
        somePublicAppNamespace, anotherPrivateAppNamespace, yetAnotherPrivateAppNamespace,
        anotherPublicAppNamespace);

    // Test init
    appNamespaceServiceWithCache.afterPropertiesSet();

    // Should have no record now
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, somePrivateNamespace));
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, somePrivateNamespaceWithIncorrectCase));
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, yetAnotherPrivateNamespace));
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, anotherPublicNamespace));
    assertTrue(appNamespaceServiceWithCache.findByAppIdAndNamespaces(someAppId, someAppIdNamespaces).isEmpty());
    assertTrue(appNamespaceServiceWithCache.findByAppIdAndNamespaces(someAppId, someAppIdNamespacesWithIncorrectCase)
        .isEmpty());
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(somePublicAppId, somePublicNamespace));
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(somePublicAppId,
        somePublicNamespaceWithIncorrectCase));
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(somePublicAppId, anotherPrivateNamespace));
    assertTrue(appNamespaceServiceWithCache.findByAppIdAndNamespaces(somePublicAppId,
        somePublicAppIdNamespaces).isEmpty());
    assertNull(appNamespaceServiceWithCache.findPublicNamespaceByName(somePublicNamespace));
    assertNull(appNamespaceServiceWithCache.findPublicNamespaceByName(somePublicNamespaceWithIncorrectCase));
    assertNull(appNamespaceServiceWithCache.findPublicNamespaceByName(anotherPublicNamespace));
    assertTrue(appNamespaceServiceWithCache.findPublicNamespacesByNames(publicNamespaces).isEmpty());
    assertTrue(appNamespaceServiceWithCache.findPublicNamespacesByNames(publicNamespacesWithIncorrectCase).isEmpty());

    // Add 1 private namespace and 1 public namespace
    when(appNamespaceRepository.findFirst500ByIdGreaterThanOrderByIdAsc(0)).thenReturn(Lists
        .newArrayList(somePrivateAppNamespace, somePublicAppNamespace));
    when(appNamespaceRepository.findAllById(Lists.newArrayList(somePrivateNamespaceId,
        somePublicNamespaceId))).thenReturn(Lists.newArrayList(somePrivateAppNamespace,
        somePublicAppNamespace));

    scanIntervalTimeUnit.sleep(sleepInterval);

    assertEquals(somePrivateAppNamespace,
        appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, somePrivateNamespace));
    assertEquals(somePrivateAppNamespace,
        appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, somePrivateNamespaceWithIncorrectCase));
    check(Lists.newArrayList(somePrivateAppNamespace), appNamespaceServiceWithCache
        .findByAppIdAndNamespaces(someAppId, someAppIdNamespaces));
    check(Lists.newArrayList(somePrivateAppNamespace), appNamespaceServiceWithCache
        .findByAppIdAndNamespaces(someAppId, someAppIdNamespacesWithIncorrectCase));
    assertEquals(somePublicAppNamespace, appNamespaceServiceWithCache.findByAppIdAndNamespace(somePublicAppId,
        somePublicNamespace));
    assertEquals(somePublicAppNamespace, appNamespaceServiceWithCache.findByAppIdAndNamespace(somePublicAppId,
        somePublicNamespaceWithIncorrectCase));
    check(Lists.newArrayList(somePublicAppNamespace), appNamespaceServiceWithCache
        .findByAppIdAndNamespaces(somePublicAppId, somePublicAppIdNamespaces));
    assertEquals(somePublicAppNamespace, appNamespaceServiceWithCache.findPublicNamespaceByName(somePublicNamespace));
    assertEquals(somePublicAppNamespace, appNamespaceServiceWithCache.findPublicNamespaceByName
        (somePublicNamespaceWithIncorrectCase));
    check(Lists.newArrayList(somePublicAppNamespace), appNamespaceServiceWithCache.findPublicNamespacesByNames
        (publicNamespaces));
    check(Lists.newArrayList(somePublicAppNamespace), appNamespaceServiceWithCache.findPublicNamespacesByNames
        (publicNamespacesWithIncorrectCase));

    // Add 2 private namespaces and 1 public namespace
    when(appNamespaceRepository.findFirst500ByIdGreaterThanOrderByIdAsc(somePublicNamespaceId))
        .thenReturn(Lists.newArrayList(anotherPrivateAppNamespace, yetAnotherPrivateAppNamespace,
            anotherPublicAppNamespace));
    when(appNamespaceRepository.findAllById(appNamespaceIds)).thenReturn(allAppNamespaces);

    scanIntervalTimeUnit.sleep(sleepInterval);

    check(Lists.newArrayList(somePrivateAppNamespace, yetAnotherPrivateAppNamespace,
        anotherPublicAppNamespace), Lists
        .newArrayList(appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, somePrivateNamespace),
            appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, yetAnotherPrivateNamespace),
            appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, anotherPublicNamespace)));
    check(Lists.newArrayList(somePrivateAppNamespace, yetAnotherPrivateAppNamespace,
        anotherPublicAppNamespace), appNamespaceServiceWithCache.findByAppIdAndNamespaces
        (someAppId, someAppIdNamespaces));
    check(Lists.newArrayList(somePublicAppNamespace, anotherPrivateAppNamespace),
        Lists.newArrayList(appNamespaceServiceWithCache.findByAppIdAndNamespace(somePublicAppId, somePublicNamespace),
            appNamespaceServiceWithCache.findByAppIdAndNamespace(somePublicAppId, anotherPrivateNamespace)));
    check(Lists.newArrayList(somePublicAppNamespace, anotherPrivateAppNamespace),
        appNamespaceServiceWithCache.findByAppIdAndNamespaces(somePublicAppId,
            somePublicAppIdNamespaces));
    check(Lists.newArrayList(somePublicAppNamespace, anotherPublicAppNamespace),
        Lists.newArrayList(appNamespaceServiceWithCache.findPublicNamespaceByName(somePublicNamespace),
            appNamespaceServiceWithCache.findPublicNamespaceByName(anotherPublicNamespace)));
    check(Lists.newArrayList(somePublicAppNamespace, anotherPublicAppNamespace),
        appNamespaceServiceWithCache.findPublicNamespacesByNames(publicNamespaces));

    // Update name
    String somePrivateNamespaceNew = "somePrivateNamespaceNew";
    AppNamespace somePrivateAppNamespaceNew = assembleAppNamespace(somePrivateAppNamespace.getId
        (), somePrivateAppNamespace.getAppId(), somePrivateNamespaceNew, somePrivateAppNamespace
        .isPublic());
    somePrivateAppNamespaceNew.setDataChangeLastModifiedTime(newDateWithDelta
        (somePrivateAppNamespace.getDataChangeLastModifiedTime(), 1));

    // Update appId
    String someAppIdNew = "someAppIdNew";
    AppNamespace yetAnotherPrivateAppNamespaceNew = assembleAppNamespace
        (yetAnotherPrivateAppNamespace.getId(), someAppIdNew, yetAnotherPrivateAppNamespace
            .getName(), false);
    yetAnotherPrivateAppNamespaceNew.setDataChangeLastModifiedTime(newDateWithDelta
        (yetAnotherPrivateAppNamespace.getDataChangeLastModifiedTime(), 1));

    // Update isPublic
    AppNamespace somePublicAppNamespaceNew = assembleAppNamespace(somePublicAppNamespace
            .getId(), somePublicAppNamespace.getAppId(), somePublicAppNamespace.getName(),
        !somePublicAppNamespace.isPublic());
    somePublicAppNamespaceNew.setDataChangeLastModifiedTime(newDateWithDelta
        (somePublicAppNamespace.getDataChangeLastModifiedTime(), 1));

    // Delete 1 private and 1 public
    when(appNamespaceRepository.findAllById(appNamespaceIds)).thenReturn(Lists.newArrayList
        (somePrivateAppNamespaceNew, yetAnotherPrivateAppNamespaceNew, somePublicAppNamespaceNew));

    scanIntervalTimeUnit.sleep(sleepInterval);

    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, somePrivateNamespace));
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, yetAnotherPrivateNamespace));
    assertNull(appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, anotherPublicNamespace));
    check(Collections.emptyList(), appNamespaceServiceWithCache
        .findByAppIdAndNamespaces(someAppId, someAppIdNamespaces));
    assertEquals(somePublicAppNamespaceNew,
        appNamespaceServiceWithCache.findByAppIdAndNamespace(somePublicAppId, somePublicNamespace));
    check(Lists.newArrayList(somePublicAppNamespaceNew),
        appNamespaceServiceWithCache.findByAppIdAndNamespaces(somePublicAppId,
            somePublicAppIdNamespaces));
    assertNull(appNamespaceServiceWithCache.findPublicNamespaceByName(somePublicNamespace));
    assertNull(appNamespaceServiceWithCache.findPublicNamespaceByName(anotherPublicNamespace));
    check(Collections.emptyList(),
        appNamespaceServiceWithCache.findPublicNamespacesByNames(publicNamespaces));

    assertEquals(somePrivateAppNamespaceNew,
        appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppId, somePrivateNamespaceNew));
    check(Lists.newArrayList(somePrivateAppNamespaceNew), appNamespaceServiceWithCache
        .findByAppIdAndNamespaces(someAppId, Sets.newHashSet(somePrivateNamespaceNew)));
    assertEquals(yetAnotherPrivateAppNamespaceNew,
        appNamespaceServiceWithCache.findByAppIdAndNamespace(someAppIdNew, yetAnotherPrivateNamespace));
    check(Lists.newArrayList(yetAnotherPrivateAppNamespaceNew), appNamespaceServiceWithCache
        .findByAppIdAndNamespaces(someAppIdNew, Sets.newHashSet(yetAnotherPrivateNamespace)));
  }

  private void check(List<AppNamespace> someList, List<AppNamespace> anotherList) {
    Collections.sort(someList, appNamespaceComparator);
    Collections.sort(anotherList, appNamespaceComparator);
    assertEquals(someList, anotherList);
  }

  private Date newDateWithDelta(Date date, int deltaInSeconds) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.SECOND, deltaInSeconds);

    return calendar.getTime();
  }

  private AppNamespace assembleAppNamespace(long id, String appId, String name, boolean isPublic) {
    AppNamespace appNamespace = new AppNamespace();
    appNamespace.setId(id);
    appNamespace.setAppId(appId);
    appNamespace.setName(name);
    appNamespace.setPublic(isPublic);
    appNamespace.setDataChangeLastModifiedTime(new Date());
    return appNamespace;
  }
}
