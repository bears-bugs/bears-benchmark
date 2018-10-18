/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.controller.api.resources;

import com.linkedin.pinot.common.Utils;
import com.linkedin.pinot.common.utils.CommonConstants;
import com.linkedin.pinot.common.utils.NetUtil;
import java.io.IOException;
import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;


// A class to add the controller host and version in the response headers for all APIs.
@Singleton
@Provider
public class PinotControllerResponseFilter implements ContainerResponseFilter {
  private static final String CONTROLLER_COMPONENT = "pinot-controller";
  private static final String UNKNOWN = "Unknown";

  private final String _controllerHost;
  private final String _controllerVersion;

  public PinotControllerResponseFilter() {
    String controllerHost = NetUtil.getHostnameOrAddress();
    if (controllerHost != null) {
      _controllerHost = controllerHost;
    } else {
      _controllerHost = UNKNOWN;
    }
    String controllerVersion = Utils.getComponentVersions().get(CONTROLLER_COMPONENT);
    if (controllerVersion != null) {
      _controllerVersion = controllerVersion;
    } else {
      _controllerVersion = UNKNOWN;
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {
    responseContext.getHeaders().putSingle(CommonConstants.Controller.HOST_HTTP_HEADER, _controllerHost);
    responseContext.getHeaders().putSingle(CommonConstants.Controller.VERSION_HTTP_HEADER, _controllerVersion);
  }
}
