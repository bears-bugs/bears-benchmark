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

import org.jasig.cas.client.Protocol;

/**
 * Extension to the default Authentication filter that sets the required SAML1.1 artifact parameter name and service parameter name.
 * <p>
 * Note, as of 3.3, the final keyword was removed to allow you to override the method to retrieve tickets, per CASC-154
 *
 * @author Scott Battaglia
 * @since 3.1.12
 * @version $Revision$ $Date$
 */
public class Saml11AuthenticationFilter extends AuthenticationFilter {

    public Saml11AuthenticationFilter() {
        super(Protocol.SAML11);
    }
}
