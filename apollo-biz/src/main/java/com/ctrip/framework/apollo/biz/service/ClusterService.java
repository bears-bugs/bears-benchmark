package com.ctrip.framework.apollo.biz.service;

import com.google.common.base.Strings;

import com.ctrip.framework.apollo.biz.entity.Audit;
import com.ctrip.framework.apollo.biz.entity.Cluster;
import com.ctrip.framework.apollo.biz.repository.ClusterRepository;
import com.ctrip.framework.apollo.common.exception.BadRequestException;
import com.ctrip.framework.apollo.common.exception.ServiceException;
import com.ctrip.framework.apollo.common.utils.BeanUtils;
import com.ctrip.framework.apollo.core.ConfigConsts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ClusterService {

  @Autowired
  private ClusterRepository clusterRepository;
  @Autowired
  private AuditService auditService;
  @Autowired
  private NamespaceService namespaceService;


  public boolean isClusterNameUnique(String appId, String clusterName) {
    Objects.requireNonNull(appId, "AppId must not be null");
    Objects.requireNonNull(clusterName, "ClusterName must not be null");
    return Objects.isNull(clusterRepository.findByAppIdAndName(appId, clusterName));
  }

  public Cluster findOne(String appId, String name) {
    return clusterRepository.findByAppIdAndName(appId, name);
  }

  public Cluster findOne(long clusterId) {
    return clusterRepository.findById(clusterId).orElse(null);
  }

  public List<Cluster> findParentClusters(String appId) {
    if (Strings.isNullOrEmpty(appId)) {
      return Collections.emptyList();
    }

    List<Cluster> clusters = clusterRepository.findByAppIdAndParentClusterId(appId, 0L);
    if (clusters == null) {
      return Collections.emptyList();
    }

    Collections.sort(clusters);

    return clusters;
  }

  @Transactional
  public Cluster saveWithInstanceOfAppNamespaces(Cluster entity) {

    Cluster savedCluster = saveWithoutInstanceOfAppNamespaces(entity);

    namespaceService.instanceOfAppNamespaces(savedCluster.getAppId(), savedCluster.getName(),
                                             savedCluster.getDataChangeCreatedBy());

    return savedCluster;
  }

  @Transactional
  public Cluster saveWithoutInstanceOfAppNamespaces(Cluster entity) {
    if (!isClusterNameUnique(entity.getAppId(), entity.getName())) {
      throw new BadRequestException("cluster not unique");
    }
    entity.setId(0);//protection
    Cluster cluster = clusterRepository.save(entity);

    auditService.audit(Cluster.class.getSimpleName(), cluster.getId(), Audit.OP.INSERT,
                       cluster.getDataChangeCreatedBy());

    return cluster;
  }

  @Transactional
  public void delete(long id, String operator) {
    Cluster cluster = clusterRepository.findById(id).orElse(null);
    if (cluster == null) {
      throw new BadRequestException("cluster not exist");
    }

    //delete linked namespaces
    namespaceService.deleteByAppIdAndClusterName(cluster.getAppId(), cluster.getName(), operator);

    cluster.setDeleted(true);
    cluster.setDataChangeLastModifiedBy(operator);
    clusterRepository.save(cluster);

    auditService.audit(Cluster.class.getSimpleName(), id, Audit.OP.DELETE, operator);
  }

  @Transactional
  public Cluster update(Cluster cluster) {
    Cluster managedCluster =
        clusterRepository.findByAppIdAndName(cluster.getAppId(), cluster.getName());
    BeanUtils.copyEntityProperties(cluster, managedCluster);
    managedCluster = clusterRepository.save(managedCluster);

    auditService.audit(Cluster.class.getSimpleName(), managedCluster.getId(), Audit.OP.UPDATE,
                       managedCluster.getDataChangeLastModifiedBy());

    return managedCluster;
  }

  @Transactional
  public void createDefaultCluster(String appId, String createBy) {
    if (!isClusterNameUnique(appId, ConfigConsts.CLUSTER_NAME_DEFAULT)) {
      throw new ServiceException("cluster not unique");
    }
    Cluster cluster = new Cluster();
    cluster.setName(ConfigConsts.CLUSTER_NAME_DEFAULT);
    cluster.setAppId(appId);
    cluster.setDataChangeCreatedBy(createBy);
    cluster.setDataChangeLastModifiedBy(createBy);
    clusterRepository.save(cluster);

    auditService.audit(Cluster.class.getSimpleName(), cluster.getId(), Audit.OP.INSERT, createBy);
  }

  public List<Cluster> findChildClusters(String appId, String parentClusterName) {
    Cluster parentCluster = findOne(appId, parentClusterName);
    if (parentCluster == null) {
      throw new BadRequestException("parent cluster not exist");
    }

    return clusterRepository.findByParentClusterId(parentCluster.getId());
  }

  public List<Cluster> findClusters(String appId) {
    List<Cluster> clusters = clusterRepository.findByAppId(appId);

    if (clusters == null) {
      return Collections.emptyList();
    }

    // to make sure parent cluster is ahead of branch cluster
    Collections.sort(clusters);

    return clusters;
  }
}
