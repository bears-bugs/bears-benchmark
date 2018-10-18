/*
 * Copyright 2011 the original author or authors.
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
package org.springframework.data.jpa.repository.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Type;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.core.EntityInformation;

/**
 * Unit tests for {@link JpaPersistableEntityInformation}.
 * 
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class JpaPersistableEntityInformationUnitTests {

	@Mock Metamodel metamodel;

	@Mock EntityType<Foo> type;

	@Mock @SuppressWarnings("rawtypes") Type idType;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {

		when(metamodel.managedType(Object.class)).thenThrow(IllegalArgumentException.class);
		when(metamodel.managedType(Foo.class)).thenReturn(type);
		when(type.getIdType()).thenReturn(idType);
	}

	@Test
	public void usesPersistableMethodsForIsNewAndGetId() {

		EntityInformation<Foo, Long> entityInformation = new JpaPersistableEntityInformation<Foo, Long>(Foo.class,
				metamodel);

		Foo foo = new Foo();
		assertThat(entityInformation.isNew(foo), is(false));
		assertThat(entityInformation.getId(foo), is(nullValue()));

		foo.id = 1L;
		assertThat(entityInformation.isNew(foo), is(true));
		assertThat(entityInformation.getId(foo), is(1L));
	}

	@SuppressWarnings("serial")
	class Foo implements Persistable<Long> {

		Long id;

		public Long getId() {

			return id;
		}

		public boolean isNew() {

			return id != null;
		}
	}
}
