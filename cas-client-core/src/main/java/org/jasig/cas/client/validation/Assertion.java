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

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 * Represents a response to a validation request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public interface Assertion extends Serializable {

    /**
     * The date from which the assertion is valid from.
     *
     * @return the valid from date.
     */
    Date getValidFromDate();

    /**
     * The date which the assertion is valid until.
     *
     * @return the valid until date.
     */
    Date getValidUntilDate();

    /**
     * The date the authentication actually occurred on.  If its unable to be determined, it should be set to the current
     * time.
     *
     * @return the authentication date, or the current time if it can't be determined.
     */
    Date getAuthenticationDate();

    /**
     * The key/value pairs associated with this assertion.
     *
     * @return the map of attributes.
     */
    Map<String, Object> getAttributes();

    /**
     * The principal for which this assertion is valid.
     *
     * @return the principal.
     */
    AttributePrincipal getPrincipal();

    /**
     * Determines whether an Assertion is considered usable or not.  A naive implementation may just check the date validity.
     *
     * @return true if its valid, false otherwise.
     * @since 3.3.0 (though in 3.3.0, no one actually calls this)
     */
    boolean isValid();
}
