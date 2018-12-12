/*
 * Copyright 2008-2016 the original author or authors.
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
package org.springframework.data.repository.support;

import java.util.Collections;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link org.springframework.core.convert.converter.Converter} to convert arbitrary input into domain classes managed
 * by Spring Data {@link CrudRepository}s. The implementation uses a {@link ConversionService} in turn to convert the
 * source type into the domain class' id type which is then converted into a domain class object by using a
 * {@link CrudRepository}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
public class DomainClassConverter<T extends ConversionService & ConverterRegistry>
		implements ConditionalGenericConverter, ApplicationContextAware {

	private final T conversionService;
	private Repositories repositories = Repositories.NONE;
	private ToEntityConverter toEntityConverter;
	private ToIdConverter toIdConverter;

	/**
	 * Creates a new {@link DomainClassConverter} for the given {@link ConversionService}.
	 * 
	 * @param conversionService must not be {@literal null}.
	 */
	public DomainClassConverter(T conversionService) {

		Assert.notNull(conversionService, "ConversionService must not be null!");

		this.conversionService = conversionService;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
	 */
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		return repositories.hasRepositoryFor(targetType.getType())
				? toEntityConverter.convert(source, sourceType, targetType)
				: toIdConverter.convert(source, sourceType, targetType);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.ConditionalConverter#matches(org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {

		return repositories.hasRepositoryFor(targetType.getType()) ? toEntityConverter.matches(sourceType, targetType)
				: toIdConverter.matches(sourceType, targetType);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext context) {

		this.repositories = new Repositories(context);

		this.toEntityConverter = new ToEntityConverter(this.repositories, this.conversionService);
		this.conversionService.addConverter(this.toEntityConverter);

		this.toIdConverter = new ToIdConverter();
		this.conversionService.addConverter(this.toIdConverter);

	}

	/**
	 * Converter to create domain types from any source that can be converted into the domain types identifier type.
	 *
	 * @author Oliver Gierke
	 * @since 1.10
	 */
	private class ToEntityConverter implements ConditionalGenericConverter {

		private final RepositoryInvokerFactory repositoryInvokerFactory;

		/**
		 * Creates a new {@link ToEntityConverter} for the given {@link Repositories} and {@link ConversionService}.
		 * 
		 * @param repositories must not be {@literal null}.
		 * @param conversionService must not be {@literal null}.
		 */
		public ToEntityConverter(Repositories repositories, ConversionService conversionService) {
			this.repositoryInvokerFactory = new DefaultRepositoryInvokerFactory(repositories, conversionService);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
		 */
		@Override
		public Set<ConvertiblePair> getConvertibleTypes() {
			return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
		 */
		@Override
		public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

			if (source == null || !StringUtils.hasText(source.toString())) {
				return null;
			}

			if (sourceType.equals(targetType)) {
				return source;
			}

			Class<?> domainType = targetType.getType();

			RepositoryInformation info = repositories.getRepositoryInformationFor(domainType);
			RepositoryInvoker invoker = repositoryInvokerFactory.getInvokerFor(domainType);

			return invoker.invokeFindOne(conversionService.convert(source, info.getIdType()));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.ConditionalConverter#matches(org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
		 */
		@Override
		public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {

			if (sourceType.isAssignableTo(targetType)) {
				return false;
			}

			if (!repositories.hasRepositoryFor(targetType.getType())) {
				return false;
			}

			Class<?> rawIdType = repositories.getRepositoryInformationFor(targetType.getType()).getIdType();

			return sourceType.equals(TypeDescriptor.valueOf(rawIdType))
					|| conversionService.canConvert(sourceType.getType(), rawIdType);
		}
	}

	/**
	 * Converter to turn domain types into their identifiers or any transitively convertible type.
	 *
	 * @author Oliver Gierke
	 * @since 1.10
	 */
	class ToIdConverter implements ConditionalGenericConverter {

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
		 */
		@Override
		public Set<ConvertiblePair> getConvertibleTypes() {
			return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
		 */
		@Override
		public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

			if (source == null || !StringUtils.hasText(source.toString())) {
				return null;
			}

			if (sourceType.equals(targetType)) {
				return source;
			}

			Class<?> domainType = sourceType.getType();

			EntityInformation<Object, ?> entityInformation = repositories.getEntityInformationFor(domainType);

			return conversionService.convert(entityInformation.getId(source), targetType.getType());
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.ConditionalConverter#matches(org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
		 */
		@Override
		public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {

			if (sourceType.isAssignableTo(targetType)) {
				return false;
			}

			if (!repositories.hasRepositoryFor(sourceType.getType())) {
				return false;
			}

			Class<?> rawIdType = repositories.getRepositoryInformationFor(sourceType.getType()).getIdType();

			return targetType.equals(TypeDescriptor.valueOf(rawIdType))
					|| conversionService.canConvert(rawIdType, targetType.getType());
		}
	}
}
