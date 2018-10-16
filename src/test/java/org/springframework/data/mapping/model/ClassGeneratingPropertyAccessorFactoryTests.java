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

package org.springframework.data.mapping.model;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.context.SampleMappingContext;
import org.springframework.data.mapping.context.SamplePersistentProperty;
import org.springframework.data.mapping.model.subpackage.TypeInOtherPackage;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit tests for {@link ClassGeneratingPropertyAccessorFactory}
 *
 * @author Mark Paluch
 */
@RunWith(Parameterized.class)
public class ClassGeneratingPropertyAccessorFactoryTests {

	private final ClassGeneratingPropertyAccessorFactory factory = new ClassGeneratingPropertyAccessorFactory();
	private final SampleMappingContext mappingContext = new SampleMappingContext();

	private final Object bean;
	private final String propertyName;
	private final Class<?> expectedConstructorType;

	public ClassGeneratingPropertyAccessorFactoryTests(Object bean, String propertyName, Class<?> expectedConstructorType,
			String displayName) {

		this.bean = bean;
		this.propertyName = propertyName;
		this.expectedConstructorType = expectedConstructorType;
	}

	@Parameters(name = "{3}")
	public static List<Object[]> parameters() {

		List<Object[]> parameters = new ArrayList<Object[]>();
		List<String> propertyNames = Arrays.asList("privateField", "packageDefaultField", "protectedField", "publicField",
				"privateProperty", "packageDefaultProperty", "protectedProperty", "publicProperty", "syntheticProperty");

		parameters.addAll(parameters(new InnerPrivateType(), propertyNames, Object.class));
		parameters.addAll(parameters(new InnerTypeWithPrivateAncestor(), propertyNames, InnerTypeWithPrivateAncestor.class));
		parameters.addAll(parameters(new InnerPackageDefaultType(), propertyNames, InnerPackageDefaultType.class));
		parameters.addAll(parameters(new InnerProtectedType(), propertyNames, InnerProtectedType.class));
		parameters.addAll(parameters(new InnerPublicType(), propertyNames, InnerPublicType.class));
		parameters.addAll(parameters(new ClassGeneratingPropertyAccessorPackageDefaultType(), propertyNames,
				ClassGeneratingPropertyAccessorPackageDefaultType.class));
		parameters.addAll(parameters(new ClassGeneratingPropertyAccessorPublicType(), propertyNames,
				ClassGeneratingPropertyAccessorPublicType.class));
		parameters.addAll(parameters(new SubtypeOfTypeInOtherPackage(), propertyNames, SubtypeOfTypeInOtherPackage.class));

		return parameters;
	}

	private static List<Object[]> parameters(Object bean, List<String> propertyNames, Class<?> expectedConstructorType) {

		List<Object[]> parameters = new ArrayList<Object[]>();

		for (String propertyName : propertyNames) {
			parameters.add(new Object[] { bean, propertyName, expectedConstructorType,
					bean.getClass().getSimpleName() + "/" + propertyName });
		}

		return parameters;
	}

	@Test // DATACMNS-809
	public void shouldSetAndGetProperty() throws Exception {

		PersistentProperty<?> property = getProperty(bean, propertyName);
		PersistentPropertyAccessor persistentPropertyAccessor = getPersistentPropertyAccessor(bean);

		persistentPropertyAccessor.setProperty(property, "value");
		assertThat(persistentPropertyAccessor.getProperty(property), is(equalTo((Object) "value")));
	}

	@Test // DATACMNS-809
	@SuppressWarnings("rawtypes")
	public void accessorShouldDeclareConstructor() throws Exception {

		PersistentPropertyAccessor persistentPropertyAccessor = getPersistentPropertyAccessor(bean);

		Constructor<?>[] declaredConstructors = persistentPropertyAccessor.getClass().getDeclaredConstructors();
		assertThat(declaredConstructors.length, is(1));
		assertThat(declaredConstructors[0].getParameterTypes().length, is(1));
		assertThat(declaredConstructors[0].getParameterTypes()[0], is(equalTo((Class) expectedConstructorType)));
	}

	@Test(expected = IllegalArgumentException.class) // DATACMNS-809
	public void shouldFailOnNullBean() {
		factory.getPropertyAccessor(mappingContext.getPersistentEntity(bean.getClass()), null);
	}

	@Test(expected = UnsupportedOperationException.class) // DATACMNS-809
	public void getPropertyShouldFailOnUnhandledProperty() {

		PersistentProperty<?> property = getProperty(new Dummy(), "dummy");
		PersistentPropertyAccessor persistentPropertyAccessor = getPersistentPropertyAccessor(bean);

		persistentPropertyAccessor.getProperty(property);
	}

	@Test(expected = UnsupportedOperationException.class) // DATACMNS-809
	public void setPropertyShouldFailOnUnhandledProperty() {

		PersistentProperty<?> property = getProperty(new Dummy(), "dummy");
		PersistentPropertyAccessor persistentPropertyAccessor = getPersistentPropertyAccessor(bean);

		persistentPropertyAccessor.setProperty(property, null);
	}

	@Test // DATACMNS-809
	public void shouldUseClassPropertyAccessorFactory() throws Exception {

		BasicPersistentEntity<Object, SamplePersistentProperty> persistentEntity = mappingContext
				.getPersistentEntity(bean.getClass());

		assertThat(ReflectionTestUtils.getField(persistentEntity, "propertyAccessorFactory"),
				is(instanceOf(ClassGeneratingPropertyAccessorFactory.class)));
	}

	private PersistentPropertyAccessor getPersistentPropertyAccessor(Object bean) {
		return factory.getPropertyAccessor(mappingContext.getPersistentEntity(bean.getClass()), bean);
	}

	private PersistentProperty<?> getProperty(Object bean, String name) {

		BasicPersistentEntity<Object, SamplePersistentProperty> persistentEntity = mappingContext
				.getPersistentEntity(bean.getClass());
		return persistentEntity.getPersistentProperty(name);
	}

	// DATACMNS-809
	@SuppressWarnings("unused")
	private static class InnerPrivateType {

		private String privateField;
		String packageDefaultField;
		protected String protectedField;
		public String publicField;
		private String backing;

		@AccessType(Type.PROPERTY) private String privateProperty;

		@AccessType(Type.PROPERTY) private String packageDefaultProperty;

		@AccessType(Type.PROPERTY) private String protectedProperty;

		@AccessType(Type.PROPERTY) private String publicProperty;

		private String getPrivateProperty() {
			return privateProperty;
		}

		private void setPrivateProperty(String privateProperty) {
			this.privateProperty = privateProperty;
		}

		String getPackageDefaultProperty() {
			return packageDefaultProperty;
		}

		void setPackageDefaultProperty(String packageDefaultProperty) {
			this.packageDefaultProperty = packageDefaultProperty;
		}

		protected String getProtectedProperty() {
			return protectedProperty;
		}

		protected void setProtectedProperty(String protectedProperty) {
			this.protectedProperty = protectedProperty;
		}

		public String getPublicProperty() {
			return publicProperty;
		}

		public void setPublicProperty(String publicProperty) {
			this.publicProperty = publicProperty;
		}

		@AccessType(Type.PROPERTY)
		public String getSyntheticProperty() {
			return backing;
		}

		public void setSyntheticProperty(String syntheticProperty) {
			backing = syntheticProperty;
		}
	}

	// DATACMNS-809
	public static class InnerTypeWithPrivateAncestor extends InnerPrivateType {

	}

	// DATACMNS-809
	@SuppressWarnings("unused")
	static class InnerPackageDefaultType {

		private String privateField;
		String packageDefaultField;
		protected String protectedField;
		public String publicField;
		private String backing;

		@AccessType(Type.PROPERTY) private String privateProperty;

		@AccessType(Type.PROPERTY) private String packageDefaultProperty;

		@AccessType(Type.PROPERTY) private String protectedProperty;

		@AccessType(Type.PROPERTY) private String publicProperty;

		private String getPrivateProperty() {
			return privateProperty;
		}

		private void setPrivateProperty(String privateProperty) {
			this.privateProperty = privateProperty;
		}

		String getPackageDefaultProperty() {
			return packageDefaultProperty;
		}

		void setPackageDefaultProperty(String packageDefaultProperty) {
			this.packageDefaultProperty = packageDefaultProperty;
		}

		protected String getProtectedProperty() {
			return protectedProperty;
		}

		protected void setProtectedProperty(String protectedProperty) {
			this.protectedProperty = protectedProperty;
		}

		public String getPublicProperty() {
			return publicProperty;
		}

		public void setPublicProperty(String publicProperty) {
			this.publicProperty = publicProperty;
		}

		@AccessType(Type.PROPERTY)
		public String getSyntheticProperty() {
			return backing;
		}

		public void setSyntheticProperty(String syntheticProperty) {
			backing = syntheticProperty;
		}
	}

	// DATACMNS-809
	@SuppressWarnings("unused")
	protected static class InnerProtectedType {

		private String privateField;
		String packageDefaultField;
		protected String protectedField;
		public String publicField;
		private String backing;

		@AccessType(Type.PROPERTY) private String privateProperty;

		@AccessType(Type.PROPERTY) private String packageDefaultProperty;

		@AccessType(Type.PROPERTY) private String protectedProperty;

		@AccessType(Type.PROPERTY) private String publicProperty;

		private String getPrivateProperty() {
			return privateProperty;
		}

		private void setPrivateProperty(String privateProperty) {
			this.privateProperty = privateProperty;
		}

		String getPackageDefaultProperty() {
			return packageDefaultProperty;
		}

		void setPackageDefaultProperty(String packageDefaultProperty) {
			this.packageDefaultProperty = packageDefaultProperty;
		}

		protected String getProtectedProperty() {
			return protectedProperty;
		}

		protected void setProtectedProperty(String protectedProperty) {
			this.protectedProperty = protectedProperty;
		}

		public String getPublicProperty() {
			return publicProperty;
		}

		public void setPublicProperty(String publicProperty) {
			this.publicProperty = publicProperty;
		}

		@AccessType(Type.PROPERTY)
		public String getSyntheticProperty() {
			return backing;
		}

		public void setSyntheticProperty(String syntheticProperty) {
			backing = syntheticProperty;
		}
	}

	// DATACMNS-809
	@SuppressWarnings("unused")
	public static class InnerPublicType {

		private String privateField;
		String packageDefaultField;
		protected String protectedField;
		public String publicField;
		private String backing;

		@AccessType(Type.PROPERTY) private String privateProperty;

		@AccessType(Type.PROPERTY) private String packageDefaultProperty;

		@AccessType(Type.PROPERTY) private String protectedProperty;

		@AccessType(Type.PROPERTY) private String publicProperty;

		private String getPrivateProperty() {
			return privateProperty;
		}

		private void setPrivateProperty(String privateProperty) {
			this.privateProperty = privateProperty;
		}

		String getPackageDefaultProperty() {
			return packageDefaultProperty;
		}

		void setPackageDefaultProperty(String packageDefaultProperty) {
			this.packageDefaultProperty = packageDefaultProperty;
		}

		protected String getProtectedProperty() {
			return protectedProperty;
		}

		protected void setProtectedProperty(String protectedProperty) {
			this.protectedProperty = protectedProperty;
		}

		public String getPublicProperty() {
			return publicProperty;
		}

		public void setPublicProperty(String publicProperty) {
			this.publicProperty = publicProperty;
		}

		@AccessType(Type.PROPERTY)
		public String getSyntheticProperty() {
			return backing;
		}

		public void setSyntheticProperty(String syntheticProperty) {
			backing = syntheticProperty;
		}
	}

	public static class SubtypeOfTypeInOtherPackage extends TypeInOtherPackage {}

	// DATACMNS-809
	@SuppressWarnings("unused")
	private static class Dummy {

		private String dummy;
		public String publicField;
	}
}
