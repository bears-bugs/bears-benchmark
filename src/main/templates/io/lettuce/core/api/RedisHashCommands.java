/*
 * Copyright 2017-2018 the original author or authors.
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
package io.lettuce.core.api;

import java.util.List;
import java.util.Map;

import io.lettuce.core.*;
import io.lettuce.core.output.KeyStreamingChannel;
import io.lettuce.core.output.KeyValueStreamingChannel;
import io.lettuce.core.output.ValueStreamingChannel;

/**
 * ${intent} for Hashes (Key-Value pairs).
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Mark Paluch
 * @since 4.0
 */
public interface RedisHashCommands<K, V> {

    /**
     * Delete one or more hash fields.
     *
     * @param key the key
     * @param fields the field type: key
     * @return Long integer-reply the number of fields that were removed from the hash, not including specified but non existing
     *         fields.
     */
    Long hdel(K key, K... fields);

    /**
     * Determine if a hash field exists.
     *
     * @param key the key
     * @param field the field type: key
     * @return Boolean integer-reply specifically:
     *
     *         {@literal true} if the hash contains {@code field}. {@literal false} if the hash does not contain {@code field},
     *         or {@code key} does not exist.
     */
    Boolean hexists(K key, K field);

    /**
     * Get the value of a hash field.
     *
     * @param key the key
     * @param field the field type: key
     * @return V bulk-string-reply the value associated with {@code field}, or {@literal null} when {@code field} is not present
     *         in the hash or {@code key} does not exist.
     */
    V hget(K key, K field);

    /**
     * Increment the integer value of a hash field by the given number.
     *
     * @param key the key
     * @param field the field type: key
     * @param amount the increment type: long
     * @return Long integer-reply the value at {@code field} after the increment operation.
     */
    Long hincrby(K key, K field, long amount);

    /**
     * Increment the float value of a hash field by the given amount.
     *
     * @param key the key
     * @param field the field type: key
     * @param amount the increment type: double
     * @return Double bulk-string-reply the value of {@code field} after the increment.
     */
    Double hincrbyfloat(K key, K field, double amount);

    /**
     * Get all the fields and values in a hash.
     *
     * @param key the key
     * @return Map&lt;K,V&gt; array-reply list of fields and their values stored in the hash, or an empty list when {@code key}
     *         does not exist.
     */
    Map<K, V> hgetall(K key);

    /**
     * Stream over all the fields and values in a hash.
     *
     * @param channel the channel
     * @param key the key
     *
     * @return Long count of the keys.
     */
    Long hgetall(KeyValueStreamingChannel<K, V> channel, K key);

    /**
     * Get all the fields in a hash.
     *
     * @param key the key
     * @return List&lt;K&gt; array-reply list of fields in the hash, or an empty list when {@code key} does not exist.
     */
    List<K> hkeys(K key);

    /**
     * Stream over all the fields in a hash.
     *
     * @param channel the channel
     * @param key the key
     *
     * @return Long count of the keys.
     */
    Long hkeys(KeyStreamingChannel<K> channel, K key);

    /**
     * Get the number of fields in a hash.
     *
     * @param key the key
     * @return Long integer-reply number of fields in the hash, or {@code 0} when {@code key} does not exist.
     */
    Long hlen(K key);

    /**
     * Get the values of all the given hash fields.
     *
     * @param key the key
     * @param fields the field type: key
     * @return List&lt;V&gt; array-reply list of values associated with the given fields, in the same
     */
    List<KeyValue<K, V>> hmget(K key, K... fields);

    /**
     * Stream over the values of all the given hash fields.
     *
     * @param channel the channel
     * @param key the key
     * @param fields the fields
     *
     * @return Long count of the keys
     */
    Long hmget(KeyValueStreamingChannel<K, V> channel, K key, K... fields);

    /**
     * Set multiple hash fields to multiple values.
     *
     * @param key the key
     * @param map the null
     * @return String simple-string-reply
     */
    String hmset(K key, Map<K, V> map);

    /**
     * Incrementally iterate hash fields and associated values.
     *
     * @param key the key
     * @return MapScanCursor&lt;K, V&gt; map scan cursor.
     */
    MapScanCursor<K, V> hscan(K key);

    /**
     * Incrementally iterate hash fields and associated values.
     *
     * @param key the key
     * @param scanArgs scan arguments
     * @return MapScanCursor&lt;K, V&gt; map scan cursor.
     */
    MapScanCursor<K, V> hscan(K key, ScanArgs scanArgs);

    /**
     * Incrementally iterate hash fields and associated values.
     *
     * @param key the key
     * @param scanCursor cursor to resume from a previous scan, must not be {@literal null}
     * @param scanArgs scan arguments
     * @return MapScanCursor&lt;K, V&gt; map scan cursor.
     */
    MapScanCursor<K, V> hscan(K key, ScanCursor scanCursor, ScanArgs scanArgs);

    /**
     * Incrementally iterate hash fields and associated values.
     *
     * @param key the key
     * @param scanCursor cursor to resume from a previous scan, must not be {@literal null}
     * @return MapScanCursor&lt;K, V&gt; map scan cursor.
     */
    MapScanCursor<K, V> hscan(K key, ScanCursor scanCursor);

    /**
     * Incrementally iterate hash fields and associated values.
     *
     * @param channel streaming channel that receives a call for every key-value pair
     * @param key the key
     * @return StreamScanCursor scan cursor.
     */
    StreamScanCursor hscan(KeyValueStreamingChannel<K, V> channel, K key);

    /**
     * Incrementally iterate hash fields and associated values.
     *
     * @param channel streaming channel that receives a call for every key-value pair
     * @param key the key
     * @param scanArgs scan arguments
     * @return StreamScanCursor scan cursor.
     */
    StreamScanCursor hscan(KeyValueStreamingChannel<K, V> channel, K key, ScanArgs scanArgs);

    /**
     * Incrementally iterate hash fields and associated values.
     *
     * @param channel streaming channel that receives a call for every key-value pair
     * @param key the key
     * @param scanCursor cursor to resume from a previous scan, must not be {@literal null}
     * @param scanArgs scan arguments
     * @return StreamScanCursor scan cursor.
     */
    StreamScanCursor hscan(KeyValueStreamingChannel<K, V> channel, K key, ScanCursor scanCursor, ScanArgs scanArgs);

    /**
     * Incrementally iterate hash fields and associated values.
     *
     * @param channel streaming channel that receives a call for every key-value pair
     * @param key the key
     * @param scanCursor cursor to resume from a previous scan, must not be {@literal null}
     * @return StreamScanCursor scan cursor.
     */
    StreamScanCursor hscan(KeyValueStreamingChannel<K, V> channel, K key, ScanCursor scanCursor);

    /**
     * Set the string value of a hash field.
     *
     * @param key the key
     * @param field the field type: key
     * @param value the value
     * @return Boolean integer-reply specifically:
     *
     *         {@literal true} if {@code field} is a new field in the hash and {@code value} was set. {@literal false} if
     *         {@code field} already exists in the hash and the value was updated.
     */
    Boolean hset(K key, K field, V value);

    /**
     * Set the value of a hash field, only if the field does not exist.
     *
     * @param key the key
     * @param field the field type: key
     * @param value the value
     * @return Boolean integer-reply specifically:
     *
     *         {@code 1} if {@code field} is a new field in the hash and {@code value} was set. {@code 0} if {@code field}
     *         already exists in the hash and no operation was performed.
     */
    Boolean hsetnx(K key, K field, V value);

    /**
     * Get the string length of the field value in a hash.
     *
     * @param key the key
     * @param field the field type: key
     * @return Long integer-reply the string length of the {@code field} value, or {@code 0} when {@code field} is not present
     *         in the hash or {@code key} does not exist at all.
     */
    Long hstrlen(K key, K field);

        /**
     * Get all the values in a hash.
     *
     * @param key the key
     * @return List&lt;V&gt; array-reply list of values in the hash, or an empty list when {@code key} does not exist.
     */
    List<V> hvals(K key);

    /**
     * Stream over all the values in a hash.
     *
     * @param channel streaming channel that receives a call for every value
     * @param key the key
     *
     * @return Long count of the keys.
     */
    Long hvals(ValueStreamingChannel<V> channel, K key);
}
