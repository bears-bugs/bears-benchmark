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
import javax.servlet.http.HttpServletResponse;

/**
 * Strategy pattern interface for ending a CAS authentication session.
 *
 * @author Marvin S. Addison
 * @version $Revision$
 * @since 3.1.12
 *
 */
public interface LogoutHandler {
    /**
     * Determines whether the given request is a logout request.
     *
     * @param request HTTP request.
     *
     * @return True if request is a logout request, false otherwise.
     */
    boolean isLogoutRequest(HttpServletRequest request);

    /**
     * Ends the current authenticated user session bound to the given request.
     * The response is provided to allow the handler to customize the response
     * behavior on logout as needed.
     * 
     * @param request HTTP request.
     * @param response HTTP response.
     */
    void logout(HttpServletRequest request, HttpServletResponse response);
}
