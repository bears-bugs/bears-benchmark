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

package org.apache.servicecomb.transport.rest.client.http;

import java.util.List;

import javax.servlet.http.Part;

import org.apache.servicecomb.common.rest.RestConst;
import org.apache.servicecomb.common.rest.codec.param.RestClientRequestImpl;
import org.apache.servicecomb.common.rest.definition.RestOperationMeta;
import org.apache.servicecomb.common.rest.filter.HttpClientFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.definition.OperationMeta;
import org.apache.servicecomb.core.transport.AbstractTransport;
import org.apache.servicecomb.foundation.common.http.HttpStatus;
import org.apache.servicecomb.foundation.common.net.IpPort;
import org.apache.servicecomb.foundation.common.net.URIEndpointObject;
import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.foundation.vertx.client.http.HttpClientWithContext;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.HttpServletResponseEx;
import org.apache.servicecomb.foundation.vertx.http.ReadStreamPart;
import org.apache.servicecomb.foundation.vertx.http.VertxClientRequestToHttpServletRequest;
import org.apache.servicecomb.foundation.vertx.http.VertxClientResponseToHttpServletResponse;
import org.apache.servicecomb.serviceregistry.api.Const;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;

public class RestClientInvocation {
  private static final Logger LOGGER = LoggerFactory.getLogger(RestClientInvocation.class);

  private HttpClientWithContext httpClientWithContext;

  private Invocation invocation;

  private AsyncResponse asyncResp;

  private List<HttpClientFilter> httpClientFilters;

  private HttpClientRequest clientRequest;

  private HttpClientResponse clientResponse;

  public RestClientInvocation(HttpClientWithContext httpClientWithContext, List<HttpClientFilter> httpClientFilters) {
    this.httpClientWithContext = httpClientWithContext;
    this.httpClientFilters = httpClientFilters;
  }

  public void invoke(Invocation invocation, AsyncResponse asyncResp) throws Exception {
    this.invocation = invocation;
    this.asyncResp = asyncResp;

    OperationMeta operationMeta = invocation.getOperationMeta();
    RestOperationMeta swaggerRestOperation = operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION);

    String path = this.createRequestPath(swaggerRestOperation);
    IpPort ipPort = (IpPort) invocation.getEndpoint().getAddress();

    createRequest(ipPort, path);
    clientRequest.putHeader(org.apache.servicecomb.core.Const.TARGET_MICROSERVICE, invocation.getMicroserviceName());
    RestClientRequestImpl restClientRequest =
        new RestClientRequestImpl(clientRequest, httpClientWithContext.context().owner(), asyncResp);
    invocation.getHandlerContext().put(RestConst.INVOCATION_HANDLER_REQUESTCLIENT, restClientRequest);

    Buffer requestBodyBuffer = restClientRequest.getBodyBuffer();
    HttpServletRequestEx requestEx = new VertxClientRequestToHttpServletRequest(clientRequest, requestBodyBuffer);
    for (HttpClientFilter filter : httpClientFilters) {
      filter.beforeSendRequest(invocation, requestEx);
    }

    clientRequest.exceptionHandler(e -> {
      LOGGER.error("Failed to send request to {}.", ipPort.getSocketAddress(), e);
      asyncResp.fail(invocation.getInvocationType(), e);
    });
    clientRequest.connectionHandler(connection -> {
      LOGGER.info("http connection connected, local:{}, remote:{}.",
          connection.localAddress(),
          connection.remoteAddress());
      connection.closeHandler(v -> {
        LOGGER.info("http connection closed, local:{}, remote:{}.",
            connection.localAddress(),
            connection.remoteAddress());
      });
      connection.exceptionHandler(e -> {
        LOGGER.info("http connection exception, local:{}, remote:{}.",
            connection.localAddress(),
            connection.remoteAddress(),
            e);
      });
    });
    // 从业务线程转移到网络线程中去发送
    httpClientWithContext.runOnContext(httpClient -> {
      this.setCseContext();
      clientRequest.setTimeout(AbstractTransport.getRequestTimeoutProperty().get());
      try {
        restClientRequest.end();
      } catch (Throwable e) {
        LOGGER.error("send http request failed,", e);
        asyncResp.fail(invocation.getInvocationType(), e);
      }
    });
  }

  private HttpMethod getMethod() {
    OperationMeta operationMeta = invocation.getOperationMeta();
    RestOperationMeta swaggerRestOperation = operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION);
    String method = swaggerRestOperation.getHttpMethod();
    return HttpMethod.valueOf(method);
  }

  void createRequest(IpPort ipPort, String path) {
    URIEndpointObject endpoint = (URIEndpointObject) invocation.getEndpoint().getAddress();
    RequestOptions requestOptions = new RequestOptions();
    requestOptions.setHost(ipPort.getHostOrIp())
        .setPort(ipPort.getPort())
        .setSsl(endpoint.isSslEnabled())
        .setURI(path);

    HttpMethod method = getMethod();
    LOGGER.debug("Sending request by rest, method={}, qualifiedName={}, path={}, endpoint={}.",
        method,
        invocation.getMicroserviceQualifiedName(),
        path,
        invocation.getEndpoint().getEndpoint());
    clientRequest = httpClientWithContext.getHttpClient().request(method, requestOptions, this::handleResponse);
  }

  protected void handleResponse(HttpClientResponse httpClientResponse) {
    this.clientResponse = httpClientResponse;

    if (HttpStatus.isSuccess(clientResponse.statusCode())
        && Part.class.equals(invocation.getOperationMeta().getMethod().getReturnType())) {
      ReadStreamPart part = new ReadStreamPart(httpClientWithContext.context(), httpClientResponse);
      invocation.getHandlerContext().put(RestConst.READ_STREAM_PART, part);
      processResponseBody(null);
      return;
    }

    httpClientResponse.exceptionHandler(e -> {
      LOGGER.error("Failed to receive response from {}.", httpClientResponse.netSocket().remoteAddress(), e);
      asyncResp.fail(invocation.getInvocationType(), e);
    });

    clientResponse.bodyHandler(responseBuf -> {
      processResponseBody(responseBuf);
    });
  }

  protected void processResponseBody(Buffer responseBuf) {
    invocation.getResponseExecutor().execute(() -> {
      try {
        HttpServletResponseEx responseEx =
            new VertxClientResponseToHttpServletResponse(clientResponse, responseBuf);
        for (HttpClientFilter filter : httpClientFilters) {
          Response response = filter.afterReceiveResponse(invocation, responseEx);
          if (response != null) {
            asyncResp.complete(response);
            return;
          }
        }
      } catch (Throwable e) {
        asyncResp.fail(invocation.getInvocationType(), e);
      }
    });
  }

  protected void setCseContext() {
    try {
      String cseContext = JsonUtils.writeValueAsString(invocation.getContext());
      clientRequest.putHeader(org.apache.servicecomb.core.Const.CSE_CONTEXT, cseContext);
    } catch (Exception e) {
      LOGGER.debug("Failed to encode and set cseContext.", e);
    }
  }

  protected String createRequestPath(RestOperationMeta swaggerRestOperation) throws Exception {
    URIEndpointObject address = (URIEndpointObject) invocation.getEndpoint().getAddress();
    String urlPrefix = address.getFirst(Const.URL_PREFIX);

    String path = (String) invocation.getHandlerContext().get(RestConst.REST_CLIENT_REQUEST_PATH);
    if (path == null) {
      path = swaggerRestOperation.getPathBuilder().createRequestPath(invocation.getArgs());
    }

    if (StringUtils.isEmpty(urlPrefix) || path.startsWith(urlPrefix)) {
      return path;
    }

    return urlPrefix + path;
  }
}
