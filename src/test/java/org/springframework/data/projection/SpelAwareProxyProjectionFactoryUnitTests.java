/*
 * Copyright 2015-2018 the original author or authors.
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

import java.beans.PropertyDescriptor;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Value;

/**
 * Unit tests for {@link SpelAwareProxyProjectionFactory}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 */
public class SpelAwareProxyProjectionFactoryUnitTests {

	public @Rule ExpectedException exception = ExpectedException.none();

	SpelAwareProxyProjectionFactory factory;

	@Before
	public void setup() {
		factory = new SpelAwareProxyProjectionFactory();
	}

	@Test // DATAREST-221, DATACMNS-630
	public void exposesSpelInvokingMethod() {

		Customer customer = new Customer();
		customer.firstname = "Dave";
		customer.lastname = "Matthews";

		CustomerExcerpt excerpt = factory.createProjection(CustomerExcerpt.class, customer);
		assertThat(excerpt.getFullName()).isEqualTo("Dave Matthews");
	}

	@Test // DATACMNS-630
	public void excludesAtValueAnnotatedMethodsForInputProperties() {

		List<PropertyDescriptor> properties = factory //
				.getProjectionInformation(CustomerExcerpt.class) //
				.getInputProperties();

		assertThat(properties) //
				.extracting(PropertyDescriptor::getName) //
				.containsExactly("firstname");
	}

	@Test // DATACMNS-89
	public void considersProjectionUsingAtValueNotClosed() {

		ProjectionInformation information = factory.getProjectionInformation(CustomerExcerpt.class);

		assertThat(information.isClosed()).isFalse();
	}

	@Test // DATACMNS-820
	public void setsValueUsingProjection() {

		Customer customer = new Customer();
		customer.firstname = "Dave";

		CustomerExcerpt excerpt = factory.createProjection(CustomerExcerpt.class, customer);
		excerpt.setFirstname("Carl");

		assertThat(customer.firstname).isEqualTo("Carl");
	}

	@Test // DATACMNS-820
	public void settingNotWriteablePropertyFails() {

		Customer customer = new Customer();
		customer.firstname = "Dave";

		ProjectionWithNotWriteableProperty projection = factory.createProjection(ProjectionWithNotWriteableProperty.class,
				customer);

		exception.expect(NotWritablePropertyException.class);
		projection.setFirstName("Carl");
	}

	static class Customer {

		public String firstname, lastname;
	}

	interface CustomerExcerpt {

		@Value("#{target.firstname + ' ' + target.lastname}")
		String getFullName();

		String getFirstname();

		void setFirstname(String firstname);
	}

	interface ProjectionWithNotWriteableProperty {

		void setFirstName(String firstname);
	}
}
