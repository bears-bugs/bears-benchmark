/*
 * Copyright (c) 2016 Mind Consulting UG(haftungsbeschränkt)
 */

package de.mc.ladon.s3server.exceptions;

import de.mc.ladon.s3server.entities.api.S3RequestId;

import java.net.HttpURLConnection;

/**
 * @author Ralf Ulrich on 20.02.16.
 */
public class UserKeyMustBeSpecifiedException extends S3ServerException {
    public UserKeyMustBeSpecifiedException(String resource, S3RequestId requestId) {
        super("The bucket POST must contain the specified field name. If it is specified, check the order of the fields.", resource, requestId, HttpURLConnection.HTTP_BAD_REQUEST);
    }


}
