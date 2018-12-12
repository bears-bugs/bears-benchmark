/*
 * Copyright 2014-2017 the original author or authors.
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
package org.springframework.data.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;

/**
 * Unit tests for {@link DirectFieldAccessFallbackBeanWrapper}.
 * 
 * @author Oliver Gierke
 */
public class DirectFieldAccessFallbackBeanWrapperUnitTests {

	@Test // DATACMNS-452
	public void usesFieldAccessForReadIfNoAccessorCanBeFound() {

		Sample sample = new Sample();
		sample.firstname = "Dave";

		BeanWrapper wrapper = new DirectFieldAccessFallbackBeanWrapper(sample);

		assertThat(wrapper.getPropertyValue("firstname"), is((Object) "Dave"));
	}

	@Test // DATACMNS-452
	public void usesFieldAccessForWriteIfNoAccessorCanBeFound() {

		Sample sample = new Sample();

		BeanWrapper wrapper = new DirectFieldAccessFallbackBeanWrapper(sample);
		wrapper.setPropertyValue("firstname", "Dave");

		assertThat(sample.firstname, is("Dave"));
	}

	@Test(expected = NotReadablePropertyException.class) // DATACMNS-452
	public void throwsAppropriateExceptionIfNoFieldFoundForRead() {

		BeanWrapper wrapper = new DirectFieldAccessFallbackBeanWrapper(new Sample());
		wrapper.getPropertyValue("lastname");
	}

	@Test(expected = NotWritablePropertyException.class) // DATACMNS-452
	public void throwsAppropriateExceptionIfNoFieldFoundForWrite() {

		BeanWrapper wrapper = new DirectFieldAccessFallbackBeanWrapper(new Sample());
		wrapper.setPropertyValue("lastname", "Matthews");
	}

	static class Sample {

		String firstname;
	}
}
