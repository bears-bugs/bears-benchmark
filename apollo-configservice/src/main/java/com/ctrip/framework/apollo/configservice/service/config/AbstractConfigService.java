package com.ctrip.framework.apollo.configservice.service.config;

import com.ctrip.framework.apollo.biz.entity.Release;
import com.ctrip.framework.apollo.biz.grayReleaseRule.GrayReleaseRulesHolder;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.dto.ApolloNotificationMessages;

import com.google.common.base.Strings;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public abstract class AbstractConfigService implements ConfigService {
  @Autowired
  private GrayReleaseRulesHolder grayReleaseRulesHolder;

  @Override
  public Release loadConfig(String clientAppId, String clientIp, String configAppId, String configClusterName,
      String configNamespace, String dataCenter, ApolloNotificationMessages clientMessages) {
    // load from specified cluster fist
    if (!Objects.equals(ConfigConsts.CLUSTER_NAME_DEFAULT, configClusterName)) {
      Release clusterRelease = findRelease(clientAppId, clientIp, configAppId, configClusterName, configNamespace,
          clientMessages);

      if (!Objects.isNull(clusterRelease)) {
        return clusterRelease;
      }
    }

    // try to load via data center
    if (!Strings.isNullOrEmpty(dataCenter) && !Objects.equals(dataCenter, configClusterName)) {
      Release dataCenterRelease = findRelease(clientAppId, clientIp, configAppId, dataCenter, configNamespace,
          clientMessages);
      if (!Objects.isNull(dataCenterRelease)) {
        return dataCenterRelease;
      }
    }

    // fallback to default release
    return findRelease(clientAppId, clientIp, configAppId, ConfigConsts.CLUSTER_NAME_DEFAULT, configNamespace,
        clientMessages);
  }

  /**
   * Find release
   * 
   * @param clientAppId the client's app id
   * @param clientIp the client ip
   * @param configAppId the requested config's app id
   * @param configClusterName the requested config's cluster name
   * @param configNamespace the requested config's namespace name
   * @param clientMessages the messages received in client side
   * @return the release
   */
  private Release findRelease(String clientAppId, String clientIp, String configAppId, String configClusterName,
      String configNamespace, ApolloNotificationMessages clientMessages) {
    Long grayReleaseId = grayReleaseRulesHolder.findReleaseIdFromGrayReleaseRule(clientAppId, clientIp, configAppId,
        configClusterName, configNamespace);

    Release release = null;

    if (grayReleaseId != null) {
      release = findActiveOne(grayReleaseId, clientMessages);
    }

    if (release == null) {
      release = findLatestActiveRelease(configAppId, configClusterName, configNamespace, clientMessages);
    }

    return release;
  }

  /**
   * Find active release by id
   */
  protected abstract Release findActiveOne(long id, ApolloNotificationMessages clientMessages);

  /**
   * Find active release by app id, cluster name and namespace name
   */
  protected abstract Release findLatestActiveRelease(String configAppId, String configClusterName,
      String configNamespaceName, ApolloNotificationMessages clientMessages);
}
