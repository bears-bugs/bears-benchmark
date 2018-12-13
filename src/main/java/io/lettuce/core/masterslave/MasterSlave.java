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
package io.lettuce.core.masterslave;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.Futures;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.internal.LettuceLists;

/**
 * Master-Slave connection API.
 * <p>
 * This API allows connections to Redis Master/Slave setups which run either in a static Master/Slave setup or are managed by
 * Redis Sentinel. Master-Slave connections can discover topologies and select a source for read operations using
 * {@link io.lettuce.core.ReadFrom}.
 * </p>
 * <p>
 *
 * Connections can be obtained by providing the {@link RedisClient}, a {@link RedisURI} and a {@link RedisCodec}.
 *
 * <pre class="code">
 * RedisClient client = RedisClient.create();
 * StatefulRedisMasterSlaveConnection&lt;String, String&gt; connection = MasterSlave.connect(client,
 *         RedisURI.create(&quot;redis://localhost&quot;), StringCodec.UTF8);
 * // ...
 *
 * connection.close();
 * client.shutdown();
 * </pre>
 *
 * </p>
 * <h3>Topology Discovery</h3>
 * <p>
 * Master-Slave topologies are either static or semi-static. Redis Standalone instances with attached slaves provide no
 * failover/HA mechanism. Redis Sentinel managed instances are controlled by Redis Sentinel and allow failover (which include
 * master promotion). The {@link MasterSlave} API supports both mechanisms. The topology is provided by a
 * {@link TopologyProvider}:
 *
 * <ul>
 * <li>{@link MasterSlaveTopologyProvider}: Dynamic topology lookup using the {@code INFO REPLICATION} output. Slaves are listed
 * as {@code slaveN=...} entries. The initial connection can either point to a master or a slave and the topology provider will
 * discover nodes. The connection needs to be re-established outside of lettuce in a case of Master/Slave failover or topology
 * changes.</li>
 * <li>{@link StaticMasterSlaveTopologyProvider}: Topology is defined by the list of {@link RedisURI URIs} and the {@code ROLE}
 * output. MasterSlave uses only the supplied nodes and won't discover additional nodes in the setup. The connection needs to be
 * re-established outside of lettuce in a case of Master/Slave failover or topology changes.</li>
 * <li>{@link SentinelTopologyProvider}: Dynamic topology lookup using the Redis Sentinel API. In particular,
 * {@code SENTINEL MASTER} and {@code SENTINEL SLAVES} output. Master/Slave failover is handled by lettuce.</li>
 * </ul>
 *
 * <p>
 * Topology Updates
 * </p>
 * <ul>
 * <li>Standalone Master/Slave: Performs a one-time topology lookup which remains static afterward</li>
 * <li>Redis Sentinel: Subscribes to all Sentinels and listens for Pub/Sub messages to trigger topology refreshing</li>
 * </ul>
 * </p>
 *
 * @author Mark Paluch
 * @since 4.1
 */
public class MasterSlave {

    /**
     * Open a new connection to a Redis Master-Slave server/servers using the supplied {@link RedisURI} and the supplied
     * {@link RedisCodec codec} to encode/decode keys.
     * <p>
     * This {@link MasterSlave} performs auto-discovery of nodes using either Redis Sentinel or Master/Slave. A {@link RedisURI}
     * can point to either a master or a slave host.
     * </p>
     *
     * @param redisClient the Redis client.
     * @param codec Use this codec to encode/decode keys and values, must not be {@literal null}.
     * @param redisURI the Redis server to connect to, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new connection.
     */
    public static <K, V> StatefulRedisMasterSlaveConnection<K, V> connect(RedisClient redisClient, RedisCodec<K, V> codec,
            RedisURI redisURI) {

        LettuceAssert.notNull(redisClient, "RedisClient must not be null");
        LettuceAssert.notNull(codec, "RedisCodec must not be null");
        LettuceAssert.notNull(redisURI, "RedisURI must not be null");

        return getConnection(connectAsyncSentinelOrAutodiscovery(redisClient, codec, redisURI), redisURI);
    }

    /**
     * Open asynchronously a new connection to a Redis Master-Slave server/servers using the supplied {@link RedisURI} and the
     * supplied {@link RedisCodec codec} to encode/decode keys.
     * <p>
     * This {@link MasterSlave} performs auto-discovery of nodes using either Redis Sentinel or Master/Slave. A {@link RedisURI}
     * can point to either a master or a slave host.
     * </p>
     *
     * @param redisClient the Redis client.
     * @param codec Use this codec to encode/decode keys and values, must not be {@literal null}.
     * @param redisURI the Redis server to connect to, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return {@link CompletableFuture} that is notified once the connect is finished.
     * @since
     */
    public static <K, V> CompletableFuture<StatefulRedisMasterSlaveConnection<K, V>> connectAsync(RedisClient redisClient,
            RedisCodec<K, V> codec, RedisURI redisURI) {
        return transformAsyncConnectionException(connectAsyncSentinelOrAutodiscovery(redisClient, codec, redisURI), redisURI);
    }

    private static <K, V> CompletableFuture<StatefulRedisMasterSlaveConnection<K, V>> connectAsyncSentinelOrAutodiscovery(
            RedisClient redisClient, RedisCodec<K, V> codec, RedisURI redisURI) {

        LettuceAssert.notNull(redisClient, "RedisClient must not be null");
        LettuceAssert.notNull(codec, "RedisCodec must not be null");
        LettuceAssert.notNull(redisURI, "RedisURI must not be null");

        if (isSentinel(redisURI)) {
            return new SentinelConnector<>(redisClient, codec, redisURI).connectAsync();
        }

        return new AutodiscoveryConnector<>(redisClient, codec, redisURI).connectAsync();
    }

    /**
     * Open a new connection to a Redis Master-Slave server/servers using the supplied {@link RedisURI} and the supplied
     * {@link RedisCodec codec} to encode/decode keys.
     * <p>
     * This {@link MasterSlave} performs auto-discovery of nodes if the URI is a Redis Sentinel URI. Master/Slave URIs will be
     * treated as static topology and no additional hosts are discovered in such case. Redis Standalone Master/Slave will
     * discover the roles of the supplied {@link RedisURI URIs} and issue commands to the appropriate node.
     * </p>
     *
     * @param redisClient the Redis client.
     * @param codec Use this codec to encode/decode keys and values, must not be {@literal null}.
     * @param redisURIs the Redis server to connect to, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new connection.
     */
    public static <K, V> StatefulRedisMasterSlaveConnection<K, V> connect(RedisClient redisClient, RedisCodec<K, V> codec,
            Iterable<RedisURI> redisURIs) {
        return getConnection(connectAsyncSentinelOrStaticSetup(redisClient, codec, redisURIs), redisURIs);
    }

    /**
     * Open asynchronously a new connection to a Redis Master-Slave server/servers using the supplied {@link RedisURI} and the
     * supplied {@link RedisCodec codec} to encode/decode keys.
     * <p>
     * This {@link MasterSlave} performs auto-discovery of nodes if the URI is a Redis Sentinel URI. Master/Slave URIs will be
     * treated as static topology and no additional hosts are discovered in such case. Redis Standalone Master/Slave will
     * discover the roles of the supplied {@link RedisURI URIs} and issue commands to the appropriate node.
     * </p>
     *
     * @param redisClient the Redis client.
     * @param codec Use this codec to encode/decode keys and values, must not be {@literal null}.
     * @param redisURIs the Redis server to connect to, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return {@link CompletableFuture} that is notified once the connect is finished.
     */
    public static <K, V> CompletableFuture<StatefulRedisMasterSlaveConnection<K, V>> connectAsync(RedisClient redisClient,
            RedisCodec<K, V> codec, Iterable<RedisURI> redisURIs) {
        return transformAsyncConnectionException(connectAsyncSentinelOrStaticSetup(redisClient, codec, redisURIs), redisURIs);
    }

    private static <K, V> CompletableFuture<StatefulRedisMasterSlaveConnection<K, V>> connectAsyncSentinelOrStaticSetup(
            RedisClient redisClient, RedisCodec<K, V> codec, Iterable<RedisURI> redisURIs) {

        LettuceAssert.notNull(redisClient, "RedisClient must not be null");
        LettuceAssert.notNull(codec, "RedisCodec must not be null");
        LettuceAssert.notNull(redisURIs, "RedisURIs must not be null");

        List<RedisURI> uriList = LettuceLists.newList(redisURIs);
        LettuceAssert.isTrue(!uriList.isEmpty(), "RedisURIs must not be empty");

        if (isSentinel(uriList.get(0))) {
            return new SentinelConnector<>(redisClient, codec, uriList.get(0)).connectAsync();
        }

        return new StaticMasterSlaveConnector<>(redisClient, codec, uriList).connectAsync();
    }

    private static boolean isSentinel(RedisURI redisURI) {
        return !redisURI.getSentinels().isEmpty();
    }

    /**
     * Retrieve the connection from {@link ConnectionFuture}. Performs a blocking {@link ConnectionFuture#get()} to synchronize
     * the channel/connection initialization. Any exception is rethrown as {@link RedisConnectionException}.
     *
     * @param connectionFuture must not be null.
     * @param context context information (single RedisURI, multiple URIs), used as connection target in the reported exception.
     * @param <T> Connection type.
     * @return the connection.
     * @throws RedisConnectionException in case of connection failures.
     */
    private static <T> T getConnection(CompletableFuture<T> connectionFuture, Object context) {

        try {
            return connectionFuture.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw RedisConnectionException.create(context.toString(), e);
        } catch (Exception e) {

            if (e instanceof ExecutionException) {

                // filter intermediate RedisConnectionException exceptions that bloat the stack trace
                if (e.getCause() instanceof RedisConnectionException
                        && e.getCause().getCause() instanceof RedisConnectionException) {
                    throw RedisConnectionException.create(context.toString(), e.getCause().getCause());
                }

                throw RedisConnectionException.create(context.toString(), e.getCause());
            }

            throw RedisConnectionException.create(context.toString(), e);
        }
    }

    private static <T> CompletableFuture<T> transformAsyncConnectionException(CompletionStage<T> future, Object context) {

        return ConnectionFuture
                .from(null, future.toCompletableFuture())
                .thenCompose((v, e) -> {

                    if (e != null) {

                        // filter intermediate RedisConnectionException exceptions that bloat the stack trace
                        if (e.getCause() instanceof RedisConnectionException
                                && e.getCause().getCause() instanceof RedisConnectionException) {
                            return Futures.failed(RedisConnectionException.create(context.toString(), e.getCause()));
                        }
                        return Futures.failed(RedisConnectionException.create(context.toString(), e));
                    }

                    return CompletableFuture.completedFuture(v);
                }).toCompletableFuture();
    }
}
