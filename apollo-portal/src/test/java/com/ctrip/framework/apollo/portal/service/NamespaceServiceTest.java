package com.ctrip.framework.apollo.portal.service;

import com.ctrip.framework.apollo.common.dto.ItemDTO;
import com.ctrip.framework.apollo.common.dto.NamespaceDTO;
import com.ctrip.framework.apollo.common.dto.ReleaseDTO;
import com.ctrip.framework.apollo.common.entity.AppNamespace;
import com.ctrip.framework.apollo.common.exception.BadRequestException;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.portal.AbstractUnitTest;
import com.ctrip.framework.apollo.portal.api.AdminServiceAPI;
import com.ctrip.framework.apollo.portal.component.txtresolver.PropertyResolver;
import com.ctrip.framework.apollo.portal.entity.bo.NamespaceBO;
import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NamespaceServiceTest extends AbstractUnitTest {

  @Mock
  private AdminServiceAPI.NamespaceAPI namespaceAPI;
  @Mock
  private ReleaseService releaseService;
  @Mock
  private ItemService itemService;
  @Mock
  private PropertyResolver resolver;
  @Mock
  private AppNamespaceService appNamespaceService;
  @Mock
  private InstanceService instanceService;
  @Mock
  private NamespaceBranchService branchService;
  @Mock
  private UserInfoHolder userInfoHolder;

  @InjectMocks
  private NamespaceService namespaceService;

  private String testAppId = "6666";
  private String testClusterName = "default";
  private String testNamespaceName = "application";
  private Env testEnv = Env.DEV;

  @Before
  public void setup() {
  }

  @Test
  public void testFindNamespace() {

    AppNamespace applicationAppNamespace = mock(AppNamespace.class);
    AppNamespace hermesAppNamespace = mock(AppNamespace.class);

    NamespaceDTO application = new NamespaceDTO();
    application.setId(1);
    application.setClusterName(testClusterName);
    application.setAppId(testAppId);
    application.setNamespaceName(testNamespaceName);

    NamespaceDTO hermes = new NamespaceDTO();
    hermes.setId(2);
    hermes.setClusterName("default");
    hermes.setAppId(testAppId);
    hermes.setNamespaceName("hermes");
    List<NamespaceDTO> namespaces = Arrays.asList(application, hermes);

    ReleaseDTO someRelease = new ReleaseDTO();
    someRelease.setConfigurations("{\"a\":\"123\",\"b\":\"123\"}");

    ItemDTO i1 = new ItemDTO("a", "123", "", 1);
    ItemDTO i2 = new ItemDTO("b", "1", "", 2);
    ItemDTO i3 = new ItemDTO("", "", "#dddd", 3);
    ItemDTO i4 = new ItemDTO("c", "1", "", 4);
    List<ItemDTO> someItems = Arrays.asList(i1, i2, i3, i4);

    when(applicationAppNamespace.getFormat()).thenReturn(ConfigFileFormat.Properties.getValue());
    when(hermesAppNamespace.getFormat()).thenReturn(ConfigFileFormat.XML.getValue());
    when(appNamespaceService.findByAppIdAndName(testAppId, testNamespaceName))
        .thenReturn(applicationAppNamespace);
    when(appNamespaceService.findPublicAppNamespace("hermes")).thenReturn(hermesAppNamespace);
    when(namespaceAPI.findNamespaceByCluster(testAppId, Env.DEV, testClusterName)).thenReturn(namespaces);
    when(releaseService.loadLatestRelease(testAppId, Env.DEV, testClusterName,
                                          testNamespaceName)).thenReturn(someRelease);
    when(releaseService.loadLatestRelease(testAppId, Env.DEV, testClusterName, "hermes")).thenReturn(someRelease);
    when(itemService.findItems(testAppId, Env.DEV, testClusterName, testNamespaceName)).thenReturn(someItems);

    List<NamespaceBO> namespaceVOs = namespaceService.findNamespaceBOs(testAppId, Env.DEV, testClusterName);
    assertEquals(2, namespaceVOs.size());
    NamespaceBO namespaceVO = namespaceVOs.get(0);
    assertEquals(4, namespaceVO.getItems().size());
    assertEquals("a", namespaceVO.getItems().get(0).getItem().getKey());
    assertEquals(2, namespaceVO.getItemModifiedCnt());
    assertEquals(testAppId, namespaceVO.getBaseInfo().getAppId());
    assertEquals(testClusterName, namespaceVO.getBaseInfo().getClusterName());
    assertEquals(testNamespaceName, namespaceVO.getBaseInfo().getNamespaceName());

  }

  @Test
  public void testDeletePrivateNamespace() {
    String operator = "user";
    AppNamespace privateNamespace = createAppNamespace(testAppId, testNamespaceName, false);

    when(appNamespaceService.findByAppIdAndName(testAppId, testNamespaceName)).thenReturn(privateNamespace);

    when(userInfoHolder.getUser()).thenReturn(createUser(operator));

    namespaceService.deleteNamespace(testAppId, testEnv, testClusterName, testNamespaceName);

    verify(namespaceAPI, times(1)).deleteNamespace(testEnv, testAppId, testClusterName, testNamespaceName, operator);
  }

  @Test(expected = BadRequestException.class)
  public void testDeleteNamespaceHasInstance() {
    AppNamespace publicNamespace = createAppNamespace(testAppId, testNamespaceName, true);

    when(appNamespaceService.findByAppIdAndName(testAppId, testNamespaceName)).thenReturn(publicNamespace);
    when(instanceService.getInstanceCountByNamepsace(testAppId, testEnv, testClusterName, testNamespaceName))
        .thenReturn(10);

    namespaceService.deleteNamespace(testAppId, testEnv, testClusterName, testNamespaceName);

  }

  @Test(expected = BadRequestException.class)
  public void testDeleteNamespaceBranchHasInstance() {
    AppNamespace publicNamespace = createAppNamespace(testAppId, testNamespaceName, true);
    String branchName = "branch";
    NamespaceDTO branch = createNamespace(testAppId, branchName, testNamespaceName);

    when(appNamespaceService.findByAppIdAndName(testAppId, testNamespaceName)).thenReturn(publicNamespace);
    when(instanceService.getInstanceCountByNamepsace(testAppId, testEnv, testClusterName, testNamespaceName))
        .thenReturn(0);
    when(branchService.findBranchBaseInfo(testAppId, testEnv, testClusterName, testNamespaceName)).thenReturn(branch);
    when(instanceService.getInstanceCountByNamepsace(testAppId, testEnv, branchName, testNamespaceName)).thenReturn(10);

    namespaceService.deleteNamespace(testAppId, testEnv, testClusterName, testNamespaceName);

  }

  @Test(expected = BadRequestException.class)
  public void testDeleteNamespaceWithAssociatedNamespace() {
    AppNamespace publicNamespace = createAppNamespace(testAppId, testNamespaceName, true);
    String branchName = "branch";

    NamespaceDTO branch = createNamespace(testAppId, branchName, testNamespaceName);

    when(appNamespaceService.findByAppIdAndName(testAppId, testNamespaceName)).thenReturn(publicNamespace);
    when(instanceService.getInstanceCountByNamepsace(testAppId, testEnv, testClusterName, testNamespaceName))
        .thenReturn(0);
    when(branchService.findBranchBaseInfo(testAppId, testEnv, testClusterName, testNamespaceName)).thenReturn(branch);
    when(instanceService.getInstanceCountByNamepsace(testAppId, testEnv, branchName, testNamespaceName)).thenReturn(0);
    when(appNamespaceService.findPublicAppNamespace(testNamespaceName)).thenReturn(publicNamespace);

   when(namespaceAPI.countPublicAppNamespaceAssociatedNamespaces(testEnv, testNamespaceName)).thenReturn(10);

    namespaceService.deleteNamespace(testAppId, testEnv, testClusterName, testNamespaceName);
  }

  @Test
  public void testDeleteEmptyNamespace() {
    String branchName = "branch";
    String operator = "user";

    AppNamespace publicNamespace = createAppNamespace(testAppId, testNamespaceName, true);
    NamespaceDTO branch = createNamespace(testAppId, branchName, testNamespaceName);

    when(appNamespaceService.findByAppIdAndName(testAppId, testNamespaceName)).thenReturn(publicNamespace);
    when(instanceService.getInstanceCountByNamepsace(testAppId, testEnv, testClusterName, testNamespaceName))
        .thenReturn(0);
    when(branchService.findBranchBaseInfo(testAppId, testEnv, testClusterName, testNamespaceName)).thenReturn(branch);
    when(instanceService.getInstanceCountByNamepsace(testAppId, testEnv, branchName, testNamespaceName)).thenReturn(0);
    when(appNamespaceService.findPublicAppNamespace(testNamespaceName)).thenReturn(publicNamespace);

    NamespaceDTO namespace = createNamespace(testAppId, testClusterName, testNamespaceName);
    when(namespaceAPI.getPublicAppNamespaceAllNamespaces(testEnv, testNamespaceName, 0, 10)).thenReturn(
        Collections.singletonList(namespace));
    when(userInfoHolder.getUser()).thenReturn(createUser(operator));

    namespaceService.deleteNamespace(testAppId, testEnv, testClusterName, testNamespaceName);

    verify(namespaceAPI, times(1)).deleteNamespace(testEnv, testAppId, testClusterName, testNamespaceName, operator);

  }


  private AppNamespace createAppNamespace(String appId, String name, boolean isPublic) {
    AppNamespace instance = new AppNamespace();

    instance.setAppId(appId);
    instance.setName(name);
    instance.setPublic(isPublic);

    return instance;
  }

  private NamespaceDTO createNamespace(String appId, String clusterName, String namespaceName) {
    NamespaceDTO instance = new NamespaceDTO();

    instance.setAppId(appId);
    instance.setClusterName(clusterName);
    instance.setNamespaceName(namespaceName);

    return instance;
  }

  private UserInfo createUser(String userId) {
    UserInfo instance = new UserInfo();

    instance.setUserId(userId);

    return instance;
  }
}
