/*
 * Copyright 2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.k8s.api;

import java.util.Set;

public interface Watcher<T> {
    void onUpdate(Set<T> items) throws Exception;
}
