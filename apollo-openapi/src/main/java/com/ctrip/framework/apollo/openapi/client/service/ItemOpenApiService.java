package com.ctrip.framework.apollo.openapi.client.service;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class ItemOpenApiService extends AbstractOpenApiService {

  public ItemOpenApiService(CloseableHttpClient client, String baseUrl, Gson gson) {
    super(client, baseUrl, gson);
  }

  public OpenItemDTO createItem(String appId, String env, String clusterName, String namespaceName, OpenItemDTO itemDTO) {
    if (Strings.isNullOrEmpty(clusterName)) {
      clusterName = ConfigConsts.CLUSTER_NAME_DEFAULT;
    }
    if (Strings.isNullOrEmpty(namespaceName)) {
      namespaceName = ConfigConsts.NAMESPACE_APPLICATION;
    }

    checkNotEmpty(appId, "App id");
    checkNotEmpty(env, "Env");
    checkNotEmpty(itemDTO.getKey(), "Item key");
    checkNotEmpty(itemDTO.getValue(), "Item value");
    checkNotEmpty(itemDTO.getDataChangeCreatedBy(), "Item created by");

    String path = String.format("envs/%s/apps/%s/clusters/%s/namespaces/%s/items",
        escapePath(env), escapePath(appId), escapePath(clusterName), escapePath(namespaceName));

    try (CloseableHttpResponse response = post(path, itemDTO)) {
      return gson.fromJson(EntityUtils.toString(response.getEntity()), OpenItemDTO.class);
    } catch (Throwable ex) {
      throw new RuntimeException(String
          .format("Create item: %s for appId: %s, cluster: %s, namespace: %s in env: %s failed", itemDTO.getKey(),
              appId, clusterName, namespaceName, env), ex);
    }
  }

  public void updateItem(String appId, String env, String clusterName, String namespaceName, OpenItemDTO itemDTO) {
    if (Strings.isNullOrEmpty(clusterName)) {
      clusterName = ConfigConsts.CLUSTER_NAME_DEFAULT;
    }
    if (Strings.isNullOrEmpty(namespaceName)) {
      namespaceName = ConfigConsts.NAMESPACE_APPLICATION;
    }

    checkNotEmpty(appId, "App id");
    checkNotEmpty(env, "Env");
    checkNotEmpty(itemDTO.getKey(), "Item key");
    checkNotEmpty(itemDTO.getValue(), "Item value");
    checkNotEmpty(itemDTO.getDataChangeLastModifiedBy(), "Item modified by");

    String path = String.format("envs/%s/apps/%s/clusters/%s/namespaces/%s/items/%s",
        escapePath(env), escapePath(appId), escapePath(clusterName), escapePath(namespaceName),
        escapePath(itemDTO.getKey()));

    try (CloseableHttpResponse ignored = put(path, itemDTO)) {
    } catch (Throwable ex) {
      throw new RuntimeException(String
          .format("Update item: %s for appId: %s, cluster: %s, namespace: %s in env: %s failed", itemDTO.getKey(),
              appId, clusterName, namespaceName, env), ex);
    }
  }

  public void createOrUpdateItem(String appId, String env, String clusterName, String namespaceName, OpenItemDTO itemDTO) {
    if (Strings.isNullOrEmpty(clusterName)) {
      clusterName = ConfigConsts.CLUSTER_NAME_DEFAULT;
    }
    if (Strings.isNullOrEmpty(namespaceName)) {
      namespaceName = ConfigConsts.NAMESPACE_APPLICATION;
    }

    checkNotEmpty(appId, "App id");
    checkNotEmpty(env, "Env");
    checkNotEmpty(itemDTO.getKey(), "Item key");
    checkNotEmpty(itemDTO.getValue(), "Item value");
    checkNotEmpty(itemDTO.getDataChangeCreatedBy(), "Item created by");

    if (Strings.isNullOrEmpty(itemDTO.getDataChangeLastModifiedBy())) {
      itemDTO.setDataChangeLastModifiedBy(itemDTO.getDataChangeCreatedBy());
    }

    String path = String.format("envs/%s/apps/%s/clusters/%s/namespaces/%s/items/%s?createIfNotExists=true",
        escapePath(env), escapePath(appId), escapePath(clusterName), escapePath(namespaceName),
        escapePath(itemDTO.getKey()));

    try (CloseableHttpResponse ignored = put(path, itemDTO)) {
    } catch (Throwable ex) {
      throw new RuntimeException(String
          .format("CreateOrUpdate item: %s for appId: %s, cluster: %s, namespace: %s in env: %s failed", itemDTO.getKey(),
              appId, clusterName, namespaceName, env), ex);
    }
  }

  public void removeItem(String appId, String env, String clusterName, String namespaceName, String key, String operator) {
    if (Strings.isNullOrEmpty(clusterName)) {
      clusterName = ConfigConsts.CLUSTER_NAME_DEFAULT;
    }
    if (Strings.isNullOrEmpty(namespaceName)) {
      namespaceName = ConfigConsts.NAMESPACE_APPLICATION;
    }

    checkNotEmpty(appId, "App id");
    checkNotEmpty(env, "Env");
    checkNotEmpty(key, "Item key");
    checkNotEmpty(operator, "Operator");

    String path = String.format("envs/%s/apps/%s/clusters/%s/namespaces/%s/items/%s?operator=%s",
        escapePath(env), escapePath(appId), escapePath(clusterName), escapePath(namespaceName), escapePath(key),
        escapeParam(operator));

    try (CloseableHttpResponse ignored = delete(path)) {
    } catch (Throwable ex) {
      throw new RuntimeException(String
          .format("Remove item: %s for appId: %s, cluster: %s, namespace: %s in env: %s failed", key, appId,
              clusterName, namespaceName, env), ex);
    }

  }
}
