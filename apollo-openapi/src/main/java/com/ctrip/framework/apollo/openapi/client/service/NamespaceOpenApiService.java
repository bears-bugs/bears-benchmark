package com.ctrip.framework.apollo.openapi.client.service;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.openapi.dto.OpenAppNamespaceDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceLockDTO;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class NamespaceOpenApiService extends AbstractOpenApiService {
  private static final Type OPEN_NAMESPACE_DTO_LIST_TYPE = new TypeToken<List<OpenNamespaceDTO>>() {
  }.getType();

  public NamespaceOpenApiService(CloseableHttpClient client, String baseUrl, Gson gson) {
    super(client, baseUrl, gson);
  }

  public OpenNamespaceDTO getNamespace(String appId, String env, String clusterName, String namespaceName) {
    if (Strings.isNullOrEmpty(clusterName)) {
      clusterName = ConfigConsts.CLUSTER_NAME_DEFAULT;
    }
    if (Strings.isNullOrEmpty(namespaceName)) {
      namespaceName = ConfigConsts.NAMESPACE_APPLICATION;
    }

    checkNotEmpty(appId, "App id");
    checkNotEmpty(env, "Env");

    String path = String.format("envs/%s/apps/%s/clusters/%s/namespaces/%s", escapePath(env), escapePath(appId),
        escapePath(clusterName), escapePath(namespaceName));

    try (CloseableHttpResponse response = get(path)) {
      return gson.fromJson(EntityUtils.toString(response.getEntity()), OpenNamespaceDTO.class);
    } catch (Throwable ex) {
      throw new RuntimeException(String
          .format("Get namespace for appId: %s, cluster: %s, namespace: %s in env: %s failed", appId, clusterName,
              namespaceName, env), ex);
    }
  }

  public List<OpenNamespaceDTO> getNamespaces(String appId, String env, String clusterName) {
    if (Strings.isNullOrEmpty(clusterName)) {
      clusterName = ConfigConsts.CLUSTER_NAME_DEFAULT;
    }

    checkNotEmpty(appId, "App id");
    checkNotEmpty(env, "Env");

    String path = String.format("envs/%s/apps/%s/clusters/%s/namespaces", escapePath(env), escapePath(appId),
        escapePath(clusterName));

    try (CloseableHttpResponse response = get(path)) {
      return gson.fromJson(EntityUtils.toString(response.getEntity()), OPEN_NAMESPACE_DTO_LIST_TYPE);
    } catch (Throwable ex) {
      throw new RuntimeException(String
          .format("Get namespaces for appId: %s, cluster: %s in env: %s failed", appId, clusterName, env), ex);
    }
  }

  public OpenAppNamespaceDTO createAppNamespace(OpenAppNamespaceDTO appNamespaceDTO) {
    checkNotEmpty(appNamespaceDTO.getAppId(), "App id");
    checkNotEmpty(appNamespaceDTO.getName(), "Name");
    checkNotEmpty(appNamespaceDTO.getDataChangeCreatedBy(), "Created by");

    if (Strings.isNullOrEmpty(appNamespaceDTO.getFormat())) {
      appNamespaceDTO.setFormat(ConfigFileFormat.Properties.getValue());
    }

    String path = String.format("apps/%s/appnamespaces", escapePath(appNamespaceDTO.getAppId()));

    try (CloseableHttpResponse response = post(path, appNamespaceDTO)) {
      return gson.fromJson(EntityUtils.toString(response.getEntity()), OpenAppNamespaceDTO.class);
    } catch (Throwable ex) {
      throw new RuntimeException(String
          .format("Create app namespace: %s for appId: %s, format: %s failed", appNamespaceDTO.getName(),
              appNamespaceDTO.getAppId(), appNamespaceDTO.getFormat()), ex);
    }
  }

  public OpenNamespaceLockDTO getNamespaceLock(String appId, String env, String clusterName, String namespaceName) {
    if (Strings.isNullOrEmpty(clusterName)) {
      clusterName = ConfigConsts.CLUSTER_NAME_DEFAULT;
    }
    if (Strings.isNullOrEmpty(namespaceName)) {
      namespaceName = ConfigConsts.NAMESPACE_APPLICATION;
    }

    checkNotEmpty(appId, "App id");
    checkNotEmpty(env, "Env");

    String path = String.format("envs/%s/apps/%s/clusters/%s/namespaces/%s/lock", escapePath(env), escapePath(appId),
        escapePath(clusterName), escapePath(namespaceName));

    try (CloseableHttpResponse response = get(path)) {
      return gson.fromJson(EntityUtils.toString(response.getEntity()), OpenNamespaceLockDTO.class);
    } catch (Throwable ex) {
      throw new RuntimeException(String
          .format("Get namespace lock for appId: %s, cluster: %s, namespace: %s in env: %s failed", appId, clusterName,
              namespaceName, env), ex);
    }
  }
}
