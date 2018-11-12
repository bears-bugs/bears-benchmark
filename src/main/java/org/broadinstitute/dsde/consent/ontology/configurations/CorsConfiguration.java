package org.broadinstitute.dsde.consent.ontology.configurations;

/**
 * see https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS to understand CORS.
 *
 * This config tracks the third-party domains that are allowed to access the autocomplete service
 * via JavaScript. For instance, the Vault Portal accesses the autocomplete service, over Ajax, to
 * assist the end user in creating research purposes.
 *
 */
public class CorsConfiguration {
    // default to the empty string, i.e. no cross-domain access allowed
    public String allowedDomains = "";
}
