package com.ctrip.framework.apollo.biz.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.ctrip.framework.apollo.biz.entity.Audit;
import com.ctrip.framework.apollo.biz.entity.Cluster;
import com.ctrip.framework.apollo.biz.entity.Namespace;
import com.ctrip.framework.apollo.biz.repository.AppNamespaceRepository;
import com.ctrip.framework.apollo.common.entity.AppNamespace;
import com.ctrip.framework.apollo.common.exception.ServiceException;
import com.ctrip.framework.apollo.common.utils.BeanUtils;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.core.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class AppNamespaceService {

  private static final Logger logger = LoggerFactory.getLogger(AppNamespaceService.class);

  @Autowired
  private AppNamespaceRepository appNamespaceRepository;
  @Autowired
  private NamespaceService namespaceService;
  @Autowired
  private ClusterService clusterService;
  @Autowired
  private AuditService auditService;

  public boolean isAppNamespaceNameUnique(String appId, String namespaceName) {
    Objects.requireNonNull(appId, "AppId must not be null");
    Objects.requireNonNull(namespaceName, "Namespace must not be null");
    return Objects.isNull(appNamespaceRepository.findByAppIdAndName(appId, namespaceName));
  }

  public AppNamespace findPublicNamespaceByName(String namespaceName) {
    Preconditions.checkArgument(namespaceName != null, "Namespace must not be null");
    return appNamespaceRepository.findByNameAndIsPublicTrue(namespaceName);
  }

  public List<AppNamespace> findByAppId(String appId) {
    return appNamespaceRepository.findByAppId(appId);
  }

  public List<AppNamespace> findPublicNamespacesByNames(Set<String> namespaceNames) {
    if (namespaceNames == null || namespaceNames.isEmpty()) {
      return Collections.emptyList();
    }

    return appNamespaceRepository.findByNameInAndIsPublicTrue(namespaceNames);
  }

  public List<AppNamespace> findPrivateAppNamespace(String appId) {
    return appNamespaceRepository.findByAppIdAndIsPublic(appId, false);
  }

  public AppNamespace findOne(String appId, String namespaceName) {
    Preconditions
        .checkArgument(!StringUtils.isContainEmpty(appId, namespaceName), "appId or Namespace must not be null");
    return appNamespaceRepository.findByAppIdAndName(appId, namespaceName);
  }

  public List<AppNamespace> findByAppIdAndNamespaces(String appId, Set<String> namespaceNames) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(appId), "appId must not be null");
    if (namespaceNames == null || namespaceNames.isEmpty()) {
      return Collections.emptyList();
    }
    return appNamespaceRepository.findByAppIdAndNameIn(appId, namespaceNames);
  }

  @Transactional
  public void createDefaultAppNamespace(String appId, String createBy) {
    if (!isAppNamespaceNameUnique(appId, ConfigConsts.NAMESPACE_APPLICATION)) {
      throw new ServiceException("appnamespace not unique");
    }
    AppNamespace appNs = new AppNamespace();
    appNs.setAppId(appId);
    appNs.setName(ConfigConsts.NAMESPACE_APPLICATION);
    appNs.setComment("default app namespace");
    appNs.setFormat(ConfigFileFormat.Properties.getValue());
    appNs.setDataChangeCreatedBy(createBy);
    appNs.setDataChangeLastModifiedBy(createBy);
    appNamespaceRepository.save(appNs);

    auditService.audit(AppNamespace.class.getSimpleName(), appNs.getId(), Audit.OP.INSERT,
                       createBy);
  }

  @Transactional
  public AppNamespace createAppNamespace(AppNamespace appNamespace) {
    String createBy = appNamespace.getDataChangeCreatedBy();
    if (!isAppNamespaceNameUnique(appNamespace.getAppId(), appNamespace.getName())) {
      throw new ServiceException("appnamespace not unique");
    }
    appNamespace.setId(0);//protection
    appNamespace.setDataChangeCreatedBy(createBy);
    appNamespace.setDataChangeLastModifiedBy(createBy);

    appNamespace = appNamespaceRepository.save(appNamespace);

    instanceOfAppNamespaceInAllCluster(appNamespace.getAppId(), appNamespace.getName(), createBy);

    auditService.audit(AppNamespace.class.getSimpleName(), appNamespace.getId(), Audit.OP.INSERT, createBy);
    return appNamespace;
  }

  public AppNamespace update(AppNamespace appNamespace) {
    AppNamespace managedNs = appNamespaceRepository.findByAppIdAndName(appNamespace.getAppId(), appNamespace.getName());
    BeanUtils.copyEntityProperties(appNamespace, managedNs);
    managedNs = appNamespaceRepository.save(managedNs);

    auditService.audit(AppNamespace.class.getSimpleName(), managedNs.getId(), Audit.OP.UPDATE,
                       managedNs.getDataChangeLastModifiedBy());

    return managedNs;
  }

  private void instanceOfAppNamespaceInAllCluster(String appId, String namespaceName, String createBy) {
    List<Cluster> clusters = clusterService.findParentClusters(appId);

    for (Cluster cluster : clusters) {

      // in case there is some dirty data, e.g. public namespace deleted in other app and now created in this app
      if (!namespaceService.isNamespaceUnique(appId, cluster.getName(), namespaceName)) {
        continue;
      }

      Namespace namespace = new Namespace();
      namespace.setClusterName(cluster.getName());
      namespace.setAppId(appId);
      namespace.setNamespaceName(namespaceName);
      namespace.setDataChangeCreatedBy(createBy);
      namespace.setDataChangeLastModifiedBy(createBy);

      namespaceService.save(namespace);
    }
  }

  @Transactional
  public void batchDelete(String appId, String operator) {
    appNamespaceRepository.batchDeleteByAppId(appId, operator);
  }

  @Transactional
  public void deleteAppNamespace(AppNamespace appNamespace, String operator) {
    String appId = appNamespace.getAppId();
    String namespaceName = appNamespace.getName();

    logger.info("{} is deleting AppNamespace, appId: {}, namespace: {}", operator, appId, namespaceName);

    // 1. delete namespaces
    List<Namespace> namespaces = namespaceService.findByAppIdAndNamespaceName(appId, namespaceName);

    if (namespaces != null) {
      for (Namespace namespace : namespaces) {
        namespaceService.deleteNamespace(namespace, operator);
      }
    }

    // 2. delete app namespace
    appNamespaceRepository.delete(appId, namespaceName, operator);
  }
}
