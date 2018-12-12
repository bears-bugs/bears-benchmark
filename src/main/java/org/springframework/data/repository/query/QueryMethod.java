/*
 * Copyright 2008-2017 the original author or authors.
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

import static org.springframework.data.repository.util.ClassUtils.*;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.EntityMetadata;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/**
 * Abstraction of a method that is designated to execute a finder query. Enriches the standard {@link Method} interface
 * with specific information that is necessary to construct {@link RepositoryQuery}s for the method.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Maciek Opała
 */
public class QueryMethod {

	private final RepositoryMetadata metadata;
	private final Method method;
	private final Class<?> unwrappedReturnType;
	private final Parameters<?, ?> parameters;
	private final ResultProcessor resultProcessor;

	private Class<?> domainClass;

	/**
	 * Creates a new {@link QueryMethod} from the given parameters. Looks up the correct query to use for following
	 * invocations of the method given.
	 * 
	 * @param method must not be {@literal null}.
	 * @param metadata must not be {@literal null}.
	 * @param factory must not be {@literal null}.
	 */
	public QueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {

		Assert.notNull(method, "Method must not be null!");
		Assert.notNull(metadata, "Repository metadata must not be null!");
		Assert.notNull(factory, "ProjectionFactory must not be null!");

		for (Class<?> type : Parameters.TYPES) {
			if (getNumberOfOccurences(method, type) > 1) {
				throw new IllegalStateException(String.format("Method must only one argument of type %s! Offending method: %s",
						type.getSimpleName(), method.toString()));
			}
		}

		this.method = method;
		this.unwrappedReturnType = potentiallyUnwrapReturnTypeFor(method);
		this.parameters = createParameters(method);
		this.metadata = metadata;

		if (hasParameterOfType(method, Pageable.class)) {

			if (!isStreamQuery()) {
				assertReturnTypeAssignable(method, QueryExecutionConverters.getAllowedPageableTypes());
			}

			if (hasParameterOfType(method, Sort.class)) {
				throw new IllegalStateException(String.format("Method must not have Pageable *and* Sort parameter. "
						+ "Use sorting capabilities on Pageble instead! Offending method: %s", method.toString()));
			}
		}

		Assert.notNull(this.parameters,
				String.format("Parameters extracted from method '%s' must not be null!", method.getName()));

		if (isPageQuery()) {
			Assert.isTrue(this.parameters.hasPageableParameter(),
					String.format("Paging query needs to have a Pageable parameter! Offending method %s", method.toString()));
		}

		this.resultProcessor = new ResultProcessor(this, factory);
	}

	/**
	 * Creates a {@link Parameters} instance.
	 * 
	 * @param method
	 * @return must not return {@literal null}.
	 */
	protected Parameters<?, ?> createParameters(Method method) {
		return new DefaultParameters(method);
	}

	/**
	 * Returns the method's name.
	 * 
	 * @return
	 */
	public String getName() {

		return method.getName();
	}

	@SuppressWarnings("rawtypes")
	public EntityMetadata<?> getEntityInformation() {

		return new EntityMetadata() {

			public Class<?> getJavaType() {

				return getDomainClass();
			}
		};
	}

	/**
	 * Returns the name of the named query this method belongs to.
	 * 
	 * @return
	 */
	public String getNamedQueryName() {
		return String.format("%s.%s", getDomainClass().getSimpleName(), method.getName());
	}

	/**
	 * Returns the domain class the query method is targeted at.
	 * 
	 * @return will never be {@literal null}.
	 */
	protected Class<?> getDomainClass() {

		if (domainClass == null) {

			Class<?> repositoryDomainClass = metadata.getDomainType();
			Class<?> methodDomainClass = metadata.getReturnedDomainClass(method);

			this.domainClass = repositoryDomainClass == null || repositoryDomainClass.isAssignableFrom(methodDomainClass)
					? methodDomainClass : repositoryDomainClass;
		}

		return domainClass;
	}

	/**
	 * Returns the type of the object that will be returned.
	 * 
	 * @return
	 */
	public Class<?> getReturnedObjectType() {
		return metadata.getReturnedDomainClass(method);
	}

	/**
	 * Returns whether the finder will actually return a collection of entities or a single one.
	 * 
	 * @return
	 */
	public boolean isCollectionQuery() {

		if (isPageQuery() || isSliceQuery()) {
			return false;
		}

		Class<?> returnType = method.getReturnType();

		if (QueryExecutionConverters.supports(returnType) && !QueryExecutionConverters.isSingleValue(returnType)) {
			return true;
		}

		if (QueryExecutionConverters.supports(unwrappedReturnType)
				&& QueryExecutionConverters.isSingleValue(unwrappedReturnType)) {
			return false;
		}

		return org.springframework.util.ClassUtils.isAssignable(Iterable.class, unwrappedReturnType)
				|| unwrappedReturnType.isArray();
	}

	/**
	 * Returns whether the query method will return a {@link Slice}.
	 * 
	 * @return
	 * @since 1.8
	 */
	public boolean isSliceQuery() {
		return !isPageQuery() && org.springframework.util.ClassUtils.isAssignable(Slice.class, unwrappedReturnType);
	}

	/**
	 * Returns whether the finder will return a {@link Page} of results.
	 * 
	 * @return
	 */
	public final boolean isPageQuery() {
		return org.springframework.util.ClassUtils.isAssignable(Page.class, unwrappedReturnType);
	}

	/**
	 * Returns whether the query method is a modifying one.
	 * 
	 * @return
	 */
	public boolean isModifyingQuery() {
		return false;
	}

	/**
	 * Returns whether the query for theis method actually returns entities.
	 * 
	 * @return
	 */
	public boolean isQueryForEntity() {
		return getDomainClass().isAssignableFrom(getReturnedObjectType());
	}

	/**
	 * Returns whether the method returns a Stream.
	 * 
	 * @return
	 * @since 1.10
	 */
	public boolean isStreamQuery() {
		return ReflectionUtils.isJava8StreamType(unwrappedReturnType);
	}

	/**
	 * Returns the {@link Parameters} wrapper to gain additional information about {@link Method} parameters.
	 * 
	 * @return
	 */
	public Parameters<?, ?> getParameters() {
		return parameters;
	}

	/**
	 * Returns the {@link ResultProcessor} to be usedwith the query method.
	 * 
	 * @return the resultFactory
	 */
	public ResultProcessor getResultProcessor() {
		return resultProcessor;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return method.toString();
	}

	private static Class<? extends Object> potentiallyUnwrapReturnTypeFor(Method method) {

		if (QueryExecutionConverters.supports(method.getReturnType())) {
			// unwrap only one level to handle cases like Future<List<Entity>> correctly.
			return ClassTypeInformation.fromReturnTypeOf(method).getComponentType().getType();
		}

		return method.getReturnType();
	}

	private static void assertReturnTypeAssignable(Method method, Set<Class<?>> types) {

		Assert.notNull(method, "Method must not be null!");
		Assert.notEmpty(types, "Types must not be null or empty!");

		TypeInformation<?> returnType = ClassTypeInformation.fromReturnTypeOf(method);
		returnType = QueryExecutionConverters.isSingleValue(returnType.getType()) ? returnType.getComponentType()
				: returnType;

		for (Class<?> type : types) {
			if (type.isAssignableFrom(returnType.getType())) {
				return;
			}
		}

		throw new IllegalStateException("Method has to have one of the following return types! " + types.toString());
	}
}
