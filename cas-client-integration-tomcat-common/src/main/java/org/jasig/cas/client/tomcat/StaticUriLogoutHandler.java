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
package org.jasig.cas.client.tomcat;

import javax.servlet.http.HttpServletRequest;
import org.jasig.cas.client.util.CommonUtils;

/**
 * Performs CAS logout when the request URI matches a fixed context-relative
 * URI.
 *
 * @author Marvin S. Addison
 * @version $Revision$
 * @since 3.1.12
 *
 */
public final class StaticUriLogoutHandler extends AbstractLogoutHandler {

    private String logoutUri;

    /**
     * The logout URI to watch for logout requests.
     *
     * @param logoutUri  Logout URI.  CANNOT be null.  MUST be relative and start with "/"
     */
    public void setLogoutUri(final String logoutUri) {
        this.logoutUri = logoutUri;
    }

    /**
     * Initializes the component for use.
     */
    public void init() {
        CommonUtils.assertNotNull(this.logoutUri, "logoutUri cannot be null.");
        CommonUtils.assertTrue(this.logoutUri.startsWith("/"), "logoutUri must start with \"/\"");
    }

    /** {@inheritDoc} */
    public boolean isLogoutRequest(final HttpServletRequest request) {
        return this.logoutUri.equals(request.getRequestURI());
    }

}
