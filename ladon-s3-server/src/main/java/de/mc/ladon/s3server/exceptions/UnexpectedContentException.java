/*
 * Copyright (c) 2016 Mind Consulting UG(haftungsbeschränkt)
 */

package de.mc.ladon.s3server.exceptions;

import de.mc.ladon.s3server.entities.api.S3RequestId;

import java.net.HttpURLConnection;

/**
 * @author Ralf Ulrich on 20.02.16.
 */
public class UnexpectedContentException extends S3ServerException {
    public UnexpectedContentException(String resource, S3RequestId requestId) {
        super("This request does not support content.", resource, requestId, HttpURLConnection.HTTP_BAD_REQUEST);
    }


}
