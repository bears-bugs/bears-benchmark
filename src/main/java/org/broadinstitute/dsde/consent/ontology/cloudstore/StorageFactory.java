package org.broadinstitute.dsde.consent.ontology.cloudstore;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;

public class StorageFactory {

    private static Storage instance = null;
    private static String STORAGE_APPLICATION_NAME = "DUOS Oversight Ontology";

    public static synchronized Storage getService(String password) throws IOException, GeneralSecurityException {
        if (instance == null) {
            instance = buildService(password);
        }
        return instance;
    }

    private static Storage buildService(String password) throws IOException, GeneralSecurityException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleCredential credential = GoogleCredential.
                fromStream(new FileInputStream(password)).
                createScoped(Collections.singletonList(StorageScopes.DEVSTORAGE_FULL_CONTROL));

        Collection<String> scopes = StorageScopes.all();
        credential = credential.createScoped(scopes);

        return new Storage.Builder(transport, jsonFactory, credential)
                .setApplicationName(STORAGE_APPLICATION_NAME)
                .build();
    }
}
