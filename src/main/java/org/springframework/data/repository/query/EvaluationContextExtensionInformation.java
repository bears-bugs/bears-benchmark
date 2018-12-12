/*
 * Copyright 2014 the original author or authors.
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
package org.springframework.data.repository.query;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.query.EvaluationContextExtensionInformation.ExtensionTypeInformation.PublicMethodAndFieldFilter;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.Function;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

/**
 * Inspects the configured {@link EvaluationContextExtension} type for static methods and fields to avoid repeated
 * reflection lookups. Also inspects the return type of the {@link EvaluationContextExtension#getRootObject()} method
 * and captures the methods declared on it as {@link Function}s.
 * <p>
 * The type basically allows us to cache the type based information within
 * {@link ExtensionAwareEvaluationContextProvider} to avoid repeated reflection lookups for every creation of an
 * {@link org.springframework.expression.EvaluationContext}.
 * 
 * @author Oliver Gierke
 * @since 1.9
 */
class EvaluationContextExtensionInformation {

	private final ExtensionTypeInformation extensionTypeInformation;
	private final RootObjectInformation rootObjectInformation;

	/**
	 * Creates a new {@link EvaluationContextExtension} for the given extension type.
	 * 
	 * @param type must not be {@literal null}.
	 */
	public EvaluationContextExtensionInformation(Class<? extends EvaluationContextExtension> type) {

		Assert.notNull(type, "Extension type must not be null!");
		Class<?> rootObjectType = getRootObjectMethod(type).getReturnType();

		this.rootObjectInformation = Object.class.equals(rootObjectType) ? null : new RootObjectInformation(rootObjectType);
		this.extensionTypeInformation = new ExtensionTypeInformation(type);
	}

	/**
	 * Returns the {@link ExtensionTypeInformation} for the extension.
	 * 
	 * @return
	 */
	public ExtensionTypeInformation getExtensionTypeInformation() {
		return this.extensionTypeInformation;
	}

	/**
	 * Returns the {@link RootObjectInformation} for the given target object. If the information has been pre-computed
	 * earlier, the existing one will be used.
	 * 
	 * @param target
	 * @return
	 */
	public RootObjectInformation getRootObjectInformation(Object target) {
		return target == null ? RootObjectInformation.NONE : rootObjectInformation == null ? new RootObjectInformation(
				target.getClass()) : rootObjectInformation;
	}

	private static Method getRootObjectMethod(Class<?> type) {

		try {
			return type.getMethod("getRootObject");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Static information about the given {@link EvaluationContextExtension} type. Discovers public static methods and
	 * fields. The fields' values are obtained directly, the methods are exposed {@link Function} invocations.
	 *
	 * @author Oliver Gierke
	 */
	public static class ExtensionTypeInformation {

		private final Map<String, Object> properties;
		private final Map<String, Function> functions;

		/**
		 * Creates a new {@link ExtensionTypeInformation} fir the given type.
		 * 
		 * @param type must not be {@literal null}.
		 */
		public ExtensionTypeInformation(Class<? extends EvaluationContextExtension> type) {

			Assert.notNull(type, "Extension type must not be null!");

			this.functions = discoverDeclaredFunctions(type);
			this.properties = discoverDeclaredProperties(type, PublicMethodAndFieldFilter.STATIC);
		}

		/**
		 * Returns the statically defined properties of the extension type.
		 * 
		 * @return the properties will never be {@literal null}.
		 */
		public Map<String, Object> getProperties() {
			return properties;
		}

		/**
		 * Returns the statically exposed functions of the extension type.
		 * 
		 * @return the functions will never be {@literal null}.
		 */
		public Map<String, Function> getFunctions() {
			return functions;
		}

		private static Map<String, Function> discoverDeclaredFunctions(Class<?> type) {

			final Map<String, Function> map = new HashMap<String, Function>();

			ReflectionUtils.doWithMethods(type, new MethodCallback() {

				@Override
				public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
					if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
						map.put(method.getName(), new Function(method, null));
					}
				}
			});

			return map.isEmpty() ? Collections.<String, Function> emptyMap() : Collections.unmodifiableMap(map);
		}

		static class PublicMethodAndFieldFilter implements MethodFilter, FieldFilter {

			public static final PublicMethodAndFieldFilter STATIC = new PublicMethodAndFieldFilter(true);
			public static final PublicMethodAndFieldFilter NON_STATIC = new PublicMethodAndFieldFilter(false);

			private final boolean staticOnly;

			/**
			 * @param staticOnly
			 */
			public PublicMethodAndFieldFilter(boolean forStatic) {
				this.staticOnly = forStatic;
			}

			/* 
			 * (non-Javadoc)
			 * @see org.springframework.util.ReflectionUtils.MethodFilter#matches(java.lang.reflect.Method)
			 */
			@Override
			public boolean matches(Method method) {

				if (ReflectionUtils.isObjectMethod(method)) {
					return false;
				}

				boolean methodStatic = Modifier.isStatic(method.getModifiers());
				boolean staticMatch = staticOnly ? methodStatic : !methodStatic;

				return Modifier.isPublic(method.getModifiers()) && staticMatch;
			}

			/* 
			 * (non-Javadoc)
			 * @see org.springframework.util.ReflectionUtils.FieldFilter#matches(java.lang.reflect.Field)
			 */
			@Override
			public boolean matches(Field field) {

				boolean fieldStatic = Modifier.isStatic(field.getModifiers());
				boolean staticMatch = staticOnly ? fieldStatic : !fieldStatic;

				return Modifier.isPublic(field.getModifiers()) && staticMatch;
			}
		}
	}

	/**
	 * Information about the root object of an extension.
	 *
	 * @author Oliver Gierke
	 */
	static class RootObjectInformation {

		private static final RootObjectInformation NONE = new RootObjectInformation(Object.class);

		private final Map<String, Method> accessors;
		private final Collection<Method> methods;
		private final Collection<Field> fields;

		/**
		 * Creates a new {@link RootObjectInformation} for the given type. Inspects public methods and fields to register
		 * them as {@link Function}s and properties.
		 * 
		 * @param type must not be {@literal null}.
		 */
		public RootObjectInformation(Class<?> type) {

			Assert.notNull(type, "Type must not be null!");

			this.accessors = new HashMap<String, Method>();
			this.methods = new HashSet<Method>();
			this.fields = new ArrayList<Field>();

			if (Object.class.equals(type)) {
				return;
			}

			final PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(type);

			ReflectionUtils.doWithMethods(type, new MethodCallback() {

				@Override
				public void doWith(Method method) {

					RootObjectInformation.this.methods.add(method);

					for (PropertyDescriptor descriptor : descriptors) {
						if (method.equals(descriptor.getReadMethod())) {
							RootObjectInformation.this.accessors.put(descriptor.getName(), method);
						}
					}
				}
			}, PublicMethodAndFieldFilter.NON_STATIC);

			ReflectionUtils.doWithFields(type, new FieldCallback() {

				@Override
				public void doWith(Field field) {
					RootObjectInformation.this.fields.add(field);
				}
			}, PublicMethodAndFieldFilter.NON_STATIC);
		}

		/**
		 * Returns {@link Function} instances that wrap method invocations on the given target object.
		 * 
		 * @param target can be {@literal null}.
		 * @return the methods
		 */
		public Map<String, Function> getFunctions(Object target) {

			if (target == null) {
				return Collections.emptyMap();
			}

			Map<String, Function> functions = new HashMap<String, Function>(methods.size());

			for (Method method : methods) {
				functions.put(method.getName(), new Function(method, target));
			}

			return Collections.unmodifiableMap(functions);

		}

		/**
		 * Returns the properties of the target object. This will also include {@link Function} instances for all properties
		 * with accessor methods that need to be resolved downstream.
		 * 
		 * @return the properties
		 */
		public Map<String, Object> getProperties(Object target) {

			if (target == null) {
				return Collections.emptyMap();
			}

			Map<String, Object> properties = new HashMap<String, Object>();

			for (Entry<String, Method> method : accessors.entrySet()) {
				properties.put(method.getKey(), new Function(method.getValue(), target));
			}

			for (Field field : fields) {
				properties.put(field.getName(), ReflectionUtils.getField(field, target));
			}

			return Collections.unmodifiableMap(properties);
		}
	}

	private static Map<String, Object> discoverDeclaredProperties(Class<?> type, FieldFilter filter) {

		final Map<String, Object> map = new HashMap<String, Object>();

		ReflectionUtils.doWithFields(type, new FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalAccessException {
				map.put(field.getName(), field.get(null));
			}
		}, filter);

		return map.isEmpty() ? Collections.<String, Object> emptyMap() : Collections.unmodifiableMap(map);
	}
}
