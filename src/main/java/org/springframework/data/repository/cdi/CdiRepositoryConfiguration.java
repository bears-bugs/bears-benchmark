/*
 * Copyright 2014-2018 the original author or authors.
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

package org.springframework.data.repository.cdi;

import java.util.Optional;

import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;

/**
 * Interface containing the configurable options for the Spring Data repository subsystem using CDI.
 *
 * @author Mark Paluch
 * @author Fabian Henniges
 */
public interface CdiRepositoryConfiguration {

	/**
	 * Return the {@link QueryMethodEvaluationContextProvider} to use. Can be {@link Optional#empty()} .
	 *
	 * @return the optional {@link QueryMethodEvaluationContextProvider} base to use, can be {@link Optional#empty()},
	 *         must not be {@literal null}.
	 * @since 2.1
	 */
	default Optional<QueryMethodEvaluationContextProvider> getEvaluationContextProvider() {
		return Optional.empty();
	}

	/**
	 * Return the {@link NamedQueries} to use. Can be {@link Optional#empty()}.
	 *
	 * @return the optional named queries to use, can be {@link Optional#empty()}, must not be {@literal null}.
	 * @since 2.1
	 */
	default Optional<NamedQueries> getNamedQueries() {
		return Optional.empty();
	}

	/**
	 * Return the {@link QueryLookupStrategy.Key} to lookup queries. Can be {@link Optional#empty()}.
	 *
	 * @return the lookup strategy to use, can be {@link Optional#empty()}, must not be {@literal null}.
	 * @since 2.1
	 */
	default Optional<QueryLookupStrategy.Key> getQueryLookupStrategy() {
		return Optional.empty();
	}

	/**
	 * Return the {@link Class repository base class} to use. Can be {@link Optional#empty()} .
	 *
	 * @return the optional repository base to use, can be {@link Optional#empty()}, must not be {@literal null}.
	 * @since 2.1
	 */
	default Optional<Class<?>> getRepositoryBeanClass() {
		return Optional.empty();
	}

	/**
	 * Returns the configured postfix to be used for looking up custom implementation classes.
	 *
	 * @return the postfix to use, must not be {@literal null}.
	 */
	default String getRepositoryImplementationPostfix() {
		return "Impl";
	}
}
