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
package io.lettuce.core.pubsub.api.reactive;

/**
 * Message payload for a subscription to a channel.
 *
 * @author Mark Paluch
 */
public class ChannelMessage<K, V> {

    private final K channel;
    private final V message;

    /**
     *
     * @param channel the channel
     * @param message the message
     */
    public ChannelMessage(K channel, V message) {
        this.channel = channel;
        this.message = message;
    }

    /**
     *
     * @return the channel
     */
    public K getChannel() {
        return channel;
    }

    /**
     *
     * @return the message
     */
    public V getMessage() {
        return message;
    }
}
