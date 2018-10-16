/*
 * Copyright 2011-2015 the original author or authors.
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
package org.springframework.data.repository.core.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/**
 * Base class for {@link RepositoryMetadata} implementations.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
public abstract class AbstractRepositoryMetadata implements RepositoryMetadata {

	private final TypeInformation<?> typeInformation;
	private final Class<?> repositoryInterface;
	private CrudMethods crudMethods;

	/**
	 * Creates a new {@link AbstractRepositoryMetadata}.
	 * 
	 * @param repositoryInterface must not be {@literal null} and must be an interface.
	 */
	public AbstractRepositoryMetadata(Class<?> repositoryInterface) {

		Assert.notNull(repositoryInterface, "Given type must not be null!");
		Assert.isTrue(repositoryInterface.isInterface(), "Given type must be an interface!");

		this.repositoryInterface = repositoryInterface;
		this.typeInformation = ClassTypeInformation.from(repositoryInterface);
	}

	/**
	 * Creates a new {@link RepositoryMetadata} for the given repsository interface.
	 * 
	 * @param repositoryInterface must not be {@literal null}.
	 * @since 1.9
	 * @return
	 */
	public static RepositoryMetadata getMetadata(Class<?> repositoryInterface) {

		Assert.notNull(repositoryInterface, "Repository interface must not be null!");

		return Repository.class.isAssignableFrom(repositoryInterface) ? new DefaultRepositoryMetadata(repositoryInterface)
				: new AnnotationRepositoryMetadata(repositoryInterface);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.RepositoryMetadata#getReturnedDomainClass(java.lang.reflect.Method)
	 */
	public Class<?> getReturnedDomainClass(Method method) {
		return unwrapWrapperTypes(typeInformation.getReturnType(method));
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.RepositoryMetadata#getRepositoryInterface()
	 */
	public Class<?> getRepositoryInterface() {
		return this.repositoryInterface;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.RepositoryMetadata#getCrudMethods()
	 */
	@Override
	public CrudMethods getCrudMethods() {

		if (this.crudMethods == null) {
			this.crudMethods = new DefaultCrudMethods(this);
		}

		return this.crudMethods;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.RepositoryMetadata#isPagingRepository()
	 */
	@Override
	public boolean isPagingRepository() {

		Method findAllMethod = getCrudMethods().getFindAllMethod();

		if (findAllMethod == null) {
			return false;
		}

		return Arrays.asList(findAllMethod.getParameterTypes()).contains(Pageable.class);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.RepositoryMetadata#getAlternativeDomainTypes()
	 */
	@Override
	public Set<Class<?>> getAlternativeDomainTypes() {
		return Collections.emptySet();
	}

	/**
	 * Recursively unwraps well known wrapper types from the given {@link TypeInformation}.
	 * 
	 * @param type must not be {@literal null}.
	 * @return
	 */
	private static Class<?> unwrapWrapperTypes(TypeInformation<?> type) {

		Class<?> rawType = type.getType();

		boolean needToUnwrap = Iterable.class.isAssignableFrom(rawType) || rawType.isArray()
				|| QueryExecutionConverters.supports(rawType) || ReflectionUtils.isJava8StreamType(rawType);

		return needToUnwrap ? unwrapWrapperTypes(type.getComponentType()) : rawType;
	}
}
