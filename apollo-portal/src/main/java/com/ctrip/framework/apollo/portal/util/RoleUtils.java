package com.ctrip.framework.apollo.portal.util;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.portal.constant.RoleType;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.util.Iterator;

public class RoleUtils {

  private static final Joiner STRING_JOINER = Joiner.on(ConfigConsts.CLUSTER_NAMESPACE_SEPARATOR).skipNulls();
  private static final Splitter STRING_SPLITTER = Splitter.on(ConfigConsts.CLUSTER_NAMESPACE_SEPARATOR)
      .omitEmptyStrings().trimResults();

  public static String buildAppMasterRoleName(String appId) {
    return STRING_JOINER.join(RoleType.MASTER, appId);
  }

  public static String extractAppIdFromMasterRoleName(String masterRoleName) {
    Iterator<String> parts = STRING_SPLITTER.split(masterRoleName).iterator();

    // skip role type
    if (parts.hasNext() && parts.next().equals(RoleType.MASTER) && parts.hasNext()) {
      return parts.next();
    }

    return null;
  }

  public static String buildAppRoleName(String appId, String roleType) {
    return STRING_JOINER.join(roleType, appId);
  }

  public static String buildModifyNamespaceRoleName(String appId, String namespaceName) {
    return buildModifyNamespaceRoleName(appId, namespaceName, null);
  }

  public static String buildModifyNamespaceRoleName(String appId, String namespaceName, String env) {
    return STRING_JOINER.join(RoleType.MODIFY_NAMESPACE, appId, namespaceName, env);
  }

  public static String buildModifyDefaultNamespaceRoleName(String appId) {
    return STRING_JOINER.join(RoleType.MODIFY_NAMESPACE, appId, ConfigConsts.NAMESPACE_APPLICATION);
  }

  public static String buildReleaseNamespaceRoleName(String appId, String namespaceName) {
    return buildReleaseNamespaceRoleName(appId, namespaceName, null);
  }

  public static String buildReleaseNamespaceRoleName(String appId, String namespaceName, String env) {
    return STRING_JOINER.join(RoleType.RELEASE_NAMESPACE, appId, namespaceName, env);
  }

  public static String buildNamespaceRoleName(String appId, String namespaceName, String roleType) {
    return buildNamespaceRoleName(appId, namespaceName, roleType, null);
  }

  public static String buildNamespaceRoleName(String appId, String namespaceName, String roleType, String env) {
    return STRING_JOINER.join(roleType, appId, namespaceName, env);
  }

  public static String buildReleaseDefaultNamespaceRoleName(String appId) {
    return STRING_JOINER.join(RoleType.RELEASE_NAMESPACE, appId, ConfigConsts.NAMESPACE_APPLICATION);
  }

  public static String buildNamespaceTargetId(String appId, String namespaceName) {
    return buildNamespaceTargetId(appId, namespaceName, null);
  }

  public static String buildNamespaceTargetId(String appId, String namespaceName, String env) {
    return STRING_JOINER.join(appId, namespaceName, env);
  }

  public static String buildDefaultNamespaceTargetId(String appId) {
    return STRING_JOINER.join(appId, ConfigConsts.NAMESPACE_APPLICATION);
  }


}
