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
package org.jasig.cas.client.validation;

import java.net.URL;
import org.jasig.cas.client.util.CommonUtils;

/**
 * Abstract class that knows the protocol for validating a CAS ticket.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public abstract class AbstractCasProtocolUrlBasedTicketValidator extends AbstractUrlBasedTicketValidator {

    protected AbstractCasProtocolUrlBasedTicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    /**
     * Retrieves the response from the server by opening a connection and merely reading the response.
     */
    protected final String retrieveResponseFromServer(final URL validationUrl, final String ticket) {
        return CommonUtils.getResponseFromServer(validationUrl, getURLConnectionFactory(), getEncoding());
    }
}
