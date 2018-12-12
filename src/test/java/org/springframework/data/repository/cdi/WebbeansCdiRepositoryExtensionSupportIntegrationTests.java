/*
 * Copyright 2011-2017 the original author or authors.
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
package org.springframework.data.repository.cdi;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * CDI extension integration test using OpenWebbeans.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class WebbeansCdiRepositoryExtensionSupportIntegrationTests
		extends CdiRepositoryExtensionSupportIntegrationTests {

	private static SeContainer container;

	@BeforeClass
	public static void setUp() {

		container = SeContainerInitializer.newInstance() //
				.disableDiscovery() //
				.addPackages(SampleRepository.class) //
				.initialize();
	}

	@Override
	protected <T> T getBean(Class<T> type) {
		return container.select(type).get();
	}

	@AfterClass
	public static void tearDown() {
		container.close();
	}
}
