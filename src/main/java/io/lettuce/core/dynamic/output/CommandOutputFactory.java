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
package io.lettuce.core.dynamic.output;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;

/**
 * Strategy interface to create {@link CommandOutput} given {@link RedisCodec}.
 *
 * <p>
 * Implementing classes usually produce the same {@link CommandOutput} type.
 *
 * @author Mark Paluch
 * @since 5.0
 */
@FunctionalInterface
public interface CommandOutputFactory {

    /**
     * Create and initialize a new {@link CommandOutput} given {@link RedisCodec}.
     *
     * @param codec must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return the new {@link CommandOutput}.
     */
    <K, V> CommandOutput<K, V, ?> create(RedisCodec<K, V> codec);
}
