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
package io.lettuce.core.metrics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.lettuce.core.protocol.CommandKeyword;
import io.netty.channel.local.LocalAddress;

/**
 * @author Mark Paluch
 */
class CommandLatencyIdUnitTests {

    private CommandLatencyId sut = CommandLatencyId.create(LocalAddress.ANY, new LocalAddress("me"), CommandKeyword.ADDR);

    @Test
    void testToString() {
        assertThat(sut.toString()).contains("local:any -> local:me");
    }

    @Test
    void testValues() {
        assertThat(sut.localAddress()).isEqualTo(LocalAddress.ANY);
        assertThat(sut.remoteAddress()).isEqualTo(new LocalAddress("me"));
    }
}
