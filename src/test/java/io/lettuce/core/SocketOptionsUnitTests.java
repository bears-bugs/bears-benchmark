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
package io.lettuce.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class SocketOptionsUnitTests {

    @Test
    void testNew() {
        checkAssertions(SocketOptions.create());
    }

    @Test
    void testBuilder() {

        SocketOptions sut = SocketOptions.builder().connectTimeout(1, TimeUnit.MINUTES).keepAlive(true).tcpNoDelay(true)
                .build();

        assertThat(sut.isKeepAlive()).isEqualTo(true);
        assertThat(sut.isTcpNoDelay()).isEqualTo(true);
        assertThat(sut.getConnectTimeout()).isEqualTo(Duration.ofMinutes(1));
    }

    @Test
    void testCopy() {
        checkAssertions(SocketOptions.copyOf(SocketOptions.builder().build()));
    }

    void checkAssertions(SocketOptions sut) {
        assertThat(sut.isKeepAlive()).isEqualTo(false);
        assertThat(sut.isTcpNoDelay()).isEqualTo(false);
        assertThat(sut.getConnectTimeout()).isEqualTo(Duration.ofSeconds(10));
    }
}
