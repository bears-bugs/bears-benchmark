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
package org.springframework.data.mapping.context;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import groovy.lang.MetaClass;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.StringUtils;

/**
 * Unit test for {@link AbstractMappingContext}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 */
public class AbstractMappingContextUnitTests {

	final SimpleTypeHolder holder = SimpleTypeHolder.DEFAULT;
	SampleMappingContext context;

	@Before
	public void setUp() {
		context = new SampleMappingContext();
		context.setSimpleTypeHolder(holder);
	}

	@Test
	public void doesNotTryToLookupPersistentEntityForLeafProperty() {
		PersistentPropertyPath<SamplePersistentProperty> path = context
				.getPersistentPropertyPath(PropertyPath.from("name", Person.class));
		assertThat(path).isNotNull();
	}

	@Test(expected = MappingException.class) // DATACMNS-92
	public void doesNotAddInvalidEntity() {

		context = new SampleMappingContext() {
			@Override
			@SuppressWarnings("unchecked")
			protected <S> BasicPersistentEntity<Object, SamplePersistentProperty> createPersistentEntity(
					TypeInformation<S> typeInformation) {
				return new BasicPersistentEntity<Object, SamplePersistentProperty>((TypeInformation<Object>) typeInformation) {
					@Override
					public void verify() {
						if (Unsupported.class.isAssignableFrom(getType())) {
							throw new MappingException("Unsupported type!");
						}
					}
				};
			}
		};

		try {
			context.getPersistentEntity(Unsupported.class);
		} catch (MappingException e) {
			// expected
		}

		context.getPersistentEntity(Unsupported.class);
	}

	@Test
	public void registersEntitiesOnInitialization() {

		ApplicationContext context = mock(ApplicationContext.class);

		SampleMappingContext mappingContext = new SampleMappingContext();
		mappingContext.setInitialEntitySet(Collections.singleton(Person.class));
		mappingContext.setApplicationEventPublisher(context);

		verify(context, times(0)).publishEvent(Mockito.any(ApplicationEvent.class));

		mappingContext.afterPropertiesSet();
		verify(context, times(1)).publishEvent(Mockito.any(ApplicationEvent.class));
	}

	@Test // DATACMNS-214
	public void returnsNullPersistentEntityForSimpleTypes() {

		SampleMappingContext context = new SampleMappingContext();
		assertThat(context.getPersistentEntity(String.class)).isNull();
	}

	@Test(expected = IllegalArgumentException.class) // DATACMNS-214
	public void rejectsNullValueForGetPersistentEntityOfClass() {
		context.getPersistentEntity((Class<?>) null);
	}

	@Test(expected = IllegalArgumentException.class) // DATACMNS-214
	public void rejectsNullValueForGetPersistentEntityOfTypeInformation() {
		context.getPersistentEntity((TypeInformation<?>) null);
	}

	@Test // DATACMNS-228
	public void doesNotCreatePersistentPropertyForGroovyMetaClass() {

		SampleMappingContext mappingContext = new SampleMappingContext();
		mappingContext.initialize();

		PersistentEntity<Object, SamplePersistentProperty> entity = mappingContext
				.getRequiredPersistentEntity(Sample.class);
		assertThat(entity.getPersistentProperty("metaClass")).isNull();
	}

	@Test // DATACMNS-332
	public void usesMostConcreteProperty() {

		SampleMappingContext mappingContext = new SampleMappingContext();
		PersistentEntity<Object, SamplePersistentProperty> entity = mappingContext
				.getRequiredPersistentEntity(Extension.class);

		assertThat(entity.getPersistentProperty("foo")).satisfies(it -> assertThat(it.isIdProperty()).isTrue());
	}

	@Test // DATACMNS-345
	@SuppressWarnings("rawtypes")
	public void returnsEntityForComponentType() {

		SampleMappingContext mappingContext = new SampleMappingContext();
		PersistentEntity<Object, SamplePersistentProperty> entity = mappingContext
				.getRequiredPersistentEntity(Sample.class);

		assertThat(entity.getPersistentProperty("persons"))
				.satisfies(it -> assertThat(mappingContext.getPersistentEntity(it))
						.satisfies(inner -> assertThat(((PersistentEntity) inner).getType()).isEqualTo(Person.class)));
	}

	@Test // DATACMNS-380
	public void returnsPersistentPropertyPathForDotPath() {

		PersistentPropertyPath<SamplePersistentProperty> path = context.getPersistentPropertyPath("persons.name",
				Sample.class);

		assertThat(path.getLength()).isEqualTo(2);
		assertThat(path.getBaseProperty().getName()).isEqualTo("persons");
		assertThat(path.getLeafProperty().getName()).isEqualTo("name");
	}

	@Test(expected = MappingException.class) // DATACMNS-380
	public void rejectsInvalidPropertyReferenceWithMappingException() {
		context.getPersistentPropertyPath("foo", Sample.class);
	}

	@Test // DATACMNS-390
	public void exposesCopyOfPersistentEntitiesToAvoidConcurrentModificationException() {

		SampleMappingContext context = new SampleMappingContext();
		context.getPersistentEntity(ClassTypeInformation.MAP);

		Iterator<BasicPersistentEntity<Object, SamplePersistentProperty>> iterator = context.getPersistentEntities()
				.iterator();

		while (iterator.hasNext()) {
			context.getPersistentEntity(ClassTypeInformation.SET);
			iterator.next();
		}
	}

	@Test // DATACMNS-447
	public void shouldReturnNullForSimpleTypesIfInStrictIsEnabled() {

		context.setStrict(true);
		assertThat(context.getPersistentEntity(Integer.class)).isNull();
	}

	@Test // DATACMNS-462
	public void hasPersistentEntityForCollectionPropertiesAfterInitialization() {

		context.getPersistentEntity(Sample.class);
		assertHasEntityFor(Person.class, context, true);
	}

	@Test // DATACMNS-479
	public void doesNotAddMapImplementationClassesAsPersistentEntity() {

		context.getPersistentEntity(Sample.class);
		assertHasEntityFor(TreeMap.class, context, false);
	}

	@Test // DATACMNS-1171
	public void shouldCreateEntityForKotlinDataClass() {
		assertThat(context.getPersistentEntity(SimpleDataClass.class)).isNotNull();
	}

	@Test // DATACMNS-1171
	public void shouldNotCreateEntityForSyntheticKotlinClass() {
		assertThat(context.getPersistentEntity(TypeCreatingSyntheticClass.class)).isNotNull();
	}

	@Test // DATACMNS-695
	public void persistentPropertyPathTraversesGenericTypesCorrectly() {
		assertThat(context.getPersistentPropertyPath("field.wrapped.field", Outer.class)).hasSize(3);
	}

	@Test // DATACMNS-727
	public void exposesContextForFailingPropertyPathLookup() {

		assertThatExceptionOfType(InvalidPersistentPropertyPath.class)//
				.isThrownBy(() -> context.getPersistentPropertyPath("persons.firstname", Sample.class))//
				.matches(e -> StringUtils.hasText(e.getMessage()))//
				.matches(e -> e.getResolvedPath().equals("persons"))//
				.matches(e -> e.getUnresolvableSegment().equals("firstname"))//
				.matches(e -> context.getPersistentPropertyPath(e) != null);
	}

	@Test // DATACMNS-1116
	public void cachesPersistentPropertyPaths() {

		assertThat(context.getPersistentPropertyPath("persons.name", Sample.class)) //
				.isSameAs(context.getPersistentPropertyPath("persons.name", Sample.class));
	}

	private static void assertHasEntityFor(Class<?> type, SampleMappingContext context, boolean expected) {

		boolean found = false;

		for (BasicPersistentEntity<Object, SamplePersistentProperty> entity : context.getPersistentEntities()) {
			if (entity.getType().equals(type)) {
				found = true;
				break;
			}
		}

		if (found != expected) {
			fail(String.format("%s to find persistent entity for %s!", expected ? "Expected" : "Did not expect", type));
		}
	}

	class Person {
		String name;
	}

	class Unsupported {

	}

	class Sample {

		MetaClass metaClass;
		List<Person> persons;
		TreeMap<String, Person> personMap;
	}

	static class Base {
		String foo;
	}

	static class Extension extends Base {
		@Id String foo;
	}

	static class Outer {

		Wrapper<Inner> field;
	}

	static class Wrapper<T> {
		T wrapped;
	}

	static class Inner {
		String field;
	}
}
