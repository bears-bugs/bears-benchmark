/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.client.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class DefaultGatewayResolverImpl implements GatewayResolver {

    public static final String CONST_CAS_GATEWAY = "_const_cas_gateway_";

    public boolean hasGatewayedAlready(final HttpServletRequest request, final String serviceUrl) {
        final HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        final boolean result = session.getAttribute(CONST_CAS_GATEWAY) != null;
        return result;
    }

    public String storeGatewayInformation(final HttpServletRequest request, final String serviceUrl) {
        request.getSession(true).setAttribute(CONST_CAS_GATEWAY, "yes");
        return serviceUrl;
    }
}
