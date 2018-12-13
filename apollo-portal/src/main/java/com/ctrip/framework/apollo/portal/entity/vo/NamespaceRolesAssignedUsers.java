package com.ctrip.framework.apollo.portal.entity.vo;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;

import java.util.Set;

public class NamespaceRolesAssignedUsers {

  private String appId;
  private String namespaceName;

  private Set<UserInfo> modifyRoleUsers;
  private Set<UserInfo> releaseRoleUsers;

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getNamespaceName() {
    return namespaceName;
  }

  public void setNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
  }

  public Set<UserInfo> getModifyRoleUsers() {
    return modifyRoleUsers;
  }

  public void setModifyRoleUsers(
      Set<UserInfo> modifyRoleUsers) {
    this.modifyRoleUsers = modifyRoleUsers;
  }

  public Set<UserInfo> getReleaseRoleUsers() {
    return releaseRoleUsers;
  }

  public void setReleaseRoleUsers(
      Set<UserInfo> releaseRoleUsers) {
    this.releaseRoleUsers = releaseRoleUsers;
  }
}
