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

package org.apache.servicecomb.transport.rest.vertx.accesslog.element.impl;

import org.apache.servicecomb.transport.rest.vertx.accesslog.AccessLogParam;
import org.apache.servicecomb.transport.rest.vertx.accesslog.element.AccessLogItem;
import org.springframework.util.StringUtils;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.RoutingContext;

public class RemoteHostItem implements AccessLogItem<RoutingContext> {

  public static final String EMPTY_RESULT = "-";

  @Override
  public String getFormattedItem(AccessLogParam<RoutingContext> accessLogParam) {
    HttpServerRequest request = accessLogParam.getContextData().request();
    if (null == request) {
      return EMPTY_RESULT;
    }

    SocketAddress remoteAddress = request.remoteAddress();
    if (null == remoteAddress) {
      return EMPTY_RESULT;
    }

    String remoteHost = remoteAddress.host();
    if (StringUtils.isEmpty(remoteHost)) {
      return EMPTY_RESULT;
    }
    return remoteHost;
  }
}
