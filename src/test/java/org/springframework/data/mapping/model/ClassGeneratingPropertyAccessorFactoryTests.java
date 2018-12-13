/*
 * Copyright 2016-2018 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assume.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import org.springframework.util.StringUtils;

/**
 * Unit tests for {@link ClassGeneratingPropertyAccessorFactory}
 *
 * @author Mark Paluch
 */
@RunWith(Parameterized.class)
public class ClassGeneratingPropertyAccessorFactoryTests {

	private final static ClassGeneratingPropertyAccessorFactory factory = new ClassGeneratingPropertyAccessorFactory();
	private final static SampleMappingContext mappingContext = new SampleMappingContext();

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
	@SuppressWarnings("unchecked")
	public static List<Object[]> parameters() throws ReflectiveOperationException {

		List<Object[]> parameters = new ArrayList<>();
		List<String> propertyNames = Arrays.asList("privateField", "packageDefaultField", "protectedField", "publicField",
				"privateProperty", "packageDefaultProperty", "protectedProperty", "publicProperty", "syntheticProperty",
				"immutable", "wither");

		parameters.addAll(parameters(new InnerPrivateType(), propertyNames, Object.class));
		parameters
				.addAll(parameters(new InnerTypeWithPrivateAncestor(), propertyNames, InnerTypeWithPrivateAncestor.class));
		parameters.addAll(parameters(new InnerPackageDefaultType(), propertyNames, InnerPackageDefaultType.class));
		parameters.addAll(parameters(new InnerProtectedType(), propertyNames, InnerProtectedType.class));
		parameters.addAll(parameters(new InnerPublicType(), propertyNames, InnerPublicType.class));
		parameters.addAll(parameters(new ClassGeneratingPropertyAccessorPackageDefaultType(), propertyNames,
				ClassGeneratingPropertyAccessorPackageDefaultType.class));
		parameters.addAll(parameters(new ClassGeneratingPropertyAccessorPublicType(), propertyNames,
				ClassGeneratingPropertyAccessorPublicType.class));
		parameters.addAll(parameters(new SubtypeOfTypeInOtherPackage(), propertyNames, SubtypeOfTypeInOtherPackage.class));

		Class<Object> defaultPackageClass = (Class) Class.forName("TypeInDefaultPackage");

		parameters
				.add(new Object[] { defaultPackageClass.newInstance(), "", defaultPackageClass, "Class in default package" });

		return parameters;
	}

	private static List<Object[]> parameters(Object bean, List<String> propertyNames, Class<?> expectedConstructorType) {

		List<Object[]> parameters = new ArrayList<>();

		for (String propertyName : propertyNames) {
			parameters.add(new Object[] { bean, propertyName, expectedConstructorType,
					bean.getClass().getSimpleName() + "/" + propertyName });
		}

		return parameters;
	}

	@Test // DATACMNS-1201
	public void shouldSupportGeneratedPropertyAccessors() {
		assertThat(factory.isSupported(mappingContext.getRequiredPersistentEntity(bean.getClass()))).isTrue();
	}

	@Test // DATACMNS-809, // DATACMNS-1322
	public void shouldSetAndGetProperty() throws Exception {

		assumeTrue(StringUtils.hasText(propertyName));

		assertThat(getProperty(bean, propertyName)).satisfies(property -> {

			PersistentPropertyAccessor persistentPropertyAccessor = getPersistentPropertyAccessor(bean);
			if (property.isImmutable() && property.getWither() == null) {

				assertThatThrownBy(() -> persistentPropertyAccessor.setProperty(property, "value"))
						.isInstanceOf(UnsupportedOperationException.class);
			} else {

				persistentPropertyAccessor.setProperty(property, "value");
				assertThat(persistentPropertyAccessor.getProperty(property)).isEqualTo("value");
			}
		});
	}

	@Test // DATACMNS-809
	@SuppressWarnings("rawtypes")
	public void accessorShouldDeclareConstructor() throws Exception {

		PersistentPropertyAccessor persistentPropertyAccessor = getPersistentPropertyAccessor(bean);

		Constructor<?>[] declaredConstructors = persistentPropertyAccessor.getClass().getDeclaredConstructors();
		assertThat(declaredConstructors.length).isEqualTo(1);
		assertThat(declaredConstructors[0].getParameterCount()).isEqualTo(1);
		assertThat(declaredConstructors[0].getParameterTypes()[0]).isEqualTo(expectedConstructorType);
	}

	@Test(expected = IllegalArgumentException.class) // DATACMNS-809
	public void shouldFailOnNullBean() {
		factory.getPropertyAccessor(mappingContext.getRequiredPersistentEntity(bean.getClass()), null);
	}

	@Test // DATACMNS-809
	public void getPropertyShouldFailOnUnhandledProperty() {

		assertThat(getProperty(new Dummy(), "dummy"))
				.satisfies(property -> assertThatExceptionOfType(UnsupportedOperationException.class)//
				.isThrownBy(() -> getPersistentPropertyAccessor(bean).getProperty(property)));
	}

	@Test // DATACMNS-809
	public void setPropertyShouldFailOnUnhandledProperty() {

		assertThat(getProperty(new Dummy(), "dummy"))
				.satisfies(property -> assertThatExceptionOfType(UnsupportedOperationException.class)//
				.isThrownBy(() -> getPersistentPropertyAccessor(bean).setProperty(property, Optional.empty())));
	}

	@Test // DATACMNS-809
	public void shouldUseClassPropertyAccessorFactory() throws Exception {

		BasicPersistentEntity<Object, SamplePersistentProperty> persistentEntity = mappingContext
				.getRequiredPersistentEntity(bean.getClass());

		assertThat(ReflectionTestUtils.getField(persistentEntity, "propertyAccessorFactory"))
				.isInstanceOf(ClassGeneratingPropertyAccessorFactory.class);
	}

	private PersistentPropertyAccessor getPersistentPropertyAccessor(Object bean) {
		return factory.getPropertyAccessor(mappingContext.getRequiredPersistentEntity(bean.getClass()), bean);
	}

	private PersistentProperty<?> getProperty(Object bean, String name) {

		BasicPersistentEntity<Object, SamplePersistentProperty> persistentEntity = mappingContext
				.getRequiredPersistentEntity(bean.getClass());
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
		private final String immutable = "";
		private final String wither;

		@AccessType(Type.PROPERTY) private String privateProperty;

		@AccessType(Type.PROPERTY) private String packageDefaultProperty;

		@AccessType(Type.PROPERTY) private String protectedProperty;

		@AccessType(Type.PROPERTY) private String publicProperty;

		private InnerPrivateType() {
			this.wither = "";
		}

		private InnerPrivateType(String wither) {
			this.wither = wither;
		}

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

		public String getWither() {
			return wither;
		}

		public InnerPrivateType withWither(String wither) {
			return new InnerPrivateType(wither);
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
		private final String immutable = "";
		private final String wither;

		@AccessType(Type.PROPERTY) private String privateProperty;

		@AccessType(Type.PROPERTY) private String packageDefaultProperty;

		@AccessType(Type.PROPERTY) private String protectedProperty;

		@AccessType(Type.PROPERTY) private String publicProperty;

		InnerPackageDefaultType() {
			this.wither = "";
		}

		private InnerPackageDefaultType(String wither) {
			this.wither = wither;
		}

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

		public String getWither() {
			return wither;
		}

		public InnerPrivateType withWither(String wither) {
			return new InnerPrivateType(wither);
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
		private final String immutable = "";
		private final String wither;

		@AccessType(Type.PROPERTY) private String privateProperty;

		@AccessType(Type.PROPERTY) private String packageDefaultProperty;

		@AccessType(Type.PROPERTY) private String protectedProperty;

		@AccessType(Type.PROPERTY) private String publicProperty;

		InnerProtectedType() {
			this.wither = "";
		}

		private InnerProtectedType(String wither) {
			this.wither = wither;
		}

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

		public String getWither() {
			return wither;
		}

		public InnerPrivateType withWither(String wither) {
			return new InnerPrivateType(wither);
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
		private final String immutable = "";
		private final String wither;

		@AccessType(Type.PROPERTY) private String privateProperty;

		@AccessType(Type.PROPERTY) private String packageDefaultProperty;

		@AccessType(Type.PROPERTY) private String protectedProperty;

		@AccessType(Type.PROPERTY) private String publicProperty;

		InnerPublicType() {
			this.wither = "";
		}

		private InnerPublicType(String wither) {
			this.wither = wither;
		}

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

		public String getWither() {
			return wither;
		}

		public InnerPrivateType withWither(String wither) {
			return new InnerPrivateType(wither);
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
