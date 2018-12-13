/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tamaya;

import org.apache.tamaya.spi.ConfigurationContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * An immutable configuration snapshot containing the given keys only.
 */
public interface ConfigurationSnapshot extends Configuration{


    /**
     * The requested keys. Note that not all keys must be accessible as a property.
     * @return the requested keys.
     */
    Set<String> getKeys();

    /**
     * Get the timestamp, when this snapshot has been taken.
     * @return the timestamp
     */
    long getTimestamp();

    /**
     * An empty snapshot.
     */
    ConfigurationSnapshot EMPTY = new ConfigurationSnapshot() {

        @Override
        public <T> T get(String key, TypeLiteral<T> type) {
            return null;
        }

        @Override
        public <T> T getOrDefault(String key, TypeLiteral<T> type, T defaultValue) {
            return defaultValue;
        }

        @Override
        public Map<String, String> getProperties() {
            return Collections.emptyMap();
        }

        @Override
        public ConfigurationContext getContext() {
            return ConfigurationContext.EMPTY;
        }

        @Override
        public ConfigurationSnapshot getSnapshot(Iterable<String> keys) {
            return this;
        }

        @Override
        public Set<String> getKeys() {
            return Collections.emptySet();
        }

        @Override
        public long getTimestamp() {
            return 0;
        }

        @Override
        public String toString() {
            return "ConfigurationSnapshot<EMPTY>";
        }
    };

}
