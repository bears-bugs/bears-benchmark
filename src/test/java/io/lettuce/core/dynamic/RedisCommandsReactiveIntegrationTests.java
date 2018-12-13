/*
 * Copyright 2016-2018 the original author or authors.
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
package io.lettuce.core.dynamic;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.lettuce.core.TestSupport;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.test.LettuceExtension;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
class RedisCommandsReactiveIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    RedisCommandsReactiveIntegrationTests(StatefulRedisConnection<String, String> connection) {
        this.redis = connection.sync();
    }

    @Test
    void reactive() {

        RedisCommandFactory factory = new RedisCommandFactory(redis.getStatefulConnection());

        MultipleExecutionModels api = factory.getCommands(MultipleExecutionModels.class);

        StepVerifier.create(api.setReactive(key, value)).expectNext("OK").verifyComplete();
    }

    static interface MultipleExecutionModels extends Commands {
        @Command("SET")
        Mono<String> setReactive(String key, String value);
    }
}
