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
package org.springframework.data.repository.util;

import javaslang.collection.Seq;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import scala.Function0;
import scala.Option;
import scala.runtime.AbstractFunction0;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.concurrent.ListenableFuture;

import com.google.common.base.Optional;

/**
 * Converters to potentially wrap the execution of a repository method into a variety of wrapper types potentially being
 * available on the classpath. Currently supported:
 * <ul>
 * <li>{@code java.util.Optional}</li>
 * <li>{@code com.google.common.base.Optional}</li>
 * <li>{@code scala.Option} - as of 1.12</li>
 * <li>{@code java.util.concurrent.Future}</li>
 * <li>{@code java.util.concurrent.CompletableFuture}</li>
 * <li>{@code org.springframework.util.concurrent.ListenableFuture<}</li>
 * <li>{@code javaslang.control.Option} - as of 1.13</li>
 * <li>{@code javaslang.collection.Seq}, {@code javaslang.collection.Map}, {@code javaslang.collection.Set} - as of
 * 1.13</li>
 * <li>{@code io.vavr.collection.Seq}, {@code io.vavr.collection.Map}, {@code io.vavr.collection.Set} - as of 2.0</li>
 * </ul>
 * 
 * @author Oliver Gierke
 * @author Mark Paluch
 * @author Maciek Opała
 * @author Jacek Jackowiak
 * @since 1.8
 */
public abstract class QueryExecutionConverters {

	private static final boolean GUAVA_PRESENT = ClassUtils.isPresent("com.google.common.base.Optional",
			QueryExecutionConverters.class.getClassLoader());
	private static final boolean JDK_8_PRESENT = ClassUtils.isPresent("java.util.Optional",
			QueryExecutionConverters.class.getClassLoader());
	private static final boolean SCALA_PRESENT = ClassUtils.isPresent("scala.Option",
			QueryExecutionConverters.class.getClassLoader());
	private static final boolean JAVASLANG_PRESENT = ClassUtils.isPresent("javaslang.control.Option",
			QueryExecutionConverters.class.getClassLoader());
	private static final boolean VAVR_PRESENT = ClassUtils.isPresent("io.vavr.control.Option",
			QueryExecutionConverters.class.getClassLoader());

	private static final Set<WrapperType> WRAPPER_TYPES = new HashSet<WrapperType>();
	private static final Set<Converter<Object, Object>> UNWRAPPERS = new HashSet<Converter<Object, Object>>();
	private static final Set<Class<?>> ALLOWED_PAGEABLE_TYPES = new HashSet<Class<?>>();

	static {

		WRAPPER_TYPES.add(WrapperType.singleValue(Future.class));
		WRAPPER_TYPES.add(WrapperType.singleValue(ListenableFuture.class));

		ALLOWED_PAGEABLE_TYPES.add(Slice.class);
		ALLOWED_PAGEABLE_TYPES.add(Page.class);
		ALLOWED_PAGEABLE_TYPES.add(List.class);

		if (GUAVA_PRESENT) {
			WRAPPER_TYPES.add(NullableWrapperToGuavaOptionalConverter.getWrapperType());
			UNWRAPPERS.add(GuavaOptionalUnwrapper.INSTANCE);
		}

		if (JDK_8_PRESENT) {
			WRAPPER_TYPES.add(NullableWrapperToJdk8OptionalConverter.getWrapperType());
			UNWRAPPERS.add(Jdk8OptionalUnwrapper.INSTANCE);
		}

		if (JDK_8_PRESENT) {
			WRAPPER_TYPES.add(NullableWrapperToCompletableFutureConverter.getWrapperType());
		}

		if (SCALA_PRESENT) {
			WRAPPER_TYPES.add(NullableWrapperToScalaOptionConverter.getWrapperType());
			UNWRAPPERS.add(ScalOptionUnwrapper.INSTANCE);
		}

		if (JAVASLANG_PRESENT) {

			WRAPPER_TYPES.add(NullableWrapperToJavaslangOptionConverter.getWrapperType());
			WRAPPER_TYPES.add(JavaslangCollections.ToJavaConverter.INSTANCE.getWrapperType());

			UNWRAPPERS.add(JavaslangOptionUnwrapper.INSTANCE);

			ALLOWED_PAGEABLE_TYPES.add(Seq.class);
		}

		if (VAVR_PRESENT) {

			WRAPPER_TYPES.add(NullableWrapperToVavrOptionConverter.getWrapperType());
			WRAPPER_TYPES.add(VavrCollections.ToJavaConverter.INSTANCE.getWrapperType());

			UNWRAPPERS.add(VavrOptionUnwrapper.INSTANCE);

			ALLOWED_PAGEABLE_TYPES.add(io.vavr.collection.Seq.class);
		}
	}

	private QueryExecutionConverters() {}

	/**
	 * Returns whether the given type is a supported wrapper type.
	 * 
	 * @param type must not be {@literal null}.
	 * @return
	 */
	public static boolean supports(Class<?> type) {

		Assert.notNull(type, "Type must not be null!");

		for (WrapperType candidate : WRAPPER_TYPES) {
			if (candidate.getType().isAssignableFrom(type)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isSingleValue(Class<?> type) {

		for (WrapperType candidate : WRAPPER_TYPES) {
			if (candidate.getType().isAssignableFrom(type)) {
				return candidate.isSingleValue();
			}
		}

		return false;
	}

	/**
	 * Returns the types that are supported on paginating query methods. Will include custom collection types of e.g.
	 * Javaslang.
	 * 
	 * @return
	 */
	public static Set<Class<?>> getAllowedPageableTypes() {
		return Collections.unmodifiableSet(ALLOWED_PAGEABLE_TYPES);
	}

	/**
	 * Registers converters for wrapper types found on the classpath.
	 * 
	 * @param conversionService must not be {@literal null}.
	 */
	public static void registerConvertersIn(ConfigurableConversionService conversionService) {

		Assert.notNull(conversionService, "ConversionService must not be null!");

		conversionService.removeConvertible(Collection.class, Object.class);

		if (GUAVA_PRESENT) {
			conversionService.addConverter(new NullableWrapperToGuavaOptionalConverter(conversionService));
		}

		if (JDK_8_PRESENT) {
			conversionService.addConverter(new NullableWrapperToJdk8OptionalConverter(conversionService));
			conversionService.addConverter(new NullableWrapperToCompletableFutureConverter(conversionService));
		}

		if (SCALA_PRESENT) {
			conversionService.addConverter(new NullableWrapperToScalaOptionConverter(conversionService));
		}

		if (JAVASLANG_PRESENT) {
			conversionService.addConverter(new NullableWrapperToJavaslangOptionConverter(conversionService));
			conversionService.addConverter(JavaslangCollections.FromJavaConverter.INSTANCE);
		}

		if (VAVR_PRESENT) {
			conversionService.addConverter(new NullableWrapperToVavrOptionConverter(conversionService));
			conversionService.addConverter(VavrCollections.FromJavaConverter.INSTANCE);
		}

		conversionService.addConverter(new NullableWrapperToFutureConverter(conversionService));
	}

	/**
	 * Unwraps the given source value in case it's one of the currently supported wrapper types detected at runtime.
	 * 
	 * @param source can be {@literal null}.
	 * @return
	 */
	public static Object unwrap(Object source) {

		if (source == null || !supports(source.getClass())) {
			return source;
		}

		for (Converter<Object, Object> converter : UNWRAPPERS) {

			Object result = converter.convert(source);

			if (result != source) {
				return result;
			}
		}

		return source;
	}

	/**
	 * Base class for converters that create instances of wrapper types such as Google Guava's and JDK 8's
	 * {@code Optional} types.
	 *
	 * @author Oliver Gierke
	 */
	private static abstract class AbstractWrapperTypeConverter implements GenericConverter {

		@SuppressWarnings("unused") //
		private final ConversionService conversionService;
		private final Class<?>[] wrapperTypes;
		private final Object nullValue;

		/**
		 * Creates a new {@link AbstractWrapperTypeConverter} using the given {@link ConversionService} and wrapper type.
		 * 
		 * @param conversionService must not be {@literal null}.
		 * @param wrapperTypes must not be {@literal null}.
		 */
		protected AbstractWrapperTypeConverter(ConversionService conversionService, Object nullValue,
				Class<?>... wrapperTypes) {

			Assert.notNull(conversionService, "ConversionService must not be null!");
			Assert.notEmpty(wrapperTypes, "Wrapper type must not be empty!");

			this.conversionService = conversionService;
			this.wrapperTypes = wrapperTypes;
			this.nullValue = nullValue;
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
		 */
		@Override
		public Set<ConvertiblePair> getConvertibleTypes() {

			Set<ConvertiblePair> pairs = new HashSet<ConvertiblePair>(wrapperTypes.length);

			for (Class<?> wrapperType : wrapperTypes) {
				pairs.add(new ConvertiblePair(NullableWrapper.class, wrapperType));
			}

			return Collections.unmodifiableSet(pairs);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
		 */
		@Override
		public final Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

			NullableWrapper wrapper = (NullableWrapper) source;
			Object value = wrapper.getValue();

			// TODO: Add Recursive conversion once we move to Spring 4
			return value == null ? nullValue : wrap(value);
		}

		/**
		 * Wrap the given, non-{@literal null} value into the wrapper type.
		 * 
		 * @param source will never be {@literal null}.
		 * @return must not be {@literal null}.
		 */
		protected abstract Object wrap(Object source);
	}

	/**
	 * A Spring {@link Converter} to support Google Guava's {@link Optional}.
	 * 
	 * @author Oliver Gierke
	 */
	private static class NullableWrapperToGuavaOptionalConverter extends AbstractWrapperTypeConverter {

		/**
		 * Creates a new {@link NullableWrapperToGuavaOptionalConverter} using the given {@link ConversionService}.
		 * 
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToGuavaOptionalConverter(ConversionService conversionService) {
			super(conversionService, Optional.absent(), Optional.class);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return Optional.of(source);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(Optional.class);
		}
	}

	/**
	 * A Spring {@link Converter} to support JDK 8's {@link java.util.Optional}.
	 * 
	 * @author Oliver Gierke
	 */
	private static class NullableWrapperToJdk8OptionalConverter extends AbstractWrapperTypeConverter {

		/**
		 * Creates a new {@link NullableWrapperToJdk8OptionalConverter} using the given {@link ConversionService}.
		 * 
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToJdk8OptionalConverter(ConversionService conversionService) {
			super(conversionService, java.util.Optional.empty(), java.util.Optional.class);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return java.util.Optional.of(source);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(java.util.Optional.class);
		}
	}

	/**
	 * A Spring {@link Converter} to support returning {@link Future} instances from repository methods.
	 * 
	 * @author Oliver Gierke
	 */
	private static class NullableWrapperToFutureConverter extends AbstractWrapperTypeConverter {

		/**
		 * Creates a new {@link NullableWrapperToFutureConverter} using the given {@link ConversionService}.
		 * 
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToFutureConverter(ConversionService conversionService) {
			super(conversionService, new AsyncResult<Object>(null), Future.class, ListenableFuture.class);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return new AsyncResult<Object>(source);
		}
	}

	/**
	 * A Spring {@link Converter} to support returning {@link CompletableFuture} instances from repository methods.
	 * 
	 * @author Oliver Gierke
	 */
	private static class NullableWrapperToCompletableFutureConverter extends AbstractWrapperTypeConverter {

		/**
		 * Creates a new {@link NullableWrapperToCompletableFutureConverter} using the given {@link ConversionService}.
		 * 
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToCompletableFutureConverter(ConversionService conversionService) {
			super(conversionService, CompletableFuture.completedFuture(null), CompletableFuture.class);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return source instanceof CompletableFuture ? source : CompletableFuture.completedFuture(source);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(CompletableFuture.class);
		}
	}

	/**
	 * A Spring {@link Converter} to support Scala's {@link Option}.
	 *
	 * @author Oliver Gierke
	 * @since 1.13
	 */
	private static class NullableWrapperToScalaOptionConverter extends AbstractWrapperTypeConverter {

		public NullableWrapperToScalaOptionConverter(ConversionService conversionService) {
			super(conversionService, Option.empty(), Option.class);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return Option.apply(source);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(Option.class);
		}
	}

	/**
	 * Converter to convert from {@link NullableWrapper} into JavaSlang's {@link javaslang.control.Option}.
	 *
	 * @author Oliver Gierke
	 * @since 1.13
	 */
	private static class NullableWrapperToJavaslangOptionConverter extends AbstractWrapperTypeConverter {

		private static final Method OF_METHOD;
		private static final Method NONE_METHOD;

		static {
			OF_METHOD = ReflectionUtils.findMethod(javaslang.control.Option.class, "of", Object.class);
			NONE_METHOD = ReflectionUtils.findMethod(javaslang.control.Option.class, "none");
		}

		/**
		 * Creates a new {@link NullableWrapperToJavaslangOptionConverter} using the given {@link ConversionService}.
		 * 
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToJavaslangOptionConverter(ConversionService conversionService) {
			super(conversionService, createEmptyOption(), javaslang.control.Option.class);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(javaslang.control.Option.class);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return ReflectionUtils.invokeMethod(OF_METHOD, null, source);
		}

		@SuppressWarnings("unchecked")
		private static javaslang.control.Option<Object> createEmptyOption() {
			return (javaslang.control.Option<Object>) ReflectionUtils.invokeMethod(NONE_METHOD, null);
		}
	}

	/**
	 * Converter to convert from {@link NullableWrapper} into Vavr's {@link io.vavr.control.Option}.
	 *
	 * @author Oliver Gierke
	 * @since 2.0
	 */
	private static class NullableWrapperToVavrOptionConverter extends AbstractWrapperTypeConverter {

		private static final Method OF_METHOD;
		private static final Method NONE_METHOD;

		static {
			OF_METHOD = ReflectionUtils.findMethod(io.vavr.control.Option.class, "of", Object.class);
			NONE_METHOD = ReflectionUtils.findMethod(io.vavr.control.Option.class, "none");
		}

		/**
		 * Creates a new {@link NullableWrapperToVavrOptionConverter} using the given {@link ConversionService}.
		 * 
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToVavrOptionConverter(ConversionService conversionService) {
			super(conversionService, createEmptyOption(), io.vavr.control.Option.class);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return ReflectionUtils.invokeMethod(OF_METHOD, null, source);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(io.vavr.control.Option.class);
		}

		@SuppressWarnings("unchecked")
		private static io.vavr.control.Option<Object> createEmptyOption() {
			return (io.vavr.control.Option<Object>) ReflectionUtils.invokeMethod(NONE_METHOD, null);
		}
	}

	/**
	 * A {@link Converter} to unwrap Guava {@link Optional} instances.
	 *
	 * @author Oliver Gierke
	 * @since 1.12
	 */
	private static enum GuavaOptionalUnwrapper implements Converter<Object, Object> {

		INSTANCE;

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		@Override
		public Object convert(Object source) {
			return source instanceof Optional ? ((Optional<?>) source).orNull() : source;
		}
	}

	/**
	 * A {@link Converter} to unwrap JDK 8 {@link java.util.Optional} instances.
	 *
	 * @author Oliver Gierke
	 * @since 1.12
	 */
	private static enum Jdk8OptionalUnwrapper implements Converter<Object, Object> {

		INSTANCE;

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		@Override
		public Object convert(Object source) {
			return source instanceof java.util.Optional ? ((java.util.Optional<?>) source).orElse(null) : source;
		}
	}

	/**
	 * A {@link Converter} to unwrap a Scala {@link Option} instance.
	 *
	 * @author Oliver Gierke
	 * @author Mark Paluch
	 * @since 1.12
	 */
	private static enum ScalOptionUnwrapper implements Converter<Object, Object> {

		INSTANCE;

		private final Function0<Object> alternative = new AbstractFunction0<Object>() {

			/*
			 * (non-Javadoc)
			 * @see scala.Function0#apply()
			 */
			@Override
			public Option<Object> apply() {
				return null;
			}
		};

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		@Override
		public Object convert(Object source) {
			return source instanceof Option ? ((Option<?>) source).getOrElse(alternative) : source;
		}
	}

	/**
	 * Converter to unwrap Javaslang {@link javaslang.control.Option} instances.
	 *
	 * @author Oliver Gierke
	 * @since 1.13
	 */
	private static enum JavaslangOptionUnwrapper implements Converter<Object, Object> {

		INSTANCE;

		private static final Supplier<Object> NULL_SUPPLIER = new Supplier<Object>() {

			/*
			 * (non-Javadoc)
			 * @see java.util.function.Supplier#get()
			 */
			public Object get() {
				return null;
			}
		};

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public Object convert(Object source) {

			if (source instanceof javaslang.control.Option) {
				return ((javaslang.control.Option<Object>) source).getOrElse(NULL_SUPPLIER);
			}

			if (source instanceof javaslang.collection.Traversable) {
				return JavaslangCollections.ToJavaConverter.INSTANCE.convert(source);
			}

			return source;
		}
	}

	/**
	 * Converter to unwrap Vavr {@link io.vavr.control.Option} instances.
	 *
	 * @author Oliver Gierke
	 * @since 2.0
	 */
	private static enum VavrOptionUnwrapper implements Converter<Object, Object> {

		INSTANCE;

		private static final Supplier<Object> NULL_SUPPLIER = new Supplier<Object>() {

			/*
			 * (non-Javadoc)
			 * @see java.util.function.Supplier#get()
			 */
			public Object get() {
				return null;
			}
		};

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public Object convert(Object source) {

			if (source instanceof io.vavr.control.Option) {
				return ((io.vavr.control.Option<Object>) source).getOrElse(NULL_SUPPLIER);
			}

			if (source instanceof io.vavr.collection.Traversable) {
				return VavrCollections.ToJavaConverter.INSTANCE.convert(source);
			}

			return source;
		}
	}

	@Value
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class WrapperType {

		Class<?> type;
		boolean singleValue;

		public static WrapperType singleValue(Class<?> type) {
			return new WrapperType(type, true);
		}

		public static WrapperType multiValue(Class<?> type) {
			return new WrapperType(type, false);
		}
	}
}
