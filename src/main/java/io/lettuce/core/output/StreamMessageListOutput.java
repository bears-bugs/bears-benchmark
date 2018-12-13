/*
 * Copyright 2018 the original author or authors.
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
package io.lettuce.core.output;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.lettuce.core.StreamMessage;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.LettuceAssert;

/**
 * {@link List} of {@link StreamMessage}s.
 *
 * @author Mark Paluch
 * @since 5.1
 */
public class StreamMessageListOutput<K, V> extends CommandOutput<K, V, List<StreamMessage<K, V>>> implements
        StreamingOutput<StreamMessage<K, V>> {

    private final K stream;

    private boolean initialized;
    private Subscriber<StreamMessage<K, V>> subscriber;

    private K key;
    private String id;
    private Map<K, V> body;

    public StreamMessageListOutput(RedisCodec<K, V> codec, K stream) {
        super(codec, Collections.emptyList());
        setSubscriber(ListSubscriber.instance());
        this.stream = stream;
    }

    @Override
    public void set(ByteBuffer bytes) {

        if (id == null) {
            id = decodeAscii(bytes);
            return;
        }

        if (key == null) {
            key = codec.decodeKey(bytes);
            return;
        }

        if (body == null) {
            body = new LinkedHashMap<>();
        }

        body.put(key, bytes == null ? null : codec.decodeValue(bytes));
        key = null;
    }

    @Override
    public void multi(int count) {

        if (!initialized) {
            output = OutputFactory.newList(count);
            initialized = true;
        }
    }

    @Override
    public void complete(int depth) {

        if (depth == 1) {
            subscriber.onNext(output, new StreamMessage<>(stream, id, body));
            key = null;
            id = null;
            body = null;
        }
    }

    @Override
    public void setSubscriber(Subscriber<StreamMessage<K, V>> subscriber) {
        LettuceAssert.notNull(subscriber, "Subscriber must not be null");
        this.subscriber = subscriber;
    }

    @Override
    public Subscriber<StreamMessage<K, V>> getSubscriber() {
        return subscriber;
    }
}
