package com.ctrip.framework.apollo.portal.service;

import com.ctrip.framework.apollo.common.dto.NamespaceLockDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.portal.api.AdminServiceAPI;
import com.ctrip.framework.apollo.portal.component.config.PortalConfig;
import com.ctrip.framework.apollo.portal.entity.vo.LockInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NamespaceLockService {

  @Autowired
  private AdminServiceAPI.NamespaceLockAPI namespaceLockAPI;
  @Autowired
  private PortalConfig portalConfig;


  public NamespaceLockDTO getNamespaceLock(String appId, Env env, String clusterName, String namespaceName) {
    return namespaceLockAPI.getNamespaceLockOwner(appId, env, clusterName, namespaceName);
  }

  public LockInfo getNamespaceLockInfo(String appId, Env env, String clusterName, String namespaceName) {
    LockInfo lockInfo = new LockInfo();

    NamespaceLockDTO namespaceLockDTO = namespaceLockAPI.getNamespaceLockOwner(appId, env, clusterName, namespaceName);
    String lockOwner = namespaceLockDTO == null ? "" : namespaceLockDTO.getDataChangeCreatedBy();
    lockInfo.setLockOwner(lockOwner);

    lockInfo.setEmergencyPublishAllowed(portalConfig.isEmergencyPublishAllowed(env));

    return lockInfo;
  }

}
