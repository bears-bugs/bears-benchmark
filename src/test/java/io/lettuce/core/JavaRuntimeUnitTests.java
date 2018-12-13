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
package io.lettuce.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

import io.lettuce.core.internal.LettuceClassUtils;

class JavaRuntimeUnitTests {

    @Test
    void testJava8() {

        assumeTrue(System.getProperty("java.version").startsWith("1.8"));

        assertThat(JavaRuntime.AT_LEAST_JDK_8).isTrue();
    }

    @Test
    void testJava9() {

        assumeTrue(System.getProperty("java.version").startsWith("9"));

        assertThat(JavaRuntime.AT_LEAST_JDK_8).isTrue();
    }

    @Test
    void testNotPresentClass() {
        assertThat(LettuceClassUtils.isPresent("total.fancy.class.name")).isFalse();
    }
}
