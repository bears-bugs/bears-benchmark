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
package org.springframework.data.projection;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assume.*;

import org.junit.Test;
import org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.MethodHandleLookup;
import org.springframework.data.util.Version;

/**
 * Unit tests for {@link DefaultMethodInvokingMethodInterceptor}.
 *
 * @author Mark Paluch
 * @author Oliver Gierke
 */
public class DefaultMethodInvokingMethodInterceptorUnitTests {

	@Test // DATACMNS-1376
	public void shouldApplyEncapsulatedLookupOnJava9AndHigher() {

		assumeTrue(Version.javaVersion().isGreaterThanOrEqualTo(Version.parse("9.0")));

		assertThat(MethodHandleLookup.getMethodHandleLookup()).isEqualTo(MethodHandleLookup.ENCAPSULATED);
		assertThat(MethodHandleLookup.ENCAPSULATED.isAvailable()).isTrue();
	}

	@Test // DATACMNS-1376
	public void shouldApplyOpenLookupOnJava8() {

		assumeTrue(Version.javaVersion().isLessThan(Version.parse("1.8.9999")));

		assertThat(MethodHandleLookup.getMethodHandleLookup()).isEqualTo(MethodHandleLookup.OPEN);
		assertThat(MethodHandleLookup.OPEN.isAvailable()).isTrue();
		assertThat(MethodHandleLookup.ENCAPSULATED.isAvailable()).isFalse();
	}
}
