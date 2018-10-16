/*
 * Copyright 2013-2014 the original author or authors.
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

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * {@link RepositoryInvoker} to shortcut execution of CRUD methods into direct calls on a {@link CrudRepository}. Used
 * to avoid reflection overhead introduced by the base class if we know we work with a {@link CrudRepository}.
 * 
 * @author Oliver Gierke
 * @since 1.10
 */
class CrudRepositoryInvoker extends ReflectionRepositoryInvoker {

	private final CrudRepository<Object, Serializable> repository;

	private final boolean customSaveMethod;
	private final boolean customFindOneMethod;
	private final boolean customFindAllMethod;
	private final boolean customDeleteMethod;

	/**
	 * Creates a new {@link CrudRepositoryInvoker} for the given {@link CrudRepository}, {@link RepositoryMetadata} and
	 * {@link ConversionService}.
	 * 
	 * @param repository must not be {@literal null}.
	 * @param metadata must not be {@literal null}.
	 * @param conversionService must not be {@literal null}.
	 */
	public CrudRepositoryInvoker(CrudRepository<Object, Serializable> repository, RepositoryMetadata metadata,
			ConversionService conversionService) {

		super(repository, metadata, conversionService);

		CrudMethods crudMethods = metadata.getCrudMethods();

		this.customSaveMethod = isRedeclaredMethod(crudMethods.getSaveMethod());
		this.customFindOneMethod = isRedeclaredMethod(crudMethods.getFindOneMethod());
		this.customDeleteMethod = isRedeclaredMethod(crudMethods.getDeleteMethod());
		this.customFindAllMethod = isRedeclaredMethod(crudMethods.getFindAllMethod());
		this.repository = repository;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.invoke.RepositoryInvoker#invokeFindAll(org.springframework.data.domain.Sort)
	 */
	@Override
	public Iterable<Object> invokeFindAll(Sort sort) {
		return customFindAllMethod ? super.invokeFindAll(sort) : repository.findAll();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.invoke.RepositoryInvoker#invokeFindAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Iterable<Object> invokeFindAll(Pageable pageable) {
		return customFindAllMethod ? super.invokeFindAll(pageable) : repository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.invoke.RepositoryInvoker#invokeFindOne(java.io.Serializable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T invokeFindOne(Serializable id) {
		return customFindOneMethod ? super.<T> invokeFindOne(id) : (T) repository.findOne(convertId(id));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.invoke.ReflectionRepositoryInvoker#invokeSave(java.lang.Object)
	 */
	@Override
	public <T> T invokeSave(T entity) {
		return customSaveMethod ? super.invokeSave(entity) : repository.save(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.rest.core.invoke.RepositoryInvoker#invokeDelete(java.io.Serializable)
	 */
	@Override
	public void invokeDelete(Serializable id) {

		if (customDeleteMethod) {
			super.invokeDelete(id);
		} else {
			repository.delete(convertId(id));
		}
	}

	private boolean isRedeclaredMethod(Method method) {
		return !method.getDeclaringClass().equals(CrudRepository.class);
	}
}
