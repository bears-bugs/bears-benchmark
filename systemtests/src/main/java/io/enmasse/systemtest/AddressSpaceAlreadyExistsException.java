/*
 * Copyright 2017-2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.systemtest;

public class AddressSpaceAlreadyExistsException extends Exception {

    public AddressSpaceAlreadyExistsException(String msg) {
        super(msg);
    }
}
