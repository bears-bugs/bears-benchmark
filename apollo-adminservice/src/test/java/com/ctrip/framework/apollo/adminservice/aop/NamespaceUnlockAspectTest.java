package com.ctrip.framework.apollo.adminservice.aop;

import com.ctrip.framework.apollo.biz.entity.Item;
import com.ctrip.framework.apollo.biz.entity.Namespace;
import com.ctrip.framework.apollo.biz.entity.Release;
import com.ctrip.framework.apollo.biz.service.ItemService;
import com.ctrip.framework.apollo.biz.service.NamespaceService;
import com.ctrip.framework.apollo.biz.service.ReleaseService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NamespaceUnlockAspectTest {

  @Mock
  private ReleaseService releaseService;
  @Mock
  private ItemService itemService;
  @Mock
  private NamespaceService namespaceService;

  @InjectMocks
  private NamespaceUnlockAspect namespaceUnlockAspect;


  @Test
  public void testNamespaceHasNoNormalItemsAndRelease() {

    long namespaceId = 1;
    Namespace namespace = createNamespace(namespaceId);

    when(releaseService.findLatestActiveRelease(namespace)).thenReturn(null);
    when(itemService.findItemsWithOrdered(namespaceId)).thenReturn(Collections.singletonList(createItem("", "")));

    boolean isModified = namespaceUnlockAspect.isModified(namespace);

    Assert.assertFalse(isModified);
  }

  @Test
  public void testNamespaceAddItem() {
    long namespaceId = 1;
    Namespace namespace = createNamespace(namespaceId);

    Release release = createRelease("{\"k1\":\"v1\"}");
    List<Item> items = Arrays.asList(createItem("k1", "v1"), createItem("k2", "v2"));

    when(releaseService.findLatestActiveRelease(namespace)).thenReturn(release);
    when(itemService.findItemsWithOrdered(namespaceId)).thenReturn(items);
    when(namespaceService.findParentNamespace(namespace)).thenReturn(null);

    boolean isModified = namespaceUnlockAspect.isModified(namespace);

    Assert.assertTrue(isModified);
  }

  @Test
  public void testNamespaceModifyItem() {
    long namespaceId = 1;
    Namespace namespace = createNamespace(namespaceId);

    Release release = createRelease("{\"k1\":\"v1\"}");
    List<Item> items = Arrays.asList(createItem("k1", "v2"));

    when(releaseService.findLatestActiveRelease(namespace)).thenReturn(release);
    when(itemService.findItemsWithOrdered(namespaceId)).thenReturn(items);
    when(namespaceService.findParentNamespace(namespace)).thenReturn(null);

    boolean isModified = namespaceUnlockAspect.isModified(namespace);

    Assert.assertTrue(isModified);
  }

  @Test
  public void testNamespaceDeleteItem() {
    long namespaceId = 1;
    Namespace namespace = createNamespace(namespaceId);

    Release release = createRelease("{\"k1\":\"v1\"}");
    List<Item> items = Arrays.asList(createItem("k2", "v2"));

    when(releaseService.findLatestActiveRelease(namespace)).thenReturn(release);
    when(itemService.findItemsWithOrdered(namespaceId)).thenReturn(items);
    when(namespaceService.findParentNamespace(namespace)).thenReturn(null);

    boolean isModified = namespaceUnlockAspect.isModified(namespace);

    Assert.assertTrue(isModified);
  }

  @Test
  public void testChildNamespaceModified() {
    long childNamespaceId = 1, parentNamespaceId = 2;
    Namespace childNamespace = createNamespace(childNamespaceId);
    Namespace parentNamespace = createNamespace(parentNamespaceId);

    Release childRelease = createRelease("{\"k1\":\"v1\", \"k2\":\"v2\"}");
    List<Item> childItems = Arrays.asList(createItem("k1", "v3"));
    Release parentRelease = createRelease("{\"k1\":\"v1\", \"k2\":\"v2\"}");

    when(releaseService.findLatestActiveRelease(childNamespace)).thenReturn(childRelease);
    when(releaseService.findLatestActiveRelease(parentNamespace)).thenReturn(parentRelease);
    when(itemService.findItemsWithoutOrdered(childNamespaceId)).thenReturn(childItems);
    when(namespaceService.findParentNamespace(childNamespace)).thenReturn(parentNamespace);

    boolean isModified = namespaceUnlockAspect.isModified(childNamespace);

    Assert.assertTrue(isModified);
  }

  @Test
  public void testChildNamespaceNotModified() {
    long childNamespaceId = 1, parentNamespaceId = 2;
    Namespace childNamespace = createNamespace(childNamespaceId);
    Namespace parentNamespace = createNamespace(parentNamespaceId);

    Release childRelease = createRelease("{\"k1\":\"v3\", \"k2\":\"v2\"}");
    List<Item> childItems = Arrays.asList(createItem("k1", "v3"));
    Release parentRelease = createRelease("{\"k1\":\"v1\", \"k2\":\"v2\"}");

    when(releaseService.findLatestActiveRelease(childNamespace)).thenReturn(childRelease);
    when(releaseService.findLatestActiveRelease(parentNamespace)).thenReturn(parentRelease);
    when(itemService.findItemsWithoutOrdered(childNamespaceId)).thenReturn(childItems);
    when(namespaceService.findParentNamespace(childNamespace)).thenReturn(parentNamespace);

    boolean isModified = namespaceUnlockAspect.isModified(childNamespace);

    Assert.assertFalse(isModified);
  }

  @Test
  public void testParentNamespaceNotReleased() {
    long childNamespaceId = 1, parentNamespaceId = 2;
    Namespace childNamespace = createNamespace(childNamespaceId);
    Namespace parentNamespace = createNamespace(parentNamespaceId);

    Release childRelease = createRelease("{\"k1\":\"v3\", \"k2\":\"v2\"}");
    List<Item> childItems = Arrays.asList(createItem("k1", "v2"), createItem("k2", "v2"));

    when(releaseService.findLatestActiveRelease(childNamespace)).thenReturn(childRelease);
    when(releaseService.findLatestActiveRelease(parentNamespace)).thenReturn(null);
    when(itemService.findItemsWithOrdered(childNamespaceId)).thenReturn(childItems);
    when(namespaceService.findParentNamespace(childNamespace)).thenReturn(parentNamespace);

    boolean isModified = namespaceUnlockAspect.isModified(childNamespace);

    Assert.assertTrue(isModified);
  }


  private Namespace createNamespace(long namespaceId) {
    Namespace namespace = new Namespace();
    namespace.setId(namespaceId);
    return namespace;
  }

  private Item createItem(String key, String value) {
    Item item = new Item();
    item.setKey(key);
    item.setValue(value);
    return item;
  }

  private Release createRelease(String configuration) {
    Release release = new Release();
    release.setConfigurations(configuration);
    return release;
  }

}
