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
package io.lettuce.core.cluster.topology;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandType;

/**
 * @author Mark Paluch
 */
class RequestsUnitTests {

    @Test
    void shouldCreateTopologyView() throws Exception {

        RedisURI redisURI = RedisURI.create("localhost", 6379);

        Requests clusterNodesRequests = new Requests();
        String clusterNodesOutput = "1 127.0.0.1:7380 master,myself - 0 1401258245007 2 disconnected 8000-11999\n";
        clusterNodesRequests.addRequest(redisURI, getCommand(clusterNodesOutput));

        Requests clientListRequests = new Requests();
        String clientListOutput = "id=2 addr=127.0.0.1:58919 fd=6 name= age=3 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=32768 obl=0 oll=0 omem=0 events=r cmd=client\n";
        clientListRequests.addRequest(redisURI, getCommand(clientListOutput));

        NodeTopologyView nodeTopologyView = NodeTopologyView.from(redisURI, clusterNodesRequests, clientListRequests);

        assertThat(nodeTopologyView.isAvailable()).isTrue();
        assertThat(nodeTopologyView.getConnectedClients()).isEqualTo(1);
        assertThat(nodeTopologyView.getPartitions()).hasSize(1);
        assertThat(nodeTopologyView.getClusterNodes()).isEqualTo(clusterNodesOutput);
        assertThat(nodeTopologyView.getClientList()).isEqualTo(clientListOutput);
    }

    @Test
    void shouldCreateTopologyViewWithoutClientCount() throws Exception {

        RedisURI redisURI = RedisURI.create("localhost", 6379);

        Requests clusterNodesRequests = new Requests();
        String clusterNodesOutput = "1 127.0.0.1:7380 master,myself - 0 1401258245007 2 disconnected 8000-11999\n";
        clusterNodesRequests.addRequest(redisURI, getCommand(clusterNodesOutput));

        Requests clientListRequests = new Requests();

        NodeTopologyView nodeTopologyView = NodeTopologyView.from(redisURI, clusterNodesRequests, clientListRequests);

        assertThat(nodeTopologyView.isAvailable()).isFalse();
        assertThat(nodeTopologyView.getConnectedClients()).isEqualTo(0);
        assertThat(nodeTopologyView.getPartitions()).isEmpty();
        assertThat(nodeTopologyView.getClusterNodes()).isNull();
    }

    @Test
    void awaitShouldReturnAwaitedTime() throws Exception {

        RedisURI redisURI = RedisURI.create("localhost", 6379);
        Requests requests = new Requests();
        Command<String, String, String> command = new Command<>(CommandType.TYPE,
                new StatusOutput<>(new Utf8StringCodec()));
        TimedAsyncCommand timedAsyncCommand = new TimedAsyncCommand(command);

        requests.addRequest(redisURI, timedAsyncCommand);

        assertThat(requests.await(100, TimeUnit.MILLISECONDS)).isGreaterThan(TimeUnit.MILLISECONDS.toNanos(90));
    }

    @Test
    void awaitShouldReturnAwaitedTimeIfNegative() throws Exception {

        RedisURI redisURI = RedisURI.create("localhost", 6379);
        Requests requests = new Requests();
        Command<String, String, String> command = new Command<>(CommandType.TYPE,
                new StatusOutput<>(new Utf8StringCodec()));
        TimedAsyncCommand timedAsyncCommand = new TimedAsyncCommand(command);

        requests.addRequest(redisURI, timedAsyncCommand);

        assertThat(requests.await(-1, TimeUnit.MILLISECONDS)).isEqualTo(0);

    }

    private TimedAsyncCommand getCommand(String response) {
        Command<String, String, String> command = new Command<>(CommandType.TYPE,
                new StatusOutput<>(new Utf8StringCodec()));
        TimedAsyncCommand timedAsyncCommand = new TimedAsyncCommand(command);

        command.getOutput().set(ByteBuffer.wrap(response.getBytes()));
        timedAsyncCommand.complete();
        return timedAsyncCommand;
    }
}
