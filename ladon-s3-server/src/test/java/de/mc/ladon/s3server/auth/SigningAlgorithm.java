/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.mc.ladon.s3server.auth;

import javax.crypto.Mac;
import java.security.NoSuchAlgorithmException;

public enum SigningAlgorithm {

    HmacSHA1,
    HmacSHA256;

    private final ThreadLocal<Mac> macReference;

    private SigningAlgorithm() {
        final String algorithmName = this.toString();
        macReference = new ThreadLocal<Mac>() {
            @Override
            protected Mac initialValue() {
                try {
                    return Mac.getInstance(algorithmName);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Unable to fetch Mac instance for Algorithm "
                            + algorithmName + e.getMessage(), e);

                }
            }
        };
    }

    /**
     * Returns the thread local reference for the crypto algorithm
     */
    public Mac getMac() {
        return macReference.get();
    }
}
