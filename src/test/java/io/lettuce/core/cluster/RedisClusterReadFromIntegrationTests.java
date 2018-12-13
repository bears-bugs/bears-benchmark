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
package io.lettuce.core.cluster;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.TestSupport;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@SuppressWarnings("unchecked")
@ExtendWith(LettuceExtension.class)
class RedisClusterReadFromIntegrationTests extends TestSupport {

    private final RedisClusterClient clusterClient;
    private StatefulRedisClusterConnection<String, String> connection;
    private RedisAdvancedClusterCommands<String, String> sync;

    @Inject
    RedisClusterReadFromIntegrationTests(RedisClusterClient clusterClient) {
        this.clusterClient = clusterClient;
    }

    @BeforeEach
    void before() {
        connection = clusterClient.connect();
        sync = connection.sync();
    }

    @AfterEach
    void after() {
        connection.close();
    }

    @Test
    void defaultTest() {
        assertThat(connection.getReadFrom()).isEqualTo(ReadFrom.MASTER);
    }

    @Test
    void readWriteMaster() {

        connection.setReadFrom(ReadFrom.MASTER);

        sync.set(key, value);
        assertThat(sync.get(key)).isEqualTo(value);
    }

    @Test
    void readWriteMasterPreferred() {

        connection.setReadFrom(ReadFrom.MASTER_PREFERRED);

        sync.set(key, value);
        assertThat(sync.get(key)).isEqualTo(value);
    }

    @Test
    void readWriteSlave() {

        connection.setReadFrom(ReadFrom.SLAVE);

        sync.set(key, "value1");

        connection.getConnection(ClusterTestSettings.host, ClusterTestSettings.port2).sync().waitForReplication(1, 1000);
        assertThat(sync.get(key)).isEqualTo("value1");
    }

    @Test
    void readWriteSlavePreferred() {

        connection.setReadFrom(ReadFrom.SLAVE_PREFERRED);

        sync.set(key, "value1");

        connection.getConnection(ClusterTestSettings.host, ClusterTestSettings.port2).sync().waitForReplication(1, 1000);
        assertThat(sync.get(key)).isEqualTo("value1");
    }

    @Test
    void readWriteNearest() {

        connection.setReadFrom(ReadFrom.NEAREST);

        sync.set(key, "value1");

        connection.getConnection(ClusterTestSettings.host, ClusterTestSettings.port2).sync().waitForReplication(1, 1000);
        assertThat(sync.get(key)).isEqualTo("value1");
    }
}
