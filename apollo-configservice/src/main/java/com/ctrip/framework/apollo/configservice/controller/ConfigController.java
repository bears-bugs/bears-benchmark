package com.ctrip.framework.apollo.configservice.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctrip.framework.apollo.biz.entity.Release;
import com.ctrip.framework.apollo.common.entity.AppNamespace;
import com.ctrip.framework.apollo.configservice.service.AppNamespaceServiceWithCache;
import com.ctrip.framework.apollo.configservice.service.config.ConfigService;
import com.ctrip.framework.apollo.configservice.util.InstanceConfigAuditUtil;
import com.ctrip.framework.apollo.configservice.util.NamespaceUtil;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.dto.ApolloConfig;
import com.ctrip.framework.apollo.core.dto.ApolloNotificationMessages;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@RestController
@RequestMapping("/configs")
public class ConfigController {
  private static final Splitter X_FORWARDED_FOR_SPLITTER = Splitter.on(",").omitEmptyStrings()
      .trimResults();
  @Autowired
  private ConfigService configService;
  @Autowired
  private AppNamespaceServiceWithCache appNamespaceService;
  @Autowired
  private NamespaceUtil namespaceUtil;
  @Autowired
  private InstanceConfigAuditUtil instanceConfigAuditUtil;
  @Autowired
  private Gson gson;

  private static final Type configurationTypeReference = new TypeToken<Map<String, String>>() {
      }.getType();

  @RequestMapping(value = "/{appId}/{clusterName}/{namespace:.+}", method = RequestMethod.GET)
  public ApolloConfig queryConfig(@PathVariable String appId, @PathVariable String clusterName,
                                  @PathVariable String namespace,
                                  @RequestParam(value = "dataCenter", required = false) String dataCenter,
                                  @RequestParam(value = "releaseKey", defaultValue = "-1") String clientSideReleaseKey,
                                  @RequestParam(value = "ip", required = false) String clientIp,
                                  @RequestParam(value = "messages", required = false) String messagesAsString,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
    String originalNamespace = namespace;
    //strip out .properties suffix
    namespace = namespaceUtil.filterNamespaceName(namespace);
    //fix the character case issue, such as FX.apollo <-> fx.apollo
    namespace = namespaceUtil.normalizeNamespace(appId, namespace);

    if (Strings.isNullOrEmpty(clientIp)) {
      clientIp = tryToGetClientIp(request);
    }

    ApolloNotificationMessages clientMessages = transformMessages(messagesAsString);

    List<Release> releases = Lists.newLinkedList();

    String appClusterNameLoaded = clusterName;
    if (!ConfigConsts.NO_APPID_PLACEHOLDER.equalsIgnoreCase(appId)) {
      Release currentAppRelease = configService.loadConfig(appId, clientIp, appId, clusterName, namespace,
          dataCenter, clientMessages);

      if (currentAppRelease != null) {
        releases.add(currentAppRelease);
        //we have cluster search process, so the cluster name might be overridden
        appClusterNameLoaded = currentAppRelease.getClusterName();
      }
    }

    //if namespace does not belong to this appId, should check if there is a public configuration
    if (!namespaceBelongsToAppId(appId, namespace)) {
      Release publicRelease = this.findPublicConfig(appId, clientIp, clusterName, namespace,
          dataCenter, clientMessages);
      if (!Objects.isNull(publicRelease)) {
        releases.add(publicRelease);
      }
    }

    if (releases.isEmpty()) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND,
          String.format(
              "Could not load configurations with appId: %s, clusterName: %s, namespace: %s",
              appId, clusterName, originalNamespace));
      Tracer.logEvent("Apollo.Config.NotFound",
          assembleKey(appId, clusterName, originalNamespace, dataCenter));
      return null;
    }

    auditReleases(appId, clusterName, dataCenter, clientIp, releases);

    String mergedReleaseKey = releases.stream().map(Release::getReleaseKey)
            .collect(Collectors.joining(ConfigConsts.CLUSTER_NAMESPACE_SEPARATOR));

    if (mergedReleaseKey.equals(clientSideReleaseKey)) {
      // Client side configuration is the same with server side, return 304
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      Tracer.logEvent("Apollo.Config.NotModified",
          assembleKey(appId, appClusterNameLoaded, originalNamespace, dataCenter));
      return null;
    }

    ApolloConfig apolloConfig = new ApolloConfig(appId, appClusterNameLoaded, originalNamespace,
        mergedReleaseKey);
    apolloConfig.setConfigurations(mergeReleaseConfigurations(releases));

    Tracer.logEvent("Apollo.Config.Found", assembleKey(appId, appClusterNameLoaded,
        originalNamespace, dataCenter));
    return apolloConfig;
  }

  private boolean namespaceBelongsToAppId(String appId, String namespaceName) {
    //Every app has an 'application' namespace
    if (Objects.equals(ConfigConsts.NAMESPACE_APPLICATION, namespaceName)) {
      return true;
    }

    //if no appId is present, then no other namespace belongs to it
    if (ConfigConsts.NO_APPID_PLACEHOLDER.equalsIgnoreCase(appId)) {
      return false;
    }

    AppNamespace appNamespace = appNamespaceService.findByAppIdAndNamespace(appId, namespaceName);

    return appNamespace != null;
  }

  /**
   * @param clientAppId the application which uses public config
   * @param namespace   the namespace
   * @param dataCenter  the datacenter
   */
  private Release findPublicConfig(String clientAppId, String clientIp, String clusterName,
                                   String namespace, String dataCenter, ApolloNotificationMessages clientMessages) {
    AppNamespace appNamespace = appNamespaceService.findPublicNamespaceByName(namespace);

    //check whether the namespace's appId equals to current one
    if (Objects.isNull(appNamespace) || Objects.equals(clientAppId, appNamespace.getAppId())) {
      return null;
    }

    String publicConfigAppId = appNamespace.getAppId();

    return configService.loadConfig(clientAppId, clientIp, publicConfigAppId, clusterName, namespace, dataCenter,
        clientMessages);
  }

  /**
   * Merge configurations of releases.
   * Release in lower index override those in higher index
   */
  Map<String, String> mergeReleaseConfigurations(List<Release> releases) {
    Map<String, String> result = Maps.newHashMap();
    for (Release release : Lists.reverse(releases)) {
      result.putAll(gson.fromJson(release.getConfigurations(), configurationTypeReference));
    }
    return result;
  }

  private String assembleKey(String appId, String cluster, String namespace, String dataCenter) {
    List<String> keyParts = Lists.newArrayList(appId, cluster, namespace);
    if (!Strings.isNullOrEmpty(dataCenter)) {
      keyParts.add(dataCenter);
    }
    return keyParts.stream().collect(Collectors.joining(ConfigConsts.CLUSTER_NAMESPACE_SEPARATOR));
  }

  private void auditReleases(String appId, String cluster, String dataCenter, String clientIp,
                             List<Release> releases) {
    if (Strings.isNullOrEmpty(clientIp)) {
      //no need to audit instance config when there is no ip
      return;
    }
    for (Release release : releases) {
      instanceConfigAuditUtil.audit(appId, cluster, dataCenter, clientIp, release.getAppId(),
          release.getClusterName(),
          release.getNamespaceName(), release.getReleaseKey());
    }
  }

  private String tryToGetClientIp(HttpServletRequest request) {
    String forwardedFor = request.getHeader("X-FORWARDED-FOR");
    if (!Strings.isNullOrEmpty(forwardedFor)) {
      return X_FORWARDED_FOR_SPLITTER.splitToList(forwardedFor).get(0);
    }
    return request.getRemoteAddr();
  }

  ApolloNotificationMessages transformMessages(String messagesAsString) {
    ApolloNotificationMessages notificationMessages = null;
    if (!Strings.isNullOrEmpty(messagesAsString)) {
      try {
        notificationMessages = gson.fromJson(messagesAsString, ApolloNotificationMessages.class);
      } catch (Throwable ex) {
        Tracer.logError(ex);
      }
    }

    return notificationMessages;
  }
}
