/*
 * Copyright (c) 2016 Mind Consulting UG(haftungsbeschränkt)
 */

package de.mc.ladon.s3server.exceptions;

import de.mc.ladon.s3server.entities.api.S3RequestId;

import java.net.HttpURLConnection;

/**
 * Created by max on 20.02.16.
 */
public class BucketAlreadyOwnedByYouException extends S3ServerException {

    public BucketAlreadyOwnedByYouException(String resource, S3RequestId requestId) {
        super("Your previous request to create the named bucket succeeded and you already own it.", resource, requestId, HttpURLConnection.HTTP_CONFLICT);
    }


}
