/*
 * Copyright (c) 2016 Mind Consulting UG(haftungsbeschränkt)
 */

package de.mc.ladon.s3server.exceptions;

import de.mc.ladon.s3server.entities.api.S3RequestId;

import java.net.HttpURLConnection;

/**
 * @author Ralf Ulrich on 20.02.16.
 */
public class RequestIsNotMultiPartContentException extends S3ServerException {
    public RequestIsNotMultiPartContentException(String resource, S3RequestId requestId) {
        super("POST must be of the enclosure-type multipart/form-data.", resource, requestId, HttpURLConnection.HTTP_BAD_REQUEST);
    }

}
