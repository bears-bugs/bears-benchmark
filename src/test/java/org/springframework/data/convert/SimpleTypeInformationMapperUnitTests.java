/*
 * Copyright 2012-2013 the original author or authors.
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
package org.springframework.data.convert;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

/**
 * Unit tests for {@link SimpleTypeInformationMapper}.
 * 
 * @author Oliver Gierke
 */
public class SimpleTypeInformationMapperUnitTests {

	@Test
	@SuppressWarnings({ "rawtypes" })
	public void resolvesTypeByLoadingClass() {

		TypeInformationMapper mapper = new SimpleTypeInformationMapper();
		TypeInformation type = mapper.resolveTypeFrom("java.lang.String");

		TypeInformation expected = ClassTypeInformation.from(String.class);

		assertThat(type, is(expected));
	}

	@Test
	public void returnsNullForNonStringKey() {

		TypeInformationMapper mapper = new SimpleTypeInformationMapper();
		assertThat(mapper.resolveTypeFrom(new Object()), is(nullValue()));
	}

	@Test
	public void returnsNullForEmptyTypeKey() {

		TypeInformationMapper mapper = new SimpleTypeInformationMapper();
		assertThat(mapper.resolveTypeFrom(""), is(nullValue()));
	}

	@Test
	public void returnsNullForUnloadableClass() {

		TypeInformationMapper mapper = new SimpleTypeInformationMapper();
		assertThat(mapper.resolveTypeFrom("Foo"), is(nullValue()));
	}

	@Test
	public void usesFullyQualifiedClassNameAsTypeKey() {

		TypeInformationMapper mapper = new SimpleTypeInformationMapper();
		Object alias = mapper.createAliasFor(ClassTypeInformation.from(String.class));

		assertTrue(alias instanceof String);
		assertThat(alias, is((Object) String.class.getName()));
	}
}
