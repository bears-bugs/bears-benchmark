/*
 * Copyright 2015 the original author or authors.
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
package org.springframework.data.querydsl;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.RepositoryInvoker;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import com.querydsl.core.types.Predicate;

/**
 * {@link RepositoryInvoker} that is aware of a {@link QuerydslPredicateExecutor} and {@link Predicate} to be executed
 * for all flavors of {@code findAll(…)}. All other calls are forwarded to the configured delegate.
 * 
 * @author Oliver Gierke
 */
public class QuerydslRepositoryInvokerAdapter implements RepositoryInvoker {

	private final RepositoryInvoker delegate;
	private final QuerydslPredicateExecutor<Object> executor;
	private final Predicate predicate;

	/**
	 * Creates a new {@link QuerydslRepositoryInvokerAdapter} for the given delegate {@link RepositoryInvoker},
	 * {@link QuerydslPredicateExecutor} and Querydsl {@link Predicate}.
	 * 
	 * @param delegate must not be {@literal null}.
	 * @param executor must not be {@literal null}.
	 * @param predicate can be {@literal null}.
	 */
	public QuerydslRepositoryInvokerAdapter(RepositoryInvoker delegate, QuerydslPredicateExecutor<Object> executor,
			Predicate predicate) {

		Assert.notNull(delegate, "Delegate RepositoryInvoker must not be null!");
		Assert.notNull(executor, "QuerydslPredicateExecutor must not be null!");

		this.delegate = delegate;
		this.executor = executor;
		this.predicate = predicate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvoker#invokePagedFindAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Iterable<Object> invokeFindAll(Pageable pageable) {
		return executor.findAll(predicate, pageable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvoker#invokeSortedFindAll(org.springframework.data.domain.Sort)
	 */
	@Override
	public Iterable<Object> invokeFindAll(Sort sort) {
		return executor.findAll(predicate, sort);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvocationInformation#hasDeleteMethod()
	 */
	@Override
	public boolean hasDeleteMethod() {
		return delegate.hasDeleteMethod();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvocationInformation#hasFindAllMethod()
	 */
	@Override
	public boolean hasFindAllMethod() {
		return delegate.hasFindAllMethod();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvocationInformation#hasFindOneMethod()
	 */
	@Override
	public boolean hasFindOneMethod() {
		return delegate.hasFindOneMethod();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvocationInformation#hasSaveMethod()
	 */
	@Override
	public boolean hasSaveMethod() {
		return delegate.hasSaveMethod();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvoker#invokeDeleteById(java.lang.Object)
	 */
	@Override
	public void invokeDeleteById(Object id) {
		delegate.invokeDeleteById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvoker#invokeFindById(java.lang.Object)
	 */
	@Override
	public <T> Optional<T> invokeFindById(Object id) {
		return delegate.invokeFindById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvoker#invokeQueryMethod(java.lang.reflect.Method, org.springframework.util.MultiValueMap, org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort)
	 */
	@Override
	public Optional<Object> invokeQueryMethod(Method method, MultiValueMap<String, ? extends Object> parameters,
			Pageable pageable, Sort sort) {
		return delegate.invokeQueryMethod(method, parameters, pageable, sort);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryInvoker#invokeSave(java.lang.Object)
	 */
	@Override
	public <T> T invokeSave(T object) {
		return delegate.invokeSave(object);
	}
}
