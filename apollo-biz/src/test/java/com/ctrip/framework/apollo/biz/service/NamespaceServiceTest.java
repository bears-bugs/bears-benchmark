package com.ctrip.framework.apollo.biz.service;

import com.ctrip.framework.apollo.biz.AbstractUnitTest;
import com.ctrip.framework.apollo.biz.MockBeanFactory;
import com.ctrip.framework.apollo.biz.entity.Namespace;
import com.ctrip.framework.apollo.biz.repository.NamespaceRepository;
import com.ctrip.framework.apollo.common.entity.AppNamespace;
import com.ctrip.framework.apollo.common.exception.BadRequestException;
import com.ctrip.framework.apollo.core.ConfigConsts;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class NamespaceServiceTest extends AbstractUnitTest {

  @Mock
  private AppNamespaceService appNamespaceService;
  @Mock
  private NamespaceRepository namespaceRepository;

  @Spy
  @InjectMocks
  private NamespaceService namespaceService;

  private String testPublicAppNamespace = "publicAppNamespace";


  @Test(expected = BadRequestException.class)
  public void testFindPublicAppNamespaceWithWrongNamespace() {
    Pageable page = PageRequest.of(0, 10);

    when(appNamespaceService.findPublicNamespaceByName(testPublicAppNamespace)).thenReturn(null);

    namespaceService.findPublicAppNamespaceAllNamespaces(testPublicAppNamespace, page);
  }

  @Test
  public void testFindPublicAppNamespace() {

    AppNamespace publicAppNamespace = MockBeanFactory.mockAppNamespace(null, testPublicAppNamespace, true);
    when(appNamespaceService.findPublicNamespaceByName(testPublicAppNamespace)).thenReturn(publicAppNamespace);

    Namespace firstParentNamespace =
        MockBeanFactory.mockNamespace("app", ConfigConsts.CLUSTER_NAME_DEFAULT, testPublicAppNamespace);
    Namespace secondParentNamespace =
        MockBeanFactory.mockNamespace("app1", ConfigConsts.CLUSTER_NAME_DEFAULT, testPublicAppNamespace);
    Namespace childNamespace =
        MockBeanFactory.mockNamespace("app2", ConfigConsts.CLUSTER_NAME_DEFAULT, testPublicAppNamespace);


    Pageable page = PageRequest.of(0, 10);

    when(namespaceRepository.findByNamespaceName(testPublicAppNamespace, page))
        .thenReturn(Arrays.asList(firstParentNamespace, secondParentNamespace));

    doReturn(false).when(namespaceService).isChildNamespace(firstParentNamespace);
    doReturn(false).when(namespaceService).isChildNamespace(secondParentNamespace);
    doReturn(true).when(namespaceService).isChildNamespace(childNamespace);

    List<Namespace> namespaces = namespaceService.findPublicAppNamespaceAllNamespaces(testPublicAppNamespace, page);

    assertEquals(2, namespaces.size());
  }

}
