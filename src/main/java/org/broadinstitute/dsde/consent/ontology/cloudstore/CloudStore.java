package org.broadinstitute.dsde.consent.ontology.cloudstore;

import com.google.api.client.http.HttpResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface CloudStore {

    HttpResponse getStorageDocument(String documentSuffix) throws IOException, GeneralSecurityException;

}
