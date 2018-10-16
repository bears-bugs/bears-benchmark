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
package org.springframework.data.mapping.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.Person;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.ReflectionUtils;

/**
 * Unit tests for {@link AbstractPersistentProperty}.
 * 
 * @author Oliver Gierke
 */
public class AbstractPersistentPropertyUnitTests {

	TypeInformation<TestClassComplex> typeInfo;
	PersistentEntity<TestClassComplex, SamplePersistentProperty> entity;
	SimpleTypeHolder typeHolder;

	@Before
	public void setUp() {

		typeInfo = ClassTypeInformation.from(TestClassComplex.class);
		entity = new BasicPersistentEntity<TestClassComplex, SamplePersistentProperty>(typeInfo);
		typeHolder = new SimpleTypeHolder();
	}

	@Test // DATACMNS-68
	public void discoversComponentTypeCorrectly() throws Exception {

		Field field = ReflectionUtils.findField(TestClassComplex.class, "testClassSet");

		SamplePersistentProperty property = new SamplePersistentProperty(field, null, entity, typeHolder);
		property.getComponentType();
	}

	@Test // DATACMNS-101
	public void returnsNestedEntityTypeCorrectly() {

		Field field = ReflectionUtils.findField(TestClassComplex.class, "testClassSet");

		SamplePersistentProperty property = new SamplePersistentProperty(field, null, entity, typeHolder);
		assertThat(property.getPersistentEntityType().iterator().hasNext(), is(false));
	}

	@Test // DATACMNS-132
	public void isEntityWorksForUntypedMaps() throws Exception {

		Field field = ReflectionUtils.findField(TestClassComplex.class, "map");
		SamplePersistentProperty property = new SamplePersistentProperty(field, null, entity, typeHolder);
		assertThat(property.isEntity(), is(false));
	}

	@Test // DATACMNS-132
	public void isEntityWorksForUntypedCollection() throws Exception {

		Field field = ReflectionUtils.findField(TestClassComplex.class, "collection");
		SamplePersistentProperty property = new SamplePersistentProperty(field, null, entity, typeHolder);
		assertThat(property.isEntity(), is(false));
	}

	@Test // DATACMNS-121
	public void considersPropertiesEqualIfFieldEquals() {

		Field first = ReflectionUtils.findField(FirstConcrete.class, "genericField");
		Field second = ReflectionUtils.findField(SecondConcrete.class, "genericField");

		SamplePersistentProperty firstProperty = new SamplePersistentProperty(first, null, entity, typeHolder);
		SamplePersistentProperty secondProperty = new SamplePersistentProperty(second, null, entity, typeHolder);

		assertThat(firstProperty, is(secondProperty));
		assertThat(firstProperty.hashCode(), is(secondProperty.hashCode()));
	}

	@Test // DATACMNS-180
	public void doesNotConsiderJavaTransientFieldsTransient() {

		Field transientField = ReflectionUtils.findField(TestClassComplex.class, "transientField");

		PersistentProperty<?> property = new SamplePersistentProperty(transientField, null, entity, typeHolder);
		assertThat(property.isTransient(), is(false));
	}

	@Test // DATACMNS-206
	public void findsSimpleGettersAndASetters() {

		Field field = ReflectionUtils.findField(AccessorTestClass.class, "id");
		PersistentProperty<SamplePersistentProperty> property = new SamplePersistentProperty(field, getPropertyDescriptor(
				AccessorTestClass.class, "id"), entity, typeHolder);

		assertThat(property.getGetter(), is(notNullValue()));
		assertThat(property.getSetter(), is(notNullValue()));
	}

	@Test // DATACMNS-206
	public void doesNotUseInvalidGettersAndASetters() {

		Field field = ReflectionUtils.findField(AccessorTestClass.class, "anotherId");
		PersistentProperty<SamplePersistentProperty> property = new SamplePersistentProperty(field, getPropertyDescriptor(
				AccessorTestClass.class, "anotherId"), entity, typeHolder);

		assertThat(property.getGetter(), is(nullValue()));
		assertThat(property.getSetter(), is(nullValue()));
	}

	@Test // DATACMNS-206
	public void usesCustomGetter() {

		Field field = ReflectionUtils.findField(AccessorTestClass.class, "yetAnotherId");
		PersistentProperty<SamplePersistentProperty> property = new SamplePersistentProperty(field, getPropertyDescriptor(
				AccessorTestClass.class, "yetAnotherId"), entity, typeHolder);

		assertThat(property.getGetter(), is(notNullValue()));
		assertThat(property.getSetter(), is(nullValue()));
	}

	@Test // DATACMNS-206
	public void usesCustomSetter() {

		Field field = ReflectionUtils.findField(AccessorTestClass.class, "yetYetAnotherId");
		PersistentProperty<SamplePersistentProperty> property = new SamplePersistentProperty(field, getPropertyDescriptor(
				AccessorTestClass.class, "yetYetAnotherId"), entity, typeHolder);

		assertThat(property.getGetter(), is(nullValue()));
		assertThat(property.getSetter(), is(notNullValue()));
	}

	@Test // DATACMNS-206
	public void returnsNullGetterAndSetterIfNoPropertyDescriptorGiven() {

		Field field = ReflectionUtils.findField(AccessorTestClass.class, "id");
		PersistentProperty<SamplePersistentProperty> property = new SamplePersistentProperty(field, null, entity,
				typeHolder);

		assertThat(property.getGetter(), is(nullValue()));
		assertThat(property.getSetter(), is(nullValue()));
	}

	@Test // DATACMNS-337
	public void resolvesActualType() {

		SamplePersistentProperty property = getProperty(Sample.class, "person");
		assertThat(property.getActualType(), is((Object) Person.class));

		property = getProperty(Sample.class, "persons");
		assertThat(property.getActualType(), is((Object) Person.class));

		property = getProperty(Sample.class, "personArray");
		assertThat(property.getActualType(), is((Object) Person.class));

		property = getProperty(Sample.class, "personMap");
		assertThat(property.getActualType(), is((Object) Person.class));
	}

	@Test // DATACMNS-462
	public void considersCollectionPropertyEntitiesIfComponentTypeIsEntity() {

		SamplePersistentProperty property = getProperty(Sample.class, "persons");
		assertThat(property.isEntity(), is(true));
	}

	@Test // DATACMNS-462
	public void considersMapPropertyEntitiesIfValueTypeIsEntity() {

		SamplePersistentProperty property = getProperty(Sample.class, "personMap");
		assertThat(property.isEntity(), is(true));
	}

	@Test // DATACMNS-462
	public void considersArrayPropertyEntitiesIfComponentTypeIsEntity() {

		SamplePersistentProperty property = getProperty(Sample.class, "personArray");
		assertThat(property.isEntity(), is(true));
	}

	@Test // DATACMNS-462
	public void considersCollectionPropertySimpleIfComponentTypeIsSimple() {

		SamplePersistentProperty property = getProperty(Sample.class, "strings");
		assertThat(property.isEntity(), is(false));
	}

	@Test // DATACMNS-562
	public void doesNotConsiderPropertyWithTreeMapMapValueAnEntity() {

		SamplePersistentProperty property = getProperty(TreeMapWrapper.class, "map");
		assertThat(property.getPersistentEntityType(), is(emptyIterable()));
		assertThat(property.isEntity(), is(false));
	}

	private <T> SamplePersistentProperty getProperty(Class<T> type, String name) {

		BasicPersistentEntity<T, SamplePersistentProperty> entity = new BasicPersistentEntity<T, SamplePersistentProperty>(
				ClassTypeInformation.from(type));

		Field field = ReflectionUtils.findField(type, name);
		return new SamplePersistentProperty(field, null, entity, typeHolder);
	}

	private static PropertyDescriptor getPropertyDescriptor(Class<?> type, String propertyName) {

		try {

			BeanInfo info = Introspector.getBeanInfo(type);

			for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
				if (descriptor.getName().equals(propertyName)) {
					return descriptor;
				}
			}

			return null;

		} catch (IntrospectionException e) {
			return null;
		}
	}

	class Generic<T> {
		T genericField;

	}

	class FirstConcrete extends Generic<String> {

	}

	class SecondConcrete extends Generic<Integer> {

	}

	@SuppressWarnings("serial")
	class TestClassSet extends TreeSet<Object> {}

	@SuppressWarnings("rawtypes")
	class TestClassComplex {

		String id;
		TestClassSet testClassSet;
		Map map;
		Collection collection;
		transient Object transientField;
	}

	class AccessorTestClass {

		// Valid getters and setters
		Long id;
		// Invalid getters and setters
		Long anotherId;

		// Customized getter
		Number yetAnotherId;

		// Customized setter
		Number yetYetAnotherId;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getAnotherId() {
			return anotherId.toString();
		}

		public void setAnotherId(String anotherId) {
			this.anotherId = Long.parseLong(anotherId);
		}

		public Long getYetAnotherId() {
			return null;
		}

		public void setYetYetAnotherId(Object yetYetAnotherId) {
			this.yetYetAnotherId = null;
		}
	}

	class SamplePersistentProperty extends AbstractPersistentProperty<SamplePersistentProperty> {

		public SamplePersistentProperty(Field field, PropertyDescriptor propertyDescriptor,
				PersistentEntity<?, SamplePersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
			super(field, propertyDescriptor, owner, simpleTypeHolder);
		}

		public boolean isIdProperty() {
			return false;
		}

		public boolean isVersionProperty() {
			return false;
		}

		@Override
		public boolean isAssociation() {
			return false;
		}

		@Override
		protected Association<SamplePersistentProperty> createAssociation() {
			return null;
		}

		@Override
		public <A extends Annotation> A findAnnotation(Class<A> annotationType) {
			return null;
		}

		@Override
		public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
			return false;
		}

		@Override
		public <A extends Annotation> A findPropertyOrOwnerAnnotation(Class<A> annotationType) {
			return null;
		}
	}

	static class Sample {

		Person person;
		Collection<Person> persons;
		Person[] personArray;
		Map<String, Person> personMap;
		Collection<String> strings;
	}

	class TreeMapWrapper {
		TreeMap<String, TreeMap<String, String>> map;
	}
}
