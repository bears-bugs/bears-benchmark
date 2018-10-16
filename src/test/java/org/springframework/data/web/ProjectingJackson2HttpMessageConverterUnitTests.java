/*
 * Copyright 2016-2017 the original author or authors.
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
package org.springframework.data.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.Test;
import org.springframework.http.MediaType;

/**
 * Unit tests for {@link ProjectingJackson2HttpMessageConverter}.
 * 
 * @author Oliver Gierke
 * @soundtrack Richard Spaven - Tribute (Whole Other*)
 * @since 1.13
 */
public class ProjectingJackson2HttpMessageConverterUnitTests {

	ProjectingJackson2HttpMessageConverter converter = new ProjectingJackson2HttpMessageConverter();
	MediaType ANYTHING_JSON = MediaType.parseMediaType("application/*+json");

	@Test // DATCMNS-885
	public void canReadJsonIntoAnnotatedInterface() {
		assertThat(converter.canRead(SampleInterface.class, ANYTHING_JSON), is(true));
	}

	@Test // DATCMNS-885
	public void cannotReadUnannotatedInterface() {
		assertThat(converter.canRead(UnannotatedInterface.class, ANYTHING_JSON), is(false));
	}

	@Test // DATCMNS-885
	public void cannotReadClass() {
		assertThat(converter.canRead(SampleClass.class, ANYTHING_JSON), is(false));
	}

	@Test // DATACMNS-972
	public void doesNotConsiderTypeVariableBoundTo() throws Throwable {

		Method method = BaseController.class.getDeclaredMethod("createEntity", AbstractDto.class);
		Type type = method.getGenericParameterTypes()[0];

		assertThat(converter.canRead(type, BaseController.class, ANYTHING_JSON), is(false));
	}

	@Test // DATACMNS-972
	public void genericTypeOnConcreteOne() throws Throwable {

		Method method = ConcreteController.class.getMethod("createEntity", AbstractDto.class);
		Type type = method.getGenericParameterTypes()[0];

		assertThat(converter.canRead(type, ConcreteController.class, ANYTHING_JSON), is(false));
	}

	@ProjectedPayload
	interface SampleInterface {}

	interface UnannotatedInterface {}

	class SampleClass {}

	class AbstractDto {}

	abstract class BaseController<D extends AbstractDto> {
		public void createEntity(D dto) {}
	}

	class ConcreteController extends BaseController<AbstractDto> {}
}
