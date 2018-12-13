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

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * @author Mark Paluch
 */
class DefaultDefaultCommandLatencyCollectorOptionsUnitTests {

    @Test
    void testDefault() {

        DefaultCommandLatencyCollectorOptions sut = DefaultCommandLatencyCollectorOptions.create();

        assertThat(sut.targetPercentiles()).hasSize(5);
        assertThat(sut.targetUnit()).isEqualTo(TimeUnit.MICROSECONDS);
    }

    @Test
    void testDisabled() {

        DefaultCommandLatencyCollectorOptions sut = DefaultCommandLatencyCollectorOptions.disabled();

        assertThat(sut.isEnabled()).isEqualTo(false);
    }

    @Test
    void testBuilder() {

        DefaultCommandLatencyCollectorOptions sut = DefaultCommandLatencyCollectorOptions.builder()
                .targetUnit(TimeUnit.HOURS).targetPercentiles(new double[] { 1, 2, 3 }).build();

        assertThat(sut.targetPercentiles()).hasSize(3);
        assertThat(sut.targetUnit()).isEqualTo(TimeUnit.HOURS);
    }
}
