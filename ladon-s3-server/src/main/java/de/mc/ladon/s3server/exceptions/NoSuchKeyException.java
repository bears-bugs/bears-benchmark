/*
 * Copyright (c) 2016 Mind Consulting UG(haftungsbeschränkt)
 */

package de.mc.ladon.s3server.exceptions;

import de.mc.ladon.s3server.entities.api.S3RequestId;

import java.net.HttpURLConnection;

/**
 * @author Ralf Ulrich on 20.02.16.
 */
public class NoSuchKeyException extends S3ServerException {
    public NoSuchKeyException(String resource, S3RequestId requestId) {
        super("The specified key does not exist.", resource, requestId, HttpURLConnection.HTTP_NOT_FOUND);
    }

}
