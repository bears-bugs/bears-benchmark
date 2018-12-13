package com.ctrip.framework.apollo.portal.service;

import com.ctrip.framework.apollo.portal.entity.model.NamespaceGrayDelReleaseModel;
import com.google.common.base.Objects;
import com.google.gson.Gson;

import com.ctrip.framework.apollo.common.constants.GsonType;
import com.ctrip.framework.apollo.common.dto.ItemChangeSets;
import com.ctrip.framework.apollo.common.dto.ReleaseDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.ctrip.framework.apollo.portal.api.AdminServiceAPI;
import com.ctrip.framework.apollo.portal.constant.TracerEventType;
import com.ctrip.framework.apollo.portal.entity.model.NamespaceReleaseModel;
import com.ctrip.framework.apollo.portal.entity.bo.KVEntity;
import com.ctrip.framework.apollo.portal.entity.vo.ReleaseCompareResult;
import com.ctrip.framework.apollo.portal.entity.bo.ReleaseBO;
import com.ctrip.framework.apollo.portal.enums.ChangeType;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.ctrip.framework.apollo.tracer.Tracer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ReleaseService {

  private static final Gson gson = new Gson();

  @Autowired
  private UserInfoHolder userInfoHolder;
  @Autowired
  private AdminServiceAPI.ReleaseAPI releaseAPI;

  public ReleaseDTO publish(NamespaceReleaseModel model) {
    Env env = model.getEnv();
    boolean isEmergencyPublish = model.isEmergencyPublish();
    String appId = model.getAppId();
    String clusterName = model.getClusterName();
    String namespaceName = model.getNamespaceName();
    String releaseBy = StringUtils.isEmpty(model.getReleasedBy()) ?
                       userInfoHolder.getUser().getUserId() : model.getReleasedBy();

    ReleaseDTO releaseDTO = releaseAPI.createRelease(appId, env, clusterName, namespaceName,
                                                     model.getReleaseTitle(), model.getReleaseComment(),
                                                     releaseBy, isEmergencyPublish);

    Tracer.logEvent(TracerEventType.RELEASE_NAMESPACE,
                    String.format("%s+%s+%s+%s", appId, env, clusterName, namespaceName));

    return releaseDTO;
  }

  //gray deletion release
  public ReleaseDTO publish(NamespaceGrayDelReleaseModel model, String releaseBy) {
    Env env = model.getEnv();
    boolean isEmergencyPublish = model.isEmergencyPublish();
    String appId = model.getAppId();
    String clusterName = model.getClusterName();
    String namespaceName = model.getNamespaceName();

    ReleaseDTO releaseDTO = releaseAPI.createGrayDeletionRelease(appId, env, clusterName, namespaceName,
            model.getReleaseTitle(), model.getReleaseComment(),
            releaseBy, isEmergencyPublish, model.getGrayDelKeys());

    Tracer.logEvent(TracerEventType.RELEASE_NAMESPACE,
            String.format("%s+%s+%s+%s", appId, env, clusterName, namespaceName));

    return releaseDTO;
  }

  public ReleaseDTO updateAndPublish(String appId, Env env, String clusterName, String namespaceName,
                                     String releaseTitle, String releaseComment, String branchName,
                                     boolean isEmergencyPublish, boolean deleteBranch, ItemChangeSets changeSets) {

    return releaseAPI.updateAndPublish(appId, env, clusterName, namespaceName, releaseTitle, releaseComment, branchName,
                                       isEmergencyPublish, deleteBranch, changeSets);
  }

  public List<ReleaseBO> findAllReleases(String appId, Env env, String clusterName, String namespaceName, int page,
                                         int size) {
    List<ReleaseDTO> releaseDTOs = releaseAPI.findAllReleases(appId, env, clusterName, namespaceName, page, size);

    if (CollectionUtils.isEmpty(releaseDTOs)) {
      return Collections.emptyList();
    }

    List<ReleaseBO> releases = new LinkedList<>();
    for (ReleaseDTO releaseDTO : releaseDTOs) {
      ReleaseBO release = new ReleaseBO();
      release.setBaseInfo(releaseDTO);

      Set<KVEntity> kvEntities = new LinkedHashSet<>();
      Map<String, String> configurations = gson.fromJson(releaseDTO.getConfigurations(), GsonType.CONFIG);
      Set<Map.Entry<String, String>> entries = configurations.entrySet();
      for (Map.Entry<String, String> entry : entries) {
        kvEntities.add(new KVEntity(entry.getKey(), entry.getValue()));
      }
      release.setItems(kvEntities);
      //为了减少数据量
      releaseDTO.setConfigurations("");
      releases.add(release);
    }

    return releases;
  }

  public List<ReleaseDTO> findActiveReleases(String appId, Env env, String clusterName, String namespaceName, int page,
                                             int size) {
    return releaseAPI.findActiveReleases(appId, env, clusterName, namespaceName, page, size);
  }

  public ReleaseDTO findReleaseById(Env env, long releaseId) {
    Set<Long> releaseIds = new HashSet<>(1);
    releaseIds.add(releaseId);
    List<ReleaseDTO> releases = findReleaseByIds(env, releaseIds);
    if (CollectionUtils.isEmpty(releases)) {
      return null;
    } else {
      return releases.get(0);
    }

  }

  public List<ReleaseDTO> findReleaseByIds(Env env, Set<Long> releaseIds) {
    return releaseAPI.findReleaseByIds(env, releaseIds);
  }

  public ReleaseDTO loadLatestRelease(String appId, Env env, String clusterName, String namespaceName) {
    return releaseAPI.loadLatestRelease(appId, env, clusterName, namespaceName);
  }

  public void rollback(Env env, long releaseId) {
    releaseAPI.rollback(env, releaseId, userInfoHolder.getUser().getUserId());
  }

  public ReleaseCompareResult compare(Env env, long baseReleaseId, long toCompareReleaseId) {

    ReleaseDTO baseRelease = null;
    ReleaseDTO toCompareRelease = null;
    if (baseReleaseId != 0) {
      baseRelease = releaseAPI.loadRelease(env, baseReleaseId);
    }

    if (toCompareReleaseId != 0) {
      toCompareRelease = releaseAPI.loadRelease(env, toCompareReleaseId);
    }

    return compare(baseRelease, toCompareRelease);
  }

  public ReleaseCompareResult compare(ReleaseDTO baseRelease, ReleaseDTO toCompareRelease) {
    Map<String, String> baseReleaseConfiguration = baseRelease == null ? new HashMap<>() :
                                                   gson.fromJson(baseRelease.getConfigurations(), GsonType.CONFIG);
    Map<String, String> toCompareReleaseConfiguration = toCompareRelease == null ? new HashMap<>() :
                                                        gson.fromJson(toCompareRelease.getConfigurations(),
                                                                      GsonType.CONFIG);

    ReleaseCompareResult compareResult = new ReleaseCompareResult();

    //added and modified in firstRelease
    for (Map.Entry<String, String> entry : baseReleaseConfiguration.entrySet()) {
      String key = entry.getKey();
      String firstValue = entry.getValue();
      String secondValue = toCompareReleaseConfiguration.get(key);
      //added
      if (secondValue == null) {
        compareResult.addEntityPair(ChangeType.DELETED, new KVEntity(key, firstValue),
                                    new KVEntity(key, null));
      } else if (!Objects.equal(firstValue, secondValue)) {
        compareResult.addEntityPair(ChangeType.MODIFIED, new KVEntity(key, firstValue),
                                    new KVEntity(key, secondValue));
      }

    }

    //deleted in firstRelease
    for (Map.Entry<String, String> entry : toCompareReleaseConfiguration.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (baseReleaseConfiguration.get(key) == null) {
        compareResult
            .addEntityPair(ChangeType.ADDED, new KVEntity(key, ""), new KVEntity(key, value));
      }

    }

    return compareResult;
  }
}
