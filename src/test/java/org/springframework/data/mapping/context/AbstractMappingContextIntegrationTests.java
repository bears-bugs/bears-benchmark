/*
 * Copyright 2011-2017 by the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mapping.context;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collections;

import org.junit.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

/**
 * Integration tests for {@link AbstractMappingContext}.
 * 
 * @author Oliver Gierke
 */
public class AbstractMappingContextIntegrationTests<T extends PersistentProperty<T>> {

	@Test // DATACMNS-457
	public void returnsManagedType() {

		SampleMappingContext context = new SampleMappingContext();
		context.setInitialEntitySet(Collections.singleton(Person.class));
		context.initialize();

		assertThat(context.getManagedTypes(), hasItem(ClassTypeInformation.from(Person.class)));
	}

	@Test // DATACMNS-457
	public void indicatesManagedType() {

		SampleMappingContext context = new SampleMappingContext();
		context.setInitialEntitySet(Collections.singleton(Person.class));
		context.initialize();

		assertThat(context.hasPersistentEntityFor(Person.class), is(true));
	}

	@Test // DATACMNS-243
	public void createsPersistentEntityForInterfaceCorrectly() {

		SampleMappingContext context = new SampleMappingContext();
		PersistentEntity<Object, SamplePersistentProperty> entity = context.getPersistentEntity(InterfaceOnly.class);

		assertThat(entity.getIdProperty(), is(notNullValue()));
	}

	@Test // DATACMNS-65
	public void foo() throws InterruptedException {

		final DummyMappingContext context = new DummyMappingContext();

		Thread a = new Thread(new Runnable() {
			public void run() {
				context.getPersistentEntity(Person.class);
			}
		});

		Thread b = new Thread(new Runnable() {

			public void run() {

				PersistentEntity<Object, T> entity = context.getPersistentEntity(Person.class);

				entity.doWithProperties(new PropertyHandler<T>() {
					public void doWithPersistentProperty(T persistentProperty) {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
		});

		a.start();
		Thread.sleep(700);
		b.start();

		a.join();
		b.join();
	}

	class DummyMappingContext extends AbstractMappingContext<BasicPersistentEntity<Object, T>, T> {

		@Override
		@SuppressWarnings("unchecked")
		protected <S> BasicPersistentEntity<Object, T> createPersistentEntity(TypeInformation<S> typeInformation) {
			return (BasicPersistentEntity<Object, T>) new BasicPersistentEntity<S, T>(typeInformation);
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected T createPersistentProperty(final Field field, final PropertyDescriptor descriptor,
				final BasicPersistentEntity<Object, T> owner, final SimpleTypeHolder simpleTypeHolder) {

			PersistentProperty prop = mock(PersistentProperty.class);

			when(prop.getTypeInformation()).thenReturn(owner.getTypeInformation());
			when(prop.getName()).thenReturn(field == null ? descriptor.getName() : field.getName());
			when(prop.getPersistentEntityType()).thenReturn(Collections.EMPTY_SET);

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			return (T) prop;
		}
	}

	class Person {

		String firstname;
		String lastname;
		String email;
	}

	interface InterfaceOnly {

		@Id
		String getId();
	}
}
