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
package org.jasig.cas.client.tomcat.v85;

import org.apache.catalina.LifecycleException;
import org.jasig.cas.client.tomcat.LogoutHandler;
import org.jasig.cas.client.tomcat.RegexUriLogoutHandler;

/**
 * Performs CAS logout when the request URI matches a regular expression.
 *
 * @author Scott Battaglia
 * @author Marvin S. Addison
 * @version $Revision$ $Date$
 * @since 3.1.12
 */
public final class RegexUriLogoutValve extends AbstractLogoutValve {

    private RegexUriLogoutHandler logoutHandler = new RegexUriLogoutHandler();

    public void setRedirectUrl(final String redirectUrl) {
        this.logoutHandler.setRedirectUrl(redirectUrl);
    }

    public void setLogoutUriRegex(final String regex) {
        this.logoutHandler.setLogoutUriRegex(regex);
    }

    protected void startInternal() throws LifecycleException {
        super.startInternal();
        this.logoutHandler.init();
        logger.info("Startup completed.");
    }

    /** {@inheritDoc} */
    protected LogoutHandler getLogoutHandler() {
        return this.logoutHandler;
    }
}
