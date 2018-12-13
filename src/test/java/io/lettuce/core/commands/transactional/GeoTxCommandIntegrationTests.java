/*
 * Copyright 2011-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.commands.transactional;

import javax.inject.Inject;

import org.junit.jupiter.api.Disabled;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.commands.GeoCommandIntegrationTests;

/**
 * @author Mark Paluch
 */
class GeoTxCommandIntegrationTests extends GeoCommandIntegrationTests {

    @Inject
    GeoTxCommandIntegrationTests(StatefulRedisConnection<String, String> connection) {
        super(TxSyncInvocationHandler.sync(connection));
    }

    @Disabled
    @Override
    public void georadiusbymemberWithArgsInTransaction() {
    }

    @Disabled
    @Override
    public void geoaddInTransaction() {
    }

    @Disabled
    @Override
    public void geoaddMultiInTransaction() {
    }

    @Disabled
    @Override
    public void geoposInTransaction() {
    }

    @Disabled
    @Override
    public void georadiusWithArgsAndTransaction() {
    }

    @Disabled
    @Override
    public void georadiusInTransaction() {
    }

    @Disabled
    @Override
    public void geodistInTransaction() {
    }

    @Disabled
    @Override
    public void geohashInTransaction() {
    }
}
