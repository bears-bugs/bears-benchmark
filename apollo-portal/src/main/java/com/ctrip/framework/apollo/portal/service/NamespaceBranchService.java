package com.ctrip.framework.apollo.portal.service;

import com.ctrip.framework.apollo.common.dto.GrayReleaseRuleDTO;
import com.ctrip.framework.apollo.common.dto.ItemChangeSets;
import com.ctrip.framework.apollo.common.dto.ItemDTO;
import com.ctrip.framework.apollo.common.dto.NamespaceDTO;
import com.ctrip.framework.apollo.common.dto.ReleaseDTO;
import com.ctrip.framework.apollo.common.exception.BadRequestException;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.portal.api.AdminServiceAPI;
import com.ctrip.framework.apollo.portal.component.ItemsComparator;
import com.ctrip.framework.apollo.portal.constant.TracerEventType;
import com.ctrip.framework.apollo.portal.entity.bo.NamespaceBO;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.ctrip.framework.apollo.tracer.Tracer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class NamespaceBranchService {

  @Autowired
  private ItemsComparator itemsComparator;
  @Autowired
  private UserInfoHolder userInfoHolder;
  @Autowired
  private NamespaceService namespaceService;
  @Autowired
  private ItemService itemService;
  @Autowired
  private AdminServiceAPI.NamespaceBranchAPI namespaceBranchAPI;
  @Autowired
  private ReleaseService releaseService;


  @Transactional
  public NamespaceDTO createBranch(String appId, Env env, String parentClusterName, String namespaceName) {
    String operator = userInfoHolder.getUser().getUserId();
    return createBranch(appId, env, parentClusterName, namespaceName, operator);
  }

  @Transactional
  public NamespaceDTO createBranch(String appId, Env env, String parentClusterName, String namespaceName, String operator) {
    NamespaceDTO createdBranch = namespaceBranchAPI.createBranch(appId, env, parentClusterName, namespaceName,
            operator);

    Tracer.logEvent(TracerEventType.CREATE_GRAY_RELEASE, String.format("%s+%s+%s+%s", appId, env, parentClusterName,
            namespaceName));
    return createdBranch;

  }

  public GrayReleaseRuleDTO findBranchGrayRules(String appId, Env env, String clusterName,
                                                String namespaceName, String branchName) {
    return namespaceBranchAPI.findBranchGrayRules(appId, env, clusterName, namespaceName, branchName);

  }

  public void updateBranchGrayRules(String appId, Env env, String clusterName, String namespaceName,
                                    String branchName, GrayReleaseRuleDTO rules) {

    String operator = userInfoHolder.getUser().getUserId();
    updateBranchGrayRules(appId, env, clusterName, namespaceName, branchName, rules, operator);
  }

  public void updateBranchGrayRules(String appId, Env env, String clusterName, String namespaceName,
                                    String branchName, GrayReleaseRuleDTO rules, String operator) {
    rules.setDataChangeCreatedBy(operator);
    rules.setDataChangeLastModifiedBy(operator);

    namespaceBranchAPI.updateBranchGrayRules(appId, env, clusterName, namespaceName, branchName, rules);

    Tracer.logEvent(TracerEventType.UPDATE_GRAY_RELEASE_RULE,
            String.format("%s+%s+%s+%s", appId, env, clusterName, namespaceName));
  }

  public void deleteBranch(String appId, Env env, String clusterName, String namespaceName,
                           String branchName) {

    String operator = userInfoHolder.getUser().getUserId();
    deleteBranch(appId, env, clusterName, namespaceName, branchName, operator);
  }

  public void deleteBranch(String appId, Env env, String clusterName, String namespaceName,
                           String branchName, String operator) {
    namespaceBranchAPI.deleteBranch(appId, env, clusterName, namespaceName, branchName, operator);

    Tracer.logEvent(TracerEventType.DELETE_GRAY_RELEASE,
            String.format("%s+%s+%s+%s", appId, env, clusterName, namespaceName));
  }


  public ReleaseDTO merge(String appId, Env env, String clusterName, String namespaceName,
                          String branchName, String title, String comment,
                          boolean isEmergencyPublish, boolean deleteBranch) {
    String operator = userInfoHolder.getUser().getUserId();
    return merge(appId, env, clusterName, namespaceName, branchName, title, comment, isEmergencyPublish, deleteBranch, operator);
  }

  public ReleaseDTO merge(String appId, Env env, String clusterName, String namespaceName,
                          String branchName, String title, String comment,
                          boolean isEmergencyPublish, boolean deleteBranch, String operator) {

    ItemChangeSets changeSets = calculateBranchChangeSet(appId, env, clusterName, namespaceName, branchName, operator);

    ReleaseDTO mergedResult =
            releaseService.updateAndPublish(appId, env, clusterName, namespaceName, title, comment,
                    branchName, isEmergencyPublish, deleteBranch, changeSets);

    Tracer.logEvent(TracerEventType.MERGE_GRAY_RELEASE,
            String.format("%s+%s+%s+%s", appId, env, clusterName, namespaceName));

    return mergedResult;
  }

  private ItemChangeSets calculateBranchChangeSet(String appId, Env env, String clusterName, String namespaceName,
                                                  String branchName, String operator) {
    NamespaceBO parentNamespace = namespaceService.loadNamespaceBO(appId, env, clusterName, namespaceName);

    if (parentNamespace == null) {
      throw new BadRequestException("base namespace not existed");
    }

    if (parentNamespace.getItemModifiedCnt() > 0) {
      throw new BadRequestException("Merge operation failed. Because master has modified items");
    }

    List<ItemDTO> masterItems = itemService.findItems(appId, env, clusterName, namespaceName);

    List<ItemDTO> branchItems = itemService.findItems(appId, env, branchName, namespaceName);

    ItemChangeSets changeSets = itemsComparator.compareIgnoreBlankAndCommentItem(parentNamespace.getBaseInfo().getId(),
                                                                                 masterItems, branchItems);
    changeSets.setDeleteItems(Collections.emptyList());
    changeSets.setDataChangeLastModifiedBy(operator);
    return changeSets;
  }

  public NamespaceDTO findBranchBaseInfo(String appId, Env env, String clusterName, String namespaceName) {
    return namespaceBranchAPI.findBranch(appId, env, clusterName, namespaceName);
  }

  public NamespaceBO findBranch(String appId, Env env, String clusterName, String namespaceName) {
    NamespaceDTO namespaceDTO = findBranchBaseInfo(appId, env, clusterName, namespaceName);
    if (namespaceDTO == null) {
      return null;
    }
    return namespaceService.loadNamespaceBO(appId, env, namespaceDTO.getClusterName(), namespaceName);
  }

}
