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
package io.lettuce.core;

import static io.lettuce.core.TimeoutOptions.TimeoutSource;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class TimeoutOptionsUnitTests {

    @Test
    void noTimeoutByDefault() {

        TimeoutOptions timeoutOptions = TimeoutOptions.create();

        assertThat(timeoutOptions.isTimeoutCommands()).isFalse();
        assertThat(timeoutOptions.getSource()).isNull();
    }

    @Test
    void defaultConnectionTimeout() {

        TimeoutOptions timeoutOptions = TimeoutOptions.enabled();

        TimeoutSource source = timeoutOptions.getSource();
        assertThat(timeoutOptions.isTimeoutCommands()).isTrue();
        assertThat(timeoutOptions.isApplyConnectionTimeout()).isTrue();
        assertThat(source.getTimeout(null)).isEqualTo(-1);
    }

    @Test
    void fixedConnectionTimeout() {

        TimeoutOptions timeoutOptions = TimeoutOptions.enabled(Duration.ofMinutes(1));

        TimeoutSource source = timeoutOptions.getSource();
        assertThat(timeoutOptions.isTimeoutCommands()).isTrue();
        assertThat(timeoutOptions.isApplyConnectionTimeout()).isFalse();
        assertThat(source.getTimeout(null)).isEqualTo(TimeUnit.MINUTES.toNanos(1));
    }
}
