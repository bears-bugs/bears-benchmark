package com.ctrip.framework.apollo.portal.service;

import com.ctrip.framework.apollo.common.dto.CommitDTO;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.portal.api.AdminServiceAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitService {


  @Autowired
  private AdminServiceAPI.CommitAPI commitAPI;

  public List<CommitDTO> find(String appId, Env env, String clusterName, String namespaceName, int page, int size) {
    return commitAPI.find(appId, env, clusterName, namespaceName, page, size);
  }

}
