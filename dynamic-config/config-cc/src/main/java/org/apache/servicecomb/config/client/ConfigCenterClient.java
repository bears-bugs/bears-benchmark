/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.config.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.config.archaius.sources.ConfigCenterConfigurationSourceImpl;
import org.apache.servicecomb.foundation.auth.AuthHeaderProvider;
import org.apache.servicecomb.foundation.auth.SignRequest;
import org.apache.servicecomb.foundation.common.event.EventManager;
import org.apache.servicecomb.foundation.common.net.IpPort;
import org.apache.servicecomb.foundation.common.net.NetUtils;
import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.foundation.ssl.SSLCustom;
import org.apache.servicecomb.foundation.ssl.SSLOption;
import org.apache.servicecomb.foundation.ssl.SSLOptionFactory;
import org.apache.servicecomb.foundation.vertx.AddressResolverConfig;
import org.apache.servicecomb.foundation.vertx.VertxTLSBuilder;
import org.apache.servicecomb.foundation.vertx.VertxUtils;
import org.apache.servicecomb.foundation.vertx.client.ClientPoolManager;
import org.apache.servicecomb.foundation.vertx.client.ClientVerticle;
import org.apache.servicecomb.foundation.vertx.client.http.HttpClientPoolFactory;
import org.apache.servicecomb.foundation.vertx.client.http.HttpClientWithContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.impl.FrameType;
import io.vertx.core.http.impl.ws.WebSocketFrameImpl;
import io.vertx.core.net.ProxyOptions;

/**
 * Created by on 2016/5/17.
 */

public class ConfigCenterClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigCenterClient.class);

  private static final ConfigCenterConfig CONFIG_CENTER_CONFIG = ConfigCenterConfig.INSTANCE;

  private static final String SSL_KEY = "cc.consumer";

  private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1);

  private static final long HEARTBEAT_INTERVAL = 30000;

  private ScheduledExecutorService heartbeatTask = null;

  private int refreshMode = CONFIG_CENTER_CONFIG.getRefreshMode();

  private int refreshInterval = CONFIG_CENTER_CONFIG.getRefreshInterval();

  private int firstRefreshInterval = CONFIG_CENTER_CONFIG.getFirstRefreshInterval();

  private int refreshPort = CONFIG_CENTER_CONFIG.getRefreshPort();

  private String tenantName = CONFIG_CENTER_CONFIG.getTenantName();

  private String serviceName = CONFIG_CENTER_CONFIG.getServiceName();

  private String environment = CONFIG_CENTER_CONFIG.getEnvironment();

  private MemberDiscovery memberDiscovery = new MemberDiscovery(CONFIG_CENTER_CONFIG.getServerUri());

  private ConfigCenterConfigurationSourceImpl.UpdateHandler updateHandler;

  private static ClientPoolManager<HttpClientWithContext> clientMgr;

  private boolean isWatching = false;

  private static final ServiceLoader<AuthHeaderProvider> authHeaderProviders =
      ServiceLoader.load(AuthHeaderProvider.class);

  public ConfigCenterClient(ConfigCenterConfigurationSourceImpl.UpdateHandler updateHandler) {
    this.updateHandler = updateHandler;
  }

  public void connectServer() {
    if (refreshMode != 0 && refreshMode != 1) {
      LOGGER.error("refreshMode must be 0 or 1.");
      return;
    }
    ParseConfigUtils parseConfigUtils = new ParseConfigUtils(updateHandler);
    try {
      deployConfigClient();
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
    refreshMembers(memberDiscovery);
    EXECUTOR.scheduleWithFixedDelay(new ConfigRefresh(parseConfigUtils, memberDiscovery),
        firstRefreshInterval,
        refreshInterval,
        TimeUnit.MILLISECONDS);
  }

  private void refreshMembers(MemberDiscovery memberDiscovery) {
    if (CONFIG_CENTER_CONFIG.getAutoDiscoveryEnabled()) {
      String configCenter = memberDiscovery.getConfigServer();
      IpPort ipPort = NetUtils.parseIpPortFromURI(configCenter);
      clientMgr.findThreadBindClientPool().runOnContext(client -> {
        HttpClientRequest request = client.get(ipPort.getPort(), ipPort.getHostOrIp(), URIConst.MEMBERS, rsp -> {
          if (rsp.statusCode() == HttpResponseStatus.OK.code()) {
            rsp.bodyHandler(buf -> {
              memberDiscovery.refreshMembers(buf.toJsonObject());
            });
          }
        });
        SignRequest signReq = createSignRequest(request.method().toString(),
            configCenter + URIConst.MEMBERS,
            new HashMap<>(),
            null);
        if (ConfigCenterConfig.INSTANCE.getToken() != null) {
          request.headers().add("X-Auth-Token", ConfigCenterConfig.INSTANCE.getToken());
        }
        authHeaderProviders.forEach(provider -> request.headers().addAll(provider.getSignAuthHeaders(signReq)));
        request.end();
      });
    }
  }

  private void deployConfigClient() throws InterruptedException {
    VertxOptions vertxOptions = new VertxOptions();
    vertxOptions.setAddressResolverOptions(AddressResolverConfig.getAddressResover(SSL_KEY,
        ConfigCenterConfig.INSTANCE.getConcurrentCompositeConfiguration()));
    Vertx vertx = VertxUtils.getOrCreateVertxByName("config-center", vertxOptions);

    HttpClientOptions httpClientOptions = createHttpClientOptions();
    clientMgr = new ClientPoolManager<>(vertx, new HttpClientPoolFactory(httpClientOptions));

    DeploymentOptions deployOptions = VertxUtils.createClientDeployOptions(clientMgr, 1);
    VertxUtils.blockDeploy(vertx, ClientVerticle.class, deployOptions);
  }

  private HttpClientOptions createHttpClientOptions() {
    HttpClientOptions httpClientOptions = new HttpClientOptions();
    if (ConfigCenterConfig.INSTANCE.isProxyEnable()) {
      ProxyOptions proxy = new ProxyOptions()
          .setHost(ConfigCenterConfig.INSTANCE.getProxyHost())
          .setPort(ConfigCenterConfig.INSTANCE.getProxyPort())
          .setUsername(ConfigCenterConfig.INSTANCE.getProxyUsername())
          .setPassword(ConfigCenterConfig.INSTANCE.getProxyPasswd());
      httpClientOptions.setProxyOptions(proxy);
    }
    httpClientOptions.setConnectTimeout(CONFIG_CENTER_CONFIG.getConnectionTimeout());
    if (this.memberDiscovery.getConfigServer().toLowerCase().startsWith("https")) {
      LOGGER.debug("config center client performs requests over TLS");
      SSLOptionFactory factory = SSLOptionFactory.createSSLOptionFactory(SSL_KEY,
          ConfigCenterConfig.INSTANCE.getConcurrentCompositeConfiguration());
      SSLOption sslOption;
      if (factory == null) {
        sslOption = SSLOption.buildFromYaml(SSL_KEY,
            ConfigCenterConfig.INSTANCE.getConcurrentCompositeConfiguration());
      } else {
        sslOption = factory.createSSLOption();
      }
      SSLCustom sslCustom = SSLCustom.createSSLCustom(sslOption.getSslCustomClass());
      VertxTLSBuilder.buildHttpClientOptions(sslOption, sslCustom, httpClientOptions);
    }
    return httpClientOptions;
  }

  class ConfigRefresh implements Runnable {
    private ParseConfigUtils parseConfigUtils;

    private MemberDiscovery memberdis;

    ConfigRefresh(ParseConfigUtils parseConfigUtils, MemberDiscovery memberdis) {
      this.parseConfigUtils = parseConfigUtils;
      this.memberdis = memberdis;
    }

    // 具体动作
    @Override
    public void run() {
      // this will be single threaded, so we don't care about concurrent
      // staffs
      try {
        String configCenter = memberdis.getConfigServer();
        if (refreshMode == 1) {
          refreshConfig(configCenter);
        } else if (!isWatching) {
          // 重新监听时需要先加载，避免在断开期间丢失变更
          refreshConfig(configCenter);
          doWatch(configCenter);
        }
      } catch (Exception e) {
        LOGGER.error("client refresh thread exception", e);
      }
    }

    // create watch and wait for done
    public void doWatch(String configCenter)
        throws UnsupportedEncodingException, InterruptedException {
      CountDownLatch waiter = new CountDownLatch(1);
      IpPort ipPort = NetUtils.parseIpPortFromURI(configCenter);
      String url = URIConst.REFRESH_ITEMS + "?dimensionsInfo="
          + StringUtils.deleteWhitespace(URLEncoder.encode(serviceName, "UTF-8"));
      Map<String, String> headers = new HashMap<>();
      headers.put("x-domain-name", tenantName);
      if (ConfigCenterConfig.INSTANCE.getToken() != null) {
        headers.put("X-Auth-Token", ConfigCenterConfig.INSTANCE.getToken());
      }
      headers.put("x-environment", environment);

      HttpClientWithContext vertxHttpClient = clientMgr.findThreadBindClientPool();
      vertxHttpClient.runOnContext(client -> {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaderProviders.forEach(provider -> authHeaders.putAll(provider.getSignAuthHeaders(
            createSignRequest(null, configCenter + url, headers, null))));

        client.websocket(refreshPort,
            ipPort.getHostOrIp(),
            url,
            new CaseInsensitiveHeaders().addAll(headers)
                .addAll(authHeaders),
            ws -> {
              ws.exceptionHandler(e -> {
                LOGGER.error("watch config read fail", e);
                stopHeartBeatThread();
                isWatching = false;
              });
              ws.closeHandler(v -> {
                LOGGER.warn("watching config connection is closed accidentally");
                stopHeartBeatThread();
                isWatching = false;
              });
              ws.handler(action -> {
                LOGGER.info("watching config recieved {}", action);
                Map<String, Object> mAction = action.toJsonObject().getMap();
                if ("CREATE".equals(mAction.get("action"))) {
                  refreshConfig(configCenter);
                } else if ("MEMBER_CHANGE".equals(mAction.get("action"))) {
                  refreshMembers(memberdis);
                } else {
                  parseConfigUtils.refreshConfigItemsIncremental(mAction);
                }
              });
              startHeartBeatThread(ws);
              isWatching = true;
              waiter.countDown();
            },
            e -> {
              LOGGER.error("watcher connect to config center {} refresh port {} failed. Error message is [{}]", configCenter, refreshPort, e.getMessage());
              waiter.countDown();
            });
      });
      waiter.await();
    }

    private void startHeartBeatThread(WebSocket ws) {
      heartbeatTask = Executors.newScheduledThreadPool(1);
      heartbeatTask.scheduleWithFixedDelay(() -> sendHeartbeat(ws),
          HEARTBEAT_INTERVAL,
          HEARTBEAT_INTERVAL,
          TimeUnit.MILLISECONDS);
    }

    private void stopHeartBeatThread() {
      if (heartbeatTask != null) {
        heartbeatTask.shutdownNow();
      }
    }

    private void sendHeartbeat(WebSocket ws) {
      try {
        ws.writeFrame(new WebSocketFrameImpl(FrameType.PING));
        EventManager.post(new ConnSuccEvent());
      } catch (IllegalStateException e) {
        EventManager.post(new ConnFailEvent("heartbeat fail, " + e.getMessage()));
        LOGGER.error("heartbeat fail", e);
      }
    }

    public void refreshConfig(String configcenter) {
      clientMgr.findThreadBindClientPool().runOnContext(client -> {
        String path = URIConst.ITEMS + "?dimensionsInfo=" + StringUtils.deleteWhitespace(serviceName);
        IpPort ipPort = NetUtils.parseIpPortFromURI(configcenter);
        HttpClientRequest request = client.get(ipPort.getPort(), ipPort.getHostOrIp(), path, rsp -> {
          if (rsp.statusCode() == HttpResponseStatus.OK.code()) {
            rsp.bodyHandler(buf -> {
              try {
                parseConfigUtils
                    .refreshConfigItems(JsonUtils.OBJ_MAPPER.readValue(buf.toString(),
                        new TypeReference<LinkedHashMap<String, Map<String, String>>>() {
                        }));
                EventManager.post(new ConnSuccEvent());
              } catch (IOException e) {
                EventManager.post(new ConnFailEvent("config refresh result parse fail " + e.getMessage()));
                LOGGER.error("Config refresh from {} failed. Error message is [{}].", configcenter, e.getMessage());
              }
            });
          } else {
            rsp.bodyHandler(buf -> {
              LOGGER.error("Server error message is [{}].", buf);
            });
            EventManager.post(new ConnFailEvent("fetch config fail"));
            LOGGER.error("Config refresh from {} failed.", configcenter);
          }
        });
        Map<String, String> headers = new HashMap<>();
        headers.put("x-domain-name", tenantName);
        if (ConfigCenterConfig.INSTANCE.getToken() != null) {
          headers.put("X-Auth-Token", ConfigCenterConfig.INSTANCE.getToken());
        }
        headers.put("x-environment", environment);
        request.headers().addAll(headers);
        authHeaderProviders.forEach(provider -> request.headers()
            .addAll(provider.getSignAuthHeaders(createSignRequest(request.method().toString(),
                configcenter + path,
                headers,
                null))));
        request.exceptionHandler(e -> {
          EventManager.post(new ConnFailEvent("fetch config fail"));
          LOGGER.error("Config refresh from {} failed. Error message is [{}].", configcenter, e.getMessage());
        });
        request.end();
      });
    }
  }

  public static SignRequest createSignRequest(String method, String endpoint, Map<String, String> headers,
      InputStream content) {
    SignRequest signReq = new SignRequest();
    try {
      signReq.setEndpoint(new URI(endpoint));
    } catch (URISyntaxException e) {
      LOGGER.warn("set uri failed, uri is {}, message: {}", endpoint, e.getMessage());
    }

    Map<String, String[]> queryParams = new HashMap<>();
    if (endpoint.contains("?")) {
      String parameters = endpoint.substring(endpoint.indexOf("?") + 1);
      if (null != parameters && !"".equals(parameters)) {
        String[] parameterarray = parameters.split("&");
        for (String p : parameterarray) {
          String key = p.split("=")[0];
          String value = p.split("=")[1];
          if (!queryParams.containsKey(key)) {
            queryParams.put(key, new String[] {value});
          } else {
            List<String> vals = new ArrayList<>(Arrays.asList(queryParams.get(key)));
            vals.add(value);
            queryParams.put(key, vals.toArray(new String[vals.size()]));
          }
        }
      }
    }
    signReq.setQueryParams(queryParams);

    signReq.setHeaders(headers);
    signReq.setHttpMethod(method);
    signReq.setContent(content);
    return signReq;
  }
}
