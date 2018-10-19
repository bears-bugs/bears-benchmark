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

import java.util.Arrays;
import java.util.List;
import org.jasig.cas.client.util.XmlUtils;

/**
 * Extension to the traditional Service Ticket validation that will validate service tickets and proxy tickets.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
public class Cas20ProxyTicketValidator extends Cas20ServiceTicketValidator {

    private boolean acceptAnyProxy;

    /** This should be a list of an array of Strings */
    private ProxyList allowedProxyChains = new ProxyList();

    /** Allows for an empty chain of proxy callback urls. **/
    private boolean allowEmptyProxyChain = true;

    public Cas20ProxyTicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    protected final ProxyList getAllowedProxyChains() {
        return this.allowedProxyChains;
    }

    protected String getUrlSuffix() {
        return "proxyValidate";
    }

    @Override
    protected void customParseResponse(final String response, final Assertion assertion)
            throws TicketValidationException {
        final List<String> proxies = parseProxiesFromResponse(response);

        if (proxies == null) {
            throw new InvalidProxyChainTicketValidationException(
                    "Invalid proxy chain: No proxy could be retrieved from response. "
                    + "This indicates a problem with CAS validation. Review logs/configuration to find the root cause."
            );
        }
        // this means there was nothing in the proxy chain, which is okay
        if (this.allowEmptyProxyChain && proxies.isEmpty()) {
            logger.debug("Found an empty proxy chain, permitted by client configuration");
            return;
        }

        if (this.acceptAnyProxy) {
            logger.debug("Client configuration accepts any proxy. "
                    + "It is generally dangerous to use a non-proxied CAS filter "
                    + "specially for protecting resources that require proxy access.");
            return;
        }

        final String[] proxiedList = proxies.toArray(new String[proxies.size()]);
        if (this.allowedProxyChains.contains(proxiedList)) {
            return;
        }

        logger.warn("Proxies received from the CAS validation response are {}. "
                + "However, none are allowed by allowed proxy chain of the client which is {}",
                Arrays.toString(proxiedList), this.allowedProxyChains);

        throw new InvalidProxyChainTicketValidationException("Invalid proxy chain: " + proxies.toString());
    }

    protected List<String> parseProxiesFromResponse(final String response) {
        return XmlUtils.getTextForElements(response, "proxy");
    }

    public final void setAcceptAnyProxy(final boolean acceptAnyProxy) {
        this.acceptAnyProxy = acceptAnyProxy;
    }

    public final void setAllowedProxyChains(final ProxyList allowedProxyChains) {
        this.allowedProxyChains = allowedProxyChains;
    }

    protected final boolean isAcceptAnyProxy() {
        return this.acceptAnyProxy;
    }

    protected final boolean isAllowEmptyProxyChain() {
        return this.allowEmptyProxyChain;
    }

    /**
     * Set to determine whether empty proxy chains are allowed.
     * @see #customParseResponse(String, Assertion)
     * @param allowEmptyProxyChain whether to allow empty proxy chains or not.  True if so, false otherwise.
     */
    public final void setAllowEmptyProxyChain(final boolean allowEmptyProxyChain) {
        this.allowEmptyProxyChain = allowEmptyProxyChain;
    }
}
